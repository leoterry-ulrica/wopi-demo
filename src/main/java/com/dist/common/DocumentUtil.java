package com.dist.common;

import com.filenet.api.constants.RefreshMode;
import com.filenet.api.constants.VersionStatus;
import com.filenet.api.core.Document;
import com.filenet.api.exception.EngineRuntimeException;

public class DocumentUtil {

    public static Document getReseVersion(Document doc) {
        try {
            doc.checkout(com.filenet.api.constants.ReservationType.EXCLUSIVE, null, doc.getClassName(),
                    doc.getProperties());
            doc.save(RefreshMode.REFRESH);
        } catch (EngineRuntimeException e) {
            e.printStackTrace();
        }
        
        if (doc.get_IsCurrentVersion().booleanValue() == false) {
            doc = (Document) doc.get_CurrentVersion();
        }
        if (doc.get_IsReserved().booleanValue() == true
                && (doc.get_VersionStatus().getValue() != VersionStatus.RESERVATION_AS_INT)) {
            doc = (Document) doc.get_Reservation();
        }
        return doc;
    }
    
    
    
}
