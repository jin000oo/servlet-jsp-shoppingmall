<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="container mt-4">
    <div class="row">
        <!-- Sidebar -->
        <div class="col-md-3">
            <jsp:include page="sidebar.jsp">
                <jsp:param name="menu" value="withdraw"/>
            </jsp:include>
        </div>

        <!-- Content Area -->
        <div class="col-md-9">
            <div class="card shadow-sm mb-4 border-danger">
                <div class="card-header bg-danger text-white">
                    <h5 class="mb-0">회원탈퇴 확인</h5>
                </div>
                <div class="card-body">
                    <h5 class="card-title text-danger mb-4">정말로 탈퇴하시겠습니까?</h5>

                    <c:if test="${not empty error_message}">
                        <div class="alert alert-danger">
                            ${error_message}
                        </div>
                    </c:if>

                    <div class="alert alert-warning">
                        탈퇴를 원하시면 본인 확인을 위해 비밀번호를 입력해 주세요. 이 작업은 취소할 수 없습니다.
                    </div>

                    <form action="/mypage/withdraw.do" method="post">
                        <div class="mb-3">
                            <label for="userPassword" class="form-label">비밀번호 확인</label>
                            <input type="password" class="form-control" id="userPassword" name="user_password" placeholder="비밀번호를 입력하세요" required>
                        </div>
                        <div class="d-flex justify-content-end gap-2 mt-4">
                            <a href="/mypage/index.do" class="btn btn-secondary">취소</a>
                            <button type="submit" class="btn btn-danger">탈퇴하기</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
