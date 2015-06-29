package example;

import java.util.ArrayList;

public class HttpDemo {
	
	public static void main(String[] args){
		// TODO Auto-generated method stub
		ArrayList<String>  mArrayList = new ArrayList<String>();
		
		for(int i = 1; i <= 10; i++)
			mArrayList.add(i + "hello");
		
		//Integer xInteger;		
		for(String s : mArrayList)
		{
			s += "world";
			System.out.println(s);
		}
			
	}
	
	/**
	 * 
	 * @param start  出发城市
	 * @param end    到达城市
	 * @param date   乘车时间
	 * @param type   乘客类型
	 * @return   余票数
	 */
	public int checkLeftTickets(String start, String end, String date, boolean type){
		
		
		return 0;
	}
}
