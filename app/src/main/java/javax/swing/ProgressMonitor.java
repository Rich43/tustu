package javax.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.IllegalComponentStateException;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Locale;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleComponent;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.accessibility.AccessibleStateSet;
import javax.accessibility.AccessibleText;
import javax.accessibility.AccessibleValue;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.AttributeSet;

/* loaded from: rt.jar:javax/swing/ProgressMonitor.class */
public class ProgressMonitor implements Accessible {
    private ProgressMonitor root;
    private JDialog dialog;
    private JOptionPane pane;
    private JProgressBar myBar;
    private JLabel noteLabel;
    private Component parentComponent;
    private String note;
    private Object[] cancelOption;
    private Object message;
    private long T0;
    private int millisToDecideToPopup;
    private int millisToPopup;
    private int min;
    private int max;
    protected AccessibleContext accessibleContext;
    private AccessibleContext accessibleJOptionPane;

    public ProgressMonitor(Component component, Object obj, String str, int i2, int i3) {
        this(component, obj, str, i2, i3, null);
    }

    private ProgressMonitor(Component component, Object obj, String str, int i2, int i3, ProgressMonitor progressMonitor) {
        this.cancelOption = null;
        this.millisToDecideToPopup = 500;
        this.millisToPopup = 2000;
        this.accessibleContext = null;
        this.accessibleJOptionPane = null;
        this.min = i2;
        this.max = i3;
        this.parentComponent = component;
        this.cancelOption = new Object[1];
        this.cancelOption[0] = UIManager.getString("OptionPane.cancelButtonText");
        this.message = obj;
        this.note = str;
        if (progressMonitor != null) {
            this.root = progressMonitor.root != null ? progressMonitor.root : progressMonitor;
            this.T0 = this.root.T0;
            this.dialog = this.root.dialog;
            return;
        }
        this.T0 = System.currentTimeMillis();
    }

    /* loaded from: rt.jar:javax/swing/ProgressMonitor$ProgressOptionPane.class */
    private class ProgressOptionPane extends JOptionPane {
        ProgressOptionPane(Object obj) {
            super(obj, 1, -1, null, ProgressMonitor.this.cancelOption, null);
        }

        @Override // javax.swing.JOptionPane
        public int getMaxCharactersPerLineCount() {
            return 60;
        }

        @Override // javax.swing.JOptionPane
        public JDialog createDialog(Component component, String str) throws HeadlessException {
            JDialog jDialog;
            Window windowForComponent = JOptionPane.getWindowForComponent(component);
            if (windowForComponent instanceof Frame) {
                jDialog = new JDialog((Frame) windowForComponent, str, false);
            } else {
                jDialog = new JDialog((Dialog) windowForComponent, str, false);
            }
            if (windowForComponent instanceof SwingUtilities.SharedOwnerFrame) {
                jDialog.addWindowListener(SwingUtilities.getSharedOwnerFrameShutdownListener());
            }
            Container contentPane = jDialog.getContentPane();
            contentPane.setLayout(new BorderLayout());
            contentPane.add(this, BorderLayout.CENTER);
            jDialog.pack();
            jDialog.setLocationRelativeTo(component);
            jDialog.addWindowListener(new WindowAdapter() { // from class: javax.swing.ProgressMonitor.ProgressOptionPane.1
                boolean gotFocus = false;

                @Override // java.awt.event.WindowAdapter, java.awt.event.WindowListener
                public void windowClosing(WindowEvent windowEvent) {
                    ProgressOptionPane.this.setValue(ProgressMonitor.this.cancelOption[0]);
                }

                @Override // java.awt.event.WindowAdapter, java.awt.event.WindowListener
                public void windowActivated(WindowEvent windowEvent) {
                    if (!this.gotFocus) {
                        ProgressOptionPane.this.selectInitialValue();
                        this.gotFocus = true;
                    }
                }
            });
            final JDialog jDialog2 = jDialog;
            addPropertyChangeListener(new PropertyChangeListener() { // from class: javax.swing.ProgressMonitor.ProgressOptionPane.2
                @Override // java.beans.PropertyChangeListener
                public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                    if (jDialog2.isVisible() && propertyChangeEvent.getSource() == ProgressOptionPane.this) {
                        if (propertyChangeEvent.getPropertyName().equals("value") || propertyChangeEvent.getPropertyName().equals(JOptionPane.INPUT_VALUE_PROPERTY)) {
                            jDialog2.setVisible(false);
                            jDialog2.dispose();
                        }
                    }
                }
            });
            return jDialog;
        }

