package javafx.scene.paint;

import java.util.Arrays;
import java.util.List;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/paint/RadialGradientBuilder.class */
public final class RadialGradientBuilder implements Builder<RadialGradient> {
    private double centerX;
    private double centerY;
    private CycleMethod cycleMethod;
    private double focusAngle;
    private double focusDistance;
    private boolean proportional = true;
    private double radius = 1.0d;
    private List<Stop> stops;

    protected RadialGradientBuilder() {
    }

    public static RadialGradientBuilder create() {
        return new RadialGradientBuilder();
    }

    public RadialGradientBuilder centerX(double x2) {
        this.centerX = x2;
        return this;
    }

    public RadialGradientBuilder centerY(double x2) {
        this.centerY = x2;
        return this;
    }

    public RadialGradientBuilder cycleMethod(CycleMethod x2) {
        this.cycleMethod = x2;
        return this;
    }

    public RadialGradientBuilder focusAngle(double x2) {
        this.focusAngle = x2;
        return this;
    }

    public RadialGradientBuilder focusDistance(double x2) {
        this.focusDistance = x2;
        return this;
    }

    public RadialGradientBuilder proportional(boolean x2) {
        this.proportional = x2;
        return this;
    }

    public RadialGradientBuilder radius(double x2) {
        this.radius = x2;
        return this;
    }

    public RadialGradientBuilder stops(List<Stop> x2) {
        this.stops = x2;
        return this;
    }

    public RadialGradientBuilder stops(Stop... x2) {
        return stops(Arrays.asList(x2));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    /* renamed from: build */
    public RadialGradient build2() {
        RadialGradient x2 = new RadialGradient(this.focusAngle, this.focusDistance, this.centerX, this.centerY, this.radius, this.proportional, this.cycleMethod, this.stops);
        return x2;
    }
}
