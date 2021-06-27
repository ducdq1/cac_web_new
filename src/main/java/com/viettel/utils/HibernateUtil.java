package com.viettel.utils;

import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.xml.sax.SAXException;

import com.viettel.voffice.config.Config;

public class HibernateUtil {

	// private static Logger log = WFUtil.getLogger(HibernateUtil.class);
	private static HashMap<String, SessionFactory> sessionFactories = new HashMap<String, SessionFactory>();
	private static String dbcConfigFile = "";
	private static Config dbcConfig = new Config();

	@SuppressWarnings("static-access")
	private static void loadEncryptedDBConfig(Configuration config, String filePath) {
		
		config.setProperty("hibernate.connection.url",ResourceBundleUtil.getString("dbConnection"));
		config.setProperty("hibernate.connection.username",ResourceBundleUtil.getString("dbUser"));
		config.setProperty("hibernate.connection.password",ResourceBundleUtil.getString("dbPassword"));
		 
	}

	public static void startup() {
		LogUtils.addLog("Khởi tạo HibernateUtil");
	}

	public static void shutdown() {
		LogUtils.addLog("Destroy HibernateUtil");
		for (String key : getSessionFactories().keySet()) {
			((SessionFactory) getSessionFactories().get(key)).close();
		}
	}

	public static HashMap<String, SessionFactory> getSessionFactories() {
		return sessionFactories;
	}

	public static SessionFactory getSessionFactory(String sessionName) {
		return (SessionFactory) getSessionFactories().get(sessionName.toLowerCase());
	}

	public static SessionFactory getSessionFactory() {
		return (SessionFactory) getSessionFactories().get("default session");
	}

	public static HashMap<String, Session> getCurrentSessions() {
		HashMap<String, Session>  sessions = new HashMap<String, Session> ();
		for (String key : getSessionFactories().keySet()) {
			sessions.put(key, ((SessionFactory) getSessionFactories().get(key)).getCurrentSession());
		}
		return sessions;
	}

	public static Session getSessionAndBeginTransaction() {
		return getSessionAndBeginTransaction("default session");
	}

	public static Session getSessionAndBeginTransaction(int transTimeout) {
		return getSessionAndBeginTransaction("default session", transTimeout);
	}

	public static Session getSessionAndBeginTransaction(String sessionName) {
		if (getSessionFactory(sessionName) == null) {
			LogUtils.addLog(
					String.format("Không tồn tại hibernate session ứng với key +'%s'", new Object[] { sessionName }));
			return null;
		}
		Session session = getSessionFactory(sessionName).getCurrentSession();
		session.beginTransaction();
		return session;
	}

	public static Session getSessionAndBeginTransaction(String sessionName, int transTimeout) {
		if (getSessionFactory(sessionName) == null) {
			LogUtils.addLog(
					String.format("Không tồn tại hibernate session ứng với key +'%s'", new Object[] { sessionName }));
			return null;
		}
		Session session = getSessionFactory(sessionName).getCurrentSession();
		session.getTransaction().setTimeout(transTimeout);
		session.getTransaction().begin();

		return session;
	}

	public static HashMap<String, Session> commitCurrentSessions() throws Exception {
		HashMap<String, Session> sessions = getCurrentSessions();

		HashMap<String, Session>  sessionsToRollBack = new HashMap<String, Session> ();
		boolean hasExceptionDuringCommit = false;

		for (String sessionName : sessions.keySet()) {
			Session session = (Session) sessions.get(sessionName);
			if (session.isOpen()) {
				Transaction t = session.getTransaction();

				if ((t.isActive()) && (!hasExceptionDuringCommit)) {
					try {
						t.commit();
					} catch (Exception ex) {
						hasExceptionDuringCommit = true;
						sessionsToRollBack.put(sessionName, session);
						LogUtils.addLog(ex);
					}
				} else if (hasExceptionDuringCommit) {
					sessionsToRollBack.put(sessionName, session);
				}
			}
		}
		return sessionsToRollBack;
	}

	public static void rollBackSessions(HashMap<String, Session> sessionsToRollBack) {
		if (sessionsToRollBack != null) {
			for (String sessionName : sessionsToRollBack.keySet()) {
				Session session = (Session) sessionsToRollBack.get(sessionName);
				if (session.isOpen()) {
					Transaction t = session.getTransaction();
					try {
						t.rollback();
					} catch (Exception ex) {
						LogUtils.addLog(ex);
						if (("Transaction not successfully started").equals(ex.getMessage())) {
							LogUtils.addLog("Session " + sessionName + " không rollback do chưa được khởi tạo");
						} else {
							LogUtils.addLog("Có lỗi xảy ra khi rollback session " + sessionName);
						}
					}
				}
			}
		}
	}

	public static void closeCurrentSessions() {
		HashMap sessionMaps = getCurrentSessions();
		if (sessionMaps != null) {
			for (Object key : sessionMaps.keySet()) {
				Session session = (Session) sessionMaps.get(key);
				try {
					if (session.isOpen()) {
						session.close();
					}
				} catch (Exception ex) {
					LogUtils.addLog(ex);
					LogUtils.addLog("Lỗi khi đóng session \"" + key + "\"");
				}
			}
		}
	}

	static {
		try {
			
			dbcConfigFile = "/com/viettel/config/database/DBCConfig.xml";
			dbcConfigFile = ResourceBundleUtil.getDBCConfigFileLocation();
			dbcConfig.loadInstance(dbcConfigFile);
			HashMap dbcConfigInfo = dbcConfig.getSessions();

			if (dbcConfigInfo != null) {
				for (Object key : dbcConfigInfo.keySet()) {
					String path = (String) dbcConfigInfo.get(key);
					try {
						LogUtils.addLog("Tạo SessionFactory cho file cấu hình: " + path);
						AnnotationConfiguration dbConfig = new AnnotationConfiguration().configure(path);
						String encryptedFile = dbcConfigFile.substring(0, dbcConfigFile.lastIndexOf("/") + 1) + key;
						try {
							loadEncryptedDBConfig(dbConfig, encryptedFile);
						} catch (Exception ex) {
							// log.info("Error while reading encrypted file: " +
							// encryptedFile, ex);
							// log.info("Read HibernateConfigFile again, file to
							// read: " + path);
							dbConfig = new AnnotationConfiguration().configure(path);
							LogUtils.addLog(ex);
						}

						SessionFactory sessionFactory = dbConfig.buildSessionFactory();
						sessionFactories.put(key.toString().toLowerCase(), sessionFactory);
					} catch (Exception ex) {
						System.out.println(ex.getMessage());
						LogUtils.addLog(ex);
						LogUtils.addLog(
								"Tiếp tục thực hiện tạo SessionFactory cho các file cấu hình tiếp theo (nếu còn)");
					}
				}
			}

			if (sessionFactories.isEmpty()) {
				LogUtils.addLog(
						"Không tạo đuợc SessionFactory nào. Ứng dụng sẽ chạy mà không có khả năng truy cập CSDL");
			}
		} catch (IOException ex) {
			LogUtils.addLog(ex);
			LogUtils.addLog("Lỗi khi đọc file : " + dbcConfigFile);
			throw new ExceptionInInitializerError(ex);
		} catch (SAXException ex) {
			LogUtils.addLog(ex);
			LogUtils.addLog("Lỗi khi parse nội dung file : " + dbcConfigFile);
			throw new ExceptionInInitializerError(ex);
		}

	}
}

/*
 * Location: C:\Work\RDFW 315.jar Qualified Name:
 * com.viettel.common.util.HibernateUtil JD-Core Version: 0.6.2
 */