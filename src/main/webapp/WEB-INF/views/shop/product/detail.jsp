<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

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

<div class="row">
    <div class="col-md-6">
        <img src="${product.thumbnailImagePath}" class="img-fluid rounded-3 shadow-lg mb-5 w-100"
             alt="${product.productName}">
        <div class="row g-3">
            <c:forEach var="imagePath" items="${product.detailImagePaths}">
                <div class="col-3">
                    <img src="${imagePath}" class="img-thumbnail"
                         style="height: 100%; width: 100px; object-fit: contain;">
                </div>
            </c:forEach>
        </div>
    </div>
    <div class="col-md-5">
        <nav aria-label="breadcrumb">
            <ol class="breadcrumb">
                <li class="page-item"><a href="/index.do" class="text-decoration-none text-muted">Home</a></li>
                <li class="breadcrumb-item active" aria-current="page">${product.productName}</li>
            </ol>
        </nav>

        <h2 class="fw-bold mb-3">${product.productName}</h2>
        <h3 class="text-primary fw-bold mb-4">
            <fmt:formatNumber value="${product.price}" type="number"/>원
        </h3>

        <div class="mb-4">
            <span class="badge bg-light text-dark border me-2">재고: ${product.stock}개</span>
            <c:forEach var="catId" items="${product.categoryIds}">
                <span class="badge bg-secondary me-1">
                    <c:choose>
                        <c:when test="${not empty categoryMap[catId]}">${categoryMap[catId]}</c:when>
                        <c:otherwise>${catId}</c:otherwise>
                    </c:choose>
                </span>
            </c:forEach>
        </div>

        <div class="d-grid gap-2">
            <form action="/cart.do" method="post" class="d-grid">
                <input type="hidden" name="product_id" value="${product.productId}">
                <div class="input-group mb-3">
                    <span class="input-group-text">수량</span>
                    <input type="number" name="quantity" class="form-control" value="1" min="1" max="${product.stock}">
                </div>
                <button type="submit"
                        class="btn btn-primary btn-lg" ${product.stock <= 0 || sessionScope.user.userAuth == "ROLE_ADMIN" ? 'disabled' : ''}>
                    장바구니 담기
                </button>
            </form>
            <a href="/index.do" class="btn btn-outline-secondary">목록으로</a>
        </div>
    </div>
</div>
