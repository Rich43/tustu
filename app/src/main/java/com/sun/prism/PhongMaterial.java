package com.sun.prism;

/* loaded from: jfxrt.jar:com/sun/prism/PhongMaterial.class */
public interface PhongMaterial extends Material {
    public static final int DIFFUSE = MapType.DIFFUSE.ordinal();
    public static final int SPECULAR = MapType.SPECULAR.ordinal();
    public static final int BUMP = MapType.BUMP.ordinal();
    public static final int SELF_ILLUM = MapType.SELF_ILLUM.ordinal();
    public static final int MAX_MAP_TYPE = MapType.values().length;

    /* loaded from: jfxrt.jar:com/sun/prism/PhongMaterial$MapType.class */
    public enum MapType {
        DIFFUSE,
        SPECULAR,
        BUMP,
        SELF_ILLUM
    }

    void setDiffuseColor(float f2, float f3, float f4, float f5);

    void setSpecularColor(boolean z2, float f2, float f3, float f4, float f5);

    void setTextureMap(TextureMap textureMap);

    void lockTextureMaps();

    void unlockTextureMaps();
}
