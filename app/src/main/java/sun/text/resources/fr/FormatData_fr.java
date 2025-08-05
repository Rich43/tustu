package sun.text.resources.fr;

import javafx.fxml.FXMLLoader;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;
import sun.util.resources.ParallelListResourceBundle;

/* loaded from: localedata.jar:sun/text/resources/fr/FormatData_fr.class */
public class FormatData_fr extends ParallelListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // sun.util.resources.ParallelListResourceBundle
    protected final Object[][] getContents() {
        return new Object[]{new Object[]{"MonthNames", new String[]{"janvier", "février", "mars", "avril", "mai", "juin", "juillet", "août", "septembre", "octobre", "novembre", "décembre", ""}}, new Object[]{"MonthAbbreviations", new String[]{"janv.", "févr.", "mars", "avr.", "mai", "juin", "juil.", "août", "sept.", "oct.", "nov.", "déc.", ""}}, new Object[]{"MonthNarrows", new String[]{"J", PdfOps.F_TOKEN, PdfOps.M_TOKEN, "A", PdfOps.M_TOKEN, "J", "J", "A", PdfOps.S_TOKEN, "O", "N", PdfOps.D_TOKEN, ""}}, new Object[]{"DayNames", new String[]{"dimanche", "lundi", "mardi", "mercredi", "jeudi", "vendredi", "samedi"}}, new Object[]{"DayAbbreviations", new String[]{"dim.", "lun.", "mar.", "mer.", "jeu.", "ven.", "sam."}}, new Object[]{"standalone.DayAbbreviations", new String[]{"dim.", "lun.", "mar.", "mer.", "jeu.", "ven.", "sam."}}, new Object[]{"DayNarrows", new String[]{PdfOps.D_TOKEN, "L", PdfOps.M_TOKEN, PdfOps.M_TOKEN, "J", "V", PdfOps.S_TOKEN}}, new Object[]{"Eras", new String[]{"BC", "ap. J.-C."}}, new Object[]{"short.Eras", new String[]{"av. J.-C.", "ap. J.-C."}}, new Object[]{"buddhist.Eras", new String[]{"BC", "ère bouddhiste"}}, new Object[]{"buddhist.short.Eras", new String[]{"BC", "ère b."}}, new Object[]{"buddhist.narrow.Eras", new String[]{"BC", "E.B."}}, new Object[]{"NumberPatterns", new String[]{"#,##0.###;-#,##0.###", "#,##0.00 ¤;-#,##0.00 ¤", "#,##0 %"}}, new Object[]{"NumberElements", new String[]{",", " ", ";", FXMLLoader.RESOURCE_KEY_PREFIX, "0", FXMLLoader.CONTROLLER_METHOD_PREFIX, LanguageTag.SEP, "E", "‰", "∞", "�"}}, new Object[]{"TimePatterns", new String[]{"HH' h 'mm z", "HH:mm:ss z", "HH:mm:ss", "HH:mm"}}, new Object[]{"DatePatterns", new String[]{"EEEE d MMMM yyyy", "d MMMM yyyy", "d MMM yyyy", "dd/MM/yy"}}, new Object[]{"DateTimePatterns", new String[]{"{1} {0}"}}, new Object[]{"DateTimePatternChars", "GaMjkHmsSEDFwWxhKzZ"}};
    }
}
