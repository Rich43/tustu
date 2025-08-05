package com.sun.org.apache.xml.internal.utils;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/utils/Hashtree2Node.class */
public abstract class Hashtree2Node {
    public static void appendHashToNode(Hashtable hash, String name, Node container, Document factory) {
        String elemName;
        if (null == container || null == factory || null == hash) {
            return;
        }
        if (null == name || "".equals(name)) {
            elemName = "appendHashToNode";
        } else {
            elemName = name;
        }
        try {
            Element hashNode = factory.createElement(elemName);
            container.appendChild(hashNode);
            Enumeration keys = hash.keys();
            Vector v2 = new Vector();
            while (keys.hasMoreElements()) {
                Object key = keys.nextElement2();
                String keyStr = key.toString();
                Object item = hash.get(key);
                if (item instanceof Hashtable) {
                    v2.addElement(keyStr);
                    v2.addElement((Hashtable) item);
                } else {
                    try {
                        Element node = factory.createElement("item");
                        node.setAttribute("key", keyStr);
                        node.appendChild(factory.createTextNode((String) item));
                        hashNode.appendChild(node);
                    } catch (Exception e2) {
                        Element node2 = factory.createElement("item");
                        node2.setAttribute("key", keyStr);
                        node2.appendChild(factory.createTextNode("ERROR: Reading " + key + " threw: " + e2.toString()));
                        hashNode.appendChild(node2);
                    }
                }
            }
            Enumeration keys2 = v2.elements();
            while (keys2.hasMoreElements()) {
                String n2 = (String) keys2.nextElement2();
                Hashtable h2 = (Hashtable) keys2.nextElement2();
                appendHashToNode(h2, n2, hashNode, factory);
            }
        } catch (Exception e22) {
            e22.printStackTrace();
        }
    }
}
