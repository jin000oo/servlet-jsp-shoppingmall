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
                        ⚙️ 관리자 대시보드
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

        <main>
            <div class="container">
                <jsp:include page="${layout_content_holder}" />
            </div>
        </main>

        <footer class="text-muted py-5 mt-5 border-top">
            <div class="container text-center">
                <p class="mb-1">admin dashboard is &copy; nhnacademy.com</p>
            </div>
        </footer>
    </div>
</body>
</html>