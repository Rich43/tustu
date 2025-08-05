package com.sun.javafx.geom;

import com.sun.javafx.geom.BaseBounds;

/* loaded from: jfxrt.jar:com/sun/javafx/geom/RectBounds.class */
public final class RectBounds extends BaseBounds {
    private float minX;
    private float maxX;
    private float minY;
    private float maxY;

    public RectBounds() {
        this.minY = 0.0f;
        this.minX = 0.0f;
        this.maxY = -1.0f;
        this.maxX = -1.0f;
    }

    @Override // com.sun.javafx.geom.BaseBounds
    public BaseBounds copy() {
        return new RectBounds(this.minX, this.minY, this.maxX, this.maxY);
    }

    public RectBounds(float minX, float minY, float maxX, float maxY) {
        setBounds(minX, minY, maxX, maxY);
    }

    public RectBounds(RectBounds other) {
        setBounds(other);
    }

    public RectBounds(Rectangle other) {
        setBounds(other.f11913x, other.f11914y, other.f11913x + other.width, other.f11914y + other.height);
    }

    @Override // com.sun.javafx.geom.BaseBounds
    public BaseBounds.BoundsType getBoundsType() {
        return BaseBounds.BoundsType.RECTANGLE;
    }

    @Override // com.sun.javafx.geom.BaseBounds
    public boolean is2D() {
        return true;
    }

    @Override // com.sun.javafx.geom.BaseBounds
    public float getWidth() {
        return this.maxX - this.minX;
    }

    @Override // com.sun.javafx.geom.BaseBounds
    public float getHeight() {
        return this.maxY - this.minY;
    }

    @Override // com.sun.javafx.geom.BaseBounds
    public float getDepth() {
        return 0.0f;
    }

    @Override // com.sun.javafx.geom.BaseBounds
    public float getMinX() {
        return this.minX;
    }

    public void setMinX(float minX) {
        this.minX = minX;
    }

    @Override // com.sun.javafx.geom.BaseBounds
    public float getMinY() {
        return this.minY;
    }

    public void setMinY(float minY) {
        this.minY = minY;
    }

    @Override // com.sun.javafx.geom.BaseBounds
    public float getMinZ() {
        return 0.0f;
    }

    @Override // com.sun.javafx.geom.BaseBounds
    public float getMaxX() {
        return this.maxX;
    }

    public void setMaxX(float maxX) {
        this.maxX = maxX;
    }

    @Override // com.sun.javafx.geom.BaseBounds
    public float getMaxY() {
        return this.maxY;
    }

    public void setMaxY(float maxY) {
        this.maxY = maxY;
    }

    @Override // com.sun.javafx.geom.BaseBounds
    public float getMaxZ() {
        return 0.0f;
    }

    @Override // com.sun.javafx.geom.BaseBounds
    public Vec2f getMin(Vec2f min) {
        if (min == null) {
            min = new Vec2f();
        }
        min.f11928x = this.minX;
        min.f11929y = this.minY;
        return min;
    }

    @Override // com.sun.javafx.geom.BaseBounds
    public Vec2f getMax(Vec2f max) {
        if (max == null) {
            max = new Vec2f();
        }
        max.f11928x = this.maxX;
        max.f11929y = this.maxY;
        return max;
    }

    @Override // com.sun.javafx.geom.BaseBounds
    public Vec3f getMin(Vec3f min) {
        if (min == null) {
            min = new Vec3f();
        }
        min.f11933x = this.minX;
        min.f11934y = this.minY;
        min.f11935z = 0.0f;
        return min;
    }

    @Override // com.sun.javafx.geom.BaseBounds
    public Vec3f getMax(Vec3f max) {
        if (max == null) {
            max = new Vec3f();
        }
        max.f11933x = this.maxX;
        max.f11934y = this.maxY;
        max.f11935z = 0.0f;
        return max;
    }

