package sun.text.resources.cldr.vai;

import java.util.ListResourceBundle;
import javafx.fxml.FXMLLoader;
import javax.swing.plaf.nimbus.NimbusStyle;
import sun.util.locale.LanguageTag;

/* loaded from: cldrdata.jar:sun/text/resources/cldr/vai/FormatData_vai_Latn.class */
public class FormatData_vai_Latn extends ListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // java.util.ListResourceBundle
    protected final Object[][] getContents() {
        return new Object[]{new Object[]{"MonthNames", new String[]{"luukao kemã", "ɓandaɓu", "vɔɔ", "fulu", "goo", "6", "7", "kɔnde", "saah", "galo", "kenpkato ɓololɔ", "luukao lɔma", ""}}, new Object[]{"DayNames", new String[]{"lahadi", "tɛɛnɛɛ", "talata", "alaba", "aimisa", "aijima", "siɓiti"}}, new Object[]{"field.year", "saŋ"}, new Object[]{"field.month", "kalo"}, new Object[]{"field.week", "wiki"}, new Object[]{"field.weekday", "wikiyɛma tele"}, new Object[]{"field.hour", "hawa"}, new Object[]{"field.minute", NimbusStyle.MINI_KEY}, new Object[]{"field.second", "jaki-jaka"}, new Object[]{"TimePatterns", new String[]{"h:mm:ss a zzzz", "h:mm:ss a z", "h:mm:ss a", "h:mm a"}}, new Object[]{"DatePatterns", new String[]{"EEEE, d MMMM y", "d MMMM y", "d MMM y", "dd/MM/yyyy"}}, new Object[]{"DefaultNumberingSystem", "latn"}, new Object[]{"latn.NumberElements", new String[]{".", ",", ";", FXMLLoader.RESOURCE_KEY_PREFIX, "0", FXMLLoader.CONTROLLER_METHOD_PREFIX, LanguageTag.SEP, "E", "‰", "∞", "NaN"}}, new Object[]{"NumberPatterns", new String[]{"#,##0.###", "¤#,##0.00;(¤#,##0.00)", "#,##0%"}}};
    }
}
