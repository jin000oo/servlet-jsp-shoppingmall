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
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<div class="container mt-5">
    <h2>장바구니</h2>
    <table class="table table-striped mt-3">
        <thead class="table-dark">
        <tr>
            <th>이미지</th>
            <th>상품명</th>
            <th>단가</th>
            <th>수량</th>
            <th>합계</th>
            <th>관리</th>
        </tr>
        </thead>
        <tbody>
        <c:if test="${empty cartList}">
            <tr>
                <td colspan="6" class="text-center py-4">장바구니가 비어있습니다.</td>
            </tr>
        </c:if>

        <c:forEach items="${cartList}" var="cart">
            <c:set var="product" value="${productMap[cart.productId]}"/>
            <tr>
                <td>
                    <img src="${product.thumbnailImagePath}" alt="${product.productName}"
                         class="img-thumbnail" style="width: 60px; height: 60px; object-fit: contain;">
                </td>
                <td>
                    <a href="/product.do?id=${product.productId}" class="text-decoration-none fw-bold text-dark">
                            ${product.productName}
                    </a>
                </td>
                <td><fmt:formatNumber value="${product.price}" type="number"/> 원</td>
                <td>
                    <form action="/cart/update.do" method="post"
                          style="display: inline-flex; align-items: center; gap: 5px;">
                        <input type="hidden" name="productId" value="${cart.productId}">
                        <input type="number" name="quantity" value="${cart.quantity}" min="1" max="${product.stock}"
                               style="width: 70px;" class="form-control form-control-sm">
                        <button type="submit" class="btn btn-sm btn-secondary">변경</button>
                    </form>
                </td>
                <td class="text-primary fw-bold">
                    <fmt:formatNumber value="${product.price * cart.quantity}" type="number"/> 원
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

    <div class="text-end mt-4">
        <c:choose>
            <c:when test="${empty cartList}">
                <button class="btn btn-secondary btn-lg" disabled>주문하기</button>
            </c:when>
            <c:otherwise>
                <a href="/order.do" class="btn btn-primary btn-lg px-5">주문하기</a>
            </c:otherwise>
        </c:choose>
    </div>
</div>
