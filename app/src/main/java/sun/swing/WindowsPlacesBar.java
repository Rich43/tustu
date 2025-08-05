package sun.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileSystemView;
import sun.awt.OSInfo;
import sun.awt.shell.ShellFolder;

/* loaded from: rt.jar:sun/swing/WindowsPlacesBar.class */
public class WindowsPlacesBar extends JToolBar implements ActionListener, PropertyChangeListener {
    JFileChooser fc;
    JToggleButton[] buttons;
    ButtonGroup buttonGroup;
    File[] files;
    final Dimension buttonSize;

    public WindowsPlacesBar(JFileChooser jFileChooser, boolean z2) {
        Icon systemIcon;
        super(1);
        this.fc = jFileChooser;
        setFloatable(false);
        putClientProperty("JToolBar.isRollover", Boolean.TRUE);
        boolean z3 = OSInfo.getOSType() == OSInfo.OSType.WINDOWS && OSInfo.getWindowsVersion().compareTo(OSInfo.WINDOWS_XP) >= 0;
        if (z2) {
            this.buttonSize = new Dimension(83, 69);
            putClientProperty("XPStyle.subAppName", "placesbar");
            setBorder(new EmptyBorder(1, 1, 1, 1));
        } else {
            this.buttonSize = new Dimension(83, z3 ? 65 : 54);
            setBorder(new BevelBorder(1, UIManager.getColor("ToolBar.highlight"), UIManager.getColor("ToolBar.background"), UIManager.getColor("ToolBar.darkShadow"), UIManager.getColor("ToolBar.shadow")));
        }
        setBackground(new Color(UIManager.getColor("ToolBar.shadow").getRGB()));
        FileSystemView fileSystemView = jFileChooser.getFileSystemView();
        this.files = (File[]) ShellFolder.get("fileChooserShortcutPanelFolders");
        this.buttons = new JToggleButton[this.files.length];
        this.buttonGroup = new ButtonGroup();
        for (int i2 = 0; i2 < this.files.length; i2++) {
            if (fileSystemView.isFileSystemRoot(this.files[i2])) {
                this.files[i2] = fileSystemView.createFileObject(this.files[i2].getAbsolutePath());
            }
            String systemDisplayName = fileSystemView.getSystemDisplayName(this.files[i2]);
            int iLastIndexOf = systemDisplayName.lastIndexOf(File.separatorChar);
            if (iLastIndexOf >= 0 && iLastIndexOf < systemDisplayName.length() - 1) {
                systemDisplayName = systemDisplayName.substring(iLastIndexOf + 1);
            }
            if (this.files[i2] instanceof ShellFolder) {
                ShellFolder shellFolder = (ShellFolder) this.files[i2];
                Image icon = shellFolder.getIcon(true);
                icon = icon == null ? (Image) ShellFolder.get("shell32LargeIcon 1") : icon;
                systemIcon = icon == null ? null : new ImageIcon(icon, shellFolder.getFolderType());
            } else {
                systemIcon = fileSystemView.getSystemIcon(this.files[i2]);
            }
            this.buttons[i2] = new JToggleButton(systemDisplayName, systemIcon);
            if (z2) {
                this.buttons[i2].putClientProperty("XPStyle.subAppName", "placesbar");
            } else {
                Color color = new Color(UIManager.getColor("List.selectionForeground").getRGB());
                this.buttons[i2].setContentAreaFilled(false);
                this.buttons[i2].setForeground(color);
            }
            this.buttons[i2].setMargin(new Insets(3, 2, 1, 2));
            this.buttons[i2].setFocusPainted(false);
            this.buttons[i2].setIconTextGap(0);
            this.buttons[i2].setHorizontalTextPosition(0);
            this.buttons[i2].setVerticalTextPosition(3);
            this.buttons[i2].setAlignmentX(0.5f);
            this.buttons[i2].setPreferredSize(this.buttonSize);
            this.buttons[i2].setMaximumSize(this.buttonSize);
            this.buttons[i2].addActionListener(this);
            add(this.buttons[i2]);
            if (i2 < this.files.length - 1 && z2) {
                add(Box.createRigidArea(new Dimension(1, 1)));
            }
            this.buttonGroup.add(this.buttons[i2]);
        }
        doDirectoryChanged(jFileChooser.getCurrentDirectory());
    }

    protected void doDirectoryChanged(File file) {
        for (int i2 = 0; i2 < this.buttons.length; i2++) {
            JToggleButton jToggleButton = this.buttons[i2];
            if (this.files[i2].equals(file)) {
                jToggleButton.setSelected(true);
                return;
            }
            if (jToggleButton.isSelected()) {
                this.buttonGroup.remove(jToggleButton);
                jToggleButton.setSelected(false);
                this.buttonGroup.add(jToggleButton);
            }
        }
    }

    @Override // java.beans.PropertyChangeListener
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if (propertyChangeEvent.getPropertyName() == JFileChooser.DIRECTORY_CHANGED_PROPERTY) {
            doDirectoryChanged(this.fc.getCurrentDirectory());
        }
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        JToggleButton jToggleButton = (JToggleButton) actionEvent.getSource();
        for (int i2 = 0; i2 < this.buttons.length; i2++) {
            if (jToggleButton == this.buttons[i2]) {
                this.fc.setCurrentDirectory(this.files[i2]);
                return;
            }
        }
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() {
        JToggleButton jToggleButton;
        int i2;
        Dimension minimumSize = super.getMinimumSize();
        Dimension preferredSize = super.getPreferredSize();
        int i3 = minimumSize.height;
        if (this.buttons != null && this.buttons.length > 0 && this.buttons.length < 5 && (jToggleButton = this.buttons[0]) != null && (i2 = 5 * (jToggleButton.getPreferredSize().height + 1)) > i3) {
            i3 = i2;
        }
        if (i3 > preferredSize.height) {
            preferredSize = new Dimension(preferredSize.width, i3);
        }
        return preferredSize;
    }
}
