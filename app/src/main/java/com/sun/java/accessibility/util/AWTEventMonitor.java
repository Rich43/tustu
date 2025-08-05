package com.sun.java.accessibility.util;

import com.sun.jmx.defaults.ServiceName;
import java.awt.AWTEventMulticaster;
import java.awt.Adjustable;
import java.awt.Component;
import java.awt.Container;
import java.awt.KeyboardFocusManager;
import java.awt.Window;
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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.JRootPane;
import javax.swing.MenuElement;
import javax.swing.MenuSelectionManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import jdk.Exported;
import sun.security.util.SecurityConstants;

@Exported
/* loaded from: jaccess.jar:com/sun/java/accessibility/util/AWTEventMonitor.class */
public class AWTEventMonitor {
    private static boolean runningOnJDK1_4 = false;

    @Deprecated
    protected static Component componentWithFocus = null;
    private static Component componentWithFocus_private = null;

    @Deprecated
    protected static ComponentListener componentListener = null;
    private static ComponentListener componentListener_private = null;

    @Deprecated
    protected static ContainerListener containerListener = null;
    private static ContainerListener containerListener_private = null;

    @Deprecated
    protected static FocusListener focusListener = null;
    private static FocusListener focusListener_private = null;

    @Deprecated
    protected static KeyListener keyListener = null;
    private static KeyListener keyListener_private = null;

    @Deprecated
    protected static MouseListener mouseListener = null;
    private static MouseListener mouseListener_private = null;

    @Deprecated
    protected static MouseMotionListener mouseMotionListener = null;
    private static MouseMotionListener mouseMotionListener_private = null;

    @Deprecated
    protected static WindowListener windowListener = null;
    private static WindowListener windowListener_private = null;

    @Deprecated
    protected static ActionListener actionListener = null;
    private static ActionListener actionListener_private = null;

    @Deprecated
    protected static AdjustmentListener adjustmentListener = null;
    private static AdjustmentListener adjustmentListener_private = null;

    @Deprecated
    protected static ItemListener itemListener = null;
    private static ItemListener itemListener_private = null;

    @Deprecated
    protected static TextListener textListener = null;
    private static TextListener textListener_private = null;

    @Deprecated
    protected static AWTEventsListener awtListener = new AWTEventsListener();
    private static final AWTEventsListener awtListener_private = new AWTEventsListener();

    public static Component getComponentWithFocus() {
        return componentWithFocus_private;
    }

