package example.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 数据库连接帮助类
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
	
	//构造方法
	public SqlHelper() {
		// TODO Auto-generated constructor stub
		String creatTable = "CREATE TABLE IF NOT EXISTS save_url(id int NOT NULL AUTO_INCREMENT,"
				+ "name varchar(100) NOT NULL,"
				+ "type char(10) NOT NULL,"
				+ "url varchar(255) NOT NULL,"
				+ "PRIMARY KEY (id))";
		connSQL();   //连接数据库
		updateSQL(creatTable);   //第一次构造时自动创建一个表
	}
	
	/**
	 * 连接数据库
	 */
	public void connSQL(){
		String driver = "com.mysql.jdbc.Driver";  //驱动名
		String url = "jdbc:mysql://localhost:3306/spider?characterEncoding=UTF-8";  //连接地址
		String username = "root";
		String password = "0618pure";   
		//加载驱动程序以连接数据库
		try{
			Class.forName(driver);
			conn = DriverManager.getConnection(url, username, password);
		}catch(ClassNotFoundException e){   //捕获加载驱动异常
			// TODO: handle exception
			System.err.println("装载 JDBC/ODBC 驱动程序失败");
			e.printStackTrace();
		}catch (SQLException e) {   //捕获连接数据库异常
			// TODO: handle exception
			System.err.println("无法连接数据库");
			e.printStackTrace();
		}
	}
	
	/**
	 * 关闭数据库连接
	 */
	public void disConnSQL(){
		try{
			if(conn != null){
				conn.close();
			}
		}catch(Exception e){
			System.out.println("关闭数据库出现问题");
			e.printStackTrace();
		}
	}
	
	/**
	 * 执行SQL SELECT语句,返回结果集
	 * @param sql
	 * @return
	 */
	public ResultSet selectSQL(String sql){
		ResultSet rs = null;
		try{
			//线程同步
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
	 * 用于执行INSERT,UPDATE或DELETE语句以及SQL DDL语句
	 * 如 CREATE TABLE和DROP TABLE等
	 * @param sql
	 * @return
	 */
	public int updateSQL(String sql){
		int res = 0;
		try{
			//线程同步
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
