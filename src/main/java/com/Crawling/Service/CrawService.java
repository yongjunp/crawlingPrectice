package com.Crawling.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;

@Service
public class CrawService {

	public ArrayList<HashMap<String,String>> getOliveRankItem() throws IOException {
		System.out.println("Service - 올리브영 랭킹 아이템 수집 기능 호출");
		// Jsoup
		//1. https://www.oliveyoung.co.kr/store/main/getBestList.do 접속
		String oliveRankUrl = "https://www.oliveyoung.co.kr/store/main/getBestList.do";
		//2. 랭킹 페이지 문서 리턴 <HTML> ~ <HTML>
		Document oliveRankDoc = Jsoup.connect(oliveRankUrl).get();
//		System.out.println(oliveRankDoc);
		
		//3. 필요한 정보가 있는 부분(태그,요소) 선택<CSS선택자>
		Elements itemsDiv = oliveRankDoc.select("div.TabsConts"); //div선택
		
		Elements items = itemsDiv.get(0).select("ul.cate_prd_list li");//0번 인덱스 지정후 ul태그 중 cate_prd_list 안에 li태그 선택
		System.out.println(items.get(0));
		System.out.println(items.size());
		
		//4. 데이터를 수집
		// 브랜드명, 상품이름, 상품가격, 상품이미지
		// 상품리뷰수 : 상품 상세페이지 이동 >> 리뷰수 수집
		
		// 상세 페이지 URL
		ArrayList<HashMap<String,String>> prdList = new ArrayList<HashMap<String,String>>();
		
		for(int i = 0; i < items.size(); i++) {
			HashMap<String, String> prd_map = new HashMap<String, String>();
			String imgUrl = items.get(i).select("div.prd_info>a>img").attr("src");
			prd_map.put("prdImg", imgUrl);
			System.out.println("상품 이미지 : " + imgUrl);
			String brandName = items.get(i).select("span.tx_brand").text(); 
			prd_map.put("prdBrd", brandName);   
			System.out.println("브랜드명 : " + brandName); 
			String prdName = items.get(i).select("p.tx_name").text();
			prd_map.put("prdName", prdName);
			System.out.println("상품이름 : " + prdName);
			String prdPrice = items.get(i).select("span.tx_cur>span.tx_num").text();
			prd_map.put("prdPrice", prdPrice);
			System.out.println("상품가격 : " + prdPrice); 		  
			
			  
			// 상세페이지 URL
			String detailUrl = items.get(i).select("div.prd_info>a").attr("href");
			// 상세페이지 Document
			Document detatilDoc = Jsoup.connect(detailUrl).get();
			// #repReview > em			
			String reviewCount = detatilDoc.select("#repReview > em").text();
			reviewCount = reviewCount.replace("(", "").replace(")", "").replace(",", "");
			prd_map.put("prdRev", reviewCount);
			System.out.println("리뷰 수 : " + reviewCount);
			System.out.println(prd_map);
			prdList.add(prd_map);
		  }
		//5. DB에 저장
		
		return prdList;
	}

	public ArrayList<HashMap<String, String>> getPrdList_11st(String searchText){
		System.out.println("service - getPrdList_11st(String searchText)");
		ChromeOptions chromeOptions = new ChromeOptions();
	    chromeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
	    chromeOptions.addArguments("headless");
	    WebDriver driver = new ChromeDriver(chromeOptions);

	    String connectUrl = "https://search.11st.co.kr/Search.tmall?kwd="+searchText;
        
        driver.get(connectUrl);
        List<WebElement> items = driver.findElements(By.cssSelector("section.search_section>ul.c_listing>li>div.c_card"));
        ArrayList<HashMap<String, String>> prdList_11st = new ArrayList<HashMap<String, String>>();
        for(WebElement item : items) {
        	HashMap<String, String> prdInfo = new HashMap<String, String>();
        	prdInfo.put("prdSite", "11st");
        	try {
        		String name = item.findElement(By.cssSelector("div.c_prd_name strong")).getText();
        		prdInfo.put("name", name);
        		String url = item.findElement(By.cssSelector("div.c_prd_name a")).getAttribute("href");
        		prdInfo.put("url", url);
        		String price = item.findElement(By.cssSelector("div.c_prd_price dd> span.value")).getText();
        		price = price.replace(",", "");
        		prdInfo.put("price", price);				
			} catch (Exception e) {
				continue;
			}
        	prdList_11st.add(prdInfo);
        	
        }
        
        driver.quit();
		return prdList_11st;
	}

