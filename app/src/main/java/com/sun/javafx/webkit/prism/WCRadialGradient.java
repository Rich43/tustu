package com.sun.javafx.webkit.prism;

import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.paint.Color;
import com.sun.prism.paint.RadialGradient;
import com.sun.prism.paint.Stop;
import com.sun.webkit.graphics.WCGradient;
import com.sun.webkit.graphics.WCPoint;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: jfxrt.jar:com/sun/javafx/webkit/prism/WCRadialGradient.class */
final class WCRadialGradient extends WCGradient<RadialGradient> {
    static final Comparator<Stop> COMPARATOR = (s1, s2) -> {
        float o1 = s1.getOffset();
        float o2 = s2.getOffset();
        if (o1 < o2) {
            return -1;
        }
        if (o1 > o2) {
            return 1;
        }
        return 0;
    };
    private final boolean reverse;
    private final WCPoint p1;
    private final WCPoint p2;
    private final float r1over;
    private final float r1;
    private final float r2;
    private final List<Stop> stops = new ArrayList();

    WCRadialGradient(WCPoint p1, float r1, WCPoint p2, float r2) {
        this.reverse = r1 < r2;
        this.p1 = this.reverse ? p2 : p1;
        this.p2 = this.reverse ? p1 : p2;
        this.r1 = this.reverse ? r2 : r1;
        this.r2 = this.reverse ? r1 : r2;
        this.r1over = this.r1 > 0.0f ? 1.0f / this.r1 : 0.0f;
    }

    @Override // com.sun.webkit.graphics.WCGradient
    protected void addStop(Color color, float offset) {
        if (this.reverse) {
            offset = 1.0f - offset;
        }
        this.stops.add(new Stop(color, (1.0f - offset) + (offset * this.r2 * this.r1over)));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.sun.webkit.graphics.WCGradient
    public RadialGradient getPlatformGradient() {
        Collections.sort(this.stops, COMPARATOR);
        float dx = this.p2.getX() - this.p1.getX();
        float dy = this.p2.getY() - this.p1.getY();
        return new RadialGradient(this.p1.getX(), this.p1.getY(), (float) ((Math.atan2(dy, dx) * 180.0d) / 3.141592653589793d), ((float) Math.sqrt((dx * dx) + (dy * dy))) * this.r1over, this.r1, BaseTransform.IDENTITY_TRANSFORM, isProportional(), getSpreadMethod() - 1, this.stops);
    }

    public String toString() {
        return toString(this, this.p1, this.p2, Float.valueOf(this.r1), this.stops);
    }

    static String toString(WCGradient g2, WCPoint p1, WCPoint p2, Float radius, List<Stop> stops) {
        StringBuilder sb = new StringBuilder(g2.getClass().getSimpleName());
        switch (g2.getSpreadMethod()) {
            case 1:
                sb.append("[spreadMethod=PAD");
                break;
            case 2:
                sb.append("[spreadMethod=REFLECT");
                break;
            case 3:
                sb.append("[spreadMethod=REPEAT");
                break;
        }
        sb.append(", proportional=").append(g2.isProportional());
        if (radius != null) {
            sb.append(", radius=").append((Object) radius);
        }
        sb.append(", x1=").append(p1.getX());
        sb.append(", y1=").append(p1.getY());
        sb.append(", x2=").append(p2.getX());
        sb.append(", y2=").append(p2.getY());
        sb.append(", stops=");
        int i2 = 0;
        while (i2 < stops.size()) {
            sb.append(i2 == 0 ? "[" : ", ");
            sb.append(stops.get(i2).getOffset()).append(CallSiteDescriptor.TOKEN_DELIMITER).append((Object) stops.get(i2).getColor());
            i2++;
        }
        return sb.append("]]").toString();
    }
}
