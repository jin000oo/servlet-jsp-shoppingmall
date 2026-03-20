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

<div class="container mt-4">
    <div class="row">
        <div class="col-md-3">
            <jsp:include page="../mypage/sidebar.jsp">
                <jsp:param name="menu" value="point"/>
            </jsp:include>
        </div>

        <div class="col-md-9">
            <div class="card shadow-sm mb-4">
                <div class="card-header bg-white py-3">
                    <h5 class="mb-0 fw-bold">내 포인트 내역</h5>
                </div>

                <div class="card-body p-4">
                    <div class="alert alert-info mb-4">
                        <h5 class="mb-0">현재 잔액: <strong>${currentPoint} P</strong></h5>
                    </div>

                    <table class="table table-hover align-middle">
                        <thead class="table-light">
                        <tr>
                            <th>날짜</th>
                            <th>내용 (사유)</th>
                            <th class="text-end">금액</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${pointList}" var="point">
                            <tr>
                                <td>${point.createdAt}</td>
                                <td>${point.reason}</td>
                                <td class="text-end fw-bold" style="color: ${point.amount > 0 ? 'blue' : 'red'};">
                                        ${point.amount > 0 ? '+' : ''}${point.amount} P
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>

                    <c:if test="${totalPages > 0}">
                        <nav aria-label="Page navigation" class="mt-4">
                            <ul class="pagination justify-content-center mb-0">
                                <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                    <a class="page-link" href="?page=${currentPage - 1}">이전</a>
                                </li>
                                <c:forEach begin="1" end="${totalPages}" var="i">
                                    <li class="page-item ${i == currentPage ? 'active' : ''}">
                                        <a class="page-link" href="?page=${i}">${i}</a>
                                    </li>
                                </c:forEach>
                                <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                                    <a class="page-link" href="?page=${currentPage + 1}">다음</a>
                                </li>
                            </ul>
                        </nav>
                    </c:if>
                </div>
            </div>
        </div>
    </div>
</div>
