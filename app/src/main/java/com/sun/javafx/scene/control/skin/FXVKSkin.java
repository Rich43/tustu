package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Window;
import javafx.util.Duration;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/FXVKSkin.class */
public class FXVKSkin extends BehaviorSkinBase<FXVK, BehaviorBase<FXVK>> {
    private static final int GAP = 6;
    private List<List<Key>> currentBoard;
    private int numCols;
    private boolean capsDown;
    private boolean shiftDown;
    private boolean isSymbol;
    long lastTime;
    private static Popup vkPopup;
    private static Popup secondaryPopup;
    private static FXVK primaryVK;
    private static FXVK secondaryVK;
    private static Timeline secondaryVKDelay;
    private static CharKey secondaryVKKey;
    private static TextInputKey repeatKey;
    private static Timeline repeatInitialDelay;
    private static Timeline repeatSubsequentDelay;
    private Node attachedNode;
    private String vkType;
    FXVK fxvk;
    static final double VK_HEIGHT = 243.0d;
    static final double VK_SLIDE_MILLIS = 250.0d;
    static final double PREF_PORTRAIT_KEY_WIDTH = 40.0d;
    static final double PREF_KEY_HEIGHT = 56.0d;
    private static DoubleProperty winY;
    EventHandler<InputEvent> unHideEventHandler;
    private boolean isVKHidden;
    private Double origWindowYPos;
    private static HashMap<String, List<List<Key>>> boardMap = new HashMap<>();
    private static Timeline slideInTimeline = new Timeline();
    private static Timeline slideOutTimeline = new Timeline();
    private static boolean hideAfterSlideOut = false;
    private static double KEY_REPEAT_DELAY = 400.0d;
    private static double KEY_REPEAT_DELAY_MIN = 100.0d;
    private static double KEY_REPEAT_DELAY_MAX = 1000.0d;
    private static double KEY_REPEAT_RATE = 25.0d;
    private static double KEY_REPEAT_RATE_MIN = 2.0d;
    private static double KEY_REPEAT_RATE_MAX = 50.0d;
    static boolean vkAdjustWindow = false;
    static boolean vkLookup = false;

    static {
        AccessController.doPrivileged(() -> {
            String s2 = System.getProperty("com.sun.javafx.vk.adjustwindow");
            if (s2 != null) {
                vkAdjustWindow = Boolean.valueOf(s2).booleanValue();
            }
            String s3 = System.getProperty("com.sun.javafx.sqe.vk.lookup");
            if (s3 != null) {
                vkLookup = Boolean.valueOf(s3).booleanValue();
            }
            String s4 = System.getProperty("com.sun.javafx.virtualKeyboard.backspaceRepeatDelay");
            if (s4 != null) {
                Double delay = Double.valueOf(s4);
                KEY_REPEAT_DELAY = Math.min(Math.max(delay.doubleValue(), KEY_REPEAT_DELAY_MIN), KEY_REPEAT_DELAY_MAX);
            }
            String s5 = System.getProperty("com.sun.javafx.virtualKeyboard.backspaceRepeatRate");
            if (s5 != null) {
                Double rate = Double.valueOf(s5);
                if (rate.doubleValue() <= 0.0d) {
                    KEY_REPEAT_RATE = 0.0d;
                    return null;
                }
                KEY_REPEAT_RATE = Math.min(Math.max(rate.doubleValue(), KEY_REPEAT_RATE_MIN), KEY_REPEAT_RATE_MAX);
                return null;
            }
            return null;
        });
        winY = new SimpleDoubleProperty();
        winY.addListener(valueModel -> {
            if (vkPopup != null) {
                vkPopup.setY(winY.get());
            }
        });
    }

    void clearShift() {
        if (this.shiftDown && !this.capsDown) {
            this.shiftDown = false;
            updateKeys();
        }
        this.lastTime = -1L;
    }

    void pressShift() {
        long time = System.currentTimeMillis();
        if (!this.shiftDown || this.capsDown) {
            if (!this.shiftDown && !this.capsDown) {
                this.shiftDown = true;
            } else {
                this.shiftDown = false;
                this.capsDown = false;
            }
        } else if (this.lastTime > 0 && time - this.lastTime < 400) {
            this.shiftDown = false;
            this.capsDown = true;
        } else {
            this.shiftDown = false;
            this.capsDown = false;
        }
        updateKeys();
        this.lastTime = time;
    }

    void clearSymbolABC() {
        this.isSymbol = false;
        updateKeys();
    }

    void pressSymbolABC() {
        this.isSymbol = !this.isSymbol;
        updateKeys();
    }

    void clearStateKeys() {
        this.capsDown = false;
        this.shiftDown = false;
        this.isSymbol = false;
        this.lastTime = -1L;
        updateKeys();
    }

