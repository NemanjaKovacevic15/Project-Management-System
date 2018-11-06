package com.get.configuration.security;

import com.get.model.UserRoleType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//Strategy used to handle a successful user authentication.
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final Log logger = LogFactory.getLog(this.getClass());

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    private final static String ROLE_PREFIX = "ROLE_";

    //Called when a user has been successfully authenticated.
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication)
            throws IOException, ServletException {

        handle(request, response, authentication);
        clearAuthenticationAttributes(request);

    }

    private void handle(final HttpServletRequest request, final HttpServletResponse response,
            final Authentication authentication) throws IOException {
        final String targetUrl = determineTargetUrl(authentication);

        if (response.isCommitted()) {
            logger.debug("response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        redirectStrategy.sendRedirect(request, response, targetUrl);

    }

    //Builds the target URL according to the logic defined in the main class Javadoc.
    private String determineTargetUrl(final Authentication authentication) {

        String targetUrl = null;

        final Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        List<String> roles = new ArrayList<>();

        for (final GrantedAuthority grantedAuthority : authorities) {
            roles.add(grantedAuthority.getAuthority());
        }

        if (isAdmin(roles)) {
            targetUrl = "/list";
        } else if (isPM(roles)) {
            targetUrl = "/project";
        } else if (isDeveloper(roles)) {
            targetUrl = "/dev";
        } else {
            targetUrl = "/Access_Denied";
        }

        return targetUrl;
    }

    private boolean isAdmin(List<String> roles) {
        return roles.contains(ROLE_PREFIX + UserRoleType.ADMIN.getUserRoleType());
    }

    private boolean isPM(List<String> roles) {
        return roles.contains(ROLE_PREFIX + UserRoleType.PROJECT_MANAGER.getUserRoleType());
    }

    private boolean isDeveloper(List<String> roles) {
        return roles.contains(ROLE_PREFIX + UserRoleType.DEVELOPER.getUserRoleType());
    }

    /*Removes temporary authentication-related data which may have been stored in the session
      during the authentication process.
     */
    private final void clearAuthenticationAttributes(final HttpServletRequest request) {

        final HttpSession httpSession = request.getSession(false);

        if (httpSession == null) {
            return;
        }

        httpSession.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }

    public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
        this.redirectStrategy = redirectStrategy;
    }

    public RedirectStrategy getRedirectStrategy() {
        return redirectStrategy;
    }
}
