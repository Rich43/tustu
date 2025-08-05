package com.sun.org.apache.xml.internal.utils;

import jdk.internal.dynalink.CallSiteDescriptor;
import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/utils/PrefixResolverDefault.class */
public class PrefixResolverDefault implements PrefixResolver {
    Node m_context;

    public PrefixResolverDefault(Node xpathExpressionContext) {
        this.m_context = xpathExpressionContext;
    }

    @Override // com.sun.org.apache.xml.internal.utils.PrefixResolver
    public String getNamespaceForPrefix(String prefix) {
        return getNamespaceForPrefix(prefix, this.m_context);
    }

    @Override // com.sun.org.apache.xml.internal.utils.PrefixResolver
    public String getNamespaceForPrefix(String prefix, Node namespaceContext) throws DOMException {
        int type;
        String namespace = null;
        if (prefix.equals("xml")) {
            namespace = "http://www.w3.org/XML/1998/namespace";
        } else {
            for (Node parent = namespaceContext; null != parent && null == namespace && ((type = parent.getNodeType()) == 1 || type == 5); parent = parent.getParentNode()) {
                if (type == 1) {
                    if (parent.getNodeName().indexOf(prefix + CallSiteDescriptor.TOKEN_DELIMITER) == 0) {
                        return parent.getNamespaceURI();
                    }
                    NamedNodeMap nnm = parent.getAttributes();
                    int i2 = 0;
                    while (true) {
                        if (i2 >= nnm.getLength()) {
                            break;
                        }
                        Node attr = nnm.item(i2);
                        String aname = attr.getNodeName();
                        boolean isPrefix = aname.startsWith("xmlns:");
                        if (isPrefix || aname.equals("xmlns")) {
                            int index = aname.indexOf(58);
                            String p2 = isPrefix ? aname.substring(index + 1) : "";
                            if (p2.equals(prefix)) {
                                namespace = attr.getNodeValue();
                                break;
                            }
                        }
                        i2++;
                    }
                }
            }
        }
        return namespace;
    }

    @Override // com.sun.org.apache.xml.internal.utils.PrefixResolver
    public String getBaseIdentifier() {
        return null;
    }

    @Override // com.sun.org.apache.xml.internal.utils.PrefixResolver
    public boolean handlesNullPrefixes() {
        return false;
    }
}
