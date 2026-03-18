<%--
  ~ /*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
  ~ + Copyright 2026. NHN Academy Corp. All rights reserved.
  ~ + * While every precaution has been taken in the preparation of this resource,  assumes no
  ~ + responsibility for errors or omissions, or for damages resulting from the use of the information
  ~ + contained herein
  ~ + No part of this resource may be reproduced, stored in a retrieval system, or transmitted, in any
  ~ + form or by any means, electronic, mechanical, photocopying, recording, or otherwise, without the
  ~ + prior written permission.
  ~ +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
  --%>

<%--
  Created by IntelliJ IDEA.
  User: chosun-nhn38
  Date: 26. 3. 12.
  Time: 오전 9:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="container mt-5">
    <h2>장바구니</h2>
    <table class="table table-striped mt-3">
        <thead>
        <tr>
            <th>상품 ID</th>
            <th>수량</th>
            <th>관리</th>
        </tr>
        </thead>
        <tbody>
        <c:if test="${empty cartList}">
            <tr>
                <td colspan="3" class="text-center">장바구니가 비어있습니다.</td>
            </tr>
        </c:if>

        <c:forEach items="${cartList}" var="cart">
            <tr>
                <td>${cart.productId}</td>
                <td>
                    <form action="/cart/update.do" method="post"
                          style="display: inline-flex; align-items: center; gap: 5px;">
                        <input type="hidden" name="productId" value="${cart.productId}">

                        <input type="number" name="quantity" value="${cart.quantity}" min="1" style="width: 70px;"
                               class="form-control form-control-sm">

                        <button type="submit" class="btn btn-sm btn-secondary">변경</button>
                    </form>
                </td>
                <td>
                    <form action="/cart/delete.do" method="post" style="display: inline;">
                        <input type="hidden" name="productId" value="${cart.productId}">
                        <button type="submit" class="btn btn-sm btn-danger">삭제</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <div class="text-end mt-3">
        <c:choose>
            <c:when test="${empty cartList}">
                <button class="btn btn-secondary btn-lg" disabled>주문하기</button>
            </c:when>
            <c:otherwise>
                <a href="/order.do" class="btn btn-primary btn-lg">주문하기</a>
            </c:otherwise>
        </c:choose>
    </div>
</div>
