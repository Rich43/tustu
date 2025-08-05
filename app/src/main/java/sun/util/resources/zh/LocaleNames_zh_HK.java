package sun.util.resources.zh;

import java.util.Locale;
import sun.util.locale.provider.LocaleProviderAdapter;
import sun.util.locale.provider.ResourceBundleBasedAdapter;
import sun.util.resources.OpenListResourceBundle;

/* loaded from: localedata.jar:sun/util/resources/zh/LocaleNames_zh_HK.class */
public final class LocaleNames_zh_HK extends OpenListResourceBundle {
    public LocaleNames_zh_HK() {
        setParent(((ResourceBundleBasedAdapter) LocaleProviderAdapter.forJRE()).getLocaleData().getLocaleNames(Locale.TAIWAN));
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // sun.util.resources.OpenListResourceBundle
    protected Object[][] getContents() {
        return new Object[0];
    }
}
