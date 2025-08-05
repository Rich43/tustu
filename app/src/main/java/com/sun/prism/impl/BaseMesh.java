package com.sun.prism.impl;

import com.sun.javafx.geom.Quat4f;
import com.sun.javafx.geom.Vec2f;
import com.sun.javafx.geom.Vec3f;
import com.sun.prism.Mesh;
import com.sun.prism.impl.Disposer;
import java.util.Arrays;
import java.util.HashMap;
import javafx.scene.shape.VertexFormat;
import sun.util.logging.PlatformLogger;

/* loaded from: jfxrt.jar:com/sun/prism/impl/BaseMesh.class */
public abstract class BaseMesh extends BaseGraphicsResource implements Mesh {
    private int nVerts;
    private int nTVerts;
    private int nFaces;
    private float[] pos;
    private float[] uv;
    private int[] faces;
    private int[] smoothing;
    private boolean allSameSmoothing;
    private boolean allHardEdges;
    protected static final int POINT_SIZE = 3;
    protected static final int NORMAL_SIZE = 3;
    protected static final int TEXCOORD_SIZE = 2;
    protected static final int POINT_SIZE_VB = 3;
    protected static final int TEXCOORD_SIZE_VB = 2;
    protected static final int NORMAL_SIZE_VB = 4;
    protected static final int VERTEX_SIZE_VB = 9;
    public static final int FACE_MEMBERS_SIZE = 7;
    private boolean[] dirtyVertices;
    private float[] cachedNormals;
    private float[] cachedTangents;
    private float[] cachedBitangents;
    private float[] vertexBuffer;
    private int[] indexBuffer;
    private short[] indexBufferShort;
    private int indexBufferSize;
    private int numberOfVertices;
    private HashMap<Integer, MeshGeomComp2VB> point2vbMap;
    private HashMap<Integer, MeshGeomComp2VB> normal2vbMap;
    private HashMap<Integer, MeshGeomComp2VB> texCoord2vbMap;
    static final /* synthetic */ boolean $assertionsDisabled;

    /* loaded from: jfxrt.jar:com/sun/prism/impl/BaseMesh$FaceMembers.class */
    public enum FaceMembers {
        POINT0,
        TEXCOORD0,
        POINT1,
        TEXCOORD1,
        POINT2,
        TEXCOORD2,
        SMOOTHING_GROUP
    }

    public abstract boolean buildNativeGeometry(float[] fArr, int i2, int[] iArr, int i3);

    public abstract boolean buildNativeGeometry(float[] fArr, int i2, short[] sArr, int i3);

    static {
        $assertionsDisabled = !BaseMesh.class.desiredAssertionStatus();
    }

    protected BaseMesh(Disposer.Record disposerRecord) {
        super(disposerRecord);
    }

    private boolean updateSkipMeshNormalGeometry(int[] posFromAndLengthIndices, int[] uvFromAndLengthIndices) {
        int startTexCoord = uvFromAndLengthIndices[0] / 2;
        int numTexCoords = uvFromAndLengthIndices[1] / 2;
        if (uvFromAndLengthIndices[1] % 2 > 0) {
            numTexCoords++;
        }
        if (numTexCoords > 0) {
            for (int i2 = 0; i2 < numTexCoords; i2++) {
                int texCoordOffset = (startTexCoord + i2) * 2;
                MeshGeomComp2VB mt2vb = this.texCoord2vbMap.get(Integer.valueOf(texCoordOffset));
                if (!$assertionsDisabled && mt2vb == null) {
                    throw new AssertionError();
                }
                if (mt2vb != null) {
                    int[] locs = mt2vb.getLocs();
                    int validLocs = mt2vb.getValidLocs();
                    if (locs != null) {
                        for (int j2 = 0; j2 < validLocs; j2++) {
                            int vbIndex = (locs[j2] * 9) + 3;
                            this.vertexBuffer[vbIndex] = this.uv[texCoordOffset];
                            this.vertexBuffer[vbIndex + 1] = this.uv[texCoordOffset + 1];
                        }
                    } else {
                        int loc = mt2vb.getLoc();
                        int vbIndex2 = (loc * 9) + 3;
                        this.vertexBuffer[vbIndex2] = this.uv[texCoordOffset];
                        this.vertexBuffer[vbIndex2 + 1] = this.uv[texCoordOffset + 1];
                    }
                }
            }
        }
        int startPoint = posFromAndLengthIndices[0] / 3;
        int numPoints = posFromAndLengthIndices[1] / 3;
        if (posFromAndLengthIndices[1] % 3 > 0) {
            numPoints++;
        }
        if (numPoints > 0) {
            for (int i3 = 0; i3 < numPoints; i3++) {
                int pointOffset = (startPoint + i3) * 3;
                MeshGeomComp2VB mp2vb = this.point2vbMap.get(Integer.valueOf(pointOffset));
                if (!$assertionsDisabled && mp2vb == null) {
                    throw new AssertionError();
                }
                if (mp2vb != null) {
                    int[] locs2 = mp2vb.getLocs();
                    int validLocs2 = mp2vb.getValidLocs();
                    if (locs2 != null) {
                        for (int j3 = 0; j3 < validLocs2; j3++) {
                            int vbIndex3 = locs2[j3] * 9;
                            this.vertexBuffer[vbIndex3] = this.pos[pointOffset];
                            this.vertexBuffer[vbIndex3 + 1] = this.pos[pointOffset + 1];
                            this.vertexBuffer[vbIndex3 + 2] = this.pos[pointOffset + 2];
                        }
                    } else {
                        int loc2 = mp2vb.getLoc();
                        int vbIndex4 = loc2 * 9;
                        this.vertexBuffer[vbIndex4] = this.pos[pointOffset];
                        this.vertexBuffer[vbIndex4 + 1] = this.pos[pointOffset + 1];
                        this.vertexBuffer[vbIndex4 + 2] = this.pos[pointOffset + 2];
                    }
                }
            }
        }
        if (this.indexBuffer != null) {
            return buildNativeGeometry(this.vertexBuffer, this.numberOfVertices * 9, this.indexBuffer, this.nFaces * 3);
        }
        return buildNativeGeometry(this.vertexBuffer, this.numberOfVertices * 9, this.indexBufferShort, this.nFaces * 3);
    }

