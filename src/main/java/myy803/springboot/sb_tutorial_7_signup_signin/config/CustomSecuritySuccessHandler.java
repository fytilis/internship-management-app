package myy803.springboot.sb_tutorial_7_signup_signin.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/*
 * Determines the url that is appropriate for the logged user based on his role
 */
@Configuration
public class CustomSecuritySuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	 @Override
	    protected void handle(
	    		HttpServletRequest request, 
	    		HttpServletResponse response, 
	    		Authentication authentication)
	    throws java.io.IOException {
	        String targetUrl = determineTargetUrl(authentication);
	        if(response.isCommitted()) return;
	        RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	        redirectStrategy.sendRedirect(request, response, targetUrl);
	    }

	    protected String determineTargetUrl(Authentication authentication){
	       // String url = "/login?error=true";
	        //Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
	        //List<String> roles = new ArrayList<String>();
	        
	        //for(GrantedAuthority a : authorities){
	            //roles.add(a.getAuthority());
	        //}
	        
	        //if(roles.contains("ADMIN")){
	            //url = "/admin/dashboard";
	        //}else if(roles.contains("USER")) {
	            //url = "/user/dashboard"; // ZAS added /user/ here
	        //}
	        
	        //return url;
	    //}
	    	return authentication.getAuthorities().stream()
	    	        .map(GrantedAuthority::getAuthority)
	    	        .map(role -> {
	    	            switch (role) {
	    	                case "ROLE_STUDENT":
	    	                    return "/student/dashboard";
	    	                case "ROLE_PROFESSOR":
	    	                    return "/professor/dashboard";
	    	                case "ROLE_COMPANY":
	    	                    return "/company/dashboard";
	    	                case "ROLE_COMMITTEE":
	    	                    return "/committee/dashboard";
	    	                default:
	    	                    throw new IllegalStateException("Invalid role: " + role);
	    	            }
	    	        })
	    	        .findFirst()
	    	        .orElse("/login?error=true");	    	
	    }
}