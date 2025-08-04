package com.uvg.earth.way.service.interfaces;

import com.uvg.earth.way.dto.UserDto;
import com.uvg.earth.way.model.User;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface IUserService {
    Optional<User> findUserById(Long id);

    void saveUser(User user);

    void deleteUser(Long id);

    Optional<User> findUserByEmail(String email);

    void updateUser(UserDto user, Long idUser);

    Optional<User> findByEmail(String email);

    Optional<User> findById(Long id);

    Page<User> getAllUsers(int page, int size, String email);

    void changePassword(Long idUser, String newPassword, String confirmPassword);
}
