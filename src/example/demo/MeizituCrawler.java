package example.demo;

import example.http.Crawler;

/**
 * 针对具体网址的爬虫
 * @author zjupure
 *
 */

public class MeizituCrawler extends Crawler {
	private static String base_url = "http://www.mzitu.com";
	private static String base_domain = "mzitu.com";
	private static String site_tag = "Meizitu";
	
	private static String img_lable = "div.list img";
	
	public MeizituCrawler() {
		// TODO Auto-generated constructor stub
		super(base_url, base_domain, site_tag);
		
		setImageLable(img_lable);
	}
	
	/* 测试main方法 */
	public static void main(String[] args)
	{
		MeizituCrawler mCrawler = new MeizituCrawler();
		
		mCrawler.execute();
	}		
}
