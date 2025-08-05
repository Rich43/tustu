package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.TwoLevelFocusPopupBehavior;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.AccessibleAttribute;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.Skin;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import javafx.stage.WindowEvent;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/ContextMenuSkin.class */
public class ContextMenuSkin implements Skin<ContextMenu> {
    private ContextMenu popupMenu;
    private final Region root;
    private TwoLevelFocusPopupBehavior tlFocus;
    private final EventHandler<KeyEvent> keyListener = new EventHandler<KeyEvent>() { // from class: com.sun.javafx.scene.control.skin.ContextMenuSkin.1
        @Override // javafx.event.EventHandler
        public void handle(KeyEvent event) {
            if (event.getEventType() == KeyEvent.KEY_PRESSED && ContextMenuSkin.this.root.isFocused()) {
                KeyCode code = event.getCode();
                switch (AnonymousClass5.$SwitchMap$javafx$scene$input$KeyCode[code.ordinal()]) {
                    case 1:
                    case 2:
                        ContextMenuSkin.this.popupMenu.hide();
                        break;
                }
            }
        }
    };

    /* renamed from: com.sun.javafx.scene.control.skin.ContextMenuSkin$5, reason: invalid class name */
    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/ContextMenuSkin$5.class */
    static /* synthetic */ class AnonymousClass5 {
        static final /* synthetic */ int[] $SwitchMap$javafx$scene$input$KeyCode = new int[KeyCode.values().length];

        static {
            try {
                $SwitchMap$javafx$scene$input$KeyCode[KeyCode.ENTER.ordinal()] = 1;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$javafx$scene$input$KeyCode[KeyCode.SPACE.ordinal()] = 2;
            } catch (NoSuchFieldError e3) {
            }
        }
    }

    public ContextMenuSkin(final ContextMenu popupMenu) {
        this.popupMenu = popupMenu;
        popupMenu.addEventHandler(Menu.ON_SHOWN, new EventHandler<Event>() { // from class: com.sun.javafx.scene.control.skin.ContextMenuSkin.2
            @Override // javafx.event.EventHandler
            public void handle(Event event) {
                Node cmContent = popupMenu.getSkin().getNode();
                if (cmContent != null) {
                    cmContent.requestFocus();
                    if (cmContent instanceof ContextMenuContent) {
                        Node accMenu = ((ContextMenuContent) cmContent).getItemsContainer();
                        accMenu.notifyAccessibleAttributeChanged(AccessibleAttribute.VISIBLE);
                    }
                }
                ContextMenuSkin.this.root.addEventHandler(KeyEvent.KEY_PRESSED, ContextMenuSkin.this.keyListener);
            }
        });
        popupMenu.addEventHandler(Menu.ON_HIDDEN, new EventHandler<Event>() { // from class: com.sun.javafx.scene.control.skin.ContextMenuSkin.3
            @Override // javafx.event.EventHandler
            public void handle(Event event) {
                Node cmContent = popupMenu.getSkin().getNode();
                if (cmContent != null) {
                    cmContent.requestFocus();
                }
                ContextMenuSkin.this.root.removeEventHandler(KeyEvent.KEY_PRESSED, ContextMenuSkin.this.keyListener);
            }
        });
        popupMenu.addEventFilter(WindowEvent.WINDOW_HIDING, new EventHandler<Event>() { // from class: com.sun.javafx.scene.control.skin.ContextMenuSkin.4
            @Override // javafx.event.EventHandler
            public void handle(Event event) {
                Node cmContent = popupMenu.getSkin().getNode();
                if (cmContent instanceof ContextMenuContent) {
                    Node accMenu = ((ContextMenuContent) cmContent).getItemsContainer();
                    accMenu.notifyAccessibleAttributeChanged(AccessibleAttribute.VISIBLE);
                }
            }
        });
        if (BehaviorSkinBase.IS_TOUCH_SUPPORTED && popupMenu.getStyleClass().contains("text-input-context-menu")) {
            this.root = new EmbeddedTextContextMenuContent(popupMenu);
        } else {
            this.root = new ContextMenuContent(popupMenu);
        }
        this.root.idProperty().bind(popupMenu.idProperty());
        this.root.styleProperty().bind(popupMenu.styleProperty());
        this.root.getStyleClass().addAll(popupMenu.getStyleClass());
        if (Utils.isTwoLevelFocus()) {
            this.tlFocus = new TwoLevelFocusPopupBehavior(popupMenu);
        }
    }

    @Override // javafx.scene.control.Skin
    public ContextMenu getSkinnable() {
        return this.popupMenu;
    }

    @Override // javafx.scene.control.Skin
    public Node getNode() {
        return this.root;
    }

    @Override // javafx.scene.control.Skin
    public void dispose() {
        this.root.idProperty().unbind();
        this.root.styleProperty().unbind();
        if (this.tlFocus != null) {
            this.tlFocus.dispose();
        }
    }
}
