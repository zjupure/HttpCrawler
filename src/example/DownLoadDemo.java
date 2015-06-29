package example;

import example.http.HttpDownLoader;

public class DownLoadDemo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HttpDownLoader downLoader = new HttpDownLoader();
		downLoader.downLoadPage("http://www.mzitu.com/");
		downLoader.downLoadImage("http://pic.dofay.com/tb/38709_10t19_236.jpg");
	}

}
