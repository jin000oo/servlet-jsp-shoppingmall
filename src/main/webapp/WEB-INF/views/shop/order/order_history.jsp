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
  Date: 26. 3. 19.
  Time: 오후 5:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<div class="container mt-4">
    <div class="row">
        <div class="col-md-3">
            <jsp:include page="../mypage/sidebar.jsp">
                <jsp:param name="menu" value="order"/>
            </jsp:include>
        </div>

        <div class="col-md-9">
            <h4 class="mb-4 fw-bold">주문 내역</h4>

            <c:if test="${empty orderList}">
                <div class="alert alert-light text-center py-5 border shadow-sm">
                    <h5 class="text-muted mb-0">주문 내역이 없습니다.</h5>
                </div>
            </c:if>

            <c:forEach items="${orderList}" var="order">
                <div class="card mb-4 shadow-sm">
                    <div class="card-header bg-light d-flex justify-content-between align-items-center py-3">
                        <div>
                            <span class="fw-bold me-3">
                                주문일자:
                                <fmt:parseDate value="${order.createdAt}" pattern="yyyy-MM-dd'T'HH:mm:ss"
                                               var="parsedDate" type="both"/>
                                <fmt:formatDate value="${parsedDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                            </span>
                            <span class="text-muted" style="font-size: 0.9em;">
                                주문번호: ${order.orderId}
                            </span>
                        </div>
                        <div class="fw-bold text-danger">
                            총 결제금액: <fmt:formatNumber value="${order.totalAmount}" type="number"/> 원
                        </div>
                    </div>

                    <div class="card-body p-0">
                        <table class="table table-hover mb-0 align-middle">
                            <tbody>
                            <c:forEach items="${orderDetailMap[order.orderId]}" var="detail">
                                <c:set var="product" value="${productMap[detail.productId]}"/>
                                <tr>
                                    <td style="width: 80px; text-align: center;">
                                        <img src="${product.thumbnailImagePath}" alt="${product.productName}"
                                             class="img-thumbnail"
                                             style="width: 60px; height: 60px; object-fit: contain;">
                                    </td>
                                    <td>
                                        <a href="/product.do?id=${product.productId}"
                                           class="text-decoration-none text-dark fw-bold">
                                                ${product.productName}
                                        </a>
                                    </td>
                                    <td style="width: 120px;" class="text-end">
                                        <fmt:formatNumber value="${product.price}" type="number"/> 원
                                    </td>
                                    <td style="width: 100px;" class="text-center">
                                            ${detail.quantity} 개
                                    </td>
                                    <td style="width: 120px;" class="text-end text-primary fw-bold pe-4">
                                        <fmt:formatNumber value="${product.price * detail.quantity}" type="number"/> 원
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </c:forEach>

            <c:if test="${totalPages > 0}">
                <nav aria-label="Page navigation" class="mt-4 mb-5">
                    <ul class="pagination justify-content-center">
                        <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                            <a class="page-link" href="?page=${currentPage - 1}">이전</a>
                        </li>
                        <c:forEach begin="1" end="${totalPages}" var="i">
                            <li class="page-item ${i == currentPage ? 'active' : ''}">
                                <a class="page-link" href="?page=${i}">${i}</a>
                            </li>
                        </c:forEach>
                        <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                            <a class="page-link" href="?page=${currentPage + 1}">다음</a>
                        </li>
                    </ul>
                </nav>
            </c:if>
        </div>
    </div>
</div>
