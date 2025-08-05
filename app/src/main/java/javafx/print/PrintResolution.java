package javafx.print;

/* loaded from: jfxrt.jar:javafx/print/PrintResolution.class */
public final class PrintResolution {
    private int cfRes;
    private int fRes;

    PrintResolution(int crossFeedResolution, int feedResolution) throws IllegalArgumentException {
        if (crossFeedResolution <= 0 || feedResolution <= 0) {
            throw new IllegalArgumentException("Values must be positive");
        }
        this.cfRes = crossFeedResolution;
        this.fRes = feedResolution;
    }

    public int getCrossFeedResolution() {
        return this.cfRes;
    }

    public int getFeedResolution() {
        return this.fRes;
    }

    public boolean equals(Object o2) {
        try {
            PrintResolution other = (PrintResolution) o2;
            if (this.cfRes == other.cfRes) {
                if (this.fRes == other.fRes) {
                    return true;
                }
            }
            return false;
        } catch (Exception e2) {
            return false;
        }
    }

    public int hashCode() {
        return (this.cfRes << 16) | this.fRes;
    }

    public String toString() {
        return "Feed res=" + this.fRes + "dpi. Cross Feed res=" + this.cfRes + "dpi.";
    }
}