        @Override // javax.swing.JOptionPane, java.awt.Component
        public AccessibleContext getAccessibleContext() {
            return ProgressMonitor.this.getAccessibleContext();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public AccessibleContext getAccessibleJOptionPane() {
            return super.getAccessibleContext();
        }
    }

    public void setProgress(int i2) {
        int i3;
        if (i2 >= this.max) {
            close();
            return;
        }
        if (this.myBar != null) {
            this.myBar.setValue(i2);
            return;
        }
        long jCurrentTimeMillis = (int) (System.currentTimeMillis() - this.T0);
        if (jCurrentTimeMillis >= this.millisToDecideToPopup) {
            if (i2 > this.min) {
                i3 = (int) ((jCurrentTimeMillis * (this.max - this.min)) / (i2 - this.min));
            } else {
                i3 = this.millisToPopup;
            }
            if (i3 >= this.millisToPopup) {
                this.myBar = new JProgressBar();
                this.myBar.setMinimum(this.min);
                this.myBar.setMaximum(this.max);
                this.myBar.setValue(i2);
                if (this.note != null) {
                    this.noteLabel = new JLabel(this.note);
                }
                this.pane = new ProgressOptionPane(new Object[]{this.message, this.noteLabel, this.myBar});
                this.dialog = this.pane.createDialog(this.parentComponent, UIManager.getString("ProgressMonitor.progressText"));
                this.dialog.show();
            }
        }
    }

    public void close() {
        if (this.dialog != null) {
            this.dialog.setVisible(false);
            this.dialog.dispose();
            this.dialog = null;
            this.pane = null;
            this.myBar = null;
        }
    }

    public int getMinimum() {
        return this.min;
    }

    public void setMinimum(int i2) {
        if (this.myBar != null) {
            this.myBar.setMinimum(i2);
        }
        this.min = i2;
    }

    public int getMaximum() {
        return this.max;
    }

    public void setMaximum(int i2) {
        if (this.myBar != null) {
            this.myBar.setMaximum(i2);
        }
        this.max = i2;
    }

    public boolean isCanceled() {
        Object value;
        return this.pane != null && (value = this.pane.getValue()) != null && this.cancelOption.length == 1 && value.equals(this.cancelOption[0]);
    }

    public void setMillisToDecideToPopup(int i2) {
        this.millisToDecideToPopup = i2;
    }

    public int getMillisToDecideToPopup() {
        return this.millisToDecideToPopup;
    }

    public void setMillisToPopup(int i2) {
        this.millisToPopup = i2;
    }

    public int getMillisToPopup() {
        return this.millisToPopup;
    }

    public void setNote(String str) throws IllegalArgumentException {
        this.note = str;
        if (this.noteLabel != null) {
            this.noteLabel.setText(str);
        }
    }

    public String getNote() {
        return this.note;
    }

    @Override // javax.accessibility.Accessible
    public AccessibleContext getAccessibleContext() {
        if (this.accessibleContext == null) {
            this.accessibleContext = new AccessibleProgressMonitor();
        }
        if (this.pane != null && this.accessibleJOptionPane == null && (this.accessibleContext instanceof AccessibleProgressMonitor)) {
            ((AccessibleProgressMonitor) this.accessibleContext).optionPaneCreated();
        }
        return this.accessibleContext;
    }

    /* loaded from: rt.jar:javax/swing/ProgressMonitor$AccessibleProgressMonitor.class */
    protected class AccessibleProgressMonitor extends AccessibleContext implements AccessibleText, ChangeListener, PropertyChangeListener {
        private Object oldModelValue;

        protected AccessibleProgressMonitor() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void optionPaneCreated() {
            ProgressMonitor.this.accessibleJOptionPane = ((ProgressOptionPane) ProgressMonitor.this.pane).getAccessibleJOptionPane();
            if (ProgressMonitor.this.myBar != null) {
                ProgressMonitor.this.myBar.addChangeListener(this);
            }
            if (ProgressMonitor.this.noteLabel != null) {
                ProgressMonitor.this.noteLabel.addPropertyChangeListener(this);
            }
        }

        @Override // javax.swing.event.ChangeListener
        public void stateChanged(ChangeEvent changeEvent) {
            if (changeEvent != null && ProgressMonitor.this.myBar != null) {
                Integer numValueOf = Integer.valueOf(ProgressMonitor.this.myBar.getValue());
                firePropertyChange(AccessibleContext.ACCESSIBLE_VALUE_PROPERTY, this.oldModelValue, numValueOf);
                this.oldModelValue = numValueOf;
            }
        }

