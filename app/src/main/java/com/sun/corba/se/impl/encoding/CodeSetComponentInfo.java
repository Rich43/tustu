package com.sun.corba.se.impl.encoding;

import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import javafx.fxml.FXMLLoader;

/* loaded from: rt.jar:com/sun/corba/se/impl/encoding/CodeSetComponentInfo.class */
public final class CodeSetComponentInfo {
    private CodeSetComponent forCharData;
    private CodeSetComponent forWCharData;
    public static final CodeSetComponentInfo JAVASOFT_DEFAULT_CODESETS = new CodeSetComponentInfo(new CodeSetComponent(OSFCodeSetRegistry.ISO_8859_1.getNumber(), new int[]{OSFCodeSetRegistry.UTF_8.getNumber(), OSFCodeSetRegistry.ISO_646.getNumber()}), new CodeSetComponent(OSFCodeSetRegistry.UTF_16.getNumber(), new int[]{OSFCodeSetRegistry.UCS_2.getNumber()}));
    public static final CodeSetContext LOCAL_CODE_SETS = new CodeSetContext(OSFCodeSetRegistry.ISO_8859_1.getNumber(), OSFCodeSetRegistry.UTF_16.getNumber());

    /* loaded from: rt.jar:com/sun/corba/se/impl/encoding/CodeSetComponentInfo$CodeSetComponent.class */
    public static final class CodeSetComponent {
        int nativeCodeSet;
        int[] conversionCodeSets;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof CodeSetComponent)) {
                return false;
            }
            CodeSetComponent codeSetComponent = (CodeSetComponent) obj;
            return this.nativeCodeSet == codeSetComponent.nativeCodeSet && Arrays.equals(this.conversionCodeSets, codeSetComponent.conversionCodeSets);
        }

        public int hashCode() {
            int i2 = this.nativeCodeSet;
            for (int i3 = 0; i3 < this.conversionCodeSets.length; i3++) {
                i2 = (37 * i2) + this.conversionCodeSets[i3];
            }
            return i2;
        }

        public CodeSetComponent() {
        }

        public CodeSetComponent(int i2, int[] iArr) {
            this.nativeCodeSet = i2;
            if (iArr == null) {
                this.conversionCodeSets = new int[0];
            } else {
                this.conversionCodeSets = iArr;
            }
        }

        public void read(MarshalInputStream marshalInputStream) {
            this.nativeCodeSet = marshalInputStream.read_ulong();
            int i2 = marshalInputStream.read_long();
            this.conversionCodeSets = new int[i2];
            marshalInputStream.read_ulong_array(this.conversionCodeSets, 0, i2);
        }

        public void write(MarshalOutputStream marshalOutputStream) {
            marshalOutputStream.write_ulong(this.nativeCodeSet);
            marshalOutputStream.write_long(this.conversionCodeSets.length);
            marshalOutputStream.write_ulong_array(this.conversionCodeSets, 0, this.conversionCodeSets.length);
        }

        public String toString() {
            StringBuffer stringBuffer = new StringBuffer("CodeSetComponent(");
            stringBuffer.append("native:");
            stringBuffer.append(Integer.toHexString(this.nativeCodeSet));
            stringBuffer.append(" conversion:");
            if (this.conversionCodeSets == null) {
                stringBuffer.append(FXMLLoader.NULL_KEYWORD);
            } else {
                for (int i2 = 0; i2 < this.conversionCodeSets.length; i2++) {
                    stringBuffer.append(Integer.toHexString(this.conversionCodeSets[i2]));
                    stringBuffer.append(' ');
                }
            }
            stringBuffer.append(")");
            return stringBuffer.toString();
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CodeSetComponentInfo)) {
            return false;
        }
        CodeSetComponentInfo codeSetComponentInfo = (CodeSetComponentInfo) obj;
        return this.forCharData.equals(codeSetComponentInfo.forCharData) && this.forWCharData.equals(codeSetComponentInfo.forWCharData);
    }

    public int hashCode() {
        return this.forCharData.hashCode() ^ this.forWCharData.hashCode();
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("CodeSetComponentInfo(");
        stringBuffer.append("char_data:");
        stringBuffer.append(this.forCharData.toString());
        stringBuffer.append(" wchar_data:");
        stringBuffer.append(this.forWCharData.toString());
        stringBuffer.append(")");
        return stringBuffer.toString();
    }

    public CodeSetComponentInfo() {
        this.forCharData = JAVASOFT_DEFAULT_CODESETS.forCharData;
        this.forWCharData = JAVASOFT_DEFAULT_CODESETS.forWCharData;
    }

    public CodeSetComponentInfo(CodeSetComponent codeSetComponent, CodeSetComponent codeSetComponent2) {
        this.forCharData = codeSetComponent;
        this.forWCharData = codeSetComponent2;
    }

    public void read(MarshalInputStream marshalInputStream) {
        this.forCharData = new CodeSetComponent();
        this.forCharData.read(marshalInputStream);
        this.forWCharData = new CodeSetComponent();
        this.forWCharData.read(marshalInputStream);
    }

    public void write(MarshalOutputStream marshalOutputStream) {
        this.forCharData.write(marshalOutputStream);
        this.forWCharData.write(marshalOutputStream);
    }

    public CodeSetComponent getCharComponent() {
        return this.forCharData;
    }

    public CodeSetComponent getWCharComponent() {
        return this.forWCharData;
    }

    /* loaded from: rt.jar:com/sun/corba/se/impl/encoding/CodeSetComponentInfo$CodeSetContext.class */
    public static final class CodeSetContext {
        private int char_data;
        private int wchar_data;

        public CodeSetContext() {
        }

        public CodeSetContext(int i2, int i3) {
            this.char_data = i2;
            this.wchar_data = i3;
        }

        public void read(MarshalInputStream marshalInputStream) {
            this.char_data = marshalInputStream.read_ulong();
            this.wchar_data = marshalInputStream.read_ulong();
        }

        public void write(MarshalOutputStream marshalOutputStream) {
            marshalOutputStream.write_ulong(this.char_data);
            marshalOutputStream.write_ulong(this.wchar_data);
        }

        public int getCharCodeSet() {
            return this.char_data;
        }

        public int getWCharCodeSet() {
            return this.wchar_data;
        }

        public String toString() {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("CodeSetContext char set: ");
            stringBuffer.append(Integer.toHexString(this.char_data));
            stringBuffer.append(" wchar set: ");
            stringBuffer.append(Integer.toHexString(this.wchar_data));
            return stringBuffer.toString();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static CodeSetComponent createFromString(String str) {
        ORBUtilSystemException oRBUtilSystemException = ORBUtilSystemException.get(CORBALogDomains.RPC_ENCODING);
        if (str == null || str.length() == 0) {
            throw oRBUtilSystemException.badCodeSetString();
        }
        StringTokenizer stringTokenizer = new StringTokenizer(str, ", ", false);
        try {
            int iIntValue = Integer.decode(stringTokenizer.nextToken()).intValue();
            if (OSFCodeSetRegistry.lookupEntry(iIntValue) == null) {
                throw oRBUtilSystemException.unknownNativeCodeset(new Integer(iIntValue));
            }
            ArrayList arrayList = new ArrayList(10);
            while (stringTokenizer.hasMoreTokens()) {
                Integer numDecode = Integer.decode(stringTokenizer.nextToken());
                if (OSFCodeSetRegistry.lookupEntry(numDecode.intValue()) == null) {
                    throw oRBUtilSystemException.unknownConversionCodeSet(numDecode);
                }
                arrayList.add(numDecode);
            }
            int[] iArr = new int[arrayList.size()];
            for (int i2 = 0; i2 < iArr.length; i2++) {
                iArr[i2] = ((Integer) arrayList.get(i2)).intValue();
            }
            return new CodeSetComponent(iIntValue, iArr);
        } catch (NumberFormatException e2) {
            throw oRBUtilSystemException.invalidCodeSetNumber(e2);
        } catch (NoSuchElementException e3) {
            throw oRBUtilSystemException.invalidCodeSetString(e3, str);
        }
    }
}
