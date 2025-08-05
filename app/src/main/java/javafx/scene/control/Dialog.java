package javafx.scene.control;

import com.sun.javafx.event.EventHandlerManager;
import com.sun.javafx.tk.Toolkit;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Optional;
import javafx.beans.InvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.css.PseudoClass;
import javafx.event.Event;
import javafx.event.EventDispatchChain;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.Callback;

/* loaded from: jfxrt.jar:javafx/scene/control/Dialog.class */
public class Dialog<R> implements EventTarget {
    private boolean isClosing;
    private ObjectProperty<EventHandler<DialogEvent>> onShowing;
    private ObjectProperty<EventHandler<DialogEvent>> onShown;
    private ObjectProperty<EventHandler<DialogEvent>> onHiding;
    private ObjectProperty<EventHandler<DialogEvent>> onHidden;
    private ObjectProperty<EventHandler<DialogEvent>> onCloseRequest;
    private static final PseudoClass HEADER_PSEUDO_CLASS = PseudoClass.getPseudoClass("header");
    private static final PseudoClass NO_HEADER_PSEUDO_CLASS = PseudoClass.getPseudoClass("no-header");
    private ObjectProperty<DialogPane> dialogPane = new SimpleObjectProperty<DialogPane>(this, "dialogPane", new DialogPane()) { // from class: javafx.scene.control.Dialog.1
        final InvalidationListener expandedListener = o2 -> {
            DialogPane dialogPane = Dialog.this.getDialogPane();
            if (dialogPane == null) {
                return;
            }
            Node content = dialogPane.getExpandableContent();
            boolean isExpanded = content == null ? false : content.isVisible();
            Dialog.this.setResizable(isExpanded);
            Dialog.this.dialog.sizeToScene();
        };
        final InvalidationListener headerListener = o2 -> {
            Dialog.this.updatePseudoClassState();
        };
        WeakReference<DialogPane> dialogPaneRef = new WeakReference<>(null);

        @Override // javafx.beans.property.ObjectPropertyBase
        protected void invalidated() {
            DialogPane oldDialogPane = this.dialogPaneRef.get();
            if (oldDialogPane != null) {
                oldDialogPane.expandedProperty().removeListener(this.expandedListener);
                oldDialogPane.headerProperty().removeListener(this.headerListener);
                oldDialogPane.headerTextProperty().removeListener(this.headerListener);
                oldDialogPane.setDialog(null);
            }
            DialogPane newDialogPane = Dialog.this.getDialogPane();
            if (newDialogPane != null) {
                newDialogPane.setDialog(Dialog.this);
                newDialogPane.getButtonTypes().addListener(c2 -> {
                    newDialogPane.requestLayout();
                });
                newDialogPane.expandedProperty().addListener(this.expandedListener);
                newDialogPane.headerProperty().addListener(this.headerListener);
                newDialogPane.headerTextProperty().addListener(this.headerListener);
                Dialog.this.updatePseudoClassState();
                newDialogPane.requestLayout();
            }
            Dialog.this.dialog.setDialogPane(newDialogPane);
            this.dialogPaneRef = new WeakReference<>(newDialogPane);
        }
    };
    private final ObjectProperty<R> resultProperty = new SimpleObjectProperty<R>() { // from class: javafx.scene.control.Dialog.2
        @Override // javafx.beans.property.ObjectPropertyBase
        protected void invalidated() {
            Dialog.this.close();
        }
    };
    private final ObjectProperty<Callback<ButtonType, R>> resultConverterProperty = new SimpleObjectProperty(this, "resultConverter");
    private final EventHandlerManager eventHandlerManager = new EventHandlerManager(this);
    final FXDialog dialog = new HeavyweightDialog(this);

    public Dialog() {
        setDialogPane(new DialogPane());
        initModality(Modality.APPLICATION_MODAL);
    }

    public final void show() {
        Toolkit.getToolkit().checkFxUserThread();
        Event.fireEvent(this, new DialogEvent(this, DialogEvent.DIALOG_SHOWING));
        if (getWidth() == Double.NaN && getHeight() == Double.NaN) {
            this.dialog.sizeToScene();
        }
        this.dialog.show();
        Event.fireEvent(this, new DialogEvent(this, DialogEvent.DIALOG_SHOWN));
    }

    public final Optional<R> showAndWait() {
        Toolkit.getToolkit().checkFxUserThread();
        if (!Toolkit.getToolkit().canStartNestedEventLoop()) {
            throw new IllegalStateException("showAndWait is not allowed during animation or layout processing");
        }
        Event.fireEvent(this, new DialogEvent(this, DialogEvent.DIALOG_SHOWING));
        if (getWidth() == Double.NaN && getHeight() == Double.NaN) {
            this.dialog.sizeToScene();
        }
        Event.fireEvent(this, new DialogEvent(this, DialogEvent.DIALOG_SHOWN));
        this.dialog.showAndWait();
        return Optional.ofNullable(getResult());
    }

