package sun.text.resources.ca;

import javafx.fxml.FXMLLoader;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;
import sun.util.resources.ParallelListResourceBundle;

/* loaded from: localedata.jar:sun/text/resources/ca/FormatData_ca.class */
public class FormatData_ca extends ParallelListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // sun.util.resources.ParallelListResourceBundle
    protected final Object[][] getContents() {
        return new Object[]{new Object[]{"MonthNames", new String[]{"de gener", "de febrer", "de març", "d’abril", "de maig", "de juny", "de juliol", "d’agost", "de setembre", "d’octubre", "de novembre", "de desembre", ""}}, new Object[]{"MonthNarrows", new String[]{"G", PdfOps.F_TOKEN, PdfOps.M_TOKEN, "A", PdfOps.M_TOKEN, "J", "G", "A", PdfOps.S_TOKEN, "O", "N", PdfOps.D_TOKEN, ""}}, new Object[]{"standalone.MonthNames", new String[]{"gener", "febrer", "març", "abril", "maig", "juny", "juliol", "agost", "setembre", "octubre", "novembre", "desembre", ""}}, new Object[]{"MonthAbbreviations", new String[]{"de gen.", "de febr.", "de març", "d’abr.", "de maig", "de juny", "de jul.", "d’ag.", "de set.", "d’oct.", "de nov.", "de des.", ""}}, new Object[]{"standalone.MonthAbbreviations", new String[]{"gen.", "feb.", "març", "abr.", "maig", "juny", "jul.", "ag.", "set.", "oct.", "nov.", "des.", ""}}, new Object[]{"standalone.MonthNarrows", new String[]{PdfOps.g_TOKEN, PdfOps.f_TOKEN, PdfOps.m_TOKEN, "a", PdfOps.m_TOKEN, PdfOps.j_TOKEN, PdfOps.j_TOKEN, "a", PdfOps.s_TOKEN, "o", PdfOps.n_TOKEN, PdfOps.d_TOKEN, ""}}, new Object[]{"DayNames", new String[]{"diumenge", "dilluns", "dimarts", "dimecres", "dijous", "divendres", "dissabte"}}, new Object[]{"standalone.DayNames", new String[]{"Diumenge", "Dilluns", "Dimarts", "Dimecres", "Dijous", "Divendres", "Dissabte"}}, new Object[]{"DayAbbreviations", new String[]{"dg.", "dl.", "dt.", "dc.", "dj.", "dv.", "ds."}}, new Object[]{"standalone.DayAbbreviations", new String[]{"dg", "dl", "dt", "dc", "dj", "dv", "ds"}}, new Object[]{"DayNarrows", new String[]{"G", "L", "T", "C", "J", "V", PdfOps.S_TOKEN}}, new Object[]{"standalone.DayNarrows", new String[]{PdfOps.g_TOKEN, PdfOps.l_TOKEN, "t", PdfOps.c_TOKEN, PdfOps.j_TOKEN, PdfOps.v_TOKEN, PdfOps.s_TOKEN}}, new Object[]{"short.Eras", new String[]{"aC", "dC"}}, new Object[]{"NumberElements", new String[]{",", ".", ";", FXMLLoader.RESOURCE_KEY_PREFIX, "0", FXMLLoader.CONTROLLER_METHOD_PREFIX, LanguageTag.SEP, "E", "‰", "∞", "�"}}, new Object[]{"TimePatterns", new String[]{"HH:mm:ss z", "HH:mm:ss z", "HH:mm:ss", "HH:mm"}}, new Object[]{"DatePatterns", new String[]{"EEEE, d' / 'MMMM' / 'yyyy", "d' / 'MMMM' / 'yyyy", "dd/MM/yyyy", "dd/MM/yy"}}, new Object[]{"DateTimePatterns", new String[]{"{1} {0}"}}, new Object[]{"DateTimePatternChars", "GuMtkHmsSEDFwWahKzZ"}};
    }
}
