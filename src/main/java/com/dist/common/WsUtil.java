package com.dist.common;

import java.util.Locale;

import javax.security.auth.Subject;

import com.filenet.api.core.Connection;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.util.UserContext;

public class WsUtil {
	public static Connection getCEConnection() {
		String uri = "http://" + GlobalConfig.get("site.ECMServer") +":9080/wsi/FNCEWS40MTOM/";
		String username = GlobalConfig.get("ecm.username");
		String password =  GlobalConfig.get("ecm.password");

		System.setProperty("java.security.auth.login.config", "jaas.conf.wsi");

		System.setProperty("java.naming.factory.initial",
				"com.ibm.websphere.naming.WsnInitialContextFactory");

		System.setProperty("filenet.pe.bootstrap.ceuri", uri);

		Connection conn = Factory.Connection.getConnection(uri);
		try {
			Subject subject = UserContext.createSubject(conn, username,
					password, null);
			UserContext uc = UserContext.get();
			uc.pushSubject(subject);
			System.out.println("!--connect success");
		} catch (Exception e) {
			System.out.println("!--" + e.getCause());
		}

		return conn;
	}

	public static Domain getDomain() {
		Domain domain = Factory.Domain.fetchInstance(getCEConnection(),
				GlobalConfig.get("ecm.domain"), null);
		System.out.println(domain.get_Name());
		return domain;
	}

	public static ObjectStore getObjectStore(String osName) {
		return Factory.ObjectStore.fetchInstance(getDomain(), osName, null);
	}
}
