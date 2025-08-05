package sun.text.resources.cldr.az;

import java.util.ListResourceBundle;
import javafx.fxml.FXMLLoader;
import sun.util.locale.LanguageTag;

/* loaded from: cldrdata.jar:sun/text/resources/cldr/az/FormatData_az_Cyrl.class */
public class FormatData_az_Cyrl extends ListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // java.util.ListResourceBundle
    protected final Object[][] getContents() {
        return new Object[]{new Object[]{"MonthNames", new String[]{"јанвар", "феврал", "март", "апрел", "май", "ијун", "ијул", "август", "сентјабр", "октјабр", "нојабр", "декабр", ""}}, new Object[]{"DayNames", new String[]{"базар", "базар ертәси", "чәршәнбә ахшамы", "чәршәнбә", "ҹүмә ахшамы", "ҹүмә", "шәнбә"}}, new Object[]{"TimePatterns", new String[]{"HH:mm:ss zzzz", "HH:mm:ss z", "HH:mm:ss", "HH:mm"}}, new Object[]{"DatePatterns", new String[]{"EEEE, d, MMMM, y", "d MMMM , y", "d MMM, y", "yyyy-MM-dd"}}, new Object[]{"DefaultNumberingSystem", "latn"}, new Object[]{"latn.NumberElements", new String[]{",", ".", ";", FXMLLoader.RESOURCE_KEY_PREFIX, "0", FXMLLoader.CONTROLLER_METHOD_PREFIX, LanguageTag.SEP, "E", "‰", "∞", "NaN"}}, new Object[]{"NumberPatterns", new String[]{"#,##0.###", "¤ #,##0.00", "#,##0%"}}};
    }
}
