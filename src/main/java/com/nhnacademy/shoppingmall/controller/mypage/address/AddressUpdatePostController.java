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
@RequestMapping(method = RequestMapping.Method.POST, value = "/mypage/address/update.do")
public class AddressUpdatePostController implements BaseController {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        AddressService addressService = (AddressService) req.getServletContext().getAttribute(AddressService.CONTEXT_ADDRESS_SERVICE_NAME);
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("user");

        String addressId = req.getParameter("addressId");
        String addressName = req.getParameter("addressName");
        String zipCode = req.getParameter("zipCode");
        String roadAddress = req.getParameter("roadAddress");
        String detailAddress = req.getParameter("detailAddress");

        Address address = addressService.getAddress(addressId);

        // 본인 소유 확인
        if (!Objects.equals(address.getUserId(), user.getUserId())) {
            return "redirect:/mypage/myinfo.do";
        }

        address.setAddressName(addressName);
        address.setZipCode(zipCode);
        address.setRoadAddress(roadAddress);
        address.setDetailAddress(detailAddress);

        addressService.updateAddress(address);

        return "redirect:/mypage/myinfo.do";
    }
}
