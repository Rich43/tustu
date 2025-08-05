package java.text;

import java.util.Calendar;

/* loaded from: rt.jar:java/text/CalendarBuilder.class */
class CalendarBuilder {
    private static final int UNSET = 0;
    private static final int COMPUTED = 1;
    private static final int MINIMUM_USER_STAMP = 2;
    private static final int MAX_FIELD = 18;
    public static final int WEEK_YEAR = 17;
    public static final int ISO_DAY_OF_WEEK = 1000;
    private final int[] field = new int[36];
    private int nextStamp = 2;
    private int maxFieldIndex = -1;

    CalendarBuilder() {
    }

    CalendarBuilder set(int i2, int i3) {
        if (i2 == 1000) {
            i2 = 7;
            i3 = toCalendarDayOfWeek(i3);
        }
        int i4 = this.nextStamp;
        this.nextStamp = i4 + 1;
        this.field[i2] = i4;
        this.field[18 + i2] = i3;
        if (i2 > this.maxFieldIndex && i2 < 17) {
            this.maxFieldIndex = i2;
        }
        return this;
    }

    CalendarBuilder addYear(int i2) {
        int[] iArr = this.field;
        iArr[19] = iArr[19] + i2;
        int[] iArr2 = this.field;
        iArr2[35] = iArr2[35] + i2;
        return this;
    }

    boolean isSet(int i2) {
        if (i2 == 1000) {
            i2 = 7;
        }
        return this.field[i2] > 0;
    }

    CalendarBuilder clear(int i2) {
        if (i2 == 1000) {
            i2 = 7;
        }
        this.field[i2] = 0;
        this.field[18 + i2] = 0;
        return this;
    }

    Calendar establish(Calendar calendar) {
        boolean z2 = isSet(17) && this.field[17] > this.field[1];
        if (z2 && !calendar.isWeekDateSupported()) {
            if (!isSet(1)) {
                set(1, this.field[35]);
            }
            z2 = false;
        }
        calendar.clear();
        for (int i2 = 2; i2 < this.nextStamp; i2++) {
            int i3 = 0;
            while (true) {
                if (i3 > this.maxFieldIndex) {
                    break;
                }
                if (this.field[i3] != i2) {
                    i3++;
                } else {
                    calendar.set(i3, this.field[18 + i3]);
                    break;
                }
            }
        }
        if (z2) {
            int i4 = isSet(3) ? this.field[21] : 1;
            int firstDayOfWeek = isSet(7) ? this.field[25] : calendar.getFirstDayOfWeek();
            if (!isValidDayOfWeek(firstDayOfWeek) && calendar.isLenient()) {
                if (firstDayOfWeek >= 8) {
                    int i5 = firstDayOfWeek - 1;
                    i4 += i5 / 7;
                    firstDayOfWeek = (i5 % 7) + 1;
                } else {
                    while (firstDayOfWeek <= 0) {
                        firstDayOfWeek += 7;
                        i4--;
                    }
                }
                firstDayOfWeek = toCalendarDayOfWeek(firstDayOfWeek);
            }
            calendar.setWeekDate(this.field[35], i4, firstDayOfWeek);
        }
        return calendar;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CalendarBuilder:[");
        for (int i2 = 0; i2 < this.field.length; i2++) {
            if (isSet(i2)) {
                sb.append(i2).append('=').append(this.field[18 + i2]).append(',');
            }
        }
        int length = sb.length() - 1;
        if (sb.charAt(length) == ',') {
            sb.setLength(length);
        }
        sb.append(']');
        return sb.toString();
    }

    static int toISODayOfWeek(int i2) {
        if (i2 == 1) {
            return 7;
        }
        return i2 - 1;
    }

    static int toCalendarDayOfWeek(int i2) {
        if (!isValidDayOfWeek(i2)) {
            return i2;
        }
        if (i2 == 7) {
            return 1;
        }
        return i2 + 1;
    }

    static boolean isValidDayOfWeek(int i2) {
        return i2 > 0 && i2 <= 7;
    }
}
