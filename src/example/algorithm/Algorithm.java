package example.algorithm;

import java.security.MessageDigest;

/**
 * 常用算法类
 * @author liuchun
 *
 */
public final class Algorithm {
	
	/**
	 * 传入一个字符数组,生成字节数组的MD5结果字符串
	 * @param source
	 * @return
	 */
	public static String getMD5(byte[] source){
		String s = null;
		char hexDigits[] = {'0','1','2','3','4','5','6','7','8','9','a',
				'b','c','d','e','f'};    //用来将字节转换成16进制表示的字符
		try{
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(source);
			byte tmp[] = md.digest();    //MD5的计算结果是一个128位的长整数
			char str[] = new char[16*2];   //每个字节用16进制表示的话,使用两个字符,因此需要32个字符
			int k = 0;
			for(int i = 0; i < 16; i++){
				byte byte0 = tmp[i];  //取第i个字节
				str[k++] = hexDigits[byte0>>>4 & 0x0f];  //取字节中的高4位数字转换
				str[k++] = hexDigits[byte0 & 0x0f];    //取字节中低4位的数字转换
			}
			s = new String(str);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return s;
	}
}
