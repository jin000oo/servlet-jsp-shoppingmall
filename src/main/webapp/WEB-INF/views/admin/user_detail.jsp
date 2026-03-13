<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<!-- 상단 네비게이션 및 타이틀 -->
<div class="d-flex justify-content-between align-items-center mb-4 border-bottom pb-2">
    <h4>회원 상세 정보</h4>
    <a href="/admin/user.do" class="btn btn-outline-secondary">목록으로</a>
</div>

<div class="row justify-content-center">
    <div class="col-md-8">
        <div class="card shadow-sm">
            <div class="card-header bg-light d-flex justify-content-between align-items-center">
                <span class="fw-bold text-dark">기본 정보</span>
                <c:choose>
                    <c:when test="${userDetail.userAuth == 'ROLE_ADMIN'}">
                        <span class="badge bg-danger fs-6">관리자 (ADMIN)</span>
                    </c:when>
                    <c:otherwise>
                        <span class="badge bg-secondary fs-6">일반회원 (USER)</span>
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="card-body">
                <div class="row mb-3">
                    <div class="col-sm-3 fw-bold text-muted">아이디</div>
                    <div class="col-sm-9">${userDetail.userId}</div>
                </div>
                <div class="row mb-3">
                    <div class="col-sm-3 fw-bold text-muted">이름</div>
                    <div class="col-sm-9">${userDetail.userName}</div>
                </div>
                <div class="row mb-3">
                    <div class="col-sm-3 fw-bold text-muted">생년월일</div>
                    <div class="col-sm-9">
                        <c:choose>
                            <c:when test="${not empty userDetail.userBirth and fn:length(userDetail.userBirth) == 8}">
                                ${fn:substring(userDetail.userBirth, 0, 4)}년 ${fn:substring(userDetail.userBirth, 4, 6)}월 ${fn:substring(userDetail.userBirth, 6, 8)}일
                            </c:when>
                            <c:otherwise>
                                ${userDetail.userBirth}
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
                <div class="row mb-3">
                    <div class="col-sm-3 fw-bold text-muted">보유 포인트</div>
                    <div class="col-sm-9 text-primary fw-bold">
                        <fmt:formatNumber value="${userDetail.userPoint}" type="number" /> P
                    </div>
                </div>
                <hr>
                <div class="row mb-3">
                    <div class="col-sm-3 fw-bold text-muted">가입 일시</div>
                    <div class="col-sm-9">
                        <c:set var="createdAtStr" value="${userDetail.createdAt.toString()}" />
                        ${fn:substring(createdAtStr, 0, 10)} ${fn:substring(createdAtStr, 11, 19)}
                    </div>
                </div>
                <div class="row mb-3">
                    <div class="col-sm-3 fw-bold text-muted">최근 로그인</div>
                    <div class="col-sm-9">
                        <c:choose>
                            <c:when test="${not empty userDetail.latestLoginAt}">
                                <c:set var="loginAtStr" value="${userDetail.latestLoginAt.toString()}" />
                                ${fn:substring(loginAtStr, 0, 10)} ${fn:substring(loginAtStr, 11, 19)}
                            </c:when>
                            <c:otherwise>
                                <span class="text-muted">로그인 이력 없음</span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>