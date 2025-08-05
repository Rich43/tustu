package com.sun.corba.se.impl.encoding;

import com.sun.corba.se.impl.encoding.CodeSetConversion;
import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
import org.omg.CORBA.CompletionStatus;

/* loaded from: rt.jar:com/sun/corba/se/impl/encoding/CDROutputStream_1_1.class */
public class CDROutputStream_1_1 extends CDROutputStream_1_0 {
    protected int fragmentOffset = 0;

    @Override // com.sun.corba.se.impl.encoding.CDROutputStream_1_0
    protected void alignAndReserve(int i2, int i3) {
        int iComputeAlignment = computeAlignment(i2);
        if (this.bbwi.position() + i3 + iComputeAlignment > this.bbwi.buflen) {
            grow(i2, i3);
            iComputeAlignment = computeAlignment(i2);
        }
        this.bbwi.position(this.bbwi.position() + iComputeAlignment);
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStream_1_0
    protected void grow(int i2, int i3) {
        int iPosition = this.bbwi.position();
        super.grow(i2, i3);
        if (this.bbwi.fragmented) {
            this.bbwi.fragmented = false;
            this.fragmentOffset += iPosition - this.bbwi.position();
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStream_1_0
    public int get_offset() {
        return this.bbwi.position() + this.fragmentOffset;
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStream_1_0, com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public GIOPVersion getGIOPVersion() {
        return GIOPVersion.V1_1;
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStream_1_0, com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public void write_wchar(char c2) {
        CodeSetConversion.CTBConverter wCharConverter = getWCharConverter();
        wCharConverter.convert(c2);
        if (wCharConverter.getNumBytes() != 2) {
            throw this.wrapper.badGiop11Ctb(CompletionStatus.COMPLETED_MAYBE);
        }
        alignAndReserve(wCharConverter.getAlignment(), wCharConverter.getNumBytes());
        this.parent.write_octet_array(wCharConverter.getBytes(), 0, wCharConverter.getNumBytes());
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStream_1_0, com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public void write_wstring(String str) {
        if (str == null) {
            throw this.wrapper.nullParam(CompletionStatus.COMPLETED_MAYBE);
        }
        write_long(str.length() + 1);
        CodeSetConversion.CTBConverter wCharConverter = getWCharConverter();
        wCharConverter.convert(str);
        internalWriteOctetArray(wCharConverter.getBytes(), 0, wCharConverter.getNumBytes());
        write_short((short) 0);
    }
}
