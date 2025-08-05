package com.sun.corba.se.impl.encoding;

import com.sun.corba.se.impl.encoding.CodeSetComponentInfo;
import com.sun.corba.se.impl.encoding.OSFCodeSetRegistry;
import com.sun.corba.se.impl.logging.OMGSystemException;
import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.MalformedInputException;
import java.nio.charset.UnmappableCharacterException;
import java.nio.charset.UnsupportedCharsetException;

/* loaded from: rt.jar:com/sun/corba/se/impl/encoding/CodeSetConversion.class */
public class CodeSetConversion {
    private static CodeSetConversion implementation;
    private static final int FALLBACK_CODESET = 0;
    private CodeSetCache cache;

    /* loaded from: rt.jar:com/sun/corba/se/impl/encoding/CodeSetConversion$BTCConverter.class */
    public static abstract class BTCConverter {
        public abstract boolean isFixedWidthEncoding();

        public abstract int getFixedCharWidth();

        public abstract int getNumChars();

        public abstract char[] getChars(byte[] bArr, int i2, int i3);
    }

    /* loaded from: rt.jar:com/sun/corba/se/impl/encoding/CodeSetConversion$CTBConverter.class */
    public static abstract class CTBConverter {
        public abstract void convert(char c2);

        public abstract void convert(String str);

        public abstract int getNumBytes();

        public abstract float getMaxBytesPerChar();

        public abstract boolean isFixedWidthEncoding();

        public abstract int getAlignment();

        public abstract byte[] getBytes();
    }

    /* loaded from: rt.jar:com/sun/corba/se/impl/encoding/CodeSetConversion$JavaCTBConverter.class */
    private class JavaCTBConverter extends CTBConverter {
        private CharsetEncoder ctb;
        private int alignment;
        private ByteBuffer buffer;
        private OSFCodeSetRegistry.Entry codeset;
        private ORBUtilSystemException wrapper = ORBUtilSystemException.get(CORBALogDomains.RPC_ENCODING);
        private OMGSystemException omgWrapper = OMGSystemException.get(CORBALogDomains.RPC_ENCODING);
        private char[] chars = null;
        private int numBytes = 0;
        private int numChars = 0;

        public JavaCTBConverter(OSFCodeSetRegistry.Entry entry, int i2) {
            try {
                this.ctb = CodeSetConversion.this.cache.getCharToByteConverter(entry.getName());
                if (this.ctb == null) {
                    this.ctb = Charset.forName(entry.getName()).newEncoder();
                    CodeSetConversion.this.cache.setConverter(entry.getName(), this.ctb);
                }
                this.codeset = entry;
                this.alignment = i2;
            } catch (IllegalCharsetNameException e2) {
                throw this.wrapper.invalidCtbConverterName(e2, entry.getName());
            } catch (UnsupportedCharsetException e3) {
                throw this.wrapper.invalidCtbConverterName(e3, entry.getName());
            }
        }

        @Override // com.sun.corba.se.impl.encoding.CodeSetConversion.CTBConverter
        public final float getMaxBytesPerChar() {
            return this.ctb.maxBytesPerChar();
        }

        @Override // com.sun.corba.se.impl.encoding.CodeSetConversion.CTBConverter
        public void convert(char c2) {
            if (this.chars == null) {
                this.chars = new char[1];
            }
            this.chars[0] = c2;
            this.numChars = 1;
            convertCharArray();
        }

        @Override // com.sun.corba.se.impl.encoding.CodeSetConversion.CTBConverter
        public void convert(String str) {
            if (this.chars == null || this.chars.length < str.length()) {
                this.chars = new char[str.length()];
            }
            this.numChars = str.length();
            str.getChars(0, this.numChars, this.chars, 0);
            convertCharArray();
        }

        @Override // com.sun.corba.se.impl.encoding.CodeSetConversion.CTBConverter
        public final int getNumBytes() {
            return this.numBytes;
        }

        @Override // com.sun.corba.se.impl.encoding.CodeSetConversion.CTBConverter
        public final int getAlignment() {
            return this.alignment;
        }

        @Override // com.sun.corba.se.impl.encoding.CodeSetConversion.CTBConverter
        public final boolean isFixedWidthEncoding() {
            return this.codeset.isFixedWidth();
        }

        @Override // com.sun.corba.se.impl.encoding.CodeSetConversion.CTBConverter
        public byte[] getBytes() {
            return this.buffer.array();
        }

        private void convertCharArray() {
            try {
                this.buffer = this.ctb.encode(CharBuffer.wrap(this.chars, 0, this.numChars));
                this.numBytes = this.buffer.limit();
            } catch (IllegalStateException e2) {
                throw this.wrapper.ctbConverterFailure(e2);
            } catch (MalformedInputException e3) {
                throw this.wrapper.badUnicodePair(e3);
            } catch (UnmappableCharacterException e4) {
                throw this.omgWrapper.charNotInCodeset(e4);
            } catch (CharacterCodingException e5) {
                throw this.wrapper.ctbConverterFailure(e5);
            }
        }
    }

