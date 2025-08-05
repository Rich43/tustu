package sun.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.DefaultKeyboardFocusManager;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;
import javax.accessibility.AccessibleContext;
import javax.swing.AbstractAction;
import javax.swing.AbstractListModel;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListSelectionModel;
import javax.swing.DefaultRowSorter;
import javax.swing.Icon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.LookAndFeel;
import javax.swing.RowSorter;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.plaf.basic.BasicDirectoryModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.Position;
import sun.awt.AWTAccessor;
import sun.awt.shell.ShellFolder;
import sun.awt.shell.ShellFolderColumnInfo;

/* loaded from: rt.jar:sun/swing/FilePane.class */
public class FilePane extends JPanel implements PropertyChangeListener {
    public static final String ACTION_APPROVE_SELECTION = "approveSelection";
    public static final String ACTION_CANCEL = "cancelSelection";
    public static final String ACTION_EDIT_FILE_NAME = "editFileName";
    public static final String ACTION_REFRESH = "refresh";
    public static final String ACTION_CHANGE_TO_PARENT_DIRECTORY = "Go Up";
    public static final String ACTION_NEW_FOLDER = "New Folder";
    public static final String ACTION_VIEW_LIST = "viewTypeList";
    public static final String ACTION_VIEW_DETAILS = "viewTypeDetails";
    private Action[] actions;
    public static final int VIEWTYPE_LIST = 0;
    public static final int VIEWTYPE_DETAILS = 1;
    private static final int VIEWTYPE_COUNT = 2;
    private int viewType;
    private JPanel[] viewPanels;
    private JPanel currentViewPanel;
    private String[] viewTypeActionNames;
    private String filesListAccessibleName;
    private String filesDetailsAccessibleName;
    private JPopupMenu contextMenu;
    private JMenu viewMenu;
    private String viewMenuLabelText;
    private String refreshActionLabelText;
    private String newFolderActionLabelText;
    private String kiloByteString;
    private String megaByteString;
    private String gigaByteString;
    private String renameErrorTitleText;
    private String renameErrorText;
    private String renameErrorFileExistsText;
    private final KeyListener detailsKeyListener;
    private FocusListener editorFocusListener;
    private boolean smallIconsView;
    private Border listViewBorder;
    private Color listViewBackground;
    private boolean listViewWindowsStyle;
    private boolean readOnly;
    private boolean fullRowSelection;
    private ListSelectionModel listSelectionModel;
    private JList list;
    private JTable detailsTable;
    private static final int COLUMN_FILENAME = 0;
    private File newFolderFile;
    private FileChooserUIAccessor fileChooserUIAccessor;
    private DetailsTableModel detailsTableModel;
    private DetailsTableRowSorter rowSorter;
    private DetailsTableCellEditor tableCellEditor;
    int lastIndex;
    File editFile;
    JTextField editCell;
    protected Action newFolderAction;
    private Handler handler;
    private static final Cursor waitCursor = Cursor.getPredefinedCursor(3);
    private static FocusListener repaintListener = new FocusListener() { // from class: sun.swing.FilePane.3
        @Override // java.awt.event.FocusListener
        public void focusGained(FocusEvent focusEvent) {
            repaintSelection(focusEvent.getSource());
        }

        @Override // java.awt.event.FocusListener
        public void focusLost(FocusEvent focusEvent) {
            repaintSelection(focusEvent.getSource());
        }

        private void repaintSelection(Object obj) {
            if (obj instanceof JList) {
                repaintListSelection((JList) obj);
            } else if (obj instanceof JTable) {
                repaintTableSelection((JTable) obj);
            }
        }

        private void repaintListSelection(JList jList) {
            for (int i2 : jList.getSelectedIndices()) {
                jList.repaint(jList.getCellBounds(i2, i2));
            }
        }

        private void repaintTableSelection(JTable jTable) {
            int minSelectionIndex = jTable.getSelectionModel().getMinSelectionIndex();
            int maxSelectionIndex = jTable.getSelectionModel().getMaxSelectionIndex();
            if (minSelectionIndex == -1 || maxSelectionIndex == -1) {
                return;
            }
            int iConvertColumnIndexToView = jTable.convertColumnIndexToView(0);
            jTable.repaint(jTable.getCellRect(minSelectionIndex, iConvertColumnIndexToView, false).union(jTable.getCellRect(maxSelectionIndex, iConvertColumnIndexToView, false)));
        }
    };

    /* loaded from: rt.jar:sun/swing/FilePane$FileChooserUIAccessor.class */
    public interface FileChooserUIAccessor {
        JFileChooser getFileChooser();

        BasicDirectoryModel getModel();

        JPanel createList();

        JPanel createDetailsView();

        boolean isDirectorySelected();

        File getDirectory();

        Action getApproveSelectionAction();

        Action getChangeToParentDirectoryAction();

        Action getNewFolderAction();

        MouseListener createDoubleClickListener(JList jList);

        ListSelectionListener createListSelectionListener();
    }

    public FilePane(FileChooserUIAccessor fileChooserUIAccessor) {
        super(new BorderLayout());
        this.viewType = -1;
        this.viewPanels = new JPanel[2];
        this.filesListAccessibleName = null;
        this.filesDetailsAccessibleName = null;
        this.detailsKeyListener = new KeyAdapter() { // from class: sun.swing.FilePane.1
            private final long timeFactor;
            private final StringBuilder typedString = new StringBuilder();
            private long lastTime = 1000;

            {
                Long l2 = (Long) UIManager.get("Table.timeFactor");
                this.timeFactor = l2 != null ? l2.longValue() : 1000L;
            }

            @Override // java.awt.event.KeyAdapter, java.awt.event.KeyListener
            public void keyTyped(KeyEvent keyEvent) {
                int size = FilePane.this.getModel().getSize();
                if (FilePane.this.detailsTable != null && size != 0 && !keyEvent.isAltDown() && !keyEvent.isControlDown() && !keyEvent.isMetaDown()) {
                    InputMap inputMap = FilePane.this.detailsTable.getInputMap(1);
                    KeyStroke keyStrokeForEvent = KeyStroke.getKeyStrokeForEvent(keyEvent);
                    if (inputMap == null || inputMap.get(keyStrokeForEvent) == null) {
                        int leadSelectionIndex = FilePane.this.detailsTable.getSelectionModel().getLeadSelectionIndex();
                        if (leadSelectionIndex < 0) {
                            leadSelectionIndex = 0;
                        }
                        if (leadSelectionIndex >= size) {
                            leadSelectionIndex = size - 1;
                        }
                        char keyChar = keyEvent.getKeyChar();
                        long when = keyEvent.getWhen();
                        if (when - this.lastTime < this.timeFactor) {
                            if (this.typedString.length() == 1 && this.typedString.charAt(0) == keyChar) {
                                leadSelectionIndex++;
                            } else {
                                this.typedString.append(keyChar);
                            }
                        } else {
                            leadSelectionIndex++;
                            this.typedString.setLength(0);
                            this.typedString.append(keyChar);
                        }
                        this.lastTime = when;
                        if (leadSelectionIndex >= size) {
                            leadSelectionIndex = 0;
                        }
                        int nextMatch = getNextMatch(leadSelectionIndex, size - 1);
                        if (nextMatch < 0 && leadSelectionIndex > 0) {
                            nextMatch = getNextMatch(0, leadSelectionIndex - 1);
                        }
                        if (nextMatch >= 0) {
                            FilePane.this.detailsTable.getSelectionModel().setSelectionInterval(nextMatch, nextMatch);
                            FilePane.this.detailsTable.scrollRectToVisible(FilePane.this.detailsTable.getCellRect(nextMatch, FilePane.this.detailsTable.convertColumnIndexToView(0), false));
                        }
                    }
                }
            }

            private int getNextMatch(int i2, int i3) {
                BasicDirectoryModel model = FilePane.this.getModel();
                JFileChooser fileChooser = FilePane.this.getFileChooser();
                DetailsTableRowSorter rowSorter = FilePane.this.getRowSorter();
                String lowerCase = this.typedString.toString().toLowerCase();
                for (int i4 = i2; i4 <= i3; i4++) {
                    if (fileChooser.getName((File) model.getElementAt(rowSorter.convertRowIndexToModel(i4))).toLowerCase().startsWith(lowerCase)) {
                        return i4;
                    }
                }
                return -1;
            }
        };
        this.editorFocusListener = new FocusAdapter() { // from class: sun.swing.FilePane.2
            @Override // java.awt.event.FocusAdapter, java.awt.event.FocusListener
            public void focusLost(FocusEvent focusEvent) throws HeadlessException {
                if (!focusEvent.isTemporary()) {
                    FilePane.this.applyEdit();
                }
            }
        };
        this.smallIconsView = false;
        this.fullRowSelection = false;
        this.lastIndex = -1;
        this.editFile = null;
        this.editCell = null;
        this.fileChooserUIAccessor = fileChooserUIAccessor;
        installDefaults();
        createActionMap();
    }

