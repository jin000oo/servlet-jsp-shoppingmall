<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!-- 카테고리 관리 영역 -->
<div class="d-flex justify-content-between align-items-center mb-3">
    <h5>전체 카테고리 수: <span class="text-primary fw-bold">${categories.size()}</span> 건</h5>
    <a href="/admin/category/register.do" class="btn btn-primary">+ 신규 카테고리 등록</a>
</div>

<div class="table-responsive border rounded">
    <table class="table table-striped table-hover align-middle mb-0 text-nowrap">
        <thead class="table-dark">
            <tr class="text-center">
                <th scope="col" style="width: 150px;">카테고리 ID</th>
                <th scope="col">카테고리명</th>
                <th scope="col" style="width: 150px;">정렬 순서</th>
                <th scope="col" style="width: 160px;">관리</th>
            </tr>
        </thead>
        <tbody>
            <c:choose>
                <c:when test="${empty categories}">
                    <tr>
                        <td colspan="4" class="text-center py-5 text-muted">등록된 카테고리가 없습니다.</td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <c:forEach var="category" items="${categories}">
                        <tr class="text-center">
                            <td class="text-start ps-4">${category.categoryId}</td>
                            <td class="fw-bold text-start ps-4">${category.categoryName}</td>
                            <td>${category.sortOrder}</td>
                            <td>
                                <div class="d-flex justify-content-center gap-2">
                                    <a href="/admin/category/edit.do?id=${category.categoryId}" class="btn btn-sm btn-outline-secondary">수정</a>
                                    <form action="/admin/category/delete.do" method="post" style="display:inline;" onsubmit="return confirm('정말 [${category.categoryName}] 카테고리를 삭제하시겠습니까?');">
                                        <input type="hidden" name="categoryId" value="${category.categoryId}">
                                        <button type="submit" class="btn btn-sm btn-outline-danger">삭제</button>
                                    </form>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </tbody>
    </table>
</div>
