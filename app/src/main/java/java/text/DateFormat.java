package java.text;

import com.sun.media.jfxmedia.MetadataParser;
import com.sun.org.glassfish.external.statistics.impl.StatisticImpl;
import java.io.InvalidObjectException;
import java.text.Format;
import java.text.spi.DateFormatProvider;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.provider.LocaleProviderAdapter;
import sun.util.locale.provider.LocaleServiceProviderPool;

/* loaded from: rt.jar:java/text/DateFormat.class */
public abstract class DateFormat extends Format {
    protected Calendar calendar;
    protected NumberFormat numberFormat;
    public static final int ERA_FIELD = 0;
    public static final int YEAR_FIELD = 1;
    public static final int MONTH_FIELD = 2;
    public static final int DATE_FIELD = 3;
    public static final int HOUR_OF_DAY1_FIELD = 4;
    public static final int HOUR_OF_DAY0_FIELD = 5;
    public static final int MINUTE_FIELD = 6;
    public static final int SECOND_FIELD = 7;
    public static final int MILLISECOND_FIELD = 8;
    public static final int DAY_OF_WEEK_FIELD = 9;
    public static final int DAY_OF_YEAR_FIELD = 10;
    public static final int DAY_OF_WEEK_IN_MONTH_FIELD = 11;
    public static final int WEEK_OF_YEAR_FIELD = 12;
    public static final int WEEK_OF_MONTH_FIELD = 13;
    public static final int AM_PM_FIELD = 14;
    public static final int HOUR1_FIELD = 15;
    public static final int HOUR0_FIELD = 16;
    public static final int TIMEZONE_FIELD = 17;
    private static final long serialVersionUID = 7218322306649953788L;
    public static final int FULL = 0;
    public static final int LONG = 1;
    public static final int MEDIUM = 2;
    public static final int SHORT = 3;
    public static final int DEFAULT = 2;

    public abstract StringBuffer format(Date date, StringBuffer stringBuffer, FieldPosition fieldPosition);

    public abstract Date parse(String str, ParsePosition parsePosition);

    @Override // java.text.Format
    public final StringBuffer format(Object obj, StringBuffer stringBuffer, FieldPosition fieldPosition) {
        if (obj instanceof Date) {
            return format((Date) obj, stringBuffer, fieldPosition);
        }
        if (obj instanceof Number) {
            return format(new Date(((Number) obj).longValue()), stringBuffer, fieldPosition);
        }
        throw new IllegalArgumentException("Cannot format given Object as a Date");
    }

    public final String format(Date date) {
        return format(date, new StringBuffer(), DontCareFieldPosition.INSTANCE).toString();
    }

    public Date parse(String str) throws ParseException {
        ParsePosition parsePosition = new ParsePosition(0);
        Date date = parse(str, parsePosition);
        if (parsePosition.index == 0) {
            throw new ParseException("Unparseable date: \"" + str + PdfOps.DOUBLE_QUOTE__TOKEN, parsePosition.errorIndex);
        }
        return date;
    }

    @Override // java.text.Format
    public Object parseObject(String str, ParsePosition parsePosition) {
        return parse(str, parsePosition);
    }

    public static final DateFormat getTimeInstance() {
        return get(2, 0, 1, Locale.getDefault(Locale.Category.FORMAT));
    }

    public static final DateFormat getTimeInstance(int i2) {
        return get(i2, 0, 1, Locale.getDefault(Locale.Category.FORMAT));
    }

    public static final DateFormat getTimeInstance(int i2, Locale locale) {
        return get(i2, 0, 1, locale);
    }

    public static final DateFormat getDateInstance() {
        return get(0, 2, 2, Locale.getDefault(Locale.Category.FORMAT));
    }

    public static final DateFormat getDateInstance(int i2) {
        return get(0, i2, 2, Locale.getDefault(Locale.Category.FORMAT));
    }

    public static final DateFormat getDateInstance(int i2, Locale locale) {
        return get(0, i2, 2, locale);
    }

    public static final DateFormat getDateTimeInstance() {
        return get(2, 2, 3, Locale.getDefault(Locale.Category.FORMAT));
    }

    public static final DateFormat getDateTimeInstance(int i2, int i3) {
        return get(i3, i2, 3, Locale.getDefault(Locale.Category.FORMAT));
    }

    public static final DateFormat getDateTimeInstance(int i2, int i3, Locale locale) {
        return get(i3, i2, 3, locale);
    }

    public static final DateFormat getInstance() {
        return getDateTimeInstance(3, 3);
    }

