package sun.text.resources.cldr.ps;

import java.util.ListResourceBundle;
import javafx.fxml.FXMLLoader;
import sun.util.locale.LanguageTag;

/* loaded from: cldrdata.jar:sun/text/resources/cldr/ps/FormatData_ps.class */
public class FormatData_ps extends ListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // java.util.ListResourceBundle
    protected final Object[][] getContents() {
        return new Object[]{new Object[]{"MonthNames", new String[]{"جنوري", "فبروري", "مارچ", "اپریل", "می", "جون", "جولای", "اګست", "سپتمبر", "اکتوبر", "نومبر", "دسمبر", ""}}, new Object[]{"DayNames", new String[]{"یکشنبه", "دوشنبه", "سه\u200cشنبه", "چهارشنبه", "پنجشنبه", "جمعه", "شنبه"}}, new Object[]{"AmPmMarkers", new String[]{"غ.م.", "غ.و."}}, new Object[]{"Eras", new String[]{"ق.م.", "م."}}, new Object[]{"TimePatterns", new String[]{"H:mm:ss (zzzz)", "H:mm:ss (z)", "H:mm:ss", "H:mm"}}, new Object[]{"DatePatterns", new String[]{"EEEE د y د MMMM d", "د y د MMMM d", "d MMM y", "yyyy/M/d"}}, new Object[]{"DefaultNumberingSystem", "arabext"}, new Object[]{"arabext.NumberElements", new String[]{"٫", "٬", "؛", "٪", "۰", FXMLLoader.CONTROLLER_METHOD_PREFIX, LanguageTag.SEP, "×۱۰^", "؉", "∞", "NaN"}}, new Object[]{"latn.NumberElements", new String[]{",", ".", ";", FXMLLoader.RESOURCE_KEY_PREFIX, "0", FXMLLoader.CONTROLLER_METHOD_PREFIX, "−", "E", "‰", "∞", "NaN"}}, new Object[]{"NumberPatterns", new String[]{"#,##0.###", "#,##0.00 ¤", "#,##0%"}}};
    }
}
