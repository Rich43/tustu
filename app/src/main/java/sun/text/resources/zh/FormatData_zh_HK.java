package sun.text.resources.zh;

import java.util.Locale;
import sun.util.locale.provider.LocaleProviderAdapter;
import sun.util.locale.provider.ResourceBundleBasedAdapter;
import sun.util.resources.ParallelListResourceBundle;

/* loaded from: localedata.jar:sun/text/resources/zh/FormatData_zh_HK.class */
public class FormatData_zh_HK extends ParallelListResourceBundle {
    public FormatData_zh_HK() {
        setParent(((ResourceBundleBasedAdapter) LocaleProviderAdapter.forJRE()).getLocaleData().getDateFormatData(Locale.TAIWAN));
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // sun.util.resources.ParallelListResourceBundle
    protected final Object[][] getContents() {
        return new Object[]{new Object[]{"MonthAbbreviations", new String[]{"1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月", ""}}, new Object[]{"DayAbbreviations", new String[]{"日", "一", "二", "三", "四", "五", "六"}}, new Object[]{"NumberPatterns", new String[]{"#,##0.###;-#,##0.###", "¤#,##0.00;(¤#,##0.00)", "#,##0%"}}, new Object[]{"TimePatterns", new String[]{"ahh'時'mm'分'ss'秒' z", "ahh'時'mm'分'ss'秒'", "ahh:mm:ss", "ah:mm"}}, new Object[]{"DatePatterns", new String[]{"yyyy'年'MM'月'dd'日' EEEE", "yyyy'年'MM'月'dd'日' EEEE", "yyyy'年'M'月'd'日'", "yy'年'M'月'd'日'"}}, new Object[]{"DateTimePatterns", new String[]{"{1} {0}"}}, new Object[]{"DateTimePatternChars", "GanjkHmsSEDFwWxhKzZ"}};
    }
}
