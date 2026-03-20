<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!doctype html>
<html lang="ko">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
    <title>관리자 대시보드 - NHN Shopping Mall</title>
</head>
<body>
    <div class="mainContainer">
        <header class="p-3 bg-dark text-white mb-4">
            <div class="container">
                <div class="d-flex flex-wrap align-items-center justify-content-between">
                    <a href="/admin/index.do" class="text-white text-decoration-none fs-4 fw-bold">
                        관리자 대시보드
                    </a>
                    <div class="text-end">
                        <a href="/index.do" class="btn btn-outline-light me-2">쇼핑몰 홈으로</a>
                        <form action="/logout.do" method="post" style="display:inline;">
                            <button type="submit" class="btn btn-danger">로그아웃</button>
                        </form>
                    </div>
                </div>
            </div>
        </header>
                <%-- 공통 에러 메시지 출력 (Flash Attribute 지원) --%>
                <c:set var="errorMsg" value="${not empty errorMessage ? errorMessage : sessionScope.errorMessage}" />
                <c:if test="${not empty errorMsg}">
                    <div class="alert alert-danger mx-auto" role="alert" style="width: 70%;">
                        ${errorMsg}
                    </div>
                    <c:remove var="errorMessage" scope="session" />
                </c:if>

                <%-- 공통 성공 메시지 출력 (Flash Attribute 지원) --%>
                <c:set var="successMsg" value="${not empty successMessage ? successMessage : sessionScope.successMessage}" />
                <c:if test="${not empty successMsg}">
                    <div class="alert alert-success mx-auto" role="alert" style="width: 70%;">
                        ${successMsg}
                    </div>
                    <c:remove var="successMessage" scope="session" />
                </c:if>

                <jsp:include page="${layout_content_holder}" />
            </div>
        </main>

        <footer class="text-muted py-5">
            <div class="container">
                <p class="float-end mb-1">
                    <a href="#">Back to top</a>
                </p>
                <p class="mb-1">shoppingmall example is © nhnacademy.com</p>
            </div>
        </footer>
    </div>
</body>
</html>