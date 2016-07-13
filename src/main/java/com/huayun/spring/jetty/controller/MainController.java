package com.huayun.spring.jetty.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/main/test")
public class MainController {

	
	@RequestMapping("/hello.do")
	public void sayHello(HttpServletRequest request,HttpServletResponse response){
		
		System.out.println("hello");
		
	}
	
	
}
