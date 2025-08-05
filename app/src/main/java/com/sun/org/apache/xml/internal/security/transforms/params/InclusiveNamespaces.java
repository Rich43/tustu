package com.sun.org.apache.xml.internal.security.transforms.params;

import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
import com.sun.org.apache.xml.internal.security.transforms.TransformParam;
import com.sun.org.apache.xml.internal.security.utils.ElementProxy;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/transforms/params/InclusiveNamespaces.class */
public class InclusiveNamespaces extends ElementProxy implements TransformParam {
    public static final String _TAG_EC_INCLUSIVENAMESPACES = "InclusiveNamespaces";
    public static final String _ATT_EC_PREFIXLIST = "PrefixList";
    public static final String ExclusiveCanonicalizationNamespace = "http://www.w3.org/2001/10/xml-exc-c14n#";

    public InclusiveNamespaces(Document document, String str) {
        this(document, prefixStr2Set(str));
    }

    public InclusiveNamespaces(Document document, Set<String> set) {
        Set<String> treeSet;
        super(document);
        if (set instanceof SortedSet) {
            treeSet = (SortedSet) set;
        } else {
            treeSet = new TreeSet(set);
        }
        StringBuilder sb = new StringBuilder();
        for (String str : treeSet) {
            if ("xmlns".equals(str)) {
                sb.append("#default ");
            } else {
                sb.append(str);
                sb.append(" ");
            }
        }
        setLocalAttribute(_ATT_EC_PREFIXLIST, sb.toString().trim());
    }

    public InclusiveNamespaces(Element element, String str) throws XMLSecurityException {
        super(element, str);
    }

    public String getInclusiveNamespaces() {
        return getLocalAttribute(_ATT_EC_PREFIXLIST);
    }

    public static SortedSet<String> prefixStr2Set(String str) {
        TreeSet treeSet = new TreeSet();
        if (str == null || str.length() == 0) {
            return treeSet;
        }
        for (String str2 : str.split("\\s")) {
            if (str2.equals("#default")) {
                treeSet.add("xmlns");
            } else {
                treeSet.add(str2);
            }
        }
        return treeSet;
    }

    @Override // com.sun.org.apache.xml.internal.security.utils.ElementProxy
    public String getBaseNamespace() {
        return "http://www.w3.org/2001/10/xml-exc-c14n#";
    }

    @Override // com.sun.org.apache.xml.internal.security.utils.ElementProxy
    public String getBaseLocalName() {
        return _TAG_EC_INCLUSIVENAMESPACES;
    }
}
