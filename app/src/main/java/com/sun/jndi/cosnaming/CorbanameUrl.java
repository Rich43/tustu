package com.sun.jndi.cosnaming;

import com.sun.corba.se.impl.orbutil.ORBConstants;
import com.sun.jndi.toolkit.url.UrlUtil;
import java.net.MalformedURLException;
import javax.naming.Name;
import javax.naming.NamingException;

/* loaded from: rt.jar:com/sun/jndi/cosnaming/CorbanameUrl.class */
public final class CorbanameUrl {
    private String stringName;
    private String location;

    public String getStringName() {
        return this.stringName;
    }

    public Name getCosName() throws NamingException {
        return CNCtx.parser.parse(this.stringName);
    }

    public String getLocation() {
        return "corbaloc:" + this.location;
    }

    public CorbanameUrl(String str) throws MalformedURLException {
        if (!str.startsWith("corbaname:")) {
            throw new MalformedURLException("Invalid corbaname URL: " + str);
        }
        int iIndexOf = str.indexOf(35, 10);
        if (iIndexOf < 0) {
            iIndexOf = str.length();
            this.stringName = "";
        } else {
            this.stringName = UrlUtil.decode(str.substring(iIndexOf + 1));
        }
        this.location = str.substring(10, iIndexOf);
        int iIndexOf2 = this.location.indexOf("/");
        if (iIndexOf2 >= 0) {
            if (iIndexOf2 == this.location.length() - 1) {
                this.location += ORBConstants.PERSISTENT_NAME_SERVICE_NAME;
                return;
            }
            return;
        }
        this.location += "/NameService";
    }
}
