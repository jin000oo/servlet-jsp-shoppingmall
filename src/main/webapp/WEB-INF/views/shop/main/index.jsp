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
    <!-- 좌측 사이드바: 카테고리 및 최근 본 상품 -->
    <div class="col-md-3">
        <div class="card mb-4">
            <div class="card-header bg-dark text-white fw-bold">카테고리</div>
            <div class="list-group list-group-flush">
                <a href="/index.do"
                   class="list-group-item list-group-item-action ${empty selectedCategoryId ? 'active' : ''}">전체 상품</a>
                <c:forEach var="category" items="${categories}">
                    <a href="/index.do?categoryId=${category.categoryId}"
                       class="list-group-item list-group-item-action ${selectedCategoryId == category.categoryId ? 'active' : ''}">
                            ${category.categoryName}
                    </a>
                </c:forEach>
            </div>
        </div>

        <c:if test="${not empty recentlyViewedProducts}">
            <div class="card">
                <div class="card-header bg-secondary text-white fw-bold">최근 본 상품</div>
                <div class="list-group list-group-flush">
                    <c:forEach var="rp" items="${recentlyViewedProducts}">
                        <a href="/product.do?id=${rp.productId}" class="list-group-item list-group-item-action p-2">
                            <div class="d-flex align-items-center">
                                <img src="${rp.thumbnailImagePath}" class="img-thumbnail me-2"
                                     style="width: 50px; height: 50px; object-fit: contain;">
                                <div class="text-truncate" style="font-size: 0.85rem;">${rp.productName}</div>
                            </div>
                        </a>
                    </c:forEach>
                </div>
            </div>
        </c:if>
    </div>

    <!-- 우측 메인 콘텐츠: 상품 리스트 -->
    <div class="col-md-9">
        <c:if test="${not empty searchName}">
            <div class="alert alert-light border mb-4">
                <span class="fw-bold">'${searchName}'</span> 검색 결과: <span
                    class="text-primary fw-bold">${totalCount}</span> 건
            </div>
        </c:if>

        <c:choose>
            <c:when test="${empty products}">
                <div class="text-center py-5 bg-white border rounded">
                    <p class="text-muted mb-0">등록된 상품이 없습니다.</p>
                </div>
            </c:when>
            <c:otherwise>
                <div class="row row-cols-1 row-cols-md-3 g-4">
                    <c:forEach var="product" items="${products}">
                        <div class="col">
                            <div class="card h-100 shadow-sm border-0">
                                <a href="/product.do?id=${product.productId}">
                                    <img src="${product.thumbnailImagePath}" class="card-img-top"
                                         alt="${product.productName}"
                                         style="height: 225px; object-fit: contain; border-top-left-radius: .25rem; border-top-right-radius: .25rem;">
                                </a>
                                <div class="card-body">
                                    <h6 class="card-title text-truncate fw-bold mb-2">${product.productName}</h6>
                                    <div class="d-flex justify-content-between align-items-center">
                                        <span class="text-primary fw-bold fs-5">
                                            <fmt:formatNumber value="${product.price}" type="number"/>원
                                        </span>
                                        <c:if test="${product.stock <= 0}">
                                            <span class="badge bg-danger">품절</span>
                                        </c:if>
                                    </div>
                                    <div class="d-grid gap-2 mt-3">
                                        <form action="/cart.do" method="post" class="d-grid">
                                            <input type="hidden" name="productId" value="${product.productId}">
                                            <input type="hidden" name="quantity" value="1">
                                            <button type="submit"
                                                    class="btn btn-sm btn-outline-primary" ${product.stock <= 0 || sessionScope.user.userAuth == "ROLE_ADMIN" ? 'disabled' : ''}>
                                                장바구니 담기
                                            </button>
                                        </form>
                                        <a href="/product.do?id=${product.productId}"
                                           class="btn btn-sm btn-light border">상세보기</a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>

                <!-- 페이지네이션 -->
                <c:if test="${totalPages > 1}">
                    <nav aria-label="Page navigation" class="mt-5">
                        <ul class="pagination justify-content-center">
                            <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                <a class="page-link"
                                   href="?page=${currentPage - 1}${not empty selectedCategoryId ? '&categoryId='.concat(selectedCategoryId) : ''}${not empty searchName ? '&searchName='.concat(searchName) : ''}">이전</a>
                            </li>
                            <c:forEach begin="1" end="${totalPages}" var="i">
                                <li class="page-item ${i == currentPage ? 'active' : ''}">
                                    <a class="page-link"
                                       href="?page=${i}${not empty selectedCategoryId ? '&categoryId='.concat(selectedCategoryId) : ''}${not empty searchName ? '&searchName='.concat(searchName) : ''}">${i}</a>
                                </li>
                            </c:forEach>
                            <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                                <a class="page-link"
                                   href="?page=${currentPage + 1}${not empty selectedCategoryId ? '&categoryId='.concat(selectedCategoryId) : ''}${not empty searchName ? '&searchName='.concat(searchName) : ''}">다음</a>
                            </li>
                        </ul>
                    </nav>
                </c:if>
            </c:otherwise>
        </c:choose>
    </div>
</div>
