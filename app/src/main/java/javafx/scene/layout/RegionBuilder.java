package javafx.scene.layout;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.ParentBuilder;
import javafx.scene.layout.RegionBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/layout/RegionBuilder.class */
public class RegionBuilder<B extends RegionBuilder<B>> extends ParentBuilder<B> implements Builder<Region> {
    private int __set;
    private double maxHeight;
    private double maxWidth;
    private double minHeight;
    private double minWidth;
    private Insets padding;
    private double prefHeight;
    private double prefWidth;
    private boolean snapToPixel;

    protected RegionBuilder() {
    }

    public static RegionBuilder<?> create() {
        return new RegionBuilder<>();
    }

    private void __set(int i2) {
        this.__set |= 1 << i2;
    }

    public void applyTo(Region x2) {
        super.applyTo((Parent) x2);
        int set = this.__set;
        while (set != 0) {
            int i2 = Integer.numberOfTrailingZeros(set);
            set &= (1 << i2) ^ (-1);
            switch (i2) {
                case 0:
                    x2.setMaxHeight(this.maxHeight);
                    break;
                case 1:
                    x2.setMaxWidth(this.maxWidth);
                    break;
                case 2:
                    x2.setMinHeight(this.minHeight);
                    break;
                case 3:
                    x2.setMinWidth(this.minWidth);
                    break;
                case 4:
                    x2.setPadding(this.padding);
                    break;
                case 5:
                    x2.setPrefHeight(this.prefHeight);
                    break;
                case 6:
                    x2.setPrefWidth(this.prefWidth);
                    break;
                case 7:
                    x2.setSnapToPixel(this.snapToPixel);
                    break;
            }
        }
    }

    public B maxHeight(double x2) {
        this.maxHeight = x2;
        __set(0);
        return this;
    }

    public B maxWidth(double x2) {
        this.maxWidth = x2;
        __set(1);
        return this;
    }

    public B minHeight(double x2) {
        this.minHeight = x2;
        __set(2);
        return this;
    }

    public B minWidth(double x2) {
        this.minWidth = x2;
        __set(3);
        return this;
    }

    public B padding(Insets x2) {
        this.padding = x2;
        __set(4);
        return this;
    }

    public B prefHeight(double x2) {
        this.prefHeight = x2;
        __set(5);
        return this;
    }

    public B prefWidth(double x2) {
        this.prefWidth = x2;
        __set(6);
        return this;
    }

    public B snapToPixel(boolean x2) {
        this.snapToPixel = x2;
        __set(7);
        return this;
    }

    @Override // javafx.util.Builder
    /* renamed from: build */
    public Region build2() {
        Region x2 = new Region();
        applyTo(x2);
        return x2;
    }
}
