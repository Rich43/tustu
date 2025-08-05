package com.sun.xml.internal.bind.v2.runtime.unmarshaller;

import com.sun.xml.internal.bind.v2.runtime.output.Pcdata;
import com.sun.xml.internal.bind.v2.runtime.output.UTF8XmlOutput;
import java.io.IOException;
import java.time.Year;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/unmarshaller/IntData.class */
public class IntData extends Pcdata {
    private int data;
    private int length;
    private static final int[] sizeTable = {9, 99, 999, 9999, 99999, 999999, 9999999, 99999999, Year.MAX_VALUE, Integer.MAX_VALUE};

    public void reset(int i2) {
        this.data = i2;
        if (i2 == Integer.MIN_VALUE) {
            this.length = 11;
        } else {
            this.length = i2 < 0 ? stringSizeOfInt(-i2) + 1 : stringSizeOfInt(i2);
        }
    }

    private static int stringSizeOfInt(int x2) {
        int i2 = 0;
        while (x2 > sizeTable[i2]) {
            i2++;
        }
        return i2 + 1;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.output.Pcdata, java.lang.CharSequence
    public String toString() {
        return String.valueOf(this.data);
    }

    @Override // java.lang.CharSequence
    public int length() {
        return this.length;
    }

    @Override // java.lang.CharSequence
    public char charAt(int index) {
        return toString().charAt(index);
    }

    @Override // java.lang.CharSequence
    public CharSequence subSequence(int start, int end) {
        return toString().substring(start, end);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.output.Pcdata
    public void writeTo(UTF8XmlOutput output) throws IOException {
        output.text(this.data);
    }
}
