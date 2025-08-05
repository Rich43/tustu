package sun.util.locale.provider;

import java.util.Locale;
import java.util.Map;
import java.util.spi.CalendarDataProvider;
import java.util.spi.CalendarNameProvider;
import sun.util.locale.provider.LocaleServiceProviderPool;

/* loaded from: rt.jar:sun/util/locale/provider/CalendarDataUtility.class */
public class CalendarDataUtility {
    public static final String FIRST_DAY_OF_WEEK = "firstDayOfWeek";
    public static final String MINIMAL_DAYS_IN_FIRST_WEEK = "minimalDaysInFirstWeek";

    private CalendarDataUtility() {
    }

    public static int retrieveFirstDayOfWeek(Locale locale) {
        Integer num = (Integer) LocaleServiceProviderPool.getPool(CalendarDataProvider.class).getLocalizedObject(CalendarWeekParameterGetter.INSTANCE, locale, FIRST_DAY_OF_WEEK, new Object[0]);
        if (num == null || num.intValue() < 1 || num.intValue() > 7) {
            return 1;
        }
        return num.intValue();
    }

    public static int retrieveMinimalDaysInFirstWeek(Locale locale) {
        Integer num = (Integer) LocaleServiceProviderPool.getPool(CalendarDataProvider.class).getLocalizedObject(CalendarWeekParameterGetter.INSTANCE, locale, MINIMAL_DAYS_IN_FIRST_WEEK, new Object[0]);
        if (num == null || num.intValue() < 1 || num.intValue() > 7) {
            return 1;
        }
        return num.intValue();
    }

    public static String retrieveFieldValueName(String str, int i2, int i3, int i4, Locale locale) {
        return (String) LocaleServiceProviderPool.getPool(CalendarNameProvider.class).getLocalizedObject(CalendarFieldValueNameGetter.INSTANCE, locale, normalizeCalendarType(str), Integer.valueOf(i2), Integer.valueOf(i3), Integer.valueOf(i4), false);
    }

    public static String retrieveJavaTimeFieldValueName(String str, int i2, int i3, int i4, Locale locale) {
        LocaleServiceProviderPool pool = LocaleServiceProviderPool.getPool(CalendarNameProvider.class);
        String str2 = (String) pool.getLocalizedObject(CalendarFieldValueNameGetter.INSTANCE, locale, normalizeCalendarType(str), Integer.valueOf(i2), Integer.valueOf(i3), Integer.valueOf(i4), true);
        if (str2 == null) {
            str2 = (String) pool.getLocalizedObject(CalendarFieldValueNameGetter.INSTANCE, locale, normalizeCalendarType(str), Integer.valueOf(i2), Integer.valueOf(i3), Integer.valueOf(i4), false);
        }
        return str2;
    }

    public static Map<String, Integer> retrieveFieldValueNames(String str, int i2, int i3, Locale locale) {
        return (Map) LocaleServiceProviderPool.getPool(CalendarNameProvider.class).getLocalizedObject(CalendarFieldValueNamesMapGetter.INSTANCE, locale, normalizeCalendarType(str), Integer.valueOf(i2), Integer.valueOf(i3), false);
    }

    public static Map<String, Integer> retrieveJavaTimeFieldValueNames(String str, int i2, int i3, Locale locale) {
        LocaleServiceProviderPool pool = LocaleServiceProviderPool.getPool(CalendarNameProvider.class);
        Map<String, Integer> map = (Map) pool.getLocalizedObject(CalendarFieldValueNamesMapGetter.INSTANCE, locale, normalizeCalendarType(str), Integer.valueOf(i2), Integer.valueOf(i3), true);
        if (map == null) {
            map = (Map) pool.getLocalizedObject(CalendarFieldValueNamesMapGetter.INSTANCE, locale, normalizeCalendarType(str), Integer.valueOf(i2), Integer.valueOf(i3), false);
        }
        return map;
    }

    static String normalizeCalendarType(String str) {
        String str2;
        if (str.equals("gregorian") || str.equals("iso8601")) {
            str2 = "gregory";
        } else if (str.startsWith("islamic")) {
            str2 = "islamic";
        } else {
            str2 = str;
        }
        return str2;
    }

    /* loaded from: rt.jar:sun/util/locale/provider/CalendarDataUtility$CalendarFieldValueNameGetter.class */
    private static class CalendarFieldValueNameGetter implements LocaleServiceProviderPool.LocalizedObjectGetter<CalendarNameProvider, String> {
        private static final CalendarFieldValueNameGetter INSTANCE;
        static final /* synthetic */ boolean $assertionsDisabled;

