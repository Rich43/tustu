package javafx.scene.paint;

import com.sun.javafx.scene.paint.GradientUtils;
import com.sun.javafx.tk.Toolkit;
import java.util.List;
import javafx.beans.NamedArg;

/* loaded from: jfxrt.jar:javafx/scene/paint/RadialGradient.class */
public final class RadialGradient extends Paint {
    private double focusAngle;
    private double focusDistance;
    private double centerX;
    private double centerY;
    private double radius;
    private boolean proportional;
    private CycleMethod cycleMethod;
    private List<Stop> stops;
    private final boolean opaque;
    private Object platformPaint;
    private int hash;

    public final double getFocusAngle() {
        return this.focusAngle;
    }

    public final double getFocusDistance() {
        return this.focusDistance;
    }

    public final double getCenterX() {
        return this.centerX;
    }

    public final double getCenterY() {
        return this.centerY;
    }

    public final double getRadius() {
        return this.radius;
    }

    public final boolean isProportional() {
        return this.proportional;
    }

    public final CycleMethod getCycleMethod() {
        return this.cycleMethod;
    }

    public final List<Stop> getStops() {
        return this.stops;
    }

    @Override // javafx.scene.paint.Paint
    public final boolean isOpaque() {
        return this.opaque;
    }

    public RadialGradient(@NamedArg("focusAngle") double focusAngle, @NamedArg("focusDistance") double focusDistance, @NamedArg("centerX") double centerX, @NamedArg("centerY") double centerY, @NamedArg(value = "radius", defaultValue = "1") double radius, @NamedArg(value = "proportional", defaultValue = "true") boolean proportional, @NamedArg("cycleMethod") CycleMethod cycleMethod, @NamedArg("stops") Stop... stops) {
        this.focusAngle = focusAngle;
        this.focusDistance = focusDistance;
        this.centerX = centerX;
        this.centerY = centerY;
        this.radius = radius;
        this.proportional = proportional;
        this.cycleMethod = cycleMethod == null ? CycleMethod.NO_CYCLE : cycleMethod;
        this.stops = Stop.normalize(stops);
        this.opaque = determineOpacity();
    }

    public RadialGradient(@NamedArg("focusAngle") double focusAngle, @NamedArg("focusDistance") double focusDistance, @NamedArg("centerX") double centerX, @NamedArg("centerY") double centerY, @NamedArg(value = "radius", defaultValue = "1") double radius, @NamedArg(value = "proportional", defaultValue = "true") boolean proportional, @NamedArg("cycleMethod") CycleMethod cycleMethod, @NamedArg("stops") List<Stop> stops) {
        this.focusAngle = focusAngle;
        this.focusDistance = focusDistance;
        this.centerX = centerX;
        this.centerY = centerY;
        this.radius = radius;
        this.proportional = proportional;
        this.cycleMethod = cycleMethod == null ? CycleMethod.NO_CYCLE : cycleMethod;
        this.stops = Stop.normalize(stops);
        this.opaque = determineOpacity();
    }

    private boolean determineOpacity() {
        int numStops = this.stops.size();
        for (int i2 = 0; i2 < numStops; i2++) {
            if (!this.stops.get(i2).getColor().isOpaque()) {
                return false;
            }
        }
        return true;
    }

