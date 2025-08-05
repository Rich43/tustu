package sun.text.resources.cldr.eo;

import java.util.ListResourceBundle;
import javafx.fxml.FXMLLoader;
import sun.util.locale.LanguageTag;

/* loaded from: cldrdata.jar:sun/text/resources/cldr/eo/FormatData_eo.class */
public class FormatData_eo extends ListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // java.util.ListResourceBundle
    protected final Object[][] getContents() {
        return new Object[]{new Object[]{"MonthNames", new String[]{"januaro", "februaro", "marto", "aprilo", "majo", "junio", "julio", "aŭgusto", "septembro", "oktobro", "novembro", "decembro", ""}}, new Object[]{"MonthAbbreviations", new String[]{"jan", "feb", "mar", "apr", "maj", "jun", "jul", "aŭg", "sep", "okt", "nov", "dec", ""}}, new Object[]{"DayNames", new String[]{"dimanĉo", "lundo", "mardo", "merkredo", "ĵaŭdo", "vendredo", "sabato"}}, new Object[]{"DayAbbreviations", new String[]{"di", "lu", "ma", "me", "ĵa", "ve", "sa"}}, new Object[]{"AmPmMarkers", new String[]{"atm", "ptm"}}, new Object[]{"Eras", new String[]{"aK", "pK"}}, new Object[]{"TimePatterns", new String[]{"H-'a' 'horo' 'kaj' m:ss zzzz", "HH:mm:ss z", "HH:mm:ss", "HH:mm"}}, new Object[]{"DatePatterns", new String[]{"EEEE, d-'a' 'de' MMMM y", "y-MMMM-dd", "y-MMM-dd", "yy-MM-dd"}}, new Object[]{"DefaultNumberingSystem", "latn"}, new Object[]{"latn.NumberElements", new String[]{",", " ", ";", FXMLLoader.RESOURCE_KEY_PREFIX, "0", FXMLLoader.CONTROLLER_METHOD_PREFIX, LanguageTag.SEP, "E", "‰", "∞", "NaN"}}};
    }
}
