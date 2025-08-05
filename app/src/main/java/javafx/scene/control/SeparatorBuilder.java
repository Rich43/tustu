package javafx.scene.control;

import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.scene.control.SeparatorBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/control/SeparatorBuilder.class */
public class SeparatorBuilder<B extends SeparatorBuilder<B>> extends ControlBuilder<B> implements Builder<Separator> {
    private int __set;
    private HPos halignment;
    private Orientation orientation;
    private VPos valignment;

    protected SeparatorBuilder() {
    }

    public static SeparatorBuilder<?> create() {
        return new SeparatorBuilder<>();
    }

    public void applyTo(Separator x2) {
        super.applyTo((Control) x2);
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setHalignment(this.halignment);
        }
        if ((set & 2) != 0) {
            x2.setOrientation(this.orientation);
        }
        if ((set & 4) != 0) {
            x2.setValignment(this.valignment);
        }
    }

    public B halignment(HPos x2) {
        this.halignment = x2;
        this.__set |= 1;
        return this;
    }

    public B orientation(Orientation x2) {
        this.orientation = x2;
        this.__set |= 2;
        return this;
    }

    public B valignment(VPos x2) {
        this.valignment = x2;
        this.__set |= 4;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    /* renamed from: build */
    public Separator build2() {
        Separator x2 = new Separator();
        applyTo(x2);
        return x2;
    }
}
