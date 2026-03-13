<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="isEdit" value="${not empty product}" />
<c:set var="formAction" value="${isEdit ? '/admin/product/edit.do' : '/admin/product/register.do'}" />
<c:set var="submitBtnText" value="${isEdit ? '수정하기' : '등록하기'}" />
<c:set var="pageTitle" value="${isEdit ? '상품 수정' : '신규 상품 등록'}" />

<!-- 상단 네비게이션 및 타이틀 -->
<div class="d-flex justify-content-between align-items-center mb-4 border-bottom pb-2">
    <h4>${pageTitle}</h4>
    <a href="/admin/index.do" class="btn btn-outline-secondary">목록으로</a>
</div>

<div class="row justify-content-center">
    <div class="col-md-8">
        <div class="card shadow-sm">
            <div class="card-body">
                <form action="${formAction}" method="post">
                    
                    <div class="mb-3">
                        <label for="productId" class="form-label fw-bold">상품 ID</label>
                        <input type="text" class="form-control" id="productId" name="productId" 
                               value="${product.productId}" required ${isEdit ? 'readonly' : ''} 
                               placeholder="예: P001" />
                        <c:if test="${isEdit}">
                            <div class="form-text">상품 ID는 수정할 수 없습니다.</div>
                        </c:if>
                    </div>

                    <div class="mb-3">
                        <label for="productName" class="form-label fw-bold">상품명</label>
                        <input type="text" class="form-control" id="productName" name="productName" 
                               value="${product.productName}" required placeholder="상품 이름을 입력하세요" />
                    </div>

                    <div class="row mb-3">
                        <div class="col-md-6">
                            <label for="price" class="form-label fw-bold">가격 (원)</label>
                            <input type="number" class="form-control" id="price" name="price" 
                                   value="${isEdit ? product.price : 0}" required min="0" />
                        </div>
                        <div class="col-md-6">
                            <label for="stock" class="form-label fw-bold">재고수량 (개)</label>
                            <input type="number" class="form-control" id="stock" name="stock" 
                                   value="${isEdit ? product.stock : 0}" required min="0" />
                        </div>
                    </div>

                    <div class="mb-3">
                        <label class="form-label fw-bold">카테고리 선택 (1~3개 선택)</label>
                        <div class="border rounded p-3 bg-light">
                            <div class="row">
                                <c:forEach var="category" items="${categories}">
                                    <c:set var="isChecked" value="false" />
                                    <c:if test="${isEdit}">
                                        <c:forEach var="selectedCatId" items="${product.categoryIds}">
                                            <c:if test="${selectedCatId == category.categoryId}">
                                                <c:set var="isChecked" value="true" />
                                            </c:if>
                                        </c:forEach>
                                    </c:if>
                                    <div class="col-md-4 mb-2">
                                        <div class="form-check">
                                            <input class="form-check-input" type="checkbox" name="categoryIds" 
                                                   value="${category.categoryId}" id="cat_${category.categoryId}" 
                                                   ${isChecked ? 'checked' : ''}>
                                            <label class="form-check-label" for="cat_${category.categoryId}">
                                                ${category.categoryName} <small class="text-muted">(${category.categoryId})</small>
                                            </label>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                        <div class="form-text text-danger" id="categoryHelp">최소 1개, 최대 3개까지만 선택 가능합니다.</div>
                    </div>

                    <div class="mb-4">
                        <label for="thumbnailImagePath" class="form-label fw-bold">상품 썸네일 이미지 경로</label>
                        <input type="text" class="form-control" id="thumbnailImagePath" name="thumbnailImagePath" 
                               value="${product.thumbnailImagePath}" placeholder="/resources/no-image.png" />
                        <div class="form-text">이미지가 없을 경우 기본 이미지가 표시됩니다.</div>
                    </div>

                    <div class="d-grid gap-2">
                        <button type="submit" class="btn btn-primary btn-lg" onclick="return validateForm();">${submitBtnText}</button>
                    </div>

                </form>
            </div>
        </div>
    </div>
</div>

<script>
function validateForm() {
    const checkboxes = document.querySelectorAll('input[name="categoryIds"]:checked');
    if (checkboxes.length < 1) {
        alert('카테고리를 최소 1개 이상 선택해야 합니다.');
        return false;
    }
    if (checkboxes.length > 3) {
        alert('카테고리는 최대 3개까지만 선택할 수 있습니다.');
        return false;
    }
    return true;
}
</script>
