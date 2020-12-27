package com.ecom.demo.api.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ecom.demo.api.utils.ApplicationUtils;
import com.ecom.demo.db.model.User;
import com.ecom.demo.db.service.MyUserDetailsService;
import com.ecom.demo.db.service.UserDbService;

import io.jsonwebtoken.ExpiredJwtException;

@Component
public class RequestAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private MyUserDetailsService userDetailsService;
	
	@Autowired
	private UserDbService userDbService;
	
	@Autowired
	private ServletContext servletContext;

	@Autowired
	private ApplicationUtils tokenUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		final String requestTokenHeader = request.getHeader("Authorization");
		String username = null;
		String jwtToken = null;
		// GET ONLY TOKEN PART FROM AUTHORIZATION HEADER
		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			jwtToken = requestTokenHeader.substring(7);
			try {
				username = tokenUtil.getUsernameFromToken(jwtToken);
				List<User> users = userDbService.getUser(username);
				if(!CollectionUtils.isEmpty(users)) {
					User user = users.get(0);
					servletContext.setUser(user);
				}
			} catch (IllegalArgumentException e) {
				System.out.println("Unable to get JWT Token");
			} catch (ExpiredJwtException e) {
				System.out.println("JWT Token has expired");
			}
		} else {
			logger.warn("JWT Token does not begin with Bearer String");
		}
		// ONCE WE GET THE TOKEN VALIDATE IT.
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
				if (tokenUtil.validateToken(jwtToken, userDetails)) {
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, new ArrayList<>());
				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		}
		chain.doFilter(request, response);
	}
}
