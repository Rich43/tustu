package sun.text.resources.cldr.fo;

import java.util.ListResourceBundle;
import javafx.fxml.FXMLLoader;
import sun.util.locale.LanguageTag;

/* loaded from: cldrdata.jar:sun/text/resources/cldr/fo/FormatData_fo.class */
public class FormatData_fo extends ListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // java.util.ListResourceBundle
    protected final Object[][] getContents() {
        return new Object[]{new Object[]{"MonthNames", new String[]{"januar", "februar", "mars", "apríl", "mai", "juni", "juli", "august", "september", "oktober", "november", "desember", ""}}, new Object[]{"MonthAbbreviations", new String[]{"jan", "feb", "mar", "apr", "mai", "jun", "jul", "aug", "sep", "okt", "nov", "des", ""}}, new Object[]{"DayNames", new String[]{"sunnudagur", "mánadagur", "týsdagur", "mikudagur", "hósdagur", "fríggjadagur", "leygardagur"}}, new Object[]{"DayAbbreviations", new String[]{"sun", "mán", "týs", "mik", "hós", "frí", "ley"}}, new Object[]{"QuarterNames", new String[]{"1. kvartal", "2. kvartal", "3. kvartal", "4. kvartal"}}, new Object[]{"QuarterAbbreviations", new String[]{"K1", "K2", "K3", "K4"}}, new Object[]{"DatePatterns", new String[]{"EEEE dd MMMM y", "d. MMM y", "dd-MM-yyyy", "dd-MM-yy"}}, new Object[]{"DefaultNumberingSystem", "latn"}, new Object[]{"latn.NumberElements", new String[]{",", ".", ";", FXMLLoader.RESOURCE_KEY_PREFIX, "0", FXMLLoader.CONTROLLER_METHOD_PREFIX, LanguageTag.SEP, "E", "‰", "∞", "NaN"}}, new Object[]{"NumberPatterns", new String[]{"#,##0.###", "¤#,##0.00;¤-#,##0.00", "#,##0%"}}};
    }
}
