package com.sun.prism.paint;

import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.Image;
import com.sun.prism.paint.Paint;

/* loaded from: jfxrt.jar:com/sun/prism/paint/ImagePattern.class */
public final class ImagePattern extends Paint {
    private final Image image;

    /* renamed from: x, reason: collision with root package name */
    private final float f12028x;

    /* renamed from: y, reason: collision with root package name */
    private final float f12029y;
    private final float width;
    private final float height;
    private final BaseTransform patternTransform;

    public ImagePattern(Image image, float x2, float y2, float width, float height, BaseTransform patternTransform, boolean proportional, boolean isMutable) {
        super(Paint.Type.IMAGE_PATTERN, proportional, isMutable);
        if (image == null) {
            throw new IllegalArgumentException("Image must be non-null");
        }
        this.image = image;
        this.f12028x = x2;
        this.f12029y = y2;
        this.width = width;
        this.height = height;
        if (patternTransform != null) {
            this.patternTransform = patternTransform.copy();
        } else {
            this.patternTransform = BaseTransform.IDENTITY_TRANSFORM;
        }
    }

    public ImagePattern(Image image, float x2, float y2, float width, float height, boolean proportional, boolean isMutable) {
        this(image, x2, y2, width, height, null, proportional, isMutable);
    }

    public Image getImage() {
        return this.image;
    }

    public float getX() {
        return this.f12028x;
    }

    public float getY() {
        return this.f12029y;
    }

    public float getWidth() {
        return this.width;
    }

    public float getHeight() {
        return this.height;
    }

    public BaseTransform getPatternTransformNoClone() {
        return this.patternTransform;
    }

    @Override // com.sun.prism.paint.Paint
    public boolean isOpaque() {
        return this.image.isOpaque();
    }
}
