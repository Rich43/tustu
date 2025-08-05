package javax.swing.plaf.basic;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.LookAndFeel;
import javax.swing.TransferHandler;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.colorchooser.ColorChooserComponentFactory;
import javax.swing.colorchooser.ColorSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ColorChooserUI;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import sun.swing.DefaultLookup;

/* loaded from: rt.jar:javax/swing/plaf/basic/BasicColorChooserUI.class */
public class BasicColorChooserUI extends ColorChooserUI {
    protected JColorChooser chooser;
    JTabbedPane tabbedPane;
    JPanel singlePanel;
    JPanel previewPanelHolder;
    JComponent previewPanel;
    boolean isMultiPanel = false;
    private static TransferHandler defaultTransferHandler = new ColorTransferHandler();
    protected AbstractColorChooserPanel[] defaultChoosers;
    protected ChangeListener previewListener;
    protected PropertyChangeListener propertyChangeListener;
    private Handler handler;

    public static ComponentUI createUI(JComponent jComponent) {
        return new BasicColorChooserUI();
    }

    protected AbstractColorChooserPanel[] createDefaultChoosers() {
        return ColorChooserComponentFactory.getDefaultChooserPanels();
    }

    protected void uninstallDefaultChoosers() {
        for (AbstractColorChooserPanel abstractColorChooserPanel : this.chooser.getChooserPanels()) {
            this.chooser.removeChooserPanel(abstractColorChooserPanel);
        }
    }

    @Override // javax.swing.plaf.ComponentUI
    public void installUI(JComponent jComponent) {
        this.chooser = (JColorChooser) jComponent;
        super.installUI(jComponent);
        installDefaults();
        installListeners();
        this.tabbedPane = new JTabbedPane();
        this.tabbedPane.setName("ColorChooser.tabPane");
        this.tabbedPane.setInheritsPopupMenu(true);
        this.tabbedPane.getAccessibleContext().setAccessibleDescription(this.tabbedPane.getName());
        this.singlePanel = new JPanel(new CenterLayout());
        this.singlePanel.setName("ColorChooser.panel");
        this.singlePanel.setInheritsPopupMenu(true);
        this.chooser.setLayout(new BorderLayout());
        this.defaultChoosers = createDefaultChoosers();
        this.chooser.setChooserPanels(this.defaultChoosers);
        this.previewPanelHolder = new JPanel(new CenterLayout());
        this.previewPanelHolder.setName("ColorChooser.previewPanelHolder");
        if (DefaultLookup.getBoolean(this.chooser, this, "ColorChooser.showPreviewPanelText", true)) {
            this.previewPanelHolder.setBorder(new TitledBorder(UIManager.getString("ColorChooser.previewText", this.chooser.getLocale())));
        }
        this.previewPanelHolder.setInheritsPopupMenu(true);
        installPreviewPanel();
        this.chooser.applyComponentOrientation(jComponent.getComponentOrientation());
    }

    @Override // javax.swing.plaf.ComponentUI
    public void uninstallUI(JComponent jComponent) {
        this.chooser.remove(this.tabbedPane);
        this.chooser.remove(this.singlePanel);
        this.chooser.remove(this.previewPanelHolder);
        uninstallDefaultChoosers();
        uninstallListeners();
        uninstallPreviewPanel();
        uninstallDefaults();
        this.previewPanelHolder = null;
        this.previewPanel = null;
        this.defaultChoosers = null;
        this.chooser = null;
        this.tabbedPane = null;
        this.handler = null;
    }

    protected void installPreviewPanel() {
        JComponent previewPanel = this.chooser.getPreviewPanel();
        if (previewPanel == null) {
            previewPanel = ColorChooserComponentFactory.getPreviewPanel();
        } else if (JPanel.class.equals(previewPanel.getClass()) && 0 == previewPanel.getComponentCount()) {
            previewPanel = null;
        }
        this.previewPanel = previewPanel;
        if (previewPanel != null) {
            this.chooser.add(this.previewPanelHolder, "South");
            previewPanel.setForeground(this.chooser.getColor());
            this.previewPanelHolder.add(previewPanel);
            previewPanel.addMouseListener(getHandler());
            previewPanel.setInheritsPopupMenu(true);
        }
    }

