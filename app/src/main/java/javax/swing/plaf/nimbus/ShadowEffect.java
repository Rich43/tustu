package javax.swing.plaf.nimbus;

import java.awt.Color;

/* loaded from: rt.jar:javax/swing/plaf/nimbus/ShadowEffect.class */
abstract class ShadowEffect extends Effect {
    protected Color color = Color.BLACK;
    protected float opacity = 0.75f;
    protected int angle = 135;
    protected int distance = 5;
    protected int spread = 0;
    protected int size = 5;

    ShadowEffect() {
    }

    Color getColor() {
        return this.color;
    }

    void setColor(Color color) {
        getColor();
        this.color = color;
    }

    @Override // javax.swing.plaf.nimbus.Effect
    float getOpacity() {
        return this.opacity;
    }

    void setOpacity(float f2) {
        getOpacity();
        this.opacity = f2;
    }

    int getAngle() {
        return this.angle;
    }

    void setAngle(int i2) {
        getAngle();
        this.angle = i2;
    }

    int getDistance() {
        return this.distance;
    }

    void setDistance(int i2) {
        getDistance();
        this.distance = i2;
    }

    int getSpread() {
        return this.spread;
    }

    void setSpread(int i2) {
        getSpread();
        this.spread = i2;
    }

    int getSize() {
        return this.size;
    }

    void setSize(int i2) {
        getSize();
        this.size = i2;
    }
}
