package com.sun.javafx.css;

import javafx.css.CssMetaData;
import javafx.css.StyleConverter;
import javafx.css.StyleableProperty;
import javafx.scene.Node;

/* loaded from: jfxrt.jar:com/sun/javafx/css/SubCssMetaData.class */
public class SubCssMetaData<T> extends CssMetaData<Node, T> {
    public SubCssMetaData(String property, StyleConverter converter, T initialValue) {
        super(property, converter, initialValue);
    }

    public SubCssMetaData(String property, StyleConverter converter) {
        super(property, converter);
    }

    @Override // javafx.css.CssMetaData
    public boolean isSettable(Node node) {
        return false;
    }

    @Override // javafx.css.CssMetaData
    public StyleableProperty<T> getStyleableProperty(Node node) {
        return null;
    }
}
