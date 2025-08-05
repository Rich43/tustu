package javafx.scene.control;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

/* loaded from: jfxrt.jar:javafx/scene/control/HeavyweightDialog.class */
class HeavyweightDialog extends FXDialog {
    private Scene scene;
    private final Dialog<?> dialog;
    private DialogPane dialogPane;
    final Stage stage = new Stage() { // from class: javafx.scene.control.HeavyweightDialog.1
        @Override // javafx.stage.Window
        public void centerOnScreen() {
            Window owner = getOwner();
            if (owner != null) {
                HeavyweightDialog.this.positionStage();
            } else if (getWidth() > 0.0d && getHeight() > 0.0d) {
                super.centerOnScreen();
            }
        }
    };
    private final Parent DUMMY_ROOT = new Region();
    private double prefX = Double.NaN;
    private double prefY = Double.NaN;

    HeavyweightDialog(Dialog<?> dialog) {
        this.dialog = dialog;
        this.stage.setResizable(false);
        this.stage.setOnCloseRequest(windowEvent -> {
            if (requestPermissionToClose(dialog)) {
                dialog.close();
            } else {
                windowEvent.consume();
            }
        });
        this.stage.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ESCAPE && !keyEvent.isConsumed() && requestPermissionToClose(dialog)) {
                dialog.close();
                keyEvent.consume();
            }
        });
    }

    @Override // javafx.scene.control.FXDialog
    void initStyle(StageStyle style) {
        this.stage.initStyle(style);
    }

    @Override // javafx.scene.control.FXDialog
    StageStyle getStyle() {
        return this.stage.getStyle();
    }

    @Override // javafx.scene.control.FXDialog
    public void initOwner(Window newOwner) {
        updateStageBindings(this.stage.getOwner(), newOwner);
        this.stage.initOwner(newOwner);
    }

    @Override // javafx.scene.control.FXDialog
    public Window getOwner() {
        return this.stage.getOwner();
    }

    @Override // javafx.scene.control.FXDialog
    public void initModality(Modality modality) {
        this.stage.initModality(modality == null ? Modality.APPLICATION_MODAL : modality);
    }

    @Override // javafx.scene.control.FXDialog
    public Modality getModality() {
        return this.stage.getModality();
    }

    @Override // javafx.scene.control.FXDialog
    public void setDialogPane(DialogPane dialogPane) {
        this.dialogPane = dialogPane;
        if (this.scene == null) {
            this.scene = new Scene(dialogPane);
            this.stage.setScene(this.scene);
        } else {
            this.scene.setRoot(dialogPane);
        }
        dialogPane.autosize();
        this.stage.sizeToScene();
    }

    @Override // javafx.scene.control.FXDialog
    public void show() {
        this.scene.setRoot(this.dialogPane);
        this.stage.centerOnScreen();
        this.stage.show();
    }

    @Override // javafx.scene.control.FXDialog
    public void showAndWait() {
        this.scene.setRoot(this.dialogPane);
        this.stage.centerOnScreen();
        this.stage.showAndWait();
    }

    @Override // javafx.scene.control.FXDialog
    public void close() {
        if (this.stage.isShowing()) {
            this.stage.hide();
        }
        if (this.scene != null) {
            this.scene.setRoot(this.DUMMY_ROOT);
        }
    }

    @Override // javafx.scene.control.FXDialog
    public ReadOnlyBooleanProperty showingProperty() {
        return this.stage.showingProperty();
    }

    @Override // javafx.scene.control.FXDialog
    public Window getWindow() {
        return this.stage;
    }

    @Override // javafx.scene.control.FXDialog
    public Node getRoot() {
        return this.stage.getScene().getRoot();
    }

    @Override // javafx.scene.control.FXDialog
    public double getX() {
        return this.stage.getX();
    }

    @Override // javafx.scene.control.FXDialog
    public void setX(double x2) {
        this.stage.setX(x2);
    }

    @Override // javafx.scene.control.FXDialog
    public ReadOnlyDoubleProperty xProperty() {
        return this.stage.xProperty();
    }

    @Override // javafx.scene.control.FXDialog
    public double getY() {
        return this.stage.getY();
    }

    @Override // javafx.scene.control.FXDialog
    public void setY(double y2) {
        this.stage.setY(y2);
    }

    @Override // javafx.scene.control.FXDialog
    public ReadOnlyDoubleProperty yProperty() {
        return this.stage.yProperty();
    }

    @Override // javafx.scene.control.FXDialog
    ReadOnlyDoubleProperty heightProperty() {
        return this.stage.heightProperty();
    }

    @Override // javafx.scene.control.FXDialog
    void setHeight(double height) {
        this.stage.setHeight(height);
    }

    @Override // javafx.scene.control.FXDialog
    double getSceneHeight() {
        if (this.scene == null) {
            return 0.0d;
        }
        return this.scene.getHeight();
    }

    @Override // javafx.scene.control.FXDialog
    ReadOnlyDoubleProperty widthProperty() {
        return this.stage.widthProperty();
    }

    @Override // javafx.scene.control.FXDialog
    void setWidth(double width) {
        this.stage.setWidth(width);
    }

    @Override // javafx.scene.control.FXDialog
    BooleanProperty resizableProperty() {
        return this.stage.resizableProperty();
    }

    @Override // javafx.scene.control.FXDialog
    StringProperty titleProperty() {
        return this.stage.titleProperty();
    }

    @Override // javafx.scene.control.FXDialog
    ReadOnlyBooleanProperty focusedProperty() {
        return this.stage.focusedProperty();
    }

    @Override // javafx.scene.control.FXDialog
    public void sizeToScene() {
        this.stage.sizeToScene();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void positionStage() {
        double x2 = getX();
        double y2 = getY();
        if (!Double.isNaN(x2) && !Double.isNaN(y2) && Double.compare(x2, this.prefX) != 0 && Double.compare(y2, this.prefY) != 0) {
            setX(x2);
            setY(y2);
            return;
        }
        this.dialogPane.applyCss();
        this.dialogPane.layout();
        Window owner = getOwner();
        Scene ownerScene = owner.getScene();
        double titleBarHeight = ownerScene.getY();
        double dialogWidth = this.dialogPane.prefWidth(-1.0d);
        double dialogHeight = this.dialogPane.prefHeight(dialogWidth);
        double x3 = (owner.getX() + (ownerScene.getWidth() / 2.0d)) - (dialogWidth / 2.0d);
        double y3 = ((owner.getY() + (titleBarHeight / 2.0d)) + (ownerScene.getHeight() / 2.0d)) - (dialogHeight / 2.0d);
        this.prefX = x3;
        this.prefY = y3;
        setX(x3);
        setY(y3);
    }

    private void updateStageBindings(Window oldOwner, Window newOwner) {
        Scene dialogScene = this.stage.getScene();
        if (oldOwner != null && (oldOwner instanceof Stage)) {
            Stage oldStage = (Stage) oldOwner;
            Bindings.unbindContent(this.stage.getIcons(), oldStage.getIcons());
            Scene oldScene = oldStage.getScene();
            if (this.scene != null && dialogScene != null) {
                Bindings.unbindContent(dialogScene.getStylesheets(), oldScene.getStylesheets());
            }
        }
        if (newOwner instanceof Stage) {
            Stage newStage = (Stage) newOwner;
            Bindings.bindContent(this.stage.getIcons(), newStage.getIcons());
            Scene newScene = newStage.getScene();
            if (this.scene != null && dialogScene != null) {
                Bindings.bindContent(dialogScene.getStylesheets(), newScene.getStylesheets());
            }
        }
    }
}
