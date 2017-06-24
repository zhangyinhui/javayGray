package com.jackie.crawler.doubanmovie.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Random;

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
import org.apache.http.message.BasicHeader;
import org.apache.http.util.CharsetUtils;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MsgReturn {
	
	static String[] msggo={"你好","今天天气真不错","其实我是机器人自动回复的","你是做什么的呢","可以简单介绍一下你自己吗","你是做什么工作的啊","你是哪个大学毕业的啊","对不起，我现在还不会回答问题","等我长大了就好了","不要喜欢我，我不喜欢你","不过我有点羡慕你","你是不是傻啊","不要说了，不想和你说话，再见再也不见","你是谁啊","你干嘛给我发信息","我要去吃饭了","哎，今天心情不好","你呢","88"};

	//static String redir="https://www.douban.com/doumail/";    // 输入你登录成功后要跳转的网页
	//static String redir="https://www.douban.com/doumail/102694807/";
	
	public static void  getMSg(CloseableHttpClient httpClient,String redir, int num){
		
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
		httpGet.setHeader("Cookie",MianLogin.cookie);
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
		try {
    		Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace(); 
        }
		SendMsg(httpClient,lastString,num);
	}
	
	public static void SendMsg(CloseableHttpClient httpClient,String toid,int num){
		 HttpPost post = new HttpPost("https://www.douban.com/j/doumail/send");  
         post.setHeader("user-agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.110 Safari/537.36");
         post.setHeader("Host", "www.douban.com");  
         post.setHeader("Accept-Encoding","gzip");  
         post.setHeader("Accept-Language","zh-CN");
         post.setHeader("charset", "utf-8"); 
         post.setHeader("Connection", "Keep-Alive");
         post.setHeader("Cookie",MianLogin.cookie);
         MultipartEntityBuilder myentity = MultipartEntityBuilder.create(); 
         
         try {
			myentity.setCharset(CharsetUtils.get("UTF-8")); //设置请求的编码格式
			myentity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);//设置浏览器兼容模式
			myentity.addPart("ck", new StringBody("cewS",ContentType.TEXT_PLAIN));
			myentity.addPart("to",new StringBody(toid,ContentType.TEXT_PLAIN));
			int numofstr = msggo.length -1;
			Random random = new Random();

		    int s = random.nextInt(numofstr)%(numofstr-0+1) + 0;
		       
			myentity.addPart("m_text",new StringBody(msggo[s], ContentType.create("text/plain", CharsetUtils.get("UTF-8")))); 
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
