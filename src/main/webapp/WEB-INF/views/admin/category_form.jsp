<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="isEdit" value="${not empty category}" />
<c:set var="formAction" value="${isEdit ? '/admin/category/edit.do' : '/admin/category/register.do'}" />
<c:set var="submitBtnText" value="${isEdit ? '수정하기' : '등록하기'}" />
<c:set var="pageTitle" value="${isEdit ? '카테고리 수정' : '신규 카테고리 등록'}" />

<!-- 상단 네비게이션 및 타이틀 -->
<div class="d-flex justify-content-between align-items-center mb-4 border-bottom pb-2">
    <h4>${pageTitle}</h4>
    <a href="/admin/category.do" class="btn btn-outline-secondary">목록으로</a>
</div>

<div class="row justify-content-center">
    <div class="col-md-8">
        <div class="card shadow-sm">
            <div class="card-body">
                <form action="${formAction}" method="post">
                    
                    <div class="mb-3">
                        <label for="categoryId" class="form-label fw-bold">카테고리 ID</label>
                        <input type="text" class="form-control" id="categoryId" name="categoryId" 
                               value="${category.categoryId}" required ${isEdit ? 'readonly' : ''} 
                               placeholder="예: C001" />
                        <c:if test="${isEdit}">
                            <div class="form-text">카테고리 ID는 수정할 수 없습니다.</div>
                        </c:if>
                    </div>

                    <div class="mb-3">
                        <label for="categoryName" class="form-label fw-bold">카테고리명</label>
                        <input type="text" class="form-control" id="categoryName" name="categoryName" 
                               value="${category.categoryName}" required placeholder="카테고리 이름을 입력하세요" />
                    </div>

                    <div class="mb-4">
                        <label for="sortOrder" class="form-label fw-bold">정렬 순서</label>
                        <input type="number" class="form-control" id="sortOrder" name="sortOrder" 
                               value="${isEdit ? category.sortOrder : 0}" required min="0" />
                        <div class="form-text">목록에 표시될 순서입니다. (숫자가 작을수록 먼저 표시됩니다)</div>
                    </div>

                    <div class="d-grid gap-2">
                        <button type="submit" class="btn btn-primary btn-lg">${submitBtnText}</button>
                    </div>

                </form>
            </div>
        </div>
    </div>
</div>
