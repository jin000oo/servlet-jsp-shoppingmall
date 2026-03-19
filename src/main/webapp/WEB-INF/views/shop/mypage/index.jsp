<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<div class="container mt-4">
    <div class="row">
        <!-- Sidebar -->
        <div class="col-md-3">
            <jsp:include page="sidebar.jsp">
                <jsp:param name="menu" value="home"/>
            </jsp:include>
        </div>

        <!-- Content Area -->
        <div class="col-md-9">
            <!-- User Summary Header -->
            <div class="card shadow-sm mb-4 border-primary">
                <div class="card-body py-4">
                    <div class="row align-items-center">
                        <div class="col-md-8">
                            <h4 class="mb-1"><strong>${sessionScope.user.userName}</strong>님, 반갑습니다!</h4>
                            <p class="text-muted mb-0">마이페이지에서 회원님의 활동 내역을 한눈에 확인하세요.</p>
                        </div>
                        <div class="col-md-4 text-md-end mt-3 mt-md-0">
                            <div class="p-3 bg-light rounded shadow-sm d-inline-block w-100 text-center">
                                <span class="text-muted small d-block mb-1">나의 보유 포인트</span>
                                <span class="h3 text-primary fw-bold mb-0">
                                    <fmt:formatNumber value="${sessionScope.user.userPoint}" pattern="#,###"/>
                                </span>
                                <small class="text-primary ms-1">P</small>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Dashboard Menu Cards -->
            <div class="row mt-4">
                <div class="col-sm-6 mb-3">
                    <div class="card border-light shadow-sm h-100">
                        <div class="card-body">
                            <h6 class="card-title">내 정보 관리</h6>
                            <p class="card-text small">회원님의 회원정보 및 배송지를 수정하거나 관리하세요.</p>
                            <a href="/mypage/myinfo.do" class="btn btn-sm btn-outline-primary">바로가기</a>
                        </div>
                    </div>
                </div>
                <div class="col-sm-6 mb-3">
                    <div class="card border-light shadow-sm h-100">
                        <div class="card-body">
                            <h6 class="card-title">비밀번호 변경</h6>
                            <p class="card-text small">보안을 위해 회원님의 소중한 비밀번호를 주기적으로 변경하세요.</p>
                            <a href="/mypage/password.do" class="btn btn-sm btn-outline-primary">바로가기</a>
                        </div>
                    </div>
                </div>
                <div class="col-sm-6 mb-3">
                    <div class="card border-light shadow-sm h-100">
                        <div class="card-body">
                            <h6 class="card-title">주문 내역</h6>
                            <p class="card-text small">최근 주문한 내역을 한눈에 확인하세요.</p>
                            <a href="#" class="btn btn-sm btn-outline-primary">바로가기</a>
                        </div>
                    </div>
                </div>
                <div class="col-sm-6 mb-3">
                    <div class="card border-light shadow-sm h-100">
                        <div class="card-body">
                            <h6 class="card-title">포인트 내역</h6>
                            <p class="card-text small">포인트의 적립 및 사용 내역을 확인하세요.</p>
                            <a href="#" class="btn btn-sm btn-outline-primary">바로가기</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
