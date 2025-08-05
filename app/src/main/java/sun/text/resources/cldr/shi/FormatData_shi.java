package sun.text.resources.cldr.shi;

import java.util.ListResourceBundle;
import javafx.fxml.FXMLLoader;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;

/* loaded from: cldrdata.jar:sun/text/resources/cldr/shi/FormatData_shi.class */
public class FormatData_shi extends ListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // java.util.ListResourceBundle
    protected final Object[][] getContents() {
        return new Object[]{new Object[]{"MonthNames", new String[]{"innayr", "bṛayṛ", "maṛṣ", "ibrir", "mayyu", "yunyu", "yulyuz", "ɣuct", "cutanbir", "ktubr", "nuwanbir", "dujanbir", ""}}, new Object[]{"MonthAbbreviations", new String[]{"inn", "bṛa", "maṛ", "ibr", "may", "yun", "yul", "ɣuc", "cut", "ktu", "nuw", "duj", ""}}, new Object[]{"MonthNarrows", new String[]{PdfOps.i_TOKEN, PdfOps.b_TOKEN, PdfOps.m_TOKEN, PdfOps.i_TOKEN, PdfOps.m_TOKEN, PdfOps.y_TOKEN, PdfOps.y_TOKEN, "ɣ", PdfOps.c_TOKEN, PdfOps.k_TOKEN, PdfOps.n_TOKEN, PdfOps.d_TOKEN, ""}}, new Object[]{"DayNames", new String[]{"asamas", "aynas", "asinas", "akṛas", "akwas", "asimwas", "asiḍyas"}}, new Object[]{"DayAbbreviations", new String[]{"asa", "ayn", "asi", "akṛ", "akw", "asim", "asiḍ"}}, new Object[]{"QuarterNames", new String[]{"akṛaḍyur 1", "akṛaḍyur 2", "akṛaḍyur 3", "akṛaḍyur 4"}}, new Object[]{"QuarterAbbreviations", new String[]{"ak 1", "ak 2", "ak 3", "ak 4"}}, new Object[]{"AmPmMarkers", new String[]{"tifawt", "tadggʷat"}}, new Object[]{"long.Eras", new String[]{"dat n ɛisa", "dffir n ɛisa"}}, new Object[]{"Eras", new String[]{"daɛ", "dfɛ"}}, new Object[]{"field.era", "tasut"}, new Object[]{"field.year", "asggʷas"}, new Object[]{"field.month", "ayyur"}, new Object[]{"field.week", "imalass"}, new Object[]{"field.weekday", "ass g imalass"}, new Object[]{"field.dayperiod", "tizi g wass: tifawt / tadggʷat"}, new Object[]{"field.hour", "tasragt"}, new Object[]{"field.minute", "tusdidt"}, new Object[]{"field.second", "tasint"}, new Object[]{"field.zone", "akud n ugmmaḍ"}, new Object[]{"TimePatterns", new String[]{"HH:mm:ss zzzz", "HH:mm:ss z", "HH:mm:ss", "HH:mm"}}, new Object[]{"DatePatterns", new String[]{"EEEE d MMMM y", "d MMMM y", "d MMM, y", "d/M/yyyy"}}, new Object[]{"DefaultNumberingSystem", "latn"}, new Object[]{"latn.NumberElements", new String[]{",", " ", ";", FXMLLoader.RESOURCE_KEY_PREFIX, "0", FXMLLoader.CONTROLLER_METHOD_PREFIX, LanguageTag.SEP, "E", "‰", "∞", "NaN"}}, new Object[]{"NumberPatterns", new String[]{"#,##0.###", "#,##0.00¤", "#,##0%"}}};
    }
}