    @Override // javafx.scene.paint.Paint
    Object acc_getPlatformPaint() {
        if (this.platformPaint == null) {
            this.platformPaint = Toolkit.getToolkit().getPaint((Paint) this);
        }
        return this.platformPaint;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof RadialGradient) {
            RadialGradient other = (RadialGradient) obj;
            return this.focusAngle == other.focusAngle && this.focusDistance == other.focusDistance && this.centerX == other.centerX && this.centerY == other.centerY && this.radius == other.radius && this.proportional == other.proportional && this.cycleMethod == other.cycleMethod && this.stops.equals(other.stops);
        }
        return false;
    }

    public int hashCode() {
        if (this.hash == 0) {
            long bits = (37 * 17) + Double.doubleToLongBits(this.focusAngle);
            long bits2 = (37 * ((37 * ((37 * ((37 * ((37 * ((37 * bits) + Double.doubleToLongBits(this.focusDistance))) + Double.doubleToLongBits(this.centerX))) + Double.doubleToLongBits(this.centerY))) + Double.doubleToLongBits(this.radius))) + (this.proportional ? 1231 : 1237))) + this.cycleMethod.hashCode();
            for (Stop stop : this.stops) {
                bits2 = (37 * bits2) + stop.hashCode();
            }
            this.hash = (int) (bits2 ^ (bits2 >> 32));
        }
        return this.hash;
    }

    public String toString() {
        StringBuilder s2 = new StringBuilder("radial-gradient(focus-angle ").append(this.focusAngle).append("deg, focus-distance ").append(this.focusDistance * 100.0d).append("% , center ").append(GradientUtils.lengthToString(this.centerX, this.proportional)).append(" ").append(GradientUtils.lengthToString(this.centerY, this.proportional)).append(", radius ").append(GradientUtils.lengthToString(this.radius, this.proportional)).append(", ");
        switch (this.cycleMethod) {
            case REFLECT:
                s2.append("reflect").append(", ");
                break;
            case REPEAT:
                s2.append("repeat").append(", ");
                break;
        }
        for (Stop stop : this.stops) {
            s2.append((Object) stop).append(", ");
        }
        s2.delete(s2.length() - 2, s2.length());
        s2.append(")");
        return s2.toString();
    }

    public static RadialGradient valueOf(String value) {
        GradientUtils.Point centerX;
        GradientUtils.Point centerY;
        if (value == null) {
            throw new NullPointerException("gradient must be specified");
        }
        if (value.startsWith("radial-gradient(")) {
            if (!value.endsWith(")")) {
                throw new IllegalArgumentException("Invalid gradient specification, must end with \")\"");
            }
            value = value.substring("radial-gradient(".length(), value.length() - ")".length());
        }
        GradientUtils.Parser parser = new GradientUtils.Parser(value);
        if (parser.getSize() < 2) {
            throw new IllegalArgumentException("Invalid gradient specification");
        }
        double angle = 0.0d;
        double distance = 0.0d;
        String[] tokens = parser.splitCurrentToken();
        if ("focus-angle".equals(tokens[0])) {
            GradientUtils.Parser.checkNumberOfArguments(tokens, 1);
            angle = GradientUtils.Parser.parseAngle(tokens[1]);
            parser.shift();
        }
        String[] tokens2 = parser.splitCurrentToken();
        if ("focus-distance".equals(tokens2[0])) {
            GradientUtils.Parser.checkNumberOfArguments(tokens2, 1);
            distance = GradientUtils.Parser.parsePercentage(tokens2[1]);
            parser.shift();
        }
        String[] tokens3 = parser.splitCurrentToken();
        if ("center".equals(tokens3[0])) {
            GradientUtils.Parser.checkNumberOfArguments(tokens3, 2);
            centerX = parser.parsePoint(tokens3[1]);
            centerY = parser.parsePoint(tokens3[2]);
            parser.shift();
        } else {
            centerX = GradientUtils.Point.MIN;
            centerY = GradientUtils.Point.MIN;
        }
        String[] tokens4 = parser.splitCurrentToken();
        if ("radius".equals(tokens4[0])) {
            GradientUtils.Parser.checkNumberOfArguments(tokens4, 1);
            GradientUtils.Point radius = parser.parsePoint(tokens4[1]);
            parser.shift();
            CycleMethod method = CycleMethod.NO_CYCLE;
            String currentToken = parser.getCurrentToken();
            if ("repeat".equals(currentToken)) {
                method = CycleMethod.REPEAT;
                parser.shift();
            } else if ("reflect".equals(currentToken)) {
                method = CycleMethod.REFLECT;
                parser.shift();
            }
            Stop[] stops = parser.parseStops(radius.proportional, radius.value);
            return new RadialGradient(angle, distance, centerX.value, centerY.value, radius.value, radius.proportional, method, stops);
        }
        throw new IllegalArgumentException("Invalid gradient specification: radius must be specified");
    }
}
