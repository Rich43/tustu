package javafx.scene.layout;

import javafx.geometry.Pos;
import javafx.scene.layout.HBoxBuilder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/layout/HBoxBuilder.class */
public class HBoxBuilder<B extends HBoxBuilder<B>> extends PaneBuilder<B> {
    private int __set;
    private Pos alignment;
    private boolean fillHeight;
    private double spacing;

    protected HBoxBuilder() {
    }

    public static HBoxBuilder<?> create() {
        return new HBoxBuilder<>();
    }

    public void applyTo(HBox x2) {
        super.applyTo((Pane) x2);
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setAlignment(this.alignment);
        }
        if ((set & 2) != 0) {
            x2.setFillHeight(this.fillHeight);
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

    public B fillHeight(boolean x2) {
        this.fillHeight = x2;
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
    public HBox build2() {
        HBox x2 = new HBox();
        applyTo(x2);
        return x2;
    }
}
