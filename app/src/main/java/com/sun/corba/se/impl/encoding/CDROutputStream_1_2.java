package com.sun.corba.se.impl.encoding;

import com.sun.corba.se.impl.encoding.CodeSetConversion;
import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
import org.omg.CORBA.CompletionStatus;

/* loaded from: rt.jar:com/sun/corba/se/impl/encoding/CDROutputStream_1_2.class */
public class CDROutputStream_1_2 extends CDROutputStream_1_1 {
    protected boolean primitiveAcrossFragmentedChunk = false;
    protected boolean specialChunk = false;
    private boolean headerPadding;

    @Override // com.sun.corba.se.impl.encoding.CDROutputStream_1_0
    protected void handleSpecialChunkBegin(int i2) {
        if (this.inBlock && i2 + this.bbwi.position() > this.bbwi.buflen) {
            int iPosition = this.bbwi.position();
            this.bbwi.position(this.blockSizeIndex - 4);
            writeLongWithoutAlign((iPosition - this.blockSizeIndex) + i2);
            this.bbwi.position(iPosition);
            this.specialChunk = true;
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStream_1_0
    protected void handleSpecialChunkEnd() {
        if (this.inBlock && this.specialChunk) {
            this.inBlock = false;
            this.blockSizeIndex = -1;
            this.blockSizePosition = -1;
            start_block();
            this.specialChunk = false;
        }
    }

    private void checkPrimitiveAcrossFragmentedChunk() {
        if (this.primitiveAcrossFragmentedChunk) {
            this.primitiveAcrossFragmentedChunk = false;
            this.inBlock = false;
            this.blockSizeIndex = -1;
            this.blockSizePosition = -1;
            start_block();
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStream_1_0, com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public void write_octet(byte b2) {
        super.write_octet(b2);
        checkPrimitiveAcrossFragmentedChunk();
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStream_1_0, com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public void write_short(short s2) {
        super.write_short(s2);
        checkPrimitiveAcrossFragmentedChunk();
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStream_1_0, com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public void write_long(int i2) {
        super.write_long(i2);
        checkPrimitiveAcrossFragmentedChunk();
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStream_1_0, com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public void write_longlong(long j2) {
        super.write_longlong(j2);
        checkPrimitiveAcrossFragmentedChunk();
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStream_1_0, com.sun.corba.se.impl.encoding.CDROutputStreamBase
    void setHeaderPadding(boolean z2) {
        this.headerPadding = z2;
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStream_1_1, com.sun.corba.se.impl.encoding.CDROutputStream_1_0
    protected void alignAndReserve(int i2, int i3) {
        if (this.headerPadding) {
            this.headerPadding = false;
            alignOnBoundary(8);
        }
        this.bbwi.position(this.bbwi.position() + computeAlignment(i2));
        if (this.bbwi.position() + i3 > this.bbwi.buflen) {
            grow(i2, i3);
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStream_1_1, com.sun.corba.se.impl.encoding.CDROutputStream_1_0
    protected void grow(int i2, int i3) {
        int iPosition = this.bbwi.position();
        boolean z2 = this.inBlock && !this.specialChunk;
        if (z2) {
            int iPosition2 = this.bbwi.position();
            this.bbwi.position(this.blockSizeIndex - 4);
            writeLongWithoutAlign((iPosition2 - this.blockSizeIndex) + i3);
            this.bbwi.position(iPosition2);
        }
        this.bbwi.needed = i3;
        this.bufferManagerWrite.overflow(this.bbwi);
        if (this.bbwi.fragmented) {
            this.bbwi.fragmented = false;
            this.fragmentOffset += iPosition - this.bbwi.position();
            if (z2) {
                this.primitiveAcrossFragmentedChunk = true;
            }
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStream_1_1, com.sun.corba.se.impl.encoding.CDROutputStream_1_0, com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public GIOPVersion getGIOPVersion() {
        return GIOPVersion.V1_2;
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStream_1_1, com.sun.corba.se.impl.encoding.CDROutputStream_1_0, com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public void write_wchar(char c2) {
        CodeSetConversion.CTBConverter wCharConverter = getWCharConverter();
        wCharConverter.convert(c2);
        handleSpecialChunkBegin(1 + wCharConverter.getNumBytes());
        write_octet((byte) wCharConverter.getNumBytes());
        internalWriteOctetArray(wCharConverter.getBytes(), 0, wCharConverter.getNumBytes());
        handleSpecialChunkEnd();
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStream_1_0, com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public void write_wchar_array(char[] cArr, int i2, int i3) {
        if (cArr == null) {
            throw this.wrapper.nullParam(CompletionStatus.COMPLETED_MAYBE);
        }
        CodeSetConversion.CTBConverter wCharConverter = getWCharConverter();
        int numBytes = 0;
        byte[] bArr = new byte[((int) Math.ceil(wCharConverter.getMaxBytesPerChar() * i3)) + i3];
        for (int i4 = 0; i4 < i3; i4++) {
            wCharConverter.convert(cArr[i2 + i4]);
            int i5 = numBytes;
            int i6 = numBytes + 1;
            bArr[i5] = (byte) wCharConverter.getNumBytes();
            System.arraycopy(wCharConverter.getBytes(), 0, bArr, i6, wCharConverter.getNumBytes());
            numBytes = i6 + wCharConverter.getNumBytes();
        }
        handleSpecialChunkBegin(numBytes);
        internalWriteOctetArray(bArr, 0, numBytes);
        handleSpecialChunkEnd();
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStream_1_1, com.sun.corba.se.impl.encoding.CDROutputStream_1_0, com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public void write_wstring(String str) {
        if (str == null) {
            throw this.wrapper.nullParam(CompletionStatus.COMPLETED_MAYBE);
        }
        if (str.length() == 0) {
            write_long(0);
            return;
        }
        CodeSetConversion.CTBConverter wCharConverter = getWCharConverter();
        wCharConverter.convert(str);
        handleSpecialChunkBegin(computeAlignment(4) + 4 + wCharConverter.getNumBytes());
        write_long(wCharConverter.getNumBytes());
        internalWriteOctetArray(wCharConverter.getBytes(), 0, wCharConverter.getNumBytes());
        handleSpecialChunkEnd();
    }
}
