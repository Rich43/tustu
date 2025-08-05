package java.text;

import com.sun.org.apache.xalan.internal.templates.Constants;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.Format;
import java.text.spi.NumberFormatProvider;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.provider.LocaleProviderAdapter;
import sun.util.locale.provider.LocaleServiceProviderPool;

/* loaded from: rt.jar:java/text/NumberFormat.class */
public abstract class NumberFormat extends Format {
    public static final int INTEGER_FIELD = 0;
    public static final int FRACTION_FIELD = 1;
    private static final int NUMBERSTYLE = 0;
    private static final int CURRENCYSTYLE = 1;
    private static final int PERCENTSTYLE = 2;
    private static final int SCIENTIFICSTYLE = 3;
    private static final int INTEGERSTYLE = 4;
    static final int currentSerialVersion = 1;
    static final long serialVersionUID = -2308460125733713944L;
    private boolean groupingUsed = true;
    private byte maxIntegerDigits = 40;
    private byte minIntegerDigits = 1;
    private byte maxFractionDigits = 3;
    private byte minFractionDigits = 0;
    private boolean parseIntegerOnly = false;
    private int maximumIntegerDigits = 40;
    private int minimumIntegerDigits = 1;
    private int maximumFractionDigits = 3;
    private int minimumFractionDigits = 0;
    private int serialVersionOnStream = 1;

    public abstract StringBuffer format(double d2, StringBuffer stringBuffer, FieldPosition fieldPosition);

    public abstract StringBuffer format(long j2, StringBuffer stringBuffer, FieldPosition fieldPosition);

    public abstract Number parse(String str, ParsePosition parsePosition);

    protected NumberFormat() {
    }

    @Override // java.text.Format
    public StringBuffer format(Object obj, StringBuffer stringBuffer, FieldPosition fieldPosition) {
        if ((obj instanceof Long) || (obj instanceof Integer) || (obj instanceof Short) || (obj instanceof Byte) || (obj instanceof AtomicInteger) || (obj instanceof AtomicLong) || ((obj instanceof BigInteger) && ((BigInteger) obj).bitLength() < 64)) {
            return format(((Number) obj).longValue(), stringBuffer, fieldPosition);
        }
        if (obj instanceof Number) {
            return format(((Number) obj).doubleValue(), stringBuffer, fieldPosition);
        }
        throw new IllegalArgumentException("Cannot format given Object as a Number");
    }

    @Override // java.text.Format
    public final Object parseObject(String str, ParsePosition parsePosition) {
        return parse(str, parsePosition);
    }

    public final String format(double d2) {
        String strFastFormat = fastFormat(d2);
        if (strFastFormat != null) {
            return strFastFormat;
        }
        return format(d2, new StringBuffer(), DontCareFieldPosition.INSTANCE).toString();
    }

    String fastFormat(double d2) {
        return null;
    }

    public final String format(long j2) {
        return format(j2, new StringBuffer(), DontCareFieldPosition.INSTANCE).toString();
    }

    public Number parse(String str) throws ParseException {
        ParsePosition parsePosition = new ParsePosition(0);
        Number number = parse(str, parsePosition);
        if (parsePosition.index == 0) {
            throw new ParseException("Unparseable number: \"" + str + PdfOps.DOUBLE_QUOTE__TOKEN, parsePosition.errorIndex);
        }
        return number;
    }

    public boolean isParseIntegerOnly() {
        return this.parseIntegerOnly;
    }

    public void setParseIntegerOnly(boolean z2) {
        this.parseIntegerOnly = z2;
    }

    public static final NumberFormat getInstance() {
        return getInstance(Locale.getDefault(Locale.Category.FORMAT), 0);
    }

    public static NumberFormat getInstance(Locale locale) {
        return getInstance(locale, 0);
    }

    public static final NumberFormat getNumberInstance() {
        return getInstance(Locale.getDefault(Locale.Category.FORMAT), 0);
    }

