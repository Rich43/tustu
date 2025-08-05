package java.util;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.spi.CurrencyNameProvider;
import jdk.internal.dynalink.CallSiteDescriptor;
import sun.util.locale.LanguageTag;
import sun.util.locale.provider.LocaleServiceProviderPool;
import sun.util.logging.PlatformLogger;

/* loaded from: rt.jar:java/util/Currency.class */
public final class Currency implements Serializable {
    private static final long serialVersionUID = -158308464356906721L;
    private final String currencyCode;
    private final transient int defaultFractionDigits;
    private final transient int numericCode;
    private static ConcurrentMap<String, Currency> instances = new ConcurrentHashMap(7);
    private static HashSet<Currency> available;
    static int formatVersion;
    static int dataVersion;
    static int[] mainTable;
    static long[] scCutOverTimes;
    static String[] scOldCurrencies;
    static String[] scNewCurrencies;
    static int[] scOldCurrenciesDFD;
    static int[] scNewCurrenciesDFD;
    static int[] scOldCurrenciesNumericCode;
    static int[] scNewCurrenciesNumericCode;
    static String otherCurrencies;
    static int[] otherCurrenciesDFD;
    static int[] otherCurrenciesNumericCode;
    private static final int MAGIC_NUMBER = 1131770436;
    private static final int A_TO_Z = 26;
    private static final int INVALID_COUNTRY_ENTRY = 127;
    private static final int COUNTRY_WITHOUT_CURRENCY_ENTRY = 512;
    private static final int SIMPLE_CASE_COUNTRY_MASK = 0;
    private static final int SIMPLE_CASE_COUNTRY_FINAL_CHAR_MASK = 31;
    private static final int SIMPLE_CASE_COUNTRY_DEFAULT_DIGITS_MASK = 480;
    private static final int SIMPLE_CASE_COUNTRY_DEFAULT_DIGITS_SHIFT = 5;
    private static final int SIMPLE_CASE_COUNTRY_MAX_DEFAULT_DIGITS = 9;
    private static final int SPECIAL_CASE_COUNTRY_MASK = 512;
    private static final int SPECIAL_CASE_COUNTRY_INDEX_MASK = 31;
    private static final int SPECIAL_CASE_COUNTRY_INDEX_DELTA = 1;
    private static final int COUNTRY_TYPE_MASK = 512;
    private static final int NUMERIC_CODE_MASK = 1047552;
    private static final int NUMERIC_CODE_SHIFT = 10;
    private static final int VALID_FORMAT_VERSION = 2;
    private static final int SYMBOL = 0;
    private static final int DISPLAYNAME = 1;

