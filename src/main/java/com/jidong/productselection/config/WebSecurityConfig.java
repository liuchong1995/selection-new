package com.jidong.productselection.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jidong.productselection.response.LoginResponse;
import com.jidong.productselection.service.impl.UserServiceImpl;
import com.jidong.productselection.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.web.cors.CorsUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by sang on 2017/12/28.
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	UserServiceImpl userService;

	@Autowired
	UrlAccessDecisionManager urlAccessDecisionManager;

	@Autowired
	AuthenticationAccessDeniedHandler deniedHandler;

	@Autowired
	CorsControllerFilter corsControllerFilter;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService)
				.passwordEncoder(new BCryptPasswordEncoder());
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/index.html", "/static/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.addFilterBefore(corsControllerFilter, SecurityContextPersistenceFilter.class)
				.authorizeRequests()
				.requestMatchers(CorsUtils::isPreFlightRequest).permitAll()//就是这一行啦
				.and()
				.formLogin().loginProcessingUrl("/login")
				.usernameParameter("username").passwordParameter("password")
				.failureHandler((req, resp, e) -> {
					resp.setContentType("application/json;charset=utf-8");
					LoginResponse loginResponse = null;
					if (e instanceof BadCredentialsException ||
							e instanceof UsernameNotFoundException) {
						loginResponse = LoginResponse.error("账户名或者密码输入错误!");
					} else if (e instanceof LockedException) {
						loginResponse = LoginResponse.error("账户被锁定，请联系管理员!");
					} else if (e instanceof CredentialsExpiredException) {
						loginResponse = LoginResponse.error("密码过期，请联系管理员!");
					} else if (e instanceof AccountExpiredException) {
						loginResponse = LoginResponse.error("账户过期，请联系管理员!");
					} else if (e instanceof DisabledException) {
						loginResponse = LoginResponse.error("账户被禁用，请联系管理员!");
					} else {
						loginResponse = LoginResponse.error("登录失败!");
					}
					resp.setStatus(401);
					writeResult(resp, loginResponse);
				})
				.successHandler((req, resp, auth) -> {
					resp.setContentType("application/json;charset=utf-8");
					LoginResponse loginResponse = LoginResponse.ok("登录成功!", UserUtils.getCurrentUser());
					writeResult(resp, loginResponse);
				})
				.permitAll()
				.and()
				.logout().permitAll()
				.and().csrf().disable()
				.exceptionHandling().accessDeniedHandler(deniedHandler);
	}

	private void writeResult(HttpServletResponse resp, LoginResponse loginResponse) throws IOException {
		ObjectMapper om = new ObjectMapper();
		PrintWriter out = resp.getWriter();
		out.write(om.writeValueAsString(loginResponse));
		out.flush();
		out.close();
	}
}