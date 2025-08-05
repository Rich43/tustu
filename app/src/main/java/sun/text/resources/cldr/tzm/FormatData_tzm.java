package sun.text.resources.cldr.tzm;

import com.sun.org.apache.xml.internal.security.utils.Constants;
import java.util.ListResourceBundle;
import javafx.fxml.FXMLLoader;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;

/* loaded from: cldrdata.jar:sun/text/resources/cldr/tzm/FormatData_tzm.class */
public class FormatData_tzm extends ListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // java.util.ListResourceBundle
    protected final Object[][] getContents() {
        return new Object[]{new Object[]{"MonthNames", new String[]{"Yennayer", "Yebrayer", "Mars", "Ibrir", "Mayyu", "Yunyu", "Yulyuz", "Ɣuct", "Cutanbir", "Kṭuber", "Nwanbir", "Dujanbir", ""}}, new Object[]{"MonthAbbreviations", new String[]{"Yen", "Yeb", "Mar", "Ibr", "May", "Yun", "Yul", "Ɣuc", "Cut", "Kṭu", "Nwa", "Duj", ""}}, new Object[]{"MonthNarrows", new String[]{Constants._TAG_Y, Constants._TAG_Y, PdfOps.M_TOKEN, "I", PdfOps.M_TOKEN, Constants._TAG_Y, Constants._TAG_Y, "Ɣ", "C", PdfOps.K_TOKEN, "N", PdfOps.D_TOKEN, ""}}, new Object[]{"DayNames", new String[]{"Asamas", "Aynas", "Asinas", "Akras", "Akwas", "Asimwas", "Asiḍyas"}}, new Object[]{"DayAbbreviations", new String[]{"Asa", "Ayn", "Asn", "Akr", "Akw", "Asm", "Asḍ"}}, new Object[]{"DayNarrows", new String[]{"A", "A", "A", "A", "A", "A", "A"}}, new Object[]{"QuarterNames", new String[]{"Imir adamsan 1", "Imir adamsan 2", "Imir adamsan 3", "Imir adamsan 4"}}, new Object[]{"QuarterAbbreviations", new String[]{"IA1", "IA2", "IA3", "IA4"}}, new Object[]{"AmPmMarkers", new String[]{"Zdat azal", "Ḍeffir aza"}}, new Object[]{"long.Eras", new String[]{"Zdat Ɛisa (TAƔ)", "Ḍeffir Ɛisa (TAƔ)"}}, new Object[]{"Eras", new String[]{"ZƐ", "ḌƐ"}}, new Object[]{"field.era", "Tallit"}, new Object[]{"field.year", "Asseggas"}, new Object[]{"field.month", "Ayur"}, new Object[]{"field.week", "Imalass"}, new Object[]{"field.weekday", "Ass n Imalass"}, new Object[]{"field.dayperiod", "Zdat azal/Deffir azal"}, new Object[]{"field.hour", "Tasragt"}, new Object[]{"field.minute", "Tusdat"}, new Object[]{"field.second", "Tusnat"}, new Object[]{"field.zone", "Aseglem asergan"}, new Object[]{"TimePatterns", new String[]{"h:mm:ss a zzzz", "h:mm:ss a z", "h:mm:ss a", "h:mm a"}}, new Object[]{"DatePatterns", new String[]{"EEEE, d MMMM y", "d MMMM y", "d MMM y", "dd/MM/yyyy"}}, new Object[]{"DefaultNumberingSystem", "latn"}, new Object[]{"latn.NumberElements", new String[]{",", " ", ";", FXMLLoader.RESOURCE_KEY_PREFIX, "0", FXMLLoader.CONTROLLER_METHOD_PREFIX, LanguageTag.SEP, "E", "‰", "∞", "NaN"}}, new Object[]{"NumberPatterns", new String[]{"#,##0.###", "#,##0.00 ¤", "#,##0%"}}};
    }
}
