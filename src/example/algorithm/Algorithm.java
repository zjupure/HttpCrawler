package example.algorithm;

import java.security.MessageDigest;

/**
 * �����㷨��
 * @author liuchun
 *
 */
public final class Algorithm {
	
	/**
	 * ����һ���ַ�����,�����ֽ������MD5����ַ���
	 * @param source
	 * @return
	 */
	public static String getMD5(byte[] source){
		String s = null;
		char hexDigits[] = {'0','1','2','3','4','5','6','7','8','9','a',
				'b','c','d','e','f'};    //�������ֽ�ת����16���Ʊ�ʾ���ַ�
		try{
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(source);
			byte tmp[] = md.digest();    //MD5�ļ�������һ��128λ�ĳ�����
			char str[] = new char[16*2];   //ÿ���ֽ���16���Ʊ�ʾ�Ļ�,ʹ�������ַ�,�����Ҫ32���ַ�
			int k = 0;
			for(int i = 0; i < 16; i++){
				byte byte0 = tmp[i];  //ȡ��i���ֽ�
				str[k++] = hexDigits[byte0>>>4 & 0x0f];  //ȡ�ֽ��еĸ�4λ����ת��
				str[k++] = hexDigits[byte0 & 0x0f];    //ȡ�ֽ��е�4λ������ת��
			}
			s = new String(str);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return s;
	}
}
