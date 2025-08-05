package sun.text.resources.no;

import javafx.fxml.FXMLLoader;
import sun.util.locale.LanguageTag;
import sun.util.resources.ParallelListResourceBundle;

/* loaded from: localedata.jar:sun/text/resources/no/FormatData_no_NO_NY.class */
public class FormatData_no_NO_NY extends ParallelListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // sun.util.resources.ParallelListResourceBundle
    protected final Object[][] getContents() {
        return new Object[]{new Object[]{"MonthNames", new String[]{"januar", "februar", "mars", "april", "mai", "juni", "juli", "august", "september", "oktober", "november", "desember", ""}}, new Object[]{"MonthAbbreviations", new String[]{"jan", "feb", "mar", "apr", "mai", "jun", "jul", "aug", "sep", "okt", "nov", "des", ""}}, new Object[]{"DayNames", new String[]{"sundag", "måndag", "tysdag", "onsdag", "torsdag", "fredag", "laurdag"}}, new Object[]{"DayAbbreviations", new String[]{"su", "må", "ty", FXMLLoader.EVENT_HANDLER_PREFIX, "to", "fr", "lau"}}, new Object[]{"NumberElements", new String[]{",", " ", ";", FXMLLoader.RESOURCE_KEY_PREFIX, "0", FXMLLoader.CONTROLLER_METHOD_PREFIX, LanguageTag.SEP, "E", "‰", "∞", "�"}}, new Object[]{"TimePatterns", new String[]{"'kl 'HH.mm z", "HH:mm:ss z", "HH:mm:ss", "HH:mm"}}, new Object[]{"DatePatterns", new String[]{"d. MMMM yyyy", "d. MMMM yyyy", "dd.MMM.yyyy", "dd.MM.yy"}}, new Object[]{"DateTimePatterns", new String[]{"{1} {0}"}}, new Object[]{"DateTimePatternChars", "GyMdkHmsSEDFwWahKzZ"}};
    }
}
