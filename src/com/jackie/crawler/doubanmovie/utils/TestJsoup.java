package com.jackie.crawler.doubanmovie.utils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;  
import org.jsoup.select.Elements;

public class TestJsoup {
	
	public static void main(String[] args) {  
	      
	       
	      
	    
	      
	    String content ="<html xmlns=\"http://www.w3.org/1999/xhtml\"><head>  <meta http-equiv=\"Cache-Control\" content=\"no-siteapp\" /><link rel=\"alternate\" media=\"handheld\" href=\"#\" /><title>直到世界的尽头- 博客频道 - CSDN.NET</title><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /><meta name=\"description\" content=\"\" /><meta name=\"keywords\" content=\"\" />\"; <script src=\"http://static.blog.csdn.net/scripts/blog_static_head.min.js\" type=\"text/javascript\"></script>";
	//  System.out.println(content);  
	    Document doc = Jsoup.parse(content);  
	    String title = doc.title();  
	    System.out.println(title);  
	      
	    Element a=doc.getElementById("panel_Profile");  
	      
	    Elements li_content=a.getElementsByTag("li");  
	  
	    //提取方式一:  
	//  for(Element i:li_content)  
	//  {  
//	      String tag=i.tagName();  
//	      if(tag.equals("li"))  
//	      {  
//	          System.out.println(i.text());  
//	            
//	      }  
//	        
//	        
	//  }  
	      
	    //提取方式二:  
	  for(Element i:li_content)  
	  {  
	      String tag=i.tagName();  
	      if(tag.equals("li"))  
	      {  
	          String fre_content= i.ownText();  
	        
	         System.out.println(fre_content);  
	         if(i.children().size()>0&&i.children()!=null)  
	         {  
	         String after_content=i.child(0).text();  
	         System.out.println(after_content);  
	         }  
	      }  
//	        
//	        
	//  }  
	      
	      
//	    //提取方式三:  
//	    for(Element i:li_content)  
//	    {  
//	        String tag=i.tagName();  
//	        if(tag.equals("li"))  
//	        {  
//	            String all_content=i.text();  
//	            String all_string=Jsoup.clean(all_content, Whitelist.none());  
//	        String[] all=all_string.split("：");        
//	           if(all.length>0&&all!=null)  
//	           {  
//	               String fre_content=all[0].toString();  
//	               System.out.println(fre_content);  
//	           String after_content=all[1].toString();  
//	           System.out.println(after_content);  
//	           }  
//	        }  
	          
	          
	    }  
	      
	      
	      
	}  

}
