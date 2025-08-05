package org.icepdf.ri.common;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.dnd.InvalidDnDOperationException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Media;
import javax.print.attribute.standard.MediaSize;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.PrintQuality;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.ProgressMonitor;
import javax.swing.ProgressMonitorInputStream;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import org.icepdf.core.exceptions.PDFException;
import org.icepdf.core.exceptions.PDFSecurityException;
import org.icepdf.core.io.SizeInputStream;
import org.icepdf.core.pobjects.Catalog;
import org.icepdf.core.pobjects.Destination;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.pobjects.Name;
import org.icepdf.core.pobjects.OptionalContent;
import org.icepdf.core.pobjects.OutlineItem;
import org.icepdf.core.pobjects.Outlines;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.pobjects.PageTree;
import org.icepdf.core.pobjects.ViewerPreferences;
import org.icepdf.core.pobjects.actions.Action;
import org.icepdf.core.pobjects.actions.GoToAction;
import org.icepdf.core.pobjects.actions.URIAction;
import org.icepdf.core.pobjects.fonts.FontFactory;
import org.icepdf.core.pobjects.security.Permissions;
import org.icepdf.core.pobjects.security.SecurityManager;
import org.icepdf.core.search.DocumentSearchController;
import org.icepdf.core.util.Library;
import org.icepdf.core.util.PropertyConstants;
import org.icepdf.ri.common.search.DocumentSearchControllerImpl;
import org.icepdf.ri.common.utility.annotation.AnnotationPanel;
import org.icepdf.ri.common.utility.layers.LayersPanel;
import org.icepdf.ri.common.utility.outline.OutlineItemTreeNode;
import org.icepdf.ri.common.utility.search.SearchPanel;
import org.icepdf.ri.common.utility.thumbs.ThumbnailsPanel;
import org.icepdf.ri.common.views.AnnotationComponent;
import org.icepdf.ri.common.views.Controller;
import org.icepdf.ri.common.views.DocumentView;
import org.icepdf.ri.common.views.DocumentViewComponent;
import org.icepdf.ri.common.views.DocumentViewController;
import org.icepdf.ri.common.views.DocumentViewControllerImpl;
import org.icepdf.ri.common.views.DocumentViewModelImpl;
import org.icepdf.ri.common.views.annotations.AnnotationState;
import org.icepdf.ri.util.BareBonesBrowserLaunch;
import org.icepdf.ri.util.PropertiesManager;
import org.icepdf.ri.util.Resources;
import org.icepdf.ri.util.SVG;
import org.icepdf.ri.util.TextExtractionTask;
import org.icepdf.ri.util.URLAccess;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/SwingController.class */
public class SwingController implements Controller, ActionListener, FocusListener, ItemListener, TreeSelectionListener, WindowListener, DropTargetListener, KeyListener, PropertyChangeListener {
    public static final int CURSOR_OPEN_HAND = 1;
    public static final int CURSOR_CLOSE_HAND = 2;
    public static final int CURSOR_ZOOM_IN = 3;
    public static final int CURSOR_ZOOM_OUT = 4;
    public static final int CURSOR_WAIT = 6;
    public static final int CURSOR_SELECT = 7;
    public static final int CURSOR_DEFAULT = 8;
    private static final int MAX_SELECT_ALL_PAGE_COUNT = 250;
    private JMenuItem openFileMenuItem;
    private JMenuItem openURLMenuItem;
    private JMenuItem closeMenuItem;
    private JMenuItem saveAsFileMenuItem;
    private JMenuItem exportTextMenuItem;
    private JMenuItem exportSVGMenuItem;
    private JMenuItem permissionsMenuItem;
    private JMenuItem informationMenuItem;
    private JMenuItem printSetupMenuItem;
    private JMenuItem printMenuItem;
    private JMenuItem exitMenuItem;
    private JMenuItem undoMenuItem;
    private JMenuItem redoMenuItem;
    private JMenuItem copyMenuItem;
    private JMenuItem deleteMenuItem;
    private JMenuItem selectAllMenuItem;
    private JMenuItem deselectAllMenuItem;
    private JMenuItem fitActualSizeMenuItem;
    private JMenuItem fitPageMenuItem;
    private JMenuItem fitWidthMenuItem;
    private JMenuItem zoomInMenuItem;
    private JMenuItem zoomOutMenuItem;
    private JMenuItem rotateLeftMenuItem;
    private JMenuItem rotateRightMenuItem;
    private JMenuItem showHideToolBarMenuItem;
    private JMenuItem showHideUtilityPaneMenuItem;
    private JMenuItem firstPageMenuItem;
    private JMenuItem previousPageMenuItem;
    private JMenuItem nextPageMenuItem;
    private JMenuItem lastPageMenuItem;
    private JMenuItem searchMenuItem;
    private JMenuItem goToPageMenuItem;
    private JMenuItem minimiseAllMenuItem;
    private JMenuItem bringAllToFrontMenuItem;
    private List windowListMenuItems;
    private JMenuItem aboutMenuItem;
    private JButton openFileButton;
    private JButton saveAsFileButton;
    private JButton printButton;
    private JButton searchButton;
    private JToggleButton showHideUtilityPaneButton;
    private JButton firstPageButton;
    private JButton previousPageButton;
    private JButton nextPageButton;
    private JButton lastPageButton;
    private JTextField currentPageNumberTextField;
    private JLabel numberOfPagesLabel;
    private JButton zoomInButton;
    private JButton zoomOutButton;
    private JComboBox zoomComboBox;
    private JToggleButton fitActualSizeButton;
    private JToggleButton fitHeightButton;
    private JToggleButton fitWidthButton;
    private JToggleButton fontEngineButton;
    private JToggleButton facingPageViewContinuousButton;
    private JToggleButton singlePageViewContinuousButton;
    private JToggleButton facingPageViewNonContinuousButton;
    private JToggleButton singlePageViewNonContinuousButton;
    private JButton rotateLeftButton;
    private JButton rotateRightButton;
    private JToggleButton panToolButton;
    private JToggleButton textSelectToolButton;
    private JToggleButton zoomInToolButton;
    private JToggleButton zoomDynamicToolButton;
    private JToggleButton selectToolButton;
    private JToggleButton highlightAnnotationToolButton;
    private JToggleButton textAnnotationToolButton;
    private JToggleButton linkAnnotationToolButton;
    private JToggleButton highlightAnnotationUtilityToolButton;
    private JToggleButton strikeOutAnnotationToolButton;
    private JToggleButton underlineAnnotationToolButton;
    private JToggleButton lineAnnotationToolButton;
    private JToggleButton lineArrowAnnotationToolButton;
    private JToggleButton squareAnnotationToolButton;
    private JToggleButton circleAnnotationToolButton;
    private JToggleButton inkAnnotationToolButton;
    private JToggleButton freeTextAnnotationToolButton;
    private JToggleButton textAnnotationUtilityToolButton;
    private JToolBar completeToolBar;
    private ProgressMonitor printProgressMonitor;
    private Timer printActivityMonitor;
    private JTree outlinesTree;
    private JScrollPane outlinesScrollPane;
    private SearchPanel searchPanel;
    private ThumbnailsPanel thumbnailsPanel;
    private LayersPanel layersPanel;
    private AnnotationPanel annotationPanel;
    private JTabbedPane utilityTabbedPane;
    private JSplitPane utilityAndDocumentSplitPane;
    private int utilityAndDocumentSplitPaneLastDividerLocation;
    private JLabel statusLabel;
    private JFrame viewer;
    private WindowManagementCallback windowManagementCallback;
    private ViewModel viewModel;
    private DocumentViewControllerImpl documentViewController;
    private DocumentSearchController documentSearchController;
    private Document document;
    private boolean disposed;
    private PropertiesManager propertiesManager;
    private boolean reflectingZoomInZoomComboBox;
    private static final Logger logger = Logger.getLogger(SwingController.class.toString());
    private static ResourceBundle messageBundle = null;

    public SwingController() {
        this(null);
    }

    public SwingController(ResourceBundle messageBundle2) {
        this.reflectingZoomInZoomComboBox = false;
        this.viewModel = new ViewModel();
        this.documentViewController = new DocumentViewControllerImpl(this);
        this.documentSearchController = new DocumentSearchControllerImpl(this);
        this.documentViewController.addPropertyChangeListener(this);
        if (messageBundle2 != null) {
            messageBundle = messageBundle2;
        } else {
            messageBundle = ResourceBundle.getBundle(PropertiesManager.DEFAULT_MESSAGE_BUNDLE);
        }
    }

    @Override // org.icepdf.ri.common.views.Controller
    public DocumentViewController getDocumentViewController() {
        return this.documentViewController;
    }

    @Override // org.icepdf.ri.common.views.Controller
    public DocumentSearchController getDocumentSearchController() {
        return this.documentSearchController;
    }

    @Override // org.icepdf.ri.common.views.Controller
    public ResourceBundle getMessageBundle() {
        return messageBundle;
    }

    public void setWindowManagementCallback(WindowManagementCallback wm) {
        this.windowManagementCallback = wm;
    }

    public WindowManagementCallback getWindowManagementCallback() {
        return this.windowManagementCallback;
    }

    public void setPropertiesManager(PropertiesManager propertiesManager) {
        this.propertiesManager = propertiesManager;
    }

    public void setOpenFileMenuItem(JMenuItem mi) {
        this.openFileMenuItem = mi;
        mi.addActionListener(this);
    }

    public void setOpenURLMenuItem(JMenuItem mi) {
        this.openURLMenuItem = mi;
        mi.addActionListener(this);
    }

    public void setCloseMenuItem(JMenuItem mi) {
        this.closeMenuItem = mi;
        mi.addActionListener(this);
    }

    public void setSaveAsFileMenuItem(JMenuItem mi) {
        this.saveAsFileMenuItem = mi;
        mi.addActionListener(this);
    }

    public void setExportTextMenuItem(JMenuItem mi) {
        this.exportTextMenuItem = mi;
        mi.addActionListener(this);
    }

    public void setExportSVGMenuItem(JMenuItem mi) {
        this.exportSVGMenuItem = mi;
        mi.addActionListener(this);
    }

    public void setPermissionsMenuItem(JMenuItem mi) {
        this.permissionsMenuItem = mi;
        mi.addActionListener(this);
    }

    public void setInformationMenuItem(JMenuItem mi) {
        this.informationMenuItem = mi;
        mi.addActionListener(this);
    }

    public void setPrintSetupMenuItem(JMenuItem mi) {
        this.printSetupMenuItem = mi;
        mi.addActionListener(this);
    }

    public void setPrintMenuItem(JMenuItem mi) {
        this.printMenuItem = mi;
        mi.addActionListener(this);
    }

    public void setExitMenuItem(JMenuItem mi) {
        this.exitMenuItem = mi;
        mi.addActionListener(this);
    }

    public void setUndoMenuItem(JMenuItem mi) {
        this.undoMenuItem = mi;
        mi.addActionListener(this);
    }

    public void setReduMenuItem(JMenuItem mi) {
        this.redoMenuItem = mi;
        mi.addActionListener(this);
    }

    public void setCopyMenuItem(JMenuItem mi) {
        this.copyMenuItem = mi;
        mi.addActionListener(this);
    }

    public void setDeleteMenuItem(JMenuItem mi) {
        this.deleteMenuItem = mi;
        mi.addActionListener(this);
    }

    public void setSelectAllMenuItem(JMenuItem mi) {
        this.selectAllMenuItem = mi;
        mi.addActionListener(this);
    }

    public void setDselectAllMenuItem(JMenuItem mi) {
        this.deselectAllMenuItem = mi;
        mi.addActionListener(this);
    }

    public void setFitActualSizeMenuItem(JMenuItem mi) {
        this.fitActualSizeMenuItem = mi;
        mi.addActionListener(this);
    }

    public void setFitPageMenuItem(JMenuItem mi) {
        this.fitPageMenuItem = mi;
        mi.addActionListener(this);
    }

    public void setFitWidthMenuItem(JMenuItem mi) {
        this.fitWidthMenuItem = mi;
        mi.addActionListener(this);
    }

    public void setZoomInMenuItem(JMenuItem mi) {
        this.zoomInMenuItem = mi;
        mi.addActionListener(this);
    }

    public void setZoomOutMenuItem(JMenuItem mi) {
        this.zoomOutMenuItem = mi;
        mi.addActionListener(this);
    }

    public void setRotateLeftMenuItem(JMenuItem mi) {
        this.rotateLeftMenuItem = mi;
        mi.addActionListener(this);
    }

    public void setRotateRightMenuItem(JMenuItem mi) {
        this.rotateRightMenuItem = mi;
        mi.addActionListener(this);
    }

    public void setShowHideToolBarMenuItem(JMenuItem mi) {
        this.showHideToolBarMenuItem = mi;
        mi.addActionListener(this);
    }

    public void setShowHideUtilityPaneMenuItem(JMenuItem mi) {
        this.showHideUtilityPaneMenuItem = mi;
        mi.addActionListener(this);
    }

    public void setFirstPageMenuItem(JMenuItem mi) {
        this.firstPageMenuItem = mi;
        mi.addActionListener(this);
    }

    public void setPreviousPageMenuItem(JMenuItem mi) {
        this.previousPageMenuItem = mi;
        mi.addActionListener(this);
    }

    public void setNextPageMenuItem(JMenuItem mi) {
        this.nextPageMenuItem = mi;
        mi.addActionListener(this);
    }

    public void setLastPageMenuItem(JMenuItem mi) {
        this.lastPageMenuItem = mi;
        mi.addActionListener(this);
    }

    public void setSearchMenuItem(JMenuItem mi) {
        this.searchMenuItem = mi;
        mi.addActionListener(this);
    }

    public void setGoToPageMenuItem(JMenuItem mi) {
        this.goToPageMenuItem = mi;
        mi.addActionListener(this);
    }

    public void setMinimiseAllMenuItem(JMenuItem mi) {
        this.minimiseAllMenuItem = mi;
        mi.addActionListener(this);
    }

    public void setBringAllToFrontMenuItem(JMenuItem mi) {
        this.bringAllToFrontMenuItem = mi;
        mi.addActionListener(this);
    }

    public void setWindowListMenuItems(List menuItems) {
        this.windowListMenuItems = menuItems;
        int count = this.windowListMenuItems != null ? this.windowListMenuItems.size() : 0;
        for (int i2 = 0; i2 < count; i2++) {
            JMenuItem mi = (JMenuItem) this.windowListMenuItems.get(i2);
            mi.addActionListener(this);
        }
    }

    public void setAboutMenuItem(JMenuItem mi) {
        this.aboutMenuItem = mi;
        mi.addActionListener(this);
    }

    public void setOpenFileButton(JButton btn) {
        this.openFileButton = btn;
        btn.addActionListener(this);
    }

    public void setSaveAsFileButton(JButton btn) {
        this.saveAsFileButton = btn;
        btn.addActionListener(this);
    }

    public void setPrintButton(JButton btn) {
        this.printButton = btn;
        btn.addActionListener(this);
    }

    public void setSearchButton(JButton btn) {
        this.searchButton = btn;
        btn.addActionListener(this);
    }

    public void setShowHideUtilityPaneButton(JToggleButton btn) {
        this.showHideUtilityPaneButton = btn;
        btn.addActionListener(this);
    }

    public void setFirstPageButton(JButton btn) {
        this.firstPageButton = btn;
        btn.addActionListener(this);
    }

    public void setPreviousPageButton(JButton btn) {
        this.previousPageButton = btn;
        btn.addActionListener(this);
    }

    public void setNextPageButton(JButton btn) {
        this.nextPageButton = btn;
        btn.addActionListener(this);
    }

