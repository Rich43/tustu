package com.sun.javafx.scene.control.skin;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableNumberValue;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Stop;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import sun.security.tools.policytool.ToolWindow;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/CustomColorDialog.class */
public class CustomColorDialog extends HBox {
    private ColorRectPane colorRectPane;
    private ControlsPane controlsPane;
    private Runnable onSave;
    private Runnable onUse;
    private Runnable onCancel;
    private Scene customScene;
    private String saveBtnText;
    private final Stage dialog = new Stage();
    private ObjectProperty<Color> currentColorProperty = new SimpleObjectProperty(Color.WHITE);
    private ObjectProperty<Color> customColorProperty = new SimpleObjectProperty(Color.TRANSPARENT);
    private WebColorField webField = null;
    private boolean showUseBtn = true;
    private boolean showOpacitySlider = true;
    private final EventHandler<KeyEvent> keyEventListener = e2 -> {
        switch (e2.getCode()) {
            case ESCAPE:
                this.dialog.setScene(null);
                this.dialog.close();
                break;
        }
    };
    private InvalidationListener positionAdjuster = new InvalidationListener() { // from class: com.sun.javafx.scene.control.skin.CustomColorDialog.1
        @Override // javafx.beans.InvalidationListener
        public void invalidated(Observable ignored) {
            if (!Double.isNaN(CustomColorDialog.this.dialog.getWidth()) && !Double.isNaN(CustomColorDialog.this.dialog.getHeight())) {
                CustomColorDialog.this.dialog.widthProperty().removeListener(CustomColorDialog.this.positionAdjuster);
                CustomColorDialog.this.dialog.heightProperty().removeListener(CustomColorDialog.this.positionAdjuster);
                CustomColorDialog.this.fixPosition();
            }
        }
    };

    public CustomColorDialog(Window owner) {
        getStyleClass().add("custom-color-dialog");
        if (owner != null) {
            this.dialog.initOwner(owner);
        }
        this.dialog.setTitle(ColorPickerSkin.getString("customColorDialogTitle"));
        this.dialog.initModality(Modality.APPLICATION_MODAL);
        this.dialog.initStyle(StageStyle.UTILITY);
        this.dialog.setResizable(false);
        this.dialog.addEventHandler(KeyEvent.ANY, this.keyEventListener);
        this.customScene = new Scene(this);
        Scene ownerScene = owner.getScene();
        if (ownerScene != null) {
            if (ownerScene.getUserAgentStylesheet() != null) {
                this.customScene.setUserAgentStylesheet(ownerScene.getUserAgentStylesheet());
            }
            this.customScene.getStylesheets().addAll(ownerScene.getStylesheets());
        }
        buildUI();
        this.dialog.setScene(this.customScene);
    }

    private void buildUI() {
        this.colorRectPane = new ColorRectPane();
        this.controlsPane = new ControlsPane();
        setHgrow(this.controlsPane, Priority.ALWAYS);
        getChildren().setAll(this.colorRectPane, this.controlsPane);
    }

    public void setCurrentColor(Color currentColor) {
        this.currentColorProperty.set(currentColor);
    }

    Color getCurrentColor() {
        return this.currentColorProperty.get();
    }

    ObjectProperty<Color> customColorProperty() {
        return this.customColorProperty;
    }

    void setCustomColor(Color color) {
        this.customColorProperty.set(color);
    }

    public Color getCustomColor() {
        return this.customColorProperty.get();
    }

    public Runnable getOnSave() {
        return this.onSave;
    }

    public void setSaveBtnToOk() {
        this.saveBtnText = ColorPickerSkin.getString("OK");
        buildUI();
    }

    public void setOnSave(Runnable onSave) {
        this.onSave = onSave;
    }

    public Runnable getOnUse() {
        return this.onUse;
    }

    public void setOnUse(Runnable onUse) {
        this.onUse = onUse;
    }

    public void setShowUseBtn(boolean showUseBtn) {
        this.showUseBtn = showUseBtn;
        buildUI();
    }

    public void setShowOpacitySlider(boolean showOpacitySlider) {
        this.showOpacitySlider = showOpacitySlider;
        buildUI();
    }

    public Runnable getOnCancel() {
        return this.onCancel;
    }

