package javafx.scene.layout;

import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.layout.FlowPaneBuilder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/layout/FlowPaneBuilder.class */
public class FlowPaneBuilder<B extends FlowPaneBuilder<B>> extends PaneBuilder<B> {
    private int __set;
    private Pos alignment;
    private HPos columnHalignment;
    private double hgap;
    private Orientation orientation;
    private double prefWrapLength;
    private VPos rowValignment;
    private double vgap;

    protected FlowPaneBuilder() {
    }

    public static FlowPaneBuilder<?> create() {
        return new FlowPaneBuilder<>();
    }

    public void applyTo(FlowPane x2) {
        super.applyTo((Pane) x2);
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setAlignment(this.alignment);
        }
        if ((set & 2) != 0) {
            x2.setColumnHalignment(this.columnHalignment);
        }
        if ((set & 4) != 0) {
            x2.setHgap(this.hgap);
        }
        if ((set & 8) != 0) {
            x2.setOrientation(this.orientation);
        }
        if ((set & 16) != 0) {
            x2.setPrefWrapLength(this.prefWrapLength);
        }
        if ((set & 32) != 0) {
            x2.setRowValignment(this.rowValignment);
        }
        if ((set & 64) != 0) {
            x2.setVgap(this.vgap);
        }
    }

    public B alignment(Pos x2) {
        this.alignment = x2;
        this.__set |= 1;
        return this;
    }

    public B columnHalignment(HPos x2) {
        this.columnHalignment = x2;
        this.__set |= 2;
        return this;
    }

    public B hgap(double x2) {
        this.hgap = x2;
        this.__set |= 4;
        return this;
    }

    public B orientation(Orientation x2) {
        this.orientation = x2;
        this.__set |= 8;
        return this;
    }

    public B prefWrapLength(double x2) {
        this.prefWrapLength = x2;
        this.__set |= 16;
        return this;
    }

    public B rowValignment(VPos x2) {
        this.rowValignment = x2;
        this.__set |= 32;
        return this;
    }

    public B vgap(double x2) {
        this.vgap = x2;
        this.__set |= 64;
        return this;
    }

    @Override // javafx.scene.layout.PaneBuilder, javafx.scene.layout.RegionBuilder, javafx.util.Builder
    /* renamed from: build */
    public FlowPane build2() {
        FlowPane x2 = new FlowPane();
        applyTo(x2);
        return x2;
    }
}
