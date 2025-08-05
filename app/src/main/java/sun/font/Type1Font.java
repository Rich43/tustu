package sun.font;

import com.sun.javafx.font.LogicalFont;
import java.awt.FontFormatException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.FileChannel;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.HashSet;
import net.lingala.zip4j.util.InternalZipConstants;
import sun.java2d.Disposer;
import sun.java2d.DisposerRecord;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:sun/font/Type1Font.class */
public class Type1Font extends FileFont {
    WeakReference bufferRef;
    private String psName;
    private static HashMap styleAbbreviationsMapping = new HashMap();
    private static HashSet styleNameTokes = new HashSet();
    private static final int PSEOFTOKEN = 0;
    private static final int PSNAMETOKEN = 1;
    private static final int PSSTRINGTOKEN = 2;

    /* loaded from: rt.jar:sun/font/Type1Font$T1DisposerRecord.class */
    private static class T1DisposerRecord implements DisposerRecord {
        String fileName;

        T1DisposerRecord(String str) {
            this.fileName = null;
            this.fileName = str;
        }

        @Override // sun.java2d.DisposerRecord
        public synchronized void dispose() {
            AccessController.doPrivileged(new PrivilegedAction() { // from class: sun.font.Type1Font.T1DisposerRecord.1
                @Override // java.security.PrivilegedAction
                public Object run() {
                    if (T1DisposerRecord.this.fileName != null) {
                        new File(T1DisposerRecord.this.fileName).delete();
                        return null;
                    }
                    return null;
                }
            });
        }
    }

    static {
        String[] strArr = {"Black", LogicalFont.STYLE_BOLD, "Book", "Demi", "Heavy", "Light", "Meduium", "Nord", "Poster", LogicalFont.STYLE_REGULAR, "Super", "Thin", "Compressed", "Condensed", "Compact", "Extended", "Narrow", "Inclined", LogicalFont.STYLE_ITALIC, "Kursiv", "Oblique", "Upright", "Sloped", "Semi", "Ultra", "Extra", "Alternate", "Alternate", "Deutsche Fraktur", "Expert", "Inline", "Ornaments", "Outline", "Roman", "Rounded", "Script", "Shaded", "Swash", "Titling", "Typewriter"};
        String[] strArr2 = {"Blk", "Bd", "Bk", "Dm", "Hv", "Lt", "Md", "Nd", "Po", "Rg", "Su", "Th", "Cm", "Cn", "Ct", "Ex", "Nr", "Ic", "It", "Ks", "Obl", "Up", "Sl", "Sm", "Ult", "X", "A", "Alt", "Dfr", "Exp", "In", "Or", "Ou", "Rm", "Rd", "Scr", "Sh", "Sw", "Ti", "Typ"};
        String[] strArr3 = {"Black", LogicalFont.STYLE_BOLD, "Book", "Demi", "Heavy", "Light", "Medium", "Nord", "Poster", LogicalFont.STYLE_REGULAR, "Super", "Thin", "Compressed", "Condensed", "Compact", "Extended", "Narrow", "Inclined", LogicalFont.STYLE_ITALIC, "Kursiv", "Oblique", "Upright", "Sloped", "Slanted", "Semi", "Ultra", "Extra"};
        for (int i2 = 0; i2 < strArr.length; i2++) {
            styleAbbreviationsMapping.put(strArr2[i2], strArr[i2]);
        }
        for (String str : strArr3) {
            styleNameTokes.add(str);
        }
    }

    public Type1Font(String str, Object obj) throws FontFormatException {
        this(str, obj, false);
    }

