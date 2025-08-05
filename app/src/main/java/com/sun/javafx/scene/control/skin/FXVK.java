package com.sun.javafx.scene.control.skin;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.geometry.NodeOrientation;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.input.KeyEvent;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/FXVK.class */
public class FXVK extends Control {
    private final ObjectProperty<EventHandler<KeyEvent>> onAction = new SimpleObjectProperty(this, "onAction");
    static final String[] VK_TYPE_NAMES = {"text", "numeric", "url", "email"};
    public static final String VK_TYPE_PROP_KEY = "vkType";
    String[] chars;
    private ObjectProperty<Node> attachedNode;
    static FXVK vk;
    private static final String DEFAULT_STYLE_CLASS = "fxvk";

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/FXVK$Type.class */
    public enum Type {
        TEXT,
        NUMERIC,
        EMAIL
    }

    public final void setOnAction(EventHandler<KeyEvent> value) {
        this.onAction.set(value);
    }

    public final EventHandler<KeyEvent> getOnAction() {
        return this.onAction.get();
    }

    public final ObjectProperty<EventHandler<KeyEvent>> onActionProperty() {
        return this.onAction;
    }

    public FXVK() {
        setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        getStyleClass().add(DEFAULT_STYLE_CLASS);
    }

    final ObjectProperty<Node> attachedNodeProperty() {
        if (this.attachedNode == null) {
            this.attachedNode = new ObjectPropertyBase<Node>() { // from class: com.sun.javafx.scene.control.skin.FXVK.1
                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return FXVK.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "attachedNode";
                }
            };
        }
        return this.attachedNode;
    }

    final void setAttachedNode(Node value) {
        attachedNodeProperty().setValue(value);
    }

    final Node getAttachedNode() {
        if (this.attachedNode == null) {
            return null;
        }
        return this.attachedNode.getValue2();
    }

    public static void init(Node textInput) {
        if (vk != null) {
            return;
        }
        vk = new FXVK();
        FXVKSkin vkskin = new FXVKSkin(vk);
        vk.setSkin(vkskin);
        vkskin.prerender(textInput);
    }

    public static void attach(Node textInput) {
        if (vk == null) {
            vk = new FXVK();
            vk.setSkin(new FXVKSkin(vk));
        }
        vk.setAttachedNode(textInput);
    }

    public static void detach() {
        if (vk != null) {
            vk.setAttachedNode(null);
        }
    }

    @Override // javafx.scene.control.Control
    protected Skin<?> createDefaultSkin() {
        return new FXVKSkin(this);
    }
}
