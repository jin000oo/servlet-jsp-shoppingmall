package com.nhnacademy.shoppingmall.controller.mypage.withdraw;

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
@RequestMapping(method = RequestMapping.Method.POST, value = "/mypage/withdraw.do")
public class MemberWithdrawPostController implements BaseController {

    private final UserService userService = new UserServiceImpl(new UserRepositoryImpl());

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return "redirect:/login.do";
        }

        User user = (User) session.getAttribute("user");
        String inputPassword = req.getParameter("user_password");

        // 비밀번호 체크
        if (inputPassword == null || !Objects.equals(user.getUserPassword(), inputPassword)) {
            req.setAttribute("error_message", "비밀번호가 일치하지 않습니다.");
            return "shop/mypage/withdraw";
        }

        // 비밀번호가 일치하면 탈퇴 처리
        userService.deleteUser(user.getUserId());
        session.invalidate();

        return "redirect:/index.do";
    }

}