    public void setLastPageButton(JButton btn) {
        this.lastPageButton = btn;
        btn.addActionListener(this);
    }

    public void setCurrentPageNumberTextField(JTextField textField) {
        this.currentPageNumberTextField = textField;
        this.currentPageNumberTextField.addActionListener(this);
        this.currentPageNumberTextField.addFocusListener(this);
        this.currentPageNumberTextField.addKeyListener(this);
    }

    public void setNumberOfPagesLabel(JLabel lbl) {
        this.numberOfPagesLabel = lbl;
    }

    public void setZoomOutButton(JButton btn) {
        this.zoomOutButton = btn;
        btn.addActionListener(this);
    }

    public void setZoomComboBox(JComboBox zcb, float[] zl) {
        this.zoomComboBox = zcb;
        this.documentViewController.setZoomLevels(zl);
        this.zoomComboBox.setSelectedItem(NumberFormat.getPercentInstance().format(1.0d));
        this.zoomComboBox.addItemListener(this);
    }

    public void setZoomInButton(JButton btn) {
        this.zoomInButton = btn;
        btn.addActionListener(this);
    }

    public void setFitActualSizeButton(JToggleButton btn) {
        this.fitActualSizeButton = btn;
        btn.addItemListener(this);
    }

    public void setFitHeightButton(JToggleButton btn) {
        this.fitHeightButton = btn;
        btn.addItemListener(this);
    }

    public void setFontEngineButton(JToggleButton btn) {
        this.fontEngineButton = btn;
        btn.addItemListener(this);
    }

    public void setFitWidthButton(JToggleButton btn) {
        this.fitWidthButton = btn;
        btn.addItemListener(this);
    }

    public void setRotateLeftButton(JButton btn) {
        this.rotateLeftButton = btn;
        btn.addActionListener(this);
    }

    public void setRotateRightButton(JButton btn) {
        this.rotateRightButton = btn;
        btn.addActionListener(this);
    }

    public void setPanToolButton(JToggleButton btn) {
        this.panToolButton = btn;
        btn.addItemListener(this);
    }

    public void setZoomInToolButton(JToggleButton btn) {
        this.zoomInToolButton = btn;
        btn.addItemListener(this);
    }

    public void setTextSelectToolButton(JToggleButton btn) {
        this.textSelectToolButton = btn;
        btn.addItemListener(this);
    }

    public void setSelectToolButton(JToggleButton btn) {
        this.selectToolButton = btn;
        btn.addItemListener(this);
    }

    public void setLinkAnnotationToolButton(JToggleButton btn) {
        this.linkAnnotationToolButton = btn;
        btn.addItemListener(this);
    }

    public void setHighlightAnnotationToolButton(JToggleButton btn) {
        this.highlightAnnotationToolButton = btn;
        btn.addItemListener(this);
    }

    public void setHighlightAnnotationUtilityToolButton(JToggleButton btn) {
        this.highlightAnnotationUtilityToolButton = btn;
        btn.addItemListener(this);
    }

    public void setStrikeOutAnnotationToolButton(JToggleButton btn) {
        this.strikeOutAnnotationToolButton = btn;
        btn.addItemListener(this);
    }

    public void setUnderlineAnnotationToolButton(JToggleButton btn) {
        this.underlineAnnotationToolButton = btn;
        btn.addItemListener(this);
    }

    public void setLineAnnotationToolButton(JToggleButton btn) {
        this.lineAnnotationToolButton = btn;
        btn.addItemListener(this);
    }

    public void setLineArrowAnnotationToolButton(JToggleButton btn) {
        this.lineArrowAnnotationToolButton = btn;
        btn.addItemListener(this);
    }

    public void setSquareAnnotationToolButton(JToggleButton btn) {
        this.squareAnnotationToolButton = btn;
        btn.addItemListener(this);
    }

    public void setCircleAnnotationToolButton(JToggleButton btn) {
        this.circleAnnotationToolButton = btn;
        btn.addItemListener(this);
    }

    public void setInkAnnotationToolButton(JToggleButton btn) {
        this.inkAnnotationToolButton = btn;
        btn.addItemListener(this);
    }

    public void setFreeTextAnnotationToolButton(JToggleButton btn) {
        this.freeTextAnnotationToolButton = btn;
        btn.addItemListener(this);
    }

    public void setTextAnnotationToolButton(JToggleButton btn) {
        this.textAnnotationToolButton = btn;
        btn.addItemListener(this);
    }

    public void setTextAnnotationUtilityToolButton(JToggleButton btn) {
        this.textAnnotationUtilityToolButton = btn;
        btn.addItemListener(this);
    }

    public void setZoomDynamicToolButton(JToggleButton btn) {
        this.zoomDynamicToolButton = btn;
        btn.addItemListener(this);
    }

    public void setCompleteToolBar(JToolBar toolbar) {
        this.completeToolBar = toolbar;
    }

    public void setOutlineComponents(JTree tree, JScrollPane scroll) {
        this.outlinesTree = tree;
        this.outlinesScrollPane = scroll;
        this.outlinesTree.addTreeSelectionListener(this);
    }

    public void setSearchPanel(SearchPanel sp) {
        this.searchPanel = sp;
    }

    public void setThumbnailsPanel(ThumbnailsPanel tn) {
        this.thumbnailsPanel = tn;
    }

    public void setLayersPanel(LayersPanel tn) {
        this.layersPanel = tn;
    }

    public void setAnnotationPanel(AnnotationPanel lp) {
        this.annotationPanel = lp;
    }

    public void setUtilityTabbedPane(JTabbedPane util) {
        this.utilityTabbedPane = util;
    }

    public void setIsEmbeddedComponent(boolean embeddableComponent) {
        if (embeddableComponent) {
            this.documentViewController.setViewKeyListener(this);
            this.documentViewController.getViewContainer().addKeyListener(this);
        }
    }

    public void setUtilityAndDocumentSplitPane(JSplitPane splitPane) throws IllegalArgumentException {
        this.utilityAndDocumentSplitPane = splitPane;
        setUtilityPaneVisible(false);
        this.utilityAndDocumentSplitPane.addPropertyChangeListener(this);
    }

    public void setStatusLabel(JLabel lbl) {
        this.statusLabel = lbl;
    }

    public void setViewerFrame(JFrame v2) throws IllegalArgumentException {
        this.viewer = v2;
        this.viewer.addWindowListener(this);
        new DropTarget(this.viewer, 3, this);
        reflectStateInComponents();
    }

    public JFrame getViewerFrame() {
        return this.viewer;
    }

    public boolean isPdfCollection() {
        Catalog catalog = this.document.getCatalog();
        if (catalog.getNames() != null && catalog.getNames().getEmbeddedFilesNameTree() != null && catalog.getNames().getEmbeddedFilesNameTree().getRoot().getNamesAndValues() != null) {
            if (catalog.getObject(Catalog.PAGEMODE_KEY) == null || ((Name) catalog.getObject(Catalog.PAGEMODE_KEY)).getName().equalsIgnoreCase("UseAttachments")) {
                return true;
            }
            return false;
        }
        return false;
    }

    private void reflectStateInComponents() throws IllegalArgumentException {
        boolean opened = this.document != null;
        boolean pdfCollection = opened && isPdfCollection();
        int nPages = getPageTree() != null ? getPageTree().getNumberOfPages() : 0;
        boolean canPrint = havePermissionToPrint();
        boolean canExtract = havePermissionToExtractContent();
        boolean canModify = havePermissionToModifyDocument();
        reflectPageChangeInComponents();
        setEnabled(this.closeMenuItem, opened);
        setEnabled(this.saveAsFileMenuItem, opened);
        setEnabled(this.exportTextMenuItem, opened && canExtract && !pdfCollection);
        setEnabled(this.exportSVGMenuItem, opened && canPrint && !pdfCollection);
        setEnabled(this.permissionsMenuItem, opened);
        setEnabled(this.informationMenuItem, opened);
        setEnabled(this.printSetupMenuItem, opened && canPrint && !pdfCollection);
        setEnabled(this.printMenuItem, opened && canPrint && !pdfCollection);
        setEnabled(this.undoMenuItem, false);
        setEnabled(this.redoMenuItem, false);
        setEnabled(this.copyMenuItem, false);
        setEnabled(this.deleteMenuItem, false);
        setEnabled(this.selectAllMenuItem, opened && canExtract && !pdfCollection);
        setEnabled(this.deselectAllMenuItem, false);
        setEnabled(this.fitActualSizeMenuItem, opened && !pdfCollection);
        setEnabled(this.fitPageMenuItem, opened && !pdfCollection);
        setEnabled(this.fitWidthMenuItem, opened && !pdfCollection);
        setEnabled(this.zoomInMenuItem, opened && !pdfCollection);
        setEnabled(this.zoomOutMenuItem, opened && !pdfCollection);
        setEnabled(this.rotateLeftMenuItem, opened && !pdfCollection);
        setEnabled(this.rotateRightMenuItem, opened && !pdfCollection);
        setEnabled(this.fitPageMenuItem, opened && !pdfCollection);
        setEnabled(this.fitWidthMenuItem, opened && !pdfCollection);
        if (this.showHideToolBarMenuItem != null) {
            boolean vis = this.completeToolBar != null && this.completeToolBar.isVisible();
            this.showHideToolBarMenuItem.setText(vis ? messageBundle.getString("viewer.toolbar.hideToolBar.label") : messageBundle.getString("viewer.toolbar.showToolBar.label"));
        }
        setEnabled(this.showHideToolBarMenuItem, this.completeToolBar != null);
        if (this.showHideUtilityPaneMenuItem != null) {
            boolean vis2 = isUtilityPaneVisible();
            this.showHideUtilityPaneMenuItem.setText((opened && vis2) ? messageBundle.getString("viewer.toolbar.hideUtilityPane.label") : messageBundle.getString("viewer.toolbar.showUtilityPane.label"));
        }
        setEnabled(this.showHideUtilityPaneMenuItem, (!opened || this.utilityTabbedPane == null || pdfCollection) ? false : true);
        setEnabled(this.searchMenuItem, (!opened || this.searchPanel == null || pdfCollection) ? false : true);
        setEnabled(this.goToPageMenuItem, opened && nPages > 1 && !pdfCollection);
        setEnabled(this.saveAsFileButton, opened);
        setEnabled(this.printButton, opened && canPrint && !pdfCollection);
        setEnabled(this.searchButton, (!opened || this.searchPanel == null || pdfCollection) ? false : true);
        setEnabled(this.showHideUtilityPaneButton, (!opened || this.utilityTabbedPane == null || pdfCollection) ? false : true);
        setEnabled(this.currentPageNumberTextField, opened && nPages > 1 && !pdfCollection);
        if (this.numberOfPagesLabel != null) {
            Object[] messageArguments = {String.valueOf(nPages)};
            MessageFormat formatter = new MessageFormat(messageBundle.getString("viewer.toolbar.pageIndicator"));
            String numberOfPages = formatter.format(messageArguments);
            this.numberOfPagesLabel.setText(opened ? numberOfPages : "");
        }
        setEnabled(this.zoomInButton, opened && !pdfCollection);
        setEnabled(this.zoomOutButton, opened && !pdfCollection);
        setEnabled(this.zoomComboBox, opened && !pdfCollection);
        setEnabled(this.fitActualSizeButton, opened && !pdfCollection);
        setEnabled(this.fitHeightButton, opened && !pdfCollection);
        setEnabled(this.fitWidthButton, opened && !pdfCollection);
        setEnabled(this.rotateLeftButton, opened && !pdfCollection);
        setEnabled(this.rotateRightButton, opened && !pdfCollection);
        setEnabled(this.panToolButton, opened && !pdfCollection);
        setEnabled(this.zoomInToolButton, opened && !pdfCollection);
        setEnabled(this.zoomDynamicToolButton, opened && !pdfCollection);
        setEnabled(this.textSelectToolButton, opened && canExtract && !pdfCollection);
        setEnabled(this.selectToolButton, opened && canModify && !pdfCollection);
        setEnabled(this.linkAnnotationToolButton, opened && canModify && !pdfCollection);
        setEnabled(this.highlightAnnotationToolButton, opened && canModify && !pdfCollection);
        setEnabled(this.highlightAnnotationUtilityToolButton, opened && canModify && !pdfCollection);
        setEnabled(this.strikeOutAnnotationToolButton, opened && canModify && !pdfCollection);
        setEnabled(this.underlineAnnotationToolButton, opened && canModify && !pdfCollection);
        setEnabled(this.lineAnnotationToolButton, opened && canModify && !pdfCollection);
        setEnabled(this.lineArrowAnnotationToolButton, opened && canModify && !pdfCollection);
        setEnabled(this.squareAnnotationToolButton, opened && canModify && !pdfCollection);
        setEnabled(this.circleAnnotationToolButton, opened && canModify && !pdfCollection);
        setEnabled(this.inkAnnotationToolButton, opened && canModify && !pdfCollection);
        setEnabled(this.freeTextAnnotationToolButton, opened && canModify && !pdfCollection);
        setEnabled(this.textAnnotationToolButton, opened && canModify && !pdfCollection);
        setEnabled(this.textAnnotationUtilityToolButton, opened && canModify && !pdfCollection);
        setEnabled(this.fontEngineButton, opened && !pdfCollection);
        setEnabled(this.facingPageViewContinuousButton, opened && !pdfCollection);
        setEnabled(this.singlePageViewContinuousButton, opened && !pdfCollection);
        setEnabled(this.facingPageViewNonContinuousButton, opened && !pdfCollection);
        setEnabled(this.singlePageViewNonContinuousButton, opened && !pdfCollection);
        if (opened) {
            reflectZoomInZoomComboBox();
            reflectFitInFitButtons();
            reflectDocumentViewModeInButtons();
            reflectToolInToolButtons();
        }
    }

    private void reflectPageChangeInComponents() {
        boolean opened = this.document != null;
        int nPages = getPageTree() != null ? getPageTree().getNumberOfPages() : 0;
        int currentPage = isCurrentPage() ? this.documentViewController.getCurrentPageDisplayValue() : 0;
        setEnabled(this.firstPageMenuItem, opened && currentPage != 1);
        setEnabled(this.previousPageMenuItem, opened && currentPage != 1);
        setEnabled(this.nextPageMenuItem, opened && currentPage != nPages);
        setEnabled(this.lastPageMenuItem, opened && currentPage != nPages);
        setEnabled(this.firstPageButton, opened && currentPage != 1);
        setEnabled(this.previousPageButton, opened && currentPage != 1);
        setEnabled(this.nextPageButton, opened && currentPage != nPages);
        setEnabled(this.lastPageButton, opened && currentPage != nPages);
        if (this.currentPageNumberTextField != null) {
            this.currentPageNumberTextField.setText(opened ? Integer.toString(currentPage) : "");
        }
    }

    public boolean havePermissionToPrint() {
        Permissions permissions;
        if (this.document == null) {
            return false;
        }
        SecurityManager securityManager = this.document.getSecurityManager();
        return securityManager == null || (permissions = securityManager.getPermissions()) == null || permissions.getPermissions(0);
    }

    public boolean havePermissionToExtractContent() {
        Permissions permissions;
        if (this.document == null) {
            return false;
        }
        SecurityManager securityManager = this.document.getSecurityManager();
        return securityManager == null || (permissions = securityManager.getPermissions()) == null || permissions.getPermissions(3);
    }

    public boolean havePermissionToModifyDocument() {
        Permissions permissions;
        if (this.document == null) {
            return false;
        }
        SecurityManager securityManager = this.document.getSecurityManager();
        return securityManager == null || (permissions = securityManager.getPermissions()) == null || permissions.getPermissions(2);
    }

