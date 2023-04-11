package com.test.common.filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import com.alibaba.fastjson.JSON;

@Slf4j
@WebFilter
public class ReqFilter implements Filter{
	public ReqFilter() {
		log.info("init reFilter");
	}
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException{}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,ServletException{
		HttpServletRequest req=(HttpServletRequest)request;
		log.info("url={}, params={}",req.getRequestURI(),JSON.toJSONString(req.getParameterMap()));
		chain.doFilter(req, response);//不执行这一句，则访问被拦截
	}
	
	@Override
	public void destroy() {
		log.info("filter 销毁");
	}
}
