package sun.text.resources.it;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import javafx.fxml.FXMLLoader;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;
import sun.util.resources.ParallelListResourceBundle;

/* loaded from: localedata.jar:sun/text/resources/it/FormatData_it.class */
public class FormatData_it extends ParallelListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // sun.util.resources.ParallelListResourceBundle
    protected final Object[][] getContents() {
        return new Object[]{new Object[]{"MonthNames", new String[]{"gennaio", "febbraio", "marzo", "aprile", "maggio", "giugno", "luglio", "agosto", "settembre", "ottobre", "novembre", "dicembre", ""}}, new Object[]{"standalone.MonthNames", new String[]{"Gennaio", "Febbraio", "Marzo", "Aprile", "Maggio", "Giugno", "Luglio", "Agosto", "Settembre", "Ottobre", "Novembre", "Dicembre", ""}}, new Object[]{"MonthAbbreviations", new String[]{"gen", "feb", "mar", "apr", "mag", "giu", "lug", "ago", "set", "ott", "nov", "dic", ""}}, new Object[]{"MonthNarrows", new String[]{"G", PdfOps.F_TOKEN, PdfOps.M_TOKEN, "A", PdfOps.M_TOKEN, "G", "L", "A", PdfOps.S_TOKEN, "O", "N", PdfOps.D_TOKEN, ""}}, new Object[]{"standalone.MonthNarrows", new String[]{"G", PdfOps.F_TOKEN, PdfOps.M_TOKEN, "A", PdfOps.M_TOKEN, "G", "L", "A", PdfOps.S_TOKEN, "O", "N", PdfOps.D_TOKEN, ""}}, new Object[]{"DayNames", new String[]{"domenica", "lunedì", "martedì", "mercoledì", "giovedì", "venerdì", "sabato"}}, new Object[]{"standalone.DayNames", new String[]{"Domenica", "Lunedì", "Martedì", "Mercoledì", "Giovedì", "Venerdì", "Sabato"}}, new Object[]{"DayAbbreviations", new String[]{Constants.DOM_PNAME, "lun", "mar", "mer", "gio", "ven", "sab"}}, new Object[]{"DayNarrows", new String[]{PdfOps.D_TOKEN, "L", PdfOps.M_TOKEN, PdfOps.M_TOKEN, "G", "V", PdfOps.S_TOKEN}}, new Object[]{"Eras", new String[]{"BC", "dopo Cristo"}}, new Object[]{"short.Eras", new String[]{"aC", "dC"}}, new Object[]{"NumberElements", new String[]{",", ".", ";", FXMLLoader.RESOURCE_KEY_PREFIX, "0", FXMLLoader.CONTROLLER_METHOD_PREFIX, LanguageTag.SEP, "E", "‰", "∞", "�"}}, new Object[]{"TimePatterns", new String[]{"H.mm.ss z", "H.mm.ss z", "H.mm.ss", "H.mm"}}, new Object[]{"DatePatterns", new String[]{"EEEE d MMMM yyyy", "d MMMM yyyy", "d-MMM-yyyy", "dd/MM/yy"}}, new Object[]{"DateTimePatterns", new String[]{"{1} {0}"}}, new Object[]{"DateTimePatternChars", "GyMdkHmsSEDFwWahKzZ"}};
    }
}
