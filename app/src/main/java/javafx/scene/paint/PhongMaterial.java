package javafx.scene.paint;

import com.sun.javafx.beans.event.AbstractNotifyListener;
import com.sun.javafx.sg.prism.NGPhongMaterial;
import com.sun.javafx.tk.Toolkit;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;

/* loaded from: jfxrt.jar:javafx/scene/paint/PhongMaterial.class */
public class PhongMaterial extends Material {
    private ObjectProperty<Color> diffuseColor;
    private ObjectProperty<Color> specularColor;
    private DoubleProperty specularPower;
    private ObjectProperty<Image> diffuseMap;
    private Image oldDiffuseMap;
    private ObjectProperty<Image> specularMap;
    private Image oldSpecularMap;
    private ObjectProperty<Image> bumpMap;
    private Image oldBumpMap;
    private ObjectProperty<Image> selfIlluminationMap;
    private Image oldSelfIlluminationMap;
    private NGPhongMaterial peer;
    private boolean diffuseColorDirty = true;
    private boolean specularColorDirty = true;
    private boolean specularPowerDirty = true;
    private boolean diffuseMapDirty = true;
    private boolean specularMapDirty = true;
    private boolean bumpMapDirty = true;
    private boolean selfIlluminationMapDirty = true;
    private final AbstractNotifyListener platformImageChangeListener = new AbstractNotifyListener() { // from class: javafx.scene.paint.PhongMaterial.4
        @Override // javafx.beans.InvalidationListener
        public void invalidated(Observable valueModel) {
            if (PhongMaterial.this.oldDiffuseMap == null || valueModel != Toolkit.getImageAccessor().getImageProperty(PhongMaterial.this.oldDiffuseMap)) {
                if (PhongMaterial.this.oldSpecularMap == null || valueModel != Toolkit.getImageAccessor().getImageProperty(PhongMaterial.this.oldSpecularMap)) {
                    if (PhongMaterial.this.oldBumpMap == null || valueModel != Toolkit.getImageAccessor().getImageProperty(PhongMaterial.this.oldBumpMap)) {
                        if (PhongMaterial.this.oldSelfIlluminationMap != null && valueModel == Toolkit.getImageAccessor().getImageProperty(PhongMaterial.this.oldSelfIlluminationMap)) {
                            PhongMaterial.this.selfIlluminationMapDirty = true;
                        }
                    } else {
                        PhongMaterial.this.bumpMapDirty = true;
                    }
                } else {
                    PhongMaterial.this.specularMapDirty = true;
                }
            } else {
                PhongMaterial.this.diffuseMapDirty = true;
            }
            PhongMaterial.this.setDirty(true);
        }
    };

    public PhongMaterial() {
        setDiffuseColor(Color.WHITE);
    }

    public PhongMaterial(Color diffuseColor) {
        setDiffuseColor(diffuseColor);
    }

    public PhongMaterial(Color diffuseColor, Image diffuseMap, Image specularMap, Image bumpMap, Image selfIlluminationMap) {
        setDiffuseColor(diffuseColor);
        setDiffuseMap(diffuseMap);
        setSpecularMap(specularMap);
        setBumpMap(bumpMap);
        setSelfIlluminationMap(selfIlluminationMap);
    }

    public final void setDiffuseColor(Color value) {
        diffuseColorProperty().set(value);
    }

    public final Color getDiffuseColor() {
        if (this.diffuseColor == null) {
            return null;
        }
        return this.diffuseColor.get();
    }

