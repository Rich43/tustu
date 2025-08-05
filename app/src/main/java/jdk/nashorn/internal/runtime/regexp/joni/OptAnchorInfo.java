package jdk.nashorn.internal.runtime.regexp.joni;

import jdk.nashorn.internal.runtime.regexp.joni.constants.AnchorType;

/* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/regexp/joni/OptAnchorInfo.class */
final class OptAnchorInfo implements AnchorType {
    int leftAnchor;
    int rightAnchor;

    OptAnchorInfo() {
    }

    void clear() {
        this.rightAnchor = 0;
        this.leftAnchor = 0;
    }

    void copy(OptAnchorInfo other) {
        this.leftAnchor = other.leftAnchor;
        this.rightAnchor = other.rightAnchor;
    }

    void concat(OptAnchorInfo left, OptAnchorInfo right, int leftLength, int rightLength) {
        this.leftAnchor = left.leftAnchor;
        if (leftLength == 0) {
            this.leftAnchor |= right.leftAnchor;
        }
        this.rightAnchor = right.rightAnchor;
        if (rightLength == 0) {
            this.rightAnchor |= left.rightAnchor;
        }
    }

    boolean isSet(int anchor) {
        return ((this.leftAnchor & anchor) == 0 && (this.rightAnchor & anchor) == 0) ? false : true;
    }

    void add(int anchor) {
        if (isLeftAnchor(anchor)) {
            this.leftAnchor |= anchor;
        } else {
            this.rightAnchor |= anchor;
        }
    }

    void remove(int anchor) {
        if (isLeftAnchor(anchor)) {
            this.leftAnchor &= anchor ^ (-1);
        } else {
            this.rightAnchor &= anchor ^ (-1);
        }
    }

    void altMerge(OptAnchorInfo other) {
        this.leftAnchor &= other.leftAnchor;
        this.rightAnchor &= other.rightAnchor;
    }

    static boolean isLeftAnchor(int anchor) {
        return (anchor == 8 || anchor == 16 || anchor == 32 || anchor == 1024 || anchor == 2048) ? false : true;
    }

    static String anchorToString(int anchor) {
        StringBuffer s2 = new StringBuffer("[");
        if ((anchor & 1) != 0) {
            s2.append("begin-buf ");
        }
        if ((anchor & 2) != 0) {
            s2.append("begin-line ");
        }
        if ((anchor & 4) != 0) {
            s2.append("begin-pos ");
        }
        if ((anchor & 8) != 0) {
            s2.append("end-buf ");
        }
        if ((anchor & 16) != 0) {
            s2.append("semi-end-buf ");
        }
        if ((anchor & 32) != 0) {
            s2.append("end-line ");
        }
        if ((anchor & 16384) != 0) {
            s2.append("anychar-star ");
        }
        if ((anchor & 32768) != 0) {
            s2.append("anychar-star-pl ");
        }
        s2.append("]");
        return s2.toString();
    }
}
