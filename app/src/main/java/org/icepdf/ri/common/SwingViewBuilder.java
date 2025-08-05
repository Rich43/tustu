package org.icepdf.ri.common;

import apple.dts.samplecode.osxadapter.OSXAdapter;
import com.sun.org.apache.xpath.internal.compiler.Keywords;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.HeadlessException;
import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.text.rtf.RTFGenerator;
import org.icepdf.core.util.Defs;
import org.icepdf.ri.common.utility.annotation.AnnotationPanel;
import org.icepdf.ri.common.utility.layers.LayersPanel;
import org.icepdf.ri.common.utility.outline.OutlinesTree;
import org.icepdf.ri.common.utility.search.SearchPanel;
import org.icepdf.ri.common.utility.thumbs.ThumbnailsPanel;
import org.icepdf.ri.common.views.DocumentViewController;
import org.icepdf.ri.common.views.DocumentViewControllerImpl;
import org.icepdf.ri.images.Images;
import org.icepdf.ri.util.PropertiesManager;
import sun.security.pkcs11.wrapper.Constants;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/SwingViewBuilder.class */
public class SwingViewBuilder {
    public static final int TOOL_BAR_STYLE_FLOATING = 1;
    public static final int TOOL_BAR_STYLE_FIXED = 2;
    protected SwingController viewerController;
    protected Font buttonFont;
    protected boolean showButtonText;
    protected int toolbarStyle;
    protected float[] zoomLevels;
    protected boolean haveMadeAToolBar;
    protected int documentViewType;
    protected int documentPageFitMode;
    protected ResourceBundle messageBundle;
    protected PropertiesManager propertiesManager;
    public static boolean isMacOs;
    private static boolean isDemo;
    private static final Logger logger = Logger.getLogger(SwingViewBuilder.class.toString());
    protected static final float[] DEFAULT_ZOOM_LEVELS = {0.05f, 0.1f, 0.25f, 0.5f, 0.75f, 1.0f, 1.5f, 2.0f, 3.0f, 4.0f, 8.0f, 16.0f, 24.0f, 32.0f, 64.0f};

    static {
        isMacOs = Defs.sysProperty("mrj.version") != null;
        isDemo = Defs.sysPropertyBoolean("org.icepdf.ri.viewer.demo", false);
    }

    public SwingViewBuilder(SwingController c2) {
        this(c2, null, null, false, 2, null, 1, 3);
    }

    public SwingViewBuilder(SwingController c2, PropertiesManager properties) {
        this(c2, properties, null, false, 2, null, 1, 3);
    }

    public SwingViewBuilder(SwingController c2, int documentViewType, int documentPageFitMode) {
        this(c2, null, null, false, 2, null, documentViewType, documentPageFitMode);
    }

    public SwingViewBuilder(SwingController c2, Font bf2, boolean bt2, int ts, float[] zl, int documentViewType, int documentPageFitMode) {
        this(c2, null, bf2, bt2, ts, zl, documentViewType, documentPageFitMode);
    }

    public SwingViewBuilder(SwingController c2, PropertiesManager properties, Font bf2, boolean bt2, int ts, float[] zl, int documentViewType, int documentPageFitMode) {
        this.viewerController = c2;
        this.messageBundle = this.viewerController.getMessageBundle();
        if (properties != null) {
            this.viewerController.setPropertiesManager(properties);
            this.propertiesManager = properties;
        }
        overrideHighlightColor();
        DocumentViewControllerImpl documentViewController = (DocumentViewControllerImpl) this.viewerController.getDocumentViewController();
        documentViewController.setDocumentViewType(documentViewType, documentPageFitMode);
        this.buttonFont = bf2;
        if (this.buttonFont == null) {
            this.buttonFont = buildButtonFont();
        }
        this.showButtonText = bt2;
        this.toolbarStyle = ts;
        this.zoomLevels = zl;
        if (this.zoomLevels == null) {
            this.zoomLevels = DEFAULT_ZOOM_LEVELS;
        }
        this.documentViewType = documentViewType;
        this.documentPageFitMode = documentPageFitMode;
    }

    public JFrame buildViewerFrame() throws HeadlessException, IllegalArgumentException {
        JFrame viewer = new JFrame();
        viewer.setIconImage(new ImageIcon(Images.get("icepdf-app-icon-64x64.png")).getImage());
        viewer.setTitle(this.messageBundle.getString("viewer.window.title.default"));
        viewer.setDefaultCloseOperation(0);
        JMenuBar menuBar = buildCompleteMenuBar();
        if (menuBar != null) {
            viewer.setJMenuBar(menuBar);
        }
        Container contentPane = viewer.getContentPane();
        buildContents(contentPane, false);
        if (this.viewerController != null) {
            this.viewerController.setViewerFrame(viewer);
        }
        return viewer;
    }

    public JPanel buildViewerPanel() {
        JPanel panel = new JPanel();
        buildContents(panel, true);
        return panel;
    }

    public void buildContents(Container cp, boolean embeddableComponent) throws HeadlessException, IllegalArgumentException {
        cp.setLayout(new BorderLayout());
        JToolBar toolBar = buildCompleteToolBar(embeddableComponent);
        if (toolBar != null) {
            cp.add(toolBar, "North");
        }
        JSplitPane utilAndDocSplit = buildUtilityAndDocumentSplitPane(embeddableComponent);
        if (utilAndDocSplit != null) {
            cp.add(utilAndDocSplit, BorderLayout.CENTER);
        }
        JPanel statusPanel = buildStatusPanel();
        if (statusPanel != null) {
            cp.add(statusPanel, "South");
        }
    }

