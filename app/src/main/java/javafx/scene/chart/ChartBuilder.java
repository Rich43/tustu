package javafx.scene.chart;

import javafx.geometry.Side;
import javafx.scene.chart.ChartBuilder;
import javafx.scene.layout.Region;
import javafx.scene.layout.RegionBuilder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/chart/ChartBuilder.class */
public abstract class ChartBuilder<B extends ChartBuilder<B>> extends RegionBuilder<B> {
    private int __set;
    private boolean animated;
    private Side legendSide;
    private boolean legendVisible;
    private String title;
    private Side titleSide;

    protected ChartBuilder() {
    }

    public void applyTo(Chart x2) {
        super.applyTo((Region) x2);
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setAnimated(this.animated);
        }
        if ((set & 2) != 0) {
            x2.setLegendSide(this.legendSide);
        }
        if ((set & 4) != 0) {
            x2.setLegendVisible(this.legendVisible);
        }
        if ((set & 8) != 0) {
            x2.setTitle(this.title);
        }
        if ((set & 16) != 0) {
            x2.setTitleSide(this.titleSide);
        }
    }

    public B animated(boolean x2) {
        this.animated = x2;
        this.__set |= 1;
        return this;
    }

    public B legendSide(Side x2) {
        this.legendSide = x2;
        this.__set |= 2;
        return this;
    }

    public B legendVisible(boolean x2) {
        this.legendVisible = x2;
        this.__set |= 4;
        return this;
    }

    public B title(String x2) {
        this.title = x2;
        this.__set |= 8;
        return this;
    }

    public B titleSide(Side x2) {
        this.titleSide = x2;
        this.__set |= 16;
        return this;
    }
}
