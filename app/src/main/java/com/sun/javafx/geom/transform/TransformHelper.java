package com.sun.javafx.geom.transform;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Vec3d;

/* loaded from: jfxrt.jar:com/sun/javafx/geom/transform/TransformHelper.class */
public class TransformHelper {
    private TransformHelper() {
    }

    public static BaseBounds general3dBoundsTransform(CanTransformVec3d tx, BaseBounds src, BaseBounds dst, Vec3d tempV3d) {
        if (tempV3d == null) {
            tempV3d = new Vec3d();
        }
        double srcMinX = src.getMinX();
        double srcMinY = src.getMinY();
        double srcMinZ = src.getMinZ();
        double srcMaxX = src.getMaxX();
        double srcMaxY = src.getMaxY();
        double srcMaxZ = src.getMaxZ();
        tempV3d.set(srcMaxX, srcMaxY, srcMaxZ);
        Vec3d tempV3d2 = tx.transform(tempV3d, tempV3d);
        double minX = tempV3d2.f11930x;
        double minY = tempV3d2.f11931y;
        double minZ = tempV3d2.f11932z;
        double maxX = tempV3d2.f11930x;
        double maxY = tempV3d2.f11931y;
        double maxZ = tempV3d2.f11932z;
        tempV3d2.set(srcMinX, srcMaxY, srcMaxZ);
        Vec3d tempV3d3 = tx.transform(tempV3d2, tempV3d2);
        if (tempV3d3.f11930x > maxX) {
            maxX = tempV3d3.f11930x;
        }
        if (tempV3d3.f11931y > maxY) {
            maxY = tempV3d3.f11931y;
        }
        if (tempV3d3.f11932z > maxZ) {
            maxZ = tempV3d3.f11932z;
        }
        if (tempV3d3.f11930x < minX) {
            minX = tempV3d3.f11930x;
        }
        if (tempV3d3.f11931y < minY) {
            minY = tempV3d3.f11931y;
        }
        if (tempV3d3.f11932z < minZ) {
            minZ = tempV3d3.f11932z;
        }
        tempV3d3.set(srcMinX, srcMinY, srcMaxZ);
        Vec3d tempV3d4 = tx.transform(tempV3d3, tempV3d3);
        if (tempV3d4.f11930x > maxX) {
            maxX = tempV3d4.f11930x;
        }
        if (tempV3d4.f11931y > maxY) {
            maxY = tempV3d4.f11931y;
        }
        if (tempV3d4.f11932z > maxZ) {
            maxZ = tempV3d4.f11932z;
        }
        if (tempV3d4.f11930x < minX) {
            minX = tempV3d4.f11930x;
        }
        if (tempV3d4.f11931y < minY) {
            minY = tempV3d4.f11931y;
        }
        if (tempV3d4.f11932z < minZ) {
            minZ = tempV3d4.f11932z;
        }
        tempV3d4.set(srcMaxX, srcMinY, srcMaxZ);
        Vec3d tempV3d5 = tx.transform(tempV3d4, tempV3d4);
        if (tempV3d5.f11930x > maxX) {
            maxX = tempV3d5.f11930x;
        }
        if (tempV3d5.f11931y > maxY) {
            maxY = tempV3d5.f11931y;
        }
        if (tempV3d5.f11932z > maxZ) {
            maxZ = tempV3d5.f11932z;
        }
        if (tempV3d5.f11930x < minX) {
            minX = tempV3d5.f11930x;
        }
        if (tempV3d5.f11931y < minY) {
            minY = tempV3d5.f11931y;
        }
        if (tempV3d5.f11932z < minZ) {
            minZ = tempV3d5.f11932z;
        }
        tempV3d5.set(srcMinX, srcMaxY, srcMinZ);
        Vec3d tempV3d6 = tx.transform(tempV3d5, tempV3d5);
        if (tempV3d6.f11930x > maxX) {
            maxX = tempV3d6.f11930x;
        }
        if (tempV3d6.f11931y > maxY) {
            maxY = tempV3d6.f11931y;
        }
        if (tempV3d6.f11932z > maxZ) {
            maxZ = tempV3d6.f11932z;
        }
        if (tempV3d6.f11930x < minX) {
            minX = tempV3d6.f11930x;
        }
        if (tempV3d6.f11931y < minY) {
            minY = tempV3d6.f11931y;
        }
        if (tempV3d6.f11932z < minZ) {
            minZ = tempV3d6.f11932z;
        }
        tempV3d6.set(srcMaxX, srcMaxY, srcMinZ);
        Vec3d tempV3d7 = tx.transform(tempV3d6, tempV3d6);
        if (tempV3d7.f11930x > maxX) {
            maxX = tempV3d7.f11930x;
        }
        if (tempV3d7.f11931y > maxY) {
            maxY = tempV3d7.f11931y;
        }
        if (tempV3d7.f11932z > maxZ) {
            maxZ = tempV3d7.f11932z;
        }
        if (tempV3d7.f11930x < minX) {
            minX = tempV3d7.f11930x;
        }
        if (tempV3d7.f11931y < minY) {
            minY = tempV3d7.f11931y;
        }
        if (tempV3d7.f11932z < minZ) {
            minZ = tempV3d7.f11932z;
        }
        tempV3d7.set(srcMinX, srcMinY, srcMinZ);
        Vec3d tempV3d8 = tx.transform(tempV3d7, tempV3d7);
        if (tempV3d8.f11930x > maxX) {
            maxX = tempV3d8.f11930x;
        }
        if (tempV3d8.f11931y > maxY) {
            maxY = tempV3d8.f11931y;
        }
        if (tempV3d8.f11932z > maxZ) {
            maxZ = tempV3d8.f11932z;
        }
        if (tempV3d8.f11930x < minX) {
            minX = tempV3d8.f11930x;
        }
        if (tempV3d8.f11931y < minY) {
            minY = tempV3d8.f11931y;
        }
        if (tempV3d8.f11932z < minZ) {
            minZ = tempV3d8.f11932z;
        }
        tempV3d8.set(srcMaxX, srcMinY, srcMinZ);
        Vec3d tempV3d9 = tx.transform(tempV3d8, tempV3d8);
        if (tempV3d9.f11930x > maxX) {
            maxX = tempV3d9.f11930x;
        }
        if (tempV3d9.f11931y > maxY) {
            maxY = tempV3d9.f11931y;
        }
        if (tempV3d9.f11932z > maxZ) {
            maxZ = tempV3d9.f11932z;
        }
        if (tempV3d9.f11930x < minX) {
            minX = tempV3d9.f11930x;
        }
        if (tempV3d9.f11931y < minY) {
            minY = tempV3d9.f11931y;
        }
        if (tempV3d9.f11932z < minZ) {
            minZ = tempV3d9.f11932z;
        }
        return dst.deriveWithNewBounds((float) minX, (float) minY, (float) minZ, (float) maxX, (float) maxY, (float) maxZ);
    }
}