    private boolean buildSkipMeshNormalGeometry() {
        HashMap<Long, Integer> face2vbMap = new HashMap<>();
        if (this.point2vbMap == null) {
            this.point2vbMap = new HashMap<>();
        } else {
            this.point2vbMap.clear();
        }
        if (this.texCoord2vbMap == null) {
            this.texCoord2vbMap = new HashMap<>();
        } else {
            this.texCoord2vbMap.clear();
        }
        this.vertexBuffer = new float[this.nVerts * 9];
        this.indexBuffer = new int[this.nFaces * 3];
        int ibCount = 0;
        int vbCount = 0;
        for (int faceCount = 0; faceCount < this.nFaces; faceCount++) {
            int faceIndex = faceCount * 6;
            for (int i2 = 0; i2 < 3; i2++) {
                int vertexIndex = i2 * 2;
                int pointIndex = faceIndex + vertexIndex;
                int texCoordIndex = pointIndex + 1;
                long key = (this.faces[pointIndex] << 32) | this.faces[texCoordIndex];
                Integer mf2vb = face2vbMap.get(Long.valueOf(key));
                if (mf2vb == null) {
                    mf2vb = Integer.valueOf(vbCount / 9);
                    face2vbMap.put(Long.valueOf(key), mf2vb);
                    if (this.vertexBuffer.length <= vbCount) {
                        float[] temp = new float[vbCount + 90];
                        System.arraycopy(this.vertexBuffer, 0, temp, 0, this.vertexBuffer.length);
                        this.vertexBuffer = temp;
                    }
                    int pointOffset = this.faces[pointIndex] * 3;
                    int texCoordOffset = this.faces[texCoordIndex] * 2;
                    this.vertexBuffer[vbCount] = this.pos[pointOffset];
                    this.vertexBuffer[vbCount + 1] = this.pos[pointOffset + 1];
                    this.vertexBuffer[vbCount + 2] = this.pos[pointOffset + 2];
                    this.vertexBuffer[vbCount + 3] = this.uv[texCoordOffset];
                    this.vertexBuffer[vbCount + 4] = this.uv[texCoordOffset + 1];
                    this.vertexBuffer[vbCount + 5] = 0.0f;
                    this.vertexBuffer[vbCount + 6] = 0.0f;
                    this.vertexBuffer[vbCount + 7] = 0.0f;
                    this.vertexBuffer[vbCount + 8] = 0.0f;
                    vbCount += 9;
                    MeshGeomComp2VB mp2vb = this.point2vbMap.get(Integer.valueOf(pointOffset));
                    if (mp2vb == null) {
                        this.point2vbMap.put(Integer.valueOf(pointOffset), new MeshGeomComp2VB(pointOffset, mf2vb.intValue()));
                    } else {
                        mp2vb.addLoc(mf2vb.intValue());
                    }
                    MeshGeomComp2VB mt2vb = this.texCoord2vbMap.get(Integer.valueOf(texCoordOffset));
                    if (mt2vb == null) {
                        this.texCoord2vbMap.put(Integer.valueOf(texCoordOffset), new MeshGeomComp2VB(texCoordOffset, mf2vb.intValue()));
                    } else {
                        mt2vb.addLoc(mf2vb.intValue());
                    }
                }
                int i3 = ibCount;
                ibCount++;
                this.indexBuffer[i3] = mf2vb.intValue();
            }
        }
        this.numberOfVertices = vbCount / 9;
        if (this.numberOfVertices > 65536) {
            return buildNativeGeometry(this.vertexBuffer, this.numberOfVertices * 9, this.indexBuffer, this.nFaces * 3);
        }
        if (this.indexBufferShort == null || this.indexBufferShort.length < this.nFaces * 3) {
            this.indexBufferShort = new short[this.nFaces * 3];
        }
        int ii = 0;
        for (int i4 = 0; i4 < this.nFaces; i4++) {
            int i5 = ii;
            int i6 = ii;
            int ii2 = ii + 1;
            this.indexBufferShort[i5] = (short) this.indexBuffer[i6];
            int ii3 = ii2 + 1;
            this.indexBufferShort[ii2] = (short) this.indexBuffer[ii2];
            ii = ii3 + 1;
            this.indexBufferShort[ii3] = (short) this.indexBuffer[ii3];
        }
        this.indexBuffer = null;
        return buildNativeGeometry(this.vertexBuffer, this.numberOfVertices * 9, this.indexBufferShort, this.nFaces * 3);
    }

