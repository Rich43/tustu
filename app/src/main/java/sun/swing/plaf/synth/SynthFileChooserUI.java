package sun.swing.plaf.synth;

import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicFileChooserUI;
import javax.swing.plaf.synth.ColorType;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthLookAndFeel;
import javax.swing.plaf.synth.SynthStyle;
import javax.swing.plaf.synth.SynthUI;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:sun/swing/plaf/synth/SynthFileChooserUI.class */
public abstract class SynthFileChooserUI extends BasicFileChooserUI implements SynthUI {
    private JButton approveButton;
    private JButton cancelButton;
    private SynthStyle style;
    private Action fileNameCompletionAction;
    private FileFilter actualFileFilter;
    private GlobFilter globFilter;
    private String fileNameCompletionString;

    protected abstract ActionMap createActionMap();

    @Override // javax.swing.plaf.basic.BasicFileChooserUI
    public abstract void setFileName(String str);

    @Override // javax.swing.plaf.basic.BasicFileChooserUI
    public abstract String getFileName();

    public static ComponentUI createUI(JComponent jComponent) {
        return new SynthFileChooserUIImpl((JFileChooser) jComponent);
    }

    public SynthFileChooserUI(JFileChooser jFileChooser) {
        super(jFileChooser);
        this.fileNameCompletionAction = new FileNameCompletionAction();
        this.actualFileFilter = null;
        this.globFilter = null;
    }

    @Override // javax.swing.plaf.synth.SynthUI
    public SynthContext getContext(JComponent jComponent) {
        return new SynthContext(jComponent, Region.FILE_CHOOSER, this.style, getComponentState(jComponent));
    }

    protected SynthContext getContext(JComponent jComponent, int i2) {
        SynthLookAndFeel.getRegion(jComponent);
        return new SynthContext(jComponent, Region.FILE_CHOOSER, this.style, i2);
    }

    private Region getRegion(JComponent jComponent) {
        return SynthLookAndFeel.getRegion(jComponent);
    }

    private int getComponentState(JComponent jComponent) {
        if (jComponent.isEnabled()) {
            if (jComponent.isFocusOwner()) {
                return 257;
            }
            return 1;
        }
        return 8;
    }

    private void updateStyle(JComponent jComponent) {
        SynthStyle style = SynthLookAndFeel.getStyleFactory().getStyle(jComponent, Region.FILE_CHOOSER);
        if (style != this.style) {
            if (this.style != null) {
                this.style.uninstallDefaults(getContext(jComponent, 1));
            }
            this.style = style;
            SynthContext context = getContext(jComponent, 1);
            this.style.installDefaults(context);
            Border border = jComponent.getBorder();
            if (border == null || (border instanceof UIResource)) {
                jComponent.setBorder(new UIBorder(this.style.getInsets(context, null)));
            }
            this.directoryIcon = this.style.getIcon(context, "FileView.directoryIcon");
            this.fileIcon = this.style.getIcon(context, "FileView.fileIcon");
            this.computerIcon = this.style.getIcon(context, "FileView.computerIcon");
            this.hardDriveIcon = this.style.getIcon(context, "FileView.hardDriveIcon");
            this.floppyDriveIcon = this.style.getIcon(context, "FileView.floppyDriveIcon");
            this.newFolderIcon = this.style.getIcon(context, "FileChooser.newFolderIcon");
            this.upFolderIcon = this.style.getIcon(context, "FileChooser.upFolderIcon");
            this.homeFolderIcon = this.style.getIcon(context, "FileChooser.homeFolderIcon");
            this.detailsViewIcon = this.style.getIcon(context, "FileChooser.detailsViewIcon");
            this.listViewIcon = this.style.getIcon(context, "FileChooser.listViewIcon");
        }
    }

    @Override // javax.swing.plaf.basic.BasicFileChooserUI, javax.swing.plaf.ComponentUI
    public void installUI(JComponent jComponent) {
        super.installUI(jComponent);
        SwingUtilities.replaceUIActionMap(jComponent, createActionMap());
    }

