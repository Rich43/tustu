package com.sun.xml.internal.fastinfoset.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.namespace.NamespaceContext;

/* loaded from: rt.jar:com/sun/xml/internal/fastinfoset/util/NamespaceContextImplementation.class */
public final class NamespaceContextImplementation implements NamespaceContext {
    private static int DEFAULT_SIZE = 8;
    private int namespacePosition;
    private int contextPosition;
    private int currentContext;
    private String[] prefixes = new String[DEFAULT_SIZE];
    private String[] namespaceURIs = new String[DEFAULT_SIZE];
    private int[] contexts = new int[DEFAULT_SIZE];

    public NamespaceContextImplementation() {
        this.prefixes[0] = "xml";
        this.namespaceURIs[0] = "http://www.w3.org/XML/1998/namespace";
        this.prefixes[1] = "xmlns";
        this.namespaceURIs[1] = "http://www.w3.org/2000/xmlns/";
        this.namespacePosition = 2;
        this.currentContext = 2;
    }

    @Override // javax.xml.namespace.NamespaceContext
    public String getNamespaceURI(String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException();
        }
        for (int i2 = this.namespacePosition - 1; i2 >= 0; i2--) {
            String declaredPrefix = this.prefixes[i2];
            if (declaredPrefix.equals(prefix)) {
                return this.namespaceURIs[i2];
            }
        }
        return "";
    }

    @Override // javax.xml.namespace.NamespaceContext
    public String getPrefix(String namespaceURI) {
        if (namespaceURI == null) {
            throw new IllegalArgumentException();
        }
        for (int i2 = this.namespacePosition - 1; i2 >= 0; i2--) {
            String declaredNamespaceURI = this.namespaceURIs[i2];
            if (declaredNamespaceURI.equals(namespaceURI)) {
                String declaredPrefix = this.prefixes[i2];
                boolean isOutOfScope = false;
                int j2 = i2 + 1;
                while (true) {
                    if (j2 >= this.namespacePosition) {
                        break;
                    }
                    if (!declaredPrefix.equals(this.prefixes[j2])) {
                        j2++;
                    } else {
                        isOutOfScope = true;
                        break;
                    }
                }
                if (!isOutOfScope) {
                    return declaredPrefix;
                }
            }
        }
        return null;
    }

    public String getNonDefaultPrefix(String namespaceURI) {
        if (namespaceURI == null) {
            throw new IllegalArgumentException();
        }
        int i2 = this.namespacePosition - 1;
        while (i2 >= 0) {
            String declaredNamespaceURI = this.namespaceURIs[i2];
            if (!declaredNamespaceURI.equals(namespaceURI) || this.prefixes[i2].length() <= 0) {
                i2--;
            } else {
                String declaredPrefix = this.prefixes[i2];
                do {
                    i2++;
                    if (i2 >= this.namespacePosition) {
                        return declaredPrefix;
                    }
                } while (!declaredPrefix.equals(this.prefixes[i2]));
                return null;
            }
        }
        return null;
    }

    @Override // javax.xml.namespace.NamespaceContext
    public Iterator getPrefixes(String namespaceURI) {
        if (namespaceURI == null) {
            throw new IllegalArgumentException();
        }
        List l2 = new ArrayList();
        for (int i2 = this.namespacePosition - 1; i2 >= 0; i2--) {
            String declaredNamespaceURI = this.namespaceURIs[i2];
            if (declaredNamespaceURI.equals(namespaceURI)) {
                String declaredPrefix = this.prefixes[i2];
                int j2 = i2 + 1;
                while (true) {
                    if (j2 < this.namespacePosition) {
                        if (declaredPrefix.equals(this.prefixes[j2])) {
                            break;
                        }
                        j2++;
                    } else {
                        l2.add(declaredPrefix);
                        break;
                    }
                }
            }
        }
        return l2.iterator();
    }

    public String getPrefix(int index) {
        return this.prefixes[index];
    }

    public String getNamespaceURI(int index) {
        return this.namespaceURIs[index];
    }

    public int getCurrentContextStartIndex() {
        return this.currentContext;
    }

    public int getCurrentContextEndIndex() {
        return this.namespacePosition;
    }

    public boolean isCurrentContextEmpty() {
        return this.currentContext == this.namespacePosition;
    }

    public void declarePrefix(String prefix, String namespaceURI) {
        String prefix2 = prefix.intern();
        String namespaceURI2 = namespaceURI.intern();
        if (prefix2 == "xml" || prefix2 == "xmlns") {
            return;
        }
        for (int i2 = this.currentContext; i2 < this.namespacePosition; i2++) {
            String declaredPrefix = this.prefixes[i2];
            if (declaredPrefix == prefix2) {
                this.prefixes[i2] = prefix2;
                this.namespaceURIs[i2] = namespaceURI2;
                return;
            }
        }
        if (this.namespacePosition == this.namespaceURIs.length) {
            resizeNamespaces();
        }
        this.prefixes[this.namespacePosition] = prefix2;
        String[] strArr = this.namespaceURIs;
        int i3 = this.namespacePosition;
        this.namespacePosition = i3 + 1;
        strArr[i3] = namespaceURI2;
    }

    private void resizeNamespaces() {
        int newLength = ((this.namespaceURIs.length * 3) / 2) + 1;
        String[] newPrefixes = new String[newLength];
        System.arraycopy(this.prefixes, 0, newPrefixes, 0, this.prefixes.length);
        this.prefixes = newPrefixes;
        String[] newNamespaceURIs = new String[newLength];
        System.arraycopy(this.namespaceURIs, 0, newNamespaceURIs, 0, this.namespaceURIs.length);
        this.namespaceURIs = newNamespaceURIs;
    }

    public void pushContext() {
        if (this.contextPosition == this.contexts.length) {
            resizeContexts();
        }
        int[] iArr = this.contexts;
        int i2 = this.contextPosition;
        this.contextPosition = i2 + 1;
        int i3 = this.namespacePosition;
        this.currentContext = i3;
        iArr[i2] = i3;
    }

    private void resizeContexts() {
        int[] newContexts = new int[((this.contexts.length * 3) / 2) + 1];
        System.arraycopy(this.contexts, 0, newContexts, 0, this.contexts.length);
        this.contexts = newContexts;
    }

    public void popContext() {
        if (this.contextPosition > 0) {
            int[] iArr = this.contexts;
            int i2 = this.contextPosition - 1;
            this.contextPosition = i2;
            int i3 = iArr[i2];
            this.currentContext = i3;
            this.namespacePosition = i3;
        }
    }

    public void reset() {
        this.namespacePosition = 2;
        this.currentContext = 2;
    }
}
