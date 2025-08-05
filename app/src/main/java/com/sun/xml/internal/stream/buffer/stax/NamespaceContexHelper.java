package com.sun.xml.internal.stream.buffer.stax;

import com.sun.xml.internal.org.jvnet.staxex.NamespaceContextEx;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/* loaded from: rt.jar:com/sun/xml/internal/stream/buffer/stax/NamespaceContexHelper.class */
public final class NamespaceContexHelper implements NamespaceContextEx {
    private static int DEFAULT_SIZE = 8;
    private int namespacePosition;
    private int contextPosition;
    private String[] prefixes = new String[DEFAULT_SIZE];
    private String[] namespaceURIs = new String[DEFAULT_SIZE];
    private int[] contexts = new int[DEFAULT_SIZE];

    public NamespaceContexHelper() {
        this.prefixes[0] = "xml";
        this.namespaceURIs[0] = "http://www.w3.org/XML/1998/namespace";
        this.prefixes[1] = "xmlns";
        this.namespaceURIs[1] = "http://www.w3.org/2000/xmlns/";
        this.namespacePosition = 2;
    }

    @Override // javax.xml.namespace.NamespaceContext
    public String getNamespaceURI(String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException();
        }
        String prefix2 = prefix.intern();
        for (int i2 = this.namespacePosition - 1; i2 >= 0; i2--) {
            String declaredPrefix = this.prefixes[i2];
            if (declaredPrefix == prefix2) {
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
        int i2 = this.namespacePosition - 1;
        while (i2 >= 0) {
            String declaredNamespaceURI = this.namespaceURIs[i2];
            if (declaredNamespaceURI != namespaceURI && !declaredNamespaceURI.equals(namespaceURI)) {
                i2--;
            } else {
                String declaredPrefix = this.prefixes[i2];
                do {
                    i2++;
                    if (i2 >= this.namespacePosition) {
                        return declaredPrefix;
                    }
                } while (declaredPrefix != this.prefixes[i2]);
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
        List<String> l2 = new ArrayList<>();
        for (int i2 = this.namespacePosition - 1; i2 >= 0; i2--) {
            String declaredNamespaceURI = this.namespaceURIs[i2];
            if (declaredNamespaceURI == namespaceURI || declaredNamespaceURI.equals(namespaceURI)) {
                String declaredPrefix = this.prefixes[i2];
                int j2 = i2 + 1;
                while (true) {
                    if (j2 < this.namespacePosition) {
                        if (declaredPrefix == this.prefixes[j2]) {
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

    @Override // com.sun.xml.internal.org.jvnet.staxex.NamespaceContextEx, java.lang.Iterable, java.util.List
    public Iterator<NamespaceContextEx.Binding> iterator() {
        if (this.namespacePosition == 2) {
            return Collections.EMPTY_LIST.iterator();
        }
        List<NamespaceContextEx.Binding> namespaces = new ArrayList<>(this.namespacePosition);
        for (int i2 = this.namespacePosition - 1; i2 >= 2; i2--) {
            String declaredPrefix = this.prefixes[i2];
            for (int j2 = i2 + 1; j2 < this.namespacePosition && declaredPrefix != this.prefixes[j2]; j2++) {
                namespaces.add(new NamespaceBindingImpl(i2));
            }
        }
        return namespaces.iterator();
    }

    /* loaded from: rt.jar:com/sun/xml/internal/stream/buffer/stax/NamespaceContexHelper$NamespaceBindingImpl.class */
    private final class NamespaceBindingImpl implements NamespaceContextEx.Binding {
        int index;

        NamespaceBindingImpl(int index) {
            this.index = index;
        }

        @Override // com.sun.xml.internal.org.jvnet.staxex.NamespaceContextEx.Binding
        public String getPrefix() {
            return NamespaceContexHelper.this.prefixes[this.index];
        }

        @Override // com.sun.xml.internal.org.jvnet.staxex.NamespaceContextEx.Binding
        public String getNamespaceURI() {
            return NamespaceContexHelper.this.namespaceURIs[this.index];
        }
    }

    public void declareDefaultNamespace(String namespaceURI) {
        declareNamespace("", namespaceURI);
    }

    public void declareNamespace(String prefix, String namespaceURI) {
        if (prefix == null) {
            throw new IllegalArgumentException();
        }
        String prefix2 = prefix.intern();
        if (prefix2 == "xml" || prefix2 == "xmlns") {
            return;
        }
        if (namespaceURI != null) {
            namespaceURI = namespaceURI.intern();
        }
        if (this.namespacePosition == this.namespaceURIs.length) {
            resizeNamespaces();
        }
        this.prefixes[this.namespacePosition] = prefix2;
        String[] strArr = this.namespaceURIs;
        int i2 = this.namespacePosition;
        this.namespacePosition = i2 + 1;
        strArr[i2] = namespaceURI;
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
        iArr[i2] = this.namespacePosition;
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
            this.namespacePosition = iArr[i2];
        }
    }

    public void resetContexts() {
        this.namespacePosition = 2;
    }
}
