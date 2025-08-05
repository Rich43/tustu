package com.sun.org.apache.xml.internal.security.utils.resolver;

import com.sun.org.apache.xml.internal.security.signature.XMLSignatureInput;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/utils/resolver/ResourceResolverSpi.class */
public abstract class ResourceResolverSpi {
    private static final Logger LOG = LoggerFactory.getLogger(ResourceResolverSpi.class);
    protected Map<String, String> properties;

    public abstract XMLSignatureInput engineResolveURI(ResourceResolverContext resourceResolverContext) throws ResourceResolverException;

    public abstract boolean engineCanResolveURI(ResourceResolverContext resourceResolverContext);

    public void engineSetProperty(String str, String str2) {
        if (this.properties == null) {
            this.properties = new HashMap();
        }
        this.properties.put(str, str2);
    }

    public String engineGetProperty(String str) {
        if (this.properties == null) {
            return null;
        }
        return this.properties.get(str);
    }

    public void engineAddProperies(Map<String, String> map) {
        if (map != null && !map.isEmpty()) {
            if (this.properties == null) {
                this.properties = new HashMap();
            }
            this.properties.putAll(map);
        }
    }

    public boolean engineIsThreadSafe() {
        return false;
    }

    public String[] engineGetPropertyKeys() {
        return new String[0];
    }

    public boolean understandsProperty(String str) {
        String[] strArrEngineGetPropertyKeys = engineGetPropertyKeys();
        if (strArrEngineGetPropertyKeys != null) {
            for (String str2 : strArrEngineGetPropertyKeys) {
                if (str2.equals(str)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public static String fixURI(String str) {
        char upperCase;
        String strReplace = str.replace(File.separatorChar, '/');
        if (strReplace.length() >= 4) {
            char upperCase2 = Character.toUpperCase(strReplace.charAt(0));
            if ('A' <= upperCase2 && upperCase2 <= 'Z' && strReplace.charAt(1) == ':' && strReplace.charAt(2) == '/' && strReplace.charAt(3) != '/') {
                LOG.debug("Found DOS filename: {}", strReplace);
            }
        }
        if (strReplace.length() >= 2 && strReplace.charAt(1) == ':' && 'A' <= (upperCase = Character.toUpperCase(strReplace.charAt(0))) && upperCase <= 'Z') {
            strReplace = "/" + strReplace;
        }
        return strReplace;
    }
}
