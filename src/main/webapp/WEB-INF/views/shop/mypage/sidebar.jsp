<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="card mb-4 shadow">
    <div class="card-body text-center py-3">
        <p class="mb-0"><span class="fs-5"><strong>${sessionScope.user.userName}</strong>님</span>, 환영합니다!</p>
    </div>
</div>

<div class="list-group shadow-sm mb-4">
    <a href="/mypage/index.do" class="list-group-item list-group-item-action ${param.menu == 'home' ? 'active' : ''}">마이페이지 홈</a>
    <a href="/mypage/myinfo.do" class="list-group-item list-group-item-action ${param.menu == 'info' ? 'active' : ''}">내 정보 관리</a>
    <a href="/mypage/password.do" class="list-group-item list-group-item-action ${param.menu == 'password' ? 'active' : ""}">비밀번호 변경</a>
    <a href="#" class="list-group-item list-group-item-action ${param.menu == 'order' ? 'active' : ''}">주문 내역</a>
    <a href="#" class="list-group-item list-group-item-action ${param.menu == 'point' ? 'active' : ''}">포인트 내역</a>
    <a href="/mypage/withdraw.do" class="list-group-item list-group-item-action text-danger mt-3">회원탈퇴</a>
</div>
