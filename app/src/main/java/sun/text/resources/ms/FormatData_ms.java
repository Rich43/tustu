package sun.text.resources.ms;

import com.sun.glass.ui.Platform;
import javafx.fxml.FXMLLoader;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;
import sun.util.resources.ParallelListResourceBundle;

/* loaded from: localedata.jar:sun/text/resources/ms/FormatData_ms.class */
public class FormatData_ms extends ParallelListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // sun.util.resources.ParallelListResourceBundle
    protected final Object[][] getContents() {
        return new Object[]{new Object[]{"MonthNames", new String[]{"Januari", "Februari", Platform.MAC, "April", "Mei", "Jun", "Julai", "Ogos", "September", "Oktober", "November", "Disember", ""}}, new Object[]{"MonthAbbreviations", new String[]{"Jan", "Feb", Platform.MAC, "Apr", "Mei", "Jun", "Jul", "Ogos", "Sep", "Okt", "Nov", "Dis", ""}}, new Object[]{"MonthNarrows", new String[]{"J", PdfOps.F_TOKEN, PdfOps.M_TOKEN, "A", PdfOps.M_TOKEN, "J", "J", "O", PdfOps.S_TOKEN, "O", "N", PdfOps.D_TOKEN, ""}}, new Object[]{"MonthNarrows", new String[]{"J", PdfOps.F_TOKEN, PdfOps.M_TOKEN, "A", PdfOps.M_TOKEN, "J", "J", "O", PdfOps.S_TOKEN, "O", "N", PdfOps.D_TOKEN, ""}}, new Object[]{"standalone.MonthNarrows", new String[]{"J", PdfOps.F_TOKEN, PdfOps.M_TOKEN, "A", PdfOps.M_TOKEN, "J", "J", "O", PdfOps.S_TOKEN, "O", "N", PdfOps.D_TOKEN, ""}}, new Object[]{"DayNames", new String[]{"Ahad", "Isnin", "Selasa", "Rabu", "Khamis", "Jumaat", "Sabtu"}}, new Object[]{"DayAbbreviations", new String[]{"Ahd", "Isn", "Sel", "Rab", "Kha", "Jum", "Sab"}}, new Object[]{"DayNarrows", new String[]{"A", "I", PdfOps.S_TOKEN, "R", PdfOps.K_TOKEN, "J", PdfOps.S_TOKEN}}, new Object[]{"standalone.DayNarrows", new String[]{"A", "I", PdfOps.S_TOKEN, "R", PdfOps.K_TOKEN, "J", PdfOps.S_TOKEN}}, new Object[]{"Eras", new String[]{"BCE", "CE"}}, new Object[]{"NumberPatterns", new String[]{"#,##0.###", "¤ #,##0.00", "#,##0%"}}, new Object[]{"NumberElements", new String[]{".", ",", ";", FXMLLoader.RESOURCE_KEY_PREFIX, "0", FXMLLoader.CONTROLLER_METHOD_PREFIX, LanguageTag.SEP, "E", "‰", "∞", "NaN"}}, new Object[]{"TimePatterns", new String[]{"HH:mm:ss z", "HH:mm:ss z", "HH:mm:ss", "HH:mm"}}, new Object[]{"DatePatterns", new String[]{"EEEE, yyyy MMMM dd", "yyyy MMMM d", "yyyy MMM d", "yy/MM/dd"}}, new Object[]{"DateTimePatterns", new String[]{"{1} {0}"}}};
    }
}
