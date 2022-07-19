package cn.marak.oss.local.config;

import org.springframework.context.annotation.Configuration;
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
            .anyRequest()
              .authenticated()
		.and()
			.oauth2ResourceServer()
				.jwt();
	}
	// @formatter:on

}
