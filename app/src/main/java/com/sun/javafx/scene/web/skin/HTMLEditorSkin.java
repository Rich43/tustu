package com.sun.javafx.scene.web.skin;

import com.sun.glass.ui.Clipboard;
import com.sun.javafx.application.PlatformImpl;
import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import com.sun.javafx.scene.control.skin.ColorPickerSkin;
import com.sun.javafx.scene.control.skin.FXVK;
import com.sun.javafx.scene.traversal.Algorithm;
import com.sun.javafx.scene.traversal.Direction;
import com.sun.javafx.scene.traversal.ParentTraversalEngine;
import com.sun.javafx.scene.traversal.TraversalContext;
import com.sun.javafx.scene.web.behavior.HTMLEditorBehavior;
import com.sun.javafx.webkit.Accessor;
import com.sun.org.apache.xalan.internal.templates.Constants;
import com.sun.webkit.WebPage;
import java.security.AccessController;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.css.StyleableProperty;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Orientation;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebView;
import javafx.util.Callback;
import javax.swing.plaf.nimbus.NimbusStyle;
import javax.swing.text.AbstractDocument;
import org.icepdf.core.util.PdfOps;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/web/skin/HTMLEditorSkin.class */
public class HTMLEditorSkin extends BehaviorSkinBase<HTMLEditor, HTMLEditorBehavior> {
    private GridPane gridPane;
    private ToolBar toolbar1;
    private ToolBar toolbar2;
    private Button cutButton;
    private Button copyButton;
    private Button pasteButton;
    private Button insertHorizontalRuleButton;
    private ToggleGroup alignmentToggleGroup;
    private ToggleButton alignLeftButton;
    private ToggleButton alignCenterButton;
    private ToggleButton alignRightButton;
    private ToggleButton alignJustifyButton;
    private ToggleButton bulletsButton;
    private ToggleButton numbersButton;
    private Button indentButton;
    private Button outdentButton;
    private ComboBox<String> formatComboBox;
    private Map<String, String> formatStyleMap;
    private Map<String, String> styleFormatMap;
    private ComboBox<String> fontFamilyComboBox;
    private ComboBox<String> fontSizeComboBox;
    private Map<String, String> fontSizeMap;
    private Map<String, String> sizeFontMap;
    private ToggleButton boldButton;
    private ToggleButton italicButton;
    private ToggleButton underlineButton;
    private ToggleButton strikethroughButton;
    private ColorPicker fgColorButton;
    private ColorPicker bgColorButton;
    private WebView webView;
    private WebPage webPage;
    private static final String CUT_COMMAND = "cut";
    private static final String COPY_COMMAND = "copy";
    private static final String PASTE_COMMAND = "paste";
    private static final String UNDO_COMMAND = "undo";
    private static final String REDO_COMMAND = "redo";
    private static final String INSERT_HORIZONTAL_RULE_COMMAND = "inserthorizontalrule";
    private static final String ALIGN_LEFT_COMMAND = "justifyleft";
    private static final String ALIGN_CENTER_COMMAND = "justifycenter";
    private static final String ALIGN_RIGHT_COMMAND = "justifyright";
    private static final String ALIGN_JUSTIFY_COMMAND = "justifyfull";
    private static final String BULLETS_COMMAND = "insertUnorderedList";
    private static final String NUMBERS_COMMAND = "insertOrderedList";
    private static final String INDENT_COMMAND = "indent";
    private static final String OUTDENT_COMMAND = "outdent";
    private static final String FORMAT_COMMAND = "formatblock";
    private static final String FONT_FAMILY_COMMAND = "fontname";
    private static final String FONT_SIZE_COMMAND = "fontsize";
    private static final String ITALIC_COMMAND = "italic";
    private static final String UNDERLINE_COMMAND = "underline";
    private static final String STRIKETHROUGH_COMMAND = "strikethrough";
    private static final String FOREGROUND_COLOR_COMMAND = "forecolor";
    private static final String BACKGROUND_COLOR_COMMAND = "backcolor";
    private static final String SIZE_XX_SMALL = "1";
    private static final String SIZE_XX_LARGE = "7";
    private static final String INSERT_NEW_LINE_COMMAND = "insertnewline";
    private static final String INSERT_TAB_COMMAND = "inserttab";
    private ParentTraversalEngine engine;
    private boolean resetToolbarState;
    private String cachedHTMLText;
    private ListChangeListener<Node> itemsListener;
    private ResourceBundle resources;
    private boolean enableAtomicityCheck;
    private int atomicityCount;
    private boolean isFirstRun;
    private static final int FONT_FAMILY_MENUBUTTON_WIDTH = 150;
    private static final int FONT_FAMILY_MENU_WIDTH = 100;
    private static final int FONT_SIZE_MENUBUTTON_WIDTH = 80;
    private static final Color DEFAULT_BG_COLOR = Color.WHITE;
    private static final Color DEFAULT_FG_COLOR = Color.BLACK;
    private static final String FORMAT_PARAGRAPH = "<p>";
    private static final String SIZE_SMALL = "3";
    private static final String FORMAT_HEADING_1 = "<h1>";
    private static final String BOLD_COMMAND = "bold";
    private static final String SIZE_X_LARGE = "6";
    private static final String FORMAT_HEADING_2 = "<h2>";
    private static final String SIZE_LARGE = "5";
    private static final String FORMAT_HEADING_3 = "<h3>";
    private static final String SIZE_MEDIUM = "4";
    private static final String FORMAT_HEADING_4 = "<h4>";
    private static final String FORMAT_HEADING_5 = "<h5>";
    private static final String SIZE_X_SMALL = "2";
    private static final String FORMAT_HEADING_6 = "<h6>";
    private static final String[][] DEFAULT_FORMAT_MAPPINGS = {new String[]{FORMAT_PARAGRAPH, "", SIZE_SMALL}, new String[]{FORMAT_HEADING_1, BOLD_COMMAND, SIZE_X_LARGE}, new String[]{FORMAT_HEADING_2, BOLD_COMMAND, SIZE_LARGE}, new String[]{FORMAT_HEADING_3, BOLD_COMMAND, SIZE_MEDIUM}, new String[]{FORMAT_HEADING_4, BOLD_COMMAND, SIZE_SMALL}, new String[]{FORMAT_HEADING_5, BOLD_COMMAND, SIZE_X_SMALL}, new String[]{FORMAT_HEADING_6, BOLD_COMMAND, "1"}};
    private static final String[] DEFAULT_WINDOWS_7_MAPPINGS = {"Windows 7", "Segoe UI", "12px", "", "120"};
    private static final String[][] DEFAULT_OS_MAPPINGS = {new String[]{"Windows XP", "Tahoma", "12px", "", "96"}, new String[]{"Windows Vista", "Segoe UI", "12px", "", "96"}, DEFAULT_WINDOWS_7_MAPPINGS, new String[]{"Mac OS X", "Lucida Grande", "12px", "", "72"}, new String[]{"Linux", "Lucida Sans", "12px", "", "96"}};
    private static final String DEFAULT_OS_FONT = getOSMappings()[1];
    private static PseudoClass CONTAINS_FOCUS_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("contains-focus");

