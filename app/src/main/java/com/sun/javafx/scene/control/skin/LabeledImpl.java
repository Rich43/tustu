package com.sun.javafx.scene.control.skin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.css.CssMetaData;
import javafx.css.StyleOrigin;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.layout.Region;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/LabeledImpl.class */
public class LabeledImpl extends Label {
    private final Shuttler shuttler;

    public LabeledImpl(Labeled labeled) {
        this.shuttler = new Shuttler(this, labeled);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void initialize(Shuttler shuttler, LabeledImpl labeledImpl, Labeled labeled) {
        labeledImpl.setText(labeled.getText());
        labeled.textProperty().addListener(shuttler);
        labeledImpl.setGraphic(labeled.getGraphic());
        labeled.graphicProperty().addListener(shuttler);
        List<CssMetaData<? extends Styleable, ?>> styleables = StyleableProperties.STYLEABLES_TO_MIRROR;
        int nMax = styleables.size();
        for (int n2 = 0; n2 < nMax; n2++) {
            CssMetaData<? extends Styleable, ?> cssMetaData = styleables.get(n2);
            if (!"-fx-skin".equals(cssMetaData.getProperty())) {
                StyleableProperty<?> fromVal = cssMetaData.getStyleableProperty(labeled);
                if (fromVal instanceof Observable) {
                    ((Observable) fromVal).addListener(shuttler);
                    StyleOrigin origin = fromVal.getStyleOrigin();
                    if (origin != null) {
                        StyleableProperty<Object> styleableProperty = cssMetaData.getStyleableProperty(labeledImpl);
                        styleableProperty.applyStyle(origin, fromVal.getValue());
                    }
                }
            }
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/LabeledImpl$Shuttler.class */
    private static class Shuttler implements InvalidationListener {
        private final LabeledImpl labeledImpl;
        private final Labeled labeled;

        Shuttler(LabeledImpl labeledImpl, Labeled labeled) {
            this.labeledImpl = labeledImpl;
            this.labeled = labeled;
            LabeledImpl.initialize(this, labeledImpl, labeled);
        }

        @Override // javafx.beans.InvalidationListener
        public void invalidated(Observable valueModel) {
            StyleableProperty<?> styleableProperty;
            CssMetaData<? extends Styleable, ?> cssMetaData;
            if (valueModel == this.labeled.textProperty()) {
                this.labeledImpl.setText(this.labeled.getText());
                return;
            }
            if (valueModel == this.labeled.graphicProperty()) {
                StyleOrigin origin = ((StyleableProperty) this.labeled.graphicProperty()).getStyleOrigin();
                if (origin == null || origin == StyleOrigin.USER) {
                    this.labeledImpl.setGraphic(this.labeled.getGraphic());
                    return;
                }
                return;
            }
            if ((valueModel instanceof StyleableProperty) && (cssMetaData = (styleableProperty = (StyleableProperty) valueModel).getCssMetaData()) != null) {
                StyleOrigin origin2 = styleableProperty.getStyleOrigin();
                StyleableProperty<Object> targetProperty = cssMetaData.getStyleableProperty(this.labeledImpl);
                targetProperty.applyStyle(origin2, styleableProperty.getValue());
            }
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/LabeledImpl$StyleableProperties.class */
    static final class StyleableProperties {
        static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES_TO_MIRROR;

        StyleableProperties() {
        }

        static {
            List<CssMetaData<? extends Styleable, ?>> labeledStyleables = Labeled.getClassCssMetaData();
            Collection<?> parentStyleables = Region.getClassCssMetaData();
            List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(labeledStyleables);
            styleables.removeAll(parentStyleables);
            STYLEABLES_TO_MIRROR = Collections.unmodifiableList(styleables);
        }
    }
}