    public void uninstallUI() {
        if (getModel() != null) {
            getModel().removePropertyChangeListener(this);
        }
    }

    protected JFileChooser getFileChooser() {
        return this.fileChooserUIAccessor.getFileChooser();
    }

    protected BasicDirectoryModel getModel() {
        return this.fileChooserUIAccessor.getModel();
    }

    public int getViewType() {
        return this.viewType;
    }

    public void setViewType(int i2) {
        if (i2 == this.viewType) {
            return;
        }
        int i3 = this.viewType;
        this.viewType = i2;
        JPanel jPanelCreateDetailsView = null;
        Component component = null;
        switch (i2) {
            case 0:
                if (this.viewPanels[i2] == null) {
                    jPanelCreateDetailsView = this.fileChooserUIAccessor.createList();
                    if (jPanelCreateDetailsView == null) {
                        jPanelCreateDetailsView = createList();
                    }
                    this.list = (JList) findChildComponent(jPanelCreateDetailsView, JList.class);
                    if (this.listSelectionModel == null) {
                        this.listSelectionModel = this.list.getSelectionModel();
                        if (this.detailsTable != null) {
                            this.detailsTable.setSelectionModel(this.listSelectionModel);
                        }
                    } else {
                        this.list.setSelectionModel(this.listSelectionModel);
                    }
                }
                this.list.setLayoutOrientation(1);
                component = this.list;
                break;
            case 1:
                if (this.viewPanels[i2] == null) {
                    jPanelCreateDetailsView = this.fileChooserUIAccessor.createDetailsView();
                    if (jPanelCreateDetailsView == null) {
                        jPanelCreateDetailsView = createDetailsView();
                    }
                    this.detailsTable = (JTable) findChildComponent(jPanelCreateDetailsView, JTable.class);
                    this.detailsTable.setRowHeight(Math.max(this.detailsTable.getFont().getSize() + 4, 17));
                    if (this.listSelectionModel != null) {
                        this.detailsTable.setSelectionModel(this.listSelectionModel);
                    }
                }
                component = this.detailsTable;
                break;
        }
        if (jPanelCreateDetailsView != null) {
            this.viewPanels[i2] = jPanelCreateDetailsView;
            recursivelySetInheritsPopupMenu(jPanelCreateDetailsView, true);
        }
        boolean z2 = false;
        if (this.currentViewPanel != null) {
            Component permanentFocusOwner = DefaultKeyboardFocusManager.getCurrentKeyboardFocusManager().getPermanentFocusOwner();
            z2 = permanentFocusOwner == this.detailsTable || permanentFocusOwner == this.list;
            remove(this.currentViewPanel);
        }
        this.currentViewPanel = this.viewPanels[i2];
        add(this.currentViewPanel, BorderLayout.CENTER);
        if (z2 && component != null) {
            component.requestFocusInWindow();
        }
        revalidate();
        repaint();
        updateViewMenu();
        firePropertyChange("viewType", i3, i2);
    }

    /* loaded from: rt.jar:sun/swing/FilePane$ViewTypeAction.class */
    class ViewTypeAction extends AbstractAction {
        private int viewType;

        ViewTypeAction(int i2) {
            String str;
            super(FilePane.this.viewTypeActionNames[i2]);
            this.viewType = i2;
            switch (i2) {
                case 0:
                    str = FilePane.ACTION_VIEW_LIST;
                    break;
                case 1:
                    str = FilePane.ACTION_VIEW_DETAILS;
                    break;
                default:
                    str = (String) getValue("Name");
                    break;
            }
            putValue(Action.ACTION_COMMAND_KEY, str);
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            FilePane.this.setViewType(this.viewType);
        }
    }

    public Action getViewTypeAction(int i2) {
        return new ViewTypeAction(i2);
    }

    private static void recursivelySetInheritsPopupMenu(Container container, boolean z2) {
        if (container instanceof JComponent) {
            ((JComponent) container).setInheritsPopupMenu(z2);
        }
        int componentCount = container.getComponentCount();
        for (int i2 = 0; i2 < componentCount; i2++) {
            recursivelySetInheritsPopupMenu((Container) container.getComponent(i2), z2);
        }
    }

    protected void installDefaults() {
        Locale locale = getFileChooser().getLocale();
        this.listViewBorder = UIManager.getBorder("FileChooser.listViewBorder");
        this.listViewBackground = UIManager.getColor("FileChooser.listViewBackground");
        this.listViewWindowsStyle = UIManager.getBoolean("FileChooser.listViewWindowsStyle");
        this.readOnly = UIManager.getBoolean("FileChooser.readOnly");
        this.viewMenuLabelText = UIManager.getString("FileChooser.viewMenuLabelText", locale);
        this.refreshActionLabelText = UIManager.getString("FileChooser.refreshActionLabelText", locale);
        this.newFolderActionLabelText = UIManager.getString("FileChooser.newFolderActionLabelText", locale);
        this.viewTypeActionNames = new String[2];
        this.viewTypeActionNames[0] = UIManager.getString("FileChooser.listViewActionLabelText", locale);
        this.viewTypeActionNames[1] = UIManager.getString("FileChooser.detailsViewActionLabelText", locale);
        this.kiloByteString = UIManager.getString("FileChooser.fileSizeKiloBytes", locale);
        this.megaByteString = UIManager.getString("FileChooser.fileSizeMegaBytes", locale);
        this.gigaByteString = UIManager.getString("FileChooser.fileSizeGigaBytes", locale);
        this.fullRowSelection = UIManager.getBoolean("FileView.fullRowSelection");
        this.filesListAccessibleName = UIManager.getString("FileChooser.filesListAccessibleName", locale);
        this.filesDetailsAccessibleName = UIManager.getString("FileChooser.filesDetailsAccessibleName", locale);
        this.renameErrorTitleText = UIManager.getString("FileChooser.renameErrorTitleText", locale);
        this.renameErrorText = UIManager.getString("FileChooser.renameErrorText", locale);
        this.renameErrorFileExistsText = UIManager.getString("FileChooser.renameErrorFileExistsText", locale);
    }

    public Action[] getActions() {
        if (this.actions == null) {
            ArrayList arrayList = new ArrayList(8);
            arrayList.add(new C1FilePaneAction(this, ACTION_CANCEL));
            arrayList.add(new C1FilePaneAction(this, ACTION_EDIT_FILE_NAME));
            arrayList.add(new C1FilePaneAction(this.refreshActionLabelText, ACTION_REFRESH));
            Action approveSelectionAction = this.fileChooserUIAccessor.getApproveSelectionAction();
            if (approveSelectionAction != null) {
                arrayList.add(approveSelectionAction);
            }
            Action changeToParentDirectoryAction = this.fileChooserUIAccessor.getChangeToParentDirectoryAction();
            if (changeToParentDirectoryAction != null) {
                arrayList.add(changeToParentDirectoryAction);
            }
            Action newFolderAction = getNewFolderAction();
            if (newFolderAction != null) {
                arrayList.add(newFolderAction);
            }
            Action viewTypeAction = getViewTypeAction(0);
            if (viewTypeAction != null) {
                arrayList.add(viewTypeAction);
            }
            Action viewTypeAction2 = getViewTypeAction(1);
            if (viewTypeAction2 != null) {
                arrayList.add(viewTypeAction2);
            }
            this.actions = (Action[]) arrayList.toArray(new Action[arrayList.size()]);
        }
        return this.actions;
    }

    /* renamed from: sun.swing.FilePane$1FilePaneAction, reason: invalid class name */
    /* loaded from: rt.jar:sun/swing/FilePane$1FilePaneAction.class */
    class C1FilePaneAction extends AbstractAction {
        C1FilePaneAction(FilePane filePane, String str) {
            this(str, str);
        }

        C1FilePaneAction(String str, String str2) {
            super(str);
            putValue(Action.ACTION_COMMAND_KEY, str2);
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            String str = (String) getValue(Action.ACTION_COMMAND_KEY);
            if (str == FilePane.ACTION_CANCEL) {
                if (FilePane.this.editFile != null) {
                    FilePane.this.cancelEdit();
                    return;
                } else {
                    FilePane.this.getFileChooser().cancelSelection();
                    return;
                }
            }
            if (str != FilePane.ACTION_EDIT_FILE_NAME) {
                if (str == FilePane.ACTION_REFRESH) {
                    FilePane.this.getFileChooser().rescanCurrentDirectory();
                    return;
                }
                return;
            }
            JFileChooser fileChooser = FilePane.this.getFileChooser();
            int minSelectionIndex = FilePane.this.listSelectionModel.getMinSelectionIndex();
            if (minSelectionIndex >= 0 && FilePane.this.editFile == null) {
                if (!fileChooser.isMultiSelectionEnabled() || fileChooser.getSelectedFiles().length <= 1) {
                    FilePane.this.editFileName(minSelectionIndex);
                }
            }
        }

