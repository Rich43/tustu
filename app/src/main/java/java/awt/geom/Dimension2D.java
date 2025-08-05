package java.awt.geom;

/* loaded from: rt.jar:java/awt/geom/Dimension2D.class */
public abstract class Dimension2D implements Cloneable {
    public abstract double getWidth();

    public abstract double getHeight();

    public abstract void setSize(double d2, double d3);

    protected Dimension2D() {
    }

    public void setSize(Dimension2D dimension2D) {
        setSize(dimension2D.getWidth(), dimension2D.getHeight());
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e2) {
            throw new InternalError(e2);
        }
    }
}
