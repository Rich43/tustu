package sun.text.resources.sv;

import javafx.fxml.FXMLLoader;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;
import sun.util.resources.ParallelListResourceBundle;

/* loaded from: localedata.jar:sun/text/resources/sv/FormatData_sv.class */
public class FormatData_sv extends ParallelListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v3, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // sun.util.resources.ParallelListResourceBundle
    protected final Object[][] getContents() {
        String[] strArr = {"före R.K.", "R.K."};
        return new Object[]{new Object[]{"MonthNames", new String[]{"januari", "februari", "mars", "april", "maj", "juni", "juli", "augusti", "september", "oktober", "november", "december", ""}}, new Object[]{"MonthNarrows", new String[]{"J", PdfOps.F_TOKEN, PdfOps.M_TOKEN, "A", PdfOps.M_TOKEN, "J", "J", "A", PdfOps.S_TOKEN, "O", "N", PdfOps.D_TOKEN, ""}}, new Object[]{"MonthAbbreviations", new String[]{"jan", "feb", "mar", "apr", "maj", "jun", "jul", "aug", "sep", "okt", "nov", "dec", ""}}, new Object[]{"standalone.MonthAbbreviations", new String[]{"jan", "feb", "mar", "apr", "maj", "jun", "jul", "aug", "sep", "okt", "nov", "dec", ""}}, new Object[]{"standalone.MonthNarrows", new String[]{"J", PdfOps.F_TOKEN, PdfOps.M_TOKEN, "A", PdfOps.M_TOKEN, "J", "J", "A", PdfOps.S_TOKEN, "O", "N", PdfOps.D_TOKEN, ""}}, new Object[]{"DayNames", new String[]{"söndag", "måndag", "tisdag", "onsdag", "torsdag", "fredag", "lördag"}}, new Object[]{"DayAbbreviations", new String[]{"sö", "må", "ti", FXMLLoader.EVENT_HANDLER_PREFIX, "to", "fr", "lö"}}, new Object[]{"standalone.DayAbbreviations", new String[]{"sön", "mån", "tis", "ons", "tor", "fre", "lör"}}, new Object[]{"DayNarrows", new String[]{PdfOps.S_TOKEN, PdfOps.M_TOKEN, "T", "O", "T", PdfOps.F_TOKEN, "L"}}, new Object[]{"standalone.DayNames", new String[]{"söndag", "måndag", "tisdag", "onsdag", "torsdag", "fredag", "lördag"}}, new Object[]{"standalone.DayNarrows", new String[]{PdfOps.S_TOKEN, PdfOps.M_TOKEN, "T", "O", "T", PdfOps.F_TOKEN, "L"}}, new Object[]{"Eras", new String[]{"före Kristus", "efter Kristus"}}, new Object[]{"short.Eras", new String[]{"f.Kr.", "e.Kr."}}, new Object[]{"narrow.Eras", new String[]{"f.Kr.", "e.Kr."}}, new Object[]{"AmPmMarkers", new String[]{"fm", "em"}}, new Object[]{"narrow.AmPmMarkers", new String[]{PdfOps.f_TOKEN, "e"}}, new Object[]{"NumberElements", new String[]{",", " ", ";", FXMLLoader.RESOURCE_KEY_PREFIX, "0", FXMLLoader.CONTROLLER_METHOD_PREFIX, LanguageTag.SEP, "E", "‰", "∞", "�"}}, new Object[]{"TimePatterns", new String[]{"'kl 'H:mm z", "HH:mm:ss z", "HH:mm:ss", "HH:mm"}}, new Object[]{"DatePatterns", new String[]{"'den 'd MMMM yyyy", "'den 'd MMMM yyyy", "yyyy-MMM-dd", "yyyy-MM-dd"}}, new Object[]{"DateTimePatterns", new String[]{"{1} {0}"}}, new Object[]{"DateTimePatternChars", "GyMdkHmsSEDFwWahKzZ"}};
    }
}
