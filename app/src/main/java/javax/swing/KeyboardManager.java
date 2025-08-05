package javax.swing;

import java.applet.Applet;
import java.awt.Container;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import sun.awt.EmbeddedFrame;

/* loaded from: rt.jar:javax/swing/KeyboardManager.class */
class KeyboardManager {
    static KeyboardManager currentManager = new KeyboardManager();
    Hashtable<Container, Hashtable> containerMap = new Hashtable<>();
    Hashtable<ComponentKeyStrokePair, Container> componentKeyStrokeMap = new Hashtable<>();

    KeyboardManager() {
    }

    public static KeyboardManager getCurrentManager() {
        return currentManager;
    }

    public static void setCurrentManager(KeyboardManager keyboardManager) {
        currentManager = keyboardManager;
    }

    public void registerKeyStroke(KeyStroke keyStroke, JComponent jComponent) {
        Container topAncestor = getTopAncestor(jComponent);
        if (topAncestor == null) {
            return;
        }
        Hashtable hashtableRegisterNewTopContainer = this.containerMap.get(topAncestor);
        if (hashtableRegisterNewTopContainer == null) {
            hashtableRegisterNewTopContainer = registerNewTopContainer(topAncestor);
        }
        Object obj = hashtableRegisterNewTopContainer.get(keyStroke);
        if (obj == null) {
            hashtableRegisterNewTopContainer.put(keyStroke, jComponent);
        } else if (obj instanceof Vector) {
            Vector vector = (Vector) obj;
            if (!vector.contains(jComponent)) {
                vector.addElement(jComponent);
            }
        } else if (obj instanceof JComponent) {
            if (obj != jComponent) {
                Vector vector2 = new Vector();
                vector2.addElement((JComponent) obj);
                vector2.addElement(jComponent);
                hashtableRegisterNewTopContainer.put(keyStroke, vector2);
            }
        } else {
            System.out.println("Unexpected condition in registerKeyStroke");
            Thread.dumpStack();
        }
        this.componentKeyStrokeMap.put(new ComponentKeyStrokePair(jComponent, keyStroke), topAncestor);
        if (topAncestor instanceof EmbeddedFrame) {
            ((EmbeddedFrame) topAncestor).registerAccelerator(keyStroke);
        }
    }

    private static Container getTopAncestor(JComponent jComponent) {
        Container container;
        Container parent = jComponent.getParent();
        while (true) {
            container = parent;
            if (container != null) {
                if (((container instanceof Window) && ((Window) container).isFocusableWindow()) || (container instanceof Applet) || (container instanceof JInternalFrame)) {
                    break;
                }
                parent = container.getParent();
            } else {
                return null;
            }
        }
        return container;
    }

    public void unregisterKeyStroke(KeyStroke keyStroke, JComponent jComponent) {
        ComponentKeyStrokePair componentKeyStrokePair = new ComponentKeyStrokePair(jComponent, keyStroke);
        Container container = this.componentKeyStrokeMap.get(componentKeyStrokePair);
        if (container == null) {
            return;
        }
        Hashtable hashtable = this.containerMap.get(container);
        if (hashtable == null) {
            Thread.dumpStack();
            return;
        }
        Object obj = hashtable.get(keyStroke);
        if (obj == null) {
            Thread.dumpStack();
            return;
        }
        if ((obj instanceof JComponent) && obj == jComponent) {
            hashtable.remove(keyStroke);
        } else if (obj instanceof Vector) {
            Vector vector = (Vector) obj;
            vector.removeElement(jComponent);
            if (vector.isEmpty()) {
                hashtable.remove(keyStroke);
            }
        }
        if (hashtable.isEmpty()) {
            this.containerMap.remove(container);
        }
        this.componentKeyStrokeMap.remove(componentKeyStrokePair);
        if (container instanceof EmbeddedFrame) {
            ((EmbeddedFrame) container).unregisterAccelerator(keyStroke);
        }
    }

