package example.http;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.CharArrayBuffer;

import example.algorithm.Algorithm;

public class HttpDownLoader {
	//网站入口地址,base url
	private static String base_url = "http://www.mzitu.com/";
	private String site_page = "";
	//SQL插入语句
	private static String format = "INSERT INTO save_url "
				+ "(name,type,url) "
				+ "VALUES(\'%s\',\'%s\',\'%s\')";
	
	
	public HttpDownLoader() {
		// TODO Auto-generated constructor stub
		this(base_url);
	}
	
	public HttpDownLoader(String url) {
		// TODO Auto-generated constructor stub
		base_url = url;     //网站基址
	}	
	
	/* 下载某一个网页的源代码  */
	public String downLoadPage(String url){
		String filename = Crawler.urlPath + "/" + Algorithm.getMD5(url.getBytes()) + ".html";
		
		HttpClient httpClient = new DefaultHttpClient();
		//httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 60000);  //设置超时时间	
		try {
			HttpGet httpGet = new HttpGet(url);
			//设置请求头
			httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:37.0) Gecko/20100101 Firefox/37.0");
			httpGet.setHeader("Accept", "text/html");	
			//httpGet.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
			httpGet.setHeader("Accept-Encoding", "gzip,inflate");  //允许服务器发送gzip的数据
			//执行Http请求
			HttpResponse response = httpClient.execute(httpGet);	
			if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){			
				HttpEntity entity = response.getEntity();
				//获取网站的编码字符集
				String header = response.getFirstHeader("Content-Type").getValue();
				String charset = "";
				if(header.contains("charset=")){
					charset = header.substring(header.indexOf("charset=")+"charset=".length());
				}else{
					charset = "gbk";   //默认gbk编码
				}
				//获取网站内容的压缩方式
				String contentcode = ""; 
				InputStream is;
				if(response.getFirstHeader("Content-Encoding") != null){
					contentcode = response.getFirstHeader("Content-Encoding").getValue();
					if(contentcode.equalsIgnoreCase("gzip")){
						is = new GZIPInputStream(entity.getContent());
					}else{
						is = entity.getContent();
					}
				}else{
					is = entity.getContent();
				}		
				//从网络输入流读取数据
				BufferedReader br = new BufferedReader(new InputStreamReader(is, charset)); 
				int count = (int)entity.getContentLength();
				if(count < 0){
					count = 4096;
				}
				//文件输入流
				OutputStreamWriter fos = new OutputStreamWriter(new FileOutputStream(new File(filename)), charset);
				//缓存空间
				CharArrayBuffer buffer = new CharArrayBuffer(count);
				char[] tmp = new char[4096];   //一次读多个字节,提高速率
				int len;
				while((len = br.read(tmp)) != -1){
					buffer.append(tmp,0,len);
					fos.write(tmp, 0, len);
				}
				is.close();
				fos.close();
				site_page = buffer.toString();	
				//下载过的网页插入数据库
				String sql = String.format(format, filename,"url",url);
				Crawler.mSqlHelper.updateSQL(sql);
				
				return site_page;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			httpClient.getConnectionManager().closeIdleConnections(60, TimeUnit.SECONDS);
		}
		
		return site_page;
	}
	
	/**
	 * 下载给定链接的图片
	 * @param url
	 */
	public void downLoadImage(String url){
		String[] urlist = url.split("/");
		String filename = Crawler.imgPath + "/" + urlist[urlist.length-1];  //图片文件保存目录,根据url地址自动生成		
		
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet;
		try {
			httpGet = new HttpGet(url);
			httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:37.0) Gecko/20100101 Firefox/37.0");
			httpGet.setHeader("Accept", "text/html");
			//httpGet.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
			//httpGet.setHeader("Accept-Encoding", "gzip,inflate");  //允许服务器发送gzip的数据
			HttpResponse response = httpClient.execute(httpGet);
			if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				HttpEntity entity = response.getEntity();
				//网络输入流
				InputStream is = entity.getContent();			
				BufferedInputStream bIn = new BufferedInputStream(is);
				//文件输出流
				FileOutputStream fos = new FileOutputStream(new File(filename));
				//缓存数组
				byte[] tmp = new byte[4096];
				int len;
				while((len = bIn.read(tmp)) != -1){
					fos.write(tmp, 0, len);
				}
				is.close();
				fos.close();
				
				//下载过的图片插入数据库
				String sql = String.format(format, filename,"image",url);
				Crawler.mSqlHelper.updateSQL(sql);
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally{
			httpClient.getConnectionManager().closeIdleConnections(60, TimeUnit.SECONDS);
		}
		
	}
}
