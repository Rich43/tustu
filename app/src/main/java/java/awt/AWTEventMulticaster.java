package java.awt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.EventListener;

/* loaded from: rt.jar:java/awt/AWTEventMulticaster.class */
public class AWTEventMulticaster implements ComponentListener, ContainerListener, FocusListener, KeyListener, MouseListener, MouseMotionListener, WindowListener, WindowFocusListener, WindowStateListener, ActionListener, ItemListener, AdjustmentListener, TextListener, InputMethodListener, HierarchyListener, HierarchyBoundsListener, MouseWheelListener {

    /* renamed from: a, reason: collision with root package name */
    protected final EventListener f12359a;

    /* renamed from: b, reason: collision with root package name */
    protected final EventListener f12360b;

    protected AWTEventMulticaster(EventListener eventListener, EventListener eventListener2) {
        this.f12359a = eventListener;
        this.f12360b = eventListener2;
    }

    protected EventListener remove(EventListener eventListener) {
        if (eventListener == this.f12359a) {
            return this.f12360b;
        }
        if (eventListener == this.f12360b) {
            return this.f12359a;
        }
        EventListener eventListenerRemoveInternal = removeInternal(this.f12359a, eventListener);
        EventListener eventListenerRemoveInternal2 = removeInternal(this.f12360b, eventListener);
        if (eventListenerRemoveInternal == this.f12359a && eventListenerRemoveInternal2 == this.f12360b) {
            return this;
        }
        return addInternal(eventListenerRemoveInternal, eventListenerRemoveInternal2);
    }

    @Override // java.awt.event.ComponentListener
    public void componentResized(ComponentEvent componentEvent) {
        ((ComponentListener) this.f12359a).componentResized(componentEvent);
        ((ComponentListener) this.f12360b).componentResized(componentEvent);
    }

    @Override // java.awt.event.ComponentListener
    public void componentMoved(ComponentEvent componentEvent) {
        ((ComponentListener) this.f12359a).componentMoved(componentEvent);
        ((ComponentListener) this.f12360b).componentMoved(componentEvent);
    }

    @Override // java.awt.event.ComponentListener
    public void componentShown(ComponentEvent componentEvent) {
        ((ComponentListener) this.f12359a).componentShown(componentEvent);
        ((ComponentListener) this.f12360b).componentShown(componentEvent);
    }

    @Override // java.awt.event.ComponentListener
    public void componentHidden(ComponentEvent componentEvent) {
        ((ComponentListener) this.f12359a).componentHidden(componentEvent);
        ((ComponentListener) this.f12360b).componentHidden(componentEvent);
    }

    @Override // java.awt.event.ContainerListener
    public void componentAdded(ContainerEvent containerEvent) {
        ((ContainerListener) this.f12359a).componentAdded(containerEvent);
        ((ContainerListener) this.f12360b).componentAdded(containerEvent);
    }

    @Override // java.awt.event.ContainerListener
    public void componentRemoved(ContainerEvent containerEvent) {
        ((ContainerListener) this.f12359a).componentRemoved(containerEvent);
        ((ContainerListener) this.f12360b).componentRemoved(containerEvent);
    }

    @Override // java.awt.event.FocusListener
    public void focusGained(FocusEvent focusEvent) {
        ((FocusListener) this.f12359a).focusGained(focusEvent);
        ((FocusListener) this.f12360b).focusGained(focusEvent);
    }

    @Override // java.awt.event.FocusListener
    public void focusLost(FocusEvent focusEvent) {
        ((FocusListener) this.f12359a).focusLost(focusEvent);
        ((FocusListener) this.f12360b).focusLost(focusEvent);
    }

    @Override // java.awt.event.KeyListener
    public void keyTyped(KeyEvent keyEvent) {
        ((KeyListener) this.f12359a).keyTyped(keyEvent);
        ((KeyListener) this.f12360b).keyTyped(keyEvent);
    }

    @Override // java.awt.event.KeyListener
    public void keyPressed(KeyEvent keyEvent) {
        ((KeyListener) this.f12359a).keyPressed(keyEvent);
        ((KeyListener) this.f12360b).keyPressed(keyEvent);
    }

    @Override // java.awt.event.KeyListener
    public void keyReleased(KeyEvent keyEvent) {
        ((KeyListener) this.f12359a).keyReleased(keyEvent);
        ((KeyListener) this.f12360b).keyReleased(keyEvent);
    }

