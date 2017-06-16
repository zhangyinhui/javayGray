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

public class MsgReturn {

	//static String redir="https://www.douban.com/doumail/";    // 输入你登录成功后要跳转的网页
	//static String redir="https://www.douban.com/doumail/102694807/";
	
	public static void  getMSg(CloseableHttpClient httpClient,String redir){
		
		String newStr = new String(redir);
		newStr = newStr.substring(0,newStr.length()-1);
		int index = newStr.lastIndexOf("/");
		String lastString = newStr.substring(index+1,newStr.length());
		
		HttpGet httpGet=new HttpGet(redir);
		httpGet.setHeader("user-agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.110 Safari/537.36");
		httpGet.setHeader("Host", "www.douban.com");  
		httpGet.setHeader("Accept-Encoding","gzip");  
		httpGet.setHeader("Accept-Language","zh-CN");
		httpGet.setHeader("charset", "utf-8"); 
		httpGet.setHeader("Connection", "Keep-Alive");
		System.out.println("content-------------------------------------------------------------------------------------");
        HttpResponse response1;
		try {
			response1 = httpClient.execute(httpGet);
			if(response1.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				System.out.println("get请求失败");
				return;
			}
			HttpEntity entity1=response1.getEntity();
	        String result1=EntityUtils.toString(entity1,"utf-8");
	        
	        Document doc = Jsoup.parse(result1);  
	        Element msgcontents = doc.getElementById("content");  
	        Elements li_content =  msgcontents.getElementsByClass("content");
	        
	        for(Element i:li_content)  
  		  	{  
	        	System.out.println(i.toString());
	        	//System.out.println("------------------");
	        	
	        	String aText = i.select("a").get(0).text();  
                System.out.println(aText);
	        	
                if( i.select("p").isEmpty())
                {
                	//图片信息
                	//String img = i.select("img").first();
                	System.out.println(i.select("img").first().absUrl("src"));
                }
                else
                {
                	String pText = i.select("p").get(0).text();  
                	System.out.println(pText);
                }
	    
	        	/*Elements content=i.getElementsByTag("a");
	        	for(Element i1:content)  
      		  	{  
      		      System.out.println( i1.text()); 
      		  	}
	        	Elements contentq=i.getElementsByTag("p");
	        	for(Element i1:contentq)  
      		  	{  
      		      System.out.println( i1.text()); 
      		  	}
	        	String msgtex = i.text();
	        	System.out.println(msgtex);
	        	*/    
  		  	}
	        //System.out.println("-------------------------------------------------------------------------------");  
	        
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SendMsg(httpClient,lastString);
	}
	
	public static void SendMsg(CloseableHttpClient httpClient,String toid){
		 HttpPost post = new HttpPost("https://www.douban.com/j/doumail/send");  
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
			myentity.addPart("to",new StringBody(toid,ContentType.TEXT_PLAIN));
			myentity.addPart("m_text",new StringBody("我只是想你和我说说话了。。", ContentType.create("text/plain", CharsetUtils.get("UTF-8")))); 
            myentity.addPart("m_reply",new StringBody("回应",ContentType.TEXT_PLAIN));  
               
            HttpEntity reqEntity = myentity.build();
            post.setEntity(reqEntity);  
            HttpResponse response3 = httpClient.execute(post); 
            System.out.println(response3.getStatusLine().getStatusCode());
            if(response3.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				System.out.println("post请求失败");
				return;
			}
            HttpEntity entity1=response3.getEntity();
	        String result1=EntityUtils.toString(entity1,"utf-8");
                
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
