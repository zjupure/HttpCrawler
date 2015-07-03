package example.utils;

import java.util.BitSet;

/**
 * Bloom Filter��Javaʵ��
 * ��ͳ��Bloom Filter��֧�ִӼ�����ɾ����Ա
 * Counting Bloom Filter���ڲ����˼���,���֧��remove����
 * ����BitSetʵ��,�����Ͽ��ܴ�������
 * @author zjupure
 *
 */
public class SimpleBloomFilter {
	//DEFAULT_SIZEΪ2��25�η�
	private static final int DEFAULT_SIZE = 2<<24;
	/* ��ͬ��ϣ����������,һ��Ӧȡ����,seeds���ݹ���7��ֵ,��������7�ֲ�ͬ��HASH�㷨*/
	private static final int[] seeds = new int[]{5,7,11,13,31,37,61};
	//BitSetʵ�������ɡ�������λ�����ɵ�һ��Vector.����ϣ����Ч�ʵر������0-1��Ϣ,��Ӧʹ��BitSet
	//BitSet����С������һ��������(Long)�ĳ���:64λ
	private BitSet bits = new BitSet(DEFAULT_SIZE);
	/* ��ϣ�������� */
	private SimpleHash[] func = new SimpleHash[seeds.length];
	
	/* ����main���� */
	public static void main(String[] args){
		String value = "stone2083@yahoo.cn";
		//����һ��filter,�����ʱ�����ù��캯��,��ʼ����ϣ���������������Ϣ
		SimpleBloomFilter filter = new SimpleBloomFilter();
		//�ж��Ƿ����������
		System.out.println(filter.contains(value));
		filter.add(value);
		System.out.println(filter.contains(value));
	}
	
	//���캯��
	public SimpleBloomFilter() {
		// TODO Auto-generated constructor stub
		for(int i = 0; i < seeds.length; i++){
			//�������е�Hashֵ,����seeds.length��Hashֵ,������Ϊ7λ
			//ͨ������SimpleHash.hash(),���Եõ�����7��hash��������ó���hashֵ
			//����DEFAULT_SIZE(�����ַ����ĳ���),seeds[i]���ɵõ���Ҫ���Ǹ�hashֵ��λ��
			func[i] = new SimpleHash(DEFAULT_SIZE,seeds[i]);
		}
	}
	
	//���ַ�����ǵ�bits��,�������ַ�����7��hash����ֵ��Ӧλ��Ϊ1
	public void add(String value){
		for(SimpleHash f:func){
			bits.set(f.hash(value), true);
		}
	}
	
	//�ж��ַ����Ƿ��Ѿ���bits���
	public boolean contains(String value){
		//ȷ������Ĳ��ǿ�ֵ
		if(value == null)
			return false;
		
		boolean ret = true;
		//����7��hash�㷨�¸��Զ�Ӧ��hashֵ,���ж�
		for(SimpleHash f:func){
			//&&���߼������,ֻҪ��һ��Ϊ0,��Ϊ0;����Ҫ���е�λ��Ϊ1,�Ŵ������������
			//f.hash(value)����hash��Ӧ��λ��ֵ
			//bits.get��������bitset�ж�Ӧ��position��ֵ,������hashֵ�Ƿ�Ϊ0��1
			ret = ret && bits.get(f.hash(value));
		}
		
		return ret;
	}
	
	/* ��ϣ������*/
	public static class SimpleHash{
		//capΪDEFAULT_SIZE��ֵ,�����ڽ��������ַ����ĳ���
		//seedΪ����hashֵ��һ��������key,�����Ӧ�����seeds����
		private int cap;
		private int seed;
		
		//���캯��
		public SimpleHash(int cap, int seed){
			this.cap = cap;
			this.seed = seed;
		}
		
		//����hashֵ�ľ����㷨,hash����,���ü򵥵ļ�Ȩ��hash
		public int hash(String value){
			int result = 0;
			int len = value.length();
			for(int i = 0; i < len; i++){
				//���ֺ��ַ������,�ַ���ת��ΪASCII��
				result = seed*result + value.charAt(i);
			}
			//&��λ�߼�����,���ڹ��˸���
			return (cap-1)&result;
		}
	}
}
