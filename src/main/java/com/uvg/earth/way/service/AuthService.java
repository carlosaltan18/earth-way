package com.uvg.earth.way.service;

import java.util.ArrayList;
import java.util.List;

import com.uvg.earth.way.dto.LoginUserDto;
import com.uvg.earth.way.dto.RegisterUserDto;
import com.uvg.earth.way.model.Role;
import com.uvg.earth.way.model.User;
import com.uvg.earth.way.repository.UserRepository;
import com.uvg.earth.way.service.interfaces.IAuthServices;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class AuthService implements IAuthServices {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    public User register(RegisterUserDto registerUserDto) {
        User user = new User();
        user.setName(registerUserDto.getName());
        user.setSurname(registerUserDto.getSurname());
        user.setEmail(registerUserDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerUserDto.getPassword()));
        user.setPhone(registerUserDto.getPhone());
        List<Role> roles = new ArrayList<>();
        roles.add(roleService.findRoleById(Long.valueOf(2)));
        user.setRoles(roles);

        return userRepository.save(user);
    }

    @Override
    public User login(LoginUserDto loginUserDto) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUserDto.getEmail(),
                        loginUserDto.getPassword()
                )
        );

        return userRepository.findUserByEmail(loginUserDto.getEmail())
                .orElseThrow();

    }

}