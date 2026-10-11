package web;

import dominio.Administrador;
import dominio.Empleado;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Set;

@WebFilter("/*")
public class AccessFilter implements Filter {
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final Set<String> PUBLIC_PAGES = Set.of(
            "/acceso.jsp", "/loginAdmin.jsp", "/loginEmpleado.jsp",
            "/SvLoginAdmin", "/SvLoginEmpleado");
    private static final Set<String> ADMIN_ONLY = Set.of(
            "/SvEmpleado", "/SvAddEmpleado", "/SvUpdateEmpleado", "/SvDeleteEmpleado",
            "/VistaEmpleado.jsp", "/AddEmpleado.jsp", "/UpdateEmpleado.jsp",
            "/SvHabitacion", "/SvEditHabitacion", "/SvElimHabitacion",
            "/verHabitacion.jsp", "/agregarHabitacion.jsp", "/editarHabitacion.jsp");

    @Override
    public void doFilter(ServletRequest rawRequest, ServletResponse rawResponse, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) rawRequest;
        HttpServletResponse response = (HttpServletResponse) rawResponse;
        String path = request.getServletPath();
        String method = request.getMethod();

        response.setHeader("X-Content-Type-Options", "nosniff");
        response.setHeader("X-Frame-Options", "DENY");
        response.setHeader("Referrer-Policy", "same-origin");
        if (request.isSecure()) {
            response.setHeader("Strict-Transport-Security", "max-age=31536000");
        }

        if (isStatic(path)) {
            chain.doFilter(rawRequest, rawResponse);
            return;
        }
        if (!"GET".equals(method) && !"HEAD".equals(method) && !"POST".equals(method)) {
            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }

        HttpSession session = request.getSession(false);
        if (PUBLIC_PAGES.contains(path)) {
            if (session == null && ("GET".equals(method) || "HEAD".equals(method))) {
                session = request.getSession(true);
            }
            if (session != null) {
                ensureCsrfToken(session);
            }
            if ("POST".equals(method) && !validCsrf(request, session)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
            chain.doFilter(rawRequest, rawResponse);
            return;
        }

        Object principal = session == null ? null : session.getAttribute("login");
        boolean admin = principal instanceof Administrador
                && session.getAttribute("admin") == principal
                && session.getAttribute("empleado") == null;
        boolean employee = principal instanceof Empleado
                && session.getAttribute("empleado") == principal
                && session.getAttribute("admin") == null;
        if (!admin && !employee) {
            if ("GET".equals(method) || "HEAD".equals(method)) {
                response.sendRedirect(request.getContextPath() + "/acceso.jsp");
            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            }
            return;
        }
        if (ADMIN_ONLY.contains(path) && !admin) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        if ("POST".equals(method) && !validCsrf(request, session)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        response.setHeader("Cache-Control", "no-store");
        ensureCsrfToken(session);
        chain.doFilter(rawRequest, rawResponse);
    }

    private static boolean isStatic(String path) {
        return path.startsWith("/vendor/") || path.startsWith("/css/")
                || path.startsWith("/js/") || path.startsWith("/dist/")
                || path.startsWith("/img/") || "/favicon.ico".equals(path);
    }

    private static void ensureCsrfToken(HttpSession session) {
        if (session.getAttribute("csrfToken") == null) {
            byte[] random = new byte[32];
            RANDOM.nextBytes(random);
            session.setAttribute("csrfToken", Base64.getUrlEncoder().withoutPadding().encodeToString(random));
        }
    }

    private static boolean validCsrf(HttpServletRequest request, HttpSession session) {
        if (session == null) {
            return false;
        }
        Object expected = session.getAttribute("csrfToken");
        String supplied = request.getParameter("_csrf");
        return expected instanceof String && supplied != null
                && MessageDigest.isEqual(((String) expected).getBytes(StandardCharsets.UTF_8),
                        supplied.getBytes(StandardCharsets.UTF_8));
    }
}