    @Override // com.sun.javafx.geom.BaseBounds
    public BaseBounds deriveWithUnion(BaseBounds other) {
        if (other.getBoundsType() == BaseBounds.BoundsType.RECTANGLE) {
            RectBounds rb = (RectBounds) other;
            unionWith(rb);
            return this;
        }
        if (other.getBoundsType() == BaseBounds.BoundsType.BOX) {
            BoxBounds bb2 = new BoxBounds((BoxBounds) other);
            bb2.unionWith(this);
            return bb2;
        }
        throw new UnsupportedOperationException("Unknown BoundsType");
    }

    @Override // com.sun.javafx.geom.BaseBounds
    public BaseBounds deriveWithNewBounds(Rectangle other) {
        if (other.width < 0 || other.height < 0) {
            return makeEmpty();
        }
        setBounds(other.f11913x, other.f11914y, other.f11913x + other.width, other.f11914y + other.height);
        return this;
    }

    @Override // com.sun.javafx.geom.BaseBounds
    public BaseBounds deriveWithNewBounds(BaseBounds other) {
        if (other.isEmpty()) {
            return makeEmpty();
        }
        if (other.getBoundsType() != BaseBounds.BoundsType.RECTANGLE) {
            if (other.getBoundsType() == BaseBounds.BoundsType.BOX) {
                return new BoxBounds((BoxBounds) other);
            }
            throw new UnsupportedOperationException("Unknown BoundsType");
        }
        RectBounds rb = (RectBounds) other;
        this.minX = rb.getMinX();
        this.minY = rb.getMinY();
        this.maxX = rb.getMaxX();
        this.maxY = rb.getMaxY();
        return this;
    }

    @Override // com.sun.javafx.geom.BaseBounds
    public BaseBounds deriveWithNewBounds(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        if (maxX < minX || maxY < minY || maxZ < minZ) {
            return makeEmpty();
        }
        if (minZ == 0.0f && maxZ == 0.0f) {
            this.minX = minX;
            this.minY = minY;
            this.maxX = maxX;
            this.maxY = maxY;
            return this;
        }
        return new BoxBounds(minX, minY, minZ, maxX, maxY, maxZ);
    }

    @Override // com.sun.javafx.geom.BaseBounds
    public BaseBounds deriveWithNewBoundsAndSort(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        if (minZ == 0.0f && maxZ == 0.0f) {
            setBoundsAndSort(minX, minY, minZ, maxX, maxY, maxZ);
            return this;
        }
        BaseBounds bb2 = new BoxBounds();
        bb2.setBoundsAndSort(minX, minY, minZ, maxX, maxY, maxZ);
        return bb2;
    }

    public final void setBounds(RectBounds other) {
        this.minX = other.getMinX();
        this.minY = other.getMinY();
        this.maxX = other.getMaxX();
        this.maxY = other.getMaxY();
    }

    public final void setBounds(float minX, float minY, float maxX, float maxY) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    public void setBoundsAndSort(float minX, float minY, float maxX, float maxY) {
        setBounds(minX, minY, maxX, maxY);
        sortMinMax();
    }

    @Override // com.sun.javafx.geom.BaseBounds
    public void setBoundsAndSort(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        if (minZ != 0.0f || maxZ != 0.0f) {
            throw new UnsupportedOperationException("Unknown BoundsType");
        }
        setBounds(minX, minY, maxX, maxY);
        sortMinMax();
    }

    @Override // com.sun.javafx.geom.BaseBounds
    public void setBoundsAndSort(Point2D p1, Point2D p2) {
        setBoundsAndSort(p1.f11907x, p1.f11908y, p2.f11907x, p2.f11908y);
    }

    @Override // com.sun.javafx.geom.BaseBounds
    public RectBounds flattenInto(RectBounds bounds) {
        if (bounds == null) {
            bounds = new RectBounds();
        }
        if (isEmpty()) {
            return bounds.makeEmpty();
        }
        bounds.setBounds(this.minX, this.minY, this.maxX, this.maxY);
        return bounds;
    }

