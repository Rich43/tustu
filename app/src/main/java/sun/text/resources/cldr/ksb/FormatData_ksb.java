package sun.text.resources.cldr.ksb;

import com.sun.glass.ui.Platform;
import java.util.ListResourceBundle;
import org.icepdf.core.util.PdfOps;

/* loaded from: cldrdata.jar:sun/text/resources/cldr/ksb/FormatData_ksb.class */
public class FormatData_ksb extends ListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // java.util.ListResourceBundle
    protected final Object[][] getContents() {
        return new Object[]{new Object[]{"MonthNames", new String[]{"Januali", "Febluali", "Machi", "Aplili", "Mei", "Juni", "Julai", "Agosti", "Septemba", "Oktoba", "Novemba", "Desemba", ""}}, new Object[]{"MonthAbbreviations", new String[]{"Jan", "Feb", Platform.MAC, "Apr", "Mei", "Jun", "Jul", "Ago", "Sep", "Okt", "Nov", "Des", ""}}, new Object[]{"MonthNarrows", new String[]{"J", PdfOps.F_TOKEN, PdfOps.M_TOKEN, "A", PdfOps.M_TOKEN, "J", "J", "A", PdfOps.S_TOKEN, "O", "N", PdfOps.D_TOKEN, ""}}, new Object[]{"DayNames", new String[]{"Jumaapii", "Jumaatatu", "Jumaane", "Jumaatano", "Alhamisi", "Ijumaa", "Jumaamosi"}}, new Object[]{"DayAbbreviations", new String[]{"Jpi", "Jtt", "Jmn", "Jtn", "Alh", "Iju", "Jmo"}}, new Object[]{"DayNarrows", new String[]{"2", "3", "4", "5", "A", "I", "1"}}, new Object[]{"QuarterNames", new String[]{"Lobo ya bosi", "Lobo ya mbii", "Lobo ya nnd'atu", "Lobo ya nne"}}, new Object[]{"QuarterAbbreviations", new String[]{"L1", "L2", "L3", "L4"}}, new Object[]{"AmPmMarkers", new String[]{"makeo", "nyiaghuo"}}, new Object[]{"long.Eras", new String[]{"Kabla ya Klisto", "Baada ya Klisto"}}, new Object[]{"Eras", new String[]{"KK", "BK"}}, new Object[]{"field.era", "Mishi"}, new Object[]{"field.year", "Ng'waka"}, new Object[]{"field.month", "Ng'ezi"}, new Object[]{"field.week", "Niki"}, new Object[]{"field.weekday", "Mwesiku za wiki"}, new Object[]{"field.dayperiod", "Namshii"}, new Object[]{"field.hour", "Saa"}, new Object[]{"field.minute", "Dakika"}, new Object[]{"field.second", "Sekunde"}, new Object[]{"field.zone", "Majila"}, new Object[]{"TimePatterns", new String[]{"h:mm:ss a zzzz", "h:mm:ss a z", "h:mm:ss a", "h:mm a"}}, new Object[]{"DatePatterns", new String[]{"EEEE, d MMMM y", "d MMMM y", "d MMM y", "dd/MM/yyyy"}}, new Object[]{"NumberPatterns", new String[]{"#,##0.###", "#,##0.00¤", "#,##0%"}}};
    }
}
