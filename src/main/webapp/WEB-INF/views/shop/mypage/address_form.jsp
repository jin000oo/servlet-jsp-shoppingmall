<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="isEdit" value="${not empty address}" />
<c:set var="formAction" value="${isEdit ? '/mypage/address/update.do' : '/mypage/address/register.do'}" />
<c:set var="submitBtnText" value="${isEdit ? '수정 완료' : '저장하기'}" />
<c:set var="pageTitle" value="${isEdit ? '배송지 수정' : '새 배송지 추가'}" />

<div class="container mt-4">
    <div class="row">
        <!-- Sidebar -->
        <div class="col-md-3">
            <jsp:include page="sidebar.jsp">
                <jsp:param name="menu" value="address"/>
            </jsp:include>
        </div>

        <!-- Content Area -->
        <div class="col-md-9">
            <div class="card shadow-sm mb-4">
                <div class="card-header bg-white">
                    <h5 class="mb-0">${pageTitle}</h5>
                </div>
                <div class="card-body">
                    <form action="${formAction}" method="post">
                        <c:if test="${isEdit}">
                            <input type="hidden" name="addressId" value="${address.addressId}">
                        </c:if>

                        <div class="mb-3">
                            <label for="addressName" class="form-label">배송지 이름</label>
                            <input type="text" class="form-control" id="addressName" name="addressName" value="${address.addressName}" placeholder="예: 우리집, 회사" required>
                        </div>

                        <div class="mb-3">
                            <label for="zipCode" class="form-label">우편번호</label>
                            <input type="text" class="form-control" id="zipCode" name="zipCode" value="${address.zipCode}" required>
                        </div>

                        <div class="mb-3">
                            <label for="roadAddress" class="form-label">도로명 주소</label>
                            <input type="text" class="form-control" id="roadAddress" name="roadAddress" value="${address.roadAddress}" required>
                        </div>

                        <div class="mb-3">
                            <label for="detailAddress" class="form-label">상세 주소</label>
                            <input type="text" class="form-control" id="detailAddress" name="detailAddress" value="${address.detailAddress}" required>
                        </div>

                        <div class="d-flex justify-content-end gap-2 mt-4">
                            <a href="/mypage/myinfo.do" class="btn btn-secondary">취소</a>
                            <button type="submit" class="btn btn-primary">${submitBtnText}</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
