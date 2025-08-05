package com.sun.javafx.geom;

import com.sun.javafx.geom.BaseBounds;

/* loaded from: jfxrt.jar:com/sun/javafx/geom/BoxBounds.class */
public class BoxBounds extends BaseBounds {
    private float minX;
    private float maxX;
    private float minY;
    private float maxY;
    private float minZ;
    private float maxZ;

    public BoxBounds() {
        this.minZ = 0.0f;
        this.minY = 0.0f;
        this.minX = 0.0f;
        this.maxZ = -1.0f;
        this.maxY = -1.0f;
        this.maxX = -1.0f;
    }

    @Override // com.sun.javafx.geom.BaseBounds
    public BaseBounds copy() {
        return new BoxBounds(this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ);
    }

    public BoxBounds(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        setBounds(minX, minY, minZ, maxX, maxY, maxZ);
    }

    public BoxBounds(BoxBounds other) {
        setBounds(other);
    }

    @Override // com.sun.javafx.geom.BaseBounds
    public BaseBounds.BoundsType getBoundsType() {
        return BaseBounds.BoundsType.BOX;
    }

    @Override // com.sun.javafx.geom.BaseBounds
    public boolean is2D() {
        return false;
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
        return this.maxZ - this.minZ;
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
        return this.minZ;
    }

    public void setMinZ(float minZ) {
        this.minZ = minZ;
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
        return this.maxZ;
    }

    public void setMaxZ(float maxZ) {
        this.maxZ = maxZ;
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
        min.f11935z = this.minZ;
        return min;
    }

    @Override // com.sun.javafx.geom.BaseBounds
    public Vec3f getMax(Vec3f max) {
        if (max == null) {
            max = new Vec3f();
        }
        max.f11933x = this.maxX;
        max.f11934y = this.maxY;
        max.f11935z = this.maxZ;
        return max;
    }

    @Override // com.sun.javafx.geom.BaseBounds
    public BaseBounds deriveWithUnion(BaseBounds other) {
        if (other.getBoundsType() == BaseBounds.BoundsType.RECTANGLE || other.getBoundsType() == BaseBounds.BoundsType.BOX) {
            unionWith(other);
            return this;
        }
        throw new UnsupportedOperationException("Unknown BoundsType");
    }

    @Override // com.sun.javafx.geom.BaseBounds
    public BaseBounds deriveWithNewBounds(Rectangle other) {
        if (other.width < 0 || other.height < 0) {
            return makeEmpty();
        }
        setBounds(other.f11913x, other.f11914y, 0.0f, other.f11913x + other.width, other.f11914y + other.height, 0.0f);
        return this;
    }

    @Override // com.sun.javafx.geom.BaseBounds
    public BaseBounds deriveWithNewBounds(BaseBounds other) {
        if (other.isEmpty()) {
            return makeEmpty();
        }
        if (other.getBoundsType() == BaseBounds.BoundsType.RECTANGLE || other.getBoundsType() == BaseBounds.BoundsType.BOX) {
            this.minX = other.getMinX();
            this.minY = other.getMinY();
            this.minZ = other.getMinZ();
            this.maxX = other.getMaxX();
            this.maxY = other.getMaxY();
            this.maxZ = other.getMaxZ();
            return this;
        }
        throw new UnsupportedOperationException("Unknown BoundsType");
    }

    @Override // com.sun.javafx.geom.BaseBounds
    public BaseBounds deriveWithNewBounds(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        if (maxX < minX || maxY < minY || maxZ < minZ) {
            return makeEmpty();
        }
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
        return this;
    }

