package sun.text.resources.sr;

import javafx.fxml.FXMLLoader;
import sun.util.locale.LanguageTag;
import sun.util.resources.ParallelListResourceBundle;

/* loaded from: localedata.jar:sun/text/resources/sr/FormatData_sr.class */
public class FormatData_sr extends ParallelListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v3, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // sun.util.resources.ParallelListResourceBundle
    protected final Object[][] getContents() {
        String[] strArr = {"Пре РК", "РК"};
        return new Object[]{new Object[]{"MonthNames", new String[]{"јануар", "фебруар", "март", "април", "мај", "јун", "јул", "август", "септембар", "октобар", "новембар", "децембар", ""}}, new Object[]{"MonthAbbreviations", new String[]{"јан", "феб", "мар", "апр", "мај", "јун", "јул", "авг", "сеп", "окт", "нов", "дец", ""}}, new Object[]{"MonthNarrows", new String[]{"ј", "ф", "м", "а", "м", "ј", "ј", "а", "с", "о", "н", "д", ""}}, new Object[]{"MonthNarrows", new String[]{"ј", "ф", "м", "а", "м", "ј", "ј", "а", "с", "о", "н", "д", ""}}, new Object[]{"DayNames", new String[]{"недеља", "понедељак", "уторак", "среда", "четвртак", "петак", "субота"}}, new Object[]{"DayAbbreviations", new String[]{"нед", "пон", "уто", "сре", "чет", "пет", "суб"}}, new Object[]{"DayNarrows", new String[]{"н", "п", "у", "с", "ч", "п", "с"}}, new Object[]{"Eras", new String[]{"п. н. е.", "н. е"}}, new Object[]{"short.Eras", new String[]{"п. н. е.", "н. е."}}, new Object[]{"narrow.Eras", new String[]{"п.н.е.", "н.е."}}, new Object[]{"NumberPatterns", new String[]{"#,##0.###", "¤ #,##0.00", "#,##0%"}}, new Object[]{"NumberElements", new String[]{",", ".", ";", FXMLLoader.RESOURCE_KEY_PREFIX, "0", FXMLLoader.CONTROLLER_METHOD_PREFIX, LanguageTag.SEP, "E", "‰", "∞", "NaN"}}, new Object[]{"TimePatterns", new String[]{"HH.mm.ss z", "HH.mm.ss z", "HH.mm.ss", "HH.mm"}}, new Object[]{"DatePatterns", new String[]{"EEEE, dd.MMMM.yyyy.", "dd.MM.yyyy.", "dd.MM.yyyy.", "d.M.yy."}}, new Object[]{"DateTimePatterns", new String[]{"{1} {0}"}}, new Object[]{"DateTimePatternChars", "GanjkHmsSEDFwWxhKzZ"}};
    }
}
