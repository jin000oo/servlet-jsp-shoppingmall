package com.nhnacademy.shoppingmall.user.service;

import com.nhnacademy.shoppingmall.common.page.Page;
import com.nhnacademy.shoppingmall.user.domain.User;

public interface UserService {
    String CONTEXT_USER_SERVICE_NAME = "CONTEXT_USER_SERVICE";

    User getUser(String userId);

    void saveUser(User user);

    void updateUser(User user);

    void deleteUser(String userId);

    User doLogin(String userId, String userPassword);

    Page<User> getUsers(int page, int pageSize);
}
