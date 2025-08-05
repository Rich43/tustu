package javax.lang.model;

import com.sun.org.apache.xalan.internal.templates.Constants;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import com.sun.xml.internal.ws.model.RuntimeModeler;
import com.sun.xml.internal.ws.policy.PolicyConstants;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javafx.fxml.FXMLLoader;

/* loaded from: rt.jar:javax/lang/model/SourceVersion.class */
public enum SourceVersion {
    RELEASE_0,
    RELEASE_1,
    RELEASE_2,
    RELEASE_3,
    RELEASE_4,
    RELEASE_5,
    RELEASE_6,
    RELEASE_7,
    RELEASE_8;

    private static final SourceVersion latestSupported = getLatestSupported();
    private static final Set<String> keywords;

    static {
        HashSet hashSet = new HashSet();
        for (String str : new String[]{"abstract", "continue", "for", "new", "switch", "assert", "default", Constants.ELEMNAME_IF_STRING, "package", "synchronized", "boolean", "do", "goto", PolicyConstants.VISIBILITY_VALUE_PRIVATE, "this", "break", SchemaSymbols.ATTVAL_DOUBLE, "implements", "protected", "throw", SchemaSymbols.ATTVAL_BYTE, "else", "import", "public", "throws", "case", "enum", "instanceof", RuntimeModeler.RETURN, "transient", "catch", "extends", "int", SchemaSymbols.ATTVAL_SHORT, "try", "char", "final", "interface", "static", "void", Constants.ATTRNAME_CLASS, "finally", SchemaSymbols.ATTVAL_LONG, "strictfp", "volatile", "const", SchemaSymbols.ATTVAL_FLOAT, "native", "super", "while", FXMLLoader.NULL_KEYWORD, "true", "false"}) {
            hashSet.add(str);
        }
        keywords = Collections.unmodifiableSet(hashSet);
    }

    public static SourceVersion latest() {
        return RELEASE_8;
    }

    private static SourceVersion getLatestSupported() {
        String property;
        try {
            property = System.getProperty("java.specification.version");
        } catch (SecurityException e2) {
        }
        if ("1.8".equals(property)) {
            return RELEASE_8;
        }
        if ("1.7".equals(property)) {
            return RELEASE_7;
        }
        if ("1.6".equals(property)) {
            return RELEASE_6;
        }
        return RELEASE_5;
    }

    public static SourceVersion latestSupported() {
        return latestSupported;
    }

    public static boolean isIdentifier(CharSequence charSequence) {
        String string = charSequence.toString();
        if (string.length() == 0) {
            return false;
        }
        int iCodePointAt = string.codePointAt(0);
        if (!Character.isJavaIdentifierStart(iCodePointAt)) {
            return false;
        }
        int iCharCount = Character.charCount(iCodePointAt);
        while (true) {
            int i2 = iCharCount;
            if (i2 < string.length()) {
                int iCodePointAt2 = string.codePointAt(i2);
                if (Character.isJavaIdentifierPart(iCodePointAt2)) {
                    iCharCount = i2 + Character.charCount(iCodePointAt2);
                } else {
                    return false;
                }
            } else {
                return true;
            }
        }
    }

    public static boolean isName(CharSequence charSequence) {
        for (String str : charSequence.toString().split("\\.", -1)) {
            if (!isIdentifier(str) || isKeyword(str)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isKeyword(CharSequence charSequence) {
        return keywords.contains(charSequence.toString());
    }
}