    static {
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: java.util.Currency.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Void run2() throws NumberFormatException {
                String property = System.getProperty("java.home");
                try {
                    DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(property + File.separator + "lib" + File.separator + "currency.data")));
                    Throwable th = null;
                    try {
                        if (dataInputStream.readInt() != Currency.MAGIC_NUMBER) {
                            throw new InternalError("Currency data is possibly corrupted");
                        }
                        Currency.formatVersion = dataInputStream.readInt();
                        if (Currency.formatVersion != 2) {
                            throw new InternalError("Currency data format is incorrect");
                        }
                        Currency.dataVersion = dataInputStream.readInt();
                        Currency.mainTable = Currency.readIntArray(dataInputStream, 676);
                        int i2 = dataInputStream.readInt();
                        Currency.scCutOverTimes = Currency.readLongArray(dataInputStream, i2);
                        Currency.scOldCurrencies = Currency.readStringArray(dataInputStream, i2);
                        Currency.scNewCurrencies = Currency.readStringArray(dataInputStream, i2);
                        Currency.scOldCurrenciesDFD = Currency.readIntArray(dataInputStream, i2);
                        Currency.scNewCurrenciesDFD = Currency.readIntArray(dataInputStream, i2);
                        Currency.scOldCurrenciesNumericCode = Currency.readIntArray(dataInputStream, i2);
                        Currency.scNewCurrenciesNumericCode = Currency.readIntArray(dataInputStream, i2);
                        int i3 = dataInputStream.readInt();
                        Currency.otherCurrencies = dataInputStream.readUTF();
                        Currency.otherCurrenciesDFD = Currency.readIntArray(dataInputStream, i3);
                        Currency.otherCurrenciesNumericCode = Currency.readIntArray(dataInputStream, i3);
                        if (dataInputStream != null) {
                            if (0 != 0) {
                                try {
                                    dataInputStream.close();
                                } catch (Throwable th2) {
                                    th.addSuppressed(th2);
                                }
                            } else {
                                dataInputStream.close();
                            }
                        }
                        String property2 = System.getProperty("java.util.currency.data");
                        if (property2 == null) {
                            property2 = property + File.separator + "lib" + File.separator + "currency.properties";
                        }
                        try {
                            File file = new File(property2);
                            if (file.exists()) {
                                Properties properties = new Properties();
                                FileReader fileReader = new FileReader(file);
                                Throwable th3 = null;
                                try {
                                    try {
                                        properties.load(fileReader);
                                        if (fileReader != null) {
                                            if (0 != 0) {
                                                try {
                                                    fileReader.close();
                                                } catch (Throwable th4) {
                                                    th3.addSuppressed(th4);
                                                }
                                            } else {
                                                fileReader.close();
                                            }
                                        }
                                        Set<String> setStringPropertyNames = properties.stringPropertyNames();
                                        Pattern patternCompile = Pattern.compile("([A-Z]{3})\\s*,\\s*(\\d{3})\\s*,\\s*(\\d+)\\s*,?\\s*(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2})?");
                                        for (String str : setStringPropertyNames) {
                                            Currency.replaceCurrencyData(patternCompile, str.toUpperCase(Locale.ROOT), properties.getProperty(str).toUpperCase(Locale.ROOT));
                                        }
                                    } finally {
                                    }
                                } catch (Throwable th5) {
                                    th3 = th5;
                                    throw th5;
                                }
                            }
                            return null;
                        } catch (IOException e2) {
                            Currency.info("currency.properties is ignored because of an IOException", e2);
                            return null;
                        }
                    } finally {
                    }
                } catch (IOException e3) {
                    throw new InternalError(e3);
                }
            }
        });
    }

    private Currency(String str, int i2, int i3) {
        this.currencyCode = str;
        this.defaultFractionDigits = i2;
        this.numericCode = i3;
    }

    public static Currency getInstance(String str) {
        return getInstance(str, Integer.MIN_VALUE, 0);
    }

    private static Currency getInstance(String str, int i2, int i3) {
        Currency currency = instances.get(str);
        if (currency != null) {
            return currency;
        }
        if (i2 == Integer.MIN_VALUE) {
            if (str.length() != 3) {
                throw new IllegalArgumentException();
            }
            int mainTableEntry = getMainTableEntry(str.charAt(0), str.charAt(1));
            if ((mainTableEntry & 512) == 0 && mainTableEntry != 127 && str.charAt(2) - 'A' == (mainTableEntry & 31)) {
                i2 = (mainTableEntry & 480) >> 5;
                i3 = (mainTableEntry & NUMERIC_CODE_MASK) >> 10;
            } else {
                if (str.charAt(2) == '-') {
                    throw new IllegalArgumentException();
                }
                int iIndexOf = otherCurrencies.indexOf(str);
                if (iIndexOf == -1) {
                    throw new IllegalArgumentException();
                }
                i2 = otherCurrenciesDFD[iIndexOf / 4];
                i3 = otherCurrenciesNumericCode[iIndexOf / 4];
            }
        }
        Currency currency2 = new Currency(str, i2, i3);
        Currency currencyPutIfAbsent = instances.putIfAbsent(str, currency2);
        return currencyPutIfAbsent != null ? currencyPutIfAbsent : currency2;
    }

    public static Currency getInstance(Locale locale) {
        String country = locale.getCountry();
        if (country == null) {
            throw new NullPointerException();
        }
        if (country.length() != 2) {
            throw new IllegalArgumentException();
        }
        int mainTableEntry = getMainTableEntry(country.charAt(0), country.charAt(1));
        if ((mainTableEntry & 512) == 0 && mainTableEntry != 127) {
            char c2 = (char) ((mainTableEntry & 31) + 65);
            return getInstance(country + c2, (mainTableEntry & 480) >> 5, (mainTableEntry & NUMERIC_CODE_MASK) >> 10);
        }
        if (mainTableEntry == 127) {
            throw new IllegalArgumentException();
        }
        if (mainTableEntry == 512) {
            return null;
        }
        int i2 = (mainTableEntry & 31) - 1;
        if (scCutOverTimes[i2] == Long.MAX_VALUE || System.currentTimeMillis() < scCutOverTimes[i2]) {
            return getInstance(scOldCurrencies[i2], scOldCurrenciesDFD[i2], scOldCurrenciesNumericCode[i2]);
        }
        return getInstance(scNewCurrencies[i2], scNewCurrenciesDFD[i2], scNewCurrenciesNumericCode[i2]);
    }

    public static Set<Currency> getAvailableCurrencies() {
        synchronized (Currency.class) {
            if (available == null) {
                available = new HashSet<>(256);
                for (char c2 = 'A'; c2 <= 'Z'; c2 = (char) (c2 + 1)) {
                    for (char c3 = 'A'; c3 <= 'Z'; c3 = (char) (c3 + 1)) {
                        int mainTableEntry = getMainTableEntry(c2, c3);
                        if ((mainTableEntry & 512) == 0 && mainTableEntry != 127) {
                            char c4 = (char) ((mainTableEntry & 31) + 65);
                            int i2 = (mainTableEntry & 480) >> 5;
                            int i3 = (mainTableEntry & NUMERIC_CODE_MASK) >> 10;
                            StringBuilder sb = new StringBuilder();
                            sb.append(c2);
                            sb.append(c3);
                            sb.append(c4);
                            available.add(getInstance(sb.toString(), i2, i3));
                        }
                    }
                }
                StringTokenizer stringTokenizer = new StringTokenizer(otherCurrencies, LanguageTag.SEP);
                while (stringTokenizer.hasMoreElements()) {
                    available.add(getInstance((String) stringTokenizer.nextElement2()));
                }
            }
        }
        return (Set) available.clone();
    }

    public String getCurrencyCode() {
        return this.currencyCode;
    }

    public String getSymbol() {
        return getSymbol(Locale.getDefault(Locale.Category.DISPLAY));
    }

    public String getSymbol(Locale locale) {
        String str = (String) LocaleServiceProviderPool.getPool(CurrencyNameProvider.class).getLocalizedObject(CurrencyNameGetter.INSTANCE, locale, this.currencyCode, 0);
        if (str != null) {
            return str;
        }
        return this.currencyCode;
    }

    public int getDefaultFractionDigits() {
        return this.defaultFractionDigits;
    }

    public int getNumericCode() {
        return this.numericCode;
    }

    public String getDisplayName() {
        return getDisplayName(Locale.getDefault(Locale.Category.DISPLAY));
    }

    public String getDisplayName(Locale locale) {
        String str = (String) LocaleServiceProviderPool.getPool(CurrencyNameProvider.class).getLocalizedObject(CurrencyNameGetter.INSTANCE, locale, this.currencyCode, 1);
        if (str != null) {
            return str;
        }
        return this.currencyCode;
    }

    public String toString() {
        return this.currencyCode;
    }

    private Object readResolve() {
        return getInstance(this.currencyCode);
    }

    private static int getMainTableEntry(char c2, char c3) {
        if (c2 < 'A' || c2 > 'Z' || c3 < 'A' || c3 > 'Z') {
            throw new IllegalArgumentException();
        }
        return mainTable[((c2 - 'A') * 26) + (c3 - 'A')];
    }

    private static void setMainTableEntry(char c2, char c3, int i2) {
        if (c2 < 'A' || c2 > 'Z' || c3 < 'A' || c3 > 'Z') {
            throw new IllegalArgumentException();
        }
        mainTable[((c2 - 'A') * 26) + (c3 - 'A')] = i2;
    }

    /* loaded from: rt.jar:java/util/Currency$CurrencyNameGetter.class */
    private static class CurrencyNameGetter implements LocaleServiceProviderPool.LocalizedObjectGetter<CurrencyNameProvider, String> {
        private static final CurrencyNameGetter INSTANCE;
        static final /* synthetic */ boolean $assertionsDisabled;

        private CurrencyNameGetter() {
        }

        static {
            $assertionsDisabled = !Currency.class.desiredAssertionStatus();
            INSTANCE = new CurrencyNameGetter();
        }

        @Override // sun.util.locale.provider.LocaleServiceProviderPool.LocalizedObjectGetter
        public String getObject(CurrencyNameProvider currencyNameProvider, Locale locale, String str, Object... objArr) {
            if (!$assertionsDisabled && objArr.length != 1) {
                throw new AssertionError();
            }
            switch (((Integer) objArr[0]).intValue()) {
                case 0:
                    return currencyNameProvider.getSymbol(str, locale);
                case 1:
                    return currencyNameProvider.getDisplayName(str, locale);
                default:
                    if ($assertionsDisabled) {
                        return null;
                    }
                    throw new AssertionError();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int[] readIntArray(DataInputStream dataInputStream, int i2) throws IOException {
        int[] iArr = new int[i2];
        for (int i3 = 0; i3 < i2; i3++) {
            iArr[i3] = dataInputStream.readInt();
        }
        return iArr;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static long[] readLongArray(DataInputStream dataInputStream, int i2) throws IOException {
        long[] jArr = new long[i2];
        for (int i3 = 0; i3 < i2; i3++) {
            jArr[i3] = dataInputStream.readLong();
        }
        return jArr;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String[] readStringArray(DataInputStream dataInputStream, int i2) throws IOException {
        String[] strArr = new String[i2];
        for (int i3 = 0; i3 < i2; i3++) {
            strArr[i3] = dataInputStream.readUTF();
        }
        return strArr;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void replaceCurrencyData(Pattern pattern, String str, String str2) throws NumberFormatException {
        int iCharAt;
        if (str.length() != 2) {
            info("currency.properties entry for " + str + " is ignored because of the invalid country code.", null);
            return;
        }
        Matcher matcher = pattern.matcher(str2);
        if (!matcher.find() || (matcher.group(4) == null && countOccurrences(str2, ',') >= 3)) {
            info("currency.properties entry for " + str + " ignored because the value format is not recognized.", null);
            return;
        }
        try {
            if (matcher.group(4) != null && !isPastCutoverDate(matcher.group(4))) {
                info("currency.properties entry for " + str + " ignored since cutover date has not passed :" + str2, null);
                return;
            }
            String strGroup = matcher.group(1);
            int i2 = Integer.parseInt(matcher.group(2)) << 10;
            int i3 = Integer.parseInt(matcher.group(3));
            if (i3 > 9) {
                info("currency.properties entry for " + str + " ignored since the fraction is more than 9" + CallSiteDescriptor.TOKEN_DELIMITER + str2, null);
                return;
            }
            int i4 = 0;
            while (i4 < scOldCurrencies.length && !scOldCurrencies[i4].equals(strGroup)) {
                i4++;
            }
            if (i4 == scOldCurrencies.length) {
                iCharAt = i2 | (i3 << 5) | (strGroup.charAt(2) - 'A');
            } else {
                iCharAt = i2 | 512 | (i4 + 1);
            }
            setMainTableEntry(str.charAt(0), str.charAt(1), iCharAt);
        } catch (ParseException e2) {
            info("currency.properties entry for " + str + " ignored since exception encountered :" + e2.getMessage(), null);
        }
    }

    private static boolean isPastCutoverDate(String str) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ROOT);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        simpleDateFormat.setLenient(false);
        return System.currentTimeMillis() > simpleDateFormat.parse(str.trim()).getTime();
    }

    private static int countOccurrences(String str, char c2) {
        int i2 = 0;
        for (char c3 : str.toCharArray()) {
            if (c3 == c2) {
                i2++;
            }
        }
        return i2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void info(String str, Throwable th) {
        PlatformLogger logger = PlatformLogger.getLogger("java.util.Currency");
        if (logger.isLoggable(PlatformLogger.Level.INFO)) {
            if (th != null) {
                logger.info(str, th);
            } else {
                logger.info(str);
            }
        }
    }
}
