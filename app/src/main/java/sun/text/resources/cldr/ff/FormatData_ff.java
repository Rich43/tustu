package sun.text.resources.cldr.ff;

import java.util.ListResourceBundle;
import javafx.fxml.FXMLLoader;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;

/* loaded from: cldrdata.jar:sun/text/resources/cldr/ff/FormatData_ff.class */
public class FormatData_ff extends ListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // java.util.ListResourceBundle
    protected final Object[][] getContents() {
        return new Object[]{new Object[]{"MonthNames", new String[]{"siilo", "colte", "mbooy", "seeɗto", "duujal", "korse", "morso", "juko", "siilto", "yarkomaa", "jolal", "bowte", ""}}, new Object[]{"MonthAbbreviations", new String[]{"sii", "col", "mbo", "see", "duu", "kor", "mor", "juk", "slt", "yar", "jol", "bow", ""}}, new Object[]{"MonthNarrows", new String[]{PdfOps.s_TOKEN, PdfOps.c_TOKEN, PdfOps.m_TOKEN, PdfOps.s_TOKEN, PdfOps.d_TOKEN, PdfOps.k_TOKEN, PdfOps.m_TOKEN, PdfOps.j_TOKEN, PdfOps.s_TOKEN, PdfOps.y_TOKEN, PdfOps.j_TOKEN, PdfOps.b_TOKEN, ""}}, new Object[]{"DayNames", new String[]{"dewo", "aaɓnde", "mawbaare", "njeslaare", "naasaande", "mawnde", "hoore-biir"}}, new Object[]{"DayAbbreviations", new String[]{"dew", "aaɓ", "maw", "nje", "naa", "mwd", "hbi"}}, new Object[]{"DayNarrows", new String[]{PdfOps.d_TOKEN, "a", PdfOps.m_TOKEN, PdfOps.n_TOKEN, PdfOps.n_TOKEN, PdfOps.m_TOKEN, PdfOps.h_TOKEN}}, new Object[]{"QuarterNames", new String[]{"Termes 1", "Termes 2", "Termes 3", "Termes 4"}}, new Object[]{"QuarterAbbreviations", new String[]{"T1", "T2", "T3", "T4"}}, new Object[]{"AmPmMarkers", new String[]{"subaka", "kikiiɗe"}}, new Object[]{"long.Eras", new String[]{"Hade Iisa", "Caggal Iisa"}}, new Object[]{"Eras", new String[]{"H-I", "C-I"}}, new Object[]{"field.era", "Jamaanu"}, new Object[]{"field.year", "Hitaande"}, new Object[]{"field.month", "Lewru"}, new Object[]{"field.week", "Yontere"}, new Object[]{"field.weekday", "Ñalɗi yontere"}, new Object[]{"field.dayperiod", "Sahnga"}, new Object[]{"field.hour", "Waktu"}, new Object[]{"field.minute", "Hoƴom"}, new Object[]{"field.second", "Majaango"}, new Object[]{"field.zone", "Diiwaan waktu"}, new Object[]{"TimePatterns", new String[]{"HH:mm:ss zzzz", "HH:mm:ss z", "HH:mm:ss", "HH:mm"}}, new Object[]{"DatePatterns", new String[]{"EEEE d MMMM y", "d MMMM y", "d MMM, y", "d/M/yyyy"}}, new Object[]{"DefaultNumberingSystem", "latn"}, new Object[]{"latn.NumberElements", new String[]{",", " ", ";", FXMLLoader.RESOURCE_KEY_PREFIX, "0", FXMLLoader.CONTROLLER_METHOD_PREFIX, LanguageTag.SEP, "E", "‰", "∞", "NaN"}}, new Object[]{"NumberPatterns", new String[]{"#,##0.###", "#,##0.00 ¤", "#,##0%"}}};
    }
}
