package com.jackie.crawler.doubanmovie.utils;

import java.awt.image.BufferedImage;
import com.asprise.ocr.Ocr;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

class Point{
	int x;
	int y;
	public Point(int x,int y)
	{
		this.x = x;
		this.y = y;
		
	}
}

public class Imagescan {
	

	//容错最大的有色判断
	final static  int  MAX_RGB_VALUE = 25;
	//噪点大小
	final static int MAX_NOISY_COUNT = 20;
	final static int WHITE = ((255<<16)& 0xff0000)+((255<<8)&0xff00)+(255&0xff);
	final static int BLACK = 0;
	static String pointstr = new String(""); 
	
	public static void print_char_pic(BufferedImage img){
		int width = img.getWidth(); // 得到源图宽
        int height = img.getHeight(); // 得到源图长
		 for(int i=0;i<height;i++)
	        {
	        	for(int j=0;j<width;j++)
	        	{
	        		//System.out.println(img.getRGB(i, j)+"=="+WHITE);
	        		int[] rgb = new int [3];
	        		int pixel = img.getRGB(j, i);
	        		rgb[0] = (pixel & 0xff0000) >> 16;
	                rgb[1] = (pixel & 0xff00) >> 8;
	                rgb[2] = (pixel & 0xff);
	        		if( (rgb[0]+rgb[1]+rgb[2]) == BLACK){
	        			pointstr +='*';
	        		}
	        		else {
	        			pointstr +=' ';
	        		}
	        	}
	        	pointstr +='\n';
	        }
		 System.out.println(pointstr);
	}
	public static void inner_recursion(int x, int y, ArrayList<Point> pointlist,int[][] flage_image,BufferedImage img){
		int[] rgb = new int [3];
		int pixel = img.getRGB(x, y);
		rgb[0] = (pixel & 0xff0000) >> 16;
        rgb[1] = (pixel & 0xff00) >> 8;
        rgb[2] = (pixel & 0xff);
		if (flage_image[x][y] == 0 && (rgb[0]+rgb[1]+rgb[2]) == BLACK){
			pointlist.add(new Point(x, y));
			flage_image[x][y] =1;
			recursion_scan_black_point(x,y,pointlist,flage_image,img);
		}
		else{
			flage_image[x][y] =1;
		}
	}
	public static void recursion_scan_black_point(int x, int y, ArrayList<Point> pointlist,int[][] flage_image,BufferedImage img){
		int width = img.getWidth(); // 得到源图宽
        int height = img.getHeight(); // 得到源图长
        int _x,_y;
		// 左上
	    if(0 <= (x - 1)&&(x - 1) < width && 0 <= (y - 1)&&(y - 1) < height){
	        _x = x - 1;
	        _y = y - 1;
	        inner_recursion(_x, _y, pointlist,flage_image, img);
	       }

	    // 上
	    if (0 <= (y - 1) && (y - 1)< height){
	        _x = x;
	        _y = y - 1;
	        inner_recursion(_x, _y, pointlist,flage_image, img);
	    }

	    // 右上
	    if (0 <= (x + 1) && (x + 1)< width && 0 <= (y - 1) && (y - 1)< height){
	        _x = x + 1;
	        _y = y - 1;
	        inner_recursion(_x, _y, pointlist,flage_image, img);
	    }

	    // 左
	    if (0 <= (x - 1) && (x - 1)< width){
	        _x = x - 1;
	        _y = y;
	        inner_recursion(_x, _y,  pointlist,flage_image, img);
	    }

	    // 右
	    if (0 <= (x + 1) && (x + 1)< width){
	        _x = x + 1;
	        _y = y;
	        inner_recursion(_x, _y, pointlist,flage_image, img);
	    }

	    // 左下
	    if (0 <= (x - 1) && (x - 1)< width && 0 <= (y + 1) && (y + 1)< height){
	        _x = x - 1;
	        _y = y + 1;
	        inner_recursion(_x, _y, pointlist,flage_image, img);
	    }

	    // 下
	    if (0 <= (y + 1) && (y + 1)< height){
	        _x = x;
	        _y = y + 1;
	        inner_recursion(_x, _y, pointlist,flage_image, img);
	    }

	    // 右下
	    if (0 <= (x + 1) && (x + 1)< width && 0 <= (y + 1) && (y + 1)< height){
	        _x = x + 1;
	        _y = y + 1;
	        inner_recursion(_x, _y, pointlist,flage_image, img);
	    }
	}
	