    private void convertNormalsToQuats(MeshTempState instance, int numberOfVertices, float[] normals, float[] tangents, float[] bitangents, float[] vertexBuffer, boolean[] dirtys) {
        Vec3f normal = instance.vec3f1;
        Vec3f tangent = instance.vec3f2;
        Vec3f bitangent = instance.vec3f3;
        int i2 = 0;
        int vbIndex = 0;
        while (i2 < numberOfVertices) {
            if (dirtys == null || dirtys[i2]) {
                int index = i2 * 3;
                normal.f11933x = normals[index];
                normal.f11934y = normals[index + 1];
                normal.f11935z = normals[index + 2];
                normal.normalize();
                tangent.f11933x = tangents[index];
                tangent.f11934y = tangents[index + 1];
                tangent.f11935z = tangents[index + 2];
                bitangent.f11933x = bitangents[index];
                bitangent.f11934y = bitangents[index + 1];
                bitangent.f11935z = bitangents[index + 2];
                instance.triNormals[0].set(normal);
                instance.triNormals[1].set(tangent);
                instance.triNormals[2].set(bitangent);
                MeshUtil.fixTSpace(instance.triNormals);
                buildVSQuat(instance.triNormals, instance.quat);
                vertexBuffer[vbIndex + 5] = instance.quat.f11909x;
                vertexBuffer[vbIndex + 6] = instance.quat.f11910y;
                vertexBuffer[vbIndex + 7] = instance.quat.f11911z;
                vertexBuffer[vbIndex + 8] = instance.quat.f11912w;
            }
            i2++;
            vbIndex += 9;
        }
    }