    @Override // java.awt.event.MouseListener
    public void mouseClicked(MouseEvent mouseEvent) {
        ((MouseListener) this.f12359a).mouseClicked(mouseEvent);
        ((MouseListener) this.f12360b).mouseClicked(mouseEvent);
    }

    @Override // java.awt.event.MouseListener
    public void mousePressed(MouseEvent mouseEvent) {
        ((MouseListener) this.f12359a).mousePressed(mouseEvent);
        ((MouseListener) this.f12360b).mousePressed(mouseEvent);
    }

    @Override // java.awt.event.MouseListener
    public void mouseReleased(MouseEvent mouseEvent) {
        ((MouseListener) this.f12359a).mouseReleased(mouseEvent);
        ((MouseListener) this.f12360b).mouseReleased(mouseEvent);
    }

    @Override // java.awt.event.MouseListener
    public void mouseEntered(MouseEvent mouseEvent) {
        ((MouseListener) this.f12359a).mouseEntered(mouseEvent);
        ((MouseListener) this.f12360b).mouseEntered(mouseEvent);
    }

    @Override // java.awt.event.MouseListener
    public void mouseExited(MouseEvent mouseEvent) {
        ((MouseListener) this.f12359a).mouseExited(mouseEvent);
        ((MouseListener) this.f12360b).mouseExited(mouseEvent);
    }

    @Override // java.awt.event.MouseMotionListener
    public void mouseDragged(MouseEvent mouseEvent) {
        ((MouseMotionListener) this.f12359a).mouseDragged(mouseEvent);
        ((MouseMotionListener) this.f12360b).mouseDragged(mouseEvent);
    }

    @Override // java.awt.event.MouseMotionListener
    public void mouseMoved(MouseEvent mouseEvent) {
        ((MouseMotionListener) this.f12359a).mouseMoved(mouseEvent);
        ((MouseMotionListener) this.f12360b).mouseMoved(mouseEvent);
    }

    @Override // java.awt.event.WindowListener
    public void windowOpened(WindowEvent windowEvent) {
        ((WindowListener) this.f12359a).windowOpened(windowEvent);
        ((WindowListener) this.f12360b).windowOpened(windowEvent);
    }

    @Override // java.awt.event.WindowListener
    public void windowClosing(WindowEvent windowEvent) {
        ((WindowListener) this.f12359a).windowClosing(windowEvent);
        ((WindowListener) this.f12360b).windowClosing(windowEvent);
    }

    @Override // java.awt.event.WindowListener
    public void windowClosed(WindowEvent windowEvent) {
        ((WindowListener) this.f12359a).windowClosed(windowEvent);
        ((WindowListener) this.f12360b).windowClosed(windowEvent);
    }

    @Override // java.awt.event.WindowListener
    public void windowIconified(WindowEvent windowEvent) {
        ((WindowListener) this.f12359a).windowIconified(windowEvent);
        ((WindowListener) this.f12360b).windowIconified(windowEvent);
    }

    @Override // java.awt.event.WindowListener
    public void windowDeiconified(WindowEvent windowEvent) {
        ((WindowListener) this.f12359a).windowDeiconified(windowEvent);
        ((WindowListener) this.f12360b).windowDeiconified(windowEvent);
    }

    @Override // java.awt.event.WindowListener
    public void windowActivated(WindowEvent windowEvent) {
        ((WindowListener) this.f12359a).windowActivated(windowEvent);
        ((WindowListener) this.f12360b).windowActivated(windowEvent);
    }

    @Override // java.awt.event.WindowListener
    public void windowDeactivated(WindowEvent windowEvent) {
        ((WindowListener) this.f12359a).windowDeactivated(windowEvent);
        ((WindowListener) this.f12360b).windowDeactivated(windowEvent);
    }

    @Override // java.awt.event.WindowStateListener
    public void windowStateChanged(WindowEvent windowEvent) {
        ((WindowStateListener) this.f12359a).windowStateChanged(windowEvent);
        ((WindowStateListener) this.f12360b).windowStateChanged(windowEvent);
    }

    @Override // java.awt.event.WindowFocusListener
    public void windowGainedFocus(WindowEvent windowEvent) {
        ((WindowFocusListener) this.f12359a).windowGainedFocus(windowEvent);
        ((WindowFocusListener) this.f12360b).windowGainedFocus(windowEvent);
    }

