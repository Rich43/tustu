package sun.util.calendar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.TimeZone;
import sun.util.calendar.BaseCalendar;

/* loaded from: rt.jar:sun/util/calendar/LocalGregorianCalendar.class */
public class LocalGregorianCalendar extends BaseCalendar {
    private String name;
    private Era[] eras;

    /* loaded from: rt.jar:sun/util/calendar/LocalGregorianCalendar$Date.class */
    public static class Date extends BaseCalendar.Date {
        private int gregorianYear;

        protected Date() {
            this.gregorianYear = Integer.MIN_VALUE;
        }

        protected Date(TimeZone timeZone) {
            super(timeZone);
            this.gregorianYear = Integer.MIN_VALUE;
        }

        @Override // sun.util.calendar.CalendarDate
        public Date setEra(Era era) {
            if (getEra() != era) {
                super.setEra(era);
                this.gregorianYear = Integer.MIN_VALUE;
            }
            return this;
        }

        @Override // sun.util.calendar.CalendarDate
        public Date addYear(int i2) {
            super.addYear(i2);
            this.gregorianYear += i2;
            return this;
        }

        @Override // sun.util.calendar.CalendarDate
        public Date setYear(int i2) {
            if (getYear() != i2) {
                super.setYear(i2);
                this.gregorianYear = Integer.MIN_VALUE;
            }
            return this;
        }

        @Override // sun.util.calendar.BaseCalendar.Date
        public int getNormalizedYear() {
            return this.gregorianYear;
        }

        @Override // sun.util.calendar.BaseCalendar.Date
        public void setNormalizedYear(int i2) {
            this.gregorianYear = i2;
        }

        void setLocalEra(Era era) {
            super.setEra(era);
        }

        void setLocalYear(int i2) {
            super.setYear(i2);
        }

        @Override // sun.util.calendar.CalendarDate
        public String toString() {
            String abbreviation;
            String string = super.toString();
            String strSubstring = string.substring(string.indexOf(84));
            StringBuffer stringBuffer = new StringBuffer();
            Era era = getEra();
            if (era != null && (abbreviation = era.getAbbreviation()) != null) {
                stringBuffer.append(abbreviation);
            }
            stringBuffer.append(getYear()).append('.');
            CalendarUtils.sprintf0d(stringBuffer, getMonth(), 2).append('.');
            CalendarUtils.sprintf0d(stringBuffer, getDayOfMonth(), 2);
            stringBuffer.append(strSubstring);
            return stringBuffer.toString();
        }
    }

    static LocalGregorianCalendar getLocalGregorianCalendar(String str) {
        try {
            String property = CalendarSystem.getCalendarProperties().getProperty("calendar." + str + ".eras");
            if (property == null) {
                return null;
            }
            ArrayList arrayList = new ArrayList();
            StringTokenizer stringTokenizer = new StringTokenizer(property, ";");
            while (stringTokenizer.hasMoreTokens()) {
                StringTokenizer stringTokenizer2 = new StringTokenizer(stringTokenizer.nextToken().trim(), ",");
                String str2 = null;
                boolean z2 = true;
                long j2 = 0;
                String str3 = null;
                while (stringTokenizer2.hasMoreTokens()) {
                    String strNextToken = stringTokenizer2.nextToken();
                    int iIndexOf = strNextToken.indexOf(61);
                    if (iIndexOf == -1) {
                        return null;
                    }
                    String strSubstring = strNextToken.substring(0, iIndexOf);
                    String strSubstring2 = strNextToken.substring(iIndexOf + 1);
                    if ("name".equals(strSubstring)) {
                        str2 = strSubstring2;
                    } else if ("since".equals(strSubstring)) {
                        if (strSubstring2.endsWith("u")) {
                            z2 = false;
                            j2 = Long.parseLong(strSubstring2.substring(0, strSubstring2.length() - 1));
                        } else {
                            j2 = Long.parseLong(strSubstring2);
                        }
                    } else if ("abbr".equals(strSubstring)) {
                        str3 = strSubstring2;
                    } else {
                        throw new RuntimeException("Unknown key word: " + strSubstring);
                    }
                }
                arrayList.add(new Era(str2, str3, j2, z2));
            }
            Era[] eraArr = new Era[arrayList.size()];
            arrayList.toArray(eraArr);
            return new LocalGregorianCalendar(str, eraArr);
        } catch (IOException | IllegalArgumentException e2) {
            throw new InternalError(e2);
        }
    }

    private LocalGregorianCalendar(String str, Era[] eraArr) {
        this.name = str;
        this.eras = eraArr;
        setEras(eraArr);
    }

    @Override // sun.util.calendar.CalendarSystem
    public String getName() {
        return this.name;
    }

    @Override // sun.util.calendar.AbstractCalendar, sun.util.calendar.CalendarSystem
    public Date getCalendarDate() {
        return getCalendarDate(System.currentTimeMillis(), (CalendarDate) newCalendarDate());
    }

    @Override // sun.util.calendar.AbstractCalendar, sun.util.calendar.CalendarSystem
    public Date getCalendarDate(long j2) {
        return getCalendarDate(j2, (CalendarDate) newCalendarDate());
    }

    @Override // sun.util.calendar.AbstractCalendar, sun.util.calendar.CalendarSystem
    public Date getCalendarDate(long j2, TimeZone timeZone) {
        return getCalendarDate(j2, (CalendarDate) newCalendarDate(timeZone));
    }

