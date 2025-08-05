package com.sun.corba.se.impl.naming.cosnaming;

import java.io.StringWriter;
import javafx.fxml.FXMLLoader;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExtPackage.InvalidAddress;
import org.omg.CosNaming.NamingContextPackage.InvalidName;

/* loaded from: rt.jar:com/sun/corba/se/impl/naming/cosnaming/InterOperableNamingImpl.class */
public class InterOperableNamingImpl {
    public String convertToString(NameComponent[] nameComponentArr) {
        String strConvertNameComponentToString = convertNameComponentToString(nameComponentArr[0]);
        for (int i2 = 1; i2 < nameComponentArr.length; i2++) {
            if (convertNameComponentToString(nameComponentArr[i2]) != null) {
                strConvertNameComponentToString = strConvertNameComponentToString + "/" + convertNameComponentToString(nameComponentArr[i2]);
            }
        }
        return strConvertNameComponentToString;
    }

    private String convertNameComponentToString(NameComponent nameComponent) {
        if ((nameComponent.id == null || nameComponent.id.length() == 0) && (nameComponent.kind == null || nameComponent.kind.length() == 0)) {
            return ".";
        }
        if (nameComponent.id == null || nameComponent.id.length() == 0) {
            return "." + addEscape(nameComponent.kind);
        }
        if (nameComponent.kind == null || nameComponent.kind.length() == 0) {
            return addEscape(nameComponent.id);
        }
        return addEscape(nameComponent.id) + "." + addEscape(nameComponent.kind);
    }

    private String addEscape(String str) {
        if (str != null && (str.indexOf(46) != -1 || str.indexOf(47) != -1)) {
            StringBuffer stringBuffer = new StringBuffer();
            for (int i2 = 0; i2 < str.length(); i2++) {
                char cCharAt = str.charAt(i2);
                if (cCharAt != '.' && cCharAt != '/') {
                    stringBuffer.append(cCharAt);
                } else {
                    stringBuffer.append('\\');
                    stringBuffer.append(cCharAt);
                }
            }
            return new String(stringBuffer);
        }
        return str;
    }

    public NameComponent[] convertToNameComponent(String str) throws InvalidName {
        String[] strArrBreakStringToNameComponents = breakStringToNameComponents(str);
        if (strArrBreakStringToNameComponents == null || strArrBreakStringToNameComponents.length == 0) {
            return null;
        }
        NameComponent[] nameComponentArr = new NameComponent[strArrBreakStringToNameComponents.length];
        for (int i2 = 0; i2 < strArrBreakStringToNameComponents.length; i2++) {
            nameComponentArr[i2] = createNameComponentFromString(strArrBreakStringToNameComponents[i2]);
        }
        return nameComponentArr;
    }

    private String[] breakStringToNameComponents(String str) {
        int[] iArr = new int[100];
        int i2 = 0;
        int length = 0;
        while (length <= str.length()) {
            iArr[i2] = str.indexOf(47, length);
            if (iArr[i2] == -1) {
                length = str.length() + 1;
            } else if (iArr[i2] > 0 && str.charAt(iArr[i2] - 1) == '\\') {
                length = iArr[i2] + 1;
                iArr[i2] = -1;
            } else {
                length = iArr[i2] + 1;
                i2++;
            }
        }
        if (i2 == 0) {
            return new String[]{str};
        }
        if (i2 != 0) {
            i2++;
        }
        return StringComponentsFromIndices(iArr, i2, str);
    }

    private String[] StringComponentsFromIndices(int[] iArr, int i2, String str) {
        String[] strArr = new String[i2];
        int i3 = 0;
        int i4 = iArr[0];
        int i5 = 0;
        while (i5 < i2) {
            strArr[i5] = str.substring(i3, i4);
            if (iArr[i5] < str.length() - 1 && iArr[i5] != -1) {
                i3 = iArr[i5] + 1;
            } else {
                i3 = 0;
                i5 = i2;
            }
            if (i5 + 1 < iArr.length && iArr[i5 + 1] < str.length() - 1 && iArr[i5 + 1] != -1) {
                i4 = iArr[i5 + 1];
            } else {
                i5 = i2;
            }
            if (i3 != 0 && i5 == i2) {
                strArr[i2 - 1] = str.substring(i3);
            }
            i5++;
        }
        return strArr;
    }

