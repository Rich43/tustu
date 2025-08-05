package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.traversal.Algorithm;
import com.sun.javafx.scene.traversal.Direction;
import com.sun.javafx.scene.traversal.ParentTraversalEngine;
import com.sun.javafx.scene.traversal.TraversalContext;
import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PopupControl;
import javafx.scene.control.Separator;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/ColorPalette.class */
public class ColorPalette extends Region {
    private static final int SQUARE_SIZE = 15;
    ColorPickerGrid colorPickerGrid;
    private ColorPicker colorPicker;
    private PopupControl popupControl;
    private ColorSquare focusedSquare;
    private static final int NUM_OF_COLUMNS = 12;
    private static double[] RAW_VALUES = {255.0d, 255.0d, 255.0d, 242.0d, 242.0d, 242.0d, 230.0d, 230.0d, 230.0d, 204.0d, 204.0d, 204.0d, 179.0d, 179.0d, 179.0d, 153.0d, 153.0d, 153.0d, 128.0d, 128.0d, 128.0d, 102.0d, 102.0d, 102.0d, 77.0d, 77.0d, 77.0d, 51.0d, 51.0d, 51.0d, 26.0d, 26.0d, 26.0d, 0.0d, 0.0d, 0.0d, 0.0d, 51.0d, 51.0d, 0.0d, 26.0d, 128.0d, 26.0d, 0.0d, 104.0d, 51.0d, 0.0d, 51.0d, 77.0d, 0.0d, 26.0d, 153.0d, 0.0d, 0.0d, 153.0d, 51.0d, 0.0d, 153.0d, 77.0d, 0.0d, 153.0d, 102.0d, 0.0d, 153.0d, 153.0d, 0.0d, 102.0d, 102.0d, 0.0d, 0.0d, 51.0d, 0.0d, 26.0d, 77.0d, 77.0d, 26.0d, 51.0d, 153.0d, 51.0d, 26.0d, 128.0d, 77.0d, 26.0d, 77.0d, 102.0d, 26.0d, 51.0d, 179.0d, 26.0d, 26.0d, 179.0d, 77.0d, 26.0d, 179.0d, 102.0d, 26.0d, 179.0d, 128.0d, 26.0d, 179.0d, 179.0d, 26.0d, 128.0d, 128.0d, 26.0d, 26.0d, 77.0d, 26.0d, 51.0d, 102.0d, 102.0d, 51.0d, 77.0d, 179.0d, 77.0d, 51.0d, 153.0d, 102.0d, 51.0d, 102.0d, 128.0d, 51.0d, 77.0d, 204.0d, 51.0d, 51.0d, 204.0d, 102.0d, 51.0d, 204.0d, 128.0d, 51.0d, 204.0d, 153.0d, 51.0d, 204.0d, 204.0d, 51.0d, 153.0d, 153.0d, 51.0d, 51.0d, 102.0d, 51.0d, 77.0d, 128.0d, 128.0d, 77.0d, 102.0d, 204.0d, 102.0d, 77.0d, 179.0d, 128.0d, 77.0d, 128.0d, 153.0d, 77.0d, 102.0d, 230.0d, 77.0d, 77.0d, 230.0d, 128.0d, 77.0d, 230.0d, 153.0d, 77.0d, 230.0d, 179.0d, 77.0d, 230.0d, 230.0d, 77.0d, 179.0d, 179.0d, 77.0d, 77.0d, 128.0d, 77.0d, 102.0d, 153.0d, 153.0d, 102.0d, 128.0d, 230.0d, 128.0d, 102.0d, 204.0d, 153.0d, 102.0d, 153.0d, 179.0d, 102.0d, 128.0d, 255.0d, 102.0d, 102.0d, 255.0d, 153.0d, 102.0d, 255.0d, 179.0d, 102.0d, 255.0d, 204.0d, 102.0d, 255.0d, 255.0d, 77.0d, 204.0d, 204.0d, 102.0d, 102.0d, 153.0d, 102.0d, 128.0d, 179.0d, 179.0d, 128.0d, 153.0d, 255.0d, 153.0d, 128.0d, 230.0d, 179.0d, 128.0d, 179.0d, 204.0d, 128.0d, 153.0d, 255.0d, 128.0d, 128.0d, 255.0d, 153.0d, 128.0d, 255.0d, 204.0d, 128.0d, 255.0d, 230.0d, 102.0d, 255.0d, 255.0d, 102.0d, 230.0d, 230.0d, 128.0d, 128.0d, 179.0d, 128.0d, 153.0d, 204.0d, 204.0d, 153.0d, 179.0d, 255.0d, 179.0d, 153.0d, 255.0d, 204.0d, 153.0d, 204.0d, 230.0d, 153.0d, 179.0d, 255.0d, 153.0d, 153.0d, 255.0d, 179.0d, 128.0d, 255.0d, 204.0d, 153.0d, 255.0d, 230.0d, 128.0d, 255.0d, 255.0d, 128.0d, 230.0d, 230.0d, 153.0d, 153.0d, 204.0d, 153.0d, 179.0d, 230.0d, 230.0d, 179.0d, 204.0d, 255.0d, 204.0d, 179.0d, 255.0d, 230.0d, 179.0d, 230.0d, 230.0d, 179.0d, 204.0d, 255.0d, 179.0d, 179.0d, 255.0d, 179.0d, 153.0d, 255.0d, 230.0d, 179.0d, 255.0d, 230.0d, 153.0d, 255.0d, 255.0d, 153.0d, 230.0d, 230.0d, 179.0d, 179.0d, 230.0d, 179.0d, 204.0d, 255.0d, 255.0d, 204.0d, 230.0d, 255.0d, 230.0d, 204.0d, 255.0d, 255.0d, 204.0d, 255.0d, 255.0d, 204.0d, 230.0d, 255.0d, 204.0d, 204.0d, 255.0d, 204.0d, 179.0d, 255.0d, 230.0d, 204.0d, 255.0d, 255.0d, 179.0d, 255.0d, 255.0d, 204.0d, 230.0d, 230.0d, 204.0d, 204.0d, 255.0d, 204.0d};
    private static final int NUM_OF_COLORS = RAW_VALUES.length / 3;
    private static final int NUM_OF_ROWS = NUM_OF_COLORS / 12;
    final Hyperlink customColorLink = new Hyperlink(ColorPickerSkin.getString("customColorLink"));
    CustomColorDialog customColorDialog = null;
    private final GridPane customColorGrid = new GridPane();
    private final Separator separator = new Separator();
    private final Label customColorLabel = new Label(ColorPickerSkin.getString("customColorLabel"));
    private ContextMenu contextMenu = null;
    private Color mouseDragColor = null;
    private boolean dragDetected = false;
    private int customColorNumber = 0;
    private int customColorRows = 0;
    private int customColorLastRowLength = 0;
    private final ColorSquare hoverSquare = new ColorSquare(this);