    @Override // com.sun.javafx.geom.BaseBounds
    public BaseBounds deriveWithNewBoundsAndSort(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        setBoundsAndSort(minX, minY, minZ, maxX, maxY, maxZ);
        return this;
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

    public final void setBounds(BaseBounds other) {
        this.minX = other.getMinX();
        this.minY = other.getMinY();
        this.minZ = other.getMinZ();
        this.maxX = other.getMaxX();
        this.maxY = other.getMaxY();
        this.maxZ = other.getMaxZ();
    }

    public final void setBounds(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }

    @Override // com.sun.javafx.geom.BaseBounds
    public void setBoundsAndSort(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        setBounds(minX, minY, minZ, maxX, maxY, maxZ);
        sortMinMax();
    }

    @Override // com.sun.javafx.geom.BaseBounds
    public void setBoundsAndSort(Point2D p1, Point2D p2) {
        setBoundsAndSort(p1.f11907x, p1.f11908y, 0.0f, p2.f11907x, p2.f11908y, 0.0f);
    }

    public void unionWith(BaseBounds other) {
        if (other.isEmpty()) {
            return;
        }
        if (isEmpty()) {
            setBounds(other);
            return;
        }
        this.minX = Math.min(this.minX, other.getMinX());
        this.minY = Math.min(this.minY, other.getMinY());
        this.minZ = Math.min(this.minZ, other.getMinZ());
        this.maxX = Math.max(this.maxX, other.getMaxX());
        this.maxY = Math.max(this.maxY, other.getMaxY());
        this.maxZ = Math.max(this.maxZ, other.getMaxZ());
    }

    public void unionWith(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        if (maxX < minX || maxY < minY || maxZ < minZ) {
            return;
        }
        if (isEmpty()) {
            setBounds(minX, minY, minZ, maxX, maxY, maxZ);
            return;
        }
        this.minX = Math.min(this.minX, minX);
        this.minY = Math.min(this.minY, minY);
        this.minZ = Math.min(this.minZ, minZ);
        this.maxX = Math.max(this.maxX, maxX);
        this.maxY = Math.max(this.maxY, maxY);
        this.maxZ = Math.max(this.maxZ, maxZ);
    }

    @Override // com.sun.javafx.geom.BaseBounds
    public void add(float x2, float y2, float z2) {
        unionWith(x2, y2, z2, x2, y2, z2);
    }

    @Override // com.sun.javafx.geom.BaseBounds
    public void add(Point2D p2) {
        add(p2.f11907x, p2.f11908y, 0.0f);
    }

    @Override // com.sun.javafx.geom.BaseBounds
    public void intersectWith(Rectangle other) {
        float x2 = other.f11913x;
        float y2 = other.f11914y;
        intersectWith(x2, y2, 0.0f, x2 + other.width, y2 + other.height, 0.0f);
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
        this.minZ = Math.max(this.minZ, other.getMinZ());
        this.maxX = Math.min(this.maxX, other.getMaxX());
        this.maxY = Math.min(this.maxY, other.getMaxY());
        this.maxZ = Math.min(this.maxZ, other.getMaxZ());
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
        this.minZ = Math.max(this.minZ, minZ);
        this.maxX = Math.min(this.maxX, maxX);
        this.maxY = Math.min(this.maxY, maxY);
        this.maxZ = Math.min(this.maxZ, maxZ);
    }

    @Override // com.sun.javafx.geom.BaseBounds
    public boolean contains(Point2D p2) {
        if (p2 == null || isEmpty()) {
            return false;
        }
        return contains(p2.f11907x, p2.f11908y, 0.0f);
    }

    @Override // com.sun.javafx.geom.BaseBounds
    public boolean contains(float x2, float y2) {
        if (isEmpty()) {
            return false;
        }
        return contains(x2, y2, 0.0f);
    }

    public boolean contains(float x2, float y2, float z2) {
        return !isEmpty() && x2 >= this.minX && x2 <= this.maxX && y2 >= this.minY && y2 <= this.maxY && z2 >= this.minZ && z2 <= this.maxZ;
    }

    public boolean contains(float x2, float y2, float z2, float width, float height, float depth) {
        return !isEmpty() && contains(x2, y2, z2) && contains(x2 + width, y2 + height, z2 + depth);
    }

    @Override // com.sun.javafx.geom.BaseBounds
    public boolean intersects(float x2, float y2, float width, float height) {
        return intersects(x2, y2, 0.0f, width, height, 0.0f);
    }

    public boolean intersects(float x2, float y2, float z2, float width, float height, float depth) {
        return !isEmpty() && x2 + width >= this.minX && y2 + height >= this.minY && z2 + depth >= this.minZ && x2 <= this.maxX && y2 <= this.maxY && z2 <= this.maxZ;
    }

    public boolean intersects(BaseBounds other) {
        return other != null && !other.isEmpty() && !isEmpty() && other.getMaxX() >= this.minX && other.getMaxY() >= this.minY && other.getMaxZ() >= this.minZ && other.getMinX() <= this.maxX && other.getMinY() <= this.maxY && other.getMinZ() <= this.maxZ;
    }

    @Override // com.sun.javafx.geom.BaseBounds
    public boolean disjoint(float x2, float y2, float width, float height) {
        return disjoint(x2, y2, 0.0f, width, height, 0.0f);
    }

    public boolean disjoint(float x2, float y2, float z2, float width, float height, float depth) {
        return isEmpty() || x2 + width < this.minX || y2 + height < this.minY || z2 + depth < this.minZ || x2 > this.maxX || y2 > this.maxY || z2 > this.maxZ;
    }

    @Override // com.sun.javafx.geom.BaseBounds
    public boolean isEmpty() {
        return this.maxX < this.minX || this.maxY < this.minY || this.maxZ < this.minZ;
    }

    @Override // com.sun.javafx.geom.BaseBounds
    public void roundOut() {
        this.minX = (float) Math.floor(this.minX);
        this.minY = (float) Math.floor(this.minY);
        this.minZ = (float) Math.floor(this.minZ);
        this.maxX = (float) Math.ceil(this.maxX);
        this.maxY = (float) Math.ceil(this.maxY);
        this.maxZ = (float) Math.ceil(this.maxZ);
    }

    public void grow(float h2, float v2, float d2) {
        this.minX -= h2;
        this.maxX += h2;
        this.minY -= v2;
        this.maxY += v2;
        this.minZ -= d2;
        this.maxZ += d2;
    }

    @Override // com.sun.javafx.geom.BaseBounds
    public BaseBounds deriveWithPadding(float h2, float v2, float d2) {
        grow(h2, v2, d2);
        return this;
    }

    @Override // com.sun.javafx.geom.BaseBounds
    public BoxBounds makeEmpty() {
        this.minZ = 0.0f;
        this.minY = 0.0f;
        this.minX = 0.0f;
        this.maxZ = -1.0f;
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
        if (this.minZ > this.maxZ) {
            float tmp3 = this.maxZ;
            this.maxZ = this.minZ;
            this.minZ = tmp3;
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
        BoxBounds other = (BoxBounds) obj;
        return this.minX == other.getMinX() && this.minY == other.getMinY() && this.minZ == other.getMinZ() && this.maxX == other.getMaxX() && this.maxY == other.getMaxY() && this.maxZ == other.getMaxZ();
    }

    public int hashCode() {
        int hash = (79 * 7) + Float.floatToIntBits(this.minX);
        return (79 * ((79 * ((79 * ((79 * ((79 * hash) + Float.floatToIntBits(this.minY))) + Float.floatToIntBits(this.minZ))) + Float.floatToIntBits(this.maxX))) + Float.floatToIntBits(this.maxY))) + Float.floatToIntBits(this.maxZ);
    }

    public String toString() {
        return "BoxBounds { minX:" + this.minX + ", minY:" + this.minY + ", minZ:" + this.minZ + ", maxX:" + this.maxX + ", maxY:" + this.maxY + ", maxZ:" + this.maxZ + "}";
    }
}
