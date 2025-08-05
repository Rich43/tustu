package java.awt.color;

import java.io.IOException;
import java.io.ObjectInputStream;
import sun.java2d.cmm.CMSManager;
import sun.java2d.cmm.ColorTransform;
import sun.java2d.cmm.PCMM;

/* loaded from: rt.jar:java/awt/color/ICC_ColorSpace.class */
public class ICC_ColorSpace extends ColorSpace {
    static final long serialVersionUID = 3455889114070431483L;
    private ICC_Profile thisProfile;
    private float[] minVal;
    private float[] maxVal;
    private float[] diffMinMax;
    private float[] invDiffMinMax;
    private boolean needScaleInit;
    private transient ColorTransform this2srgb;
    private transient ColorTransform srgb2this;
    private transient ColorTransform this2xyz;
    private transient ColorTransform xyz2this;

    public ICC_ColorSpace(ICC_Profile iCC_Profile) {
        super(iCC_Profile.getColorSpaceType(), iCC_Profile.getNumComponents());
        this.needScaleInit = true;
        int profileClass = iCC_Profile.getProfileClass();
        if (profileClass != 0 && profileClass != 1 && profileClass != 2 && profileClass != 4 && profileClass != 6 && profileClass != 5) {
            throw new IllegalArgumentException("Invalid profile type");
        }
        this.thisProfile = iCC_Profile;
        setMinMax();
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        if (this.thisProfile == null) {
            this.thisProfile = ICC_Profile.getInstance(1000);
        }
    }

    public ICC_Profile getProfile() {
        return this.thisProfile;
    }

    @Override // java.awt.color.ColorSpace
    public float[] toRGB(float[] fArr) {
        if (this.this2srgb == null) {
            ICC_ColorSpace iCC_ColorSpace = (ICC_ColorSpace) ColorSpace.getInstance(1000);
            PCMM module = CMSManager.getModule();
            this.this2srgb = module.createTransform(new ColorTransform[]{module.createTransform(this.thisProfile, -1, 1), module.createTransform(iCC_ColorSpace.getProfile(), -1, 2)});
            if (this.needScaleInit) {
                setComponentScaling();
            }
        }
        int numComponents = getNumComponents();
        short[] sArr = new short[numComponents];
        for (int i2 = 0; i2 < numComponents; i2++) {
            sArr[i2] = (short) (((fArr[i2] - this.minVal[i2]) * this.invDiffMinMax[i2]) + 0.5f);
        }
        short[] sArrColorConvert = this.this2srgb.colorConvert(sArr, (short[]) null);
        float[] fArr2 = new float[3];
        for (int i3 = 0; i3 < 3; i3++) {
            fArr2[i3] = (sArrColorConvert[i3] & 65535) / 65535.0f;
        }
        return fArr2;
    }

    @Override // java.awt.color.ColorSpace
    public float[] fromRGB(float[] fArr) {
        if (this.srgb2this == null) {
            ICC_ColorSpace iCC_ColorSpace = (ICC_ColorSpace) ColorSpace.getInstance(1000);
            PCMM module = CMSManager.getModule();
            this.srgb2this = module.createTransform(new ColorTransform[]{module.createTransform(iCC_ColorSpace.getProfile(), -1, 1), module.createTransform(this.thisProfile, -1, 2)});
            if (this.needScaleInit) {
                setComponentScaling();
            }
        }
        short[] sArr = new short[3];
        for (int i2 = 0; i2 < 3; i2++) {
            sArr[i2] = (short) ((fArr[i2] * 65535.0f) + 0.5f);
        }
        short[] sArrColorConvert = this.srgb2this.colorConvert(sArr, (short[]) null);
        int numComponents = getNumComponents();
        float[] fArr2 = new float[numComponents];
        for (int i3 = 0; i3 < numComponents; i3++) {
            fArr2[i3] = (((sArrColorConvert[i3] & 65535) / 65535.0f) * this.diffMinMax[i3]) + this.minVal[i3];
        }
        return fArr2;
    }

