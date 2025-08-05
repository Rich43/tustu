package com.sun.corba.se.impl.encoding;

import com.sun.corba.se.impl.encoding.CDRInputStream_1_0;
import com.sun.corba.se.spi.ior.iiop.GIOPVersion;

/* loaded from: rt.jar:com/sun/corba/se/impl/encoding/CDRInputStream_1_1.class */
public class CDRInputStream_1_1 extends CDRInputStream_1_0 {
    protected int fragmentOffset = 0;

    @Override // com.sun.corba.se.impl.encoding.CDRInputStream_1_0, com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public GIOPVersion getGIOPVersion() {
        return GIOPVersion.V1_1;
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStream_1_0, com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public CDRInputStreamBase dup() {
        CDRInputStreamBase cDRInputStreamBaseDup = super.dup();
        ((CDRInputStream_1_1) cDRInputStreamBaseDup).fragmentOffset = this.fragmentOffset;
        return cDRInputStreamBaseDup;
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStream_1_0
    protected int get_offset() {
        return this.bbwi.position() + this.fragmentOffset;
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStream_1_0
    protected void alignAndCheck(int i2, int i3) {
        checkBlockLength(i2, i3);
        int iComputeAlignment = computeAlignment(this.bbwi.position(), i2);
        if (this.bbwi.position() + i3 + iComputeAlignment > this.bbwi.buflen) {
            if (this.bbwi.position() + iComputeAlignment == this.bbwi.buflen) {
                this.bbwi.position(this.bbwi.position() + iComputeAlignment);
            }
            grow(i2, i3);
            iComputeAlignment = computeAlignment(this.bbwi.position(), i2);
        }
        this.bbwi.position(this.bbwi.position() + iComputeAlignment);
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStream_1_0
    protected void grow(int i2, int i3) {
        this.bbwi.needed = i3;
        int iPosition = this.bbwi.position();
        this.bbwi = this.bufferManagerRead.underflow(this.bbwi);
        if (this.bbwi.fragmented) {
            this.fragmentOffset += iPosition - this.bbwi.position();
            this.markAndResetHandler.fragmentationOccured(this.bbwi);
            this.bbwi.fragmented = false;
        }
    }

    /* loaded from: rt.jar:com/sun/corba/se/impl/encoding/CDRInputStream_1_1$FragmentableStreamMemento.class */
    private class FragmentableStreamMemento extends CDRInputStream_1_0.StreamMemento {
        private int fragmentOffset_;

        public FragmentableStreamMemento() {
            super();
            this.fragmentOffset_ = CDRInputStream_1_1.this.fragmentOffset;
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStream_1_0, com.sun.corba.se.impl.encoding.RestorableInputStream
    public Object createStreamMemento() {
        return new FragmentableStreamMemento();
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStream_1_0, com.sun.corba.se.impl.encoding.RestorableInputStream
    public void restoreInternalState(Object obj) {
        super.restoreInternalState(obj);
        this.fragmentOffset = ((FragmentableStreamMemento) obj).fragmentOffset_;
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStream_1_0, com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public char read_wchar() {
        alignAndCheck(2, 2);
        char[] convertedChars = getConvertedChars(2, getWCharConverter());
        if (getWCharConverter().getNumChars() > 1) {
            throw this.wrapper.btcResultMoreThanOneChar();
        }
        return convertedChars[0];
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStream_1_0, com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public String read_wstring() {
        int i2 = read_long();
        if (i2 == 0) {
            return new String("");
        }
        checkForNegativeLength(i2);
        char[] convertedChars = getConvertedChars((i2 - 1) * 2, getWCharConverter());
        read_short();
        return new String(convertedChars, 0, getWCharConverter().getNumChars());
    }
}
