package sun.text.resources.cldr.kde;

import com.sun.glass.ui.Platform;
import java.util.ListResourceBundle;
import org.icepdf.core.util.PdfOps;

/* loaded from: cldrdata.jar:sun/text/resources/cldr/kde/FormatData_kde.class */
public class FormatData_kde extends ListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // java.util.ListResourceBundle
    protected final Object[][] getContents() {
        return new Object[]{new Object[]{"MonthNames", new String[]{"Mwedi Ntandi", "Mwedi wa Pili", "Mwedi wa Tatu", "Mwedi wa Nchechi", "Mwedi wa Nnyano", "Mwedi wa Nnyano na Umo", "Mwedi wa Nnyano na Mivili", "Mwedi wa Nnyano na Mitatu", "Mwedi wa Nnyano na Nchechi", "Mwedi wa Nnyano na Nnyano", "Mwedi wa Nnyano na Nnyano na U", "Mwedi wa Nnyano na Nnyano na M", ""}}, new Object[]{"MonthAbbreviations", new String[]{"Jan", "Feb", Platform.MAC, "Apr", "Mei", "Jun", "Jul", "Ago", "Sep", "Okt", "Nov", "Des", ""}}, new Object[]{"MonthNarrows", new String[]{"J", PdfOps.F_TOKEN, PdfOps.M_TOKEN, "A", PdfOps.M_TOKEN, "J", "J", "A", PdfOps.S_TOKEN, "O", "N", PdfOps.D_TOKEN, ""}}, new Object[]{"DayNames", new String[]{"Liduva lyapili", "Liduva lyatatu", "Liduva lyanchechi", "Liduva lyannyano", "Liduva lyannyano na linji", "Liduva lyannyano na mavili", "Liduva litandi"}}, new Object[]{"DayAbbreviations", new String[]{"Ll2", "Ll3", "Ll4", "Ll5", "Ll6", "Ll7", "Ll1"}}, new Object[]{"DayNarrows", new String[]{"2", "3", "4", "5", "6", "7", "1"}}, new Object[]{"QuarterNames", new String[]{"Lobo 1", "Lobo 2", "Lobo 3", "Lobo 4"}}, new Object[]{"QuarterAbbreviations", new String[]{"L1", "L2", "L3", "L4"}}, new Object[]{"AmPmMarkers", new String[]{"Muhi", "Chilo"}}, new Object[]{"long.Eras", new String[]{"Akanapawa Yesu", "Nankuida Yesu"}}, new Object[]{"Eras", new String[]{"AY", "NY"}}, new Object[]{"field.era", "Mahiku"}, new Object[]{"field.year", "Mwaka"}, new Object[]{"field.month", "Mwedi"}, new Object[]{"field.week", "Lijuma"}, new Object[]{"field.weekday", "Disiku dya lijuma"}, new Object[]{"field.dayperiod", "Muhi/Chilo"}, new Object[]{"field.hour", "Saa"}, new Object[]{"field.minute", "Dakika"}, new Object[]{"field.second", "Sekunde"}, new Object[]{"field.zone", "Npanda wa muda"}, new Object[]{"TimePatterns", new String[]{"h:mm:ss a zzzz", "h:mm:ss a z", "h:mm:ss a", "h:mm a"}}, new Object[]{"DatePatterns", new String[]{"EEEE, d MMMM y", "d MMMM y", "d MMM y", "dd/MM/yyyy"}}, new Object[]{"NumberPatterns", new String[]{"#,##0.###", "¤#,##0.00;(¤#,##0.00)", "#,##0%"}}};
    }
}
