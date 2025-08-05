package javafx.scene.control;

import java.util.Arrays;
import java.util.Collection;
import javafx.scene.control.ToggleGroupBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/control/ToggleGroupBuilder.class */
public class ToggleGroupBuilder<B extends ToggleGroupBuilder<B>> implements Builder<ToggleGroup> {
    private boolean __set;
    private Collection<? extends Toggle> toggles;

    protected ToggleGroupBuilder() {
    }

    public static ToggleGroupBuilder<?> create() {
        return new ToggleGroupBuilder<>();
    }

    public void applyTo(ToggleGroup x2) {
        if (this.__set) {
            x2.getToggles().addAll(this.toggles);
        }
    }

    public B toggles(Collection<? extends Toggle> x2) {
        this.toggles = x2;
        this.__set = true;
        return this;
    }

    public B toggles(Toggle... toggleArr) {
        return (B) toggles(Arrays.asList(toggleArr));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    /* renamed from: build */
    public ToggleGroup build2() {
        ToggleGroup x2 = new ToggleGroup();
        applyTo(x2);
        return x2;
    }
}
