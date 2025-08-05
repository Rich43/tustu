package org.xml.sax.helpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EmptyStackException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: rt.jar:org/xml/sax/helpers/NamespaceSupport.class */
public class NamespaceSupport {
    public static final String XMLNS = "http://www.w3.org/XML/1998/namespace";
    public static final String NSDECL = "http://www.w3.org/xmlns/2000/";
    private static final Enumeration EMPTY_ENUMERATION = Collections.enumeration(new ArrayList());
    private Context[] contexts;
    private Context currentContext;
    private int contextPos;
    private boolean namespaceDeclUris;

    public NamespaceSupport() {
        reset();
    }

    public void reset() {
        this.contexts = new Context[32];
        this.namespaceDeclUris = false;
        this.contextPos = 0;
        Context[] contextArr = this.contexts;
        int i2 = this.contextPos;
        Context context = new Context();
        this.currentContext = context;
        contextArr[i2] = context;
        this.currentContext.declarePrefix("xml", "http://www.w3.org/XML/1998/namespace");
    }

    public void pushContext() {
        int max = this.contexts.length;
        this.contextPos++;
        if (this.contextPos >= max) {
            Context[] newContexts = new Context[max * 2];
            System.arraycopy(this.contexts, 0, newContexts, 0, max);
            int i2 = max * 2;
            this.contexts = newContexts;
        }
        this.currentContext = this.contexts[this.contextPos];
        if (this.currentContext == null) {
            Context[] contextArr = this.contexts;
            int i3 = this.contextPos;
            Context context = new Context();
            this.currentContext = context;
            contextArr[i3] = context;
        }
        if (this.contextPos > 0) {
            this.currentContext.setParent(this.contexts[this.contextPos - 1]);
        }
    }

    public void popContext() {
        this.contexts[this.contextPos].clear();
        this.contextPos--;
        if (this.contextPos < 0) {
            throw new EmptyStackException();
        }
        this.currentContext = this.contexts[this.contextPos];
    }

    public boolean declarePrefix(String prefix, String uri) {
        if (prefix.equals("xml") || prefix.equals("xmlns")) {
            return false;
        }
        this.currentContext.declarePrefix(prefix, uri);
        return true;
    }

    public String[] processName(String qName, String[] parts, boolean isAttribute) {
        String[] myParts = this.currentContext.processName(qName, isAttribute);
        if (myParts == null) {
            return null;
        }
        parts[0] = myParts[0];
        parts[1] = myParts[1];
        parts[2] = myParts[2];
        return parts;
    }

    public String getURI(String prefix) {
        return this.currentContext.getURI(prefix);
    }

    public Enumeration getPrefixes() {
        return this.currentContext.getPrefixes();
    }

    public String getPrefix(String uri) {
        return this.currentContext.getPrefix(uri);
    }

    public Enumeration getPrefixes(String uri) {
        List<String> prefixes = new ArrayList<>();
        Enumeration allPrefixes = getPrefixes();
        while (allPrefixes.hasMoreElements()) {
            String prefix = (String) allPrefixes.nextElement2();
            if (uri.equals(getURI(prefix))) {
                prefixes.add(prefix);
            }
        }
        return Collections.enumeration(prefixes);
    }

    public Enumeration getDeclaredPrefixes() {
        return this.currentContext.getDeclaredPrefixes();
    }

    public void setNamespaceDeclUris(boolean value) {
        if (this.contextPos != 0) {
            throw new IllegalStateException();
        }
        if (value == this.namespaceDeclUris) {
            return;
        }
        this.namespaceDeclUris = value;
        if (value) {
            this.currentContext.declarePrefix("xmlns", "http://www.w3.org/xmlns/2000/");
            return;
        }
        Context[] contextArr = this.contexts;
        int i2 = this.contextPos;
        Context context = new Context();
        this.currentContext = context;
        contextArr[i2] = context;
        this.currentContext.declarePrefix("xml", "http://www.w3.org/XML/1998/namespace");
    }

    public boolean isNamespaceDeclUris() {
        return this.namespaceDeclUris;
    }

