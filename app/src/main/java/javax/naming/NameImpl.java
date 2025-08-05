package javax.naming;

import java.util.Enumeration;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Vector;
import javafx.fxml.FXMLLoader;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:javax/naming/NameImpl.class */
class NameImpl {
    private static final byte LEFT_TO_RIGHT = 1;
    private static final byte RIGHT_TO_LEFT = 2;
    private static final byte FLAT = 0;
    private Vector<String> components;
    private byte syntaxDirection;
    private String syntaxSeparator;
    private String syntaxSeparator2;
    private boolean syntaxCaseInsensitive;
    private boolean syntaxTrimBlanks;
    private String syntaxEscape;
    private String syntaxBeginQuote1;
    private String syntaxEndQuote1;
    private String syntaxBeginQuote2;
    private String syntaxEndQuote2;
    private String syntaxAvaSeparator;
    private String syntaxTypevalSeparator;
    private static final int STYLE_NONE = 0;
    private static final int STYLE_QUOTE1 = 1;
    private static final int STYLE_QUOTE2 = 2;
    private static final int STYLE_ESCAPE = 3;
    private int escapingStyle;

    private final boolean isA(String str, int i2, String str2) {
        return str2 != null && str.startsWith(str2, i2);
    }

    private final boolean isMeta(String str, int i2) {
        return isA(str, i2, this.syntaxEscape) || isA(str, i2, this.syntaxBeginQuote1) || isA(str, i2, this.syntaxBeginQuote2) || isSeparator(str, i2);
    }

    private final boolean isSeparator(String str, int i2) {
        return isA(str, i2, this.syntaxSeparator) || isA(str, i2, this.syntaxSeparator2);
    }

    private final int skipSeparator(String str, int i2) {
        if (isA(str, i2, this.syntaxSeparator)) {
            i2 += this.syntaxSeparator.length();
        } else if (isA(str, i2, this.syntaxSeparator2)) {
            i2 += this.syntaxSeparator2.length();
        }
        return i2;
    }

    private final int extractComp(String str, int i2, int i3, Vector<String> vector) throws InvalidNameException {
        boolean zIsA;
        boolean zIsA2;
        boolean z2 = true;
        StringBuffer stringBuffer = new StringBuffer(i3);
        while (i2 < i3) {
            if (z2 && ((zIsA2 = isA(str, i2, this.syntaxBeginQuote1)) || isA(str, i2, this.syntaxBeginQuote2))) {
                String str2 = zIsA2 ? this.syntaxBeginQuote1 : this.syntaxBeginQuote2;
                String str3 = zIsA2 ? this.syntaxEndQuote1 : this.syntaxEndQuote2;
                if (this.escapingStyle == 0) {
                    this.escapingStyle = zIsA2 ? 1 : 2;
                }
                int length = i2 + str2.length();
                while (length < i3 && !str.startsWith(str3, length)) {
                    if (isA(str, length, this.syntaxEscape) && isA(str, length + this.syntaxEscape.length(), str3)) {
                        length += this.syntaxEscape.length();
                    }
                    stringBuffer.append(str.charAt(length));
                    length++;
                }
                if (length >= i3) {
                    throw new InvalidNameException(str + ": no close quote");
                }
                i2 = length + str3.length();
                if (i2 != i3 && !isSeparator(str, i2)) {
                    throw new InvalidNameException(str + ": close quote appears before end of component");
                }
            } else {
                if (isSeparator(str, i2)) {
                    break;
                }
                if (isA(str, i2, this.syntaxEscape)) {
                    if (isMeta(str, i2 + this.syntaxEscape.length())) {
                        i2 += this.syntaxEscape.length();
                        if (this.escapingStyle == 0) {
                            this.escapingStyle = 3;
                        }
                    } else if (i2 + this.syntaxEscape.length() >= i3) {
                        throw new InvalidNameException(str + ": unescaped " + this.syntaxEscape + " at end of component");
                    }
                } else if (isA(str, i2, this.syntaxTypevalSeparator) && ((zIsA = isA(str, i2 + this.syntaxTypevalSeparator.length(), this.syntaxBeginQuote1)) || isA(str, i2 + this.syntaxTypevalSeparator.length(), this.syntaxBeginQuote2))) {
                    String str4 = zIsA ? this.syntaxBeginQuote1 : this.syntaxBeginQuote2;
                    String str5 = zIsA ? this.syntaxEndQuote1 : this.syntaxEndQuote2;
                    int length2 = i2 + this.syntaxTypevalSeparator.length();
                    stringBuffer.append(this.syntaxTypevalSeparator + str4);
                    int length3 = length2 + str4.length();
                    while (length3 < i3 && !str.startsWith(str5, length3)) {
                        if (isA(str, length3, this.syntaxEscape) && isA(str, length3 + this.syntaxEscape.length(), str5)) {
                            length3 += this.syntaxEscape.length();
                        }
                        stringBuffer.append(str.charAt(length3));
                        length3++;
                    }
                    if (length3 >= i3) {
                        throw new InvalidNameException(str + ": typeval no close quote");
                    }
                    i2 = length3 + str5.length();
                    stringBuffer.append(str5);
                    if (i2 != i3 && !isSeparator(str, i2)) {
                        throw new InvalidNameException(str.substring(i2) + ": typeval close quote appears before end of component");
                    }
                }
                int i4 = i2;
                i2++;
                stringBuffer.append(str.charAt(i4));
                z2 = false;
            }
        }
        if (this.syntaxDirection == 2) {
            vector.insertElementAt(stringBuffer.toString(), 0);
        } else {
            vector.addElement(stringBuffer.toString());
        }
        return i2;
    }

