package sun.text.resources.bg;

import javafx.fxml.FXMLLoader;
import sun.util.locale.LanguageTag;
import sun.util.resources.ParallelListResourceBundle;

/* loaded from: localedata.jar:sun/text/resources/bg/FormatData_bg.class */
public class FormatData_bg extends ParallelListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // sun.util.resources.ParallelListResourceBundle
    protected final Object[][] getContents() {
        return new Object[]{new Object[]{"MonthNames", new String[]{"Януари", "Февруари", "Март", "Април", "Май", "Юни", "Юли", "Август", "Септември", "Октомври", "Ноември", "Декември", ""}}, new Object[]{"MonthAbbreviations", new String[]{"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X", "XI", "XII", ""}}, new Object[]{"MonthNarrows", new String[]{"я", "ф", "м", "а", "м", "ю", "ю", "а", "с", "о", "н", "д", ""}}, new Object[]{"DayNames", new String[]{"Неделя", "Понеделник", "Вторник", "Сряда", "Четвъртък", "Петък", "Събота"}}, new Object[]{"DayAbbreviations", new String[]{"Нд", "Пн", "Вт", "Ср", "Чт", "Пт", "Сб"}}, new Object[]{"DayNarrows", new String[]{"н", "п", "в", "с", "ч", "п", "с"}}, new Object[]{"Eras", new String[]{"пр.н.е.", "н.е."}}, new Object[]{"short.Eras", new String[]{"пр. н. е.", "от н. е."}}, new Object[]{"NumberElements", new String[]{",", " ", ";", FXMLLoader.RESOURCE_KEY_PREFIX, "0", FXMLLoader.CONTROLLER_METHOD_PREFIX, LanguageTag.SEP, "E", "‰", "∞", "�"}}, new Object[]{"TimePatterns", new String[]{"HH:mm:ss zzzz", "HH:mm:ss z", "HH:mm:ss", "HH:mm"}}, new Object[]{"DatePatterns", new String[]{"dd MMMM y, EEEE", "dd MMMM y", "dd.MM.yyyy", "dd.MM.yy"}}, new Object[]{"DateTimePatterns", new String[]{"{1} {0}"}}, new Object[]{"DateTimePatternChars", "GanjkHmsSEDFwWxhKzZ"}};
    }
}
