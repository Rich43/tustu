package sun.text.resources.ro;

import javafx.fxml.FXMLLoader;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;
import sun.util.resources.ParallelListResourceBundle;

/* loaded from: localedata.jar:sun/text/resources/ro/FormatData_ro.class */
public class FormatData_ro extends ParallelListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // sun.util.resources.ParallelListResourceBundle
    protected final Object[][] getContents() {
        return new Object[]{new Object[]{"MonthNames", new String[]{"ianuarie", "februarie", "martie", "aprilie", "mai", "iunie", "iulie", "august", "septembrie", "octombrie", "noiembrie", "decembrie", ""}}, new Object[]{"standalone.MonthNames", new String[]{"ianuarie", "februarie", "martie", "aprilie", "mai", "iunie", "iulie", "august", "septembrie", "octombrie", "noiembrie", "decembrie", ""}}, new Object[]{"MonthAbbreviations", new String[]{"Ian", "Feb", "Mar", "Apr", "Mai", "Iun", "Iul", "Aug", "Sep", "Oct", "Nov", "Dec", ""}}, new Object[]{"standalone.MonthAbbreviations", new String[]{"ian.", "feb.", "mar.", "apr.", "mai", "iun.", "iul.", "aug.", "sept.", "oct.", "nov.", "dec.", ""}}, new Object[]{"MonthNarrows", new String[]{"I", PdfOps.F_TOKEN, PdfOps.M_TOKEN, "A", PdfOps.M_TOKEN, "I", "I", "A", PdfOps.S_TOKEN, "O", "N", PdfOps.D_TOKEN, ""}}, new Object[]{"standalone.MonthNarrows", new String[]{"I", PdfOps.F_TOKEN, PdfOps.M_TOKEN, "A", PdfOps.M_TOKEN, "I", "I", "A", PdfOps.S_TOKEN, "O", "N", PdfOps.D_TOKEN, ""}}, new Object[]{"DayNames", new String[]{"duminică", "luni", "marţi", "miercuri", "joi", "vineri", "sâmbătă"}}, new Object[]{"standalone.DayNames", new String[]{"duminică", "luni", "marți", "miercuri", "joi", "vineri", "sâmbătă"}}, new Object[]{"DayAbbreviations", new String[]{PdfOps.D_TOKEN, "L", "Ma", "Mi", "J", "V", PdfOps.S_TOKEN}}, new Object[]{"standalone.DayAbbreviations", new String[]{"Du", "Lu", "Ma", "Mi", "Jo", "Vi", "Sâ"}}, new Object[]{"DayNarrows", new String[]{PdfOps.D_TOKEN, "L", PdfOps.M_TOKEN, PdfOps.M_TOKEN, "J", "V", PdfOps.S_TOKEN}}, new Object[]{"standalone.DayNarrows", new String[]{PdfOps.D_TOKEN, "L", PdfOps.M_TOKEN, PdfOps.M_TOKEN, "J", "V", PdfOps.S_TOKEN}}, new Object[]{"Eras", new String[]{"d.C.", "î.d.C."}}, new Object[]{"NumberElements", new String[]{",", ".", ";", FXMLLoader.RESOURCE_KEY_PREFIX, "0", FXMLLoader.CONTROLLER_METHOD_PREFIX, LanguageTag.SEP, "E", "‰", "∞", "�"}}, new Object[]{"TimePatterns", new String[]{"HH:mm:ss z", "HH:mm:ss z", "HH:mm:ss", "HH:mm"}}, new Object[]{"DatePatterns", new String[]{"dd MMMM yyyy", "dd MMMM yyyy", "dd.MM.yyyy", "dd.MM.yyyy"}}, new Object[]{"DateTimePatterns", new String[]{"{1} {0}"}}, new Object[]{"DateTimePatternChars", "GanjkHmsSEDFwWxhKzZ"}};
    }
}
