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
  Date: 26. 3. 19.
  Time: 오후 6:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="container mt-5">
    <div class="row justify-content-center">
        <div class="col-md-6 col-lg-5">
            <div class="card shadow-sm border-0">
                <div class="card-body p-5">
                    <h3 class="text-center mb-4 fw-bold">회원가입</h3>

                    <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger text-center">
                                ${errorMessage}
                        </div>
                    </c:if>

                    <form action="/signup.do" method="post">
                        <div class="mb-3">
                            <label for="userId" class="form-label">아이디</label>
                            <input type="text" class="form-control" id="userId" name="userId" value="${userId}"
                                   placeholder="아이디를 입력하세요" required>
                        </div>
                        <div class="mb-3">
                            <label for="userPassword" class="form-label">비밀번호</label>
                            <input type="password" class="form-control" id="userPassword" name="userPassword"
                                   placeholder="비밀번호를 입력하세요" required>
                        </div>
                        <div class="mb-3">
                            <label for="userName" class="form-label">이름</label>
                            <input type="text" class="form-control" id="userName" name="userName" value="${userName}"
                                   placeholder="이름을 입력하세요" required>
                        </div>
                        <div class="mb-4">
                            <label for="userBirth" class="form-label">생년월일 (8자리)</label>
                            <input type="text" class="form-control" id="userBirth" name="userBirth" value="${userBirth}"
                                   placeholder="예: 20031107" maxlength="8" required>
                        </div>
                        <div class="d-grid gap-2">
                            <button type="submit" class="btn btn-primary btn-lg">가입하기</button>
                            <a href="/login.do" class="btn btn-outline-secondary">이미 계정이 있으신가요? 로그인</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
