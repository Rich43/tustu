package sun.text.resources.sl;

import javafx.fxml.FXMLLoader;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;
import sun.util.resources.ParallelListResourceBundle;

/* loaded from: localedata.jar:sun/text/resources/sl/FormatData_sl.class */
public class FormatData_sl extends ParallelListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // sun.util.resources.ParallelListResourceBundle
    protected final Object[][] getContents() {
        return new Object[]{new Object[]{"MonthNames", new String[]{"januar", "februar", "marec", "april", "maj", "junij", "julij", "avgust", "september", "oktober", "november", "december", ""}}, new Object[]{"MonthAbbreviations", new String[]{"jan.", "feb.", "mar.", "apr.", "maj", "jun.", "jul.", "avg.", "sep.", "okt.", "nov.", "dec.", ""}}, new Object[]{"standalone.MonthAbbreviations", new String[]{"jan", "feb", "mar", "apr", "maj", "jun", "jul", "avg", "sep", "okt", "nov", "dec", ""}}, new Object[]{"MonthNarrows", new String[]{PdfOps.j_TOKEN, PdfOps.f_TOKEN, PdfOps.m_TOKEN, "a", PdfOps.m_TOKEN, PdfOps.j_TOKEN, PdfOps.j_TOKEN, "a", PdfOps.s_TOKEN, "o", PdfOps.n_TOKEN, PdfOps.d_TOKEN, ""}}, new Object[]{"DayNames", new String[]{"Nedelja", "Ponedeljek", "Torek", "Sreda", "Četrtek", "Petek", "Sobota"}}, new Object[]{"DayAbbreviations", new String[]{"Ned", "Pon", "Tor", "Sre", "Čet", "Pet", "Sob"}}, new Object[]{"standalone.DayAbbreviations", new String[]{"ned", "pon", "tor", "sre", "čet", "pet", "sob"}}, new Object[]{"DayNarrows", new String[]{PdfOps.n_TOKEN, "p", "t", PdfOps.s_TOKEN, "č", "p", PdfOps.s_TOKEN}}, new Object[]{"Eras", new String[]{"pr.n.š.", "po Kr."}}, new Object[]{"NumberElements", new String[]{",", ".", ";", FXMLLoader.RESOURCE_KEY_PREFIX, "0", FXMLLoader.CONTROLLER_METHOD_PREFIX, LanguageTag.SEP, "E", "‰", "∞", "�"}}, new Object[]{"TimePatterns", new String[]{"H:mm:ss z", "H:mm:ss z", "H:mm:ss", "H:mm"}}, new Object[]{"DatePatterns", new String[]{"EEEE, dd. MMMM y", "dd. MMMM y", "d.M.yyyy", "d.M.y"}}, new Object[]{"DateTimePatterns", new String[]{"{1} {0}"}}, new Object[]{"DateTimePatternChars", "GanjkHmsSEDFwWxhKzZ"}};
    }
}
