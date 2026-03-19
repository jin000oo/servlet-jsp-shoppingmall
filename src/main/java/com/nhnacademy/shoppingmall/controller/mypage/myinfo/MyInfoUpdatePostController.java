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
import javax.transaction.Transactional;

@Transactional
@RequestMapping(method = RequestMapping.Method.POST, value = "/mypage/myinfo.do")
public class MyInfoUpdatePostController implements BaseController {

    private final UserService userService = new UserServiceImpl(new UserRepositoryImpl());

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return "redirect:/login.do";
        }

        User user = (User) session.getAttribute("user");
        
        String userName = req.getParameter("user_name");
        String userBirth = req.getParameter("user_birth");

        // 기본 유효성 검사
        if (userName == null || userName.isBlank() || userBirth == null || userBirth.isBlank()) {
            req.setAttribute("error_message", "이름과 생년월일은 필수 입력 항목입니다.");
            return "shop/mypage/myinfo";
        }

        user.setUserName(userName);
        user.setUserBirth(userBirth);

        userService.updateUser(user);
        
        // 세션 정보 갱신
        session.setAttribute("user", user);

        return "redirect:/mypage/index.do";
    }
}
