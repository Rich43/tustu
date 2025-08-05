package sun.text.resources.da;

import javafx.fxml.FXMLLoader;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;
import sun.util.resources.ParallelListResourceBundle;

/* loaded from: localedata.jar:sun/text/resources/da/FormatData_da.class */
public class FormatData_da extends ParallelListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // sun.util.resources.ParallelListResourceBundle
    protected final Object[][] getContents() {
        return new Object[]{new Object[]{"MonthNames", new String[]{"januar", "februar", "marts", "april", "maj", "juni", "juli", "august", "september", "oktober", "november", "december", ""}}, new Object[]{"MonthAbbreviations", new String[]{"jan.", "feb.", "mar.", "apr.", "maj", "jun.", "jul.", "aug.", "sep.", "okt.", "nov.", "dec.", ""}}, new Object[]{"MonthNarrows", new String[]{"J", PdfOps.F_TOKEN, PdfOps.M_TOKEN, "A", PdfOps.M_TOKEN, "J", "J", "A", PdfOps.S_TOKEN, "O", "N", PdfOps.D_TOKEN, ""}}, new Object[]{"standalone.MonthAbbreviations", new String[]{"jan", "feb", "mar", "apr", "maj", "jun", "jul", "aug", "sep", "okt", "nov", "dec", ""}}, new Object[]{"DayNames", new String[]{"søndag", "mandag", "tirsdag", "onsdag", "torsdag", "fredag", "lørdag"}}, new Object[]{"DayAbbreviations", new String[]{"sø", "ma", "ti", FXMLLoader.EVENT_HANDLER_PREFIX, "to", "fr", "lø"}}, new Object[]{"DayNarrows", new String[]{PdfOps.S_TOKEN, PdfOps.M_TOKEN, "T", "O", "T", PdfOps.F_TOKEN, "L"}}, new Object[]{"NumberElements", new String[]{",", ".", ";", FXMLLoader.RESOURCE_KEY_PREFIX, "0", FXMLLoader.CONTROLLER_METHOD_PREFIX, LanguageTag.SEP, "E", "‰", "∞", "�"}}, new Object[]{"Eras", new String[]{"f.Kr.", "e.Kr."}}, new Object[]{"short.Eras", new String[]{"f.Kr.", "e.Kr."}}, new Object[]{"TimePatterns", new String[]{"HH:mm:ss z", "HH:mm:ss z", "HH:mm:ss", "HH:mm"}}, new Object[]{"DatePatterns", new String[]{"d. MMMM yyyy", "d. MMMM yyyy", "dd-MM-yyyy", "dd-MM-yy"}}, new Object[]{"DateTimePatterns", new String[]{"{1} {0}"}}, new Object[]{"DateTimePatternChars", "GuMtkHmsSEDFwWahKzZ"}};
    }
}
