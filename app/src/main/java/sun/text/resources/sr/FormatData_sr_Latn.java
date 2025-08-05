package sun.text.resources.sr;

import javafx.fxml.FXMLLoader;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;
import sun.util.resources.ParallelListResourceBundle;

/* loaded from: localedata.jar:sun/text/resources/sr/FormatData_sr_Latn.class */
public class FormatData_sr_Latn extends ParallelListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // sun.util.resources.ParallelListResourceBundle
    protected final Object[][] getContents() {
        return new Object[]{new Object[]{"MonthNames", new String[]{"januar", "februar", "mart", "april", "maj", "jun", "jul", "avgust", "septembar", "oktobar", "novembar", "decembar", ""}}, new Object[]{"MonthAbbreviations", new String[]{"jan", "feb", "mar", "apr", "maj", "jun", "jul", "avg", "sep", "okt", "nov", "dec", ""}}, new Object[]{"MonthNarrows", new String[]{PdfOps.j_TOKEN, PdfOps.f_TOKEN, PdfOps.m_TOKEN, "a", PdfOps.m_TOKEN, PdfOps.j_TOKEN, PdfOps.j_TOKEN, "a", PdfOps.s_TOKEN, "o", PdfOps.n_TOKEN, PdfOps.d_TOKEN, ""}}, new Object[]{"DayNames", new String[]{"nedelja", "ponedeljak", "utorak", "sreda", "četvrtak", "petak", "subota"}}, new Object[]{"DayAbbreviations", new String[]{"ned", "pon", "uto", "sre", "čet", "pet", "sub"}}, new Object[]{"DayNarrows", new String[]{PdfOps.n_TOKEN, "p", "u", PdfOps.s_TOKEN, "č", "p", PdfOps.s_TOKEN}}, new Object[]{"Eras", new String[]{"p. n. e.", "n. e"}}, new Object[]{"NumberPatterns", new String[]{"#,##0.###", "¤ #,##0.00", "#,##0%"}}, new Object[]{"NumberElements", new String[]{".", ",", ";", FXMLLoader.RESOURCE_KEY_PREFIX, "0", FXMLLoader.CONTROLLER_METHOD_PREFIX, LanguageTag.SEP, "E", "‰", "∞", "NaN"}}, new Object[]{"TimePatterns", new String[]{"HH.mm.ss zzzz", "HH.mm.ss z", "HH.mm.ss", "HH.mm"}}, new Object[]{"DatePatterns", new String[]{"EEEE, dd. MMMM y.", "dd. MMMM y.", "dd.MM.y.", "d.M.yy."}}, new Object[]{"DateTimePatterns", new String[]{"{1} {0}"}}};
    }
}
