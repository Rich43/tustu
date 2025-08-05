package javax.swing.text;

import java.io.Serializable;

/* loaded from: rt.jar:javax/swing/text/TabStop.class */
public class TabStop implements Serializable {
    public static final int ALIGN_LEFT = 0;
    public static final int ALIGN_RIGHT = 1;
    public static final int ALIGN_CENTER = 2;
    public static final int ALIGN_DECIMAL = 4;
    public static final int ALIGN_BAR = 5;
    public static final int LEAD_NONE = 0;
    public static final int LEAD_DOTS = 1;
    public static final int LEAD_HYPHENS = 2;
    public static final int LEAD_UNDERLINE = 3;
    public static final int LEAD_THICKLINE = 4;
    public static final int LEAD_EQUALS = 5;
    private int alignment;
    private float position;
    private int leader;

    public TabStop(float f2) {
        this(f2, 0, 0);
    }

    public TabStop(float f2, int i2, int i3) {
        this.alignment = i2;
        this.leader = i3;
        this.position = f2;
    }

    public float getPosition() {
        return this.position;
    }

    public int getAlignment() {
        return this.alignment;
    }

    public int getLeader() {
        return this.leader;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof TabStop) {
            TabStop tabStop = (TabStop) obj;
            return this.alignment == tabStop.alignment && this.leader == tabStop.leader && this.position == tabStop.position;
        }
        return false;
    }

    public int hashCode() {
        return (this.alignment ^ this.leader) ^ Math.round(this.position);
    }

    public String toString() {
        String str;
        switch (this.alignment) {
            case 0:
            case 3:
            default:
                str = "";
                break;
            case 1:
                str = "right ";
                break;
            case 2:
                str = "center ";
                break;
            case 4:
                str = "decimal ";
                break;
            case 5:
                str = "bar ";
                break;
        }
        String str2 = str + "tab @" + String.valueOf(this.position);
        if (this.leader != 0) {
            str2 = str2 + " (w/leaders)";
        }
        return str2;
    }
}
