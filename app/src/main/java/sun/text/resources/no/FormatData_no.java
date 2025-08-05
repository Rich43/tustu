package sun.text.resources.no;

import javafx.fxml.FXMLLoader;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;
import sun.util.resources.ParallelListResourceBundle;

/* loaded from: localedata.jar:sun/text/resources/no/FormatData_no.class */
public class FormatData_no extends ParallelListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // sun.util.resources.ParallelListResourceBundle
    protected final Object[][] getContents() {
        return new Object[]{new Object[]{"MonthNames", new String[]{"januar", "februar", "mars", "april", "mai", "juni", "juli", "august", "september", "oktober", "november", "desember", ""}}, new Object[]{"standalone.MonthNames", new String[]{"januar", "februar", "mars", "april", "mai", "juni", "juli", "august", "september", "oktober", "november", "desember", ""}}, new Object[]{"MonthAbbreviations", new String[]{"jan", "feb", "mar", "apr", "mai", "jun", "jul", "aug", "sep", "okt", "nov", "des", ""}}, new Object[]{"standalone.MonthAbbreviations", new String[]{"jan", "feb", "mar", "apr", "mai", "jun", "jul", "aug", "sep", "okt", "nov", "des", ""}}, new Object[]{"MonthNarrows", new String[]{"J", PdfOps.F_TOKEN, PdfOps.M_TOKEN, "A", PdfOps.M_TOKEN, "J", "J", "A", PdfOps.S_TOKEN, "O", "N", PdfOps.D_TOKEN, ""}}, new Object[]{"standalone.MonthNarrows", new String[]{"J", PdfOps.F_TOKEN, PdfOps.M_TOKEN, "A", PdfOps.M_TOKEN, "J", "J", "A", PdfOps.S_TOKEN, "O", "N", PdfOps.D_TOKEN, ""}}, new Object[]{"DayNames", new String[]{"søndag", "mandag", "tirsdag", "onsdag", "torsdag", "fredag", "lørdag"}}, new Object[]{"standalone.DayNames", new String[]{"søndag", "mandag", "tirsdag", "onsdag", "torsdag", "fredag", "lørdag"}}, new Object[]{"DayAbbreviations", new String[]{"sø", "ma", "ti", FXMLLoader.EVENT_HANDLER_PREFIX, "to", "fr", "lø"}}, new Object[]{"standalone.DayAbbreviations", new String[]{"sø.", "ma.", "ti.", "on.", "to.", "fr.", "lø."}}, new Object[]{"DayNarrows", new String[]{PdfOps.S_TOKEN, PdfOps.M_TOKEN, "T", "O", "T", PdfOps.F_TOKEN, "L"}}, new Object[]{"standalone.DayNarrows", new String[]{PdfOps.S_TOKEN, PdfOps.M_TOKEN, "T", "O", "T", PdfOps.F_TOKEN, "L"}}, new Object[]{"NumberElements", new String[]{",", " ", ";", FXMLLoader.RESOURCE_KEY_PREFIX, "0", FXMLLoader.CONTROLLER_METHOD_PREFIX, LanguageTag.SEP, "E", "‰", "∞", "�"}}, new Object[]{"TimePatterns", new String[]{"'kl 'HH.mm z", "HH:mm:ss z", "HH:mm:ss", "HH:mm"}}, new Object[]{"DatePatterns", new String[]{"d. MMMM yyyy", "d. MMMM yyyy", "dd.MMM.yyyy", "dd.MM.yy"}}, new Object[]{"DateTimePatterns", new String[]{"{1} {0}"}}, new Object[]{"DateTimePatternChars", "GyMdkHmsSEDFwWahKzZ"}};
    }
}
