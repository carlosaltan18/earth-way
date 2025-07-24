package com.uvg.earth.way.service.interfaces;

import com.uvg.earth.way.model.User;

import java.util.List;

public interface IUserService {
    User findUserById(Long id);

    List<User> getAllUsers();

    void saveUser(User user);

    void deleteUser(User user);

    User findUserByEmail(String email);
}
