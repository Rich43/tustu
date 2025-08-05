package javafx.scene.control;

import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.stage.Window;

/* loaded from: jfxrt.jar:javafx/scene/control/FXDialog.class */
abstract class FXDialog {
    protected Object owner;

    public abstract void show();

    public abstract void showAndWait();

    public abstract void close();

    public abstract void initOwner(Window window);

    public abstract Window getOwner();

    public abstract void initModality(Modality modality);

    public abstract Modality getModality();

    public abstract ReadOnlyBooleanProperty showingProperty();

    public abstract Window getWindow();

    public abstract void sizeToScene();

    public abstract double getX();

    public abstract void setX(double d2);

    public abstract ReadOnlyDoubleProperty xProperty();

    public abstract double getY();

    public abstract void setY(double d2);

    public abstract ReadOnlyDoubleProperty yProperty();

    abstract BooleanProperty resizableProperty();

    abstract ReadOnlyBooleanProperty focusedProperty();

    abstract StringProperty titleProperty();

    public abstract void setDialogPane(DialogPane dialogPane);

    public abstract Node getRoot();

    abstract ReadOnlyDoubleProperty widthProperty();

    abstract void setWidth(double d2);

    abstract ReadOnlyDoubleProperty heightProperty();

    abstract void setHeight(double d2);

    abstract void initStyle(StageStyle stageStyle);

    abstract StageStyle getStyle();

    abstract double getSceneHeight();

    protected FXDialog() {
    }

    public boolean requestPermissionToClose(Dialog<?> dialog) {
        ButtonBar.ButtonData type;
        boolean denyClose = true;
        DialogPane dialogPane = dialog.getDialogPane();
        if (dialogPane != null) {
            List<ButtonType> buttons = dialogPane.getButtonTypes();
            if (buttons.size() == 1) {
                denyClose = false;
            } else {
                for (ButtonType button : buttons) {
                    if (button != null && (type = button.getButtonData()) != null && (type == ButtonBar.ButtonData.CANCEL_CLOSE || type.isCancelButton())) {
                        denyClose = false;
                        break;
                    }
                }
            }
        }
        return !denyClose;
    }
}
