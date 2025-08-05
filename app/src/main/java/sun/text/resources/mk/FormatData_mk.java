package sun.text.resources.mk;

import javafx.fxml.FXMLLoader;
import sun.util.locale.LanguageTag;
import sun.util.resources.ParallelListResourceBundle;

/* loaded from: localedata.jar:sun/text/resources/mk/FormatData_mk.class */
public class FormatData_mk extends ParallelListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // sun.util.resources.ParallelListResourceBundle
    protected final Object[][] getContents() {
        return new Object[]{new Object[]{"MonthNames", new String[]{"јануари", "февруари", "март", "април", "мај", "јуни", "јули", "август", "септември", "октомври", "ноември", "декември", ""}}, new Object[]{"MonthAbbreviations", new String[]{"јан.", "фев.", "мар.", "апр.", "мај.", "јун.", "јул.", "авг.", "септ.", "окт.", "ноем.", "декем.", ""}}, new Object[]{"MonthNarrows", new String[]{"ј", "ф", "м", "а", "м", "ј", "ј", "а", "с", "о", "н", "д", ""}}, new Object[]{"DayNames", new String[]{"недела", "понеделник", "вторник", "среда", "четврток", "петок", "сабота"}}, new Object[]{"DayAbbreviations", new String[]{"нед.", "пон.", "вт.", "сре.", "чет.", "пет.", "саб."}}, new Object[]{"DayNarrows", new String[]{"н", "п", "в", "с", "ч", "п", "с"}}, new Object[]{"Eras", new String[]{"пр.н.е.", "ае."}}, new Object[]{"NumberElements", new String[]{",", ".", ";", FXMLLoader.RESOURCE_KEY_PREFIX, "0", FXMLLoader.CONTROLLER_METHOD_PREFIX, LanguageTag.SEP, "E", "‰", "∞", "�"}}, new Object[]{"TimePatterns", new String[]{"HH:mm:ss z", "HH:mm:ss z", "HH:mm:", "HH:mm"}}, new Object[]{"DatePatterns", new String[]{"EEEE, d, MMMM yyyy", "d, MMMM yyyy", "d.M.yyyy", "d.M.yy"}}, new Object[]{"DateTimePatterns", new String[]{"{1} {0}"}}, new Object[]{"DateTimePatternChars", "GuMtkHmsSEDFwWahKzZ"}};
    }
}
