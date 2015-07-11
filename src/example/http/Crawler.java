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
	//���Ӷ���,
	public  UrlQueue urlQueue = new UrlQueue();  //�����ӵĶ���
	public  UrlQueue imgQueue = new UrlQueue();  //ͼƬ��ַ�Ķ���
	//bloom filter
	public  SimpleBloomFilter urlFilter = new SimpleBloomFilter();
	public  SimpleBloomFilter imgFilter = new SimpleBloomFilter();
	//�����������,ҳ�����
	public  volatile int page_num = 0;
	//���ݿ����
	public  SqlHelper mSqlHelper;
	//����������
	public  HttpDownLoader mDownLoader;
	//ҳ�������
	public  PageParser mPageParser;
	//�ض���ǩ
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
	 * ִ���߳� 
	 */
	public void execute(){
		//�Ȱ�վ����ҳ��������,����������,��֤������������
		String site_src = mDownLoader.downLoadPage(base_url);
		mPageParser = new PageParser(site_src);
		//��������
		AddSuperLink();
		AddImageLink(image_lable);
		//�������������߳�
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
	 * ���ñ�ǩ����
	 * @param lable
	 */
	public void setImageLable(String lable)
	{
		image_lable = lable;
	}
	
	/**
	 * ��ӳ�����
	 */
	public void AddSuperLink()
	{
		ArrayList<String> urlList = mPageParser.getSuperLink();
		
		for(String url:urlList)
		{
			if(url.contains(base_url) && !urlFilter.contains(url))
			{
				urlQueue.addElem(base_url + url);
				urlFilter.add(base_url + url);
				//log out
				System.out.println(url);
			}
		}
	}
	
	/**
	 * ���ͼƬ����
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
	 *  ����Url���� 
	 */
	public class UrlCrawlerTask implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			//���в�Ϊ��,��û�дﵽ�������
			while(!urlQueue.isEmpty()){
				String url = urlQueue.outElem();  //ȡ������
				String site_src = mDownLoader.downLoadPage(url);
				mPageParser.setPage(site_src);
				AddSuperLink();
				AddImageLink(image_lable);
			}
		}
		
	}
	/**
	 *  ����ͼƬ������  
	 * 
	 */
	public class ImgCrawlerTask implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			//�����Ӷ��в�Ϊ��,һֱѭ���ȴ�ͼƬ���л�ȡ����λ��
			while(!urlQueue.isEmpty()){
				//��ͼƬ���в�Ϊ��,��ʼ��������
				while(!imgQueue.isEmpty()){
					String url = imgQueue.outElem();
					mDownLoader.downLoadImage(url);
				}
			}
		}
		
	}
}
