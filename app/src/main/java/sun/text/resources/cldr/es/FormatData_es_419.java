package sun.text.resources.cldr.es;

import java.util.ListResourceBundle;
import javafx.fxml.FXMLLoader;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;

/* loaded from: cldrdata.jar:sun/text/resources/cldr/es/FormatData_es_419.class */
public class FormatData_es_419 extends ListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // java.util.ListResourceBundle
    protected final Object[][] getContents() {
        return new Object[]{new Object[]{"DayNarrows", new String[]{PdfOps.D_TOKEN, "L", PdfOps.M_TOKEN, PdfOps.M_TOKEN, "J", "V", PdfOps.S_TOKEN}}, new Object[]{"QuarterNarrows", new String[]{"1", "2", "3", "4"}}, new Object[]{"DefaultNumberingSystem", "latn"}, new Object[]{"latn.NumberElements", new String[]{".", ",", ";", FXMLLoader.RESOURCE_KEY_PREFIX, "0", FXMLLoader.CONTROLLER_METHOD_PREFIX, LanguageTag.SEP, "E", "‰", "∞", "NaN"}}, new Object[]{"NumberPatterns", new String[]{"#,##0.###", "¤#,##0.00", "#,##0%"}}};
    }
}