    public ColorPalette(final ColorPicker colorPicker) {
        getStyleClass().add("color-palette-region");
        this.colorPicker = colorPicker;
        this.colorPickerGrid = new ColorPickerGrid();
        this.colorPickerGrid.getChildren().get(0).requestFocus();
        this.customColorLabel.setAlignment(Pos.CENTER_LEFT);
        this.customColorLink.setPrefWidth(this.colorPickerGrid.prefWidth(-1.0d));
        this.customColorLink.setAlignment(Pos.CENTER);
        this.customColorLink.setFocusTraversable(true);
        this.customColorLink.setVisited(true);
        this.customColorLink.setOnAction(new EventHandler<ActionEvent>() { // from class: com.sun.javafx.scene.control.skin.ColorPalette.1
            @Override // javafx.event.EventHandler
            public void handle(ActionEvent t2) {
                if (ColorPalette.this.customColorDialog == null) {
                    ColorPalette.this.customColorDialog = new CustomColorDialog(ColorPalette.this.popupControl);
                    ObjectProperty<Color> objectPropertyCustomColorProperty = ColorPalette.this.customColorDialog.customColorProperty();
                    ColorPicker colorPicker2 = colorPicker;
                    objectPropertyCustomColorProperty.addListener((ov, t1, t22) -> {
                        colorPicker2.setValue(ColorPalette.this.customColorDialog.customColorProperty().get());
                    });
                    CustomColorDialog customColorDialog = ColorPalette.this.customColorDialog;
                    ColorPicker colorPicker3 = colorPicker;
                    customColorDialog.setOnSave(() -> {
                        Color customColor = ColorPalette.this.customColorDialog.customColorProperty().get();
                        ColorPalette.this.buildCustomColors();
                        colorPicker3.getCustomColors().add(customColor);
                        ColorPalette.this.updateSelection(customColor);
                        Event.fireEvent(colorPicker3, new ActionEvent());
                        colorPicker3.hide();
                    });
                    CustomColorDialog customColorDialog2 = ColorPalette.this.customColorDialog;
                    ColorPicker colorPicker4 = colorPicker;
                    customColorDialog2.setOnUse(() -> {
                        Event.fireEvent(colorPicker4, new ActionEvent());
                        colorPicker4.hide();
                    });
                }
                ColorPalette.this.customColorDialog.setCurrentColor(colorPicker.valueProperty().get());
                if (ColorPalette.this.popupControl != null) {
                    ColorPalette.this.popupControl.setAutoHide(false);
                }
                ColorPalette.this.customColorDialog.show();
                ColorPalette.this.customColorDialog.setOnHidden(event -> {
                    if (ColorPalette.this.popupControl != null) {
                        ColorPalette.this.popupControl.setAutoHide(true);
                    }
                });
            }
        });
        initNavigation();
        this.customColorGrid.getStyleClass().add("color-picker-grid");
        this.customColorGrid.setVisible(false);
        buildCustomColors();
        colorPicker.getCustomColors().addListener(new ListChangeListener<Color>() { // from class: com.sun.javafx.scene.control.skin.ColorPalette.2
            @Override // javafx.collections.ListChangeListener
            public void onChanged(ListChangeListener.Change<? extends Color> change) {
                ColorPalette.this.buildCustomColors();
            }
        });
        VBox paletteBox = new VBox();
        paletteBox.getStyleClass().add("color-palette");
        paletteBox.getChildren().addAll(this.colorPickerGrid, this.customColorLabel, this.customColorGrid, this.separator, this.customColorLink);
        this.hoverSquare.setMouseTransparent(true);
        this.hoverSquare.getStyleClass().addAll("hover-square");
        setFocusedSquare(null);
        getChildren().addAll(paletteBox, this.hoverSquare);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setFocusedSquare(ColorSquare square) {
        double xAdjust;
        if (square == this.focusedSquare) {
            return;
        }
        this.focusedSquare = square;
        this.hoverSquare.setVisible(this.focusedSquare != null);
        if (this.focusedSquare == null) {
            return;
        }
        if (!this.focusedSquare.isFocused()) {
            this.focusedSquare.requestFocus();
        }
        this.hoverSquare.rectangle.setFill(this.focusedSquare.rectangle.getFill());
        Bounds b2 = square.localToScene(square.getLayoutBounds());
        double x2 = b2.getMinX();
        double y2 = b2.getMinY();
        double scaleAdjust = this.hoverSquare.getScaleX() == 1.0d ? 0.0d : this.hoverSquare.getWidth() / 4.0d;
        if (this.colorPicker.getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT) {
            x2 = this.focusedSquare.getLayoutX();
            xAdjust = (-this.focusedSquare.getWidth()) + scaleAdjust;
        } else {
            xAdjust = (this.focusedSquare.getWidth() / 2.0d) + scaleAdjust;
        }
        this.hoverSquare.setLayoutX(snapPosition(x2) - xAdjust);
        this.hoverSquare.setLayoutY((snapPosition(y2) - (this.focusedSquare.getHeight() / 2.0d)) + (this.hoverSquare.getScaleY() == 1.0d ? 0.0d : this.focusedSquare.getHeight() / 4.0d));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void buildCustomColors() {
        ObservableList<Color> customColors = this.colorPicker.getCustomColors();
        this.customColorNumber = customColors.size();
        this.customColorGrid.getChildren().clear();
        if (customColors.isEmpty()) {
            this.customColorLabel.setVisible(false);
            this.customColorLabel.setManaged(false);
            this.customColorGrid.setVisible(false);
            this.customColorGrid.setManaged(false);
            return;
        }
        this.customColorLabel.setVisible(true);
        this.customColorLabel.setManaged(true);
        this.customColorGrid.setVisible(true);
        this.customColorGrid.setManaged(true);
        if (this.contextMenu == null) {
            MenuItem item = new MenuItem(ColorPickerSkin.getString("removeColor"));
            item.setOnAction(e2 -> {
                ColorSquare square = (ColorSquare) this.contextMenu.getOwnerNode();
                customColors.remove(square.rectangle.getFill());
                buildCustomColors();
            });
            this.contextMenu = new ContextMenu(item);
        }
        int customColumnIndex = 0;
        int customRowIndex = 0;
        int remainingSquares = customColors.size() % 12;
        int numEmpty = remainingSquares == 0 ? 0 : 12 - remainingSquares;
        this.customColorLastRowLength = remainingSquares == 0 ? 12 : remainingSquares;
        for (int i2 = 0; i2 < customColors.size(); i2++) {
            Color c2 = customColors.get(i2);
            ColorSquare square = new ColorSquare(c2, i2, true);
            square.addEventHandler(KeyEvent.KEY_PRESSED, e3 -> {
                if (e3.getCode() == KeyCode.DELETE) {
                    customColors.remove(square.rectangle.getFill());
                    buildCustomColors();
                }
            });
            this.customColorGrid.add(square, customColumnIndex, customRowIndex);
            customColumnIndex++;
            if (customColumnIndex == 12) {
                customColumnIndex = 0;
                customRowIndex++;
            }
        }
        for (int i3 = 0; i3 < numEmpty; i3++) {
            ColorSquare emptySquare = new ColorSquare(this);
            this.customColorGrid.add(emptySquare, customColumnIndex, customRowIndex);
            customColumnIndex++;
        }
        this.customColorRows = customRowIndex + 1;
        requestLayout();
    }

    /* renamed from: com.sun.javafx.scene.control.skin.ColorPalette$4, reason: invalid class name */
    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/ColorPalette$4.class */
    static /* synthetic */ class AnonymousClass4 {
        static final /* synthetic */ int[] $SwitchMap$com$sun$javafx$scene$traversal$Direction;

        static {
            try {
                $SwitchMap$javafx$scene$input$KeyCode[KeyCode.SPACE.ordinal()] = 1;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$javafx$scene$input$KeyCode[KeyCode.ENTER.ordinal()] = 2;
            } catch (NoSuchFieldError e3) {
            }
            $SwitchMap$com$sun$javafx$scene$traversal$Direction = new int[Direction.values().length];
            try {
                $SwitchMap$com$sun$javafx$scene$traversal$Direction[Direction.NEXT.ordinal()] = 1;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$sun$javafx$scene$traversal$Direction[Direction.NEXT_IN_LINE.ordinal()] = 2;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$sun$javafx$scene$traversal$Direction[Direction.PREVIOUS.ordinal()] = 3;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$com$sun$javafx$scene$traversal$Direction[Direction.LEFT.ordinal()] = 4;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$com$sun$javafx$scene$traversal$Direction[Direction.RIGHT.ordinal()] = 5;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$com$sun$javafx$scene$traversal$Direction[Direction.UP.ordinal()] = 6;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$com$sun$javafx$scene$traversal$Direction[Direction.DOWN.ordinal()] = 7;
            } catch (NoSuchFieldError e10) {
            }
        }
    }

    private void initNavigation() {
        setOnKeyPressed(ke -> {
            switch (ke.getCode()) {
                case SPACE:
                case ENTER:
                    processSelectKey(ke);
                    ke.consume();
                    break;
            }
        });
        setImpl_traversalEngine(new ParentTraversalEngine(this, new Algorithm() { // from class: com.sun.javafx.scene.control.skin.ColorPalette.3
            @Override // com.sun.javafx.scene.traversal.Algorithm
            public Node select(Node owner, Direction dir, TraversalContext context) {
                Node subsequentNode = context.selectInSubtree(context.getRoot(), owner, dir);
                switch (AnonymousClass4.$SwitchMap$com$sun$javafx$scene$traversal$Direction[dir.ordinal()]) {
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                        if (!(owner instanceof ColorSquare)) {
                            break;
                        } else {
                            Node result = processArrow((ColorSquare) owner, dir);
                            if (result == null) {
                                break;
                            } else {
                                break;
                            }
                        }
                }
                return subsequentNode;
            }

            private Node processArrow(ColorSquare owner, Direction dir) {
                int i2;
                int row = owner.index / 12;
                int column = owner.index % 12;
                Direction dir2 = dir.getDirectionForNodeOrientation(ColorPalette.this.colorPicker.getEffectiveNodeOrientation());
                if (isAtBorder(dir2, row, column, owner.isCustom)) {
                    int subsequentRow = row;
                    int subsequentColumn = column;
                    boolean subSequentSquareCustom = owner.isCustom;
                    switch (AnonymousClass4.$SwitchMap$com$sun$javafx$scene$traversal$Direction[dir2.ordinal()]) {
                        case 4:
                        case 5:
                            if (owner.isCustom) {
                                subsequentRow = Math.floorMod(dir2 == Direction.LEFT ? row - 1 : row + 1, ColorPalette.this.customColorRows);
                                if (dir2 == Direction.LEFT) {
                                    i2 = subsequentRow == ColorPalette.this.customColorRows - 1 ? ColorPalette.this.customColorLastRowLength - 1 : 11;
                                } else {
                                    i2 = 0;
                                }
                                subsequentColumn = i2;
                                break;
                            } else {
                                subsequentRow = Math.floorMod(dir2 == Direction.LEFT ? row - 1 : row + 1, ColorPalette.NUM_OF_ROWS);
                                subsequentColumn = dir2 == Direction.LEFT ? 11 : 0;
                                break;
                            }
                        case 6:
                            subsequentRow = ColorPalette.NUM_OF_ROWS - 1;
                            break;
                        case 7:
                            if (ColorPalette.this.customColorNumber > 0) {
                                subSequentSquareCustom = true;
                                subsequentRow = 0;
                                subsequentColumn = ColorPalette.this.customColorRows > 1 ? column : Math.min(ColorPalette.this.customColorLastRowLength - 1, column);
                                break;
                            } else {
                                return null;
                            }
                    }
                    if (subSequentSquareCustom) {
                        return ColorPalette.this.customColorGrid.getChildren().get((subsequentRow * 12) + subsequentColumn);
                    }
                    return ColorPalette.this.colorPickerGrid.getChildren().get((subsequentRow * 12) + subsequentColumn);
                }
                return null;
            }

            private boolean isAtBorder(Direction dir, int row, int column, boolean custom) {
                switch (AnonymousClass4.$SwitchMap$com$sun$javafx$scene$traversal$Direction[dir.ordinal()]) {
                    case 4:
                        if (column == 0) {
                        }
                        break;
                    case 5:
                        if (custom && row == ColorPalette.this.customColorRows - 1) {
                            if (column == ColorPalette.this.customColorLastRowLength - 1) {
                            }
                        } else if (column == 11) {
                        }
                        break;
                    case 6:
                        if (custom || row != 0) {
                        }
                        break;
                    case 7:
                        if (custom || row != ColorPalette.NUM_OF_ROWS - 1) {
                        }
                        break;
                }
                return false;
            }

            @Override // com.sun.javafx.scene.traversal.Algorithm
            public Node selectFirst(TraversalContext context) {
                return ColorPalette.this.colorPickerGrid.getChildren().get(0);
            }

            @Override // com.sun.javafx.scene.traversal.Algorithm
            public Node selectLast(TraversalContext context) {
                return ColorPalette.this.customColorLink;
            }
        }));
    }

    private void processSelectKey(KeyEvent ke) {
        if (this.focusedSquare != null) {
            this.focusedSquare.selectColor(ke);
        }
    }

    public void setPopupControl(PopupControl pc) {
        this.popupControl = pc;
    }

    public ColorPickerGrid getColorGrid() {
        return this.colorPickerGrid;
    }

    public boolean isCustomColorDialogShowing() {
        if (this.customColorDialog != null) {
            return this.customColorDialog.isVisible();
        }
        return false;
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/ColorPalette$ColorSquare.class */
    class ColorSquare extends StackPane {
        Rectangle rectangle;
        int index;
        boolean isEmpty;
        boolean isCustom;

        public ColorSquare(ColorPalette this$0) {
            this(null, -1, false);
        }

        public ColorSquare(ColorPalette this$0, Color color, int index) {
            this(color, index, false);
        }

        public ColorSquare(Color color, int index, boolean isCustom) {
            getStyleClass().add("color-square");
            if (color != null) {
                setFocusTraversable(true);
                focusedProperty().addListener((s2, ov, nv) -> {
                    ColorPalette.this.setFocusedSquare(nv.booleanValue() ? this : null);
                });
                addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
                    ColorPalette.this.setFocusedSquare(this);
                });
                addEventHandler(MouseEvent.MOUSE_EXITED, event2 -> {
                    ColorPalette.this.setFocusedSquare(null);
                });
                addEventHandler(MouseEvent.MOUSE_RELEASED, event3 -> {
                    if (!ColorPalette.this.dragDetected && event3.getButton() == MouseButton.PRIMARY && event3.getClickCount() == 1) {
                        if (!this.isEmpty) {
                            Color fill = (Color) this.rectangle.getFill();
                            ColorPalette.this.colorPicker.setValue(fill);
                            ColorPalette.this.colorPicker.fireEvent(new ActionEvent());
                            ColorPalette.this.updateSelection(fill);
                            event3.consume();
                        }
                        ColorPalette.this.colorPicker.hide();
                        return;
                    }
                    if ((event3.getButton() == MouseButton.SECONDARY || event3.getButton() == MouseButton.MIDDLE) && isCustom && ColorPalette.this.contextMenu != null) {
                        if (!ColorPalette.this.contextMenu.isShowing()) {
                            ColorPalette.this.contextMenu.show(this, Side.RIGHT, 0.0d, 0.0d);
                            Utils.addMnemonics(ColorPalette.this.contextMenu, getScene(), ColorPalette.this.colorPicker.impl_isShowMnemonics());
                        } else {
                            ColorPalette.this.contextMenu.hide();
                            Utils.removeMnemonics(ColorPalette.this.contextMenu, getScene());
                        }
                    }
                });
            }
            this.index = index;
            this.isCustom = isCustom;
            this.rectangle = new Rectangle(15.0d, 15.0d);
            if (color == null) {
                this.rectangle.setFill(Color.WHITE);
                this.isEmpty = true;
            } else {
                this.rectangle.setFill(color);
            }
            this.rectangle.setStrokeType(StrokeType.INSIDE);
            String tooltipStr = ColorPickerSkin.tooltipString(color);
            Tooltip.install(this, new Tooltip(tooltipStr == null ? "" : tooltipStr));
            this.rectangle.getStyleClass().add("color-rect");
            getChildren().add(this.rectangle);
        }

        public void selectColor(KeyEvent event) {
            if (this.rectangle.getFill() != null) {
                if (this.rectangle.getFill() instanceof Color) {
                    ColorPalette.this.colorPicker.setValue((Color) this.rectangle.getFill());
                    ColorPalette.this.colorPicker.fireEvent(new ActionEvent());
                }
                event.consume();
            }
            ColorPalette.this.colorPicker.hide();
        }
    }

    public void updateSelection(Color color) {
        setFocusedSquare(null);
        for (ColorSquare c2 : this.colorPickerGrid.getSquares()) {
            if (c2.rectangle.getFill().equals(color)) {
                setFocusedSquare(c2);
                return;
            }
        }
        for (Node n2 : this.customColorGrid.getChildren()) {
            ColorSquare c3 = (ColorSquare) n2;
            if (c3.rectangle.getFill().equals(color)) {
                setFocusedSquare(c3);
                return;
            }
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/ColorPalette$ColorPickerGrid.class */
    class ColorPickerGrid extends GridPane {
        private final List<ColorSquare> squares;

        public ColorPickerGrid() {
            getStyleClass().add("color-picker-grid");
            setId("ColorCustomizerColorGrid");
            int columnIndex = 0;
            int rowIndex = 0;
            this.squares = FXCollections.observableArrayList();
            int numColors = ColorPalette.RAW_VALUES.length / 3;
            Color[] colors = new Color[numColors];
            for (int i2 = 0; i2 < numColors; i2++) {
                colors[i2] = new Color(ColorPalette.RAW_VALUES[i2 * 3] / 255.0d, ColorPalette.RAW_VALUES[(i2 * 3) + 1] / 255.0d, ColorPalette.RAW_VALUES[(i2 * 3) + 2] / 255.0d, 1.0d);
                ColorSquare cs = new ColorSquare(ColorPalette.this, colors[i2], i2);
                this.squares.add(cs);
            }
            for (ColorSquare square : this.squares) {
                add(square, columnIndex, rowIndex);
                columnIndex++;
                if (columnIndex == 12) {
                    columnIndex = 0;
                    rowIndex++;
                }
            }
            setOnMouseDragged(t2 -> {
                if (!ColorPalette.this.dragDetected) {
                    ColorPalette.this.dragDetected = true;
                    ColorPalette.this.mouseDragColor = ColorPalette.this.colorPicker.getValue();
                }
                int xIndex = com.sun.javafx.util.Utils.clamp(0, ((int) t2.getX()) / 16, 11);
                int yIndex = com.sun.javafx.util.Utils.clamp(0, ((int) t2.getY()) / 16, ColorPalette.NUM_OF_ROWS - 1);
                int index = xIndex + (yIndex * 12);
                ColorPalette.this.colorPicker.setValue((Color) this.squares.get(index).rectangle.getFill());
                ColorPalette.this.updateSelection(ColorPalette.this.colorPicker.getValue());
            });
            addEventHandler(MouseEvent.MOUSE_RELEASED, t3 -> {
                if (ColorPalette.this.colorPickerGrid.getBoundsInLocal().contains(t3.getX(), t3.getY())) {
                    ColorPalette.this.updateSelection(ColorPalette.this.colorPicker.getValue());
                    ColorPalette.this.colorPicker.fireEvent(new ActionEvent());
                    ColorPalette.this.colorPicker.hide();
                } else if (ColorPalette.this.mouseDragColor != null) {
                    ColorPalette.this.colorPicker.setValue(ColorPalette.this.mouseDragColor);
                    ColorPalette.this.updateSelection(ColorPalette.this.mouseDragColor);
                }
                ColorPalette.this.dragDetected = false;
            });
        }

        public List<ColorSquare> getSquares() {
            return this.squares;
        }

        @Override // javafx.scene.layout.GridPane, javafx.scene.layout.Region, javafx.scene.Parent
        protected double computePrefWidth(double height) {
            return 192.0d;
        }

        @Override // javafx.scene.layout.GridPane, javafx.scene.layout.Region, javafx.scene.Parent
        protected double computePrefHeight(double width) {
            return 16 * ColorPalette.NUM_OF_ROWS;
        }
    }
}
