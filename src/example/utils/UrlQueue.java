package example.utils;

import java.util.LinkedList;

/**
 * Url队列,用来存放没有访问过的URL队列
 * @author liuchun
 *
 */
public class UrlQueue {
	//队列中最多的超链接数量
	public static final int MAX_SIZE = 10000;
	//超链接队列
	public LinkedList<String> urlQueue = new LinkedList<String>();
	
	//入队
	public  void addElem(String url){
		synchronized (this) {
			urlQueue.add(url);
		}
	}
	//出队
	public  String outElem(){
		synchronized (this) {
			return urlQueue.removeFirst();
		}
		
	}
	
	//判空
	public  boolean isEmpty(){
		synchronized (this) {
			return urlQueue.isEmpty();
		}
		
	}
	//返回队列的长度
	public  int size(){
		synchronized (this) {
			return urlQueue.size();
		}
		
	}
	
}
