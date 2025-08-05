package sun.text.resources.zh;

import java.util.ListResourceBundle;
import java.util.Locale;
import sun.util.locale.provider.LocaleProviderAdapter;
import sun.util.locale.provider.ResourceBundleBasedAdapter;

/* loaded from: localedata.jar:sun/text/resources/zh/CollationData_zh_HK.class */
public class CollationData_zh_HK extends ListResourceBundle {
    public CollationData_zh_HK() {
        setParent(((ResourceBundleBasedAdapter) LocaleProviderAdapter.forJRE()).getLocaleData().getCollationData(Locale.TAIWAN));
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // java.util.ListResourceBundle
    protected final Object[][] getContents() {
        return new Object[0];
    }
}
