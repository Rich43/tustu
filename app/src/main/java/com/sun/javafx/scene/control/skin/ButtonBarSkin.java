package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.beans.InvalidationListener;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/ButtonBarSkin.class */
public class ButtonBarSkin extends BehaviorSkinBase<ButtonBar, BehaviorBase<ButtonBar>> {
    private static final double GAP_SIZE = 10.0d;
    private static final String CATEGORIZED_TYPES = "LRHEYNXBIACO";
    public static final String BUTTON_DATA_PROPERTY = "javafx.scene.control.ButtonBar.ButtonData";
    public static final String BUTTON_SIZE_INDEPENDENCE = "javafx.scene.control.ButtonBar.independentSize";
    private static final double DO_NOT_CHANGE_SIZE = Double.MAX_VALUE;
    private HBox layout;
    private InvalidationListener buttonDataListener;

    public ButtonBarSkin(ButtonBar control) {
        super(control, new BehaviorBase(control, Collections.emptyList()));
        this.buttonDataListener = o2 -> {
            layoutButtons();
        };
        this.layout = new HBox(10.0d) { // from class: com.sun.javafx.scene.control.skin.ButtonBarSkin.1
            @Override // javafx.scene.layout.HBox, javafx.scene.Parent
            protected void layoutChildren() {
                ButtonBarSkin.this.resizeButtons();
                super.layoutChildren();
            }
        };
        this.layout.setAlignment(Pos.CENTER);
        this.layout.getStyleClass().add("container");
        getChildren().add(this.layout);
        layoutButtons();
        updateButtonListeners(control.getButtons(), true);
        control.getButtons().addListener(c2 -> {
            while (c2.next()) {
                updateButtonListeners(c2.getRemoved(), false);
                updateButtonListeners(c2.getAddedSubList(), true);
            }
            layoutButtons();
        });
        registerChangeListener(control.buttonOrderProperty(), "BUTTON_ORDER");
        registerChangeListener(control.buttonMinWidthProperty(), "BUTTON_MIN_WIDTH");
    }

    private void updateButtonListeners(List<? extends Node> list, boolean buttonsAdded) {
        ObjectProperty<ButtonBar.ButtonData> property;
        if (list != null) {
            for (Node n2 : list) {
                Map<Object, Object> properties = n2.getProperties();
                if (properties.containsKey(BUTTON_DATA_PROPERTY) && (property = (ObjectProperty) properties.get(BUTTON_DATA_PROPERTY)) != null) {
                    if (buttonsAdded) {
                        property.addListener(this.buttonDataListener);
                    } else {
                        property.removeListener(this.buttonDataListener);
                    }
                }
            }
        }
    }

