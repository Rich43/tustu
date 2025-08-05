package com.sun.corba.se.impl.encoding;

import com.sun.corba.se.spi.ior.iiop.GIOPVersion;

/* loaded from: rt.jar:com/sun/corba/se/impl/encoding/CDRInputStream_1_2.class */
public class CDRInputStream_1_2 extends CDRInputStream_1_1 {
    protected boolean headerPadding;
    protected boolean restoreHeaderPadding;

    @Override // com.sun.corba.se.impl.encoding.CDRInputStream_1_0, com.sun.corba.se.impl.encoding.CDRInputStreamBase
    void setHeaderPadding(boolean z2) {
        this.headerPadding = z2;
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStream_1_0, com.sun.corba.se.impl.encoding.CDRInputStreamBase, java.io.InputStream
    public void mark(int i2) {
        super.mark(i2);
        this.restoreHeaderPadding = this.headerPadding;
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStream_1_0, com.sun.corba.se.impl.encoding.CDRInputStreamBase, java.io.InputStream
    public void reset() {
        super.reset();
        this.headerPadding = this.restoreHeaderPadding;
        this.restoreHeaderPadding = false;
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStream_1_1, com.sun.corba.se.impl.encoding.CDRInputStream_1_0, com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public CDRInputStreamBase dup() {
        CDRInputStreamBase cDRInputStreamBaseDup = super.dup();
        ((CDRInputStream_1_2) cDRInputStreamBaseDup).headerPadding = this.headerPadding;
        return cDRInputStreamBaseDup;
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStream_1_1, com.sun.corba.se.impl.encoding.CDRInputStream_1_0
    protected void alignAndCheck(int i2, int i3) {
        if (this.headerPadding) {
            this.headerPadding = false;
            alignOnBoundary(8);
        }
        checkBlockLength(i2, i3);
        this.bbwi.position(this.bbwi.position() + computeAlignment(this.bbwi.position(), i2));
        if (this.bbwi.position() + i3 > this.bbwi.buflen) {
            grow(1, i3);
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStream_1_1, com.sun.corba.se.impl.encoding.CDRInputStream_1_0, com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public GIOPVersion getGIOPVersion() {
        return GIOPVersion.V1_2;
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStream_1_1, com.sun.corba.se.impl.encoding.CDRInputStream_1_0, com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public char read_wchar() {
        char[] convertedChars = getConvertedChars(read_octet(), getWCharConverter());
        if (getWCharConverter().getNumChars() > 1) {
            throw this.wrapper.btcResultMoreThanOneChar();
        }
        return convertedChars[0];
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStream_1_1, com.sun.corba.se.impl.encoding.CDRInputStream_1_0, com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public String read_wstring() {
        int i2 = read_long();
        if (i2 == 0) {
            return new String("");
        }
        checkForNegativeLength(i2);
        return new String(getConvertedChars(i2, getWCharConverter()), 0, getWCharConverter().getNumChars());
    }
}
