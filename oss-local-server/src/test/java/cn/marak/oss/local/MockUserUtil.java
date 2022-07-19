package cn.marak.oss.local;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * 模拟用户登录工具
 * 
 * @author PinWei Wan
 * @since 1.0.0
 */
@UtilityClass
@Slf4j
public class MockUserUtil {
    private final static SecurityContextImpl securityContext = new SecurityContextImpl();

    public void logInAs(String username) {
        logInAs(username, new String[0]);
    }

    public void logInAs(String username, String[] roles) {
        List<GrantedAuthority> authoritiesStrings = Arrays.asList(roles).stream().map(s -> new SimpleGrantedAuthority("ROLE_" + s)).collect(Collectors.toList());

        log.info(">" + Thread.currentThread().toString());
        log.info("> Logged in as: " + username + " with the following Authorities[" + authoritiesStrings + "]");

        final UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, "", authoritiesStrings);
        securityContext.setAuthentication(token);
        SecurityContextHolder.setContext(securityContext);
    }
}
