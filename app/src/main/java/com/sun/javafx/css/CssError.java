package com.sun.javafx.css;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.scene.Scene;

/* loaded from: jfxrt.jar:com/sun/javafx/css/CssError.class */
public class CssError {
    private static Reference<Scene> SCENE_REF;
    private final Reference<Scene> sceneRef = SCENE_REF;
    protected final String message;

    public static void setCurrentScene(Scene scene) {
        if (StyleManager.getErrors() == null) {
            return;
        }
        if (scene != null) {
            Scene oldScene = SCENE_REF != null ? SCENE_REF.get() : null;
            if (oldScene != scene) {
                SCENE_REF = new WeakReference(scene);
                return;
            }
            return;
        }
        SCENE_REF = null;
    }

    public final String getMessage() {
        return this.message;
    }

    public CssError(String message) {
        this.message = message;
    }

    public Scene getScene() {
        if (this.sceneRef != null) {
            return this.sceneRef.get();
        }
        return null;
    }

    public String toString() {
        return "CSS Error: " + this.message;
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/css/CssError$StylesheetParsingError.class */
    public static final class StylesheetParsingError extends CssError {
        private final String url;

        public StylesheetParsingError(String url, String message) {
            super(message);
            this.url = url;
        }

        public String getURL() {
            return this.url;
        }

        @Override // com.sun.javafx.css.CssError
        public String toString() {
            String path = this.url != null ? this.url : "?";
            return "CSS Error parsing " + path + ": " + this.message;
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/css/CssError$InlineStyleParsingError.class */
    public static final class InlineStyleParsingError extends CssError {
        private final Styleable styleable;

        public InlineStyleParsingError(Styleable styleable, String message) {
            super(message);
            this.styleable = styleable;
        }

        public Styleable getStyleable() {
            return this.styleable;
        }

        @Override // com.sun.javafx.css.CssError
        public String toString() {
            String inlineStyle = this.styleable.getStyle();
            String source = this.styleable.toString();
            return "CSS Error parsing in-line style '" + inlineStyle + "' from " + source + ": " + this.message;
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/css/CssError$StringParsingError.class */
    public static final class StringParsingError extends CssError {
        private final String style;

        public StringParsingError(String style, String message) {
            super(message);
            this.style = style;
        }

        public String getStyle() {
            return this.style;
        }

        @Override // com.sun.javafx.css.CssError
        public String toString() {
            return "CSS Error parsing '" + this.style + ": " + this.message;
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/css/CssError$PropertySetError.class */
    public static final class PropertySetError extends CssError {
        private final CssMetaData styleableProperty;
        private final Styleable styleable;

        public PropertySetError(CssMetaData styleableProperty, Styleable styleable, String message) {
            super(message);
            this.styleableProperty = styleableProperty;
            this.styleable = styleable;
        }

        public Styleable getStyleable() {
            return this.styleable;
        }

        public CssMetaData getProperty() {
            return this.styleableProperty;
        }
    }
}
