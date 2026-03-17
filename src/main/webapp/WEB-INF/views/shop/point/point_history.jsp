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
  Date: 26. 3. 17.
  Time: 오후 2:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<div class="container mt-5">
    <h2>내 포인트 내역</h2>

    <div class="alert alert-info mt-3">
        <h4>현재 잔액: <strong>${currentPoint} P</strong></h4>
    </div>

    <table class="table table-striped mt-3">
        <thead>
        <tr>
            <th>날짜</th>
            <th>내용 (사유)</th>
            <th>금액</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${pointList}" var="point">
            <tr>
                <td>${point.createdAt}</td>
                <td>${point.reason}</td>
                <td style="color: ${point.amount > 0 ? 'blue' : 'red'};">
                        ${point.amount > 0 ? '+' : ''}${point.amount} P
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
