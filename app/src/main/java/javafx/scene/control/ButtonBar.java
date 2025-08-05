package javafx.scene.control;

import com.sun.javafx.scene.control.skin.ButtonBarSkin;
import com.sun.javafx.util.Utils;
import com.sun.org.apache.xml.internal.security.utils.Constants;
import java.util.Map;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.StyleableProperty;
import javafx.scene.Node;
import org.icepdf.core.util.PdfOps;
import org.slf4j.Marker;

/* loaded from: jfxrt.jar:javafx/scene/control/ButtonBar.class */
public class ButtonBar extends Control {
    public static final String BUTTON_ORDER_WINDOWS = "L_E+U+FBXI_YNOCAH_R";
    public static final String BUTTON_ORDER_MAC_OS = "L_HE+U+FBIX_NCYOA_R";
    public static final String BUTTON_ORDER_LINUX = "L_HE+UNYACBXIO_R";
    public static final String BUTTON_ORDER_NONE = "";
    private ObservableList<Node> buttons;
    private final StringProperty buttonOrderProperty;
    private final DoubleProperty buttonMinWidthProperty;

    /* loaded from: jfxrt.jar:javafx/scene/control/ButtonBar$ButtonData.class */
    public enum ButtonData {
        LEFT("L", false, false),
        RIGHT("R", false, false),
        HELP(PdfOps.H_TOKEN, false, false),
        HELP_2("E", false, false),
        YES(Constants._TAG_Y, false, true),
        NO("N", true, false),
        NEXT_FORWARD("X", false, true),
        BACK_PREVIOUS(PdfOps.B_TOKEN, false, false),
        FINISH("I", false, true),
        APPLY("A", false, false),
        CANCEL_CLOSE("C", true, false),
        OK_DONE("O", false, true),
        OTHER("U", false, false),
        BIG_GAP(Marker.ANY_NON_NULL_MARKER, false, false),
        SMALL_GAP("_", false, false);

        private final String typeCode;
        private final boolean cancelButton;
        private final boolean defaultButton;

        ButtonData(String type, boolean cancelButton, boolean defaultButton) {
            this.typeCode = type;
            this.cancelButton = cancelButton;
            this.defaultButton = defaultButton;
        }

        public String getTypeCode() {
            return this.typeCode;
        }

        public final boolean isCancelButton() {
            return this.cancelButton;
        }

        public final boolean isDefaultButton() {
            return this.defaultButton;
        }
    }

    public static void setButtonData(Node button, ButtonData buttonData) {
        Map<Object, Object> properties = button.getProperties();
        ObjectProperty<ButtonData> property = (ObjectProperty) properties.getOrDefault(ButtonBarSkin.BUTTON_DATA_PROPERTY, new SimpleObjectProperty(button, "buttonData", buttonData));
        property.set(buttonData);
        properties.putIfAbsent(ButtonBarSkin.BUTTON_DATA_PROPERTY, property);
    }

    public static ButtonData getButtonData(Node button) {
        ObjectProperty<ButtonData> property;
        Map<Object, Object> properties = button.getProperties();
        if (!properties.containsKey(ButtonBarSkin.BUTTON_DATA_PROPERTY) || (property = (ObjectProperty) properties.get(ButtonBarSkin.BUTTON_DATA_PROPERTY)) == null) {
            return null;
        }
        return property.get();
    }

    public static void setButtonUniformSize(Node button, boolean uniformSize) {
        if (uniformSize) {
            button.getProperties().remove(ButtonBarSkin.BUTTON_SIZE_INDEPENDENCE);
        } else {
            button.getProperties().put(ButtonBarSkin.BUTTON_SIZE_INDEPENDENCE, Boolean.valueOf(uniformSize));
        }
    }

    public static boolean isButtonUniformSize(Node button) {
        return ((Boolean) button.getProperties().getOrDefault(ButtonBarSkin.BUTTON_SIZE_INDEPENDENCE, true)).booleanValue();
    }

    public ButtonBar() {
        this(null);
    }

    public ButtonBar(String buttonOrder) {
        this.buttons = FXCollections.observableArrayList();
        this.buttonOrderProperty = new SimpleStringProperty(this, "buttonOrder");
        this.buttonMinWidthProperty = new SimpleDoubleProperty(this, "buttonMinWidthProperty");
        getStyleClass().add("button-bar");
        ((StyleableProperty) focusTraversableProperty()).applyStyle(null, Boolean.FALSE);
        boolean buttonOrderEmpty = buttonOrder == null || buttonOrder.isEmpty();
        if (Utils.isMac()) {
            setButtonOrder(buttonOrderEmpty ? BUTTON_ORDER_MAC_OS : buttonOrder);
            setButtonMinWidth(70.0d);
        } else if (Utils.isUnix()) {
            setButtonOrder(buttonOrderEmpty ? BUTTON_ORDER_LINUX : buttonOrder);
            setButtonMinWidth(85.0d);
        } else {
            setButtonOrder(buttonOrderEmpty ? BUTTON_ORDER_WINDOWS : buttonOrder);
            setButtonMinWidth(75.0d);
        }
    }

    @Override // javafx.scene.control.Control
    protected Skin<?> createDefaultSkin() {
        return new ButtonBarSkin(this);
    }

    public final ObservableList<Node> getButtons() {
        return this.buttons;
    }

    public final StringProperty buttonOrderProperty() {
        return this.buttonOrderProperty;
    }

    public final void setButtonOrder(String buttonOrder) {
        this.buttonOrderProperty.set(buttonOrder);
    }

    public final String getButtonOrder() {
        return this.buttonOrderProperty.get();
    }

    public final DoubleProperty buttonMinWidthProperty() {
        return this.buttonMinWidthProperty;
    }

    public final void setButtonMinWidth(double value) {
        this.buttonMinWidthProperty.set(value);
    }

    public final double getButtonMinWidth() {
        return this.buttonMinWidthProperty.get();
    }

    @Override // javafx.scene.control.Control, javafx.scene.Node
    @Deprecated
    protected Boolean impl_cssGetFocusTraversableInitialValue() {
        return Boolean.FALSE;
    }
}
