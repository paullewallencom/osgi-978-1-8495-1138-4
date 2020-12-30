<%@ include file="init-no-check.inc.jsp" %>
<%  // check session
    if (!sessionBean.sessionIsValid()) {
        response.sendRedirect("login.jsp");
    }
%>