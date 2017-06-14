package com.jackie.crawler.doubanmovie.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.CharsetUtils;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MianLogin {
	private static CloseableHttpClient httpClient = HttpClientBuilder.create().build();
	
	 //主登录入口
    public static void loginDouban(){
        String redir="https://www.douban.com/doumail/";    // 输入你登录成功后要跳转的网页
        String login_src="https://accounts.douban.com/login";
        String form_email="1316637851@qq.com";    // 你的用户名
        String form_password="zyh19920412db";    // 你的密码
        String captcha_id=getImgID();
        System.out.println(captcha_id);
        String login="登录";
        String captcha_solution = "";
		/*try {
			captcha_solution = Imagescan.getstring();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
        System.out.println("请输入验证码：");
        BufferedReader buff=new BufferedReader(new InputStreamReader(System.in));
        try {
            captcha_solution=buff.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //构建参数
        List<NameValuePair> list=new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("redir", redir));
        list.add(new BasicNameValuePair("form_email", form_email));
        list.add(new BasicNameValuePair("form_password", form_password));
        list.add(new BasicNameValuePair("captcha-solution", captcha_solution));
        list.add(new BasicNameValuePair("captcha-id", captcha_id));
        list.add(new BasicNameValuePair("login", login));
        HttpPost httpPost = new HttpPost(login_src);
        httpPost.setHeader("user-agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.110 Safari/537.36");
        httpPost.setHeader("Host", "www.douban.com");  
        httpPost.setHeader("Accept-Encoding","gzip");  
        httpPost.setHeader("Accept-Language","zh-CN");
        httpPost.setHeader("charset", "utf-8"); 
        
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(list));
            HttpResponse response=httpClient.execute(httpPost);
            HttpEntity entity=response.getEntity();
            String result=EntityUtils.toString(entity,"utf-8");

            while(true){
            	
            	MsgList.getMSg(httpClient);
                System.out.println("请输入获取网址：");
                BufferedReader wz=new BufferedReader(new InputStreamReader(System.in));
                redir = wz.readLine();
                
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	/**
     * 获取验证码图片“token”值
     * @return token
     */
    private static String getImgID(){
        String src="https://www.douban.com/j/misc/captcha";
        HttpGet httpGet=new HttpGet(src);
        httpGet.setHeader("user-agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.110 Safari/537.36");
        httpGet.setHeader("Host", "www.douban.com");  
        httpGet.setHeader("Accept-Encoding","gzip");  
        httpGet.setHeader("Accept-Language","zh-CN");
        httpGet.setHeader("charset", "utf-8"); 
        
        String token="";
        try {
            HttpResponse response=httpClient.execute(httpGet);
            HttpEntity entity=response.getEntity();
            String content=EntityUtils.toString(entity,"utf-8");
            Map<String,String> mapList=getResultList(content);
            token=mapList.get("token");
            String url="https:"+mapList.get("url");
            downImg(url);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return token;
    }
    /**
     * 用JSON 把数据格式化，并生成迭代器，放入Map中返回
     * @param content 请求验证码时服务器返回的数据
     * @return Map集合
     */
    public static Map<String, String> getResultList(String content){
        Map<String, String> maplist=new HashMap<String, String>();

        try {
            JSONObject jo=new JSONObject(content);
            Iterator it = jo.keys();
            String key="";
            String value="";
            while(it.hasNext()){
                key=(String) it.next();
                if ("r".equals(key)) {
                    value = jo.getBoolean(key) + "";
                }else {
                    value=jo.getString(key);
                }
                maplist.put(key, value);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return maplist;
    }
    /**
     * 此方法是下载验证码图片到本地
     * @param src  给个验证图片完整的地址
     */
    private static void downImg(String src){
        File fileDir=new File("D://jackie");
        if(!fileDir.exists()){
            fileDir.mkdirs();
        }
        File file=new File("D://jackie/jackie.png");
        if(file.exists()){
            file.delete();
        }
        InputStream input = null;
        FileOutputStream out= null;
        HttpGet httpGet=new HttpGet(src);
        httpGet.setHeader("user-agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.110 Safari/537.36");
        httpGet.setHeader("Host", "www.douban.com");  
        httpGet.setHeader("Accept-Encoding","gzip");  
        httpGet.setHeader("Accept-Language","zh-CN");
        httpGet.setHeader("charset", "utf-8"); 
        try {
            HttpResponse response=httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            input = entity.getContent();
            int i=-1;
            byte[] byt=new byte[1024];
            out=new FileOutputStream(file);
            while((i=input.read(byt))!=-1){
                out.write(byt);
            }
            System.out.println("图片下载成功！");
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String arg[]){
    	 loginDouban();
    }

}