    public Type1Font(String str, Object obj, boolean z2) throws FontFormatException {
        super(str, obj);
        this.bufferRef = new WeakReference(null);
        this.psName = null;
        this.fontRank = 4;
        this.checkedNatives = true;
        try {
            verify();
        } catch (Throwable th) {
            if (z2) {
                Disposer.addObjectRecord(this.bufferRef, new T1DisposerRecord(str));
                this.bufferRef = null;
            }
            if (th instanceof FontFormatException) {
                throw ((FontFormatException) th);
            }
            throw new FontFormatException("Unexpected runtime exception.");
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private synchronized ByteBuffer getBuffer() throws FontFormatException {
        MappedByteBuffer map = (MappedByteBuffer) this.bufferRef.get();
        if (map == null) {
            try {
                FileChannel channel = ((RandomAccessFile) AccessController.doPrivileged(new PrivilegedAction() { // from class: sun.font.Type1Font.1
                    @Override // java.security.PrivilegedAction
                    public Object run() {
                        try {
                            return new RandomAccessFile(Type1Font.this.platName, InternalZipConstants.READ_MODE);
                        } catch (FileNotFoundException e2) {
                            return null;
                        }
                    }
                })).getChannel();
                this.fileSize = (int) channel.size();
                map = channel.map(FileChannel.MapMode.READ_ONLY, 0L, this.fileSize);
                map.position(0);
                this.bufferRef = new WeakReference(map);
                channel.close();
            } catch (ClosedChannelException e2) {
                Thread.interrupted();
                return getBuffer();
            } catch (IOException e3) {
                throw new FontFormatException(e3.toString());
            } catch (NullPointerException e4) {
                throw new FontFormatException(e4.toString());
            }
        }
        return map;
    }

    @Override // sun.font.FileFont
    protected void close() {
    }

    /* JADX WARN: Removed duplicated region for block: B:56:0x0069 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:64:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    void readFile(java.nio.ByteBuffer r5) {
        /*
            r4 = this;
            r0 = 0
            r6 = r0
            sun.font.Type1Font$2 r0 = new sun.font.Type1Font$2     // Catch: java.lang.NullPointerException -> L39 java.nio.channels.ClosedChannelException -> L4b java.io.IOException -> L75 java.lang.Throwable -> L87
            r1 = r0
            r2 = r4
            r1.<init>()     // Catch: java.lang.NullPointerException -> L39 java.nio.channels.ClosedChannelException -> L4b java.io.IOException -> L75 java.lang.Throwable -> L87
            java.lang.Object r0 = java.security.AccessController.doPrivileged(r0)     // Catch: java.lang.NullPointerException -> L39 java.nio.channels.ClosedChannelException -> L4b java.io.IOException -> L75 java.lang.Throwable -> L87
            java.io.RandomAccessFile r0 = (java.io.RandomAccessFile) r0     // Catch: java.lang.NullPointerException -> L39 java.nio.channels.ClosedChannelException -> L4b java.io.IOException -> L75 java.lang.Throwable -> L87
            r6 = r0
            r0 = r6
            java.nio.channels.FileChannel r0 = r0.getChannel()     // Catch: java.lang.NullPointerException -> L39 java.nio.channels.ClosedChannelException -> L4b java.io.IOException -> L75 java.lang.Throwable -> L87
            r7 = r0
        L16:
            r0 = r5
            int r0 = r0.remaining()     // Catch: java.lang.NullPointerException -> L39 java.nio.channels.ClosedChannelException -> L4b java.io.IOException -> L75 java.lang.Throwable -> L87
            if (r0 <= 0) goto L29
            r0 = r7
            r1 = r5
            int r0 = r0.read(r1)     // Catch: java.lang.NullPointerException -> L39 java.nio.channels.ClosedChannelException -> L4b java.io.IOException -> L75 java.lang.Throwable -> L87
            r1 = -1
            if (r0 == r1) goto L29
            goto L16
        L29:
            r0 = r6
            if (r0 == 0) goto L99
            r0 = r6
            r0.close()     // Catch: java.io.IOException -> L34
            goto L99
        L34:
            r8 = move-exception
            goto L99
        L39:
            r8 = move-exception
            r0 = r6
            if (r0 == 0) goto L99
            r0 = r6
            r0.close()     // Catch: java.io.IOException -> L46
            goto L99
        L46:
            r8 = move-exception
            goto L99
        L4b:
            r8 = move-exception
            r0 = r6
            if (r0 == 0) goto L57
            r0 = r6
            r0.close()     // Catch: java.io.IOException -> L5a java.lang.Throwable -> L87
            r0 = 0
            r6 = r0
        L57:
            goto L5c
        L5a:
            r9 = move-exception
        L5c:
            boolean r0 = java.lang.Thread.interrupted()     // Catch: java.lang.Throwable -> L87
            r0 = r4
            r1 = r5
            r0.readFile(r1)     // Catch: java.lang.Throwable -> L87
            r0 = r6
            if (r0 == 0) goto L99
            r0 = r6
            r0.close()     // Catch: java.io.IOException -> L70
            goto L99
        L70:
            r8 = move-exception
            goto L99
        L75:
            r8 = move-exception
            r0 = r6
            if (r0 == 0) goto L99
            r0 = r6
            r0.close()     // Catch: java.io.IOException -> L82
            goto L99
        L82:
            r8 = move-exception
            goto L99
        L87:
            r10 = move-exception
            r0 = r6
            if (r0 == 0) goto L96
            r0 = r6
            r0.close()     // Catch: java.io.IOException -> L94
            goto L96
        L94:
            r11 = move-exception
        L96:
            r0 = r10
            throw r0
        L99:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.font.Type1Font.readFile(java.nio.ByteBuffer):void");
    }

    @Override // sun.font.FileFont
    public synchronized ByteBuffer readBlock(int i2, int i3) {
        try {
            ByteBuffer buffer = getBuffer();
            if (i2 > this.fileSize) {
                i2 = this.fileSize;
            }
            buffer.position(i2);
            return buffer.slice();
        } catch (FontFormatException e2) {
            return null;
        }
    }

    private void verify() throws FontFormatException {
        ByteBuffer buffer = getBuffer();
        if (buffer.capacity() < 6) {
            throw new FontFormatException("short file");
        }
        int i2 = buffer.get(0) & 255;
        if ((buffer.get(0) & 255) == 128) {
            verifyPFB(buffer);
            buffer.position(6);
        } else {
            verifyPFA(buffer);
            buffer.position(0);
        }
        initNames(buffer);
        if (this.familyName == null || this.fullName == null) {
            throw new FontFormatException("Font name not found");
        }
        setStyle();
    }

    public int getFileSize() {
        if (this.fileSize == 0) {
            try {
                getBuffer();
            } catch (FontFormatException e2) {
            }
        }
        return this.fileSize;
    }

    private void verifyPFA(ByteBuffer byteBuffer) throws FontFormatException {
        if (byteBuffer.getShort() != 9505) {
            throw new FontFormatException("bad pfa font");
        }
    }

    private void verifyPFB(ByteBuffer byteBuffer) throws FontFormatException {
        int i2 = 0;
        while (true) {
            try {
                int i3 = byteBuffer.getShort(i2) & 65535;
                if (i3 != 32769 && i3 != 32770) {
                    if (i3 == 32771) {
                        return;
                    } else {
                        throw new FontFormatException("bad pfb file");
                    }
                }
                byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
                int i4 = byteBuffer.getInt(i2 + 2);
                byteBuffer.order(ByteOrder.BIG_ENDIAN);
                if (i4 <= 0) {
                    throw new FontFormatException("bad segment length");
                }
                i2 += i4 + 6;
            } catch (BufferUnderflowException e2) {
                throw new FontFormatException(e2.toString());
            } catch (Exception e3) {
                throw new FontFormatException(e3.toString());
            }
        }
    }

    private void initNames(ByteBuffer byteBuffer) throws FontFormatException {
        boolean z2 = false;
        String str = null;
        while (true) {
            try {
                if ((this.fullName != null && this.familyName != null && this.psName != null && str != null) || z2) {
                    break;
                }
                int iNextTokenType = nextTokenType(byteBuffer);
                if (iNextTokenType == 1) {
                    if (byteBuffer.get(byteBuffer.position()) == 70) {
                        String simpleToken = getSimpleToken(byteBuffer);
                        if ("FullName".equals(simpleToken)) {
                            if (nextTokenType(byteBuffer) == 2) {
                                this.fullName = getString(byteBuffer);
                            }
                        } else if ("FamilyName".equals(simpleToken)) {
                            if (nextTokenType(byteBuffer) == 2) {
                                this.familyName = getString(byteBuffer);
                            }
                        } else if ("FontName".equals(simpleToken)) {
                            if (nextTokenType(byteBuffer) == 1) {
                                this.psName = getSimpleToken(byteBuffer);
                            }
                        } else if ("FontType".equals(simpleToken)) {
                            String simpleToken2 = getSimpleToken(byteBuffer);
                            if ("def".equals(getSimpleToken(byteBuffer))) {
                                str = simpleToken2;
                            }
                        }
                    } else {
                        while (byteBuffer.get() > 32) {
                        }
                    }
                } else if (iNextTokenType == 0) {
                    z2 = true;
                }
            } catch (Exception e2) {
                throw new FontFormatException(e2.toString());
            }
        }
        if (!"1".equals(str)) {
            throw new FontFormatException("Unsupported font type");
        }
        if (this.psName == null) {
            byteBuffer.position(0);
            if (byteBuffer.getShort() != 9505) {
                byteBuffer.position(8);
            }
            String simpleToken3 = getSimpleToken(byteBuffer);
            if (!simpleToken3.startsWith("FontType1-") && !simpleToken3.startsWith("PS-AdobeFont-")) {
                throw new FontFormatException("Unsupported font format [" + simpleToken3 + "]");
            }
            this.psName = getSimpleToken(byteBuffer);
        }
        if (z2) {
            if (this.fullName != null) {
                this.familyName = fullName2FamilyName(this.fullName);
            } else if (this.familyName != null) {
                this.fullName = this.familyName;
            } else {
                this.fullName = psName2FullName(this.psName);
                this.familyName = psName2FamilyName(this.psName);
            }
        }
    }

    private String fullName2FamilyName(String str) {
        int length = str.length();
        while (true) {
            int i2 = length;
            if (i2 > 0) {
                int i3 = i2 - 1;
                while (i3 > 0 && str.charAt(i3) != ' ') {
                    i3--;
                }
                if (!isStyleToken(str.substring(i3 + 1, i2))) {
                    return str.substring(0, i2);
                }
                length = i3;
            } else {
                return str;
            }
        }
    }

    private String expandAbbreviation(String str) {
        if (styleAbbreviationsMapping.containsKey(str)) {
            return (String) styleAbbreviationsMapping.get(str);
        }
        return str;
    }

    private boolean isStyleToken(String str) {
        return styleNameTokes.contains(str);
    }

    private String psName2FullName(String str) {
        String strExpandName;
        int iIndexOf = str.indexOf(LanguageTag.SEP);
        if (iIndexOf >= 0) {
            strExpandName = expandName(str.substring(0, iIndexOf), false) + " " + expandName(str.substring(iIndexOf + 1), true);
        } else {
            strExpandName = expandName(str, false);
        }
        return strExpandName;
    }

    private String psName2FamilyName(String str) {
        String strSubstring = str;
        if (strSubstring.indexOf(LanguageTag.SEP) > 0) {
            strSubstring = strSubstring.substring(0, strSubstring.indexOf(LanguageTag.SEP));
        }
        return expandName(strSubstring, false);
    }

    private int nextCapitalLetter(String str, int i2) {
        while (i2 >= 0 && i2 < str.length()) {
            if (str.charAt(i2) < 'A' || str.charAt(i2) > 'Z') {
                i2++;
            } else {
                return i2;
            }
        }
        return -1;
    }

    private String expandName(String str, boolean z2) {
        StringBuffer stringBuffer = new StringBuffer(str.length() + 10);
        int i2 = 0;
        while (true) {
            int i3 = i2;
            if (i3 < str.length()) {
                int iNextCapitalLetter = nextCapitalLetter(str, i3 + 1);
                if (iNextCapitalLetter < 0) {
                    iNextCapitalLetter = str.length();
                }
                if (i3 != 0) {
                    stringBuffer.append(" ");
                }
                if (z2) {
                    stringBuffer.append(expandAbbreviation(str.substring(i3, iNextCapitalLetter)));
                } else {
                    stringBuffer.append(str.substring(i3, iNextCapitalLetter));
                }
                i2 = iNextCapitalLetter;
            } else {
                return stringBuffer.toString();
            }
        }
    }

    private byte skip(ByteBuffer byteBuffer) {
        byte b2 = byteBuffer.get();
        while (b2 == 37) {
            do {
                b2 = byteBuffer.get();
                if (b2 != 13) {
                }
            } while (b2 != 10);
        }
        while (b2 <= 32) {
            b2 = byteBuffer.get();
        }
        return b2;
    }

    private int nextTokenType(ByteBuffer byteBuffer) {
        try {
            byte bSkip = skip(byteBuffer);
            while (bSkip != 47) {
                if (bSkip == 40) {
                    return 2;
                }
                if (bSkip == 13 || bSkip == 10) {
                    bSkip = skip(byteBuffer);
                } else {
                    bSkip = byteBuffer.get();
                }
            }
            return 1;
        } catch (BufferUnderflowException e2) {
            return 0;
        }
    }

    private String getSimpleToken(ByteBuffer byteBuffer) {
        while (byteBuffer.get() <= 32) {
        }
        int iPosition = byteBuffer.position() - 1;
        while (byteBuffer.get() > 32) {
        }
        byte[] bArr = new byte[(byteBuffer.position() - iPosition) - 1];
        byteBuffer.position(iPosition);
        byteBuffer.get(bArr);
        try {
            return new String(bArr, "US-ASCII");
        } catch (UnsupportedEncodingException e2) {
            return new String(bArr);
        }
    }

    private String getString(ByteBuffer byteBuffer) {
        int iPosition = byteBuffer.position();
        while (byteBuffer.get() != 41) {
        }
        byte[] bArr = new byte[(byteBuffer.position() - iPosition) - 1];
        byteBuffer.position(iPosition);
        byteBuffer.get(bArr);
        try {
            return new String(bArr, "US-ASCII");
        } catch (UnsupportedEncodingException e2) {
            return new String(bArr);
        }
    }

    @Override // sun.font.Font2D
    public String getPostscriptName() {
        return this.psName;
    }

    @Override // sun.font.FileFont
    protected synchronized FontScaler getScaler() {
        if (this.scaler == null) {
            this.scaler = FontScaler.getScaler(this, 0, false, this.fileSize);
        }
        return this.scaler;
    }

    @Override // sun.font.Font2D
    CharToGlyphMapper getMapper() {
        if (this.mapper == null) {
            this.mapper = new Type1GlyphMapper(this);
        }
        return this.mapper;
    }

    @Override // sun.font.Font2D
    public int getNumGlyphs() {
        try {
            return getScaler().getNumGlyphs();
        } catch (FontScalerException e2) {
            this.scaler = FontScaler.getNullScaler();
            return getNumGlyphs();
        }
    }

    @Override // sun.font.Font2D
    public int getMissingGlyphCode() {
        try {
            return getScaler().getMissingGlyphCode();
        } catch (FontScalerException e2) {
            this.scaler = FontScaler.getNullScaler();
            return getMissingGlyphCode();
        }
    }

    public int getGlyphCode(char c2) {
        try {
            return getScaler().getGlyphCode(c2);
        } catch (FontScalerException e2) {
            this.scaler = FontScaler.getNullScaler();
            return getGlyphCode(c2);
        }
    }

    public String toString() {
        return "** Type1 Font: Family=" + this.familyName + " Name=" + this.fullName + " style=" + this.style + " fileName=" + getPublicFileName();
    }
}
