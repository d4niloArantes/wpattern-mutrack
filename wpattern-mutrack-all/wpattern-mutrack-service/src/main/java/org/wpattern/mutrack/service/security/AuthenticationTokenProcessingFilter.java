package org.wpattern.mutrack.service.security;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import org.wpattern.mutrack.service.security.properties.SecurityProperties;

@Component
public class AuthenticationTokenProcessingFilter extends GenericFilterBean {

	private static final String TOKEN_HEADER = "X-Auth-Token";

	@Inject
	private UserDetailsService userService;

	@Inject
	private SecurityProperties securityProperties;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = this.getAsHttpRequest(request);
		String authToken = httpRequest.getHeader(TOKEN_HEADER);
		String userName = this.getUserNameFromToken(authToken);

		// CORS
		HttpServletResponse httpResp = (HttpServletResponse) response;
		httpResp.setHeader("Access-Control-Allow-Origin", "*");
		httpResp.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		httpResp.setHeader("Access-Control-Max-Age", "3600");
		httpResp.setHeader("Access-Control-Allow-Headers", "*");

		if (userName != null) {
			UserDetails userDetails = this.userService.loadUserByUsername(userName);

			if (TokenUtils.validateToken(authToken, userDetails, this.securityProperties.getMagickey())) {
				UsernamePasswordAuthenticationToken authentication =
						new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));

				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}

		chain.doFilter(request, response);
	}

	private String getUserNameFromToken(String authToken) {
		if ((authToken == null) || authToken.trim().isEmpty()) {
			return null;
		}

		return authToken.split(":")[0];
	}

	private HttpServletRequest getAsHttpRequest(ServletRequest request) {
		if (!(request instanceof HttpServletRequest)) {
			throw new RuntimeException("Expecting an HTTP request");
		}

		return (HttpServletRequest) request;
	}

}
