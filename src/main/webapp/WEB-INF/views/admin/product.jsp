<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!-- 상품 관리 영역 -->
<div class="d-flex justify-content-between align-items-center mb-3">
    <h5>전체 상품 수: <span class="text-primary fw-bold">${totalCount}</span> 건</h5>
    <a href="/admin/product/register.do" class="btn btn-primary">+ 신규 상품 등록</a>
</div>

<div class="table-responsive border rounded">
    <table class="table table-striped table-hover align-middle mb-0 text-nowrap">
        <thead class="table-dark">
            <tr class="text-center">
                <th scope="col" style="width: 80px;">이미지</th>
                <th scope="col" style="width: 120px;">상품 ID</th>
                <th scope="col">상품명</th>
                <th scope="col">카테고리 ID</th>
                <th scope="col" style="width: 150px;">가격</th>
                <th scope="col" style="width: 100px;">재고</th>
                <th scope="col" style="width: 160px;">관리</th>
            </tr>
        </thead>
        <tbody>
            <c:choose>
                <c:when test="${empty products}">
                    <tr>
                        <td colspan="7" class="text-center py-5 text-muted">등록된 상품이 없습니다.</td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <c:forEach var="product" items="${products}">
                        <tr class="text-center">
                            <td>
                                <img src="${product.thumbnailImagePath}" alt="상품 이미지" class="img-thumbnail" style="width: 50px; height: 50px; object-fit: cover;">
                            </td>
                            <td class="text-start ps-4">${product.productId}</td>
                            <td class="text-start fw-bold">${product.productName}</td>
                            <td>
                                <c:forEach var="catId" items="${product.categoryIds}">
                                    <span class="badge bg-secondary">
                                        <c:choose>
                                            <c:when test="${not empty categoryMap[catId]}">${categoryMap[catId]}</c:when>
                                            <c:otherwise>${catId}</c:otherwise>
                                        </c:choose>
                                    </span>
                                </c:forEach>
                            </td>
                            <td class="text-end pe-4"><fmt:formatNumber value="${product.price}" type="number" />원</td>
                            <td class="text-end pe-4">
                                <c:choose>
                                    <c:when test="${product.stock <= 5}">
                                        <span class="text-danger fw-bold">${product.stock}</span>
                                    </c:when>
                                    <c:otherwise>
                                        ${product.stock}
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <div class="d-flex justify-content-center gap-2">
                                    <a href="/admin/product/edit.do?id=${product.productId}" class="btn btn-sm btn-outline-secondary">수정</a>
                                    <form action="/admin/product/delete.do" method="post" style="display:inline;" onsubmit="return confirm('정말 [${product.productName}] 상품을 삭제하시겠습니까?');">
                                        <input type="hidden" name="productId" value="${product.productId}">
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