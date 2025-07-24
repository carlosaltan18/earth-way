package com.uvg.earth.way.service.interfaces;

import com.uvg.earth.way.dto.LoginUserDto;
import com.uvg.earth.way.dto.RegisterUserDto;
import com.uvg.earth.way.model.User;

public interface IAuthServices {

    User register(RegisterUserDto registerUserDto);

    User login(LoginUserDto loginUserDto);

}