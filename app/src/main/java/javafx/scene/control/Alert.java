package javafx.scene.control;

import com.sun.javafx.scene.control.skin.resources.ControlResources;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.jar.Pack200;
import javafx.beans.InvalidationListener;
import javafx.beans.NamedArg;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

/* loaded from: jfxrt.jar:javafx/scene/control/Alert.class */
public class Alert extends Dialog<ButtonType> {
    private WeakReference<DialogPane> dialogPaneRef;
    private boolean installingDefaults;
    private boolean hasCustomButtons;
    private boolean hasCustomTitle;
    private boolean hasCustomHeaderText;
    private final InvalidationListener headerTextListener;
    private final InvalidationListener titleListener;
    private final ListChangeListener<ButtonType> buttonsListener;
    private final ObjectProperty<AlertType> alertType;

    /* loaded from: jfxrt.jar:javafx/scene/control/Alert$AlertType.class */
    public enum AlertType {
        NONE,
        INFORMATION,
        WARNING,
        CONFIRMATION,
        ERROR
    }

    public Alert(@NamedArg("alertType") AlertType alertType) {
        this(alertType, "", new ButtonType[0]);
    }

    public Alert(@NamedArg("alertType") AlertType alertType, @NamedArg("contentText") String contentText, ButtonType... buttons) {
        this.installingDefaults = false;
        this.hasCustomButtons = false;
        this.hasCustomTitle = false;
        this.hasCustomHeaderText = false;
        this.headerTextListener = o2 -> {
            if (!this.installingDefaults) {
                this.hasCustomHeaderText = true;
            }
        };
        this.titleListener = o3 -> {
            if (!this.installingDefaults) {
                this.hasCustomTitle = true;
            }
        };
        this.buttonsListener = change -> {
            if (!this.installingDefaults) {
                this.hasCustomButtons = true;
            }
        };
        this.alertType = new SimpleObjectProperty<AlertType>(null) { // from class: javafx.scene.control.Alert.1
            final String[] styleClasses = {"information", "warning", Pack200.Packer.ERROR, "confirmation"};

            @Override // javafx.beans.property.ObjectPropertyBase
            protected void invalidated() {
                String newTitle = "";
                String newHeader = "";
                String styleClass = "";
                ButtonType[] newButtons = {ButtonType.OK};
                switch (AnonymousClass2.$SwitchMap$javafx$scene$control$Alert$AlertType[Alert.this.getAlertType().ordinal()]) {
                    case 1:
                        newButtons = new ButtonType[0];
                        break;
                    case 2:
                        newTitle = ControlResources.getString("Dialog.info.title");
                        newHeader = ControlResources.getString("Dialog.info.header");
                        styleClass = "information";
                        break;
                    case 3:
                        newTitle = ControlResources.getString("Dialog.warning.title");
                        newHeader = ControlResources.getString("Dialog.warning.header");
                        styleClass = "warning";
                        break;
                    case 4:
                        newTitle = ControlResources.getString("Dialog.error.title");
                        newHeader = ControlResources.getString("Dialog.error.header");
                        styleClass = Pack200.Packer.ERROR;
                        break;
                    case 5:
                        newTitle = ControlResources.getString("Dialog.confirm.title");
                        newHeader = ControlResources.getString("Dialog.confirm.header");
                        styleClass = "confirmation";
                        newButtons = new ButtonType[]{ButtonType.OK, ButtonType.CANCEL};
                        break;
                }
                Alert.this.installingDefaults = true;
                if (!Alert.this.hasCustomTitle) {
                    Alert.this.setTitle(newTitle);
                }
                if (!Alert.this.hasCustomHeaderText) {
                    Alert.this.setHeaderText(newHeader);
                }
                if (!Alert.this.hasCustomButtons) {
                    Alert.this.getButtonTypes().setAll(newButtons);
                }
                DialogPane dialogPane = Alert.this.getDialogPane();
                if (dialogPane != null) {
                    List<String> toRemove = new ArrayList<>(Arrays.asList(this.styleClasses));
                    toRemove.remove(styleClass);
                    dialogPane.getStyleClass().removeAll(toRemove);
                    if (!dialogPane.getStyleClass().contains(styleClass)) {
                        dialogPane.getStyleClass().add(styleClass);
                    }
                }
                Alert.this.installingDefaults = false;
            }
        };
        DialogPane dialogPane = getDialogPane();
        dialogPane.setContentText(contentText);
        getDialogPane().getStyleClass().add("alert");
        this.dialogPaneRef = new WeakReference<>(dialogPane);
        this.hasCustomButtons = buttons != null && buttons.length > 0;
        if (this.hasCustomButtons) {
            for (ButtonType btnType : buttons) {
                dialogPane.getButtonTypes().addAll(btnType);
            }
        }
        setAlertType(alertType);
        dialogPaneProperty().addListener(o4 -> {
            updateListeners();
        });
        titleProperty().addListener(this.titleListener);
        updateListeners();
    }

    /* renamed from: javafx.scene.control.Alert$2, reason: invalid class name */
    /* loaded from: jfxrt.jar:javafx/scene/control/Alert$2.class */
    static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$javafx$scene$control$Alert$AlertType = new int[AlertType.values().length];

        static {
            try {
                $SwitchMap$javafx$scene$control$Alert$AlertType[AlertType.NONE.ordinal()] = 1;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$javafx$scene$control$Alert$AlertType[AlertType.INFORMATION.ordinal()] = 2;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$javafx$scene$control$Alert$AlertType[AlertType.WARNING.ordinal()] = 3;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$javafx$scene$control$Alert$AlertType[AlertType.ERROR.ordinal()] = 4;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$javafx$scene$control$Alert$AlertType[AlertType.CONFIRMATION.ordinal()] = 5;
            } catch (NoSuchFieldError e6) {
            }
        }
    }

    public final AlertType getAlertType() {
        return this.alertType.get();
    }

    public final void setAlertType(AlertType alertType) {
        this.alertType.setValue(alertType);
    }

    public final ObjectProperty<AlertType> alertTypeProperty() {
        return this.alertType;
    }

    public final ObservableList<ButtonType> getButtonTypes() {
        return getDialogPane().getButtonTypes();
    }

    private void updateListeners() {
        DialogPane oldPane = this.dialogPaneRef.get();
        if (oldPane != null) {
            oldPane.headerTextProperty().removeListener(this.headerTextListener);
            oldPane.getButtonTypes().removeListener(this.buttonsListener);
        }
        DialogPane newPane = getDialogPane();
        if (newPane != null) {
            newPane.headerTextProperty().addListener(this.headerTextListener);
            newPane.getButtonTypes().addListener(this.buttonsListener);
        }
        this.dialogPaneRef = new WeakReference<>(newPane);
    }
}
