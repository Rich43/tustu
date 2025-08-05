package sun.text.resources.hr;

import com.sun.org.apache.xml.internal.security.utils.Constants;
import javafx.fxml.FXMLLoader;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;
import sun.util.resources.ParallelListResourceBundle;

/* loaded from: localedata.jar:sun/text/resources/hr/FormatData_hr.class */
public class FormatData_hr extends ParallelListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v3, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // sun.util.resources.ParallelListResourceBundle
    protected final Object[][] getContents() {
        String[] strArr = {"prije R.O.C.", "R.O.C."};
        return new Object[]{new Object[]{"MonthNames", new String[]{"siječnja", "veljače", "ožujka", "travnja", "svibnja", "lipnja", "srpnja", "kolovoza", "rujna", "listopada", "studenoga", "prosinca", ""}}, new Object[]{"standalone.MonthNames", new String[]{"siječanj", "veljača", "ožujak", "travanj", "svibanj", "lipanj", "srpanj", "kolovoz", "rujan", "listopad", "studeni", "prosinac", ""}}, new Object[]{"MonthAbbreviations", new String[]{"sij", "velj", "ožu", "tra", "svi", "lip", "srp", "kol", "ruj", "lis", "stu", "pro", ""}}, new Object[]{"standalone.MonthAbbreviations", new String[]{"sij", "vel", "ožu", "tra", "svi", "lip", "srp", "kol", "ruj", "lis", "stu", "pro", ""}}, new Object[]{"MonthNarrows", new String[]{"1.", "2.", "3.", "4.", "5.", "6.", "7.", "8.", "9.", "10.", "11.", "12.", ""}}, new Object[]{"standalone.MonthNarrows", new String[]{"1.", "2.", "3.", "4.", "5.", "6.", "7.", "8.", "9.", "10.", "11.", "12.", ""}}, new Object[]{"DayNames", new String[]{"nedjelja", "ponedjeljak", "utorak", "srijeda", "četvrtak", "petak", "subota"}}, new Object[]{"standalone.DayNames", new String[]{"nedjelja", "ponedjeljak", "utorak", "srijeda", "četvrtak", "petak", "subota"}}, new Object[]{"DayAbbreviations", new String[]{"ned", "pon", "uto", "sri", "čet", "pet", "sub"}}, new Object[]{"standalone.DayAbbreviations", new String[]{"ned", "pon", "uto", "sri", "čet", "pet", "sub"}}, new Object[]{"DayNarrows", new String[]{"N", Constants._TAG_P, "U", PdfOps.S_TOKEN, "Č", Constants._TAG_P, PdfOps.S_TOKEN}}, new Object[]{"standalone.DayNarrows", new String[]{PdfOps.n_TOKEN, "p", "u", PdfOps.s_TOKEN, "č", "p", PdfOps.s_TOKEN}}, new Object[]{"Eras", new String[]{"Prije Krista", "Poslije Krista"}}, new Object[]{"short.Eras", new String[]{"p. n. e.", "A. D."}}, new Object[]{"NumberElements", new String[]{",", ".", ";", FXMLLoader.RESOURCE_KEY_PREFIX, "0", FXMLLoader.CONTROLLER_METHOD_PREFIX, LanguageTag.SEP, "E", "‰", "∞", "�"}}, new Object[]{"TimePatterns", new String[]{"HH:mm:ss z", "HH:mm:ss z", "HH:mm:ss", "HH:mm"}}, new Object[]{"DatePatterns", new String[]{"yyyy. MMMM dd", "yyyy. MMMM dd", "yyyy.MM.dd", "yyyy.MM.dd"}}, new Object[]{"DateTimePatterns", new String[]{"{1} {0}"}}, new Object[]{"DateTimePatternChars", "GanjkHmsSEDFwWxhKzZ"}};
    }
}
