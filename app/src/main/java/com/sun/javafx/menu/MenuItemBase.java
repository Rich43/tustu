package com.sun.javafx.menu;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.KeyCombination;

/* loaded from: jfxrt.jar:com/sun/javafx/menu/MenuItemBase.class */
public interface MenuItemBase {
    void setId(String str);

    String getId();

    StringProperty idProperty();

    void setText(String str);

    String getText();

    StringProperty textProperty();

    void setGraphic(Node node);

    Node getGraphic();

    ObjectProperty<Node> graphicProperty();

    void setOnAction(EventHandler<ActionEvent> eventHandler);

    EventHandler<ActionEvent> getOnAction();

    ObjectProperty<EventHandler<ActionEvent>> onActionProperty();

    void setDisable(boolean z2);

    boolean isDisable();

    BooleanProperty disableProperty();

    void setVisible(boolean z2);

    boolean isVisible();

    BooleanProperty visibleProperty();

    void setAccelerator(KeyCombination keyCombination);

    KeyCombination getAccelerator();

    ObjectProperty<KeyCombination> acceleratorProperty();

    void setMnemonicParsing(boolean z2);

    boolean isMnemonicParsing();

    BooleanProperty mnemonicParsingProperty();

    void fire();

    void fireValidation();
}
