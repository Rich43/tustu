package javafx.scene.transform;

import javafx.scene.transform.AffineBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/transform/AffineBuilder.class */
public class AffineBuilder<B extends AffineBuilder<B>> implements Builder<Affine> {
    private int __set;
    private double mxx;
    private double mxy;
    private double mxz;
    private double myx;
    private double myy;
    private double myz;
    private double mzx;
    private double mzy;
    private double mzz;
    private double tx;
    private double ty;
    private double tz;

    protected AffineBuilder() {
    }

    public static AffineBuilder<?> create() {
        return new AffineBuilder<>();
    }

    private void __set(int i2) {
        this.__set |= 1 << i2;
    }

    public void applyTo(Affine x2) {
        int set = this.__set;
        while (set != 0) {
            int i2 = Integer.numberOfTrailingZeros(set);
            set &= (1 << i2) ^ (-1);
            switch (i2) {
                case 0:
                    x2.setMxx(this.mxx);
                    break;
                case 1:
                    x2.setMxy(this.mxy);
                    break;
                case 2:
                    x2.setMxz(this.mxz);
                    break;
                case 3:
                    x2.setMyx(this.myx);
                    break;
                case 4:
                    x2.setMyy(this.myy);
                    break;
                case 5:
                    x2.setMyz(this.myz);
                    break;
                case 6:
                    x2.setMzx(this.mzx);
                    break;
                case 7:
                    x2.setMzy(this.mzy);
                    break;
                case 8:
                    x2.setMzz(this.mzz);
                    break;
                case 9:
                    x2.setTx(this.tx);
                    break;
                case 10:
                    x2.setTy(this.ty);
                    break;
                case 11:
                    x2.setTz(this.tz);
                    break;
            }
        }
    }

    public B mxx(double x2) {
        this.mxx = x2;
        __set(0);
        return this;
    }

    public B mxy(double x2) {
        this.mxy = x2;
        __set(1);
        return this;
    }

    public B mxz(double x2) {
        this.mxz = x2;
        __set(2);
        return this;
    }

    public B myx(double x2) {
        this.myx = x2;
        __set(3);
        return this;
    }

    public B myy(double x2) {
        this.myy = x2;
        __set(4);
        return this;
    }

    public B myz(double x2) {
        this.myz = x2;
        __set(5);
        return this;
    }

    public B mzx(double x2) {
        this.mzx = x2;
        __set(6);
        return this;
    }

    public B mzy(double x2) {
        this.mzy = x2;
        __set(7);
        return this;
    }

    public B mzz(double x2) {
        this.mzz = x2;
        __set(8);
        return this;
    }

    public B tx(double x2) {
        this.tx = x2;
        __set(9);
        return this;
    }

    public B ty(double x2) {
        this.ty = x2;
        __set(10);
        return this;
    }

    public B tz(double x2) {
        this.tz = x2;
        __set(11);
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    public Affine build() {
        Affine x2 = new Affine();
        applyTo(x2);
        return x2;
    }
}
