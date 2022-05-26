package cn.springseed.oss.common.util;

import java.util.Map;
import java.util.Optional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.util.StringUtils;

import cn.springseed.common.typeof.TypeOf;
import lombok.Builder;
import lombok.Data;
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
        return getCurrentUser().map(loginUser -> loginUser.getUsername())
                .orElseThrow(() -> OSSRuntimeException.unauthorized());
    }

    public String getCurrentUserInfo() {
        return getCurrentUser().map(loginUser -> loginUser.info()).orElse(null);
    }

    public Optional<LoginUser> getCurrentUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        final Authentication authentication = securityContext.getAuthentication();
        if (log.isDebugEnabled()) {
            log.debug("Get current user by {}",
                    authentication == null ? null : authentication.getClass().getSimpleName());
        }

        final LoginUser user = TypeOf.whenTypeOf(authentication)
                // jwt令牌
                .is(JwtAuthenticationToken.class).thenReturn(jwtAuthenticationToken -> {
                    final Jwt jwt = jwtAuthenticationToken.getToken();
                    return LoginUser.builder().username(jwt.getClaimAsString(PREFERRED_USERNAME))
                            .name(jwt.getClaimAsString("name")).build();
                })
                // 普通令牌
                .is(UsernamePasswordAuthenticationToken.class).thenReturn(usernamePasswordAuthenticationToken -> {
                    return TypeOf.whenTypeOf(usernamePasswordAuthenticationToken.getPrincipal())
                            .is(UserDetails.class)
                            .thenReturn(userDetails -> LoginUser.builder().username(userDetails.getUsername()).build())
                            .is(DefaultOidcUser.class).thenReturn(defaultOidcUser -> {
                                Map<String, Object> attributes = defaultOidcUser.getAttributes();
                                if (attributes.containsKey(PREFERRED_USERNAME)) {
                                    return LoginUser.builder().username((String) attributes.get(PREFERRED_USERNAME))
                                            .build();
                                }
                                return null;
                            })
                            .is(String.class).thenReturn(string -> LoginUser.builder().username(string).build())
                            .get();
                }).orElse((LoginUser) null);
        return Optional.ofNullable(user);
    }

    @Data
    @Builder
    public static class LoginUser {
        private String username;
        private String name;

        public String info() {
            final StringBuilder sb = new StringBuilder(username);
            if (StringUtils.hasText(name)) {
                sb.append("(").append(name).append(")");
            }
            return sb.toString();
        }
    }
}
