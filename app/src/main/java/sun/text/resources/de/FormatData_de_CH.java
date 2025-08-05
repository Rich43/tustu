package sun.text.resources.de;

import javafx.fxml.FXMLLoader;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;
import sun.util.resources.ParallelListResourceBundle;

/* loaded from: localedata.jar:sun/text/resources/de/FormatData_de_CH.class */
public class FormatData_de_CH extends ParallelListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // sun.util.resources.ParallelListResourceBundle
    protected final Object[][] getContents() {
        return new Object[]{new Object[]{"NumberPatterns", new String[]{"#,##0.###;-#,##0.###", "¤ #,##0.00;¤-#,##0.00", "#,##0 %"}}, new Object[]{"NumberElements", new String[]{".", PdfOps.SINGLE_QUOTE_TOKEN, ";", FXMLLoader.RESOURCE_KEY_PREFIX, "0", FXMLLoader.CONTROLLER_METHOD_PREFIX, LanguageTag.SEP, "E", "‰", "∞", "�"}}, new Object[]{"DateTimePatternChars", "GuMtkHmsSEDFwWahKzZ"}};
    }
}
