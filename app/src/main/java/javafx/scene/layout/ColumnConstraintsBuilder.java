package javafx.scene.layout;

import javafx.geometry.HPos;
import javafx.scene.layout.ColumnConstraintsBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/layout/ColumnConstraintsBuilder.class */
public class ColumnConstraintsBuilder<B extends ColumnConstraintsBuilder<B>> implements Builder<ColumnConstraints> {
    private int __set;
    private boolean fillWidth;
    private HPos halignment;
    private Priority hgrow;
    private double maxWidth;
    private double minWidth;
    private double percentWidth;
    private double prefWidth;

    protected ColumnConstraintsBuilder() {
    }

    public static ColumnConstraintsBuilder<?> create() {
        return new ColumnConstraintsBuilder<>();
    }

    public void applyTo(ColumnConstraints x2) {
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setFillWidth(this.fillWidth);
        }
        if ((set & 2) != 0) {
            x2.setHalignment(this.halignment);
        }
        if ((set & 4) != 0) {
            x2.setHgrow(this.hgrow);
        }
        if ((set & 8) != 0) {
            x2.setMaxWidth(this.maxWidth);
        }
        if ((set & 16) != 0) {
            x2.setMinWidth(this.minWidth);
        }
        if ((set & 32) != 0) {
            x2.setPercentWidth(this.percentWidth);
        }
        if ((set & 64) != 0) {
            x2.setPrefWidth(this.prefWidth);
        }
    }

    public B fillWidth(boolean x2) {
        this.fillWidth = x2;
        this.__set |= 1;
        return this;
    }

    public B halignment(HPos x2) {
        this.halignment = x2;
        this.__set |= 2;
        return this;
    }

    public B hgrow(Priority x2) {
        this.hgrow = x2;
        this.__set |= 4;
        return this;
    }

    public B maxWidth(double x2) {
        this.maxWidth = x2;
        this.__set |= 8;
        return this;
    }

    public B minWidth(double x2) {
        this.minWidth = x2;
        this.__set |= 16;
        return this;
    }

    public B percentWidth(double x2) {
        this.percentWidth = x2;
        this.__set |= 32;
        return this;
    }

    public B prefWidth(double x2) {
        this.prefWidth = x2;
        this.__set |= 64;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    public ColumnConstraints build() {
        ColumnConstraints x2 = new ColumnConstraints();
        applyTo(x2);
        return x2;
    }
}