    @Override // java.awt.event.WindowFocusListener
    public void windowLostFocus(WindowEvent windowEvent) {
        ((WindowFocusListener) this.f12359a).windowLostFocus(windowEvent);
        ((WindowFocusListener) this.f12360b).windowLostFocus(windowEvent);
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        ((ActionListener) this.f12359a).actionPerformed(actionEvent);
        ((ActionListener) this.f12360b).actionPerformed(actionEvent);
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        ((ItemListener) this.f12359a).itemStateChanged(itemEvent);
        ((ItemListener) this.f12360b).itemStateChanged(itemEvent);
    }

    @Override // java.awt.event.AdjustmentListener
    public void adjustmentValueChanged(AdjustmentEvent adjustmentEvent) {
        ((AdjustmentListener) this.f12359a).adjustmentValueChanged(adjustmentEvent);
        ((AdjustmentListener) this.f12360b).adjustmentValueChanged(adjustmentEvent);
    }

    @Override // java.awt.event.TextListener
    public void textValueChanged(TextEvent textEvent) {
        ((TextListener) this.f12359a).textValueChanged(textEvent);
        ((TextListener) this.f12360b).textValueChanged(textEvent);
    }

    @Override // java.awt.event.InputMethodListener
    public void inputMethodTextChanged(InputMethodEvent inputMethodEvent) {
        ((InputMethodListener) this.f12359a).inputMethodTextChanged(inputMethodEvent);
        ((InputMethodListener) this.f12360b).inputMethodTextChanged(inputMethodEvent);
    }

    @Override // java.awt.event.InputMethodListener
    public void caretPositionChanged(InputMethodEvent inputMethodEvent) {
        ((InputMethodListener) this.f12359a).caretPositionChanged(inputMethodEvent);
        ((InputMethodListener) this.f12360b).caretPositionChanged(inputMethodEvent);
    }

    @Override // java.awt.event.HierarchyListener
    public void hierarchyChanged(HierarchyEvent hierarchyEvent) {
        ((HierarchyListener) this.f12359a).hierarchyChanged(hierarchyEvent);
        ((HierarchyListener) this.f12360b).hierarchyChanged(hierarchyEvent);
    }

    @Override // java.awt.event.HierarchyBoundsListener
    public void ancestorMoved(HierarchyEvent hierarchyEvent) {
        ((HierarchyBoundsListener) this.f12359a).ancestorMoved(hierarchyEvent);
        ((HierarchyBoundsListener) this.f12360b).ancestorMoved(hierarchyEvent);
    }

    @Override // java.awt.event.HierarchyBoundsListener
    public void ancestorResized(HierarchyEvent hierarchyEvent) {
        ((HierarchyBoundsListener) this.f12359a).ancestorResized(hierarchyEvent);
        ((HierarchyBoundsListener) this.f12360b).ancestorResized(hierarchyEvent);
    }

    @Override // java.awt.event.MouseWheelListener
    public void mouseWheelMoved(MouseWheelEvent mouseWheelEvent) {
        ((MouseWheelListener) this.f12359a).mouseWheelMoved(mouseWheelEvent);
        ((MouseWheelListener) this.f12360b).mouseWheelMoved(mouseWheelEvent);
    }

    public static ComponentListener add(ComponentListener componentListener, ComponentListener componentListener2) {
        return (ComponentListener) addInternal(componentListener, componentListener2);
    }

    public static ContainerListener add(ContainerListener containerListener, ContainerListener containerListener2) {
        return (ContainerListener) addInternal(containerListener, containerListener2);
    }

    public static FocusListener add(FocusListener focusListener, FocusListener focusListener2) {
        return (FocusListener) addInternal(focusListener, focusListener2);
    }

    public static KeyListener add(KeyListener keyListener, KeyListener keyListener2) {
        return (KeyListener) addInternal(keyListener, keyListener2);
    }

    public static MouseListener add(MouseListener mouseListener, MouseListener mouseListener2) {
        return (MouseListener) addInternal(mouseListener, mouseListener2);
    }

    public static MouseMotionListener add(MouseMotionListener mouseMotionListener, MouseMotionListener mouseMotionListener2) {
        return (MouseMotionListener) addInternal(mouseMotionListener, mouseMotionListener2);
    }