    public boolean fireKeyboardAction(KeyEvent keyEvent, boolean z2, Container container) {
        KeyStroke keyStroke;
        Vector vector;
        if (keyEvent.isConsumed()) {
            System.out.println("Acquired pre-used event!");
            Thread.dumpStack();
        }
        KeyStroke keyStroke2 = null;
        if (keyEvent.getID() == 400) {
            keyStroke = KeyStroke.getKeyStroke(keyEvent.getKeyChar());
        } else {
            if (keyEvent.getKeyCode() != keyEvent.getExtendedKeyCode()) {
                keyStroke2 = KeyStroke.getKeyStroke(keyEvent.getExtendedKeyCode(), keyEvent.getModifiers(), !z2);
            }
            keyStroke = KeyStroke.getKeyStroke(keyEvent.getKeyCode(), keyEvent.getModifiers(), !z2);
        }
        Hashtable hashtable = this.containerMap.get(container);
        if (hashtable != null) {
            Object obj = null;
            if (keyStroke2 != null) {
                obj = hashtable.get(keyStroke2);
                if (obj != null) {
                    keyStroke = keyStroke2;
                }
            }
            if (obj == null) {
                obj = hashtable.get(keyStroke);
            }
            if (obj != null) {
                if (obj instanceof JComponent) {
                    JComponent jComponent = (JComponent) obj;
                    if (jComponent.isShowing() && jComponent.isEnabled()) {
                        fireBinding(jComponent, keyStroke, keyEvent, z2);
                    }
                } else if (obj instanceof Vector) {
                    Vector vector2 = (Vector) obj;
                    for (int size = vector2.size() - 1; size >= 0; size--) {
                        JComponent jComponent2 = (JComponent) vector2.elementAt(size);
                        if (jComponent2.isShowing() && jComponent2.isEnabled()) {
                            fireBinding(jComponent2, keyStroke, keyEvent, z2);
                            if (keyEvent.isConsumed()) {
                                return true;
                            }
                        }
                    }
                } else {
                    System.out.println("Unexpected condition in fireKeyboardAction " + obj);
                    Thread.dumpStack();
                }
            }
        }
        if (keyEvent.isConsumed()) {
            return true;
        }
        if (hashtable != null && (vector = (Vector) hashtable.get(JMenuBar.class)) != null) {
            Enumeration enumerationElements = vector.elements();
            while (enumerationElements.hasMoreElements()) {
                JMenuBar jMenuBar = (JMenuBar) enumerationElements.nextElement2();
                if (jMenuBar.isShowing() && jMenuBar.isEnabled()) {
                    boolean z3 = (keyStroke2 == null || keyStroke2.equals(keyStroke)) ? false : true;
                    if (z3) {
                        fireBinding(jMenuBar, keyStroke2, keyEvent, z2);
                    }
                    if (!z3 || !keyEvent.isConsumed()) {
                        fireBinding(jMenuBar, keyStroke, keyEvent, z2);
                    }
                    if (keyEvent.isConsumed()) {
                        return true;
                    }
                }
            }
        }
        return keyEvent.isConsumed();
    }

    void fireBinding(JComponent jComponent, KeyStroke keyStroke, KeyEvent keyEvent, boolean z2) {
        if (jComponent.processKeyBinding(keyStroke, keyEvent, 2, z2)) {
            keyEvent.consume();
        }
    }

    public void registerMenuBar(JMenuBar jMenuBar) {
        Container topAncestor = getTopAncestor(jMenuBar);
        if (topAncestor == null) {
            return;
        }
        Hashtable hashtableRegisterNewTopContainer = this.containerMap.get(topAncestor);
        if (hashtableRegisterNewTopContainer == null) {
            hashtableRegisterNewTopContainer = registerNewTopContainer(topAncestor);
        }
        Vector vector = (Vector) hashtableRegisterNewTopContainer.get(JMenuBar.class);
        if (vector == null) {
            vector = new Vector();
            hashtableRegisterNewTopContainer.put(JMenuBar.class, vector);
        }
        if (!vector.contains(jMenuBar)) {
            vector.addElement(jMenuBar);
        }
    }

    public void unregisterMenuBar(JMenuBar jMenuBar) {
        Hashtable hashtable;
        Vector vector;
        Container topAncestor = getTopAncestor(jMenuBar);
        if (topAncestor != null && (hashtable = this.containerMap.get(topAncestor)) != null && (vector = (Vector) hashtable.get(JMenuBar.class)) != null) {
            vector.removeElement(jMenuBar);
            if (vector.isEmpty()) {
                hashtable.remove(JMenuBar.class);
                if (hashtable.isEmpty()) {
                    this.containerMap.remove(topAncestor);
                }
            }
        }
    }

    protected Hashtable registerNewTopContainer(Container container) {
        Hashtable hashtable = new Hashtable();
        this.containerMap.put(container, hashtable);
        return hashtable;
    }

    /* loaded from: rt.jar:javax/swing/KeyboardManager$ComponentKeyStrokePair.class */
    class ComponentKeyStrokePair {
        Object component;
        Object keyStroke;

        public ComponentKeyStrokePair(Object obj, Object obj2) {
            this.component = obj;
            this.keyStroke = obj2;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof ComponentKeyStrokePair)) {
                return false;
            }
            ComponentKeyStrokePair componentKeyStrokePair = (ComponentKeyStrokePair) obj;
            return this.component.equals(componentKeyStrokePair.component) && this.keyStroke.equals(componentKeyStrokePair.keyStroke);
        }

        public int hashCode() {
            return this.component.hashCode() * this.keyStroke.hashCode();
        }
    }
}
