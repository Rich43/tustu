package javafx.scene.layout;

import javafx.geometry.Pos;
import javafx.scene.layout.VBoxBuilder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/layout/VBoxBuilder.class */
public class VBoxBuilder<B extends VBoxBuilder<B>> extends PaneBuilder<B> {
    private int __set;
    private Pos alignment;
    private boolean fillWidth;
    private double spacing;

    protected VBoxBuilder() {
    }

    public static VBoxBuilder<?> create() {
        return new VBoxBuilder<>();
    }

    public void applyTo(VBox x2) {
        super.applyTo((Pane) x2);
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setAlignment(this.alignment);
        }
        if ((set & 2) != 0) {
            x2.setFillWidth(this.fillWidth);
        }
        if ((set & 4) != 0) {
            x2.setSpacing(this.spacing);
        }
    }

    public B alignment(Pos x2) {
        this.alignment = x2;
        this.__set |= 1;
        return this;
    }

    public B fillWidth(boolean x2) {
        this.fillWidth = x2;
        this.__set |= 2;
        return this;
    }

    public B spacing(double x2) {
        this.spacing = x2;
        this.__set |= 4;
        return this;
    }

    @Override // javafx.scene.layout.PaneBuilder, javafx.scene.layout.RegionBuilder, javafx.util.Builder
    /* renamed from: build */
    public VBox build2() {
        VBox x2 = new VBox();
        applyTo(x2);
        return x2;
    }
}
