package sun.text.resources.es;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import javafx.fxml.FXMLLoader;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;
import sun.util.resources.ParallelListResourceBundle;

/* loaded from: localedata.jar:sun/text/resources/es/FormatData_es.class */
public class FormatData_es extends ParallelListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // sun.util.resources.ParallelListResourceBundle
    protected final Object[][] getContents() {
        return new Object[]{new Object[]{"MonthNames", new String[]{"enero", "febrero", "marzo", "abril", "mayo", "junio", "julio", "agosto", "septiembre", "octubre", "noviembre", "diciembre", ""}}, new Object[]{"MonthAbbreviations", new String[]{"ene", "feb", "mar", "abr", "may", "jun", "jul", "ago", "sep", "oct", "nov", "dic", ""}}, new Object[]{"MonthNarrows", new String[]{"E", PdfOps.F_TOKEN, PdfOps.M_TOKEN, "A", PdfOps.M_TOKEN, "J", "J", "A", PdfOps.S_TOKEN, "O", "N", PdfOps.D_TOKEN, ""}}, new Object[]{"DayNames", new String[]{"domingo", "lunes", "martes", "miércoles", "jueves", "viernes", "sábado"}}, new Object[]{"DayAbbreviations", new String[]{Constants.DOM_PNAME, "lun", "mar", "mié", "jue", "vie", "sáb"}}, new Object[]{"DayNarrows", new String[]{PdfOps.D_TOKEN, "L", PdfOps.M_TOKEN, "X", "J", "V", PdfOps.S_TOKEN}}, new Object[]{"Eras", new String[]{"antes de Cristo", "anno Dómini"}}, new Object[]{"short.Eras", new String[]{"a.C.", "d.C."}}, new Object[]{"NumberPatterns", new String[]{"#,##0.###;-#,##0.###", "¤#,##0.00;(¤#,##0.00)", "#,##0%"}}, new Object[]{"NumberElements", new String[]{",", ".", ";", FXMLLoader.RESOURCE_KEY_PREFIX, "0", FXMLLoader.CONTROLLER_METHOD_PREFIX, LanguageTag.SEP, "E", "‰", "∞", "�"}}, new Object[]{"TimePatterns", new String[]{"HH'H'mm'' z", "H:mm:ss z", "H:mm:ss", "H:mm"}}, new Object[]{"DatePatterns", new String[]{"EEEE d' de 'MMMM' de 'yyyy", "d' de 'MMMM' de 'yyyy", "dd-MMM-yyyy", "d/MM/yy"}}, new Object[]{"DateTimePatterns", new String[]{"{1} {0}"}}, new Object[]{"DateTimePatternChars", "GyMdkHmsSEDFwWahKzZ"}};
    }
}
