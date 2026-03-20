<%@ page contentType="text/html;charset=UTF-8" language="java" trimDirectiveWhitespaces="true" session="false" %>

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

<div style="margin: auto; width: 400px;">
    <div class="p-2">
        <form method="post" action="/loginAction.do">

            <h1 class="h3 mb-3 fw-normal">Please sign in</h1>

            <div class="form-floating">
                <input type="text" name="user_id" class="form-control" id="user_id" placeholder="회원 아이디" required>
                <label for="user_id">회원아이디</label>
            </div>

            <div class="form-floating">
                <input type="password" name="user_password" class="form-control" id="user_password" placeholder="비밀번호"
                       required>
                <label for="user_password">비밀번호</label>
            </div>

            <button class="w-100 btn btn-lg btn-primary mt-3" type="submit">Sign in</button>

            <div class="text-center mt-3">
                <a href="/signup.do" class="text-decoration-none">회원가입</a>
            </div>

            <p class="mt-5 mb-3 text-muted">© 2022-2024</p>

        </form>
    </div>
</div>