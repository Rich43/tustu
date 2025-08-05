package com.sun.prism.paint;

import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.paint.Paint;
import java.util.List;

/* loaded from: jfxrt.jar:com/sun/prism/paint/RadialGradient.class */
public final class RadialGradient extends Gradient {
    private final float centerX;
    private final float centerY;
    private final float focusAngle;
    private final float focusDistance;
    private final float radius;

    public RadialGradient(float centerX, float centerY, float focusAngle, float focusDistance, float radius, BaseTransform gradientTransform, boolean proportional, int spreadMethod, List<Stop> stops) {
        super(Paint.Type.RADIAL_GRADIENT, gradientTransform, proportional, spreadMethod, stops);
        this.centerX = centerX;
        this.centerY = centerY;
        this.focusAngle = focusAngle;
        this.focusDistance = focusDistance;
        this.radius = radius;
    }

    public float getCenterX() {
        return this.centerX;
    }

    public float getCenterY() {
        return this.centerY;
    }

    public float getFocusAngle() {
        return this.focusAngle;
    }

    public float getFocusDistance() {
        return this.focusDistance;
    }

    public float getRadius() {
        return this.radius;
    }

    public String toString() {
        return "RadialGradient: FocusAngle: " + this.focusAngle + " FocusDistance: " + this.focusDistance + " CenterX: " + this.centerX + " CenterY " + this.centerY + " Radius: " + this.radius + "stops:" + ((Object) getStops());
    }
}
