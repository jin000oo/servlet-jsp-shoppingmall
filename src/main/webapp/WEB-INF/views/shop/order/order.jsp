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

<%--
  Created by IntelliJ IDEA.
  User: chosun-nhn38
  Date: 26. 3. 18.
  Time: 오후 3:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="container mt-5">
    <h2>주문서 / 결제</h2>

    <h4>주문 상품</h4>
    <table class="table table-bordered"></table>

    <hr>

    <div class="alert alert-secondary">
        <h4>총 결제 금액: <strong>${totalAmount} 원</strong></h4>
        <h5>사용 가능 포인트: <strong>${currentPoint} P</strong></h5>

        <c:if test="${currentPoint < totalAmount}">
            <p style="color: red;">포인트가 부족합니다.</p>
        </c:if>
    </div>

    <form action="/order.do" method="post" class="text-end">
        <input type="hidden" name="total_amount" value="${totalAmount}">
        <button type="submit" class="btn btn-primary btn-lg" ${currentPoint < totalAmount ? 'disabled' : ''}>
            결제
        </button>
    </form>
</div>
