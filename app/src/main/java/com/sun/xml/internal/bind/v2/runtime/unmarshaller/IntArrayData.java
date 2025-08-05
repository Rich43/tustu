package com.sun.xml.internal.bind.v2.runtime.unmarshaller;

import com.sun.xml.internal.bind.v2.runtime.output.Pcdata;
import com.sun.xml.internal.bind.v2.runtime.output.UTF8XmlOutput;
import java.io.IOException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/unmarshaller/IntArrayData.class */
public final class IntArrayData extends Pcdata {
    private int[] data;
    private int start;
    private int len;
    private StringBuilder literal;

    public IntArrayData(int[] data, int start, int len) {
        set(data, start, len);
    }

    public IntArrayData() {
    }

    public void set(int[] data, int start, int len) {
        this.data = data;
        this.start = start;
        this.len = len;
        this.literal = null;
    }

    @Override // java.lang.CharSequence
    public int length() {
        return getLiteral().length();
    }

    @Override // java.lang.CharSequence
    public char charAt(int index) {
        return getLiteral().charAt(index);
    }

    @Override // java.lang.CharSequence
    public CharSequence subSequence(int start, int end) {
        return getLiteral().subSequence(start, end);
    }

    private StringBuilder getLiteral() {
        if (this.literal != null) {
            return this.literal;
        }
        this.literal = new StringBuilder();
        int p2 = this.start;
        for (int i2 = this.len; i2 > 0; i2--) {
            if (this.literal.length() > 0) {
                this.literal.append(' ');
            }
            int i3 = p2;
            p2++;
            this.literal.append(this.data[i3]);
        }
        return this.literal;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.output.Pcdata, java.lang.CharSequence
    public String toString() {
        return this.literal.toString();
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.output.Pcdata
    public void writeTo(UTF8XmlOutput output) throws IOException {
        int p2 = this.start;
        for (int i2 = this.len; i2 > 0; i2--) {
            if (i2 != this.len) {
                output.write(32);
            }
            int i3 = p2;
            p2++;
            output.text(this.data[i3]);
        }
    }
}
