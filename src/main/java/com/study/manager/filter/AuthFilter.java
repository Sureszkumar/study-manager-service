package com.study.manager.filter;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.study.manager.entity.UserEntity;
import com.study.manager.service.UserService;

@Component
public class AuthFilter implements Filter {

	private Logger log = LoggerFactory.getLogger(AuthFilter.class);
	public static final String AUTH_TOKEN = "auth-token";
	public static final String USER_ID = "user-id";

	private UserService userService;

	public AuthFilter() {

	}

	public AuthFilter(UserService userService) {
		this.userService = userService;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		/*StringBuilder sb = new StringBuilder();
	    BufferedReader reader = request.getReader();
	    try {
	        String line;
	        while ((line = reader.readLine()) != null) {
	            sb.append(line).append('\n');
	        }
	    } finally {
	        reader.close();
	    }
	    log.debug("request : ", sb.toString());*/
		String authToken = request.getHeader(AUTH_TOKEN);
		String userId = request.getHeader(USER_ID);
		log.debug("User id : {}, AuthToken : {}", userId, authToken);
		if (StringUtils.isEmpty(authToken) || StringUtils.isEmpty(userId)) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User id or Token is empty");
			return;
		}

		UserEntity userEntity = userService.findByUserIdAndAuthToken(Long.valueOf(userId), authToken);
		if (userEntity == null) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User id and Token does not match");
			return;
		}
		chain.doFilter(req, res);
	}

	@Override
	public void destroy() {
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

}