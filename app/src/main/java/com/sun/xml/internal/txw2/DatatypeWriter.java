package com.sun.xml.internal.txw2;

import java.util.AbstractList;
import java.util.Collections;
import java.util.List;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/txw2/DatatypeWriter.class */
public interface DatatypeWriter<DT> {
    public static final List<DatatypeWriter<?>> BUILTIN = Collections.unmodifiableList(new AbstractList() { // from class: com.sun.xml.internal.txw2.DatatypeWriter.1
        private DatatypeWriter<?>[] BUILTIN_ARRAY = {new DatatypeWriter<String>() { // from class: com.sun.xml.internal.txw2.DatatypeWriter.1.1
            @Override // com.sun.xml.internal.txw2.DatatypeWriter
            public Class<String> getType() {
                return String.class;
            }

            @Override // com.sun.xml.internal.txw2.DatatypeWriter
            public void print(String s2, NamespaceResolver resolver, StringBuilder buf) {
                buf.append(s2);
            }
        }, new DatatypeWriter<Integer>() { // from class: com.sun.xml.internal.txw2.DatatypeWriter.1.2
            @Override // com.sun.xml.internal.txw2.DatatypeWriter
            public Class<Integer> getType() {
                return Integer.class;
            }

            @Override // com.sun.xml.internal.txw2.DatatypeWriter
            public void print(Integer i2, NamespaceResolver resolver, StringBuilder buf) {
                buf.append((Object) i2);
            }
        }, new DatatypeWriter<Float>() { // from class: com.sun.xml.internal.txw2.DatatypeWriter.1.3
            @Override // com.sun.xml.internal.txw2.DatatypeWriter
            public Class<Float> getType() {
                return Float.class;
            }

            @Override // com.sun.xml.internal.txw2.DatatypeWriter
            public void print(Float f2, NamespaceResolver resolver, StringBuilder buf) {
                buf.append((Object) f2);
            }
        }, new DatatypeWriter<Double>() { // from class: com.sun.xml.internal.txw2.DatatypeWriter.1.4
            @Override // com.sun.xml.internal.txw2.DatatypeWriter
            public Class<Double> getType() {
                return Double.class;
            }

            @Override // com.sun.xml.internal.txw2.DatatypeWriter
            public void print(Double d2, NamespaceResolver resolver, StringBuilder buf) {
                buf.append((Object) d2);
            }
        }, new DatatypeWriter<QName>() { // from class: com.sun.xml.internal.txw2.DatatypeWriter.1.5
            @Override // com.sun.xml.internal.txw2.DatatypeWriter
            public Class<QName> getType() {
                return QName.class;
            }

            @Override // com.sun.xml.internal.txw2.DatatypeWriter
            public void print(QName qn, NamespaceResolver resolver, StringBuilder buf) {
                String p2 = resolver.getPrefix(qn.getNamespaceURI());
                if (p2.length() != 0) {
                    buf.append(p2).append(':');
                }
                buf.append(qn.getLocalPart());
            }
        }};

        @Override // java.util.AbstractList, java.util.List
        public DatatypeWriter<?> get(int n2) {
            return this.BUILTIN_ARRAY[n2];
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return this.BUILTIN_ARRAY.length;
        }
    });

    Class<DT> getType();

    void print(DT dt, NamespaceResolver namespaceResolver, StringBuilder sb);
}