    public void setOnCancel(Runnable onCancel) {
        this.onCancel = onCancel;
    }

    public void setOnHidden(EventHandler<WindowEvent> onHidden) {
        this.dialog.setOnHidden(onHidden);
    }

    public Stage getDialog() {
        return this.dialog;
    }

    public void show() {
        if (this.dialog.getOwner() != null) {
            this.dialog.widthProperty().addListener(this.positionAdjuster);
            this.dialog.heightProperty().addListener(this.positionAdjuster);
            this.positionAdjuster.invalidated(null);
        }
        if (this.dialog.getScene() == null) {
            this.dialog.setScene(this.customScene);
        }
        this.colorRectPane.updateValues();
        this.dialog.show();
    }

    public void hide() {
        if (this.dialog.getOwner() != null) {
            this.dialog.hide();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fixPosition() {
        double x2;
        Window w2 = this.dialog.getOwner();
        Screen s2 = com.sun.javafx.util.Utils.getScreen(w2);
        Rectangle2D sb = s2.getBounds();
        double xR = w2.getX() + w2.getWidth();
        double xL = w2.getX() - this.dialog.getWidth();
        if (sb.getMaxX() >= xR + this.dialog.getWidth()) {
            x2 = xR;
        } else if (sb.getMinX() <= xL) {
            x2 = xL;
        } else {
            x2 = Math.max(sb.getMinX(), sb.getMaxX() - this.dialog.getWidth());
        }
        double y2 = Math.max(sb.getMinY(), Math.min(sb.getMaxY() - this.dialog.getHeight(), w2.getY()));
        this.dialog.setX(x2);
        this.dialog.setY(y2);
    }

    @Override // javafx.scene.layout.HBox, javafx.scene.Parent
    public void layoutChildren() {
        super.layoutChildren();
        if (this.dialog.getMinWidth() > 0.0d && this.dialog.getMinHeight() > 0.0d) {
            return;
        }
        double minWidth = Math.max(0.0d, computeMinWidth(getHeight()) + (this.dialog.getWidth() - this.customScene.getWidth()));
        double minHeight = Math.max(0.0d, computeMinHeight(getWidth()) + (this.dialog.getHeight() - this.customScene.getHeight()));
        this.dialog.setMinWidth(minWidth);
        this.dialog.setMinHeight(minHeight);
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/CustomColorDialog$ColorRectPane.class */
    private class ColorRectPane extends HBox {
        private Pane colorRect;
        private Pane colorBar;
        private Pane colorRectOverlayOne;
        private Pane colorRectOverlayTwo;
        private Region colorRectIndicator;
        private Region colorBarIndicator;
        private boolean changeIsLocal = false;
        private DoubleProperty hue = new SimpleDoubleProperty(-1.0d) { // from class: com.sun.javafx.scene.control.skin.CustomColorDialog.ColorRectPane.1
            @Override // javafx.beans.property.DoublePropertyBase
            protected void invalidated() {
                if (!ColorRectPane.this.changeIsLocal) {
                    ColorRectPane.this.changeIsLocal = true;
                    ColorRectPane.this.updateHSBColor();
                    ColorRectPane.this.changeIsLocal = false;
                }
            }
        };
        private DoubleProperty sat = new SimpleDoubleProperty(-1.0d) { // from class: com.sun.javafx.scene.control.skin.CustomColorDialog.ColorRectPane.2
            @Override // javafx.beans.property.DoublePropertyBase
            protected void invalidated() {
                if (!ColorRectPane.this.changeIsLocal) {
                    ColorRectPane.this.changeIsLocal = true;
                    ColorRectPane.this.updateHSBColor();
                    ColorRectPane.this.changeIsLocal = false;
                }
            }
        };
        private DoubleProperty bright = new SimpleDoubleProperty(-1.0d) { // from class: com.sun.javafx.scene.control.skin.CustomColorDialog.ColorRectPane.3
            @Override // javafx.beans.property.DoublePropertyBase
            protected void invalidated() {
                if (!ColorRectPane.this.changeIsLocal) {
                    ColorRectPane.this.changeIsLocal = true;
                    ColorRectPane.this.updateHSBColor();
                    ColorRectPane.this.changeIsLocal = false;
                }
            }
        };
        private IntegerProperty red = new SimpleIntegerProperty(-1) { // from class: com.sun.javafx.scene.control.skin.CustomColorDialog.ColorRectPane.4
            @Override // javafx.beans.property.IntegerPropertyBase
            protected void invalidated() {
                if (!ColorRectPane.this.changeIsLocal) {
                    ColorRectPane.this.changeIsLocal = true;
                    ColorRectPane.this.updateRGBColor();
                    ColorRectPane.this.changeIsLocal = false;
                }
            }
        };
        private IntegerProperty green = new SimpleIntegerProperty(-1) { // from class: com.sun.javafx.scene.control.skin.CustomColorDialog.ColorRectPane.5
            @Override // javafx.beans.property.IntegerPropertyBase
            protected void invalidated() {
                if (!ColorRectPane.this.changeIsLocal) {
                    ColorRectPane.this.changeIsLocal = true;
                    ColorRectPane.this.updateRGBColor();
                    ColorRectPane.this.changeIsLocal = false;
                }
            }
        };
        private IntegerProperty blue = new SimpleIntegerProperty(-1) { // from class: com.sun.javafx.scene.control.skin.CustomColorDialog.ColorRectPane.6
            @Override // javafx.beans.property.IntegerPropertyBase
            protected void invalidated() {
                if (!ColorRectPane.this.changeIsLocal) {
                    ColorRectPane.this.changeIsLocal = true;
                    ColorRectPane.this.updateRGBColor();
                    ColorRectPane.this.changeIsLocal = false;
                }
            }
        };
        private DoubleProperty alpha = new SimpleDoubleProperty(100.0d) { // from class: com.sun.javafx.scene.control.skin.CustomColorDialog.ColorRectPane.7
            @Override // javafx.beans.property.DoublePropertyBase
            protected void invalidated() {
                if (!ColorRectPane.this.changeIsLocal) {
                    ColorRectPane.this.changeIsLocal = true;
                    CustomColorDialog.this.setCustomColor(new Color(CustomColorDialog.this.getCustomColor().getRed(), CustomColorDialog.this.getCustomColor().getGreen(), CustomColorDialog.this.getCustomColor().getBlue(), CustomColorDialog.clamp(ColorRectPane.this.alpha.get() / 100.0d)));
                    ColorRectPane.this.changeIsLocal = false;
                }
            }
        };

        /* JADX INFO: Access modifiers changed from: private */
        public void updateRGBColor() {
            Color newColor = Color.rgb(this.red.get(), this.green.get(), this.blue.get(), CustomColorDialog.clamp(this.alpha.get() / 100.0d));
            this.hue.set(newColor.getHue());
            this.sat.set(newColor.getSaturation() * 100.0d);
            this.bright.set(newColor.getBrightness() * 100.0d);
            CustomColorDialog.this.setCustomColor(newColor);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateHSBColor() {
            Color newColor = Color.hsb(this.hue.get(), CustomColorDialog.clamp(this.sat.get() / 100.0d), CustomColorDialog.clamp(this.bright.get() / 100.0d), CustomColorDialog.clamp(this.alpha.get() / 100.0d));
            this.red.set(CustomColorDialog.doubleToInt(newColor.getRed()));
            this.green.set(CustomColorDialog.doubleToInt(newColor.getGreen()));
            this.blue.set(CustomColorDialog.doubleToInt(newColor.getBlue()));
            CustomColorDialog.this.setCustomColor(newColor);
        }

        private void colorChanged() {
            if (!this.changeIsLocal) {
                this.changeIsLocal = true;
                this.hue.set(CustomColorDialog.this.getCustomColor().getHue());
                this.sat.set(CustomColorDialog.this.getCustomColor().getSaturation() * 100.0d);
                this.bright.set(CustomColorDialog.this.getCustomColor().getBrightness() * 100.0d);
                this.red.set(CustomColorDialog.doubleToInt(CustomColorDialog.this.getCustomColor().getRed()));
                this.green.set(CustomColorDialog.doubleToInt(CustomColorDialog.this.getCustomColor().getGreen()));
                this.blue.set(CustomColorDialog.doubleToInt(CustomColorDialog.this.getCustomColor().getBlue()));
                this.changeIsLocal = false;
            }
        }

        public ColorRectPane() {
            getStyleClass().add("color-rect-pane");
            CustomColorDialog.this.customColorProperty().addListener((ov, t2, t1) -> {
                colorChanged();
            });
            this.colorRectIndicator = new Region();
            this.colorRectIndicator.setId("color-rect-indicator");
            this.colorRectIndicator.setManaged(false);
            this.colorRectIndicator.setMouseTransparent(true);
            this.colorRectIndicator.setCache(true);
            Pane colorRectOpacityContainer = new StackPane();
            this.colorRect = new StackPane() { // from class: com.sun.javafx.scene.control.skin.CustomColorDialog.ColorRectPane.8
                @Override // javafx.scene.layout.StackPane, javafx.scene.Node
                public Orientation getContentBias() {
                    return Orientation.VERTICAL;
                }

                @Override // javafx.scene.layout.StackPane, javafx.scene.layout.Region, javafx.scene.Parent
                protected double computePrefWidth(double height) {
                    return height;
                }

                @Override // javafx.scene.layout.Region
                protected double computeMaxWidth(double height) {
                    return height;
                }
            };
            this.colorRect.getStyleClass().addAll("color-rect", "transparent-pattern");
            Pane colorRectHue = new Pane();
            colorRectHue.backgroundProperty().bind(new ObjectBinding<Background>() { // from class: com.sun.javafx.scene.control.skin.CustomColorDialog.ColorRectPane.9
                {
                    bind(ColorRectPane.this.hue);
                }

                /* JADX INFO: Access modifiers changed from: protected */
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // javafx.beans.binding.ObjectBinding
                public Background computeValue() {
                    return new Background(new BackgroundFill(Color.hsb(ColorRectPane.this.hue.getValue2().doubleValue(), 1.0d, 1.0d), CornerRadii.EMPTY, Insets.EMPTY));
                }
            });
            this.colorRectOverlayOne = new Pane();
            this.colorRectOverlayOne.getStyleClass().add("color-rect");
            this.colorRectOverlayOne.setBackground(new Background(new BackgroundFill(new LinearGradient(0.0d, 0.0d, 1.0d, 0.0d, true, CycleMethod.NO_CYCLE, new Stop(0.0d, Color.rgb(255, 255, 255, 1.0d)), new Stop(1.0d, Color.rgb(255, 255, 255, 0.0d))), CornerRadii.EMPTY, Insets.EMPTY)));
            EventHandler<MouseEvent> rectMouseHandler = event -> {
                double x2 = event.getX();
                double y2 = event.getY();
                this.sat.set(CustomColorDialog.clamp(x2 / this.colorRect.getWidth()) * 100.0d);
                this.bright.set(100.0d - (CustomColorDialog.clamp(y2 / this.colorRect.getHeight()) * 100.0d));
            };
            this.colorRectOverlayTwo = new Pane();
            this.colorRectOverlayTwo.getStyleClass().addAll("color-rect");
            this.colorRectOverlayTwo.setBackground(new Background(new BackgroundFill(new LinearGradient(0.0d, 0.0d, 0.0d, 1.0d, true, CycleMethod.NO_CYCLE, new Stop(0.0d, Color.rgb(0, 0, 0, 0.0d)), new Stop(1.0d, Color.rgb(0, 0, 0, 1.0d))), CornerRadii.EMPTY, Insets.EMPTY)));
            this.colorRectOverlayTwo.setOnMouseDragged(rectMouseHandler);
            this.colorRectOverlayTwo.setOnMousePressed(rectMouseHandler);
            Pane colorRectBlackBorder = new Pane();
            colorRectBlackBorder.setMouseTransparent(true);
            colorRectBlackBorder.getStyleClass().addAll("color-rect", "color-rect-border");
            this.colorBar = new Pane();
            this.colorBar.getStyleClass().add("color-bar");
            this.colorBar.setBackground(new Background(new BackgroundFill(CustomColorDialog.createHueGradient(), CornerRadii.EMPTY, Insets.EMPTY)));
            this.colorBarIndicator = new Region();
            this.colorBarIndicator.setId("color-bar-indicator");
            this.colorBarIndicator.setMouseTransparent(true);
            this.colorBarIndicator.setCache(true);
            this.colorRectIndicator.layoutXProperty().bind(this.sat.divide(100).multiply((ObservableNumberValue) this.colorRect.widthProperty()));
            this.colorRectIndicator.layoutYProperty().bind(Bindings.subtract(1, (ObservableNumberValue) this.bright.divide(100)).multiply(this.colorRect.heightProperty()));
            this.colorBarIndicator.layoutYProperty().bind(this.hue.divide(360).multiply((ObservableNumberValue) this.colorBar.heightProperty()));
            colorRectOpacityContainer.opacityProperty().bind(this.alpha.divide(100));
            EventHandler<MouseEvent> barMouseHandler = event2 -> {
                double y2 = event2.getY();
                this.hue.set(CustomColorDialog.clamp(y2 / this.colorRect.getHeight()) * 360.0d);
            };
            this.colorBar.setOnMouseDragged(barMouseHandler);
            this.colorBar.setOnMousePressed(barMouseHandler);
            this.colorBar.getChildren().setAll(this.colorBarIndicator);
            colorRectOpacityContainer.getChildren().setAll(colorRectHue, this.colorRectOverlayOne, this.colorRectOverlayTwo);
            this.colorRect.getChildren().setAll(colorRectOpacityContainer, colorRectBlackBorder, this.colorRectIndicator);
            HBox.setHgrow(this.colorRect, Priority.SOMETIMES);
            getChildren().addAll(this.colorRect, this.colorBar);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateValues() {
            if (CustomColorDialog.this.getCurrentColor() == null) {
                CustomColorDialog.this.setCurrentColor(Color.TRANSPARENT);
            }
            this.changeIsLocal = true;
            this.hue.set(CustomColorDialog.this.getCurrentColor().getHue());
            this.sat.set(CustomColorDialog.this.getCurrentColor().getSaturation() * 100.0d);
            this.bright.set(CustomColorDialog.this.getCurrentColor().getBrightness() * 100.0d);
            this.alpha.set(CustomColorDialog.this.getCurrentColor().getOpacity() * 100.0d);
            CustomColorDialog.this.setCustomColor(Color.hsb(this.hue.get(), CustomColorDialog.clamp(this.sat.get() / 100.0d), CustomColorDialog.clamp(this.bright.get() / 100.0d), CustomColorDialog.clamp(this.alpha.get() / 100.0d)));
            this.red.set(CustomColorDialog.doubleToInt(CustomColorDialog.this.getCustomColor().getRed()));
            this.green.set(CustomColorDialog.doubleToInt(CustomColorDialog.this.getCustomColor().getGreen()));
            this.blue.set(CustomColorDialog.doubleToInt(CustomColorDialog.this.getCustomColor().getBlue()));
            this.changeIsLocal = false;
        }

        @Override // javafx.scene.layout.HBox, javafx.scene.Parent
        protected void layoutChildren() {
            super.layoutChildren();
            this.colorRectIndicator.autosize();
            double size = Math.min(this.colorRect.getWidth(), this.colorRect.getHeight());
            this.colorRect.resize(size, size);
            this.colorBar.resize(this.colorBar.getWidth(), size);
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/CustomColorDialog$ControlsPane.class */
    private class ControlsPane extends VBox {
        private Label currentColorLabel;
        private Label newColorLabel;
        private Region currentColorRect;
        private Region newColorRect;
        private Region currentTransparent;
        private GridPane currentAndNewColor;
        private Region currentNewColorBorder;
        private ToggleButton hsbButton;
        private ToggleButton rgbButton;
        private ToggleButton webButton;
        private HBox hBox;
        private HBox buttonBox;
        private Region whiteBox;
        private GridPane settingsPane;
        private Label[] labels = new Label[4];
        private Slider[] sliders = new Slider[4];
        private IntegerField[] fields = new IntegerField[4];
        private Label[] units = new Label[4];
        private Property<Number>[] bindedProperties = new Property[4];

        public ControlsPane() {
            this.settingsPane = new GridPane();
            getStyleClass().add("controls-pane");
            this.currentNewColorBorder = new Region();
            this.currentNewColorBorder.setId("current-new-color-border");
            this.currentTransparent = new Region();
            this.currentTransparent.getStyleClass().addAll("transparent-pattern");
            this.currentColorRect = new Region();
            this.currentColorRect.getStyleClass().add("color-rect");
            this.currentColorRect.setId("current-color");
            this.currentColorRect.backgroundProperty().bind(new ObjectBinding<Background>() { // from class: com.sun.javafx.scene.control.skin.CustomColorDialog.ControlsPane.1
                {
                    bind(CustomColorDialog.this.currentColorProperty);
                }

                /* JADX INFO: Access modifiers changed from: protected */
                /* JADX WARN: Can't rename method to resolve collision */
                /* JADX WARN: Multi-variable type inference failed */
                @Override // javafx.beans.binding.ObjectBinding
                public Background computeValue() {
                    return new Background(new BackgroundFill((Paint) CustomColorDialog.this.currentColorProperty.get(), CornerRadii.EMPTY, Insets.EMPTY));
                }
            });
            this.newColorRect = new Region();
            this.newColorRect.getStyleClass().add("color-rect");
            this.newColorRect.setId("new-color");
            this.newColorRect.backgroundProperty().bind(new ObjectBinding<Background>() { // from class: com.sun.javafx.scene.control.skin.CustomColorDialog.ControlsPane.2
                {
                    bind(CustomColorDialog.this.customColorProperty);
                }

                /* JADX INFO: Access modifiers changed from: protected */
                /* JADX WARN: Can't rename method to resolve collision */
                /* JADX WARN: Multi-variable type inference failed */
                @Override // javafx.beans.binding.ObjectBinding
                public Background computeValue() {
                    return new Background(new BackgroundFill((Paint) CustomColorDialog.this.customColorProperty.get(), CornerRadii.EMPTY, Insets.EMPTY));
                }
            });
            this.currentColorLabel = new Label(ColorPickerSkin.getString("currentColor"));
            this.newColorLabel = new Label(ColorPickerSkin.getString("newColor"));
            this.whiteBox = new Region();
            this.whiteBox.getStyleClass().add("customcolor-controls-background");
            this.hsbButton = new ToggleButton(ColorPickerSkin.getString("colorType.hsb"));
            this.hsbButton.getStyleClass().add("left-pill");
            this.rgbButton = new ToggleButton(ColorPickerSkin.getString("colorType.rgb"));
            this.rgbButton.getStyleClass().add("center-pill");
            this.webButton = new ToggleButton(ColorPickerSkin.getString("colorType.web"));
            this.webButton.getStyleClass().add("right-pill");
            ToggleGroup group = new ToggleGroup();
            this.hBox = new HBox();
            this.hBox.setAlignment(Pos.CENTER);
            this.hBox.getChildren().addAll(this.hsbButton, this.rgbButton, this.webButton);
            Region spacer1 = new Region();
            spacer1.setId("spacer1");
            Region spacer2 = new Region();
            spacer2.setId("spacer2");
            Region leftSpacer = new Region();
            leftSpacer.setId("spacer-side");
            Region rightSpacer = new Region();
            rightSpacer.setId("spacer-side");
            Region bottomSpacer = new Region();
            bottomSpacer.setId("spacer-bottom");
            this.currentAndNewColor = new GridPane();
            this.currentAndNewColor.getColumnConstraints().addAll(new ColumnConstraints(), new ColumnConstraints());
            this.currentAndNewColor.getColumnConstraints().get(0).setHgrow(Priority.ALWAYS);
            this.currentAndNewColor.getColumnConstraints().get(1).setHgrow(Priority.ALWAYS);
            this.currentAndNewColor.getRowConstraints().addAll(new RowConstraints(), new RowConstraints(), new RowConstraints());
            this.currentAndNewColor.getRowConstraints().get(2).setVgrow(Priority.ALWAYS);
            VBox.setVgrow(this.currentAndNewColor, Priority.ALWAYS);
            this.currentAndNewColor.getStyleClass().add("current-new-color-grid");
            this.currentAndNewColor.add(this.currentColorLabel, 0, 0);
            this.currentAndNewColor.add(this.newColorLabel, 1, 0);
            this.currentAndNewColor.add(spacer1, 0, 1, 2, 1);
            this.currentAndNewColor.add(this.currentTransparent, 0, 2, 2, 1);
            this.currentAndNewColor.add(this.currentColorRect, 0, 2);
            this.currentAndNewColor.add(this.newColorRect, 1, 2);
            this.currentAndNewColor.add(this.currentNewColorBorder, 0, 2, 2, 1);
            this.currentAndNewColor.add(spacer2, 0, 3, 2, 1);
            this.settingsPane = new GridPane();
            this.settingsPane.setId("settings-pane");
            this.settingsPane.getColumnConstraints().addAll(new ColumnConstraints(), new ColumnConstraints(), new ColumnConstraints(), new ColumnConstraints(), new ColumnConstraints(), new ColumnConstraints());
            this.settingsPane.getColumnConstraints().get(0).setHgrow(Priority.NEVER);
            this.settingsPane.getColumnConstraints().get(2).setHgrow(Priority.ALWAYS);
            this.settingsPane.getColumnConstraints().get(3).setHgrow(Priority.NEVER);
            this.settingsPane.getColumnConstraints().get(4).setHgrow(Priority.NEVER);
            this.settingsPane.getColumnConstraints().get(5).setHgrow(Priority.NEVER);
            this.settingsPane.add(this.whiteBox, 0, 0, 6, 5);
            this.settingsPane.add(this.hBox, 0, 0, 6, 1);
            this.settingsPane.add(leftSpacer, 0, 0);
            this.settingsPane.add(rightSpacer, 5, 0);
            this.settingsPane.add(bottomSpacer, 0, 4);
            CustomColorDialog.this.webField = new WebColorField();
            CustomColorDialog.this.webField.getStyleClass().add("web-field");
            CustomColorDialog.this.webField.setSkin(new WebColorFieldSkin(CustomColorDialog.this.webField));
            CustomColorDialog.this.webField.valueProperty().bindBidirectional(CustomColorDialog.this.customColorProperty);
            CustomColorDialog.this.webField.visibleProperty().bind(group.selectedToggleProperty().isEqualTo(this.webButton));
            this.settingsPane.add(CustomColorDialog.this.webField, 2, 1);
            int i2 = 0;
            while (i2 < 4) {
                this.labels[i2] = new Label();
                this.labels[i2].getStyleClass().add("settings-label");
                this.sliders[i2] = new Slider();
                this.fields[i2] = new IntegerField();
                this.fields[i2].getStyleClass().add("color-input-field");
                this.fields[i2].setSkin(new IntegerFieldSkin(this.fields[i2]));
                this.units[i2] = new Label(i2 == 0 ? "°" : FXMLLoader.RESOURCE_KEY_PREFIX);
                this.units[i2].getStyleClass().add("settings-unit");
                if (i2 > 0 && i2 < 3) {
                    this.labels[i2].visibleProperty().bind(group.selectedToggleProperty().isNotEqualTo(this.webButton));
                }
                if (i2 < 3) {
                    this.sliders[i2].visibleProperty().bind(group.selectedToggleProperty().isNotEqualTo(this.webButton));
                    this.fields[i2].visibleProperty().bind(group.selectedToggleProperty().isNotEqualTo(this.webButton));
                    this.units[i2].visibleProperty().bind(group.selectedToggleProperty().isEqualTo(this.hsbButton));
                }
                int row = 1 + i2;
                row = i2 == 3 ? row + 1 : row;
                if (i2 != 3 || CustomColorDialog.this.showOpacitySlider) {
                    this.settingsPane.add(this.labels[i2], 1, row);
                    this.settingsPane.add(this.sliders[i2], 2, row);
                    this.settingsPane.add(this.fields[i2], 3, row);
                    this.settingsPane.add(this.units[i2], 4, row);
                }
                i2++;
            }
            set(3, ColorPickerSkin.getString("opacity_colon"), 100, CustomColorDialog.this.colorRectPane.alpha);
            this.hsbButton.setToggleGroup(group);
            this.rgbButton.setToggleGroup(group);
            this.webButton.setToggleGroup(group);
            group.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue == null) {
                    group.selectToggle(oldValue);
                    return;
                }
                if (newValue == this.hsbButton) {
                    showHSBSettings();
                } else if (newValue == this.rgbButton) {
                    showRGBSettings();
                } else {
                    showWebSettings();
                }
            });
            group.selectToggle(this.hsbButton);
            this.buttonBox = new HBox();
            this.buttonBox.setId("buttons-hbox");
            Button saveButton = new Button((CustomColorDialog.this.saveBtnText == null || CustomColorDialog.this.saveBtnText.isEmpty()) ? ColorPickerSkin.getString(ToolWindow.SAVE_POLICY_FILE) : CustomColorDialog.this.saveBtnText);
            saveButton.setDefaultButton(true);
            saveButton.setOnAction(t2 -> {
                if (CustomColorDialog.this.onSave != null) {
                    CustomColorDialog.this.onSave.run();
                }
                CustomColorDialog.this.dialog.hide();
            });
            Button useButton = new Button(ColorPickerSkin.getString("Use"));
            useButton.setOnAction(t3 -> {
                if (CustomColorDialog.this.onUse != null) {
                    CustomColorDialog.this.onUse.run();
                }
                CustomColorDialog.this.dialog.hide();
            });
            Button cancelButton = new Button(ColorPickerSkin.getString("Cancel"));
            cancelButton.setCancelButton(true);
            cancelButton.setOnAction(e2 -> {
                CustomColorDialog.this.customColorProperty.set(CustomColorDialog.this.getCurrentColor());
                if (CustomColorDialog.this.onCancel != null) {
                    CustomColorDialog.this.onCancel.run();
                }
                CustomColorDialog.this.dialog.hide();
            });
            if (CustomColorDialog.this.showUseBtn) {
                this.buttonBox.getChildren().addAll(saveButton, useButton, cancelButton);
            } else {
                this.buttonBox.getChildren().addAll(saveButton, cancelButton);
            }
            getChildren().addAll(this.currentAndNewColor, this.settingsPane, this.buttonBox);
        }

        private void showHSBSettings() {
            set(0, ColorPickerSkin.getString("hue_colon"), 360, CustomColorDialog.this.colorRectPane.hue);
            set(1, ColorPickerSkin.getString("saturation_colon"), 100, CustomColorDialog.this.colorRectPane.sat);
            set(2, ColorPickerSkin.getString("brightness_colon"), 100, CustomColorDialog.this.colorRectPane.bright);
        }

        private void showRGBSettings() {
            set(0, ColorPickerSkin.getString("red_colon"), 255, CustomColorDialog.this.colorRectPane.red);
            set(1, ColorPickerSkin.getString("green_colon"), 255, CustomColorDialog.this.colorRectPane.green);
            set(2, ColorPickerSkin.getString("blue_colon"), 255, CustomColorDialog.this.colorRectPane.blue);
        }

        private void showWebSettings() {
            this.labels[0].setText(ColorPickerSkin.getString("web_colon"));
        }

        private void set(int row, String caption, int maxValue, Property<Number> prop) {
            this.labels[row].setText(caption);
            if (this.bindedProperties[row] != null) {
                this.sliders[row].valueProperty().unbindBidirectional(this.bindedProperties[row]);
                this.fields[row].valueProperty().unbindBidirectional(this.bindedProperties[row]);
            }
            this.sliders[row].setMax(maxValue);
            this.sliders[row].valueProperty().bindBidirectional(prop);
            this.labels[row].setLabelFor(this.sliders[row]);
            this.fields[row].setMaxValue(maxValue);
            this.fields[row].valueProperty().bindBidirectional(prop);
            this.bindedProperties[row] = prop;
        }
    }

    static double clamp(double value) {
        if (value < 0.0d) {
            return 0.0d;
        }
        if (value > 1.0d) {
            return 1.0d;
        }
        return value;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static LinearGradient createHueGradient() {
        Stop[] stops = new Stop[255];
        for (int y2 = 0; y2 < 255; y2++) {
            double offset = 1.0d - (0.00392156862745098d * y2);
            int h2 = (int) ((y2 / 255.0d) * 360.0d);
            stops[y2] = new Stop(offset, Color.hsb(h2, 1.0d, 1.0d));
        }
        return new LinearGradient(0.0d, 1.0d, 0.0d, 0.0d, true, CycleMethod.NO_CYCLE, stops);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int doubleToInt(double value) {
        return (int) ((value * 255.0d) + 0.5d);
    }
}
