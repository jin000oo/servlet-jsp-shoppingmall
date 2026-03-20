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

package com.nhnacademy.shoppingmall.common.mvc.view;

public class ViewResolver {

    public static final String DEFAULT_PREFIX = "/WEB-INF/views/";
    public static final String DEFAULT_POSTFIX = ".jsp";
    public static final String REDIRECT_PREFIX = "redirect:";
    public static final String DEFAULT_SHOP_LAYOUT = "/WEB-INF/views/layout/shop.jsp";
    public static final String DEFAULT_ADMIN_LAYOUT = "/WEB-INF/views/layout/admin.jsp";
    public static final String LAYOUT_CONTENT_HOLDER = "layout_content_holder";

    private final String prefix;
    private final String postfix;

    public ViewResolver() {
        this(DEFAULT_PREFIX, DEFAULT_POSTFIX);
    }

    public ViewResolver(String prefix, String postfix) {
        this.prefix = prefix;
        this.postfix = postfix;
    }

    public String getPath(String viewName) {
        // prefix+viewNAme+postfix 반환 합니다.
        if (viewName.startsWith("/")) {
            viewName = viewName.substring(1);
        }

        return String.format("%s%s%s", prefix, viewName, postfix);
    }

    public boolean isRedirect(String viewName) {
        // REDIRECT_PREFIX가 포함되어 있는지 체크 합니다.
        return viewName.toLowerCase().startsWith(REDIRECT_PREFIX);
    }

    public String getRedirectUrl(String viewName) {
        // REDIRECT_PREFIX를 제외한 url을 반환 합니다.
        return viewName.substring(REDIRECT_PREFIX.length());
    }

    public String getLayOut(String viewName) {
        /* viewName에
           /admin/경로가 포함되었다면 DEFAULT_ADMIN_LAYOUT 반환 합니다.
           /admin/경로가 포함되어 있지않다면 DEFAULT_SHOP_LAYOUT 반환 합니다.
        */
        return viewName.contains("/admin/") ? DEFAULT_ADMIN_LAYOUT : DEFAULT_SHOP_LAYOUT;
    }

}
