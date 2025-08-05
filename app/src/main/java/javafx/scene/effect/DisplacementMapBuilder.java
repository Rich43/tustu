package javafx.scene.effect;

import javafx.scene.effect.DisplacementMapBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/effect/DisplacementMapBuilder.class */
public class DisplacementMapBuilder<B extends DisplacementMapBuilder<B>> implements Builder<DisplacementMap> {
    private int __set;
    private Effect input;
    private FloatMap mapData;
    private double offsetX;
    private double offsetY;
    private double scaleX;
    private double scaleY;
    private boolean wrap;

    protected DisplacementMapBuilder() {
    }

    public static DisplacementMapBuilder<?> create() {
        return new DisplacementMapBuilder<>();
    }

    public void applyTo(DisplacementMap x2) {
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setInput(this.input);
        }
        if ((set & 2) != 0) {
            x2.setMapData(this.mapData);
        }
        if ((set & 4) != 0) {
            x2.setOffsetX(this.offsetX);
        }
        if ((set & 8) != 0) {
            x2.setOffsetY(this.offsetY);
        }
        if ((set & 16) != 0) {
            x2.setScaleX(this.scaleX);
        }
        if ((set & 32) != 0) {
            x2.setScaleY(this.scaleY);
        }
        if ((set & 64) != 0) {
            x2.setWrap(this.wrap);
        }
    }

    public B input(Effect x2) {
        this.input = x2;
        this.__set |= 1;
        return this;
    }

    public B mapData(FloatMap x2) {
        this.mapData = x2;
        this.__set |= 2;
        return this;
    }

    public B offsetX(double x2) {
        this.offsetX = x2;
        this.__set |= 4;
        return this;
    }

    public B offsetY(double x2) {
        this.offsetY = x2;
        this.__set |= 8;
        return this;
    }

    public B scaleX(double x2) {
        this.scaleX = x2;
        this.__set |= 16;
        return this;
    }

    public B scaleY(double x2) {
        this.scaleY = x2;
        this.__set |= 32;
        return this;
    }

    public B wrap(boolean x2) {
        this.wrap = x2;
        this.__set |= 64;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    /* renamed from: build */
    public DisplacementMap build2() {
        DisplacementMap x2 = new DisplacementMap();
        applyTo(x2);
        return x2;
    }
}