    public static Locale[] getAvailableLocales() {
        return LocaleServiceProviderPool.getPool(DateFormatProvider.class).getAvailableLocales();
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public Calendar getCalendar() {
        return this.calendar;
    }

    public void setNumberFormat(NumberFormat numberFormat) {
        this.numberFormat = numberFormat;
    }

    public NumberFormat getNumberFormat() {
        return this.numberFormat;
    }

    public void setTimeZone(TimeZone timeZone) {
        this.calendar.setTimeZone(timeZone);
    }

    public TimeZone getTimeZone() {
        return this.calendar.getTimeZone();
    }

    public void setLenient(boolean z2) {
        this.calendar.setLenient(z2);
    }

    public boolean isLenient() {
        return this.calendar.isLenient();
    }

    public int hashCode() {
        return this.numberFormat.hashCode();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        DateFormat dateFormat = (DateFormat) obj;
        return this.calendar.getFirstDayOfWeek() == dateFormat.calendar.getFirstDayOfWeek() && this.calendar.getMinimalDaysInFirstWeek() == dateFormat.calendar.getMinimalDaysInFirstWeek() && this.calendar.isLenient() == dateFormat.calendar.isLenient() && this.calendar.getTimeZone().equals(dateFormat.calendar.getTimeZone()) && this.numberFormat.equals(dateFormat.numberFormat);
    }

    @Override // java.text.Format
    public Object clone() {
        DateFormat dateFormat = (DateFormat) super.clone();
        dateFormat.calendar = (Calendar) this.calendar.clone();
        dateFormat.numberFormat = (NumberFormat) this.numberFormat.clone();
        return dateFormat;
    }

    private static DateFormat get(int i2, int i3, int i4, Locale locale) {
        if ((i4 & 1) != 0) {
            if (i2 < 0 || i2 > 3) {
                throw new IllegalArgumentException("Illegal time style " + i2);
            }
        } else {
            i2 = -1;
        }
        if ((i4 & 2) != 0) {
            if (i3 < 0 || i3 > 3) {
                throw new IllegalArgumentException("Illegal date style " + i3);
            }
        } else {
            i3 = -1;
        }
        DateFormat dateFormat = get(LocaleProviderAdapter.getAdapter(DateFormatProvider.class, locale), i2, i3, locale);
        if (dateFormat == null) {
            dateFormat = get(LocaleProviderAdapter.forJRE(), i2, i3, locale);
        }
        return dateFormat;
    }

    private static DateFormat get(LocaleProviderAdapter localeProviderAdapter, int i2, int i3, Locale locale) {
        DateFormat dateTimeInstance;
        DateFormatProvider dateFormatProvider = localeProviderAdapter.getDateFormatProvider();
        if (i2 == -1) {
            dateTimeInstance = dateFormatProvider.getDateInstance(i3, locale);
        } else if (i3 == -1) {
            dateTimeInstance = dateFormatProvider.getTimeInstance(i2, locale);
        } else {
            dateTimeInstance = dateFormatProvider.getDateTimeInstance(i3, i2, locale);
        }
        return dateTimeInstance;
    }

    protected DateFormat() {
    }

    /* loaded from: rt.jar:java/text/DateFormat$Field.class */
    public static class Field extends Format.Field {
        private static final long serialVersionUID = 7441350119349544720L;
        private int calendarField;
        private static final Map<String, Field> instanceMap = new HashMap(18);
        private static final Field[] calendarToFieldMapping = new Field[17];
        public static final Field ERA = new Field("era", 0);
        public static final Field YEAR = new Field(MetadataParser.YEAR_TAG_NAME, 1);
        public static final Field MONTH = new Field("month", 2);
        public static final Field DAY_OF_MONTH = new Field("day of month", 5);
        public static final Field HOUR_OF_DAY1 = new Field("hour of day 1", -1);
        public static final Field HOUR_OF_DAY0 = new Field("hour of day", 11);
        public static final Field MINUTE = new Field("minute", 12);
        public static final Field SECOND = new Field(StatisticImpl.UNIT_SECOND, 13);
        public static final Field MILLISECOND = new Field(StatisticImpl.UNIT_MILLISECOND, 14);
        public static final Field DAY_OF_WEEK = new Field("day of week", 7);
        public static final Field DAY_OF_YEAR = new Field("day of year", 6);
        public static final Field DAY_OF_WEEK_IN_MONTH = new Field("day of week in month", 8);
        public static final Field WEEK_OF_YEAR = new Field("week of year", 3);
        public static final Field WEEK_OF_MONTH = new Field("week of month", 4);
        public static final Field AM_PM = new Field("am pm", 9);
        public static final Field HOUR1 = new Field("hour 1", -1);
        public static final Field HOUR0 = new Field("hour", 10);
        public static final Field TIME_ZONE = new Field("time zone", -1);

        public static Field ofCalendarField(int i2) {
            if (i2 < 0 || i2 >= calendarToFieldMapping.length) {
                throw new IllegalArgumentException("Unknown Calendar constant " + i2);
            }
            return calendarToFieldMapping[i2];
        }

        protected Field(String str, int i2) {
            super(str);
            this.calendarField = i2;
            if (getClass() == Field.class) {
                instanceMap.put(str, this);
                if (i2 >= 0) {
                    calendarToFieldMapping[i2] = this;
                }
            }
        }

        public int getCalendarField() {
            return this.calendarField;
        }

        @Override // java.text.AttributedCharacterIterator.Attribute
        protected Object readResolve() throws InvalidObjectException {
            if (getClass() != Field.class) {
                throw new InvalidObjectException("subclass didn't correctly implement readResolve");
            }
            Field field = instanceMap.get(getName());
            if (field != null) {
                return field;
            }
            throw new InvalidObjectException("unknown attribute name");
        }
    }
}
