package com.uvg.earth.way.service;

import java.util.Optional;

import com.uvg.earth.way.dto.UserDto;
import com.uvg.earth.way.exception.UserDeletionException;
import com.uvg.earth.way.exception.UserNotFoundException;
import com.uvg.earth.way.model.User;
import com.uvg.earth.way.repository.UserRepository;
import com.uvg.earth.way.service.interfaces.IUserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;


@Service
@AllArgsConstructor
public class UserService implements IUserService {

    private UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final String USER_WITH = "User with id ";
    private static final String DONT_EXIST = "Don't exist";

    @Override
    public User findUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public Page<User> getAllUsers(int page, int size, String email) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("email")));

        if (email != null && !email.isEmpty()) {
            return userRepository.findByEmailContainingIgnoreCase(email, pageable);
        }
        Page<User> users = userRepository.findAll(pageable);

        return users;
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void deleteUser(Long idUser) {
        if (!userRepository.existsById(idUser)) {
            throw new IllegalArgumentException(USER_WITH + idUser + DONT_EXIST);
        }
        try {
            userRepository.deleteById(idUser);
        } catch (DataAccessException e) {
            throw new UserDeletionException("Error deleting user with ID " + idUser, e);
        }
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email).orElse(null);
    }

    @Override
    public void updateUser(UserDto userDto, Long idUser) {
        validateUserDto(idUser);
        Optional<User> optionalUser = userRepository.findById(idUser);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (userDto.getName() != null) user.setName(userDto.getName());
            if (userDto.getSurname() != null) user.setSurname(userDto.getSurname());
            if (userDto.getEmail() != null) user.setEmail(userDto.getEmail());
            if (userDto.getPhone() != null) user.setPhone(userDto.getPhone());
            userRepository.save(user);

        }
    }

    public void validateUserDto (Long idUser) {
        if (!userRepository.existsById(idUser))
            throw new EntityNotFoundException("User" + idUser + "Donde exist");
    }


    @Override
    public Optional<User> findByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user;
    }

    @Override
    public Optional<User> findById(Long id){
        if (id == null) {
            throw new IllegalArgumentException("Id is necessary");
        }
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
           throw new UserNotFoundException("User with ID " + id + " not found");
        }
        return optionalUser;
    }

    @Override
    public void changePassword(Long idUser, String newPassword, String confirmPassword) {
        User user = userRepository.findById(idUser).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (!newPassword.equals(confirmPassword)) {
            throw new IllegalArgumentException("The new password and confirm password do not match");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }



}