    public void unionWith(RectBounds other) {
        if (other.isEmpty()) {
            return;
        }
        if (isEmpty()) {
            setBounds(other);
            return;
        }
        this.minX = Math.min(this.minX, other.getMinX());
        this.minY = Math.min(this.minY, other.getMinY());
        this.maxX = Math.max(this.maxX, other.getMaxX());
        this.maxY = Math.max(this.maxY, other.getMaxY());
    }

    public void unionWith(float minX, float minY, float maxX, float maxY) {
        if (maxX < minX || maxY < minY) {
            return;
        }
        if (isEmpty()) {
            setBounds(minX, minY, maxX, maxY);
            return;
        }
        this.minX = Math.min(this.minX, minX);
        this.minY = Math.min(this.minY, minY);
        this.maxX = Math.max(this.maxX, maxX);
        this.maxY = Math.max(this.maxY, maxY);
    }

    @Override // com.sun.javafx.geom.BaseBounds
    public void add(float x2, float y2, float z2) {
        if (z2 != 0.0f) {
            throw new UnsupportedOperationException("Unknown BoundsType");
        }
        unionWith(x2, y2, x2, y2);
    }

    public void add(float x2, float y2) {
        unionWith(x2, y2, x2, y2);
    }

    @Override // com.sun.javafx.geom.BaseBounds
    public void add(Point2D p2) {
        add(p2.f11907x, p2.f11908y);
    }

    @Override // com.sun.javafx.geom.BaseBounds
    public void intersectWith(BaseBounds other) {
        if (isEmpty()) {
            return;
        }
        if (other.isEmpty()) {
            makeEmpty();
            return;
        }
        this.minX = Math.max(this.minX, other.getMinX());
        this.minY = Math.max(this.minY, other.getMinY());
        this.maxX = Math.min(this.maxX, other.getMaxX());
        this.maxY = Math.min(this.maxY, other.getMaxY());
    }

    @Override // com.sun.javafx.geom.BaseBounds
    public void intersectWith(Rectangle other) {
        float x2 = other.f11913x;
        float y2 = other.f11914y;
        intersectWith(x2, y2, x2 + other.width, y2 + other.height);
    }

    public void intersectWith(float minX, float minY, float maxX, float maxY) {
        if (isEmpty()) {
            return;
        }
        if (maxX < minX || maxY < minY) {
            makeEmpty();
            return;
        }
        this.minX = Math.max(this.minX, minX);
        this.minY = Math.max(this.minY, minY);
        this.maxX = Math.min(this.maxX, maxX);
        this.maxY = Math.min(this.maxY, maxY);
    }

    @Override // com.sun.javafx.geom.BaseBounds
    public void intersectWith(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        if (isEmpty()) {
            return;
        }
        if (maxX < minX || maxY < minY || maxZ < minZ) {
            makeEmpty();
            return;
        }
        this.minX = Math.max(this.minX, minX);
        this.minY = Math.max(this.minY, minY);
        this.maxX = Math.min(this.maxX, maxX);
        this.maxY = Math.min(this.maxY, maxY);
    }

    @Override // com.sun.javafx.geom.BaseBounds
    public boolean contains(Point2D p2) {
        return p2 != null && !isEmpty() && p2.f11907x >= this.minX && p2.f11907x <= this.maxX && p2.f11908y >= this.minY && p2.f11908y <= this.maxY;
    }

    @Override // com.sun.javafx.geom.BaseBounds
    public boolean contains(float x2, float y2) {
        return !isEmpty() && x2 >= this.minX && x2 <= this.maxX && y2 >= this.minY && y2 <= this.maxY;
    }

    public boolean contains(RectBounds other) {
        return !isEmpty() && !other.isEmpty() && this.minX <= other.minX && this.maxX >= other.maxX && this.minY <= other.minY && this.maxY >= other.maxY;
    }

