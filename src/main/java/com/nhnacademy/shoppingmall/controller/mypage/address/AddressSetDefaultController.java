package com.nhnacademy.shoppingmall.controller.mypage.address;

import com.nhnacademy.shoppingmall.address.domain.Address;
import com.nhnacademy.shoppingmall.address.service.AddressService;
import com.nhnacademy.shoppingmall.common.mvc.annotation.RequestMapping;
import com.nhnacademy.shoppingmall.common.mvc.controller.BaseController;
import com.nhnacademy.shoppingmall.user.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.Objects;
import javax.transaction.Transactional;

@Transactional
@RequestMapping(method = RequestMapping.Method.POST, value = "/mypage/address/setDefault.do")
public class AddressSetDefaultController implements BaseController {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        AddressService addressService = (AddressService) req.getServletContext().getAttribute(AddressService.CONTEXT_ADDRESS_SERVICE_NAME);
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("user");

        String addressId = req.getParameter("addressId");
        if (addressId == null || addressId.isBlank()) {
            return "redirect:/mypage/myinfo.do";
        }

        Address address = addressService.getAddress(addressId);

        // 본인 소유 address인지 체크 및 이미 defaultAddress라면 반환
        if (!Objects.equals(address.getUserId(), user.getUserId()) || address.isDefaultAddress()) {
            return "redirect:/mypage/myinfo.do";
        }

        // 기본으로 설정
        address.setDefaultAddress(true);
        addressService.updateAddress(address);

        return "redirect:/mypage/myinfo.do";
    }
}
