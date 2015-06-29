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
	//���Ӷ���,��������
	public static UrlQueue urlQueue = new UrlQueue();  //�����ӵĶ���
	public static UrlQueue imgQueue = new UrlQueue();  //ͼƬ��ַ�Ķ���
	//bloom filter
	public static SimpleBloomFilter urlFilter = new SimpleBloomFilter();
	public static SimpleBloomFilter imgFilter = new SimpleBloomFilter();
	//�����������,ҳ�����
	public static volatile int page_num = 0;
	//���ݿ����
	public static SqlHelper mSqlHelper = new SqlHelper();
	//�ļ��洢·��
	public static String urlPath = "DownLoaderPages";
	public static String imgPath = "DoanLoaderImages";
	
	/* ���� main���� */
	public static void main(String[] args){
		//�����ļ��洢Ŀ¼
		File file = new File(urlPath);file.mkdir();
		file = new File(imgPath);file.mkdir();
		//��ʼ��ȡ
		Crawler mCrawler = new Crawler();
		mCrawler.execute();
	}
	
	
	public void execute(){
		//�Ȱ�վ����ҳ��������,����������,��֤������������
		new PageParser(base_url).parserHtml(new HttpDownLoader().downLoadPage(base_url));
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
	
	/* ����Url���� */
	public class UrlCrawlerTask implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			//���в�Ϊ��,��û�дﵽ�������
			while(!urlQueue.isEmpty()){
				String url = urlQueue.outElem();  //ȡ������
				new PageParser(base_url).parserHtml(new HttpDownLoader().downLoadPage(url));		
			}
		}
		
	}
	/* ����ͼƬ������  */
	public class ImgCrawlerTask implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			//�����Ӷ��в�Ϊ��,һֱѭ���ȴ�ͼƬ���л�ȡ����λ��
			while(!urlQueue.isEmpty()){
				//��ͼƬ���в�Ϊ��,��ʼ��������
				while(!imgQueue.isEmpty()){
					String url = imgQueue.outElem();
					new HttpDownLoader().downLoadImage(url);
				}
			}
		}
		
	}
}