        private CalendarFieldValueNameGetter() {
        }

        static {
            $assertionsDisabled = !CalendarDataUtility.class.desiredAssertionStatus();
            INSTANCE = new CalendarFieldValueNameGetter();
        }

        @Override // sun.util.locale.provider.LocaleServiceProviderPool.LocalizedObjectGetter
        public String getObject(CalendarNameProvider calendarNameProvider, Locale locale, String str, Object... objArr) {
            if (!$assertionsDisabled && objArr.length != 4) {
                throw new AssertionError();
            }
            int iIntValue = ((Integer) objArr[0]).intValue();
            int iIntValue2 = ((Integer) objArr[1]).intValue();
            int iIntValue3 = ((Integer) objArr[2]).intValue();
            if (((Boolean) objArr[3]).booleanValue() && (calendarNameProvider instanceof CalendarNameProviderImpl)) {
                return ((CalendarNameProviderImpl) calendarNameProvider).getJavaTimeDisplayName(str, iIntValue, iIntValue2, iIntValue3, locale);
            }
            return calendarNameProvider.getDisplayName(str, iIntValue, iIntValue2, iIntValue3, locale);
        }
    }

    /* loaded from: rt.jar:sun/util/locale/provider/CalendarDataUtility$CalendarFieldValueNamesMapGetter.class */
    private static class CalendarFieldValueNamesMapGetter implements LocaleServiceProviderPool.LocalizedObjectGetter<CalendarNameProvider, Map<String, Integer>> {
        private static final CalendarFieldValueNamesMapGetter INSTANCE;
        static final /* synthetic */ boolean $assertionsDisabled;

        private CalendarFieldValueNamesMapGetter() {
        }

        static {
            $assertionsDisabled = !CalendarDataUtility.class.desiredAssertionStatus();
            INSTANCE = new CalendarFieldValueNamesMapGetter();
        }

        @Override // sun.util.locale.provider.LocaleServiceProviderPool.LocalizedObjectGetter
        public Map<String, Integer> getObject(CalendarNameProvider calendarNameProvider, Locale locale, String str, Object... objArr) {
            if (!$assertionsDisabled && objArr.length != 3) {
                throw new AssertionError();
            }
            int iIntValue = ((Integer) objArr[0]).intValue();
            int iIntValue2 = ((Integer) objArr[1]).intValue();
            if (((Boolean) objArr[2]).booleanValue() && (calendarNameProvider instanceof CalendarNameProviderImpl)) {
                return ((CalendarNameProviderImpl) calendarNameProvider).getJavaTimeDisplayNames(str, iIntValue, iIntValue2, locale);
            }
            return calendarNameProvider.getDisplayNames(str, iIntValue, iIntValue2, locale);
        }
    }

    /* loaded from: rt.jar:sun/util/locale/provider/CalendarDataUtility$CalendarWeekParameterGetter.class */
    private static class CalendarWeekParameterGetter implements LocaleServiceProviderPool.LocalizedObjectGetter<CalendarDataProvider, Integer> {
        private static final CalendarWeekParameterGetter INSTANCE;
        static final /* synthetic */ boolean $assertionsDisabled;

        private CalendarWeekParameterGetter() {
        }

        static {
            $assertionsDisabled = !CalendarDataUtility.class.desiredAssertionStatus();
            INSTANCE = new CalendarWeekParameterGetter();
        }

        @Override // sun.util.locale.provider.LocaleServiceProviderPool.LocalizedObjectGetter
        public Integer getObject(CalendarDataProvider calendarDataProvider, Locale locale, String str, Object... objArr) {
            int minimalDaysInFirstWeek;
            if (!$assertionsDisabled && objArr.length != 0) {
                throw new AssertionError();
            }
            switch (str) {
                case "firstDayOfWeek":
                    minimalDaysInFirstWeek = calendarDataProvider.getFirstDayOfWeek(locale);
                    break;
                case "minimalDaysInFirstWeek":
                    minimalDaysInFirstWeek = calendarDataProvider.getMinimalDaysInFirstWeek(locale);
                    break;
                default:
                    throw new InternalError("invalid requestID: " + str);
            }
            if (minimalDaysInFirstWeek != 0) {
                return Integer.valueOf(minimalDaysInFirstWeek);
            }
            return null;
        }
    }
}
