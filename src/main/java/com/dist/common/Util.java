package com.dist.common;

import javax.security.auth.Subject;

import com.dist.common.GlobalConfig;
import com.dist.common.WsUtil;
import com.filenet.api.core.Connection;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.util.UserContext;

public class Util {
    
    public static Connection getLocalConnection() {
        
        boolean isDebugMode = GlobalConfig.isDebugModel();
        if(isDebugMode){
            return WsUtil.getCEConnection();
        }
        String uri = "iiop://" + GlobalConfig.get("site.ECMServer") + ":2809/FileNet/Engine";
        String username = "administrator";
        String password = "filenet";

        // Make connection.
        Connection conn = Factory.Connection.getConnection(uri);
        Subject subject = UserContext.createSubject(conn, username, password, null);
        UserContext.get().pushSubject(subject);
        return conn;
    }
    
    public static Domain getDomain(){
        Domain domain = Factory.Domain.fetchInstance(getLocalConnection(), null, null);
        return domain;
        
    }
    
    public static ObjectStore getObjectStore(String objName){
//    	if(os == null){
//    		os =  Factory.ObjectStore.getInstance(getDomain(), objName);
//    		os.refresh();
//    	}
//         return os;
        return Factory.ObjectStore.fetchInstance(WsUtil.getDomain(),
                objName, null);
    }
    
    

}
