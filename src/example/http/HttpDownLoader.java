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
	public String page_dir;
	public String image_dir;
	
	public HttpDownLoader(String tags)
	{
		page_dir = tags + "Pages";
		image_dir = tags + "Images";
		
		File file = new File(page_dir);
		if(!file.exists())
			file.mkdir();
		
		file = new File(image_dir);
		if(!file.exists())
			file.mkdir();
	}
	
	/* ����ĳһ����ҳ��Դ����  */
	public String downLoadPage(String url){
		String filepath = page_dir + "/" + Algorithm.getMD5(url.getBytes()) + ".html";
		
		HttpClient httpClient = new DefaultHttpClient();
		//httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 60000);  //���ó�ʱʱ��	
		try {
			HttpGet httpGet = new HttpGet(url);
			//��������ͷ
			httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:37.0) Gecko/20100101 Firefox/37.0");
			httpGet.setHeader("Accept", "text/html");	
			//httpGet.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
			httpGet.setHeader("Accept-Encoding", "gzip,inflate");  //�������������gzip������
			//ִ��Http����
			HttpResponse response = httpClient.execute(httpGet);	
			if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){			
				HttpEntity entity = response.getEntity();
				//��ȡ��վ�ı����ַ���
				String header = response.getFirstHeader("Content-Type").getValue();
				String charset = "";
				if(header.contains("charset=")){
					charset = header.substring(header.indexOf("charset=")+"charset=".length());
				}else{
					charset = "gbk";   //Ĭ��gbk����
				}
				//��ȡ��վ���ݵ�ѹ����ʽ
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
				//��������������ȡ����
				BufferedReader br = new BufferedReader(new InputStreamReader(is, charset)); 
				int count = (int)entity.getContentLength();
				if(count < 0){
					count = 4096;
				}
				//�ļ�������
				OutputStreamWriter fos = new OutputStreamWriter(new FileOutputStream(new File(filepath)), charset);
				//����ռ�
				CharArrayBuffer buffer = new CharArrayBuffer(count);
				char[] tmp = new char[4096];   //һ�ζ�����ֽ�,�������
				int len;
				while((len = br.read(tmp)) != -1){
					buffer.append(tmp,0,len);
					fos.write(tmp, 0, len);
				}
				is.close();
				fos.close();
				String site_page = buffer.toString();
				
				//log out
				System.out.println(url);
				
				return site_page;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			httpClient.getConnectionManager().closeIdleConnections(60, TimeUnit.SECONDS);
		}
		
		return "";
	}
	
	/**
	 * ���ظ������ӵ�ͼƬ
	 * @param url
	 */
	public void downLoadImage(String url){
		String[] urlist = url.split("/");
		String filepath = image_dir + "/" + urlist[urlist.length-1];  //ͼƬ�ļ�����Ŀ¼,����url��ַ�Զ�����		
		
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet;
		try {
			httpGet = new HttpGet(url);
			httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:37.0) Gecko/20100101 Firefox/37.0");
			httpGet.setHeader("Accept", "text/html");
			//httpGet.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
			//httpGet.setHeader("Accept-Encoding", "gzip,inflate");  //�������������gzip������
			HttpResponse response = httpClient.execute(httpGet);
			if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				HttpEntity entity = response.getEntity();
				//����������
				InputStream is = entity.getContent();			
				BufferedInputStream bIn = new BufferedInputStream(is);
				//�ļ������
				FileOutputStream fos = new FileOutputStream(new File(filepath));
				//��������
				byte[] tmp = new byte[4096];
				int len;
				while((len = bIn.read(tmp)) != -1){
					fos.write(tmp, 0, len);
				}
				is.close();
				fos.close();
				
				//log out
				System.out.println(url);
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally{
			httpClient.getConnectionManager().closeIdleConnections(60, TimeUnit.SECONDS);
		}
		
	}
}
