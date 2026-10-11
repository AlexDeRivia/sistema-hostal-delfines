<%@include file="components/auth.jsp"%>
<%@include file="components/head.jsp"%>
<%@include file="components/sidebar.jsp"%>
<%@include file="components/navBar.jsp"%>

<% if(empleado != null) { %>
<%@include file="components/presentationEmpleado.jsp"%>
<% } else { %>
<%@include file="components/presentationAdmin.jsp"%>
<% } %>

<%@include file="components/footer.jsp"%>
