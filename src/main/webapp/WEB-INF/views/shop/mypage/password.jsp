<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="container mt-4">
    <div class="row">
        <!-- Sidebar -->
        <div class="col-md-3">
            <jsp:include page="sidebar.jsp">
                <jsp:param name="menu" value="password"/>
            </jsp:include>
        </div>

        <!-- Content Area -->
        <div class="col-md-9">
            <div class="card shadow-sm mb-4">
                <div class="card-header bg-white">
                    <h5 class="mb-0">비밀번호 변경</h5>
                </div>
                <div class="card-body">
                    <c:if test="${not empty error_message}">
                        <div class="alert alert-danger">
                            ${error_message}
                        </div>
                    </c:if>

                    <form action="/mypage/password.do" method="post">
                        <div class="mb-3">
                            <label for="currentPassword" class="form-label">현재 비밀번호</label>
                            <input type="password" class="form-control" id="currentPassword" name="current_password" required>
                        </div>

                        <hr class="my-4">

                        <div class="mb-3">
                            <label for="newPassword" class="form-label">새 비밀번호</label>
                            <input type="password" class="form-control" id="newPassword" name="new_password" required>
                        </div>

                        <div class="mb-3">
                            <label for="confirmPassword" class="form-label">새 비밀번호 확인</label>
                            <input type="password" class="form-control" id="confirmPassword" name="confirm_password" required>
                        </div>

                        <div class="d-flex justify-content-end gap-2 mt-4">
                            <a href="/mypage/myinfo.do" class="btn btn-secondary">취소</a>
                            <button type="submit" class="btn btn-primary">비밀번호 변경</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
