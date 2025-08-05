package sun.util.resources.zh;

import java.util.Locale;
import sun.util.locale.provider.LocaleProviderAdapter;
import sun.util.locale.provider.ResourceBundleBasedAdapter;
import sun.util.resources.OpenListResourceBundle;

/* loaded from: localedata.jar:sun/util/resources/zh/CurrencyNames_zh_HK.class */
public final class CurrencyNames_zh_HK extends OpenListResourceBundle {
    public CurrencyNames_zh_HK() {
        setParent(((ResourceBundleBasedAdapter) LocaleProviderAdapter.forJRE()).getLocaleData().getCurrencyNames(Locale.TAIWAN));
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // sun.util.resources.OpenListResourceBundle
    protected Object[][] getContents() {
        return new Object[]{new Object[]{"HKD", "HK$"}, new Object[]{"TWD", "TWD"}};
    }
}
