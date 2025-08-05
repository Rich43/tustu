package sun.text.resources.cldr.dz;

import java.util.ListResourceBundle;
import javafx.fxml.FXMLLoader;
import sun.util.locale.LanguageTag;

/* loaded from: cldrdata.jar:sun/text/resources/cldr/dz/FormatData_dz.class */
public class FormatData_dz extends ListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // java.util.ListResourceBundle
    protected final Object[][] getContents() {
        return new Object[]{new Object[]{"DatePatterns", new String[]{"EEEE, y MMMM dd", "y MMMM d", "y MMM d", "yyyy-MM-dd"}}, new Object[]{"DefaultNumberingSystem", "latn"}, new Object[]{"latn.NumberElements", new String[]{".", ",", ";", FXMLLoader.RESOURCE_KEY_PREFIX, "0", FXMLLoader.CONTROLLER_METHOD_PREFIX, LanguageTag.SEP, "E", "‰", "∞", "NaN"}}, new Object[]{"tibt.NumberElements", new String[]{".", ",", ";", FXMLLoader.RESOURCE_KEY_PREFIX, "༠", FXMLLoader.CONTROLLER_METHOD_PREFIX, LanguageTag.SEP, "E", "‰", "∞", "NaN"}}};
    }
}
