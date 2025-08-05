package javax.xml.crypto.dsig.spec;

import com.sun.org.apache.xml.internal.security.transforms.params.XPath2FilterContainer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* loaded from: rt.jar:javax/xml/crypto/dsig/spec/XPathType.class */
public class XPathType {
    private final String expression;
    private final Filter filter;
    private Map<String, String> nsMap;

    /* loaded from: rt.jar:javax/xml/crypto/dsig/spec/XPathType$Filter.class */
    public static class Filter {
        private final String operation;
        public static final Filter INTERSECT = new Filter(XPath2FilterContainer.INTERSECT);
        public static final Filter SUBTRACT = new Filter(XPath2FilterContainer.SUBTRACT);
        public static final Filter UNION = new Filter("union");

        private Filter(String str) {
            this.operation = str;
        }

        public String toString() {
            return this.operation;
        }
    }

    public XPathType(String str, Filter filter) {
        if (str == null) {
            throw new NullPointerException("expression cannot be null");
        }
        if (filter == null) {
            throw new NullPointerException("filter cannot be null");
        }
        this.expression = str;
        this.filter = filter;
        this.nsMap = Collections.emptyMap();
    }

    public XPathType(String str, Filter filter, Map map) {
        this(str, filter);
        if (map == null) {
            throw new NullPointerException("namespaceMap cannot be null");
        }
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

    public String getExpression() {
        return this.expression;
    }

    public Filter getFilter() {
        return this.filter;
    }

    public Map getNamespaceMap() {
        return this.nsMap;
    }
}
