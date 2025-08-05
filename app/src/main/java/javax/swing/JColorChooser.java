package javax.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.swing.ColorChooserDialog;
import javax.swing.JComponent;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.colorchooser.ColorChooserComponentFactory;
import javax.swing.colorchooser.ColorSelectionModel;
import javax.swing.colorchooser.DefaultColorSelectionModel;
import javax.swing.plaf.ColorChooserUI;
import javax.swing.plaf.ComponentUI;

/* loaded from: rt.jar:javax/swing/JColorChooser.class */
public class JColorChooser extends JComponent implements Accessible {
    private static final String uiClassID = "ColorChooserUI";
    private ColorSelectionModel selectionModel;
    private JComponent previewPanel;
    private AbstractColorChooserPanel[] chooserPanels;
    private boolean dragEnabled;
    public static final String SELECTION_MODEL_PROPERTY = "selectionModel";
    public static final String PREVIEW_PANEL_PROPERTY = "previewPanel";
    public static final String CHOOSER_PANELS_PROPERTY = "chooserPanels";
    protected AccessibleContext accessibleContext;

    public static Color showDialog(Component component, String str, Color color) throws HeadlessException {
        JColorChooser jColorChooser = new JColorChooser(color != null ? color : Color.white);
        ColorTracker colorTracker = new ColorTracker(jColorChooser);
        JDialog jDialogCreateDialog = createDialog(component, str, true, jColorChooser, colorTracker, null);
        jDialogCreateDialog.addComponentListener(new ColorChooserDialog.DisposeOnClose());
        jDialogCreateDialog.show();
        return colorTracker.getColor();
    }

    public static JDialog createDialog(Component component, String str, boolean z2, JColorChooser jColorChooser, ActionListener actionListener, ActionListener actionListener2) throws HeadlessException {
        ColorChooserDialog colorChooserDialog;
        Window windowForComponent = JOptionPane.getWindowForComponent(component);
        if (windowForComponent instanceof Frame) {
            colorChooserDialog = new ColorChooserDialog((Frame) windowForComponent, str, z2, component, jColorChooser, actionListener, actionListener2);
        } else {
            colorChooserDialog = new ColorChooserDialog((Dialog) windowForComponent, str, z2, component, jColorChooser, actionListener, actionListener2);
        }
        colorChooserDialog.getAccessibleContext().setAccessibleDescription(str);
        return colorChooserDialog;
    }

    public JColorChooser() {
        this(Color.white);
    }

    public JColorChooser(Color color) {
        this(new DefaultColorSelectionModel(color));
    }

    public JColorChooser(ColorSelectionModel colorSelectionModel) {
        this.previewPanel = ColorChooserComponentFactory.getPreviewPanel();
        this.chooserPanels = new AbstractColorChooserPanel[0];
        this.accessibleContext = null;
        this.selectionModel = colorSelectionModel;
        updateUI();
        this.dragEnabled = false;
    }

    public ColorChooserUI getUI() {
        return (ColorChooserUI) this.ui;
    }

    public void setUI(ColorChooserUI colorChooserUI) {
        super.setUI((ComponentUI) colorChooserUI);
    }

    @Override // javax.swing.JComponent
    public void updateUI() {
        setUI((ColorChooserUI) UIManager.getUI(this));
    }

    @Override // javax.swing.JComponent
    public String getUIClassID() {
        return uiClassID;
    }

    public Color getColor() {
        return this.selectionModel.getSelectedColor();
    }

    public void setColor(Color color) {
        this.selectionModel.setSelectedColor(color);
    }

    public void setColor(int i2, int i3, int i4) {
        setColor(new Color(i2, i3, i4));
    }

    public void setColor(int i2) {
        setColor((i2 >> 16) & 255, (i2 >> 8) & 255, i2 & 255);
    }

    public void setDragEnabled(boolean z2) {
        if (z2 && GraphicsEnvironment.isHeadless()) {
            throw new HeadlessException();
        }
        this.dragEnabled = z2;
    }

    public boolean getDragEnabled() {
        return this.dragEnabled;
    }

    public void setPreviewPanel(JComponent jComponent) {
        if (this.previewPanel != jComponent) {
            JComponent jComponent2 = this.previewPanel;
            this.previewPanel = jComponent;
            firePropertyChange(PREVIEW_PANEL_PROPERTY, jComponent2, jComponent);
        }
    }