    protected void uninstallPreviewPanel() {
        if (this.previewPanel != null) {
            this.previewPanel.removeMouseListener(getHandler());
            this.previewPanelHolder.remove(this.previewPanel);
        }
        this.chooser.remove(this.previewPanelHolder);
    }

    protected void installDefaults() {
        LookAndFeel.installColorsAndFont(this.chooser, "ColorChooser.background", "ColorChooser.foreground", "ColorChooser.font");
        LookAndFeel.installProperty(this.chooser, "opaque", Boolean.TRUE);
        TransferHandler transferHandler = this.chooser.getTransferHandler();
        if (transferHandler == null || (transferHandler instanceof UIResource)) {
            this.chooser.setTransferHandler(defaultTransferHandler);
        }
    }

    protected void uninstallDefaults() {
        if (this.chooser.getTransferHandler() instanceof UIResource) {
            this.chooser.setTransferHandler(null);
        }
    }

    protected void installListeners() {
        this.propertyChangeListener = createPropertyChangeListener();
        this.chooser.addPropertyChangeListener(this.propertyChangeListener);
        this.previewListener = getHandler();
        this.chooser.getSelectionModel().addChangeListener(this.previewListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Handler getHandler() {
        if (this.handler == null) {
            this.handler = new Handler();
        }
        return this.handler;
    }

    protected PropertyChangeListener createPropertyChangeListener() {
        return getHandler();
    }

    protected void uninstallListeners() {
        this.chooser.removePropertyChangeListener(this.propertyChangeListener);
        this.chooser.getSelectionModel().removeChangeListener(this.previewListener);
        this.previewListener = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void selectionChanged(ColorSelectionModel colorSelectionModel) {
        JComponent previewPanel = this.chooser.getPreviewPanel();
        if (previewPanel != null) {
            previewPanel.setForeground(colorSelectionModel.getSelectedColor());
            previewPanel.repaint();
        }
        AbstractColorChooserPanel[] chooserPanels = this.chooser.getChooserPanels();
        if (chooserPanels != null) {
            for (AbstractColorChooserPanel abstractColorChooserPanel : chooserPanels) {
                if (abstractColorChooserPanel != null) {
                    abstractColorChooserPanel.updateChooser();
                }
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicColorChooserUI$Handler.class */
    private class Handler implements ChangeListener, MouseListener, PropertyChangeListener {
        private Handler() {
        }

        @Override // javax.swing.event.ChangeListener
        public void stateChanged(ChangeEvent changeEvent) {
            BasicColorChooserUI.this.selectionChanged((ColorSelectionModel) changeEvent.getSource());
        }

        @Override // java.awt.event.MouseListener
        public void mousePressed(MouseEvent mouseEvent) {
            if (BasicColorChooserUI.this.chooser.getDragEnabled()) {
                BasicColorChooserUI.this.chooser.getTransferHandler().exportAsDrag(BasicColorChooserUI.this.chooser, mouseEvent, 1);
            }
        }

        @Override // java.awt.event.MouseListener
        public void mouseReleased(MouseEvent mouseEvent) {
        }

        @Override // java.awt.event.MouseListener
        public void mouseClicked(MouseEvent mouseEvent) {
        }

        @Override // java.awt.event.MouseListener
        public void mouseEntered(MouseEvent mouseEvent) {
        }

        @Override // java.awt.event.MouseListener
        public void mouseExited(MouseEvent mouseEvent) {
        }

        @Override // java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            String propertyName = propertyChangeEvent.getPropertyName();
            if (propertyName == JColorChooser.CHOOSER_PANELS_PROPERTY) {
                AbstractColorChooserPanel[] abstractColorChooserPanelArr = (AbstractColorChooserPanel[]) propertyChangeEvent.getOldValue();
                AbstractColorChooserPanel[] abstractColorChooserPanelArr2 = (AbstractColorChooserPanel[]) propertyChangeEvent.getNewValue();
                for (int i2 = 0; i2 < abstractColorChooserPanelArr.length; i2++) {
                    Container parent = abstractColorChooserPanelArr[i2].getParent();
                    if (parent != null) {
                        Container parent2 = parent.getParent();
                        if (parent2 != null) {
                            parent2.remove(parent);
                        }
                        abstractColorChooserPanelArr[i2].uninstallChooserPanel(BasicColorChooserUI.this.chooser);
                    }
                }
                int length = abstractColorChooserPanelArr2.length;
                if (length == 0) {
                    BasicColorChooserUI.this.chooser.remove(BasicColorChooserUI.this.tabbedPane);
                    return;
                }
                if (length == 1) {
                    BasicColorChooserUI.this.chooser.remove(BasicColorChooserUI.this.tabbedPane);
                    JPanel jPanel = new JPanel(new CenterLayout());
                    jPanel.setInheritsPopupMenu(true);
                    jPanel.add(abstractColorChooserPanelArr2[0]);
                    BasicColorChooserUI.this.singlePanel.add(jPanel, BorderLayout.CENTER);
                    BasicColorChooserUI.this.chooser.add(BasicColorChooserUI.this.singlePanel);
                } else {
                    if (abstractColorChooserPanelArr.length < 2) {
                        BasicColorChooserUI.this.chooser.remove(BasicColorChooserUI.this.singlePanel);
                        BasicColorChooserUI.this.chooser.add(BasicColorChooserUI.this.tabbedPane, BorderLayout.CENTER);
                    }
                    for (int i3 = 0; i3 < abstractColorChooserPanelArr2.length; i3++) {
                        JPanel jPanel2 = new JPanel(new CenterLayout());
                        jPanel2.setInheritsPopupMenu(true);
                        String displayName = abstractColorChooserPanelArr2[i3].getDisplayName();
                        int mnemonic = abstractColorChooserPanelArr2[i3].getMnemonic();
                        jPanel2.add(abstractColorChooserPanelArr2[i3]);
                        BasicColorChooserUI.this.tabbedPane.addTab(displayName, jPanel2);
                        if (mnemonic > 0) {
                            BasicColorChooserUI.this.tabbedPane.setMnemonicAt(i3, mnemonic);
                            int displayedMnemonicIndex = abstractColorChooserPanelArr2[i3].getDisplayedMnemonicIndex();
                            if (displayedMnemonicIndex >= 0) {
                                BasicColorChooserUI.this.tabbedPane.setDisplayedMnemonicIndexAt(i3, displayedMnemonicIndex);
                            }
                        }
                    }
                }
                BasicColorChooserUI.this.chooser.applyComponentOrientation(BasicColorChooserUI.this.chooser.getComponentOrientation());
                for (AbstractColorChooserPanel abstractColorChooserPanel : abstractColorChooserPanelArr2) {
                    abstractColorChooserPanel.installChooserPanel(BasicColorChooserUI.this.chooser);
                }
                return;
            }
            if (propertyName == JColorChooser.PREVIEW_PANEL_PROPERTY) {
                BasicColorChooserUI.this.uninstallPreviewPanel();
                BasicColorChooserUI.this.installPreviewPanel();
                return;
            }
            if (propertyName == "selectionModel") {
                ((ColorSelectionModel) propertyChangeEvent.getOldValue()).removeChangeListener(BasicColorChooserUI.this.previewListener);
                ColorSelectionModel colorSelectionModel = (ColorSelectionModel) propertyChangeEvent.getNewValue();
                colorSelectionModel.addChangeListener(BasicColorChooserUI.this.previewListener);
                BasicColorChooserUI.this.selectionChanged(colorSelectionModel);
                return;
            }
            if (propertyName == "componentOrientation") {
                ComponentOrientation componentOrientation = (ComponentOrientation) propertyChangeEvent.getNewValue();
                JColorChooser jColorChooser = (JColorChooser) propertyChangeEvent.getSource();
                if (componentOrientation != ((ComponentOrientation) propertyChangeEvent.getOldValue())) {
                    jColorChooser.applyComponentOrientation(componentOrientation);
                    jColorChooser.updateUI();
                }
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicColorChooserUI$PropertyHandler.class */
    public class PropertyHandler implements PropertyChangeListener {
        public PropertyHandler() {
        }

        @Override // java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            BasicColorChooserUI.this.getHandler().propertyChange(propertyChangeEvent);
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicColorChooserUI$ColorTransferHandler.class */
    static class ColorTransferHandler extends TransferHandler implements UIResource {
        ColorTransferHandler() {
            super("color");
        }
    }
}
