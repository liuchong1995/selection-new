package com.jidong.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jidong.response.RespBean;
import com.jidong.service.impl.UserServiceImpl;
import com.jidong.util.UserUtils;
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

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService)
				.passwordEncoder(new BCryptPasswordEncoder());
	}
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/index.html", "/static/**", "/login_p");
	}
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.and()
				.formLogin().loginPage("/login_p").loginProcessingUrl("/login")
				.usernameParameter("username").passwordParameter("password")
				.failureHandler((req, resp, e) -> {
					resp.setContentType("application/json;charset=utf-8");
					RespBean respBean = null;
					if (e instanceof BadCredentialsException ||
							e instanceof UsernameNotFoundException) {
						respBean = RespBean.error("账户名或者密码输入错误!");
					} else if (e instanceof LockedException) {
						respBean = RespBean.error("账户被锁定，请联系管理员!");
					} else if (e instanceof CredentialsExpiredException) {
						respBean = RespBean.error("密码过期，请联系管理员!");
					} else if (e instanceof AccountExpiredException) {
						respBean = RespBean.error("账户过期，请联系管理员!");
					} else if (e instanceof DisabledException) {
						respBean = RespBean.error("账户被禁用，请联系管理员!");
					} else {
						respBean = RespBean.error("登录失败!");
					}
					resp.setStatus(401);
					writeResult(resp,respBean);
				})
				.successHandler((req, resp, auth) -> {
					resp.setContentType("application/json;charset=utf-8");
					RespBean respBean = RespBean.ok("登录成功!", UserUtils.getCurrentUser());
					writeResult(resp,respBean);
				})
				.permitAll()
				.and()
				.logout().permitAll()
				.and().csrf().disable()
				.exceptionHandling().accessDeniedHandler(deniedHandler);
	}

	private void writeResult(HttpServletResponse resp, RespBean respBean) throws IOException {
		ObjectMapper om = new ObjectMapper();
		PrintWriter out = resp.getWriter();
		out.write(om.writeValueAsString(respBean));
		out.flush();
		out.close();
	}
}