    private static void checkInstallPermission() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(SecurityConstants.AWT.ALL_AWT_EVENTS_PERMISSION);
        }
    }

    public static void addComponentListener(ComponentListener componentListener2) throws IllegalArgumentException {
        if (componentListener_private == null) {
            checkInstallPermission();
            awtListener_private.installListeners(2);
        }
        componentListener_private = AWTEventMulticaster.add(componentListener_private, componentListener2);
    }

    public static void removeComponentListener(ComponentListener componentListener2) throws IllegalArgumentException {
        componentListener_private = AWTEventMulticaster.remove(componentListener_private, componentListener2);
        if (componentListener_private == null) {
            awtListener_private.removeListeners(2);
        }
    }

    public static void addContainerListener(ContainerListener containerListener2) {
        containerListener_private = AWTEventMulticaster.add(containerListener_private, containerListener2);
    }

    public static void removeContainerListener(ContainerListener containerListener2) {
        containerListener_private = AWTEventMulticaster.remove(containerListener_private, containerListener2);
    }

    public static void addFocusListener(FocusListener focusListener2) {
        focusListener_private = AWTEventMulticaster.add(focusListener_private, focusListener2);
    }

    public static void removeFocusListener(FocusListener focusListener2) {
        focusListener_private = AWTEventMulticaster.remove(focusListener_private, focusListener2);
    }

    public static void addKeyListener(KeyListener keyListener2) throws IllegalArgumentException {
        if (keyListener_private == null) {
            checkInstallPermission();
            awtListener_private.installListeners(6);
        }
        keyListener_private = AWTEventMulticaster.add(keyListener_private, keyListener2);
    }

    public static void removeKeyListener(KeyListener keyListener2) throws IllegalArgumentException {
        keyListener_private = AWTEventMulticaster.remove(keyListener_private, keyListener2);
        if (keyListener_private == null) {
            awtListener_private.removeListeners(6);
        }
    }

    public static void addMouseListener(MouseListener mouseListener2) throws IllegalArgumentException {
        if (mouseListener_private == null) {
            checkInstallPermission();
            awtListener_private.installListeners(7);
        }
        mouseListener_private = AWTEventMulticaster.add(mouseListener_private, mouseListener2);
    }

    public static void removeMouseListener(MouseListener mouseListener2) throws IllegalArgumentException {
        mouseListener_private = AWTEventMulticaster.remove(mouseListener_private, mouseListener2);
        if (mouseListener_private == null) {
            awtListener_private.removeListeners(7);
        }
    }

    public static void addMouseMotionListener(MouseMotionListener mouseMotionListener2) throws IllegalArgumentException {
        if (mouseMotionListener_private == null) {
            checkInstallPermission();
            awtListener_private.installListeners(8);
        }
        mouseMotionListener_private = AWTEventMulticaster.add(mouseMotionListener_private, mouseMotionListener2);
    }

    public static void removeMouseMotionListener(MouseMotionListener mouseMotionListener2) throws IllegalArgumentException {
        mouseMotionListener_private = AWTEventMulticaster.remove(mouseMotionListener_private, mouseMotionListener2);
        if (mouseMotionListener_private == null) {
            awtListener_private.removeListeners(8);
        }
    }

    public static void addWindowListener(WindowListener windowListener2) throws IllegalArgumentException {
        if (windowListener_private == null) {
            checkInstallPermission();
            awtListener_private.installListeners(11);
        }
        windowListener_private = AWTEventMulticaster.add(windowListener_private, windowListener2);
    }

    public static void removeWindowListener(WindowListener windowListener2) throws IllegalArgumentException {
        windowListener_private = AWTEventMulticaster.remove(windowListener_private, windowListener2);
        if (windowListener_private == null) {
            awtListener_private.removeListeners(11);
        }
    }

    public static void addActionListener(ActionListener actionListener2) throws IllegalArgumentException {
        if (actionListener_private == null) {
            checkInstallPermission();
            awtListener_private.installListeners(0);
        }
        actionListener_private = AWTEventMulticaster.add(actionListener_private, actionListener2);
    }

    public static void removeActionListener(ActionListener actionListener2) throws IllegalArgumentException {
        actionListener_private = AWTEventMulticaster.remove(actionListener_private, actionListener2);
        if (actionListener_private == null) {
            awtListener_private.removeListeners(0);
        }
    }

    public static void addAdjustmentListener(AdjustmentListener adjustmentListener2) throws IllegalArgumentException {
        if (adjustmentListener_private == null) {
            checkInstallPermission();
            awtListener_private.installListeners(1);
        }
        adjustmentListener_private = AWTEventMulticaster.add(adjustmentListener_private, adjustmentListener2);
    }

    public static void removeAdjustmentListener(AdjustmentListener adjustmentListener2) throws IllegalArgumentException {
        adjustmentListener_private = AWTEventMulticaster.remove(adjustmentListener_private, adjustmentListener2);
        if (adjustmentListener_private == null) {
            awtListener_private.removeListeners(1);
        }
    }

    public static void addItemListener(ItemListener itemListener2) throws IllegalArgumentException {
        if (itemListener_private == null) {
            checkInstallPermission();
            awtListener_private.installListeners(5);
        }
        itemListener_private = AWTEventMulticaster.add(itemListener_private, itemListener2);
    }

    public static void removeItemListener(ItemListener itemListener2) throws IllegalArgumentException {
        itemListener_private = AWTEventMulticaster.remove(itemListener_private, itemListener2);
        if (itemListener_private == null) {
            awtListener_private.removeListeners(5);
        }
    }

    public static void addTextListener(TextListener textListener2) throws IllegalArgumentException {
        if (textListener_private == null) {
            checkInstallPermission();
            awtListener_private.installListeners(10);
        }
        textListener_private = AWTEventMulticaster.add(textListener_private, textListener2);
    }

    public static void removeTextListener(TextListener textListener2) throws IllegalArgumentException {
        textListener_private = AWTEventMulticaster.remove(textListener_private, textListener2);
        if (textListener_private == null) {
            awtListener_private.removeListeners(10);
        }
    }

    /* loaded from: jaccess.jar:com/sun/java/accessibility/util/AWTEventMonitor$AWTEventsListener.class */
    static class AWTEventsListener implements TopLevelWindowListener, ActionListener, AdjustmentListener, ComponentListener, ContainerListener, FocusListener, ItemListener, KeyListener, MouseListener, MouseMotionListener, TextListener, WindowListener, ChangeListener {
        private Class[] actionListeners;
        private Method removeActionMethod;
        private Method addActionMethod;
        private Object[] actionArgs;
        private Class[] itemListeners;
        private Method removeItemMethod;
        private Method addItemMethod;
        private Object[] itemArgs;
        private Class[] textListeners;
        private Method removeTextMethod;
        private Method addTextMethod;
        private Object[] textArgs;
        private Class[] windowListeners;
        private Method removeWindowMethod;
        private Method addWindowMethod;
        private Object[] windowArgs;

        public AWTEventsListener() throws IllegalArgumentException {
            String property = System.getProperty("java.version");
            if (property != null) {
                boolean unused = AWTEventMonitor.runningOnJDK1_4 = property.compareTo(ServiceName.JMX_SPEC_VERSION) >= 0;
            }
            initializeIntrospection();
            installListeners();
            if (AWTEventMonitor.runningOnJDK1_4) {
                MenuSelectionManager.defaultManager().addChangeListener(this);
            }
            EventQueueMonitor.addTopLevelWindowListener(this);
        }

        private boolean initializeIntrospection() {
            try {
                this.actionListeners = new Class[1];
                this.actionArgs = new Object[1];
                this.actionListeners[0] = Class.forName("java.awt.event.ActionListener");
                this.actionArgs[0] = this;
                this.itemListeners = new Class[1];
                this.itemArgs = new Object[1];
                this.itemListeners[0] = Class.forName("java.awt.event.ItemListener");
                this.itemArgs[0] = this;
                this.textListeners = new Class[1];
                this.textArgs = new Object[1];
                this.textListeners[0] = Class.forName("java.awt.event.TextListener");
                this.textArgs[0] = this;
                this.windowListeners = new Class[1];
                this.windowArgs = new Object[1];
                this.windowListeners[0] = Class.forName("java.awt.event.WindowListener");
                this.windowArgs[0] = this;
                return true;
            } catch (ClassNotFoundException e2) {
                System.out.println("EXCEPTION - Class 'java.awt.event.*' not in CLASSPATH");
                return false;
            }
        }

        protected void installListeners() throws IllegalArgumentException {
            Window[] topLevelWindows = EventQueueMonitor.getTopLevelWindows();
            if (topLevelWindows != null) {
                for (Window window : topLevelWindows) {
                    installListeners(window);
                }
            }
        }

        protected void installListeners(int i2) throws IllegalArgumentException {
            Window[] topLevelWindows = EventQueueMonitor.getTopLevelWindows();
            if (topLevelWindows != null) {
                for (Window window : topLevelWindows) {
                    installListeners(window, i2);
                }
            }
        }

        protected void installListeners(Component component) throws IllegalArgumentException {
            installListeners(component, 3);
            installListeners(component, 4);
            if (AWTEventMonitor.componentListener_private != null) {
                installListeners(component, 2);
            }
            if (AWTEventMonitor.keyListener_private != null) {
                installListeners(component, 6);
            }
            if (AWTEventMonitor.mouseListener_private != null) {
                installListeners(component, 7);
            }
            if (AWTEventMonitor.mouseMotionListener_private != null) {
                installListeners(component, 8);
            }
            if (AWTEventMonitor.windowListener_private != null) {
                installListeners(component, 11);
            }
            if (AWTEventMonitor.actionListener_private != null) {
                installListeners(component, 0);
            }
            if (AWTEventMonitor.adjustmentListener_private != null) {
                installListeners(component, 1);
            }
            if (AWTEventMonitor.itemListener_private != null) {
                installListeners(component, 5);
            }
            if (AWTEventMonitor.textListener_private != null) {
                installListeners(component, 10);
            }
        }

        @Override // javax.swing.event.ChangeListener
        public void stateChanged(ChangeEvent changeEvent) {
            processFocusGained();
        }

        private void processFocusGained() {
            Component focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
            if (focusOwner == null) {
                return;
            }
            MenuSelectionManager.defaultManager().removeChangeListener(this);
            MenuSelectionManager.defaultManager().addChangeListener(this);
            if (!(focusOwner instanceof JRootPane)) {
                Component unused = AWTEventMonitor.componentWithFocus_private = focusOwner;
                return;
            }
            MenuElement[] selectedPath = MenuSelectionManager.defaultManager().getSelectedPath();
            if (selectedPath.length > 1) {
                Component component = selectedPath[selectedPath.length - 2].getComponent();
                Component component2 = selectedPath[selectedPath.length - 1].getComponent();
                if ((component2 instanceof JPopupMenu) || (component2 instanceof JMenu)) {
                    Component unused2 = AWTEventMonitor.componentWithFocus_private = component2;
                } else if (component instanceof JPopupMenu) {
                    Component unused3 = AWTEventMonitor.componentWithFocus_private = component;
                }
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        protected void installListeners(Component component, int i2) throws IllegalArgumentException {
            switch (i2) {
                case 0:
                    try {
                        this.removeActionMethod = component.getClass().getMethod("removeActionListener", this.actionListeners);
                        this.addActionMethod = component.getClass().getMethod("addActionListener", this.actionListeners);
                        try {
                            this.removeActionMethod.invoke(component, this.actionArgs);
                            this.addActionMethod.invoke(component, this.actionArgs);
                        } catch (IllegalAccessException e2) {
                            System.out.println("Exception: " + e2.toString());
                        } catch (InvocationTargetException e3) {
                            System.out.println("Exception: " + e3.toString());
                        }
                        break;
                    } catch (NoSuchMethodException e4) {
                        break;
                    } catch (SecurityException e5) {
                        System.out.println("Exception: " + e5.toString());
                        break;
                    }
                case 1:
                    if (component instanceof Adjustable) {
                        ((Adjustable) component).removeAdjustmentListener(this);
                        ((Adjustable) component).addAdjustmentListener(this);
                        break;
                    }
                    break;
                case 2:
                    component.removeComponentListener(this);
                    component.addComponentListener(this);
                    break;
                case 3:
                    if (component instanceof Container) {
                        ((Container) component).removeContainerListener(this);
                        ((Container) component).addContainerListener(this);
                        break;
                    }
                    break;
                case 4:
                    component.removeFocusListener(this);
                    component.addFocusListener(this);
                    if (AWTEventMonitor.runningOnJDK1_4) {
                        processFocusGained();
                        break;
                    } else if (component != AWTEventMonitor.componentWithFocus_private && component.hasFocus()) {
                        Component unused = AWTEventMonitor.componentWithFocus_private = component;
                        break;
                    }
                    break;
                case 5:
                    try {
                        this.removeItemMethod = component.getClass().getMethod("removeItemListener", this.itemListeners);
                        this.addItemMethod = component.getClass().getMethod("addItemListener", this.itemListeners);
                        try {
                            this.removeItemMethod.invoke(component, this.itemArgs);
                            this.addItemMethod.invoke(component, this.itemArgs);
                        } catch (IllegalAccessException e6) {
                            System.out.println("Exception: " + e6.toString());
                        } catch (InvocationTargetException e7) {
                            System.out.println("Exception: " + e7.toString());
                        }
                        break;
                    } catch (NoSuchMethodException e8) {
                        break;
                    } catch (SecurityException e9) {
                        System.out.println("Exception: " + e9.toString());
                        break;
                    }
                case 6:
                    component.removeKeyListener(this);
                    component.addKeyListener(this);
                    break;
                case 7:
                    component.removeMouseListener(this);
                    component.addMouseListener(this);
                    break;
                case 8:
                    component.removeMouseMotionListener(this);
                    component.addMouseMotionListener(this);
                    break;
                case 9:
                default:
                    return;
                case 10:
                    try {
                        this.removeTextMethod = component.getClass().getMethod("removeTextListener", this.textListeners);
                        this.addTextMethod = component.getClass().getMethod("addTextListener", this.textListeners);
                        try {
                            this.removeTextMethod.invoke(component, this.textArgs);
                            this.addTextMethod.invoke(component, this.textArgs);
                        } catch (IllegalAccessException e10) {
                            System.out.println("Exception: " + e10.toString());
                        } catch (InvocationTargetException e11) {
                            System.out.println("Exception: " + e11.toString());
                        }
                        break;
                    } catch (NoSuchMethodException e12) {
                        break;
                    } catch (SecurityException e13) {
                        System.out.println("Exception: " + e13.toString());
                        break;
                    }
                case 11:
                    try {
                        this.removeWindowMethod = component.getClass().getMethod("removeWindowListener", this.windowListeners);
                        this.addWindowMethod = component.getClass().getMethod("addWindowListener", this.windowListeners);
                        try {
                            this.removeWindowMethod.invoke(component, this.windowArgs);
                            this.addWindowMethod.invoke(component, this.windowArgs);
                        } catch (IllegalAccessException e14) {
                            System.out.println("Exception: " + e14.toString());
                        } catch (InvocationTargetException e15) {
                            System.out.println("Exception: " + e15.toString());
                        }
                        break;
                    } catch (NoSuchMethodException e16) {
                        break;
                    } catch (SecurityException e17) {
                        System.out.println("Exception: " + e17.toString());
                        break;
                    }
            }
            if (component instanceof Container) {
                int componentCount = ((Container) component).getComponentCount();
                for (int i3 = 0; i3 < componentCount; i3++) {
                    installListeners(((Container) component).getComponent(i3), i2);
                }
            }
        }

        protected void removeListeners(int i2) throws IllegalArgumentException {
            Window[] topLevelWindows = EventQueueMonitor.getTopLevelWindows();
            if (topLevelWindows != null) {
                for (Window window : topLevelWindows) {
                    removeListeners(window, i2);
                }
            }
        }

        protected void removeListeners(Component component) throws IllegalArgumentException {
            if (AWTEventMonitor.componentListener_private != null) {
                removeListeners(component, 2);
            }
            if (AWTEventMonitor.keyListener_private != null) {
                removeListeners(component, 6);
            }
            if (AWTEventMonitor.mouseListener_private != null) {
                removeListeners(component, 7);
            }
            if (AWTEventMonitor.mouseMotionListener_private != null) {
                removeListeners(component, 8);
            }
            if (AWTEventMonitor.windowListener_private != null) {
                removeListeners(component, 11);
            }
            if (AWTEventMonitor.actionListener_private != null) {
                removeListeners(component, 0);
            }
            if (AWTEventMonitor.adjustmentListener_private != null) {
                removeListeners(component, 1);
            }
            if (AWTEventMonitor.itemListener_private != null) {
                removeListeners(component, 5);
            }
            if (AWTEventMonitor.textListener_private != null) {
                removeListeners(component, 10);
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        protected void removeListeners(Component component, int i2) throws IllegalArgumentException {
            switch (i2) {
                case 0:
                    try {
                        this.removeActionMethod = component.getClass().getMethod("removeActionListener", this.actionListeners);
                        try {
                            this.removeActionMethod.invoke(component, this.actionArgs);
                        } catch (IllegalAccessException e2) {
                            System.out.println("Exception: " + e2.toString());
                        } catch (InvocationTargetException e3) {
                            System.out.println("Exception: " + e3.toString());
                        }
                        break;
                    } catch (NoSuchMethodException e4) {
                        break;
                    } catch (SecurityException e5) {
                        System.out.println("Exception: " + e5.toString());
                        break;
                    }
                case 1:
                    if (component instanceof Adjustable) {
                        ((Adjustable) component).removeAdjustmentListener(this);
                        break;
                    }
                    break;
                case 2:
                    component.removeComponentListener(this);
                    break;
                case 3:
                case 4:
                case 9:
                default:
                    return;
                case 5:
                    try {
                        this.removeItemMethod = component.getClass().getMethod("removeItemListener", this.itemListeners);
                        try {
                            this.removeItemMethod.invoke(component, this.itemArgs);
                        } catch (IllegalAccessException e6) {
                            System.out.println("Exception: " + e6.toString());
                        } catch (InvocationTargetException e7) {
                            System.out.println("Exception: " + e7.toString());
                        }
                        break;
                    } catch (NoSuchMethodException e8) {
                        break;
                    } catch (SecurityException e9) {
                        System.out.println("Exception: " + e9.toString());
                        break;
                    }
                case 6:
                    component.removeKeyListener(this);
                    break;
                case 7:
                    component.removeMouseListener(this);
                    break;
                case 8:
                    component.removeMouseMotionListener(this);
                    break;
                case 10:
                    try {
                        this.removeTextMethod = component.getClass().getMethod("removeTextListener", this.textListeners);
                        try {
                            this.removeTextMethod.invoke(component, this.textArgs);
                        } catch (IllegalAccessException e10) {
                            System.out.println("Exception: " + e10.toString());
                        } catch (InvocationTargetException e11) {
                            System.out.println("Exception: " + e11.toString());
                        }
                        break;
                    } catch (NoSuchMethodException e12) {
                        break;
                    } catch (SecurityException e13) {
                        System.out.println("Exception: " + e13.toString());
                        break;
                    }
                case 11:
                    try {
                        this.removeWindowMethod = component.getClass().getMethod("removeWindowListener", this.windowListeners);
                        try {
                            this.removeWindowMethod.invoke(component, this.windowArgs);
                        } catch (IllegalAccessException e14) {
                            System.out.println("Exception: " + e14.toString());
                        } catch (InvocationTargetException e15) {
                            System.out.println("Exception: " + e15.toString());
                        }
                        break;
                    } catch (NoSuchMethodException e16) {
                        break;
                    } catch (SecurityException e17) {
                        System.out.println("Exception: " + e17.toString());
                        break;
                    }
            }
            if (component instanceof Container) {
                int componentCount = ((Container) component).getComponentCount();
                for (int i3 = 0; i3 < componentCount; i3++) {
                    removeListeners(((Container) component).getComponent(i3), i2);
                }
            }
        }

        @Override // com.sun.java.accessibility.util.TopLevelWindowListener
        public void topLevelWindowCreated(Window window) throws IllegalArgumentException {
            installListeners(window);
        }

        @Override // com.sun.java.accessibility.util.TopLevelWindowListener
        public void topLevelWindowDestroyed(Window window) {
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            if (AWTEventMonitor.actionListener_private != null) {
                AWTEventMonitor.actionListener_private.actionPerformed(actionEvent);
            }
        }

        @Override // java.awt.event.AdjustmentListener
        public void adjustmentValueChanged(AdjustmentEvent adjustmentEvent) {
            if (AWTEventMonitor.adjustmentListener_private != null) {
                AWTEventMonitor.adjustmentListener_private.adjustmentValueChanged(adjustmentEvent);
            }
        }

        @Override // java.awt.event.ComponentListener
        public void componentHidden(ComponentEvent componentEvent) {
            if (AWTEventMonitor.componentListener_private != null) {
                AWTEventMonitor.componentListener_private.componentHidden(componentEvent);
            }
        }

        @Override // java.awt.event.ComponentListener
        public void componentMoved(ComponentEvent componentEvent) {
            if (AWTEventMonitor.componentListener_private != null) {
                AWTEventMonitor.componentListener_private.componentMoved(componentEvent);
            }
        }

        @Override // java.awt.event.ComponentListener
        public void componentResized(ComponentEvent componentEvent) {
            if (AWTEventMonitor.componentListener_private != null) {
                AWTEventMonitor.componentListener_private.componentResized(componentEvent);
            }
        }

        @Override // java.awt.event.ComponentListener
        public void componentShown(ComponentEvent componentEvent) {
            if (AWTEventMonitor.componentListener_private != null) {
                AWTEventMonitor.componentListener_private.componentShown(componentEvent);
            }
        }

        @Override // java.awt.event.ContainerListener
        public void componentAdded(ContainerEvent containerEvent) throws IllegalArgumentException {
            installListeners(containerEvent.getChild());
            if (AWTEventMonitor.containerListener_private != null) {
                AWTEventMonitor.containerListener_private.componentAdded(containerEvent);
            }
        }

        @Override // java.awt.event.ContainerListener
        public void componentRemoved(ContainerEvent containerEvent) throws IllegalArgumentException {
            removeListeners(containerEvent.getChild());
            if (AWTEventMonitor.containerListener_private != null) {
                AWTEventMonitor.containerListener_private.componentRemoved(containerEvent);
            }
        }

        @Override // java.awt.event.FocusListener
        public void focusGained(FocusEvent focusEvent) {
            Component unused = AWTEventMonitor.componentWithFocus_private = (Component) focusEvent.getSource();
            if (AWTEventMonitor.focusListener_private != null) {
                AWTEventMonitor.focusListener_private.focusGained(focusEvent);
            }
        }

        @Override // java.awt.event.FocusListener
        public void focusLost(FocusEvent focusEvent) {
            Component unused = AWTEventMonitor.componentWithFocus_private = null;
            if (AWTEventMonitor.focusListener_private != null) {
                AWTEventMonitor.focusListener_private.focusLost(focusEvent);
            }
        }

        @Override // java.awt.event.ItemListener
        public void itemStateChanged(ItemEvent itemEvent) {
            if (AWTEventMonitor.itemListener_private != null) {
                AWTEventMonitor.itemListener_private.itemStateChanged(itemEvent);
            }
        }

        @Override // java.awt.event.KeyListener
        public void keyPressed(KeyEvent keyEvent) {
            if (AWTEventMonitor.keyListener_private != null) {
                AWTEventMonitor.keyListener_private.keyPressed(keyEvent);
            }
        }

        @Override // java.awt.event.KeyListener
        public void keyReleased(KeyEvent keyEvent) {
            if (AWTEventMonitor.keyListener_private != null) {
                AWTEventMonitor.keyListener_private.keyReleased(keyEvent);
            }
        }

        @Override // java.awt.event.KeyListener
        public void keyTyped(KeyEvent keyEvent) {
            if (AWTEventMonitor.keyListener_private != null) {
                AWTEventMonitor.keyListener_private.keyTyped(keyEvent);
            }
        }

        @Override // java.awt.event.MouseListener
        public void mouseClicked(MouseEvent mouseEvent) {
            if (AWTEventMonitor.mouseListener_private != null) {
                AWTEventMonitor.mouseListener_private.mouseClicked(mouseEvent);
            }
        }

        @Override // java.awt.event.MouseListener
        public void mouseEntered(MouseEvent mouseEvent) {
            if (AWTEventMonitor.mouseListener_private != null) {
                AWTEventMonitor.mouseListener_private.mouseEntered(mouseEvent);
            }
        }

        @Override // java.awt.event.MouseListener
        public void mouseExited(MouseEvent mouseEvent) {
            if (AWTEventMonitor.mouseListener_private != null) {
                AWTEventMonitor.mouseListener_private.mouseExited(mouseEvent);
            }
        }

        @Override // java.awt.event.MouseListener
        public void mousePressed(MouseEvent mouseEvent) {
            if (AWTEventMonitor.mouseListener_private != null) {
                AWTEventMonitor.mouseListener_private.mousePressed(mouseEvent);
            }
        }

        @Override // java.awt.event.MouseListener
        public void mouseReleased(MouseEvent mouseEvent) {
            if (AWTEventMonitor.mouseListener_private != null) {
                AWTEventMonitor.mouseListener_private.mouseReleased(mouseEvent);
            }
        }

        @Override // java.awt.event.MouseMotionListener
        public void mouseDragged(MouseEvent mouseEvent) {
            if (AWTEventMonitor.mouseMotionListener_private != null) {
                AWTEventMonitor.mouseMotionListener_private.mouseDragged(mouseEvent);
            }
        }

        @Override // java.awt.event.MouseMotionListener
        public void mouseMoved(MouseEvent mouseEvent) {
            if (AWTEventMonitor.mouseMotionListener_private != null) {
                AWTEventMonitor.mouseMotionListener_private.mouseMoved(mouseEvent);
            }
        }

        @Override // java.awt.event.TextListener
        public void textValueChanged(TextEvent textEvent) {
            if (AWTEventMonitor.textListener_private != null) {
                AWTEventMonitor.textListener_private.textValueChanged(textEvent);
            }
        }

        @Override // java.awt.event.WindowListener
        public void windowOpened(WindowEvent windowEvent) {
            if (AWTEventMonitor.windowListener_private != null) {
                AWTEventMonitor.windowListener_private.windowOpened(windowEvent);
            }
        }

        @Override // java.awt.event.WindowListener
        public void windowClosing(WindowEvent windowEvent) {
            if (AWTEventMonitor.windowListener_private != null) {
                AWTEventMonitor.windowListener_private.windowClosing(windowEvent);
            }
        }

        @Override // java.awt.event.WindowListener
        public void windowClosed(WindowEvent windowEvent) {
            if (AWTEventMonitor.windowListener_private != null) {
                AWTEventMonitor.windowListener_private.windowClosed(windowEvent);
            }
        }

        @Override // java.awt.event.WindowListener
        public void windowIconified(WindowEvent windowEvent) {
            if (AWTEventMonitor.windowListener_private != null) {
                AWTEventMonitor.windowListener_private.windowIconified(windowEvent);
            }
        }

        @Override // java.awt.event.WindowListener
        public void windowDeiconified(WindowEvent windowEvent) {
            if (AWTEventMonitor.windowListener_private != null) {
                AWTEventMonitor.windowListener_private.windowDeiconified(windowEvent);
            }
        }

        @Override // java.awt.event.WindowListener
        public void windowActivated(WindowEvent windowEvent) {
            if (AWTEventMonitor.windowListener_private != null) {
                AWTEventMonitor.windowListener_private.windowActivated(windowEvent);
            }
        }

        @Override // java.awt.event.WindowListener
        public void windowDeactivated(WindowEvent windowEvent) {
            if (AWTEventMonitor.windowListener_private != null) {
                AWTEventMonitor.windowListener_private.windowDeactivated(windowEvent);
            }
        }
    }
}