    @Override // com.sun.javafx.geom.BaseBounds
    public boolean intersects(float x2, float y2, float width, float height) {
        return !isEmpty() && x2 + width >= this.minX && y2 + height >= this.minY && x2 <= this.maxX && y2 <= this.maxY;
    }

    public boolean intersects(BaseBounds other) {
        return other != null && !other.isEmpty() && !isEmpty() && other.getMaxX() >= this.minX && other.getMaxY() >= this.minY && other.getMaxZ() >= getMinZ() && other.getMinX() <= this.maxX && other.getMinY() <= this.maxY && other.getMinZ() <= getMaxZ();
    }

    @Override // com.sun.javafx.geom.BaseBounds
    public boolean disjoint(float x2, float y2, float width, float height) {
        return isEmpty() || x2 + width < this.minX || y2 + height < this.minY || x2 > this.maxX || y2 > this.maxY;
    }

    public boolean disjoint(RectBounds other) {
        return other == null || other.isEmpty() || isEmpty() || other.getMaxX() < this.minX || other.getMaxY() < this.minY || other.getMinX() > this.maxX || other.getMinY() > this.maxY;
    }

    @Override // com.sun.javafx.geom.BaseBounds
    public boolean isEmpty() {
        return this.maxX < this.minX || this.maxY < this.minY;
    }

    @Override // com.sun.javafx.geom.BaseBounds
    public void roundOut() {
        this.minX = (float) Math.floor(this.minX);
        this.minY = (float) Math.floor(this.minY);
        this.maxX = (float) Math.ceil(this.maxX);
        this.maxY = (float) Math.ceil(this.maxY);
    }

    public void grow(float h2, float v2) {
        this.minX -= h2;
        this.maxX += h2;
        this.minY -= v2;
        this.maxY += v2;
    }

    @Override // com.sun.javafx.geom.BaseBounds
    public BaseBounds deriveWithPadding(float h2, float v2, float d2) {
        if (d2 == 0.0f) {
            grow(h2, v2);
            return this;
        }
        BoxBounds bb2 = new BoxBounds(this.minX, this.minY, 0.0f, this.maxX, this.maxY, 0.0f);
        bb2.grow(h2, v2, d2);
        return bb2;
    }

    @Override // com.sun.javafx.geom.BaseBounds
    public RectBounds makeEmpty() {
        this.minY = 0.0f;
        this.minX = 0.0f;
        this.maxY = -1.0f;
        this.maxX = -1.0f;
        return this;
    }

    @Override // com.sun.javafx.geom.BaseBounds
    protected void sortMinMax() {
        if (this.minX > this.maxX) {
            float tmp = this.maxX;
            this.maxX = this.minX;
            this.minX = tmp;
        }
        if (this.minY > this.maxY) {
            float tmp2 = this.maxY;
            this.maxY = this.minY;
            this.minY = tmp2;
        }
    }

    @Override // com.sun.javafx.geom.BaseBounds
    public void translate(float x2, float y2, float z2) {
        setMinX(getMinX() + x2);
        setMinY(getMinY() + y2);
        setMaxX(getMaxX() + x2);
        setMaxY(getMaxY() + y2);
    }

    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        RectBounds other = (RectBounds) obj;
        return this.minX == other.getMinX() && this.minY == other.getMinY() && this.maxX == other.getMaxX() && this.maxY == other.getMaxY();
    }

    public int hashCode() {
        int hash = (79 * 7) + Float.floatToIntBits(this.minX);
        return (79 * ((79 * ((79 * hash) + Float.floatToIntBits(this.minY))) + Float.floatToIntBits(this.maxX))) + Float.floatToIntBits(this.maxY);
    }

    public String toString() {
        return "RectBounds { minX:" + this.minX + ", minY:" + this.minY + ", maxX:" + this.maxX + ", maxY:" + this.maxY + "} (w:" + (this.maxX - this.minX) + ", h:" + (this.maxY - this.minY) + ")";
    }
}
