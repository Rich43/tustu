package java.text;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.text.spi.DecimalFormatSymbolsProvider;
import java.util.Currency;
import java.util.Locale;
import sun.util.locale.provider.LocaleProviderAdapter;
import sun.util.locale.provider.LocaleServiceProviderPool;
import sun.util.locale.provider.ResourceBundleBasedAdapter;

/* loaded from: rt.jar:java/text/DecimalFormatSymbols.class */
public class DecimalFormatSymbols implements Cloneable, Serializable {
    private char zeroDigit;
    private char groupingSeparator;
    private char decimalSeparator;
    private char perMill;
    private char percent;
    private char digit;
    private char patternSeparator;
    private String infinity;
    private String NaN;
    private char minusSign;
    private String currencySymbol;
    private String intlCurrencySymbol;
    private char monetarySeparator;
    private char exponential;
    private String exponentialSeparator;
    private Locale locale;
    private transient Currency currency;
    static final long serialVersionUID = 5772796243397350300L;
    private static final int currentSerialVersion = 3;
    private int serialVersionOnStream = 3;

    public DecimalFormatSymbols() {
        initialize(Locale.getDefault(Locale.Category.FORMAT));
    }

    public DecimalFormatSymbols(Locale locale) {
        initialize(locale);
    }

    public static Locale[] getAvailableLocales() {
        return LocaleServiceProviderPool.getPool(DecimalFormatSymbolsProvider.class).getAvailableLocales();
    }

    public static final DecimalFormatSymbols getInstance() {
        return getInstance(Locale.getDefault(Locale.Category.FORMAT));
    }

    public static final DecimalFormatSymbols getInstance(Locale locale) {
        DecimalFormatSymbols decimalFormatSymbolsProvider = LocaleProviderAdapter.getAdapter(DecimalFormatSymbolsProvider.class, locale).getDecimalFormatSymbolsProvider().getInstance(locale);
        if (decimalFormatSymbolsProvider == null) {
            decimalFormatSymbolsProvider = LocaleProviderAdapter.forJRE().getDecimalFormatSymbolsProvider().getInstance(locale);
        }
        return decimalFormatSymbolsProvider;
    }

    public char getZeroDigit() {
        return this.zeroDigit;
    }

    public void setZeroDigit(char c2) {
        this.zeroDigit = c2;
    }

    public char getGroupingSeparator() {
        return this.groupingSeparator;
    }

    public void setGroupingSeparator(char c2) {
        this.groupingSeparator = c2;
    }

    public char getDecimalSeparator() {
        return this.decimalSeparator;
    }

    public void setDecimalSeparator(char c2) {
        this.decimalSeparator = c2;
    }

    public char getPerMill() {
        return this.perMill;
    }

    public void setPerMill(char c2) {
        this.perMill = c2;
    }

    public char getPercent() {
        return this.percent;
    }

    public void setPercent(char c2) {
        this.percent = c2;
    }

    public char getDigit() {
        return this.digit;
    }

    public void setDigit(char c2) {
        this.digit = c2;
    }

    public char getPatternSeparator() {
        return this.patternSeparator;
    }

    public void setPatternSeparator(char c2) {
        this.patternSeparator = c2;
    }

    public String getInfinity() {
        return this.infinity;
    }

    public void setInfinity(String str) {
        this.infinity = str;
    }

    public String getNaN() {
        return this.NaN;
    }

    public void setNaN(String str) {
        this.NaN = str;
    }

    public char getMinusSign() {
        return this.minusSign;
    }

    public void setMinusSign(char c2) {
        this.minusSign = c2;
    }

    public String getCurrencySymbol() {
        return this.currencySymbol;
    }

    public void setCurrencySymbol(String str) {
        this.currencySymbol = str;
    }

    public String getInternationalCurrencySymbol() {
        return this.intlCurrencySymbol;
    }

    public void setInternationalCurrencySymbol(String str) {
        this.intlCurrencySymbol = str;
        this.currency = null;
        if (str != null) {
            try {
                this.currency = Currency.getInstance(str);
                this.currencySymbol = this.currency.getSymbol();
            } catch (IllegalArgumentException e2) {
            }
        }
    }

    public Currency getCurrency() {
        return this.currency;
    }

    public void setCurrency(Currency currency) {
        if (currency == null) {
            throw new NullPointerException();
        }
        this.currency = currency;
        this.intlCurrencySymbol = currency.getCurrencyCode();
        this.currencySymbol = currency.getSymbol(this.locale);
    }

    public char getMonetaryDecimalSeparator() {
        return this.monetarySeparator;
    }

    public void setMonetaryDecimalSeparator(char c2) {
        this.monetarySeparator = c2;
    }

