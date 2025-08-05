package java.time.format;

import java.text.DecimalFormatSymbols;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/* loaded from: rt.jar:java/time/format/DecimalStyle.class */
public final class DecimalStyle {
    public static final DecimalStyle STANDARD = new DecimalStyle('0', '+', '-', '.');
    private static final ConcurrentMap<Locale, DecimalStyle> CACHE = new ConcurrentHashMap(16, 0.75f, 2);
    private final char zeroDigit;
    private final char positiveSign;
    private final char negativeSign;
    private final char decimalSeparator;

    public static Set<Locale> getAvailableLocales() {
        Locale[] availableLocales = DecimalFormatSymbols.getAvailableLocales();
        HashSet hashSet = new HashSet(availableLocales.length);
        Collections.addAll(hashSet, availableLocales);
        return hashSet;
    }

    public static DecimalStyle ofDefaultLocale() {
        return of(Locale.getDefault(Locale.Category.FORMAT));
    }

    public static DecimalStyle of(Locale locale) {
        Objects.requireNonNull(locale, "locale");
        DecimalStyle decimalStyle = CACHE.get(locale);
        if (decimalStyle == null) {
            CACHE.putIfAbsent(locale, create(locale));
            decimalStyle = CACHE.get(locale);
        }
        return decimalStyle;
    }

    private static DecimalStyle create(Locale locale) {
        DecimalFormatSymbols decimalFormatSymbols = DecimalFormatSymbols.getInstance(locale);
        char zeroDigit = decimalFormatSymbols.getZeroDigit();
        char minusSign = decimalFormatSymbols.getMinusSign();
        char decimalSeparator = decimalFormatSymbols.getDecimalSeparator();
        if (zeroDigit == '0' && minusSign == '-' && decimalSeparator == '.') {
            return STANDARD;
        }
        return new DecimalStyle(zeroDigit, '+', minusSign, decimalSeparator);
    }

    private DecimalStyle(char c2, char c3, char c4, char c5) {
        this.zeroDigit = c2;
        this.positiveSign = c3;
        this.negativeSign = c4;
        this.decimalSeparator = c5;
    }

    public char getZeroDigit() {
        return this.zeroDigit;
    }

    public DecimalStyle withZeroDigit(char c2) {
        if (c2 == this.zeroDigit) {
            return this;
        }
        return new DecimalStyle(c2, this.positiveSign, this.negativeSign, this.decimalSeparator);
    }

    public char getPositiveSign() {
        return this.positiveSign;
    }

    public DecimalStyle withPositiveSign(char c2) {
        if (c2 == this.positiveSign) {
            return this;
        }
        return new DecimalStyle(this.zeroDigit, c2, this.negativeSign, this.decimalSeparator);
    }

    public char getNegativeSign() {
        return this.negativeSign;
    }

    public DecimalStyle withNegativeSign(char c2) {
        if (c2 == this.negativeSign) {
            return this;
        }
        return new DecimalStyle(this.zeroDigit, this.positiveSign, c2, this.decimalSeparator);
    }

    public char getDecimalSeparator() {
        return this.decimalSeparator;
    }

    public DecimalStyle withDecimalSeparator(char c2) {
        if (c2 == this.decimalSeparator) {
            return this;
        }
        return new DecimalStyle(this.zeroDigit, this.positiveSign, this.negativeSign, c2);
    }

    int convertToDigit(char c2) {
        int i2 = c2 - this.zeroDigit;
        if (i2 < 0 || i2 > 9) {
            return -1;
        }
        return i2;
    }

    String convertNumberToI18N(String str) {
        if (this.zeroDigit == '0') {
            return str;
        }
        int i2 = this.zeroDigit - '0';
        char[] charArray = str.toCharArray();
        for (int i3 = 0; i3 < charArray.length; i3++) {
            charArray[i3] = (char) (charArray[i3] + i2);
        }
        return new String(charArray);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof DecimalStyle) {
            DecimalStyle decimalStyle = (DecimalStyle) obj;
            return this.zeroDigit == decimalStyle.zeroDigit && this.positiveSign == decimalStyle.positiveSign && this.negativeSign == decimalStyle.negativeSign && this.decimalSeparator == decimalStyle.decimalSeparator;
        }
        return false;
    }

    public int hashCode() {
        return this.zeroDigit + this.positiveSign + this.negativeSign + this.decimalSeparator;
    }

    public String toString() {
        return "DecimalStyle[" + this.zeroDigit + this.positiveSign + this.negativeSign + this.decimalSeparator + "]";
    }
}
