package sun.text.resources.cldr.ga;

import java.util.ListResourceBundle;
import javafx.fxml.FXMLLoader;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;

/* loaded from: cldrdata.jar:sun/text/resources/cldr/ga/FormatData_ga.class */
public class FormatData_ga extends ListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // java.util.ListResourceBundle
    protected final Object[][] getContents() {
        return new Object[]{new Object[]{"MonthNames", new String[]{"Eanáir", "Feabhra", "Márta", "Aibreán", "Bealtaine", "Meitheamh", "Iúil", "Lúnasa", "Meán Fómhair", "Deireadh Fómhair", "Samhain", "Nollaig", ""}}, new Object[]{"MonthAbbreviations", new String[]{"Ean", "Feabh", "Márta", "Aib", "Beal", "Meith", "Iúil", "Lún", "MFómh", "DFómh", "Samh", "Noll", ""}}, new Object[]{"MonthNarrows", new String[]{"E", PdfOps.F_TOKEN, PdfOps.M_TOKEN, "A", PdfOps.B_TOKEN, PdfOps.M_TOKEN, "I", "L", PdfOps.M_TOKEN, PdfOps.D_TOKEN, PdfOps.S_TOKEN, "N", ""}}, new Object[]{"DayNames", new String[]{"Dé Domhnaigh", "Dé Luain", "Dé Máirt", "Dé Céadaoin", "Déardaoin", "Dé hAoine", "Dé Sathairn"}}, new Object[]{"DayAbbreviations", new String[]{"Domh", "Luan", "Máirt", "Céad", "Déar", "Aoine", "Sath"}}, new Object[]{"QuarterNames", new String[]{"1ú ráithe", "2ú ráithe", "3ú ráithe", "4ú ráithe"}}, new Object[]{"QuarterAbbreviations", new String[]{"R1", "R2", "R3", "R4"}}, new Object[]{"AmPmMarkers", new String[]{"a.m.", "p.m."}}, new Object[]{"Eras", new String[]{"RC", "AD"}}, new Object[]{"DatePatterns", new String[]{"EEEE d MMMM y", "d MMMM y", "d MMM y", "dd/MM/yyyy"}}, new Object[]{"DefaultNumberingSystem", "latn"}, new Object[]{"latn.NumberElements", new String[]{".", ",", ";", FXMLLoader.RESOURCE_KEY_PREFIX, "0", FXMLLoader.CONTROLLER_METHOD_PREFIX, LanguageTag.SEP, "E", "‰", "∞", "NaN"}}, new Object[]{"NumberPatterns", new String[]{"#,##0.###", "¤#,##0.00", "#,##0%"}}};
    }
}