    char getExponentialSymbol() {
        return this.exponential;
    }

    public String getExponentSeparator() {
        return this.exponentialSeparator;
    }

    void setExponentialSymbol(char c2) {
        this.exponential = c2;
    }

    public void setExponentSeparator(String str) {
        if (str == null) {
            throw new NullPointerException();
        }
        this.exponentialSeparator = str;
    }

    public Object clone() {
        try {
            return (DecimalFormatSymbols) super.clone();
        } catch (CloneNotSupportedException e2) {
            throw new InternalError(e2);
        }
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
        DecimalFormatSymbols decimalFormatSymbols = (DecimalFormatSymbols) obj;
        return this.zeroDigit == decimalFormatSymbols.zeroDigit && this.groupingSeparator == decimalFormatSymbols.groupingSeparator && this.decimalSeparator == decimalFormatSymbols.decimalSeparator && this.percent == decimalFormatSymbols.percent && this.perMill == decimalFormatSymbols.perMill && this.digit == decimalFormatSymbols.digit && this.minusSign == decimalFormatSymbols.minusSign && this.patternSeparator == decimalFormatSymbols.patternSeparator && this.infinity.equals(decimalFormatSymbols.infinity) && this.NaN.equals(decimalFormatSymbols.NaN) && this.currencySymbol.equals(decimalFormatSymbols.currencySymbol) && this.intlCurrencySymbol.equals(decimalFormatSymbols.intlCurrencySymbol) && this.currency == decimalFormatSymbols.currency && this.monetarySeparator == decimalFormatSymbols.monetarySeparator && this.exponentialSeparator.equals(decimalFormatSymbols.exponentialSeparator) && this.locale.equals(decimalFormatSymbols.locale);
    }

    public int hashCode() {
        return (((this.zeroDigit * '%') + this.groupingSeparator) * 37) + this.decimalSeparator;
    }

    private void initialize(Locale locale) {
        this.locale = locale;
        LocaleProviderAdapter adapter = LocaleProviderAdapter.getAdapter(DecimalFormatSymbolsProvider.class, locale);
        if (!(adapter instanceof ResourceBundleBasedAdapter)) {
            adapter = LocaleProviderAdapter.getResourceBundleBased();
        }
        Object[] decimalFormatSymbolsData = adapter.getLocaleResources(locale).getDecimalFormatSymbolsData();
        String[] strArr = (String[]) decimalFormatSymbolsData[0];
        this.decimalSeparator = strArr[0].charAt(0);
        this.groupingSeparator = strArr[1].charAt(0);
        this.patternSeparator = strArr[2].charAt(0);
        this.percent = strArr[3].charAt(0);
        this.zeroDigit = strArr[4].charAt(0);
        this.digit = strArr[5].charAt(0);
        this.minusSign = strArr[6].charAt(0);
        this.exponential = strArr[7].charAt(0);
        this.exponentialSeparator = strArr[7];
        this.perMill = strArr[8].charAt(0);
        this.infinity = strArr[9];
        this.NaN = strArr[10];
        if (locale.getCountry().length() > 0) {
            try {
                this.currency = Currency.getInstance(locale);
            } catch (IllegalArgumentException e2) {
            }
        }
        if (this.currency != null) {
            this.intlCurrencySymbol = this.currency.getCurrencyCode();
            if (decimalFormatSymbolsData[1] != null && decimalFormatSymbolsData[1] == this.intlCurrencySymbol) {
                this.currencySymbol = (String) decimalFormatSymbolsData[2];
            } else {
                this.currencySymbol = this.currency.getSymbol(locale);
                decimalFormatSymbolsData[1] = this.intlCurrencySymbol;
                decimalFormatSymbolsData[2] = this.currencySymbol;
            }
        } else {
            this.intlCurrencySymbol = "XXX";
            try {
                this.currency = Currency.getInstance(this.intlCurrencySymbol);
            } catch (IllegalArgumentException e3) {
            }
            this.currencySymbol = "¤";
        }
        this.monetarySeparator = this.decimalSeparator;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        if (this.serialVersionOnStream < 1) {
            this.monetarySeparator = this.decimalSeparator;
            this.exponential = 'E';
        }
        if (this.serialVersionOnStream < 2) {
            this.locale = Locale.ROOT;
        }
        if (this.serialVersionOnStream < 3) {
            this.exponentialSeparator = Character.toString(this.exponential);
        }
        this.serialVersionOnStream = 3;
        if (this.intlCurrencySymbol != null) {
            try {
                this.currency = Currency.getInstance(this.intlCurrencySymbol);
            } catch (IllegalArgumentException e2) {
            }
        }
    }
}