    public static WindowListener add(WindowListener windowListener, WindowListener windowListener2) {
        return (WindowListener) addInternal(windowListener, windowListener2);
    }

    public static WindowStateListener add(WindowStateListener windowStateListener, WindowStateListener windowStateListener2) {
        return (WindowStateListener) addInternal(windowStateListener, windowStateListener2);
    }

    public static WindowFocusListener add(WindowFocusListener windowFocusListener, WindowFocusListener windowFocusListener2) {
        return (WindowFocusListener) addInternal(windowFocusListener, windowFocusListener2);
    }

    public static ActionListener add(ActionListener actionListener, ActionListener actionListener2) {
        return (ActionListener) addInternal(actionListener, actionListener2);
    }

    public static ItemListener add(ItemListener itemListener, ItemListener itemListener2) {
        return (ItemListener) addInternal(itemListener, itemListener2);
    }

    public static AdjustmentListener add(AdjustmentListener adjustmentListener, AdjustmentListener adjustmentListener2) {
        return (AdjustmentListener) addInternal(adjustmentListener, adjustmentListener2);
    }

    public static TextListener add(TextListener textListener, TextListener textListener2) {
        return (TextListener) addInternal(textListener, textListener2);
    }

    public static InputMethodListener add(InputMethodListener inputMethodListener, InputMethodListener inputMethodListener2) {
        return (InputMethodListener) addInternal(inputMethodListener, inputMethodListener2);
    }

    public static HierarchyListener add(HierarchyListener hierarchyListener, HierarchyListener hierarchyListener2) {
        return (HierarchyListener) addInternal(hierarchyListener, hierarchyListener2);
    }

    public static HierarchyBoundsListener add(HierarchyBoundsListener hierarchyBoundsListener, HierarchyBoundsListener hierarchyBoundsListener2) {
        return (HierarchyBoundsListener) addInternal(hierarchyBoundsListener, hierarchyBoundsListener2);
    }

    public static MouseWheelListener add(MouseWheelListener mouseWheelListener, MouseWheelListener mouseWheelListener2) {
        return (MouseWheelListener) addInternal(mouseWheelListener, mouseWheelListener2);
    }

    public static ComponentListener remove(ComponentListener componentListener, ComponentListener componentListener2) {
        return (ComponentListener) removeInternal(componentListener, componentListener2);
    }

    public static ContainerListener remove(ContainerListener containerListener, ContainerListener containerListener2) {
        return (ContainerListener) removeInternal(containerListener, containerListener2);
    }

    public static FocusListener remove(FocusListener focusListener, FocusListener focusListener2) {
        return (FocusListener) removeInternal(focusListener, focusListener2);
    }

    public static KeyListener remove(KeyListener keyListener, KeyListener keyListener2) {
        return (KeyListener) removeInternal(keyListener, keyListener2);
    }

    public static MouseListener remove(MouseListener mouseListener, MouseListener mouseListener2) {
        return (MouseListener) removeInternal(mouseListener, mouseListener2);
    }

    public static MouseMotionListener remove(MouseMotionListener mouseMotionListener, MouseMotionListener mouseMotionListener2) {
        return (MouseMotionListener) removeInternal(mouseMotionListener, mouseMotionListener2);
    }

    public static WindowListener remove(WindowListener windowListener, WindowListener windowListener2) {
        return (WindowListener) removeInternal(windowListener, windowListener2);
    }

    public static WindowStateListener remove(WindowStateListener windowStateListener, WindowStateListener windowStateListener2) {
        return (WindowStateListener) removeInternal(windowStateListener, windowStateListener2);
    }

    public static WindowFocusListener remove(WindowFocusListener windowFocusListener, WindowFocusListener windowFocusListener2) {
        return (WindowFocusListener) removeInternal(windowFocusListener, windowFocusListener2);
    }

    public static ActionListener remove(ActionListener actionListener, ActionListener actionListener2) {
        return (ActionListener) removeInternal(actionListener, actionListener2);
    }

    public static ItemListener remove(ItemListener itemListener, ItemListener itemListener2) {
        return (ItemListener) removeInternal(itemListener, itemListener2);
    }

    public static AdjustmentListener remove(AdjustmentListener adjustmentListener, AdjustmentListener adjustmentListener2) {
        return (AdjustmentListener) removeInternal(adjustmentListener, adjustmentListener2);
    }

