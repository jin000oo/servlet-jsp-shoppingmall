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

package com.nhnacademy.shoppingmall.common.mvc.controller;

import com.nhnacademy.shoppingmall.common.mvc.transaction.DbConnectionThreadLocal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

public class TransactionProxy implements BaseController {

    private final BaseController baseController;

    public TransactionProxy(final BaseController baseController) {
        this.baseController = baseController;
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        if (baseController.getClass().isAnnotationPresent(Transactional.class)) {
            DbConnectionThreadLocal.initialize();

            try {
                return baseController.execute(req, resp);

            } catch (Throwable e) {
                DbConnectionThreadLocal.setSqlError(true);
                throw new RuntimeException(e);

            } finally {
                DbConnectionThreadLocal.reset();
            }
        }

        return baseController.execute(req, resp);
    }

}
