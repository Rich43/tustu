package javax.swing.plaf.metal;

import java.awt.Container;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.ActionMap;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.LookAndFeel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.MouseInputAdapter;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicInternalFrameUI;

/* loaded from: rt.jar:javax/swing/plaf/metal/MetalInternalFrameUI.class */
public class MetalInternalFrameUI extends BasicInternalFrameUI {
    private static final PropertyChangeListener metalPropertyChangeListener = new MetalPropertyChangeHandler();
    private static final Border handyEmptyBorder = new EmptyBorder(0, 0, 0, 0);
    protected static String IS_PALETTE = "JInternalFrame.isPalette";
    private static String IS_PALETTE_KEY = "JInternalFrame.isPalette";
    private static String FRAME_TYPE = "JInternalFrame.frameType";
    private static String NORMAL_FRAME = "normal";
    private static String PALETTE_FRAME = "palette";
    private static String OPTION_DIALOG = "optionDialog";

    public MetalInternalFrameUI(JInternalFrame jInternalFrame) {
        super(jInternalFrame);
    }

    public static ComponentUI createUI(JComponent jComponent) {
        return new MetalInternalFrameUI((JInternalFrame) jComponent);
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameUI, javax.swing.plaf.ComponentUI
    public void installUI(JComponent jComponent) {
        super.installUI(jComponent);
        Object clientProperty = jComponent.getClientProperty(IS_PALETTE_KEY);
        if (clientProperty != null) {
            setPalette(((Boolean) clientProperty).booleanValue());
        }
        stripContentBorder(this.frame.getContentPane());
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameUI, javax.swing.plaf.ComponentUI
    public void uninstallUI(JComponent jComponent) {
        this.frame = (JInternalFrame) jComponent;
        Container contentPane = ((JInternalFrame) jComponent).getContentPane();
        if (contentPane instanceof JComponent) {
            JComponent jComponent2 = (JComponent) contentPane;
            if (jComponent2.getBorder() == handyEmptyBorder) {
                jComponent2.setBorder(null);
            }
        }
        super.uninstallUI(jComponent);
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameUI
    protected void installListeners() {
        super.installListeners();
        this.frame.addPropertyChangeListener(metalPropertyChangeListener);
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameUI
    protected void uninstallListeners() {
        this.frame.removePropertyChangeListener(metalPropertyChangeListener);
        super.uninstallListeners();
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameUI
    protected void installKeyboardActions() {
        super.installKeyboardActions();
        ActionMap uIActionMap = SwingUtilities.getUIActionMap(this.frame);
        if (uIActionMap != null) {
            uIActionMap.remove("showSystemMenu");
        }
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameUI
    protected void uninstallKeyboardActions() {
        super.uninstallKeyboardActions();
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameUI
    protected void uninstallComponents() {
        this.titlePane = null;
        super.uninstallComponents();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stripContentBorder(Object obj) {
        if (obj instanceof JComponent) {
            JComponent jComponent = (JComponent) obj;
            Border border = jComponent.getBorder();
            if (border == null || (border instanceof UIResource)) {
                jComponent.setBorder(handyEmptyBorder);
            }
        }
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameUI
    protected JComponent createNorthPane(JInternalFrame jInternalFrame) {
        return new MetalInternalFrameTitlePane(jInternalFrame);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setFrameType(String str) {
        if (str.equals(OPTION_DIALOG)) {
            LookAndFeel.installBorder(this.frame, "InternalFrame.optionDialogBorder");
            ((MetalInternalFrameTitlePane) this.titlePane).setPalette(false);
        } else if (str.equals(PALETTE_FRAME)) {
            LookAndFeel.installBorder(this.frame, "InternalFrame.paletteBorder");
            ((MetalInternalFrameTitlePane) this.titlePane).setPalette(true);
        } else {
            LookAndFeel.installBorder(this.frame, "InternalFrame.border");
            ((MetalInternalFrameTitlePane) this.titlePane).setPalette(false);
        }
    }

    public void setPalette(boolean z2) {
        if (z2) {
            LookAndFeel.installBorder(this.frame, "InternalFrame.paletteBorder");
        } else {
            LookAndFeel.installBorder(this.frame, "InternalFrame.border");
        }
        ((MetalInternalFrameTitlePane) this.titlePane).setPalette(z2);
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalInternalFrameUI$MetalPropertyChangeHandler.class */
    private static class MetalPropertyChangeHandler implements PropertyChangeListener {
        private MetalPropertyChangeHandler() {
        }

        @Override // java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            String propertyName = propertyChangeEvent.getPropertyName();
            JInternalFrame jInternalFrame = (JInternalFrame) propertyChangeEvent.getSource();
            if (!(jInternalFrame.getUI() instanceof MetalInternalFrameUI)) {
                return;
            }
            MetalInternalFrameUI metalInternalFrameUI = (MetalInternalFrameUI) jInternalFrame.getUI();
            if (propertyName.equals(MetalInternalFrameUI.FRAME_TYPE)) {
                if (propertyChangeEvent.getNewValue() instanceof String) {
                    metalInternalFrameUI.setFrameType((String) propertyChangeEvent.getNewValue());
                }
            } else {
                if (propertyName.equals(MetalInternalFrameUI.IS_PALETTE_KEY)) {
                    if (propertyChangeEvent.getNewValue() != null) {
                        metalInternalFrameUI.setPalette(((Boolean) propertyChangeEvent.getNewValue()).booleanValue());
                        return;
                    } else {
                        metalInternalFrameUI.setPalette(false);
                        return;
                    }
                }
                if (propertyName.equals(JInternalFrame.CONTENT_PANE_PROPERTY)) {
                    metalInternalFrameUI.stripContentBorder(propertyChangeEvent.getNewValue());
                }
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalInternalFrameUI$BorderListener1.class */
    private class BorderListener1 extends BasicInternalFrameUI.BorderListener implements SwingConstants {
        private BorderListener1() {
            super();
        }

        Rectangle getIconBounds() {
            boolean zIsLeftToRight = MetalUtils.isLeftToRight(MetalInternalFrameUI.this.frame);
            int width = zIsLeftToRight ? 5 : MetalInternalFrameUI.this.titlePane.getWidth() - 5;
            Rectangle rectangle = null;
            Icon frameIcon = MetalInternalFrameUI.this.frame.getFrameIcon();
            if (frameIcon != null) {
                if (!zIsLeftToRight) {
                    width -= frameIcon.getIconWidth();
                }
                rectangle = new Rectangle(width, (MetalInternalFrameUI.this.titlePane.getHeight() / 2) - (frameIcon.getIconHeight() / 2), frameIcon.getIconWidth(), frameIcon.getIconHeight());
            }
            return rectangle;
        }

        @Override // javax.swing.plaf.basic.BasicInternalFrameUI.BorderListener, java.awt.event.MouseAdapter, java.awt.event.MouseListener
        public void mouseClicked(MouseEvent mouseEvent) {
            if (mouseEvent.getClickCount() == 2 && mouseEvent.getSource() == MetalInternalFrameUI.this.getNorthPane() && MetalInternalFrameUI.this.frame.isClosable() && !MetalInternalFrameUI.this.frame.isIcon()) {
                Rectangle iconBounds = getIconBounds();
                if (iconBounds != null && iconBounds.contains(mouseEvent.getX(), mouseEvent.getY())) {
                    MetalInternalFrameUI.this.frame.doDefaultCloseAction();
                    return;
                } else {
                    super.mouseClicked(mouseEvent);
                    return;
                }
            }
            super.mouseClicked(mouseEvent);
        }
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameUI
    protected MouseInputAdapter createBorderListener(JInternalFrame jInternalFrame) {
        return new BorderListener1();
    }
}
