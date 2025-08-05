package sun.text.resources.fr;

import javafx.fxml.FXMLLoader;
import sun.util.locale.LanguageTag;
import sun.util.resources.ParallelListResourceBundle;

/* loaded from: localedata.jar:sun/text/resources/fr/FormatData_fr_BE.class */
public class FormatData_fr_BE extends ParallelListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // sun.util.resources.ParallelListResourceBundle
    protected final Object[][] getContents() {
        return new Object[]{new Object[]{"NumberElements", new String[]{",", ".", ";", FXMLLoader.RESOURCE_KEY_PREFIX, "0", FXMLLoader.CONTROLLER_METHOD_PREFIX, LanguageTag.SEP, "E", "‰", "∞", "�"}}, new Object[]{"TimePatterns", new String[]{"H' h 'mm' min 'ss' s 'z", "H:mm:ss z", "H:mm:ss", "H:mm"}}, new Object[]{"DatePatterns", new String[]{"EEEE d MMMM yyyy", "d MMMM yyyy", "dd-MMM-yyyy", "d/MM/yy"}}, new Object[]{"DateTimePatterns", new String[]{"{1} {0}"}}, new Object[]{"DateTimePatternChars", "GaMjkHmsSEDFwWxhKzZ"}};
    }
}
