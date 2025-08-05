package sun.text.resources.en;

import javafx.fxml.FXMLLoader;
import sun.util.locale.LanguageTag;
import sun.util.resources.ParallelListResourceBundle;

/* loaded from: rt.jar:sun/text/resources/en/FormatData_en_SG.class */
public class FormatData_en_SG extends ParallelListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // sun.util.resources.ParallelListResourceBundle
    protected final Object[][] getContents() {
        return new Object[]{new Object[]{"NumberPatterns", new String[]{"#,##0.###", "¤#,##0.00", "#,##0%"}}, new Object[]{"NumberElements", new String[]{".", ",", ";", FXMLLoader.RESOURCE_KEY_PREFIX, "0", FXMLLoader.CONTROLLER_METHOD_PREFIX, LanguageTag.SEP, "E", "‰", "∞", "NaN"}}, new Object[]{"DatePatterns", new String[]{"EEEE, d MMMM, yyyy", "d MMMM, yyyy", "d MMM, yyyy", "d/M/yy"}}};
    }
}
