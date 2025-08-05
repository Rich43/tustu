package javax.swing.plaf.basic;

import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ComponentInputMapUIResource;
import sun.swing.DefaultLookup;
import sun.swing.SwingUtilities2;
import sun.swing.UIAction;

/* loaded from: rt.jar:javax/swing/plaf/basic/BasicButtonListener.class */
public class BasicButtonListener implements MouseListener, MouseMotionListener, FocusListener, ChangeListener, PropertyChangeListener {
    private long lastPressedTimestamp = -1;
    private boolean shouldDiscardRelease = false;

    /*  JADX ERROR: Failed to decode insn: 0x002E: MOVE_MULTI
        java.lang.ArrayIndexOutOfBoundsException: arraycopy: source index -1 out of bounds for object array[6]
        	at java.base/java.lang.System.arraycopy(Native Method)
        	at jadx.plugins.input.java.data.code.StackState.insert(StackState.java:52)
        	at jadx.plugins.input.java.data.code.CodeDecodeState.insert(CodeDecodeState.java:137)
        	at jadx.plugins.input.java.data.code.JavaInsnsRegister.dup2x1(JavaInsnsRegister.java:313)
        	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
        	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
        	at jadx.core.ProcessClass.process(ProcessClass.java:69)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:117)
        	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
        */
    @Override // java.awt.event.MouseListener
    public void mousePressed(java.awt.event.MouseEvent r7) {
        /*
            r6 = this;
            r0 = r7
            boolean r0 = javax.swing.SwingUtilities.isLeftMouseButton(r0)
            if (r0 == 0) goto L8a
            r0 = r7
            java.lang.Object r0 = r0.getSource()
            javax.swing.AbstractButton r0 = (javax.swing.AbstractButton) r0
            r8 = r0
            r0 = r8
            r1 = r7
            int r1 = r1.getX()
            r2 = r7
            int r2 = r2.getY()
            boolean r0 = r0.contains(r1, r2)
            if (r0 == 0) goto L8a
            r0 = r8
            long r0 = r0.getMultiClickThreshhold()
            r9 = r0
            r0 = r6
            long r0 = r0.lastPressedTimestamp
            r11 = r0
            r0 = r6
            r1 = r7
            long r1 = r1.getWhen()
            // decode failed: arraycopy: source index -1 out of bounds for object array[6]
            r0.lastPressedTimestamp = r1
            r13 = r-1
            r-1 = r11
            r0 = -1
            int r-1 = (r-1 > r0 ? 1 : (r-1 == r0 ? 0 : -1))
            if (r-1 == 0) goto L4d
            r-1 = r13
            r0 = r11
            long r-1 = r-1 - r0
            r0 = r9
            int r-1 = (r-1 > r0 ? 1 : (r-1 == r0 ? 0 : -1))
            if (r-1 >= 0) goto L4d
            r-1 = r6
            r0 = 1
            r-1.shouldDiscardRelease = r0
            return
            r-1 = r8
            r-1.getModel()
            r15 = r-1
            r-1 = r15
            r-1.isEnabled()
            if (r-1 != 0) goto L5e
            return
            r-1 = r15
            r-1.isArmed()
            if (r-1 != 0) goto L70
            r-1 = r15
            r0 = 1
            r-1.setArmed(r0)
            r-1 = r15
            r0 = 1
            r-1.setPressed(r0)
            r-1 = r8
            r-1.hasFocus()
            if (r-1 != 0) goto L8a
            r-1 = r8
            r-1.isRequestFocusEnabled()
            if (r-1 == 0) goto L8a
            r-1 = r8
            r-1.requestFocus()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.swing.plaf.basic.BasicButtonListener.mousePressed(java.awt.event.MouseEvent):void");
    }

    static void loadActionMap(LazyActionMap lazyActionMap) {
        lazyActionMap.put(new Actions("pressed"));
        lazyActionMap.put(new Actions("released"));
    }

    public BasicButtonListener(AbstractButton abstractButton) {
    }

    @Override // java.beans.PropertyChangeListener
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        String propertyName = propertyChangeEvent.getPropertyName();
        if (propertyName == AbstractButton.MNEMONIC_CHANGED_PROPERTY) {
            updateMnemonicBinding((AbstractButton) propertyChangeEvent.getSource());
            return;
        }
        if (propertyName == AbstractButton.CONTENT_AREA_FILLED_CHANGED_PROPERTY) {
            checkOpacity((AbstractButton) propertyChangeEvent.getSource());
        } else if (propertyName == "text" || "font" == propertyName || "foreground" == propertyName) {
            AbstractButton abstractButton = (AbstractButton) propertyChangeEvent.getSource();
            BasicHTML.updateRenderer(abstractButton, abstractButton.getText());
        }
    }

    protected void checkOpacity(AbstractButton abstractButton) {
        abstractButton.setOpaque(abstractButton.isContentAreaFilled());
    }

    public void installKeyboardActions(JComponent jComponent) {
        updateMnemonicBinding((AbstractButton) jComponent);
        LazyActionMap.installLazyActionMap(jComponent, BasicButtonListener.class, "Button.actionMap");
        SwingUtilities.replaceUIInputMap(jComponent, 0, getInputMap(0, jComponent));
    }

    public void uninstallKeyboardActions(JComponent jComponent) {
        SwingUtilities.replaceUIInputMap(jComponent, 2, null);
        SwingUtilities.replaceUIInputMap(jComponent, 0, null);
        SwingUtilities.replaceUIActionMap(jComponent, null);
    }

    InputMap getInputMap(int i2, JComponent jComponent) {
        BasicButtonUI basicButtonUI;
        if (i2 == 0 && (basicButtonUI = (BasicButtonUI) BasicLookAndFeel.getUIOfType(((AbstractButton) jComponent).getUI(), BasicButtonUI.class)) != null) {
            return (InputMap) DefaultLookup.get(jComponent, basicButtonUI, basicButtonUI.getPropertyPrefix() + "focusInputMap");
        }
        return null;
    }

    void updateMnemonicBinding(AbstractButton abstractButton) {
        int mnemonic = abstractButton.getMnemonic();
        if (mnemonic != 0) {
            InputMap uIInputMap = SwingUtilities.getUIInputMap(abstractButton, 2);
            if (uIInputMap == null) {
                uIInputMap = new ComponentInputMapUIResource(abstractButton);
                SwingUtilities.replaceUIInputMap(abstractButton, 2, uIInputMap);
            }
            uIInputMap.clear();
            uIInputMap.put(KeyStroke.getKeyStroke(mnemonic, BasicLookAndFeel.getFocusAcceleratorKeyMask(), false), "pressed");
            uIInputMap.put(KeyStroke.getKeyStroke(mnemonic, SwingUtilities2.setAltGraphMask(BasicLookAndFeel.getFocusAcceleratorKeyMask()), false), "pressed");
            uIInputMap.put(KeyStroke.getKeyStroke(mnemonic, BasicLookAndFeel.getFocusAcceleratorKeyMask(), true), "released");
            uIInputMap.put(KeyStroke.getKeyStroke(mnemonic, SwingUtilities2.setAltGraphMask(BasicLookAndFeel.getFocusAcceleratorKeyMask()), true), "released");
            uIInputMap.put(KeyStroke.getKeyStroke(mnemonic, 0, true), "released");
            return;
        }
        InputMap uIInputMap2 = SwingUtilities.getUIInputMap(abstractButton, 2);
        if (uIInputMap2 != null) {
            uIInputMap2.clear();
        }
    }

    @Override // javax.swing.event.ChangeListener
    public void stateChanged(ChangeEvent changeEvent) {
        ((AbstractButton) changeEvent.getSource()).repaint();
    }

    @Override // java.awt.event.FocusListener
    public void focusGained(FocusEvent focusEvent) {
        JRootPane rootPane;
        BasicButtonUI basicButtonUI;
        AbstractButton abstractButton = (AbstractButton) focusEvent.getSource();
        if ((abstractButton instanceof JButton) && ((JButton) abstractButton).isDefaultCapable() && (rootPane = abstractButton.getRootPane()) != null && (basicButtonUI = (BasicButtonUI) BasicLookAndFeel.getUIOfType(abstractButton.getUI(), BasicButtonUI.class)) != null && DefaultLookup.getBoolean(abstractButton, basicButtonUI, basicButtonUI.getPropertyPrefix() + "defaultButtonFollowsFocus", true)) {
            rootPane.putClientProperty("temporaryDefaultButton", abstractButton);
            rootPane.setDefaultButton((JButton) abstractButton);
            rootPane.putClientProperty("temporaryDefaultButton", null);
        }
        abstractButton.repaint();
    }

    @Override // java.awt.event.FocusListener
    public void focusLost(FocusEvent focusEvent) {
        JButton jButton;
        BasicButtonUI basicButtonUI;
        AbstractButton abstractButton = (AbstractButton) focusEvent.getSource();
        JRootPane rootPane = abstractButton.getRootPane();
        if (rootPane != null && abstractButton != (jButton = (JButton) rootPane.getClientProperty("initialDefaultButton")) && (basicButtonUI = (BasicButtonUI) BasicLookAndFeel.getUIOfType(abstractButton.getUI(), BasicButtonUI.class)) != null && DefaultLookup.getBoolean(abstractButton, basicButtonUI, basicButtonUI.getPropertyPrefix() + "defaultButtonFollowsFocus", true)) {
            rootPane.setDefaultButton(jButton);
        }
        ButtonModel model = abstractButton.getModel();
        model.setPressed(false);
        model.setArmed(false);
        abstractButton.repaint();
    }

    @Override // java.awt.event.MouseMotionListener
    public void mouseMoved(MouseEvent mouseEvent) {
    }

    @Override // java.awt.event.MouseMotionListener
    public void mouseDragged(MouseEvent mouseEvent) {
    }

    @Override // java.awt.event.MouseListener
    public void mouseClicked(MouseEvent mouseEvent) {
    }

    @Override // java.awt.event.MouseListener
    public void mouseReleased(MouseEvent mouseEvent) {
        if (SwingUtilities.isLeftMouseButton(mouseEvent)) {
            if (this.shouldDiscardRelease) {
                this.shouldDiscardRelease = false;
                return;
            }
            ButtonModel model = ((AbstractButton) mouseEvent.getSource()).getModel();
            model.setPressed(false);
            model.setArmed(false);
        }
    }

    @Override // java.awt.event.MouseListener
    public void mouseEntered(MouseEvent mouseEvent) {
        AbstractButton abstractButton = (AbstractButton) mouseEvent.getSource();
        ButtonModel model = abstractButton.getModel();
        if (abstractButton.isRolloverEnabled() && !SwingUtilities.isLeftMouseButton(mouseEvent)) {
            model.setRollover(true);
        }
        if (model.isPressed()) {
            model.setArmed(true);
        }
    }

    @Override // java.awt.event.MouseListener
    public void mouseExited(MouseEvent mouseEvent) {
        AbstractButton abstractButton = (AbstractButton) mouseEvent.getSource();
        ButtonModel model = abstractButton.getModel();
        if (abstractButton.isRolloverEnabled()) {
            model.setRollover(false);
        }
        model.setArmed(false);
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicButtonListener$Actions.class */
    private static class Actions extends UIAction {
        private static final String PRESS = "pressed";
        private static final String RELEASE = "released";

        Actions(String str) {
            super(str);
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            AbstractButton abstractButton = (AbstractButton) actionEvent.getSource();
            String name = getName();
            if (name == PRESS) {
                ButtonModel model = abstractButton.getModel();
                model.setArmed(true);
                model.setPressed(true);
                if (!abstractButton.hasFocus()) {
                    abstractButton.requestFocus();
                    return;
                }
                return;
            }
            if (name == RELEASE) {
                ButtonModel model2 = abstractButton.getModel();
                model2.setPressed(false);
                model2.setArmed(false);
            }
        }

        @Override // sun.swing.UIAction
        public boolean isEnabled(Object obj) {
            if (obj != null && (obj instanceof AbstractButton) && !((AbstractButton) obj).getModel().isEnabled()) {
                return false;
            }
            return true;
        }
    }
}