    private static boolean getBoolean(Properties properties, String str) {
        return toBoolean(properties.getProperty(str));
    }

    private static boolean toBoolean(String str) {
        return str != null && str.toLowerCase(Locale.ENGLISH).equals("true");
    }

    private final void recordNamingConvention(Properties properties) {
        String property = properties.getProperty("jndi.syntax.direction", "flat");
        if (property.equals("left_to_right")) {
            this.syntaxDirection = (byte) 1;
        } else if (property.equals("right_to_left")) {
            this.syntaxDirection = (byte) 2;
        } else if (property.equals("flat")) {
            this.syntaxDirection = (byte) 0;
        } else {
            throw new IllegalArgumentException(property + "is not a valid value for the jndi.syntax.direction property");
        }
        if (this.syntaxDirection != 0) {
            this.syntaxSeparator = properties.getProperty("jndi.syntax.separator");
            this.syntaxSeparator2 = properties.getProperty("jndi.syntax.separator2");
            if (this.syntaxSeparator == null) {
                throw new IllegalArgumentException("jndi.syntax.separator property required for non-flat syntax");
            }
        } else {
            this.syntaxSeparator = null;
        }
        this.syntaxEscape = properties.getProperty("jndi.syntax.escape");
        this.syntaxCaseInsensitive = getBoolean(properties, "jndi.syntax.ignorecase");
        this.syntaxTrimBlanks = getBoolean(properties, "jndi.syntax.trimblanks");
        this.syntaxBeginQuote1 = properties.getProperty("jndi.syntax.beginquote");
        this.syntaxEndQuote1 = properties.getProperty("jndi.syntax.endquote");
        if (this.syntaxEndQuote1 == null && this.syntaxBeginQuote1 != null) {
            this.syntaxEndQuote1 = this.syntaxBeginQuote1;
        } else if (this.syntaxBeginQuote1 == null && this.syntaxEndQuote1 != null) {
            this.syntaxBeginQuote1 = this.syntaxEndQuote1;
        }
        this.syntaxBeginQuote2 = properties.getProperty("jndi.syntax.beginquote2");
        this.syntaxEndQuote2 = properties.getProperty("jndi.syntax.endquote2");
        if (this.syntaxEndQuote2 == null && this.syntaxBeginQuote2 != null) {
            this.syntaxEndQuote2 = this.syntaxBeginQuote2;
        } else if (this.syntaxBeginQuote2 == null && this.syntaxEndQuote2 != null) {
            this.syntaxBeginQuote2 = this.syntaxEndQuote2;
        }
        this.syntaxAvaSeparator = properties.getProperty("jndi.syntax.separator.ava");
        this.syntaxTypevalSeparator = properties.getProperty("jndi.syntax.separator.typeval");
    }

