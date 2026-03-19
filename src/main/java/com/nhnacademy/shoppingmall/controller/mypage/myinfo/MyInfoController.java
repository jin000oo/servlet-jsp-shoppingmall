package com.nhnacademy.shoppingmall.controller.mypage.myinfo;

import com.nhnacademy.shoppingmall.address.domain.Address;
import com.nhnacademy.shoppingmall.address.repository.impl.AddressRepositoryImpl;
import com.nhnacademy.shoppingmall.address.service.AddressService;
import com.nhnacademy.shoppingmall.address.service.impl.AddressServiceImpl;
import com.nhnacademy.shoppingmall.common.mvc.annotation.RequestMapping;
import com.nhnacademy.shoppingmall.common.mvc.controller.BaseController;
import com.nhnacademy.shoppingmall.user.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
@RequestMapping(method = RequestMapping.Method.GET, value = "/mypage/myinfo.do")
public class MyInfoController implements BaseController {

    private final AddressService addressService = new AddressServiceImpl(new AddressRepositoryImpl());

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            List<Address> addresses = addressService.getAddressesByUserId(user.getUserId());
            req.setAttribute("addresses", addresses);
        }
        return "shop/mypage/myinfo";
    }

}
