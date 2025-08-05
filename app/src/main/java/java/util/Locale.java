package java.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.ObjectStreamField;
import java.io.Serializable;
import java.security.AccessController;
import java.text.MessageFormat;
import java.util.spi.LocaleNameProvider;
import sun.security.action.GetPropertyAction;
import sun.util.locale.BaseLocale;
import sun.util.locale.InternalLocaleBuilder;
import sun.util.locale.LanguageTag;
import sun.util.locale.LocaleExtensions;
import sun.util.locale.LocaleMatcher;
import sun.util.locale.LocaleObjectCache;
import sun.util.locale.LocaleSyntaxException;
import sun.util.locale.LocaleUtils;
import sun.util.locale.ParseStatus;
import sun.util.locale.provider.LocaleProviderAdapter;
import sun.util.locale.provider.LocaleResources;
import sun.util.locale.provider.LocaleServiceProviderPool;

/* loaded from: rt.jar:java/util/Locale.class */
public final class Locale implements Cloneable, Serializable {
    private static final Cache LOCALECACHE;
    public static final Locale ENGLISH;
    public static final Locale FRENCH;
    public static final Locale GERMAN;
    public static final Locale ITALIAN;
    public static final Locale JAPANESE;
    public static final Locale KOREAN;
    public static final Locale CHINESE;
    public static final Locale SIMPLIFIED_CHINESE;
    public static final Locale TRADITIONAL_CHINESE;
    public static final Locale FRANCE;
    public static final Locale GERMANY;
    public static final Locale ITALY;
    public static final Locale JAPAN;
    public static final Locale KOREA;
    public static final Locale CHINA;
    public static final Locale PRC;
    public static final Locale TAIWAN;
    public static final Locale UK;
    public static final Locale US;
    public static final Locale CANADA;
    public static final Locale CANADA_FRENCH;
    public static final Locale ROOT;
    public static final char PRIVATE_USE_EXTENSION = 'x';
    public static final char UNICODE_LOCALE_EXTENSION = 'u';
    static final long serialVersionUID = 9149081749638150636L;
    private static final int DISPLAY_LANGUAGE = 0;
    private static final int DISPLAY_COUNTRY = 1;
    private static final int DISPLAY_VARIANT = 2;
    private static final int DISPLAY_SCRIPT = 3;
    private transient BaseLocale baseLocale;
    private transient LocaleExtensions localeExtensions;
    private volatile transient int hashCodeValue;
    private static volatile Locale defaultLocale;
    private static volatile Locale defaultDisplayLocale;
    private static volatile Locale defaultFormatLocale;
    private volatile transient String languageTag;
    private static final ObjectStreamField[] serialPersistentFields;
    private static volatile String[] isoLanguages;
    private static volatile String[] isoCountries;
    static final /* synthetic */ boolean $assertionsDisabled;

    /* loaded from: rt.jar:java/util/Locale$FilteringMode.class */
    public enum FilteringMode {
        AUTOSELECT_FILTERING,
        EXTENDED_FILTERING,
        IGNORE_EXTENDED_RANGES,
        MAP_EXTENDED_RANGES,
        REJECT_EXTENDED_RANGES
    }

    static {
        $assertionsDisabled = !Locale.class.desiredAssertionStatus();
        LOCALECACHE = new Cache();
        ENGLISH = createConstant("en", "");
        FRENCH = createConstant("fr", "");
        GERMAN = createConstant("de", "");
        ITALIAN = createConstant("it", "");
        JAPANESE = createConstant("ja", "");
        KOREAN = createConstant("ko", "");
        CHINESE = createConstant("zh", "");
        SIMPLIFIED_CHINESE = createConstant("zh", "CN");
        TRADITIONAL_CHINESE = createConstant("zh", "TW");
        FRANCE = createConstant("fr", "FR");
        GERMANY = createConstant("de", "DE");
        ITALY = createConstant("it", "IT");
        JAPAN = createConstant("ja", "JP");
        KOREA = createConstant("ko", "KR");
        CHINA = SIMPLIFIED_CHINESE;
        PRC = SIMPLIFIED_CHINESE;
        TAIWAN = TRADITIONAL_CHINESE;
        UK = createConstant("en", "GB");
        US = createConstant("en", "US");
        CANADA = createConstant("en", "CA");
        CANADA_FRENCH = createConstant("fr", "CA");
        ROOT = createConstant("", "");
        defaultLocale = initDefault();
        defaultDisplayLocale = null;
        defaultFormatLocale = null;
        serialPersistentFields = new ObjectStreamField[]{new ObjectStreamField("language", String.class), new ObjectStreamField("country", String.class), new ObjectStreamField("variant", String.class), new ObjectStreamField("hashcode", Integer.TYPE), new ObjectStreamField("script", String.class), new ObjectStreamField("extensions", String.class)};
        isoLanguages = null;
        isoCountries = null;
    }

    private Locale(BaseLocale baseLocale, LocaleExtensions localeExtensions) {
        this.hashCodeValue = 0;
        this.baseLocale = baseLocale;
        this.localeExtensions = localeExtensions;
    }

    public Locale(String str, String str2, String str3) {
        this.hashCodeValue = 0;
        if (str == null || str2 == null || str3 == null) {
            throw new NullPointerException();
        }
        this.baseLocale = BaseLocale.getInstance(convertOldISOCodes(str), "", str2, str3);
        this.localeExtensions = getCompatibilityExtensions(str, "", str2, str3);
    }

    public Locale(String str, String str2) {
        this(str, str2, "");
    }

    public Locale(String str) {
        this(str, "", "");
    }

    private static Locale createConstant(String str, String str2) {
        return getInstance(BaseLocale.createInstance(str, str2), null);
    }

    static Locale getInstance(String str, String str2, String str3) {
        return getInstance(str, "", str2, str3, null);
    }

