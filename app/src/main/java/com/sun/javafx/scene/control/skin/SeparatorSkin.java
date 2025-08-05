package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import java.util.Collections;
import javafx.geometry.Orientation;
import javafx.scene.control.Separator;
import javafx.scene.layout.Region;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/SeparatorSkin.class */
public class SeparatorSkin extends BehaviorSkinBase<Separator, BehaviorBase<Separator>> {
    private static final double DEFAULT_LENGTH = 10.0d;
    private final Region line;

    public SeparatorSkin(Separator separator) {
        super(separator, new BehaviorBase(separator, Collections.emptyList()));
        this.line = new Region();
        this.line.getStyleClass().setAll("line");
        getChildren().add(this.line);
        registerChangeListener(separator.orientationProperty(), "ORIENTATION");
        registerChangeListener(separator.halignmentProperty(), "HALIGNMENT");
        registerChangeListener(separator.valignmentProperty(), "VALIGNMENT");
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.skin.BehaviorSkinBase
    protected void handleControlPropertyChanged(String p2) {
        super.handleControlPropertyChanged(p2);
        if ("ORIENTATION".equals(p2) || "HALIGNMENT".equals(p2) || "VALIGNMENT".equals(p2)) {
            ((Separator) getSkinnable()).requestLayout();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // javafx.scene.control.SkinBase
    protected void layoutChildren(double x2, double y2, double w2, double h2) {
        Separator sep = (Separator) getSkinnable();
        if (sep.getOrientation() == Orientation.HORIZONTAL) {
            this.line.resize(w2, this.line.prefHeight(-1.0d));
        } else {
            this.line.resize(this.line.prefWidth(-1.0d), h2);
        }
        positionInArea(this.line, x2, y2, w2, h2, 0.0d, sep.getHalignment(), sep.getValignment());
    }

    @Override // javafx.scene.control.SkinBase
    protected double computeMinWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return computePrefWidth(height, topInset, rightInset, bottomInset, leftInset);
    }

    @Override // javafx.scene.control.SkinBase
    protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return computePrefHeight(width, topInset, rightInset, bottomInset, leftInset);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // javafx.scene.control.SkinBase
    protected double computePrefWidth(double h2, double topInset, double rightInset, double bottomInset, double leftInset) {
        Separator sep = (Separator) getSkinnable();
        double w2 = sep.getOrientation() == Orientation.VERTICAL ? this.line.prefWidth(-1.0d) : 10.0d;
        return w2 + leftInset + rightInset;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // javafx.scene.control.SkinBase
    protected double computePrefHeight(double w2, double topInset, double rightInset, double bottomInset, double leftInset) {
        Separator sep = (Separator) getSkinnable();
        double h2 = sep.getOrientation() == Orientation.VERTICAL ? 10.0d : this.line.prefHeight(-1.0d);
        return h2 + topInset + bottomInset;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // javafx.scene.control.SkinBase
    protected double computeMaxWidth(double h2, double topInset, double rightInset, double bottomInset, double leftInset) {
        Separator sep = (Separator) getSkinnable();
        if (sep.getOrientation() == Orientation.VERTICAL) {
            return sep.prefWidth(h2);
        }
        return Double.MAX_VALUE;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // javafx.scene.control.SkinBase
    protected double computeMaxHeight(double w2, double topInset, double rightInset, double bottomInset, double leftInset) {
        Separator sep = (Separator) getSkinnable();
        if (sep.getOrientation() == Orientation.VERTICAL) {
            return Double.MAX_VALUE;
        }
        return sep.prefHeight(w2);
    }
}
