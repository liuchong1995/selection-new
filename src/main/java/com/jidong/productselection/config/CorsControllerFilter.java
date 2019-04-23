package com.jidong.productselection.config;

/**
 * @Author: LiuChong
 * @Date: 2018/12/4 16:26
 * @Description:
 */

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
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
		// 跨域白名单
		String[] whiteList = {"http://localhost","http://localhost:9888", "http://192.168.1.99"};
		String myOrigin = request.getHeader("origin");
		boolean isValid = false;
		for( String ip : whiteList ) {
			if( myOrigin != null && myOrigin.equals(ip) ){
				isValid = true;
				break;
			}
		}
		response.setHeader("Access-Control-Allow-Origin", isValid ? myOrigin : "null");
		response.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE ,PUT");
		response.addHeader("Access-Control-Max-Age", "30");
		response.addHeader("Access-Control-Allow-Headers", "Origin, No-Cache, X-Requested-With, If-Modified-Since,"
				+ "Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-Token,"
						+ "X-E4M-With,userId,token,Authorization,deviceId,Access-Control-Allow-Origin,Access-Control-Allow-Headers,Access-Control-Allow-Methods");
		response.addHeader("Access-Control-Allow-Credentials", "true");
		response.addHeader("XDomainRequestAllowed", "1");

		chain.doFilter(request, response);

	}

    @SuppressWarnings("unchecked")
    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        CorsControllerFilter corsControllerFilter = new CorsControllerFilter();
        registrationBean.setFilter(corsControllerFilter);
        return registrationBean;
    }

}

