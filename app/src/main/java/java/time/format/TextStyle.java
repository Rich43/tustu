package java.time.format;

import java.util.Calendar;

/* loaded from: rt.jar:java/time/format/TextStyle.class */
public enum TextStyle {
    FULL(2, 0),
    FULL_STANDALONE(Calendar.LONG_STANDALONE, 0),
    SHORT(1, 1),
    SHORT_STANDALONE(Calendar.SHORT_STANDALONE, 1),
    NARROW(4, 1),
    NARROW_STANDALONE(Calendar.NARROW_STANDALONE, 1);

    private final int calendarStyle;
    private final int zoneNameStyleIndex;

    TextStyle(int i2, int i3) {
        this.calendarStyle = i2;
        this.zoneNameStyleIndex = i3;
    }

    public boolean isStandalone() {
        return (ordinal() & 1) == 1;
    }

    public TextStyle asStandalone() {
        return values()[ordinal() | 1];
    }

    public TextStyle asNormal() {
        return values()[ordinal() & (-2)];
    }

    int toCalendarStyle() {
        return this.calendarStyle;
    }

    int zoneNameStyleIndex() {
        return this.zoneNameStyleIndex;
    }
}
