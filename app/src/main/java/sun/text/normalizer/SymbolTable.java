package sun.text.normalizer;

import java.text.ParsePosition;

@Deprecated
/* loaded from: rt.jar:sun/text/normalizer/SymbolTable.class */
public interface SymbolTable {

    @Deprecated
    public static final char SYMBOL_REF = '$';

    @Deprecated
    char[] lookup(String str);

    @Deprecated
    UnicodeMatcher lookupMatcher(int i2);

    @Deprecated
    String parseReference(String str, ParsePosition parsePosition, int i2);
}