    static Locale getInstance(String str, String str2, String str3, String str4, LocaleExtensions localeExtensions) {
        if (str == null || str2 == null || str3 == null || str4 == null) {
            throw new NullPointerException();
        }
        if (localeExtensions == null) {
            localeExtensions = getCompatibilityExtensions(str, str2, str3, str4);
        }
        return getInstance(BaseLocale.getInstance(str, str2, str3, str4), localeExtensions);
    }

    static Locale getInstance(BaseLocale baseLocale, LocaleExtensions localeExtensions) {
        return LOCALECACHE.get(new LocaleKey(baseLocale, localeExtensions));
    }

    /* loaded from: rt.jar:java/util/Locale$Cache.class */
    private static class Cache extends LocaleObjectCache<LocaleKey, Locale> {
        private Cache() {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // sun.util.locale.LocaleObjectCache
        public Locale createObject(LocaleKey localeKey) {
            return new Locale(localeKey.base, localeKey.exts);
        }
    }

    /* loaded from: rt.jar:java/util/Locale$LocaleKey.class */
    private static final class LocaleKey {
        private final BaseLocale base;
        private final LocaleExtensions exts;
        private final int hash;

        private LocaleKey(BaseLocale baseLocale, LocaleExtensions localeExtensions) {
            this.base = baseLocale;
            this.exts = localeExtensions;
            int iHashCode = this.base.hashCode();
            this.hash = this.exts != null ? iHashCode ^ this.exts.hashCode() : iHashCode;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof LocaleKey)) {
                return false;
            }
            LocaleKey localeKey = (LocaleKey) obj;
            if (this.hash != localeKey.hash || !this.base.equals(localeKey.base)) {
                return false;
            }
            if (this.exts == null) {
                return localeKey.exts == null;
            }
            return this.exts.equals(localeKey.exts);
        }

