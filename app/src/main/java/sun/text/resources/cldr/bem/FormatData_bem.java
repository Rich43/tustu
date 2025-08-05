package sun.text.resources.cldr.bem;

import com.sun.glass.ui.Platform;
import java.util.ListResourceBundle;
import org.icepdf.core.util.PdfOps;

/* loaded from: cldrdata.jar:sun/text/resources/cldr/bem/FormatData_bem.class */
public class FormatData_bem extends ListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // java.util.ListResourceBundle
    protected final Object[][] getContents() {
        return new Object[]{new Object[]{"MonthNames", new String[]{"Januari", "Februari", "Machi", "Epreo", "Mei", "Juni", "Julai", "Ogasti", "Septemba", "Oktoba", "Novemba", "Disemba", ""}}, new Object[]{"MonthAbbreviations", new String[]{"Jan", "Feb", Platform.MAC, "Epr", "Mei", "Jun", "Jul", "Oga", "Sep", "Okt", "Nov", "Dis", ""}}, new Object[]{"MonthNarrows", new String[]{"J", PdfOps.F_TOKEN, PdfOps.M_TOKEN, "E", PdfOps.M_TOKEN, "J", "J", "O", PdfOps.S_TOKEN, "O", "N", PdfOps.D_TOKEN, ""}}, new Object[]{"DayNames", new String[]{"Pa Mulungu", "Palichimo", "Palichibuli", "Palichitatu", "Palichine", "Palichisano", "Pachibelushi"}}, new Object[]{"AmPmMarkers", new String[]{"uluchelo", "akasuba"}}, new Object[]{"long.Eras", new String[]{"Before Yesu", "After Yesu"}}, new Object[]{"Eras", new String[]{"BC", "AD"}}, new Object[]{"field.era", "Inkulo"}, new Object[]{"field.year", "Umwaka"}, new Object[]{"field.month", "Umweshi"}, new Object[]{"field.week", "Umulungu"}, new Object[]{"field.weekday", "Ubushiku"}, new Object[]{"field.dayperiod", "Akasuba"}, new Object[]{"field.hour", "Insa"}, new Object[]{"field.minute", "Mineti"}, new Object[]{"field.second", "Sekondi"}, new Object[]{"TimePatterns", new String[]{"h:mm:ss a zzzz", "h:mm:ss a z", "h:mm:ss a", "h:mm a"}}, new Object[]{"DatePatterns", new String[]{"EEEE, d MMMM y", "d MMMM y", "d MMM y", "dd/MM/yyyy"}}, new Object[]{"NumberPatterns", new String[]{"#,##0.###", "¤#,##0.00;(¤#,##0.00)", "#,##0%"}}};
    }
}
