package sun.text.resources.cldr.en;

import java.util.ListResourceBundle;
import javafx.fxml.FXMLLoader;
import sun.util.locale.LanguageTag;

/* loaded from: cldrdata.jar:sun/text/resources/cldr/en/FormatData_en_BE.class */
public class FormatData_en_BE extends ListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // java.util.ListResourceBundle
    protected final Object[][] getContents() {
        return new Object[]{new Object[]{"TimePatterns", new String[]{"HH 'h' mm 'min' ss 's' zzzz", "HH:mm:ss z", "HH:mm:ss", "HH:mm"}}, new Object[]{"DatePatterns", new String[]{"EEEE d MMMM y", "d MMM y", "dd MMM y", "dd/MM/yy"}}, new Object[]{"DefaultNumberingSystem", "latn"}, new Object[]{"latn.NumberElements", new String[]{",", ".", ";", FXMLLoader.RESOURCE_KEY_PREFIX, "0", FXMLLoader.CONTROLLER_METHOD_PREFIX, LanguageTag.SEP, "E", "‰", "∞", "NaN"}}, new Object[]{"NumberPatterns", new String[]{"#,##0.###", "#,##0.00 ¤", "#,##0%"}}};
    }
}