    @Override // javax.swing.plaf.basic.BasicFileChooserUI
    public void installComponents(JFileChooser jFileChooser) {
        SynthContext context = getContext(jFileChooser, 1);
        this.cancelButton = new JButton(this.cancelButtonText);
        this.cancelButton.setName("SynthFileChooser.cancelButton");
        this.cancelButton.setIcon(context.getStyle().getIcon(context, "FileChooser.cancelIcon"));
        this.cancelButton.setMnemonic(this.cancelButtonMnemonic);
        this.cancelButton.setToolTipText(this.cancelButtonToolTipText);
        this.cancelButton.addActionListener(getCancelSelectionAction());
        this.approveButton = new JButton(getApproveButtonText(jFileChooser));
        this.approveButton.setName("SynthFileChooser.approveButton");
        this.approveButton.setIcon(context.getStyle().getIcon(context, "FileChooser.okIcon"));
        this.approveButton.setMnemonic(getApproveButtonMnemonic(jFileChooser));
        this.approveButton.setToolTipText(getApproveButtonToolTipText(jFileChooser));
        this.approveButton.addActionListener(getApproveSelectionAction());
    }

    @Override // javax.swing.plaf.basic.BasicFileChooserUI
    public void uninstallComponents(JFileChooser jFileChooser) {
        jFileChooser.removeAll();
    }

    @Override // javax.swing.plaf.basic.BasicFileChooserUI
    protected void installListeners(JFileChooser jFileChooser) {
        super.installListeners(jFileChooser);
        getModel().addListDataListener(new ListDataListener() { // from class: sun.swing.plaf.synth.SynthFileChooserUI.1
            @Override // javax.swing.event.ListDataListener
            public void contentsChanged(ListDataEvent listDataEvent) {
                SynthFileChooserUI.this.new DelayedSelectionUpdater();
            }

            @Override // javax.swing.event.ListDataListener
            public void intervalAdded(ListDataEvent listDataEvent) {
                SynthFileChooserUI.this.new DelayedSelectionUpdater();
            }

            @Override // javax.swing.event.ListDataListener
            public void intervalRemoved(ListDataEvent listDataEvent) {
            }
        });
    }

    /* loaded from: rt.jar:sun/swing/plaf/synth/SynthFileChooserUI$DelayedSelectionUpdater.class */
    private class DelayedSelectionUpdater implements Runnable {
        DelayedSelectionUpdater() {
            SwingUtilities.invokeLater(this);
        }

        @Override // java.lang.Runnable
        public void run() {
            SynthFileChooserUI.this.updateFileNameCompletion();
        }
    }

    @Override // javax.swing.plaf.basic.BasicFileChooserUI
    protected void installDefaults(JFileChooser jFileChooser) {
        super.installDefaults(jFileChooser);
        updateStyle(jFileChooser);
    }

    @Override // javax.swing.plaf.basic.BasicFileChooserUI
    protected void uninstallDefaults(JFileChooser jFileChooser) {
        super.uninstallDefaults(jFileChooser);
        this.style.uninstallDefaults(getContext(getFileChooser(), 1));
        this.style = null;
    }

    @Override // javax.swing.plaf.basic.BasicFileChooserUI
    protected void installIcons(JFileChooser jFileChooser) {
    }

    @Override // javax.swing.plaf.ComponentUI
    public void update(Graphics graphics, JComponent jComponent) {
        SynthContext context = getContext(jComponent);
        if (jComponent.isOpaque()) {
            graphics.setColor(this.style.getColor(context, ColorType.BACKGROUND));
            graphics.fillRect(0, 0, jComponent.getWidth(), jComponent.getHeight());
        }
        this.style.getPainter(context).paintFileChooserBackground(context, graphics, 0, 0, jComponent.getWidth(), jComponent.getHeight());
        paint(context, graphics);
    }