    private boolean doBuildPNTGeometry(float[] points, float[] normals, float[] texCoords, int[] faces) {
        if (this.point2vbMap == null) {
            this.point2vbMap = new HashMap<>();
        } else {
            this.point2vbMap.clear();
        }
        if (this.normal2vbMap == null) {
            this.normal2vbMap = new HashMap<>();
        } else {
            this.normal2vbMap.clear();
        }
        if (this.texCoord2vbMap == null) {
            this.texCoord2vbMap = new HashMap<>();
        } else {
            this.texCoord2vbMap.clear();
        }
        int vertexIndexSize = VertexFormat.POINT_NORMAL_TEXCOORD.getVertexIndexSize();
        int faceIndexSize = vertexIndexSize * 3;
        int pointIndexOffset = VertexFormat.POINT_NORMAL_TEXCOORD.getPointIndexOffset();
        int normalIndexOffset = VertexFormat.POINT_NORMAL_TEXCOORD.getNormalIndexOffset();
        int texCoordIndexOffset = VertexFormat.POINT_NORMAL_TEXCOORD.getTexCoordIndexOffset();
        int numPoints = points.length / 3;
        int numNormals = normals.length / 3;
        int numTexCoords = texCoords.length / 2;
        int numFaces = faces.length / faceIndexSize;
        if (!$assertionsDisabled && (numPoints <= 0 || numNormals <= 0 || numTexCoords <= 0 || numFaces <= 0)) {
            throw new AssertionError();
        }
        this.cachedNormals = new float[numPoints * 3];
        this.cachedTangents = new float[numPoints * 3];
        this.cachedBitangents = new float[numPoints * 3];
        this.vertexBuffer = new float[numPoints * 9];
        this.indexBuffer = new int[numFaces * 3];
        int ibCount = 0;
        int vbCount = 0;
        MeshTempState instance = MeshTempState.getInstance();
        for (int i2 = 0; i2 < 3; i2++) {
            if (instance.triPoints[i2] == null) {
                instance.triPoints[i2] = new Vec3f();
            }
            if (instance.triTexCoords[i2] == null) {
                instance.triTexCoords[i2] = new Vec2f();
            }
        }
        for (int faceCount = 0; faceCount < numFaces; faceCount++) {
            int faceIndex = faceCount * faceIndexSize;
            for (int i3 = 0; i3 < 3; i3++) {
                int vertexIndex = faceIndex + (i3 * vertexIndexSize);
                int pointIndex = vertexIndex + pointIndexOffset;
                int normalIndex = vertexIndex + normalIndexOffset;
                int texCoordIndex = vertexIndex + texCoordIndexOffset;
                Integer mf2vb = Integer.valueOf(vbCount / 9);
                if (this.vertexBuffer.length <= vbCount) {
                    int numVertices = vbCount / 9;
                    int newNumVertices = numVertices + Math.max(numVertices >> 3, 6);
                    float[] temp = new float[newNumVertices * 9];
                    System.arraycopy(this.vertexBuffer, 0, temp, 0, this.vertexBuffer.length);
                    this.vertexBuffer = temp;
                    float[] temp2 = new float[newNumVertices * 3];
                    System.arraycopy(this.cachedNormals, 0, temp2, 0, this.cachedNormals.length);
                    this.cachedNormals = temp2;
                    float[] temp3 = new float[newNumVertices * 3];
                    System.arraycopy(this.cachedTangents, 0, temp3, 0, this.cachedTangents.length);
                    this.cachedTangents = temp3;
                    float[] temp4 = new float[newNumVertices * 3];
                    System.arraycopy(this.cachedBitangents, 0, temp4, 0, this.cachedBitangents.length);
                    this.cachedBitangents = temp4;
                }
                int pointOffset = faces[pointIndex] * 3;
                int normalOffset = faces[normalIndex] * 3;
                int texCoordOffset = faces[texCoordIndex] * 2;
                instance.triPointIndex[i3] = pointOffset;
                instance.triTexCoordIndex[i3] = texCoordOffset;
                instance.triVerts[i3] = vbCount / 9;
                this.vertexBuffer[vbCount] = points[pointOffset];
                this.vertexBuffer[vbCount + 1] = points[pointOffset + 1];
                this.vertexBuffer[vbCount + 2] = points[pointOffset + 2];
                this.vertexBuffer[vbCount + 3] = texCoords[texCoordOffset];
                this.vertexBuffer[vbCount + 4] = texCoords[texCoordOffset + 1];
                int index = instance.triVerts[i3] * 3;
                this.cachedNormals[index] = normals[normalOffset];
                this.cachedNormals[index + 1] = normals[normalOffset + 1];
                this.cachedNormals[index + 2] = normals[normalOffset + 2];
                vbCount += 9;
                MeshGeomComp2VB mp2vb = this.point2vbMap.get(Integer.valueOf(pointOffset));
                if (mp2vb == null) {
                    this.point2vbMap.put(Integer.valueOf(pointOffset), new MeshGeomComp2VB(pointOffset, mf2vb.intValue()));
                } else {
                    mp2vb.addLoc(mf2vb.intValue());
                }
                MeshGeomComp2VB mn2vb = this.normal2vbMap.get(Integer.valueOf(normalOffset));
                if (mn2vb == null) {
                    this.normal2vbMap.put(Integer.valueOf(normalOffset), new MeshGeomComp2VB(normalOffset, mf2vb.intValue()));
                } else {
                    mn2vb.addLoc(mf2vb.intValue());
                }
                MeshGeomComp2VB mt2vb = this.texCoord2vbMap.get(Integer.valueOf(texCoordOffset));
                if (mt2vb == null) {
                    this.texCoord2vbMap.put(Integer.valueOf(texCoordOffset), new MeshGeomComp2VB(texCoordOffset, mf2vb.intValue()));
                } else {
                    mt2vb.addLoc(mf2vb.intValue());
                }
                int i4 = ibCount;
                ibCount++;
                this.indexBuffer[i4] = mf2vb.intValue();
            }
            for (int i5 = 0; i5 < 3; i5++) {
                instance.triPoints[i5].f11933x = points[instance.triPointIndex[i5]];
                instance.triPoints[i5].f11934y = points[instance.triPointIndex[i5] + 1];
                instance.triPoints[i5].f11935z = points[instance.triPointIndex[i5] + 2];
                instance.triTexCoords[i5].f11928x = texCoords[instance.triTexCoordIndex[i5]];
                instance.triTexCoords[i5].f11929y = texCoords[instance.triTexCoordIndex[i5] + 1];
            }
            MeshUtil.computeTBNNormalized(instance.triPoints[0], instance.triPoints[1], instance.triPoints[2], instance.triTexCoords[0], instance.triTexCoords[1], instance.triTexCoords[2], instance.triNormals);
            for (int i6 = 0; i6 < 3; i6++) {
                int index2 = instance.triVerts[i6] * 3;
                this.cachedTangents[index2] = instance.triNormals[1].f11933x;
                this.cachedTangents[index2 + 1] = instance.triNormals[1].f11934y;
                this.cachedTangents[index2 + 2] = instance.triNormals[1].f11935z;
                this.cachedBitangents[index2] = instance.triNormals[2].f11933x;
                this.cachedBitangents[index2 + 1] = instance.triNormals[2].f11934y;
                this.cachedBitangents[index2 + 2] = instance.triNormals[2].f11935z;
            }
        }
        this.numberOfVertices = vbCount / 9;
        convertNormalsToQuats(instance, this.numberOfVertices, this.cachedNormals, this.cachedTangents, this.cachedBitangents, this.vertexBuffer, null);
        this.indexBufferSize = numFaces * 3;
        if (this.numberOfVertices > 65536) {
            return buildNativeGeometry(this.vertexBuffer, this.numberOfVertices * 9, this.indexBuffer, this.indexBufferSize);
        }
        if (this.indexBufferShort == null || this.indexBufferShort.length < this.indexBufferSize) {
            this.indexBufferShort = new short[this.indexBufferSize];
        }
        int ii = 0;
        for (int i7 = 0; i7 < numFaces; i7++) {
            int i8 = ii;
            int i9 = ii;
            int ii2 = ii + 1;
            this.indexBufferShort[i8] = (short) this.indexBuffer[i9];
            int ii3 = ii2 + 1;
            this.indexBufferShort[ii2] = (short) this.indexBuffer[ii2];
            ii = ii3 + 1;
            this.indexBufferShort[ii3] = (short) this.indexBuffer[ii3];
        }
        this.indexBuffer = null;
        return buildNativeGeometry(this.vertexBuffer, this.numberOfVertices * 9, this.indexBufferShort, this.indexBufferSize);
    }

