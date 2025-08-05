package javafx.scene.paint;

import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/paint/StopBuilder.class */
public final class StopBuilder implements Builder<Stop> {
    private Color color = Color.BLACK;
    private double offset;

    protected StopBuilder() {
    }

    public static StopBuilder create() {
        return new StopBuilder();
    }

    public StopBuilder color(Color x2) {
        this.color = x2;
        return this;
    }

    public StopBuilder offset(double x2) {
        this.offset = x2;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    public Stop build() {
        Stop x2 = new Stop(this.offset, this.color);
        return x2;
    }
}