    NameImpl(Properties properties) {
        this.syntaxDirection = (byte) 1;
        this.syntaxSeparator = "/";
        this.syntaxSeparator2 = null;
        this.syntaxCaseInsensitive = false;
        this.syntaxTrimBlanks = false;
        this.syntaxEscape = FXMLLoader.ESCAPE_PREFIX;
        this.syntaxBeginQuote1 = PdfOps.DOUBLE_QUOTE__TOKEN;
        this.syntaxEndQuote1 = PdfOps.DOUBLE_QUOTE__TOKEN;
        this.syntaxBeginQuote2 = PdfOps.SINGLE_QUOTE_TOKEN;
        this.syntaxEndQuote2 = PdfOps.SINGLE_QUOTE_TOKEN;
        this.syntaxAvaSeparator = null;
        this.syntaxTypevalSeparator = null;
        this.escapingStyle = 0;
        if (properties != null) {
            recordNamingConvention(properties);
        }
        this.components = new Vector<>();
    }

    NameImpl(Properties properties, String str) throws InvalidNameException {
        String strLastElement;
        this(properties);
        boolean z2 = this.syntaxDirection == 2;
        boolean z3 = true;
        int length = str.length();
        int iExtractComp = 0;
        while (iExtractComp < length) {
            iExtractComp = extractComp(str, iExtractComp, length, this.components);
            if (z2) {
                strLastElement = this.components.firstElement();
            } else {
                strLastElement = this.components.lastElement();
            }
            z3 = strLastElement.length() >= 1 ? false : z3;
            if (iExtractComp < length) {
                iExtractComp = skipSeparator(str, iExtractComp);
                if (iExtractComp == length && !z3) {
                    if (z2) {
                        this.components.insertElementAt("", 0);
                    } else {
                        this.components.addElement("");
                    }
                }
            }
        }
    }

    NameImpl(Properties properties, Enumeration<String> enumeration) {
        this(properties);
        while (enumeration.hasMoreElements()) {
            this.components.addElement(enumeration.nextElement2());
        }
    }

    private final String stringifyComp(String str) {
        int length = str.length();
        boolean z2 = false;
        boolean z3 = false;
        String str2 = null;
        String str3 = null;
        StringBuffer stringBuffer = new StringBuffer(length);
        if (this.syntaxSeparator != null && str.indexOf(this.syntaxSeparator) >= 0) {
            if (this.syntaxBeginQuote1 != null) {
                str2 = this.syntaxBeginQuote1;
                str3 = this.syntaxEndQuote1;
            } else if (this.syntaxBeginQuote2 != null) {
                str2 = this.syntaxBeginQuote2;
                str3 = this.syntaxEndQuote2;
            } else if (this.syntaxEscape != null) {
                z2 = true;
            }
        }
        if (this.syntaxSeparator2 != null && str.indexOf(this.syntaxSeparator2) >= 0) {
            if (this.syntaxBeginQuote1 != null) {
                if (str2 == null) {
                    str2 = this.syntaxBeginQuote1;
                    str3 = this.syntaxEndQuote1;
                }
            } else if (this.syntaxBeginQuote2 != null) {
                if (str2 == null) {
                    str2 = this.syntaxBeginQuote2;
                    str3 = this.syntaxEndQuote2;
                }
            } else if (this.syntaxEscape != null) {
                z3 = true;
            }
        }
        if (str2 != null) {
            stringBuffer = stringBuffer.append(str2);
            int length2 = 0;
            while (length2 < length) {
                if (str.startsWith(str3, length2)) {
                    stringBuffer.append(this.syntaxEscape).append(str3);
                    length2 += str3.length();
                } else {
                    int i2 = length2;
                    length2++;
                    stringBuffer.append(str.charAt(i2));
                }
            }
            stringBuffer.append(str3);
        } else {
            boolean z4 = true;
            int length3 = 0;
            while (length3 < length) {
                if (z4 && isA(str, length3, this.syntaxBeginQuote1)) {
                    stringBuffer.append(this.syntaxEscape).append(this.syntaxBeginQuote1);
                    length3 += this.syntaxBeginQuote1.length();
                } else if (z4 && isA(str, length3, this.syntaxBeginQuote2)) {
                    stringBuffer.append(this.syntaxEscape).append(this.syntaxBeginQuote2);
                    length3 += this.syntaxBeginQuote2.length();
                } else if (isA(str, length3, this.syntaxEscape)) {
                    if (length3 + this.syntaxEscape.length() >= length || isMeta(str, length3 + this.syntaxEscape.length())) {
                        stringBuffer.append(this.syntaxEscape);
                    }
                    stringBuffer.append(this.syntaxEscape);
                    length3 += this.syntaxEscape.length();
                } else if (z2 && str.startsWith(this.syntaxSeparator, length3)) {
                    stringBuffer.append(this.syntaxEscape).append(this.syntaxSeparator);
                    length3 += this.syntaxSeparator.length();
                } else if (z3 && str.startsWith(this.syntaxSeparator2, length3)) {
                    stringBuffer.append(this.syntaxEscape).append(this.syntaxSeparator2);
                    length3 += this.syntaxSeparator2.length();
                } else {
                    int i3 = length3;
                    length3++;
                    stringBuffer.append(str.charAt(i3));
                }
                z4 = false;
            }
        }
        return stringBuffer.toString();
    }

