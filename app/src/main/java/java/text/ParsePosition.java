package java.text;

/* loaded from: rt.jar:java/text/ParsePosition.class */
public class ParsePosition {
    int index;
    int errorIndex = -1;

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int i2) {
        this.index = i2;
    }

    public ParsePosition(int i2) {
        this.index = 0;
        this.index = i2;
    }

    public void setErrorIndex(int i2) {
        this.errorIndex = i2;
    }

    public int getErrorIndex() {
        return this.errorIndex;
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof ParsePosition)) {
            return false;
        }
        ParsePosition parsePosition = (ParsePosition) obj;
        return this.index == parsePosition.index && this.errorIndex == parsePosition.errorIndex;
    }

    public int hashCode() {
        return (this.errorIndex << 16) | this.index;
    }

    public String toString() {
        return getClass().getName() + "[index=" + this.index + ",errorIndex=" + this.errorIndex + ']';
    }
}
