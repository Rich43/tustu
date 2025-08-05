package sun.text.resources.cldr.dyo;

import java.util.ListResourceBundle;
import javafx.fxml.FXMLLoader;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;

/* loaded from: cldrdata.jar:sun/text/resources/cldr/dyo/FormatData_dyo.class */
public class FormatData_dyo extends ListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // java.util.ListResourceBundle
    protected final Object[][] getContents() {
        return new Object[]{new Object[]{"MonthNames", new String[]{"Sanvie", "Fébirie", "Mars", "Aburil", "Mee", "Sueŋ", "Súuyee", "Ut", "Settembar", "Oktobar", "Novembar", "Disambar", ""}}, new Object[]{"MonthAbbreviations", new String[]{"Sa", "Fe", "Ma", "Ab", "Me", "Su", "Sú", "Ut", "Se", "Ok", "No", "De", ""}}, new Object[]{"MonthNarrows", new String[]{PdfOps.S_TOKEN, PdfOps.F_TOKEN, PdfOps.M_TOKEN, "A", PdfOps.M_TOKEN, PdfOps.S_TOKEN, PdfOps.S_TOKEN, "U", PdfOps.S_TOKEN, "O", "N", PdfOps.D_TOKEN, ""}}, new Object[]{"DayNames", new String[]{"Dimas", "Teneŋ", "Talata", "Alarbay", "Aramisay", "Arjuma", "Sibiti"}}, new Object[]{"DayAbbreviations", new String[]{"Dim", "Ten", "Tal", "Ala", "Ara", "Arj", "Sib"}}, new Object[]{"DayNarrows", new String[]{PdfOps.D_TOKEN, "T", "T", "A", "A", "A", PdfOps.S_TOKEN}}, new Object[]{"long.Eras", new String[]{"Ariŋuu Yeesu", "Atooŋe Yeesu"}}, new Object[]{"Eras", new String[]{"ArY", "AtY"}}, new Object[]{"field.era", "Jamanay"}, new Object[]{"field.year", "Emit"}, new Object[]{"field.month", "Fuleeŋ"}, new Object[]{"field.week", "Lóokuŋ"}, new Object[]{"field.weekday", "Funak"}, new Object[]{"field.dayperiod", "Bujom / Kalíim"}, new Object[]{"TimePatterns", new String[]{"HH:mm:ss zzzz", "HH:mm:ss z", "HH:mm:ss", "HH:mm"}}, new Object[]{"DatePatterns", new String[]{"EEEE d MMMM y", "d MMMM y", "d MMM y", "d/M/yyyy"}}, new Object[]{"DefaultNumberingSystem", "latn"}, new Object[]{"latn.NumberElements", new String[]{",", " ", ";", FXMLLoader.RESOURCE_KEY_PREFIX, "0", FXMLLoader.CONTROLLER_METHOD_PREFIX, LanguageTag.SEP, "E", "‰", "∞", "NaN"}}, new Object[]{"NumberPatterns", new String[]{"#,##0.###", "#,##0.00 ¤", "#,##0%"}}};
    }
}
