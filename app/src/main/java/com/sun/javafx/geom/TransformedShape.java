package com.sun.javafx.geom;

import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.NoninvertibleTransformException;

/* loaded from: jfxrt.jar:com/sun/javafx/geom/TransformedShape.class */
public abstract class TransformedShape extends Shape {
    protected final Shape delegate;
    private Shape cachedTransformedShape;

    public abstract BaseTransform getTransformNoClone();

    public abstract BaseTransform adjust(BaseTransform baseTransform);

    public static TransformedShape transformedShape(Shape original, BaseTransform tx) {
        if (tx.isTranslateOrIdentity()) {
            return translatedShape(original, tx.getMxt(), tx.getMyt());
        }
        return new General(original, tx.copy());
    }

    public static TransformedShape translatedShape(Shape original, double tx, double ty) {
        return new Translate(original, (float) tx, (float) ty);
    }

    protected TransformedShape(Shape delegate) {
        this.delegate = delegate;
    }

    public Shape getDelegateNoClone() {
        return this.delegate;
    }

    protected Point2D untransform(float x2, float y2) {
        Point2D p2 = new Point2D(x2, y2);
        try {
            p2 = getTransformNoClone().inverseTransform(p2, p2);
        } catch (NoninvertibleTransformException e2) {
        }
        return p2;
    }

    protected BaseBounds untransformedBounds(float x2, float y2, float w2, float h2) {
        RectBounds b2 = new RectBounds(x2, y2, x2 + w2, y2 + h2);
        try {
            return getTransformNoClone().inverseTransform(b2, b2);
        } catch (NoninvertibleTransformException e2) {
            return b2.makeEmpty();
        }
    }

    @Override // com.sun.javafx.geom.Shape
    public RectBounds getBounds() {
        float[] box = new float[4];
        Shape.accumulate(box, this.delegate, getTransformNoClone());
        return new RectBounds(box[0], box[1], box[2], box[3]);
    }

    @Override // com.sun.javafx.geom.Shape
    public boolean contains(float x2, float y2) {
        return this.delegate.contains(untransform(x2, y2));
    }

    private Shape getCachedTransformedShape() {
        if (this.cachedTransformedShape == null) {
            this.cachedTransformedShape = copy();
        }
        return this.cachedTransformedShape;
    }

    @Override // com.sun.javafx.geom.Shape
    public boolean intersects(float x2, float y2, float w2, float h2) {
        return getCachedTransformedShape().intersects(x2, y2, w2, h2);
    }

    @Override // com.sun.javafx.geom.Shape
    public boolean contains(float x2, float y2, float w2, float h2) {
        return getCachedTransformedShape().contains(x2, y2, w2, h2);
    }

    @Override // com.sun.javafx.geom.Shape
    public PathIterator getPathIterator(BaseTransform transform) {
        return this.delegate.getPathIterator(adjust(transform));
    }

    @Override // com.sun.javafx.geom.Shape
    public PathIterator getPathIterator(BaseTransform transform, float flatness) {
        return this.delegate.getPathIterator(adjust(transform), flatness);
    }

    @Override // com.sun.javafx.geom.Shape
    public Shape copy() {
        return getTransformNoClone().createTransformedShape(this.delegate);
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/geom/TransformedShape$General.class */
    static final class General extends TransformedShape {
        BaseTransform transform;

        General(Shape delegate, BaseTransform transform) {
            super(delegate);
            this.transform = transform;
        }

        @Override // com.sun.javafx.geom.TransformedShape
        public BaseTransform getTransformNoClone() {
            return this.transform;
        }

        @Override // com.sun.javafx.geom.TransformedShape
        public BaseTransform adjust(BaseTransform transform) {
            if (transform == null || transform.isIdentity()) {
                return this.transform.copy();
            }
            return transform.copy().deriveWithConcatenation(this.transform);
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/geom/TransformedShape$Translate.class */
    static final class Translate extends TransformedShape {
        private final float tx;
        private final float ty;
        private BaseTransform cachedTx;

        public Translate(Shape delegate, float tx, float ty) {
            super(delegate);
            this.tx = tx;
            this.ty = ty;
        }

        @Override // com.sun.javafx.geom.TransformedShape
        public BaseTransform getTransformNoClone() {
            if (this.cachedTx == null) {
                this.cachedTx = BaseTransform.getTranslateInstance(this.tx, this.ty);
            }
            return this.cachedTx;
        }

        @Override // com.sun.javafx.geom.TransformedShape
        public BaseTransform adjust(BaseTransform transform) {
            if (transform == null || transform.isIdentity()) {
                return BaseTransform.getTranslateInstance(this.tx, this.ty);
            }
            return transform.copy().deriveWithTranslation(this.tx, this.ty);
        }

        @Override // com.sun.javafx.geom.TransformedShape, com.sun.javafx.geom.Shape
        public RectBounds getBounds() {
            RectBounds rb = this.delegate.getBounds();
            rb.setBounds(rb.getMinX() + this.tx, rb.getMinY() + this.ty, rb.getMaxX() + this.tx, rb.getMaxY() + this.ty);
            return rb;
        }

        @Override // com.sun.javafx.geom.TransformedShape, com.sun.javafx.geom.Shape
        public boolean contains(float x2, float y2) {
            return this.delegate.contains(x2 - this.tx, y2 - this.ty);
        }

        @Override // com.sun.javafx.geom.TransformedShape, com.sun.javafx.geom.Shape
        public boolean intersects(float x2, float y2, float w2, float h2) {
            return this.delegate.intersects(x2 - this.tx, y2 - this.ty, w2, h2);
        }

        @Override // com.sun.javafx.geom.TransformedShape, com.sun.javafx.geom.Shape
        public boolean contains(float x2, float y2, float w2, float h2) {
            return this.delegate.contains(x2 - this.tx, y2 - this.ty, w2, h2);
        }
    }
}
