package com.nhnacademy.shoppingmall.controller.mypage.myinfo;

import com.nhnacademy.shoppingmall.common.mvc.annotation.RequestMapping;
import com.nhnacademy.shoppingmall.common.mvc.controller.BaseController;
import com.nhnacademy.shoppingmall.user.domain.User;
import com.nhnacademy.shoppingmall.user.repository.impl.UserRepositoryImpl;
import com.nhnacademy.shoppingmall.user.service.UserService;
import com.nhnacademy.shoppingmall.user.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.Objects;
import javax.transaction.Transactional;

@Transactional
@RequestMapping(method = RequestMapping.Method.POST, value = "/mypage/password.do")
public class PasswordChangePostController implements BaseController {

    private final UserService userService = new UserServiceImpl(new UserRepositoryImpl());

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return "redirect:/login.do";
        }

        User user = (User) session.getAttribute("user");
        String currentPassword = req.getParameter("current_password");
        String newPassword = req.getParameter("new_password");
        String confirmPassword = req.getParameter("confirm_password");

        // 현재 비밀번호 확인
        if (!user.getUserPassword().equals(currentPassword)) {
            req.setAttribute("error_message", "현재 비밀번호가 일치하지 않습니다.");
            return "shop/mypage/password";
        }

        // 새 비밀번호 유효성 검사
        if (newPassword == null || newPassword.isBlank()) {
            req.setAttribute("error_message", "새 비밀번호를 입력해주세요.");
            return "shop/mypage/password";
        }

        if (!newPassword.equals(confirmPassword)) {
            req.setAttribute("error_message", "새 비밀번호 확인이 일치하지 않습니다.");
            return "shop/mypage/password";
        }

        // 비밀번호 업데이트
        user.setUserPassword(newPassword);
        userService.updateUser(user);
        
        session.setAttribute("user", user);

        return "redirect:/mypage/index.do";
    }

}