    /* loaded from: rt.jar:org/xml/sax/helpers/NamespaceSupport$Context.class */
    final class Context {
        Map<String, String> prefixTable;
        Map<String, String> uriTable;
        Map<String, String[]> elementNameTable;
        Map<String, String[]> attributeNameTable;
        String defaultNS = null;
        private List<String> declarations = null;
        private boolean declSeen = false;
        private Context parent = null;

        Context() {
            copyTables();
        }

        void setParent(Context parent) {
            this.parent = parent;
            this.declarations = null;
            this.prefixTable = parent.prefixTable;
            this.uriTable = parent.uriTable;
            this.elementNameTable = parent.elementNameTable;
            this.attributeNameTable = parent.attributeNameTable;
            this.defaultNS = parent.defaultNS;
            this.declSeen = false;
        }

        void clear() {
            this.parent = null;
            this.prefixTable = null;
            this.uriTable = null;
            this.elementNameTable = null;
            this.attributeNameTable = null;
            this.defaultNS = null;
        }

        void declarePrefix(String prefix, String uri) {
            if (!this.declSeen) {
                copyTables();
            }
            if (this.declarations == null) {
                this.declarations = new ArrayList();
            }
            String prefix2 = prefix.intern();
            String uri2 = uri.intern();
            if ("".equals(prefix2)) {
                if ("".equals(uri2)) {
                    this.defaultNS = null;
                } else {
                    this.defaultNS = uri2;
                }
            } else {
                this.prefixTable.put(prefix2, uri2);
                this.uriTable.put(uri2, prefix2);
            }
            this.declarations.add(prefix2);
        }

        String[] processName(String qName, boolean isAttribute) {
            Map<String, String[]> table;
            String uri;
            if (isAttribute) {
                table = this.attributeNameTable;
            } else {
                table = this.elementNameTable;
            }
            String[] name = table.get(qName);
            if (name != null) {
                return name;
            }
            String[] name2 = new String[3];
            name2[2] = qName.intern();
            int index = qName.indexOf(58);
            if (index == -1) {
                if (isAttribute) {
                    if (qName == "xmlns" && NamespaceSupport.this.namespaceDeclUris) {
                        name2[0] = "http://www.w3.org/xmlns/2000/";
                    } else {
                        name2[0] = "";
                    }
                } else if (this.defaultNS == null) {
                    name2[0] = "";
                } else {
                    name2[0] = this.defaultNS;
                }
                name2[1] = name2[2];
            } else {
                String prefix = qName.substring(0, index);
                String local = qName.substring(index + 1);
                if ("".equals(prefix)) {
                    uri = this.defaultNS;
                } else {
                    uri = this.prefixTable.get(prefix);
                }
                if (uri != null) {
                    if (!isAttribute && "xmlns".equals(prefix)) {
                        return null;
                    }
                    name2[0] = uri;
                    name2[1] = local.intern();
                } else {
                    return null;
                }
            }
            table.put(name2[2], name2);
            return name2;
        }

        String getURI(String prefix) {
            if ("".equals(prefix)) {
                return this.defaultNS;
            }
            if (this.prefixTable == null) {
                return null;
            }
            return this.prefixTable.get(prefix);
        }

        String getPrefix(String uri) {
            if (this.uriTable == null) {
                return null;
            }
            return this.uriTable.get(uri);
        }

        Enumeration getDeclaredPrefixes() {
            if (this.declarations == null) {
                return NamespaceSupport.EMPTY_ENUMERATION;
            }
            return Collections.enumeration(this.declarations);
        }

        Enumeration getPrefixes() {
            if (this.prefixTable == null) {
                return NamespaceSupport.EMPTY_ENUMERATION;
            }
            return Collections.enumeration(this.prefixTable.keySet());
        }

        private void copyTables() {
            if (this.prefixTable != null) {
                this.prefixTable = new HashMap(this.prefixTable);
            } else {
                this.prefixTable = new HashMap();
            }
            if (this.uriTable != null) {
                this.uriTable = new HashMap(this.uriTable);
            } else {
                this.uriTable = new HashMap();
            }
            this.elementNameTable = new HashMap();
            this.attributeNameTable = new HashMap();
            this.declSeen = true;
        }
    }
}
