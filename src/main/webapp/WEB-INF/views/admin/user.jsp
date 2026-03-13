<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<!-- 회원 관리 영역 -->
<div class="d-flex justify-content-between align-items-center mb-3">
    <h5>전체 회원 수: <span class="text-primary fw-bold">${totalCount}</span> 명</h5>
</div>

<div class="table-responsive border rounded">
    <table class="table table-striped table-hover align-middle mb-0 text-nowrap">
        <thead class="table-dark">
            <tr class="text-center">
                <th scope="col" style="width: 120px;">권한</th>
                <th scope="col" style="width: 150px;">아이디</th>
                <th scope="col">이름</th>
                <th scope="col" style="width: 120px;">보유 포인트</th>
                <th scope="col" style="width: 180px;">가입 일자</th>
                <th scope="col" style="width: 120px;">관리</th>
            </tr>
        </thead>
        <tbody>
            <c:choose>
                <c:when test="${empty users}">
                    <tr>
                        <td colspan="6" class="text-center py-5 text-muted">등록된 회원이 없습니다.</td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <c:forEach var="user" items="${users}">
                        <tr class="text-center">
                            <td>
                                <c:choose>
                                    <c:when test="${user.userAuth == 'ROLE_ADMIN'}">
                                        <span class="badge bg-danger">관리자</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-secondary">일반회원</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="fw-bold">${user.userId}</td>
                            <td class="text-start">${user.userName}</td>
                            <td class="text-end pe-4"><fmt:formatNumber value="${user.userPoint}" type="number" />P</td>
                            <td>
                                <c:set var="createdAtStr" value="${user.createdAt.toString()}" />
                                ${fn:substring(createdAtStr, 0, 10)}
                            </td>
                            <td>
                                <a href="/admin/user/detail.do?id=${user.userId}" class="btn btn-sm btn-outline-primary">상세보기</a>
                            </td>
                        </tr>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </tbody>
    </table>
</div>

<!-- 페이지네이션 -->
<c:if test="${totalPages > 1}">
    <nav aria-label="Page navigation" class="mt-4">
        <ul class="pagination justify-content-center">
            <!-- 이전 페이지 -->
            <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                <a class="page-link" href="?page=${currentPage - 1}" tabindex="-1" aria-disabled="true">이전</a>
            </li>
            
            <!-- 페이지 번호 -->
            <c:forEach begin="1" end="${totalPages}" var="i">
                <li class="page-item ${i == currentPage ? 'active' : ''}">
                    <a class="page-link" href="?page=${i}">${i}</a>
                </li>
            </c:forEach>
            
            <!-- 다음 페이지 -->
            <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                <a class="page-link" href="?page=${currentPage + 1}">다음</a>
            </li>
        </ul>
    </nav>
</c:if>
