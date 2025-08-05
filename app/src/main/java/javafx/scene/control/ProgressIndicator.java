package javafx.scene.control;

import com.sun.javafx.scene.control.skin.ProgressIndicatorSkin;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.css.PseudoClass;
import javafx.css.StyleableProperty;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;

/* loaded from: jfxrt.jar:javafx/scene/control/ProgressIndicator.class */
public class ProgressIndicator extends Control {
    public static final double INDETERMINATE_PROGRESS = -1.0d;
    private ReadOnlyBooleanWrapper indeterminate;
    private DoubleProperty progress;
    private static final String DEFAULT_STYLE_CLASS = "progress-indicator";
    private static final PseudoClass PSEUDO_CLASS_DETERMINATE = PseudoClass.getPseudoClass("determinate");
    private static final PseudoClass PSEUDO_CLASS_INDETERMINATE = PseudoClass.getPseudoClass("indeterminate");

    public ProgressIndicator() {
        this(-1.0d);
    }

    public ProgressIndicator(double progress) {
        ((StyleableProperty) focusTraversableProperty()).applyStyle(null, Boolean.FALSE);
        setProgress(progress);
        getStyleClass().setAll(DEFAULT_STYLE_CLASS);
        setAccessibleRole(AccessibleRole.PROGRESS_INDICATOR);
        int c2 = Double.compare(-1.0d, progress);
        pseudoClassStateChanged(PSEUDO_CLASS_INDETERMINATE, c2 == 0);
        pseudoClassStateChanged(PSEUDO_CLASS_DETERMINATE, c2 != 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setIndeterminate(boolean value) {
        indeterminatePropertyImpl().set(value);
    }

    public final boolean isIndeterminate() {
        if (this.indeterminate == null) {
            return true;
        }
        return this.indeterminate.get();
    }

    public final ReadOnlyBooleanProperty indeterminateProperty() {
        return indeterminatePropertyImpl().getReadOnlyProperty();
    }

    private ReadOnlyBooleanWrapper indeterminatePropertyImpl() {
        if (this.indeterminate == null) {
            this.indeterminate = new ReadOnlyBooleanWrapper(true) { // from class: javafx.scene.control.ProgressIndicator.1
                @Override // javafx.beans.property.BooleanPropertyBase
                protected void invalidated() {
                    boolean active = get();
                    ProgressIndicator.this.pseudoClassStateChanged(ProgressIndicator.PSEUDO_CLASS_INDETERMINATE, active);
                    ProgressIndicator.this.pseudoClassStateChanged(ProgressIndicator.PSEUDO_CLASS_DETERMINATE, !active);
                }

                @Override // javafx.beans.property.SimpleBooleanProperty, javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return ProgressIndicator.this;
                }

                @Override // javafx.beans.property.SimpleBooleanProperty, javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "indeterminate";
                }
            };
        }
        return this.indeterminate;
    }

    public final void setProgress(double value) {
        progressProperty().set(value);
    }

    public final double getProgress() {
        if (this.progress == null) {
            return -1.0d;
        }
        return this.progress.get();
    }

    public final DoubleProperty progressProperty() {
        if (this.progress == null) {
            this.progress = new DoublePropertyBase(-1.0d) { // from class: javafx.scene.control.ProgressIndicator.2
                @Override // javafx.beans.property.DoublePropertyBase
                protected void invalidated() {
                    ProgressIndicator.this.setIndeterminate(ProgressIndicator.this.getProgress() < 0.0d);
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return ProgressIndicator.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "progress";
                }
            };
        }
        return this.progress;
    }

    @Override // javafx.scene.control.Control
    protected Skin<?> createDefaultSkin() {
        return new ProgressIndicatorSkin(this);
    }

    @Override // javafx.scene.control.Control, javafx.scene.Node
    @Deprecated
    protected Boolean impl_cssGetFocusTraversableInitialValue() {
        return Boolean.FALSE;
    }

    @Override // javafx.scene.control.Control, javafx.scene.Parent, javafx.scene.Node
    public Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
        switch (attribute) {
            case VALUE:
                return Double.valueOf(getProgress());
            case MAX_VALUE:
                return Double.valueOf(1.0d);
            case MIN_VALUE:
                return Double.valueOf(0.0d);
            case INDETERMINATE:
                return Boolean.valueOf(isIndeterminate());
            default:
                return super.queryAccessibleAttribute(attribute, parameters);
        }
    }
}