    @Override // javax.swing.plaf.synth.SynthUI
    public void paintBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
    }

    @Override // javax.swing.plaf.ComponentUI
    public void paint(Graphics graphics, JComponent jComponent) {
        paint(getContext(jComponent), graphics);
    }

    protected void paint(SynthContext synthContext, Graphics graphics) {
    }

    protected void doSelectedFileChanged(PropertyChangeEvent propertyChangeEvent) {
    }

    protected void doSelectedFilesChanged(PropertyChangeEvent propertyChangeEvent) {
    }

    protected void doDirectoryChanged(PropertyChangeEvent propertyChangeEvent) {
    }

    protected void doAccessoryChanged(PropertyChangeEvent propertyChangeEvent) {
    }

    protected void doFileSelectionModeChanged(PropertyChangeEvent propertyChangeEvent) {
    }

    protected void doMultiSelectionChanged(PropertyChangeEvent propertyChangeEvent) {
        if (!getFileChooser().isMultiSelectionEnabled()) {
            getFileChooser().setSelectedFiles(null);
        }
    }

    protected void doControlButtonsChanged(PropertyChangeEvent propertyChangeEvent) {
        if (getFileChooser().getControlButtonsAreShown()) {
            this.approveButton.setText(getApproveButtonText(getFileChooser()));
            this.approveButton.setToolTipText(getApproveButtonToolTipText(getFileChooser()));
            this.approveButton.setMnemonic(getApproveButtonMnemonic(getFileChooser()));
        }
    }

    protected void doAncestorChanged(PropertyChangeEvent propertyChangeEvent) {
    }

    @Override // javax.swing.plaf.basic.BasicFileChooserUI
    public PropertyChangeListener createPropertyChangeListener(JFileChooser jFileChooser) {
        return new SynthFCPropertyChangeListener();
    }

    /* loaded from: rt.jar:sun/swing/plaf/synth/SynthFileChooserUI$SynthFCPropertyChangeListener.class */
    private class SynthFCPropertyChangeListener implements PropertyChangeListener {
        private SynthFCPropertyChangeListener() {
        }

        @Override // java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            String propertyName = propertyChangeEvent.getPropertyName();
            if (propertyName.equals(JFileChooser.FILE_SELECTION_MODE_CHANGED_PROPERTY)) {
                SynthFileChooserUI.this.doFileSelectionModeChanged(propertyChangeEvent);
                return;
            }
            if (propertyName.equals(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY)) {
                SynthFileChooserUI.this.doSelectedFileChanged(propertyChangeEvent);
                return;
            }
            if (propertyName.equals(JFileChooser.SELECTED_FILES_CHANGED_PROPERTY)) {
                SynthFileChooserUI.this.doSelectedFilesChanged(propertyChangeEvent);
                return;
            }
            if (propertyName.equals(JFileChooser.DIRECTORY_CHANGED_PROPERTY)) {
                SynthFileChooserUI.this.doDirectoryChanged(propertyChangeEvent);
                return;
            }
            if (propertyName == JFileChooser.MULTI_SELECTION_ENABLED_CHANGED_PROPERTY) {
                SynthFileChooserUI.this.doMultiSelectionChanged(propertyChangeEvent);
                return;
            }
            if (propertyName == JFileChooser.ACCESSORY_CHANGED_PROPERTY) {
                SynthFileChooserUI.this.doAccessoryChanged(propertyChangeEvent);
                return;
            }
            if (propertyName == JFileChooser.APPROVE_BUTTON_TEXT_CHANGED_PROPERTY || propertyName == JFileChooser.APPROVE_BUTTON_TOOL_TIP_TEXT_CHANGED_PROPERTY || propertyName == JFileChooser.DIALOG_TYPE_CHANGED_PROPERTY || propertyName == JFileChooser.CONTROL_BUTTONS_ARE_SHOWN_CHANGED_PROPERTY) {
                SynthFileChooserUI.this.doControlButtonsChanged(propertyChangeEvent);
                return;
            }
            if (!propertyName.equals("componentOrientation")) {
                if (propertyName.equals("ancestor")) {
                    SynthFileChooserUI.this.doAncestorChanged(propertyChangeEvent);
                }
            } else {
                ComponentOrientation componentOrientation = (ComponentOrientation) propertyChangeEvent.getNewValue();
                JFileChooser jFileChooser = (JFileChooser) propertyChangeEvent.getSource();
                if (componentOrientation != ((ComponentOrientation) propertyChangeEvent.getOldValue())) {
                    jFileChooser.applyComponentOrientation(componentOrientation);
                }
            }
        }
    }

    /* loaded from: rt.jar:sun/swing/plaf/synth/SynthFileChooserUI$FileNameCompletionAction.class */
    private class FileNameCompletionAction extends AbstractAction {
        protected FileNameCompletionAction() {
            super("fileNameCompletion");
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            JFileChooser fileChooser = SynthFileChooserUI.this.getFileChooser();
            String fileName = SynthFileChooserUI.this.getFileName();
            if (fileName != null) {
                fileName = fileName.trim();
            }
            SynthFileChooserUI.this.resetGlobFilter();
            if (fileName != null && !fileName.equals("")) {
                if (fileChooser.isMultiSelectionEnabled() && fileName.startsWith(PdfOps.DOUBLE_QUOTE__TOKEN)) {
                    return;
                }
                FileFilter fileFilter = fileChooser.getFileFilter();
                if (SynthFileChooserUI.this.globFilter == null) {
                    SynthFileChooserUI.this.globFilter = SynthFileChooserUI.this.new GlobFilter();
                }
                try {
                    SynthFileChooserUI.this.globFilter.setPattern(!SynthFileChooserUI.isGlobPattern(fileName) ? fileName + "*" : fileName);
                    if (!(fileFilter instanceof GlobFilter)) {
                        SynthFileChooserUI.this.actualFileFilter = fileFilter;
                    }
                    fileChooser.setFileFilter(null);
                    fileChooser.setFileFilter(SynthFileChooserUI.this.globFilter);
                    SynthFileChooserUI.this.fileNameCompletionString = fileName;
                } catch (PatternSyntaxException e2) {
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateFileNameCompletion() {
        if (this.fileNameCompletionString != null && this.fileNameCompletionString.equals(getFileName())) {
            String commonStartString = getCommonStartString((File[]) getModel().getFiles().toArray(new File[0]));
            if (commonStartString != null && commonStartString.startsWith(this.fileNameCompletionString)) {
                setFileName(commonStartString);
            }
            this.fileNameCompletionString = null;
        }
    }

    private String getCommonStartString(File[] fileArr) {
        String str = null;
        String strSubstring = null;
        int i2 = 0;
        if (fileArr.length == 0) {
            return null;
        }
        while (true) {
            for (int i3 = 0; i3 < fileArr.length; i3++) {
                String name = fileArr[i3].getName();
                if (i3 == 0) {
                    if (name.length() == i2) {
                        return str;
                    }
                    strSubstring = name.substring(0, i2 + 1);
                }
                if (!name.startsWith(strSubstring)) {
                    return str;
                }
            }
            str = strSubstring;
            i2++;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetGlobFilter() {
        if (this.actualFileFilter != null) {
            JFileChooser fileChooser = getFileChooser();
            FileFilter fileFilter = fileChooser.getFileFilter();
            if (fileFilter != null && fileFilter.equals(this.globFilter)) {
                fileChooser.setFileFilter(this.actualFileFilter);
                fileChooser.removeChoosableFileFilter(this.globFilter);
            }
            this.actualFileFilter = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isGlobPattern(String str) {
        return (File.separatorChar == '\\' && str.indexOf(42) >= 0) || (File.separatorChar == '/' && (str.indexOf(42) >= 0 || str.indexOf(63) >= 0 || str.indexOf(91) >= 0));
    }

    /* loaded from: rt.jar:sun/swing/plaf/synth/SynthFileChooserUI$GlobFilter.class */
    class GlobFilter extends FileFilter {
        Pattern pattern;
        String globPattern;

        GlobFilter() {
        }

        public void setPattern(String str) {
            char[] charArray = str.toCharArray();
            char[] cArr = new char[charArray.length * 2];
            boolean z2 = File.separatorChar == '\\';
            boolean z3 = false;
            int i2 = 0;
            this.globPattern = str;
            if (z2) {
                int length = charArray.length;
                if (str.endsWith("*.*")) {
                    length -= 2;
                }
                for (int i3 = 0; i3 < length; i3++) {
                    if (charArray[i3] == '*') {
                        int i4 = i2;
                        i2++;
                        cArr[i4] = '.';
                    }
                    int i5 = i2;
                    i2++;
                    cArr[i5] = charArray[i3];
                }
            } else {
                int i6 = 0;
                while (i6 < charArray.length) {
                    switch (charArray[i6]) {
                        case '*':
                            if (!z3) {
                                int i7 = i2;
                                i2++;
                                cArr[i7] = '.';
                            }
                            int i8 = i2;
                            i2++;
                            cArr[i8] = '*';
                            break;
                        case '?':
                            int i9 = i2;
                            i2++;
                            cArr[i9] = z3 ? '?' : '.';
                            break;
                        case '[':
                            z3 = true;
                            int i10 = i2;
                            i2++;
                            cArr[i10] = charArray[i6];
                            if (i6 < charArray.length - 1) {
                                switch (charArray[i6 + 1]) {
                                    case '!':
                                    case '^':
                                        i2++;
                                        cArr[i2] = '^';
                                        i6++;
                                        break;
                                    case ']':
                                        i2++;
                                        i6++;
                                        cArr[i2] = charArray[i6];
                                        break;
                                }
                            } else {
                                break;
                            }
                            break;
                        case '\\':
                            if (i6 == 0 && charArray.length > 1 && charArray[1] == '~') {
                                int i11 = i2;
                                i2++;
                                i6++;
                                cArr[i11] = charArray[i6];
                                break;
                            } else {
                                int i12 = i2;
                                int i13 = i2 + 1;
                                cArr[i12] = '\\';
                                if (i6 < charArray.length - 1 && "*?[]".indexOf(charArray[i6 + 1]) >= 0) {
                                    i2 = i13 + 1;
                                    i6++;
                                    cArr[i13] = charArray[i6];
                                    break;
                                } else {
                                    i2 = i13 + 1;
                                    cArr[i13] = '\\';
                                    break;
                                }
                            }
                        case ']':
                            int i14 = i2;
                            i2++;
                            cArr[i14] = charArray[i6];
                            z3 = false;
                            break;
                        default:
                            if (!Character.isLetterOrDigit(charArray[i6])) {
                                int i15 = i2;
                                i2++;
                                cArr[i15] = '\\';
                            }
                            int i16 = i2;
                            i2++;
                            cArr[i16] = charArray[i6];
                            break;
                    }
                    i6++;
                }
            }
            this.pattern = Pattern.compile(new String(cArr, 0, i2), 2);
        }

        @Override // javax.swing.filechooser.FileFilter
        public boolean accept(File file) {
            if (file == null) {
                return false;
            }
            if (file.isDirectory()) {
                return true;
            }
            return this.pattern.matcher(file.getName()).matches();
        }

        @Override // javax.swing.filechooser.FileFilter
        public String getDescription() {
            return this.globPattern;
        }
    }

    public Action getFileNameCompletionAction() {
        return this.fileNameCompletionAction;
    }

    @Override // javax.swing.plaf.basic.BasicFileChooserUI
    protected JButton getApproveButton(JFileChooser jFileChooser) {
        return this.approveButton;
    }

    protected JButton getCancelButton(JFileChooser jFileChooser) {
        return this.cancelButton;
    }

    @Override // javax.swing.plaf.basic.BasicFileChooserUI
    public void clearIconCache() {
    }

    /* loaded from: rt.jar:sun/swing/plaf/synth/SynthFileChooserUI$UIBorder.class */
    private class UIBorder extends AbstractBorder implements UIResource {
        private Insets _insets;

        UIBorder(Insets insets) {
            if (insets != null) {
                this._insets = new Insets(insets.top, insets.left, insets.bottom, insets.right);
            } else {
                this._insets = null;
            }
        }

        @Override // javax.swing.border.AbstractBorder, javax.swing.border.Border
        public void paintBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
            if (!(component instanceof JComponent)) {
                return;
            }
            SynthContext context = SynthFileChooserUI.this.getContext((JComponent) component);
            SynthStyle style = context.getStyle();
            if (style != null) {
                style.getPainter(context).paintFileChooserBorder(context, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.border.AbstractBorder
        public Insets getBorderInsets(Component component, Insets insets) {
            if (insets == null) {
                insets = new Insets(0, 0, 0, 0);
            }
            if (this._insets != null) {
                insets.top = this._insets.top;
                insets.bottom = this._insets.bottom;
                insets.left = this._insets.left;
                insets.right = this._insets.right;
            } else {
                insets.left = 0;
                insets.right = 0;
                insets.bottom = 0;
                insets.top = 0;
            }
            return insets;
        }

        @Override // javax.swing.border.AbstractBorder, javax.swing.border.Border
        public boolean isBorderOpaque() {
            return false;
        }
    }
}
