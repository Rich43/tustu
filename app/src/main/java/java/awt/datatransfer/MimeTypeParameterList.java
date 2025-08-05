package java.awt.datatransfer;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
import org.apache.commons.math3.geometry.VectorFormat;

/* loaded from: rt.jar:java/awt/datatransfer/MimeTypeParameterList.class */
class MimeTypeParameterList implements Cloneable {
    private Hashtable<String, String> parameters = new Hashtable<>();
    private static final String TSPECIALS = "()<>@,;:\\\"/[]?=";

    public MimeTypeParameterList() {
    }

    public MimeTypeParameterList(String str) throws MimeTypeParseException {
        parse(str);
    }

    public int hashCode() {
        int iHashCode = 47721858;
        Enumeration<String> names = getNames();
        while (names.hasMoreElements()) {
            String strNextElement = names.nextElement2();
            iHashCode = iHashCode + strNextElement.hashCode() + get(strNextElement).hashCode();
        }
        return iHashCode;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof MimeTypeParameterList)) {
            return false;
        }
        MimeTypeParameterList mimeTypeParameterList = (MimeTypeParameterList) obj;
        if (size() != mimeTypeParameterList.size()) {
            return false;
        }
        for (Map.Entry<String, String> entry : this.parameters.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            String str = mimeTypeParameterList.parameters.get(key);
            if (value == null || str == null) {
                if (value != str) {
                    return false;
                }
            } else if (!value.equals(str)) {
                return false;
            }
        }
        return true;
    }

    protected void parse(String str) throws MimeTypeParseException {
        String strSubstring;
        int length = str.length();
        if (length > 0) {
            int iSkipWhiteSpace = skipWhiteSpace(str, 0);
            if (iSkipWhiteSpace < length) {
                char cCharAt = str.charAt(iSkipWhiteSpace);
                while (iSkipWhiteSpace < length && cCharAt == ';') {
                    int iSkipWhiteSpace2 = skipWhiteSpace(str, iSkipWhiteSpace + 1);
                    if (iSkipWhiteSpace2 < length) {
                        char cCharAt2 = str.charAt(iSkipWhiteSpace2);
                        while (true) {
                            char c2 = cCharAt2;
                            if (iSkipWhiteSpace2 >= length || !isTokenChar(c2)) {
                                break;
                            }
                            iSkipWhiteSpace2++;
                            cCharAt2 = str.charAt(iSkipWhiteSpace2);
                        }
                        String lowerCase = str.substring(iSkipWhiteSpace2, iSkipWhiteSpace2).toLowerCase();
                        int iSkipWhiteSpace3 = skipWhiteSpace(str, iSkipWhiteSpace2);
                        if (iSkipWhiteSpace3 < length && str.charAt(iSkipWhiteSpace3) == '=') {
                            int iSkipWhiteSpace4 = skipWhiteSpace(str, iSkipWhiteSpace3 + 1);
                            if (iSkipWhiteSpace4 < length) {
                                cCharAt = str.charAt(iSkipWhiteSpace4);
                                if (cCharAt == '\"') {
                                    int i2 = iSkipWhiteSpace4 + 1;
                                    if (i2 < length) {
                                        boolean z2 = false;
                                        while (i2 < length && !z2) {
                                            cCharAt = str.charAt(i2);
                                            if (cCharAt == '\\') {
                                                i2 += 2;
                                            } else if (cCharAt == '\"') {
                                                z2 = true;
                                            } else {
                                                i2++;
                                            }
                                        }
                                        if (cCharAt == '\"') {
                                            strSubstring = unquote(str.substring(i2, i2));
                                            iSkipWhiteSpace4 = i2 + 1;
                                        } else {
                                            throw new MimeTypeParseException("Encountered unterminated quoted parameter value.");
                                        }
                                    } else {
                                        throw new MimeTypeParseException("Encountered unterminated quoted parameter value.");
                                    }
                                } else if (isTokenChar(cCharAt)) {
                                    boolean z3 = false;
                                    while (iSkipWhiteSpace4 < length && !z3) {
                                        cCharAt = str.charAt(iSkipWhiteSpace4);
                                        if (isTokenChar(cCharAt)) {
                                            iSkipWhiteSpace4++;
                                        } else {
                                            z3 = true;
                                        }
                                    }
                                    strSubstring = str.substring(iSkipWhiteSpace4, iSkipWhiteSpace4);
                                } else {
                                    throw new MimeTypeParseException("Unexpected character encountered at index " + iSkipWhiteSpace4);
                                }
                                this.parameters.put(lowerCase, strSubstring);
                                iSkipWhiteSpace = skipWhiteSpace(str, iSkipWhiteSpace4);
                                if (iSkipWhiteSpace < length) {
                                    cCharAt = str.charAt(iSkipWhiteSpace);
                                }
                            } else {
                                throw new MimeTypeParseException("Couldn't find a value for parameter named " + lowerCase);
                            }
                        } else {
                            throw new MimeTypeParseException("Couldn't find the '=' that separates a parameter name from its value.");
                        }
                    } else {
                        throw new MimeTypeParseException("Couldn't find parameter name");
                    }
                }
                if (iSkipWhiteSpace < length) {
                    throw new MimeTypeParseException("More characters encountered in input than expected.");
                }
            }
        }
    }

    public int size() {
        return this.parameters.size();
    }

    public boolean isEmpty() {
        return this.parameters.isEmpty();
    }

    public String get(String str) {
        return this.parameters.get(str.trim().toLowerCase());
    }

    public void set(String str, String str2) {
        this.parameters.put(str.trim().toLowerCase(), str2);
    }

    public void remove(String str) {
        this.parameters.remove(str.trim().toLowerCase());
    }

    public Enumeration<String> getNames() {
        return this.parameters.keys();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(this.parameters.size() * 16);
        Enumeration<String> enumerationKeys = this.parameters.keys();
        while (enumerationKeys.hasMoreElements()) {
            sb.append(VectorFormat.DEFAULT_SEPARATOR);
            String strNextElement = enumerationKeys.nextElement2();
            sb.append(strNextElement);
            sb.append('=');
            sb.append(quote(this.parameters.get(strNextElement)));
        }
        return sb.toString();
    }

    public Object clone() {
        MimeTypeParameterList mimeTypeParameterList = null;
        try {
            mimeTypeParameterList = (MimeTypeParameterList) super.clone();
        } catch (CloneNotSupportedException e2) {
        }
        mimeTypeParameterList.parameters = (Hashtable) this.parameters.clone();
        return mimeTypeParameterList;
    }

    private static boolean isTokenChar(char c2) {
        return c2 > ' ' && c2 < 127 && TSPECIALS.indexOf(c2) < 0;
    }

    private static int skipWhiteSpace(String str, int i2) {
        int length = str.length();
        if (i2 < length) {
            char cCharAt = str.charAt(i2);
            while (true) {
                char c2 = cCharAt;
                if (i2 >= length || !Character.isWhitespace(c2)) {
                    break;
                }
                i2++;
                cCharAt = str.charAt(i2);
            }
        }
        return i2;
    }

    private static String quote(String str) {
        boolean z2 = false;
        int length = str.length();
        for (int i2 = 0; i2 < length && !z2; i2++) {
            z2 = !isTokenChar(str.charAt(i2));
        }
        if (z2) {
            StringBuilder sb = new StringBuilder((int) (length * 1.5d));
            sb.append('\"');
            for (int i3 = 0; i3 < length; i3++) {
                char cCharAt = str.charAt(i3);
                if (cCharAt == '\\' || cCharAt == '\"') {
                    sb.append('\\');
                }
                sb.append(cCharAt);
            }
            sb.append('\"');
            return sb.toString();
        }
        return str;
    }

    private static String unquote(String str) {
        int length = str.length();
        StringBuilder sb = new StringBuilder(length);
        boolean z2 = false;
        for (int i2 = 0; i2 < length; i2++) {
            char cCharAt = str.charAt(i2);
            if (!z2 && cCharAt != '\\') {
                sb.append(cCharAt);
            } else if (z2) {
                sb.append(cCharAt);
                z2 = false;
            } else {
                z2 = true;
            }
        }
        return sb.toString();
    }
}