    public static NumberFormat getNumberInstance(Locale locale) {
        return getInstance(locale, 0);
    }

    public static final NumberFormat getIntegerInstance() {
        return getInstance(Locale.getDefault(Locale.Category.FORMAT), 4);
    }

    public static NumberFormat getIntegerInstance(Locale locale) {
        return getInstance(locale, 4);
    }

    public static final NumberFormat getCurrencyInstance() {
        return getInstance(Locale.getDefault(Locale.Category.FORMAT), 1);
    }

    public static NumberFormat getCurrencyInstance(Locale locale) {
        return getInstance(locale, 1);
    }

    public static final NumberFormat getPercentInstance() {
        return getInstance(Locale.getDefault(Locale.Category.FORMAT), 2);
    }

    public static NumberFormat getPercentInstance(Locale locale) {
        return getInstance(locale, 2);
    }

    static final NumberFormat getScientificInstance() {
        return getInstance(Locale.getDefault(Locale.Category.FORMAT), 3);
    }

    static NumberFormat getScientificInstance(Locale locale) {
        return getInstance(locale, 3);
    }

    public static Locale[] getAvailableLocales() {
        return LocaleServiceProviderPool.getPool(NumberFormatProvider.class).getAvailableLocales();
    }

    public int hashCode() {
        return (this.maximumIntegerDigits * 37) + this.maxFractionDigits;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        NumberFormat numberFormat = (NumberFormat) obj;
        return this.maximumIntegerDigits == numberFormat.maximumIntegerDigits && this.minimumIntegerDigits == numberFormat.minimumIntegerDigits && this.maximumFractionDigits == numberFormat.maximumFractionDigits && this.minimumFractionDigits == numberFormat.minimumFractionDigits && this.groupingUsed == numberFormat.groupingUsed && this.parseIntegerOnly == numberFormat.parseIntegerOnly;
    }

    @Override // java.text.Format
    public Object clone() {
        return (NumberFormat) super.clone();
    }

    public boolean isGroupingUsed() {
        return this.groupingUsed;
    }

    public void setGroupingUsed(boolean z2) {
        this.groupingUsed = z2;
    }

    public int getMaximumIntegerDigits() {
        return this.maximumIntegerDigits;
    }

    public void setMaximumIntegerDigits(int i2) {
        this.maximumIntegerDigits = Math.max(0, i2);
        if (this.minimumIntegerDigits > this.maximumIntegerDigits) {
            this.minimumIntegerDigits = this.maximumIntegerDigits;
        }
    }

    public int getMinimumIntegerDigits() {
        return this.minimumIntegerDigits;
    }

    public void setMinimumIntegerDigits(int i2) {
        this.minimumIntegerDigits = Math.max(0, i2);
        if (this.minimumIntegerDigits > this.maximumIntegerDigits) {
            this.maximumIntegerDigits = this.minimumIntegerDigits;
        }
    }

    public int getMaximumFractionDigits() {
        return this.maximumFractionDigits;
    }

    public void setMaximumFractionDigits(int i2) {
        this.maximumFractionDigits = Math.max(0, i2);
        if (this.maximumFractionDigits < this.minimumFractionDigits) {
            this.minimumFractionDigits = this.maximumFractionDigits;
        }
    }

    public int getMinimumFractionDigits() {
        return this.minimumFractionDigits;
    }

    public void setMinimumFractionDigits(int i2) {
        this.minimumFractionDigits = Math.max(0, i2);
        if (this.maximumFractionDigits < this.minimumFractionDigits) {
            this.maximumFractionDigits = this.minimumFractionDigits;
        }
    }

    public Currency getCurrency() {
        throw new UnsupportedOperationException();
    }

    public void setCurrency(Currency currency) {
        throw new UnsupportedOperationException();
    }

    public RoundingMode getRoundingMode() {
        throw new UnsupportedOperationException();
    }

    public void setRoundingMode(RoundingMode roundingMode) {
        throw new UnsupportedOperationException();
    }

