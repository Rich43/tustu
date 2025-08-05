package sun.text.resources.cldr.uz;

import java.util.ListResourceBundle;
import javafx.fxml.FXMLLoader;
import sun.util.locale.LanguageTag;

/* loaded from: cldrdata.jar:sun/text/resources/cldr/uz/FormatData_uz_Arab.class */
public class FormatData_uz_Arab extends ListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // java.util.ListResourceBundle
    protected final Object[][] getContents() {
        return new Object[]{new Object[]{"DefaultNumberingSystem", "arabext"}, new Object[]{"arabext.NumberElements", new String[]{"٫", "٬", "؛", "٪", "۰", FXMLLoader.CONTROLLER_METHOD_PREFIX, LanguageTag.SEP, "×۱۰^", "؉", "∞", "NaN"}}, new Object[]{"latn.NumberElements", new String[]{",", ".", ";", FXMLLoader.RESOURCE_KEY_PREFIX, "0", FXMLLoader.CONTROLLER_METHOD_PREFIX, "−", "E", "‰", "∞", "NaN"}}};
    }
}
