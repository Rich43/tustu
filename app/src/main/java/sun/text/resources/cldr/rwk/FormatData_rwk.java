package sun.text.resources.cldr.rwk;

import com.sun.glass.ui.Platform;
import java.util.ListResourceBundle;
import org.icepdf.core.util.PdfOps;

/* loaded from: cldrdata.jar:sun/text/resources/cldr/rwk/FormatData_rwk.class */
public class FormatData_rwk extends ListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // java.util.ListResourceBundle
    protected final Object[][] getContents() {
        return new Object[]{new Object[]{"MonthNames", new String[]{"Januari", "Februari", "Machi", "Aprilyi", "Mei", "Junyi", "Julyai", "Agusti", "Septemba", "Oktoba", "Novemba", "Desemba", ""}}, new Object[]{"MonthAbbreviations", new String[]{"Jan", "Feb", Platform.MAC, "Apr", "Mei", "Jun", "Jul", "Ago", "Sep", "Okt", "Nov", "Des", ""}}, new Object[]{"MonthNarrows", new String[]{"J", PdfOps.F_TOKEN, PdfOps.M_TOKEN, "A", PdfOps.M_TOKEN, "J", "J", "A", PdfOps.S_TOKEN, "O", "N", PdfOps.D_TOKEN, ""}}, new Object[]{"DayNames", new String[]{"Jumapilyi", "Jumatatuu", "Jumanne", "Jumatanu", "Alhamisi", "Ijumaa", "Jumamosi"}}, new Object[]{"DayAbbreviations", new String[]{"Jpi", "Jtt", "Jnn", "Jtn", "Alh", "Iju", "Jmo"}}, new Object[]{"DayNarrows", new String[]{"J", "J", "J", "J", "A", "I", "J"}}, new Object[]{"QuarterNames", new String[]{"Robo 1", "Robo 2", "Robo 3", "Robo 4"}}, new Object[]{"QuarterAbbreviations", new String[]{"R1", "R2", "R3", "R4"}}, new Object[]{"AmPmMarkers", new String[]{"utuko", "kyiukonyi"}}, new Object[]{"long.Eras", new String[]{"Kabla ya Kristu", "Baada ya Kristu"}}, new Object[]{"Eras", new String[]{"KK", "BK"}}, new Object[]{"field.era", "Kacha"}, new Object[]{"field.year", "Maka"}, new Object[]{"field.month", "Mori"}, new Object[]{"field.week", "Wiikyi"}, new Object[]{"field.weekday", "Mfiri"}, new Object[]{"field.dayperiod", "Mfiri o siku"}, new Object[]{"field.hour", "Saa"}, new Object[]{"field.minute", "Dakyika"}, new Object[]{"field.second", "Sekunde"}, new Object[]{"field.zone", "Mfiri o saa"}, new Object[]{"TimePatterns", new String[]{"h:mm:ss a zzzz", "h:mm:ss a z", "h:mm:ss a", "h:mm a"}}, new Object[]{"DatePatterns", new String[]{"EEEE, d MMMM y", "d MMMM y", "d MMM y", "dd/MM/yyyy"}}, new Object[]{"NumberPatterns", new String[]{"#,##0.###", "#,##0.00¤", "#,##0%"}}};
    }
}
