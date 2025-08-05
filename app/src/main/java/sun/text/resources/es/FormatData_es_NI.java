package sun.text.resources.es;

import javafx.fxml.FXMLLoader;
import sun.util.locale.LanguageTag;
import sun.util.resources.ParallelListResourceBundle;

/* loaded from: localedata.jar:sun/text/resources/es/FormatData_es_NI.class */
public class FormatData_es_NI extends ParallelListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // sun.util.resources.ParallelListResourceBundle
    protected final Object[][] getContents() {
        return new Object[]{new Object[]{"NumberElements", new String[]{".", ",", ";", FXMLLoader.RESOURCE_KEY_PREFIX, "0", FXMLLoader.CONTROLLER_METHOD_PREFIX, LanguageTag.SEP, "E", "‰", "∞", "�"}}, new Object[]{"TimePatterns", new String[]{"hh:mm:ss a z", "hh:mm:ss a z", "hh:mm:ss a", "hh:mm a"}}, new Object[]{"DatePatterns", new String[]{"EEEE dd' de 'MMMM' de 'yyyy", "dd' de 'MMMM' de 'yyyy", "MM-dd-yyyy", "MM-dd-yy"}}, new Object[]{"DateTimePatterns", new String[]{"{1} {0}"}}};
    }
}
