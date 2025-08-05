package javafx.scene.input;

import com.sun.javafx.robot.impl.FXRobotHelper;
import com.sun.javafx.scene.input.KeyCodeMap;
import com.sun.javafx.tk.Toolkit;
import javafx.beans.NamedArg;
import javafx.event.EventTarget;
import javafx.event.EventType;
import javafx.scene.input.ScrollEvent;

/* loaded from: jfxrt.jar:javafx/scene/input/KeyEvent.class */
public final class KeyEvent extends InputEvent {
    private static final long serialVersionUID = 20121107;
    public static final EventType<KeyEvent> ANY = new EventType<>(InputEvent.ANY, "KEY");
    public static final EventType<KeyEvent> KEY_PRESSED = new EventType<>(ANY, "KEY_PRESSED");
    public static final EventType<KeyEvent> KEY_RELEASED = new EventType<>(ANY, "KEY_RELEASED");
    public static final EventType<KeyEvent> KEY_TYPED = new EventType<>(ANY, "KEY_TYPED");
    public static final String CHAR_UNDEFINED;
    private final String character;
    private final String text;
    private final KeyCode code;
    private final boolean shiftDown;
    private final boolean controlDown;
    private final boolean altDown;
    private final boolean metaDown;

    static {
        FXRobotHelper.FXRobotInputAccessor a2 = new FXRobotHelper.FXRobotInputAccessor() { // from class: javafx.scene.input.KeyEvent.1
            @Override // com.sun.javafx.robot.impl.FXRobotHelper.FXRobotInputAccessor
            public int getCodeForKeyCode(KeyCode keyCode) {
                return keyCode.code;
            }

            @Override // com.sun.javafx.robot.impl.FXRobotHelper.FXRobotInputAccessor
            public KeyCode getKeyCodeForCode(int code) {
                return KeyCodeMap.valueOf(code);
            }

            @Override // com.sun.javafx.robot.impl.FXRobotHelper.FXRobotInputAccessor
            public KeyEvent createKeyEvent(EventType<? extends KeyEvent> eventType, KeyCode code, String character, String text, boolean shiftDown, boolean controlDown, boolean altDown, boolean metaDown) {
                return new KeyEvent(eventType, character, text, code, shiftDown, controlDown, altDown, metaDown);
            }

            @Override // com.sun.javafx.robot.impl.FXRobotHelper.FXRobotInputAccessor
            public MouseEvent createMouseEvent(EventType<? extends MouseEvent> eventType, int x2, int y2, int screenX, int screenY, MouseButton button, int clickCount, boolean shiftDown, boolean controlDown, boolean altDown, boolean metaDown, boolean popupTrigger, boolean primaryButtonDown, boolean middleButtonDown, boolean secondaryButtonDown) {
                return new MouseEvent(eventType, x2, y2, screenX, screenY, button, clickCount, shiftDown, controlDown, altDown, metaDown, primaryButtonDown, middleButtonDown, secondaryButtonDown, false, popupTrigger, false, null);
            }

            @Override // com.sun.javafx.robot.impl.FXRobotHelper.FXRobotInputAccessor
            public ScrollEvent createScrollEvent(EventType<? extends ScrollEvent> eventType, int scrollX, int scrollY, ScrollEvent.HorizontalTextScrollUnits xTextUnits, int xText, ScrollEvent.VerticalTextScrollUnits yTextUnits, int yText, int x2, int y2, int screenX, int screenY, boolean shiftDown, boolean controlDown, boolean altDown, boolean metaDown) {
                return new ScrollEvent(ScrollEvent.SCROLL, x2, y2, screenX, screenY, shiftDown, controlDown, altDown, metaDown, false, false, scrollX, scrollY, 0.0d, 0.0d, xTextUnits, xText, yTextUnits, yText, 0, null);
            }
        };
        FXRobotHelper.setInputAccessor(a2);
        CHAR_UNDEFINED = KeyCode.UNDEFINED.ch;
    }

