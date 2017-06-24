package com.jackie.crawler.doubanmovie.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.CharsetUtils;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class FastFloor {
	static String url = "https://www.douban.com/group/oneweeklove/?ref=sidebar";
	//static String url = "https://www.douban.com/group/515085/";
	//static String url = "https://www.douban.com/group/481977/";
	public static void GetMsglist(CloseableHttpClient httpClient){
		HttpGet httpGet=new HttpGet(url);
		httpGet.setHeader("user-agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.110 Safari/537.36");
		httpGet.setHeader("Host", "www.douban.com");  
		httpGet.setHeader("Accept-Encoding","gzip");  
		httpGet.setHeader("Accept-Language","zh-CN");
		httpGet.setHeader("charset", "utf-8"); 
		httpGet.setHeader("Connection", "Keep-Alive");
		System.out.println("listone-------------------------------------------------------------------------------------");
        HttpResponse response1;
		try {
			response1 = httpClient.execute(httpGet);
			if(response1.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				System.out.println("get请求失败");
				return;
			}
			HttpEntity entity1=response1.getEntity();
	        String result1=EntityUtils.toString(entity1,"utf-8");
	        System.out.println(result1);
	        System.out.println("list-------------------------------------------------------------------------------------");
	        Document doc = Jsoup.parse(result1);  
	        Elements msgcontents = doc.getElementsByClass("olt").select("tr");
	        //Elements msgcontents = doc.select("table").select("tr"); 
	        
	        for(Element i:msgcontents)  
  		  	{
	        	int n =0;
	        	//System.out.println(i.toString());
	        	//System.out.println("------------------");
	        	
	        	//String linkHref = i.select("a").get(0).attr("href");  
	        	System.out.println(n++ + ": "+i.toString());
	        	if(i.select("a").isEmpty())
	        	{
	        		continue;
	        	}
	        	String aText = i.select("a").get(0).attr("href");
	        	String title = i.select("a").get(0).attr("title");
	        	String Text = i.select("a").get(1).text();
	        	String num = i.select("td").get(2).text();
                System.out.println(aText+" "+title+" "+Text +" "+ num);
                
                String tourlid = aText + "add_comment";
                add_comment(httpClient,tourlid,Text);
                try {
            		Thread.sleep(40000);
                } catch (InterruptedException e) {
                    e.printStackTrace(); 
                }
	        	//System.out.println(i.text());
	        	/*Elements content=i.getElementsByTag("a");
	        	for(Element i1:content)  
      		  	{  
      		      String linkHref = i1.attr("href");  
      		      MsgReturn.getMSg(httpClient, linkHref);
      		      System.out.println( linkHref); 

      		  	}*/
  		  	}
	        
	        //System.out.println("-------------------------------------------------------------------------------");  
    	    
	        //System.out.println(result1);  
	        
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("listone222-------------------------------------------------------------------------------------");
	}
	
	public static void add_comment(CloseableHttpClient httpClient,String toidurl,String commont){
//		ck:cewS
//		rv_comment:你好
//		start:0
//		submit_btn:加上去
//		https://www.douban.com/group/topic/103764045/add_comment
		
		HttpPost post = new HttpPost(toidurl);  
        post.setHeader("user-agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.110 Safari/537.36");
        post.setHeader("Host", "www.douban.com");  
        post.setHeader("Accept-Encoding","gzip");  
        post.setHeader("Accept-Language","zh-CN");
        post.setHeader("charset", "utf-8"); 
        post.setHeader("Connection", "Keep-Alive");
        MultipartEntityBuilder myentity = MultipartEntityBuilder.create(); 
        
        try {
			myentity.setCharset(CharsetUtils.get("UTF-8")); //设置请求的编码格式
			myentity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);//设置浏览器兼容模式
			myentity.addPart("ck", new StringBody("cewS",ContentType.TEXT_PLAIN));
			myentity.addPart("rv_comment",new StringBody("你好，"+commont, ContentType.create("text/plain", CharsetUtils.get("UTF-8"))));
			myentity.addPart("start",new StringBody("0",ContentType.TEXT_PLAIN)); 
           myentity.addPart("submit_btn",new StringBody("加上去",ContentType.TEXT_PLAIN));  
              
           HttpEntity reqEntity = myentity.build();
           post.setEntity(reqEntity);  
           HttpResponse response3 = httpClient.execute(post); 
           System.out.println(response3.getStatusLine().getStatusCode());
           HttpEntity entity1=response3.getEntity();
	       String result1;
	       
           if(response3.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				System.out.println("post请求失败");
				result1 = EntityUtils.toString(entity1,"utf-8");
				 System.out.println(result1);
				return;
			}
           result1 = EntityUtils.toString(entity1,"utf-8");
	       System.out.println(result1);
           //HttpEntity entity212=response3.getEntity();
           //String result212=EntityUtils.toString(entity212,"utf-8");
		
        } catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("send-------------------------------------------------------------------------------------");
	}
}
