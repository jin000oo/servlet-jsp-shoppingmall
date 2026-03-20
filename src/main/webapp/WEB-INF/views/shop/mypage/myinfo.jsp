<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="container mt-4">
    <div class="row">
        <!-- Sidebar -->
        <div class="col-md-3">
            <jsp:include page="sidebar.jsp">
                <jsp:param name="menu" value="info"/>
            </jsp:include>
        </div>

        <!-- Content Area -->
        <div class="col-md-9">
            <!-- 기본 정보 섹션 -->
            <div class="card shadow-sm mb-4">
                <div class="card-header bg-white d-flex justify-content-between align-items-center">
                    <h5 class="mb-0">내 정보 관리</h5>
                    <a href="/mypage/password.do" class="btn btn-sm btn-outline-secondary">비밀번호 변경</a>
                </div>
                <div class="card-body">
                    <form action="/mypage/myinfo.do" method="post">
                        <div class="mb-3">
                            <label class="form-label">아이디</label>
                            <input type="text" class="form-control text-muted" value="${sessionScope.user.userId}" disabled>
                        </div>

                        <div class="mb-3">
                            <label for="userName" class="form-label">이름</label>
                            <input type="text" class="form-control" id="userName" name="user_name" value="${sessionScope.user.userName}" required>
                        </div>

                        <div class="mb-3">
                            <label for="userBirth" class="form-label">생년월일 (YYYYMMDD)</label>
                            <input type="text" class="form-control" id="userBirth" name="user_birth" value="${sessionScope.user.userBirth}" pattern="\d{8}" required>
                        </div>

                        <div class="d-flex justify-content-end gap-2 mt-4">
                            <button type="submit" class="btn btn-primary">기본 정보 수정</button>
                        </div>
                    </form>
                </div>
            </div>

            <!-- 배송지 관리 섹션 -->
            <div class="card shadow-sm mb-4">
                <div class="card-header bg-white d-flex justify-content-between align-items-center">
                    <h5 class="mb-0">배송지 관리</h5>
                    <a href="/mypage/address/register.do" class="btn btn-sm btn-primary">새 배송지 추가</a>
                </div>
                <div class="card-body">
                    <div class="table-responsive">
                        <table class="table table-hover align-middle text-nowrap">
                            <thead class="table-light">
                                <tr>
                                    <th style="width: 80px;">기본 주소</th>
                                    <th>배송지명</th>
                                    <th>주소</th>
                                    <th style="width: 120px;">관리</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:choose>
                                    <c:when test="${not empty addresses}">
                                        <c:forEach var="address" items="${addresses}">
                                            <tr>
                                                <td class="text-center">
                                                    <c:choose>
                                                        <c:when test="${address.defaultAddress}">
                                                            <span class="badge bg-info">기본</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <form action="/mypage/address/setDefault.do" method="post" class="m-0">
                                                                <input type="hidden" name="addressId" value="${address.addressId}">
                                                                <button type="submit" class="btn btn-sm btn-outline-success py-0" style="font-size: 0.75rem;">설정</button>
                                                            </form>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>${address.addressName}</td>
                                                <td>(${address.zipCode}) ${address.roadAddress} ${address.detailAddress}</td>
                                                <td>
                                                    <div class="d-flex gap-2">
                                                        <a href="/mypage/address/update.do?addressId=${address.addressId}" class="btn btn-sm btn-outline-primary">수정</a>
                                                        <form action="/mypage/address/delete.do" method="post" class="m-0" onsubmit="return confirm('정말 삭제하시겠습니까?');">
                                                            <input type="hidden" name="addressId" value="${address.addressId}">
                                                            <button type="submit" class="btn btn-sm btn-outline-danger">삭제</button>
                                                        </form>
                                                    </div>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <tr>
                                            <td colspan="3" class="text-center py-4 text-muted">등록된 배송지가 없습니다.</td>
                                        </tr>
                                    </c:otherwise>
                                </c:choose>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
