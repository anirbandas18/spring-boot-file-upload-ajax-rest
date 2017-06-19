package com.mkyong.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {
	
	private static final Logger logger = LoggerFactory.getLogger(IndexController.class);
    
	@GetMapping("/upload")
    public String index(@RequestParam("baseDir") String baseDir, HttpServletRequest request) {
    	if(StringUtils.isEmpty(baseDir)) {
    		return "error";
    	} else {
    		HttpSession session = request.getSession();
    		session.setAttribute("baseDir", baseDir);
    		String id = session.getId();
    		
    		logger.info(id + " " + baseDir);
    		return "upload";
    	}
    }
	
    @GetMapping("/")
    public String index() {
    	return "error";
    }

}