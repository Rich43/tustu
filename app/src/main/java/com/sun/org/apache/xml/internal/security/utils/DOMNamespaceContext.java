package com.sun.org.apache.xml.internal.security.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.xml.namespace.NamespaceContext;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/utils/DOMNamespaceContext.class */
public class DOMNamespaceContext implements NamespaceContext {
    private Map<String, String> namespaceMap = new HashMap();

    public DOMNamespaceContext(Node node) {
        addNamespaces(node);
    }

    @Override // javax.xml.namespace.NamespaceContext
    public String getNamespaceURI(String str) {
        return this.namespaceMap.get(str);
    }

    @Override // javax.xml.namespace.NamespaceContext
    public String getPrefix(String str) {
        for (Map.Entry<String, String> entry : this.namespaceMap.entrySet()) {
            if (entry.getValue().equals(str)) {
                return entry.getKey();
            }
        }
        return null;
    }

    @Override // javax.xml.namespace.NamespaceContext
    public Iterator<String> getPrefixes(String str) {
        return this.namespaceMap.keySet().iterator();
    }

    private void addNamespaces(Node node) {
        if (node.getParentNode() != null) {
            addNamespaces(node.getParentNode());
        }
        if (node instanceof Element) {
            NamedNodeMap attributes = ((Element) node).getAttributes();
            for (int i2 = 0; i2 < attributes.getLength(); i2++) {
                Attr attr = (Attr) attributes.item(i2);
                if ("xmlns".equals(attr.getPrefix())) {
                    this.namespaceMap.put(attr.getLocalName(), attr.getValue());
                }
            }
        }
    }
}
