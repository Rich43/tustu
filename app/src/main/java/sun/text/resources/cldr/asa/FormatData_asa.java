package sun.text.resources.cldr.asa;

import com.sun.glass.ui.Platform;
import java.util.ListResourceBundle;
import org.icepdf.core.util.PdfOps;

/* loaded from: cldrdata.jar:sun/text/resources/cldr/asa/FormatData_asa.class */
public class FormatData_asa extends ListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // java.util.ListResourceBundle
    protected final Object[][] getContents() {
        return new Object[]{new Object[]{"MonthNames", new String[]{"Januari", "Februari", "Machi", "Aprili", "Mei", "Juni", "Julai", "Agosti", "Septemba", "Oktoba", "Novemba", "Desemba", ""}}, new Object[]{"MonthAbbreviations", new String[]{"Jan", "Feb", Platform.MAC, "Apr", "Mei", "Jun", "Jul", "Ago", "Sep", "Okt", "Nov", "Dec", ""}}, new Object[]{"MonthNarrows", new String[]{"J", PdfOps.F_TOKEN, PdfOps.M_TOKEN, "A", PdfOps.M_TOKEN, "J", "J", "A", PdfOps.S_TOKEN, "O", "N", PdfOps.D_TOKEN, ""}}, new Object[]{"DayNames", new String[]{"Jumapili", "Jumatatu", "Jumanne", "Jumatano", "Alhamisi", "Ijumaa", "Jumamosi"}}, new Object[]{"DayAbbreviations", new String[]{"Jpi", "Jtt", "Jnn", "Jtn", "Alh", "Ijm", "Jmo"}}, new Object[]{"DayNarrows", new String[]{"J", "J", "J", "J", "A", "I", "J"}}, new Object[]{"QuarterNames", new String[]{"Robo 1", "Robo 2", "Robo 3", "Robo 4"}}, new Object[]{"QuarterAbbreviations", new String[]{"R1", "R2", "R3", "R4"}}, new Object[]{"AmPmMarkers", new String[]{"icheheavo", "ichamthi"}}, new Object[]{"long.Eras", new String[]{"Kabla yakwe Yethu", "Baada yakwe Yethu"}}, new Object[]{"Eras", new String[]{"KM", "BM"}}, new Object[]{"field.era", "Edhi"}, new Object[]{"field.year", "Mwaka"}, new Object[]{"field.month", "Mweji"}, new Object[]{"field.week", "Ndisha"}, new Object[]{"field.weekday", "Thiku ya ndisha"}, new Object[]{"field.dayperiod", "Marango athiku"}, new Object[]{"field.hour", "Thaa"}, new Object[]{"field.minute", "Dakika"}, new Object[]{"field.second", "Thekunde"}, new Object[]{"field.zone", "Majira Athaa"}, new Object[]{"TimePatterns", new String[]{"h:mm:ss a zzzz", "h:mm:ss a z", "h:mm:ss a", "h:mm a"}}, new Object[]{"DatePatterns", new String[]{"EEEE, d MMMM y", "d MMMM y", "d MMM y", "dd/MM/yyyy"}}, new Object[]{"NumberPatterns", new String[]{"#,##0.###", "#,##0.00 ¤", "#,##0%"}}};
    }
}