    private boolean updatePNTGeometry(float[] points, int[] pointsFromAndLengthIndices, float[] normals, int[] normalsFromAndLengthIndices, float[] texCoords, int[] texCoordsFromAndLengthIndices) {
        if (this.dirtyVertices == null) {
            this.dirtyVertices = new boolean[this.numberOfVertices];
        }
        Arrays.fill(this.dirtyVertices, false);
        int startPoint = pointsFromAndLengthIndices[0] / 3;
        int numPoints = pointsFromAndLengthIndices[1] / 3;
        if (pointsFromAndLengthIndices[1] % 3 > 0) {
            numPoints++;
        }
        if (numPoints > 0) {
            for (int i2 = 0; i2 < numPoints; i2++) {
                int pointOffset = (startPoint + i2) * 3;
                MeshGeomComp2VB mp2vb = this.point2vbMap.get(Integer.valueOf(pointOffset));
                if (!$assertionsDisabled && mp2vb == null) {
                    throw new AssertionError();
                }
                if (mp2vb != null) {
                    int[] locs = mp2vb.getLocs();
                    int validLocs = mp2vb.getValidLocs();
                    if (locs != null) {
                        for (int j2 = 0; j2 < validLocs; j2++) {
                            int vbIndex = locs[j2] * 9;
                            this.vertexBuffer[vbIndex] = points[pointOffset];
                            this.vertexBuffer[vbIndex + 1] = points[pointOffset + 1];
                            this.vertexBuffer[vbIndex + 2] = points[pointOffset + 2];
                            this.dirtyVertices[locs[j2]] = true;
                        }
                    } else {
                        int loc = mp2vb.getLoc();
                        int vbIndex2 = loc * 9;
                        this.vertexBuffer[vbIndex2] = points[pointOffset];
                        this.vertexBuffer[vbIndex2 + 1] = points[pointOffset + 1];
                        this.vertexBuffer[vbIndex2 + 2] = points[pointOffset + 2];
                        this.dirtyVertices[loc] = true;
                    }
                }
            }
        }
        int startTexCoord = texCoordsFromAndLengthIndices[0] / 2;
        int numTexCoords = texCoordsFromAndLengthIndices[1] / 2;
        if (texCoordsFromAndLengthIndices[1] % 2 > 0) {
            numTexCoords++;
        }
        if (numTexCoords > 0) {
            for (int i3 = 0; i3 < numTexCoords; i3++) {
                int texCoordOffset = (startTexCoord + i3) * 2;
                MeshGeomComp2VB mt2vb = this.texCoord2vbMap.get(Integer.valueOf(texCoordOffset));
                if (!$assertionsDisabled && mt2vb == null) {
                    throw new AssertionError();
                }
                if (mt2vb != null) {
                    int[] locs2 = mt2vb.getLocs();
                    int validLocs2 = mt2vb.getValidLocs();
                    if (locs2 != null) {
                        for (int j3 = 0; j3 < validLocs2; j3++) {
                            int vbIndex3 = (locs2[j3] * 9) + 3;
                            this.vertexBuffer[vbIndex3] = texCoords[texCoordOffset];
                            this.vertexBuffer[vbIndex3 + 1] = texCoords[texCoordOffset + 1];
                            this.dirtyVertices[locs2[j3]] = true;
                        }
                    } else {
                        int loc2 = mt2vb.getLoc();
                        int vbIndex4 = (loc2 * 9) + 3;
                        this.vertexBuffer[vbIndex4] = texCoords[texCoordOffset];
                        this.vertexBuffer[vbIndex4 + 1] = texCoords[texCoordOffset + 1];
                        this.dirtyVertices[loc2] = true;
                    }
                }
            }
        }
        int startNormal = normalsFromAndLengthIndices[0] / 3;
        int numNormals = normalsFromAndLengthIndices[1] / 3;
        if (normalsFromAndLengthIndices[1] % 3 > 0) {
            numNormals++;
        }
        if (numNormals > 0) {
            MeshTempState.getInstance();
            for (int i4 = 0; i4 < numNormals; i4++) {
                int normalOffset = (startNormal + i4) * 3;
                MeshGeomComp2VB mn2vb = this.normal2vbMap.get(Integer.valueOf(normalOffset));
                if (!$assertionsDisabled && mn2vb == null) {
                    throw new AssertionError();
                }
                if (mn2vb != null) {
                    int[] locs3 = mn2vb.getLocs();
                    int validLocs3 = mn2vb.getValidLocs();
                    if (locs3 != null) {
                        for (int j4 = 0; j4 < validLocs3; j4++) {
                            int index = locs3[j4] * 3;
                            this.cachedNormals[index] = normals[normalOffset];
                            this.cachedNormals[index + 1] = normals[normalOffset + 1];
                            this.cachedNormals[index + 2] = normals[normalOffset + 2];
                            this.dirtyVertices[locs3[j4]] = true;
                        }
                    } else {
                        int loc3 = mn2vb.getLoc();
                        int index2 = loc3 * 3;
                        this.cachedNormals[index2] = normals[normalOffset];
                        this.cachedNormals[index2 + 1] = normals[normalOffset + 1];
                        this.cachedNormals[index2 + 2] = normals[normalOffset + 2];
                        this.dirtyVertices[loc3] = true;
                    }
                }
            }
        }
        MeshTempState instance = MeshTempState.getInstance();
        for (int i5 = 0; i5 < 3; i5++) {
            if (instance.triPoints[i5] == null) {
                instance.triPoints[i5] = new Vec3f();
            }
            if (instance.triTexCoords[i5] == null) {
                instance.triTexCoords[i5] = new Vec2f();
            }
        }
        for (int j5 = 0; j5 < this.numberOfVertices; j5 += 3) {
            if (this.dirtyVertices[j5] || this.dirtyVertices[j5 + 1] || this.dirtyVertices[j5 + 2]) {
                int vbIndex5 = j5 * 9;
                for (int i6 = 0; i6 < 3; i6++) {
                    instance.triPoints[i6].f11933x = this.vertexBuffer[vbIndex5];
                    instance.triPoints[i6].f11934y = this.vertexBuffer[vbIndex5 + 1];
                    instance.triPoints[i6].f11935z = this.vertexBuffer[vbIndex5 + 2];
                    instance.triTexCoords[i6].f11928x = this.vertexBuffer[vbIndex5 + 3];
                    instance.triTexCoords[i6].f11929y = this.vertexBuffer[vbIndex5 + 3 + 1];
                    vbIndex5 += 9;
                }
                MeshUtil.computeTBNNormalized(instance.triPoints[0], instance.triPoints[1], instance.triPoints[2], instance.triTexCoords[0], instance.triTexCoords[1], instance.triTexCoords[2], instance.triNormals);
                int index3 = j5 * 3;
                for (int i7 = 0; i7 < 3; i7++) {
                    this.cachedTangents[index3] = instance.triNormals[1].f11933x;
                    this.cachedTangents[index3 + 1] = instance.triNormals[1].f11934y;
                    this.cachedTangents[index3 + 2] = instance.triNormals[1].f11935z;
                    this.cachedBitangents[index3] = instance.triNormals[2].f11933x;
                    this.cachedBitangents[index3 + 1] = instance.triNormals[2].f11934y;
                    this.cachedBitangents[index3 + 2] = instance.triNormals[2].f11935z;
                    index3 += 3;
                }
            }
        }
        convertNormalsToQuats(instance, this.numberOfVertices, this.cachedNormals, this.cachedTangents, this.cachedBitangents, this.vertexBuffer, this.dirtyVertices);
        if (this.indexBuffer != null) {
            return buildNativeGeometry(this.vertexBuffer, this.numberOfVertices * 9, this.indexBuffer, this.indexBufferSize);
        }
        return buildNativeGeometry(this.vertexBuffer, this.numberOfVertices * 9, this.indexBufferShort, this.indexBufferSize);
    }

