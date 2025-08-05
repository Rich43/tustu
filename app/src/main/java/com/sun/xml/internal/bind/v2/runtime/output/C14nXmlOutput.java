package com.sun.xml.internal.bind.v2.runtime.output;

import com.sun.istack.internal.FinalArrayList;
import com.sun.xml.internal.bind.marshaller.CharacterEscapeHandler;
import com.sun.xml.internal.bind.v2.runtime.Name;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collections;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/output/C14nXmlOutput.class */
public class C14nXmlOutput extends UTF8XmlOutput {
    private StaticAttribute[] staticAttributes;
    private int len;
    private int[] nsBuf;
    private final FinalArrayList<DynamicAttribute> otherAttributes;
    private final boolean namedAttributesAreOrdered;

    public C14nXmlOutput(OutputStream out, Encoded[] localNames, boolean namedAttributesAreOrdered, CharacterEscapeHandler escapeHandler) {
        super(out, localNames, escapeHandler);
        this.staticAttributes = new StaticAttribute[8];
        this.len = 0;
        this.nsBuf = new int[8];
        this.otherAttributes = new FinalArrayList<>();
        this.namedAttributesAreOrdered = namedAttributesAreOrdered;
        for (int i2 = 0; i2 < this.staticAttributes.length; i2++) {
            this.staticAttributes[i2] = new StaticAttribute();
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/output/C14nXmlOutput$StaticAttribute.class */
    final class StaticAttribute implements Comparable<StaticAttribute> {
        Name name;
        String value;

        StaticAttribute() {
        }

        public void set(Name name, String value) {
            this.name = name;
            this.value = value;
        }

        void write() throws IOException {
            C14nXmlOutput.super.attribute(this.name, this.value);
        }

        DynamicAttribute toDynamicAttribute() {
            int prefix;
            int nsUriIndex = this.name.nsUriIndex;
            if (nsUriIndex == -1) {
                prefix = -1;
            } else {
                prefix = C14nXmlOutput.this.nsUriIndex2prefixIndex[nsUriIndex];
            }
            return C14nXmlOutput.this.new DynamicAttribute(prefix, this.name.localName, this.value);
        }

        @Override // java.lang.Comparable
        public int compareTo(StaticAttribute that) {
            return this.name.compareTo(that.name);
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/output/C14nXmlOutput$DynamicAttribute.class */
    final class DynamicAttribute implements Comparable<DynamicAttribute> {
        final int prefix;
        final String localName;
        final String value;

        public DynamicAttribute(int prefix, String localName, String value) {
            this.prefix = prefix;
            this.localName = localName;
            this.value = value;
        }

        private String getURI() {
            return this.prefix == -1 ? "" : C14nXmlOutput.this.nsContext.getNamespaceURI(this.prefix);
        }

        @Override // java.lang.Comparable
        public int compareTo(DynamicAttribute that) {
            int r2 = getURI().compareTo(that.getURI());
            return r2 != 0 ? r2 : this.localName.compareTo(that.localName);
        }
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.output.UTF8XmlOutput, com.sun.xml.internal.bind.v2.runtime.output.XmlOutputAbstractImpl, com.sun.xml.internal.bind.v2.runtime.output.XmlOutput
    public void attribute(Name name, String value) throws IOException {
        if (this.staticAttributes.length == this.len) {
            int newLen = this.len * 2;
            StaticAttribute[] newbuf = new StaticAttribute[newLen];
            System.arraycopy(this.staticAttributes, 0, newbuf, 0, this.len);
            for (int i2 = this.len; i2 < newLen; i2++) {
                this.staticAttributes[i2] = new StaticAttribute();
            }
            this.staticAttributes = newbuf;
        }
        StaticAttribute[] staticAttributeArr = this.staticAttributes;
        int i3 = this.len;
        this.len = i3 + 1;
        staticAttributeArr[i3].set(name, value);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.output.UTF8XmlOutput, com.sun.xml.internal.bind.v2.runtime.output.XmlOutputAbstractImpl, com.sun.xml.internal.bind.v2.runtime.output.XmlOutput
    public void attribute(int prefix, String localName, String value) throws IOException {
        this.otherAttributes.add(new DynamicAttribute(prefix, localName, value));
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.output.UTF8XmlOutput, com.sun.xml.internal.bind.v2.runtime.output.XmlOutputAbstractImpl, com.sun.xml.internal.bind.v2.runtime.output.XmlOutput
    public void endStartTag() throws IOException {
        if (this.otherAttributes.isEmpty()) {
            if (this.len != 0) {
                if (!this.namedAttributesAreOrdered) {
                    Arrays.sort(this.staticAttributes, 0, this.len);
                }
                for (int i2 = 0; i2 < this.len; i2++) {
                    this.staticAttributes[i2].write();
                }
                this.len = 0;
            }
        } else {
            for (int i3 = 0; i3 < this.len; i3++) {
                this.otherAttributes.add(this.staticAttributes[i3].toDynamicAttribute());
            }
            this.len = 0;
            Collections.sort(this.otherAttributes);
            int size = this.otherAttributes.size();
            for (int i4 = 0; i4 < size; i4++) {
                DynamicAttribute a2 = this.otherAttributes.get(i4);
                super.attribute(a2.prefix, a2.localName, a2.value);
            }
            this.otherAttributes.clear();
        }
        super.endStartTag();
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.output.UTF8XmlOutput
    protected void writeNsDecls(int base) throws IOException {
        int count = this.nsContext.getCurrent().count();
        if (count == 0) {
            return;
        }
        if (count > this.nsBuf.length) {
            this.nsBuf = new int[count];
        }
        for (int i2 = count - 1; i2 >= 0; i2--) {
            this.nsBuf[i2] = base + i2;
        }
        for (int i3 = 0; i3 < count; i3++) {
            for (int j2 = i3 + 1; j2 < count; j2++) {
                String p2 = this.nsContext.getPrefix(this.nsBuf[i3]);
                String q2 = this.nsContext.getPrefix(this.nsBuf[j2]);
                if (p2.compareTo(q2) > 0) {
                    int t2 = this.nsBuf[j2];
                    this.nsBuf[j2] = this.nsBuf[i3];
                    this.nsBuf[i3] = t2;
                }
            }
        }
        for (int i4 = 0; i4 < count; i4++) {
            writeNsDecl(this.nsBuf[i4]);
        }
    }
}
