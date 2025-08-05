package com.sun.corba.se.impl.naming.namingutil;

import com.sun.corba.se.impl.logging.NamingSystemException;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import java.util.ArrayList;
import org.omg.CORBA.BAD_PARAM;

/* loaded from: rt.jar:com/sun/corba/se/impl/naming/namingutil/CorbanameURL.class */
public class CorbanameURL extends INSURLBase {
    private static NamingSystemException wrapper = NamingSystemException.get(CORBALogDomains.NAMING);

    public CorbanameURL(String str) throws BAD_PARAM {
        String str2;
        String strCleanEscapes = str;
        try {
            strCleanEscapes = Utility.cleanEscapes(strCleanEscapes);
        } catch (Exception e2) {
            badAddress(e2);
        }
        int iIndexOf = strCleanEscapes.indexOf(35);
        if (iIndexOf != -1) {
            str2 = "corbaloc:" + strCleanEscapes.substring(0, iIndexOf) + "/";
        } else {
            str2 = "corbaloc:" + strCleanEscapes.substring(0, strCleanEscapes.length());
            if (!str2.endsWith("/")) {
                str2 = str2 + "/";
            }
        }
        try {
            copyINSURL(INSURLHandler.getINSURLHandler().parseURL(str2));
            if (iIndexOf > -1 && iIndexOf < str.length() - 1) {
                this.theStringifiedName = strCleanEscapes.substring(iIndexOf + 1);
            }
        } catch (Exception e3) {
            badAddress(e3);
        }
    }

    private void badAddress(Throwable th) throws BAD_PARAM {
        throw wrapper.insBadAddress(th);
    }

    private void copyINSURL(INSURL insurl) {
        this.rirFlag = insurl.getRIRFlag();
        this.theEndpointInfo = (ArrayList) insurl.getEndpointInfo();
        this.theKeyString = insurl.getKeyString();
        this.theStringifiedName = insurl.getStringifiedName();
    }

    @Override // com.sun.corba.se.impl.naming.namingutil.INSURLBase, com.sun.corba.se.impl.naming.namingutil.INSURL
    public boolean isCorbanameURL() {
        return true;
    }
}
