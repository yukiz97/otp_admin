package ngn.otp_admin.services;


import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;

import ngn.otp_admin.utils.OTPProps;
import oracle.ucp.UniversalConnectionPoolAdapter;
import oracle.ucp.admin.UniversalConnectionPoolManager;
import oracle.ucp.admin.UniversalConnectionPoolManagerImpl;
import oracle.ucp.jdbc.PoolDataSource;
import oracle.ucp.jdbc.PoolDataSourceFactory;

public class ConnectionService implements ServletContextListener{
	public static MysqlConnectionPoolDataSource ds = null;
	public static PoolDataSource pdsMySQL = PoolDataSourceFactory.getPoolDataSource();

	@Override
	public void contextInitialized(ServletContextEvent sce) {

		// TODO Auto-generated method stub
//		createDataSource();
		createUCPDatasourceForMySQL();

	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		System.out.println("Destroy context");
		try {
			UniversalConnectionPoolManager mgr = UniversalConnectionPoolManagerImpl.getUniversalConnectionPoolManager();
			mgr.stopConnectionPool("JDBC_UCP_MYSQL");
			mgr.destroyConnectionPool("JDBC_UCP_MYSQL");
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void createDataSource() {
		try {
			System.out.println("Tao datasource");
//			OTPProps props=new OTPProps();
			String url=OTPProps.getProperty("jdbc.default.server.url");
			String userName=OTPProps.getProperty("jdbc.default.username");
			String password=OTPProps.getProperty("jdbc.default.password");
			ds=new MysqlConnectionPoolDataSource();
			ds.setURL(url);
			ds.setUser(userName);
			ds.setPassword(password);

			//			ds.setConnectTimeout(10000);
			System.out.println("Tao datasource thanh cong");

		} catch (Exception e) {
			System.out.println("Khong the tao datasource");
		}
	}

	public static void createUCPDatasourceForMySQL() {
		try {
//			OTPProps props=new OTPProps();
			String url=OTPProps.getProperty("jdbc.default.server.url");
			String userName=OTPProps.getProperty("jdbc.default.username");
			String password=OTPProps.getProperty("jdbc.default.password");
			String className = OTPProps.getProperty("jdbc.default.driverclassname");
			System.out.println("====="+className+"=====");
			pdsMySQL.setConnectionFactoryClassName(className);
			pdsMySQL.setURL(url);
			pdsMySQL.setUser(userName);
			pdsMySQL.setPassword(password);
			pdsMySQL.setConnectionPoolName("JDBC_UCP_MYSQL");
			pdsMySQL.setInitialPoolSize(5);
			pdsMySQL.setMinPoolSize(1);
			pdsMySQL.setMaxPoolSize(10);

			pdsMySQL.setMaxIdleTime(1);
			
			pdsMySQL.setConnectionWaitTimeout(40);
			pdsMySQL.setTimeToLiveConnectionTimeout(300);
			
			UniversalConnectionPoolManager mgr = UniversalConnectionPoolManagerImpl.getUniversalConnectionPoolManager();
			mgr.createConnectionPool((UniversalConnectionPoolAdapter)pdsMySQL);
			mgr.startConnectionPool("JDBC_UCP_MYSQL");
			System.out.println("Tao UCP datasource thanh cong");
		} catch (Exception e) {
			System.out.println("Khong the tao UCP datasource");
			e.printStackTrace();
		}

	}

}
