package sun.util.resources;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;
import sun.util.locale.provider.JRELocaleProviderAdapter;
import sun.util.locale.provider.LocaleProviderAdapter;

/* loaded from: rt.jar:sun/util/resources/LocaleData.class */
public class LocaleData {
    private final LocaleProviderAdapter.Type type;

    public LocaleData(LocaleProviderAdapter.Type type) {
        this.type = type;
    }

    public ResourceBundle getCalendarData(Locale locale) {
        return getBundle(this.type.getUtilResourcesPackage() + ".CalendarData", locale);
    }

    public OpenListResourceBundle getCurrencyNames(Locale locale) {
        return (OpenListResourceBundle) getBundle(this.type.getUtilResourcesPackage() + ".CurrencyNames", locale);
    }

    public OpenListResourceBundle getLocaleNames(Locale locale) {
        return (OpenListResourceBundle) getBundle(this.type.getUtilResourcesPackage() + ".LocaleNames", locale);
    }

    public TimeZoneNamesBundle getTimeZoneNames(Locale locale) {
        return (TimeZoneNamesBundle) getBundle(this.type.getUtilResourcesPackage() + ".TimeZoneNames", locale);
    }

    public ResourceBundle getBreakIteratorInfo(Locale locale) {
        return getBundle(this.type.getTextResourcesPackage() + ".BreakIteratorInfo", locale);
    }

    public ResourceBundle getCollationData(Locale locale) {
        return getBundle(this.type.getTextResourcesPackage() + ".CollationData", locale);
    }

    public ResourceBundle getDateFormatData(Locale locale) {
        return getBundle(this.type.getTextResourcesPackage() + ".FormatData", locale);
    }

    public void setSupplementary(ParallelListResourceBundle parallelListResourceBundle) {
        if (!parallelListResourceBundle.areParallelContentsComplete()) {
            setSupplementary(this.type.getTextResourcesPackage() + ".JavaTimeSupplementary", parallelListResourceBundle);
        }
    }

    private boolean setSupplementary(String str, ParallelListResourceBundle parallelListResourceBundle) {
        ParallelListResourceBundle parallelListResourceBundle2 = (ParallelListResourceBundle) parallelListResourceBundle.getParent();
        boolean supplementary = false;
        if (parallelListResourceBundle2 != null) {
            supplementary = setSupplementary(str, parallelListResourceBundle2);
        }
        OpenListResourceBundle supplementary2 = getSupplementary(str, parallelListResourceBundle.getLocale());
        parallelListResourceBundle.setParallelContents(supplementary2);
        boolean z2 = supplementary | (supplementary2 != null);
        if (z2) {
            parallelListResourceBundle.resetKeySet();
        }
        return z2;
    }

    public ResourceBundle getNumberFormatData(Locale locale) {
        return getBundle(this.type.getTextResourcesPackage() + ".FormatData", locale);
    }

