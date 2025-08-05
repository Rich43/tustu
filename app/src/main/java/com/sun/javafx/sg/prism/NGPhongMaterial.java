package com.sun.javafx.sg.prism;

import com.sun.prism.Image;
import com.sun.prism.Material;
import com.sun.prism.PhongMaterial;
import com.sun.prism.ResourceFactory;
import com.sun.prism.TextureMap;
import com.sun.prism.paint.Color;

/* loaded from: jfxrt.jar:com/sun/javafx/sg/prism/NGPhongMaterial.class */
public class NGPhongMaterial {
    private static final Image WHITE_1X1 = Image.fromIntArgbPreData(new int[]{-1}, 1, 1);
    private PhongMaterial material;
    private Color diffuseColor;
    private Color specularColor;
    private float specularPower;
    private boolean diffuseColorDirty = true;
    private TextureMap diffuseMap = new TextureMap(PhongMaterial.MapType.DIFFUSE);
    private boolean specularColorDirty = true;
    private boolean specularPowerDirty = true;
    private TextureMap specularMap = new TextureMap(PhongMaterial.MapType.SPECULAR);
    private TextureMap bumpMap = new TextureMap(PhongMaterial.MapType.BUMP);
    private TextureMap selfIllumMap = new TextureMap(PhongMaterial.MapType.SELF_ILLUM);

    Material createMaterial(ResourceFactory f2) {
        if (this.material != null && !this.material.isValid()) {
            disposeMaterial();
        }
        if (this.material == null) {
            this.material = f2.createPhongMaterial();
        }
        validate(f2);
        return this.material;
    }

    private void disposeMaterial() {
        this.diffuseColorDirty = true;
        this.specularColorDirty = true;
        this.specularPowerDirty = true;
        this.diffuseMap.setDirty(true);
        this.specularMap.setDirty(true);
        this.bumpMap.setDirty(true);
        this.selfIllumMap.setDirty(true);
        this.material.dispose();
        this.material = null;
    }

    private void validate(ResourceFactory f2) {
        if (this.diffuseColorDirty) {
            if (this.diffuseColor != null) {
                this.material.setDiffuseColor(this.diffuseColor.getRed(), this.diffuseColor.getGreen(), this.diffuseColor.getBlue(), this.diffuseColor.getAlpha());
            } else {
                this.material.setDiffuseColor(0.0f, 0.0f, 0.0f, 0.0f);
            }
            this.diffuseColorDirty = false;
        }
        if (this.diffuseMap.isDirty()) {
            if (this.diffuseMap.getImage() == null) {
                this.diffuseMap.setImage(WHITE_1X1);
            }
            this.material.setTextureMap(this.diffuseMap);
        }
        if (this.bumpMap.isDirty()) {
            this.material.setTextureMap(this.bumpMap);
        }
        if (this.selfIllumMap.isDirty()) {
            this.material.setTextureMap(this.selfIllumMap);
        }
        if (this.specularMap.isDirty()) {
            this.material.setTextureMap(this.specularMap);
        }
        if (this.specularColorDirty || this.specularPowerDirty) {
            if (this.specularColor != null) {
                float r2 = this.specularColor.getRed();
                float g2 = this.specularColor.getGreen();
                float b2 = this.specularColor.getBlue();
                this.material.setSpecularColor(true, r2, g2, b2, this.specularPower);
            } else {
                this.material.setSpecularColor(false, 1.0f, 1.0f, 1.0f, this.specularPower);
            }
            this.specularColorDirty = false;
            this.specularPowerDirty = false;
        }
    }

    public void setDiffuseColor(Object diffuseColor) {
        this.diffuseColor = (Color) diffuseColor;
        this.diffuseColorDirty = true;
    }

    public void setSpecularColor(Object specularColor) {
        this.specularColor = (Color) specularColor;
        this.specularColorDirty = true;
    }

    public void setSpecularPower(float specularPower) {
        if (specularPower < 0.001f) {
            specularPower = 0.001f;
        }
        this.specularPower = specularPower;
        this.specularPowerDirty = true;
    }

    public void setDiffuseMap(Object diffuseMap) {
        this.diffuseMap.setImage((Image) diffuseMap);
        this.diffuseMap.setDirty(true);
    }

    public void setSpecularMap(Object specularMap) {
        this.specularMap.setImage((Image) specularMap);
        this.specularMap.setDirty(true);
    }

    public void setBumpMap(Object bumpMap) {
        this.bumpMap.setImage((Image) bumpMap);
        this.bumpMap.setDirty(true);
    }

    public void setSelfIllumMap(Object selfIllumMap) {
        this.selfIllumMap.setImage((Image) selfIllumMap);
        this.selfIllumMap.setDirty(true);
    }

    Color test_getDiffuseColor() {
        return this.diffuseColor;
    }
}
