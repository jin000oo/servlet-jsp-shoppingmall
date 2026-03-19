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
  Date: 26. 3. 18.
  Time: 오후 3:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<div class="container mt-5">
    <h2>주문서 / 결제</h2>

    <div class="card mb-4">
        <div class="card-header bg-dark text-white fw-bold">주문 상품</div>
        <table class="table table-hover mb-0">
            <thead class="table-dark">
            <tr>
                <th>이미지</th>
                <th>상품명</th>
                <th>단가</th>
                <th>수량</th>
                <th>합계</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${cartList}" var="cart">
                <c:set var="product" value="${productMap[cart.productId]}"/>
                <tr>
                    <td>
                        <img src="${product.thumbnailImagePath}" alt="${product.productName}"
                             class="img-thumbnail" style="width: 60px; height: 60px; object-fit: contain;">
                    </td>
                    <td class="fw-bold">${product.productName}</td>
                    <td><fmt:formatNumber value="${product.price}" type="number"/> 원</td>
                    <td>${cart.quantity} 개</td>
                    <td class="text-primary fw-bold">
                        <fmt:formatNumber value="${product.price * cart.quantity}" type="number"/> 원
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>

    <div class="card mb-4">
        <div class="card-body bg-light text-end">
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger text-center" role="alert">
                    <strong>${errorMessage}</strong>
                </div>
            </c:if>

            <h5 class="mb-3">보유 포인트:
                <span class="text-primary fw-bold">
                    <fmt:formatNumber value="${currentPoint}" type="number"/> P
                </span>
            </h5>
            <h3>총 결제 금액:
                <span class="text-danger fw-bold">
                    <fmt:formatNumber value="${totalAmount}" type="number"/> 원
                </span>
            </h3>
        </div>
    </div>

    <form action="/order.do" method="post" class="text-end">
        <input type="hidden" name="totalAmount" value="${totalAmount}">
        <a href="/cart.do" class="btn btn-secondary btn-lg me-2">장바구니로 돌아가기</a>
        <button type="submit" class="btn btn-primary btn-lg" ${currentPoint < totalAmount ? 'disabled' : ''}>
            결제
        </button>
    </form>
</div>