    private void setEnabled(JComponent comp, boolean ena) {
        if (comp != null) {
            comp.setEnabled(ena);
        }
    }

    private void setZoomFromZoomComboBox() {
        if (this.reflectingZoomInZoomComboBox) {
            return;
        }
        int selIndex = this.zoomComboBox.getSelectedIndex();
        float[] zoomLevels = this.documentViewController.getZoomLevels();
        if (selIndex >= 0 && selIndex < zoomLevels.length) {
            float zoom = 1.0f;
            try {
                try {
                    zoom = zoomLevels[selIndex];
                    if (zoom != this.documentViewController.getZoom()) {
                        setZoom(zoom);
                        return;
                    }
                    return;
                } catch (Throwable th) {
                    if (zoom != this.documentViewController.getZoom()) {
                        setZoom(zoom);
                    }
                    throw th;
                }
            } catch (IndexOutOfBoundsException e2) {
                logger.log(Level.FINE, "Error apply zoom levels");
                if (zoom != this.documentViewController.getZoom()) {
                    setZoom(zoom);
                    return;
                }
                return;
            }
        }
        boolean success = false;
        try {
            Object selItem = this.zoomComboBox.getSelectedItem();
            if (selItem != null) {
                String str = selItem.toString();
                float zoom2 = Float.parseFloat(str.replace('%', ' ').trim()) / 100.0f;
                if (zoom2 != this.documentViewController.getZoom()) {
                    setZoom(zoom2);
                }
                success = true;
            }
        } catch (Exception e3) {
            success = false;
        }
        if (!success) {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    public void reflectUndoCommands() {
        UndoCaretaker undoCaretaker = ((DocumentViewModelImpl) this.documentViewController.getDocumentViewModel()).getAnnotationCareTaker();
        setEnabled(this.undoMenuItem, undoCaretaker.isUndo());
        setEnabled(this.redoMenuItem, undoCaretaker.isRedo());
    }

    private void reflectZoomInZoomComboBox() {
        if (this.reflectingZoomInZoomComboBox || this.document == null) {
            return;
        }
        int index = -1;
        float zoom = this.documentViewController.getZoom();
        float belowZoom = zoom * 0.99f;
        float aboveZoom = zoom * 1.01f;
        float[] zoomLevels = this.documentViewController.getZoomLevels();
        if (zoomLevels != null) {
            int i2 = 0;
            while (true) {
                if (i2 < zoomLevels.length) {
                    float curr = zoomLevels[i2];
                    if (curr < belowZoom || curr > aboveZoom) {
                        i2++;
                    } else {
                        index = i2;
                        break;
                    }
                }
            }
        }
        try {
            this.reflectingZoomInZoomComboBox = true;
            if (this.zoomComboBox != null) {
                if (index > -1) {
                    this.zoomComboBox.setSelectedIndex(index);
                } else {
                    this.zoomComboBox.setSelectedItem(NumberFormat.getPercentInstance().format(zoom));
                }
            }
        } finally {
            this.reflectingZoomInZoomComboBox = false;
        }
    }

    public int getDocumentViewToolMode() {
        return this.documentViewController.getToolMode();
    }

    public void setDisplayTool(int argToolName) {
        boolean actualToolMayHaveChanged = false;
        try {
            if (argToolName == 1) {
                actualToolMayHaveChanged = this.documentViewController.setToolMode(1);
                this.documentViewController.setViewCursor(1);
                setCursorOnComponents(8);
            } else if (argToolName == 5) {
                actualToolMayHaveChanged = this.documentViewController.setToolMode(5);
                this.documentViewController.setViewCursor(7);
                setCursorOnComponents(8);
            } else if (argToolName == 6) {
                actualToolMayHaveChanged = this.documentViewController.setToolMode(6);
                this.documentViewController.setViewCursor(7);
                setCursorOnComponents(8);
            } else if (argToolName == 7) {
                actualToolMayHaveChanged = this.documentViewController.setToolMode(7);
                this.documentViewController.setViewCursor(11);
                setCursorOnComponents(8);
            } else if (argToolName == 8) {
                actualToolMayHaveChanged = this.documentViewController.setToolMode(8);
                this.documentViewController.setViewCursor(7);
                setCursorOnComponents(8);
            } else if (argToolName == 11) {
                actualToolMayHaveChanged = this.documentViewController.setToolMode(11);
                this.documentViewController.setViewCursor(7);
                setCursorOnComponents(8);
            } else if (argToolName == 9) {
                actualToolMayHaveChanged = this.documentViewController.setToolMode(9);
                this.documentViewController.setViewCursor(7);
                setCursorOnComponents(8);
            } else if (argToolName == 12) {
                actualToolMayHaveChanged = this.documentViewController.setToolMode(12);
                this.documentViewController.setViewCursor(11);
                setCursorOnComponents(8);
            } else if (argToolName == 13) {
                actualToolMayHaveChanged = this.documentViewController.setToolMode(13);
                this.documentViewController.setViewCursor(11);
                setCursorOnComponents(8);
            } else if (argToolName == 14) {
                actualToolMayHaveChanged = this.documentViewController.setToolMode(14);
                this.documentViewController.setViewCursor(11);
                setCursorOnComponents(8);
            } else if (argToolName == 15) {
                actualToolMayHaveChanged = this.documentViewController.setToolMode(15);
                this.documentViewController.setViewCursor(11);
                setCursorOnComponents(8);
            } else if (argToolName == 16) {
                actualToolMayHaveChanged = this.documentViewController.setToolMode(16);
                this.documentViewController.setViewCursor(11);
                setCursorOnComponents(8);
            } else if (argToolName == 17) {
                actualToolMayHaveChanged = this.documentViewController.setToolMode(17);
                this.documentViewController.setViewCursor(11);
                setCursorOnComponents(8);
            } else if (argToolName == 18) {
                actualToolMayHaveChanged = this.documentViewController.setToolMode(18);
                this.documentViewController.setViewCursor(11);
                setCursorOnComponents(8);
            } else if (argToolName == 2) {
                actualToolMayHaveChanged = this.documentViewController.setToolMode(2);
                this.documentViewController.setViewCursor(3);
                setCursorOnComponents(8);
            } else if (argToolName == 4) {
                actualToolMayHaveChanged = this.documentViewController.setToolMode(4);
                this.documentViewController.setViewCursor(12);
                setCursorOnComponents(8);
            } else if (argToolName == 51) {
                setCursorOnComponents(6);
            } else if (argToolName == 50) {
                setCursorOnComponents(8);
            }
            if (actualToolMayHaveChanged) {
                reflectToolInToolButtons();
            }
            if (this.annotationPanel != null) {
                this.annotationPanel.setEnabled(false);
            }
            this.documentViewController.getViewContainer().repaint();
        } catch (HeadlessException e2) {
            e2.printStackTrace();
            logger.log(Level.FINE, "Headless exception during tool selection", (Throwable) e2);
        }
    }

    private void setCursorOnComponents(int cursorType) throws HeadlessException {
        Cursor cursor = this.documentViewController.getViewCursor(cursorType);
        if (this.utilityTabbedPane != null) {
            this.utilityTabbedPane.setCursor(cursor);
        }
        if (this.viewer != null) {
            this.viewer.setCursor(cursor);
        }
    }

    private void reflectToolInToolButtons() {
        reflectSelectionInButton(this.panToolButton, this.documentViewController.isToolModeSelected(1));
        reflectSelectionInButton(this.textSelectToolButton, this.documentViewController.isToolModeSelected(5));
        reflectSelectionInButton(this.selectToolButton, this.documentViewController.isToolModeSelected(6));
        reflectSelectionInButton(this.linkAnnotationToolButton, this.documentViewController.isToolModeSelected(7));
        reflectSelectionInButton(this.highlightAnnotationToolButton, this.documentViewController.isToolModeSelected(8));
        reflectSelectionInButton(this.highlightAnnotationUtilityToolButton, this.documentViewController.isToolModeSelected(8));
        reflectSelectionInButton(this.strikeOutAnnotationToolButton, this.documentViewController.isToolModeSelected(11));
        reflectSelectionInButton(this.underlineAnnotationToolButton, this.documentViewController.isToolModeSelected(9));
        reflectSelectionInButton(this.lineAnnotationToolButton, this.documentViewController.isToolModeSelected(12));
        reflectSelectionInButton(this.lineArrowAnnotationToolButton, this.documentViewController.isToolModeSelected(13));
        reflectSelectionInButton(this.squareAnnotationToolButton, this.documentViewController.isToolModeSelected(14));
        reflectSelectionInButton(this.circleAnnotationToolButton, this.documentViewController.isToolModeSelected(15));
        reflectSelectionInButton(this.inkAnnotationToolButton, this.documentViewController.isToolModeSelected(16));
        reflectSelectionInButton(this.freeTextAnnotationToolButton, this.documentViewController.isToolModeSelected(17));
        reflectSelectionInButton(this.textAnnotationToolButton, this.documentViewController.isToolModeSelected(18));
        reflectSelectionInButton(this.textAnnotationUtilityToolButton, this.documentViewController.isToolModeSelected(18));
        reflectSelectionInButton(this.zoomInToolButton, this.documentViewController.isToolModeSelected(2));
        reflectSelectionInButton(this.zoomDynamicToolButton, this.documentViewController.isToolModeSelected(4));
        reflectSelectionInButton(this.showHideUtilityPaneButton, isUtilityPaneVisible());
    }

    private void reflectFitInFitButtons() {
        if (this.document == null) {
            return;
        }
        reflectSelectionInButton(this.fitWidthButton, isDocumentFitMode(4));
        reflectSelectionInButton(this.fitHeightButton, isDocumentFitMode(3));
        reflectSelectionInButton(this.fitActualSizeButton, isDocumentFitMode(2));
    }

    private void reflectDocumentViewModeInButtons() {
        if (this.document == null || isDocumentViewMode(7)) {
            return;
        }
        reflectSelectionInButton(this.singlePageViewContinuousButton, isDocumentViewMode(2));
        reflectSelectionInButton(this.facingPageViewNonContinuousButton, isDocumentViewMode(5));
        reflectSelectionInButton(this.facingPageViewContinuousButton, isDocumentViewMode(6));
        reflectSelectionInButton(this.singlePageViewNonContinuousButton, isDocumentViewMode(1));
    }

    private void reflectSelectionInButton(AbstractButton btn, boolean selected) {
        if (btn != null) {
            if (btn.isSelected() != selected) {
                btn.setSelected(selected);
            }
            btn.setBorder(selected ? BorderFactory.createLoweredBevelBorder() : BorderFactory.createEmptyBorder());
        }
    }

    public void openFile() throws HeadlessException {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(0);
        fileChooser.addChoosableFileFilter(FileExtensionUtils.getPDFFileFilter());
        if (ViewModel.getDefaultFile() != null) {
            fileChooser.setCurrentDirectory(ViewModel.getDefaultFile());
            fileChooser.setSelectedFile(ViewModel.getDefaultFile());
            fileChooser.ensureFileIsVisible(ViewModel.getDefaultFile());
        }
        fileChooser.setDialogTitle(messageBundle.getString("viewer.dialog.openFile.title"));
        int returnVal = fileChooser.showOpenDialog(this.viewer);
        if (returnVal == 0) {
            File file = fileChooser.getSelectedFile();
            fileChooser.setVisible(false);
            String extension = FileExtensionUtils.getExtension(file);
            if (extension != null) {
                if (extension.equals(FileExtensionUtils.pdf)) {
                    if (this.viewer != null) {
                        this.viewer.toFront();
                        this.viewer.requestFocus();
                        Graphics g2 = this.viewer.getGraphics();
                        if (g2 != null) {
                            this.viewer.paint(g2);
                        }
                    }
                    openFileInSomeViewer(file);
                } else {
                    Resources.showMessageDialog(this.viewer, 1, messageBundle, "viewer.dialog.openFile.error.title", "viewer.dialog.openFile.error.msg", file.getPath());
                }
                ViewModel.setDefaultFile(file);
            }
        }
        fileChooser.setVisible(false);
    }

    private void openFileInSomeViewer(File file) {
        if (this.document == null) {
            openDocument(file.getPath());
            return;
        }
        if (this.windowManagementCallback != null) {
            int oldTool = getDocumentViewToolMode();
            setDisplayTool(51);
            try {
                this.windowManagementCallback.newWindow(file.getPath());
                setDisplayTool(oldTool);
            } catch (Throwable th) {
                setDisplayTool(oldTool);
                throw th;
            }
        }
    }

    public void openFileInSomeViewer(String filename) {
        try {
            File pdfFile = new File(filename);
            openFileInSomeViewer(pdfFile);
        } catch (Exception e2) {
        }
    }

    public void openDocument(String pathname) {
        if (pathname != null) {
            try {
                if (pathname.length() > 0) {
                    try {
                        try {
                            try {
                                if (this.document != null) {
                                    closeDocument();
                                }
                                setDisplayTool(51);
                                this.document = new Document();
                                if (this.documentViewController.getSecurityCallback() == null) {
                                    this.document.setSecurityCallback(new MyGUISecurityCallback(this.viewer, messageBundle));
                                }
                                this.document.setFile(pathname);
                                commonNewDocumentHandling(pathname);
                                setDisplayTool(1);
                            } catch (PDFSecurityException e2) {
                                Resources.showMessageDialog(this.viewer, 1, messageBundle, "viewer.dialog.openDocument.pdfSecurityException.title", "viewer.dialog.openDocument.pdfSecurityException.msg", pathname);
                                this.document = null;
                                logger.log(Level.FINE, "Error opening document.", (Throwable) e2);
                                setDisplayTool(1);
                            }
                        } catch (Exception e3) {
                            Resources.showMessageDialog(this.viewer, 1, messageBundle, "viewer.dialog.openDocument.exception.title", "viewer.dialog.openDocument.exception.msg", pathname);
                            this.document = null;
                            logger.log(Level.FINE, "Error opening document.", (Throwable) e3);
                            setDisplayTool(1);
                        }
                    } catch (PDFException e4) {
                        Resources.showMessageDialog(this.viewer, 1, messageBundle, "viewer.dialog.openDocument.pdfException.title", "viewer.dialog.openDocument.pdfException.msg", pathname);
                        this.document = null;
                        logger.log(Level.FINE, "Error opening document.", (Throwable) e4);
                        setDisplayTool(1);
                    }
                }
            } catch (Throwable th) {
                setDisplayTool(1);
                throw th;
            }
        }
    }

    public void openURL() throws HeadlessException, IllegalArgumentException {
        String urlLocation = ViewModel.getDefaultURL() != null ? ViewModel.getDefaultURL() : "";
        Object o2 = JOptionPane.showInputDialog(this.viewer, "URL:", "Open URL", 3, null, null, urlLocation);
        if (o2 != null) {
            URLAccess urlAccess = URLAccess.doURLAccess(o2.toString());
            urlAccess.closeConnection();
            if (urlAccess.errorMessage != null) {
                Resources.showMessageDialog(this.viewer, 1, messageBundle, "viewer.dialog.openURL.exception.title", "viewer.dialog.openURL.exception.msg", urlAccess.errorMessage, urlAccess.urlLocation);
            } else {
                if (this.viewer != null) {
                    this.viewer.toFront();
                    this.viewer.requestFocus();
                    Graphics g2 = this.viewer.getGraphics();
                    if (g2 != null) {
                        this.viewer.paint(g2);
                    }
                }
                openURLInSomeViewer(urlAccess.url);
            }
            ViewModel.setDefaultURL(urlAccess.urlLocation);
            urlAccess.dispose();
        }
    }

    private void openURLInSomeViewer(URL url) throws HeadlessException, IllegalArgumentException {
        if (this.document == null) {
            openDocument(url);
            return;
        }
        if (this.windowManagementCallback != null) {
            int oldTool = getDocumentViewToolMode();
            setDisplayTool(51);
            try {
                this.windowManagementCallback.newWindow(url);
                setDisplayTool(oldTool);
            } catch (Throwable th) {
                setDisplayTool(oldTool);
                throw th;
            }
        }
    }

    public void openDocument(final URL location) throws HeadlessException, IllegalArgumentException {
        if (location != null) {
            if (this.document != null) {
                closeDocument();
            }
            setDisplayTool(51);
            this.document = new Document();
            if (this.documentViewController.getSecurityCallback() == null) {
                this.document.setSecurityCallback(new MyGUISecurityCallback(this.viewer, messageBundle));
            }
            try {
                final URLConnection urlConnection = location.openConnection();
                final int size = urlConnection.getContentLength();
                SwingWorker worker = new SwingWorker() { // from class: org.icepdf.ri.common.SwingController.1
                    @Override // org.icepdf.ri.common.SwingWorker
                    public Object construct() throws HeadlessException, IllegalArgumentException {
                        InputStream in = null;
                        try {
                            Object[] messageArguments = {location.toString()};
                            MessageFormat formatter = new MessageFormat(SwingController.messageBundle.getString("viewer.dialog.openURL.downloading.msg"));
                            ProgressMonitorInputStream progressMonitorInputStream = new ProgressMonitorInputStream(SwingController.this.viewer, formatter.format(messageArguments), new SizeInputStream(urlConnection.getInputStream(), size));
                            in = new BufferedInputStream(progressMonitorInputStream);
                            String pathOrURL = location.toString();
                            SwingController.this.document.setInputStream(in, pathOrURL);
                            SwingController.this.commonNewDocumentHandling(location.getPath());
                            SwingController.this.setDisplayTool(1);
                            return null;
                        } catch (IOException e2) {
                            if (in != null) {
                                try {
                                    in.close();
                                } catch (IOException e3) {
                                    SwingController.logger.log(Level.FINE, "Error opening document.", (Throwable) e3);
                                }
                            }
                            SwingController.this.closeDocument();
                            SwingController.this.document = null;
                            return null;
                        } catch (PDFException e4) {
                            Resources.showMessageDialog(SwingController.this.viewer, 1, SwingController.messageBundle, "viewer.dialog.openDocument.pdfException.title", "viewer.dialog.openDocument.pdfException.msg", location);
                            SwingController.this.document = null;
                            SwingController.logger.log(Level.FINE, "Error opening document.", (Throwable) e4);
                            return null;
                        } catch (PDFSecurityException e5) {
                            Resources.showMessageDialog(SwingController.this.viewer, 1, SwingController.messageBundle, "viewer.dialog.openDocument.pdfSecurityException.title", "viewer.dialog.openDocument.pdfSecurityException.msg", location);
                            SwingController.this.document = null;
                            SwingController.logger.log(Level.FINE, "Error opening document.", (Throwable) e5);
                            return null;
                        } catch (Exception e6) {
                            Resources.showMessageDialog(SwingController.this.viewer, 1, SwingController.messageBundle, "viewer.dialog.openDocument.exception.title", "viewer.dialog.openDocument.exception.msg", location);
                            SwingController.this.document = null;
                            SwingController.logger.log(Level.FINE, "Error opening document.", (Throwable) e6);
                            return null;
                        }
                    }
                };
                worker.start();
            } catch (Exception e2) {
                Resources.showMessageDialog(this.viewer, 1, messageBundle, "viewer.dialog.openDocument.exception.title", "viewer.dialog.openDocument.exception.msg", location);
                this.document = null;
                logger.log(Level.FINE, "Error opening document.", (Throwable) e2);
            }
        }
    }

    public void openDocument(InputStream inputStream, String description, String pathOrURL) {
        try {
            if (inputStream != null) {
                try {
                    try {
                        try {
                            if (this.document != null) {
                                closeDocument();
                            }
                            setDisplayTool(51);
                            this.document = new Document();
                            if (this.documentViewController.getSecurityCallback() == null) {
                                this.document.setSecurityCallback(new MyGUISecurityCallback(this.viewer, messageBundle));
                            }
                            this.document.setInputStream(inputStream, pathOrURL);
                            commonNewDocumentHandling(description);
                            setDisplayTool(1);
                        } catch (Exception e2) {
                            Resources.showMessageDialog(this.viewer, 1, messageBundle, "viewer.dialog.openDocument.exception.title", "viewer.dialog.openDocument.exception.msg", description);
                            this.document = null;
                            logger.log(Level.FINE, "Error opening document.", (Throwable) e2);
                            setDisplayTool(1);
                        }
                    } catch (PDFSecurityException e3) {
                        Resources.showMessageDialog(this.viewer, 1, messageBundle, "viewer.dialog.openDocument.pdfSecurityException.title", "viewer.dialog.openDocument.pdfSecurityException.msg", description);
                        this.document = null;
                        logger.log(Level.FINE, "Error opening document.", (Throwable) e3);
                        setDisplayTool(1);
                    }
                } catch (PDFException e4) {
                    Resources.showMessageDialog(this.viewer, 1, messageBundle, "viewer.dialog.openDocument.pdfException.title", "viewer.dialog.openDocument.pdfException.msg", description);
                    this.document = null;
                    logger.log(Level.FINE, "Error opening document.", (Throwable) e4);
                    setDisplayTool(1);
                }
            }
        } catch (Throwable th) {
            setDisplayTool(1);
            throw th;
        }
    }

    public void openDocument(Document embeddedDocument, String fileName) {
        try {
            if (embeddedDocument != null) {
                try {
                    if (this.document != null) {
                        closeDocument();
                    }
                    setDisplayTool(51);
                    this.document = embeddedDocument;
                    if (this.documentViewController.getSecurityCallback() == null) {
                        this.document.setSecurityCallback(new MyGUISecurityCallback(this.viewer, messageBundle));
                    }
                    commonNewDocumentHandling(fileName);
                    setDisplayTool(1);
                } catch (Exception e2) {
                    Resources.showMessageDialog(this.viewer, 1, messageBundle, "viewer.dialog.openDocument.exception.title", "viewer.dialog.openDocument.exception.msg", fileName);
                    this.document = null;
                    logger.log(Level.FINE, "Error opening document.", (Throwable) e2);
                    setDisplayTool(1);
                }
            }
        } catch (Throwable th) {
            setDisplayTool(1);
            throw th;
        }
    }

    public void openDocument(byte[] data, int offset, int length, String description, String pathOrURL) {
        try {
            if (data != null) {
                try {
                    try {
                        if (this.document != null) {
                            closeDocument();
                        }
                        setDisplayTool(51);
                        this.document = new Document();
                        if (this.documentViewController.getSecurityCallback() == null) {
                            this.document.setSecurityCallback(new MyGUISecurityCallback(this.viewer, messageBundle));
                        }
                        this.document.setByteArray(data, offset, length, pathOrURL);
                        commonNewDocumentHandling(description);
                        setDisplayTool(1);
                    } catch (PDFException e2) {
                        Resources.showMessageDialog(this.viewer, 1, messageBundle, "viewer.dialog.openDocument.pdfException.title", "viewer.dialog.openDocument.pdfException.msg", description);
                        this.document = null;
                        logger.log(Level.FINE, "Error opening document.", (Throwable) e2);
                        setDisplayTool(1);
                    } catch (Exception e3) {
                        Resources.showMessageDialog(this.viewer, 1, messageBundle, "viewer.dialog.openDocument.exception.title", "viewer.dialog.openDocument.exception.msg", description);
                        this.document = null;
                        logger.log(Level.FINE, "Error opening document.", (Throwable) e3);
                        setDisplayTool(1);
                    }
                } catch (PDFSecurityException e4) {
                    Resources.showMessageDialog(this.viewer, 1, messageBundle, "viewer.dialog.openDocument.pdfSecurityException.title", "viewer.dialog.openDocument.pdfSecurityException.msg", description);
                    this.document = null;
                    logger.log(Level.FINE, "Error opening document.", (Throwable) e4);
                    setDisplayTool(1);
                }
            }
        } catch (Throwable th) {
            setDisplayTool(1);
            throw th;
        }
    }

    public void commonNewDocumentHandling(String fileDescription) throws HeadlessException, IllegalArgumentException {
        Object tmp;
        if (this.searchPanel != null) {
            this.searchPanel.setDocument(this.document);
        }
        if (this.thumbnailsPanel != null) {
            this.thumbnailsPanel.setDocument(this.document);
        }
        boolean showUtilityPane = false;
        Catalog catalog = this.document.getCatalog();
        Object tmp2 = catalog.getObject(Catalog.PAGELAYOUT_KEY);
        if (tmp2 != null && (tmp2 instanceof Name)) {
            String pageLayout = ((Name) tmp2).getName();
            int viewType = 1;
            if (pageLayout.equalsIgnoreCase("OneColumn")) {
                viewType = 2;
            } else if (pageLayout.equalsIgnoreCase("TwoColumnLeft")) {
                viewType = 4;
            } else if (pageLayout.equalsIgnoreCase("TwoColumnRight")) {
                viewType = 6;
            } else if (pageLayout.equalsIgnoreCase("TwoPageLeft")) {
                viewType = 3;
            } else if (pageLayout.equalsIgnoreCase("TwoPageRight")) {
                viewType = 5;
            }
            this.documentViewController.setViewType(viewType);
        }
        if (this.documentViewController.getViewMode() == 7) {
            this.documentViewController.revertViewType();
        }
        if (isPdfCollection()) {
            this.documentViewController.setViewType(7);
        }
        if (this.utilityTabbedPane != null && (tmp = catalog.getObject(Catalog.PAGEMODE_KEY)) != null && (tmp instanceof Name)) {
            String pageMode = ((Name) tmp).getName();
            showUtilityPane = pageMode.equalsIgnoreCase("UseOutlines") || pageMode.equalsIgnoreCase("UseOC") || pageMode.equalsIgnoreCase("UseThumbs");
        }
        this.documentViewController.setDocument(this.document);
        if (this.layersPanel != null) {
            this.layersPanel.setDocument(this.document);
        }
        if (this.propertiesManager == null && this.windowManagementCallback != null) {
            this.propertiesManager = this.windowManagementCallback.getProperties();
        }
        float defaultZoom = (float) PropertiesManager.checkAndStoreDoubleProperty(this.propertiesManager, PropertiesManager.PROPERTY_DEFAULT_ZOOM_LEVEL);
        this.documentViewController.setZoom(defaultZoom);
        setPageFitMode(PropertiesManager.checkAndStoreIntegerProperty(this.propertiesManager, PropertiesManager.PROPERTY_DEFAULT_PAGEFIT, 1), false);
        applyViewerPreferences(catalog, this.propertiesManager);
        OutlineItem item = null;
        Outlines outlines = this.document.getCatalog().getOutlines();
        if (outlines != null && this.outlinesTree != null) {
            item = outlines.getRootOutlineItem();
        }
        if (item != null) {
            this.outlinesTree.setModel(new DefaultTreeModel(new OutlineItemTreeNode(item)));
            this.outlinesTree.setRootVisible(!item.isEmpty());
            this.outlinesTree.setShowsRootHandles(true);
            if (this.utilityTabbedPane != null && this.outlinesScrollPane != null && this.utilityTabbedPane.indexOfComponent(this.outlinesScrollPane) > -1) {
                this.utilityTabbedPane.setEnabledAt(this.utilityTabbedPane.indexOfComponent(this.outlinesScrollPane), true);
                this.utilityTabbedPane.setSelectedComponent(this.outlinesScrollPane);
            }
        } else {
            if (this.utilityTabbedPane != null && this.outlinesScrollPane != null && this.utilityTabbedPane.indexOfComponent(this.outlinesScrollPane) > -1) {
                this.utilityTabbedPane.setEnabledAt(this.utilityTabbedPane.indexOfComponent(this.outlinesScrollPane), false);
            }
            if (!safelySelectUtilityPanel(this.searchPanel)) {
                safelySelectUtilityPanel(this.annotationPanel);
            }
        }
        boolean hideUtilityPane = PropertiesManager.checkAndStoreBooleanProperty(this.propertiesManager, PropertiesManager.PROPERTY_HIDE_UTILITYPANE, false);
        if (hideUtilityPane) {
            setUtilityPaneVisible(false);
        } else {
            setUtilityPaneVisible(showUtilityPane);
        }
        OptionalContent optionalContent = this.document.getCatalog().getOptionalContent();
        if (this.layersPanel != null && this.utilityTabbedPane != null) {
            if (optionalContent == null || optionalContent.getOrder() == null) {
                this.utilityTabbedPane.setEnabledAt(this.utilityTabbedPane.indexOfComponent(this.layersPanel), false);
            } else {
                this.utilityTabbedPane.setEnabledAt(this.utilityTabbedPane.indexOfComponent(this.layersPanel), true);
            }
        }
        if (this.viewer != null) {
            Object[] messageArguments = {fileDescription};
            MessageFormat formatter = new MessageFormat(messageBundle.getString("viewer.window.title.open.default"));
            this.viewer.setTitle(formatter.format(messageArguments));
        }
        if (this.annotationPanel != null) {
            this.annotationPanel.setEnabled(false);
        }
        reflectStateInComponents();
        updateDocumentView();
    }

    public void closeDocument() throws HeadlessException, IllegalArgumentException {
        if (this.searchPanel != null) {
            this.searchPanel.setDocument(null);
        }
        if (this.thumbnailsPanel != null) {
            this.thumbnailsPanel.setDocument(null);
        }
        if (this.layersPanel != null) {
            this.layersPanel.setDocument(null);
        }
        this.documentViewController.closeDocument();
        this.documentSearchController.dispose();
        if (this.document != null) {
            this.document.dispose();
            this.document = null;
        }
        if (this.currentPageNumberTextField != null) {
            this.currentPageNumberTextField.setText("");
        }
        if (this.numberOfPagesLabel != null) {
            this.numberOfPagesLabel.setText("");
        }
        if (this.currentPageNumberTextField != null) {
            this.currentPageNumberTextField.setEnabled(false);
        }
        if (this.statusLabel != null) {
            this.statusLabel.setText(" ");
        }
        if (this.zoomComboBox != null) {
            this.zoomComboBox.setSelectedItem(NumberFormat.getPercentInstance().format(1.0d));
        }
        updateDocumentView();
        TreeModel treeModel = this.outlinesTree != null ? this.outlinesTree.getModel() : null;
        if (treeModel != null) {
            OutlineItemTreeNode root = (OutlineItemTreeNode) treeModel.getRoot();
            if (root != null) {
                root.recursivelyClearOutlineItems();
            }
            this.outlinesTree.getSelectionModel().clearSelection();
            this.outlinesTree.getSelectionModel().setSelectionPath(null);
            this.outlinesTree.setSelectionPath(null);
            this.outlinesTree.setModel(null);
        }
        setUtilityPaneVisible(false);
        if (this.viewer != null) {
            this.viewer.setTitle(messageBundle.getString("viewer.window.title.default"));
            this.viewer.invalidate();
            this.viewer.validate();
            this.viewer.getContentPane().repaint();
        }
        reflectStateInComponents();
    }

    public void dispose() throws HeadlessException, IllegalArgumentException {
        if (this.disposed) {
            return;
        }
        this.disposed = true;
        closeDocument();
        this.openFileMenuItem = null;
        this.openURLMenuItem = null;
        this.closeMenuItem = null;
        this.saveAsFileMenuItem = null;
        this.exportTextMenuItem = null;
        this.exportSVGMenuItem = null;
        this.permissionsMenuItem = null;
        this.informationMenuItem = null;
        this.printSetupMenuItem = null;
        this.printMenuItem = null;
        this.exitMenuItem = null;
        this.fitActualSizeMenuItem = null;
        this.fitPageMenuItem = null;
        this.fitWidthMenuItem = null;
        this.zoomInMenuItem = null;
        this.zoomOutMenuItem = null;
        this.rotateLeftMenuItem = null;
        this.rotateRightMenuItem = null;
        this.showHideToolBarMenuItem = null;
        this.showHideUtilityPaneMenuItem = null;
        this.firstPageMenuItem = null;
        this.previousPageMenuItem = null;
        this.nextPageMenuItem = null;
        this.lastPageMenuItem = null;
        this.searchMenuItem = null;
        this.goToPageMenuItem = null;
        this.minimiseAllMenuItem = null;
        this.bringAllToFrontMenuItem = null;
        this.windowListMenuItems = null;
        this.aboutMenuItem = null;
        this.openFileButton = null;
        this.saveAsFileButton = null;
        this.printButton = null;
        this.searchButton = null;
        this.showHideUtilityPaneButton = null;
        this.firstPageButton = null;
        this.previousPageButton = null;
        this.nextPageButton = null;
        this.lastPageButton = null;
        if (this.currentPageNumberTextField != null) {
            this.currentPageNumberTextField.removeActionListener(this);
            this.currentPageNumberTextField.removeFocusListener(this);
            this.currentPageNumberTextField.removeKeyListener(this);
            this.currentPageNumberTextField = null;
        }
        this.numberOfPagesLabel = null;
        this.zoomInButton = null;
        this.zoomOutButton = null;
        if (this.zoomComboBox != null) {
            this.zoomComboBox.removeItemListener(this);
            this.zoomComboBox = null;
        }
        this.fitActualSizeButton = null;
        this.fitHeightButton = null;
        this.fitWidthButton = null;
        this.rotateLeftButton = null;
        this.rotateRightButton = null;
        this.panToolButton = null;
        this.zoomInToolButton = null;
        this.zoomDynamicToolButton = null;
        this.textSelectToolButton = null;
        this.selectToolButton = null;
        this.linkAnnotationToolButton = null;
        this.highlightAnnotationToolButton = null;
        this.highlightAnnotationUtilityToolButton = null;
        this.underlineAnnotationToolButton = null;
        this.strikeOutAnnotationToolButton = null;
        this.lineAnnotationToolButton = null;
        this.lineArrowAnnotationToolButton = null;
        this.squareAnnotationToolButton = null;
        this.circleAnnotationToolButton = null;
        this.inkAnnotationToolButton = null;
        this.freeTextAnnotationToolButton = null;
        this.textAnnotationToolButton = null;
        this.textAnnotationUtilityToolButton = null;
        this.fontEngineButton = null;
        this.completeToolBar = null;
        this.outlinesTree = null;
        if (this.outlinesScrollPane != null) {
            this.outlinesScrollPane.removeAll();
            this.outlinesScrollPane = null;
        }
        if (this.searchPanel != null) {
            this.searchPanel.dispose();
            this.searchPanel = null;
        }
        if (this.thumbnailsPanel != null) {
            this.thumbnailsPanel.dispose();
            this.thumbnailsPanel = null;
        }
        if (this.layersPanel != null) {
            this.layersPanel.dispose();
        }
        if (this.utilityTabbedPane != null) {
            this.utilityTabbedPane.removeAll();
            this.utilityTabbedPane = null;
        }
        if (this.documentViewController != null) {
            this.documentViewController.removePropertyChangeListener(this);
            this.documentViewController.dispose();
        }
        if (this.documentSearchController != null) {
            this.documentSearchController.dispose();
        }
        if (this.utilityAndDocumentSplitPane != null) {
            this.utilityAndDocumentSplitPane.removeAll();
            this.utilityAndDocumentSplitPane.removePropertyChangeListener(this);
        }
        this.statusLabel = null;
        if (this.viewer != null) {
            this.viewer.removeWindowListener(this);
            this.viewer.removeAll();
        }
        this.viewModel = null;
        this.windowManagementCallback = null;
    }

    public void saveFile() throws HeadlessException {
        int lastSeparator;
        if (!havePermissionToModifyDocument()) {
            Resources.showMessageDialog(this.viewer, 1, messageBundle, "viewer.dialog.saveAs.noPermission.title", "viewer.dialog.saveAs.noPermission.msg");
            return;
        }
        if (this.document.getStateManager().isChanged()) {
            Document document = this.document;
            if (!Document.foundIncrementalUpdater) {
                Resources.showMessageDialog(this.viewer, 1, messageBundle, "viewer.dialog.saveAs.noUpdates.title", "viewer.dialog.saveAs.noUpdates.msg");
            }
        }
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(messageBundle.getString("viewer.dialog.saveAs.title"));
        fileChooser.setFileSelectionMode(0);
        fileChooser.addChoosableFileFilter(FileExtensionUtils.getPDFFileFilter());
        if (ViewModel.getDefaultFile() != null) {
            fileChooser.setCurrentDirectory(ViewModel.getDefaultFile());
        }
        String origin = this.document.getDocumentOrigin();
        String originalFileName = null;
        if (origin != null && (lastSeparator = Math.max(Math.max(origin.lastIndexOf("/"), origin.lastIndexOf(FXMLLoader.ESCAPE_PREFIX)), origin.lastIndexOf(File.separator))) >= 0) {
            originalFileName = origin.substring(lastSeparator + 1);
            if (originalFileName != null && originalFileName.length() > 0) {
                fileChooser.setSelectedFile(new File(generateNewSaveName(originalFileName)));
            } else {
                originalFileName = null;
            }
        }
        int returnVal = fileChooser.showSaveDialog(this.viewer);
        if (returnVal == 0) {
            File file = fileChooser.getSelectedFile();
            String extension = FileExtensionUtils.getExtension(file);
            if (extension == null) {
                Resources.showMessageDialog(this.viewer, 1, messageBundle, "viewer.dialog.saveAs.noExtensionError.title", "viewer.dialog.saveAs.noExtensionError.msg");
                saveFile();
                return;
            }
            if (!extension.equals(FileExtensionUtils.pdf)) {
                Resources.showMessageDialog(this.viewer, 1, messageBundle, "viewer.dialog.saveAs.extensionError.title", "viewer.dialog.saveAs.extensionError.msg", file.getName());
                saveFile();
                return;
            }
            if (originalFileName != null && originalFileName.equalsIgnoreCase(file.getName())) {
                Resources.showMessageDialog(this.viewer, 1, messageBundle, "viewer.dialog.saveAs.noneUniqueName.title", "viewer.dialog.saveAs.noneUniqueName.msg", file.getName());
                saveFile();
                return;
            }
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                BufferedOutputStream buf = new BufferedOutputStream(fileOutputStream, 8192);
                this.document.saveToOutputStream(buf);
                buf.flush();
                fileOutputStream.flush();
                buf.close();
                fileOutputStream.close();
            } catch (MalformedURLException e2) {
                logger.log(Level.FINE, "Malformed URL Exception ", (Throwable) e2);
            } catch (IOException e3) {
                logger.log(Level.FINE, "IO Exception ", (Throwable) e3);
            }
            ViewModel.setDefaultFile(file);
        }
    }

    protected String generateNewSaveName(String fileName) {
        String result;
        if (fileName != null) {
            int endIndex = fileName.toLowerCase().indexOf(FileExtensionUtils.pdf) - 1;
            if (endIndex < 0) {
                result = fileName + "-new." + FileExtensionUtils.pdf;
            } else {
                result = fileName.substring(0, endIndex) + "-new." + FileExtensionUtils.pdf;
            }
            return result;
        }
        return fileName;
    }

    public void exportText() throws HeadlessException {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(messageBundle.getString("viewer.dialog.exportText.title"));
        fileChooser.setFileSelectionMode(0);
        fileChooser.addChoosableFileFilter(FileExtensionUtils.getTextFileFilter());
        if (ViewModel.getDefaultFile() != null) {
            fileChooser.setCurrentDirectory(ViewModel.getDefaultFile());
        }
        int returnVal = fileChooser.showSaveDialog(this.viewer);
        if (returnVal == 0) {
            File file = fileChooser.getSelectedFile();
            String extension = FileExtensionUtils.getExtension(file);
            if (extension != null) {
                ViewModel.setDefaultFile(file);
                TextExtractionTask textExtractionTask = new TextExtractionTask(this.document, file, messageBundle);
                ProgressMonitor progressMonitor = new ProgressMonitor(this.viewer, messageBundle.getString("viewer.dialog.exportText.progress.msg"), "", 0, textExtractionTask.getLengthOfTask());
                progressMonitor.setProgress(0);
                progressMonitor.setMillisToDecideToPopup(0);
                TextExtractionGlue glue = new TextExtractionGlue(textExtractionTask, progressMonitor);
                Timer timer = new Timer(1000, glue);
                glue.setTimer(timer);
                textExtractionTask.go();
                timer.start();
                return;
            }
            Resources.showMessageDialog(this.viewer, 1, messageBundle, "viewer.dialog.exportText.noExtensionError.title", "viewer.dialog.exportText.noExtensionError.msg");
            exportText();
        }
    }

    public void exportSVG() throws HeadlessException, IllegalArgumentException {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(messageBundle.getString("viewer.dialog.exportSVG.title"));
        fileChooser.setFileSelectionMode(0);
        fileChooser.addChoosableFileFilter(FileExtensionUtils.getSVGFileFilter());
        if (ViewModel.getDefaultFile() != null) {
            fileChooser.setCurrentDirectory(ViewModel.getDefaultFile());
        }
        int returnVal = fileChooser.showSaveDialog(this.viewer);
        if (returnVal == 0) {
            final File file = fileChooser.getSelectedFile();
            String extension = FileExtensionUtils.getExtension(file);
            if (extension != null) {
                if (extension.equals(FileExtensionUtils.svg)) {
                    final Document doc = this.document;
                    final int pageIndex = this.documentViewController.getCurrentPageIndex();
                    if (this.statusLabel != null) {
                        Object[] messageArguments = {String.valueOf(pageIndex + 1), file.getName()};
                        MessageFormat formatter = new MessageFormat(messageBundle.getString("viewer.dialog.exportSVG.status.exporting.msg"));
                        this.statusLabel.setText(formatter.format(messageArguments));
                    }
                    SwingWorker worker = new SwingWorker() { // from class: org.icepdf.ri.common.SwingController.2
                        @Override // org.icepdf.ri.common.SwingWorker
                        public Object construct() {
                            String error;
                            String tmpMsg;
                            try {
                                OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
                                SVG.createSVG(doc, pageIndex, out);
                                out.close();
                                error = null;
                            } catch (Throwable e2) {
                                error = e2.getMessage();
                                SwingController.logger.log(Level.FINE, "Error exporting to SVG");
                            }
                            if (error == null) {
                                Object[] messageArguments2 = {String.valueOf(pageIndex + 1), file.getName()};
                                MessageFormat formatter2 = new MessageFormat(SwingController.messageBundle.getString("viewer.dialog.exportSVG.status.exporting.msg"));
                                tmpMsg = formatter2.format(messageArguments2);
                            } else {
                                Object[] messageArguments3 = {String.valueOf(pageIndex + 1), file.getName(), error};
                                MessageFormat formatter3 = new MessageFormat(SwingController.messageBundle.getString("viewer.dialog.exportSVG.status.error.msg"));
                                tmpMsg = formatter3.format(messageArguments3);
                            }
                            final String msg = tmpMsg;
                            Runnable doSwingWork = new Runnable() { // from class: org.icepdf.ri.common.SwingController.2.1
                                @Override // java.lang.Runnable
                                public void run() throws IllegalArgumentException {
                                    if (SwingController.this.statusLabel != null) {
                                        SwingController.this.statusLabel.setText(msg);
                                    }
                                }
                            };
                            SwingUtilities.invokeLater(doSwingWork);
                            return null;
                        }
                    };
                    worker.setThreadPriority(1);
                    worker.start();
                } else {
                    Resources.showMessageDialog(this.viewer, 1, messageBundle, "viewer.dialog.exportSVG.exportError.title", "viewer.dialog.exportSVG.exportError.msg", file.getName());
                }
                ViewModel.setDefaultFile(file);
                return;
            }
            Resources.showMessageDialog(this.viewer, 1, messageBundle, "viewer.dialog.exportSVG.noExtensionError.title", "viewer.dialog.exportSVG.noExtensionError.msg");
            exportSVG();
        }
    }

    public boolean saveChangesDialog() throws HeadlessException {
        if (this.document != null) {
            boolean documentChanges = this.document.getStateManager().isChanged();
            if (!documentChanges) {
                return false;
            }
            Document document = this.document;
            if (Document.foundIncrementalUpdater) {
                Object[] colorArgument = {this.document.getDocumentOrigin()};
                MessageFormat formatter = new MessageFormat(messageBundle.getString("viewer.dialog.saveOnClose.noUpdates.msg"));
                String dialogMessage = formatter.format(colorArgument);
                int res = JOptionPane.showConfirmDialog(this.viewer, dialogMessage, messageBundle.getString("viewer.dialog.saveOnClose.noUpdates.title"), 1);
                if (res == 0) {
                    saveFile();
                    return false;
                }
                if (res != 1 && res == 2) {
                    return true;
                }
                return false;
            }
            return false;
        }
        return false;
    }

    public void toggleToolBarVisibility() throws IllegalArgumentException {
        if (this.completeToolBar != null) {
            setToolBarVisible(!this.completeToolBar.isVisible());
        }
    }

    public void setToolBarVisible(boolean show) throws IllegalArgumentException {
        if (this.completeToolBar != null) {
            this.completeToolBar.setVisible(show);
        }
        reflectStateInComponents();
    }

    public void showAboutDialog() {
        Runnable doSwingWork = new Runnable() { // from class: org.icepdf.ri.common.SwingController.3
            @Override // java.lang.Runnable
            public void run() {
                AboutDialog ad2 = new AboutDialog(SwingController.this.viewer, SwingController.messageBundle, true, 2, 0);
                ad2.setVisible(true);
            }
        };
        SwingUtilities.invokeLater(doSwingWork);
    }

    public void showDocumentPermissionsDialog() {
        PermissionsDialog pd = new PermissionsDialog(this.viewer, this.document, messageBundle);
        pd.setVisible(true);
    }

    public void showDocumentInformationDialog() {
        DocumentInformationDialog did = new DocumentInformationDialog(this.viewer, this.document, messageBundle);
        did.setVisible(true);
    }

    public void showPrintSetupDialog() throws HeadlessException {
        PrintHelper printHelper;
        PrintHelper printHelper2 = this.viewModel.getPrintHelper();
        if (printHelper2 == null) {
            MediaSizeName mediaSizeName = loadDefaultPrinterProperties();
            printHelper = new PrintHelper(this.documentViewController.getViewContainer(), getPageTree(), this.documentViewController.getRotation(), mediaSizeName, PrintQuality.NORMAL);
        } else {
            printHelper = new PrintHelper(this.documentViewController.getViewContainer(), getPageTree(), this.documentViewController.getRotation(), printHelper2.getDocAttributeSet(), printHelper2.getPrintRequestAttributeSet());
        }
        this.viewModel.setPrintHelper(printHelper);
        this.viewModel.getPrintHelper().showPrintSetupDialog();
        savePrinterProperties(printHelper);
    }

    public void setPrintDefaultMediaSizeName(MediaSizeName mediaSize) {
        PrintHelper printHelper = new PrintHelper(this.documentViewController.getViewContainer(), getPageTree(), this.documentViewController.getRotation(), mediaSize, PrintQuality.NORMAL);
        this.viewModel.setPrintHelper(printHelper);
        savePrinterProperties(printHelper);
    }

    public void print(final boolean withDialog) {
        if (this.printMenuItem != null) {
            this.printMenuItem.setEnabled(false);
        }
        if (this.printButton != null) {
            this.printButton.setEnabled(false);
        }
        Runnable runner = new Runnable() { // from class: org.icepdf.ri.common.SwingController.4
            @Override // java.lang.Runnable
            public void run() throws HeadlessException {
                SwingController.this.initialisePrinting(withDialog);
            }
        };
        Thread t2 = new Thread(runner);
        t2.setPriority(1);
        t2.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initialisePrinting(boolean withDialog) throws HeadlessException {
        PrintHelper printHelper;
        boolean canPrint = havePermissionToPrint();
        if (!canPrint) {
            reenablePrintUI();
            return;
        }
        PrintHelper printHelper2 = this.viewModel.getPrintHelper();
        if (printHelper2 == null) {
            MediaSizeName mediaSizeName = loadDefaultPrinterProperties();
            printHelper = new PrintHelper(this.documentViewController.getViewContainer(), getPageTree(), this.documentViewController.getRotation(), mediaSizeName, PrintQuality.NORMAL);
        } else {
            printHelper = new PrintHelper(this.documentViewController.getViewContainer(), getPageTree(), this.documentViewController.getRotation(), printHelper2.getDocAttributeSet(), printHelper2.getPrintRequestAttributeSet());
        }
        this.viewModel.setPrintHelper(printHelper);
        boolean canPrint2 = printHelper.setupPrintService(0, this.document.getNumberOfPages() - 1, this.viewModel.getPrintCopies(), this.viewModel.isShrinkToPrintableArea(), withDialog);
        savePrinterProperties(printHelper);
        if (!canPrint2) {
            reenablePrintUI();
        } else {
            startBackgroundPrinting(printHelper);
        }
    }

    private MediaSizeName loadDefaultPrinterProperties() throws HeadlessException {
        int printMediaUnit = PropertiesManager.checkAndStoreIntegerProperty(this.propertiesManager, PropertiesManager.PROPERTY_PRINT_MEDIA_SIZE_UNIT, 1000);
        double printMediaWidth = PropertiesManager.checkAndStoreDoubleProperty(this.propertiesManager, PropertiesManager.PROPERTY_PRINT_MEDIA_SIZE_WIDTH, 215.9d);
        double printMediaHeight = PropertiesManager.checkAndStoreDoubleProperty(this.propertiesManager, PropertiesManager.PROPERTY_PRINT_MEDIA_SIZE_HEIGHT, 279.4d);
        return MediaSize.findMedia((float) printMediaWidth, (float) printMediaHeight, printMediaUnit);
    }

    private void savePrinterProperties(PrintHelper printHelper) {
        PrintRequestAttributeSet printRequestAttributeSet = printHelper.getPrintRequestAttributeSet();
        Object printAttributeSet = printRequestAttributeSet.get(Media.class);
        if (this.propertiesManager != null && (printAttributeSet instanceof MediaSizeName)) {
            MediaSizeName paper = (MediaSizeName) printAttributeSet;
            MediaSize mediaSize = MediaSize.getMediaSizeForName(paper);
            this.propertiesManager.set(PropertiesManager.PROPERTY_PRINT_MEDIA_SIZE_UNIT, String.valueOf(1000));
            double printMediaWidth = mediaSize.getX(1000);
            this.propertiesManager.set(PropertiesManager.PROPERTY_PRINT_MEDIA_SIZE_WIDTH, String.valueOf(printMediaWidth));
            double printMediaHeight = mediaSize.getY(1000);
            this.propertiesManager.set(PropertiesManager.PROPERTY_PRINT_MEDIA_SIZE_HEIGHT, String.valueOf(printMediaHeight));
        }
    }

    private void reenablePrintUI() {
        SwingUtilities.invokeLater(new Runnable() { // from class: org.icepdf.ri.common.SwingController.5
            @Override // java.lang.Runnable
            public void run() {
                if (SwingController.this.printMenuItem != null) {
                    SwingController.this.printMenuItem.setEnabled(true);
                }
                if (SwingController.this.printButton != null) {
                    SwingController.this.printButton.setEnabled(true);
                }
            }
        });
    }

    private void startBackgroundPrinting(final PrintHelper printHelper) {
        SwingUtilities.invokeLater(new Runnable() { // from class: org.icepdf.ri.common.SwingController.6
            @Override // java.lang.Runnable
            public void run() {
                SwingController.this.printProgressMonitor = new ProgressMonitor(SwingController.this.viewer, SwingController.messageBundle.getString("viewer.dialog.printing.status.start.msg"), "", 0, printHelper.getNumberOfPages());
            }
        });
        final Thread printingThread = Thread.currentThread();
        final PrinterTask printerTask = new PrinterTask(printHelper);
        this.printActivityMonitor = new Timer(500, new ActionListener() { // from class: org.icepdf.ri.common.SwingController.7
            @Override // java.awt.event.ActionListener
            public void actionPerformed(ActionEvent event) throws IllegalArgumentException {
                int limit = printHelper.getNumberOfPages();
                int current = printHelper.getCurrentPage();
                SwingController.this.printProgressMonitor.setProgress(current);
                Object[] messageArguments = {String.valueOf(current), String.valueOf(limit)};
                MessageFormat formatter = new MessageFormat(SwingController.messageBundle.getString("viewer.dialog.printing.status.progress.msg"));
                SwingController.this.printProgressMonitor.setNote(formatter.format(messageArguments));
                if (!printingThread.isAlive() || SwingController.this.printProgressMonitor.isCanceled()) {
                    SwingController.this.printProgressMonitor.close();
                    SwingController.this.printActivityMonitor.stop();
                    printerTask.cancel();
                    if (SwingController.this.printMenuItem != null) {
                        SwingController.this.printMenuItem.setEnabled(true);
                    }
                    if (SwingController.this.printButton != null) {
                        SwingController.this.printButton.setEnabled(true);
                    }
                }
            }
        });
        this.printActivityMonitor.start();
        printerTask.run();
    }

    public void showPageFromTextField() {
        String ob = this.currentPageNumberTextField.getText();
        if (ob != null) {
            try {
                int pageIndex = Integer.parseInt(ob) - 1;
                showPage(pageIndex);
            } catch (NumberFormatException e2) {
                logger.log(Level.FINE, "Error converting page number.");
            }
        }
    }

    public void followOutlineItem(OutlineItem o2) throws Exception {
        if (o2 == null) {
            return;
        }
        Destination dest = o2.getDest();
        if (o2.getAction() != null) {
            Action action = o2.getAction();
            if (action instanceof GoToAction) {
                dest = ((GoToAction) action).getDestination();
            } else if (action instanceof URIAction) {
                BareBonesBrowserLaunch.openURL(((URIAction) action).getURI());
            } else {
                Library library = action.getLibrary();
                HashMap entries = action.getEntries();
                dest = new Destination(library, library.getObject(entries, Destination.D_KEY));
            }
        }
        if (dest == null) {
            return;
        }
        int oldTool = getDocumentViewToolMode();
        try {
            setDisplayTool(51);
            this.documentViewController.setDestinationTarget(dest);
            setDisplayTool(oldTool);
        } catch (Throwable th) {
            setDisplayTool(oldTool);
            throw th;
        }
    }

    private boolean isDragAcceptable(DropTargetDragEvent event) {
        return (event.getDropAction() & 3) != 0;
    }

    private boolean isDropAcceptable(DropTargetDropEvent event) {
        return (event.getDropAction() & 3) != 0;
    }

    public void zoomIn() {
        this.documentViewController.setZoomIn();
    }

    public void zoomOut() {
        this.documentViewController.setZoomOut();
    }

    public void setZoom(float zoom) {
        this.documentViewController.setZoom(zoom);
    }

    public void doCommonZoomUIUpdates(boolean becauseOfValidFitMode) {
        reflectZoomInZoomComboBox();
        if (!becauseOfValidFitMode) {
            setPageFitMode(1, false);
        }
    }

    public boolean isCurrentPage() {
        PageTree pageTree = getPageTree();
        if (pageTree == null) {
            return false;
        }
        Page page = pageTree.getPage(this.documentViewController.getCurrentPageIndex());
        return page != null;
    }

    public PageTree getPageTree() {
        if (this.document == null) {
            return null;
        }
        return this.document.getPageTree();
    }

    public void showPage(int nPage) {
        if (nPage >= 0 && nPage < getPageTree().getNumberOfPages()) {
            this.documentViewController.setCurrentPageIndex(nPage);
            updateDocumentView();
        }
    }

    public void goToDeltaPage(int delta) {
        int currPage = this.documentViewController.getCurrentPageIndex();
        int nPage = currPage + delta;
        int totalPages = getPageTree().getNumberOfPages();
        if (totalPages == 0) {
            return;
        }
        if (nPage >= totalPages) {
            nPage = totalPages - 1;
        }
        if (nPage < 0) {
            nPage = 0;
        }
        if (nPage != currPage) {
            this.documentViewController.setCurrentPageIndex(nPage);
            updateDocumentView();
        }
    }

    public void updateDocumentView() {
        if (this.disposed) {
            return;
        }
        int oldTool = getDocumentViewToolMode();
        try {
            try {
                setDisplayTool(51);
                reflectPageChangeInComponents();
                PageTree pageTree = getPageTree();
                if (this.currentPageNumberTextField != null) {
                    this.currentPageNumberTextField.setText(Integer.toString(this.documentViewController.getCurrentPageDisplayValue()));
                }
                if (this.numberOfPagesLabel != null && pageTree != null) {
                    Object[] messageArguments = {String.valueOf(pageTree.getNumberOfPages())};
                    MessageFormat formatter = new MessageFormat(messageBundle.getString("viewer.toolbar.pageIndicator"));
                    String numberOfPages = formatter.format(messageArguments);
                    this.numberOfPagesLabel.setText(numberOfPages);
                }
                if (this.statusLabel != null && pageTree != null) {
                    Object[] messageArguments2 = {String.valueOf(this.documentViewController.getCurrentPageDisplayValue()), String.valueOf(pageTree.getNumberOfPages())};
                    MessageFormat formatter2 = new MessageFormat(messageBundle.getString("viewer.statusbar.currentPage"));
                    this.statusLabel.setText(formatter2.format(messageArguments2));
                }
            } catch (Exception e2) {
                logger.log(Level.FINE, "Error updating page view.", (Throwable) e2);
                setDisplayTool(oldTool);
            }
        } finally {
            setDisplayTool(oldTool);
        }
    }

    public void rotateLeft() {
        this.documentViewController.setRotateLeft();
        setPageFitMode(this.documentViewController.getFitMode(), true);
    }

    public void rotateRight() {
        this.documentViewController.setRotateRight();
        setPageFitMode(this.documentViewController.getFitMode(), true);
    }

    public boolean isDocumentFitMode(int fitMode) {
        return this.documentViewController.getFitMode() == fitMode;
    }

    public boolean isDocumentViewMode(int viewMode) {
        return this.documentViewController.getViewMode() == viewMode;
    }

    public void setPageViewSinglePageConButton(JToggleButton btn) {
        this.singlePageViewContinuousButton = btn;
        btn.addItemListener(this);
    }

    public void setPageViewFacingPageConButton(JToggleButton btn) {
        this.facingPageViewContinuousButton = btn;
        btn.addItemListener(this);
    }

    public void setPageViewSinglePageNonConButton(JToggleButton btn) {
        this.singlePageViewNonContinuousButton = btn;
        btn.addItemListener(this);
    }

    public void setPageViewFacingPageNonConButton(JToggleButton btn) {
        this.facingPageViewNonContinuousButton = btn;
        btn.addItemListener(this);
    }

    public void setPageFitMode(int fitMode, boolean refresh) {
        if (!refresh && this.documentViewController.getFitMode() == fitMode) {
            return;
        }
        this.documentViewController.setFitMode(fitMode);
        reflectZoomInZoomComboBox();
        reflectFitInFitButtons();
    }

    public void setPageViewMode(int viewMode, boolean refresh) {
        if (!refresh && this.documentViewController.getViewMode() == viewMode) {
            return;
        }
        this.documentViewController.setViewType(viewMode);
        reflectDocumentViewModeInButtons();
        reflectFitInFitButtons();
    }

    @Override // org.icepdf.ri.common.views.Controller
    public void setDocumentToolMode(int toolType) {
        if (this.documentViewController.isToolModeSelected(toolType)) {
            return;
        }
        this.documentViewController.setToolMode(toolType);
        reflectToolInToolButtons();
    }

    public boolean isUtilityPaneVisible() {
        return this.utilityTabbedPane != null && this.utilityTabbedPane.isVisible();
    }

    public void setUtilityPaneVisible(boolean visible) throws IllegalArgumentException {
        if (this.utilityTabbedPane != null) {
            this.utilityTabbedPane.setVisible(visible);
        }
        if (this.utilityAndDocumentSplitPane != null) {
            if (visible) {
                this.utilityAndDocumentSplitPane.setDividerLocation(this.utilityAndDocumentSplitPaneLastDividerLocation);
                this.utilityAndDocumentSplitPane.setDividerSize(8);
            } else {
                int divLoc = this.utilityAndDocumentSplitPane.getDividerLocation();
                if (divLoc > 5) {
                    this.utilityAndDocumentSplitPaneLastDividerLocation = divLoc;
                }
                this.utilityAndDocumentSplitPane.setDividerSize(0);
            }
        }
        reflectStateInComponents();
    }

    public void toggleUtilityPaneVisibility() throws IllegalArgumentException {
        setUtilityPaneVisible(!isUtilityPaneVisible());
    }

    protected boolean safelySelectUtilityPanel(Component comp) {
        if (this.utilityTabbedPane != null && comp != null && this.utilityTabbedPane.indexOfComponent(comp) > -1) {
            this.utilityTabbedPane.setSelectedComponent(comp);
            return true;
        }
        return false;
    }

    public void showSearchPanel() throws IllegalArgumentException {
        if (this.utilityTabbedPane != null && this.searchPanel != null) {
            toggleUtilityPaneVisibility();
            if (isUtilityPaneVisible()) {
                if (this.utilityTabbedPane.getSelectedComponent() != this.searchPanel) {
                    safelySelectUtilityPanel(this.searchPanel);
                }
                this.searchPanel.requestFocus();
            }
        }
    }

    public void showAnnotationPanel(AnnotationComponent selectedAnnotation) throws IllegalArgumentException {
        if (this.utilityTabbedPane != null && this.annotationPanel != null) {
            if (selectedAnnotation != null) {
                this.annotationPanel.setEnabled(true);
                this.annotationPanel.setAnnotationComponent(selectedAnnotation);
            }
            setUtilityPaneVisible(true);
            if (this.utilityTabbedPane.getSelectedComponent() != this.annotationPanel) {
                safelySelectUtilityPanel(this.annotationPanel);
            }
        }
    }

    public void showPageSelectionDialog() throws HeadlessException {
        int numPages = getPageTree().getNumberOfPages();
        Object[] s2 = new Object[numPages];
        for (int i2 = 0; i2 < numPages; i2++) {
            s2[i2] = Integer.toString(i2 + 1);
        }
        Object initialSelection = s2[this.documentViewController.getCurrentPageIndex()];
        Object ob = JOptionPane.showInputDialog(this.viewer, messageBundle.getString("viewer.dialog.goToPage.description.label"), messageBundle.getString("viewer.dialog.goToPage.title"), 3, null, s2, initialSelection);
        if (ob != null) {
            try {
                int pageIndex = Integer.parseInt(ob.toString()) - 1;
                showPage(pageIndex);
            } catch (NumberFormatException e2) {
                logger.log(Level.FINE, "Error selecting page number.");
            }
        }
    }

    protected void applyViewerPreferences(Catalog catalog, PropertiesManager propertiesManager) {
        if (catalog == null) {
            return;
        }
        ViewerPreferences viewerPref = catalog.getViewerPreferences();
        if (viewerPref != null && viewerPref.hasHideToolbar()) {
            if (viewerPref.getHideToolbar() && this.completeToolBar != null) {
                this.completeToolBar.setVisible(false);
            }
        } else if (this.completeToolBar != null) {
            this.completeToolBar.setVisible(!PropertiesManager.checkAndStoreBooleanProperty(propertiesManager, PropertiesManager.PROPERTY_VIEWPREF_HIDETOOLBAR, false));
        }
        if (viewerPref != null && viewerPref.hasHideMenubar()) {
            if (viewerPref.getHideMenubar() && this.viewer != null && this.viewer.getJMenuBar() != null) {
                this.viewer.getJMenuBar().setVisible(false);
            }
        } else if (this.viewer != null && this.viewer.getJMenuBar() != null) {
            this.viewer.getJMenuBar().setVisible(!PropertiesManager.checkAndStoreBooleanProperty(propertiesManager, PropertiesManager.PROPERTY_VIEWPREF_HIDEMENUBAR, false));
        }
        if (viewerPref != null && viewerPref.hasFitWindow()) {
            if (viewerPref.getFitWindow() && this.viewer != null) {
                this.viewer.setSize(this.documentViewController.getDocumentView().getDocumentSize());
                return;
            }
            return;
        }
        if (PropertiesManager.checkAndStoreBooleanProperty(propertiesManager, PropertiesManager.PROPERTY_VIEWPREF_FITWINDOW, false) && this.viewer != null) {
            this.viewer.setSize(this.documentViewController.getDocumentView().getDocumentSize());
        }
    }

    public ViewModel getViewModel() {
        return this.viewModel;
    }

    @Override // org.icepdf.ri.common.views.Controller
    public Document getDocument() {
        return this.document;
    }

    @Override // org.icepdf.ri.common.views.Controller
    public int getCurrentPageNumber() {
        return this.documentViewController.getCurrentPageIndex();
    }

    @Override // org.icepdf.ri.common.views.Controller
    public float getUserRotation() {
        return this.documentViewController.getRotation();
    }

    @Override // org.icepdf.ri.common.views.Controller
    public float getUserZoom() {
        return this.documentViewController.getZoom();
    }

    /* JADX WARN: Finally extract failed */
    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();
        if (source == null) {
            return;
        }
        boolean cancelSetFocus = false;
        try {
            if (source == this.openFileMenuItem || source == this.openFileButton) {
                Runnable doSwingWork = new Runnable() { // from class: org.icepdf.ri.common.SwingController.8
                    @Override // java.lang.Runnable
                    public void run() throws HeadlessException {
                        SwingController.this.openFile();
                    }
                };
                SwingUtilities.invokeLater(doSwingWork);
            } else if (source == this.openURLMenuItem) {
                Runnable doSwingWork2 = new Runnable() { // from class: org.icepdf.ri.common.SwingController.9
                    @Override // java.lang.Runnable
                    public void run() throws HeadlessException, IllegalArgumentException {
                        SwingController.this.openURL();
                    }
                };
                SwingUtilities.invokeLater(doSwingWork2);
            } else if (source == this.closeMenuItem) {
                boolean isCanceled = saveChangesDialog();
                if (!isCanceled) {
                    closeDocument();
                }
            } else if (source == this.saveAsFileMenuItem || source == this.saveAsFileButton) {
                Runnable doSwingWork3 = new Runnable() { // from class: org.icepdf.ri.common.SwingController.10
                    @Override // java.lang.Runnable
                    public void run() throws HeadlessException {
                        SwingController.this.saveFile();
                    }
                };
                SwingUtilities.invokeLater(doSwingWork3);
            } else if (source == this.exportTextMenuItem) {
                Runnable doSwingWork4 = new Runnable() { // from class: org.icepdf.ri.common.SwingController.11
                    @Override // java.lang.Runnable
                    public void run() throws HeadlessException {
                        SwingController.this.exportText();
                    }
                };
                SwingUtilities.invokeLater(doSwingWork4);
            } else if (source == this.exportSVGMenuItem) {
                Runnable doSwingWork5 = new Runnable() { // from class: org.icepdf.ri.common.SwingController.12
                    @Override // java.lang.Runnable
                    public void run() throws HeadlessException, IllegalArgumentException {
                        SwingController.this.exportSVG();
                    }
                };
                SwingUtilities.invokeLater(doSwingWork5);
            } else if (source == this.exitMenuItem) {
                boolean isCanceled2 = saveChangesDialog();
                if (!isCanceled2 && this.windowManagementCallback != null) {
                    this.windowManagementCallback.disposeWindow(this, this.viewer, null);
                }
            } else if (source == this.showHideToolBarMenuItem) {
                toggleToolBarVisibility();
            } else if (source == this.minimiseAllMenuItem) {
                Runnable doSwingWork6 = new Runnable() { // from class: org.icepdf.ri.common.SwingController.13
                    @Override // java.lang.Runnable
                    public void run() {
                        SwingController sc = SwingController.this;
                        if (sc.getWindowManagementCallback() != null) {
                            sc.getWindowManagementCallback().minimiseAllWindows();
                        }
                    }
                };
                SwingUtilities.invokeLater(doSwingWork6);
            } else if (source == this.bringAllToFrontMenuItem) {
                Runnable doSwingWork7 = new Runnable() { // from class: org.icepdf.ri.common.SwingController.14
                    @Override // java.lang.Runnable
                    public void run() {
                        SwingController sc = SwingController.this;
                        if (sc.getWindowManagementCallback() != null) {
                            sc.getWindowManagementCallback().bringAllWindowsToFront(sc);
                        }
                    }
                };
                SwingUtilities.invokeLater(doSwingWork7);
            } else if (this.windowListMenuItems != null && this.windowListMenuItems.contains(source)) {
                final int index = this.windowListMenuItems.indexOf(source);
                Runnable doSwingWork8 = new Runnable() { // from class: org.icepdf.ri.common.SwingController.15
                    @Override // java.lang.Runnable
                    public void run() {
                        SwingController sc = SwingController.this;
                        if (sc.getWindowManagementCallback() != null) {
                            sc.getWindowManagementCallback().bringWindowToFront(index);
                        }
                    }
                };
                SwingUtilities.invokeLater(doSwingWork8);
            } else if (source == this.aboutMenuItem) {
                showAboutDialog();
            } else if (this.document != null) {
                int documentIcon = getDocumentViewToolMode();
                try {
                    setDisplayTool(51);
                    if (source == this.permissionsMenuItem) {
                        Runnable doSwingWork9 = new Runnable() { // from class: org.icepdf.ri.common.SwingController.16
                            @Override // java.lang.Runnable
                            public void run() {
                                SwingController.this.showDocumentPermissionsDialog();
                            }
                        };
                        SwingUtilities.invokeLater(doSwingWork9);
                    } else if (source == this.informationMenuItem) {
                        Runnable doSwingWork10 = new Runnable() { // from class: org.icepdf.ri.common.SwingController.17
                            @Override // java.lang.Runnable
                            public void run() {
                                SwingController.this.showDocumentInformationDialog();
                            }
                        };
                        SwingUtilities.invokeLater(doSwingWork10);
                    } else if (source == this.printSetupMenuItem) {
                        Runnable doSwingWork11 = new Runnable() { // from class: org.icepdf.ri.common.SwingController.18
                            @Override // java.lang.Runnable
                            public void run() throws HeadlessException {
                                SwingController.this.showPrintSetupDialog();
                            }
                        };
                        SwingUtilities.invokeLater(doSwingWork11);
                    } else if (source == this.printMenuItem || source == this.printButton) {
                        print(true);
                    } else if (source == this.undoMenuItem) {
                        this.documentViewController.undo();
                        reflectUndoCommands();
                    } else if (source == this.redoMenuItem) {
                        this.documentViewController.redo();
                        reflectUndoCommands();
                    } else if (source == this.deleteMenuItem) {
                        this.documentViewController.deleteCurrentAnnotation();
                        reflectUndoCommands();
                    } else if (source == this.copyMenuItem) {
                        if (this.document != null && havePermissionToExtractContent() && (!this.documentViewController.getDocumentViewModel().isSelectAll() || this.document.getNumberOfPages() <= 250)) {
                            StringSelection stringSelection = new StringSelection(this.documentViewController.getSelectedText());
                            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
                        } else {
                            Runnable doSwingWork12 = new Runnable() { // from class: org.icepdf.ri.common.SwingController.19
                                @Override // java.lang.Runnable
                                public void run() throws HeadlessException {
                                    Resources.showMessageDialog(SwingController.this.viewer, 1, SwingController.messageBundle, "viewer.dialog.information.copyAll.title", "viewer.dialog.information.copyAll.msg", 250);
                                }
                            };
                            SwingUtilities.invokeLater(doSwingWork12);
                        }
                    } else if (source == this.selectAllMenuItem) {
                        this.documentViewController.selectAllText();
                    } else if (source == this.deselectAllMenuItem) {
                        this.documentViewController.clearSelectedText();
                    } else if (source == this.fitActualSizeMenuItem) {
                        setPageFitMode(2, false);
                    } else if (source == this.fitPageMenuItem) {
                        setPageFitMode(3, false);
                    } else if (source == this.fitWidthMenuItem) {
                        setPageFitMode(4, false);
                    } else if (source == this.zoomInMenuItem || source == this.zoomInButton) {
                        zoomIn();
                    } else if (source == this.zoomOutMenuItem || source == this.zoomOutButton) {
                        zoomOut();
                    } else if (source == this.rotateLeftMenuItem || source == this.rotateLeftButton) {
                        rotateLeft();
                    } else if (source == this.rotateRightMenuItem || source == this.rotateRightButton) {
                        rotateRight();
                    } else if (source == this.showHideUtilityPaneMenuItem || source == this.showHideUtilityPaneButton) {
                        toggleUtilityPaneVisibility();
                    } else if (source == this.firstPageMenuItem || source == this.firstPageButton) {
                        showPage(0);
                    } else if (source == this.previousPageMenuItem || source == this.previousPageButton) {
                        DocumentView documentView = this.documentViewController.getDocumentView();
                        goToDeltaPage(-documentView.getPreviousPageIncrement());
                    } else if (source == this.nextPageMenuItem || source == this.nextPageButton) {
                        DocumentView documentView2 = this.documentViewController.getDocumentView();
                        goToDeltaPage(documentView2.getNextPageIncrement());
                    } else if (source == this.lastPageMenuItem || source == this.lastPageButton) {
                        showPage(getPageTree().getNumberOfPages() - 1);
                    } else if (source == this.searchMenuItem || source == this.searchButton) {
                        cancelSetFocus = true;
                        showSearchPanel();
                    } else if (source == this.goToPageMenuItem) {
                        showPageSelectionDialog();
                    } else if (source == this.currentPageNumberTextField) {
                        showPageFromTextField();
                    } else {
                        logger.log(Level.FINE, "Unknown action event: " + source.toString());
                    }
                    setDisplayTool(documentIcon);
                } catch (Throwable th) {
                    setDisplayTool(documentIcon);
                    throw th;
                }
            }
        } catch (Exception e2) {
            Runnable doSwingWork13 = new Runnable() { // from class: org.icepdf.ri.common.SwingController.20
                @Override // java.lang.Runnable
                public void run() throws HeadlessException {
                    Resources.showMessageDialog(SwingController.this.viewer, 1, SwingController.messageBundle, "viewer.dialog.error.exception.title", "viewer.dialog.error.exception.msg", e2.getMessage());
                }
            };
            SwingUtilities.invokeLater(doSwingWork13);
            logger.log(Level.FINE, "Error processing action event.", (Throwable) e2);
        }
        if (!cancelSetFocus) {
            this.documentViewController.requestViewFocusInWindow();
        }
    }

    @Override // java.awt.event.FocusListener
    public void focusGained(FocusEvent e2) {
    }

    @Override // java.awt.event.FocusListener
    public void focusLost(FocusEvent e2) {
        Object src = e2.getSource();
        if (src != null && src == this.currentPageNumberTextField) {
            String fieldValue = this.currentPageNumberTextField.getText();
            String modelValue = Integer.toString(this.documentViewController.getCurrentPageDisplayValue());
            if (!fieldValue.equals(modelValue)) {
                this.currentPageNumberTextField.setText(modelValue);
            }
        }
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent e2) {
        Object source = e2.getSource();
        if (source == null) {
            return;
        }
        boolean doSetFocus = false;
        int tool = getDocumentViewToolMode();
        setDisplayTool(51);
        try {
            if (source == this.zoomComboBox) {
                if (e2.getStateChange() == 1) {
                    setZoomFromZoomComboBox();
                }
            } else if (source == this.fitActualSizeButton) {
                if (e2.getStateChange() == 1) {
                    setPageFitMode(2, false);
                    doSetFocus = true;
                }
            } else if (source == this.fitHeightButton) {
                if (e2.getStateChange() == 1) {
                    setPageFitMode(3, false);
                    doSetFocus = true;
                }
            } else if (source == this.fitWidthButton) {
                if (e2.getStateChange() == 1) {
                    setPageFitMode(4, false);
                    doSetFocus = true;
                }
            } else if (source == this.fontEngineButton) {
                if (e2.getStateChange() == 1 || e2.getStateChange() == 2) {
                    FontFactory.getInstance().toggleAwtFontSubstitution();
                    this.documentViewController.getDocumentView().getViewModel().invalidate();
                    doSetFocus = true;
                }
            } else if (source == this.panToolButton) {
                if (e2.getStateChange() == 1) {
                    tool = 1;
                    setDocumentToolMode(1);
                    doSetFocus = true;
                }
            } else if (source == this.zoomInToolButton) {
                if (e2.getStateChange() == 1) {
                    tool = 2;
                    setDocumentToolMode(2);
                    doSetFocus = true;
                }
            } else if (source == this.zoomDynamicToolButton) {
                if (e2.getStateChange() == 1) {
                    tool = 4;
                    setDocumentToolMode(4);
                    doSetFocus = true;
                }
            } else if (source == this.textSelectToolButton) {
                if (e2.getStateChange() == 1) {
                    tool = 5;
                    setDocumentToolMode(5);
                    doSetFocus = true;
                }
            } else if (source == this.selectToolButton) {
                if (e2.getStateChange() == 1) {
                    tool = 6;
                    setDocumentToolMode(6);
                    showAnnotationPanel(null);
                }
            } else if (source == this.linkAnnotationToolButton) {
                if (e2.getStateChange() == 1) {
                    tool = 7;
                    setDocumentToolMode(7);
                }
            } else if (source == this.highlightAnnotationToolButton || source == this.highlightAnnotationUtilityToolButton) {
                if (e2.getStateChange() == 1) {
                    tool = 8;
                    setDocumentToolMode(8);
                }
            } else if (source == this.strikeOutAnnotationToolButton) {
                if (e2.getStateChange() == 1) {
                    tool = 11;
                    setDocumentToolMode(11);
                }
            } else if (source == this.underlineAnnotationToolButton) {
                if (e2.getStateChange() == 1) {
                    tool = 9;
                    setDocumentToolMode(9);
                }
            } else if (source == this.lineAnnotationToolButton) {
                if (e2.getStateChange() == 1) {
                    tool = 12;
                    setDocumentToolMode(12);
                }
            } else if (source == this.lineArrowAnnotationToolButton) {
                if (e2.getStateChange() == 1) {
                    tool = 13;
                    setDocumentToolMode(13);
                }
            } else if (source == this.squareAnnotationToolButton) {
                if (e2.getStateChange() == 1) {
                    tool = 14;
                    setDocumentToolMode(14);
                }
            } else if (source == this.circleAnnotationToolButton) {
                if (e2.getStateChange() == 1) {
                    tool = 15;
                    setDocumentToolMode(15);
                }
            } else if (source == this.inkAnnotationToolButton) {
                if (e2.getStateChange() == 1) {
                    tool = 16;
                    setDocumentToolMode(16);
                }
            } else if (source == this.freeTextAnnotationToolButton) {
                if (e2.getStateChange() == 1) {
                    tool = 17;
                    setDocumentToolMode(17);
                }
            } else if (source == this.textAnnotationToolButton || source == this.textAnnotationUtilityToolButton) {
                if (e2.getStateChange() == 1) {
                    tool = 18;
                    setDocumentToolMode(18);
                }
            } else if (source == this.facingPageViewNonContinuousButton) {
                if (e2.getStateChange() == 1) {
                    setPageViewMode(5, false);
                    doSetFocus = true;
                }
            } else if (source == this.facingPageViewContinuousButton) {
                if (e2.getStateChange() == 1) {
                    setPageViewMode(6, false);
                    doSetFocus = true;
                }
            } else if (source == this.singlePageViewNonContinuousButton) {
                if (e2.getStateChange() == 1) {
                    setPageViewMode(1, false);
                    doSetFocus = true;
                }
            } else if (source == this.singlePageViewContinuousButton && e2.getStateChange() == 1) {
                setPageViewMode(2, false);
                doSetFocus = true;
            }
            if (doSetFocus) {
                this.documentViewController.requestViewFocusInWindow();
            }
        } finally {
            setDisplayTool(tool);
        }
    }

    @Override // javax.swing.event.TreeSelectionListener
    public void valueChanged(TreeSelectionEvent e2) throws Exception {
        TreePath treePath;
        if (this.outlinesTree == null || (treePath = this.outlinesTree.getSelectionPath()) == null) {
            return;
        }
        OutlineItemTreeNode node = (OutlineItemTreeNode) treePath.getLastPathComponent();
        OutlineItem o2 = node.getOutlineItem();
        followOutlineItem(o2);
        this.outlinesTree.requestFocus();
    }

    @Override // java.awt.event.WindowListener
    public void windowActivated(WindowEvent e2) {
    }

    @Override // java.awt.event.WindowListener
    public void windowClosed(WindowEvent e2) {
    }

    @Override // java.awt.event.WindowListener
    public void windowClosing(WindowEvent e2) throws HeadlessException, IllegalArgumentException {
        WindowManagementCallback wc = this.windowManagementCallback;
        JFrame v2 = this.viewer;
        DocumentViewController viewControl = getDocumentViewController();
        Properties viewProperties = new Properties();
        viewProperties.setProperty(PropertiesManager.PROPERTY_DEFAULT_PAGEFIT, String.valueOf(viewControl.getFitMode()));
        viewProperties.setProperty("document.viewtype", String.valueOf(viewControl.getViewMode()));
        boolean cancelled = saveChangesDialog();
        if (!cancelled) {
            dispose();
            if (wc != null) {
                wc.disposeWindow(this, v2, viewProperties);
            }
        }
    }

    @Override // java.awt.event.WindowListener
    public void windowDeactivated(WindowEvent e2) {
    }

    @Override // java.awt.event.WindowListener
    public void windowDeiconified(WindowEvent e2) {
    }

    @Override // java.awt.event.WindowListener
    public void windowIconified(WindowEvent e2) {
    }

    @Override // java.awt.event.WindowListener
    public void windowOpened(WindowEvent e2) {
    }

    @Override // java.awt.dnd.DropTargetListener
    public void dragEnter(DropTargetDragEvent event) {
        if (!isDragAcceptable(event)) {
            event.rejectDrag();
        }
    }

    @Override // java.awt.dnd.DropTargetListener
    public void dragOver(DropTargetDragEvent event) {
    }

    @Override // java.awt.dnd.DropTargetListener
    public void dropActionChanged(DropTargetDragEvent event) {
        if (!isDragAcceptable(event)) {
            event.rejectDrag();
        }
    }

    @Override // java.awt.dnd.DropTargetListener
    public void drop(DropTargetDropEvent event) throws HeadlessException, IllegalArgumentException, InvalidDnDOperationException {
        try {
            if (!isDropAcceptable(event)) {
                event.rejectDrop();
                return;
            }
            event.acceptDrop(1);
            Transferable transferable = event.getTransferable();
            DataFlavor[] flavors = transferable.getTransferDataFlavors();
            for (DataFlavor dataFlavor : flavors) {
                if (dataFlavor.equals(DataFlavor.javaFileListFlavor)) {
                    List fileList = (List) transferable.getTransferData(dataFlavor);
                    for (Object aFileList : fileList) {
                        File file = (File) aFileList;
                        if (file.getName().toLowerCase().endsWith(DocumentViewComponent.PDF_EXTENSION)) {
                            openFileInSomeViewer(file);
                            ViewModel.setDefaultFile(file);
                        }
                    }
                } else if (dataFlavor.equals(DataFlavor.stringFlavor)) {
                    String s2 = (String) transferable.getTransferData(dataFlavor);
                    int startIndex = s2.toLowerCase().indexOf("http://");
                    int endIndex = s2.toLowerCase().indexOf(DocumentViewComponent.PDF_EXTENSION);
                    if (startIndex >= 0 && endIndex >= 0) {
                        String s3 = s2.substring(startIndex, endIndex + 4);
                        try {
                            URL url = new URL(s3);
                            openURLInSomeViewer(url);
                            ViewModel.setDefaultURL(s3);
                        } catch (MalformedURLException e2) {
                        }
                    }
                }
            }
            event.dropComplete(true);
        } catch (UnsupportedFlavorException ufe) {
            logger.log(Level.FINE, "Drag and drop not supported", (Throwable) ufe);
        } catch (IOException ioe) {
            logger.log(Level.FINE, "IO exception during file drop", (Throwable) ioe);
        }
    }

    @Override // java.awt.dnd.DropTargetListener
    public void dragExit(DropTargetEvent event) {
    }

    @Override // java.awt.event.KeyListener
    public void keyPressed(KeyEvent e2) {
        if (this.document == null) {
            return;
        }
        int c2 = e2.getKeyCode();
        int m2 = e2.getModifiers();
        if ((c2 == 83 && m2 == KeyEventConstants.MODIFIER_SAVE_AS) || ((c2 == 80 && m2 == KeyEventConstants.MODIFIER_PRINT_SETUP) || ((c2 == 80 && m2 == KeyEventConstants.MODIFIER_PRINT) || ((c2 == 49 && m2 == KeyEventConstants.MODIFIER_FIT_ACTUAL) || ((c2 == 50 && m2 == KeyEventConstants.MODIFIER_FIT_PAGE) || ((c2 == 51 && m2 == KeyEventConstants.MODIFIER_FIT_WIDTH) || ((c2 == 73 && m2 == KeyEventConstants.MODIFIER_ZOOM_IN) || ((c2 == 79 && m2 == KeyEventConstants.MODIFIER_ZOOM_OUT) || ((c2 == 76 && m2 == KeyEventConstants.MODIFIER_ROTATE_LEFT) || ((c2 == 82 && m2 == KeyEventConstants.MODIFIER_ROTATE_RIGHT) || ((c2 == 38 && m2 == KeyEventConstants.MODIFIER_FIRST_PAGE) || ((c2 == 37 && m2 == KeyEventConstants.MODIFIER_PREVIOUS_PAGE) || ((c2 == 39 && m2 == KeyEventConstants.MODIFIER_NEXT_PAGE) || ((c2 == 40 && m2 == KeyEventConstants.MODIFIER_LAST_PAGE) || ((c2 == 83 && m2 == KeyEventConstants.MODIFIER_SEARCH) || (c2 == 78 && m2 == KeyEventConstants.MODIFIER_GOTO)))))))))))))))) {
            int documentIcon = getDocumentViewToolMode();
            try {
                setDisplayTool(51);
                if (c2 == 83 && m2 == KeyEventConstants.MODIFIER_SAVE_AS) {
                    saveFile();
                } else if (c2 == 80 && m2 == KeyEventConstants.MODIFIER_PRINT_SETUP) {
                    showPrintSetupDialog();
                } else if (c2 == 80 && m2 == KeyEventConstants.MODIFIER_PRINT) {
                    print(true);
                } else if (c2 == 49 && m2 == KeyEventConstants.MODIFIER_FIT_ACTUAL) {
                    setPageFitMode(2, false);
                } else if (c2 == 50 && m2 == KeyEventConstants.MODIFIER_FIT_PAGE) {
                    setPageFitMode(3, false);
                } else if (c2 == 51 && m2 == KeyEventConstants.MODIFIER_FIT_WIDTH) {
                    setPageFitMode(4, false);
                } else if (c2 == 73 && m2 == KeyEventConstants.MODIFIER_ZOOM_IN) {
                    zoomIn();
                } else if (c2 == 79 && m2 == KeyEventConstants.MODIFIER_ZOOM_OUT) {
                    zoomOut();
                } else if (c2 == 76 && m2 == KeyEventConstants.MODIFIER_ROTATE_LEFT) {
                    rotateLeft();
                } else if (c2 == 82 && m2 == KeyEventConstants.MODIFIER_ROTATE_RIGHT) {
                    rotateRight();
                } else if (c2 == 38 && m2 == KeyEventConstants.MODIFIER_FIRST_PAGE) {
                    showPage(0);
                } else if (c2 == 37 && m2 == KeyEventConstants.MODIFIER_PREVIOUS_PAGE) {
                    DocumentView documentView = this.documentViewController.getDocumentView();
                    goToDeltaPage(-documentView.getPreviousPageIncrement());
                } else if (c2 == 39 && m2 == KeyEventConstants.MODIFIER_NEXT_PAGE) {
                    DocumentView documentView2 = this.documentViewController.getDocumentView();
                    goToDeltaPage(documentView2.getNextPageIncrement());
                } else if (c2 == 40 && m2 == KeyEventConstants.MODIFIER_LAST_PAGE) {
                    showPage(getPageTree().getNumberOfPages() - 1);
                } else if (c2 == 83 && m2 == KeyEventConstants.MODIFIER_SEARCH) {
                    showSearchPanel();
                } else if (c2 == 78 && m2 == KeyEventConstants.MODIFIER_GOTO) {
                    showPageSelectionDialog();
                }
            } finally {
                setDisplayTool(documentIcon);
            }
        }
    }

    @Override // java.awt.event.KeyListener
    public void keyReleased(KeyEvent e2) {
    }

    @Override // java.awt.event.KeyListener
    public void keyTyped(KeyEvent e2) {
        if (this.currentPageNumberTextField != null && e2.getSource() == this.currentPageNumberTextField) {
            char c2 = e2.getKeyChar();
            if (c2 == 27) {
                String fieldValue = this.currentPageNumberTextField.getText();
                String modelValue = Integer.toString(this.documentViewController.getCurrentPageDisplayValue());
                if (!fieldValue.equals(modelValue)) {
                    this.currentPageNumberTextField.setText(modelValue);
                }
            }
        }
    }

    @Override // java.beans.PropertyChangeListener
    public void propertyChange(PropertyChangeEvent evt) throws IllegalArgumentException {
        AnnotationComponent annotationComponent;
        Object newValue = evt.getNewValue();
        Object oldValue = evt.getOldValue();
        String propertyName = evt.getPropertyName();
        if (propertyName.equals(PropertyConstants.DOCUMENT_CURRENT_PAGE)) {
            if (this.currentPageNumberTextField != null && (newValue instanceof Integer)) {
                updateDocumentView();
                return;
            }
            return;
        }
        if (propertyName.equals(PropertyConstants.TEXT_SELECTED)) {
            boolean canExtract = havePermissionToExtractContent();
            setEnabled(this.copyMenuItem, canExtract);
            setEnabled(this.deselectAllMenuItem, canExtract);
            return;
        }
        if (propertyName.equals(PropertyConstants.TEXT_DESELECTED)) {
            boolean canExtract2 = havePermissionToExtractContent();
            setEnabled(this.copyMenuItem, false);
            setEnabled(this.deselectAllMenuItem, false);
            setEnabled(this.selectAllMenuItem, canExtract2);
            return;
        }
        if (propertyName.equals(PropertyConstants.TEXT_SELECT_ALL)) {
            boolean canExtract3 = havePermissionToExtractContent();
            setEnabled(this.selectAllMenuItem, false);
            setEnabled(this.deselectAllMenuItem, canExtract3);
            setEnabled(this.copyMenuItem, canExtract3);
            return;
        }
        if (propertyName.equals(PropertyConstants.ANNOTATION_SELECTED) || propertyName.equals(PropertyConstants.ANNOTATION_FOCUS_GAINED)) {
            setEnabled(this.deleteMenuItem, true);
            if (this.documentViewController.getToolMode() == 6 && (annotationComponent = (AnnotationComponent) newValue) != null && annotationComponent.getAnnotation() != null) {
                if (logger.isLoggable(Level.FINE)) {
                    logger.fine("selected annotation " + ((Object) annotationComponent));
                }
                showAnnotationPanel(annotationComponent);
                return;
            }
            return;
        }
        if (propertyName.equals(PropertyConstants.ANNOTATION_DESELECTED)) {
            if (this.documentViewController.getToolMode() == 6) {
                if (logger.isLoggable(Level.FINE)) {
                    logger.fine("Deselected current annotation");
                }
                setEnabled(this.deleteMenuItem, false);
                if (this.annotationPanel != null) {
                    this.annotationPanel.setEnabled(false);
                    return;
                }
                return;
            }
            return;
        }
        if (propertyName.equals(PropertyConstants.ANNOTATION_BOUNDS)) {
            if (this.documentViewController.getToolMode() == 6) {
                AnnotationState oldAnnotationState = (AnnotationState) oldValue;
                AnnotationState newAnnotationState = (AnnotationState) newValue;
                newAnnotationState.apply(newAnnotationState);
                newAnnotationState.restore();
                this.documentViewController.getDocumentViewModel().addMemento(oldAnnotationState, newAnnotationState);
            }
            reflectUndoCommands();
            return;
        }
        if (propertyName.equals(JSplitPane.LAST_DIVIDER_LOCATION_PROPERTY)) {
            JSplitPane sourceSplitPane = (JSplitPane) evt.getSource();
            int dividerLocation = ((Integer) evt.getNewValue()).intValue();
            if (sourceSplitPane.getDividerLocation() != dividerLocation && this.propertiesManager != null && dividerLocation > 5) {
                this.utilityAndDocumentSplitPaneLastDividerLocation = dividerLocation;
                this.propertiesManager.setInt(PropertiesManager.PROPERTY_DIVIDER_LOCATION, this.utilityAndDocumentSplitPaneLastDividerLocation);
            }
        }
    }
}
