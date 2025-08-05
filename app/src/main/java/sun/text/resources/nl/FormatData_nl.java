package sun.text.resources.nl;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import javafx.fxml.FXMLLoader;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;
import sun.util.resources.ParallelListResourceBundle;

/* loaded from: localedata.jar:sun/text/resources/nl/FormatData_nl.class */
public class FormatData_nl extends ParallelListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // sun.util.resources.ParallelListResourceBundle
    protected final Object[][] getContents() {
        return new Object[]{new Object[]{"MonthNames", new String[]{"januari", "februari", "maart", "april", "mei", "juni", "juli", "augustus", "september", "oktober", "november", "december", ""}}, new Object[]{"MonthAbbreviations", new String[]{"jan", "feb", "mrt", "apr", "mei", "jun", "jul", "aug", "sep", "okt", "nov", "dec", ""}}, new Object[]{"MonthNarrows", new String[]{"J", PdfOps.F_TOKEN, PdfOps.M_TOKEN, "A", PdfOps.M_TOKEN, "J", "J", "A", PdfOps.S_TOKEN, "O", "N", PdfOps.D_TOKEN, ""}}, new Object[]{"DayNames", new String[]{"zondag", "maandag", "dinsdag", "woensdag", "donderdag", "vrijdag", "zaterdag"}}, new Object[]{"DayAbbreviations", new String[]{"zo", "ma", "di", "wo", "do", "vr", "za"}}, new Object[]{"DayNarrows", new String[]{Constants.HASIDCALL_INDEX_SIG, PdfOps.M_TOKEN, PdfOps.D_TOKEN, PdfOps.W_TOKEN, PdfOps.D_TOKEN, "V", Constants.HASIDCALL_INDEX_SIG}}, new Object[]{"Eras", new String[]{"v. Chr.", "n. Chr."}}, new Object[]{"NumberElements", new String[]{",", ".", ";", FXMLLoader.RESOURCE_KEY_PREFIX, "0", FXMLLoader.CONTROLLER_METHOD_PREFIX, LanguageTag.SEP, "E", "‰", "∞", "�"}}, new Object[]{"TimePatterns", new String[]{"H:mm:ss' uur' z", "H:mm:ss z", "H:mm:ss", "H:mm"}}, new Object[]{"DatePatterns", new String[]{"EEEE d MMMM yyyy", "d MMMM yyyy", "d-MMM-yyyy", "d-M-yy"}}, new Object[]{"DateTimePatterns", new String[]{"{1} {0}"}}, new Object[]{"DateTimePatternChars", "GyMdkHmsSEDFwWahKzZ"}};
    }
}
