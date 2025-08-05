package javax.xml.crypto.dsig.spec;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* loaded from: rt.jar:javax/xml/crypto/dsig/spec/XPathFilterParameterSpec.class */
public final class XPathFilterParameterSpec implements TransformParameterSpec {
    private String xPath;
    private Map<String, String> nsMap;

    public XPathFilterParameterSpec(String str) {
        if (str == null) {
            throw new NullPointerException();
        }
        this.xPath = str;
        this.nsMap = Collections.emptyMap();
    }

    public XPathFilterParameterSpec(String str, Map map) {
        if (str == null || map == null) {
            throw new NullPointerException();
        }
        this.xPath = str;
        HashMap map2 = new HashMap(map);
        Iterator it = map2.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            if (!(entry.getKey() instanceof String) || !(entry.getValue() instanceof String)) {
                throw new ClassCastException("not a String");
            }
        }
        this.nsMap = Collections.unmodifiableMap(map2);
    }

    public String getXPath() {
        return this.xPath;
    }

    public Map getNamespaceMap() {
        return this.nsMap;
    }
}
