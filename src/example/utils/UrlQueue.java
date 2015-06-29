package example.utils;

import java.util.LinkedList;

/**
 * Url����,�������û�з��ʹ���URL����
 * @author liuchun
 *
 */
public class UrlQueue {
	//���������ĳ���������
	public static final int MAX_SIZE = 10000;
	//�����Ӷ���
	public LinkedList<String> urlQueue = new LinkedList<String>();
	
	//���
	public  void addElem(String url){
		synchronized (this) {
			urlQueue.add(url);
		}
	}
	//����
	public  String outElem(){
		synchronized (this) {
			return urlQueue.removeFirst();
		}
		
	}
	
	//�п�
	public  boolean isEmpty(){
		synchronized (this) {
			return urlQueue.isEmpty();
		}
		
	}
	//���ض��еĳ���
	public  int size(){
		synchronized (this) {
			return urlQueue.size();
		}
		
	}
	
}
