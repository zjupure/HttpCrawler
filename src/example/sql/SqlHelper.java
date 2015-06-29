package example.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * ���ݿ����Ӱ�����
 * @author liuchun
 *
 */
public class SqlHelper {
	private Connection conn = null;
	PreparedStatement statement = null;
	private static String format = "INSERT INTO save_url "
			+ "(name,type,url) "
			+ "VALUES(\'%s\',\'%s\',\'%s\')";
	
	public static void main(String[] args){
		SqlHelper helper = new SqlHelper();
		String sql = String.format(format, "abc","url","http://www.meizitu.com");
		System.out.println(sql);
		helper.updateSQL(sql);
	}
	
	//���췽��
	public SqlHelper() {
		// TODO Auto-generated constructor stub
		String creatTable = "CREATE TABLE IF NOT EXISTS save_url(id int NOT NULL AUTO_INCREMENT,"
				+ "name varchar(100) NOT NULL,"
				+ "type char(10) NOT NULL,"
				+ "url varchar(255) NOT NULL,"
				+ "PRIMARY KEY (id))";
		connSQL();   //�������ݿ�
		updateSQL(creatTable);   //��һ�ι���ʱ�Զ�����һ����
	}
	
	/**
	 * �������ݿ�
	 */
	public void connSQL(){
		String driver = "com.mysql.jdbc.Driver";  //������
		String url = "jdbc:mysql://localhost:3306/spider?characterEncoding=UTF-8";  //���ӵ�ַ
		String username = "root";
		String password = "0618pure";   
		//���������������������ݿ�
		try{
			Class.forName(driver);
			conn = DriverManager.getConnection(url, username, password);
		}catch(ClassNotFoundException e){   //������������쳣
			// TODO: handle exception
			System.err.println("װ�� JDBC/ODBC ��������ʧ��");
			e.printStackTrace();
		}catch (SQLException e) {   //�����������ݿ��쳣
			// TODO: handle exception
			System.err.println("�޷��������ݿ�");
			e.printStackTrace();
		}
	}
	
	/**
	 * �ر����ݿ�����
	 */
	public void disConnSQL(){
		try{
			if(conn != null){
				conn.close();
			}
		}catch(Exception e){
			System.out.println("�ر����ݿ��������");
			e.printStackTrace();
		}
	}
	
	/**
	 * ִ��SQL SELECT���,���ؽ����
	 * @param sql
	 * @return
	 */
	public ResultSet selectSQL(String sql){
		ResultSet rs = null;
		try{
			//�߳�ͬ��
			synchronized (this) {
				statement = conn.prepareStatement(sql);
				rs = statement.executeQuery(sql);
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		return rs;
	}
	
	/**
	 * ����ִ��INSERT,UPDATE��DELETE����Լ�SQL DDL���
	 * �� CREATE TABLE��DROP TABLE��
	 * @param sql
	 * @return
	 */
	public int updateSQL(String sql){
		int res = 0;
		try{
			//�߳�ͬ��
			synchronized (this) {
				statement = conn.prepareStatement(sql);
				res = statement.executeUpdate(sql);
			}		
		}catch(SQLException  e){
			e.printStackTrace();
		}
		
		return res;
	}
	
}
