package com.Crawling.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.Crawling.Service.CrawService;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	@Autowired
	private CrawService csvc;

	@RequestMapping(value="/prdSearch")
	public ModelAndView prdSearch(String searchText) throws IOException {
		System.out.println("CONTROLLER - /prdSearch");
		ModelAndView mav = new ModelAndView();
		System.out.println("searchText : " + searchText);
		
		ArrayList<HashMap<String, String>> prdList_11st = csvc.getPrdList_11st(searchText);
		mav.addObject("prdList_11st",prdList_11st);
		ArrayList<HashMap<String, String>> prdList_coopang = csvc.getPrdList_coopang(searchText);
		mav.addObject("prdList_coopang", prdList_coopang);
		ArrayList<HashMap<String, String>> prdList_gmarket = csvc.getPrdList_gmarket(searchText);
		mav.addObject("prdList_gmarket", prdList_gmarket);
		mav.setViewName("PrdSearchResult");
		return mav;
	}
	
	@RequestMapping(value="olive")
	public ModelAndView olive() throws IOException {
		ModelAndView mav = new ModelAndView();
		System.out.println("Controller - 올리브영");
		ArrayList<HashMap<String,String>> prdList = csvc.getOliveRankItem();
		
		mav.addObject("prdList", prdList);
		mav.setViewName("OliveBest");
		
		return mav;
		
	}
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home() {
		System.out.println("메인 페이지 이동");
		
		return "home";
	}
}
