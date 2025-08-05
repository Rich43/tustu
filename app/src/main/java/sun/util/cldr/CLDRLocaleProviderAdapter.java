package sun.util.cldr;

import java.io.File;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.text.spi.BreakIteratorProvider;
import java.text.spi.CollatorProvider;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.StringTokenizer;
import sun.security.action.GetPropertyAction;
import sun.util.locale.provider.JRELocaleProviderAdapter;
import sun.util.locale.provider.LocaleProviderAdapter;

/* loaded from: rt.jar:sun/util/cldr/CLDRLocaleProviderAdapter.class */
public class CLDRLocaleProviderAdapter extends JRELocaleProviderAdapter {
    private static final String LOCALE_DATA_JAR_NAME = "cldrdata.jar";

    public CLDRLocaleProviderAdapter() {
        String str = File.separator;
        final File file = new File(((String) AccessController.doPrivileged(new GetPropertyAction("java.home"))) + str + "lib" + str + "ext" + str + LOCALE_DATA_JAR_NAME);
        if (!((Boolean) AccessController.doPrivileged(new PrivilegedAction<Boolean>() { // from class: sun.util.cldr.CLDRLocaleProviderAdapter.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Boolean run2() {
                return Boolean.valueOf(file.exists());
            }
        })).booleanValue()) {
            throw new UnsupportedOperationException();
        }
    }

    @Override // sun.util.locale.provider.JRELocaleProviderAdapter, sun.util.locale.provider.LocaleProviderAdapter
    public LocaleProviderAdapter.Type getAdapterType() {
        return LocaleProviderAdapter.Type.CLDR;
    }

    @Override // sun.util.locale.provider.JRELocaleProviderAdapter, sun.util.locale.provider.LocaleProviderAdapter
    public BreakIteratorProvider getBreakIteratorProvider() {
        return null;
    }

    @Override // sun.util.locale.provider.JRELocaleProviderAdapter, sun.util.locale.provider.LocaleProviderAdapter
    public CollatorProvider getCollatorProvider() {
        return null;
    }

    @Override // sun.util.locale.provider.JRELocaleProviderAdapter, sun.util.locale.provider.LocaleProviderAdapter
    public Locale[] getAvailableLocales() {
        Set<String> setCreateLanguageTagSet = createLanguageTagSet("All");
        Locale[] localeArr = new Locale[setCreateLanguageTagSet.size()];
        int i2 = 0;
        Iterator<String> it = setCreateLanguageTagSet.iterator();
        while (it.hasNext()) {
            int i3 = i2;
            i2++;
            localeArr[i3] = Locale.forLanguageTag(it.next());
        }
        return localeArr;
    }

    @Override // sun.util.locale.provider.JRELocaleProviderAdapter
    protected Set<String> createLanguageTagSet(String str) {
        String string = ResourceBundle.getBundle("sun.util.cldr.CLDRLocaleDataMetaInfo", Locale.ROOT).getString(str);
        if (string == null) {
            return Collections.emptySet();
        }
        HashSet hashSet = new HashSet();
        StringTokenizer stringTokenizer = new StringTokenizer(string);
        while (stringTokenizer.hasMoreTokens()) {
            hashSet.add(stringTokenizer.nextToken());
        }
        return hashSet;
    }
}
