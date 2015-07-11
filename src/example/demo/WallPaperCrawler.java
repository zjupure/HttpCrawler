package example.demo;

import example.http.Crawler;

public class WallPaperCrawler extends Crawler {
	private static String base_url = "http://wordsmotivate.me";
	private static String base_img = "http://img.wordsmotivate.me";
	private static String site_tag = "WallPaper";
	
	private static String img_lable = "div.post_img img";
	
	
	public WallPaperCrawler() {
		// TODO Auto-generated constructor stub
		super(base_url, base_img, site_tag);
		
		setImageLable(img_lable);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		WallPaperCrawler mCrawler = new WallPaperCrawler();
		
		mCrawler.execute();
	}

}
