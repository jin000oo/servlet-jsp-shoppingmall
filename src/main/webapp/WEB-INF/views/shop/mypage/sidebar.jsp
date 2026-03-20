<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%--
  ~ /*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
  ~ + Copyright 2026. NHN Academy Corp. All rights reserved.
  ~ + * While every precaution has been taken in the preparation of this resource,  assumes no
  ~ + responsibility for errors or omissions, or for damages resulting from the use of the information
  ~ + contained herein
  ~ + No part of this resource may be reproduced, stored in a retrieval system, or transmitted, in any
  ~ + form or by any means, electronic, mechanical, photocopying, recording, or otherwise, without the
  ~ + prior written permission.
  ~ +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
  --%>

<div class="card mb-4 shadow">
    <div class="card-body text-center py-3">
        <p class="mb-0"><span class="fs-5"><strong>${sessionScope.user.userName}</strong>님</span>, 환영합니다!</p>
    </div>
</div>

<div class="list-group shadow-sm mb-4">
    <a href="/mypage/index.do" class="list-group-item list-group-item-action ${param.menu == 'home' ? 'active' : ''}">마이페이지
        홈</a>
    <a href="/mypage/myinfo.do" class="list-group-item list-group-item-action ${param.menu == 'info' ? 'active' : ''}">내
        정보 관리</a>
    <a href="/mypage/password.do"
       class="list-group-item list-group-item-action ${param.menu == 'password' ? 'active' : ""}">비밀번호 변경</a>
    <a href="/mypage/history.do" class="list-group-item list-group-item-action ${param.menu == 'order' ? 'active' : ''}">주문
        내역</a>
    <a href="/mypage/point.do" class="list-group-item list-group-item-action ${param.menu == 'point' ? 'active' : ''}">포인트
        내역</a>
    <a href="/mypage/withdraw.do" class="list-group-item list-group-item-action text-danger mt-3">회원탈퇴</a>
</div>
