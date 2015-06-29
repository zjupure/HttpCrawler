package example.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import example.http.Crawler;

/**
 * 页面解析,获取站点的url链接和图片链接
 * @author liuchun
 *
 */
public class PageParser {
	//站点基地址
	private static String base_url = "http://www.mzitu.com";	
	//页面解析的Document模型
	private Document doc;
	private boolean isContainUrl = false;  //页面是否有效
	private boolean isContainImg = false;
	
	
	public PageParser(String url) {
		// TODO Auto-generated constructor stub
		base_url = url;
		isContainUrl = false;
		isContainImg = false;
	}
	//url是html格式的文件
	public void parserHtml(String page) {
		// TODO Auto-generated constructor stub
		doc = Jsoup.parse(page);
		Elements links = doc.select("a[href]");  //获取所有超链接
		Elements imglinks = doc.select("div.m-list-content img");  //获取div class="m-list-content"块下面的img标签
		//Elements imglinks = doc.select("div.e.m img");
		//Elements imglinks = doc.select("div.bizhiin img");
		//处理链接
		for(Element link:links){
			//获取link
			String url = link.attr("abs:href");
			//属于同一站点,且没有访问过
			if(url.contains(base_url) && !Crawler.urlFilter.contains(url)){
				Crawler.urlQueue.addElem(url);  //加入队列
				Crawler.urlFilter.add(url);  //加入集合
				System.out.println(url);   //打印url列表
				isContainUrl = true;			
			}
		}
		//处理图片链接
		for(Element link:imglinks){
			//获取图片link
			String url = link.attr("abs:src");
			//没有访问过
			if(!Crawler.imgFilter.contains(url)){
				Crawler.imgQueue.addElem(url);
				Crawler.imgFilter.add(url);
				isContainImg = true;			
			}
		}
		//加锁同步
		synchronized (this) {
			if(isContainUrl && isContainImg){
				Crawler.page_num++;   //访问页面深度增加,当访问达到一定深度,就不再遍历
			}
			isContainUrl = isContainImg = false;
		}		
	}
}
