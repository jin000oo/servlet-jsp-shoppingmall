<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!-- 탭 네비게이션 -->
<ul class="nav nav-tabs mb-4">
    <li class="nav-item">
        <a class="nav-link ${activeTab == 'product' || empty activeTab ? 'active' : ''}" href="/admin/product.do">상품 관리</a>
    </li>
    <li class="nav-item">
        <a class="nav-link ${activeTab == 'category' ? 'active' : ''}" href="/admin/category.do">카테고리 관리</a>
    </li>
    <li class="nav-item">
        <a class="nav-link ${activeTab == 'user' ? 'active' : ''}" href="/admin/user.do">회원 관리</a>
    </li>
</ul>

<!-- 탭 콘텐츠 영역 -->
<c:choose>
    <c:when test="${activeTab == 'category'}">
        <jsp:include page="category.jsp" />
    </c:when>
    <c:when test="${activeTab == 'user'}">
        <!-- <jsp:include page="user.jsp" /> -->
    </c:when>
    <c:otherwise>
        <jsp:include page="product.jsp" />
    </c:otherwise>
</c:choose>