	public static void reduce_noisy(BufferedImage img){
		int width = img.getWidth(); // 得到源图宽
        int height = img.getHeight(); // 得到源图长
        int[][] flage_image = new int[width][height];
        for(int i=0;i<height;i++)
        {
        	for(int j=0;j<width;j++)
        	{
        		flage_image[j][i] = 0;
        	}
        }
        
        for(int i=0;i<height;i++)
        {
        	for(int j=0;j<width;j++)
        	{
        		int[] rgb = new int [3];
        		int pixel = img.getRGB(j, i);
        		rgb[0] = (pixel & 0xff0000) >> 16;
                rgb[1] = (pixel & 0xff00) >> 8;
                rgb[2] = (pixel & 0xff);
        		if((flage_image[j][i]==0) && (rgb[0]+rgb[1]+rgb[2]) == BLACK){
        			flage_image[j][i]=1;
        			 ArrayList<Point> pointlist = new ArrayList<Point>();
        			 pointlist.add(new Point(j, i));
        			 recursion_scan_black_point(j, i, pointlist,flage_image,img);
        			 if(pointlist.size()<MAX_NOISY_COUNT){
        				 for (int i1 = 0; i1 < pointlist.size(); i1++){
        					 img.setRGB(pointlist.get(i1).x, pointlist.get(i1).y, WHITE);
        				 }
        			 }
        		}
        		else{
        			flage_image[j][i]=1;
        		}
        	}
        }
       
		
	}
	
	public static String  getstring() throws IOException{
		File file=new File("D://jackie/jackie.png");
		BufferedImage src = ImageIO.read(file);
		int width = src.getWidth(); // 得到源图宽
        int height = src.getHeight(); // 得到源图长
        for(int i=0;i<width;i++)
        {
        	for(int j=0;j<height;j++)
        	{
        		int[] rgb = new int [3];
        		int pixel = src.getRGB(i, j);
        		rgb[0] = (pixel & 0xff0000) >> 16;
                rgb[1] = (pixel & 0xff00) >> 8;
                rgb[2] = (pixel & 0xff);
                int rgbgrv = (rgb[0]+rgb[1]+rgb[2])/3;
                //System.out.println(rgb[0]+" "+rgb[1]+" "+rgb[2]+" "+ pixel);
                //pixel= (255-rgb[0])<<16+(255-rgb[1])<<8+(255-rgb[2]);
                if(rgbgrv>Imagescan.MAX_RGB_VALUE){
                	src.setRGB(i, j, WHITE);
                }
                else{
                	src.setRGB(i, j, BLACK);
                }
        		
        	}
        }
        //Image img = null;
        File f = new File("D://jackie/yyl.png");
        ImageIO.write(src, "png", f);
        
        reduce_noisy(src);
      
        print_char_pic(src);
        f = new File("D://jackie/zyh.png");
        ImageIO.write(src, "png", f);
        
        
        Ocr.setUp(); // one time setup
        Ocr ocr = new Ocr(); // create a new OCR engine
        ocr.startEngine("eng", Ocr.SPEED_FASTEST); // English
        String s = ocr.recognize(new File[] {new File("D://jackie/zyh.png")},
          Ocr.RECOGNIZE_TYPE_ALL, Ocr.OUTPUT_FORMAT_PLAINTEXT); // PLAINTEXT | XML | PDF | RTF
        System.out.println("Result: " + s);
        ocr.stopEngine();
        
        

		System.out.println("width"+"="+width+";"+"height"+"="+height);
		return s;
	}
	
}
