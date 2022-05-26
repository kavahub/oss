package cn.springseed.oss.minio.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 资源服务器
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
@Configuration
public class ResourceServerConfig extends WebSecurityConfigurerAdapter {

	// @formatter:off
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors()
		.and()
		.csrf().disable()
          .authorizeRequests()
            .antMatchers(HttpMethod.GET)
              .hasAnyAuthority("ROLE_oss_read", "SCOPE_oss_read")
            .antMatchers(HttpMethod.POST)
              .hasAnyAuthority("ROLE_oss_write", "SCOPE_oss_write")
            .antMatchers(HttpMethod.DELETE)
              .hasAnyAuthority("ROLE_oss_delete", "SCOPE_oss_delete")              
            .anyRequest()
              .authenticated()
		.and()
			.oauth2ResourceServer()
				.jwt();
	}
	// @formatter:on
    
}
