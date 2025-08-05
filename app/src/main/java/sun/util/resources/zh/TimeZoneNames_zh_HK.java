package sun.util.resources.zh;

import java.util.Locale;
import sun.util.locale.provider.LocaleProviderAdapter;
import sun.util.locale.provider.ResourceBundleBasedAdapter;
import sun.util.resources.TimeZoneNamesBundle;

/* loaded from: localedata.jar:sun/util/resources/zh/TimeZoneNames_zh_HK.class */
public final class TimeZoneNames_zh_HK extends TimeZoneNamesBundle {
    public TimeZoneNames_zh_HK() {
        setParent(((ResourceBundleBasedAdapter) LocaleProviderAdapter.forJRE()).getLocaleData().getTimeZoneNames(Locale.TAIWAN));
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // sun.util.resources.TimeZoneNamesBundle, sun.util.resources.OpenListResourceBundle
    protected Object[][] getContents() {
        return new Object[0];
    }
}
