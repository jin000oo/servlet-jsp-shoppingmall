<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<div class="card mb-4 shadow-sm">
    <div class="card-body text-center">
        <h5 class="my-3">${sessionScope.user.userName} 님</h5>
        <p class="text-muted mb-1">보유 포인트</p>
        <p class="h4 text-primary mb-4">
            <fmt:formatNumber value="${sessionScope.user.userPoint}" pattern="#,###"/> P
        </p>
    </div>
</div>

<div class="list-group shadow-sm mb-4">
    <a href="/mypage/index.do" class="list-group-item list-group-item-action ${param.menu == 'home' ? 'active' : ''}">마이페이지 홈</a>
    <a href="#" class="list-group-item list-group-item-action ${param.menu == 'info' ? 'active' : ''}">내 정보 관리</a>
    <a href="#" class="list-group-item list-group-item-action ${param.menu == 'order' ? 'active' : ''}">주문 내역</a>
    <a href="#" class="list-group-item list-group-item-action ${param.menu == 'point' ? 'active' : ''}">포인트 내역</a>
    <a href="#" class="list-group-item list-group-item-action ${param.menu == 'address' ? 'active' : ''}">배송지 관리</a>
</div>