    @Override // com.sun.prism.Mesh
    public boolean buildGeometry(boolean userDefinedNormals, float[] points, int[] pointsFromAndLengthIndices, float[] normals, int[] normalsFromAndLengthIndices, float[] texCoords, int[] texCoordsFromAndLengthIndices, int[] faces, int[] facesFromAndLengthIndices, int[] faceSmoothingGroups, int[] faceSmoothingGroupsFromAndLengthIndices) {
        if (userDefinedNormals) {
            return buildPNTGeometry(points, pointsFromAndLengthIndices, normals, normalsFromAndLengthIndices, texCoords, texCoordsFromAndLengthIndices, faces, facesFromAndLengthIndices);
        }
        return buildPTGeometry(points, pointsFromAndLengthIndices, texCoords, texCoordsFromAndLengthIndices, faces, facesFromAndLengthIndices, faceSmoothingGroups, faceSmoothingGroupsFromAndLengthIndices);
    }

    private boolean buildPNTGeometry(float[] points, int[] pointsFromAndLengthIndices, float[] normals, int[] normalsFromAndLengthIndices, float[] texCoords, int[] texCoordsFromAndLengthIndices, int[] faces, int[] facesFromAndLengthIndices) {
        boolean updatePoints = pointsFromAndLengthIndices[1] > 0;
        boolean updateNormals = normalsFromAndLengthIndices[1] > 0;
        boolean updateTexCoords = texCoordsFromAndLengthIndices[1] > 0;
        boolean updateFaces = facesFromAndLengthIndices[1] > 0;
        boolean buildGeom = (updatePoints || updateNormals || updateTexCoords || updateFaces) ? false : true;
        if (updateFaces) {
            buildGeom = true;
        }
        if (!buildGeom && this.vertexBuffer != null && (this.indexBuffer != null || this.indexBufferShort != null)) {
            return updatePNTGeometry(points, pointsFromAndLengthIndices, normals, normalsFromAndLengthIndices, texCoords, texCoordsFromAndLengthIndices);
        }
        return doBuildPNTGeometry(points, normals, texCoords, faces);
    }

    private boolean buildPTGeometry(float[] pos, int[] posFromAndLengthIndices, float[] uv, int[] uvFromAndLengthIndices, int[] faces, int[] facesFromAndLengthIndices, int[] smoothing, int[] smoothingFromAndLengthIndices) {
        this.nVerts = pos.length / 3;
        this.nTVerts = uv.length / 2;
        this.nFaces = faces.length / (VertexFormat.POINT_TEXCOORD.getVertexIndexSize() * 3);
        if (!$assertionsDisabled && (this.nVerts <= 0 || this.nFaces <= 0 || this.nTVerts <= 0)) {
            throw new AssertionError();
        }
        this.pos = pos;
        this.uv = uv;
        this.faces = faces;
        this.smoothing = smoothing.length == this.nFaces ? smoothing : null;
        if (PrismSettings.skipMeshNormalComputation) {
            boolean updatePoints = posFromAndLengthIndices[1] > 0;
            boolean updateTexCoords = uvFromAndLengthIndices[1] > 0;
            boolean updateFaces = facesFromAndLengthIndices[1] > 0;
            boolean updateSmoothing = smoothingFromAndLengthIndices[1] > 0;
            boolean buildGeom = (updatePoints || updateTexCoords || updateFaces || updateSmoothing) ? false : true;
            if (updateFaces || updateSmoothing) {
                buildGeom = true;
            }
            if (!buildGeom && this.vertexBuffer != null && (this.indexBuffer != null || this.indexBufferShort != null)) {
                return updateSkipMeshNormalGeometry(posFromAndLengthIndices, uvFromAndLengthIndices);
            }
            return buildSkipMeshNormalGeometry();
        }
        MeshTempState instance = MeshTempState.getInstance();
        if (instance.pool == null || instance.pool.length < this.nFaces * 3) {
            instance.pool = new MeshVertex[this.nFaces * 3];
        }
        if (instance.indexBuffer == null || instance.indexBuffer.length < this.nFaces * 3) {
            instance.indexBuffer = new int[this.nFaces * 3];
        }
        if (instance.pVertex == null || instance.pVertex.length < this.nVerts) {
            instance.pVertex = new MeshVertex[this.nVerts];
        } else {
            Arrays.fill(instance.pVertex, 0, instance.pVertex.length, (Object) null);
        }
        checkSmoothingGroup();
        computeTBNormal(instance.pool, instance.pVertex, instance.indexBuffer);
        int nNewVerts = MeshVertex.processVertices(instance.pVertex, this.nVerts, this.allHardEdges, this.allSameSmoothing);
        if (instance.vertexBuffer == null || instance.vertexBuffer.length < nNewVerts * 9) {
            instance.vertexBuffer = new float[nNewVerts * 9];
        }
        buildVertexBuffer(instance.pVertex, instance.vertexBuffer);
        if (nNewVerts > 65536) {
            buildIndexBuffer(instance.pool, instance.indexBuffer, null);
            return buildNativeGeometry(instance.vertexBuffer, nNewVerts * 9, instance.indexBuffer, this.nFaces * 3);
        }
        if (instance.indexBufferShort == null || instance.indexBufferShort.length < this.nFaces * 3) {
            instance.indexBufferShort = new short[this.nFaces * 3];
        }
        buildIndexBuffer(instance.pool, instance.indexBuffer, instance.indexBufferShort);
        return buildNativeGeometry(instance.vertexBuffer, nNewVerts * 9, instance.indexBufferShort, this.nFaces * 3);
    }