    public JMenuBar buildCompleteMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        addToMenuBar(menuBar, buildFileMenu());
        addToMenuBar(menuBar, buildEditMenu());
        addToMenuBar(menuBar, buildViewMenu());
        addToMenuBar(menuBar, buildDocumentMenu());
        addToMenuBar(menuBar, buildWindowMenu());
        addToMenuBar(menuBar, buildHelpMenu());
        if (isMacOs) {
            try {
                OSXAdapter.setQuitHandler(this.viewerController, this.viewerController.getClass().getDeclaredMethod("exit", (Class[]) null));
                OSXAdapter.setAboutHandler(this.viewerController, this.viewerController.getClass().getDeclaredMethod("showAboutDialog", (Class[]) null));
            } catch (Exception e2) {
                logger.log(Level.FINE, "Error occurred while loading the OSXAdapter:", (Throwable) e2);
            }
        }
        return menuBar;
    }

    protected KeyStroke buildKeyStroke(int keyCode, int modifiers) {
        return buildKeyStroke(keyCode, modifiers, false);
    }

    protected KeyStroke buildKeyStroke(int keyCode, int modifiers, boolean onRelease) {
        doubleCheckPropertiesManager();
        if (PropertiesManager.checkAndStoreBooleanProperty(this.propertiesManager, PropertiesManager.PROPERTY_SHOW_KEYBOARD_SHORTCUTS, true)) {
            return KeyStroke.getKeyStroke(keyCode, modifiers, onRelease);
        }
        return null;
    }

    protected int buildMnemonic(char mnemonic) {
        doubleCheckPropertiesManager();
        if (PropertiesManager.checkAndStoreBooleanProperty(this.propertiesManager, PropertiesManager.PROPERTY_SHOW_KEYBOARD_SHORTCUTS, true)) {
            return mnemonic;
        }
        return -1;
    }

    public JMenu buildFileMenu() {
        JMenu fileMenu = new JMenu(this.messageBundle.getString("viewer.menu.file.label"));
        fileMenu.setMnemonic(buildMnemonic(this.messageBundle.getString("viewer.menu.file.mnemonic").charAt(0)));
        JMenuItem openFileMenuItem = buildOpenFileMenuItem();
        JMenuItem openURLMenuItem = buildOpenURLMenuItem();
        if (openFileMenuItem != null && openURLMenuItem != null) {
            JMenu openSubMenu = new JMenu(this.messageBundle.getString("viewer.menu.open.label"));
            openSubMenu.setIcon(new ImageIcon(Images.get("open_a_24.png")));
            openSubMenu.setDisabledIcon(new ImageIcon(Images.get("open_i_24.png")));
            openSubMenu.setRolloverIcon(new ImageIcon(Images.get("open_r_24.png")));
            addToMenu(openSubMenu, openFileMenuItem);
            addToMenu(openSubMenu, openURLMenuItem);
            addToMenu(fileMenu, openSubMenu);
        } else if (openFileMenuItem != null || openURLMenuItem != null) {
            addToMenu(fileMenu, openFileMenuItem);
            addToMenu(fileMenu, openURLMenuItem);
        }
        fileMenu.addSeparator();
        addToMenu(fileMenu, buildCloseMenuItem());
        addToMenu(fileMenu, buildSaveAsFileMenuItem());
        addToMenu(fileMenu, buildExportTextMenuItem());
        addToMenu(fileMenu, buildExportSVGMenuItem());
        fileMenu.addSeparator();
        addToMenu(fileMenu, buildPermissionsMenuItem());
        addToMenu(fileMenu, buildInformationMenuItem());
        fileMenu.addSeparator();
        addToMenu(fileMenu, buildPrintSetupMenuItem());
        addToMenu(fileMenu, buildPrintMenuItem());
        if (!isMacOs) {
            fileMenu.addSeparator();
            addToMenu(fileMenu, buildExitMenuItem());
        }
        return fileMenu;
    }

    public JMenuItem buildOpenFileMenuItem() {
        JMenuItem mi = makeMenuItem(this.messageBundle.getString("viewer.menu.open.file.label"), buildKeyStroke(79, KeyEventConstants.MODIFIER_OPEN_FILE));
        if (this.viewerController != null && mi != null) {
            this.viewerController.setOpenFileMenuItem(mi);
        }
        return mi;
    }

    public JMenuItem buildOpenURLMenuItem() {
        JMenuItem mi = makeMenuItem(this.messageBundle.getString("viewer.menu.open.URL.label"), buildKeyStroke(85, KeyEventConstants.MODIFIER_OPEN_URL));
        if (this.viewerController != null && mi != null) {
            this.viewerController.setOpenURLMenuItem(mi);
        }
        return mi;
    }

    public JMenuItem buildCloseMenuItem() {
        JMenuItem mi = makeMenuItem(this.messageBundle.getString("viewer.menu.close.label"), null, null, buildKeyStroke(87, KeyEventConstants.MODIFIER_CLOSE));
        if (this.viewerController != null && mi != null) {
            this.viewerController.setCloseMenuItem(mi);
        }
        return mi;
    }

    public JMenuItem buildSaveAsFileMenuItem() {
        JMenuItem mi = makeMenuItem(this.messageBundle.getString("viewer.menu.saveAs.label"), "save", Images.SIZE_MEDIUM, buildKeyStroke(83, KeyEventConstants.MODIFIER_SAVE_AS, false));
        if (this.viewerController != null && mi != null) {
            this.viewerController.setSaveAsFileMenuItem(mi);
        }
        return mi;
    }

    public JMenuItem buildExportTextMenuItem() {
        JMenuItem mi = makeMenuItem(this.messageBundle.getString("viewer.menu.exportText.label"), null, null, null);
        if (this.viewerController != null && mi != null) {
            this.viewerController.setExportTextMenuItem(mi);
        }
        return mi;
    }

    public JMenuItem buildExportSVGMenuItem() {
        JMenuItem mi = null;
        try {
            Class.forName("org.apache.batik.dom.GenericDOMImplementation");
            mi = makeMenuItem(this.messageBundle.getString("viewer.menu.exportSVG.label"), null, null, null);
            if (this.viewerController != null && mi != null) {
                this.viewerController.setExportSVGMenuItem(mi);
            }
        } catch (ClassNotFoundException e2) {
            logger.warning("SVG Support Not Found");
        }
        return mi;
    }

    public JMenuItem buildPermissionsMenuItem() {
        JMenuItem mi = makeMenuItem(this.messageBundle.getString("viewer.menu.documentPermission.label"), null, null, null);
        if (this.viewerController != null && mi != null) {
            this.viewerController.setPermissionsMenuItem(mi);
        }
        return mi;
    }

    public JMenuItem buildInformationMenuItem() {
        JMenuItem mi = makeMenuItem(this.messageBundle.getString("viewer.menu.documentInformation.label"), null, null, null);
        if (this.viewerController != null && mi != null) {
            this.viewerController.setInformationMenuItem(mi);
        }
        return mi;
    }

    public JMenuItem buildPrintSetupMenuItem() {
        JMenuItem mi = makeMenuItem(this.messageBundle.getString("viewer.menu.printSetup.label"), null, null, buildKeyStroke(80, KeyEventConstants.MODIFIER_PRINT_SETUP, false));
        if (this.viewerController != null && mi != null) {
            this.viewerController.setPrintSetupMenuItem(mi);
        }
        return mi;
    }

    public JMenuItem buildPrintMenuItem() {
        JMenuItem mi = makeMenuItem(this.messageBundle.getString("viewer.menu.print.label"), "print", Images.SIZE_MEDIUM, buildKeyStroke(80, KeyEventConstants.MODIFIER_PRINT));
        if (this.viewerController != null && mi != null) {
            this.viewerController.setPrintMenuItem(mi);
        }
        return mi;
    }

    public JMenuItem buildExitMenuItem() {
        JMenuItem mi = makeMenuItem(this.messageBundle.getString("viewer.menu.exit.label"), null, null, buildKeyStroke(81, KeyEventConstants.MODIFIER_EXIT));
        if (this.viewerController != null && mi != null) {
            this.viewerController.setExitMenuItem(mi);
        }
        return mi;
    }

    public JMenu buildEditMenu() {
        JMenu viewMenu = new JMenu(this.messageBundle.getString("viewer.menu.edit.label"));
        viewMenu.setMnemonic(buildMnemonic(this.messageBundle.getString("viewer.menu.edit.mnemonic").charAt(0)));
        addToMenu(viewMenu, buildUndoMenuItem());
        addToMenu(viewMenu, buildRedoMenuItem());
        viewMenu.addSeparator();
        addToMenu(viewMenu, buildCopyMenuItem());
        addToMenu(viewMenu, buildDeleteMenuItem());
        viewMenu.addSeparator();
        addToMenu(viewMenu, buildSelectAllMenuItem());
        addToMenu(viewMenu, buildDeselectAllMenuItem());
        return viewMenu;
    }

    public JMenuItem buildUndoMenuItem() {
        JMenuItem mi = makeMenuItem(this.messageBundle.getString("viewer.menu.edit.undo.label"), null, null, buildKeyStroke(90, KeyEventConstants.MODIFIER_UNDO));
        if (this.viewerController != null && mi != null) {
            this.viewerController.setUndoMenuItem(mi);
        }
        return mi;
    }

    public JMenuItem buildRedoMenuItem() {
        JMenuItem mi = makeMenuItem(this.messageBundle.getString("viewer.menu.edit.redo.label"), null, null, buildKeyStroke(90, KeyEventConstants.MODIFIER_REDO));
        if (this.viewerController != null && mi != null) {
            this.viewerController.setReduMenuItem(mi);
        }
        return mi;
    }

    public JMenuItem buildCopyMenuItem() {
        JMenuItem mi = makeMenuItem(this.messageBundle.getString("viewer.menu.edit.copy.label"), null, null, buildKeyStroke(67, KeyEventConstants.MODIFIER_COPY));
        if (this.viewerController != null && mi != null) {
            this.viewerController.setCopyMenuItem(mi);
        }
        return mi;
    }

    public JMenuItem buildDeleteMenuItem() {
        JMenuItem mi = makeMenuItem(this.messageBundle.getString("viewer.menu.edit.delete.label"), null, null, buildKeyStroke(68, KeyEventConstants.MODIFIER_DELETE));
        if (this.viewerController != null && mi != null) {
            this.viewerController.setDeleteMenuItem(mi);
        }
        return mi;
    }

    public JMenuItem buildSelectAllMenuItem() {
        JMenuItem mi = makeMenuItem(this.messageBundle.getString("viewer.menu.edit.selectAll.label"), null, null, buildKeyStroke(65, KeyEventConstants.MODIFIER_SELECT_ALL));
        if (this.viewerController != null && mi != null) {
            this.viewerController.setSelectAllMenuItem(mi);
        }
        return mi;
    }

    public JMenuItem buildDeselectAllMenuItem() {
        JMenuItem mi = makeMenuItem(this.messageBundle.getString("viewer.menu.edit.deselectAll.label"), null, null, buildKeyStroke(65, KeyEventConstants.MODIFIER_DESELECT_ALL));
        if (this.viewerController != null && mi != null) {
            this.viewerController.setDselectAllMenuItem(mi);
        }
        return mi;
    }

    public JMenu buildViewMenu() {
        JMenu viewMenu = new JMenu(this.messageBundle.getString("viewer.menu.view.label"));
        viewMenu.setMnemonic(buildMnemonic(this.messageBundle.getString("viewer.menu.view.mnemonic").charAt(0)));
        addToMenu(viewMenu, buildFitActualSizeMenuItem());
        addToMenu(viewMenu, buildFitPageMenuItem());
        addToMenu(viewMenu, buildFitWidthMenuItem());
        viewMenu.addSeparator();
        addToMenu(viewMenu, buildZoomInMenuItem());
        addToMenu(viewMenu, buildZoomOutMenuItem());
        viewMenu.addSeparator();
        addToMenu(viewMenu, buildRotateLeftMenuItem());
        addToMenu(viewMenu, buildRotateRightMenuItem());
        viewMenu.addSeparator();
        addToMenu(viewMenu, buildShowHideToolBarMenuItem());
        addToMenu(viewMenu, buildShowHideUtilityPaneMenuItem());
        return viewMenu;
    }

    public JMenuItem buildFitActualSizeMenuItem() {
        JMenuItem mi = makeMenuItem(this.messageBundle.getString("viewer.menu.view.actualSize.label"), "actual_size", Images.SIZE_MEDIUM, buildKeyStroke(49, KeyEventConstants.MODIFIER_FIT_ACTUAL));
        if (this.viewerController != null && mi != null) {
            this.viewerController.setFitActualSizeMenuItem(mi);
        }
        return mi;
    }

    public JMenuItem buildFitPageMenuItem() {
        JMenuItem mi = makeMenuItem(this.messageBundle.getString("viewer.menu.view.fitInWindow.label"), "fit_window", Images.SIZE_MEDIUM, buildKeyStroke(50, KeyEventConstants.MODIFIER_FIT_PAGE));
        if (this.viewerController != null && mi != null) {
            this.viewerController.setFitPageMenuItem(mi);
        }
        return mi;
    }

    public JMenuItem buildFitWidthMenuItem() {
        JMenuItem mi = makeMenuItem(this.messageBundle.getString("viewer.menu.view.fitWidth.label"), null, null, buildKeyStroke(51, KeyEventConstants.MODIFIER_FIT_WIDTH));
        if (this.viewerController != null && mi != null) {
            this.viewerController.setFitWidthMenuItem(mi);
        }
        return mi;
    }

    public JMenuItem buildZoomInMenuItem() {
        JMenuItem mi = makeMenuItem(this.messageBundle.getString("viewer.menu.view.zoomIn.label"), "zoom_in", Images.SIZE_MEDIUM, buildKeyStroke(73, KeyEventConstants.MODIFIER_ZOOM_IN, false));
        if (this.viewerController != null && mi != null) {
            this.viewerController.setZoomInMenuItem(mi);
        }
        return mi;
    }

    public JMenuItem buildZoomOutMenuItem() {
        JMenuItem mi = makeMenuItem(this.messageBundle.getString("viewer.menu.view.zoomOut.label"), "zoom_out", Images.SIZE_MEDIUM, buildKeyStroke(79, KeyEventConstants.MODIFIER_ZOOM_OUT, false));
        if (this.viewerController != null && mi != null) {
            this.viewerController.setZoomOutMenuItem(mi);
        }
        return mi;
    }

    public JMenuItem buildRotateLeftMenuItem() {
        JMenuItem mi = makeMenuItem(this.messageBundle.getString("viewer.menu.view.rotateLeft.label"), "rotate_left", Images.SIZE_MEDIUM, buildKeyStroke(76, KeyEventConstants.MODIFIER_ROTATE_LEFT));
        if (this.viewerController != null && mi != null) {
            this.viewerController.setRotateLeftMenuItem(mi);
        }
        return mi;
    }

    public JMenuItem buildRotateRightMenuItem() {
        JMenuItem mi = makeMenuItem(this.messageBundle.getString("viewer.menu.view.rotateRight.label"), "rotate_right", Images.SIZE_MEDIUM, buildKeyStroke(82, KeyEventConstants.MODIFIER_ROTATE_RIGHT));
        if (this.viewerController != null && mi != null) {
            this.viewerController.setRotateRightMenuItem(mi);
        }
        return mi;
    }

    public JMenuItem buildShowHideToolBarMenuItem() {
        JMenuItem mi = makeMenuItem("", null);
        if (this.viewerController != null && mi != null) {
            this.viewerController.setShowHideToolBarMenuItem(mi);
        }
        return mi;
    }

    public JMenuItem buildShowHideUtilityPaneMenuItem() {
        JMenuItem mi = makeMenuItem("", null);
        if (this.viewerController != null && mi != null) {
            this.viewerController.setShowHideUtilityPaneMenuItem(mi);
        }
        return mi;
    }

    public JMenu buildDocumentMenu() {
        JMenu documentMenu = new JMenu(this.messageBundle.getString("viewer.menu.document.label"));
        documentMenu.setMnemonic(buildMnemonic(this.messageBundle.getString("viewer.menu.document.mnemonic").charAt(0)));
        addToMenu(documentMenu, buildFirstPageMenuItem());
        addToMenu(documentMenu, buildPreviousPageMenuItem());
        addToMenu(documentMenu, buildNextPageMenuItem());
        addToMenu(documentMenu, buildLastPageMenuItem());
        documentMenu.addSeparator();
        addToMenu(documentMenu, buildSearchMenuItem());
        addToMenu(documentMenu, buildGoToPageMenuItem());
        return documentMenu;
    }

    public JMenuItem buildFirstPageMenuItem() {
        JMenuItem mi = makeMenuItem(this.messageBundle.getString("viewer.menu.document.firstPage.label"), "page_first", Images.SIZE_MEDIUM, buildKeyStroke(38, KeyEventConstants.MODIFIER_FIRST_PAGE));
        if (this.viewerController != null && mi != null) {
            this.viewerController.setFirstPageMenuItem(mi);
        }
        return mi;
    }

    public JMenuItem buildPreviousPageMenuItem() {
        JMenuItem mi = makeMenuItem(this.messageBundle.getString("viewer.menu.document.previousPage.label"), "page_up", Images.SIZE_MEDIUM, buildKeyStroke(37, KeyEventConstants.MODIFIER_PREVIOUS_PAGE));
        if (this.viewerController != null && mi != null) {
            this.viewerController.setPreviousPageMenuItem(mi);
        }
        return mi;
    }

    public JMenuItem buildNextPageMenuItem() {
        JMenuItem mi = makeMenuItem(this.messageBundle.getString("viewer.menu.document.nextPage.label"), "page_down", Images.SIZE_MEDIUM, buildKeyStroke(39, KeyEventConstants.MODIFIER_NEXT_PAGE));
        if (this.viewerController != null && mi != null) {
            this.viewerController.setNextPageMenuItem(mi);
        }
        return mi;
    }

    public JMenuItem buildLastPageMenuItem() {
        JMenuItem mi = makeMenuItem(this.messageBundle.getString("viewer.menu.document.lastPage.label"), "page_last", Images.SIZE_MEDIUM, buildKeyStroke(40, KeyEventConstants.MODIFIER_LAST_PAGE));
        if (this.viewerController != null && mi != null) {
            this.viewerController.setLastPageMenuItem(mi);
        }
        return mi;
    }

    public JMenuItem buildSearchMenuItem() {
        JMenuItem mi = makeMenuItem(this.messageBundle.getString("viewer.menu.document.search.label"), buildKeyStroke(83, KeyEventConstants.MODIFIER_SEARCH));
        if (this.viewerController != null && mi != null) {
            this.viewerController.setSearchMenuItem(mi);
        }
        return mi;
    }

    public JMenuItem buildGoToPageMenuItem() {
        JMenuItem mi = makeMenuItem(this.messageBundle.getString("viewer.menu.document.gotToPage.label"), buildKeyStroke(78, KeyEventConstants.MODIFIER_GOTO));
        if (this.viewerController != null && mi != null) {
            this.viewerController.setGoToPageMenuItem(mi);
        }
        return mi;
    }

    public JMenu buildWindowMenu() {
        final JMenu windowMenu = new JMenu(this.messageBundle.getString("viewer.menu.window.label"));
        windowMenu.setMnemonic(buildMnemonic(this.messageBundle.getString("viewer.menu.window.mnemonic").charAt(0)));
        addToMenu(windowMenu, buildMinimiseAllMenuItem());
        addToMenu(windowMenu, buildBringAllToFrontMenuItem());
        windowMenu.addSeparator();
        final int allowedCount = windowMenu.getItemCount();
        windowMenu.addMenuListener(new MenuListener() { // from class: org.icepdf.ri.common.SwingViewBuilder.1
            @Override // javax.swing.event.MenuListener
            public void menuCanceled(MenuEvent e2) {
            }

            @Override // javax.swing.event.MenuListener
            public void menuDeselected(MenuEvent e2) {
            }

            @Override // javax.swing.event.MenuListener
            public void menuSelected(MenuEvent e2) {
                for (int count = windowMenu.getItemCount(); count > allowedCount; count--) {
                    windowMenu.remove(count - 1);
                }
                SwingViewBuilder.this.buildWindowListMenuItems(windowMenu);
            }
        });
        return windowMenu;
    }

    public JMenuItem buildMinimiseAllMenuItem() {
        JMenuItem mi = makeMenuItem(this.messageBundle.getString("viewer.menu.window.minAll.label"), null);
        mi.setMnemonic(buildMnemonic(this.messageBundle.getString("viewer.menu.window.minAll.mnemonic").charAt(0)));
        if (this.viewerController != null) {
            this.viewerController.setMinimiseAllMenuItem(mi);
        }
        return mi;
    }

    public JMenuItem buildBringAllToFrontMenuItem() {
        JMenuItem mi = makeMenuItem(this.messageBundle.getString("viewer.menu.window.frontAll.label"), null);
        mi.setMnemonic(buildMnemonic(this.messageBundle.getString("viewer.menu.window.frontAll.mnemonic").charAt(0)));
        if (this.viewerController != null) {
            this.viewerController.setBringAllToFrontMenuItem(mi);
        }
        return mi;
    }

    public void buildWindowListMenuItems(JMenu menu) {
        String miText;
        if (this.viewerController != null && this.viewerController.getWindowManagementCallback() != null) {
            WindowManagementCallback winMgr = this.viewerController.getWindowManagementCallback();
            List<Object> windowDocOriginList = winMgr.getWindowDocumentOriginList(this.viewerController);
            int currWindowIndex = -1;
            int count = windowDocOriginList.size();
            if (count > 0 && (windowDocOriginList.get(count - 1) instanceof Integer)) {
                count--;
                currWindowIndex = ((Integer) windowDocOriginList.remove(count)).intValue();
            }
            shortenDocumentOrigins(windowDocOriginList);
            List<JMenuItem> windowListMenuItems = new ArrayList<>(Math.max(count, 1));
            for (int i2 = 0; i2 < count; i2++) {
                String number = Integer.toString(i2 + 1);
                String label = null;
                String mnemonic = null;
                try {
                    label = this.messageBundle.getString("viewer.menu.window." + number + ".label");
                    mnemonic = this.messageBundle.getString("viewer.menu.window." + number + ".mnemonic");
                } catch (Exception e2) {
                    logger.log(Level.FINER, "Error setting viewer window window title", (Throwable) e2);
                }
                String identifier = (String) windowDocOriginList.get(i2);
                if (identifier == null) {
                    identifier = "";
                }
                if (label != null && label.length() > 0) {
                    miText = number + Constants.INDENT + identifier;
                } else {
                    miText = "    " + identifier;
                }
                JMenuItem mi = new JMenuItem(miText);
                if (mnemonic != null && number.length() == 1) {
                    mi.setMnemonic(buildMnemonic(number.charAt(0)));
                }
                if (currWindowIndex == i2) {
                    mi.setEnabled(false);
                }
                menu.add(mi);
                windowListMenuItems.add(mi);
            }
            this.viewerController.setWindowListMenuItems(windowListMenuItems);
        }
    }

    protected void shortenDocumentOrigins(List<Object> windowDocOriginList) {
        for (int i2 = windowDocOriginList.size() - 1; i2 >= 0; i2--) {
            String identifier = (String) windowDocOriginList.get(i2);
            if (identifier != null) {
                int separatorIndex = identifier.lastIndexOf(File.separator);
                int forewardSlashIndex = identifier.lastIndexOf("/");
                int backwardSlashIndex = identifier.lastIndexOf(FXMLLoader.ESCAPE_PREFIX);
                int cutIndex = Math.max(separatorIndex, Math.max(forewardSlashIndex, backwardSlashIndex));
                if (cutIndex >= 0) {
                    windowDocOriginList.set(i2, identifier.substring(cutIndex));
                }
            }
        }
    }

    public JMenu buildHelpMenu() {
        JMenu helpMenu = new JMenu(this.messageBundle.getString("viewer.menu.help.label"));
        helpMenu.setMnemonic(buildMnemonic(this.messageBundle.getString("viewer.menu.help.mnemonic").charAt(0)));
        if (!isMacOs) {
            addToMenu(helpMenu, buildAboutMenuItem());
        }
        return helpMenu;
    }

    public JMenuItem buildAboutMenuItem() {
        JMenuItem mi = makeMenuItem(this.messageBundle.getString("viewer.menu.help.about.label"), null);
        if (this.viewerController != null && mi != null) {
            this.viewerController.setAboutMenuItem(mi);
        }
        return mi;
    }

    public JToolBar buildCompleteToolBar(boolean embeddableComponent) {
        JToolBar toolbar = new JToolBar();
        toolbar.setLayout(new ToolbarLayout(0, 0, 0));
        commonToolBarSetup(toolbar, true);
        doubleCheckPropertiesManager();
        if (PropertiesManager.checkAndStoreBooleanProperty(this.propertiesManager, PropertiesManager.PROPERTY_SHOW_TOOLBAR_UTILITY)) {
            addToToolBar(toolbar, buildUtilityToolBar(embeddableComponent, this.propertiesManager));
        }
        if (PropertiesManager.checkAndStoreBooleanProperty(this.propertiesManager, PropertiesManager.PROPERTY_SHOW_TOOLBAR_PAGENAV)) {
            addToToolBar(toolbar, buildPageNavigationToolBar());
        }
        if (PropertiesManager.checkAndStoreBooleanProperty(this.propertiesManager, PropertiesManager.PROPERTY_SHOW_TOOLBAR_ZOOM)) {
            addToToolBar(toolbar, buildZoomToolBar());
        }
        if (PropertiesManager.checkAndStoreBooleanProperty(this.propertiesManager, PropertiesManager.PROPERTY_SHOW_TOOLBAR_FIT)) {
            addToToolBar(toolbar, buildFitToolBar());
        }
        if (PropertiesManager.checkAndStoreBooleanProperty(this.propertiesManager, PropertiesManager.PROPERTY_SHOW_TOOLBAR_ROTATE)) {
            addToToolBar(toolbar, buildRotateToolBar());
        }
        if (PropertiesManager.checkAndStoreBooleanProperty(this.propertiesManager, PropertiesManager.PROPERTY_SHOW_TOOLBAR_TOOL)) {
            addToToolBar(toolbar, buildToolToolBar());
        }
        if (PropertiesManager.checkAndStoreBooleanProperty(this.propertiesManager, PropertiesManager.PROPERTY_SHOW_TOOLBAR_ANNOTATION)) {
            addToToolBar(toolbar, buildAnnotationlToolBar());
        }
        if (isDemo) {
            addToToolBar(toolbar, buildDemoToolBar());
        }
        if (toolbar.getComponentCount() == 0) {
            toolbar = null;
        }
        if (this.viewerController != null && toolbar != null) {
            this.viewerController.setCompleteToolBar(toolbar);
        }
        return toolbar;
    }

    public JToolBar buildUtilityToolBar(boolean embeddableComponent) {
        return buildUtilityToolBar(embeddableComponent, null);
    }

    public JToolBar buildUtilityToolBar(boolean embeddableComponent, PropertiesManager propertiesManager) {
        JToolBar toolbar = new JToolBar();
        commonToolBarSetup(toolbar, false);
        if (!embeddableComponent && PropertiesManager.checkAndStoreBooleanProperty(propertiesManager, PropertiesManager.PROPERTY_SHOW_UTILITY_OPEN)) {
            addToToolBar(toolbar, buildOpenFileButton());
        }
        if (PropertiesManager.checkAndStoreBooleanProperty(propertiesManager, PropertiesManager.PROPERTY_SHOW_UTILITY_SAVE)) {
            addToToolBar(toolbar, buildSaveAsFileButton());
        }
        if (PropertiesManager.checkAndStoreBooleanProperty(propertiesManager, PropertiesManager.PROPERTY_SHOW_UTILITY_PRINT)) {
            addToToolBar(toolbar, buildPrintButton());
        }
        if (PropertiesManager.checkAndStoreBooleanProperty(propertiesManager, PropertiesManager.PROPERTY_SHOW_UTILITY_SEARCH)) {
            addToToolBar(toolbar, buildSearchButton());
        }
        if (PropertiesManager.checkAndStoreBooleanProperty(propertiesManager, PropertiesManager.PROPERTY_SHOW_UTILITY_UPANE)) {
            addToToolBar(toolbar, buildShowHideUtilityPaneButton());
        }
        if (toolbar.getComponentCount() == 0) {
            return null;
        }
        return toolbar;
    }

    public JButton buildOpenFileButton() {
        JButton btn = makeToolbarButton(this.messageBundle.getString("viewer.toolbar.open.label"), this.messageBundle.getString("viewer.toolbar.open.tooltip"), "open", Images.SIZE_LARGE, this.buttonFont);
        if (this.viewerController != null && btn != null) {
            this.viewerController.setOpenFileButton(btn);
        }
        return btn;
    }

    public JButton buildSaveAsFileButton() {
        JButton btn = makeToolbarButton(this.messageBundle.getString("viewer.toolbar.saveAs.label"), this.messageBundle.getString("viewer.toolbar.saveAs.tooltip"), "save", Images.SIZE_LARGE, this.buttonFont);
        if (this.viewerController != null && btn != null) {
            this.viewerController.setSaveAsFileButton(btn);
        }
        return btn;
    }

    public JButton buildPrintButton() {
        JButton btn = makeToolbarButton(this.messageBundle.getString("viewer.toolbar.print.label"), this.messageBundle.getString("viewer.toolbar.print.tooltip"), "print", Images.SIZE_LARGE, this.buttonFont);
        if (this.viewerController != null && btn != null) {
            this.viewerController.setPrintButton(btn);
        }
        return btn;
    }

    public JButton buildSearchButton() {
        JButton btn = makeToolbarButton(this.messageBundle.getString("viewer.toolbar.search.label"), this.messageBundle.getString("viewer.toolbar.search.tooltip"), "search", Images.SIZE_LARGE, this.buttonFont);
        if (this.viewerController != null && btn != null) {
            this.viewerController.setSearchButton(btn);
        }
        return btn;
    }

    public JToggleButton buildShowHideUtilityPaneButton() {
        JToggleButton btn = makeToolbarToggleButton(this.messageBundle.getString("viewer.toolbar.utilityPane.label"), this.messageBundle.getString("viewer.toolbar.utilityPane.tooltip"), "utility_pane", Images.SIZE_LARGE, this.buttonFont);
        if (this.viewerController != null && btn != null) {
            this.viewerController.setShowHideUtilityPaneButton(btn);
        }
        return btn;
    }

    public JToolBar buildPageNavigationToolBar() {
        JToolBar toolbar = new JToolBar();
        commonToolBarSetup(toolbar, false);
        addToToolBar(toolbar, buildFirstPageButton());
        addToToolBar(toolbar, buildPreviousPageButton());
        addToToolBar(toolbar, buildCurrentPageNumberTextField());
        addToToolBar(toolbar, buildNumberOfPagesLabel());
        addToToolBar(toolbar, buildNextPageButton());
        addToToolBar(toolbar, buildLastPageButton());
        return toolbar;
    }

    public JButton buildFirstPageButton() {
        JButton btn = makeToolbarButton(this.messageBundle.getString("viewer.toolbar.navigation.firstPage.label"), this.messageBundle.getString("viewer.toolbar.navigation.firstPage.tooltip"), "first", Images.SIZE_LARGE, this.buttonFont);
        if (this.viewerController != null && btn != null) {
            this.viewerController.setFirstPageButton(btn);
        }
        return btn;
    }

    public JButton buildPreviousPageButton() {
        JButton btn = makeToolbarButton(this.messageBundle.getString("viewer.toolbar.navigation.previousPage.label"), this.messageBundle.getString("viewer.toolbar.navigation.previousPage.tooltip"), "back", Images.SIZE_LARGE, this.buttonFont);
        if (this.viewerController != null && btn != null) {
            this.viewerController.setPreviousPageButton(btn);
        }
        return btn;
    }

    public JButton buildNextPageButton() {
        JButton btn = makeToolbarButton(this.messageBundle.getString("viewer.toolbar.navigation.nextPage.label"), this.messageBundle.getString("viewer.toolbar.navigation.nextPage.tooltip"), "forward", Images.SIZE_LARGE, this.buttonFont);
        if (this.viewerController != null && btn != null) {
            this.viewerController.setNextPageButton(btn);
        }
        return btn;
    }

    public JButton buildLastPageButton() {
        JButton btn = makeToolbarButton(this.messageBundle.getString("viewer.toolbar.navigation.lastPage.label"), this.messageBundle.getString("viewer.toolbar.navigation.lastPage.tooltip"), Keywords.FUNC_LAST_STRING, Images.SIZE_LARGE, this.buttonFont);
        if (this.viewerController != null && btn != null) {
            this.viewerController.setLastPageButton(btn);
        }
        return btn;
    }

    public JTextField buildCurrentPageNumberTextField() {
        JTextField pageNumberTextField = new JTextField("", 3);
        pageNumberTextField.setToolTipText(this.messageBundle.getString("viewer.toolbar.navigation.current.tooltip"));
        pageNumberTextField.setInputVerifier(new PageNumberTextFieldInputVerifier());
        pageNumberTextField.addKeyListener(new PageNumberTextFieldKeyListener());
        if (this.viewerController != null) {
            this.viewerController.setCurrentPageNumberTextField(pageNumberTextField);
        }
        return pageNumberTextField;
    }

    public JLabel buildNumberOfPagesLabel() {
        JLabel lbl = new JLabel();
        lbl.setToolTipText(this.messageBundle.getString("viewer.toolbar.navigation.pages.tooltip"));
        if (this.viewerController != null) {
            this.viewerController.setNumberOfPagesLabel(lbl);
        }
        return lbl;
    }

    public JToolBar buildZoomToolBar() {
        JToolBar toolbar = new JToolBar();
        commonToolBarSetup(toolbar, false);
        addToToolBar(toolbar, buildZoomOutButton());
        addToToolBar(toolbar, buildZoomCombBox());
        addToToolBar(toolbar, buildZoomInButton());
        return toolbar;
    }

    public JButton buildZoomOutButton() {
        JButton btn = makeToolbarButton(this.messageBundle.getString("viewer.toolbar.zoom.out.label"), this.messageBundle.getString("viewer.toolbar.zoom.out.tooltip"), "zoom_out", Images.SIZE_LARGE, this.buttonFont);
        if (this.viewerController != null && btn != null) {
            this.viewerController.setZoomOutButton(btn);
        }
        return btn;
    }

    public JComboBox buildZoomCombBox() {
        doubleCheckPropertiesManager();
        this.zoomLevels = PropertiesManager.checkAndStoreFloatArrayProperty(this.propertiesManager, PropertiesManager.PROPERTY_ZOOM_RANGES, this.zoomLevels);
        JComboBox tmp = new JComboBox();
        tmp.setToolTipText(this.messageBundle.getString("viewer.toolbar.zoom.tooltip"));
        tmp.setPreferredSize(new Dimension(75, tmp.getHeight()));
        float[] arr$ = this.zoomLevels;
        for (float zoomLevel : arr$) {
            tmp.addItem(NumberFormat.getPercentInstance().format(zoomLevel));
        }
        tmp.setEditable(true);
        if (this.viewerController != null) {
            this.viewerController.setZoomComboBox(tmp, this.zoomLevels);
        }
        return tmp;
    }

    public JButton buildZoomInButton() {
        JButton btn = makeToolbarButton(this.messageBundle.getString("viewer.toolbar.zoom.in.label"), this.messageBundle.getString("viewer.toolbar.zoom.in.tooltip"), "zoom_in", Images.SIZE_LARGE, this.buttonFont);
        if (this.viewerController != null && btn != null) {
            this.viewerController.setZoomInButton(btn);
        }
        return btn;
    }

    public JToolBar buildFitToolBar() {
        JToolBar toolbar = new JToolBar();
        commonToolBarSetup(toolbar, false);
        addToToolBar(toolbar, buildFitActualSizeButton());
        addToToolBar(toolbar, buildFitPageButton());
        addToToolBar(toolbar, buildFitWidthButton());
        return toolbar;
    }

    public JToggleButton buildFitActualSizeButton() {
        JToggleButton btn = makeToolbarToggleButton(this.messageBundle.getString("viewer.toolbar.pageFit.actualsize.label"), this.messageBundle.getString("viewer.toolbar.pageFit.actualsize.tooltip"), "actual_size", Images.SIZE_LARGE, this.buttonFont);
        if (this.viewerController != null && btn != null) {
            this.viewerController.setFitActualSizeButton(btn);
        }
        return btn;
    }

    public JToggleButton buildFitPageButton() {
        JToggleButton btn = makeToolbarToggleButton(this.messageBundle.getString("viewer.toolbar.pageFit.fitWindow.label"), this.messageBundle.getString("viewer.toolbar.pageFit.fitWindow.tooltip"), "fit_window", Images.SIZE_LARGE, this.buttonFont);
        if (this.viewerController != null && btn != null) {
            this.viewerController.setFitHeightButton(btn);
        }
        return btn;
    }

    public JToggleButton buildFontEngineButton() {
        JToggleButton btn = makeToolbarToggleButton(this.messageBundle.getString("viewer.toolbar.pageFit.fontEngine.label"), this.messageBundle.getString("viewer.toolbar.pageFit.fontEngine.tooltip"), "font-engine", 118, 25, this.buttonFont);
        if (this.viewerController != null && btn != null) {
            this.viewerController.setFontEngineButton(btn);
        }
        return btn;
    }

    public JToggleButton buildFitWidthButton() {
        JToggleButton btn = makeToolbarToggleButton(this.messageBundle.getString("viewer.toolbar.pageFit.fitWidth.label"), this.messageBundle.getString("viewer.toolbar.pageFit.fitWidth.tooltip"), "fit_width", Images.SIZE_LARGE, this.buttonFont);
        if (this.viewerController != null && btn != null) {
            this.viewerController.setFitWidthButton(btn);
        }
        return btn;
    }

    public JToolBar buildRotateToolBar() {
        JToolBar toolbar = new JToolBar();
        commonToolBarSetup(toolbar, false);
        addToToolBar(toolbar, buildRotateRightButton());
        addToToolBar(toolbar, buildRotateLeftButton());
        return toolbar;
    }

    public JButton buildRotateLeftButton() {
        JButton btn = makeToolbarButton(this.messageBundle.getString("viewer.toolbar.rotation.left.label"), this.messageBundle.getString("viewer.toolbar.rotation.left.tooltip"), "rotate_left", Images.SIZE_LARGE, this.buttonFont);
        if (this.viewerController != null && btn != null) {
            this.viewerController.setRotateLeftButton(btn);
        }
        return btn;
    }

    public JButton buildRotateRightButton() {
        JButton btn = makeToolbarButton(this.messageBundle.getString("viewer.toolbar.rotation.right.label"), this.messageBundle.getString("viewer.toolbar.rotation.right.tooltip"), "rotate_right", Images.SIZE_LARGE, this.buttonFont);
        if (this.viewerController != null && btn != null) {
            this.viewerController.setRotateRightButton(btn);
        }
        return btn;
    }

    public JToolBar buildToolToolBar() {
        JToolBar toolbar = new JToolBar();
        commonToolBarSetup(toolbar, false);
        addToToolBar(toolbar, buildPanToolButton());
        addToToolBar(toolbar, buildTextSelectToolButton());
        addToToolBar(toolbar, buildZoomInToolButton());
        addToToolBar(toolbar, buildZoomOutToolButton());
        return toolbar;
    }

    public JToolBar buildAnnotationlToolBar() {
        JToolBar toolbar = new JToolBar();
        commonToolBarSetup(toolbar, false);
        addToToolBar(toolbar, buildSelectToolButton(Images.SIZE_LARGE));
        addToToolBar(toolbar, buildHighlightAnnotationToolButton(Images.SIZE_LARGE));
        addToToolBar(toolbar, buildTextAnnotationToolButton(Images.SIZE_LARGE));
        return toolbar;
    }

    public JToolBar buildAnnotationUtilityToolBar() {
        JToolBar toolbar = new JToolBar();
        commonToolBarSetup(toolbar, true);
        addToToolBar(toolbar, buildHighlightAnnotationUtilityToolButton(Images.SIZE_MEDIUM));
        addToToolBar(toolbar, buildStrikeOutAnnotationToolButton());
        addToToolBar(toolbar, buildUnderlineAnnotationToolButton());
        addToToolBar(toolbar, buildLineAnnotationToolButton());
        addToToolBar(toolbar, buildLinkAnnotationToolButton());
        addToToolBar(toolbar, buildLineArrowAnnotationToolButton());
        addToToolBar(toolbar, buildSquareAnnotationToolButton());
        addToToolBar(toolbar, buildCircleAnnotationToolButton());
        addToToolBar(toolbar, buildInkAnnotationToolButton());
        addToToolBar(toolbar, buildFreeTextAnnotationToolButton());
        addToToolBar(toolbar, buildTextAnnotationUtilityToolButton(Images.SIZE_MEDIUM));
        return toolbar;
    }

    public JToolBar buildDemoToolBar() {
        JToolBar toolbar = new JToolBar();
        commonToolBarSetup(toolbar, false);
        addToToolBar(toolbar, buildFontEngineButton());
        return toolbar;
    }

    public JToggleButton buildPanToolButton() {
        JToggleButton btn = makeToolbarToggleButton(this.messageBundle.getString("viewer.toolbar.tool.pan.label"), this.messageBundle.getString("viewer.toolbar.tool.pan.tooltip"), "pan", Images.SIZE_LARGE, this.buttonFont);
        if (this.viewerController != null && btn != null) {
            this.viewerController.setPanToolButton(btn);
        }
        return btn;
    }

    public JToggleButton buildTextSelectToolButton() {
        JToggleButton btn = makeToolbarToggleButton(this.messageBundle.getString("viewer.toolbar.tool.text.label"), this.messageBundle.getString("viewer.toolbar.tool.text.tooltip"), "selection_text", Images.SIZE_LARGE, this.buttonFont);
        if (this.viewerController != null && btn != null) {
            this.viewerController.setTextSelectToolButton(btn);
        }
        return btn;
    }

    public JToggleButton buildSelectToolButton(String imageSize) {
        JToggleButton btn = makeToolbarToggleButton(this.messageBundle.getString("viewer.toolbar.tool.select.label"), this.messageBundle.getString("viewer.toolbar.tool.select.tooltip"), com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_SELECT, imageSize, this.buttonFont);
        if (this.viewerController != null && btn != null) {
            this.viewerController.setSelectToolButton(btn);
        }
        return btn;
    }

    public JToggleButton buildLinkAnnotationToolButton() {
        JToggleButton btn = makeToolbarToggleButtonSmall(this.messageBundle.getString("viewer.toolbar.tool.link.label"), this.messageBundle.getString("viewer.toolbar.tool.link.tooltip"), "link_annot", Images.SIZE_MEDIUM, this.buttonFont);
        if (this.viewerController != null && btn != null) {
            this.viewerController.setLinkAnnotationToolButton(btn);
        }
        return btn;
    }

    public JToggleButton buildHighlightAnnotationToolButton(String imageSize) {
        JToggleButton btn = makeToolbarToggleButton(this.messageBundle.getString("viewer.toolbar.tool.highlight.label"), this.messageBundle.getString("viewer.toolbar.tool.highlight.tooltip"), "highlight_annot", imageSize, this.buttonFont);
        if (this.viewerController != null && btn != null) {
            this.viewerController.setHighlightAnnotationToolButton(btn);
        }
        return btn;
    }

    public JToggleButton buildHighlightAnnotationUtilityToolButton(String imageSize) {
        JToggleButton btn = makeToolbarToggleButtonSmall(this.messageBundle.getString("viewer.toolbar.tool.highlight.label"), this.messageBundle.getString("viewer.toolbar.tool.highlight.tooltip"), "highlight_annot", imageSize, this.buttonFont);
        if (this.viewerController != null && btn != null) {
            this.viewerController.setHighlightAnnotationUtilityToolButton(btn);
        }
        return btn;
    }

    public JToggleButton buildStrikeOutAnnotationToolButton() {
        JToggleButton btn = makeToolbarToggleButtonSmall(this.messageBundle.getString("viewer.toolbar.tool.strikeOut.label"), this.messageBundle.getString("viewer.toolbar.tool.strikeOut.tooltip"), "strikeout", Images.SIZE_MEDIUM, this.buttonFont);
        if (this.viewerController != null && btn != null) {
            this.viewerController.setStrikeOutAnnotationToolButton(btn);
        }
        return btn;
    }

    public JToggleButton buildUnderlineAnnotationToolButton() {
        JToggleButton btn = makeToolbarToggleButtonSmall(this.messageBundle.getString("viewer.toolbar.tool.underline.label"), this.messageBundle.getString("viewer.toolbar.tool.underline.tooltip"), "underline", Images.SIZE_MEDIUM, this.buttonFont);
        if (this.viewerController != null && btn != null) {
            this.viewerController.setUnderlineAnnotationToolButton(btn);
        }
        return btn;
    }

    public JToggleButton buildLineAnnotationToolButton() {
        JToggleButton btn = makeToolbarToggleButtonSmall(this.messageBundle.getString("viewer.toolbar.tool.line.label"), this.messageBundle.getString("viewer.toolbar.tool.line.tooltip"), "line", Images.SIZE_MEDIUM, this.buttonFont);
        if (this.viewerController != null && btn != null) {
            this.viewerController.setLineAnnotationToolButton(btn);
        }
        return btn;
    }

    public JToggleButton buildLineArrowAnnotationToolButton() {
        JToggleButton btn = makeToolbarToggleButtonSmall(this.messageBundle.getString("viewer.toolbar.tool.lineArrow.label"), this.messageBundle.getString("viewer.toolbar.tool.lineArrow.tooltip"), "arrow", Images.SIZE_MEDIUM, this.buttonFont);
        if (this.viewerController != null && btn != null) {
            this.viewerController.setLineArrowAnnotationToolButton(btn);
        }
        return btn;
    }

    public JToggleButton buildSquareAnnotationToolButton() {
        JToggleButton btn = makeToolbarToggleButtonSmall(this.messageBundle.getString("viewer.toolbar.tool.rectangle.label"), this.messageBundle.getString("viewer.toolbar.tool.rectangle.tooltip"), "square", Images.SIZE_MEDIUM, this.buttonFont);
        if (this.viewerController != null && btn != null) {
            this.viewerController.setSquareAnnotationToolButton(btn);
        }
        return btn;
    }

    public JToggleButton buildCircleAnnotationToolButton() {
        JToggleButton btn = makeToolbarToggleButtonSmall(this.messageBundle.getString("viewer.toolbar.tool.circle.label"), this.messageBundle.getString("viewer.toolbar.tool.circle.tooltip"), "circle", Images.SIZE_MEDIUM, this.buttonFont);
        if (this.viewerController != null && btn != null) {
            this.viewerController.setCircleAnnotationToolButton(btn);
        }
        return btn;
    }

    public JToggleButton buildInkAnnotationToolButton() {
        JToggleButton btn = makeToolbarToggleButtonSmall(this.messageBundle.getString("viewer.toolbar.tool.ink.label"), this.messageBundle.getString("viewer.toolbar.tool.ink.tooltip"), "ink", Images.SIZE_MEDIUM, this.buttonFont);
        if (this.viewerController != null && btn != null) {
            this.viewerController.setInkAnnotationToolButton(btn);
        }
        return btn;
    }

    public JToggleButton buildFreeTextAnnotationToolButton() {
        JToggleButton btn = makeToolbarToggleButtonSmall(this.messageBundle.getString("viewer.toolbar.tool.freeText.label"), this.messageBundle.getString("viewer.toolbar.tool.freeText.tooltip"), "freetext_annot", Images.SIZE_MEDIUM, this.buttonFont);
        if (this.viewerController != null && btn != null) {
            this.viewerController.setFreeTextAnnotationToolButton(btn);
        }
        return btn;
    }

    public JToggleButton buildTextAnnotationToolButton(String imageSize) {
        JToggleButton btn = makeToolbarToggleButton(this.messageBundle.getString("viewer.toolbar.tool.textAnno.label"), this.messageBundle.getString("viewer.toolbar.tool.textAnno.tooltip"), "text_annot", Images.SIZE_MEDIUM, this.buttonFont);
        if (this.viewerController != null && btn != null) {
            this.viewerController.setTextAnnotationToolButton(btn);
        }
        return btn;
    }

    public JToggleButton buildTextAnnotationUtilityToolButton(String imageSize) {
        JToggleButton btn = makeToolbarToggleButtonSmall(this.messageBundle.getString("viewer.toolbar.tool.textAnno.label"), this.messageBundle.getString("viewer.toolbar.tool.textAnno.tooltip"), "text_annot", Images.SIZE_MEDIUM, this.buttonFont);
        if (this.viewerController != null && btn != null) {
            this.viewerController.setTextAnnotationUtilityToolButton(btn);
        }
        return btn;
    }

    public JToggleButton buildZoomInToolButton() {
        JToggleButton btn = makeToolbarToggleButton(this.messageBundle.getString("viewer.toolbar.tool.zoomMarquis.label"), this.messageBundle.getString("viewer.toolbar.tool.zoomMarquis.tooltip"), "zoom_marquis", Images.SIZE_LARGE, this.buttonFont);
        if (this.viewerController != null && btn != null) {
            this.viewerController.setZoomInToolButton(btn);
        }
        return btn;
    }

    public JToggleButton buildZoomOutToolButton() {
        JToggleButton btn = makeToolbarToggleButton(this.messageBundle.getString("viewer.toolbar.tool.zoomDynamic.label"), this.messageBundle.getString("viewer.toolbar.tool.zoomDynamic.tooltip"), "zoom_dynamic", Images.SIZE_LARGE, this.buttonFont);
        if (this.viewerController != null && btn != null) {
            this.viewerController.setZoomDynamicToolButton(btn);
        }
        return btn;
    }

    public JSplitPane buildUtilityAndDocumentSplitPane(boolean embeddableComponent) throws HeadlessException, IllegalArgumentException {
        JSplitPane splitpane = new JSplitPane(1);
        splitpane.setOneTouchExpandable(false);
        splitpane.setDividerSize(8);
        splitpane.setContinuousLayout(true);
        splitpane.setLeftComponent(buildUtilityTabbedPane());
        DocumentViewController viewController = this.viewerController.getDocumentViewController();
        this.viewerController.setIsEmbeddedComponent(embeddableComponent);
        splitpane.getActionMap().getParent().remove("toggleFocus");
        splitpane.setRightComponent(viewController.getViewContainer());
        int dividerLocation = PropertiesManager.checkAndStoreIntegerProperty(this.propertiesManager, PropertiesManager.PROPERTY_DIVIDER_LOCATION, 260);
        splitpane.setDividerLocation(dividerLocation);
        if (this.viewerController != null) {
            this.viewerController.setUtilityAndDocumentSplitPane(splitpane);
        }
        return splitpane;
    }

    public JTabbedPane buildUtilityTabbedPane() {
        JTabbedPane utilityTabbedPane = new JTabbedPane();
        utilityTabbedPane.setPreferredSize(new Dimension(250, 400));
        doubleCheckPropertiesManager();
        if (PropertiesManager.checkAndStoreBooleanProperty(this.propertiesManager, PropertiesManager.PROPERTY_SHOW_UTILITYPANE_BOOKMARKS)) {
            utilityTabbedPane.add(this.messageBundle.getString("viewer.utilityPane.bookmarks.tab.title"), buildOutlineComponents());
        }
        if (PropertiesManager.checkAndStoreBooleanProperty(this.propertiesManager, PropertiesManager.PROPERTY_SHOW_UTILITYPANE_SEARCH)) {
            utilityTabbedPane.add(this.messageBundle.getString("viewer.utilityPane.search.tab.title"), buildSearchPanel());
        }
        if (PropertiesManager.checkAndStoreBooleanProperty(this.propertiesManager, PropertiesManager.PROPERTY_SHOW_UTILITYPANE_THUMBNAILS)) {
            utilityTabbedPane.add(this.messageBundle.getString("viewer.utilityPane.thumbs.tab.title"), buildThumbsPanel());
        }
        if (PropertiesManager.checkAndStoreBooleanProperty(this.propertiesManager, PropertiesManager.PROPERTY_SHOW_UTILITYPANE_LAYERS)) {
            utilityTabbedPane.add(this.messageBundle.getString("viewer.utilityPane.layers.tab.title"), buildLayersComponents());
        }
        if (PropertiesManager.checkAndStoreBooleanProperty(this.propertiesManager, PropertiesManager.PROPERTY_SHOW_UTILITYPANE_ANNOTATION)) {
            utilityTabbedPane.add(this.messageBundle.getString("viewer.utilityPane.annotation.tab.title"), buildAnnotationPanel());
        }
        if (utilityTabbedPane.getComponentCount() == 0) {
            utilityTabbedPane = null;
        }
        if (this.viewerController != null) {
            this.viewerController.setUtilityTabbedPane(utilityTabbedPane);
        }
        return utilityTabbedPane;
    }

    public JComponent buildOutlineComponents() {
        JTree tree = new OutlinesTree();
        JScrollPane scroll = new JScrollPane(tree);
        if (this.viewerController != null) {
            this.viewerController.setOutlineComponents(tree, scroll);
        }
        return scroll;
    }

    public ThumbnailsPanel buildThumbsPanel() {
        ThumbnailsPanel thumbsPanel = new ThumbnailsPanel(this.viewerController, this.propertiesManager);
        if (this.viewerController != null) {
            this.viewerController.setThumbnailsPanel(thumbsPanel);
        }
        return thumbsPanel;
    }

    public LayersPanel buildLayersComponents() {
        LayersPanel layersPanel = new LayersPanel(this.viewerController);
        if (this.viewerController != null) {
            this.viewerController.setLayersPanel(layersPanel);
        }
        return layersPanel;
    }

    public SearchPanel buildSearchPanel() {
        SearchPanel searchPanel = new SearchPanel(this.viewerController);
        if (this.viewerController != null) {
            this.viewerController.setSearchPanel(searchPanel);
        }
        return searchPanel;
    }

    public AnnotationPanel buildAnnotationPanel() {
        AnnotationPanel annotationPanel = new AnnotationPanel(this.viewerController, this.propertiesManager);
        annotationPanel.setAnnotationUtilityToolbar(buildAnnotationUtilityToolBar());
        if (this.viewerController != null) {
            this.viewerController.setAnnotationPanel(annotationPanel);
        }
        return annotationPanel;
    }

    public JPanel buildStatusPanel() {
        if (PropertiesManager.checkAndStoreBooleanProperty(this.propertiesManager, PropertiesManager.PROPERTY_SHOW_STATUSBAR)) {
            JPanel jPanel = new JPanel(new BorderLayout());
            if (PropertiesManager.checkAndStoreBooleanProperty(this.propertiesManager, PropertiesManager.PROPERTY_SHOW_STATUSBAR_STATUSLABEL)) {
                JPanel pgPanel = new JPanel();
                JLabel lbl = new JLabel(" ");
                lbl.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
                pgPanel.add(lbl);
                jPanel.add(pgPanel, "West");
                if (this.viewerController != null) {
                    this.viewerController.setStatusLabel(lbl);
                }
            }
            JPanel viewPanel = new JPanel();
            if (PropertiesManager.checkAndStoreBooleanProperty(this.propertiesManager, PropertiesManager.PROPERTY_SHOW_STATUSBAR_VIEWMODE)) {
                viewPanel.add(buildPageViewSinglePageNonConToggleButton());
                viewPanel.add(buildPageViewSinglePageConToggleButton());
                viewPanel.add(buildPageViewFacingPageNonConToggleButton());
                viewPanel.add(buildPageViewFacingPageConToggleButton());
            }
            jPanel.add(viewPanel, BorderLayout.CENTER);
            viewPanel.setLayout(new ToolbarLayout(2, 0, 1));
            JLabel lbl2 = new JLabel(" ");
            lbl2.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
            jPanel.add(lbl2, "East");
            return jPanel;
        }
        return null;
    }

    public JToggleButton buildPageViewSinglePageConToggleButton() {
        JToggleButton btn = makeToolbarToggleButton(this.messageBundle.getString("viewer.toolbar.pageView.continuous.singlePage.label"), this.messageBundle.getString("viewer.toolbar.pageView.continuous.singlePage.tooltip"), "single_page_column", Images.SIZE_MEDIUM, this.buttonFont);
        if (this.viewerController != null && btn != null) {
            this.viewerController.setPageViewSinglePageConButton(btn);
        }
        return btn;
    }

    public JToggleButton buildPageViewFacingPageConToggleButton() {
        JToggleButton btn = makeToolbarToggleButton(this.messageBundle.getString("viewer.toolbar.pageView.continuous.facingPage.label"), this.messageBundle.getString("viewer.toolbar.pageView.continuous.facingPage.tooltip"), "two_page_column", Images.SIZE_MEDIUM, this.buttonFont);
        if (this.viewerController != null && btn != null) {
            this.viewerController.setPageViewFacingPageConButton(btn);
        }
        return btn;
    }

    public JToggleButton buildPageViewSinglePageNonConToggleButton() {
        JToggleButton btn = makeToolbarToggleButton(this.messageBundle.getString("viewer.toolbar.pageView.nonContinuous.singlePage.label"), this.messageBundle.getString("viewer.toolbar.pageView.nonContinuous.singlePage.tooltip"), "single_page", Images.SIZE_MEDIUM, this.buttonFont);
        if (this.viewerController != null && btn != null) {
            this.viewerController.setPageViewSinglePageNonConButton(btn);
        }
        return btn;
    }

    public JToggleButton buildPageViewFacingPageNonConToggleButton() {
        JToggleButton btn = makeToolbarToggleButton(this.messageBundle.getString("viewer.toolbar.pageView.nonContinuous.facingPage.label"), this.messageBundle.getString("viewer.toolbar.pageView.nonContinuous.facingPage.tooltip"), "two_page", Images.SIZE_MEDIUM, this.buttonFont);
        if (this.viewerController != null && btn != null) {
            this.viewerController.setPageViewFacingPageNonConButton(btn);
        }
        return btn;
    }

    protected JButton makeToolbarButton(String title, String toolTip, String imageName, String imageSize, Font font) {
        JButton tmp = new JButton(this.showButtonText ? title : "");
        tmp.setFont(font);
        tmp.setToolTipText(toolTip);
        tmp.setPreferredSize(new Dimension(32, 32));
        try {
            tmp.setIcon(new ImageIcon(Images.get(imageName + "_a" + imageSize + ".png")));
            tmp.setPressedIcon(new ImageIcon(Images.get(imageName + "_i" + imageSize + ".png")));
            tmp.setRolloverIcon(new ImageIcon(Images.get(imageName + "_r" + imageSize + ".png")));
            tmp.setDisabledIcon(new ImageIcon(Images.get(imageName + "_i" + imageSize + ".png")));
        } catch (NullPointerException e2) {
            logger.warning("Failed to load toolbar button images: " + imageName + "_i" + imageSize + ".png");
        }
        tmp.setRolloverEnabled(true);
        tmp.setBorderPainted(false);
        tmp.setContentAreaFilled(false);
        tmp.setFocusPainted(true);
        return tmp;
    }

    protected JToggleButton makeToolbarToggleButton(String title, String toolTip, String imageName, String imageSize, Font font) {
        JToggleButton tmp = new JToggleButton(this.showButtonText ? title : "");
        tmp.setFont(font);
        tmp.setToolTipText(toolTip);
        tmp.setPreferredSize(new Dimension(32, 32));
        tmp.setRolloverEnabled(true);
        try {
            tmp.setIcon(new ImageIcon(Images.get(imageName + "_a" + imageSize + ".png")));
            tmp.setPressedIcon(new ImageIcon(Images.get(imageName + "_i" + imageSize + ".png")));
            tmp.setRolloverIcon(new ImageIcon(Images.get(imageName + "_r" + imageSize + ".png")));
            tmp.setDisabledIcon(new ImageIcon(Images.get(imageName + "_i" + imageSize + ".png")));
        } catch (NullPointerException e2) {
            logger.warning("Failed to load toolbar toggle button images: " + imageName + "_i" + imageSize + ".png");
        }
        tmp.setBorder(BorderFactory.createEmptyBorder());
        tmp.setContentAreaFilled(false);
        tmp.setFocusPainted(true);
        return tmp;
    }

    protected JToggleButton makeToolbarToggleButtonSmall(String title, String toolTip, String imageName, String imageSize, Font font) {
        JToggleButton tmp = new JToggleButton(this.showButtonText ? title : "");
        tmp.setFont(font);
        tmp.setToolTipText(toolTip);
        tmp.setPreferredSize(new Dimension(24, 24));
        try {
            tmp.setIcon(new ImageIcon(Images.get(imageName + "_a" + imageSize + ".png")));
            tmp.setPressedIcon(new ImageIcon(Images.get(imageName + "_i" + imageSize + ".png")));
            tmp.setRolloverIcon(new ImageIcon(Images.get(imageName + "_r" + imageSize + ".png")));
            tmp.setDisabledIcon(new ImageIcon(Images.get(imageName + "_i" + imageSize + ".png")));
        } catch (NullPointerException e2) {
            logger.warning("Failed to load toolbar toggle images: " + imageName + "_i" + imageSize + ".png");
        }
        tmp.setBorder(BorderFactory.createEmptyBorder());
        tmp.setContentAreaFilled(false);
        tmp.setRolloverEnabled(true);
        tmp.setFocusPainted(true);
        return tmp;
    }

    protected JToggleButton makeToolbarToggleButton(String title, String toolTip, Font font) {
        JToggleButton tmp = new JToggleButton(this.showButtonText ? title : "");
        tmp.setFont(font);
        tmp.setToolTipText(toolTip);
        tmp.setPreferredSize(new Dimension(30, 30));
        tmp.setText(title);
        tmp.setFocusPainted(true);
        return tmp;
    }

    protected JToggleButton makeToolbarToggleButton(String title, String toolTip, String imageName, int imageWidth, int imageHeight, Font font) {
        JToggleButton tmp = new JToggleButton(this.showButtonText ? title : "");
        tmp.setFont(font);
        tmp.setToolTipText(toolTip);
        tmp.setRolloverEnabled(false);
        tmp.setPreferredSize(new Dimension(imageWidth, imageHeight));
        try {
            tmp.setIcon(new ImageIcon(Images.get(imageName + "_d.png")));
            tmp.setPressedIcon(new ImageIcon(Images.get(imageName + "_d.png")));
            tmp.setSelectedIcon(new ImageIcon(Images.get(imageName + "_n.png")));
            tmp.setDisabledIcon(new ImageIcon(Images.get(imageName + "_n.png")));
        } catch (NullPointerException e2) {
            logger.warning("Failed to load toobar toggle button images: " + imageName + ".png");
        }
        tmp.setBorderPainted(false);
        tmp.setBorder(BorderFactory.createEmptyBorder());
        tmp.setContentAreaFilled(false);
        tmp.setFocusPainted(false);
        return tmp;
    }

    protected JMenuItem makeMenuItem(String text, KeyStroke accel) {
        JMenuItem jmi = new JMenuItem(text);
        if (accel != null) {
            jmi.setAccelerator(accel);
        }
        return jmi;
    }

    protected JMenuItem makeMenuItem(String text, String imageName, String imageSize, KeyStroke accel) {
        JMenuItem jmi = new JMenuItem(text);
        if (imageName != null) {
            try {
                jmi.setIcon(new ImageIcon(Images.get(imageName + "_a" + imageSize + ".png")));
                jmi.setDisabledIcon(new ImageIcon(Images.get(imageName + "_i" + imageSize + ".png")));
                jmi.setRolloverIcon(new ImageIcon(Images.get(imageName + "_r" + imageSize + ".png")));
            } catch (NullPointerException e2) {
                logger.warning("Failed to load menu images: " + imageName + "_a" + imageSize + ".png");
            }
        } else {
            jmi.setIcon(new ImageIcon(Images.get("menu_spacer.gif")));
            jmi.setDisabledIcon(new ImageIcon(Images.get("menu_spacer.gif")));
            jmi.setRolloverIcon(new ImageIcon(Images.get("menu_spacer.gif")));
        }
        jmi.setBorder(BorderFactory.createEmptyBorder());
        jmi.setContentAreaFilled(false);
        jmi.setFocusPainted(true);
        if (accel != null) {
            jmi.setAccelerator(accel);
        }
        return jmi;
    }

    protected void commonToolBarSetup(JToolBar toolbar, boolean isMainToolBar) {
        if (!isMainToolBar) {
            toolbar.requestFocus();
            toolbar.setRollover(true);
        }
        if (this.toolbarStyle == 2) {
            toolbar.setFloatable(false);
            if (!isMainToolBar) {
                if (this.haveMadeAToolBar) {
                    toolbar.addSeparator();
                }
                this.haveMadeAToolBar = true;
            }
        }
    }

    protected void doubleCheckPropertiesManager() {
        if (this.propertiesManager == null && this.viewerController != null && this.viewerController.getWindowManagementCallback() != null) {
            this.propertiesManager = this.viewerController.getWindowManagementCallback().getProperties();
        }
    }

    protected void overrideHighlightColor() {
        String newColor;
        if (Defs.sysProperty(PropertiesManager.SYSPROPERTY_HIGHLIGHT_COLOR) == null) {
            doubleCheckPropertiesManager();
            if (this.propertiesManager != null && (newColor = this.propertiesManager.getString(PropertiesManager.SYSPROPERTY_HIGHLIGHT_COLOR, null)) != null) {
                Defs.setSystemProperty(PropertiesManager.SYSPROPERTY_HIGHLIGHT_COLOR, newColor);
            }
        }
    }

    protected Font buildButtonFont() {
        return new Font(RTFGenerator.defaultFontFamily, 0, 9);
    }

    protected void addToToolBar(JToolBar toolbar, JComponent comp) {
        if (comp != null) {
            toolbar.add(comp);
        }
    }

    protected void addToMenu(JMenu menu, JMenuItem mi) {
        if (mi != null) {
            menu.add(mi);
        }
    }

    protected void addToMenuBar(JMenuBar menuBar, JMenu menu) {
        if (menu != null) {
            menuBar.add(menu);
        }
    }
}
