package sun.text.resources.cldr.to;

import com.sun.org.apache.xml.internal.security.utils.Constants;
import java.util.ListResourceBundle;
import javafx.fxml.FXMLLoader;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;

/* loaded from: cldrdata.jar:sun/text/resources/cldr/to/FormatData_to.class */
public class FormatData_to extends ListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // java.util.ListResourceBundle
    protected final Object[][] getContents() {
        return new Object[]{new Object[]{"MonthNames", new String[]{"Sānuali", "Fēpueli", "Maʻasi", "ʻEpeleli", "Mē", "Sune", "Siulai", "ʻAokosi", "Sepitema", "ʻOkatopa", "Nōvema", "Tīsema", ""}}, new Object[]{"MonthAbbreviations", new String[]{"Sān", "Fēp", "Maʻa", "ʻEpe", "Mē", "Sun", "Siu", "ʻAok", "Sep", "ʻOka", "Nōv", "Tīs", ""}}, new Object[]{"MonthNarrows", new String[]{PdfOps.S_TOKEN, PdfOps.F_TOKEN, PdfOps.M_TOKEN, "E", PdfOps.M_TOKEN, PdfOps.S_TOKEN, PdfOps.S_TOKEN, "A", PdfOps.S_TOKEN, "O", "N", "T", ""}}, new Object[]{"DayNames", new String[]{"Sāpate", "Mōnite", "Tūsite", "Pulelulu", "Tuʻapulelulu", "Falaite", "Tokonaki"}}, new Object[]{"DayAbbreviations", new String[]{"Sāp", "Mōn", "Tūs", "Pul", "Tuʻa", "Fal", "Tok"}}, new Object[]{"DayNarrows", new String[]{PdfOps.S_TOKEN, PdfOps.M_TOKEN, "T", Constants._TAG_P, "T", PdfOps.F_TOKEN, "T"}}, new Object[]{"QuarterNames", new String[]{"kuata ʻuluaki", "kuata ua", "kuata tolu", "kuata fā"}}, new Object[]{"standalone.QuarterNames", new String[]{"kuata 1", "kuata 2", "kuata 3", "kuata 4"}}, new Object[]{"QuarterAbbreviations", new String[]{"K1", "K2", "K3", "K4"}}, new Object[]{"long.Eras", new String[]{"ki muʻa", "taʻu ʻo Sīsū"}}, new Object[]{"Eras", new String[]{"KM", "TS"}}, new Object[]{"narrow.Eras", new String[]{"KāMā", "TāSā"}}, new Object[]{"field.era", "kuonga"}, new Object[]{"field.year", "taʻu"}, new Object[]{"field.month", "māhina"}, new Object[]{"field.week", "uike"}, new Object[]{"field.weekday", "ʻaho ʻo e uike"}, new Object[]{"field.dayperiod", "AM/PM"}, new Object[]{"field.hour", "houa"}, new Object[]{"field.minute", "miniti"}, new Object[]{"field.second", "sekoni"}, new Object[]{"field.zone", "taimi fakavahe"}, new Object[]{"TimePatterns", new String[]{"HH:mm:ss zzzz", "HH:mm:ss z", "HH:mm:ss", "HH:mm"}}, new Object[]{"DatePatterns", new String[]{"EEEE d MMMM y", "d MMMM y", "d MMM y", "d/M/yy"}}, new Object[]{"calendarname.islamic-civil", "fakamohameti-sivile"}, new Object[]{"calendarname.islamicc", "fakamohameti-sivile"}, new Object[]{"calendarname.gregorian", "fakakelekolia"}, new Object[]{"calendarname.gregory", "fakakelekolia"}, new Object[]{"calendarname.japanese", "fakasiapani"}, new Object[]{"calendarname.buddhist", "fakaputa"}, new Object[]{"calendarname.islamic", "fakamohameti"}, new Object[]{"DefaultNumberingSystem", "latn"}, new Object[]{"latn.NumberElements", new String[]{".", ",", ";", FXMLLoader.RESOURCE_KEY_PREFIX, "0", FXMLLoader.CONTROLLER_METHOD_PREFIX, LanguageTag.SEP, "E", "‰", "∞", "NaN"}}};
    }
}
