package example.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import example.http.Crawler;

/**
 * ҳ�����,��ȡվ���url���Ӻ�ͼƬ����
 * @author liuchun
 *
 */
public class PageParser {
	//վ�����ַ
	private static String base_url = "http://www.mzitu.com";	
	//ҳ�������Documentģ��
	private Document doc;
	private boolean isContainUrl = false;  //ҳ���Ƿ���Ч
	private boolean isContainImg = false;
	
	
	public PageParser(String url) {
		// TODO Auto-generated constructor stub
		base_url = url;
		isContainUrl = false;
		isContainImg = false;
	}
	//url��html��ʽ���ļ�
	public void parserHtml(String page) {
		// TODO Auto-generated constructor stub
		doc = Jsoup.parse(page);
		Elements links = doc.select("a[href]");  //��ȡ���г�����
		Elements imglinks = doc.select("div.m-list-content img");  //��ȡdiv class="m-list-content"�������img��ǩ
		//Elements imglinks = doc.select("div.e.m img");
		//Elements imglinks = doc.select("div.bizhiin img");
		//��������
		for(Element link:links){
			//��ȡlink
			String url = link.attr("abs:href");
			//����ͬһվ��,��û�з��ʹ�
			if(url.contains(base_url) && !Crawler.urlFilter.contains(url)){
				Crawler.urlQueue.addElem(url);  //�������
				Crawler.urlFilter.add(url);  //���뼯��
				System.out.println(url);   //��ӡurl�б�
				isContainUrl = true;			
			}
		}
		//����ͼƬ����
		for(Element link:imglinks){
			//��ȡͼƬlink
			String url = link.attr("abs:src");
			//û�з��ʹ�
			if(!Crawler.imgFilter.contains(url)){
				Crawler.imgQueue.addElem(url);
				Crawler.imgFilter.add(url);
				isContainImg = true;			
			}
		}
		//����ͬ��
		synchronized (this) {
			if(isContainUrl && isContainImg){
				Crawler.page_num++;   //����ҳ���������,�����ʴﵽһ�����,�Ͳ��ٱ���
			}
			isContainUrl = isContainImg = false;
		}		
	}
}
