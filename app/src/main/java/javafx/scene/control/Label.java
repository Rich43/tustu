package javafx.scene.control;

import com.sun.javafx.scene.NodeHelper;
import com.sun.javafx.scene.control.skin.LabelSkin;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.css.StyleableProperty;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;

/* loaded from: jfxrt.jar:javafx/scene/control/Label.class */
public class Label extends Labeled {
    private ChangeListener<Boolean> mnemonicStateListener;
    private ObjectProperty<Node> labelFor;

    public Label() {
        this.mnemonicStateListener = (observable, oldValue, newValue) -> {
            impl_showMnemonicsProperty().setValue(newValue);
        };
        initialize();
    }

    public Label(String text) {
        super(text);
        this.mnemonicStateListener = (observable, oldValue, newValue) -> {
            impl_showMnemonicsProperty().setValue(newValue);
        };
        initialize();
    }

    public Label(String text, Node graphic) {
        super(text, graphic);
        this.mnemonicStateListener = (observable, oldValue, newValue) -> {
            impl_showMnemonicsProperty().setValue(newValue);
        };
        initialize();
    }

    private void initialize() {
        getStyleClass().setAll("label");
        setAccessibleRole(AccessibleRole.TEXT);
        ((StyleableProperty) focusTraversableProperty()).applyStyle(null, Boolean.FALSE);
    }

    public ObjectProperty<Node> labelForProperty() {
        if (this.labelFor == null) {
            this.labelFor = new ObjectPropertyBase<Node>() { // from class: javafx.scene.control.Label.1
                Node oldValue = null;

                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    if (this.oldValue != null) {
                        NodeHelper.getNodeAccessor().setLabeledBy(this.oldValue, null);
                        this.oldValue.impl_showMnemonicsProperty().removeListener(Label.this.mnemonicStateListener);
                    }
                    Node node = get();
                    if (node != null) {
                        NodeHelper.getNodeAccessor().setLabeledBy(node, Label.this);
                        node.impl_showMnemonicsProperty().addListener(Label.this.mnemonicStateListener);
                        Label.this.impl_setShowMnemonics(node.impl_isShowMnemonics());
                    } else {
                        Label.this.impl_setShowMnemonics(false);
                    }
                    this.oldValue = node;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Label.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "labelFor";
                }
            };
        }
        return this.labelFor;
    }

    public final void setLabelFor(Node value) {
        labelForProperty().setValue(value);
    }

    public final Node getLabelFor() {
        if (this.labelFor == null) {
            return null;
        }
        return this.labelFor.getValue2();
    }

    @Override // javafx.scene.control.Control
    protected Skin<?> createDefaultSkin() {
        return new LabelSkin(this);
    }

    @Override // javafx.scene.control.Control, javafx.scene.Node
    @Deprecated
    protected Boolean impl_cssGetFocusTraversableInitialValue() {
        return Boolean.FALSE;
    }
}
