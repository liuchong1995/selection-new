package com.jidong.productselection.config;

/**
 * @Auther: LiuChong
 * @Date: 2018/12/4 16:26
 * @Description:
 */

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 跨域过滤器
 *
 */
/**
 * 跨域过滤器
 *
 */
@Component
public class CorsControllerFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE ,PUT");
		response.addHeader("Access-Control-Max-Age", "30");
		response.addHeader("Access-Control-Allow-Headers", "Origin, No-Cache, X-Requested-With, If-Modified-Since,"
				+ "Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-Token,"
						+ "X-E4M-With,userId,token,Authorization,deviceId,Access-Control-Allow-Origin,Access-Control-Allow-Headers,Access-Control-Allow-Methods");
		response.addHeader("Access-Control-Allow-Credentials", "true");
		response.addHeader("XDomainRequestAllowed", "1");

		chain.doFilter(request, response);

	}

}

