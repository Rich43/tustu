package sun.text.resources.mt;

import javafx.fxml.FXMLLoader;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;
import sun.util.resources.ParallelListResourceBundle;

/* loaded from: localedata.jar:sun/text/resources/mt/FormatData_mt.class */
public class FormatData_mt extends ParallelListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // sun.util.resources.ParallelListResourceBundle
    protected final Object[][] getContents() {
        return new Object[]{new Object[]{"MonthNames", new String[]{"Jannar", "Frar", "Marzu", "April", "Mejju", "Ġunju", "Lulju", "Awwissu", "Settembru", "Ottubru", "Novembru", "Diċembru", ""}}, new Object[]{"MonthAbbreviations", new String[]{"Jan", "Fra", "Mar", "Apr", "Mej", "Ġun", "Lul", "Aww", "Set", "Ott", "Nov", "Diċ", ""}}, new Object[]{"MonthNarrows", new String[]{"J", PdfOps.F_TOKEN, PdfOps.M_TOKEN, "A", PdfOps.M_TOKEN, "Ġ", "L", "A", PdfOps.S_TOKEN, "O", "N", PdfOps.D_TOKEN, ""}}, new Object[]{"DayNames", new String[]{"Il-Ħadd", "It-Tnejn", "It-Tlieta", "L-Erbgħa", "Il-Ħamis", "Il-Ġimgħa", "Is-Sibt"}}, new Object[]{"DayAbbreviations", new String[]{"Ħad", "Tne", "Tli", "Erb", "Ħam", "Ġim", "Sib"}}, new Object[]{"DayNarrows", new String[]{"Ħ", "T", "T", "E", "Ħ", "Ġ", PdfOps.S_TOKEN}}, new Object[]{"AmPmMarkers", new String[]{"QN", "WN"}}, new Object[]{"Eras", new String[]{"QK", "WK"}}, new Object[]{"NumberPatterns", new String[]{"#,##0.###", "¤ #,##0.00", "#,##0%"}}, new Object[]{"NumberElements", new String[]{".", ",", ";", FXMLLoader.RESOURCE_KEY_PREFIX, "0", FXMLLoader.CONTROLLER_METHOD_PREFIX, LanguageTag.SEP, "E", "‰", "∞", "NaN"}}, new Object[]{"TimePatterns", new String[]{"HH:mm:ss z", "HH:mm:ss z", "HH:mm:ss", "HH:mm"}}, new Object[]{"DatePatterns", new String[]{"EEEE, d 'ta’' MMMM yyyy", "d 'ta’' MMMM yyyy", "dd MMM yyyy", "dd/MM/yyyy"}}, new Object[]{"DateTimePatterns", new String[]{"{1} {0}"}}};
    }
}
