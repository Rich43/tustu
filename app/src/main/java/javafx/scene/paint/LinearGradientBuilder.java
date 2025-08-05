package javafx.scene.paint;

import java.util.Arrays;
import java.util.List;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/paint/LinearGradientBuilder.class */
public final class LinearGradientBuilder implements Builder<LinearGradient> {
    private CycleMethod cycleMethod;
    private double endX = 1.0d;
    private double endY = 1.0d;
    private boolean proportional = true;
    private double startX;
    private double startY;
    private List<Stop> stops;

    protected LinearGradientBuilder() {
    }

    public static LinearGradientBuilder create() {
        return new LinearGradientBuilder();
    }

    public LinearGradientBuilder cycleMethod(CycleMethod x2) {
        this.cycleMethod = x2;
        return this;
    }

    public LinearGradientBuilder endX(double x2) {
        this.endX = x2;
        return this;
    }

    public LinearGradientBuilder endY(double x2) {
        this.endY = x2;
        return this;
    }

    public LinearGradientBuilder proportional(boolean x2) {
        this.proportional = x2;
        return this;
    }

    public LinearGradientBuilder startX(double x2) {
        this.startX = x2;
        return this;
    }

    public LinearGradientBuilder startY(double x2) {
        this.startY = x2;
        return this;
    }

    public LinearGradientBuilder stops(List<Stop> x2) {
        this.stops = x2;
        return this;
    }

    public LinearGradientBuilder stops(Stop... x2) {
        return stops(Arrays.asList(x2));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    /* renamed from: build */
    public LinearGradient build2() {
        LinearGradient x2 = new LinearGradient(this.startX, this.startY, this.endX, this.endY, this.proportional, this.cycleMethod, this.stops);
        return x2;
    }
}