    private NameComponent createNameComponentFromString(String str) throws InvalidName {
        String strSubstring = null;
        String strSubstring2 = null;
        if (str == null || str.length() == 0 || str.endsWith(".")) {
            throw new InvalidName();
        }
        int iIndexOf = str.indexOf(46, 0);
        if (iIndexOf == -1) {
            strSubstring = str;
        } else if (iIndexOf == 0) {
            if (str.length() != 1) {
                strSubstring2 = str.substring(1);
            }
        } else if (str.charAt(iIndexOf - 1) != '\\') {
            strSubstring = str.substring(0, iIndexOf);
            strSubstring2 = str.substring(iIndexOf + 1);
        } else {
            boolean z2 = false;
            while (iIndexOf < str.length() && !z2) {
                iIndexOf = str.indexOf(46, iIndexOf + 1);
                if (iIndexOf > 0) {
                    if (str.charAt(iIndexOf - 1) != '\\') {
                        z2 = true;
                    }
                } else {
                    iIndexOf = str.length();
                }
            }
            if (z2) {
                strSubstring = str.substring(0, iIndexOf);
                strSubstring2 = str.substring(iIndexOf + 1);
            } else {
                strSubstring = str;
            }
        }
        String strCleanEscapeCharacter = cleanEscapeCharacter(strSubstring);
        String strCleanEscapeCharacter2 = cleanEscapeCharacter(strSubstring2);
        if (strCleanEscapeCharacter == null) {
            strCleanEscapeCharacter = "";
        }
        if (strCleanEscapeCharacter2 == null) {
            strCleanEscapeCharacter2 = "";
        }
        return new NameComponent(strCleanEscapeCharacter, strCleanEscapeCharacter2);
    }

    private String cleanEscapeCharacter(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        if (str.indexOf(92) == 0) {
            return str;
        }
        StringBuffer stringBuffer = new StringBuffer(str);
        StringBuffer stringBuffer2 = new StringBuffer();
        for (int i2 = 0; i2 < str.length(); i2++) {
            char cCharAt = stringBuffer.charAt(i2);
            if (cCharAt != '\\') {
                stringBuffer2.append(cCharAt);
            } else if (i2 + 1 < str.length() && Character.isLetterOrDigit(stringBuffer.charAt(i2 + 1))) {
                stringBuffer2.append(cCharAt);
            }
        }
        return new String(stringBuffer2);
    }

    public String createURLBasedAddress(String str, String str2) throws InvalidAddress {
        if (str == null || str.length() == 0) {
            throw new InvalidAddress();
        }
        return "corbaname:" + str + FXMLLoader.CONTROLLER_METHOD_PREFIX + encode(str2);
    }

    private String encode(String str) {
        StringWriter stringWriter = new StringWriter();
        for (int i2 = 0; i2 < str.length(); i2++) {
            char cCharAt = str.charAt(i2);
            if (Character.isLetterOrDigit(cCharAt)) {
                stringWriter.write(cCharAt);
            } else if (cCharAt == ';' || cCharAt == '/' || cCharAt == '?' || cCharAt == ':' || cCharAt == '@' || cCharAt == '&' || cCharAt == '=' || cCharAt == '+' || cCharAt == '$' || cCharAt == ';' || cCharAt == '-' || cCharAt == '_' || cCharAt == '.' || cCharAt == '!' || cCharAt == '~' || cCharAt == '*' || cCharAt == ' ' || cCharAt == '(' || cCharAt == ')') {
                stringWriter.write(cCharAt);
            } else {
                stringWriter.write(37);
                stringWriter.write(Integer.toHexString(cCharAt));
            }
        }
        return stringWriter.toString();
    }
}