	public ArrayList<HashMap<String, String>> getPrdList_coopang(String searchText) throws IOException {
		System.out.println("service - getPrdList_coopang(t)");
		//접속할 페이지
		//https://www.coupang.com/np/search?component=&q=keyboard&channel=user
		String connecturl = "https://www.coupang.com/np/search";
		HashMap<String,String> paramList = new HashMap<String, String>();
		paramList.put("component", "");
		paramList.put("q", searchText);
		paramList.put("channel", "user");
		Document targetPage = Jsoup.connect(connecturl).data(paramList).cookie("auth","token").get();
		
		Elements items = targetPage.select("li.search-product");
		//상품 이름, 상품가격 수집, 상세페이지URL 수집
		ArrayList<HashMap<String, String>> prdList_coopang = new ArrayList<HashMap<String, String>>();
		for(int i = 0; i<items.size();i++) {
			HashMap<String, String> prdInfo = new HashMap<String, String>();
			String name = items.get(i).select("div.descriptions-inner>div.name").text();
			prdInfo.put("name", name);
			String price = items.get(i).select("div.descriptions-inner>div.price-area strong.price-value").text();
			price = price.replace(",", "");
			prdInfo.put("price", price);
			String url = "https://www.coupang.com"+items.get(i).select("a.search-product-link").attr("href");
			prdInfo.put("url", url);
			prdInfo.put("prdSite", "coopang");
			
			//상품을 가격순 정렬 ( 높은 가격 ~ 낮은 가격 )
			int idx = -1; // prdList_coopang에 상품을 추가할 index 번
			String sortOption = "PRICE_DECS"; // PRICE_ACS;
			for(int k=0;k<prdList_coopang.size();k++) {
				int prdPrice_int = Integer.parseInt(price);
				int listPrice = Integer.parseInt(prdList_coopang.get(k).get("price"));
				boolean sortType = false;
				switch(sortOption) {
				case "PRICE_DECS":
					sortType = prdPrice_int > listPrice;
					break;
				case "PRICE_ACS":
					sortType = prdPrice_int < listPrice;
					break;
				}
				if(sortType) {
					idx=k;
					break;
				}
			}
			if(idx>-1) {
				prdList_coopang.add(idx,prdInfo);
			}else {
				prdList_coopang.add(prdInfo);									
			}
			
		}
		System.out.println(prdList_coopang);
		return prdList_coopang;
	}

	public ArrayList<HashMap<String, String>> getPrdList_gmarket(String searchText) throws IOException {
		System.out.println("service - getPrdList_gmarket()실행");
		//https://browse.gmarket.co.kr/search?keyword=keyboard
		Document targetUrl = Jsoup.connect("https://browse.gmarket.co.kr/search").data("keyword", searchText).get();
		Elements items = targetUrl.select("div.box__component-itemcard");
		ArrayList<HashMap<String, String>> prdList_gmarket = new ArrayList<HashMap<String,String>>();
		for(int i=0;i<items.size();i++) {
			HashMap<String, String> prdInfo = new HashMap<String, String>();
			String name = items.get(i).select("div.box__information-major span.text__item").text();
			prdInfo.put("name", name);
			String price = items.get(i).select("div.box__information-major strong.text__value").text();
			price = price.replace(",", "");
			prdInfo.put("price", price);
			String url = items.get(i).select("div.box__information-major a.link__item").attr("href");
			prdInfo.put("url", url);
			
			prdInfo.put("prdSite", "gmarket");
			
			prdList_gmarket.add(prdInfo);
		}
		System.out.println(prdList_gmarket);
		return prdList_gmarket;
	}

}
