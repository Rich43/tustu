package com.sun.javafx.sg.prism;

import com.sun.javafx.collections.FloatArraySyncer;
import com.sun.javafx.collections.IntegerArraySyncer;
import com.sun.prism.Mesh;
import com.sun.prism.ResourceFactory;

/* loaded from: jfxrt.jar:com/sun/javafx/sg/prism/NGTriangleMesh.class */
public class NGTriangleMesh {
    private Mesh mesh;
    private float[] points;
    private float[] normals;
    private float[] texCoords;
    private int[] faces;
    private int[] faceSmoothingGroups;
    private boolean meshDirty = true;
    private boolean userDefinedNormals = false;
    private int[] pointsFromAndLengthIndices = new int[2];
    private int[] normalsFromAndLengthIndices = new int[2];
    private int[] texCoordsFromAndLengthIndices = new int[2];
    private int[] facesFromAndLengthIndices = new int[2];
    private int[] faceSmoothingGroupsFromAndLengthIndices = new int[2];

    Mesh createMesh(ResourceFactory rf) {
        if (this.mesh != null && !this.mesh.isValid()) {
            this.mesh.dispose();
            this.mesh = null;
        }
        if (this.mesh == null) {
            this.mesh = rf.createMesh();
            this.meshDirty = true;
        }
        return this.mesh;
    }

    boolean validate() {
        if (this.points == null || this.texCoords == null || this.faces == null || this.faceSmoothingGroups == null) {
            return false;
        }
        if (this.userDefinedNormals && this.normals == null) {
            return false;
        }
        if (this.meshDirty) {
            if (!this.mesh.buildGeometry(this.userDefinedNormals, this.points, this.pointsFromAndLengthIndices, this.normals, this.normalsFromAndLengthIndices, this.texCoords, this.texCoordsFromAndLengthIndices, this.faces, this.facesFromAndLengthIndices, this.faceSmoothingGroups, this.faceSmoothingGroupsFromAndLengthIndices)) {
                throw new RuntimeException("NGTriangleMesh: buildGeometry failed");
            }
            this.meshDirty = false;
            return true;
        }
        return true;
    }

    void setPointsByRef(float[] points) {
        this.meshDirty = true;
        this.points = points;
    }

    void setNormalsByRef(float[] normals) {
        this.meshDirty = true;
        this.normals = normals;
    }

    void setTexCoordsByRef(float[] texCoords) {
        this.meshDirty = true;
        this.texCoords = texCoords;
    }

    void setFacesByRef(int[] faces) {
        this.meshDirty = true;
        this.faces = faces;
    }

    void setFaceSmoothingGroupsByRef(int[] faceSmoothingGroups) {
        this.meshDirty = true;
        this.faceSmoothingGroups = faceSmoothingGroups;
    }

    public void setUserDefinedNormals(boolean userDefinedNormals) {
        this.userDefinedNormals = userDefinedNormals;
    }

    public boolean isUserDefinedNormals() {
        return this.userDefinedNormals;
    }

    public void syncPoints(FloatArraySyncer array) {
        this.meshDirty = true;
        this.points = array != null ? array.syncTo(this.points, this.pointsFromAndLengthIndices) : null;
    }

    public void syncNormals(FloatArraySyncer array) {
        this.meshDirty = true;
        this.normals = array != null ? array.syncTo(this.normals, this.normalsFromAndLengthIndices) : null;
    }

    public void syncTexCoords(FloatArraySyncer array) {
        this.meshDirty = true;
        this.texCoords = array != null ? array.syncTo(this.texCoords, this.texCoordsFromAndLengthIndices) : null;
    }

    public void syncFaces(IntegerArraySyncer array) {
        this.meshDirty = true;
        this.faces = array != null ? array.syncTo(this.faces, this.facesFromAndLengthIndices) : null;
    }

    public void syncFaceSmoothingGroups(IntegerArraySyncer array) {
        this.meshDirty = true;
        this.faceSmoothingGroups = array != null ? array.syncTo(this.faceSmoothingGroups, this.faceSmoothingGroupsFromAndLengthIndices) : null;
    }

    int[] test_getFaceSmoothingGroups() {
        return this.faceSmoothingGroups;
    }

    int[] test_getFaces() {
        return this.faces;
    }

    float[] test_getPoints() {
        return this.points;
    }

    float[] test_getNormals() {
        return this.normals;
    }

    float[] test_getTexCoords() {
        return this.texCoords;
    }

    Mesh test_getMesh() {
        return this.mesh;
    }
}
