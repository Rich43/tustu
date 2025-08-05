package sun.text.resources.cs;

import com.sun.org.apache.xml.internal.security.utils.Constants;
import javafx.fxml.FXMLLoader;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;
import sun.util.resources.ParallelListResourceBundle;

/* loaded from: localedata.jar:sun/text/resources/cs/FormatData_cs.class */
public class FormatData_cs extends ParallelListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // sun.util.resources.ParallelListResourceBundle
    protected final Object[][] getContents() {
        return new Object[]{new Object[]{"MonthNames", new String[]{"ledna", "února", "března", "dubna", "května", "června", "července", "srpna", "září", "října", "listopadu", "prosince", ""}}, new Object[]{"standalone.MonthNames", new String[]{"leden", "únor", "březen", "duben", "květen", "červen", "červenec", "srpen", "září", "říjen", "listopad", "prosinec", ""}}, new Object[]{"MonthAbbreviations", new String[]{"Led", "Úno", "Bře", "Dub", "Kvě", "Čer", "Čvc", "Srp", "Zář", "Říj", "Lis", "Pro", ""}}, new Object[]{"standalone.MonthAbbreviations", new String[]{"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X", "XI", "XII", ""}}, new Object[]{"MonthNarrows", new String[]{PdfOps.l_TOKEN, "ú", PdfOps.b_TOKEN, PdfOps.d_TOKEN, PdfOps.k_TOKEN, "č", "č", PdfOps.s_TOKEN, "z", "ř", PdfOps.l_TOKEN, "p", ""}}, new Object[]{"standalone.MonthNarrows", new String[]{PdfOps.l_TOKEN, "ú", PdfOps.b_TOKEN, PdfOps.d_TOKEN, PdfOps.k_TOKEN, "č", "č", PdfOps.s_TOKEN, "z", "ř", PdfOps.l_TOKEN, "p", ""}}, new Object[]{"DayNames", new String[]{"Neděle", "Pondělí", "Úterý", "Středa", "Čtvrtek", "Pátek", "Sobota"}}, new Object[]{"standalone.DayNames", new String[]{"neděle", "pondělí", "úterý", "středa", "čtvrtek", "pátek", "sobota"}}, new Object[]{"DayAbbreviations", new String[]{"Ne", "Po", "Út", "St", "Čt", "Pá", "So"}}, new Object[]{"standalone.DayAbbreviations", new String[]{"ne", "po", "út", "st", "čt", "pá", "so"}}, new Object[]{"DayNarrows", new String[]{"N", Constants._TAG_P, "Ú", PdfOps.S_TOKEN, "Č", Constants._TAG_P, PdfOps.S_TOKEN}}, new Object[]{"standalone.DayNarrows", new String[]{"N", Constants._TAG_P, "Ú", PdfOps.S_TOKEN, "Č", Constants._TAG_P, PdfOps.S_TOKEN}}, new Object[]{"AmPmMarkers", new String[]{"dop.", "odp."}}, new Object[]{"Eras", new String[]{"př.Kr.", "po Kr."}}, new Object[]{"short.Eras", new String[]{"př. n. l.", "n. l."}}, new Object[]{"narrow.Eras", new String[]{"př.n.l.", "n. l."}}, new Object[]{"NumberElements", new String[]{",", " ", ";", FXMLLoader.RESOURCE_KEY_PREFIX, "0", FXMLLoader.CONTROLLER_METHOD_PREFIX, LanguageTag.SEP, "E", "‰", "∞", "�"}}, new Object[]{"TimePatterns", new String[]{"H:mm:ss z", "H:mm:ss z", "H:mm:ss", "H:mm"}}, new Object[]{"DatePatterns", new String[]{"EEEE, d. MMMM yyyy", "d. MMMM yyyy", "d.M.yyyy", "d.M.yy"}}, new Object[]{"DateTimePatterns", new String[]{"{1} {0}"}}, new Object[]{"DateTimePatternChars", "GuMtkHmsSEDFwWahKzZ"}};
    }
}
