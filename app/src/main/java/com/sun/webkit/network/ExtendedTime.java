package com.sun.webkit.network;

/* loaded from: jfxrt.jar:com/sun/webkit/network/ExtendedTime.class */
final class ExtendedTime implements Comparable<ExtendedTime> {
    private final long baseTime;
    private final int subtime;

    ExtendedTime(long baseTime, int subtime) {
        this.baseTime = baseTime;
        this.subtime = subtime;
    }

    static ExtendedTime currentTime() {
        return new ExtendedTime(System.currentTimeMillis(), 0);
    }

    long baseTime() {
        return this.baseTime;
    }

    int subtime() {
        return this.subtime;
    }

    ExtendedTime incrementSubtime() {
        return new ExtendedTime(this.baseTime, this.subtime + 1);
    }

    @Override // java.lang.Comparable
    public int compareTo(ExtendedTime otherExtendedTime) {
        int d2 = (int) (this.baseTime - otherExtendedTime.baseTime);
        if (d2 != 0) {
            return d2;
        }
        return this.subtime - otherExtendedTime.subtime;
    }

    public String toString() {
        return "[baseTime=" + this.baseTime + ", subtime=" + this.subtime + "]";
    }
}
