package com.sun.javafx.fxml.builder;

import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.shape.VertexFormat;
import javafx.util.Builder;
import sun.security.x509.CRLDistributionPointsExtension;

/* loaded from: jfxrt.jar:com/sun/javafx/fxml/builder/TriangleMeshBuilder.class */
public class TriangleMeshBuilder extends TreeMap<String, Object> implements Builder<TriangleMesh> {
    private static final String VALUE_SEPARATOR_REGEX = "[,\\s]+";
    private float[] points;
    private float[] texCoords;
    private float[] normals;
    private int[] faces;
    private int[] faceSmoothingGroups;
    private VertexFormat vertexFormat;

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    /* renamed from: build */
    public TriangleMesh build2() {
        TriangleMesh mesh = new TriangleMesh();
        if (this.points != null) {
            mesh.getPoints().setAll(this.points);
        }
        if (this.texCoords != null) {
            mesh.getTexCoords().setAll(this.texCoords);
        }
        if (this.faces != null) {
            mesh.getFaces().setAll(this.faces);
        }
        if (this.faceSmoothingGroups != null) {
            mesh.getFaceSmoothingGroups().setAll(this.faceSmoothingGroups);
        }
        if (this.normals != null) {
            mesh.getNormals().setAll(this.normals);
        }
        if (this.vertexFormat != null) {
            mesh.setVertexFormat(this.vertexFormat);
        }
        return mesh;
    }

    @Override // java.util.TreeMap, java.util.AbstractMap, java.util.Map
    public Object put(String key, Object value) {
        if (CRLDistributionPointsExtension.POINTS.equalsIgnoreCase(key)) {
            String[] split = ((String) value).split(VALUE_SEPARATOR_REGEX);
            this.points = new float[split.length];
            for (int i2 = 0; i2 < split.length; i2++) {
                this.points[i2] = Float.parseFloat(split[i2]);
            }
        } else if ("texcoords".equalsIgnoreCase(key)) {
            String[] split2 = ((String) value).split(VALUE_SEPARATOR_REGEX);
            this.texCoords = new float[split2.length];
            for (int i3 = 0; i3 < split2.length; i3++) {
                this.texCoords[i3] = Float.parseFloat(split2[i3]);
            }
        } else if ("faces".equalsIgnoreCase(key)) {
            String[] split3 = ((String) value).split(VALUE_SEPARATOR_REGEX);
            this.faces = new int[split3.length];
            for (int i4 = 0; i4 < split3.length; i4++) {
                this.faces[i4] = Integer.parseInt(split3[i4]);
            }
        } else if ("facesmoothinggroups".equalsIgnoreCase(key)) {
            String[] split4 = ((String) value).split(VALUE_SEPARATOR_REGEX);
            this.faceSmoothingGroups = new int[split4.length];
            for (int i5 = 0; i5 < split4.length; i5++) {
                this.faceSmoothingGroups[i5] = Integer.parseInt(split4[i5]);
            }
        } else if ("normals".equalsIgnoreCase(key)) {
            String[] split5 = ((String) value).split(VALUE_SEPARATOR_REGEX);
            this.normals = new float[split5.length];
            for (int i6 = 0; i6 < split5.length; i6++) {
                this.normals[i6] = Float.parseFloat(split5[i6]);
            }
        } else if ("vertexformat".equalsIgnoreCase(key)) {
            if (value instanceof VertexFormat) {
                this.vertexFormat = (VertexFormat) value;
            } else if ("point_texcoord".equalsIgnoreCase((String) value)) {
                this.vertexFormat = VertexFormat.POINT_TEXCOORD;
            } else if ("point_normal_texcoord".equalsIgnoreCase((String) value)) {
                this.vertexFormat = VertexFormat.POINT_NORMAL_TEXCOORD;
            }
        }
        return super.put((TriangleMeshBuilder) key.toLowerCase(Locale.ROOT), (String) value);
    }

    @Override // java.util.TreeMap, java.util.AbstractMap, java.util.Map
    public Set<Map.Entry<String, Object>> entrySet() {
        return super.entrySet();
    }
}
