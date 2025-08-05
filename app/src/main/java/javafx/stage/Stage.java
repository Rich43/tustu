package javafx.stage;

import com.sun.javafx.collections.TrackableObservableList;
import com.sun.javafx.robot.impl.FXRobotHelper;
import com.sun.javafx.scene.SceneHelper;
import com.sun.javafx.stage.StageHelper;
import com.sun.javafx.stage.StagePeerListener;
import com.sun.javafx.tk.TKStage;
import com.sun.javafx.tk.Toolkit;
import java.security.AllPermission;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.StringPropertyBase;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.NodeOrientation;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;

/* loaded from: jfxrt.jar:javafx/stage/Stage.class */
public class Stage extends Window {
    private boolean inNestedEventLoop;
    private static ObservableList<Stage> stages;
    private static final StagePeerListener.StageAccessor STAGE_ACCESSOR;
    private boolean primary;
    private boolean securityDialog;
    private boolean important;
    private StageStyle style;
    private Modality modality;
    private Window owner;
    private ReadOnlyBooleanWrapper fullScreen;
    private ObservableList<Image> icons;
    private StringProperty title;
    private ReadOnlyBooleanWrapper iconified;
    private ReadOnlyBooleanWrapper maximized;
    private ReadOnlyBooleanWrapper alwaysOnTop;
    private BooleanProperty resizable;
    private DoubleProperty minWidth;
    private DoubleProperty minHeight;
    private DoubleProperty maxWidth;
    private DoubleProperty maxHeight;
    private final ObjectProperty<KeyCombination> fullScreenExitCombination;
    private final ObjectProperty<String> fullScreenExitHint;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !Stage.class.desiredAssertionStatus();
        stages = FXCollections.observableArrayList();
        FXRobotHelper.setStageAccessor(new FXRobotHelper.FXRobotStageAccessor() { // from class: javafx.stage.Stage.1
            @Override // com.sun.javafx.robot.impl.FXRobotHelper.FXRobotStageAccessor
            public ObservableList<Stage> getStages() {
                return Stage.stages;
            }
        });
        StageHelper.setStageAccessor(new StageHelper.StageAccessor() { // from class: javafx.stage.Stage.2
            @Override // com.sun.javafx.stage.StageHelper.StageAccessor
            public ObservableList<Stage> getStages() {
                return Stage.stages;
            }

            @Override // com.sun.javafx.stage.StageHelper.StageAccessor
            public void initSecurityDialog(Stage stage, boolean securityDialog) {
                stage.initSecurityDialog(securityDialog);
            }
        });
        STAGE_ACCESSOR = new StagePeerListener.StageAccessor() { // from class: javafx.stage.Stage.3
            @Override // com.sun.javafx.stage.StagePeerListener.StageAccessor
            public void setIconified(Stage stage, boolean iconified) {
                stage.iconifiedPropertyImpl().set(iconified);
            }

            @Override // com.sun.javafx.stage.StagePeerListener.StageAccessor
            public void setMaximized(Stage stage, boolean maximized) {
                stage.maximizedPropertyImpl().set(maximized);
            }

            @Override // com.sun.javafx.stage.StagePeerListener.StageAccessor
            public void setResizable(Stage stage, boolean resizable) {
                ((ResizableProperty) stage.resizableProperty()).setNoInvalidate(resizable);
            }

            @Override // com.sun.javafx.stage.StagePeerListener.StageAccessor
            public void setFullScreen(Stage stage, boolean fs) {
                stage.fullScreenPropertyImpl().set(fs);
            }

            @Override // com.sun.javafx.stage.StagePeerListener.StageAccessor
            public void setAlwaysOnTop(Stage stage, boolean aot) {
                stage.alwaysOnTopPropertyImpl().set(aot);
            }
        };
    }

    public Stage() {
        this(StageStyle.DECORATED);
    }

    public Stage(StageStyle style) {
        this.inNestedEventLoop = false;
        this.primary = false;
        this.securityDialog = false;
        this.important = true;
        this.modality = Modality.NONE;
        this.owner = null;
        this.icons = new TrackableObservableList<Image>() { // from class: javafx.stage.Stage.4
            @Override // com.sun.javafx.collections.TrackableObservableList
            protected void onChanged(ListChangeListener.Change<Image> c2) {
                List<Object> platformImages = new ArrayList<>();
                for (Image icon : Stage.this.icons) {
                    platformImages.add(icon.impl_getPlatformImage());
                }
                if (Stage.this.impl_peer != null) {
                    Stage.this.impl_peer.setIcons(platformImages);
                }
            }
        };
        this.fullScreenExitCombination = new SimpleObjectProperty(this, "fullScreenExitCombination", null);
        this.fullScreenExitHint = new SimpleObjectProperty(this, "fullScreenExitHint", null);
        Toolkit.getToolkit().checkFxUserThread();
        initStyle(style);
    }

    @Override // javafx.stage.Window
    public final void setScene(Scene value) {
        Toolkit.getToolkit().checkFxUserThread();
        super.setScene(value);
    }

    @Override // javafx.stage.Window
    public final void show() {
        super.show();
    }

    final void initSecurityDialog(boolean securityDialog) {
        if (this.hasBeenVisible) {
            throw new IllegalStateException("Cannot set securityDialog once stage has been set visible");
        }
        this.securityDialog = securityDialog;
    }

    final boolean isSecurityDialog() {
        return this.securityDialog;
    }

    @Deprecated
    public void impl_setPrimary(boolean primary) {
        this.primary = primary;
    }

    boolean isPrimary() {
        return this.primary;
    }

    @Override // javafx.stage.Window
    @Deprecated
    public String impl_getMXWindowType() {
        return this.primary ? "PrimaryStage" : getClass().getSimpleName();
    }

    @Deprecated
    public void impl_setImportant(boolean important) {
        this.important = important;
    }

    private boolean isImportant() {
        return this.important;
    }

    public void showAndWait() {
        Toolkit.getToolkit().checkFxUserThread();
        if (isPrimary()) {
            throw new IllegalStateException("Cannot call this method on primary stage");
        }
        if (isShowing()) {
            throw new IllegalStateException("Stage already visible");
        }
        if (!Toolkit.getToolkit().canStartNestedEventLoop()) {
            throw new IllegalStateException("showAndWait is not allowed during animation or layout processing");
        }
        if (!$assertionsDisabled && this.inNestedEventLoop) {
            throw new AssertionError();
        }
        show();
        this.inNestedEventLoop = true;
        Toolkit.getToolkit().enterNestedEventLoop(this);
    }

    public final void initStyle(StageStyle style) {
        if (this.hasBeenVisible) {
            throw new IllegalStateException("Cannot set style once stage has been set visible");
        }
        this.style = style;
    }

    public final StageStyle getStyle() {
        return this.style;
    }

    public final void initModality(Modality modality) {
        if (this.hasBeenVisible) {
            throw new IllegalStateException("Cannot set modality once stage has been set visible");
        }
        if (isPrimary()) {
            throw new IllegalStateException("Cannot set modality for the primary stage");
        }
        this.modality = modality;
    }

    public final Modality getModality() {
        return this.modality;
    }

    public final void initOwner(Window owner) {
        if (this.hasBeenVisible) {
            throw new IllegalStateException("Cannot set owner once stage has been set visible");
        }
        if (isPrimary()) {
            throw new IllegalStateException("Cannot set owner for the primary stage");
        }
        this.owner = owner;
        Scene sceneValue = getScene();
        if (sceneValue != null) {
            SceneHelper.parentEffectiveOrientationInvalidated(sceneValue);
        }
    }

    public final Window getOwner() {
        return this.owner;
    }

    public final void setFullScreen(boolean value) {
        Toolkit.getToolkit().checkFxUserThread();
        fullScreenPropertyImpl().set(value);
        if (this.impl_peer != null) {
            this.impl_peer.setFullScreen(value);
        }
    }

    public final boolean isFullScreen() {
        if (this.fullScreen == null) {
            return false;
        }
        return this.fullScreen.get();
    }

    public final ReadOnlyBooleanProperty fullScreenProperty() {
        return fullScreenPropertyImpl().getReadOnlyProperty();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ReadOnlyBooleanWrapper fullScreenPropertyImpl() {
        if (this.fullScreen == null) {
            this.fullScreen = new ReadOnlyBooleanWrapper(this, "fullScreen");
        }
        return this.fullScreen;
    }

    public final ObservableList<Image> getIcons() {
        return this.icons;
    }

    public final void setTitle(String value) {
        titleProperty().set(value);
    }

    public final String getTitle() {
        if (this.title == null) {
            return null;
        }
        return this.title.get();
    }

    public final StringProperty titleProperty() {
        if (this.title == null) {
            this.title = new StringPropertyBase() { // from class: javafx.stage.Stage.5
                @Override // javafx.beans.property.StringPropertyBase
                protected void invalidated() {
                    if (Stage.this.impl_peer != null) {
                        Stage.this.impl_peer.setTitle(get());
                    }
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Stage.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "title";
                }
            };
        }
        return this.title;
    }

    public final void setIconified(boolean value) {
        iconifiedPropertyImpl().set(value);
        if (this.impl_peer != null) {
            this.impl_peer.setIconified(value);
        }
    }

    public final boolean isIconified() {
        if (this.iconified == null) {
            return false;
        }
        return this.iconified.get();
    }

    public final ReadOnlyBooleanProperty iconifiedProperty() {
        return iconifiedPropertyImpl().getReadOnlyProperty();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final ReadOnlyBooleanWrapper iconifiedPropertyImpl() {
        if (this.iconified == null) {
            this.iconified = new ReadOnlyBooleanWrapper(this, "iconified");
        }
        return this.iconified;
    }

    public final void setMaximized(boolean value) {
        maximizedPropertyImpl().set(value);
        if (this.impl_peer != null) {
            this.impl_peer.setMaximized(value);
        }
    }

    public final boolean isMaximized() {
        if (this.maximized == null) {
            return false;
        }
        return this.maximized.get();
    }

    public final ReadOnlyBooleanProperty maximizedProperty() {
        return maximizedPropertyImpl().getReadOnlyProperty();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final ReadOnlyBooleanWrapper maximizedPropertyImpl() {
        if (this.maximized == null) {
            this.maximized = new ReadOnlyBooleanWrapper(this, "maximized");
        }
        return this.maximized;
    }

    public final void setAlwaysOnTop(boolean value) {
        alwaysOnTopPropertyImpl().set(value);
        if (this.impl_peer != null) {
            this.impl_peer.setAlwaysOnTop(value);
        }
    }

    public final boolean isAlwaysOnTop() {
        if (this.alwaysOnTop == null) {
            return false;
        }
        return this.alwaysOnTop.get();
    }

    public final ReadOnlyBooleanProperty alwaysOnTopProperty() {
        return alwaysOnTopPropertyImpl().getReadOnlyProperty();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ReadOnlyBooleanWrapper alwaysOnTopPropertyImpl() {
        if (this.alwaysOnTop == null) {
            this.alwaysOnTop = new ReadOnlyBooleanWrapper(this, "alwaysOnTop");
        }
        return this.alwaysOnTop;
    }

    public final void setResizable(boolean value) {
        resizableProperty().set(value);
    }

    public final boolean isResizable() {
        if (this.resizable == null) {
            return true;
        }
        return this.resizable.get();
    }

    public final BooleanProperty resizableProperty() {
        if (this.resizable == null) {
            this.resizable = new ResizableProperty();
        }
        return this.resizable;
    }

    /* loaded from: jfxrt.jar:javafx/stage/Stage$ResizableProperty.class */
    private class ResizableProperty extends SimpleBooleanProperty {
        private boolean noInvalidate;

        public ResizableProperty() {
            super(Stage.this, "resizable", true);
        }

        void setNoInvalidate(boolean value) {
            this.noInvalidate = true;
            set(value);
            this.noInvalidate = false;
        }

        @Override // javafx.beans.property.BooleanPropertyBase
        protected void invalidated() {
            if (!this.noInvalidate && Stage.this.impl_peer != null) {
                Stage.this.applyBounds();
                Stage.this.impl_peer.setResizable(get());
            }
        }

        @Override // javafx.beans.property.BooleanPropertyBase, javafx.beans.property.Property
        public void bind(ObservableValue<? extends Boolean> rawObservable) {
            throw new RuntimeException("Resizable property cannot be bound");
        }
    }

    public final void setMinWidth(double value) {
        minWidthProperty().set(value);
    }

    public final double getMinWidth() {
        if (this.minWidth == null) {
            return 0.0d;
        }
        return this.minWidth.get();
    }

    public final DoubleProperty minWidthProperty() {
        if (this.minWidth == null) {
            this.minWidth = new DoublePropertyBase(0.0d) { // from class: javafx.stage.Stage.6
                @Override // javafx.beans.property.DoublePropertyBase
                protected void invalidated() {
                    if (Stage.this.impl_peer != null) {
                        Stage.this.impl_peer.setMinimumSize((int) Math.ceil(get()), (int) Math.ceil(Stage.this.getMinHeight()));
                    }
                    if (Stage.this.getWidth() < Stage.this.getMinWidth()) {
                        Stage.this.setWidth(Stage.this.getMinWidth());
                    }
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Stage.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "minWidth";
                }
            };
        }
        return this.minWidth;
    }

    public final void setMinHeight(double value) {
        minHeightProperty().set(value);
    }

    public final double getMinHeight() {
        if (this.minHeight == null) {
            return 0.0d;
        }
        return this.minHeight.get();
    }

    public final DoubleProperty minHeightProperty() {
        if (this.minHeight == null) {
            this.minHeight = new DoublePropertyBase(0.0d) { // from class: javafx.stage.Stage.7
                @Override // javafx.beans.property.DoublePropertyBase
                protected void invalidated() {
                    if (Stage.this.impl_peer != null) {
                        Stage.this.impl_peer.setMinimumSize((int) Math.ceil(Stage.this.getMinWidth()), (int) Math.ceil(get()));
                    }
                    if (Stage.this.getHeight() < Stage.this.getMinHeight()) {
                        Stage.this.setHeight(Stage.this.getMinHeight());
                    }
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Stage.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "minHeight";
                }
            };
        }
        return this.minHeight;
    }

    public final void setMaxWidth(double value) {
        maxWidthProperty().set(value);
    }

    public final double getMaxWidth() {
        if (this.maxWidth == null) {
            return Double.MAX_VALUE;
        }
        return this.maxWidth.get();
    }

    public final DoubleProperty maxWidthProperty() {
        if (this.maxWidth == null) {
            this.maxWidth = new DoublePropertyBase(Double.MAX_VALUE) { // from class: javafx.stage.Stage.8
                @Override // javafx.beans.property.DoublePropertyBase
                protected void invalidated() {
                    if (Stage.this.impl_peer != null) {
                        Stage.this.impl_peer.setMaximumSize((int) Math.floor(get()), (int) Math.floor(Stage.this.getMaxHeight()));
                    }
                    if (Stage.this.getWidth() > Stage.this.getMaxWidth()) {
                        Stage.this.setWidth(Stage.this.getMaxWidth());
                    }
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Stage.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "maxWidth";
                }
            };
        }
        return this.maxWidth;
    }

    public final void setMaxHeight(double value) {
        maxHeightProperty().set(value);
    }

    public final double getMaxHeight() {
        if (this.maxHeight == null) {
            return Double.MAX_VALUE;
        }
        return this.maxHeight.get();
    }

    public final DoubleProperty maxHeightProperty() {
        if (this.maxHeight == null) {
            this.maxHeight = new DoublePropertyBase(Double.MAX_VALUE) { // from class: javafx.stage.Stage.9
                @Override // javafx.beans.property.DoublePropertyBase
                protected void invalidated() {
                    if (Stage.this.impl_peer != null) {
                        Stage.this.impl_peer.setMaximumSize((int) Math.floor(Stage.this.getMaxWidth()), (int) Math.floor(get()));
                    }
                    if (Stage.this.getHeight() > Stage.this.getMaxHeight()) {
                        Stage.this.setHeight(Stage.this.getMaxHeight());
                    }
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Stage.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "maxHeight";
                }
            };
        }
        return this.maxHeight;
    }

    @Override // javafx.stage.Window
    @Deprecated
    protected void impl_visibleChanging(boolean value) {
        SecurityManager securityManager;
        super.impl_visibleChanging(value);
        Toolkit toolkit = Toolkit.getToolkit();
        if (value && this.impl_peer == null) {
            Window window = getOwner();
            TKStage tkStage = window == null ? null : window.impl_getPeer();
            Scene scene = getScene();
            boolean rtl = scene != null && scene.getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT;
            StageStyle stageStyle = getStyle();
            if (stageStyle == StageStyle.TRANSPARENT && (securityManager = System.getSecurityManager()) != null) {
                try {
                    securityManager.checkPermission(new AllPermission());
                } catch (SecurityException e2) {
                    stageStyle = StageStyle.UNDECORATED;
                }
            }
            this.impl_peer = toolkit.createTKStage(this, isSecurityDialog(), stageStyle, isPrimary(), getModality(), tkStage, rtl, this.acc);
            this.impl_peer.setMinimumSize((int) Math.ceil(getMinWidth()), (int) Math.ceil(getMinHeight()));
            this.impl_peer.setMaximumSize((int) Math.floor(getMaxWidth()), (int) Math.floor(getMaxHeight()));
            this.peerListener = new StagePeerListener(this, STAGE_ACCESSOR);
            stages.add(this);
        }
    }

    @Override // javafx.stage.Window
    @Deprecated
    protected void impl_visibleChanged(boolean value) {
        super.impl_visibleChanged(value);
        if (value) {
            this.impl_peer.setImportant(isImportant());
            this.impl_peer.setResizable(isResizable());
            this.impl_peer.setFullScreen(isFullScreen());
            this.impl_peer.setAlwaysOnTop(isAlwaysOnTop());
            this.impl_peer.setIconified(isIconified());
            this.impl_peer.setMaximized(isMaximized());
            this.impl_peer.setTitle(getTitle());
            List<Object> platformImages = new ArrayList<>();
            for (Image icon : this.icons) {
                platformImages.add(icon.impl_getPlatformImage());
            }
            if (this.impl_peer != null) {
                this.impl_peer.setIcons(platformImages);
            }
        }
        if (!value) {
            stages.remove(this);
        }
        if (!value && this.inNestedEventLoop) {
            this.inNestedEventLoop = false;
            Toolkit.getToolkit().exitNestedEventLoop(this, null);
        }
    }

    public void toFront() {
        if (this.impl_peer != null) {
            this.impl_peer.toFront();
        }
    }

    public void toBack() {
        if (this.impl_peer != null) {
            this.impl_peer.toBack();
        }
    }

    public void close() {
        hide();
    }

    @Override // javafx.stage.Window
    Window getWindowOwner() {
        return getOwner();
    }

    public final void setFullScreenExitKeyCombination(KeyCombination keyCombination) {
        this.fullScreenExitCombination.set(keyCombination);
    }

    public final KeyCombination getFullScreenExitKeyCombination() {
        return this.fullScreenExitCombination.get();
    }

    public final ObjectProperty<KeyCombination> fullScreenExitKeyProperty() {
        return this.fullScreenExitCombination;
    }

    public final void setFullScreenExitHint(String value) {
        this.fullScreenExitHint.set(value);
    }

    public final String getFullScreenExitHint() {
        return this.fullScreenExitHint.get();
    }

    public final ObjectProperty<String> fullScreenExitHintProperty() {
        return this.fullScreenExitHint;
    }
}
