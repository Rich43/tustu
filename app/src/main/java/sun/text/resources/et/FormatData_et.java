package sun.text.resources.et;

import com.sun.org.apache.xml.internal.security.utils.Constants;
import javafx.fxml.FXMLLoader;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;
import sun.util.resources.ParallelListResourceBundle;

/* loaded from: localedata.jar:sun/text/resources/et/FormatData_et.class */
public class FormatData_et extends ParallelListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // sun.util.resources.ParallelListResourceBundle
    protected final Object[][] getContents() {
        return new Object[]{new Object[]{"MonthNames", new String[]{"jaanuar", "veebruar", "märts", "aprill", "mai", "juuni", "juuli", "august", "september", "oktoober", "november", "detsember", ""}}, new Object[]{"MonthAbbreviations", new String[]{"jaan", "veebr", "märts", "apr", "mai", "juuni", "juuli", "aug", "sept", "okt", "nov", "dets", ""}}, new Object[]{"MonthNarrows", new String[]{"J", "V", PdfOps.M_TOKEN, "A", PdfOps.M_TOKEN, "J", "J", "A", PdfOps.S_TOKEN, "O", "N", PdfOps.D_TOKEN, ""}}, new Object[]{"DayNames", new String[]{"pühapäev", "esmaspäev", "teisipäev", "kolmapäev", "neljapäev", "reede", "laupäev"}}, new Object[]{"DayAbbreviations", new String[]{Constants._TAG_P, "E", "T", PdfOps.K_TOKEN, "N", "R", "L"}}, new Object[]{"DayNarrows", new String[]{Constants._TAG_P, "E", "T", PdfOps.K_TOKEN, "N", "R", "L"}}, new Object[]{"Eras", new String[]{"e.m.a.", "m.a.j."}}, new Object[]{"short.Eras", new String[]{"e.m.a.", "m.a.j."}}, new Object[]{"NumberElements", new String[]{",", " ", ";", FXMLLoader.RESOURCE_KEY_PREFIX, "0", FXMLLoader.CONTROLLER_METHOD_PREFIX, LanguageTag.SEP, "E", "‰", "∞", "�"}}, new Object[]{"TimePatterns", new String[]{"H:mm:ss z", "H:mm:ss z", "H:mm:ss", "H:mm"}}, new Object[]{"DatePatterns", new String[]{"EEEE, d. MMMM yyyy", "EEEE, d. MMMM yyyy. 'a'", "d.MM.yyyy", "d.MM.yy"}}, new Object[]{"DateTimePatterns", new String[]{"{1} {0}"}}};
    }
}
