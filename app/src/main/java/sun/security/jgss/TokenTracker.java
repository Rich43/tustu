package sun.security.jgss;

import java.util.LinkedList;
import org.ietf.jgss.MessageProp;

/* loaded from: rt.jar:sun/security/jgss/TokenTracker.class */
public class TokenTracker {
    static final int MAX_INTERVALS = 5;
    private int initNumber;
    private int windowStart;
    private int expectedNumber;
    private int windowStartIndex = 0;
    private LinkedList<Entry> list = new LinkedList<>();

    public TokenTracker(int i2) {
        this.initNumber = i2;
        this.windowStart = i2;
        this.expectedNumber = i2;
        this.list.add(new Entry(i2 - 1));
    }

    private int getIntervalIndex(int i2) {
        int size = this.list.size() - 1;
        while (size >= 0 && this.list.get(size).compareTo(i2) > 0) {
            size--;
        }
        return size;
    }

    public final synchronized void getProps(int i2, MessageProp messageProp) {
        boolean z2 = false;
        boolean z3 = false;
        boolean z4 = false;
        boolean z5 = false;
        int intervalIndex = getIntervalIndex(i2);
        Entry entry = null;
        if (intervalIndex != -1) {
            entry = this.list.get(intervalIndex);
        }
        if (i2 == this.expectedNumber) {
            this.expectedNumber++;
        } else if (entry != null && entry.contains(i2)) {
            z5 = true;
        } else if (this.expectedNumber >= this.initNumber) {
            if (i2 > this.expectedNumber) {
                z2 = true;
            } else if (i2 >= this.windowStart) {
                z4 = true;
            } else if (i2 >= this.initNumber) {
                z3 = true;
            } else {
                z2 = true;
            }
        } else if (i2 > this.expectedNumber) {
            if (i2 < this.initNumber) {
                z2 = true;
            } else if (this.windowStart >= this.initNumber && i2 >= this.windowStart) {
                z4 = true;
            } else {
                z3 = true;
            }
        } else if (this.windowStart <= this.expectedNumber && i2 < this.windowStart) {
            z3 = true;
        } else {
            z4 = true;
        }
        if (!z5 && !z3) {
            add(i2, intervalIndex);
        }
        if (z2) {
            this.expectedNumber = i2 + 1;
        }
        messageProp.setSupplementaryStates(z5, z3, z4, z2, 0, null);
    }

    private void add(int i2, int i3) {
        Entry entryRemove;
        Entry entry = null;
        boolean z2 = false;
        boolean z3 = false;
        if (i3 != -1) {
            entry = this.list.get(i3);
            if (i2 == entry.getEnd() + 1) {
                entry.setEnd(i2);
                z2 = true;
            }
        }
        int i4 = i3 + 1;
        if (i4 < this.list.size()) {
            Entry entry2 = this.list.get(i4);
            if (i2 == entry2.getStart() - 1) {
                if (!z2) {
                    entry2.setStart(i2);
                } else {
                    entry2.setStart(entry.getStart());
                    this.list.remove(i3);
                    if (this.windowStartIndex > i3) {
                        this.windowStartIndex--;
                    }
                }
                z3 = true;
            }
        }
        if (z3 || z2) {
            return;
        }
        if (this.list.size() < 5) {
            entryRemove = new Entry(i2);
            if (i3 < this.windowStartIndex) {
                this.windowStartIndex++;
            }
        } else {
            int i5 = this.windowStartIndex;
            if (this.windowStartIndex == this.list.size() - 1) {
                this.windowStartIndex = 0;
            }
            entryRemove = this.list.remove(i5);
            this.windowStart = this.list.get(this.windowStartIndex).getStart();
            entryRemove.setStart(i2);
            entryRemove.setEnd(i2);
            if (i3 >= i5) {
                i3--;
            } else if (i5 != this.windowStartIndex) {
                if (i3 == -1) {
                    this.windowStart = i2;
                }
            } else {
                this.windowStartIndex++;
            }
        }
        this.list.add(i3 + 1, entryRemove);
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("TokenTracker: ");
        stringBuffer.append(" initNumber=").append(this.initNumber);
        stringBuffer.append(" windowStart=").append(this.windowStart);
        stringBuffer.append(" expectedNumber=").append(this.expectedNumber);
        stringBuffer.append(" windowStartIndex=").append(this.windowStartIndex);
        stringBuffer.append("\n\tIntervals are: {");
        for (int i2 = 0; i2 < this.list.size(); i2++) {
            if (i2 != 0) {
                stringBuffer.append(", ");
            }
            stringBuffer.append(this.list.get(i2).toString());
        }
        stringBuffer.append('}');
        return stringBuffer.toString();
    }

    /* loaded from: rt.jar:sun/security/jgss/TokenTracker$Entry.class */
    class Entry {
        private int start;
        private int end;

        Entry(int i2) {
            this.start = i2;
            this.end = i2;
        }

        final int compareTo(int i2) {
            if (this.start > i2) {
                return 1;
            }
            if (this.end < i2) {
                return -1;
            }
            return 0;
        }

        final boolean contains(int i2) {
            return i2 >= this.start && i2 <= this.end;
        }

        final void append(int i2) {
            if (i2 == this.end + 1) {
                this.end = i2;
            }
        }

        final void setInterval(int i2, int i3) {
            this.start = i2;
            this.end = i3;
        }

        final void setEnd(int i2) {
            this.end = i2;
        }

        final void setStart(int i2) {
            this.start = i2;
        }

        final int getStart() {
            return this.start;
        }

        final int getEnd() {
            return this.end;
        }

        public String toString() {
            return "[" + this.start + ", " + this.end + "]";
        }
    }
}
