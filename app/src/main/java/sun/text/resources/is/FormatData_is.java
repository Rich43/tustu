package sun.text.resources.is;

import javafx.fxml.FXMLLoader;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;
import sun.util.resources.ParallelListResourceBundle;

/* loaded from: localedata.jar:sun/text/resources/is/FormatData_is.class */
public class FormatData_is extends ParallelListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // sun.util.resources.ParallelListResourceBundle
    protected final Object[][] getContents() {
        return new Object[]{new Object[]{"MonthNames", new String[]{"janúar", "febrúar", "mars", "apríl", "maí", "júní", "júlí", "ágúst", "september", "október", "nóvember", "desember", ""}}, new Object[]{"MonthAbbreviations", new String[]{"jan.", "feb.", "mar.", "apr.", "maí", "jún.", "júl.", "ágú.", "sep.", "okt.", "nóv.", "des.", ""}}, new Object[]{"MonthNarrows", new String[]{"J", PdfOps.F_TOKEN, PdfOps.M_TOKEN, "A", PdfOps.M_TOKEN, "J", "J", "Á", "L", "O", "N", PdfOps.D_TOKEN, ""}}, new Object[]{"standalone.MonthNarrows", new String[]{PdfOps.j_TOKEN, PdfOps.f_TOKEN, PdfOps.m_TOKEN, "a", PdfOps.m_TOKEN, PdfOps.j_TOKEN, PdfOps.j_TOKEN, "á", PdfOps.s_TOKEN, "o", PdfOps.n_TOKEN, PdfOps.d_TOKEN, ""}}, new Object[]{"DayNames", new String[]{"sunnudagur", "mánudagur", "þriðjudagur", "miðvikudagur", "fimmtudagur", "föstudagur", "laugardagur"}}, new Object[]{"DayAbbreviations", new String[]{"sun.", "mán.", "þri.", "mið.", "fim.", "fös.", "lau."}}, new Object[]{"DayNarrows", new String[]{PdfOps.S_TOKEN, PdfOps.M_TOKEN, "Þ", PdfOps.M_TOKEN, PdfOps.F_TOKEN, PdfOps.F_TOKEN, "L"}}, new Object[]{"standalone.DayNarrows", new String[]{PdfOps.s_TOKEN, PdfOps.m_TOKEN, "þ", PdfOps.m_TOKEN, PdfOps.f_TOKEN, PdfOps.f_TOKEN, PdfOps.l_TOKEN}}, new Object[]{"NumberElements", new String[]{",", ".", ";", FXMLLoader.RESOURCE_KEY_PREFIX, "0", FXMLLoader.CONTROLLER_METHOD_PREFIX, LanguageTag.SEP, "E", "‰", "∞", "�"}}, new Object[]{"TimePatterns", new String[]{"HH:mm:ss z", "HH:mm:ss z", "HH:mm:ss", "HH:mm"}}, new Object[]{"DatePatterns", new String[]{"d. MMMM yyyy", "d. MMMM yyyy", "d.M.yyyy", "d.M.yyyy"}}, new Object[]{"DateTimePatterns", new String[]{"{1} {0}"}}, new Object[]{"DateTimePatternChars", "GyMdkHmsSEDFwWahKzZ"}};
    }
}
