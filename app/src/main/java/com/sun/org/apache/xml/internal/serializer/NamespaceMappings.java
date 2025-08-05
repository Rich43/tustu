package com.sun.org.apache.xml.internal.serializer;

import com.sun.org.apache.xalan.internal.templates.Constants;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/serializer/NamespaceMappings.class */
public class NamespaceMappings {
    private int count;
    private HashMap m_namespaces = new HashMap();
    private Stack m_nodeStack = new Stack();
    private static final String EMPTYSTRING = "";
    private static final String XML_PREFIX = "xml";

    public NamespaceMappings() {
        initNamespaces();
    }

    private void initNamespaces() {
        HashMap map = this.m_namespaces;
        Stack stack = new Stack();
        map.put("", stack);
        stack.push(new MappingRecord("", "", 0));
        HashMap map2 = this.m_namespaces;
        Stack stack2 = new Stack();
        map2.put("xml", stack2);
        stack2.push(new MappingRecord("xml", "http://www.w3.org/XML/1998/namespace", 0));
        this.m_nodeStack.push(new MappingRecord(null, null, -1));
    }

    public String lookupNamespace(String prefix) {
        Stack stack = (Stack) this.m_namespaces.get(prefix);
        if (stack == null || stack.isEmpty()) {
            return null;
        }
        return ((MappingRecord) stack.peek()).m_uri;
    }

    MappingRecord getMappingFromPrefix(String prefix) {
        Stack stack = (Stack) this.m_namespaces.get(prefix);
        if (stack == null || stack.isEmpty()) {
            return null;
        }
        return (MappingRecord) stack.peek();
    }

    public String lookupPrefix(String uri) {
        String foundPrefix = null;
        Iterator<String> itr = this.m_namespaces.keySet().iterator();
        while (true) {
            if (!itr.hasNext()) {
                break;
            }
            String prefix = itr.next();
            String uri2 = lookupNamespace(prefix);
            if (uri2 != null && uri2.equals(uri)) {
                foundPrefix = prefix;
                break;
            }
        }
        return foundPrefix;
    }

    MappingRecord getMappingFromURI(String uri) {
        MappingRecord foundMap = null;
        Iterator<String> itr = this.m_namespaces.keySet().iterator();
        while (true) {
            if (!itr.hasNext()) {
                break;
            }
            String prefix = itr.next();
            MappingRecord map2 = getMappingFromPrefix(prefix);
            if (map2 != null && map2.m_uri.equals(uri)) {
                foundMap = map2;
                break;
            }
        }
        return foundMap;
    }

    boolean popNamespace(String prefix) {
        Stack stack;
        if (!prefix.startsWith("xml") && (stack = (Stack) this.m_namespaces.get(prefix)) != null) {
            stack.pop();
            return true;
        }
        return false;
    }

    boolean pushNamespace(String prefix, String uri, int elemDepth) {
        if (prefix.startsWith("xml")) {
            return false;
        }
        Stack stack = (Stack) this.m_namespaces.get(prefix);
        Stack stack2 = stack;
        if (stack == null) {
            HashMap map = this.m_namespaces;
            Stack stack3 = new Stack();
            stack2 = stack3;
            map.put(prefix, stack3);
        }
        if (!stack2.empty() && uri.equals(((MappingRecord) stack2.peek()).m_uri)) {
            return false;
        }
        MappingRecord map2 = new MappingRecord(prefix, uri, elemDepth);
        stack2.push(map2);
        this.m_nodeStack.push(map2);
        return true;
    }

    void popNamespaces(int elemDepth, ContentHandler saxHandler) {
        while (!this.m_nodeStack.isEmpty()) {
            MappingRecord map = (MappingRecord) this.m_nodeStack.peek();
            int depth = map.m_declarationDepth;
            if (depth < elemDepth) {
                return;
            }
            MappingRecord map2 = (MappingRecord) this.m_nodeStack.pop();
            String prefix = map2.m_prefix;
            popNamespace(prefix);
            if (saxHandler != null) {
                try {
                    saxHandler.endPrefixMapping(prefix);
                } catch (SAXException e2) {
                }
            }
        }
    }

    public String generateNextPrefix() {
        StringBuilder sbAppend = new StringBuilder().append(Constants.ATTRNAME_NS);
        int i2 = this.count;
        this.count = i2 + 1;
        return sbAppend.append(i2).toString();
    }

    public Object clone() throws CloneNotSupportedException {
        NamespaceMappings clone = new NamespaceMappings();
        clone.m_nodeStack = (Stack) this.m_nodeStack.clone();
        clone.m_namespaces = (HashMap) this.m_namespaces.clone();
        clone.count = this.count;
        return clone;
    }

    final void reset() {
        this.count = 0;
        this.m_namespaces.clear();
        this.m_nodeStack.clear();
        initNamespaces();
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/serializer/NamespaceMappings$MappingRecord.class */
    class MappingRecord {
        final String m_prefix;
        final String m_uri;
        final int m_declarationDepth;

        MappingRecord(String prefix, String uri, int depth) {
            this.m_prefix = prefix;
            this.m_uri = uri;
            this.m_declarationDepth = depth;
        }
    }
}
