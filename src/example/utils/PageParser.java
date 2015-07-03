package example.utils;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import example.http.Crawler;

/**
 * 页面解析,获取站点的url链接和图片链接
 * @author zjupure
 *
 */
public class PageParser {
	//页面解析的Document模型
	private Document doc;	
	
	/* 构造函数  */
	public PageParser(String page) {
		// TODO Auto-generated constructor stub
		doc = Jsoup.parse(page);
	}
	
	/* 设置页面源代码 */
	public void setPage(String page)
	{
		doc = Jsoup.parse(page);
	}
	
	/**
	 * 获取页面内的超链接
	 * @return
	 */
	public ArrayList<String> getSuperLink()
	{
		ArrayList<String>  urlList = new ArrayList<String>(); 
		
		Elements links = doc.select("a[href]");  // 获取所有的超链接
		
		for(Element link: links)
		{
			String url = link.attr("abs:href");
			urlList.add(url);
		}
		
		return urlList;
	}
	
	/**
	 * 获取特定标签下的图片链接
	 * @param attr
	 * @return
	 */
	public ArrayList<String> getImageLink(String attr)
	{
		ArrayList<String>  urlList = new ArrayList<String>(); 
		
		Elements links = doc.select(attr);  // 获取所有标签
		
		for(Element link: links)
		{
			String url = link.attr("abs:src");
			urlList.add(url);
		}
		
		return urlList;
	}
	
	/**
	 * 获取特定标签下的文本
	 * @param attr
	 * @return
	 */
	public ArrayList<String> getText(String attr)
	{
		ArrayList<String>  urlList = new ArrayList<String>(); 
		
		Elements links = doc.select(attr);  // 获取所有标签
		
		for(Element link: links)
		{
			String url = link.text();
			urlList.add(url);
		}
		
		return urlList;
	}
}
