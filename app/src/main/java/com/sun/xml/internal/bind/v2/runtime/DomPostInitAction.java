package com.sun.xml.internal.bind.v2.runtime;

import java.util.HashSet;
import java.util.Set;
import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/DomPostInitAction.class */
final class DomPostInitAction implements Runnable {
    private final Node node;
    private final XMLSerializer serializer;

    DomPostInitAction(Node node, XMLSerializer serializer) {
        this.node = node;
        this.serializer = serializer;
    }

    @Override // java.lang.Runnable
    public void run() {
        Set<String> declaredPrefixes = new HashSet<>();
        Node parentNode = this.node;
        while (true) {
            Node n2 = parentNode;
            if (n2 != null && n2.getNodeType() == 1) {
                NamedNodeMap atts = n2.getAttributes();
                if (atts != null) {
                    for (int i2 = 0; i2 < atts.getLength(); i2++) {
                        Attr a2 = (Attr) atts.item(i2);
                        String nsUri = a2.getNamespaceURI();
                        if (nsUri != null && nsUri.equals("http://www.w3.org/2000/xmlns/")) {
                            String prefix = a2.getLocalName();
                            if (prefix != null) {
                                if (prefix.equals("xmlns")) {
                                    prefix = "";
                                }
                                String value = a2.getValue();
                                if (value != null && declaredPrefixes.add(prefix)) {
                                    this.serializer.addInscopeBinding(value, prefix);
                                }
                            }
                        }
                    }
                }
                parentNode = n2.getParentNode();
            } else {
                return;
            }
        }
    }
}
