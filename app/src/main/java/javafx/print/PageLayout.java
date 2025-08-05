package javafx.print;

/* loaded from: jfxrt.jar:javafx/print/PageLayout.class */
public final class PageLayout {
    private PageOrientation orient;
    private Paper paper;
    private double lMargin;
    private double rMargin;
    private double tMargin;
    private double bMargin;

    PageLayout(Paper paper, PageOrientation orient) {
        this(paper, orient, 56.0d, 56.0d, 56.0d, 56.0d);
    }

    PageLayout(Paper paper, PageOrientation orient, double leftMargin, double rightMargin, double topMargin, double bottomMargin) {
        if (paper == null || orient == null || leftMargin < 0.0d || rightMargin < 0.0d || topMargin < 0.0d || bottomMargin < 0.0d) {
            throw new IllegalArgumentException("Illegal parameters");
        }
        if (orient == PageOrientation.PORTRAIT || orient == PageOrientation.REVERSE_PORTRAIT) {
            if (leftMargin + rightMargin > paper.getWidth() || topMargin + bottomMargin > paper.getHeight()) {
                throw new IllegalArgumentException("Bad margins");
            }
        } else if (leftMargin + rightMargin > paper.getHeight() || topMargin + bottomMargin > paper.getWidth()) {
            throw new IllegalArgumentException("Bad margins");
        }
        this.paper = paper;
        this.orient = orient;
        this.lMargin = leftMargin;
        this.rMargin = rightMargin;
        this.tMargin = topMargin;
        this.bMargin = bottomMargin;
    }

    public PageOrientation getPageOrientation() {
        return this.orient;
    }

    public Paper getPaper() {
        return this.paper;
    }

    public double getPrintableWidth() {
        double pw;
        if (this.orient == PageOrientation.PORTRAIT || this.orient == PageOrientation.REVERSE_PORTRAIT) {
            pw = this.paper.getWidth();
        } else {
            pw = this.paper.getHeight();
        }
        double pw2 = pw - (this.lMargin + this.rMargin);
        if (pw2 < 0.0d) {
            pw2 = 0.0d;
        }
        return pw2;
    }

    public double getPrintableHeight() {
        double ph;
        if (this.orient == PageOrientation.PORTRAIT || this.orient == PageOrientation.REVERSE_PORTRAIT) {
            ph = this.paper.getHeight();
        } else {
            ph = this.paper.getWidth();
        }
        double ph2 = ph - (this.tMargin + this.bMargin);
        if (ph2 < 0.0d) {
            ph2 = 0.0d;
        }
        return ph2;
    }

    public double getLeftMargin() {
        return this.lMargin;
    }

    public double getRightMargin() {
        return this.rMargin;
    }

    public double getTopMargin() {
        return this.tMargin;
    }

    public double getBottomMargin() {
        return this.bMargin;
    }

    public boolean equals(Object o2) {
        if (o2 instanceof PageLayout) {
            PageLayout other = (PageLayout) o2;
            return this.paper.equals(other.paper) && this.orient.equals(other.orient) && this.tMargin == other.tMargin && this.bMargin == other.bMargin && this.rMargin == other.rMargin && this.lMargin == other.lMargin;
        }
        return false;
    }

    public int hashCode() {
        return this.paper.hashCode() + this.orient.hashCode() + ((int) (this.tMargin + this.bMargin + this.lMargin + this.rMargin));
    }

    public String toString() {
        return "Paper=" + ((Object) this.paper) + " Orient=" + ((Object) this.orient) + " leftMargin=" + this.lMargin + " rightMargin=" + this.rMargin + " topMargin=" + this.tMargin + " bottomMargin=" + this.bMargin;
    }
}
