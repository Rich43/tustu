package com.sun.prism.impl;

import com.sun.javafx.geom.Quat4f;
import com.sun.javafx.geom.Vec2f;
import com.sun.javafx.geom.Vec3f;

/* loaded from: jfxrt.jar:com/sun/prism/impl/MeshTempState.class */
final class MeshTempState {
    final Vec3f vec3f1;
    final Vec3f vec3f2;
    final Vec3f vec3f3;
    final Vec3f vec3f4;
    final Vec3f vec3f5;
    final Vec3f vec3f6;
    final Vec2f vec2f1;
    final Vec2f vec2f2;
    final int[] smFace;
    final int[] triVerts;
    final Vec3f[] triPoints;
    final Vec2f[] triTexCoords;
    final Vec3f[] triNormals;
    final int[] triPointIndex;
    final int[] triNormalIndex;
    final int[] triTexCoordIndex;
    final float[][] matrix;
    final float[] vector;
    final Quat4f quat;
    MeshVertex[] pool;
    MeshVertex[] pVertex;
    int[] indexBuffer;
    short[] indexBufferShort;
    float[] vertexBuffer;
    private static final ThreadLocal<MeshTempState> tempStateRef = new ThreadLocal<MeshTempState>() { // from class: com.sun.prism.impl.MeshTempState.1
        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.lang.ThreadLocal
        public MeshTempState initialValue() {
            return new MeshTempState();
        }
    };

    private MeshTempState() {
        this.vec3f1 = new Vec3f();
        this.vec3f2 = new Vec3f();
        this.vec3f3 = new Vec3f();
        this.vec3f4 = new Vec3f();
        this.vec3f5 = new Vec3f();
        this.vec3f6 = new Vec3f();
        this.vec2f1 = new Vec2f();
        this.vec2f2 = new Vec2f();
        this.smFace = new int[7];
        this.triVerts = new int[3];
        this.triPoints = new Vec3f[3];
        this.triTexCoords = new Vec2f[3];
        this.triNormals = new Vec3f[3];
        this.triPointIndex = new int[3];
        this.triNormalIndex = new int[3];
        this.triTexCoordIndex = new int[3];
        this.matrix = new float[3][3];
        this.vector = new float[3];
        this.quat = new Quat4f();
        for (int i2 = 0; i2 < 3; i2++) {
            this.triNormals[i2] = new Vec3f();
        }
    }

    static MeshTempState getInstance() {
        return tempStateRef.get();
    }
}