    /* loaded from: rt.jar:com/sun/corba/se/impl/encoding/CodeSetConversion$UTF16CTBConverter.class */
    private class UTF16CTBConverter extends JavaCTBConverter {
        public UTF16CTBConverter() {
            super(OSFCodeSetRegistry.UTF_16, 2);
        }

        public UTF16CTBConverter(boolean z2) {
            super(z2 ? OSFCodeSetRegistry.UTF_16LE : OSFCodeSetRegistry.UTF_16BE, 2);
        }
    }

    /* loaded from: rt.jar:com/sun/corba/se/impl/encoding/CodeSetConversion$JavaBTCConverter.class */
    private class JavaBTCConverter extends BTCConverter {
        private ORBUtilSystemException wrapper = ORBUtilSystemException.get(CORBALogDomains.RPC_ENCODING);
        private OMGSystemException omgWrapper = OMGSystemException.get(CORBALogDomains.RPC_ENCODING);
        protected CharsetDecoder btc;
        private char[] buffer;
        private int resultingNumChars;
        private OSFCodeSetRegistry.Entry codeset;

        public JavaBTCConverter(OSFCodeSetRegistry.Entry entry) {
            this.btc = getConverter(entry.getName());
            this.codeset = entry;
        }

        @Override // com.sun.corba.se.impl.encoding.CodeSetConversion.BTCConverter
        public final boolean isFixedWidthEncoding() {
            return this.codeset.isFixedWidth();
        }

        @Override // com.sun.corba.se.impl.encoding.CodeSetConversion.BTCConverter
        public final int getFixedCharWidth() {
            return this.codeset.getMaxBytesPerChar();
        }

        @Override // com.sun.corba.se.impl.encoding.CodeSetConversion.BTCConverter
        public final int getNumChars() {
            return this.resultingNumChars;
        }

        @Override // com.sun.corba.se.impl.encoding.CodeSetConversion.BTCConverter
        public char[] getChars(byte[] bArr, int i2, int i3) {
            try {
                CharBuffer charBufferDecode = this.btc.decode(ByteBuffer.wrap(bArr, i2, i3));
                this.resultingNumChars = charBufferDecode.limit();
                if (charBufferDecode.limit() == charBufferDecode.capacity()) {
                    this.buffer = charBufferDecode.array();
                } else {
                    this.buffer = new char[charBufferDecode.limit()];
                    charBufferDecode.get(this.buffer, 0, charBufferDecode.limit()).position(0);
                }
                return this.buffer;
            } catch (IllegalStateException e2) {
                throw this.wrapper.btcConverterFailure(e2);
            } catch (MalformedInputException e3) {
                throw this.wrapper.badUnicodePair(e3);
            } catch (UnmappableCharacterException e4) {
                throw this.omgWrapper.charNotInCodeset(e4);
            } catch (CharacterCodingException e5) {
                throw this.wrapper.btcConverterFailure(e5);
            }
        }

        protected CharsetDecoder getConverter(String str) {
            try {
                CharsetDecoder byteToCharConverter = CodeSetConversion.this.cache.getByteToCharConverter(str);
                if (byteToCharConverter == null) {
                    byteToCharConverter = Charset.forName(str).newDecoder();
                    CodeSetConversion.this.cache.setConverter(str, byteToCharConverter);
                }
                return byteToCharConverter;
            } catch (IllegalCharsetNameException e2) {
                throw this.wrapper.invalidBtcConverterName(e2, str);
            }
        }
    }

    /* loaded from: rt.jar:com/sun/corba/se/impl/encoding/CodeSetConversion$UTF16BTCConverter.class */
    private class UTF16BTCConverter extends JavaBTCConverter {
        private boolean defaultToLittleEndian;
        private boolean converterUsesBOM;
        private static final char UTF16_BE_MARKER = 65279;
        private static final char UTF16_LE_MARKER = 65534;

        public UTF16BTCConverter(boolean z2) {
            super(OSFCodeSetRegistry.UTF_16);
            this.converterUsesBOM = true;
            this.defaultToLittleEndian = z2;
        }

        @Override // com.sun.corba.se.impl.encoding.CodeSetConversion.JavaBTCConverter, com.sun.corba.se.impl.encoding.CodeSetConversion.BTCConverter
        public char[] getChars(byte[] bArr, int i2, int i3) {
            if (hasUTF16ByteOrderMarker(bArr, i2, i3)) {
                if (!this.converterUsesBOM) {
                    switchToConverter(OSFCodeSetRegistry.UTF_16);
                }
                this.converterUsesBOM = true;
                return super.getChars(bArr, i2, i3);
            }
            if (this.converterUsesBOM) {
                if (this.defaultToLittleEndian) {
                    switchToConverter(OSFCodeSetRegistry.UTF_16LE);
                } else {
                    switchToConverter(OSFCodeSetRegistry.UTF_16BE);
                }
                this.converterUsesBOM = false;
            }
            return super.getChars(bArr, i2, i3);
        }

