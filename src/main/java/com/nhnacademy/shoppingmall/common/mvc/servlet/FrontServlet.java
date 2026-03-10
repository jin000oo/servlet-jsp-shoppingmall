/*
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * + Copyright 2026. NHN Academy Corp. All rights reserved.
 * + * While every precaution has been taken in the preparation of this resource,  assumes no
 * + responsibility for errors or omissions, or for damages resulting from the use of the information
 * + contained herein
 * + No part of this resource may be reproduced, stored in a retrieval system, or transmitted, in any
 * + form or by any means, electronic, mechanical, photocopying, recording, or otherwise, without the
 * + prior written permission.
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 */

package com.nhnacademy.shoppingmall.common.mvc.servlet;

import com.nhnacademy.shoppingmall.common.mvc.controller.BaseController;
import com.nhnacademy.shoppingmall.common.mvc.controller.ControllerFactory;
import com.nhnacademy.shoppingmall.common.mvc.transaction.DbConnectionThreadLocal;
import com.nhnacademy.shoppingmall.common.mvc.view.ViewResolver;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@WebServlet(name = "frontServlet", urlPatterns = {"*.do"})
public class FrontServlet extends HttpServlet {

    private ControllerFactory controllerFactory;
    private ViewResolver viewResolver;

    @Override
    public void init() throws ServletException {
        //todo#7-1 controllerFactory를 초기화 합니다.
        controllerFactory =
                (ControllerFactory) getServletContext().getAttribute(ControllerFactory.CONTEXT_CONTROLLER_FACTORY_NAME);

        //todo#7-2 viewResolver를 초기화 합니다.
        viewResolver = new ViewResolver();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        try {
            //todo#7-3 Connection pool로 부터 connection 할당 받습니다. connection은 Thread 내에서 공유됩니다.
            DbConnectionThreadLocal.initialize();

            BaseController baseController = (BaseController) controllerFactory.getController(req);
            String viewName = baseController.execute(req, resp);

            if (viewResolver.isRedirect(viewName)) {
                String redirectUrl = viewResolver.getRedirectUrl(viewName);
                log.debug("redirectUrl:{}", redirectUrl);

                //todo#7-6 redirect: 로 시작하면  해당 url로 redirect 합니다.
                resp.sendRedirect(redirectUrl);
            } else {
                String layout = viewResolver.getLayOut(viewName);
                log.debug("viewName:{}", viewResolver.getPath(viewName));

                req.setAttribute(ViewResolver.LAYOUT_CONTENT_HOLDER, viewResolver.getPath(viewName));

                RequestDispatcher rd = req.getRequestDispatcher(layout);
                rd.include(req, resp);
            }

        } catch (Exception e) {
            log.error("error:{}", e.getMessage());
            DbConnectionThreadLocal.setSqlError(true);

            //todo#7-5 예외가 발생하면 해당 예외에 대해서 적절한 처리를 합니다.
            req.setAttribute("status_code", 500);
            req.setAttribute("exception_type", e.getClass().getName());
            req.setAttribute("message", e.getMessage());
            req.setAttribute("exception", e);
            req.setAttribute("request_uri", req.getRequestURI());

            RequestDispatcher rd = req.getRequestDispatcher("/error.jsp");

            try {
                rd.forward(req, resp);

            } catch (ServletException | IOException ex) {
                throw new RuntimeException(ex);
            }

        } finally {
            //todo#7-4 connection을 반납합니다.
            DbConnectionThreadLocal.reset();
        }
    }

}
