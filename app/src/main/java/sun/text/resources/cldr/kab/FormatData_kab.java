package sun.text.resources.cldr.kab;

import com.sun.org.apache.xml.internal.security.utils.Constants;
import java.util.ListResourceBundle;
import javafx.fxml.FXMLLoader;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;

/* loaded from: cldrdata.jar:sun/text/resources/cldr/kab/FormatData_kab.class */
public class FormatData_kab extends ListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // java.util.ListResourceBundle
    protected final Object[][] getContents() {
        return new Object[]{new Object[]{"MonthNames", new String[]{"Yennayer", "Fuṛar", "Meɣres", "Yebrir", "Mayyu", "Yunyu", "Yulyu", "Ɣuct", "Ctembeṛ", "Tubeṛ", "Nunembeṛ", "Duǧembeṛ", ""}}, new Object[]{"MonthAbbreviations", new String[]{"Yen", "Fur", "Meɣ", "Yeb", "May", "Yun", "Yul", "Ɣuc", "Cte", "Tub", "Nun", "Duǧ", ""}}, new Object[]{"MonthNarrows", new String[]{Constants._TAG_Y, PdfOps.F_TOKEN, PdfOps.M_TOKEN, Constants._TAG_Y, PdfOps.M_TOKEN, Constants._TAG_Y, Constants._TAG_Y, "Ɣ", "C", "T", "N", PdfOps.D_TOKEN, ""}}, new Object[]{"DayNames", new String[]{"Yanass", "Sanass", "Kraḍass", "Kuẓass", "Samass", "Sḍisass", "Sayass"}}, new Object[]{"DayAbbreviations", new String[]{"Yan", "San", "Kraḍ", "Kuẓ", "Sam", "Sḍis", "Say"}}, new Object[]{"DayNarrows", new String[]{Constants._TAG_Y, PdfOps.S_TOKEN, PdfOps.K_TOKEN, PdfOps.K_TOKEN, PdfOps.S_TOKEN, PdfOps.S_TOKEN, PdfOps.S_TOKEN}}, new Object[]{"QuarterNames", new String[]{"akraḍaggur amenzu", "akraḍaggur wis-sin", "akraḍaggur wis-kraḍ", "akraḍaggur wis-kuẓ"}}, new Object[]{"QuarterAbbreviations", new String[]{"Kḍg1", "Kḍg2", "Kḍg3", "Kḍg4"}}, new Object[]{"AmPmMarkers", new String[]{"n tufat", "n tmeddit"}}, new Object[]{"long.Eras", new String[]{"send talalit n Ɛisa", "seld talalit n Ɛisa"}}, new Object[]{"Eras", new String[]{"snd. T.Ɛ", "sld. T.Ɛ"}}, new Object[]{"field.era", "Tallit"}, new Object[]{"field.year", "Aseggas"}, new Object[]{"field.month", "Aggur"}, new Object[]{"field.week", "Ddurt"}, new Object[]{"field.weekday", "Ussan n ddurt"}, new Object[]{"field.dayperiod", "n tufat / n tmeddit"}, new Object[]{"field.hour", "Tamert"}, new Object[]{"field.minute", "Tamrect"}, new Object[]{"field.second", "Tasint"}, new Object[]{"field.zone", "Aseglem asergan"}, new Object[]{"TimePatterns", new String[]{"HH:mm:ss zzzz", "HH:mm:ss z", "HH:mm:ss", "HH:mm"}}, new Object[]{"DatePatterns", new String[]{"EEEE d MMMM y", "d MMMM y", "d MMM, y", "d/M/yyyy"}}, new Object[]{"DefaultNumberingSystem", "latn"}, new Object[]{"latn.NumberElements", new String[]{",", " ", ";", FXMLLoader.RESOURCE_KEY_PREFIX, "0", FXMLLoader.CONTROLLER_METHOD_PREFIX, LanguageTag.SEP, "E", "‰", "∞", "NaN"}}, new Object[]{"NumberPatterns", new String[]{"#,##0.###", "#,##0.00¤", "#,##0%"}}};
    }
}