        private boolean hasUTF16ByteOrderMarker(byte[] bArr, int i2, int i3) {
            if (i3 >= 4) {
                char c2 = (char) (((bArr[i2] & 255) << 8) | ((bArr[i2 + 1] & 255) << 0));
                return c2 == UTF16_BE_MARKER || c2 == 65534;
            }
            return false;
        }

        private void switchToConverter(OSFCodeSetRegistry.Entry entry) {
            this.btc = super.getConverter(entry.getName());
        }
    }

    public CTBConverter getCTBConverter(OSFCodeSetRegistry.Entry entry) {
        return new JavaCTBConverter(entry, !entry.isFixedWidth() ? 1 : entry.getMaxBytesPerChar());
    }

    public CTBConverter getCTBConverter(OSFCodeSetRegistry.Entry entry, boolean z2, boolean z3) {
        if (entry == OSFCodeSetRegistry.UCS_2) {
            return new UTF16CTBConverter(z2);
        }
        if (entry == OSFCodeSetRegistry.UTF_16) {
            if (z3) {
                return new UTF16CTBConverter();
            }
            return new UTF16CTBConverter(z2);
        }
        return new JavaCTBConverter(entry, !entry.isFixedWidth() ? 1 : entry.getMaxBytesPerChar());
    }

    public BTCConverter getBTCConverter(OSFCodeSetRegistry.Entry entry) {
        return new JavaBTCConverter(entry);
    }

    public BTCConverter getBTCConverter(OSFCodeSetRegistry.Entry entry, boolean z2) {
        if (entry == OSFCodeSetRegistry.UTF_16 || entry == OSFCodeSetRegistry.UCS_2) {
            return new UTF16BTCConverter(z2);
        }
        return new JavaBTCConverter(entry);
    }

    private int selectEncoding(CodeSetComponentInfo.CodeSetComponent codeSetComponent, CodeSetComponentInfo.CodeSetComponent codeSetComponent2) {
        int i2 = codeSetComponent2.nativeCodeSet;
        if (i2 == 0) {
            if (codeSetComponent2.conversionCodeSets.length > 0) {
                i2 = codeSetComponent2.conversionCodeSets[0];
            } else {
                return 0;
            }
        }
        if (codeSetComponent.nativeCodeSet == i2) {
            return i2;
        }
        for (int i3 = 0; i3 < codeSetComponent.conversionCodeSets.length; i3++) {
            if (i2 == codeSetComponent.conversionCodeSets[i3]) {
                return i2;
            }
        }
        for (int i4 = 0; i4 < codeSetComponent2.conversionCodeSets.length; i4++) {
            if (codeSetComponent.nativeCodeSet == codeSetComponent2.conversionCodeSets[i4]) {
                return codeSetComponent.nativeCodeSet;
            }
        }
        for (int i5 = 0; i5 < codeSetComponent2.conversionCodeSets.length; i5++) {
            for (int i6 = 0; i6 < codeSetComponent.conversionCodeSets.length; i6++) {
                if (codeSetComponent2.conversionCodeSets[i5] == codeSetComponent.conversionCodeSets[i6]) {
                    return codeSetComponent2.conversionCodeSets[i5];
                }
            }
        }
        return 0;
    }

    public CodeSetComponentInfo.CodeSetContext negotiate(CodeSetComponentInfo codeSetComponentInfo, CodeSetComponentInfo codeSetComponentInfo2) {
        int iSelectEncoding = selectEncoding(codeSetComponentInfo.getCharComponent(), codeSetComponentInfo2.getCharComponent());
        if (iSelectEncoding == 0) {
            iSelectEncoding = OSFCodeSetRegistry.UTF_8.getNumber();
        }
        int iSelectEncoding2 = selectEncoding(codeSetComponentInfo.getWCharComponent(), codeSetComponentInfo2.getWCharComponent());
        if (iSelectEncoding2 == 0) {
            iSelectEncoding2 = OSFCodeSetRegistry.UTF_16.getNumber();
        }
        return new CodeSetComponentInfo.CodeSetContext(iSelectEncoding, iSelectEncoding2);
    }

    private CodeSetConversion() {
        this.cache = new CodeSetCache();
    }

    /* loaded from: rt.jar:com/sun/corba/se/impl/encoding/CodeSetConversion$CodeSetConversionHolder.class */
    private static class CodeSetConversionHolder {
        static final CodeSetConversion csc = new CodeSetConversion();

        private CodeSetConversionHolder() {
        }
    }

    public static final CodeSetConversion impl() {
        return CodeSetConversionHolder.csc;
    }
}
