package javafx.scene.layout;

import javafx.geometry.VPos;
import javafx.scene.layout.RowConstraintsBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/layout/RowConstraintsBuilder.class */
public class RowConstraintsBuilder<B extends RowConstraintsBuilder<B>> implements Builder<RowConstraints> {
    private int __set;
    private boolean fillHeight;
    private double maxHeight;
    private double minHeight;
    private double percentHeight;
    private double prefHeight;
    private VPos valignment;
    private Priority vgrow;

    protected RowConstraintsBuilder() {
    }

    public static RowConstraintsBuilder<?> create() {
        return new RowConstraintsBuilder<>();
    }

    public void applyTo(RowConstraints x2) {
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setFillHeight(this.fillHeight);
        }
        if ((set & 2) != 0) {
            x2.setMaxHeight(this.maxHeight);
        }
        if ((set & 4) != 0) {
            x2.setMinHeight(this.minHeight);
        }
        if ((set & 8) != 0) {
            x2.setPercentHeight(this.percentHeight);
        }
        if ((set & 16) != 0) {
            x2.setPrefHeight(this.prefHeight);
        }
        if ((set & 32) != 0) {
            x2.setValignment(this.valignment);
        }
        if ((set & 64) != 0) {
            x2.setVgrow(this.vgrow);
        }
    }

    public B fillHeight(boolean x2) {
        this.fillHeight = x2;
        this.__set |= 1;
        return this;
    }

    public B maxHeight(double x2) {
        this.maxHeight = x2;
        this.__set |= 2;
        return this;
    }

    public B minHeight(double x2) {
        this.minHeight = x2;
        this.__set |= 4;
        return this;
    }

    public B percentHeight(double x2) {
        this.percentHeight = x2;
        this.__set |= 8;
        return this;
    }

    public B prefHeight(double x2) {
        this.prefHeight = x2;
        this.__set |= 16;
        return this;
    }

    public B valignment(VPos x2) {
        this.valignment = x2;
        this.__set |= 32;
        return this;
    }

    public B vgrow(Priority x2) {
        this.vgrow = x2;
        this.__set |= 64;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    public RowConstraints build() {
        RowConstraints x2 = new RowConstraints();
        applyTo(x2);
        return x2;
    }
}
