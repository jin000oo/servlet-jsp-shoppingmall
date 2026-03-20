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
  Time: 오후 2:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>시스템 오류</title>
</head>
<body>
<div style="text-align: center; margin-top: 100px;">
    <h1>${status_code}</h1>
    <h3>시스템에 일시적인 오류가 발생했습니다.</h3>
    <h5>잠시 후 다시 시도해 주세요.</h5>
    <br>
    <a href="/index.do">메인으로 돌아가기</a>
</div>
</body>
</html>