    @Override // java.awt.color.ColorSpace
    public float[] toCIEXYZ(float[] fArr) {
        if (this.this2xyz == null) {
            ColorTransform[] colorTransformArr = new ColorTransform[2];
            ICC_ColorSpace iCC_ColorSpace = (ICC_ColorSpace) ColorSpace.getInstance(1001);
            PCMM module = CMSManager.getModule();
            try {
                colorTransformArr[0] = module.createTransform(this.thisProfile, 1, 1);
            } catch (CMMException e2) {
                colorTransformArr[0] = module.createTransform(this.thisProfile, -1, 1);
            }
            colorTransformArr[1] = module.createTransform(iCC_ColorSpace.getProfile(), -1, 2);
            this.this2xyz = module.createTransform(colorTransformArr);
            if (this.needScaleInit) {
                setComponentScaling();
            }
        }
        int numComponents = getNumComponents();
        short[] sArr = new short[numComponents];
        for (int i2 = 0; i2 < numComponents; i2++) {
            sArr[i2] = (short) (((fArr[i2] - this.minVal[i2]) * this.invDiffMinMax[i2]) + 0.5f);
        }
        short[] sArrColorConvert = this.this2xyz.colorConvert(sArr, (short[]) null);
        float[] fArr2 = new float[3];
        for (int i3 = 0; i3 < 3; i3++) {
            fArr2[i3] = ((sArrColorConvert[i3] & 65535) / 65535.0f) * 1.9999695f;
        }
        return fArr2;
    }

    @Override // java.awt.color.ColorSpace
    public float[] fromCIEXYZ(float[] fArr) {
        if (this.xyz2this == null) {
            ColorTransform[] colorTransformArr = new ColorTransform[2];
            ICC_ColorSpace iCC_ColorSpace = (ICC_ColorSpace) ColorSpace.getInstance(1001);
            PCMM module = CMSManager.getModule();
            colorTransformArr[0] = module.createTransform(iCC_ColorSpace.getProfile(), -1, 1);
            try {
                colorTransformArr[1] = module.createTransform(this.thisProfile, 1, 2);
            } catch (CMMException e2) {
                colorTransformArr[1] = CMSManager.getModule().createTransform(this.thisProfile, -1, 2);
            }
            this.xyz2this = module.createTransform(colorTransformArr);
            if (this.needScaleInit) {
                setComponentScaling();
            }
        }
        short[] sArr = new short[3];
        float f2 = 65535.0f / 1.9999695f;
        for (int i2 = 0; i2 < 3; i2++) {
            sArr[i2] = (short) ((fArr[i2] * f2) + 0.5f);
        }
        short[] sArrColorConvert = this.xyz2this.colorConvert(sArr, (short[]) null);
        int numComponents = getNumComponents();
        float[] fArr2 = new float[numComponents];
        for (int i3 = 0; i3 < numComponents; i3++) {
            fArr2[i3] = (((sArrColorConvert[i3] & 65535) / 65535.0f) * this.diffMinMax[i3]) + this.minVal[i3];
        }
        return fArr2;
    }

    @Override // java.awt.color.ColorSpace
    public float getMinValue(int i2) {
        if (i2 < 0 || i2 > getNumComponents() - 1) {
            throw new IllegalArgumentException("Component index out of range: " + i2);
        }
        return this.minVal[i2];
    }

    @Override // java.awt.color.ColorSpace
    public float getMaxValue(int i2) {
        if (i2 < 0 || i2 > getNumComponents() - 1) {
            throw new IllegalArgumentException("Component index out of range: " + i2);
        }
        return this.maxVal[i2];
    }

    private void setMinMax() {
        int numComponents = getNumComponents();
        int type = getType();
        this.minVal = new float[numComponents];
        this.maxVal = new float[numComponents];
        if (type == 1) {
            this.minVal[0] = 0.0f;
            this.maxVal[0] = 100.0f;
            this.minVal[1] = -128.0f;
            this.maxVal[1] = 127.0f;
            this.minVal[2] = -128.0f;
            this.maxVal[2] = 127.0f;
            return;
        }
        if (type == 0) {
            float[] fArr = this.minVal;
            float[] fArr2 = this.minVal;
            this.minVal[2] = 0.0f;
            fArr2[1] = 0.0f;
            fArr[0] = 0.0f;
            float[] fArr3 = this.maxVal;
            float[] fArr4 = this.maxVal;
            this.maxVal[2] = 1.9999695f;
            fArr4[1] = 1.9999695f;
            fArr3[0] = 1.9999695f;
            return;
        }
        for (int i2 = 0; i2 < numComponents; i2++) {
            this.minVal[i2] = 0.0f;
            this.maxVal[i2] = 1.0f;
        }
    }

    private void setComponentScaling() {
        int numComponents = getNumComponents();
        this.diffMinMax = new float[numComponents];
        this.invDiffMinMax = new float[numComponents];
        for (int i2 = 0; i2 < numComponents; i2++) {
            this.minVal[i2] = getMinValue(i2);
            this.maxVal[i2] = getMaxValue(i2);
            this.diffMinMax[i2] = this.maxVal[i2] - this.minVal[i2];
            this.invDiffMinMax[i2] = 65535.0f / this.diffMinMax[i2];
        }
        this.needScaleInit = false;
    }
}
