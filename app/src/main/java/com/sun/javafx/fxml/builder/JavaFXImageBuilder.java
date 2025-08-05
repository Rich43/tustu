package com.sun.javafx.fxml.builder;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;
import javafx.scene.image.Image;
import javafx.util.Builder;

/* loaded from: jfxrt.jar:com/sun/javafx/fxml/builder/JavaFXImageBuilder.class */
public class JavaFXImageBuilder extends AbstractMap<String, Object> implements Builder<Image> {
    private String url = "";
    private double requestedWidth = 0.0d;
    private double requestedHeight = 0.0d;
    private boolean preserveRatio = false;
    private boolean smooth = false;
    private boolean backgroundLoading = false;

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    /* renamed from: build */
    public Image build2() {
        return new Image(this.url, this.requestedWidth, this.requestedHeight, this.preserveRatio, this.smooth, this.backgroundLoading);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Object put(String key, Object value) {
        if (value != null) {
            String str = value.toString();
            if ("url".equals(key)) {
                this.url = str;
                return null;
            }
            if ("requestedWidth".equals(key)) {
                this.requestedWidth = Double.parseDouble(str);
                return null;
            }
            if ("requestedHeight".equals(key)) {
                this.requestedHeight = Double.parseDouble(str);
                return null;
            }
            if ("preserveRatio".equals(key)) {
                this.preserveRatio = Boolean.parseBoolean(str);
                return null;
            }
            if ("smooth".equals(key)) {
                this.smooth = Boolean.parseBoolean(str);
                return null;
            }
            if ("backgroundLoading".equals(key)) {
                this.backgroundLoading = Boolean.parseBoolean(str);
                return null;
            }
            throw new IllegalArgumentException("Unknown Image property: " + key);
        }
        return null;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<Map.Entry<String, Object>> entrySet() {
        throw new UnsupportedOperationException();
    }
}
