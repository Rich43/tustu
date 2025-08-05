package sun.util.resources.zh;

import java.util.Locale;
import sun.util.locale.provider.LocaleProviderAdapter;
import sun.util.locale.provider.ResourceBundleBasedAdapter;
import sun.util.resources.OpenListResourceBundle;

/* loaded from: localedata.jar:sun/util/resources/zh/CurrencyNames_zh_SG.class */
public final class CurrencyNames_zh_SG extends OpenListResourceBundle {
    public CurrencyNames_zh_SG() {
        setParent(((ResourceBundleBasedAdapter) LocaleProviderAdapter.forJRE()).getLocaleData().getCurrencyNames(Locale.CHINA));
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // sun.util.resources.OpenListResourceBundle
    protected Object[][] getContents() {
        return new Object[]{new Object[]{"CNY", "CNY"}, new Object[]{"SGD", "S$"}};
    }
}
