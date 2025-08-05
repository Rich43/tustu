package javafx.scene.control;

import com.sun.javafx.scene.control.skin.ProgressBarSkin;
import javafx.css.StyleableProperty;
import javafx.geometry.Orientation;
import javafx.scene.AccessibleAttribute;

/* loaded from: jfxrt.jar:javafx/scene/control/ProgressBar.class */
public class ProgressBar extends ProgressIndicator {
    private static final String DEFAULT_STYLE_CLASS = "progress-bar";

    public ProgressBar() {
        this(-1.0d);
    }

    public ProgressBar(double progress) {
        ((StyleableProperty) focusTraversableProperty()).applyStyle(null, Boolean.FALSE);
        setProgress(progress);
        getStyleClass().setAll(DEFAULT_STYLE_CLASS);
    }

    @Override // javafx.scene.control.ProgressIndicator, javafx.scene.control.Control
    protected Skin<?> createDefaultSkin() {
        return new ProgressBarSkin(this);
    }

    @Override // javafx.scene.control.ProgressIndicator, javafx.scene.control.Control, javafx.scene.Node
    @Deprecated
    protected Boolean impl_cssGetFocusTraversableInitialValue() {
        return Boolean.FALSE;
    }

    @Override // javafx.scene.control.ProgressIndicator, javafx.scene.control.Control, javafx.scene.Parent, javafx.scene.Node
    public Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
        switch (attribute) {
            case ORIENTATION:
                return Orientation.HORIZONTAL;
            default:
                return super.queryAccessibleAttribute(attribute, parameters);
        }
    }
}
