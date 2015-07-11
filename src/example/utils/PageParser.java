package example.utils;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import example.http.Crawler;

/**
 * ҳ�����,��ȡվ���url���Ӻ�ͼƬ����
 * @author zjupure
 *
 */
public class PageParser {
	//ҳ�������Documentģ��
	private Document doc;	
	
	/* ���캯��  */
	public PageParser(String page) {
		// TODO Auto-generated constructor stub
		doc = Jsoup.parse(page);
	}
	
	/* ����ҳ��Դ���� */
	public void setPage(String page)
	{
		doc = Jsoup.parse(page);
	}
	
	/**
	 * ��ȡҳ���ڵĳ�����
	 * @return
	 */
	public ArrayList<String> getSuperLink()
	{
		ArrayList<String>  urlList = new ArrayList<String>(); 
		
		Elements links = doc.select("a[href]");  // ��ȡ���еĳ�����
		
		for(Element link: links)
		{
			String url = link.attr("href");
			if(!url.startsWith("http") && !url.startsWith("https"))
				urlList.add(url);
		}
		
		return urlList;
	}
	
	/**
	 * ��ȡ�ض���ǩ�µ�ͼƬ����
	 * @param attr
	 * @return
	 */
	public ArrayList<String> getImageLink(String attr)
	{
		ArrayList<String>  urlList = new ArrayList<String>(); 
		
		Elements links = doc.select(attr);  // ��ȡ���б�ǩ
		
		for(Element link: links)
		{
			String url = link.attr("abs:src");
			urlList.add(url);
		}
		
		return urlList;
	}
	
	/**
	 * ��ȡ�ض���ǩ�µ��ı�
	 * @param attr
	 * @return
	 */
	public ArrayList<String> getText(String attr)
	{
		ArrayList<String>  urlList = new ArrayList<String>(); 
		
		Elements links = doc.select(attr);  // ��ȡ���б�ǩ
		
		for(Element link: links)
		{
			String url = link.text();
			urlList.add(url);
		}
		
		return urlList;
	}
}