    private void computeTBNormal(MeshVertex[] pool, MeshVertex[] pVertex, int[] indexBuffer) {
        int iOrdinal;
        MeshTempState instance = MeshTempState.getInstance();
        int[] smFace = instance.smFace;
        int[] triVerts = instance.triVerts;
        Vec3f[] triPoints = instance.triPoints;
        Vec2f[] triTexCoords = instance.triTexCoords;
        Vec3f[] triNormals = instance.triNormals;
        String logname = BaseMesh.class.getName();
        int nDeadFaces = 0;
        int poolIndex = 0;
        for (int f2 = 0; f2 < this.nFaces; f2++) {
            int index = f2 * 3;
            smFace = getFace(f2, smFace);
            triVerts[0] = smFace[FaceMembers.POINT0.ordinal()];
            triVerts[1] = smFace[FaceMembers.POINT1.ordinal()];
            triVerts[2] = smFace[FaceMembers.POINT2.ordinal()];
            if (MeshUtil.isDeadFace(triVerts) && PlatformLogger.getLogger(logname).isLoggable(PlatformLogger.Level.FINE)) {
                nDeadFaces++;
                PlatformLogger.getLogger(logname).fine("Dead face [" + triVerts[0] + ", " + triVerts[1] + ", " + triVerts[2] + "] @ face group " + f2 + "; nEmptyFaces = " + nDeadFaces);
            }
            for (int i2 = 0; i2 < 3; i2++) {
                triPoints[i2] = getVertex(triVerts[i2], triPoints[i2]);
            }
            triVerts[0] = smFace[FaceMembers.TEXCOORD0.ordinal()];
            triVerts[1] = smFace[FaceMembers.TEXCOORD1.ordinal()];
            triVerts[2] = smFace[FaceMembers.TEXCOORD2.ordinal()];
            for (int i3 = 0; i3 < 3; i3++) {
                triTexCoords[i3] = getTVertex(triVerts[i3], triTexCoords[i3]);
            }
            MeshUtil.computeTBNNormalized(triPoints[0], triPoints[1], triPoints[2], triTexCoords[0], triTexCoords[1], triTexCoords[2], triNormals);
            for (int j2 = 0; j2 < 3; j2++) {
                pool[poolIndex] = pool[poolIndex] == null ? new MeshVertex() : pool[poolIndex];
                for (int i4 = 0; i4 < 3; i4++) {
                    pool[poolIndex].norm[i4].set(triNormals[i4]);
                }
                pool[poolIndex].smGroup = smFace[FaceMembers.SMOOTHING_GROUP.ordinal()];
                pool[poolIndex].fIdx = f2;
                pool[poolIndex].tVert = triVerts[j2];
                pool[poolIndex].index = -1;
                if (j2 == 0) {
                    iOrdinal = FaceMembers.POINT0.ordinal();
                } else if (j2 == 1) {
                    iOrdinal = FaceMembers.POINT1.ordinal();
                } else {
                    iOrdinal = FaceMembers.POINT2.ordinal();
                }
                int ii = iOrdinal;
                int pIdx = smFace[ii];
                pool[poolIndex].pVert = pIdx;
                indexBuffer[index + j2] = pIdx;
                pool[poolIndex].next = pVertex[pIdx];
                pVertex[pIdx] = pool[poolIndex];
                poolIndex++;
            }
        }
    }

    private void buildVSQuat(Vec3f[] tm, Quat4f quat) {
        Vec3f v2 = MeshTempState.getInstance().vec3f1;
        v2.cross(tm[1], tm[2]);
        float d2 = tm[0].dot(v2);
        if (d2 < 0.0f) {
            tm[2].mul(-1.0f);
        }
        MeshUtil.buildQuat(tm, quat);
        if (d2 < 0.0f) {
            if (quat.f11912w == 0.0f) {
                quat.f11912w = 1.0E-10f;
            }
            quat.scale(-1.0f);
        }
    }

