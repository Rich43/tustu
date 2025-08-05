package java.awt.font;

/* loaded from: rt.jar:java/awt/font/TextHitInfo.class */
public final class TextHitInfo {
    private int charIndex;
    private boolean isLeadingEdge;

    private TextHitInfo(int i2, boolean z2) {
        this.charIndex = i2;
        this.isLeadingEdge = z2;
    }

    public int getCharIndex() {
        return this.charIndex;
    }

    public boolean isLeadingEdge() {
        return this.isLeadingEdge;
    }

    public int getInsertionIndex() {
        return this.isLeadingEdge ? this.charIndex : this.charIndex + 1;
    }

    public int hashCode() {
        return this.charIndex;
    }

    public boolean equals(Object obj) {
        return (obj instanceof TextHitInfo) && equals((TextHitInfo) obj);
    }

    public boolean equals(TextHitInfo textHitInfo) {
        return textHitInfo != null && this.charIndex == textHitInfo.charIndex && this.isLeadingEdge == textHitInfo.isLeadingEdge;
    }

    public String toString() {
        return "TextHitInfo[" + this.charIndex + (this.isLeadingEdge ? "L" : "T") + "]";
    }

    public static TextHitInfo leading(int i2) {
        return new TextHitInfo(i2, true);
    }

    public static TextHitInfo trailing(int i2) {
        return new TextHitInfo(i2, false);
    }

    public static TextHitInfo beforeOffset(int i2) {
        return new TextHitInfo(i2 - 1, false);
    }

    public static TextHitInfo afterOffset(int i2) {
        return new TextHitInfo(i2, true);
    }

    public TextHitInfo getOtherHit() {
        if (this.isLeadingEdge) {
            return trailing(this.charIndex - 1);
        }
        return leading(this.charIndex + 1);
    }

    public TextHitInfo getOffsetHit(int i2) {
        return new TextHitInfo(this.charIndex + i2, this.isLeadingEdge);
    }
}
