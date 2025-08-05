package sun.text.resources.cldr.swc;

import java.util.ListResourceBundle;
import javafx.fxml.FXMLLoader;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;

/* loaded from: cldrdata.jar:sun/text/resources/cldr/swc/FormatData_swc.class */
public class FormatData_swc extends ListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // java.util.ListResourceBundle
    protected final Object[][] getContents() {
        return new Object[]{new Object[]{"MonthNames", new String[]{"mwezi ya kwanja", "mwezi ya pili", "mwezi ya tatu", "mwezi ya ine", "mwezi ya tanu", "mwezi ya sita", "mwezi ya saba", "mwezi ya munane", "mwezi ya tisa", "mwezi ya kumi", "mwezi ya kumi na moya", "mwezi ya kumi ya mbili", ""}}, new Object[]{"MonthAbbreviations", new String[]{"mkw", "mpi", "mtu", "min", "mtn", "mst", "msb", "mun", "mts", "mku", "mkm", "mkb", ""}}, new Object[]{"MonthNarrows", new String[]{PdfOps.k_TOKEN, "p", "t", PdfOps.i_TOKEN, "t", PdfOps.s_TOKEN, PdfOps.s_TOKEN, PdfOps.m_TOKEN, "t", PdfOps.k_TOKEN, PdfOps.m_TOKEN, PdfOps.m_TOKEN, ""}}, new Object[]{"DayNames", new String[]{"siku ya yenga", "siku ya kwanza", "siku ya pili", "siku ya tatu", "siku ya ine", "siku ya tanu", "siku ya sita"}}, new Object[]{"DayAbbreviations", new String[]{"yen", "kwa", "pil", "tat", "ine", "tan", "sit"}}, new Object[]{"DayNarrows", new String[]{PdfOps.y_TOKEN, PdfOps.k_TOKEN, "p", "t", PdfOps.i_TOKEN, "t", PdfOps.s_TOKEN}}, new Object[]{"QuarterNames", new String[]{"Robo 1", "Robo 2", "Robo 3", "Robo 4"}}, new Object[]{"QuarterAbbreviations", new String[]{"R1", "R2", "R3", "R4"}}, new Object[]{"AmPmMarkers", new String[]{"ya asubuyi", "ya muchana"}}, new Object[]{"long.Eras", new String[]{"mbele ya Yezu Kristo", "kisha ya Yezu Kristo"}}, new Object[]{"Eras", new String[]{"mbele ya Y", "kisha ya Y"}}, new Object[]{"field.era", "Wakati"}, new Object[]{"field.year", "Mwaka"}, new Object[]{"field.month", "Mwezi"}, new Object[]{"field.week", "Juma"}, new Object[]{"field.weekday", "Siku ya juma"}, new Object[]{"field.dayperiod", "Muda wa siku"}, new Object[]{"field.hour", "Saa"}, new Object[]{"field.minute", "Dakika"}, new Object[]{"field.second", "Sekunde"}, new Object[]{"field.zone", "Majira ya saa"}, new Object[]{"TimePatterns", new String[]{"HH:mm:ss zzzz", "HH:mm:ss z", "HH:mm:ss", "HH:mm"}}, new Object[]{"DatePatterns", new String[]{"EEEE d MMMM y", "d MMMM y", "d MMM y", "d/M/yyyy"}}, new Object[]{"DefaultNumberingSystem", "latn"}, new Object[]{"latn.NumberElements", new String[]{",", ".", ";", FXMLLoader.RESOURCE_KEY_PREFIX, "0", FXMLLoader.CONTROLLER_METHOD_PREFIX, LanguageTag.SEP, "E", "‰", "∞", "NaN"}}, new Object[]{"NumberPatterns", new String[]{"#,##0.###", "¤#,##0.00", "#,##0%"}}};
    }
}
