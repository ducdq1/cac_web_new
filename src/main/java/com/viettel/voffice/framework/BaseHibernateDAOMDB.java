/*     */ package com.viettel.voffice.framework;

/*     */ import java.io.Serializable;
/*     */ import java.math.BigDecimal;
/*     */ import java.net.URL;
/*     */ import java.net.URLDecoder;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;

/*     */ import org.hibernate.HibernateException;
/*     */ import org.hibernate.Query;
/*     */ import org.hibernate.Session;
/*     */ import org.hibernate.SessionFactory;
/*     */ import org.hibernate.cfg.Configuration;

/*     */
/*     */ import com.viettel.utils.ArgChecker;
/*     */ import com.viettel.utils.EncryptDecryptUtils;
import com.viettel.utils.LogUtils;
/*     */ import com.viettel.utils.ResourceBundleUtil;
/*     */ import com.viettel.voffice.config.Config;
/*     */ import com.viettel.voffice.config.RuleSelector;
/*     */ import com.viettel.voffice.dbbo.BasicBO;
/*     */ import com.viettel.voffice.dbbo.ModRule;

/*     */
/*     */ public class BaseHibernateDAOMDB /*     */ {
	/* 33 */ protected static final HashMap<String, SessionFactory> sessionFactoryMap = new HashMap<String, SessionFactory>();
	/* 34 */ public static final ThreadLocal sessionMapsThreadLocal = new ThreadLocal();
	/* 35 */ private static Config config = new Config();
	/* 36 */ private static RuleSelector ruleSelector = new RuleSelector();
	/* 37 */ 

	/*     */
	/*     */ public static Session getSession(String key) throws HibernateException /*     */ {
		/* 41 */ String key_tmp = key.toLowerCase();
		/* 42 */ HashMap sessionMaps = (HashMap) sessionMapsThreadLocal.get();
		/* 43 */ if (sessionMaps == null) {
			/* 44 */ sessionMaps = new HashMap<String, Object>();
			/* 45 */ sessionMapsThreadLocal.set(sessionMaps);
			/*     */ }
		/*     */
		/* 48 */ Session s = (Session) sessionMaps.get(key_tmp);
		/*     */ try {
			/* 50 */ if ((s == null) || (!s.isOpen())) {
				/* 51 */ s = ((SessionFactory) sessionFactoryMap.get(key_tmp)).openSession();
				/* 52 */ sessionMaps.put(key_tmp, s);
				/*     */ }
			/*     */ } catch (HibernateException ex) {
			LogUtils.addLog(ex);
			/*     */ }
		/*     */ if (!s.isConnected()) /* 60 */ {
			s = createSession(key_tmp, sessionMaps, s);
		}
			//Transaction t = s.getTransaction();
		/* 67 */ if (s.getTransaction() == null) /* 68 */ {
						s.beginTransaction();
		} /* 69 */ else if (!s.getTransaction().isActive()) {
			/* 70 */ s.getTransaction().begin();
			/*     */ }
		/* 72 */ return s;
		/*     */ }

	/*     */
	/*     */ public static HashMap getSessions() {
		/* 76 */ return (HashMap) sessionMapsThreadLocal.get();
		/*     */ }

	/*     */
	/*     */ public static Session getSession(int rule_type, Object rule_value) throws HibernateException {
		/* 80 */ switch (rule_type) {
		/*     */ case 1:
			/* 82 */ return getSession(ruleSelector.getValueLocation(rule_value.toString()));
		/*     */ case 2:
			/* 84 */ int tmp = ((Integer) rule_value).intValue();
			/* 85 */ if (ruleSelector.getMods().size() > 0) /*     */ {
				/* 88 */ ModRule modRule = (ModRule) ruleSelector.getMods().values().iterator().next();
				/* 89 */ int key = tmp % modRule.getValue();
				/* 90 */ return getSession(ruleSelector.getValueMod(String.valueOf(key)));
			}
			break;
		/*     */ case 3:
			/* 92 */ if ((rule_value instanceof Date)) {
				/* 93 */ return getSession(ruleSelector.getValueDateTimeRule((Date) rule_value));
				/*     */ }
			/*     */ break;
		/*     */ }
		/*     */
		/* 98 */ return getSession();
		/*     */ }

	/*     */
	/*     */ public static Session getSession() throws HibernateException {
		/* 102 */ return getSession("default session");
		/*     */ }

	/*     */
	/*     */ public static void closeSessions() throws HibernateException {
		/* 106 */ HashMap sessionMaps = (HashMap) sessionMapsThreadLocal.get();
		/* 107 */ sessionMapsThreadLocal.set(null);
		/* 108 */ if (sessionMaps != null) /* 109 */ {
			for (Session session : (ArrayList<Session>) sessionMaps.values()) /* 110 */ {
				if (session.isOpen()) /* 111 */ {
					session.close();
				}
			}
		}
		/*     */ }

	/*     */
	/*     */ public static void closeSession() /*     */ {
		/* 118 */ HashMap sessionMaps = (HashMap) sessionMapsThreadLocal.get();
		/* 119 */ sessionMapsThreadLocal.set(null);
		/* 120 */ if (sessionMaps != null) {
			/* 121 */ Session session = (Session) sessionMaps.get("default session".toLowerCase());
			/* 122 */ if ((session != null) && (session.isOpen())) /* 123 */ {
				session.close();
			}
			/*     */ }
		/*     */ }

	/*     */
	/*     */ public static void closeSession(String key) /*     */ {
		/* 129 */ HashMap sessionMaps = (HashMap) sessionMapsThreadLocal.get();
		/* 130 */ sessionMapsThreadLocal.set(null);
		/* 131 */ if (sessionMaps != null) {
			/* 132 */ Session session = (Session) sessionMaps.get(key.toLowerCase());
			/* 133 */ if ((session != null) && (session.isOpen())) /* 134 */ {
				session.close();
			}
			/*     */ }
		/*     */ }

	/*     */
	/*     */ public static void buildSessionFactories(HashMap<String, String> configs) /*     */ {
		/*     */ try {
			/* 141 */ for (String key : configs.keySet()) {
				/* 142 */ URL url = BaseHibernateDAOMDB.class.getResource((String) configs.get(key));
				/* 143 */ SessionFactory sessionFactory = new Configuration() {
				}.configure(url).buildSessionFactory();
				/*     */
				/* 146 */ sessionFactoryMap.put(key, sessionFactory);
				/*     */ }
			/*     */ } catch (Exception ex) {
			/* 149 */ LogUtils.addLog(ex);
			/*     */
			/* 151 */ throw new ExceptionInInitializerError(ex);
			/*     */ }
		/*     */ }

	/*     */
	/*     */ public static void beginTransactions() {
		/* 156 */ HashMap hashMap = config.getSessions();
		/* 157 */ for (String key : (ArrayList<String>) hashMap.keySet()) /* 158 */ {
			getSession(key).beginTransaction();
		}
		/*     */ }

	/*     */
	/*     */ public static void buildSessionFactory(String key, String path) /*     */ {
		/*     */ try {
			/* 164 */ SessionFactory sessionFactory = new Configuration().configure(path).buildSessionFactory();
			/* 165 */ sessionFactoryMap.put(key, sessionFactory);
			/*     */ } catch (Exception ex) {
			LogUtils.addLog(ex);
			/* 167 */ throw new ExceptionInInitializerError(ex);
			/*     */ }
		/*     */ }

	/*     */
	/*     */ public static void rebuildSessionFactories() {
		/*     */ try {
			/* 173 */ HashMap hashMap = config.getSessions();
			/* 174 */ for (String key : (ArrayList<String>) hashMap.keySet()) {
				/* 175 */ String path = (String) hashMap.get(key);
				/* 176 */ SessionFactory sessionFactory = new Configuration() {
				}.configure(path).buildSessionFactory();
				/*     */
				/* 179 */ sessionFactoryMap.put(key.toLowerCase(), sessionFactory);
				/*     */ }
			/*     */ } /*     */ catch (Exception e) /*     */ {
			/*     */ HashMap hashMap;
			/* 183 */ LogUtils.addLog(e);
			/* 184 */
			/*     */ }
		/*     */ }

	/*     */
	/*     */ private static Session createSession(String key_tmp, HashMap<String, Session> sessionMaps, Session s)
			throws HibernateException {
		/* 189 */ sessionFactoryMap.put(key_tmp,
				new Configuration().configure((String) config.getSessions().get(key_tmp)).buildSessionFactory());
		/* 190 */ sessionMaps.remove(key_tmp);
		/* 191 */ s = ((SessionFactory) sessionFactoryMap.get(key_tmp)).openSession();
		/* 192 */ sessionMaps.put(key_tmp, s);
		/* 193 */ System.out.println("Finish create sessionFactory");
		/* 194 */ return s;
		/*     */ }

	/*     */
	/*     */ public void save(BasicBO objectToSave) throws Exception {
		/* 198 */ Session session = getSession("default session");
		/* 199 */ ArgChecker.denyNull(objectToSave);
		/* 200 */ session.save(objectToSave);
		/*     */ }

	/*     */
	/*     */ public void update(BasicBO objectToUpdate) throws Exception {
		/* 204 */ Session session = getSession("default session");
		/* 205 */ ArgChecker.denyNull(objectToUpdate);
		/* 206 */ session.update(objectToUpdate);
		/*     */ }

	/*     */
	/*     */ public void saveOrUpdate(BasicBO objectToSaveOrUpdate) throws Exception {
		/* 210 */ Session session = getSession("default session");
		/* 211 */ ArgChecker.denyNull(objectToSaveOrUpdate);
		/* 212 */ session.saveOrUpdate(objectToSaveOrUpdate);
		/*     */ }

	/*     */
	/*     */ public void delete(BasicBO objectToDelete) throws Exception {
		/* 216 */ Session session = getSession("default session");
		/* 217 */ ArgChecker.denyNull(objectToDelete);
		/* 218 */ session.delete(objectToDelete);
		/*     */ }

	/*     */
	/*     */ public void refresh(BasicBO objectToRefresh) throws Exception {
		/* 222 */ Session session = getSession("default session");
		/* 223 */ session.refresh(objectToRefresh);
		/*     */ }

	/*     */
	/*     */ public BasicBO get(Object id, String strClassHandle) throws Exception {
		/* 227 */ Session session = getSession("default session");
		/* 228 */ BasicBO instance = (BasicBO) session.get(strClassHandle, (Serializable) id);
		/* 229 */ session.refresh(instance);
		/* 230 */ return instance;
		/*     */ }

	/*     */
	/*     */ public List getAll(String strClassHandle) {
		/* 234 */ Session session = getSession("default session");
		/* 235 */ String queryString = "from " + strClassHandle;
		/* 236 */ Query queryObject = session.createQuery(queryString);
		/* 237 */ return queryObject.list();
		/*     */ }

	/*     */
	/*     */ public static void setSessions(HashMap<String, Session> sessions) {
		/* 241 */ sessionMapsThreadLocal.set(sessions);
		/*     */ }

	/*     */
	/*     */ public List findByProperty(String strClassHandle, String propertyName, Object value) {
		/* 245 */ 
		/* 246 */ String queryString = "from " + strClassHandle + " as model where model." + propertyName + "= ?";
		/* 247 */ Query queryObject = getSession("default session").createQuery(queryString);
		/* 248 */ queryObject.setParameter(0, value);
		/* 249 */ List lstReturn = queryObject.list();
		/* 250 */ return lstReturn;
		/*     */ }

	/*     */
	/*     */ public static long getSequence(String sequenceName) throws Exception {
		/* 254 */ String strQuery = "SELECT " + sequenceName + " .NextVal FROM Dual";
		/* 255 */ Query queryObject = getSession("default session").createSQLQuery(strQuery);
		/* 256 */ BigDecimal bigDecimal = (BigDecimal) queryObject.uniqueResult();
		/* 257 */ return bigDecimal.longValue();
		/*     */ }

	/*     */
	/*     */ public void save(BasicBO objectToSave, String sessionName) throws Exception {
		/* 261 */ Session session = getSession(sessionName);
		/* 262 */ ArgChecker.denyNull(objectToSave);
		/* 263 */ session.save(objectToSave);
		/*     */ }

	/*     */
	/*     */ public void update(BasicBO objectToUpdate, String sessionName) throws Exception {
		/* 267 */ Session session = getSession(sessionName);
		/* 268 */ ArgChecker.denyNull(objectToUpdate);
		/* 269 */ session.update(objectToUpdate);
		/*     */ }

	/*     */
	/*     */ public void saveOrUpdate(BasicBO objectToSaveOrUpdate, String sessionName) throws Exception {
		/* 273 */ Session session = getSession(sessionName);
		/* 274 */ ArgChecker.denyNull(objectToSaveOrUpdate);
		/* 275 */ session.saveOrUpdate(objectToSaveOrUpdate);
		/*     */ }

	/*     */
	/*     */ public void delete(BasicBO objectToDelete, String sessionName) throws Exception {
		/* 279 */ Session session = getSession(sessionName);
		/* 280 */ ArgChecker.denyNull(objectToDelete);
		/* 281 */ session.delete(objectToDelete);
		/*     */ }

	/*     */
	/*     */ public void refresh(BasicBO objectToRefresh, String sessionName) throws Exception {
		/* 285 */ Session session = getSession(sessionName);
		/* 286 */ session.refresh(objectToRefresh);
		/*     */ }

	/*     */
	/*     */ public BasicBO get(Object id, String strClassHandle, String sessionName) throws Exception {
		/* 290 */ Session session = getSession(sessionName);
		/* 291 */ BasicBO instance = (BasicBO) session.get(strClassHandle, (Serializable) id);
		/* 292 */ session.refresh(instance);
		/* 293 */ return instance;
		/*     */ }

	/*     */
	/*     */ public List getAll(String strClassHandle, String sessionName) {
		/* 297 */ Session session = getSession(sessionName);
		/* 298 */ String queryString = "from " + strClassHandle;
		/* 299 */ Query queryObject = session.createQuery(queryString);
		/* 300 */ return queryObject.list();
		/*     */ }

	/*     */
	/*     */ public List findByProperty(String strClassHandle, String propertyName, Object value, String sessionName) {
		/* 304 */
		/* 305 */ String queryString = "from " + strClassHandle + " as model where model." + propertyName + "= ?";
		/* 306 */ Query queryObject = getSession(sessionName).createQuery(queryString);
		/* 307 */ queryObject.setParameter(0, value);
		/* 308 */  List lstReturn = queryObject.list();
		/* 309 */ return lstReturn;
		/*     */ }

	/*     */
	/*     */ public static long getSequence(String sequenceName, String sessionName) throws Exception {
		/* 313 */ String strQuery = "SELECT " + sequenceName + " .NextVal FROM Dual";
		/* 314 */ Query queryObject = getSession(sessionName).createSQLQuery(strQuery);
		/* 315 */ BigDecimal bigDecimal = (BigDecimal) queryObject.uniqueResult();
		/* 316 */ return bigDecimal.longValue();
		/*     */ }

	/*     */
	/*     */ public List query(String query, Object[] params) {
		/* 320 */ List result = new ArrayList();
		/* 321 */ Set key = sessionFactoryMap.keySet();
		/* 322 */ Iterator iter = key.iterator();
		/* 323 */ while (iter.hasNext()) {
			/* 324 */ Session s = getSession((String) iter.next());
			/* 325 */ Query q = s.createQuery(query);
			/* 326 */ for (int i = 0; i < params.length; i++) {
				/* 327 */ q.setParameter(i, params[i]);
				/*     */ }
			/* 329 */ result.addAll(q.list());
			/*     */ }
		/* 331 */ return result;
		/*     */ }

	/*     */
	/*     */ public static void commitSessions() throws HibernateException {
		/* 335 */ HashMap sessionMaps = (HashMap) sessionMapsThreadLocal.get();
		/* 336 */ sessionMapsThreadLocal.set(null);
		/* 337 */ if (sessionMaps != null) /* 338 */ {
			for (Session session : (ArrayList<Session>) sessionMaps.values()) /* 339 */ {
				if (session.isOpen()) /* 340 */ {
					session.getTransaction().commit();
				}
			}
		}
		/*     */ }

	/*     */
	/*     */ public static void rollBackSessions()/*     */ throws HibernateException /*     */ {
		/* 348 */ HashMap sessionMaps = (HashMap) sessionMapsThreadLocal.get();
		/* 349 */ sessionMapsThreadLocal.set(null);
		/* 350 */ if (sessionMaps != null) /* 351 */ {
			for (Session session : (ArrayList<Session>) sessionMaps.values()) /* 352 */ {
				if ((session.isOpen()) && (session.getTransaction().isActive())) /* 353 */ {
					session.getTransaction().rollback();
				}
			}
		}
		/*     */ }

	/*     */
	/*     */ public void save(int rule_type, Object rule_value, BasicBO objectToSave)
			/*     */ throws Exception /*     */ {
		/* 360 */ Session session = getSession(rule_type, rule_value);
		/* 361 */ if (!session.getTransaction().isActive()) {
			/* 362 */ session.beginTransaction();
			/*     */ }
		/* 364 */ ArgChecker.denyNull(objectToSave);
		/* 365 */ session.save(objectToSave);
		/*     */ }

	/*     */
	/*     */ public void update(int rule_type, Object rule_value, BasicBO objectToUpdate) throws Exception {
		/* 369 */ Session session = getSession(rule_type, rule_value);
		/* 370 */ if (!session.getTransaction().isActive()) {
			/* 371 */ session.beginTransaction();
			/*     */ }
		/* 373 */ ArgChecker.denyNull(objectToUpdate);
		/* 374 */ session.update(objectToUpdate);
		/*     */ }

	/*     */
	/*     */ public void saveOrUpdate(int rule_type, Object rule_value, BasicBO objectToSaveOrUpdate)
			throws Exception {
		/* 378 */ Session session = getSession(rule_type, rule_value);
		/* 379 */ if (!session.getTransaction().isActive()) {
			/* 380 */ session.beginTransaction();
			/*     */ }
		/* 382 */ ArgChecker.denyNull(objectToSaveOrUpdate);
		/* 383 */ session.saveOrUpdate(objectToSaveOrUpdate);
		/*     */ }

	/*     */
	/*     */ @SuppressWarnings("static-access")
	private static void loadEncryptedDBConfig(Configuration config, String filePath) {
		/* 387 */ URL file = Thread.currentThread().getContextClassLoader().getResource(filePath);
		/* 388 */EncryptDecryptUtils encryptDecryptUtils = new EncryptDecryptUtils();
		String decryptString = encryptDecryptUtils.decrypt(URLDecoder.decode(file.getPath()));
		/* 389 */ String[] properties = decryptString.split("\r\n");
		/* 390 */ for (String property : properties) {
			/* 391 */ String[] temp = property.split("=", 2);
			/* 392 */ if (temp.length == 2) /* 393 */ {
				config.setProperty(temp[0], temp[1]);
			}
			/*     */ }
		/*     */ }

	/*     */
	/*     */ static /*     */ {
		/*     */ try {
			/* 400 */ String configFile = ResourceBundleUtil.getDBCConfigFileLocation();
			/* 401 */ config.loadInstance(configFile);
			/*     */
			/* 403 */ HashMap hashMap = config.getSessions();
			/* 404 */ ruleSelector = config.getRuleSelector();
			/* 405 */ for (String key : (ArrayList<String>) hashMap.keySet()) {
				/* 406 */ String path = (String) hashMap.get(key);
				/*     */
				/* 408 */ Configuration config = new Configuration() {
				}.configure(path);
				/*     */
				/* 411 */ String encryptedFile = configFile.substring(0, configFile.lastIndexOf("/") + 1) + key;
				/*     */ try {
					/* 413 */ loadEncryptedDBConfig(config, encryptedFile);
					/*     */ } catch (Exception ex) {
					LogUtils.addLog(ex);
					/* 415 */ config = new Configuration().configure(path);
					/*     */ }
				/*     */
				/* 418 */ SessionFactory sessionFactory = config.buildSessionFactory();
				/*     */
				/* 420 */ sessionFactoryMap.put(key.toLowerCase(), sessionFactory);
				/*     */ }
			/*     */ } /*     */ catch (Exception e) /*     */ {
			/*     */ HashMap hashMap;
			/* 425 */ LogUtils.addLog(e);
			/*     */ }
		/*     */ }
	/*     */ }

/*
 * Location: C:\Work\RDFW 315.jar Qualified Name:
 * com.viettel.framework.interceptor.BaseHibernateDAOMDB JD-Core Version: 0.6.2
 */