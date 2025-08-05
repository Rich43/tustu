package javax.swing.text;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/* loaded from: rt.jar:javax/swing/text/DateFormatter.class */
public class DateFormatter extends InternationalFormatter {
    public DateFormatter() {
        this(DateFormat.getDateInstance());
    }

    public DateFormatter(DateFormat dateFormat) {
        super(dateFormat);
        setFormat(dateFormat);
    }

    public void setFormat(DateFormat dateFormat) {
        super.setFormat((Format) dateFormat);
    }

    private Calendar getCalendar() {
        Format format = getFormat();
        if (format instanceof DateFormat) {
            return ((DateFormat) format).getCalendar();
        }
        return Calendar.getInstance();
    }

    @Override // javax.swing.text.InternationalFormatter
    boolean getSupportsIncrement() {
        return true;
    }

    @Override // javax.swing.text.InternationalFormatter
    Object getAdjustField(int i2, Map map) {
        for (Object obj : map.keySet()) {
            if ((obj instanceof DateFormat.Field) && (obj == DateFormat.Field.HOUR1 || ((DateFormat.Field) obj).getCalendarField() != -1)) {
                return obj;
            }
        }
        return null;
    }

    @Override // javax.swing.text.InternationalFormatter
    Object adjustValue(Object obj, Map map, Object obj2, int i2) throws BadLocationException, ParseException {
        Date time;
        if (obj2 != null) {
            if (obj2 == DateFormat.Field.HOUR1) {
                obj2 = DateFormat.Field.HOUR0;
            }
            int calendarField = ((DateFormat.Field) obj2).getCalendarField();
            Calendar calendar = getCalendar();
            if (calendar != null) {
                calendar.setTime((Date) obj);
                calendar.get(calendarField);
                try {
                    calendar.add(calendarField, i2);
                    time = calendar.getTime();
                } catch (Throwable th) {
                    time = null;
                }
                return time;
            }
            return null;
        }
        return null;
    }
}
