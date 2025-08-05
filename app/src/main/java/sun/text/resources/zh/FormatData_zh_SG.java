package sun.text.resources.zh;

import javafx.fxml.FXMLLoader;
import sun.util.locale.LanguageTag;
import sun.util.resources.ParallelListResourceBundle;

/* loaded from: localedata.jar:sun/text/resources/zh/FormatData_zh_SG.class */
public class FormatData_zh_SG extends ParallelListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // sun.util.resources.ParallelListResourceBundle
    protected final Object[][] getContents() {
        return new Object[]{new Object[]{"DayAbbreviations", new String[]{"周日", "周一", "周二", "周三", "周四", "周五", "周六"}}, new Object[]{"NumberPatterns", new String[]{"#,##0.###", "¤#,##0.00", "#,##0%"}}, new Object[]{"NumberElements", new String[]{".", ",", ";", FXMLLoader.RESOURCE_KEY_PREFIX, "0", FXMLLoader.CONTROLLER_METHOD_PREFIX, LanguageTag.SEP, "E", "‰", "∞", "NaN"}}, new Object[]{"TimePatterns", new String[]{"a hh:mm:ss", "a hh:mm:ss", "a hh:mm", "a hh:mm"}}, new Object[]{"DatePatterns", new String[]{"dd MMMM yyyy", "dd MMM yyyy", "dd-MMM-yy", "dd/MM/yy"}}, new Object[]{"DateTimePatterns", new String[]{"{1} {0}"}}, new Object[]{"DateTimePatternChars", "GanjkHmsSEDFwWxhKzZ"}};
    }
}
