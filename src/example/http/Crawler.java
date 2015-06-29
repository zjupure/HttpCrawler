package example.http;

import java.io.File;

import example.sql.SqlHelper;
import example.utils.PageParser;
import example.utils.SimpleBloomFilter;
import example.utils.UrlQueue;

public class Crawler {
	private final static String base_url = "http://www.mzitu.com";
	//private final static String base_url = "http://www.topit.me";
	//private final static String base_url = "http://www.zhuoku.com";
	//链接队列,共享数据
	public static UrlQueue urlQueue = new UrlQueue();  //超链接的队列
	public static UrlQueue imgQueue = new UrlQueue();  //图片地址的队列
	//bloom filter
	public static SimpleBloomFilter urlFilter = new SimpleBloomFilter();
	public static SimpleBloomFilter imgFilter = new SimpleBloomFilter();
	//爬虫搜索深度,页面个数
	public static volatile int page_num = 0;
	//数据库对象
	public static SqlHelper mSqlHelper = new SqlHelper();
	//文件存储路径
	public static String urlPath = "DownLoaderPages";
	public static String imgPath = "DoanLoaderImages";
	
	/* 测试 main方法 */
	public static void main(String[] args){
		//建立文件存储目录
		File file = new File(urlPath);file.mkdir();
		file = new File(imgPath);file.mkdir();
		//开始爬取
		Crawler mCrawler = new Crawler();
		mCrawler.execute();
	}
	
	
	public void execute(){
		//先把站点首页下载下来,并解析出来,保证队列中有内容
		new PageParser(base_url).parserHtml(new HttpDownLoader().downLoadPage(base_url));
		//建立任务并启动线程
		for(int i = 1; i <= 2; i++){
			UrlCrawlerTask urlTask = new UrlCrawlerTask();
			Thread urlThread = new Thread(urlTask,"urlTask" + i);
			urlThread.start();
			
			ImgCrawlerTask imgTask = new ImgCrawlerTask();
			Thread imgThread = new Thread(imgTask,"ImgTask" + i);
			imgThread.start();			
		}
	}
	
	/* 下载Url任务 */
	public class UrlCrawlerTask implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			//队列不为空,且没有达到搜索深度
			while(!urlQueue.isEmpty()){
				String url = urlQueue.outElem();  //取出链接
				new PageParser(base_url).parserHtml(new HttpDownLoader().downLoadPage(url));		
			}
		}
		
	}
	/* 下载图片任务类  */
	public class ImgCrawlerTask implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			//超链接队列不为空,一直循环等待图片队列获取数据位置
			while(!urlQueue.isEmpty()){
				//当图片队列不为空,开始下载任务
				while(!imgQueue.isEmpty()){
					String url = imgQueue.outElem();
					new HttpDownLoader().downLoadImage(url);
				}
			}
		}
		
	}
}