        @Override // java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            if (propertyChangeEvent.getSource() == ProgressMonitor.this.noteLabel && propertyChangeEvent.getPropertyName() == "text") {
                firePropertyChange(AccessibleContext.ACCESSIBLE_TEXT_PROPERTY, null, 0);
            }
        }

        @Override // javax.accessibility.AccessibleContext
        public String getAccessibleName() {
            if (this.accessibleName == null) {
                if (ProgressMonitor.this.accessibleJOptionPane != null) {
                    return ProgressMonitor.this.accessibleJOptionPane.getAccessibleName();
                }
                return null;
            }
            return this.accessibleName;
        }

        @Override // javax.accessibility.AccessibleContext
        public String getAccessibleDescription() {
            if (this.accessibleDescription == null) {
                if (ProgressMonitor.this.accessibleJOptionPane != null) {
                    return ProgressMonitor.this.accessibleJOptionPane.getAccessibleDescription();
                }
                return null;
            }
            return this.accessibleDescription;
        }

        @Override // javax.accessibility.AccessibleContext
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.PROGRESS_MONITOR;
        }

        @Override // javax.accessibility.AccessibleContext
        public AccessibleStateSet getAccessibleStateSet() {
            if (ProgressMonitor.this.accessibleJOptionPane != null) {
                return ProgressMonitor.this.accessibleJOptionPane.getAccessibleStateSet();
            }
            return null;
        }

        @Override // javax.accessibility.AccessibleContext
        public Accessible getAccessibleParent() {
            return ProgressMonitor.this.dialog;
        }

        private AccessibleContext getParentAccessibleContext() {
            if (ProgressMonitor.this.dialog != null) {
                return ProgressMonitor.this.dialog.getAccessibleContext();
            }
            return null;
        }

        @Override // javax.accessibility.AccessibleContext
        public int getAccessibleIndexInParent() {
            if (ProgressMonitor.this.accessibleJOptionPane != null) {
                return ProgressMonitor.this.accessibleJOptionPane.getAccessibleIndexInParent();
            }
            return -1;
        }

        @Override // javax.accessibility.AccessibleContext
        public int getAccessibleChildrenCount() {
            AccessibleContext panelAccessibleContext = getPanelAccessibleContext();
            if (panelAccessibleContext != null) {
                return panelAccessibleContext.getAccessibleChildrenCount();
            }
            return 0;
        }

        @Override // javax.accessibility.AccessibleContext
        public Accessible getAccessibleChild(int i2) {
            AccessibleContext panelAccessibleContext = getPanelAccessibleContext();
            if (panelAccessibleContext != null) {
                return panelAccessibleContext.getAccessibleChild(i2);
            }
            return null;
        }

        private AccessibleContext getPanelAccessibleContext() {
            if (ProgressMonitor.this.myBar != null) {
                Container parent = ProgressMonitor.this.myBar.getParent();
                if (parent instanceof Accessible) {
                    return parent.getAccessibleContext();
                }
                return null;
            }
            return null;
        }

        @Override // javax.accessibility.AccessibleContext
        public Locale getLocale() throws IllegalComponentStateException {
            if (ProgressMonitor.this.accessibleJOptionPane != null) {
                return ProgressMonitor.this.accessibleJOptionPane.getLocale();
            }
            return null;
        }

        @Override // javax.accessibility.AccessibleContext
        public AccessibleComponent getAccessibleComponent() {
            if (ProgressMonitor.this.accessibleJOptionPane != null) {
                return ProgressMonitor.this.accessibleJOptionPane.getAccessibleComponent();
            }
            return null;
        }

        @Override // javax.accessibility.AccessibleContext
        public AccessibleValue getAccessibleValue() {
            if (ProgressMonitor.this.myBar != null) {
                return ProgressMonitor.this.myBar.getAccessibleContext().getAccessibleValue();
            }
            return null;
        }

        @Override // javax.accessibility.AccessibleContext
        public AccessibleText getAccessibleText() {
            if (getNoteLabelAccessibleText() != null) {
                return this;
            }
            return null;
        }

        private AccessibleText getNoteLabelAccessibleText() {
            if (ProgressMonitor.this.noteLabel != null) {
                return ProgressMonitor.this.noteLabel.getAccessibleContext().getAccessibleText();
            }
            return null;
        }

        @Override // javax.accessibility.AccessibleText
        public int getIndexAtPoint(Point point) {
            Point pointConvertPoint;
            AccessibleText noteLabelAccessibleText = getNoteLabelAccessibleText();
            if (noteLabelAccessibleText != null && sameWindowAncestor(ProgressMonitor.this.pane, ProgressMonitor.this.noteLabel) && (pointConvertPoint = SwingUtilities.convertPoint(ProgressMonitor.this.pane, point, ProgressMonitor.this.noteLabel)) != null) {
                return noteLabelAccessibleText.getIndexAtPoint(pointConvertPoint);
            }
            return -1;
        }

        @Override // javax.accessibility.AccessibleText
        public Rectangle getCharacterBounds(int i2) {
            Rectangle characterBounds;
            AccessibleText noteLabelAccessibleText = getNoteLabelAccessibleText();
            if (noteLabelAccessibleText != null && sameWindowAncestor(ProgressMonitor.this.pane, ProgressMonitor.this.noteLabel) && (characterBounds = noteLabelAccessibleText.getCharacterBounds(i2)) != null) {
                return SwingUtilities.convertRectangle(ProgressMonitor.this.noteLabel, characterBounds, ProgressMonitor.this.pane);
            }
            return null;
        }

        private boolean sameWindowAncestor(Component component, Component component2) {
            return (component == null || component2 == null || SwingUtilities.getWindowAncestor(component) != SwingUtilities.getWindowAncestor(component2)) ? false : true;
        }

        @Override // javax.accessibility.AccessibleText
        public int getCharCount() {
            AccessibleText noteLabelAccessibleText = getNoteLabelAccessibleText();
            if (noteLabelAccessibleText != null) {
                return noteLabelAccessibleText.getCharCount();
            }
            return -1;
        }

        @Override // javax.accessibility.AccessibleText
        public int getCaretPosition() {
            AccessibleText noteLabelAccessibleText = getNoteLabelAccessibleText();
            if (noteLabelAccessibleText != null) {
                return noteLabelAccessibleText.getCaretPosition();
            }
            return -1;
        }

        @Override // javax.accessibility.AccessibleText
        public String getAtIndex(int i2, int i3) {
            AccessibleText noteLabelAccessibleText = getNoteLabelAccessibleText();
            if (noteLabelAccessibleText != null) {
                return noteLabelAccessibleText.getAtIndex(i2, i3);
            }
            return null;
        }

        @Override // javax.accessibility.AccessibleText
        public String getAfterIndex(int i2, int i3) {
            AccessibleText noteLabelAccessibleText = getNoteLabelAccessibleText();
            if (noteLabelAccessibleText != null) {
                return noteLabelAccessibleText.getAfterIndex(i2, i3);
            }
            return null;
        }

        @Override // javax.accessibility.AccessibleText
        public String getBeforeIndex(int i2, int i3) {
            AccessibleText noteLabelAccessibleText = getNoteLabelAccessibleText();
            if (noteLabelAccessibleText != null) {
                return noteLabelAccessibleText.getBeforeIndex(i2, i3);
            }
            return null;
        }

        @Override // javax.accessibility.AccessibleText
        public AttributeSet getCharacterAttribute(int i2) {
            AccessibleText noteLabelAccessibleText = getNoteLabelAccessibleText();
            if (noteLabelAccessibleText != null) {
                return noteLabelAccessibleText.getCharacterAttribute(i2);
            }
            return null;
        }

        @Override // javax.accessibility.AccessibleText
        public int getSelectionStart() {
            AccessibleText noteLabelAccessibleText = getNoteLabelAccessibleText();
            if (noteLabelAccessibleText != null) {
                return noteLabelAccessibleText.getSelectionStart();
            }
            return -1;
        }

        @Override // javax.accessibility.AccessibleText
        public int getSelectionEnd() {
            AccessibleText noteLabelAccessibleText = getNoteLabelAccessibleText();
            if (noteLabelAccessibleText != null) {
                return noteLabelAccessibleText.getSelectionEnd();
            }
            return -1;
        }

        @Override // javax.accessibility.AccessibleText
        public String getSelectedText() {
            AccessibleText noteLabelAccessibleText = getNoteLabelAccessibleText();
            if (noteLabelAccessibleText != null) {
                return noteLabelAccessibleText.getSelectedText();
            }
            return null;
        }
    }
}
