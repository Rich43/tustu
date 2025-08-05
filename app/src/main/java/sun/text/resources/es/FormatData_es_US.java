package sun.text.resources.es;

import javafx.fxml.FXMLLoader;
import sun.util.locale.LanguageTag;
import sun.util.resources.ParallelListResourceBundle;

/* loaded from: localedata.jar:sun/text/resources/es/FormatData_es_US.class */
public class FormatData_es_US extends ParallelListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // sun.util.resources.ParallelListResourceBundle
    protected final Object[][] getContents() {
        return new Object[]{new Object[]{"AmPmMarkers", new String[]{"a.m.", "p.m."}}, new Object[]{"Eras", new String[]{"a.C.", "d.C."}}, new Object[]{"NumberPatterns", new String[]{"#,##0.###", "¤#,##0.00;(¤#,##0.00)", "#,##0%"}}, new Object[]{"NumberElements", new String[]{".", ",", ";", FXMLLoader.RESOURCE_KEY_PREFIX, "0", FXMLLoader.CONTROLLER_METHOD_PREFIX, LanguageTag.SEP, "E", "‰", "∞", "NaN"}}, new Object[]{"TimePatterns", new String[]{"h:mm:ss a z", "h:mm:ss a z", "h:mm:ss a", "h:mm a"}}, new Object[]{"DatePatterns", new String[]{"EEEE d' de 'MMMM' de 'yyyy", "d' de 'MMMM' de 'yyyy", "MMM d, yyyy", "M/d/yy"}}, new Object[]{"DateTimePatterns", new String[]{"{1} {0}"}}, new Object[]{"DateTimePatternChars", "GuMtkHmsSEDFwWahKzZ"}};
    }
}
