package cn.marak.oss.local.util;

import java.util.Map;
import java.util.Optional;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * 安全工具
 * 
 * @author PinWei Wan
 * @since 1.0.0
 */
@UtilityClass
@Slf4j
public class SecurityUtils {
    public final static String PREFERRED_USERNAME = "preferred_username";

    public String getCurrentUsername() {
        return getCurrentUser().orElseThrow(() -> new AuthenticationCredentialsNotFoundException("未登录"));
    }

    public String getCurrentUserInfo() {
        return getCurrentUser().orElse(null);
    }

    public Optional<String> getCurrentUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        final Authentication authentication = securityContext.getAuthentication();

        if (authentication == null) {
            return Optional.ofNullable(null);
        }

        if (log.isDebugEnabled()) {
            log.debug("Get current user in {}", authentication.getClass().getSimpleName());
        }

        if (JwtAuthenticationToken.class.isAssignableFrom(authentication.getClass())) {
            // jwt令牌
            final Jwt jwt = ((JwtAuthenticationToken) authentication).getToken();
            final String username = jwt.getClaimAsString(PREFERRED_USERNAME);
            return Optional.ofNullable(username);
        }

        if (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication.getClass())) {
            // 普通令牌
            final Object principal = ((UsernamePasswordAuthenticationToken) authentication).getPrincipal();
            if (UserDetails.class.isAssignableFrom(principal.getClass())) {
                return Optional.of(((UserDetails) principal).getUsername());
            }

            if (DefaultOidcUser.class.isAssignableFrom(principal.getClass())) {
                Map<String, Object> attributes = ((DefaultOidcUser) principal).getAttributes();
                return Optional.ofNullable((String) attributes.get(PREFERRED_USERNAME));
            }

            if (String.class.isAssignableFrom(principal.getClass())) {
                return Optional.ofNullable((String) principal);
            }
        }

        return Optional.ofNullable(null);
    }
}