        @Override // javax.swing.AbstractAction, javax.swing.Action
        public boolean isEnabled() {
            String str = (String) getValue(Action.ACTION_COMMAND_KEY);
            if (str == FilePane.ACTION_CANCEL) {
                return FilePane.this.getFileChooser().isEnabled();
            }
            if (str == FilePane.ACTION_EDIT_FILE_NAME) {
                return !FilePane.this.readOnly && FilePane.this.getFileChooser().isEnabled();
            }
            return true;
        }
    }

    protected void createActionMap() {
        addActionsToMap(super.getActionMap(), getActions());
    }

    public static void addActionsToMap(ActionMap actionMap, Action[] actionArr) {
        if (actionMap != null && actionArr != null) {
            for (Action action : actionArr) {
                String str = (String) action.getValue(Action.ACTION_COMMAND_KEY);
                if (str == null) {
                    str = (String) action.getValue("Name");
                }
                actionMap.put(str, action);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateListRowCount(JList jList) {
        if (this.smallIconsView) {
            jList.setVisibleRowCount(getModel().getSize() / 3);
        } else {
            jList.setVisibleRowCount(-1);
        }
    }

    public JPanel createList() {
        JPanel jPanel = new JPanel(new BorderLayout());
        final JFileChooser fileChooser = getFileChooser();
        final JList<Object> jList = new JList<Object>() { // from class: sun.swing.FilePane.4
            @Override // javax.swing.JList
            public int getNextMatch(String str, int i2, Position.Bias bias) {
                ListModel<Object> model = getModel();
                int size = model.getSize();
                if (str == null || i2 < 0 || i2 >= size) {
                    throw new IllegalArgumentException();
                }
                boolean z2 = bias == Position.Bias.Backward;
                int i3 = i2;
                while (true) {
                    int i4 = i3;
                    if (z2) {
                        if (i4 < 0) {
                            return -1;
                        }
                    } else if (i4 >= size) {
                        return -1;
                    }
                    if (!fileChooser.getName((File) model.getElementAt(i4)).regionMatches(true, 0, str, 0, str.length())) {
                        i3 = i4 + (z2 ? -1 : 1);
                    } else {
                        return i4;
                    }
                }
            }
        };
        jList.setCellRenderer(new FileRenderer());
        jList.setLayoutOrientation(1);
        jList.putClientProperty("List.isFileList", Boolean.TRUE);
        if (this.listViewWindowsStyle) {
            jList.addFocusListener(repaintListener);
        }
        updateListRowCount(jList);
        getModel().addListDataListener(new ListDataListener() { // from class: sun.swing.FilePane.5
            @Override // javax.swing.event.ListDataListener
            public void intervalAdded(ListDataEvent listDataEvent) {
                FilePane.this.updateListRowCount(jList);
            }

            @Override // javax.swing.event.ListDataListener
            public void intervalRemoved(ListDataEvent listDataEvent) {
                FilePane.this.updateListRowCount(jList);
            }

            @Override // javax.swing.event.ListDataListener
            public void contentsChanged(ListDataEvent listDataEvent) {
                if (FilePane.this.isShowing()) {
                    FilePane.this.clearSelection();
                }
                FilePane.this.updateListRowCount(jList);
            }
        });
        getModel().addPropertyChangeListener(this);
        if (fileChooser.isMultiSelectionEnabled()) {
            jList.setSelectionMode(2);
        } else {
            jList.setSelectionMode(0);
        }
        jList.setModel(new SortableListModel());
        jList.addListSelectionListener(createListSelectionListener());
        jList.addMouseListener(getMouseHandler());
        JScrollPane jScrollPane = new JScrollPane(jList);
        if (this.listViewBackground != null) {
            jList.setBackground(this.listViewBackground);
        }
        if (this.listViewBorder != null) {
            jScrollPane.setBorder(this.listViewBorder);
        }
        jList.putClientProperty(AccessibleContext.ACCESSIBLE_NAME_PROPERTY, this.filesListAccessibleName);
        jPanel.add(jScrollPane, BorderLayout.CENTER);
        return jPanel;
    }

    /* loaded from: rt.jar:sun/swing/FilePane$SortableListModel.class */
    private class SortableListModel extends AbstractListModel<Object> implements TableModelListener, RowSorterListener {
        public SortableListModel() {
            FilePane.this.getDetailsTableModel().addTableModelListener(this);
            FilePane.this.getRowSorter().addRowSorterListener(this);
        }

        @Override // javax.swing.ListModel
        public int getSize() {
            return FilePane.this.getModel().getSize();
        }

        @Override // javax.swing.ListModel
        public Object getElementAt(int i2) {
            return FilePane.this.getModel().getElementAt(FilePane.this.getRowSorter().convertRowIndexToModel(i2));
        }

        @Override // javax.swing.event.TableModelListener
        public void tableChanged(TableModelEvent tableModelEvent) {
            fireContentsChanged(this, 0, getSize());
        }

        @Override // javax.swing.event.RowSorterListener
        public void sorterChanged(RowSorterEvent rowSorterEvent) {
            fireContentsChanged(this, 0, getSize());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public DetailsTableModel getDetailsTableModel() {
        if (this.detailsTableModel == null) {
            this.detailsTableModel = new DetailsTableModel(getFileChooser());
        }
        return this.detailsTableModel;
    }

    /* loaded from: rt.jar:sun/swing/FilePane$DetailsTableModel.class */
    class DetailsTableModel extends AbstractTableModel implements ListDataListener {
        JFileChooser chooser;
        BasicDirectoryModel directoryModel;
        ShellFolderColumnInfo[] columns;
        int[] columnMap;

        DetailsTableModel(JFileChooser jFileChooser) {
            this.chooser = jFileChooser;
            this.directoryModel = FilePane.this.getModel();
            this.directoryModel.addListDataListener(this);
            updateColumnInfo();
        }

        void updateColumnInfo() {
            File currentDirectory = this.chooser.getCurrentDirectory();
            if (currentDirectory != null && FilePane.usesShellFolder(this.chooser)) {
                try {
                    currentDirectory = ShellFolder.getShellFolder(currentDirectory);
                } catch (FileNotFoundException e2) {
                }
            }
            ShellFolderColumnInfo[] folderColumns = ShellFolder.getFolderColumns(currentDirectory);
            ArrayList arrayList = new ArrayList();
            this.columnMap = new int[folderColumns.length];
            for (int i2 = 0; i2 < folderColumns.length; i2++) {
                ShellFolderColumnInfo shellFolderColumnInfo = folderColumns[i2];
                if (shellFolderColumnInfo.isVisible()) {
                    this.columnMap[arrayList.size()] = i2;
                    arrayList.add(shellFolderColumnInfo);
                }
            }
            this.columns = new ShellFolderColumnInfo[arrayList.size()];
            arrayList.toArray(this.columns);
            this.columnMap = Arrays.copyOf(this.columnMap, this.columns.length);
            List<? extends RowSorter.SortKey> sortKeys = FilePane.this.rowSorter == null ? null : FilePane.this.rowSorter.getSortKeys();
            fireTableStructureChanged();
            restoreSortKeys(sortKeys);
        }

        private void restoreSortKeys(List<? extends RowSorter.SortKey> list) {
            if (list != null) {
                int i2 = 0;
                while (true) {
                    if (i2 >= list.size()) {
                        break;
                    }
                    if (list.get(i2).getColumn() < this.columns.length) {
                        i2++;
                    } else {
                        list = null;
                        break;
                    }
                }
                if (list != null) {
                    FilePane.this.rowSorter.setSortKeys(list);
                }
            }
        }

        @Override // javax.swing.table.TableModel
        public int getRowCount() {
            return this.directoryModel.getSize();
        }

        @Override // javax.swing.table.TableModel
        public int getColumnCount() {
            return this.columns.length;
        }

        @Override // javax.swing.table.TableModel
        public Object getValueAt(int i2, int i3) {
            return getFileColumnValue((File) this.directoryModel.getElementAt(i2), i3);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Object getFileColumnValue(File file, int i2) {
            return i2 == 0 ? file : ShellFolder.getFolderColumnValue(file, this.columnMap[i2]);
        }

        @Override // javax.swing.table.AbstractTableModel, javax.swing.table.TableModel
        public void setValueAt(Object obj, int i2, int i3) throws HeadlessException {
            if (i3 == 0) {
                final JFileChooser fileChooser = FilePane.this.getFileChooser();
                File file = (File) getValueAt(i2, i3);
                if (file != null) {
                    String name = fileChooser.getName(file);
                    String name2 = file.getName();
                    String strTrim = ((String) obj).trim();
                    if (!strTrim.equals(name)) {
                        String str = strTrim;
                        int length = name2.length();
                        int length2 = name.length();
                        if (length > length2 && name2.charAt(length2) == '.') {
                            str = strTrim + name2.substring(length2);
                        }
                        FileSystemView fileSystemView = fileChooser.getFileSystemView();
                        final File fileCreateFileObject = fileSystemView.createFileObject(file.getParentFile(), str);
                        if (fileCreateFileObject.exists()) {
                            JOptionPane.showMessageDialog(fileChooser, MessageFormat.format(FilePane.this.renameErrorFileExistsText, name2), FilePane.this.renameErrorTitleText, 0);
                        } else {
                            if (FilePane.this.getModel().renameFile(file, fileCreateFileObject)) {
                                if (fileSystemView.isParent(fileChooser.getCurrentDirectory(), fileCreateFileObject)) {
                                    SwingUtilities.invokeLater(new Runnable() { // from class: sun.swing.FilePane.DetailsTableModel.1
                                        @Override // java.lang.Runnable
                                        public void run() {
                                            if (fileChooser.isMultiSelectionEnabled()) {
                                                fileChooser.setSelectedFiles(new File[]{fileCreateFileObject});
                                            } else {
                                                fileChooser.setSelectedFile(fileCreateFileObject);
                                            }
                                        }
                                    });
                                    return;
                                }
                                return;
                            }
                            JOptionPane.showMessageDialog(fileChooser, MessageFormat.format(FilePane.this.renameErrorText, name2), FilePane.this.renameErrorTitleText, 0);
                        }
                    }
                }
            }
        }

        @Override // javax.swing.table.AbstractTableModel, javax.swing.table.TableModel
        public boolean isCellEditable(int i2, int i3) {
            return !FilePane.this.readOnly && i3 == 0 && FilePane.this.canWrite(FilePane.this.getFileChooser().getCurrentDirectory());
        }

        @Override // javax.swing.event.ListDataListener
        public void contentsChanged(ListDataEvent listDataEvent) {
            new DelayedSelectionUpdater(FilePane.this);
            fireTableDataChanged();
        }

        @Override // javax.swing.event.ListDataListener
        public void intervalAdded(ListDataEvent listDataEvent) {
            int index0 = listDataEvent.getIndex0();
            if (index0 == listDataEvent.getIndex1()) {
                File file = (File) FilePane.this.getModel().getElementAt(index0);
                if (file.equals(FilePane.this.newFolderFile)) {
                    FilePane.this.new DelayedSelectionUpdater(file);
                    FilePane.this.newFolderFile = null;
                }
            }
            fireTableRowsInserted(listDataEvent.getIndex0(), listDataEvent.getIndex1());
        }

        @Override // javax.swing.event.ListDataListener
        public void intervalRemoved(ListDataEvent listDataEvent) {
            fireTableRowsDeleted(listDataEvent.getIndex0(), listDataEvent.getIndex1());
        }

        public ShellFolderColumnInfo[] getColumns() {
            return this.columns;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDetailsColumnModel(JTable jTable) {
        String string;
        if (jTable != null) {
            ShellFolderColumnInfo[] columns = this.detailsTableModel.getColumns();
            DefaultTableColumnModel defaultTableColumnModel = new DefaultTableColumnModel();
            for (int i2 = 0; i2 < columns.length; i2++) {
                ShellFolderColumnInfo shellFolderColumnInfo = columns[i2];
                TableColumn tableColumn = new TableColumn(i2);
                String title = shellFolderColumnInfo.getTitle();
                if (title != null && title.startsWith("FileChooser.") && title.endsWith("HeaderText") && (string = UIManager.getString(title, jTable.getLocale())) != null) {
                    title = string;
                }
                tableColumn.setHeaderValue(title);
                Integer width = shellFolderColumnInfo.getWidth();
                if (width != null) {
                    tableColumn.setPreferredWidth(width.intValue());
                }
                defaultTableColumnModel.addColumn(tableColumn);
            }
            if (!this.readOnly && defaultTableColumnModel.getColumnCount() > 0) {
                defaultTableColumnModel.getColumn(0).setCellEditor(getDetailsTableCellEditor());
            }
            jTable.setColumnModel(defaultTableColumnModel);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public DetailsTableRowSorter getRowSorter() {
        if (this.rowSorter == null) {
            this.rowSorter = new DetailsTableRowSorter();
        }
        return this.rowSorter;
    }

    /* loaded from: rt.jar:sun/swing/FilePane$DetailsTableRowSorter.class */
    private class DetailsTableRowSorter extends TableRowSorter<TableModel> {
        public DetailsTableRowSorter() {
            setModelWrapper(new SorterModelWrapper());
        }

        public void updateComparators(ShellFolderColumnInfo[] shellFolderColumnInfoArr) {
            for (int i2 = 0; i2 < shellFolderColumnInfoArr.length; i2++) {
                Comparator comparator = shellFolderColumnInfoArr[i2].getComparator();
                if (comparator != null) {
                    comparator = FilePane.this.new DirectoriesFirstComparatorWrapper(i2, comparator);
                }
                setComparator(i2, comparator);
            }
        }

        @Override // javax.swing.DefaultRowSorter
        public void sort() {
            ShellFolder.invoke(new Callable<Void>() { // from class: sun.swing.FilePane.DetailsTableRowSorter.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.concurrent.Callable
                public Void call() {
                    DetailsTableRowSorter.super.sort();
                    return null;
                }
            });
        }

        @Override // javax.swing.DefaultRowSorter, javax.swing.RowSorter
        public void modelStructureChanged() {
            super.modelStructureChanged();
            updateComparators(FilePane.this.detailsTableModel.getColumns());
        }

        /* loaded from: rt.jar:sun/swing/FilePane$DetailsTableRowSorter$SorterModelWrapper.class */
        private class SorterModelWrapper extends DefaultRowSorter.ModelWrapper<TableModel, Integer> {
            private SorterModelWrapper() {
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // javax.swing.DefaultRowSorter.ModelWrapper
            public TableModel getModel() {
                return FilePane.this.getDetailsTableModel();
            }

            @Override // javax.swing.DefaultRowSorter.ModelWrapper
            public int getColumnCount() {
                return FilePane.this.getDetailsTableModel().getColumnCount();
            }

            @Override // javax.swing.DefaultRowSorter.ModelWrapper
            public int getRowCount() {
                return FilePane.this.getDetailsTableModel().getRowCount();
            }

            @Override // javax.swing.DefaultRowSorter.ModelWrapper
            public Object getValueAt(int i2, int i3) {
                return FilePane.this.getModel().getElementAt(i2);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // javax.swing.DefaultRowSorter.ModelWrapper
            public Integer getIdentifier(int i2) {
                return Integer.valueOf(i2);
            }
        }
    }

    /* loaded from: rt.jar:sun/swing/FilePane$DirectoriesFirstComparatorWrapper.class */
    private class DirectoriesFirstComparatorWrapper implements Comparator<File> {
        private Comparator comparator;
        private int column;

        public DirectoriesFirstComparatorWrapper(int i2, Comparator comparator) {
            this.column = i2;
            this.comparator = comparator;
        }

        @Override // java.util.Comparator
        public int compare(File file, File file2) {
            if (file != null && file2 != null) {
                boolean zIsTraversable = FilePane.this.getFileChooser().isTraversable(file);
                boolean zIsTraversable2 = FilePane.this.getFileChooser().isTraversable(file2);
                if (zIsTraversable && !zIsTraversable2) {
                    return -1;
                }
                if (!zIsTraversable && zIsTraversable2) {
                    return 1;
                }
            }
            if (FilePane.this.detailsTableModel.getColumns()[this.column].isCompareByColumn()) {
                return this.comparator.compare(FilePane.this.getDetailsTableModel().getFileColumnValue(file, this.column), FilePane.this.getDetailsTableModel().getFileColumnValue(file2, this.column));
            }
            return this.comparator.compare(file, file2);
        }
    }

    private DetailsTableCellEditor getDetailsTableCellEditor() {
        if (this.tableCellEditor == null) {
            this.tableCellEditor = new DetailsTableCellEditor(new JTextField());
        }
        return this.tableCellEditor;
    }

    /* loaded from: rt.jar:sun/swing/FilePane$DetailsTableCellEditor.class */
    private class DetailsTableCellEditor extends DefaultCellEditor {
        private final JTextField tf;

        public DetailsTableCellEditor(JTextField jTextField) {
            super(jTextField);
            this.tf = jTextField;
            jTextField.setName("Table.editor");
            jTextField.addFocusListener(FilePane.this.editorFocusListener);
        }

        @Override // javax.swing.DefaultCellEditor, javax.swing.table.TableCellEditor
        public Component getTableCellEditorComponent(JTable jTable, Object obj, boolean z2, int i2, int i3) {
            Component tableCellEditorComponent = super.getTableCellEditorComponent(jTable, obj, z2, i2, i3);
            if (obj instanceof File) {
                this.tf.setText(FilePane.this.getFileChooser().getName((File) obj));
                this.tf.selectAll();
            }
            return tableCellEditorComponent;
        }
    }

    /* loaded from: rt.jar:sun/swing/FilePane$DetailsTableCellRenderer.class */
    class DetailsTableCellRenderer extends DefaultTableCellRenderer {
        JFileChooser chooser;
        DateFormat df;

        DetailsTableCellRenderer(JFileChooser jFileChooser) {
            this.chooser = jFileChooser;
            this.df = DateFormat.getDateTimeInstance(3, 3, jFileChooser.getLocale());
        }

        @Override // java.awt.Component
        public void setBounds(int i2, int i3, int i4, int i5) {
            if (getHorizontalAlignment() == 10 && !FilePane.this.fullRowSelection) {
                i4 = Math.min(i4, getPreferredSize().width + 4);
            } else {
                i2 -= 4;
            }
            super.setBounds(i2, i3, i4, i5);
        }

        @Override // javax.swing.JComponent
        public Insets getInsets(Insets insets) {
            Insets insets2 = super.getInsets(insets);
            insets2.left += 4;
            insets2.right += 4;
            return insets2;
        }

        @Override // javax.swing.table.DefaultTableCellRenderer, javax.swing.table.TableCellRenderer
        public Component getTableCellRendererComponent(JTable jTable, Object obj, boolean z2, boolean z3, int i2, int i3) {
            String string;
            if ((jTable.convertColumnIndexToModel(i3) != 0 || (FilePane.this.listViewWindowsStyle && !jTable.isFocusOwner())) && !FilePane.this.fullRowSelection) {
                z2 = false;
            }
            super.getTableCellRendererComponent(jTable, obj, z2, z3, i2, i3);
            setIcon(null);
            Integer alignment = FilePane.this.detailsTableModel.getColumns()[jTable.convertColumnIndexToModel(i3)].getAlignment();
            if (alignment == null) {
                alignment = Integer.valueOf(obj instanceof Number ? 4 : 10);
            }
            setHorizontalAlignment(alignment.intValue());
            if (obj == null) {
                string = "";
            } else if (obj instanceof File) {
                File file = (File) obj;
                string = this.chooser.getName(file);
                setIcon(this.chooser.getIcon(file));
            } else if (obj instanceof Long) {
                long jLongValue = ((Long) obj).longValue() / 1024;
                if (FilePane.this.listViewWindowsStyle) {
                    string = MessageFormat.format(FilePane.this.kiloByteString, Long.valueOf(jLongValue + 1));
                } else if (jLongValue < 1024) {
                    String str = FilePane.this.kiloByteString;
                    Object[] objArr = new Object[1];
                    objArr[0] = Long.valueOf(jLongValue == 0 ? 1L : jLongValue);
                    string = MessageFormat.format(str, objArr);
                } else {
                    long j2 = jLongValue / 1024;
                    string = j2 < 1024 ? MessageFormat.format(FilePane.this.megaByteString, Long.valueOf(j2)) : MessageFormat.format(FilePane.this.gigaByteString, Long.valueOf(j2 / 1024));
                }
            } else {
                string = obj instanceof Date ? this.df.format((Date) obj) : obj.toString();
            }
            setText(string);
            return this;
        }
    }

    public JPanel createDetailsView() {
        final JFileChooser fileChooser = getFileChooser();
        JPanel jPanel = new JPanel(new BorderLayout());
        JTable jTable = new JTable(getDetailsTableModel()) { // from class: sun.swing.FilePane.6
            @Override // javax.swing.JTable, javax.swing.JComponent
            protected boolean processKeyBinding(KeyStroke keyStroke, KeyEvent keyEvent, int i2, boolean z2) {
                if (keyEvent.getKeyCode() == 27 && getCellEditor() == null) {
                    fileChooser.dispatchEvent(keyEvent);
                    return true;
                }
                return super.processKeyBinding(keyStroke, keyEvent, i2, z2);
            }

            @Override // javax.swing.JTable, javax.swing.event.TableModelListener
            public void tableChanged(TableModelEvent tableModelEvent) {
                super.tableChanged(tableModelEvent);
                if (tableModelEvent.getFirstRow() == -1) {
                    FilePane.this.updateDetailsColumnModel(this);
                }
            }
        };
        jTable.setRowSorter(getRowSorter());
        jTable.setAutoCreateColumnsFromModel(false);
        jTable.setComponentOrientation(fileChooser.getComponentOrientation());
        jTable.setAutoResizeMode(0);
        jTable.setShowGrid(false);
        jTable.putClientProperty("JTable.autoStartsEdit", Boolean.FALSE);
        jTable.addKeyListener(this.detailsKeyListener);
        jTable.setFont(this.list.getFont());
        jTable.setIntercellSpacing(new Dimension(0, 0));
        jTable.getTableHeader().setDefaultRenderer(new AlignableTableHeaderRenderer(jTable.getTableHeader().getDefaultRenderer()));
        jTable.setDefaultRenderer(Object.class, new DetailsTableCellRenderer(fileChooser));
        jTable.getColumnModel().getSelectionModel().setSelectionMode(0);
        jTable.addMouseListener(getMouseHandler());
        jTable.putClientProperty("Table.isFileList", Boolean.TRUE);
        if (this.listViewWindowsStyle) {
            jTable.addFocusListener(repaintListener);
        }
        ActionMap uIActionMap = SwingUtilities.getUIActionMap(jTable);
        uIActionMap.remove("selectNextRowCell");
        uIActionMap.remove("selectPreviousRowCell");
        uIActionMap.remove("selectNextColumnCell");
        uIActionMap.remove("selectPreviousColumnCell");
        jTable.setFocusTraversalKeys(0, null);
        jTable.setFocusTraversalKeys(1, null);
        JScrollPane jScrollPane = new JScrollPane(jTable);
        jScrollPane.setComponentOrientation(fileChooser.getComponentOrientation());
        LookAndFeel.installColors(jScrollPane.getViewport(), "Table.background", "Table.foreground");
        jScrollPane.addComponentListener(new ComponentAdapter() { // from class: sun.swing.FilePane.7
            @Override // java.awt.event.ComponentAdapter, java.awt.event.ComponentListener
            public void componentResized(ComponentEvent componentEvent) {
                JScrollPane jScrollPane2 = (JScrollPane) componentEvent.getComponent();
                FilePane.this.fixNameColumnWidth(jScrollPane2.getViewport().getSize().width);
                jScrollPane2.removeComponentListener(this);
            }
        });
        jScrollPane.addMouseListener(new MouseAdapter() { // from class: sun.swing.FilePane.8
            @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
            public void mousePressed(MouseEvent mouseEvent) {
                JTable jTable2 = (JTable) ((JScrollPane) mouseEvent.getComponent()).getViewport().getView();
                if (!mouseEvent.isShiftDown() || jTable2.getSelectionModel().getSelectionMode() == 0) {
                    FilePane.this.clearSelection();
                    TableCellEditor cellEditor = jTable2.getCellEditor();
                    if (cellEditor != null) {
                        cellEditor.stopCellEditing();
                    }
                }
            }
        });
        jTable.setForeground(this.list.getForeground());
        jTable.setBackground(this.list.getBackground());
        if (this.listViewBorder != null) {
            jScrollPane.setBorder(this.listViewBorder);
        }
        jPanel.add(jScrollPane, BorderLayout.CENTER);
        this.detailsTableModel.fireTableStructureChanged();
        jTable.putClientProperty(AccessibleContext.ACCESSIBLE_NAME_PROPERTY, this.filesDetailsAccessibleName);
        return jPanel;
    }

    /* loaded from: rt.jar:sun/swing/FilePane$AlignableTableHeaderRenderer.class */
    private class AlignableTableHeaderRenderer implements TableCellRenderer {
        TableCellRenderer wrappedRenderer;

        public AlignableTableHeaderRenderer(TableCellRenderer tableCellRenderer) {
            this.wrappedRenderer = tableCellRenderer;
        }

        @Override // javax.swing.table.TableCellRenderer
        public Component getTableCellRendererComponent(JTable jTable, Object obj, boolean z2, boolean z3, int i2, int i3) {
            Component tableCellRendererComponent = this.wrappedRenderer.getTableCellRendererComponent(jTable, obj, z2, z3, i2, i3);
            Integer alignment = FilePane.this.detailsTableModel.getColumns()[jTable.convertColumnIndexToModel(i3)].getAlignment();
            if (alignment == null) {
                alignment = 0;
            }
            if (tableCellRendererComponent instanceof JLabel) {
                ((JLabel) tableCellRendererComponent).setHorizontalAlignment(alignment.intValue());
            }
            return tableCellRendererComponent;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fixNameColumnWidth(int i2) {
        TableColumn column = this.detailsTable.getColumnModel().getColumn(0);
        int i3 = this.detailsTable.getPreferredSize().width;
        if (i3 < i2) {
            column.setPreferredWidth((column.getPreferredWidth() + i2) - i3);
        }
    }

    /* loaded from: rt.jar:sun/swing/FilePane$DelayedSelectionUpdater.class */
    private class DelayedSelectionUpdater implements Runnable {
        File editFile;

        DelayedSelectionUpdater(FilePane filePane) {
            this(null);
        }

        DelayedSelectionUpdater(File file) {
            this.editFile = file;
            if (FilePane.this.isShowing()) {
                SwingUtilities.invokeLater(this);
            }
        }

        @Override // java.lang.Runnable
        public void run() {
            FilePane.this.setFileSelected();
            if (this.editFile != null) {
                FilePane.this.editFileName(FilePane.this.getRowSorter().convertRowIndexToView(FilePane.this.getModel().indexOf(this.editFile)));
                this.editFile = null;
            }
        }
    }

    public ListSelectionListener createListSelectionListener() {
        return this.fileChooserUIAccessor.createListSelectionListener();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getEditIndex() {
        return this.lastIndex;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setEditIndex(int i2) {
        this.lastIndex = i2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetEditIndex() {
        this.lastIndex = -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelEdit() {
        if (this.editFile != null) {
            this.editFile = null;
            this.list.remove(this.editCell);
            repaint();
        } else if (this.detailsTable != null && this.detailsTable.isEditing()) {
            this.detailsTable.getCellEditor().cancelCellEditing();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void editFileName(int i2) {
        JFileChooser fileChooser = getFileChooser();
        File currentDirectory = fileChooser.getCurrentDirectory();
        if (this.readOnly || !canWrite(currentDirectory)) {
            return;
        }
        ensureIndexIsVisible(i2);
        switch (this.viewType) {
            case 0:
                this.editFile = (File) getModel().getElementAt(getRowSorter().convertRowIndexToModel(i2));
                Rectangle cellBounds = this.list.getCellBounds(i2, i2);
                if (this.editCell == null) {
                    this.editCell = new JTextField();
                    this.editCell.setName("Tree.cellEditor");
                    this.editCell.addActionListener(new EditActionListener());
                    this.editCell.addFocusListener(this.editorFocusListener);
                    this.editCell.setNextFocusableComponent(this.list);
                }
                this.list.add(this.editCell);
                this.editCell.setText(fileChooser.getName(this.editFile));
                ComponentOrientation componentOrientation = this.list.getComponentOrientation();
                this.editCell.setComponentOrientation(componentOrientation);
                Icon icon = fileChooser.getIcon(this.editFile);
                int iconWidth = icon == null ? 20 : icon.getIconWidth() + 4;
                if (componentOrientation.isLeftToRight()) {
                    this.editCell.setBounds(iconWidth + cellBounds.f12372x, cellBounds.f12373y, cellBounds.width - iconWidth, cellBounds.height);
                } else {
                    this.editCell.setBounds(cellBounds.f12372x, cellBounds.f12373y, cellBounds.width - iconWidth, cellBounds.height);
                }
                this.editCell.requestFocus();
                this.editCell.selectAll();
                break;
            case 1:
                this.detailsTable.editCellAt(i2, 0);
                break;
        }
    }

    /* loaded from: rt.jar:sun/swing/FilePane$EditActionListener.class */
    class EditActionListener implements ActionListener {
        EditActionListener() {
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) throws HeadlessException {
            FilePane.this.applyEdit();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void applyEdit() throws HeadlessException {
        if (this.editFile != null && this.editFile.exists()) {
            JFileChooser fileChooser = getFileChooser();
            String name = fileChooser.getName(this.editFile);
            String name2 = this.editFile.getName();
            String strTrim = this.editCell.getText().trim();
            if (!strTrim.equals(name)) {
                String str = strTrim;
                int length = name2.length();
                int length2 = name.length();
                if (length > length2 && name2.charAt(length2) == '.') {
                    str = strTrim + name2.substring(length2);
                }
                FileSystemView fileSystemView = fileChooser.getFileSystemView();
                File fileCreateFileObject = fileSystemView.createFileObject(this.editFile.getParentFile(), str);
                if (fileCreateFileObject.exists()) {
                    JOptionPane.showMessageDialog(fileChooser, MessageFormat.format(this.renameErrorFileExistsText, name2), this.renameErrorTitleText, 0);
                } else if (getModel().renameFile(this.editFile, fileCreateFileObject)) {
                    if (fileSystemView.isParent(fileChooser.getCurrentDirectory(), fileCreateFileObject)) {
                        if (fileChooser.isMultiSelectionEnabled()) {
                            fileChooser.setSelectedFiles(new File[]{fileCreateFileObject});
                        } else {
                            fileChooser.setSelectedFile(fileCreateFileObject);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(fileChooser, MessageFormat.format(this.renameErrorText, name2), this.renameErrorTitleText, 0);
                }
            }
        }
        if (this.detailsTable != null && this.detailsTable.isEditing()) {
            this.detailsTable.getCellEditor().stopCellEditing();
        }
        cancelEdit();
    }

    public Action getNewFolderAction() {
        if (!this.readOnly && this.newFolderAction == null) {
            this.newFolderAction = new AbstractAction(this.newFolderActionLabelText) { // from class: sun.swing.FilePane.9
                private Action basicNewFolderAction;

                {
                    putValue(Action.ACTION_COMMAND_KEY, FilePane.ACTION_NEW_FOLDER);
                    File currentDirectory = FilePane.this.getFileChooser().getCurrentDirectory();
                    if (currentDirectory != null) {
                        setEnabled(FilePane.this.canWrite(currentDirectory));
                    }
                }

                @Override // java.awt.event.ActionListener
                public void actionPerformed(ActionEvent actionEvent) {
                    if (this.basicNewFolderAction == null) {
                        this.basicNewFolderAction = FilePane.this.fileChooserUIAccessor.getNewFolderAction();
                    }
                    JFileChooser fileChooser = FilePane.this.getFileChooser();
                    File selectedFile = fileChooser.getSelectedFile();
                    this.basicNewFolderAction.actionPerformed(actionEvent);
                    File selectedFile2 = fileChooser.getSelectedFile();
                    if (selectedFile2 != null && !selectedFile2.equals(selectedFile) && selectedFile2.isDirectory()) {
                        FilePane.this.newFolderFile = selectedFile2;
                    }
                }
            };
        }
        return this.newFolderAction;
    }

    /* loaded from: rt.jar:sun/swing/FilePane$FileRenderer.class */
    protected class FileRenderer extends DefaultListCellRenderer {
        protected FileRenderer() {
        }

        @Override // javax.swing.DefaultListCellRenderer, javax.swing.ListCellRenderer
        public Component getListCellRendererComponent(JList jList, Object obj, int i2, boolean z2, boolean z3) {
            if (FilePane.this.listViewWindowsStyle && !jList.isFocusOwner()) {
                z2 = false;
            }
            super.getListCellRendererComponent(jList, obj, i2, z2, z3);
            File file = (File) obj;
            String name = FilePane.this.getFileChooser().getName(file);
            setText(name);
            setFont(jList.getFont());
            Icon icon = FilePane.this.getFileChooser().getIcon(file);
            if (icon != null) {
                setIcon(icon);
            } else if (FilePane.this.getFileChooser().getFileSystemView().isTraversable(file).booleanValue()) {
                setText(name + File.separator);
            }
            return this;
        }
    }

    void setFileSelected() {
        File selectedFile;
        int iIndexOf;
        if (getFileChooser().isMultiSelectionEnabled() && !isDirectorySelected()) {
            File[] selectedFiles = getFileChooser().getSelectedFiles();
            Object[] selectedValues = this.list.getSelectedValues();
            this.listSelectionModel.setValueIsAdjusting(true);
            try {
                int leadSelectionIndex = this.listSelectionModel.getLeadSelectionIndex();
                int anchorSelectionIndex = this.listSelectionModel.getAnchorSelectionIndex();
                Arrays.sort(selectedFiles);
                Arrays.sort(selectedValues);
                int i2 = 0;
                int i3 = 0;
                while (i2 < selectedFiles.length && i3 < selectedValues.length) {
                    int iCompareTo = selectedFiles[i2].compareTo((File) selectedValues[i3]);
                    if (iCompareTo < 0) {
                        int i4 = i2;
                        i2++;
                        doSelectFile(selectedFiles[i4]);
                    } else if (iCompareTo > 0) {
                        int i5 = i3;
                        i3++;
                        doDeselectFile(selectedValues[i5]);
                    } else {
                        i2++;
                        i3++;
                    }
                }
                while (i2 < selectedFiles.length) {
                    int i6 = i2;
                    i2++;
                    doSelectFile(selectedFiles[i6]);
                }
                while (i3 < selectedValues.length) {
                    int i7 = i3;
                    i3++;
                    doDeselectFile(selectedValues[i7]);
                }
                if (this.listSelectionModel instanceof DefaultListSelectionModel) {
                    ((DefaultListSelectionModel) this.listSelectionModel).moveLeadSelectionIndex(leadSelectionIndex);
                    this.listSelectionModel.setAnchorSelectionIndex(anchorSelectionIndex);
                }
                return;
            } finally {
                this.listSelectionModel.setValueIsAdjusting(false);
            }
        }
        JFileChooser fileChooser = getFileChooser();
        if (isDirectorySelected()) {
            selectedFile = getDirectory();
        } else {
            selectedFile = fileChooser.getSelectedFile();
        }
        if (selectedFile != null && (iIndexOf = getModel().indexOf(selectedFile)) >= 0) {
            int iConvertRowIndexToView = getRowSorter().convertRowIndexToView(iIndexOf);
            this.listSelectionModel.setSelectionInterval(iConvertRowIndexToView, iConvertRowIndexToView);
            ensureIndexIsVisible(iConvertRowIndexToView);
            return;
        }
        clearSelection();
    }

    private void doSelectFile(File file) {
        int iIndexOf = getModel().indexOf(file);
        if (iIndexOf >= 0) {
            int iConvertRowIndexToView = getRowSorter().convertRowIndexToView(iIndexOf);
            this.listSelectionModel.addSelectionInterval(iConvertRowIndexToView, iConvertRowIndexToView);
        }
    }

    private void doDeselectFile(Object obj) {
        int iConvertRowIndexToView = getRowSorter().convertRowIndexToView(getModel().indexOf(obj));
        this.listSelectionModel.removeSelectionInterval(iConvertRowIndexToView, iConvertRowIndexToView);
    }

    private void doSelectedFileChanged(PropertyChangeEvent propertyChangeEvent) throws HeadlessException {
        applyEdit();
        File file = (File) propertyChangeEvent.getNewValue();
        JFileChooser fileChooser = getFileChooser();
        if (file != null) {
            if ((fileChooser.isFileSelectionEnabled() && !file.isDirectory()) || (file.isDirectory() && fileChooser.isDirectorySelectionEnabled())) {
                setFileSelected();
            }
        }
    }

    private void doSelectedFilesChanged(PropertyChangeEvent propertyChangeEvent) throws HeadlessException {
        applyEdit();
        File[] fileArr = (File[]) propertyChangeEvent.getNewValue();
        JFileChooser fileChooser = getFileChooser();
        if (fileArr != null && fileArr.length > 0) {
            if (fileArr.length > 1 || fileChooser.isDirectorySelectionEnabled() || !fileArr[0].isDirectory()) {
                setFileSelected();
            }
        }
    }

    private void doDirectoryChanged(PropertyChangeEvent propertyChangeEvent) throws HeadlessException {
        getDetailsTableModel().updateColumnInfo();
        JFileChooser fileChooser = getFileChooser();
        FileSystemView fileSystemView = fileChooser.getFileSystemView();
        applyEdit();
        resetEditIndex();
        ensureIndexIsVisible(0);
        File currentDirectory = fileChooser.getCurrentDirectory();
        if (currentDirectory != null) {
            if (!this.readOnly) {
                getNewFolderAction().setEnabled(canWrite(currentDirectory));
            }
            this.fileChooserUIAccessor.getChangeToParentDirectoryAction().setEnabled(!fileSystemView.isRoot(currentDirectory));
        }
        if (this.list != null) {
            this.list.clearSelection();
        }
    }

    private void doFilterChanged(PropertyChangeEvent propertyChangeEvent) throws HeadlessException {
        applyEdit();
        resetEditIndex();
        clearSelection();
    }

    private void doFileSelectionModeChanged(PropertyChangeEvent propertyChangeEvent) throws HeadlessException {
        applyEdit();
        resetEditIndex();
        clearSelection();
    }

    private void doMultiSelectionChanged(PropertyChangeEvent propertyChangeEvent) {
        if (getFileChooser().isMultiSelectionEnabled()) {
            this.listSelectionModel.setSelectionMode(2);
            return;
        }
        this.listSelectionModel.setSelectionMode(0);
        clearSelection();
        getFileChooser().setSelectedFiles(null);
    }

    @Override // java.beans.PropertyChangeListener
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) throws HeadlessException {
        if (this.viewType == -1) {
            setViewType(0);
        }
        String propertyName = propertyChangeEvent.getPropertyName();
        if (propertyName.equals(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY)) {
            doSelectedFileChanged(propertyChangeEvent);
            return;
        }
        if (propertyName.equals(JFileChooser.SELECTED_FILES_CHANGED_PROPERTY)) {
            doSelectedFilesChanged(propertyChangeEvent);
            return;
        }
        if (propertyName.equals(JFileChooser.DIRECTORY_CHANGED_PROPERTY)) {
            doDirectoryChanged(propertyChangeEvent);
            return;
        }
        if (propertyName.equals(JFileChooser.FILE_FILTER_CHANGED_PROPERTY)) {
            doFilterChanged(propertyChangeEvent);
            return;
        }
        if (propertyName.equals(JFileChooser.FILE_SELECTION_MODE_CHANGED_PROPERTY)) {
            doFileSelectionModeChanged(propertyChangeEvent);
            return;
        }
        if (propertyName.equals(JFileChooser.MULTI_SELECTION_ENABLED_CHANGED_PROPERTY)) {
            doMultiSelectionChanged(propertyChangeEvent);
            return;
        }
        if (propertyName.equals(JFileChooser.CANCEL_SELECTION)) {
            applyEdit();
            return;
        }
        if (propertyName.equals("busy")) {
            setCursor(((Boolean) propertyChangeEvent.getNewValue()).booleanValue() ? waitCursor : null);
            return;
        }
        if (propertyName.equals("componentOrientation")) {
            ComponentOrientation componentOrientation = (ComponentOrientation) propertyChangeEvent.getNewValue();
            JFileChooser jFileChooser = (JFileChooser) propertyChangeEvent.getSource();
            if (componentOrientation != propertyChangeEvent.getOldValue()) {
                jFileChooser.applyComponentOrientation(componentOrientation);
            }
            if (this.detailsTable != null) {
                this.detailsTable.setComponentOrientation(componentOrientation);
                this.detailsTable.getParent().getParent().setComponentOrientation(componentOrientation);
            }
        }
    }

    private void ensureIndexIsVisible(int i2) {
        if (i2 >= 0) {
            if (this.list != null) {
                this.list.ensureIndexIsVisible(i2);
            }
            if (this.detailsTable != null) {
                this.detailsTable.scrollRectToVisible(this.detailsTable.getCellRect(i2, 0, true));
            }
        }
    }

    public void ensureFileIsVisible(JFileChooser jFileChooser, File file) {
        int iIndexOf = getModel().indexOf(file);
        if (iIndexOf >= 0) {
            ensureIndexIsVisible(getRowSorter().convertRowIndexToView(iIndexOf));
        }
    }

    public void rescanCurrentDirectory() {
        getModel().validateFileCache();
    }

    public void clearSelection() {
        if (this.listSelectionModel != null) {
            this.listSelectionModel.clearSelection();
            if (this.listSelectionModel instanceof DefaultListSelectionModel) {
                ((DefaultListSelectionModel) this.listSelectionModel).moveLeadSelectionIndex(0);
                this.listSelectionModel.setAnchorSelectionIndex(0);
            }
        }
    }

    public JMenu getViewMenu() {
        if (this.viewMenu == null) {
            this.viewMenu = new JMenu(this.viewMenuLabelText);
            ButtonGroup buttonGroup = new ButtonGroup();
            for (int i2 = 0; i2 < 2; i2++) {
                JRadioButtonMenuItem jRadioButtonMenuItem = new JRadioButtonMenuItem(new ViewTypeAction(i2));
                buttonGroup.add(jRadioButtonMenuItem);
                this.viewMenu.add((JMenuItem) jRadioButtonMenuItem);
            }
            updateViewMenu();
        }
        return this.viewMenu;
    }

    private void updateViewMenu() {
        if (this.viewMenu != null) {
            for (Component component : this.viewMenu.getMenuComponents()) {
                if (component instanceof JRadioButtonMenuItem) {
                    JRadioButtonMenuItem jRadioButtonMenuItem = (JRadioButtonMenuItem) component;
                    if (((ViewTypeAction) jRadioButtonMenuItem.getAction()).viewType == this.viewType) {
                        jRadioButtonMenuItem.setSelected(true);
                    }
                }
            }
        }
    }

    @Override // javax.swing.JComponent
    public JPopupMenu getComponentPopupMenu() {
        JPopupMenu componentPopupMenu = getFileChooser().getComponentPopupMenu();
        if (componentPopupMenu != null) {
            return componentPopupMenu;
        }
        JMenu viewMenu = getViewMenu();
        if (this.contextMenu == null) {
            this.contextMenu = new JPopupMenu();
            if (viewMenu != null) {
                this.contextMenu.add((JMenuItem) viewMenu);
                if (this.listViewWindowsStyle) {
                    this.contextMenu.addSeparator();
                }
            }
            ActionMap actionMap = getActionMap();
            Action action = actionMap.get(ACTION_REFRESH);
            Action action2 = actionMap.get(ACTION_NEW_FOLDER);
            if (action != null) {
                this.contextMenu.add(action);
                if (this.listViewWindowsStyle && action2 != null) {
                    this.contextMenu.addSeparator();
                }
            }
            if (action2 != null) {
                this.contextMenu.add(action2);
            }
        }
        if (viewMenu != null) {
            viewMenu.getPopupMenu().setInvoker(viewMenu);
        }
        return this.contextMenu;
    }

    protected Handler getMouseHandler() {
        if (this.handler == null) {
            this.handler = new Handler();
        }
        return this.handler;
    }

    /* loaded from: rt.jar:sun/swing/FilePane$Handler.class */
    private class Handler implements MouseListener {
        private MouseListener doubleClickListener;

        private Handler() {
        }

        @Override // java.awt.event.MouseListener
        public void mouseClicked(MouseEvent mouseEvent) {
            int iRowAtPoint;
            JComponent jComponent = (JComponent) mouseEvent.getSource();
            if (jComponent instanceof JList) {
                iRowAtPoint = SwingUtilities2.loc2IndexFileList(FilePane.this.list, mouseEvent.getPoint());
            } else if (jComponent instanceof JTable) {
                JTable jTable = (JTable) jComponent;
                Point point = mouseEvent.getPoint();
                iRowAtPoint = jTable.rowAtPoint(point);
                if (SwingUtilities2.pointOutsidePrefSize(jTable, iRowAtPoint, jTable.columnAtPoint(point), point) && !FilePane.this.fullRowSelection) {
                    return;
                }
                if (iRowAtPoint >= 0 && FilePane.this.list != null && FilePane.this.listSelectionModel.isSelectedIndex(iRowAtPoint)) {
                    Rectangle cellBounds = FilePane.this.list.getCellBounds(iRowAtPoint, iRowAtPoint);
                    MouseEvent mouseEvent2 = new MouseEvent(FilePane.this.list, mouseEvent.getID(), mouseEvent.getWhen(), mouseEvent.getModifiers(), cellBounds.f12372x + 1, cellBounds.f12373y + (cellBounds.height / 2), mouseEvent.getXOnScreen(), mouseEvent.getYOnScreen(), mouseEvent.getClickCount(), mouseEvent.isPopupTrigger(), mouseEvent.getButton());
                    AWTAccessor.MouseEventAccessor mouseEventAccessor = AWTAccessor.getMouseEventAccessor();
                    mouseEventAccessor.setCausedByTouchEvent(mouseEvent2, mouseEventAccessor.isCausedByTouchEvent(mouseEvent));
                    mouseEvent = mouseEvent2;
                }
            } else {
                return;
            }
            if (iRowAtPoint >= 0 && SwingUtilities.isLeftMouseButton(mouseEvent)) {
                JFileChooser fileChooser = FilePane.this.getFileChooser();
                if (mouseEvent.getClickCount() == 1 && (jComponent instanceof JList)) {
                    if ((!fileChooser.isMultiSelectionEnabled() || fileChooser.getSelectedFiles().length <= 1) && iRowAtPoint >= 0 && FilePane.this.listSelectionModel.isSelectedIndex(iRowAtPoint) && FilePane.this.getEditIndex() == iRowAtPoint && FilePane.this.editFile == null) {
                        FilePane.this.editFileName(iRowAtPoint);
                    } else if (iRowAtPoint >= 0) {
                        FilePane.this.setEditIndex(iRowAtPoint);
                    } else {
                        FilePane.this.resetEditIndex();
                    }
                } else if (mouseEvent.getClickCount() == 2) {
                    FilePane.this.resetEditIndex();
                }
            }
            if (getDoubleClickListener() != null) {
                getDoubleClickListener().mouseClicked(mouseEvent);
            }
        }

        @Override // java.awt.event.MouseListener
        public void mouseEntered(MouseEvent mouseEvent) {
            JComponent jComponent = (JComponent) mouseEvent.getSource();
            if (jComponent instanceof JTable) {
                JTable jTable = (JTable) mouseEvent.getSource();
                TransferHandler transferHandler = FilePane.this.getFileChooser().getTransferHandler();
                if (transferHandler != jTable.getTransferHandler()) {
                    jTable.setTransferHandler(transferHandler);
                }
                boolean dragEnabled = FilePane.this.getFileChooser().getDragEnabled();
                if (dragEnabled != jTable.getDragEnabled()) {
                    jTable.setDragEnabled(dragEnabled);
                    return;
                }
                return;
            }
            if ((jComponent instanceof JList) && getDoubleClickListener() != null) {
                getDoubleClickListener().mouseEntered(mouseEvent);
            }
        }

        @Override // java.awt.event.MouseListener
        public void mouseExited(MouseEvent mouseEvent) {
            if ((mouseEvent.getSource() instanceof JList) && getDoubleClickListener() != null) {
                getDoubleClickListener().mouseExited(mouseEvent);
            }
        }

        @Override // java.awt.event.MouseListener
        public void mousePressed(MouseEvent mouseEvent) {
            if ((mouseEvent.getSource() instanceof JList) && getDoubleClickListener() != null) {
                getDoubleClickListener().mousePressed(mouseEvent);
            }
        }

        @Override // java.awt.event.MouseListener
        public void mouseReleased(MouseEvent mouseEvent) {
            if ((mouseEvent.getSource() instanceof JList) && getDoubleClickListener() != null) {
                getDoubleClickListener().mouseReleased(mouseEvent);
            }
        }

        private MouseListener getDoubleClickListener() {
            if (this.doubleClickListener == null && FilePane.this.list != null) {
                this.doubleClickListener = FilePane.this.fileChooserUIAccessor.createDoubleClickListener(FilePane.this.list);
            }
            return this.doubleClickListener;
        }
    }

    protected boolean isDirectorySelected() {
        return this.fileChooserUIAccessor.isDirectorySelected();
    }

    protected File getDirectory() {
        return this.fileChooserUIAccessor.getDirectory();
    }

    private Component findChildComponent(Container container, Class cls) {
        Component componentFindChildComponent;
        int componentCount = container.getComponentCount();
        for (int i2 = 0; i2 < componentCount; i2++) {
            Component component = container.getComponent(i2);
            if (cls.isInstance(component)) {
                return component;
            }
            if ((component instanceof Container) && (componentFindChildComponent = findChildComponent((Container) component, cls)) != null) {
                return componentFindChildComponent;
            }
        }
        return null;
    }

    public boolean canWrite(File file) {
        if (!file.exists()) {
            return false;
        }
        try {
            if (file instanceof ShellFolder) {
                return file.canWrite();
            }
            if (usesShellFolder(getFileChooser())) {
                try {
                    return ShellFolder.getShellFolder(file).canWrite();
                } catch (FileNotFoundException e2) {
                    return false;
                }
            }
            return file.canWrite();
        } catch (SecurityException e3) {
            return false;
        }
    }

    public static boolean usesShellFolder(JFileChooser jFileChooser) {
        Boolean bool = (Boolean) jFileChooser.getClientProperty("FileChooser.useShellFolder");
        return bool == null ? jFileChooser.getFileSystemView().equals(FileSystemView.getFileSystemView()) : bool.booleanValue();
    }
}