    public final void close() {
        if (this.isClosing) {
            return;
        }
        this.isClosing = true;
        R result = getResult();
        if (result == null && !this.dialog.requestPermissionToClose(this)) {
            this.isClosing = false;
            return;
        }
        if (result == null) {
            ButtonType cancelButton = null;
            Iterator<ButtonType> it = getDialogPane().getButtonTypes().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                ButtonType button = it.next();
                ButtonBar.ButtonData buttonData = button.getButtonData();
                if (buttonData != null) {
                    if (buttonData == ButtonBar.ButtonData.CANCEL_CLOSE) {
                        cancelButton = button;
                        break;
                    } else if (buttonData.isCancelButton()) {
                        cancelButton = button;
                    }
                }
            }
            impl_setResultAndClose(cancelButton, false);
        }
        Event.fireEvent(this, new DialogEvent(this, DialogEvent.DIALOG_HIDING));
        DialogEvent closeRequestEvent = new DialogEvent(this, DialogEvent.DIALOG_CLOSE_REQUEST);
        Event.fireEvent(this, closeRequestEvent);
        if (closeRequestEvent.isConsumed()) {
            this.isClosing = false;
            return;
        }
        this.dialog.close();
        Event.fireEvent(this, new DialogEvent(this, DialogEvent.DIALOG_HIDDEN));
        this.isClosing = false;
    }

    public final void hide() {
        close();
    }

    public final void initModality(Modality modality) {
        this.dialog.initModality(modality);
    }

    public final Modality getModality() {
        return this.dialog.getModality();
    }

    public final void initStyle(StageStyle style) {
        this.dialog.initStyle(style);
    }

    public final void initOwner(Window window) {
        this.dialog.initOwner(window);
    }

    public final Window getOwner() {
        return this.dialog.getOwner();
    }

    public final ObjectProperty<DialogPane> dialogPaneProperty() {
        return this.dialogPane;
    }

    public final DialogPane getDialogPane() {
        return this.dialogPane.get();
    }

    public final void setDialogPane(DialogPane value) {
        this.dialogPane.set(value);
    }

    public final StringProperty contentTextProperty() {
        return getDialogPane().contentTextProperty();
    }

    public final String getContentText() {
        return getDialogPane().getContentText();
    }

    public final void setContentText(String contentText) {
        getDialogPane().setContentText(contentText);
    }

    public final StringProperty headerTextProperty() {
        return getDialogPane().headerTextProperty();
    }

    public final String getHeaderText() {
        return getDialogPane().getHeaderText();
    }

    public final void setHeaderText(String headerText) {
        getDialogPane().setHeaderText(headerText);
    }

    public final ObjectProperty<Node> graphicProperty() {
        return getDialogPane().graphicProperty();
    }

    public final Node getGraphic() {
        return getDialogPane().getGraphic();
    }

    public final void setGraphic(Node graphic) {
        getDialogPane().setGraphic(graphic);
    }

    public final ObjectProperty<R> resultProperty() {
        return this.resultProperty;
    }

    public final R getResult() {
        return resultProperty().get();
    }

    public final void setResult(R value) {
        resultProperty().set(value);
    }

    public final ObjectProperty<Callback<ButtonType, R>> resultConverterProperty() {
        return this.resultConverterProperty;
    }

    public final Callback<ButtonType, R> getResultConverter() {
        return resultConverterProperty().get();
    }

    public final void setResultConverter(Callback<ButtonType, R> value) {
        resultConverterProperty().set(value);
    }

    public final ReadOnlyBooleanProperty showingProperty() {
        return this.dialog.showingProperty();
    }

    public final boolean isShowing() {
        return showingProperty().get();
    }

    public final BooleanProperty resizableProperty() {
        return this.dialog.resizableProperty();
    }

    public final boolean isResizable() {
        return resizableProperty().get();
    }

    public final void setResizable(boolean resizable) {
        resizableProperty().set(resizable);
    }

    public final ReadOnlyDoubleProperty widthProperty() {
        return this.dialog.widthProperty();
    }

    public final double getWidth() {
        return widthProperty().get();
    }

    public final void setWidth(double width) {
        this.dialog.setWidth(width);
    }

    public final ReadOnlyDoubleProperty heightProperty() {
        return this.dialog.heightProperty();
    }

    public final double getHeight() {
        return heightProperty().get();
    }

    public final void setHeight(double height) {
        this.dialog.setHeight(height);
    }

    public final StringProperty titleProperty() {
        return this.dialog.titleProperty();
    }

    public final String getTitle() {
        return this.dialog.titleProperty().get();
    }

    public final void setTitle(String title) {
        this.dialog.titleProperty().set(title);
    }

    public final double getX() {
        return this.dialog.getX();
    }

    public final void setX(double x2) {
        this.dialog.setX(x2);
    }

    public final ReadOnlyDoubleProperty xProperty() {
        return this.dialog.xProperty();
    }

    public final double getY() {
        return this.dialog.getY();
    }

    public final void setY(double y2) {
        this.dialog.setY(y2);
    }

    public final ReadOnlyDoubleProperty yProperty() {
        return this.dialog.yProperty();
    }

    @Override // javafx.event.EventTarget
    public EventDispatchChain buildEventDispatchChain(EventDispatchChain tail) {
        return tail.prepend(this.eventHandlerManager);
    }

    public final void setOnShowing(EventHandler<DialogEvent> value) {
        onShowingProperty().set(value);
    }

    public final EventHandler<DialogEvent> getOnShowing() {
        if (this.onShowing == null) {
            return null;
        }
        return this.onShowing.get();
    }

    public final ObjectProperty<EventHandler<DialogEvent>> onShowingProperty() {
        if (this.onShowing == null) {
            this.onShowing = new SimpleObjectProperty<EventHandler<DialogEvent>>(this, "onShowing") { // from class: javafx.scene.control.Dialog.3
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    Dialog.this.eventHandlerManager.setEventHandler(DialogEvent.DIALOG_SHOWING, get());
                }
            };
        }
        return this.onShowing;
    }

    public final void setOnShown(EventHandler<DialogEvent> value) {
        onShownProperty().set(value);
    }

    public final EventHandler<DialogEvent> getOnShown() {
        if (this.onShown == null) {
            return null;
        }
        return this.onShown.get();
    }

    public final ObjectProperty<EventHandler<DialogEvent>> onShownProperty() {
        if (this.onShown == null) {
            this.onShown = new SimpleObjectProperty<EventHandler<DialogEvent>>(this, "onShown") { // from class: javafx.scene.control.Dialog.4
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    Dialog.this.eventHandlerManager.setEventHandler(DialogEvent.DIALOG_SHOWN, get());
                }
            };
        }
        return this.onShown;
    }

    public final void setOnHiding(EventHandler<DialogEvent> value) {
        onHidingProperty().set(value);
    }

    public final EventHandler<DialogEvent> getOnHiding() {
        if (this.onHiding == null) {
            return null;
        }
        return this.onHiding.get();
    }

    public final ObjectProperty<EventHandler<DialogEvent>> onHidingProperty() {
        if (this.onHiding == null) {
            this.onHiding = new SimpleObjectProperty<EventHandler<DialogEvent>>(this, "onHiding") { // from class: javafx.scene.control.Dialog.5
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    Dialog.this.eventHandlerManager.setEventHandler(DialogEvent.DIALOG_HIDING, get());
                }
            };
        }
        return this.onHiding;
    }

    public final void setOnHidden(EventHandler<DialogEvent> value) {
        onHiddenProperty().set(value);
    }

    public final EventHandler<DialogEvent> getOnHidden() {
        if (this.onHidden == null) {
            return null;
        }
        return this.onHidden.get();
    }

    public final ObjectProperty<EventHandler<DialogEvent>> onHiddenProperty() {
        if (this.onHidden == null) {
            this.onHidden = new SimpleObjectProperty<EventHandler<DialogEvent>>(this, "onHidden") { // from class: javafx.scene.control.Dialog.6
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    Dialog.this.eventHandlerManager.setEventHandler(DialogEvent.DIALOG_HIDDEN, get());
                }
            };
        }
        return this.onHidden;
    }

    public final void setOnCloseRequest(EventHandler<DialogEvent> value) {
        onCloseRequestProperty().set(value);
    }

    public final EventHandler<DialogEvent> getOnCloseRequest() {
        if (this.onCloseRequest != null) {
            return this.onCloseRequest.get();
        }
        return null;
    }

    public final ObjectProperty<EventHandler<DialogEvent>> onCloseRequestProperty() {
        if (this.onCloseRequest == null) {
            this.onCloseRequest = new SimpleObjectProperty<EventHandler<DialogEvent>>(this, "onCloseRequest") { // from class: javafx.scene.control.Dialog.7
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    Dialog.this.eventHandlerManager.setEventHandler(DialogEvent.DIALOG_CLOSE_REQUEST, get());
                }
            };
        }
        return this.onCloseRequest;
    }

    /* JADX WARN: Multi-variable type inference failed */
    void impl_setResultAndClose(ButtonType buttonType, boolean close) {
        R newResultValue;
        Callback<ButtonType, R> resultConverter = getResultConverter();
        R priorResultValue = getResult();
        if (resultConverter == null) {
            newResultValue = buttonType;
        } else {
            newResultValue = resultConverter.call(buttonType);
        }
        setResult(newResultValue);
        if (close && priorResultValue == newResultValue) {
            close();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updatePseudoClassState() {
        DialogPane dialogPane = getDialogPane();
        if (dialogPane != null) {
            boolean hasHeader = getDialogPane().hasHeader();
            dialogPane.pseudoClassStateChanged(HEADER_PSEUDO_CLASS, hasHeader);
            dialogPane.pseudoClassStateChanged(NO_HEADER_PSEUDO_CLASS, !hasHeader);
        }
    }
}
