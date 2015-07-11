package example.http;

import java.util.ArrayList;

import example.sql.SqlHelper;
import example.utils.PageParser;
import example.utils.SimpleBloomFilter;
import example.utils.UrlQueue;

public class Crawler {
	private String base_url;
	private String base_img;
	private String site_tags;
	//链接队列,
	public  UrlQueue urlQueue = new UrlQueue();  //超链接的队列
	public  UrlQueue imgQueue = new UrlQueue();  //图片地址的队列
	//bloom filter
	public  SimpleBloomFilter urlFilter = new SimpleBloomFilter();
	public  SimpleBloomFilter imgFilter = new SimpleBloomFilter();
	//爬虫搜索深度,页面个数
	public  volatile int page_num = 0;
	//数据库对象
	public  SqlHelper mSqlHelper;
	//网络下载器
	public  HttpDownLoader mDownLoader;
	//页面解析器
	public  PageParser mPageParser;
	//特定标签
	public String image_lable;
	
	public Crawler(String url, String domain, String tags)
	{
		base_url = url;
		base_img = domain;
		site_tags = tags;
		
		mSqlHelper = new SqlHelper();
		mDownLoader = new HttpDownLoader(site_tags);
	}
	
	/**
	 * 执行线程 
	 */
	public void execute(){
		//先把站点首页下载下来,并解析出来,保证队列中有内容
		String site_src = mDownLoader.downLoadPage(base_url);
		mPageParser = new PageParser(site_src);
		//处理链接
		AddSuperLink();
		AddImageLink(image_lable);
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
	
	/**
	 * 设置标签属性
	 * @param lable
	 */
	public void setImageLable(String lable)
	{
		image_lable = lable;
	}
	
	/**
	 * 添加超链接
	 */
	public void AddSuperLink()
	{
		ArrayList<String> urlList = mPageParser.getSuperLink();
		
		for(String url:urlList)
		{
			if(!urlFilter.contains(url))
			{
				urlQueue.addElem(base_url + url);
				urlFilter.add(url);
				//log out
				System.out.println(url);
			}
		}
	}
	
	/**
	 * 添加图片链接
	 * @param attr
	 */
	public void AddImageLink(String attr)
	{
		ArrayList<String> urlList = mPageParser.getImageLink(attr);
		
		for(String url:urlList)
		{
			if(url.contains(base_img) && !imgFilter.contains(url))
			{
				imgQueue.addElem(url);
				imgFilter.add(url);
				//log out
				System.out.println(url);
			}
		}
	}
	
	/**
	 *  下载Url任务 
	 */
	public class UrlCrawlerTask implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			//队列不为空,且没有达到搜索深度
			while(!urlQueue.isEmpty()){
				String url = urlQueue.outElem();  //取出链接
				String site_src = mDownLoader.downLoadPage(url);
				mPageParser.setPage(site_src);
				AddSuperLink();
				AddImageLink(image_lable);
			}
		}
		
	}
	/**
	 *  下载图片任务类  
	 * 
	 */
	public class ImgCrawlerTask implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			//超链接队列不为空,一直循环等待图片队列获取数据位置
			while(!urlQueue.isEmpty()){
				//当图片队列不为空,开始下载任务
				while(!imgQueue.isEmpty()){
					String url = imgQueue.outElem();
					mDownLoader.downLoadImage(url);
				}
			}
		}
		
	}
}