    public String toString() {
        String strStringifyComp;
        StringBuffer stringBuffer = new StringBuffer();
        boolean z2 = true;
        int size = this.components.size();
        for (int i2 = 0; i2 < size; i2++) {
            if (this.syntaxDirection == 2) {
                strStringifyComp = stringifyComp(this.components.elementAt((size - 1) - i2));
            } else {
                strStringifyComp = stringifyComp(this.components.elementAt(i2));
            }
            if (i2 != 0 && this.syntaxSeparator != null) {
                stringBuffer.append(this.syntaxSeparator);
            }
            if (strStringifyComp.length() >= 1) {
                z2 = false;
            }
            stringBuffer = stringBuffer.append(strStringifyComp);
        }
        if (z2 && size >= 1 && this.syntaxSeparator != null) {
            stringBuffer = stringBuffer.append(this.syntaxSeparator);
        }
        return stringBuffer.toString();
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof NameImpl)) {
            NameImpl nameImpl = (NameImpl) obj;
            if (nameImpl.size() == size()) {
                Enumeration<String> all = getAll();
                Enumeration<String> all2 = nameImpl.getAll();
                while (all.hasMoreElements()) {
                    String strNextElement = all.nextElement2();
                    String strNextElement2 = all2.nextElement2();
                    if (this.syntaxTrimBlanks) {
                        strNextElement = strNextElement.trim();
                        strNextElement2 = strNextElement2.trim();
                    }
                    if (this.syntaxCaseInsensitive) {
                        if (!strNextElement.equalsIgnoreCase(strNextElement2)) {
                            return false;
                        }
                    } else if (!strNextElement.equals(strNextElement2)) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }
        return false;
    }

    public int compareTo(NameImpl nameImpl) {
        int iCompareTo;
        if (this == nameImpl) {
            return 0;
        }
        int size = size();
        int size2 = nameImpl.size();
        int iMin = Math.min(size, size2);
        int i2 = 0;
        int i3 = 0;
        do {
            int i4 = iMin;
            iMin--;
            if (i4 != 0) {
                int i5 = i2;
                i2++;
                String strTrim = get(i5);
                int i6 = i3;
                i3++;
                String strTrim2 = nameImpl.get(i6);
                if (this.syntaxTrimBlanks) {
                    strTrim = strTrim.trim();
                    strTrim2 = strTrim2.trim();
                }
                if (this.syntaxCaseInsensitive) {
                    iCompareTo = strTrim.compareToIgnoreCase(strTrim2);
                } else {
                    iCompareTo = strTrim.compareTo(strTrim2);
                }
            } else {
                return size - size2;
            }
        } while (iCompareTo == 0);
        return iCompareTo;
    }

    public int size() {
        return this.components.size();
    }

    public Enumeration<String> getAll() {
        return this.components.elements();
    }

    public String get(int i2) {
        return this.components.elementAt(i2);
    }

    public Enumeration<String> getPrefix(int i2) {
        if (i2 < 0 || i2 > size()) {
            throw new ArrayIndexOutOfBoundsException(i2);
        }
        return new NameImplEnumerator(this.components, 0, i2);
    }

    public Enumeration<String> getSuffix(int i2) {
        int size = size();
        if (i2 < 0 || i2 > size) {
            throw new ArrayIndexOutOfBoundsException(i2);
        }
        return new NameImplEnumerator(this.components, i2, size);
    }

    public boolean isEmpty() {
        return this.components.isEmpty();
    }

    public boolean startsWith(int i2, Enumeration<String> enumeration) {
        if (i2 < 0 || i2 > size()) {
            return false;
        }
        try {
            Enumeration<String> prefix = getPrefix(i2);
            while (prefix.hasMoreElements()) {
                String strNextElement = prefix.nextElement2();
                String strNextElement2 = enumeration.nextElement2();
                if (this.syntaxTrimBlanks) {
                    strNextElement = strNextElement.trim();
                    strNextElement2 = strNextElement2.trim();
                }
                if (this.syntaxCaseInsensitive) {
                    if (!strNextElement.equalsIgnoreCase(strNextElement2)) {
                        return false;
                    }
                } else if (!strNextElement.equals(strNextElement2)) {
                    return false;
                }
            }
            return true;
        } catch (NoSuchElementException e2) {
            return false;
        }
    }

    public boolean endsWith(int i2, Enumeration<String> enumeration) {
        int size = size() - i2;
        if (size < 0 || size > size()) {
            return false;
        }
        try {
            Enumeration<String> suffix = getSuffix(size);
            while (suffix.hasMoreElements()) {
                String strNextElement = suffix.nextElement2();
                String strNextElement2 = enumeration.nextElement2();
                if (this.syntaxTrimBlanks) {
                    strNextElement = strNextElement.trim();
                    strNextElement2 = strNextElement2.trim();
                }
                if (this.syntaxCaseInsensitive) {
                    if (!strNextElement.equalsIgnoreCase(strNextElement2)) {
                        return false;
                    }
                } else if (!strNextElement.equals(strNextElement2)) {
                    return false;
                }
            }
            return true;
        } catch (NoSuchElementException e2) {
            return false;
        }
    }

    public boolean addAll(Enumeration<String> enumeration) throws InvalidNameException {
        boolean z2 = false;
        while (enumeration.hasMoreElements()) {
            try {
                String strNextElement = enumeration.nextElement2();
                if (size() > 0 && this.syntaxDirection == 0) {
                    throw new InvalidNameException("A flat name can only have a single component");
                }
                this.components.addElement(strNextElement);
                z2 = true;
            } catch (NoSuchElementException e2) {
            }
        }
        return z2;
    }

    public boolean addAll(int i2, Enumeration<String> enumeration) throws InvalidNameException {
        boolean z2 = false;
        int i3 = i2;
        while (enumeration.hasMoreElements()) {
            try {
                String strNextElement = enumeration.nextElement2();
                if (size() > 0 && this.syntaxDirection == 0) {
                    throw new InvalidNameException("A flat name can only have a single component");
                }
                this.components.insertElementAt(strNextElement, i3);
                z2 = true;
                i3++;
            } catch (NoSuchElementException e2) {
            }
        }
        return z2;
    }

    public void add(String str) throws InvalidNameException {
        if (size() > 0 && this.syntaxDirection == 0) {
            throw new InvalidNameException("A flat name can only have a single component");
        }
        this.components.addElement(str);
    }

    public void add(int i2, String str) throws InvalidNameException {
        if (size() > 0 && this.syntaxDirection == 0) {
            throw new InvalidNameException("A flat name can only zero or one component");
        }
        this.components.insertElementAt(str, i2);
    }

    public Object remove(int i2) {
        String strElementAt = this.components.elementAt(i2);
        this.components.removeElementAt(i2);
        return strElementAt;
    }

    public int hashCode() {
        int iHashCode = 0;
        Enumeration<String> all = getAll();
        while (all.hasMoreElements()) {
            String strNextElement = all.nextElement2();
            if (this.syntaxTrimBlanks) {
                strNextElement = strNextElement.trim();
            }
            if (this.syntaxCaseInsensitive) {
                strNextElement = strNextElement.toLowerCase(Locale.ENGLISH);
            }
            iHashCode += strNextElement.hashCode();
        }
        return iHashCode;
    }
}