        public int hashCode() {
            return this.hash;
        }
    }

    public static Locale getDefault() {
        return defaultLocale;
    }

    public static Locale getDefault(Category category) {
        switch (category) {
            case DISPLAY:
                if (defaultDisplayLocale == null) {
                    synchronized (Locale.class) {
                        if (defaultDisplayLocale == null) {
                            defaultDisplayLocale = initDefault(category);
                        }
                    }
                }
                return defaultDisplayLocale;
            case FORMAT:
                if (defaultFormatLocale == null) {
                    synchronized (Locale.class) {
                        if (defaultFormatLocale == null) {
                            defaultFormatLocale = initDefault(category);
                        }
                    }
                }
                return defaultFormatLocale;
            default:
                if ($assertionsDisabled) {
                    return getDefault();
                }
                throw new AssertionError((Object) "Unknown Category");
        }
    }

    private static Locale initDefault() {
        String str;
        String strSubstring;
        String strSubstring2;
        String str2 = (String) AccessController.doPrivileged(new GetPropertyAction("user.language", "en"));
        String str3 = (String) AccessController.doPrivileged(new GetPropertyAction("user.region"));
        if (str3 != null) {
            int iIndexOf = str3.indexOf(95);
            if (iIndexOf >= 0) {
                strSubstring = str3.substring(0, iIndexOf);
                strSubstring2 = str3.substring(iIndexOf + 1);
            } else {
                strSubstring = str3;
                strSubstring2 = "";
            }
            str = "";
        } else {
            str = (String) AccessController.doPrivileged(new GetPropertyAction("user.script", ""));
            strSubstring = (String) AccessController.doPrivileged(new GetPropertyAction("user.country", ""));
            strSubstring2 = (String) AccessController.doPrivileged(new GetPropertyAction("user.variant", ""));
        }
        return getInstance(str2, str, strSubstring, strSubstring2, null);
    }

    private static Locale initDefault(Category category) {
        return getInstance((String) AccessController.doPrivileged(new GetPropertyAction(category.languageKey, defaultLocale.getLanguage())), (String) AccessController.doPrivileged(new GetPropertyAction(category.scriptKey, defaultLocale.getScript())), (String) AccessController.doPrivileged(new GetPropertyAction(category.countryKey, defaultLocale.getCountry())), (String) AccessController.doPrivileged(new GetPropertyAction(category.variantKey, defaultLocale.getVariant())), null);
    }

    public static synchronized void setDefault(Locale locale) {
        setDefault(Category.DISPLAY, locale);
        setDefault(Category.FORMAT, locale);
        defaultLocale = locale;
    }

    public static synchronized void setDefault(Category category, Locale locale) {
        if (category == null) {
            throw new NullPointerException("Category cannot be NULL");
        }
        if (locale == null) {
            throw new NullPointerException("Can't set default locale to NULL");
        }
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new PropertyPermission("user.language", "write"));
        }
        switch (category) {
            case DISPLAY:
                defaultDisplayLocale = locale;
                return;
            case FORMAT:
                defaultFormatLocale = locale;
                return;
            default:
                if (!$assertionsDisabled) {
                    throw new AssertionError((Object) "Unknown Category");
                }
                return;
        }
    }

    public static Locale[] getAvailableLocales() {
        return LocaleServiceProviderPool.getAllAvailableLocales();
    }

    public static String[] getISOCountries() {
        if (isoCountries == null) {
            isoCountries = getISO2Table("ADANDAEAREAFAFGAGATGAIAIAALALBAMARMANANTAOAGOAQATAARARGASASMATAUTAUAUSAWABWAXALAAZAZEBABIHBBBRBBDBGDBEBELBFBFABGBGRBHBHRBIBDIBJBENBLBLMBMBMUBNBRNBOBOLBQBESBRBRABSBHSBTBTNBVBVTBWBWABYBLRBZBLZCACANCCCCKCDCODCFCAFCGCOGCHCHECICIVCKCOKCLCHLCMCMRCNCHNCOCOLCRCRICUCUBCVCPVCWCUWCXCXRCYCYPCZCZEDEDEUDJDJIDKDNKDMDMADODOMDZDZAECECUEEESTEGEGYEHESHERERIESESPETETHFIFINFJFJIFKFLKFMFSMFOFROFRFRAGAGABGBGBRGDGRDGEGEOGFGUFGGGGYGHGHAGIGIBGLGRLGMGMBGNGINGPGLPGQGNQGRGRCGSSGSGTGTMGUGUMGWGNBGYGUYHKHKGHMHMDHNHNDHRHRVHTHTIHUHUNIDIDNIEIRLILISRIMIMNININDIOIOTIQIRQIRIRNISISLITITAJEJEYJMJAMJOJORJPJPNKEKENKGKGZKHKHMKIKIRKMCOMKNKNAKPPRKKRKORKWKWTKYCYMKZKAZLALAOLBLBNLCLCALILIELKLKALRLBRLSLSOLTLTULULUXLVLVALYLBYMAMARMCMCOMDMDAMEMNEMFMAFMGMDGMHMHLMKMKDMLMLIMMMMRMNMNGMOMACMPMNPMQMTQMRMRTMSMSRMTMLTMUMUSMVMDVMWMWIMXMEXMYMYSMZMOZNANAMNCNCLNENERNFNFKNGNGANINICNLNLDNONORNPNPLNRNRUNUNIUNZNZLOMOMNPAPANPEPERPFPYFPGPNGPHPHLPKPAKPLPOLPMSPMPNPCNPRPRIPSPSEPTPRTPWPLWPYPRYQAQATREREUROROURSSRBRURUSRWRWASASAUSBSLBSCSYCSDSDNSESWESGSGPSHSHNSISVNSJSJMSKSVKSLSLESMSMRSNSENSOSOMSRSURSSSSDSTSTPSVSLVSXSXMSYSYRSZSWZTCTCATDTCDTFATFTGTGOTHTHATJTJKTKTKLTLTLSTMTKMTNTUNTOTONTRTURTTTTOTVTUVTWTWNTZTZAUAUKRUGUGAUMUMIUSUSAUYURYUZUZBVAVATVCVCTVEVENVGVGBVIVIRVNVNMVUVUTWFWLFWSWSMYEYEMYTMYTZAZAFZMZMBZWZWE");
        }
        String[] strArr = new String[isoCountries.length];
        System.arraycopy(isoCountries, 0, strArr, 0, isoCountries.length);
        return strArr;
    }

    public static String[] getISOLanguages() {
        if (isoLanguages == null) {
            isoLanguages = getISO2Table("aaaarababkaeaveafafrakakaamamhanargararaasasmavavaayaymazazebabakbebelbgbulbhbihbibisbmbambnbenbobodbrbrebsboscacatcechechchacocoscrcrecscescuchucvchvcycymdadandedeudvdivdzdzoeeeweelellenengeoepoesspaetesteueusfafasfffulfifinfjfijfofaofrfrafyfrygaglegdglaglglggngrngugujgvglvhahauhehebhihinhohmohrhrvhthathuhunhyhyehzheriainaidindieileigiboiiiiiikipkinindioidoisislititaiuikuiwhebjajpnjiyidjvjavkakatkgkonkikikkjkuakkkazklkalkmkhmknkankokorkrkaukskaskukurkvkomkwcorkykirlalatlbltzlgluglilimlnlinlolaoltlitlulublvlavmgmlgmhmahmimrimkmkdmlmalmnmonmomolmrmarmsmsamtmltmymyananaunbnobndndenenepngndonlnldnnnnononornrnblnvnavnynyaocociojojiomormororiososspapanpipliplpolpspusptporququermrohrnrunroronrurusrwkinsasanscsrdsdsndsesmesgsagsisinskslkslslvsmsmosnsnasosomsqsqisrsrpsssswstsotsusunsvsweswswatatamteteltgtgkththatitirtktuktltgltntsntotontrturtstsotttattwtwitytahuguigukukrururduzuzbvevenvivievovolwawlnwowolxhxhoyiyidyoyorzazhazhzhozuzul");
        }
        String[] strArr = new String[isoLanguages.length];
        System.arraycopy(isoLanguages, 0, strArr, 0, isoLanguages.length);
        return strArr;
    }

    private static String[] getISO2Table(String str) {
        int length = str.length() / 5;
        String[] strArr = new String[length];
        int i2 = 0;
        int i3 = 0;
        while (i2 < length) {
            strArr[i2] = str.substring(i3, i3 + 2);
            i2++;
            i3 += 5;
        }
        return strArr;
    }

    public String getLanguage() {
        return this.baseLocale.getLanguage();
    }

    public String getScript() {
        return this.baseLocale.getScript();
    }

    public String getCountry() {
        return this.baseLocale.getRegion();
    }

    public String getVariant() {
        return this.baseLocale.getVariant();
    }

    public boolean hasExtensions() {
        return this.localeExtensions != null;
    }

    public Locale stripExtensions() {
        return hasExtensions() ? getInstance(this.baseLocale, null) : this;
    }

    public String getExtension(char c2) {
        if (!LocaleExtensions.isValidKey(c2)) {
            throw new IllegalArgumentException("Ill-formed extension key: " + c2);
        }
        if (hasExtensions()) {
            return this.localeExtensions.getExtensionValue(Character.valueOf(c2));
        }
        return null;
    }

    public Set<Character> getExtensionKeys() {
        if (!hasExtensions()) {
            return Collections.emptySet();
        }
        return this.localeExtensions.getKeys();
    }

    public Set<String> getUnicodeLocaleAttributes() {
        if (!hasExtensions()) {
            return Collections.emptySet();
        }
        return this.localeExtensions.getUnicodeLocaleAttributes();
    }

    public String getUnicodeLocaleType(String str) {
        if (!isUnicodeExtensionKey(str)) {
            throw new IllegalArgumentException("Ill-formed Unicode locale key: " + str);
        }
        if (hasExtensions()) {
            return this.localeExtensions.getUnicodeLocaleType(str);
        }
        return null;
    }

    public Set<String> getUnicodeLocaleKeys() {
        if (this.localeExtensions == null) {
            return Collections.emptySet();
        }
        return this.localeExtensions.getUnicodeLocaleKeys();
    }

    BaseLocale getBaseLocale() {
        return this.baseLocale;
    }

    LocaleExtensions getLocaleExtensions() {
        return this.localeExtensions;
    }

    public final String toString() {
        boolean z2 = this.baseLocale.getLanguage().length() != 0;
        boolean z3 = this.baseLocale.getScript().length() != 0;
        boolean z4 = this.baseLocale.getRegion().length() != 0;
        boolean z5 = this.baseLocale.getVariant().length() != 0;
        boolean z6 = (this.localeExtensions == null || this.localeExtensions.getID().length() == 0) ? false : true;
        StringBuilder sb = new StringBuilder(this.baseLocale.getLanguage());
        if (z4 || (z2 && (z5 || z3 || z6))) {
            sb.append('_').append(this.baseLocale.getRegion());
        }
        if (z5 && (z2 || z4)) {
            sb.append('_').append(this.baseLocale.getVariant());
        }
        if (z3 && (z2 || z4)) {
            sb.append("_#").append(this.baseLocale.getScript());
        }
        if (z6 && (z2 || z4)) {
            sb.append('_');
            if (!z3) {
                sb.append('#');
            }
            sb.append(this.localeExtensions.getID());
        }
        return sb.toString();
    }

    public String toLanguageTag() {
        if (this.languageTag != null) {
            return this.languageTag;
        }
        LanguageTag locale = LanguageTag.parseLocale(this.baseLocale, this.localeExtensions);
        StringBuilder sb = new StringBuilder();
        String language = locale.getLanguage();
        if (language.length() > 0) {
            sb.append(LanguageTag.canonicalizeLanguage(language));
        }
        String script = locale.getScript();
        if (script.length() > 0) {
            sb.append(LanguageTag.SEP);
            sb.append(LanguageTag.canonicalizeScript(script));
        }
        String region = locale.getRegion();
        if (region.length() > 0) {
            sb.append(LanguageTag.SEP);
            sb.append(LanguageTag.canonicalizeRegion(region));
        }
        for (String str : locale.getVariants()) {
            sb.append(LanguageTag.SEP);
            sb.append(str);
        }
        for (String str2 : locale.getExtensions()) {
            sb.append(LanguageTag.SEP);
            sb.append(LanguageTag.canonicalizeExtension(str2));
        }
        String privateuse = locale.getPrivateuse();
        if (privateuse.length() > 0) {
            if (sb.length() > 0) {
                sb.append(LanguageTag.SEP);
            }
            sb.append(LanguageTag.PRIVATEUSE).append(LanguageTag.SEP);
            sb.append(privateuse);
        }
        String string = sb.toString();
        synchronized (this) {
            if (this.languageTag == null) {
                this.languageTag = string;
            }
        }
        return this.languageTag;
    }

    public static Locale forLanguageTag(String str) {
        LanguageTag languageTag = LanguageTag.parse(str, null);
        InternalLocaleBuilder internalLocaleBuilder = new InternalLocaleBuilder();
        internalLocaleBuilder.setLanguageTag(languageTag);
        BaseLocale baseLocale = internalLocaleBuilder.getBaseLocale();
        LocaleExtensions localeExtensions = internalLocaleBuilder.getLocaleExtensions();
        if (localeExtensions == null && baseLocale.getVariant().length() > 0) {
            localeExtensions = getCompatibilityExtensions(baseLocale.getLanguage(), baseLocale.getScript(), baseLocale.getRegion(), baseLocale.getVariant());
        }
        return getInstance(baseLocale, localeExtensions);
    }

    public String getISO3Language() throws MissingResourceException {
        String language = this.baseLocale.getLanguage();
        if (language.length() == 3) {
            return language;
        }
        String iSO3Code = getISO3Code(language, "aaaarababkaeaveafafrakakaamamhanargararaasasmavavaayaymazazebabakbebelbgbulbhbihbibisbmbambnbenbobodbrbrebsboscacatcechechchacocoscrcrecscescuchucvchvcycymdadandedeudvdivdzdzoeeeweelellenengeoepoesspaetesteueusfafasfffulfifinfjfijfofaofrfrafyfrygaglegdglaglglggngrngugujgvglvhahauhehebhihinhohmohrhrvhthathuhunhyhyehzheriainaidindieileigiboiiiiiikipkinindioidoisislititaiuikuiwhebjajpnjiyidjvjavkakatkgkonkikikkjkuakkkazklkalkmkhmknkankokorkrkaukskaskukurkvkomkwcorkykirlalatlbltzlgluglilimlnlinlolaoltlitlulublvlavmgmlgmhmahmimrimkmkdmlmalmnmonmomolmrmarmsmsamtmltmymyananaunbnobndndenenepngndonlnldnnnnononornrnblnvnavnynyaocociojojiomormororiososspapanpipliplpolpspusptporququermrohrnrunroronrurusrwkinsasanscsrdsdsndsesmesgsagsisinskslkslslvsmsmosnsnasosomsqsqisrsrpsssswstsotsusunsvsweswswatatamteteltgtgkththatitirtktuktltgltntsntotontrturtstsotttattwtwitytahuguigukukrururduzuzbvevenvivievovolwawlnwowolxhxhoyiyidyoyorzazhazhzhozuzul");
        if (iSO3Code == null) {
            throw new MissingResourceException("Couldn't find 3-letter language code for " + language, "FormatData_" + toString(), "ShortLanguage");
        }
        return iSO3Code;
    }

    public String getISO3Country() throws MissingResourceException {
        String iSO3Code = getISO3Code(this.baseLocale.getRegion(), "ADANDAEAREAFAFGAGATGAIAIAALALBAMARMANANTAOAGOAQATAARARGASASMATAUTAUAUSAWABWAXALAAZAZEBABIHBBBRBBDBGDBEBELBFBFABGBGRBHBHRBIBDIBJBENBLBLMBMBMUBNBRNBOBOLBQBESBRBRABSBHSBTBTNBVBVTBWBWABYBLRBZBLZCACANCCCCKCDCODCFCAFCGCOGCHCHECICIVCKCOKCLCHLCMCMRCNCHNCOCOLCRCRICUCUBCVCPVCWCUWCXCXRCYCYPCZCZEDEDEUDJDJIDKDNKDMDMADODOMDZDZAECECUEEESTEGEGYEHESHERERIESESPETETHFIFINFJFJIFKFLKFMFSMFOFROFRFRAGAGABGBGBRGDGRDGEGEOGFGUFGGGGYGHGHAGIGIBGLGRLGMGMBGNGINGPGLPGQGNQGRGRCGSSGSGTGTMGUGUMGWGNBGYGUYHKHKGHMHMDHNHNDHRHRVHTHTIHUHUNIDIDNIEIRLILISRIMIMNININDIOIOTIQIRQIRIRNISISLITITAJEJEYJMJAMJOJORJPJPNKEKENKGKGZKHKHMKIKIRKMCOMKNKNAKPPRKKRKORKWKWTKYCYMKZKAZLALAOLBLBNLCLCALILIELKLKALRLBRLSLSOLTLTULULUXLVLVALYLBYMAMARMCMCOMDMDAMEMNEMFMAFMGMDGMHMHLMKMKDMLMLIMMMMRMNMNGMOMACMPMNPMQMTQMRMRTMSMSRMTMLTMUMUSMVMDVMWMWIMXMEXMYMYSMZMOZNANAMNCNCLNENERNFNFKNGNGANINICNLNLDNONORNPNPLNRNRUNUNIUNZNZLOMOMNPAPANPEPERPFPYFPGPNGPHPHLPKPAKPLPOLPMSPMPNPCNPRPRIPSPSEPTPRTPWPLWPYPRYQAQATREREUROROURSSRBRURUSRWRWASASAUSBSLBSCSYCSDSDNSESWESGSGPSHSHNSISVNSJSJMSKSVKSLSLESMSMRSNSENSOSOMSRSURSSSSDSTSTPSVSLVSXSXMSYSYRSZSWZTCTCATDTCDTFATFTGTGOTHTHATJTJKTKTKLTLTLSTMTKMTNTUNTOTONTRTURTTTTOTVTUVTWTWNTZTZAUAUKRUGUGAUMUMIUSUSAUYURYUZUZBVAVATVCVCTVEVENVGVGBVIVIRVNVNMVUVUTWFWLFWSWSMYEYEMYTMYTZAZAFZMZMBZWZWE");
        if (iSO3Code == null) {
            throw new MissingResourceException("Couldn't find 3-letter country code for " + this.baseLocale.getRegion(), "FormatData_" + toString(), "ShortCountry");
        }
        return iSO3Code;
    }

    private static String getISO3Code(String str, String str2) {
        int length = str.length();
        if (length == 0) {
            return "";
        }
        int length2 = str2.length();
        int i2 = length2;
        if (length == 2) {
            char cCharAt = str.charAt(0);
            char cCharAt2 = str.charAt(1);
            i2 = 0;
            while (i2 < length2 && (str2.charAt(i2) != cCharAt || str2.charAt(i2 + 1) != cCharAt2)) {
                i2 += 5;
            }
        }
        if (i2 < length2) {
            return str2.substring(i2 + 2, i2 + 5);
        }
        return null;
    }

    public final String getDisplayLanguage() {
        return getDisplayLanguage(getDefault(Category.DISPLAY));
    }

    public String getDisplayLanguage(Locale locale) {
        return getDisplayString(this.baseLocale.getLanguage(), locale, 0);
    }

    public String getDisplayScript() {
        return getDisplayScript(getDefault(Category.DISPLAY));
    }

    public String getDisplayScript(Locale locale) {
        return getDisplayString(this.baseLocale.getScript(), locale, 3);
    }

    public final String getDisplayCountry() {
        return getDisplayCountry(getDefault(Category.DISPLAY));
    }

    public String getDisplayCountry(Locale locale) {
        return getDisplayString(this.baseLocale.getRegion(), locale, 1);
    }

    private String getDisplayString(String str, Locale locale, int i2) {
        if (str.length() == 0) {
            return "";
        }
        if (locale == null) {
            throw new NullPointerException();
        }
        String str2 = (String) LocaleServiceProviderPool.getPool(LocaleNameProvider.class).getLocalizedObject(LocaleNameGetter.INSTANCE, locale, i2 == 2 ? "%%" + str : str, Integer.valueOf(i2), str);
        if (str2 != null) {
            return str2;
        }
        return str;
    }

    public final String getDisplayVariant() {
        return getDisplayVariant(getDefault(Category.DISPLAY));
    }

    public String getDisplayVariant(Locale locale) {
        if (this.baseLocale.getVariant().length() == 0) {
            return "";
        }
        LocaleResources localeResources = LocaleProviderAdapter.forJRE().getLocaleResources(locale);
        return formatList(getDisplayVariantArray(locale), localeResources.getLocaleName("ListPattern"), localeResources.getLocaleName("ListCompositionPattern"));
    }

    public final String getDisplayName() {
        return getDisplayName(getDefault(Category.DISPLAY));
    }

    public String getDisplayName(Locale locale) {
        LocaleResources localeResources = LocaleProviderAdapter.forJRE().getLocaleResources(locale);
        String displayLanguage = getDisplayLanguage(locale);
        String displayScript = getDisplayScript(locale);
        String displayCountry = getDisplayCountry(locale);
        String[] displayVariantArray = getDisplayVariantArray(locale);
        String localeName = localeResources.getLocaleName("DisplayNamePattern");
        String localeName2 = localeResources.getLocaleName("ListPattern");
        String localeName3 = localeResources.getLocaleName("ListCompositionPattern");
        if (displayLanguage.length() == 0 && displayScript.length() == 0 && displayCountry.length() == 0) {
            if (displayVariantArray.length == 0) {
                return "";
            }
            return formatList(displayVariantArray, localeName2, localeName3);
        }
        ArrayList arrayList = new ArrayList(4);
        if (displayLanguage.length() != 0) {
            arrayList.add(displayLanguage);
        }
        if (displayScript.length() != 0) {
            arrayList.add(displayScript);
        }
        if (displayCountry.length() != 0) {
            arrayList.add(displayCountry);
        }
        if (displayVariantArray.length != 0) {
            arrayList.addAll(Arrays.asList(displayVariantArray));
        }
        String str = (String) arrayList.get(0);
        int size = arrayList.size();
        String[] strArr = size > 1 ? (String[]) arrayList.subList(1, size).toArray(new String[size - 1]) : new String[0];
        Object[] objArr = new Object[3];
        objArr[0] = new Integer(strArr.length != 0 ? 2 : 1);
        objArr[1] = str;
        objArr[2] = strArr.length != 0 ? formatList(strArr, localeName2, localeName3) : null;
        if (localeName != null) {
            return new MessageFormat(localeName).format(objArr);
        }
        StringBuilder sb = new StringBuilder();
        sb.append((String) objArr[1]);
        if (objArr.length > 2) {
            sb.append(" (");
            sb.append((String) objArr[2]);
            sb.append(')');
        }
        return sb.toString();
    }

    public Object clone() {
        try {
            return (Locale) super.clone();
        } catch (CloneNotSupportedException e2) {
            throw new InternalError(e2);
        }
    }

    public int hashCode() {
        int iHashCode = this.hashCodeValue;
        if (iHashCode == 0) {
            iHashCode = this.baseLocale.hashCode();
            if (this.localeExtensions != null) {
                iHashCode ^= this.localeExtensions.hashCode();
            }
            this.hashCodeValue = iHashCode;
        }
        return iHashCode;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Locale)) {
            return false;
        }
        if (!this.baseLocale.equals(((Locale) obj).baseLocale)) {
            return false;
        }
        if (this.localeExtensions == null) {
            return ((Locale) obj).localeExtensions == null;
        }
        return this.localeExtensions.equals(((Locale) obj).localeExtensions);
    }

    private String[] getDisplayVariantArray(Locale locale) {
        StringTokenizer stringTokenizer = new StringTokenizer(this.baseLocale.getVariant(), "_");
        String[] strArr = new String[stringTokenizer.countTokens()];
        for (int i2 = 0; i2 < strArr.length; i2++) {
            strArr[i2] = getDisplayString(stringTokenizer.nextToken(), locale, 2);
        }
        return strArr;
    }

    private static String formatList(String[] strArr, String str, String str2) {
        if (str == null || str2 == null) {
            StringBuilder sb = new StringBuilder();
            for (int i2 = 0; i2 < strArr.length; i2++) {
                if (i2 > 0) {
                    sb.append(',');
                }
                sb.append(strArr[i2]);
            }
            return sb.toString();
        }
        if (strArr.length > 3) {
            strArr = composeList(new MessageFormat(str2), strArr);
        }
        Object[] objArr = new Object[strArr.length + 1];
        System.arraycopy(strArr, 0, objArr, 1, strArr.length);
        objArr[0] = new Integer(strArr.length);
        return new MessageFormat(str).format(objArr);
    }

    private static String[] composeList(MessageFormat messageFormat, String[] strArr) {
        if (strArr.length <= 3) {
            return strArr;
        }
        String str = messageFormat.format(new String[]{strArr[0], strArr[1]});
        String[] strArr2 = new String[strArr.length - 1];
        System.arraycopy(strArr, 2, strArr2, 1, strArr2.length - 1);
        strArr2[0] = str;
        return composeList(messageFormat, strArr2);
    }

    private static boolean isUnicodeExtensionKey(String str) {
        return str.length() == 2 && LocaleUtils.isAlphaNumericString(str);
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        ObjectOutputStream.PutField putFieldPutFields = objectOutputStream.putFields();
        putFieldPutFields.put("language", this.baseLocale.getLanguage());
        putFieldPutFields.put("script", this.baseLocale.getScript());
        putFieldPutFields.put("country", this.baseLocale.getRegion());
        putFieldPutFields.put("variant", this.baseLocale.getVariant());
        putFieldPutFields.put("extensions", this.localeExtensions == null ? "" : this.localeExtensions.getID());
        putFieldPutFields.put("hashcode", -1);
        objectOutputStream.writeFields();
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        ObjectInputStream.GetField fields = objectInputStream.readFields();
        String str = (String) fields.get("language", "");
        String str2 = (String) fields.get("script", "");
        String str3 = (String) fields.get("country", "");
        String str4 = (String) fields.get("variant", "");
        String str5 = (String) fields.get("extensions", "");
        this.baseLocale = BaseLocale.getInstance(convertOldISOCodes(str), str2, str3, str4);
        if (str5.length() > 0) {
            try {
                InternalLocaleBuilder internalLocaleBuilder = new InternalLocaleBuilder();
                internalLocaleBuilder.setExtensions(str5);
                this.localeExtensions = internalLocaleBuilder.getLocaleExtensions();
                return;
            } catch (LocaleSyntaxException e2) {
                throw new IllformedLocaleException(e2.getMessage());
            }
        }
        this.localeExtensions = null;
    }

    private Object readResolve() throws ObjectStreamException {
        return getInstance(this.baseLocale.getLanguage(), this.baseLocale.getScript(), this.baseLocale.getRegion(), this.baseLocale.getVariant(), this.localeExtensions);
    }

    private static String convertOldISOCodes(String str) {
        String strIntern = LocaleUtils.toLowerString(str).intern();
        if (strIntern == "he") {
            return "iw";
        }
        if (strIntern == "yi") {
            return "ji";
        }
        if (strIntern == "id") {
            return "in";
        }
        return strIntern;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static LocaleExtensions getCompatibilityExtensions(String str, String str2, String str3, String str4) {
        LocaleExtensions localeExtensions = null;
        if (LocaleUtils.caseIgnoreMatch(str, "ja") && str2.length() == 0 && LocaleUtils.caseIgnoreMatch(str3, "jp") && "JP".equals(str4)) {
            localeExtensions = LocaleExtensions.CALENDAR_JAPANESE;
        } else if (LocaleUtils.caseIgnoreMatch(str, "th") && str2.length() == 0 && LocaleUtils.caseIgnoreMatch(str3, "th") && "TH".equals(str4)) {
            localeExtensions = LocaleExtensions.NUMBER_THAI;
        }
        return localeExtensions;
    }

    /* loaded from: rt.jar:java/util/Locale$LocaleNameGetter.class */
    private static class LocaleNameGetter implements LocaleServiceProviderPool.LocalizedObjectGetter<LocaleNameProvider, String> {
        private static final LocaleNameGetter INSTANCE;
        static final /* synthetic */ boolean $assertionsDisabled;

        private LocaleNameGetter() {
        }

        static {
            $assertionsDisabled = !Locale.class.desiredAssertionStatus();
            INSTANCE = new LocaleNameGetter();
        }

        @Override // sun.util.locale.provider.LocaleServiceProviderPool.LocalizedObjectGetter
        public String getObject(LocaleNameProvider localeNameProvider, Locale locale, String str, Object... objArr) {
            if (!$assertionsDisabled && objArr.length != 2) {
                throw new AssertionError();
            }
            int iIntValue = ((Integer) objArr[0]).intValue();
            String str2 = (String) objArr[1];
            switch (iIntValue) {
                case 0:
                    return localeNameProvider.getDisplayLanguage(str2, locale);
                case 1:
                    return localeNameProvider.getDisplayCountry(str2, locale);
                case 2:
                    return localeNameProvider.getDisplayVariant(str2, locale);
                case 3:
                    return localeNameProvider.getDisplayScript(str2, locale);
                default:
                    if ($assertionsDisabled) {
                        return null;
                    }
                    throw new AssertionError();
            }
        }
    }

    /* loaded from: rt.jar:java/util/Locale$Category.class */
    public enum Category {
        DISPLAY("user.language.display", "user.script.display", "user.country.display", "user.variant.display"),
        FORMAT("user.language.format", "user.script.format", "user.country.format", "user.variant.format");

        final String languageKey;
        final String scriptKey;
        final String countryKey;
        final String variantKey;

        Category(String str, String str2, String str3, String str4) {
            this.languageKey = str;
            this.scriptKey = str2;
            this.countryKey = str3;
            this.variantKey = str4;
        }
    }

    /* loaded from: rt.jar:java/util/Locale$Builder.class */
    public static final class Builder {
        private final InternalLocaleBuilder localeBuilder = new InternalLocaleBuilder();

        public Builder setLocale(Locale locale) {
            try {
                this.localeBuilder.setLocale(locale.baseLocale, locale.localeExtensions);
                return this;
            } catch (LocaleSyntaxException e2) {
                throw new IllformedLocaleException(e2.getMessage(), e2.getErrorIndex());
            }
        }

        public Builder setLanguageTag(String str) {
            ParseStatus parseStatus = new ParseStatus();
            LanguageTag languageTag = LanguageTag.parse(str, parseStatus);
            if (parseStatus.isError()) {
                throw new IllformedLocaleException(parseStatus.getErrorMessage(), parseStatus.getErrorIndex());
            }
            this.localeBuilder.setLanguageTag(languageTag);
            return this;
        }

        public Builder setLanguage(String str) {
            try {
                this.localeBuilder.setLanguage(str);
                return this;
            } catch (LocaleSyntaxException e2) {
                throw new IllformedLocaleException(e2.getMessage(), e2.getErrorIndex());
            }
        }

        public Builder setScript(String str) {
            try {
                this.localeBuilder.setScript(str);
                return this;
            } catch (LocaleSyntaxException e2) {
                throw new IllformedLocaleException(e2.getMessage(), e2.getErrorIndex());
            }
        }

        public Builder setRegion(String str) {
            try {
                this.localeBuilder.setRegion(str);
                return this;
            } catch (LocaleSyntaxException e2) {
                throw new IllformedLocaleException(e2.getMessage(), e2.getErrorIndex());
            }
        }

        public Builder setVariant(String str) {
            try {
                this.localeBuilder.setVariant(str);
                return this;
            } catch (LocaleSyntaxException e2) {
                throw new IllformedLocaleException(e2.getMessage(), e2.getErrorIndex());
            }
        }

        public Builder setExtension(char c2, String str) {
            try {
                this.localeBuilder.setExtension(c2, str);
                return this;
            } catch (LocaleSyntaxException e2) {
                throw new IllformedLocaleException(e2.getMessage(), e2.getErrorIndex());
            }
        }

        public Builder setUnicodeLocaleKeyword(String str, String str2) {
            try {
                this.localeBuilder.setUnicodeLocaleKeyword(str, str2);
                return this;
            } catch (LocaleSyntaxException e2) {
                throw new IllformedLocaleException(e2.getMessage(), e2.getErrorIndex());
            }
        }

        public Builder addUnicodeLocaleAttribute(String str) {
            try {
                this.localeBuilder.addUnicodeLocaleAttribute(str);
                return this;
            } catch (LocaleSyntaxException e2) {
                throw new IllformedLocaleException(e2.getMessage(), e2.getErrorIndex());
            }
        }

        public Builder removeUnicodeLocaleAttribute(String str) {
            try {
                this.localeBuilder.removeUnicodeLocaleAttribute(str);
                return this;
            } catch (LocaleSyntaxException e2) {
                throw new IllformedLocaleException(e2.getMessage(), e2.getErrorIndex());
            }
        }

        public Builder clear() {
            this.localeBuilder.clear();
            return this;
        }

        public Builder clearExtensions() {
            this.localeBuilder.clearExtensions();
            return this;
        }

        public Locale build() {
            BaseLocale baseLocale = this.localeBuilder.getBaseLocale();
            LocaleExtensions localeExtensions = this.localeBuilder.getLocaleExtensions();
            if (localeExtensions == null && baseLocale.getVariant().length() > 0) {
                localeExtensions = Locale.getCompatibilityExtensions(baseLocale.getLanguage(), baseLocale.getScript(), baseLocale.getRegion(), baseLocale.getVariant());
            }
            return Locale.getInstance(baseLocale, localeExtensions);
        }
    }

    /* loaded from: rt.jar:java/util/Locale$LanguageRange.class */
    public static final class LanguageRange {
        public static final double MAX_WEIGHT = 1.0d;
        public static final double MIN_WEIGHT = 0.0d;
        private final String range;
        private final double weight;
        private volatile int hash;

        public LanguageRange(String str) {
            this(str, 1.0d);
        }

        public LanguageRange(String str, double d2) {
            this.hash = 0;
            if (str == null) {
                throw new NullPointerException();
            }
            if (d2 < 0.0d || d2 > 1.0d) {
                throw new IllegalArgumentException("weight=" + d2);
            }
            String lowerCase = str.toLowerCase();
            boolean z2 = false;
            String[] strArrSplit = lowerCase.split(LanguageTag.SEP);
            if (isSubtagIllFormed(strArrSplit[0], true) || lowerCase.endsWith(LanguageTag.SEP)) {
                z2 = true;
            } else {
                int i2 = 1;
                while (true) {
                    if (i2 >= strArrSplit.length) {
                        break;
                    }
                    if (!isSubtagIllFormed(strArrSplit[i2], false)) {
                        i2++;
                    } else {
                        z2 = true;
                        break;
                    }
                }
            }
            if (z2) {
                throw new IllegalArgumentException("range=" + lowerCase);
            }
            this.range = lowerCase;
            this.weight = d2;
        }

        private static boolean isSubtagIllFormed(String str, boolean z2) {
            if (str.equals("") || str.length() > 8) {
                return true;
            }
            if (str.equals("*")) {
                return false;
            }
            char[] charArray = str.toCharArray();
            if (z2) {
                for (char c2 : charArray) {
                    if (c2 < 'a' || c2 > 'z') {
                        return true;
                    }
                }
                return false;
            }
            for (char c3 : charArray) {
                if (c3 < '0') {
                    return true;
                }
                if ((c3 > '9' && c3 < 'a') || c3 > 'z') {
                    return true;
                }
            }
            return false;
        }

        public String getRange() {
            return this.range;
        }

        public double getWeight() {
            return this.weight;
        }

        public static List<LanguageRange> parse(String str) {
            return LocaleMatcher.parse(str);
        }

        public static List<LanguageRange> parse(String str, Map<String, List<String>> map) {
            return mapEquivalents(parse(str), map);
        }

        public static List<LanguageRange> mapEquivalents(List<LanguageRange> list, Map<String, List<String>> map) {
            return LocaleMatcher.mapEquivalents(list, map);
        }

        public int hashCode() {
            if (this.hash == 0) {
                int iHashCode = (37 * 17) + this.range.hashCode();
                long jDoubleToLongBits = Double.doubleToLongBits(this.weight);
                this.hash = (37 * iHashCode) + ((int) (jDoubleToLongBits ^ (jDoubleToLongBits >>> 32)));
            }
            return this.hash;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof LanguageRange)) {
                return false;
            }
            LanguageRange languageRange = (LanguageRange) obj;
            return this.hash == languageRange.hash && this.range.equals(languageRange.range) && this.weight == languageRange.weight;
        }
    }

    public static List<Locale> filter(List<LanguageRange> list, Collection<Locale> collection, FilteringMode filteringMode) {
        return LocaleMatcher.filter(list, collection, filteringMode);
    }

    public static List<Locale> filter(List<LanguageRange> list, Collection<Locale> collection) {
        return filter(list, collection, FilteringMode.AUTOSELECT_FILTERING);
    }

    public static List<String> filterTags(List<LanguageRange> list, Collection<String> collection, FilteringMode filteringMode) {
        return LocaleMatcher.filterTags(list, collection, filteringMode);
    }

    public static List<String> filterTags(List<LanguageRange> list, Collection<String> collection) {
        return filterTags(list, collection, FilteringMode.AUTOSELECT_FILTERING);
    }

    public static Locale lookup(List<LanguageRange> list, Collection<Locale> collection) {
        return LocaleMatcher.lookup(list, collection);
    }

    public static String lookupTag(List<LanguageRange> list, Collection<String> collection) {
        return LocaleMatcher.lookupTag(list, collection);
    }
}
