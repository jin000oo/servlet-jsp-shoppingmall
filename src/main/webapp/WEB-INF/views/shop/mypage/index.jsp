<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

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
            <div class="card shadow-sm mb-4">
                <div class="card-header bg-white">
                    <h5 class="mb-0">마이페이지</h5>
                </div>
                <div class="card-body">
                    <p class="card-text">반갑습니다, <strong>${sessionScope.user.userName}</strong>님!</p>
                    <p class="card-text text-muted">마이페이지에서는 회원님의 주문 내역, 포인트, 배송지 등을 관리하실 수 있습니다.</p>
                    
                    <div class="row mt-4">
                        <div class="col-sm-6 mb-3">
                            <div class="card border-light shadow-sm h-100">
                                <div class="card-body">
                                    <h6 class="card-title">내 정보 관리</h6>
                                    <p class="card-text small">회원님의 소중한 개인정보를 수정하거나 관리하세요.</p>
                                    <a href="/mypage/myinfo.do" class="btn btn-sm btn-outline-primary">바로가기</a>
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-6 mb-3">
                            <div class="card border-light shadow-sm h-100">
                                <div class="card-body">
                                    <h6 class="card-title">배송지 관리</h6>
                                    <p class="card-text small">자주 사용하는 배송지를 등록하고 관리하세요.</p>
                                    <a href="#" class="btn btn-sm btn-outline-primary">바로가기</a>
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
    </div>
</div>