    @Override // com.sun.javafx.scene.control.skin.BehaviorSkinBase
    protected void handleControlPropertyChanged(String p2) {
        super.handleControlPropertyChanged(p2);
        if ("BUTTON_ORDER".equals(p2)) {
            layoutButtons();
        } else if ("BUTTON_MIN_WIDTH".equals(p2)) {
            resizeButtons();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void layoutButtons() {
        ButtonBar buttonBar = (ButtonBar) getSkinnable();
        List<? extends Node> buttons = buttonBar.getButtons();
        double buttonMinWidth = buttonBar.getButtonMinWidth();
        String buttonOrder = ((ButtonBar) getSkinnable()).getButtonOrder();
        this.layout.getChildren().clear();
        if (buttonOrder == null) {
            throw new IllegalStateException("ButtonBar buttonOrder string can not be null");
        }
        if (buttonOrder == "") {
            Spacer.DYNAMIC.add(this.layout, true);
            for (Node btn : buttons) {
                sizeButton(btn, buttonMinWidth, Double.MAX_VALUE, Double.MAX_VALUE);
                this.layout.getChildren().add(btn);
                HBox.setHgrow(btn, Priority.NEVER);
            }
            return;
        }
        doButtonOrderLayout(buttonOrder);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void doButtonOrderLayout(String buttonOrder) {
        ButtonBar buttonBar = (ButtonBar) getSkinnable();
        List<? extends Node> buttons = buttonBar.getButtons();
        double buttonMinWidth = buttonBar.getButtonMinWidth();
        Map<String, List<Node>> buttonMap = buildButtonMap(buttons);
        char[] buttonOrderArr = buttonOrder.toCharArray();
        int buttonIndex = 0;
        Spacer spacer = Spacer.NONE;
        for (char type : buttonOrderArr) {
            boolean edgeCase = buttonIndex <= 0 && buttonIndex >= buttons.size() - 1;
            boolean hasChildren = !this.layout.getChildren().isEmpty();
            if (type == '+') {
                spacer = spacer.replace(Spacer.DYNAMIC);
            } else if (type == '_' && hasChildren) {
                spacer = spacer.replace(Spacer.FIXED);
            } else {
                List<Node> buttonList = buttonMap.get(String.valueOf(type).toUpperCase());
                if (buttonList != null) {
                    spacer.add(this.layout, edgeCase);
                    for (Node btn : buttonList) {
                        sizeButton(btn, buttonMinWidth, Double.MAX_VALUE, Double.MAX_VALUE);
                        this.layout.getChildren().add(btn);
                        HBox.setHgrow(btn, Priority.NEVER);
                        buttonIndex++;
                    }
                    spacer = spacer.replace(Spacer.NONE);
                }
            }
        }
        boolean isDefaultSet = false;
        int childrenCount = buttons.size();
        int i2 = 0;
        while (true) {
            if (i2 >= childrenCount) {
                break;
            }
            Node btn2 = buttons.get(i2);
            if (!(btn2 instanceof Button) || !((Button) btn2).isDefaultButton()) {
                i2++;
            } else {
                btn2.requestFocus();
                isDefaultSet = true;
                break;
            }
        }
        if (!isDefaultSet) {
            for (int i3 = 0; i3 < childrenCount; i3++) {
                Node btn3 = buttons.get(i3);
                ButtonBar.ButtonData btnData = ButtonBar.getButtonData(btn3);
                if (btnData != null && btnData.isDefaultButton()) {
                    btn3.requestFocus();
                    return;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public void resizeButtons() {
        ButtonBar buttonBar = (ButtonBar) getSkinnable();
        double buttonMinWidth = buttonBar.getButtonMinWidth();
        List<? extends Node> buttons = buttonBar.getButtons();
        double widest = buttonMinWidth;
        for (Node button : buttons) {
            if (ButtonBar.isButtonUniformSize(button)) {
                widest = Math.max(button.prefWidth(-1.0d), widest);
            }
        }
        for (Node button2 : buttons) {
            if (ButtonBar.isButtonUniformSize(button2)) {
                sizeButton(button2, Double.MAX_VALUE, widest, Double.MAX_VALUE);
            }
        }
    }

    private void sizeButton(Node btn, double min, double pref, double max) {
        if (btn instanceof Region) {
            Region regionBtn = (Region) btn;
            if (min != Double.MAX_VALUE) {
                regionBtn.setMinWidth(min);
            }
            if (pref != Double.MAX_VALUE) {
                regionBtn.setPrefWidth(pref);
            }
            if (max != Double.MAX_VALUE) {
                regionBtn.setMaxWidth(max);
            }
        }
    }

    private String getButtonType(Node btn) {
        ButtonBar.ButtonData buttonType = ButtonBar.getButtonData(btn);
        if (buttonType == null) {
            buttonType = ButtonBar.ButtonData.OTHER;
        }
        String typeCode = buttonType.getTypeCode();
        String typeCode2 = typeCode.length() > 0 ? typeCode.substring(0, 1) : "";
        return CATEGORIZED_TYPES.contains(typeCode2.toUpperCase()) ? typeCode2 : ButtonBar.ButtonData.OTHER.getTypeCode();
    }

    private Map<String, List<Node>> buildButtonMap(List<? extends Node> buttons) {
        Map<String, List<Node>> buttonMap = new HashMap<>();
        for (Node btn : buttons) {
            if (btn != null) {
                String type = getButtonType(btn);
                List<Node> typedButtons = buttonMap.get(type);
                if (typedButtons == null) {
                    typedButtons = new ArrayList<>();
                    buttonMap.put(type, typedButtons);
                }
                typedButtons.add(btn);
            }
        }
        return buttonMap;
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/ButtonBarSkin$Spacer.class */
    private enum Spacer {
        FIXED { // from class: com.sun.javafx.scene.control.skin.ButtonBarSkin.Spacer.1
            @Override // com.sun.javafx.scene.control.skin.ButtonBarSkin.Spacer
            protected Node create(boolean edgeCase) {
                if (edgeCase) {
                    return null;
                }
                Region spacer = new Region();
                ButtonBar.setButtonData(spacer, ButtonBar.ButtonData.SMALL_GAP);
                spacer.setMinWidth(10.0d);
                HBox.setHgrow(spacer, Priority.NEVER);
                return spacer;
            }
        },
        DYNAMIC { // from class: com.sun.javafx.scene.control.skin.ButtonBarSkin.Spacer.2
            @Override // com.sun.javafx.scene.control.skin.ButtonBarSkin.Spacer
            protected Node create(boolean edgeCase) {
                Region spacer = new Region();
                ButtonBar.setButtonData(spacer, ButtonBar.ButtonData.BIG_GAP);
                spacer.setMinWidth(edgeCase ? 0.0d : 10.0d);
                HBox.setHgrow(spacer, Priority.ALWAYS);
                return spacer;
            }

            @Override // com.sun.javafx.scene.control.skin.ButtonBarSkin.Spacer
            public Spacer replace(Spacer spacer) {
                return FIXED == spacer ? this : spacer;
            }
        },
        NONE;

        protected Node create(boolean edgeCase) {
            return null;
        }

        public Spacer replace(Spacer spacer) {
            return spacer;
        }

        public void add(Pane pane, boolean edgeCase) {
            Node spacer = create(edgeCase);
            if (spacer != null) {
                pane.getChildren().add(spacer);
            }
        }
    }
}
