package com.sun.javafx.webkit.prism;

import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.paint.Color;
import com.sun.prism.paint.LinearGradient;
import com.sun.prism.paint.Stop;
import com.sun.webkit.graphics.WCGradient;
import com.sun.webkit.graphics.WCPoint;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* loaded from: jfxrt.jar:com/sun/javafx/webkit/prism/WCLinearGradient.class */
final class WCLinearGradient extends WCGradient<LinearGradient> {
    private final WCPoint p1;
    private final WCPoint p2;
    private final List<Stop> stops = new ArrayList();

    WCLinearGradient(WCPoint p1, WCPoint p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    @Override // com.sun.webkit.graphics.WCGradient
    protected void addStop(Color color, float offset) {
        this.stops.add(new Stop(color, offset));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.sun.webkit.graphics.WCGradient
    public LinearGradient getPlatformGradient() {
        Collections.sort(this.stops, WCRadialGradient.COMPARATOR);
        return new LinearGradient(this.p1.getX(), this.p1.getY(), this.p2.getX(), this.p2.getY(), BaseTransform.IDENTITY_TRANSFORM, isProportional(), getSpreadMethod() - 1, this.stops);
    }

    public String toString() {
        return WCRadialGradient.toString(this, this.p1, this.p2, null, this.stops);
    }
}