    @Override // sun.util.calendar.AbstractCalendar, sun.util.calendar.CalendarSystem
    public Date getCalendarDate(long j2, CalendarDate calendarDate) {
        Date date = (Date) super.getCalendarDate(j2, calendarDate);
        return adjustYear(date, j2, date.getZoneOffset());
    }

    private Date adjustYear(Date date, long j2, int i2) {
        int length = this.eras.length - 1;
        while (true) {
            if (length < 0) {
                break;
            }
            Era era = this.eras[length];
            long since = era.getSince(null);
            if (era.isLocalTime()) {
                since -= i2;
            }
            if (j2 < since) {
                length--;
            } else {
                date.setLocalEra(era);
                date.setLocalYear((date.getNormalizedYear() - era.getSinceDate().getYear()) + 1);
                break;
            }
        }
        if (length < 0) {
            date.setLocalEra(null);
            date.setLocalYear(date.getNormalizedYear());
        }
        date.setNormalized(true);
        return date;
    }

    @Override // sun.util.calendar.CalendarSystem
    public Date newCalendarDate() {
        return new Date();
    }

    @Override // sun.util.calendar.CalendarSystem
    public Date newCalendarDate(TimeZone timeZone) {
        return new Date(timeZone);
    }

    @Override // sun.util.calendar.BaseCalendar, sun.util.calendar.CalendarSystem
    public boolean validate(CalendarDate calendarDate) {
        Date date = (Date) calendarDate;
        Era era = date.getEra();
        if (era != null) {
            if (!validateEra(era)) {
                return false;
            }
            date.setNormalizedYear((era.getSinceDate().getYear() + date.getYear()) - 1);
            Date dateNewCalendarDate = newCalendarDate(calendarDate.getZone());
            dateNewCalendarDate.setEra(era).setDate(calendarDate.getYear(), calendarDate.getMonth(), calendarDate.getDayOfMonth());
            normalize(dateNewCalendarDate);
            if (dateNewCalendarDate.getEra() != era) {
                return false;
            }
        } else {
            if (calendarDate.getYear() >= this.eras[0].getSinceDate().getYear()) {
                return false;
            }
            date.setNormalizedYear(date.getYear());
        }
        return super.validate(date);
    }

    private boolean validateEra(Era era) {
        for (int i2 = 0; i2 < this.eras.length; i2++) {
            if (era == this.eras[i2]) {
                return true;
            }
        }
        return false;
    }

    @Override // sun.util.calendar.BaseCalendar, sun.util.calendar.CalendarSystem
    public boolean normalize(CalendarDate calendarDate) {
        if (calendarDate.isNormalized()) {
            return true;
        }
        normalizeYear(calendarDate);
        Date date = (Date) calendarDate;
        super.normalize(date);
        boolean z2 = false;
        long time = 0;
        int normalizedYear = date.getNormalizedYear();
        Era era = null;
        int length = this.eras.length - 1;
        while (true) {
            if (length < 0) {
                break;
            }
            era = this.eras[length];
            if (era.isLocalTime()) {
                CalendarDate sinceDate = era.getSinceDate();
                int year = sinceDate.getYear();
                if (normalizedYear > year) {
                    break;
                }
                if (normalizedYear == year) {
                    int month = date.getMonth();
                    int month2 = sinceDate.getMonth();
                    if (month > month2) {
                        break;
                    }
                    if (month == month2) {
                        int dayOfMonth = date.getDayOfMonth();
                        int dayOfMonth2 = sinceDate.getDayOfMonth();
                        if (dayOfMonth > dayOfMonth2) {
                            break;
                        }
                        if (dayOfMonth == dayOfMonth2) {
                            if (date.getTimeOfDay() < sinceDate.getTimeOfDay()) {
                                length--;
                            }
                        }
                    } else {
                        continue;
                    }
                } else {
                    continue;
                }
                length--;
            } else {
                if (!z2) {
                    time = super.getTime(calendarDate);
                    z2 = true;
                }
                if (time >= era.getSince(calendarDate.getZone())) {
                    break;
                }
                length--;
            }
        }
        if (length >= 0) {
            date.setLocalEra(era);
            date.setLocalYear((date.getNormalizedYear() - era.getSinceDate().getYear()) + 1);
        } else {
            date.setEra((Era) null);
            date.setLocalYear(normalizedYear);
            date.setNormalizedYear(normalizedYear);
        }
        date.setNormalized(true);
        return true;
    }

    @Override // sun.util.calendar.BaseCalendar
    void normalizeMonth(CalendarDate calendarDate) {
        normalizeYear(calendarDate);
        super.normalizeMonth(calendarDate);
    }

    void normalizeYear(CalendarDate calendarDate) {
        Date date = (Date) calendarDate;
        Era era = date.getEra();
        if (era == null || !validateEra(era)) {
            date.setNormalizedYear(date.getYear());
        } else {
            date.setNormalizedYear((era.getSinceDate().getYear() + date.getYear()) - 1);
        }
    }

    @Override // sun.util.calendar.BaseCalendar
    public boolean isLeapYear(int i2) {
        return CalendarUtils.isGregorianLeapYear(i2);
    }

    public boolean isLeapYear(Era era, int i2) {
        if (era == null) {
            return isLeapYear(i2);
        }
        return isLeapYear((era.getSinceDate().getYear() + i2) - 1);
    }

    @Override // sun.util.calendar.BaseCalendar, sun.util.calendar.AbstractCalendar
    public void getCalendarDateFromFixedDate(CalendarDate calendarDate, long j2) {
        Date date = (Date) calendarDate;
        super.getCalendarDateFromFixedDate(date, j2);
        adjustYear(date, (j2 - 719163) * 86400000, 0);
    }
}