    public static ResourceBundle getBundle(final String str, final Locale locale) {
        return (ResourceBundle) AccessController.doPrivileged(new PrivilegedAction<ResourceBundle>() { // from class: sun.util.resources.LocaleData.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public ResourceBundle run() {
                return ResourceBundle.getBundle(str, locale, LocaleDataResourceBundleControl.INSTANCE);
            }
        });
    }

    private static OpenListResourceBundle getSupplementary(final String str, final Locale locale) {
        return (OpenListResourceBundle) AccessController.doPrivileged(new PrivilegedAction<OpenListResourceBundle>() { // from class: sun.util.resources.LocaleData.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public OpenListResourceBundle run() {
                OpenListResourceBundle openListResourceBundle = null;
                try {
                    openListResourceBundle = (OpenListResourceBundle) ResourceBundle.getBundle(str, locale, SupplementaryResourceBundleControl.INSTANCE);
                } catch (MissingResourceException e2) {
                }
                return openListResourceBundle;
            }
        });
    }

    /* loaded from: rt.jar:sun/util/resources/LocaleData$LocaleDataResourceBundleControl.class */
    private static class LocaleDataResourceBundleControl extends ResourceBundle.Control {
        private static final LocaleDataResourceBundleControl INSTANCE;
        private static final String DOTCLDR = ".cldr";
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !LocaleData.class.desiredAssertionStatus();
            INSTANCE = new LocaleDataResourceBundleControl();
        }

        private LocaleDataResourceBundleControl() {
        }

        @Override // java.util.ResourceBundle.Control
        public List<Locale> getCandidateLocales(String str, Locale locale) {
            List<Locale> candidateLocales = super.getCandidateLocales(str, locale);
            int iLastIndexOf = str.lastIndexOf(46);
            String strSubstring = iLastIndexOf >= 0 ? str.substring(iLastIndexOf + 1) : str;
            LocaleProviderAdapter.Type type = str.contains(DOTCLDR) ? LocaleProviderAdapter.Type.CLDR : LocaleProviderAdapter.Type.JRE;
            Set<String> languageTagSet = ((JRELocaleProviderAdapter) LocaleProviderAdapter.forType(type)).getLanguageTagSet(strSubstring);
            if (!languageTagSet.isEmpty()) {
                Iterator<Locale> it = candidateLocales.iterator();
                while (it.hasNext()) {
                    if (!LocaleProviderAdapter.isSupportedLocale(it.next(), type, languageTagSet)) {
                        it.remove();
                    }
                }
            }
            if (locale.getLanguage() != "en" && type == LocaleProviderAdapter.Type.CLDR && strSubstring.equals("TimeZoneNames")) {
                candidateLocales.add(candidateLocales.size() - 1, Locale.ENGLISH);
            }
            return candidateLocales;
        }

        @Override // java.util.ResourceBundle.Control
        public Locale getFallbackLocale(String str, Locale locale) {
            if (str == null || locale == null) {
                throw new NullPointerException();
            }
            return null;
        }

        @Override // java.util.ResourceBundle.Control
        public String toBundleName(String str, Locale locale) {
            String str2 = str;
            String language = locale.getLanguage();
            if (language.length() > 0 && (str.startsWith(LocaleProviderAdapter.Type.JRE.getUtilResourcesPackage()) || str.startsWith(LocaleProviderAdapter.Type.JRE.getTextResourcesPackage()))) {
                if (!$assertionsDisabled && LocaleProviderAdapter.Type.JRE.getUtilResourcesPackage().length() != LocaleProviderAdapter.Type.JRE.getTextResourcesPackage().length()) {
                    throw new AssertionError();
                }
                int length = LocaleProviderAdapter.Type.JRE.getUtilResourcesPackage().length();
                if (str.indexOf(DOTCLDR, length) > 0) {
                    length += DOTCLDR.length();
                }
                str2 = str.substring(0, length + 1) + language + str.substring(length);
            }
            return super.toBundleName(str2, locale);
        }
    }

    /* loaded from: rt.jar:sun/util/resources/LocaleData$SupplementaryResourceBundleControl.class */
    private static class SupplementaryResourceBundleControl extends LocaleDataResourceBundleControl {
        private static final SupplementaryResourceBundleControl INSTANCE;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !LocaleData.class.desiredAssertionStatus();
            INSTANCE = new SupplementaryResourceBundleControl();
        }

        private SupplementaryResourceBundleControl() {
            super();
        }

        @Override // sun.util.resources.LocaleData.LocaleDataResourceBundleControl, java.util.ResourceBundle.Control
        public List<Locale> getCandidateLocales(String str, Locale locale) {
            return Arrays.asList(locale);
        }

        @Override // java.util.ResourceBundle.Control
        public long getTimeToLive(String str, Locale locale) {
            if ($assertionsDisabled || str.contains("JavaTimeSupplementary")) {
                return -1L;
            }
            throw new AssertionError();
        }
    }
}
