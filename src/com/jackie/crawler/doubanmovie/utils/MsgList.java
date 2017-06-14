package com.jackie.crawler.doubanmovie.utils;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MsgList {
	static String redir="https://www.douban.com/doumail/unread";    // 输入你登录成功后要跳转的网页
	//static String redir="https://www.douban.com/doumail/102694807/";
	
	public static void  getMSg( CloseableHttpClient httpClient){
		HttpGet httpGet=new HttpGet(redir);
		httpGet.setHeader("user-agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.110 Safari/537.36");
		httpGet.setHeader("Host", "www.douban.com");  
		httpGet.setHeader("Accept-Encoding","gzip");  
		httpGet.setHeader("Accept-Language","zh-CN");
		httpGet.setHeader("charset", "utf-8"); 
		
        HttpResponse response1;
		try {
			response1 = httpClient.execute(httpGet);
			HttpEntity entity1=response1.getEntity();
	        String result1=EntityUtils.toString(entity1,"utf-8");
	        
	        Document doc = Jsoup.parse(result1);  
	        Element msgcontents = doc.getElementById("content");  
	        Elements li_content =  msgcontents.getElementsByClass("title");
	        
	        for(Element i:li_content)  
  		  	{  
	        	System.out.println(i.toString());
	        	System.out.println("------------------");
	    
	        	for(int j=0; j<i.childNodeSize(); j++)
	        	{
	        		System.out.println(" "+j+" "+ i.childNode(j));
	        	}
	        	//System.out.println(i.childNodeSize());
	        	String linkHref = i.attr("href");
	        	System.out.println(linkHref);  
	        	//System.out.println(i.text());    
  		  	}
	        
	        System.out.println("-------------------------------------------------------------------------------");  
    	    
	        System.out.println(result1);  
	        
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