    public JComponent getPreviewPanel() {
        return this.previewPanel;
    }

    public void addChooserPanel(AbstractColorChooserPanel abstractColorChooserPanel) {
        AbstractColorChooserPanel[] chooserPanels = getChooserPanels();
        AbstractColorChooserPanel[] abstractColorChooserPanelArr = new AbstractColorChooserPanel[chooserPanels.length + 1];
        System.arraycopy(chooserPanels, 0, abstractColorChooserPanelArr, 0, chooserPanels.length);
        abstractColorChooserPanelArr[abstractColorChooserPanelArr.length - 1] = abstractColorChooserPanel;
        setChooserPanels(abstractColorChooserPanelArr);
    }

    public AbstractColorChooserPanel removeChooserPanel(AbstractColorChooserPanel abstractColorChooserPanel) {
        int i2 = -1;
        int i3 = 0;
        while (true) {
            if (i3 >= this.chooserPanels.length) {
                break;
            }
            if (this.chooserPanels[i3] != abstractColorChooserPanel) {
                i3++;
            } else {
                i2 = i3;
                break;
            }
        }
        if (i2 == -1) {
            throw new IllegalArgumentException("chooser panel not in this chooser");
        }
        AbstractColorChooserPanel[] abstractColorChooserPanelArr = new AbstractColorChooserPanel[this.chooserPanels.length - 1];
        if (i2 == this.chooserPanels.length - 1) {
            System.arraycopy(this.chooserPanels, 0, abstractColorChooserPanelArr, 0, abstractColorChooserPanelArr.length);
        } else if (i2 == 0) {
            System.arraycopy(this.chooserPanels, 1, abstractColorChooserPanelArr, 0, abstractColorChooserPanelArr.length);
        } else {
            System.arraycopy(this.chooserPanels, 0, abstractColorChooserPanelArr, 0, i2);
            System.arraycopy(this.chooserPanels, i2 + 1, abstractColorChooserPanelArr, i2, (this.chooserPanels.length - i2) - 1);
        }
        setChooserPanels(abstractColorChooserPanelArr);
        return abstractColorChooserPanel;
    }

    public void setChooserPanels(AbstractColorChooserPanel[] abstractColorChooserPanelArr) {
        AbstractColorChooserPanel[] abstractColorChooserPanelArr2 = this.chooserPanels;
        this.chooserPanels = abstractColorChooserPanelArr;
        firePropertyChange(CHOOSER_PANELS_PROPERTY, abstractColorChooserPanelArr2, abstractColorChooserPanelArr);
    }

    public AbstractColorChooserPanel[] getChooserPanels() {
        return this.chooserPanels;
    }

    public ColorSelectionModel getSelectionModel() {
        return this.selectionModel;
    }

    public void setSelectionModel(ColorSelectionModel colorSelectionModel) {
        ColorSelectionModel colorSelectionModel2 = this.selectionModel;
        this.selectionModel = colorSelectionModel;
        firePropertyChange("selectionModel", colorSelectionModel2, colorSelectionModel);
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        if (getUIClassID().equals(uiClassID)) {
            byte writeObjCounter = (byte) (JComponent.getWriteObjCounter(this) - 1);
            JComponent.setWriteObjCounter(this, writeObjCounter);
            if (writeObjCounter == 0 && this.ui != null) {
                this.ui.installUI(this);
            }
        }
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    protected String paramString() {
        StringBuffer stringBuffer = new StringBuffer("");
        for (int i2 = 0; i2 < this.chooserPanels.length; i2++) {
            stringBuffer.append("[" + this.chooserPanels[i2].toString() + "]");
        }
        return super.paramString() + ",chooserPanels=" + stringBuffer.toString() + ",previewPanel=" + (this.previewPanel != null ? this.previewPanel.toString() : "");
    }

    @Override // java.awt.Component
    public AccessibleContext getAccessibleContext() {
        if (this.accessibleContext == null) {
            this.accessibleContext = new AccessibleJColorChooser();
        }
        return this.accessibleContext;
    }

    /* loaded from: rt.jar:javax/swing/JColorChooser$AccessibleJColorChooser.class */
    protected class AccessibleJColorChooser extends JComponent.AccessibleJComponent {
        protected AccessibleJColorChooser() {
            super();
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.COLOR_CHOOSER;
        }
    }
}
