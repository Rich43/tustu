package javax.swing;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/* loaded from: rt.jar:javax/swing/SpinnerDateModel.class */
public class SpinnerDateModel extends AbstractSpinnerModel implements Serializable {
    private Comparable start;
    private Comparable end;
    private Calendar value;
    private int calendarField;

    private boolean calendarFieldOK(int i2) {
        switch (i2) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
                return true;
            default:
                return false;
        }
    }

    public SpinnerDateModel(Date date, Comparable comparable, Comparable comparable2, int i2) {
        if (date == null) {
            throw new IllegalArgumentException("value is null");
        }
        if (!calendarFieldOK(i2)) {
            throw new IllegalArgumentException("invalid calendarField");
        }
        if ((comparable != null && comparable.compareTo(date) > 0) || (comparable2 != null && comparable2.compareTo(date) < 0)) {
            throw new IllegalArgumentException("(start <= value <= end) is false");
        }
        this.value = Calendar.getInstance();
        this.start = comparable;
        this.end = comparable2;
        this.calendarField = i2;
        this.value.setTime(date);
    }

    public SpinnerDateModel() {
        this(new Date(), null, null, 5);
    }

    public void setStart(Comparable comparable) {
        if (comparable == null) {
            if (this.start == null) {
                return;
            }
        } else if (comparable.equals(this.start)) {
            return;
        }
        this.start = comparable;
        fireStateChanged();
    }

    public Comparable getStart() {
        return this.start;
    }

    public void setEnd(Comparable comparable) {
        if (comparable == null) {
            if (this.end == null) {
                return;
            }
        } else if (comparable.equals(this.end)) {
            return;
        }
        this.end = comparable;
        fireStateChanged();
    }

    public Comparable getEnd() {
        return this.end;
    }

    public void setCalendarField(int i2) {
        if (!calendarFieldOK(i2)) {
            throw new IllegalArgumentException("invalid calendarField");
        }
        if (i2 != this.calendarField) {
            this.calendarField = i2;
            fireStateChanged();
        }
    }

    public int getCalendarField() {
        return this.calendarField;
    }

    @Override // javax.swing.SpinnerModel
    public Object getNextValue() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.value.getTime());
        calendar.add(this.calendarField, 1);
        Date time = calendar.getTime();
        if (this.end == null || this.end.compareTo(time) >= 0) {
            return time;
        }
        return null;
    }

    @Override // javax.swing.SpinnerModel
    public Object getPreviousValue() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.value.getTime());
        calendar.add(this.calendarField, -1);
        Date time = calendar.getTime();
        if (this.start == null || this.start.compareTo(time) <= 0) {
            return time;
        }
        return null;
    }

    public Date getDate() {
        return this.value.getTime();
    }

    @Override // javax.swing.SpinnerModel
    public Object getValue() {
        return this.value.getTime();
    }

    @Override // javax.swing.SpinnerModel
    public void setValue(Object obj) {
        if (obj == null || !(obj instanceof Date)) {
            throw new IllegalArgumentException("illegal value");
        }
        if (!obj.equals(this.value.getTime())) {
            this.value.setTime((Date) obj);
            fireStateChanged();
        }
    }
}