    public static TextListener remove(TextListener textListener, TextListener textListener2) {
        return (TextListener) removeInternal(textListener, textListener2);
    }

    public static InputMethodListener remove(InputMethodListener inputMethodListener, InputMethodListener inputMethodListener2) {
        return (InputMethodListener) removeInternal(inputMethodListener, inputMethodListener2);
    }

    public static HierarchyListener remove(HierarchyListener hierarchyListener, HierarchyListener hierarchyListener2) {
        return (HierarchyListener) removeInternal(hierarchyListener, hierarchyListener2);
    }

    public static HierarchyBoundsListener remove(HierarchyBoundsListener hierarchyBoundsListener, HierarchyBoundsListener hierarchyBoundsListener2) {
        return (HierarchyBoundsListener) removeInternal(hierarchyBoundsListener, hierarchyBoundsListener2);
    }

    public static MouseWheelListener remove(MouseWheelListener mouseWheelListener, MouseWheelListener mouseWheelListener2) {
        return (MouseWheelListener) removeInternal(mouseWheelListener, mouseWheelListener2);
    }

    protected static EventListener addInternal(EventListener eventListener, EventListener eventListener2) {
        return eventListener == null ? eventListener2 : eventListener2 == null ? eventListener : new AWTEventMulticaster(eventListener, eventListener2);
    }

    protected static EventListener removeInternal(EventListener eventListener, EventListener eventListener2) {
        if (eventListener == eventListener2 || eventListener == null) {
            return null;
        }
        if (eventListener instanceof AWTEventMulticaster) {
            return ((AWTEventMulticaster) eventListener).remove(eventListener2);
        }
        return eventListener;
    }

    protected void saveInternal(ObjectOutputStream objectOutputStream, String str) throws IOException {
        if (this.f12359a instanceof AWTEventMulticaster) {
            ((AWTEventMulticaster) this.f12359a).saveInternal(objectOutputStream, str);
        } else if (this.f12359a instanceof Serializable) {
            objectOutputStream.writeObject(str);
            objectOutputStream.writeObject(this.f12359a);
        }
        if (this.f12360b instanceof AWTEventMulticaster) {
            ((AWTEventMulticaster) this.f12360b).saveInternal(objectOutputStream, str);
        } else if (this.f12360b instanceof Serializable) {
            objectOutputStream.writeObject(str);
            objectOutputStream.writeObject(this.f12360b);
        }
    }

    protected static void save(ObjectOutputStream objectOutputStream, String str, EventListener eventListener) throws IOException {
        if (eventListener == null) {
            return;
        }
        if (eventListener instanceof AWTEventMulticaster) {
            ((AWTEventMulticaster) eventListener).saveInternal(objectOutputStream, str);
        } else if (eventListener instanceof Serializable) {
            objectOutputStream.writeObject(str);
            objectOutputStream.writeObject(eventListener);
        }
    }

    private static int getListenerCount(EventListener eventListener, Class<?> cls) {
        if (!(eventListener instanceof AWTEventMulticaster)) {
            return cls.isInstance(eventListener) ? 1 : 0;
        }
        AWTEventMulticaster aWTEventMulticaster = (AWTEventMulticaster) eventListener;
        return getListenerCount(aWTEventMulticaster.f12359a, cls) + getListenerCount(aWTEventMulticaster.f12360b, cls);
    }

    private static int populateListenerArray(EventListener[] eventListenerArr, EventListener eventListener, int i2) {
        if (eventListener instanceof AWTEventMulticaster) {
            AWTEventMulticaster aWTEventMulticaster = (AWTEventMulticaster) eventListener;
            return populateListenerArray(eventListenerArr, aWTEventMulticaster.f12360b, populateListenerArray(eventListenerArr, aWTEventMulticaster.f12359a, i2));
        }
        if (eventListenerArr.getClass().getComponentType().isInstance(eventListener)) {
            eventListenerArr[i2] = eventListener;
            return i2 + 1;
        }
        return i2;
    }

    public static <T extends EventListener> T[] getListeners(EventListener eventListener, Class<T> cls) {
        if (cls == null) {
            throw new NullPointerException("Listener type should not be null");
        }
        T[] tArr = (T[]) ((EventListener[]) Array.newInstance((Class<?>) cls, getListenerCount(eventListener, cls)));
        populateListenerArray(tArr, eventListener, 0);
        return tArr;
    }
}
