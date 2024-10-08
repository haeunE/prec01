package com.example.demo.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;

import com.example.demo.domain.User;

public class AuthInterceptor implements HandlerInterceptor{

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//로그인 확인
		HttpSession session = request.getSession(); //request에서 session 가져오기
		User principal = (User)session.getAttribute("principal");//object 형은 부모객체타입이므로 형변환을 해 줘야한다.
		
		if(principal == null) {
			response.sendRedirect("/auth/login");
		}
		return true;//정상 작동 할것인지 T/F
	}
	
}