    private void buildVertexBuffer(MeshVertex[] pVerts, float[] vertexBuffer) {
        Quat4f quat = MeshTempState.getInstance().quat;
        int idLast = 0;
        int index = 0;
        for (int i2 = 0; i2 < this.nVerts; i2++) {
            MeshVertex meshVertex = pVerts[i2];
            while (true) {
                MeshVertex v2 = meshVertex;
                if (v2 != null) {
                    if (v2.index == idLast) {
                        int ind = v2.pVert * 3;
                        int i3 = index;
                        int index2 = index + 1;
                        vertexBuffer[i3] = this.pos[ind];
                        int index3 = index2 + 1;
                        vertexBuffer[index2] = this.pos[ind + 1];
                        int index4 = index3 + 1;
                        vertexBuffer[index3] = this.pos[ind + 2];
                        int ind2 = v2.tVert * 2;
                        int index5 = index4 + 1;
                        vertexBuffer[index4] = this.uv[ind2];
                        int index6 = index5 + 1;
                        vertexBuffer[index5] = this.uv[ind2 + 1];
                        buildVSQuat(v2.norm, quat);
                        int index7 = index6 + 1;
                        vertexBuffer[index6] = quat.f11909x;
                        int index8 = index7 + 1;
                        vertexBuffer[index7] = quat.f11910y;
                        int index9 = index8 + 1;
                        vertexBuffer[index8] = quat.f11911z;
                        index = index9 + 1;
                        vertexBuffer[index9] = quat.f11912w;
                        idLast++;
                    }
                    meshVertex = v2.next;
                }
            }
        }
    }

    private void buildIndexBuffer(MeshVertex[] pool, int[] indexBuffer, short[] indexBufferShort) {
        for (int i2 = 0; i2 < this.nFaces; i2++) {
            int index = i2 * 3;
            if (indexBuffer[index] != -1) {
                for (int j2 = 0; j2 < 3; j2++) {
                    if (!$assertionsDisabled && pool[index].fIdx != i2) {
                        throw new AssertionError();
                    }
                    if (indexBufferShort != null) {
                        indexBufferShort[index + j2] = (short) pool[index + j2].index;
                    } else {
                        indexBuffer[index + j2] = pool[index + j2].index;
                    }
                    pool[index + j2].next = null;
                }
            } else {
                for (int j3 = 0; j3 < 3; j3++) {
                    if (indexBufferShort != null) {
                        indexBufferShort[index + j3] = 0;
                    } else {
                        indexBuffer[index + j3] = 0;
                    }
                }
            }
        }
    }

    public int getNumVerts() {
        return this.nVerts;
    }

    public int getNumTVerts() {
        return this.nTVerts;
    }

    public int getNumFaces() {
        return this.nFaces;
    }

    public Vec3f getVertex(int pIdx, Vec3f vertex) {
        if (vertex == null) {
            vertex = new Vec3f();
        }
        int index = pIdx * 3;
        vertex.set(this.pos[index], this.pos[index + 1], this.pos[index + 2]);
        return vertex;
    }

    public Vec2f getTVertex(int tIdx, Vec2f texCoord) {
        if (texCoord == null) {
            texCoord = new Vec2f();
        }
        int index = tIdx * 2;
        texCoord.set(this.uv[index], this.uv[index + 1]);
        return texCoord;
    }

    private void checkSmoothingGroup() {
        if (this.smoothing == null || this.smoothing.length == 0) {
            this.allSameSmoothing = true;
            this.allHardEdges = false;
            return;
        }
        for (int i2 = 0; i2 + 1 < this.smoothing.length; i2++) {
            if (this.smoothing[i2] != this.smoothing[i2 + 1]) {
                this.allSameSmoothing = false;
                this.allHardEdges = false;
                return;
            }
        }
        if (this.smoothing[0] == 0) {
            this.allSameSmoothing = false;
            this.allHardEdges = true;
        } else {
            this.allSameSmoothing = true;
            this.allHardEdges = false;
        }
    }

    public int[] getFace(int fIdx, int[] face) {
        int index = fIdx * 6;
        if (face == null || face.length < 7) {
            face = new int[7];
        }
        for (int i2 = 0; i2 < 6; i2++) {
            face[i2] = this.faces[index + i2];
        }
        face[6] = this.smoothing != null ? this.smoothing[fIdx] : 1;
        return face;
    }

    public boolean isValid() {
        return true;
    }

    boolean test_isVertexBufferNull() {
        return this.vertexBuffer == null;
    }

    int test_getVertexBufferLength() {
        return this.vertexBuffer.length;
    }

    int test_getNumberOfVertices() {
        return this.numberOfVertices;
    }

    /* loaded from: jfxrt.jar:com/sun/prism/impl/BaseMesh$MeshGeomComp2VB.class */
    class MeshGeomComp2VB {
        private final int key;
        private final int loc;
        private int[] locs;
        private int validLocs;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !BaseMesh.class.desiredAssertionStatus();
        }

        MeshGeomComp2VB(int key, int loc) {
            if (!$assertionsDisabled && loc < 0) {
                throw new AssertionError();
            }
            this.key = key;
            this.loc = loc;
            this.locs = null;
            this.validLocs = 0;
        }

        void addLoc(int loc) {
            if (this.locs == null) {
                this.locs = new int[3];
                this.locs[0] = this.loc;
                this.locs[1] = loc;
                this.validLocs = 2;
                return;
            }
            if (this.locs.length > this.validLocs) {
                this.locs[this.validLocs] = loc;
                this.validLocs++;
                return;
            }
            int[] temp = new int[this.validLocs * 2];
            System.arraycopy(this.locs, 0, temp, 0, this.locs.length);
            this.locs = temp;
            this.locs[this.validLocs] = loc;
            this.validLocs++;
        }

        int getKey() {
            return this.key;
        }

        int getLoc() {
            return this.loc;
        }

        int[] getLocs() {
            return this.locs;
        }

        int getValidLocs() {
            return this.validLocs;
        }
    }
}