    public KeyEvent(@NamedArg("source") Object source, @NamedArg("target") EventTarget target, @NamedArg("eventType") EventType<KeyEvent> eventType, @NamedArg("character") String character, @NamedArg("text") String text, @NamedArg("code") KeyCode code, @NamedArg("shiftDown") boolean shiftDown, @NamedArg("controlDown") boolean controlDown, @NamedArg("altDown") boolean altDown, @NamedArg("metaDown") boolean metaDown) {
        super(source, target, eventType);
        boolean isKeyTyped = eventType == KEY_TYPED;
        this.character = isKeyTyped ? character : CHAR_UNDEFINED;
        this.text = isKeyTyped ? "" : text;
        this.code = isKeyTyped ? KeyCode.UNDEFINED : code;
        this.shiftDown = shiftDown;
        this.controlDown = controlDown;
        this.altDown = altDown;
        this.metaDown = metaDown;
    }

    public KeyEvent(@NamedArg("eventType") EventType<KeyEvent> eventType, @NamedArg("character") String character, @NamedArg("text") String text, @NamedArg("code") KeyCode code, @NamedArg("shiftDown") boolean shiftDown, @NamedArg("controlDown") boolean controlDown, @NamedArg("altDown") boolean altDown, @NamedArg("metaDown") boolean metaDown) {
        super(eventType);
        boolean isKeyTyped = eventType == KEY_TYPED;
        this.character = isKeyTyped ? character : CHAR_UNDEFINED;
        this.text = isKeyTyped ? "" : text;
        this.code = isKeyTyped ? KeyCode.UNDEFINED : code;
        this.shiftDown = shiftDown;
        this.controlDown = controlDown;
        this.altDown = altDown;
        this.metaDown = metaDown;
    }

    public final String getCharacter() {
        return this.character;
    }

    public final String getText() {
        return this.text;
    }

    public final KeyCode getCode() {
        return this.code;
    }

    public final boolean isShiftDown() {
        return this.shiftDown;
    }

    public final boolean isControlDown() {
        return this.controlDown;
    }

    public final boolean isAltDown() {
        return this.altDown;
    }

    public final boolean isMetaDown() {
        return this.metaDown;
    }

    public final boolean isShortcutDown() {
        switch (Toolkit.getToolkit().getPlatformShortcutKey()) {
            case SHIFT:
                return this.shiftDown;
            case CONTROL:
                return this.controlDown;
            case ALT:
                return this.altDown;
            case META:
                return this.metaDown;
            default:
                return false;
        }
    }

    @Override // java.util.EventObject
    public String toString() {
        StringBuilder sb = new StringBuilder("KeyEvent [");
        sb.append("source = ").append(getSource());
        sb.append(", target = ").append((Object) getTarget());
        sb.append(", eventType = ").append((Object) getEventType());
        sb.append(", consumed = ").append(isConsumed());
        sb.append(", character = ").append(getCharacter());
        sb.append(", text = ").append(getText());
        sb.append(", code = ").append((Object) getCode());
        if (isShiftDown()) {
            sb.append(", shiftDown");
        }
        if (isControlDown()) {
            sb.append(", controlDown");
        }
        if (isAltDown()) {
            sb.append(", altDown");
        }
        if (isMetaDown()) {
            sb.append(", metaDown");
        }
        if (isShortcutDown()) {
            sb.append(", shortcutDown");
        }
        return sb.append("]").toString();
    }

    @Override // javafx.event.Event
    public KeyEvent copyFor(Object newSource, EventTarget newTarget) {
        return (KeyEvent) super.copyFor(newSource, newTarget);
    }

    public KeyEvent copyFor(Object source, EventTarget target, EventType<KeyEvent> type) {
        KeyEvent e2 = copyFor(source, target);
        e2.eventType = type;
        return e2;
    }

    @Override // javafx.scene.input.InputEvent, javafx.event.Event
    public EventType<KeyEvent> getEventType() {
        return super.getEventType();
    }
}
