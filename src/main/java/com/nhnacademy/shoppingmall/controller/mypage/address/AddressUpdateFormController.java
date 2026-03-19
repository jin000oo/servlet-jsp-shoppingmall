package com.nhnacademy.shoppingmall.controller.mypage.address;

import com.nhnacademy.shoppingmall.address.domain.Address;
import com.nhnacademy.shoppingmall.address.service.AddressService;
import com.nhnacademy.shoppingmall.common.mvc.annotation.RequestMapping;
import com.nhnacademy.shoppingmall.common.mvc.controller.BaseController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.transaction.Transactional;

@Transactional
@RequestMapping(method = RequestMapping.Method.GET, value = "/mypage/address/update.do")
public class AddressUpdateFormController implements BaseController {
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        AddressService addressService = (AddressService) req.getServletContext().getAttribute(AddressService.CONTEXT_ADDRESS_SERVICE_NAME);

        String addressId = req.getParameter("addressId");
        if(addressId == null || addressId.isBlank()) {
            return "redirect:/mypage/myinfo.do";
        }

        Address address = addressService.getAddress(addressId);
        req.setAttribute("address", address);

        return "shop/mypage/address_form";
    }
}