    private void updateKeys() {
        for (List<Key> row : this.currentBoard) {
            for (Key key : row) {
                key.update(this.capsDown, this.shiftDown, this.isSymbol);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void startSlideIn() {
        slideOutTimeline.stop();
        slideInTimeline.playFromStart();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void startSlideOut(boolean doHide) {
        hideAfterSlideOut = doHide;
        slideInTimeline.stop();
        slideOutTimeline.playFromStart();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void adjustWindowPosition(Node node) {
        double inputLineBottomY;
        double newWindowYPos;
        double newWindowYPos2;
        if (!(node instanceof TextInputControl)) {
            return;
        }
        double inputControlMinY = node.localToScene(0.0d, 0.0d).getY() + node.getScene().getY();
        double inputControlHeight = ((TextInputControl) node).getHeight();
        double inputControlMaxY = inputControlMinY + inputControlHeight;
        double screenHeight = com.sun.javafx.util.Utils.getScreen(node).getBounds().getHeight();
        double visibleAreaMaxY = screenHeight - VK_HEIGHT;
        if (node instanceof TextField) {
            double inputLineCenterY = inputControlMinY + (inputControlHeight / 2.0d);
            inputLineBottomY = inputControlMaxY;
            Parent parent = this.attachedNode.getParent();
            if (parent instanceof ComboBoxBase) {
                newWindowYPos = Math.min(10.0d - inputControlMinY, 0.0d);
            } else {
                newWindowYPos = Math.min((visibleAreaMaxY / 2.0d) - inputLineCenterY, 0.0d);
            }
        } else if (node instanceof TextArea) {
            TextAreaSkin textAreaSkin = (TextAreaSkin) ((TextArea) node).getSkin();
            Bounds caretBounds = textAreaSkin.getCaretBounds();
            double caretMinY = caretBounds.getMinY();
            double caretMaxY = caretBounds.getMaxY();
            double inputLineCenterY2 = inputControlMinY + ((caretMinY + caretMaxY) / 2.0d);
            inputLineBottomY = inputControlMinY + caretMaxY;
            if (inputControlHeight < visibleAreaMaxY) {
                newWindowYPos2 = (visibleAreaMaxY / 2.0d) - (inputControlMinY + (inputControlHeight / 2.0d));
            } else {
                newWindowYPos2 = (visibleAreaMaxY / 2.0d) - inputLineCenterY2;
            }
            newWindowYPos = Math.min(newWindowYPos2, 0.0d);
        } else {
            double inputLineCenterY3 = inputControlMinY + (inputControlHeight / 2.0d);
            inputLineBottomY = inputControlMaxY;
            newWindowYPos = Math.min((visibleAreaMaxY / 2.0d) - inputLineCenterY3, 0.0d);
        }
        Window w2 = node.getScene().getWindow();
        if (this.origWindowYPos.doubleValue() + inputLineBottomY > visibleAreaMaxY) {
            w2.setY(newWindowYPos);
        } else {
            w2.setY(this.origWindowYPos.doubleValue());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void saveWindowPosition(Node node) {
        Window w2 = node.getScene().getWindow();
        this.origWindowYPos = Double.valueOf(w2.getY());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void restoreWindowPosition(Node node) {
        Scene scene;
        Window window;
        if (node != null && (scene = node.getScene()) != null && (window = scene.getWindow()) != null) {
            window.setY(this.origWindowYPos.doubleValue());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerUnhideHandler(Node node) {
        if (this.unHideEventHandler == null) {
            this.unHideEventHandler = event -> {
                if (this.attachedNode != null && this.isVKHidden) {
                    double screenHeight = com.sun.javafx.util.Utils.getScreen(this.attachedNode).getBounds().getHeight();
                    if (this.fxvk.getHeight() > 0.0d && vkPopup.getY() > screenHeight - this.fxvk.getHeight() && slideInTimeline.getStatus() != Animation.Status.RUNNING) {
                        startSlideIn();
                        if (vkAdjustWindow) {
                            adjustWindowPosition(this.attachedNode);
                        }
                    }
                }
                this.isVKHidden = false;
            };
        }
        node.addEventHandler(TouchEvent.TOUCH_PRESSED, this.unHideEventHandler);
        node.addEventHandler(MouseEvent.MOUSE_PRESSED, this.unHideEventHandler);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unRegisterUnhideHandler(Node node) {
        if (this.unHideEventHandler != null) {
            node.removeEventHandler(TouchEvent.TOUCH_PRESSED, this.unHideEventHandler);
            node.removeEventHandler(MouseEvent.MOUSE_PRESSED, this.unHideEventHandler);
        }
    }

    private String getNodeVKType(Node node) {
        Object typeValue = node.getProperties().get(FXVK.VK_TYPE_PROP_KEY);
        String typeStr = null;
        if (typeValue instanceof String) {
            typeStr = ((String) typeValue).toLowerCase(Locale.ROOT);
        }
        return typeStr != null ? typeStr : "text";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateKeyboardType(Node node) {
        String oldType = this.vkType;
        this.vkType = getNodeVKType(node);
        if (oldType == null || !this.vkType.equals(oldType)) {
            rebuildPrimaryVK(this.vkType);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void closeSecondaryVK() {
        if (secondaryVK != null) {
            secondaryVK.setAttachedNode(null);
            secondaryPopup.hide();
        }
    }

    private void setupPrimaryVK() {
        this.fxvk.setFocusTraversable(false);
        this.fxvk.setVisible(true);
        if (vkPopup == null) {
            vkPopup = new Popup();
            vkPopup.setAutoFix(false);
        }
        vkPopup.getContent().setAll(this.fxvk);
        double screenHeight = com.sun.javafx.util.Utils.getScreen(this.fxvk).getBounds().getHeight();
        double width = com.sun.javafx.util.Utils.getScreen(this.fxvk).getBounds().getWidth();
        slideInTimeline.getKeyFrames().setAll(new KeyFrame(Duration.millis(VK_SLIDE_MILLIS), new KeyValue(winY, Double.valueOf(screenHeight - VK_HEIGHT), Interpolator.EASE_BOTH)));
        slideOutTimeline.getKeyFrames().setAll(new KeyFrame(Duration.millis(VK_SLIDE_MILLIS), (EventHandler<ActionEvent>) event -> {
            if (hideAfterSlideOut && vkPopup.isShowing()) {
                vkPopup.hide();
            }
        }, new KeyValue(winY, Double.valueOf(screenHeight), Interpolator.EASE_BOTH)));
        this.fxvk.setPrefWidth(width);
        this.fxvk.setMinWidth(Double.NEGATIVE_INFINITY);
        this.fxvk.setMaxWidth(Double.NEGATIVE_INFINITY);
        this.fxvk.setPrefHeight(VK_HEIGHT);
        this.fxvk.setMinHeight(Double.NEGATIVE_INFINITY);
        if (secondaryVKDelay == null) {
            secondaryVKDelay = new Timeline();
        }
        KeyFrame kf = new KeyFrame(Duration.millis(500.0d), (EventHandler<ActionEvent>) event2 -> {
            if (secondaryVKKey != null) {
                showSecondaryVK(secondaryVKKey);
            }
        }, new KeyValue[0]);
        secondaryVKDelay.getKeyFrames().setAll(kf);
        if (KEY_REPEAT_RATE > 0.0d) {
            repeatInitialDelay = new Timeline(new KeyFrame(Duration.millis(KEY_REPEAT_DELAY), (EventHandler<ActionEvent>) event3 -> {
                repeatKey.sendKeyEvents();
                repeatSubsequentDelay.playFromStart();
            }, new KeyValue[0]));
            repeatSubsequentDelay = new Timeline(new KeyFrame(Duration.millis(1000.0d / KEY_REPEAT_RATE), (EventHandler<ActionEvent>) event4 -> {
                repeatKey.sendKeyEvents();
            }, new KeyValue[0]));
            repeatSubsequentDelay.setCycleCount(-1);
        }
    }

    void prerender(Node node) {
        if (this.fxvk != primaryVK) {
            return;
        }
        loadBoard("text");
        loadBoard("numeric");
        loadBoard("url");
        loadBoard("email");
        updateKeyboardType(node);
        this.fxvk.setVisible(true);
        if (!vkPopup.isShowing()) {
            Rectangle2D screenBounds = com.sun.javafx.util.Utils.getScreen(node).getBounds();
            vkPopup.setX((screenBounds.getWidth() - this.fxvk.prefWidth(-1.0d)) / 2.0d);
            winY.set(screenBounds.getHeight());
            vkPopup.show(node.getScene().getWindow());
        }
    }

    public FXVKSkin(final FXVK fxvk) {
        super(fxvk, new BehaviorBase(fxvk, Collections.emptyList()));
        this.capsDown = false;
        this.shiftDown = false;
        this.isSymbol = false;
        this.lastTime = -1L;
        this.vkType = null;
        this.isVKHidden = false;
        this.origWindowYPos = null;
        this.fxvk = fxvk;
        if (fxvk == FXVK.vk) {
            primaryVK = fxvk;
        }
        if (fxvk == primaryVK) {
            setupPrimaryVK();
        }
        fxvk.attachedNodeProperty().addListener(new InvalidationListener() { // from class: com.sun.javafx.scene.control.skin.FXVKSkin.1
            @Override // javafx.beans.InvalidationListener
            public void invalidated(Observable valueModel) {
                Node oldNode = FXVKSkin.this.attachedNode;
                FXVKSkin.this.attachedNode = fxvk.getAttachedNode();
                if (fxvk == FXVKSkin.primaryVK) {
                    FXVKSkin.this.closeSecondaryVK();
                    if (FXVKSkin.this.attachedNode != null) {
                        if (oldNode != null) {
                            FXVKSkin.this.unRegisterUnhideHandler(oldNode);
                        }
                        FXVKSkin.this.registerUnhideHandler(FXVKSkin.this.attachedNode);
                        FXVKSkin.this.updateKeyboardType(FXVKSkin.this.attachedNode);
                        if ((oldNode == null || oldNode.getScene() == null || oldNode.getScene().getWindow() != FXVKSkin.this.attachedNode.getScene().getWindow()) && FXVKSkin.vkPopup.isShowing()) {
                            FXVKSkin.vkPopup.hide();
                        }
                        if (!FXVKSkin.vkPopup.isShowing()) {
                            Rectangle2D screenBounds = com.sun.javafx.util.Utils.getScreen(FXVKSkin.this.attachedNode).getBounds();
                            FXVKSkin.vkPopup.setX((screenBounds.getWidth() - fxvk.prefWidth(-1.0d)) / 2.0d);
                            if (oldNode == null || FXVKSkin.this.isVKHidden) {
                                FXVKSkin.winY.set(screenBounds.getHeight());
                            } else {
                                FXVKSkin.winY.set(screenBounds.getHeight() - FXVKSkin.VK_HEIGHT);
                            }
                            FXVKSkin.vkPopup.show(FXVKSkin.this.attachedNode.getScene().getWindow());
                        }
                        if (oldNode == null || FXVKSkin.this.isVKHidden) {
                            FXVKSkin.startSlideIn();
                        }
                        if (FXVKSkin.vkAdjustWindow) {
                            if (oldNode == null || oldNode.getScene() == null || oldNode.getScene().getWindow() != FXVKSkin.this.attachedNode.getScene().getWindow()) {
                                FXVKSkin.this.saveWindowPosition(FXVKSkin.this.attachedNode);
                            }
                            FXVKSkin.this.adjustWindowPosition(FXVKSkin.this.attachedNode);
                        }
                    } else {
                        if (oldNode != null) {
                            FXVKSkin.this.unRegisterUnhideHandler(oldNode);
                        }
                        FXVKSkin.startSlideOut(true);
                        if (FXVKSkin.vkAdjustWindow) {
                            FXVKSkin.this.restoreWindowPosition(oldNode);
                        }
                    }
                    FXVKSkin.this.isVKHidden = false;
                }
            }
        });
    }

    private void rebuildSecondaryVK() {
        if (secondaryVK.chars != null) {
            int nKeys = secondaryVK.chars.length;
            int nRows = (int) Math.floor(Math.sqrt(Math.max(1, nKeys - 2)));
            int nKeysPerRow = (int) Math.ceil(nKeys / nRows);
            List<List<Key>> rows = new ArrayList<>(2);
            for (int i2 = 0; i2 < nRows; i2++) {
                int start = i2 * nKeysPerRow;
                int end = Math.min(start + nKeysPerRow, nKeys);
                if (start >= end) {
                    break;
                }
                List<Key> keys = new ArrayList<>(nKeysPerRow);
                for (int j2 = start; j2 < end; j2++) {
                    Key tmpKey = new CharKey(secondaryVK.chars[j2], (String) null, (String[]) null);
                    tmpKey.col = (j2 - start) * 2;
                    tmpKey.colSpan = 2;
                    for (String sc : tmpKey.getStyleClass()) {
                        tmpKey.text.getStyleClass().add(sc + "-text");
                        tmpKey.altText.getStyleClass().add(sc + "-alttext");
                        tmpKey.icon.getStyleClass().add(sc + "-icon");
                    }
                    if (secondaryVK.chars[j2] != null && secondaryVK.chars[j2].length() > 1) {
                        tmpKey.text.getStyleClass().add("multi-char-text");
                    }
                    keys.add(tmpKey);
                }
                rows.add(keys);
            }
            this.currentBoard = rows;
            getChildren().clear();
            this.numCols = 0;
            for (List<Key> row : this.currentBoard) {
                for (Key key : row) {
                    this.numCols = Math.max(this.numCols, key.col + key.colSpan);
                }
                getChildren().addAll(row);
            }
        }
    }

    private void rebuildPrimaryVK(String type) {
        this.currentBoard = loadBoard(type);
        clearStateKeys();
        getChildren().clear();
        this.numCols = 0;
        for (List<Key> row : this.currentBoard) {
            for (Key key : row) {
                this.numCols = Math.max(this.numCols, key.col + key.colSpan);
            }
            getChildren().addAll(row);
        }
    }

    @Override // javafx.scene.control.SkinBase
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return leftInset + (56 * this.numCols) + rightInset;
    }

    @Override // javafx.scene.control.SkinBase
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return topInset + 400.0d + bottomInset;
    }

    @Override // javafx.scene.control.SkinBase
    protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
        int numRows = this.currentBoard.size();
        double colWidth = (contentWidth - ((this.numCols - 1) * 6)) / this.numCols;
        double rowHeight = (contentHeight - ((numRows - 1) * 6)) / numRows;
        double rowY = contentY;
        for (List<Key> row : this.currentBoard) {
            for (Key key : row) {
                double startX = contentX + (key.col * (colWidth + 6.0d));
                double width = (key.colSpan * (colWidth + 6.0d)) - 6.0d;
                key.resizeRelocate((int) (startX + 0.5d), (int) (rowY + 0.5d), width, rowHeight);
            }
            rowY += rowHeight + 6.0d;
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/FXVKSkin$Key.class */
    private class Key extends Region {
        protected final Text altText;
        int col = 0;
        int colSpan = 1;
        protected final Region icon = new Region();
        protected final Text text = new Text();

        protected Key() {
            this.text.setTextOrigin(VPos.TOP);
            this.altText = new Text();
            this.altText.setTextOrigin(VPos.TOP);
            getChildren().setAll(this.text, this.altText, this.icon);
            getStyleClass().setAll("key");
            addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
                if (event.getButton() == MouseButton.PRIMARY) {
                    press();
                }
            });
            addEventHandler(MouseEvent.MOUSE_RELEASED, event2 -> {
                if (event2.getButton() == MouseButton.PRIMARY) {
                    release();
                }
            });
        }

        protected void press() {
        }

        protected void release() {
            FXVKSkin.this.clearShift();
        }

        public void update(boolean capsDown, boolean shiftDown, boolean isSymbol) {
        }

        @Override // javafx.scene.Parent
        protected void layoutChildren() {
            double left = snappedLeftInset();
            double top = snappedTopInset();
            double width = (getWidth() - left) - snappedRightInset();
            double height = (getHeight() - top) - snappedBottomInset();
            this.text.setVisible(this.icon.getBackground() == null);
            double contentPrefWidth = this.text.prefWidth(-1.0d);
            double contentPrefHeight = this.text.prefHeight(-1.0d);
            this.text.resizeRelocate((int) (left + ((width - contentPrefWidth) / 2.0d) + 0.5d), (int) (top + ((height - contentPrefHeight) / 2.0d) + 0.5d), (int) contentPrefWidth, (int) contentPrefHeight);
            this.altText.setVisible(this.icon.getBackground() == null && this.altText.getText().length() > 0);
            double contentPrefWidth2 = this.altText.prefWidth(-1.0d);
            double contentPrefHeight2 = this.altText.prefHeight(-1.0d);
            this.altText.resizeRelocate(((int) left) + (width - contentPrefWidth2) + 0.5d, (int) (((top + ((height - contentPrefHeight2) / 2.0d)) + 0.5d) - (height / 2.0d)), (int) contentPrefWidth2, (int) contentPrefHeight2);
            this.icon.resizeRelocate(left - 8.0d, top - 8.0d, width + 16.0d, height + 16.0d);
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/FXVKSkin$TextInputKey.class */
    private class TextInputKey extends Key {
        String chars;

        private TextInputKey() {
            super();
            this.chars = "";
        }

        @Override // com.sun.javafx.scene.control.skin.FXVKSkin.Key
        protected void press() {
        }

        @Override // com.sun.javafx.scene.control.skin.FXVKSkin.Key
        protected void release() {
            if (FXVKSkin.this.fxvk != FXVKSkin.secondaryVK && FXVKSkin.secondaryPopup != null && FXVKSkin.secondaryPopup.isShowing()) {
                return;
            }
            sendKeyEvents();
            if (FXVKSkin.this.fxvk == FXVKSkin.secondaryVK) {
                FXVKSkin.this.showSecondaryVK(null);
            }
            super.release();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public void sendKeyEvents() {
            Node target = FXVKSkin.this.fxvk.getAttachedNode();
            if ((target instanceof EventTarget) && this.chars != null) {
                target.fireEvent(new KeyEvent(KeyEvent.KEY_TYPED, this.chars, "", KeyCode.UNDEFINED, FXVKSkin.this.shiftDown, false, false, false));
            }
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/FXVKSkin$CharKey.class */
    private class CharKey extends TextInputKey {
        private final String letterChars;
        private final String altChars;
        private final String[] moreChars;

        private CharKey(String letter, String alt, String[] moreChars, String id) {
            super();
            this.letterChars = letter;
            this.altChars = alt;
            this.moreChars = moreChars;
            this.chars = this.letterChars;
            this.text.setText(this.chars);
            this.altText.setText(this.altChars);
            if (FXVKSkin.vkLookup) {
                setId((id != null ? id : this.chars).replaceAll("\\.", ""));
            }
        }

        private CharKey(FXVKSkin fXVKSkin, String letter, String alt, String[] moreChars) {
            this(letter, alt, moreChars, (String) null);
        }

        @Override // com.sun.javafx.scene.control.skin.FXVKSkin.TextInputKey, com.sun.javafx.scene.control.skin.FXVKSkin.Key
        protected void press() {
            super.press();
            if ((!this.letterChars.equals(this.altChars) || this.moreChars != null) && FXVKSkin.this.fxvk == FXVKSkin.primaryVK) {
                FXVKSkin.this.showSecondaryVK(null);
                CharKey unused = FXVKSkin.secondaryVKKey = this;
                FXVKSkin.secondaryVKDelay.playFromStart();
            }
        }

        @Override // com.sun.javafx.scene.control.skin.FXVKSkin.TextInputKey, com.sun.javafx.scene.control.skin.FXVKSkin.Key
        protected void release() {
            super.release();
            if ((!this.letterChars.equals(this.altChars) || this.moreChars != null) && FXVKSkin.this.fxvk == FXVKSkin.primaryVK) {
                FXVKSkin.secondaryVKDelay.stop();
            }
        }

        @Override // com.sun.javafx.scene.control.skin.FXVKSkin.Key
        public void update(boolean capsDown, boolean shiftDown, boolean isSymbol) {
            if (isSymbol) {
                this.chars = this.altChars;
                this.text.setText(this.chars);
                if (this.moreChars != null && this.moreChars.length > 0 && !Character.isLetter(this.moreChars[0].charAt(0))) {
                    this.altText.setText(this.moreChars[0]);
                    return;
                } else {
                    this.altText.setText(null);
                    return;
                }
            }
            this.chars = (capsDown || shiftDown) ? this.letterChars.toUpperCase() : this.letterChars.toLowerCase();
            this.text.setText(this.chars);
            this.altText.setText(this.altChars);
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/FXVKSkin$SuperKey.class */
    private class SuperKey extends TextInputKey {
        private SuperKey(String letter, String code) {
            super();
            this.chars = code;
            this.text.setText(letter);
            getStyleClass().add("special");
            if (FXVKSkin.vkLookup) {
                setId(letter);
            }
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/FXVKSkin$KeyCodeKey.class */
    private class KeyCodeKey extends SuperKey {
        private KeyCode code;

        private KeyCodeKey(String letter, String c2, KeyCode code) {
            super(letter, c2);
            this.code = code;
            if (FXVKSkin.vkLookup) {
                setId(letter);
            }
        }

        @Override // com.sun.javafx.scene.control.skin.FXVKSkin.TextInputKey
        protected void sendKeyEvents() {
            Node target = FXVKSkin.this.fxvk.getAttachedNode();
            if (target instanceof EventTarget) {
                target.fireEvent(new KeyEvent(KeyEvent.KEY_PRESSED, KeyEvent.CHAR_UNDEFINED, this.chars, this.code, FXVKSkin.this.shiftDown, false, false, false));
                target.fireEvent(new KeyEvent(KeyEvent.KEY_TYPED, this.chars, "", KeyCode.UNDEFINED, FXVKSkin.this.shiftDown, false, false, false));
                target.fireEvent(new KeyEvent(KeyEvent.KEY_RELEASED, KeyEvent.CHAR_UNDEFINED, this.chars, this.code, FXVKSkin.this.shiftDown, false, false, false));
            }
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/FXVKSkin$KeyboardStateKey.class */
    private class KeyboardStateKey extends Key {
        private final String defaultText;
        private final String toggledText;

        private KeyboardStateKey(String defaultText, String toggledText, String id) {
            super();
            this.defaultText = defaultText;
            this.toggledText = toggledText;
            this.text.setText(this.defaultText);
            if (FXVKSkin.vkLookup && id != null) {
                setId(id);
            }
            getStyleClass().add("special");
        }

        @Override // com.sun.javafx.scene.control.skin.FXVKSkin.Key
        public void update(boolean capsDown, boolean shiftDown, boolean isSymbol) {
            if (isSymbol) {
                this.text.setText(this.toggledText);
            } else {
                this.text.setText(this.defaultText);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showSecondaryVK(CharKey key) {
        if (key != null) {
            Node textInput = primaryVK.getAttachedNode();
            if (secondaryVK == null) {
                secondaryVK = new FXVK();
                secondaryVK.setSkin(new FXVKSkin(secondaryVK));
                secondaryVK.getStyleClass().setAll("fxvk-secondary");
                secondaryPopup = new Popup();
                secondaryPopup.setAutoHide(true);
                secondaryPopup.getContent().add(secondaryVK);
            }
            secondaryVK.chars = null;
            ArrayList<String> secondaryList = new ArrayList<>();
            if (!this.isSymbol && key.letterChars != null && key.letterChars.length() > 0) {
                if (this.shiftDown || this.capsDown) {
                    secondaryList.add(key.letterChars.toUpperCase());
                } else {
                    secondaryList.add(key.letterChars);
                }
            }
            if (key.altChars != null && key.altChars.length() > 0) {
                if (this.shiftDown || this.capsDown) {
                    secondaryList.add(key.altChars.toUpperCase());
                } else {
                    secondaryList.add(key.altChars);
                }
            }
            if (key.moreChars != null && key.moreChars.length > 0) {
                if (this.isSymbol) {
                    for (String ch : key.moreChars) {
                        if (!Character.isLetter(ch.charAt(0))) {
                            secondaryList.add(ch);
                        }
                    }
                } else {
                    for (String ch2 : key.moreChars) {
                        if (Character.isLetter(ch2.charAt(0))) {
                            if (this.shiftDown || this.capsDown) {
                                secondaryList.add(ch2.toUpperCase());
                            } else {
                                secondaryList.add(ch2);
                            }
                        }
                    }
                }
            }
            boolean isMultiChar = false;
            Iterator<String> it = secondaryList.iterator();
            while (it.hasNext()) {
                String s2 = it.next();
                if (s2.length() > 1) {
                    isMultiChar = true;
                }
            }
            secondaryVK.chars = (String[]) secondaryList.toArray(new String[secondaryList.size()]);
            if (secondaryVK.chars.length > 1) {
                if (secondaryVK.getSkin() != null) {
                    ((FXVKSkin) secondaryVK.getSkin()).rebuildSecondaryVK();
                }
                secondaryVK.setAttachedNode(textInput);
                int nKeys = secondaryVK.chars.length;
                int nRows = (int) Math.floor(Math.sqrt(Math.max(1, nKeys - 2)));
                int nKeysPerRow = (int) Math.ceil(nKeys / nRows);
                double w2 = snappedLeftInset() + snappedRightInset() + (nKeysPerRow * PREF_PORTRAIT_KEY_WIDTH * (isMultiChar ? 2 : 1)) + ((nKeysPerRow - 1) * 6);
                double h2 = snappedTopInset() + snappedBottomInset() + (nRows * PREF_KEY_HEIGHT) + ((nRows - 1) * 6);
                secondaryVK.setPrefWidth(w2);
                secondaryVK.setMinWidth(Double.NEGATIVE_INFINITY);
                secondaryVK.setPrefHeight(h2);
                secondaryVK.setMinHeight(Double.NEGATIVE_INFINITY);
                Platform.runLater(() -> {
                    Point2D nodePoint = com.sun.javafx.util.Utils.pointRelativeTo(key, w2, h2, HPos.CENTER, VPos.TOP, 5.0d, -3.0d, true);
                    double x2 = nodePoint.getX();
                    double y2 = nodePoint.getY();
                    Scene scene = key.getScene();
                    secondaryPopup.show(key.getScene().getWindow(), Math.min(x2, (scene.getWindow().getX() + scene.getWidth()) - w2), y2);
                });
                return;
            }
            return;
        }
        closeSecondaryVK();
    }

    private List<List<Key>> loadBoard(String type) {
        Key key;
        List<List<Key>> tmpBoard = boardMap.get(type);
        if (tmpBoard != null) {
            return tmpBoard;
        }
        String boardFileName = type.substring(0, 1).toUpperCase() + type.substring(1).toLowerCase() + "Board.txt";
        try {
            List<List<Key>> tmpBoard2 = new ArrayList<>(5);
            List<Key> keys = new ArrayList<>(20);
            InputStream boardFile = FXVKSkin.class.getResourceAsStream(boardFileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(boardFile));
            int c2 = 0;
            int col = 0;
            boolean identifier = false;
            List<String> charsList = new ArrayList<>(10);
            while (true) {
                String line = reader.readLine();
                if (line != null) {
                    if (line.length() != 0 && line.charAt(0) != '#') {
                        int i2 = 0;
                        while (i2 < line.length()) {
                            char ch = line.charAt(i2);
                            if (ch == ' ') {
                                c2++;
                            } else if (ch == '[') {
                                col = c2;
                                charsList = new ArrayList<>(10);
                                identifier = false;
                            } else if (ch == ']') {
                                String chars = "";
                                String alt = null;
                                String[] moreChars = null;
                                for (int idx = 0; idx < charsList.size(); idx++) {
                                    charsList.set(idx, FXVKCharEntities.get(charsList.get(idx)));
                                }
                                int listSize = charsList.size();
                                if (listSize > 0) {
                                    chars = charsList.get(0);
                                    if (listSize > 1) {
                                        alt = charsList.get(1);
                                        if (listSize > 2) {
                                            moreChars = (String[]) charsList.subList(2, listSize).toArray(new String[listSize - 2]);
                                        }
                                    }
                                }
                                int colSpan = c2 - col;
                                if (identifier) {
                                    if ("$shift".equals(chars)) {
                                        key = new KeyboardStateKey("", null, "shift") { // from class: com.sun.javafx.scene.control.skin.FXVKSkin.2
                                            @Override // com.sun.javafx.scene.control.skin.FXVKSkin.Key
                                            protected void release() {
                                                FXVKSkin.this.pressShift();
                                            }

                                            @Override // com.sun.javafx.scene.control.skin.FXVKSkin.KeyboardStateKey, com.sun.javafx.scene.control.skin.FXVKSkin.Key
                                            public void update(boolean capsDown, boolean shiftDown, boolean isSymbol) {
                                                if (isSymbol) {
                                                    setDisable(true);
                                                    setVisible(false);
                                                    return;
                                                }
                                                if (capsDown) {
                                                    this.icon.getStyleClass().remove("shift-icon");
                                                    this.icon.getStyleClass().add("capslock-icon");
                                                } else {
                                                    this.icon.getStyleClass().remove("capslock-icon");
                                                    this.icon.getStyleClass().add("shift-icon");
                                                }
                                                setDisable(false);
                                                setVisible(true);
                                            }
                                        };
                                        key.getStyleClass().add("shift");
                                    } else if ("$SymbolABC".equals(chars)) {
                                        key = new KeyboardStateKey("!#123", "ABC", "symbol") { // from class: com.sun.javafx.scene.control.skin.FXVKSkin.3
                                            @Override // com.sun.javafx.scene.control.skin.FXVKSkin.Key
                                            protected void release() {
                                                FXVKSkin.this.pressSymbolABC();
                                            }
                                        };
                                    } else if ("$backspace".equals(chars)) {
                                        key = new KeyCodeKey("backspace", "\b", KeyCode.BACK_SPACE) { // from class: com.sun.javafx.scene.control.skin.FXVKSkin.4
                                            @Override // com.sun.javafx.scene.control.skin.FXVKSkin.TextInputKey, com.sun.javafx.scene.control.skin.FXVKSkin.Key
                                            protected void press() {
                                                if (FXVKSkin.KEY_REPEAT_RATE > 0.0d) {
                                                    FXVKSkin.this.clearShift();
                                                    sendKeyEvents();
                                                    TextInputKey unused = FXVKSkin.repeatKey = this;
                                                    FXVKSkin.repeatInitialDelay.playFromStart();
                                                    return;
                                                }
                                                super.press();
                                            }

                                            @Override // com.sun.javafx.scene.control.skin.FXVKSkin.TextInputKey, com.sun.javafx.scene.control.skin.FXVKSkin.Key
                                            protected void release() {
                                                if (FXVKSkin.KEY_REPEAT_RATE > 0.0d) {
                                                    FXVKSkin.repeatInitialDelay.stop();
                                                    FXVKSkin.repeatSubsequentDelay.stop();
                                                } else {
                                                    super.release();
                                                }
                                            }
                                        };
                                        key.getStyleClass().add("backspace");
                                    } else if ("$enter".equals(chars)) {
                                        key = new KeyCodeKey("enter", "\n", KeyCode.ENTER);
                                        key.getStyleClass().add("enter");
                                    } else if ("$tab".equals(chars)) {
                                        key = new KeyCodeKey("tab", "\t", KeyCode.TAB);
                                    } else if ("$space".equals(chars)) {
                                        key = new CharKey(" ", " ", null, "space");
                                    } else if ("$clear".equals(chars)) {
                                        key = new SuperKey(Constants.CLEAR_ATTRIBUTES, "");
                                    } else if ("$.org".equals(chars)) {
                                        key = new SuperKey(".org", ".org");
                                    } else if ("$.com".equals(chars)) {
                                        key = new SuperKey(".com", ".com");
                                    } else if ("$.net".equals(chars)) {
                                        key = new SuperKey(".net", ".net");
                                    } else if ("$oracle.com".equals(chars)) {
                                        key = new SuperKey("oracle.com", "oracle.com");
                                    } else if ("$gmail.com".equals(chars)) {
                                        key = new SuperKey("gmail.com", "gmail.com");
                                    } else if ("$hide".equals(chars)) {
                                        key = new KeyboardStateKey("hide", null, "hide") { // from class: com.sun.javafx.scene.control.skin.FXVKSkin.5
                                            @Override // com.sun.javafx.scene.control.skin.FXVKSkin.Key
                                            protected void release() {
                                                FXVKSkin.this.isVKHidden = true;
                                                FXVKSkin.startSlideOut(false);
                                                if (FXVKSkin.vkAdjustWindow) {
                                                    FXVKSkin.this.restoreWindowPosition(FXVKSkin.this.attachedNode);
                                                }
                                            }
                                        };
                                        key.getStyleClass().add("hide");
                                    } else if ("$undo".equals(chars)) {
                                        key = new SuperKey("undo", "");
                                    } else if ("$redo".equals(chars)) {
                                        key = new SuperKey("redo", "");
                                    } else {
                                        key = null;
                                    }
                                } else {
                                    key = new CharKey(chars, alt, moreChars);
                                }
                                if (key != null) {
                                    key.col = col;
                                    key.colSpan = colSpan;
                                    for (String sc : key.getStyleClass()) {
                                        key.text.getStyleClass().add(sc + "-text");
                                        key.altText.getStyleClass().add(sc + "-alttext");
                                        key.icon.getStyleClass().add(sc + "-icon");
                                    }
                                    if (chars != null && chars.length() > 1) {
                                        key.text.getStyleClass().add("multi-char-text");
                                    }
                                    if (alt != null && alt.length() > 1) {
                                        key.altText.getStyleClass().add("multi-char-text");
                                    }
                                    keys.add(key);
                                }
                            } else {
                                int j2 = i2;
                                while (true) {
                                    if (j2 >= line.length()) {
                                        break;
                                    }
                                    char c22 = line.charAt(j2);
                                    boolean e2 = false;
                                    if (c22 == '\\') {
                                        j2++;
                                        i2++;
                                        e2 = true;
                                        c22 = line.charAt(j2);
                                    }
                                    if (c22 == '$' && !e2) {
                                        identifier = true;
                                    }
                                    if (c22 == '|' && !e2) {
                                        charsList.add(line.substring(i2, j2));
                                        i2 = j2 + 1;
                                    } else if ((c22 == ']' || c22 == ' ') && !e2) {
                                        charsList.add(line.substring(i2, j2));
                                        i2 = j2 - 1;
                                        break;
                                    }
                                    j2++;
                                }
                                c2++;
                            }
                            i2++;
                        }
                        c2 = 0;
                        col = 0;
                        tmpBoard2.add(keys);
                        keys = new ArrayList<>(20);
                    }
                } else {
                    reader.close();
                    boardMap.put(type, tmpBoard2);
                    return tmpBoard2;
                }
            }
        } catch (Exception e3) {
            e3.printStackTrace();
            return Collections.emptyList();
        }
    }
}