    public final ObjectProperty<Color> diffuseColorProperty() {
        if (this.diffuseColor == null) {
            this.diffuseColor = new SimpleObjectProperty<Color>(this, "diffuseColor") { // from class: javafx.scene.paint.PhongMaterial.1
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    PhongMaterial.this.diffuseColorDirty = true;
                    PhongMaterial.this.setDirty(true);
                }
            };
        }
        return this.diffuseColor;
    }

    public final void setSpecularColor(Color value) {
        specularColorProperty().set(value);
    }

    public final Color getSpecularColor() {
        if (this.specularColor == null) {
            return null;
        }
        return this.specularColor.get();
    }

    public final ObjectProperty<Color> specularColorProperty() {
        if (this.specularColor == null) {
            this.specularColor = new SimpleObjectProperty<Color>(this, "specularColor") { // from class: javafx.scene.paint.PhongMaterial.2
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    PhongMaterial.this.specularColorDirty = true;
                    PhongMaterial.this.setDirty(true);
                }
            };
        }
        return this.specularColor;
    }

    public final void setSpecularPower(double value) {
        specularPowerProperty().set(value);
    }

    public final double getSpecularPower() {
        if (this.specularPower == null) {
            return 32.0d;
        }
        return this.specularPower.get();
    }

    public final DoubleProperty specularPowerProperty() {
        if (this.specularPower == null) {
            this.specularPower = new SimpleDoubleProperty(this, "specularPower", 32.0d) { // from class: javafx.scene.paint.PhongMaterial.3
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    PhongMaterial.this.specularPowerDirty = true;
                    PhongMaterial.this.setDirty(true);
                }
            };
        }
        return this.specularPower;
    }

    public final void setDiffuseMap(Image value) {
        diffuseMapProperty().set(value);
    }

    public final Image getDiffuseMap() {
        if (this.diffuseMap == null) {
            return null;
        }
        return this.diffuseMap.get();
    }

    public final ObjectProperty<Image> diffuseMapProperty() {
        if (this.diffuseMap == null) {
            this.diffuseMap = new SimpleObjectProperty<Image>(this, "diffuseMap") { // from class: javafx.scene.paint.PhongMaterial.5
                private boolean needsListeners = false;

                @Override // javafx.beans.property.ObjectPropertyBase
                public void invalidated() {
                    Image _image = get();
                    if (this.needsListeners) {
                        Toolkit.getImageAccessor().getImageProperty(PhongMaterial.this.oldDiffuseMap).removeListener(PhongMaterial.this.platformImageChangeListener.getWeakListener());
                    }
                    this.needsListeners = _image != null && (Toolkit.getImageAccessor().isAnimation(_image) || _image.getProgress() < 1.0d);
                    if (this.needsListeners) {
                        Toolkit.getImageAccessor().getImageProperty(_image).addListener(PhongMaterial.this.platformImageChangeListener.getWeakListener());
                    }
                    PhongMaterial.this.oldDiffuseMap = _image;
                    PhongMaterial.this.diffuseMapDirty = true;
                    PhongMaterial.this.setDirty(true);
                }
            };
        }
        return this.diffuseMap;
    }

    public final void setSpecularMap(Image value) {
        specularMapProperty().set(value);
    }

    public final Image getSpecularMap() {
        if (this.specularMap == null) {
            return null;
        }
        return this.specularMap.get();
    }

    public final ObjectProperty<Image> specularMapProperty() {
        if (this.specularMap == null) {
            this.specularMap = new SimpleObjectProperty<Image>(this, "specularMap") { // from class: javafx.scene.paint.PhongMaterial.6
                private boolean needsListeners = false;

                @Override // javafx.beans.property.ObjectPropertyBase
                public void invalidated() {
                    Image _image = get();
                    if (this.needsListeners) {
                        Toolkit.getImageAccessor().getImageProperty(PhongMaterial.this.oldSpecularMap).removeListener(PhongMaterial.this.platformImageChangeListener.getWeakListener());
                    }
                    this.needsListeners = _image != null && (Toolkit.getImageAccessor().isAnimation(_image) || _image.getProgress() < 1.0d);
                    if (this.needsListeners) {
                        Toolkit.getImageAccessor().getImageProperty(_image).addListener(PhongMaterial.this.platformImageChangeListener.getWeakListener());
                    }
                    PhongMaterial.this.oldSpecularMap = _image;
                    PhongMaterial.this.specularMapDirty = true;
                    PhongMaterial.this.setDirty(true);
                }
            };
        }
        return this.specularMap;
    }

    public final void setBumpMap(Image value) {
        bumpMapProperty().set(value);
    }

    public final Image getBumpMap() {
        if (this.bumpMap == null) {
            return null;
        }
        return this.bumpMap.get();
    }

    public final ObjectProperty<Image> bumpMapProperty() {
        if (this.bumpMap == null) {
            this.bumpMap = new SimpleObjectProperty<Image>(this, "bumpMap") { // from class: javafx.scene.paint.PhongMaterial.7
                private boolean needsListeners = false;

                @Override // javafx.beans.property.ObjectPropertyBase
                public void invalidated() {
                    Image _image = get();
                    if (this.needsListeners) {
                        Toolkit.getImageAccessor().getImageProperty(PhongMaterial.this.oldBumpMap).removeListener(PhongMaterial.this.platformImageChangeListener.getWeakListener());
                    }
                    this.needsListeners = _image != null && (Toolkit.getImageAccessor().isAnimation(_image) || _image.getProgress() < 1.0d);
                    if (this.needsListeners) {
                        Toolkit.getImageAccessor().getImageProperty(_image).addListener(PhongMaterial.this.platformImageChangeListener.getWeakListener());
                    }
                    PhongMaterial.this.oldBumpMap = _image;
                    PhongMaterial.this.bumpMapDirty = true;
                    PhongMaterial.this.setDirty(true);
                }
            };
        }
        return this.bumpMap;
    }

    public final void setSelfIlluminationMap(Image value) {
        selfIlluminationMapProperty().set(value);
    }

    public final Image getSelfIlluminationMap() {
        if (this.selfIlluminationMap == null) {
            return null;
        }
        return this.selfIlluminationMap.get();
    }

    public final ObjectProperty<Image> selfIlluminationMapProperty() {
        if (this.selfIlluminationMap == null) {
            this.selfIlluminationMap = new SimpleObjectProperty<Image>(this, "selfIlluminationMap") { // from class: javafx.scene.paint.PhongMaterial.8
                private boolean needsListeners = false;

                @Override // javafx.beans.property.ObjectPropertyBase
                public void invalidated() {
                    Image _image = get();
                    if (this.needsListeners) {
                        Toolkit.getImageAccessor().getImageProperty(PhongMaterial.this.oldSelfIlluminationMap).removeListener(PhongMaterial.this.platformImageChangeListener.getWeakListener());
                    }
                    this.needsListeners = _image != null && (Toolkit.getImageAccessor().isAnimation(_image) || _image.getProgress() < 1.0d);
                    if (this.needsListeners) {
                        Toolkit.getImageAccessor().getImageProperty(_image).addListener(PhongMaterial.this.platformImageChangeListener.getWeakListener());
                    }
                    PhongMaterial.this.oldSelfIlluminationMap = _image;
                    PhongMaterial.this.selfIlluminationMapDirty = true;
                    PhongMaterial.this.setDirty(true);
                }
            };
        }
        return this.selfIlluminationMap;
    }

    @Override // javafx.scene.paint.Material
    void setDirty(boolean value) {
        super.setDirty(value);
        if (!value) {
            this.diffuseColorDirty = false;
            this.specularColorDirty = false;
            this.specularPowerDirty = false;
            this.diffuseMapDirty = false;
            this.specularMapDirty = false;
            this.bumpMapDirty = false;
            this.selfIlluminationMapDirty = false;
        }
    }

    @Override // javafx.scene.paint.Material
    @Deprecated
    public NGPhongMaterial impl_getNGMaterial() {
        if (this.peer == null) {
            this.peer = new NGPhongMaterial();
        }
        return this.peer;
    }

    @Override // javafx.scene.paint.Material
    @Deprecated
    public void impl_updatePG() {
        if (!isDirty()) {
            return;
        }
        NGPhongMaterial pMaterial = impl_getNGMaterial();
        if (this.diffuseColorDirty) {
            pMaterial.setDiffuseColor(getDiffuseColor() == null ? null : Toolkit.getPaintAccessor().getPlatformPaint(getDiffuseColor()));
        }
        if (this.specularColorDirty) {
            pMaterial.setSpecularColor(getSpecularColor() == null ? null : Toolkit.getPaintAccessor().getPlatformPaint(getSpecularColor()));
        }
        if (this.specularPowerDirty) {
            pMaterial.setSpecularPower((float) getSpecularPower());
        }
        if (this.diffuseMapDirty) {
            pMaterial.setDiffuseMap(getDiffuseMap() == null ? null : getDiffuseMap().impl_getPlatformImage());
        }
        if (this.specularMapDirty) {
            pMaterial.setSpecularMap(getSpecularMap() == null ? null : getSpecularMap().impl_getPlatformImage());
        }
        if (this.bumpMapDirty) {
            pMaterial.setBumpMap(getBumpMap() == null ? null : getBumpMap().impl_getPlatformImage());
        }
        if (this.selfIlluminationMapDirty) {
            pMaterial.setSelfIllumMap(getSelfIlluminationMap() == null ? null : getSelfIlluminationMap().impl_getPlatformImage());
        }
        setDirty(false);
    }

    public String toString() {
        return "PhongMaterial[diffuseColor=" + ((Object) getDiffuseColor()) + ", specularColor=" + ((Object) getSpecularColor()) + ", specularPower=" + getSpecularPower() + ", diffuseMap=" + ((Object) getDiffuseMap()) + ", specularMap=" + ((Object) getSpecularMap()) + ", bumpMap=" + ((Object) getBumpMap()) + ", selfIlluminationMap=" + ((Object) getSelfIlluminationMap()) + "]";
    }
}
