package LoginHandling;

import java.io.IOException;

import javax.faces.application.ResourceHandler;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebFilter("/JSF/*")
public class AuthorizationFilter implements Filter {

    private static final String AJAX_REDIRECT_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
        + "<partial-response><redirect url=\"%s\"></redirect></partial-response>";

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws ServletException, IOException {    
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);
        String loginURL = request.getContextPath() + "/login.xhtml";

        boolean loggedIn = (session != null) && (session.getAttribute("user") != null);
        boolean loginRequest = request.getRequestURI().equals(loginURL);
        boolean resourceRequest = request.getRequestURI().startsWith(request.getContextPath() + ResourceHandler.RESOURCE_IDENTIFIER + "/");
        boolean ajaxRequest = "partial/ajax".equals(request.getHeader("Faces-Request"));
        
        if (loggedIn || loginRequest || resourceRequest) {
            chain.doFilter(request, response); 
        }
        else if (ajaxRequest) {
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().printf(AJAX_REDIRECT_XML, loginURL); 
        }
        else {
            response.sendRedirect(loginURL);
        }
    }

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		System.out.println("Login Filter gesartet");
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}