    private static NumberFormat getInstance(Locale locale, int i2) {
        NumberFormat numberFormat = getInstance(LocaleProviderAdapter.getAdapter(NumberFormatProvider.class, locale), locale, i2);
        if (numberFormat == null) {
            numberFormat = getInstance(LocaleProviderAdapter.forJRE(), locale, i2);
        }
        return numberFormat;
    }

    private static NumberFormat getInstance(LocaleProviderAdapter localeProviderAdapter, Locale locale, int i2) {
        NumberFormatProvider numberFormatProvider = localeProviderAdapter.getNumberFormatProvider();
        NumberFormat integerInstance = null;
        switch (i2) {
            case 0:
                integerInstance = numberFormatProvider.getNumberInstance(locale);
                break;
            case 1:
                integerInstance = numberFormatProvider.getCurrencyInstance(locale);
                break;
            case 2:
                integerInstance = numberFormatProvider.getPercentInstance(locale);
                break;
            case 4:
                integerInstance = numberFormatProvider.getIntegerInstance(locale);
                break;
        }
        return integerInstance;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        if (this.serialVersionOnStream < 1) {
            this.maximumIntegerDigits = this.maxIntegerDigits;
            this.minimumIntegerDigits = this.minIntegerDigits;
            this.maximumFractionDigits = this.maxFractionDigits;
            this.minimumFractionDigits = this.minFractionDigits;
        }
        if (this.minimumIntegerDigits > this.maximumIntegerDigits || this.minimumFractionDigits > this.maximumFractionDigits || this.minimumIntegerDigits < 0 || this.minimumFractionDigits < 0) {
            throw new InvalidObjectException("Digit count range invalid");
        }
        this.serialVersionOnStream = 1;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        this.maxIntegerDigits = this.maximumIntegerDigits > 127 ? Byte.MAX_VALUE : (byte) this.maximumIntegerDigits;
        this.minIntegerDigits = this.minimumIntegerDigits > 127 ? Byte.MAX_VALUE : (byte) this.minimumIntegerDigits;
        this.maxFractionDigits = this.maximumFractionDigits > 127 ? Byte.MAX_VALUE : (byte) this.maximumFractionDigits;
        this.minFractionDigits = this.minimumFractionDigits > 127 ? Byte.MAX_VALUE : (byte) this.minimumFractionDigits;
        objectOutputStream.defaultWriteObject();
    }

    /* loaded from: rt.jar:java/text/NumberFormat$Field.class */
    public static class Field extends Format.Field {
        private static final long serialVersionUID = 7494728892700160890L;
        private static final Map<String, Field> instanceMap = new HashMap(11);
        public static final Field INTEGER = new Field(SchemaSymbols.ATTVAL_INTEGER);
        public static final Field FRACTION = new Field("fraction");
        public static final Field EXPONENT = new Field("exponent");
        public static final Field DECIMAL_SEPARATOR = new Field("decimal separator");
        public static final Field SIGN = new Field("sign");
        public static final Field GROUPING_SEPARATOR = new Field("grouping separator");
        public static final Field EXPONENT_SYMBOL = new Field("exponent symbol");
        public static final Field PERCENT = new Field(Constants.ATTRNAME_PERCENT);
        public static final Field PERMILLE = new Field("per mille");
        public static final Field CURRENCY = new Field("currency");
        public static final Field EXPONENT_SIGN = new Field("exponent sign");

        protected Field(String str) {
            super(str);
            if (getClass() == Field.class) {
                instanceMap.put(str, this);
            }
        }

        @Override // java.text.AttributedCharacterIterator.Attribute
        protected Object readResolve() throws InvalidObjectException {
            if (getClass() != Field.class) {
                throw new InvalidObjectException("subclass didn't correctly implement readResolve");
            }
            Field field = instanceMap.get(getName());
            if (field != null) {
                return field;
            }
            throw new InvalidObjectException("unknown attribute name");
        }
    }
}