    private static String[] getOSMappings() {
        String os = System.getProperty("os.name");
        for (int i2 = 0; i2 < DEFAULT_OS_MAPPINGS.length; i2++) {
            if (os.equals(DEFAULT_OS_MAPPINGS[i2][0])) {
                return DEFAULT_OS_MAPPINGS[i2];
            }
        }
        return DEFAULT_WINDOWS_7_MAPPINGS;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v6, types: [javafx.scene.Parent, javafx.scene.control.Control] */
    public HTMLEditorSkin(HTMLEditor htmlEditor) {
        super(htmlEditor, new HTMLEditorBehavior(htmlEditor));
        this.resetToolbarState = false;
        this.cachedHTMLText = "<html><head></head><body contenteditable=\"true\"></body></html>";
        this.itemsListener = c2 -> {
            while (c2.next()) {
                if (c2.getRemovedSize() > 0) {
                    for (Node n2 : c2.getList()) {
                        if (n2 instanceof WebView) {
                            this.webPage.dispose();
                        }
                    }
                }
            }
        };
        this.enableAtomicityCheck = false;
        this.atomicityCount = 0;
        this.isFirstRun = true;
        getChildren().clear();
        this.gridPane = new GridPane();
        this.gridPane.getStyleClass().add("grid");
        getChildren().addAll(this.gridPane);
        this.toolbar1 = new ToolBar();
        this.toolbar1.getStyleClass().add("top-toolbar");
        this.gridPane.add(this.toolbar1, 0, 0);
        this.toolbar2 = new ToolBar();
        this.toolbar2.getStyleClass().add("bottom-toolbar");
        this.gridPane.add(this.toolbar2, 0, 1);
        this.webView = new WebView();
        this.gridPane.add(this.webView, 0, 2);
        ColumnConstraints column = new ColumnConstraints();
        column.setHgrow(Priority.ALWAYS);
        this.gridPane.getColumnConstraints().add(column);
        this.webPage = Accessor.getPageFor(this.webView.getEngine());
        this.webView.addEventHandler(MouseEvent.MOUSE_RELEASED, event2 -> {
            Platform.runLater(new Runnable() { // from class: com.sun.javafx.scene.web.skin.HTMLEditorSkin.1
                @Override // java.lang.Runnable
                public void run() {
                    HTMLEditorSkin.this.enableAtomicityCheck = true;
                    HTMLEditorSkin.this.updateToolbarState(true);
                    HTMLEditorSkin.this.enableAtomicityCheck = false;
                }
            });
        });
        this.webView.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            applyTextFormatting();
            if (event.getCode() == KeyCode.CONTROL || event.getCode() == KeyCode.META) {
                return;
            }
            if (event.getCode() == KeyCode.TAB && !event.isControlDown()) {
                if (!event.isShiftDown()) {
                    if (getCommandState(BULLETS_COMMAND) || getCommandState(NUMBERS_COMMAND)) {
                        executeCommand("indent", null);
                        return;
                    } else {
                        executeCommand(INSERT_TAB_COMMAND, null);
                        return;
                    }
                }
                if (getCommandState(BULLETS_COMMAND) || getCommandState(NUMBERS_COMMAND)) {
                    executeCommand(OUTDENT_COMMAND, null);
                    return;
                }
                return;
            }
            if (this.fgColorButton == null || !this.fgColorButton.isShowing()) {
                if (this.bgColorButton != null && this.bgColorButton.isShowing()) {
                    return;
                }
                Platform.runLater(() -> {
                    if (this.webPage.getClientSelectedText().isEmpty()) {
                        if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.HOME || event.getCode() == KeyCode.END) {
                            updateToolbarState(true);
                        } else if (event.isControlDown() || event.isMetaDown()) {
                            if (event.getCode() == KeyCode.B) {
                                keyboardShortcuts(BOLD_COMMAND);
                            } else if (event.getCode() == KeyCode.I) {
                                keyboardShortcuts(ITALIC_COMMAND);
                            } else if (event.getCode() == KeyCode.U) {
                                keyboardShortcuts(UNDERLINE_COMMAND);
                            }
                            updateToolbarState(true);
                        } else {
                            this.resetToolbarState = event.getCode() == KeyCode.ENTER;
                            if (this.resetToolbarState && getCommandState(BOLD_COMMAND) != this.boldButton.selectedProperty().getValue2().booleanValue()) {
                                executeCommand(BOLD_COMMAND, this.boldButton.selectedProperty().getValue2().toString());
                            }
                            updateToolbarState(false);
                        }
                        this.resetToolbarState = false;
                        return;
                    }
                    if (event.isShiftDown()) {
                        if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.RIGHT) {
                            updateToolbarState(true);
                        }
                    }
                });
            }
        });
        this.webView.addEventHandler(KeyEvent.KEY_RELEASED, event3 -> {
            if (event3.getCode() == KeyCode.CONTROL || event3.getCode() == KeyCode.META) {
                return;
            }
            if (this.fgColorButton == null || !this.fgColorButton.isShowing()) {
                if (this.bgColorButton != null && this.bgColorButton.isShowing()) {
                    return;
                }
                Platform.runLater(() -> {
                    if (this.webPage.getClientSelectedText().isEmpty()) {
                        if (event3.getCode() == KeyCode.UP || event3.getCode() == KeyCode.DOWN || event3.getCode() == KeyCode.LEFT || event3.getCode() == KeyCode.RIGHT || event3.getCode() == KeyCode.HOME || event3.getCode() == KeyCode.END) {
                            updateToolbarState(true);
                        } else if (event3.isControlDown() || event3.isMetaDown()) {
                            if (event3.getCode() == KeyCode.B) {
                                keyboardShortcuts(BOLD_COMMAND);
                            } else if (event3.getCode() == KeyCode.I) {
                                keyboardShortcuts(ITALIC_COMMAND);
                            } else if (event3.getCode() == KeyCode.U) {
                                keyboardShortcuts(UNDERLINE_COMMAND);
                            }
                            updateToolbarState(true);
                        } else {
                            this.resetToolbarState = event3.getCode() == KeyCode.ENTER;
                            if (!this.resetToolbarState) {
                                updateToolbarState(false);
                            }
                        }
                        this.resetToolbarState = false;
                    }
                });
            }
        });
        ((HTMLEditor) getSkinnable()).focusedProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(new Runnable() { // from class: com.sun.javafx.scene.web.skin.HTMLEditorSkin.2
                @Override // java.lang.Runnable
                public void run() {
                    if (newValue.booleanValue()) {
                        HTMLEditorSkin.this.webView.requestFocus();
                    }
                }
            });
        });
        this.webView.focusedProperty().addListener((observable2, oldValue2, newValue2) -> {
            pseudoClassStateChanged(CONTAINS_FOCUS_PSEUDOCLASS_STATE, newValue2.booleanValue());
            Platform.runLater(new Runnable() { // from class: com.sun.javafx.scene.web.skin.HTMLEditorSkin.3
                /* JADX WARN: Multi-variable type inference failed */
                @Override // java.lang.Runnable
                public void run() {
                    HTMLEditorSkin.this.updateToolbarState(true);
                    if (PlatformImpl.isSupported(ConditionalFeature.VIRTUAL_KEYBOARD)) {
                        Scene scene = ((HTMLEditor) HTMLEditorSkin.this.getSkinnable()).getScene();
                        if (newValue2.booleanValue()) {
                            FXVK.attach(HTMLEditorSkin.this.webView);
                        } else if (scene == null || scene.getWindow() == null || !scene.getWindow().isFocused() || !(scene.getFocusOwner() instanceof TextInputControl)) {
                            FXVK.detach();
                        }
                    }
                }
            });
        });
        this.webView.getEngine().getLoadWorker().workDoneProperty().addListener((observable3, oldValue3, newValue3) -> {
            Platform.runLater(() -> {
                this.webView.requestLayout();
            });
            double totalWork = this.webView.getEngine().getLoadWorker().getTotalWork();
            if (newValue3.doubleValue() == totalWork) {
                this.cachedHTMLText = null;
                Platform.runLater(() -> {
                    setContentEditable(true);
                    updateToolbarState(true);
                    updateNodeOrientation();
                });
            }
        });
        enableToolbar(true);
        setHTMLText(this.cachedHTMLText);
        this.engine = new ParentTraversalEngine(getSkinnable(), new Algorithm() { // from class: com.sun.javafx.scene.web.skin.HTMLEditorSkin.4
            @Override // com.sun.javafx.scene.traversal.Algorithm
            public Node select(Node owner, Direction dir, TraversalContext context) {
                return HTMLEditorSkin.this.cutButton;
            }

            @Override // com.sun.javafx.scene.traversal.Algorithm
            public Node selectFirst(TraversalContext context) {
                return HTMLEditorSkin.this.cutButton;
            }

            @Override // com.sun.javafx.scene.traversal.Algorithm
            public Node selectLast(TraversalContext context) {
                return HTMLEditorSkin.this.cutButton;
            }
        });
        ((HTMLEditor) getSkinnable()).setImpl_traversalEngine(this.engine);
        this.webView.setFocusTraversable(true);
        this.gridPane.getChildren().addListener(this.itemsListener);
    }

    public final String getHTMLText() {
        return this.cachedHTMLText != null ? this.cachedHTMLText : this.webPage.getHtml(this.webPage.getMainFrame());
    }

    public final void setHTMLText(String htmlText) {
        this.cachedHTMLText = htmlText;
        this.webPage.load(this.webPage.getMainFrame(), htmlText, Clipboard.HTML_TYPE);
        Platform.runLater(() -> {
            updateToolbarState(true);
        });
    }

    private void populateToolbars() {
        this.resources = ResourceBundle.getBundle(HTMLEditorSkin.class.getName());
        this.cutButton = addButton(this.toolbar1, this.resources.getString("cutIcon"), this.resources.getString(CUT_COMMAND), CUT_COMMAND, "html-editor-cut");
        this.copyButton = addButton(this.toolbar1, this.resources.getString("copyIcon"), this.resources.getString("copy"), "copy", "html-editor-copy");
        this.pasteButton = addButton(this.toolbar1, this.resources.getString("pasteIcon"), this.resources.getString(PASTE_COMMAND), PASTE_COMMAND, "html-editor-paste");
        this.toolbar1.getItems().add(new Separator(Orientation.VERTICAL));
        this.alignmentToggleGroup = new ToggleGroup();
        this.alignLeftButton = addToggleButton(this.toolbar1, this.alignmentToggleGroup, this.resources.getString("alignLeftIcon"), this.resources.getString("alignLeft"), ALIGN_LEFT_COMMAND, "html-editor-align-left");
        this.alignCenterButton = addToggleButton(this.toolbar1, this.alignmentToggleGroup, this.resources.getString("alignCenterIcon"), this.resources.getString("alignCenter"), ALIGN_CENTER_COMMAND, "html-editor-align-center");
        this.alignRightButton = addToggleButton(this.toolbar1, this.alignmentToggleGroup, this.resources.getString("alignRightIcon"), this.resources.getString("alignRight"), ALIGN_RIGHT_COMMAND, "html-editor-align-right");
        this.alignJustifyButton = addToggleButton(this.toolbar1, this.alignmentToggleGroup, this.resources.getString("alignJustifyIcon"), this.resources.getString("alignJustify"), ALIGN_JUSTIFY_COMMAND, "html-editor-align-justify");
        this.toolbar1.getItems().add(new Separator(Orientation.VERTICAL));
        this.outdentButton = addButton(this.toolbar1, this.resources.getString("outdentIcon"), this.resources.getString(OUTDENT_COMMAND), OUTDENT_COMMAND, "html-editor-outdent");
        if (this.outdentButton.getGraphic() != null) {
            this.outdentButton.getGraphic().setNodeOrientation(NodeOrientation.INHERIT);
        }
        this.indentButton = addButton(this.toolbar1, this.resources.getString("indentIcon"), this.resources.getString("indent"), "indent", "html-editor-indent");
        if (this.indentButton.getGraphic() != null) {
            this.indentButton.getGraphic().setNodeOrientation(NodeOrientation.INHERIT);
        }
        this.toolbar1.getItems().add(new Separator(Orientation.VERTICAL));
        ToggleGroup listStyleToggleGroup = new ToggleGroup();
        this.bulletsButton = addToggleButton(this.toolbar1, listStyleToggleGroup, this.resources.getString("bulletsIcon"), this.resources.getString("bullets"), BULLETS_COMMAND, "html-editor-bullets");
        if (this.bulletsButton.getGraphic() != null) {
            this.bulletsButton.getGraphic().setNodeOrientation(NodeOrientation.INHERIT);
        }
        this.numbersButton = addToggleButton(this.toolbar1, listStyleToggleGroup, this.resources.getString("numbersIcon"), this.resources.getString("numbers"), NUMBERS_COMMAND, "html-editor-numbers");
        this.toolbar1.getItems().add(new Separator(Orientation.VERTICAL));
        this.formatComboBox = new ComboBox<>();
        this.formatComboBox.getStyleClass().add("font-menu-button");
        this.formatComboBox.setFocusTraversable(false);
        this.formatComboBox.setMinWidth(Double.NEGATIVE_INFINITY);
        this.toolbar2.getItems().add(this.formatComboBox);
        this.formatStyleMap = new HashMap();
        this.styleFormatMap = new HashMap();
        createFormatMenuItem(FORMAT_PARAGRAPH, this.resources.getString(AbstractDocument.ParagraphElementName));
        Platform.runLater(() -> {
            this.formatComboBox.setValue(this.resources.getString(AbstractDocument.ParagraphElementName));
        });
        createFormatMenuItem(FORMAT_HEADING_1, this.resources.getString("heading1"));
        createFormatMenuItem(FORMAT_HEADING_2, this.resources.getString("heading2"));
        createFormatMenuItem(FORMAT_HEADING_3, this.resources.getString("heading3"));
        createFormatMenuItem(FORMAT_HEADING_4, this.resources.getString("heading4"));
        createFormatMenuItem(FORMAT_HEADING_5, this.resources.getString("heading5"));
        createFormatMenuItem(FORMAT_HEADING_6, this.resources.getString("heading6"));
        this.formatComboBox.setTooltip(new Tooltip(this.resources.getString(Constants.ATTRNAME_FORMAT)));
        this.formatComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                this.formatComboBox.setValue(null);
                return;
            }
            String formatValue = this.formatStyleMap.get(newValue);
            executeCommand(FORMAT_COMMAND, formatValue);
            updateToolbarState(false);
            for (int i2 = 0; i2 < DEFAULT_FORMAT_MAPPINGS.length; i2++) {
                String[] mapping = DEFAULT_FORMAT_MAPPINGS[i2];
                if (mapping[0].equalsIgnoreCase(formatValue)) {
                    executeCommand(FONT_SIZE_COMMAND, mapping[2]);
                    updateToolbarState(false);
                    return;
                }
            }
        });
        this.fontFamilyComboBox = new ComboBox<>();
        this.fontFamilyComboBox.getStyleClass().add("font-menu-button");
        this.fontFamilyComboBox.setMinWidth(150.0d);
        this.fontFamilyComboBox.setPrefWidth(150.0d);
        this.fontFamilyComboBox.setMaxWidth(150.0d);
        this.fontFamilyComboBox.setFocusTraversable(false);
        this.fontFamilyComboBox.setTooltip(new Tooltip(this.resources.getString("fontFamily")));
        this.toolbar2.getItems().add(this.fontFamilyComboBox);
        this.fontFamilyComboBox.getProperties().put("comboBoxRowsToMeasureWidth", 0);
        this.fontFamilyComboBox.setCellFactory(new Callback<ListView<String>, ListCell<String>>() { // from class: com.sun.javafx.scene.web.skin.HTMLEditorSkin.5
            @Override // javafx.util.Callback
            public ListCell<String> call(ListView<String> param) {
                ListCell<String> cell = new ListCell<String>() { // from class: com.sun.javafx.scene.web.skin.HTMLEditorSkin.5.1
                    @Override // javafx.scene.control.Cell
                    public void updateItem(String item, boolean empty) {
                        super.updateItem((AnonymousClass1) item, empty);
                        if (item != null) {
                            setText(item);
                            setFont(new Font(item, 12.0d));
                        }
                    }
                };
                cell.setMinWidth(100.0d);
                cell.setPrefWidth(100.0d);
                cell.setMaxWidth(100.0d);
                return cell;
            }
        });
        Platform.runLater(() -> {
            ObservableList<String> fonts = FXCollections.observableArrayList(Font.getFamilies());
            for (String fontFamily : fonts) {
                if (DEFAULT_OS_FONT.equals(fontFamily)) {
                    this.fontFamilyComboBox.setValue(fontFamily);
                }
                this.fontFamilyComboBox.setItems(fonts);
            }
        });
        this.fontFamilyComboBox.valueProperty().addListener((observable2, oldValue2, newValue2) -> {
            executeCommand(FONT_FAMILY_COMMAND, newValue2);
        });
        this.fontSizeComboBox = new ComboBox<>();
        this.fontSizeComboBox.getStyleClass().add("font-menu-button");
        this.fontSizeComboBox.setFocusTraversable(false);
        this.toolbar2.getItems().add(this.fontSizeComboBox);
        this.fontSizeMap = new HashMap();
        this.sizeFontMap = new HashMap();
        createFontSizeMenuItem("1", this.resources.getString("extraExtraSmall"));
        createFontSizeMenuItem(SIZE_X_SMALL, this.resources.getString("extraSmall"));
        createFontSizeMenuItem(SIZE_SMALL, this.resources.getString(NimbusStyle.SMALL_KEY));
        Platform.runLater(() -> {
            this.fontSizeComboBox.setValue(this.resources.getString(NimbusStyle.SMALL_KEY));
        });
        createFontSizeMenuItem(SIZE_MEDIUM, this.resources.getString("medium"));
        createFontSizeMenuItem(SIZE_LARGE, this.resources.getString(NimbusStyle.LARGE_KEY));
        createFontSizeMenuItem(SIZE_X_LARGE, this.resources.getString("extraLarge"));
        createFontSizeMenuItem(SIZE_XX_LARGE, this.resources.getString("extraExtraLarge"));
        this.fontSizeComboBox.setTooltip(new Tooltip(this.resources.getString("fontSize")));
        this.fontSizeComboBox.setCellFactory(new Callback<ListView<String>, ListCell<String>>() { // from class: com.sun.javafx.scene.web.skin.HTMLEditorSkin.6
            @Override // javafx.util.Callback
            public ListCell<String> call(ListView<String> param) {
                ListCell<String> cell = new ListCell<String>() { // from class: com.sun.javafx.scene.web.skin.HTMLEditorSkin.6.1
                    /* JADX WARN: Multi-variable type inference failed */
                    @Override // javafx.scene.control.Cell
                    public void updateItem(String item, boolean empty) {
                        super.updateItem((AnonymousClass1) item, empty);
                        if (item != null) {
                            setText(item);
                            String size = item.replaceFirst("[^0-9.].*$", "");
                            setFont(new Font((String) HTMLEditorSkin.this.fontFamilyComboBox.getValue(), Double.valueOf(size).doubleValue()));
                        }
                    }
                };
                return cell;
            }
        });
        this.fontSizeComboBox.valueProperty().addListener((observable3, oldValue3, newValue3) -> {
            Object fontSizeValue = getCommandValue(FONT_SIZE_COMMAND);
            if (!newValue3.equals(fontSizeValue)) {
                executeCommand(FONT_SIZE_COMMAND, this.fontSizeMap.get(newValue3));
            }
        });
        this.toolbar2.getItems().add(new Separator(Orientation.VERTICAL));
        this.boldButton = addToggleButton(this.toolbar2, null, this.resources.getString("boldIcon"), this.resources.getString(BOLD_COMMAND), BOLD_COMMAND, "html-editor-bold");
        this.boldButton.setOnAction(event1 -> {
            if (FORMAT_PARAGRAPH.equals(this.formatStyleMap.get(this.formatComboBox.getValue()))) {
                executeCommand(BOLD_COMMAND, this.boldButton.selectedProperty().getValue2().toString());
            }
        });
        this.italicButton = addToggleButton(this.toolbar2, null, this.resources.getString("italicIcon"), this.resources.getString(ITALIC_COMMAND), ITALIC_COMMAND, "html-editor-italic");
        this.underlineButton = addToggleButton(this.toolbar2, null, this.resources.getString("underlineIcon"), this.resources.getString(UNDERLINE_COMMAND), UNDERLINE_COMMAND, "html-editor-underline");
        this.strikethroughButton = addToggleButton(this.toolbar2, null, this.resources.getString("strikethroughIcon"), this.resources.getString(STRIKETHROUGH_COMMAND), STRIKETHROUGH_COMMAND, "html-editor-strike");
        this.toolbar2.getItems().add(new Separator(Orientation.VERTICAL));
        this.insertHorizontalRuleButton = addButton(this.toolbar2, this.resources.getString("insertHorizontalRuleIcon"), this.resources.getString("insertHorizontalRule"), INSERT_HORIZONTAL_RULE_COMMAND, "html-editor-hr");
        this.insertHorizontalRuleButton.setOnAction(event -> {
            executeCommand(INSERT_NEW_LINE_COMMAND, null);
            executeCommand(INSERT_HORIZONTAL_RULE_COMMAND, null);
            updateToolbarState(false);
        });
        this.fgColorButton = new ColorPicker();
        this.fgColorButton.getStyleClass().add("html-editor-foreground");
        this.fgColorButton.setFocusTraversable(false);
        this.toolbar1.getItems().add(this.fgColorButton);
        this.fgColorButton.applyCss();
        ColorPickerSkin fgColorPickerSkin = (ColorPickerSkin) this.fgColorButton.getSkin();
        String fgIcon = (String) AccessController.doPrivileged(() -> {
            return HTMLEditorSkin.class.getResource(this.resources.getString("foregroundColorIcon")).toString();
        });
        ((StyleableProperty) fgColorPickerSkin.imageUrlProperty()).applyStyle(null, fgIcon);
        this.fgColorButton.setValue(DEFAULT_FG_COLOR);
        this.fgColorButton.setTooltip(new Tooltip(this.resources.getString("foregroundColor")));
        this.fgColorButton.setOnAction(ev1 -> {
            Color newValue4 = this.fgColorButton.getValue();
            if (newValue4 != null) {
                executeCommand(FOREGROUND_COLOR_COMMAND, colorValueToHex(newValue4));
                this.fgColorButton.hide();
            }
        });
        this.bgColorButton = new ColorPicker();
        this.bgColorButton.getStyleClass().add("html-editor-background");
        this.bgColorButton.setFocusTraversable(false);
        this.toolbar1.getItems().add(this.bgColorButton);
        this.bgColorButton.applyCss();
        ColorPickerSkin bgColorPickerSkin = (ColorPickerSkin) this.bgColorButton.getSkin();
        String bgIcon = (String) AccessController.doPrivileged(() -> {
            return HTMLEditorSkin.class.getResource(this.resources.getString("backgroundColorIcon")).toString();
        });
        ((StyleableProperty) bgColorPickerSkin.imageUrlProperty()).applyStyle(null, bgIcon);
        this.bgColorButton.setValue(DEFAULT_BG_COLOR);
        this.bgColorButton.setTooltip(new Tooltip(this.resources.getString("backgroundColor")));
        this.bgColorButton.setOnAction(ev -> {
            Color newValue4 = this.bgColorButton.getValue();
            if (newValue4 != null) {
                executeCommand(BACKGROUND_COLOR_COMMAND, colorValueToHex(newValue4));
                this.bgColorButton.hide();
            }
        });
    }

    private String colorValueToHex(Color c2) {
        return String.format((Locale) null, "#%02x%02x%02x", Long.valueOf(Math.round(c2.getRed() * 255.0d)), Long.valueOf(Math.round(c2.getGreen() * 255.0d)), Long.valueOf(Math.round(c2.getBlue() * 255.0d)));
    }

    private Button addButton(ToolBar toolbar, String iconName, String tooltipText, String command, String styleClass) {
        Button button = new Button();
        button.setFocusTraversable(false);
        button.getStyleClass().add(styleClass);
        toolbar.getItems().add(button);
        Image icon = (Image) AccessController.doPrivileged(() -> {
            return new Image(HTMLEditorSkin.class.getResource(iconName).toString());
        });
        ((StyleableProperty) button.graphicProperty()).applyStyle(null, new ImageView(icon));
        button.setTooltip(new Tooltip(tooltipText));
        button.setOnAction(event -> {
            executeCommand(command, null);
            updateToolbarState(false);
        });
        return button;
    }

    private ToggleButton addToggleButton(ToolBar toolbar, ToggleGroup toggleGroup, String iconName, String tooltipText, String command, String styleClass) {
        ToggleButton toggleButton = new ToggleButton();
        toggleButton.setUserData(command);
        toggleButton.setFocusTraversable(false);
        toggleButton.getStyleClass().add(styleClass);
        toolbar.getItems().add(toggleButton);
        if (toggleGroup != null) {
            toggleButton.setToggleGroup(toggleGroup);
        }
        Image icon = (Image) AccessController.doPrivileged(() -> {
            return new Image(HTMLEditorSkin.class.getResource(iconName).toString());
        });
        ((StyleableProperty) toggleButton.graphicProperty()).applyStyle(null, new ImageView(icon));
        toggleButton.setTooltip(new Tooltip(tooltipText));
        if (!BOLD_COMMAND.equals(command)) {
            toggleButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (getCommandState(command) != newValue.booleanValue()) {
                    executeCommand(command, null);
                }
            });
        }
        return toggleButton;
    }

    private void createFormatMenuItem(String formatValue, String label) {
        this.formatComboBox.getItems().add(label);
        this.formatStyleMap.put(label, formatValue);
        this.styleFormatMap.put(formatValue, label);
    }

    private void createFontSizeMenuItem(String fontSizeValue, String label) {
        this.fontSizeComboBox.getItems().add(label);
        this.fontSizeMap.put(label, fontSizeValue);
        this.sizeFontMap.put(fontSizeValue, label);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void updateNodeOrientation() {
        NodeOrientation orientation = ((HTMLEditor) getSkinnable()).getEffectiveNodeOrientation();
        HTMLDocument htmlDocument = (HTMLDocument) this.webPage.getDocument(this.webPage.getMainFrame());
        HTMLElement htmlDocumentElement = (HTMLElement) htmlDocument.getDocumentElement();
        if (htmlDocumentElement.getAttribute("dir") == null) {
            htmlDocumentElement.setAttribute("dir", orientation == NodeOrientation.RIGHT_TO_LEFT ? "rtl" : "ltr");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateToolbarState(boolean updateAlignment) {
        int i2;
        if (!this.webView.isFocused()) {
            return;
        }
        this.atomicityCount++;
        this.copyButton.setDisable(!isCommandEnabled(CUT_COMMAND));
        this.cutButton.setDisable(!isCommandEnabled("copy"));
        this.pasteButton.setDisable(!isCommandEnabled(PASTE_COMMAND));
        this.insertHorizontalRuleButton.setDisable(!isCommandEnabled(INSERT_HORIZONTAL_RULE_COMMAND));
        if (updateAlignment) {
            this.alignLeftButton.setDisable(!isCommandEnabled(ALIGN_LEFT_COMMAND));
            this.alignLeftButton.setSelected(getCommandState(ALIGN_LEFT_COMMAND));
            this.alignCenterButton.setDisable(!isCommandEnabled(ALIGN_CENTER_COMMAND));
            this.alignCenterButton.setSelected(getCommandState(ALIGN_CENTER_COMMAND));
            this.alignRightButton.setDisable(!isCommandEnabled(ALIGN_RIGHT_COMMAND));
            this.alignRightButton.setSelected(getCommandState(ALIGN_RIGHT_COMMAND));
            this.alignJustifyButton.setDisable(!isCommandEnabled(ALIGN_JUSTIFY_COMMAND));
            this.alignJustifyButton.setSelected(getCommandState(ALIGN_JUSTIFY_COMMAND));
        } else if (this.alignmentToggleGroup.getSelectedToggle() != null) {
            String command = this.alignmentToggleGroup.getSelectedToggle().getUserData().toString();
            if (isCommandEnabled(command) && !getCommandState(command)) {
                executeCommand(command, null);
            }
        }
        if (this.alignmentToggleGroup.getSelectedToggle() == null) {
            this.alignmentToggleGroup.selectToggle(this.alignLeftButton);
        }
        this.bulletsButton.setDisable(!isCommandEnabled(BULLETS_COMMAND));
        this.bulletsButton.setSelected(getCommandState(BULLETS_COMMAND));
        this.numbersButton.setDisable(!isCommandEnabled(NUMBERS_COMMAND));
        this.numbersButton.setSelected(getCommandState(NUMBERS_COMMAND));
        this.indentButton.setDisable(!isCommandEnabled("indent"));
        this.outdentButton.setDisable(!isCommandEnabled(OUTDENT_COMMAND));
        this.formatComboBox.setDisable(!isCommandEnabled(FORMAT_COMMAND));
        String formatValue = getCommandValue(FORMAT_COMMAND);
        if (formatValue != null) {
            String htmlTag = "<" + formatValue + ">";
            String comboFormatValue = this.styleFormatMap.get(htmlTag);
            String format = this.formatComboBox.getValue();
            if (this.resetToolbarState || htmlTag.equals("<>") || htmlTag.equalsIgnoreCase("<div>") || htmlTag.equalsIgnoreCase("<blockquote>")) {
                this.formatComboBox.setValue(this.resources.getString(AbstractDocument.ParagraphElementName));
            } else if (format != null && !format.equalsIgnoreCase(comboFormatValue)) {
                this.formatComboBox.setValue(comboFormatValue);
            }
        }
        this.fontFamilyComboBox.setDisable(!isCommandEnabled(FONT_FAMILY_COMMAND));
        String fontFamilyValue = getCommandValue(FONT_FAMILY_COMMAND);
        if (fontFamilyValue != null) {
            String fontFamilyStr = fontFamilyValue;
            if (fontFamilyStr.startsWith(PdfOps.SINGLE_QUOTE_TOKEN)) {
                fontFamilyStr = fontFamilyStr.substring(1);
            }
            if (fontFamilyStr.endsWith(PdfOps.SINGLE_QUOTE_TOKEN)) {
                fontFamilyStr = fontFamilyStr.substring(0, fontFamilyStr.length() - 1);
            }
            Object selectedFont = this.fontFamilyComboBox.getValue();
            if ((selectedFont instanceof String) && !selectedFont.equals(fontFamilyStr)) {
                ObservableList<String> fontFamilyItems = this.fontFamilyComboBox.getItems();
                String selectedComboFont = null;
                Iterator<String> it = fontFamilyItems.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    String comboFontFamilyValue = it.next();
                    if (comboFontFamilyValue.equals(fontFamilyStr)) {
                        selectedComboFont = comboFontFamilyValue;
                        break;
                    } else if (comboFontFamilyValue.equals(DEFAULT_OS_FONT) && fontFamilyStr.equals(java.awt.Font.DIALOG)) {
                        selectedComboFont = comboFontFamilyValue;
                        break;
                    }
                }
                if (selectedComboFont != null) {
                    this.fontFamilyComboBox.setValue(selectedComboFont);
                }
            }
        }
        this.fontSizeComboBox.setDisable(!isCommandEnabled(FONT_SIZE_COMMAND));
        String fontSizeValue = getCommandValue(FONT_SIZE_COMMAND);
        if (this.resetToolbarState && fontSizeValue == null) {
            this.fontSizeComboBox.setValue(this.sizeFontMap.get(SIZE_SMALL));
        } else if (fontSizeValue != null) {
            if (!this.fontSizeComboBox.getValue().equals(this.sizeFontMap.get(fontSizeValue))) {
                this.fontSizeComboBox.setValue(this.sizeFontMap.get(fontSizeValue));
            }
        } else if (this.fontSizeComboBox.getValue() == null || !this.fontSizeComboBox.getValue().equals(this.sizeFontMap.get(SIZE_SMALL))) {
            this.fontSizeComboBox.setValue(this.sizeFontMap.get(SIZE_SMALL));
        }
        this.boldButton.setDisable(!isCommandEnabled(BOLD_COMMAND));
        this.boldButton.setSelected(getCommandState(BOLD_COMMAND));
        this.italicButton.setDisable(!isCommandEnabled(ITALIC_COMMAND));
        this.italicButton.setSelected(getCommandState(ITALIC_COMMAND));
        this.underlineButton.setDisable(!isCommandEnabled(UNDERLINE_COMMAND));
        this.underlineButton.setSelected(getCommandState(UNDERLINE_COMMAND));
        this.strikethroughButton.setDisable(!isCommandEnabled(STRIKETHROUGH_COMMAND));
        this.strikethroughButton.setSelected(getCommandState(STRIKETHROUGH_COMMAND));
        this.fgColorButton.setDisable(!isCommandEnabled(FOREGROUND_COLOR_COMMAND));
        String foregroundColorValue = getCommandValue(FOREGROUND_COLOR_COMMAND);
        if (foregroundColorValue != null) {
            this.fgColorButton.setValue(getColor(foregroundColorValue));
        }
        this.bgColorButton.setDisable(!isCommandEnabled(BACKGROUND_COLOR_COMMAND));
        String backgroundColorValue = getCommandValue(BACKGROUND_COLOR_COMMAND);
        if (backgroundColorValue != null) {
            this.bgColorButton.setValue(getColor(backgroundColorValue));
        }
        if (this.atomicityCount == 0) {
            i2 = 0;
        } else {
            int i3 = this.atomicityCount - 1;
            i2 = i3;
            this.atomicityCount = i3;
        }
        this.atomicityCount = i2;
    }

    private void enableToolbar(boolean enable) {
        Platform.runLater(() -> {
            if (this.copyButton == null) {
                return;
            }
            if (enable) {
                this.copyButton.setDisable(!isCommandEnabled("copy"));
                this.cutButton.setDisable(!isCommandEnabled(CUT_COMMAND));
                this.pasteButton.setDisable(!isCommandEnabled(PASTE_COMMAND));
            } else {
                this.copyButton.setDisable(true);
                this.cutButton.setDisable(true);
                this.pasteButton.setDisable(true);
            }
            this.insertHorizontalRuleButton.setDisable(!enable);
            this.alignLeftButton.setDisable(!enable);
            this.alignCenterButton.setDisable(!enable);
            this.alignRightButton.setDisable(!enable);
            this.alignJustifyButton.setDisable(!enable);
            this.bulletsButton.setDisable(!enable);
            this.numbersButton.setDisable(!enable);
            this.indentButton.setDisable(!enable);
            this.outdentButton.setDisable(!enable);
            this.formatComboBox.setDisable(!enable);
            this.fontFamilyComboBox.setDisable(!enable);
            this.fontSizeComboBox.setDisable(!enable);
            this.boldButton.setDisable(!enable);
            this.italicButton.setDisable(!enable);
            this.underlineButton.setDisable(!enable);
            this.strikethroughButton.setDisable(!enable);
            this.fgColorButton.setDisable(!enable);
            this.bgColorButton.setDisable(!enable);
        });
    }

    private boolean executeCommand(String command, String value) {
        if (!this.enableAtomicityCheck || (this.enableAtomicityCheck && this.atomicityCount == 0)) {
            return this.webPage.executeCommand(command, value);
        }
        return false;
    }

    private boolean isCommandEnabled(String command) {
        return this.webPage.queryCommandEnabled(command);
    }

    private void setContentEditable(boolean b2) {
        HTMLDocument htmlDocument = (HTMLDocument) this.webPage.getDocument(this.webPage.getMainFrame());
        HTMLElement htmlDocumentElement = (HTMLElement) htmlDocument.getDocumentElement();
        HTMLElement htmlBodyElement = (HTMLElement) htmlDocumentElement.getElementsByTagName("body").item(0);
        htmlBodyElement.setAttribute("contenteditable", Boolean.toString(b2));
    }

    private boolean getCommandState(String command) {
        return this.webPage.queryCommandState(command);
    }

    private String getCommandValue(String command) {
        return this.webPage.queryCommandValue(command);
    }

    private Color getColor(String value) {
        Color color = Color.web(value);
        if (color.equals(Color.TRANSPARENT)) {
            color = Color.WHITE;
        }
        return color;
    }

    private void applyTextFormatting() {
        if (!getCommandState(BULLETS_COMMAND) && !getCommandState(NUMBERS_COMMAND) && this.webPage.getClientCommittedTextLength() == 0) {
            String format = this.formatStyleMap.get(this.formatComboBox.getValue());
            String font = this.fontFamilyComboBox.getValue().toString();
            executeCommand(FORMAT_COMMAND, format);
            executeCommand(FONT_FAMILY_COMMAND, font);
        }
    }

    public void keyboardShortcuts(String name) {
        if (BOLD_COMMAND.equals(name)) {
            this.boldButton.fire();
        } else if (ITALIC_COMMAND.equals(name)) {
            this.italicButton.setSelected(!this.italicButton.isSelected());
        } else if (UNDERLINE_COMMAND.equals(name)) {
            this.underlineButton.setSelected(!this.underlineButton.isSelected());
        }
    }

    @Override // javafx.scene.control.SkinBase
    protected void layoutChildren(double x2, double y2, double w2, double h2) {
        if (this.isFirstRun) {
            populateToolbars();
            this.isFirstRun = false;
        }
        super.layoutChildren(x2, y2, w2, h2);
        double toolbarWidth = Math.max(this.toolbar1.prefWidth(-1.0d), this.toolbar2.prefWidth(-1.0d));
        this.toolbar1.setMinWidth(toolbarWidth);
        this.toolbar1.setPrefWidth(toolbarWidth);
        this.toolbar2.setMinWidth(toolbarWidth);
        this.toolbar2.setPrefWidth(toolbarWidth);
    }

    public void print(PrinterJob job) {
        this.webView.getEngine().print(job);
    }
}
