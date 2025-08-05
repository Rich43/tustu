package javax.swing.plaf.nimbus;

import com.sun.imageio.plugins.jpeg.JPEG;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.Painter;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.DimensionUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.InsetsUIResource;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.nimbus.AbstractRegionPainter;
import javax.swing.plaf.nimbus.DerivedColor;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthStyle;
import sun.font.FontUtilities;
import sun.swing.plaf.synth.DefaultSynthStyle;

/* loaded from: rt.jar:javax/swing/plaf/nimbus/NimbusDefaults.class */
final class NimbusDefaults {
    private Map<String, Region> registeredRegions = new HashMap();
    private Map<JComponent, Map<Region, SynthStyle>> overridesCache = new WeakHashMap();
    private ColorTree colorTree = new ColorTree();
    private DefaultsListener defaultsListener = new DefaultsListener();
    private Map<DerivedColor, DerivedColor> derivedColors = new HashMap();

    /* renamed from: m, reason: collision with root package name */
    private Map<Region, List<LazyStyle>> f12828m = new HashMap();
    private FontUIResource defaultFont = FontUtilities.getFontConfigFUIR("sans", 0, 12);
    private DefaultSynthStyle defaultStyle = new DefaultSynthStyle();

    void initialize() {
        UIManager.addPropertyChangeListener(this.defaultsListener);
        UIManager.getDefaults().addPropertyChangeListener(this.colorTree);
    }

    void uninitialize() {
        UIManager.removePropertyChangeListener(this.defaultsListener);
        UIManager.getDefaults().removePropertyChangeListener(this.colorTree);
    }

    NimbusDefaults() {
        this.defaultStyle.setFont(this.defaultFont);
        register(Region.ARROW_BUTTON, "ArrowButton");
        register(Region.BUTTON, "Button");
        register(Region.TOGGLE_BUTTON, "ToggleButton");
        register(Region.RADIO_BUTTON, "RadioButton");
        register(Region.CHECK_BOX, "CheckBox");
        register(Region.COLOR_CHOOSER, "ColorChooser");
        register(Region.PANEL, "ColorChooser:\"ColorChooser.previewPanelHolder\"");
        register(Region.LABEL, "ColorChooser:\"ColorChooser.previewPanelHolder\":\"OptionPane.label\"");
        register(Region.COMBO_BOX, "ComboBox");
        register(Region.TEXT_FIELD, "ComboBox:\"ComboBox.textField\"");
        register(Region.ARROW_BUTTON, "ComboBox:\"ComboBox.arrowButton\"");
        register(Region.LABEL, "ComboBox:\"ComboBox.listRenderer\"");
        register(Region.LABEL, "ComboBox:\"ComboBox.renderer\"");
        register(Region.SCROLL_PANE, "\"ComboBox.scrollPane\"");
        register(Region.FILE_CHOOSER, "FileChooser");
        register(Region.INTERNAL_FRAME_TITLE_PANE, "InternalFrameTitlePane");
        register(Region.INTERNAL_FRAME, "InternalFrame");
        register(Region.INTERNAL_FRAME_TITLE_PANE, "InternalFrame:InternalFrameTitlePane");
        register(Region.BUTTON, "InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.menuButton\"");
        register(Region.BUTTON, "InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.iconifyButton\"");
        register(Region.BUTTON, "InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\"");
        register(Region.BUTTON, "InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.closeButton\"");
        register(Region.DESKTOP_ICON, "DesktopIcon");
        register(Region.DESKTOP_PANE, "DesktopPane");
        register(Region.LABEL, "Label");
        register(Region.LIST, "List");
        register(Region.LABEL, "List:\"List.cellRenderer\"");
        register(Region.MENU_BAR, "MenuBar");
        register(Region.MENU, "MenuBar:Menu");
        register(Region.MENU_ITEM_ACCELERATOR, "MenuBar:Menu:MenuItemAccelerator");
        register(Region.MENU_ITEM, "MenuItem");
        register(Region.MENU_ITEM_ACCELERATOR, "MenuItem:MenuItemAccelerator");
        register(Region.RADIO_BUTTON_MENU_ITEM, "RadioButtonMenuItem");
        register(Region.MENU_ITEM_ACCELERATOR, "RadioButtonMenuItem:MenuItemAccelerator");
        register(Region.CHECK_BOX_MENU_ITEM, "CheckBoxMenuItem");
        register(Region.MENU_ITEM_ACCELERATOR, "CheckBoxMenuItem:MenuItemAccelerator");
        register(Region.MENU, "Menu");
        register(Region.MENU_ITEM_ACCELERATOR, "Menu:MenuItemAccelerator");
        register(Region.POPUP_MENU, "PopupMenu");
        register(Region.POPUP_MENU_SEPARATOR, "PopupMenuSeparator");
        register(Region.OPTION_PANE, "OptionPane");
        register(Region.SEPARATOR, "OptionPane:\"OptionPane.separator\"");
        register(Region.PANEL, "OptionPane:\"OptionPane.messageArea\"");
        register(Region.LABEL, "OptionPane:\"OptionPane.messageArea\":\"OptionPane.label\"");
        register(Region.PANEL, "Panel");
        register(Region.PROGRESS_BAR, "ProgressBar");
        register(Region.SEPARATOR, "Separator");
        register(Region.SCROLL_BAR, "ScrollBar");
        register(Region.ARROW_BUTTON, "ScrollBar:\"ScrollBar.button\"");
        register(Region.SCROLL_BAR_THUMB, "ScrollBar:ScrollBarThumb");
        register(Region.SCROLL_BAR_TRACK, "ScrollBar:ScrollBarTrack");
        register(Region.SCROLL_PANE, "ScrollPane");
        register(Region.VIEWPORT, "Viewport");
        register(Region.SLIDER, "Slider");
        register(Region.SLIDER_THUMB, "Slider:SliderThumb");
        register(Region.SLIDER_TRACK, "Slider:SliderTrack");
        register(Region.SPINNER, "Spinner");
        register(Region.PANEL, "Spinner:\"Spinner.editor\"");
        register(Region.FORMATTED_TEXT_FIELD, "Spinner:Panel:\"Spinner.formattedTextField\"");
        register(Region.ARROW_BUTTON, "Spinner:\"Spinner.previousButton\"");
        register(Region.ARROW_BUTTON, "Spinner:\"Spinner.nextButton\"");
        register(Region.SPLIT_PANE, "SplitPane");
        register(Region.SPLIT_PANE_DIVIDER, "SplitPane:SplitPaneDivider");
        register(Region.TABBED_PANE, "TabbedPane");
        register(Region.TABBED_PANE_TAB, "TabbedPane:TabbedPaneTab");
        register(Region.TABBED_PANE_TAB_AREA, "TabbedPane:TabbedPaneTabArea");
        register(Region.TABBED_PANE_CONTENT, "TabbedPane:TabbedPaneContent");
        register(Region.TABLE, "Table");
        register(Region.LABEL, "Table:\"Table.cellRenderer\"");
        register(Region.TABLE_HEADER, "TableHeader");
        register(Region.LABEL, "TableHeader:\"TableHeader.renderer\"");
        register(Region.TEXT_FIELD, "\"Table.editor\"");
        register(Region.TEXT_FIELD, "\"Tree.cellEditor\"");
        register(Region.TEXT_FIELD, "TextField");
        register(Region.FORMATTED_TEXT_FIELD, "FormattedTextField");
        register(Region.PASSWORD_FIELD, "PasswordField");
        register(Region.TEXT_AREA, "TextArea");
        register(Region.TEXT_PANE, "TextPane");
        register(Region.EDITOR_PANE, "EditorPane");
        register(Region.TOOL_BAR, "ToolBar");
        register(Region.BUTTON, "ToolBar:Button");
        register(Region.TOGGLE_BUTTON, "ToolBar:ToggleButton");
        register(Region.TOOL_BAR_SEPARATOR, "ToolBarSeparator");
        register(Region.TOOL_TIP, "ToolTip");
        register(Region.TREE, "Tree");
        register(Region.TREE_CELL, "Tree:TreeCell");
        register(Region.LABEL, "Tree:\"Tree.cellRenderer\"");
        register(Region.ROOT_PANE, "RootPane");
    }

    void initializeDefaults(UIDefaults uIDefaults) {
        addColor(uIDefaults, "text", 0, 0, 0, 255);
        addColor(uIDefaults, "control", 214, 217, 223, 255);
        addColor(uIDefaults, "nimbusBase", 51, 98, 140, 255);
        addColor(uIDefaults, "nimbusBlueGrey", "nimbusBase", 0.032459438f, -0.52518797f, 0.19607842f, 0);
        addColor(uIDefaults, "nimbusOrange", 191, 98, 4, 255);
        addColor(uIDefaults, "nimbusGreen", 176, 179, 50, 255);
        addColor(uIDefaults, "nimbusRed", 169, 46, 34, 255);
        addColor(uIDefaults, "nimbusBorder", "nimbusBlueGrey", 0.0f, -0.017358616f, -0.11372548f, 0);
        addColor(uIDefaults, "nimbusSelection", "nimbusBase", -0.010750473f, -0.04875779f, -0.007843137f, 0);
        addColor(uIDefaults, "nimbusInfoBlue", 47, 92, 180, 255);
        addColor(uIDefaults, "nimbusAlertYellow", 255, 220, 35, 255);
        addColor(uIDefaults, "nimbusFocus", 115, 164, 209, 255);
        addColor(uIDefaults, "nimbusSelectedText", 255, 255, 255, 255);
        addColor(uIDefaults, "nimbusSelectionBackground", 57, 105, 138, 255);
        addColor(uIDefaults, "nimbusDisabledText", 142, 143, 145, 255);
        addColor(uIDefaults, "nimbusLightBackground", 255, 255, 255, 255);
        addColor(uIDefaults, "infoText", "text", 0.0f, 0.0f, 0.0f, 0);
        addColor(uIDefaults, "info", 242, 242, 189, 255);
        addColor(uIDefaults, "menuText", "text", 0.0f, 0.0f, 0.0f, 0);
        addColor(uIDefaults, "menu", "nimbusBase", 0.021348298f, -0.6150531f, 0.39999998f, 0);
        addColor(uIDefaults, "scrollbar", "nimbusBlueGrey", -0.006944418f, -0.07296763f, 0.09019607f, 0);
        addColor(uIDefaults, "controlText", "text", 0.0f, 0.0f, 0.0f, 0);
        addColor(uIDefaults, "controlHighlight", "nimbusBlueGrey", 0.0f, -0.07333623f, 0.20392156f, 0);
        addColor(uIDefaults, "controlLHighlight", "nimbusBlueGrey", 0.0f, -0.098526314f, 0.2352941f, 0);
        addColor(uIDefaults, "controlShadow", "nimbusBlueGrey", -0.0027777553f, -0.0212406f, 0.13333333f, 0);
        addColor(uIDefaults, "controlDkShadow", "nimbusBlueGrey", -0.0027777553f, -0.0018306673f, -0.02352941f, 0);
        addColor(uIDefaults, "textHighlight", "nimbusSelectionBackground", 0.0f, 0.0f, 0.0f, 0);
        addColor(uIDefaults, "textHighlightText", "nimbusSelectedText", 0.0f, 0.0f, 0.0f, 0);
        addColor(uIDefaults, "textInactiveText", "nimbusDisabledText", 0.0f, 0.0f, 0.0f, 0);
        addColor(uIDefaults, "desktop", "nimbusBase", -0.009207249f, -0.13984653f, -0.07450983f, 0);
        addColor(uIDefaults, "activeCaption", "nimbusBlueGrey", 0.0f, -0.049920253f, 0.031372547f, 0);
        addColor(uIDefaults, "inactiveCaption", "nimbusBlueGrey", -0.00505054f, -0.055526316f, 0.039215684f, 0);
        uIDefaults.put("defaultFont", new FontUIResource(this.defaultFont));
        uIDefaults.put("InternalFrame.titleFont", new DerivedFont("defaultFont", 1.0f, true, null));
        addColor(uIDefaults, "textForeground", "text", 0.0f, 0.0f, 0.0f, 0);
        addColor(uIDefaults, "textBackground", "nimbusSelectionBackground", 0.0f, 0.0f, 0.0f, 0);
        addColor(uIDefaults, "background", "control", 0.0f, 0.0f, 0.0f, 0);
        uIDefaults.put("TitledBorder.position", "ABOVE_TOP");
        uIDefaults.put("FileView.fullRowSelection", Boolean.TRUE);
        uIDefaults.put("ArrowButton.contentMargins", new InsetsUIResource(0, 0, 0, 0));
        uIDefaults.put("ArrowButton.size", new Integer(16));
        uIDefaults.put("ArrowButton[Disabled].foregroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ArrowButtonPainter", 2, new Insets(0, 0, 0, 0), new Dimension(10, 10), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("ArrowButton[Enabled].foregroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ArrowButtonPainter", 3, new Insets(0, 0, 0, 0), new Dimension(10, 10), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("Button.contentMargins", new InsetsUIResource(6, 14, 6, 14));
        uIDefaults.put("Button.defaultButtonFollowsFocus", Boolean.FALSE);
        uIDefaults.put("Button[Default].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ButtonPainter", 1, new Insets(7, 7, 7, 7), new Dimension(104, 33), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 2.0d));
        uIDefaults.put("Button[Default+Focused].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ButtonPainter", 2, new Insets(7, 7, 7, 7), new Dimension(104, 33), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 2.0d));
        uIDefaults.put("Button[Default+MouseOver].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ButtonPainter", 3, new Insets(7, 7, 7, 7), new Dimension(104, 33), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 2.0d));
        uIDefaults.put("Button[Default+Focused+MouseOver].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ButtonPainter", 4, new Insets(7, 7, 7, 7), new Dimension(104, 33), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 2.0d));
        addColor(uIDefaults, "Button[Default+Pressed].textForeground", "nimbusSelectedText", 0.0f, 0.0f, 0.0f, 0);
        uIDefaults.put("Button[Default+Pressed].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ButtonPainter", 5, new Insets(7, 7, 7, 7), new Dimension(104, 33), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 2.0d));
        uIDefaults.put("Button[Default+Focused+Pressed].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ButtonPainter", 6, new Insets(7, 7, 7, 7), new Dimension(104, 33), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 2.0d));
        addColor(uIDefaults, "Button[Disabled].textForeground", "nimbusDisabledText", 0.0f, 0.0f, 0.0f, 0);
        uIDefaults.put("Button[Disabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ButtonPainter", 7, new Insets(7, 7, 7, 7), new Dimension(104, 33), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 2.0d));
        uIDefaults.put("Button[Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ButtonPainter", 8, new Insets(7, 7, 7, 7), new Dimension(104, 33), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 2.0d));
        uIDefaults.put("Button[Focused].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ButtonPainter", 9, new Insets(7, 7, 7, 7), new Dimension(104, 33), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 2.0d));
        uIDefaults.put("Button[MouseOver].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ButtonPainter", 10, new Insets(7, 7, 7, 7), new Dimension(104, 33), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 2.0d));
        uIDefaults.put("Button[Focused+MouseOver].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ButtonPainter", 11, new Insets(7, 7, 7, 7), new Dimension(104, 33), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 2.0d));
        uIDefaults.put("Button[Pressed].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ButtonPainter", 12, new Insets(7, 7, 7, 7), new Dimension(104, 33), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 2.0d));
        uIDefaults.put("Button[Focused+Pressed].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ButtonPainter", 13, new Insets(7, 7, 7, 7), new Dimension(104, 33), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 2.0d));
        uIDefaults.put("ToggleButton.contentMargins", new InsetsUIResource(6, 14, 6, 14));
        addColor(uIDefaults, "ToggleButton[Disabled].textForeground", "nimbusDisabledText", 0.0f, 0.0f, 0.0f, 0);
        uIDefaults.put("ToggleButton[Disabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ToggleButtonPainter", 1, new Insets(7, 7, 7, 7), new Dimension(104, 33), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 2.0d));
        uIDefaults.put("ToggleButton[Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ToggleButtonPainter", 2, new Insets(7, 7, 7, 7), new Dimension(104, 33), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 2.0d));
        uIDefaults.put("ToggleButton[Focused].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ToggleButtonPainter", 3, new Insets(7, 7, 7, 7), new Dimension(104, 33), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 2.0d));
        uIDefaults.put("ToggleButton[MouseOver].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ToggleButtonPainter", 4, new Insets(7, 7, 7, 7), new Dimension(104, 33), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 2.0d));
        uIDefaults.put("ToggleButton[Focused+MouseOver].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ToggleButtonPainter", 5, new Insets(7, 7, 7, 7), new Dimension(104, 33), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 2.0d));
        uIDefaults.put("ToggleButton[Pressed].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ToggleButtonPainter", 6, new Insets(7, 7, 7, 7), new Dimension(104, 33), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 2.0d));
        uIDefaults.put("ToggleButton[Focused+Pressed].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ToggleButtonPainter", 7, new Insets(7, 7, 7, 7), new Dimension(104, 33), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 2.0d));
        uIDefaults.put("ToggleButton[Selected].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ToggleButtonPainter", 8, new Insets(7, 7, 7, 7), new Dimension(72, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 2.0d));
        uIDefaults.put("ToggleButton[Focused+Selected].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ToggleButtonPainter", 9, new Insets(7, 7, 7, 7), new Dimension(72, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 2.0d));
        uIDefaults.put("ToggleButton[Pressed+Selected].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ToggleButtonPainter", 10, new Insets(7, 7, 7, 7), new Dimension(72, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 2.0d));
        uIDefaults.put("ToggleButton[Focused+Pressed+Selected].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ToggleButtonPainter", 11, new Insets(7, 7, 7, 7), new Dimension(72, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 2.0d));
        uIDefaults.put("ToggleButton[MouseOver+Selected].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ToggleButtonPainter", 12, new Insets(7, 7, 7, 7), new Dimension(72, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 2.0d));
        uIDefaults.put("ToggleButton[Focused+MouseOver+Selected].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ToggleButtonPainter", 13, new Insets(7, 7, 7, 7), new Dimension(72, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 2.0d));
        addColor(uIDefaults, "ToggleButton[Disabled+Selected].textForeground", "nimbusDisabledText", 0.0f, 0.0f, 0.0f, 0);
        uIDefaults.put("ToggleButton[Disabled+Selected].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ToggleButtonPainter", 14, new Insets(7, 7, 7, 7), new Dimension(72, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 2.0d));
        uIDefaults.put("RadioButton.contentMargins", new InsetsUIResource(0, 0, 0, 0));
        addColor(uIDefaults, "RadioButton[Disabled].textForeground", "nimbusDisabledText", 0.0f, 0.0f, 0.0f, 0);
        uIDefaults.put("RadioButton[Disabled].iconPainter", new LazyPainter("javax.swing.plaf.nimbus.RadioButtonPainter", 3, new Insets(5, 5, 5, 5), new Dimension(18, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("RadioButton[Enabled].iconPainter", new LazyPainter("javax.swing.plaf.nimbus.RadioButtonPainter", 4, new Insets(5, 5, 5, 5), new Dimension(18, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("RadioButton[Focused].iconPainter", new LazyPainter("javax.swing.plaf.nimbus.RadioButtonPainter", 5, new Insets(5, 5, 5, 5), new Dimension(18, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("RadioButton[MouseOver].iconPainter", new LazyPainter("javax.swing.plaf.nimbus.RadioButtonPainter", 6, new Insets(5, 5, 5, 5), new Dimension(18, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("RadioButton[Focused+MouseOver].iconPainter", new LazyPainter("javax.swing.plaf.nimbus.RadioButtonPainter", 7, new Insets(5, 5, 5, 5), new Dimension(18, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("RadioButton[Pressed].iconPainter", new LazyPainter("javax.swing.plaf.nimbus.RadioButtonPainter", 8, new Insets(5, 5, 5, 5), new Dimension(18, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("RadioButton[Focused+Pressed].iconPainter", new LazyPainter("javax.swing.plaf.nimbus.RadioButtonPainter", 9, new Insets(5, 5, 5, 5), new Dimension(18, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("RadioButton[Selected].iconPainter", new LazyPainter("javax.swing.plaf.nimbus.RadioButtonPainter", 10, new Insets(5, 5, 5, 5), new Dimension(18, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("RadioButton[Focused+Selected].iconPainter", new LazyPainter("javax.swing.plaf.nimbus.RadioButtonPainter", 11, new Insets(5, 5, 5, 5), new Dimension(18, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("RadioButton[Pressed+Selected].iconPainter", new LazyPainter("javax.swing.plaf.nimbus.RadioButtonPainter", 12, new Insets(5, 5, 5, 5), new Dimension(18, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("RadioButton[Focused+Pressed+Selected].iconPainter", new LazyPainter("javax.swing.plaf.nimbus.RadioButtonPainter", 13, new Insets(5, 5, 5, 5), new Dimension(18, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("RadioButton[MouseOver+Selected].iconPainter", new LazyPainter("javax.swing.plaf.nimbus.RadioButtonPainter", 14, new Insets(5, 5, 5, 5), new Dimension(18, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("RadioButton[Focused+MouseOver+Selected].iconPainter", new LazyPainter("javax.swing.plaf.nimbus.RadioButtonPainter", 15, new Insets(5, 5, 5, 5), new Dimension(18, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("RadioButton[Disabled+Selected].iconPainter", new LazyPainter("javax.swing.plaf.nimbus.RadioButtonPainter", 16, new Insets(5, 5, 5, 5), new Dimension(18, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("RadioButton.icon", new NimbusIcon("RadioButton", "iconPainter", 18, 18));
        uIDefaults.put("CheckBox.contentMargins", new InsetsUIResource(0, 0, 0, 0));
        addColor(uIDefaults, "CheckBox[Disabled].textForeground", "nimbusDisabledText", 0.0f, 0.0f, 0.0f, 0);
        uIDefaults.put("CheckBox[Disabled].iconPainter", new LazyPainter("javax.swing.plaf.nimbus.CheckBoxPainter", 3, new Insets(5, 5, 5, 5), new Dimension(18, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("CheckBox[Enabled].iconPainter", new LazyPainter("javax.swing.plaf.nimbus.CheckBoxPainter", 4, new Insets(5, 5, 5, 5), new Dimension(18, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("CheckBox[Focused].iconPainter", new LazyPainter("javax.swing.plaf.nimbus.CheckBoxPainter", 5, new Insets(5, 5, 5, 5), new Dimension(18, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("CheckBox[MouseOver].iconPainter", new LazyPainter("javax.swing.plaf.nimbus.CheckBoxPainter", 6, new Insets(5, 5, 5, 5), new Dimension(18, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("CheckBox[Focused+MouseOver].iconPainter", new LazyPainter("javax.swing.plaf.nimbus.CheckBoxPainter", 7, new Insets(5, 5, 5, 5), new Dimension(18, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("CheckBox[Pressed].iconPainter", new LazyPainter("javax.swing.plaf.nimbus.CheckBoxPainter", 8, new Insets(5, 5, 5, 5), new Dimension(18, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("CheckBox[Focused+Pressed].iconPainter", new LazyPainter("javax.swing.plaf.nimbus.CheckBoxPainter", 9, new Insets(5, 5, 5, 5), new Dimension(18, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("CheckBox[Selected].iconPainter", new LazyPainter("javax.swing.plaf.nimbus.CheckBoxPainter", 10, new Insets(5, 5, 5, 5), new Dimension(18, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("CheckBox[Focused+Selected].iconPainter", new LazyPainter("javax.swing.plaf.nimbus.CheckBoxPainter", 11, new Insets(5, 5, 5, 5), new Dimension(18, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("CheckBox[Pressed+Selected].iconPainter", new LazyPainter("javax.swing.plaf.nimbus.CheckBoxPainter", 12, new Insets(5, 5, 5, 5), new Dimension(18, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("CheckBox[Focused+Pressed+Selected].iconPainter", new LazyPainter("javax.swing.plaf.nimbus.CheckBoxPainter", 13, new Insets(5, 5, 5, 5), new Dimension(18, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("CheckBox[MouseOver+Selected].iconPainter", new LazyPainter("javax.swing.plaf.nimbus.CheckBoxPainter", 14, new Insets(5, 5, 5, 5), new Dimension(18, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("CheckBox[Focused+MouseOver+Selected].iconPainter", new LazyPainter("javax.swing.plaf.nimbus.CheckBoxPainter", 15, new Insets(5, 5, 5, 5), new Dimension(18, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("CheckBox[Disabled+Selected].iconPainter", new LazyPainter("javax.swing.plaf.nimbus.CheckBoxPainter", 16, new Insets(5, 5, 5, 5), new Dimension(18, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("CheckBox.icon", new NimbusIcon("CheckBox", "iconPainter", 18, 18));
        uIDefaults.put("ColorChooser.contentMargins", new InsetsUIResource(5, 0, 0, 0));
        addColor(uIDefaults, "ColorChooser.swatchesDefaultRecentColor", 255, 255, 255, 255);
        uIDefaults.put("ColorChooser:\"ColorChooser.previewPanelHolder\".contentMargins", new InsetsUIResource(0, 5, 10, 5));
        uIDefaults.put("ColorChooser:\"ColorChooser.previewPanelHolder\":\"OptionPane.label\".contentMargins", new InsetsUIResource(0, 10, 10, 10));
        uIDefaults.put("ComboBox.contentMargins", new InsetsUIResource(0, 0, 0, 0));
        uIDefaults.put("ComboBox.States", "Enabled,MouseOver,Pressed,Selected,Disabled,Focused,Editable");
        uIDefaults.put("ComboBox.Editable", new ComboBoxEditableState());
        uIDefaults.put("ComboBox.forceOpaque", Boolean.TRUE);
        uIDefaults.put("ComboBox.buttonWhenNotEditable", Boolean.TRUE);
        uIDefaults.put("ComboBox.rendererUseListColors", Boolean.FALSE);
        uIDefaults.put("ComboBox.pressedWhenPopupVisible", Boolean.TRUE);
        uIDefaults.put("ComboBox.squareButton", Boolean.FALSE);
        uIDefaults.put("ComboBox.popupInsets", new InsetsUIResource(-2, 2, 0, 2));
        uIDefaults.put("ComboBox.padding", new InsetsUIResource(3, 3, 3, 3));
        uIDefaults.put("ComboBox[Disabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ComboBoxPainter", 1, new Insets(8, 9, 8, 19), new Dimension(83, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 2.0d));
        uIDefaults.put("ComboBox[Disabled+Pressed].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ComboBoxPainter", 2, new Insets(8, 9, 8, 19), new Dimension(83, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 2.0d));
        uIDefaults.put("ComboBox[Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ComboBoxPainter", 3, new Insets(8, 9, 8, 19), new Dimension(83, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 2.0d));
        uIDefaults.put("ComboBox[Focused].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ComboBoxPainter", 4, new Insets(8, 9, 8, 19), new Dimension(83, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 2.0d));
        uIDefaults.put("ComboBox[Focused+MouseOver].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ComboBoxPainter", 5, new Insets(8, 9, 8, 19), new Dimension(83, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 2.0d));
        uIDefaults.put("ComboBox[MouseOver].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ComboBoxPainter", 6, new Insets(8, 9, 8, 19), new Dimension(83, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 2.0d));
        uIDefaults.put("ComboBox[Focused+Pressed].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ComboBoxPainter", 7, new Insets(8, 9, 8, 19), new Dimension(83, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 2.0d));
        uIDefaults.put("ComboBox[Pressed].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ComboBoxPainter", 8, new Insets(8, 9, 8, 19), new Dimension(83, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 2.0d));
        uIDefaults.put("ComboBox[Enabled+Selected].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ComboBoxPainter", 9, new Insets(8, 9, 8, 19), new Dimension(83, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 2.0d));
        uIDefaults.put("ComboBox[Disabled+Editable].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ComboBoxPainter", 10, new Insets(6, 5, 6, 17), new Dimension(79, 21), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 2.0d));
        uIDefaults.put("ComboBox[Editable+Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ComboBoxPainter", 11, new Insets(6, 5, 6, 17), new Dimension(79, 21), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 2.0d));
        uIDefaults.put("ComboBox[Editable+Focused].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ComboBoxPainter", 12, new Insets(5, 5, 5, 5), new Dimension(142, 27), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 2.0d));
        uIDefaults.put("ComboBox[Editable+MouseOver].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ComboBoxPainter", 13, new Insets(4, 5, 5, 17), new Dimension(79, 21), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 2.0d));
        uIDefaults.put("ComboBox[Editable+Pressed].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ComboBoxPainter", 14, new Insets(4, 5, 5, 17), new Dimension(79, 21), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 2.0d));
        uIDefaults.put("ComboBox:\"ComboBox.textField\".contentMargins", new InsetsUIResource(0, 6, 0, 3));
        addColor(uIDefaults, "ComboBox:\"ComboBox.textField\"[Disabled].textForeground", "nimbusDisabledText", 0.0f, 0.0f, 0.0f, 0);
        uIDefaults.put("ComboBox:\"ComboBox.textField\"[Disabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ComboBoxTextFieldPainter", 1, new Insets(5, 3, 3, 1), new Dimension(64, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 2.0d));
        uIDefaults.put("ComboBox:\"ComboBox.textField\"[Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ComboBoxTextFieldPainter", 2, new Insets(5, 3, 3, 1), new Dimension(64, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 2.0d));
        addColor(uIDefaults, "ComboBox:\"ComboBox.textField\"[Selected].textForeground", "nimbusSelectedText", 0.0f, 0.0f, 0.0f, 0);
        uIDefaults.put("ComboBox:\"ComboBox.textField\"[Selected].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ComboBoxTextFieldPainter", 3, new Insets(5, 3, 3, 1), new Dimension(64, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 2.0d));
        uIDefaults.put("ComboBox:\"ComboBox.arrowButton\".contentMargins", new InsetsUIResource(0, 0, 0, 0));
        uIDefaults.put("ComboBox:\"ComboBox.arrowButton\".States", "Enabled,MouseOver,Pressed,Disabled,Editable");
        uIDefaults.put("ComboBox:\"ComboBox.arrowButton\".Editable", new ComboBoxArrowButtonEditableState());
        uIDefaults.put("ComboBox:\"ComboBox.arrowButton\".size", new Integer(19));
        uIDefaults.put("ComboBox:\"ComboBox.arrowButton\"[Disabled+Editable].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ComboBoxArrowButtonPainter", 5, new Insets(8, 1, 8, 8), new Dimension(20, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 2.0d));
        uIDefaults.put("ComboBox:\"ComboBox.arrowButton\"[Editable+Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ComboBoxArrowButtonPainter", 6, new Insets(8, 1, 8, 8), new Dimension(20, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 2.0d));
        uIDefaults.put("ComboBox:\"ComboBox.arrowButton\"[Editable+MouseOver].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ComboBoxArrowButtonPainter", 7, new Insets(8, 1, 8, 8), new Dimension(20, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 2.0d));
        uIDefaults.put("ComboBox:\"ComboBox.arrowButton\"[Editable+Pressed].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ComboBoxArrowButtonPainter", 8, new Insets(8, 1, 8, 8), new Dimension(20, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 2.0d));
        uIDefaults.put("ComboBox:\"ComboBox.arrowButton\"[Editable+Selected].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ComboBoxArrowButtonPainter", 9, new Insets(8, 1, 8, 8), new Dimension(20, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 2.0d));
        uIDefaults.put("ComboBox:\"ComboBox.arrowButton\"[Enabled].foregroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ComboBoxArrowButtonPainter", 10, new Insets(6, 9, 6, 10), new Dimension(24, 19), true, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 2.0d));
        uIDefaults.put("ComboBox:\"ComboBox.arrowButton\"[MouseOver].foregroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ComboBoxArrowButtonPainter", 11, new Insets(6, 9, 6, 10), new Dimension(24, 19), true, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 2.0d));
        uIDefaults.put("ComboBox:\"ComboBox.arrowButton\"[Disabled].foregroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ComboBoxArrowButtonPainter", 12, new Insets(6, 9, 6, 10), new Dimension(24, 19), true, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 2.0d));
        uIDefaults.put("ComboBox:\"ComboBox.arrowButton\"[Pressed].foregroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ComboBoxArrowButtonPainter", 13, new Insets(6, 9, 6, 10), new Dimension(24, 19), true, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 2.0d));
        uIDefaults.put("ComboBox:\"ComboBox.arrowButton\"[Selected].foregroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ComboBoxArrowButtonPainter", 14, new Insets(6, 9, 6, 10), new Dimension(24, 19), true, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 2.0d));
        uIDefaults.put("ComboBox:\"ComboBox.listRenderer\".contentMargins", new InsetsUIResource(2, 4, 2, 4));
        uIDefaults.put("ComboBox:\"ComboBox.listRenderer\".opaque", Boolean.TRUE);
        addColor(uIDefaults, "ComboBox:\"ComboBox.listRenderer\".background", "nimbusLightBackground", 0.0f, 0.0f, 0.0f, 0);
        addColor(uIDefaults, "ComboBox:\"ComboBox.listRenderer\"[Disabled].textForeground", "nimbusDisabledText", 0.0f, 0.0f, 0.0f, 0);
        addColor(uIDefaults, "ComboBox:\"ComboBox.listRenderer\"[Selected].textForeground", "nimbusSelectedText", 0.0f, 0.0f, 0.0f, 0);
        addColor(uIDefaults, "ComboBox:\"ComboBox.listRenderer\"[Selected].background", "nimbusSelectionBackground", 0.0f, 0.0f, 0.0f, 0);
        uIDefaults.put("ComboBox:\"ComboBox.renderer\".contentMargins", new InsetsUIResource(2, 4, 2, 4));
        addColor(uIDefaults, "ComboBox:\"ComboBox.renderer\"[Disabled].textForeground", "nimbusDisabledText", 0.0f, 0.0f, 0.0f, 0);
        addColor(uIDefaults, "ComboBox:\"ComboBox.renderer\"[Selected].textForeground", "nimbusSelectedText", 0.0f, 0.0f, 0.0f, 0);
        addColor(uIDefaults, "ComboBox:\"ComboBox.renderer\"[Selected].background", "nimbusSelectionBackground", 0.0f, 0.0f, 0.0f, 0);
        uIDefaults.put("\"ComboBox.scrollPane\".contentMargins", new InsetsUIResource(0, 0, 0, 0));
        uIDefaults.put("FileChooser.contentMargins", new InsetsUIResource(10, 10, 10, 10));
        uIDefaults.put("FileChooser.opaque", Boolean.TRUE);
        uIDefaults.put("FileChooser.usesSingleFilePane", Boolean.TRUE);
        uIDefaults.put("FileChooser[Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.FileChooserPainter", 1, new Insets(0, 0, 0, 0), new Dimension(100, 30), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0d, 1.0d));
        uIDefaults.put("FileChooser[Enabled].fileIconPainter", new LazyPainter("javax.swing.plaf.nimbus.FileChooserPainter", 2, new Insets(5, 5, 5, 5), new Dimension(16, 16), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("FileChooser.fileIcon", new NimbusIcon("FileChooser", "fileIconPainter", 16, 16));
        uIDefaults.put("FileChooser[Enabled].directoryIconPainter", new LazyPainter("javax.swing.plaf.nimbus.FileChooserPainter", 3, new Insets(5, 5, 5, 5), new Dimension(16, 16), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("FileChooser.directoryIcon", new NimbusIcon("FileChooser", "directoryIconPainter", 16, 16));
        uIDefaults.put("FileChooser[Enabled].upFolderIconPainter", new LazyPainter("javax.swing.plaf.nimbus.FileChooserPainter", 4, new Insets(5, 5, 5, 5), new Dimension(16, 16), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("FileChooser.upFolderIcon", new NimbusIcon("FileChooser", "upFolderIconPainter", 16, 16));
        uIDefaults.put("FileChooser[Enabled].newFolderIconPainter", new LazyPainter("javax.swing.plaf.nimbus.FileChooserPainter", 5, new Insets(5, 5, 5, 5), new Dimension(16, 16), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("FileChooser.newFolderIcon", new NimbusIcon("FileChooser", "newFolderIconPainter", 16, 16));
        uIDefaults.put("FileChooser[Enabled].hardDriveIconPainter", new LazyPainter("javax.swing.plaf.nimbus.FileChooserPainter", 7, new Insets(5, 5, 5, 5), new Dimension(16, 16), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("FileChooser.hardDriveIcon", new NimbusIcon("FileChooser", "hardDriveIconPainter", 16, 16));
        uIDefaults.put("FileChooser[Enabled].floppyDriveIconPainter", new LazyPainter("javax.swing.plaf.nimbus.FileChooserPainter", 8, new Insets(5, 5, 5, 5), new Dimension(16, 16), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("FileChooser.floppyDriveIcon", new NimbusIcon("FileChooser", "floppyDriveIconPainter", 16, 16));
        uIDefaults.put("FileChooser[Enabled].homeFolderIconPainter", new LazyPainter("javax.swing.plaf.nimbus.FileChooserPainter", 9, new Insets(5, 5, 5, 5), new Dimension(16, 16), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("FileChooser.homeFolderIcon", new NimbusIcon("FileChooser", "homeFolderIconPainter", 16, 16));
        uIDefaults.put("FileChooser[Enabled].detailsViewIconPainter", new LazyPainter("javax.swing.plaf.nimbus.FileChooserPainter", 10, new Insets(5, 5, 5, 5), new Dimension(16, 16), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("FileChooser.detailsViewIcon", new NimbusIcon("FileChooser", "detailsViewIconPainter", 16, 16));
        uIDefaults.put("FileChooser[Enabled].listViewIconPainter", new LazyPainter("javax.swing.plaf.nimbus.FileChooserPainter", 11, new Insets(5, 5, 5, 5), new Dimension(16, 16), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("FileChooser.listViewIcon", new NimbusIcon("FileChooser", "listViewIconPainter", 16, 16));
        uIDefaults.put("InternalFrameTitlePane.contentMargins", new InsetsUIResource(0, 0, 0, 0));
        uIDefaults.put("InternalFrameTitlePane.maxFrameIconSize", new DimensionUIResource(18, 18));
        uIDefaults.put("InternalFrame.contentMargins", new InsetsUIResource(1, 6, 6, 6));
        uIDefaults.put("InternalFrame.States", "Enabled,WindowFocused");
        uIDefaults.put("InternalFrame.WindowFocused", new InternalFrameWindowFocusedState());
        uIDefaults.put("InternalFrame[Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.InternalFramePainter", 1, new Insets(25, 6, 6, 6), new Dimension(25, 36), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("InternalFrame[Enabled+WindowFocused].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.InternalFramePainter", 2, new Insets(25, 6, 6, 6), new Dimension(25, 36), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("InternalFrame:InternalFrameTitlePane.contentMargins", new InsetsUIResource(3, 0, 3, 0));
        uIDefaults.put("InternalFrame:InternalFrameTitlePane.States", "Enabled,WindowFocused");
        uIDefaults.put("InternalFrame:InternalFrameTitlePane.WindowFocused", new InternalFrameTitlePaneWindowFocusedState());
        uIDefaults.put("InternalFrame:InternalFrameTitlePane.titleAlignment", "CENTER");
        addColor(uIDefaults, "InternalFrame:InternalFrameTitlePane[Enabled].textForeground", "nimbusDisabledText", 0.0f, 0.0f, 0.0f, 0);
        uIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.menuButton\".contentMargins", new InsetsUIResource(0, 0, 0, 0));
        uIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.menuButton\".States", "Enabled,MouseOver,Pressed,Disabled,Focused,Selected,WindowNotFocused");
        uIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.menuButton\".WindowNotFocused", new InternalFrameTitlePaneMenuButtonWindowNotFocusedState());
        uIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.menuButton\".test", "am InternalFrameTitlePane.menuButton");
        uIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.menuButton\"[Enabled].iconPainter", new LazyPainter("javax.swing.plaf.nimbus.InternalFrameTitlePaneMenuButtonPainter", 1, new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.menuButton\"[Disabled].iconPainter", new LazyPainter("javax.swing.plaf.nimbus.InternalFrameTitlePaneMenuButtonPainter", 2, new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.menuButton\"[MouseOver].iconPainter", new LazyPainter("javax.swing.plaf.nimbus.InternalFrameTitlePaneMenuButtonPainter", 3, new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.menuButton\"[Pressed].iconPainter", new LazyPainter("javax.swing.plaf.nimbus.InternalFrameTitlePaneMenuButtonPainter", 4, new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.menuButton\"[Enabled+WindowNotFocused].iconPainter", new LazyPainter("javax.swing.plaf.nimbus.InternalFrameTitlePaneMenuButtonPainter", 5, new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.menuButton\"[MouseOver+WindowNotFocused].iconPainter", new LazyPainter("javax.swing.plaf.nimbus.InternalFrameTitlePaneMenuButtonPainter", 6, new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.menuButton\"[Pressed+WindowNotFocused].iconPainter", new LazyPainter("javax.swing.plaf.nimbus.InternalFrameTitlePaneMenuButtonPainter", 7, new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.menuButton\".icon", new NimbusIcon("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.menuButton\"", "iconPainter", 19, 18));
        uIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.iconifyButton\".contentMargins", new InsetsUIResource(9, 9, 9, 9));
        uIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.iconifyButton\".States", "Enabled,MouseOver,Pressed,Disabled,Focused,Selected,WindowNotFocused");
        uIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.iconifyButton\".WindowNotFocused", new InternalFrameTitlePaneIconifyButtonWindowNotFocusedState());
        uIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.iconifyButton\"[Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.InternalFrameTitlePaneIconifyButtonPainter", 1, new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.iconifyButton\"[Disabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.InternalFrameTitlePaneIconifyButtonPainter", 2, new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.iconifyButton\"[MouseOver].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.InternalFrameTitlePaneIconifyButtonPainter", 3, new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.iconifyButton\"[Pressed].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.InternalFrameTitlePaneIconifyButtonPainter", 4, new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.iconifyButton\"[Enabled+WindowNotFocused].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.InternalFrameTitlePaneIconifyButtonPainter", 5, new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.iconifyButton\"[MouseOver+WindowNotFocused].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.InternalFrameTitlePaneIconifyButtonPainter", 6, new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.iconifyButton\"[Pressed+WindowNotFocused].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.InternalFrameTitlePaneIconifyButtonPainter", 7, new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\".contentMargins", new InsetsUIResource(9, 9, 9, 9));
        uIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\".States", "Enabled,MouseOver,Pressed,Disabled,Focused,Selected,WindowNotFocused,WindowMaximized");
        uIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\".WindowNotFocused", new InternalFrameTitlePaneMaximizeButtonWindowNotFocusedState());
        uIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\".WindowMaximized", new InternalFrameTitlePaneMaximizeButtonWindowMaximizedState());
        uIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\"[Disabled+WindowMaximized].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.InternalFrameTitlePaneMaximizeButtonPainter", 1, new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\"[Enabled+WindowMaximized].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.InternalFrameTitlePaneMaximizeButtonPainter", 2, new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\"[MouseOver+WindowMaximized].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.InternalFrameTitlePaneMaximizeButtonPainter", 3, new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\"[Pressed+WindowMaximized].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.InternalFrameTitlePaneMaximizeButtonPainter", 4, new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\"[Enabled+WindowMaximized+WindowNotFocused].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.InternalFrameTitlePaneMaximizeButtonPainter", 5, new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\"[MouseOver+WindowMaximized+WindowNotFocused].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.InternalFrameTitlePaneMaximizeButtonPainter", 6, new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\"[Pressed+WindowMaximized+WindowNotFocused].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.InternalFrameTitlePaneMaximizeButtonPainter", 7, new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\"[Disabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.InternalFrameTitlePaneMaximizeButtonPainter", 8, new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\"[Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.InternalFrameTitlePaneMaximizeButtonPainter", 9, new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\"[MouseOver].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.InternalFrameTitlePaneMaximizeButtonPainter", 10, new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\"[Pressed].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.InternalFrameTitlePaneMaximizeButtonPainter", 11, new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\"[Enabled+WindowNotFocused].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.InternalFrameTitlePaneMaximizeButtonPainter", 12, new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\"[MouseOver+WindowNotFocused].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.InternalFrameTitlePaneMaximizeButtonPainter", 13, new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.maximizeButton\"[Pressed+WindowNotFocused].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.InternalFrameTitlePaneMaximizeButtonPainter", 14, new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.closeButton\".contentMargins", new InsetsUIResource(9, 9, 9, 9));
        uIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.closeButton\".States", "Enabled,MouseOver,Pressed,Disabled,Focused,Selected,WindowNotFocused");
        uIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.closeButton\".WindowNotFocused", new InternalFrameTitlePaneCloseButtonWindowNotFocusedState());
        uIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.closeButton\"[Disabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.InternalFrameTitlePaneCloseButtonPainter", 1, new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.closeButton\"[Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.InternalFrameTitlePaneCloseButtonPainter", 2, new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.closeButton\"[MouseOver].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.InternalFrameTitlePaneCloseButtonPainter", 3, new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.closeButton\"[Pressed].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.InternalFrameTitlePaneCloseButtonPainter", 4, new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.closeButton\"[Enabled+WindowNotFocused].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.InternalFrameTitlePaneCloseButtonPainter", 5, new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.closeButton\"[MouseOver+WindowNotFocused].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.InternalFrameTitlePaneCloseButtonPainter", 6, new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("InternalFrame:InternalFrameTitlePane:\"InternalFrameTitlePane.closeButton\"[Pressed+WindowNotFocused].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.InternalFrameTitlePaneCloseButtonPainter", 7, new Insets(0, 0, 0, 0), new Dimension(19, 18), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("DesktopIcon.contentMargins", new InsetsUIResource(4, 6, 5, 4));
        uIDefaults.put("DesktopIcon[Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.DesktopIconPainter", 1, new Insets(5, 5, 5, 5), new Dimension(28, 26), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("DesktopPane.contentMargins", new InsetsUIResource(0, 0, 0, 0));
        uIDefaults.put("DesktopPane.opaque", Boolean.TRUE);
        uIDefaults.put("DesktopPane[Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.DesktopPanePainter", 1, new Insets(0, 0, 0, 0), new Dimension(300, JPEG.APP8), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("Label.contentMargins", new InsetsUIResource(0, 0, 0, 0));
        addColor(uIDefaults, "Label[Disabled].textForeground", "nimbusDisabledText", 0.0f, 0.0f, 0.0f, 0);
        uIDefaults.put("List.contentMargins", new InsetsUIResource(0, 0, 0, 0));
        uIDefaults.put("List.opaque", Boolean.TRUE);
        addColor(uIDefaults, "List.background", "nimbusLightBackground", 0.0f, 0.0f, 0.0f, 0);
        uIDefaults.put("List.rendererUseListColors", Boolean.FALSE);
        uIDefaults.put("List.rendererUseUIBorder", Boolean.TRUE);
        uIDefaults.put("List.cellNoFocusBorder", new BorderUIResource(BorderFactory.createEmptyBorder(2, 5, 2, 5)));
        uIDefaults.put("List.focusCellHighlightBorder", new BorderUIResource(new PainterBorder("Tree:TreeCell[Enabled+Focused].backgroundPainter", new Insets(2, 5, 2, 5))));
        addColor(uIDefaults, "List.dropLineColor", "nimbusFocus", 0.0f, 0.0f, 0.0f, 0);
        addColor(uIDefaults, "List[Selected].textForeground", "nimbusLightBackground", 0.0f, 0.0f, 0.0f, 0);
        addColor(uIDefaults, "List[Selected].textBackground", "nimbusSelectionBackground", 0.0f, 0.0f, 0.0f, 0);
        addColor(uIDefaults, "List[Disabled+Selected].textBackground", "nimbusSelectionBackground", 0.0f, 0.0f, 0.0f, 0);
        addColor(uIDefaults, "List[Disabled].textForeground", "nimbusDisabledText", 0.0f, 0.0f, 0.0f, 0);
        uIDefaults.put("List:\"List.cellRenderer\".contentMargins", new InsetsUIResource(0, 0, 0, 0));
        uIDefaults.put("List:\"List.cellRenderer\".opaque", Boolean.TRUE);
        addColor(uIDefaults, "List:\"List.cellRenderer\"[Selected].textForeground", "nimbusLightBackground", 0.0f, 0.0f, 0.0f, 0);
        addColor(uIDefaults, "List:\"List.cellRenderer\"[Selected].background", "nimbusSelectionBackground", 0.0f, 0.0f, 0.0f, 0);
        addColor(uIDefaults, "List:\"List.cellRenderer\"[Disabled+Selected].background", "nimbusSelectionBackground", 0.0f, 0.0f, 0.0f, 0);
        addColor(uIDefaults, "List:\"List.cellRenderer\"[Disabled].textForeground", "nimbusDisabledText", 0.0f, 0.0f, 0.0f, 0);
        uIDefaults.put("MenuBar.contentMargins", new InsetsUIResource(2, 6, 2, 6));
        uIDefaults.put("MenuBar[Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.MenuBarPainter", 1, new Insets(1, 0, 0, 0), new Dimension(18, 22), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 2.0d));
        uIDefaults.put("MenuBar[Enabled].borderPainter", new LazyPainter("javax.swing.plaf.nimbus.MenuBarPainter", 2, new Insets(0, 0, 1, 0), new Dimension(30, 30), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 2.0d));
        uIDefaults.put("MenuBar:Menu.contentMargins", new InsetsUIResource(1, 4, 2, 4));
        addColor(uIDefaults, "MenuBar:Menu[Disabled].textForeground", "nimbusDisabledText", 0.0f, 0.0f, 0.0f, 0);
        addColor(uIDefaults, "MenuBar:Menu[Enabled].textForeground", 35, 35, 36, 255);
        addColor(uIDefaults, "MenuBar:Menu[Selected].textForeground", 255, 255, 255, 255);
        uIDefaults.put("MenuBar:Menu[Selected].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.MenuBarMenuPainter", 3, new Insets(0, 0, 0, 0), new Dimension(100, 30), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0d, 1.0d));
        uIDefaults.put("MenuBar:Menu:MenuItemAccelerator.contentMargins", new InsetsUIResource(0, 0, 0, 0));
        uIDefaults.put("MenuItem.contentMargins", new InsetsUIResource(1, 12, 2, 13));
        uIDefaults.put("MenuItem.textIconGap", new Integer(5));
        addColor(uIDefaults, "MenuItem[Disabled].textForeground", "nimbusDisabledText", 0.0f, 0.0f, 0.0f, 0);
        addColor(uIDefaults, "MenuItem[Enabled].textForeground", 35, 35, 36, 255);
        addColor(uIDefaults, "MenuItem[MouseOver].textForeground", 255, 255, 255, 255);
        uIDefaults.put("MenuItem[MouseOver].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.MenuItemPainter", 3, new Insets(0, 0, 0, 0), new Dimension(100, 3), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0d, 1.0d));
        uIDefaults.put("MenuItem:MenuItemAccelerator.contentMargins", new InsetsUIResource(0, 0, 0, 0));
        addColor(uIDefaults, "MenuItem:MenuItemAccelerator[Disabled].textForeground", "nimbusDisabledText", 0.0f, 0.0f, 0.0f, 0);
        addColor(uIDefaults, "MenuItem:MenuItemAccelerator[MouseOver].textForeground", 255, 255, 255, 255);
        uIDefaults.put("RadioButtonMenuItem.contentMargins", new InsetsUIResource(1, 12, 2, 13));
        uIDefaults.put("RadioButtonMenuItem.textIconGap", new Integer(5));
        addColor(uIDefaults, "RadioButtonMenuItem[Disabled].textForeground", "nimbusDisabledText", 0.0f, 0.0f, 0.0f, 0);
        addColor(uIDefaults, "RadioButtonMenuItem[Enabled].textForeground", 35, 35, 36, 255);
        addColor(uIDefaults, "RadioButtonMenuItem[MouseOver].textForeground", 255, 255, 255, 255);
        uIDefaults.put("RadioButtonMenuItem[MouseOver].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.RadioButtonMenuItemPainter", 3, new Insets(0, 0, 0, 0), new Dimension(100, 3), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0d, 1.0d));
        addColor(uIDefaults, "RadioButtonMenuItem[MouseOver+Selected].textForeground", 255, 255, 255, 255);
        uIDefaults.put("RadioButtonMenuItem[MouseOver+Selected].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.RadioButtonMenuItemPainter", 4, new Insets(0, 0, 0, 0), new Dimension(100, 3), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0d, 1.0d));
        uIDefaults.put("RadioButtonMenuItem[Disabled+Selected].checkIconPainter", new LazyPainter("javax.swing.plaf.nimbus.RadioButtonMenuItemPainter", 5, new Insets(5, 5, 5, 5), new Dimension(9, 10), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("RadioButtonMenuItem[Enabled+Selected].checkIconPainter", new LazyPainter("javax.swing.plaf.nimbus.RadioButtonMenuItemPainter", 6, new Insets(5, 5, 5, 5), new Dimension(9, 10), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("RadioButtonMenuItem[MouseOver+Selected].checkIconPainter", new LazyPainter("javax.swing.plaf.nimbus.RadioButtonMenuItemPainter", 7, new Insets(5, 5, 5, 5), new Dimension(9, 10), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("RadioButtonMenuItem.checkIcon", new NimbusIcon("RadioButtonMenuItem", "checkIconPainter", 9, 10));
        uIDefaults.put("RadioButtonMenuItem:MenuItemAccelerator.contentMargins", new InsetsUIResource(0, 0, 0, 0));
        addColor(uIDefaults, "RadioButtonMenuItem:MenuItemAccelerator[MouseOver].textForeground", 255, 255, 255, 255);
        uIDefaults.put("CheckBoxMenuItem.contentMargins", new InsetsUIResource(1, 12, 2, 13));
        uIDefaults.put("CheckBoxMenuItem.textIconGap", new Integer(5));
        addColor(uIDefaults, "CheckBoxMenuItem[Disabled].textForeground", "nimbusDisabledText", 0.0f, 0.0f, 0.0f, 0);
        addColor(uIDefaults, "CheckBoxMenuItem[Enabled].textForeground", 35, 35, 36, 255);
        addColor(uIDefaults, "CheckBoxMenuItem[MouseOver].textForeground", 255, 255, 255, 255);
        uIDefaults.put("CheckBoxMenuItem[MouseOver].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.CheckBoxMenuItemPainter", 3, new Insets(0, 0, 0, 0), new Dimension(100, 3), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0d, 1.0d));
        addColor(uIDefaults, "CheckBoxMenuItem[MouseOver+Selected].textForeground", 255, 255, 255, 255);
        uIDefaults.put("CheckBoxMenuItem[MouseOver+Selected].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.CheckBoxMenuItemPainter", 4, new Insets(0, 0, 0, 0), new Dimension(100, 3), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0d, 1.0d));
        uIDefaults.put("CheckBoxMenuItem[Disabled+Selected].checkIconPainter", new LazyPainter("javax.swing.plaf.nimbus.CheckBoxMenuItemPainter", 5, new Insets(5, 5, 5, 5), new Dimension(9, 10), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("CheckBoxMenuItem[Enabled+Selected].checkIconPainter", new LazyPainter("javax.swing.plaf.nimbus.CheckBoxMenuItemPainter", 6, new Insets(5, 5, 5, 5), new Dimension(9, 10), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("CheckBoxMenuItem[MouseOver+Selected].checkIconPainter", new LazyPainter("javax.swing.plaf.nimbus.CheckBoxMenuItemPainter", 7, new Insets(5, 5, 5, 5), new Dimension(9, 10), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("CheckBoxMenuItem.checkIcon", new NimbusIcon("CheckBoxMenuItem", "checkIconPainter", 9, 10));
        uIDefaults.put("CheckBoxMenuItem:MenuItemAccelerator.contentMargins", new InsetsUIResource(0, 0, 0, 0));
        addColor(uIDefaults, "CheckBoxMenuItem:MenuItemAccelerator[MouseOver].textForeground", 255, 255, 255, 255);
        uIDefaults.put("Menu.contentMargins", new InsetsUIResource(1, 12, 2, 5));
        uIDefaults.put("Menu.textIconGap", new Integer(5));
        addColor(uIDefaults, "Menu[Disabled].textForeground", "nimbusDisabledText", 0.0f, 0.0f, 0.0f, 0);
        addColor(uIDefaults, "Menu[Enabled].textForeground", 35, 35, 36, 255);
        addColor(uIDefaults, "Menu[Enabled+Selected].textForeground", 255, 255, 255, 255);
        uIDefaults.put("Menu[Enabled+Selected].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.MenuPainter", 3, new Insets(0, 0, 0, 0), new Dimension(100, 30), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0d, 1.0d));
        uIDefaults.put("Menu[Disabled].arrowIconPainter", new LazyPainter("javax.swing.plaf.nimbus.MenuPainter", 4, new Insets(5, 5, 5, 5), new Dimension(9, 10), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("Menu[Enabled].arrowIconPainter", new LazyPainter("javax.swing.plaf.nimbus.MenuPainter", 5, new Insets(5, 5, 5, 5), new Dimension(9, 10), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("Menu[Enabled+Selected].arrowIconPainter", new LazyPainter("javax.swing.plaf.nimbus.MenuPainter", 6, new Insets(1, 1, 1, 1), new Dimension(9, 10), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("Menu.arrowIcon", new NimbusIcon("Menu", "arrowIconPainter", 9, 10));
        uIDefaults.put("Menu:MenuItemAccelerator.contentMargins", new InsetsUIResource(0, 0, 0, 0));
        addColor(uIDefaults, "Menu:MenuItemAccelerator[MouseOver].textForeground", 255, 255, 255, 255);
        uIDefaults.put("PopupMenu.contentMargins", new InsetsUIResource(6, 1, 6, 1));
        uIDefaults.put("PopupMenu.opaque", Boolean.TRUE);
        uIDefaults.put("PopupMenu.consumeEventOnClose", Boolean.TRUE);
        uIDefaults.put("PopupMenu[Disabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.PopupMenuPainter", 1, new Insets(9, 0, 11, 0), new Dimension(220, 313), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0d, 1.0d));
        uIDefaults.put("PopupMenu[Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.PopupMenuPainter", 2, new Insets(11, 2, 11, 2), new Dimension(220, 313), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0d, 1.0d));
        uIDefaults.put("PopupMenuSeparator.contentMargins", new InsetsUIResource(1, 0, 2, 0));
        uIDefaults.put("PopupMenuSeparator[Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.PopupMenuSeparatorPainter", 1, new Insets(1, 1, 1, 1), new Dimension(3, 3), true, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0d, 1.0d));
        uIDefaults.put("OptionPane.contentMargins", new InsetsUIResource(15, 15, 15, 15));
        uIDefaults.put("OptionPane.opaque", Boolean.TRUE);
        uIDefaults.put("OptionPane.buttonOrientation", new Integer(4));
        uIDefaults.put("OptionPane.messageAnchor", new Integer(17));
        uIDefaults.put("OptionPane.separatorPadding", new Integer(0));
        uIDefaults.put("OptionPane.sameSizeButtons", Boolean.FALSE);
        uIDefaults.put("OptionPane:\"OptionPane.separator\".contentMargins", new InsetsUIResource(1, 0, 0, 0));
        uIDefaults.put("OptionPane:\"OptionPane.messageArea\".contentMargins", new InsetsUIResource(0, 0, 10, 0));
        uIDefaults.put("OptionPane:\"OptionPane.messageArea\":\"OptionPane.label\".contentMargins", new InsetsUIResource(0, 10, 10, 10));
        uIDefaults.put("OptionPane:\"OptionPane.messageArea\":\"OptionPane.label\"[Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.OptionPaneMessageAreaOptionPaneLabelPainter", 1, new Insets(5, 5, 5, 5), new Dimension(100, 30), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0d, 1.0d));
        uIDefaults.put("OptionPane[Enabled].errorIconPainter", new LazyPainter("javax.swing.plaf.nimbus.OptionPanePainter", 2, new Insets(0, 0, 0, 0), new Dimension(48, 48), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("OptionPane.errorIcon", new NimbusIcon("OptionPane", "errorIconPainter", 48, 48));
        uIDefaults.put("OptionPane[Enabled].informationIconPainter", new LazyPainter("javax.swing.plaf.nimbus.OptionPanePainter", 3, new Insets(0, 0, 0, 0), new Dimension(48, 48), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("OptionPane.informationIcon", new NimbusIcon("OptionPane", "informationIconPainter", 48, 48));
        uIDefaults.put("OptionPane[Enabled].questionIconPainter", new LazyPainter("javax.swing.plaf.nimbus.OptionPanePainter", 4, new Insets(0, 0, 0, 0), new Dimension(48, 48), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("OptionPane.questionIcon", new NimbusIcon("OptionPane", "questionIconPainter", 48, 48));
        uIDefaults.put("OptionPane[Enabled].warningIconPainter", new LazyPainter("javax.swing.plaf.nimbus.OptionPanePainter", 5, new Insets(0, 0, 0, 0), new Dimension(48, 48), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("OptionPane.warningIcon", new NimbusIcon("OptionPane", "warningIconPainter", 48, 48));
        uIDefaults.put("Panel.contentMargins", new InsetsUIResource(0, 0, 0, 0));
        uIDefaults.put("Panel.opaque", Boolean.TRUE);
        uIDefaults.put("ProgressBar.contentMargins", new InsetsUIResource(0, 0, 0, 0));
        uIDefaults.put("ProgressBar.States", "Enabled,Disabled,Indeterminate,Finished");
        uIDefaults.put("ProgressBar.Indeterminate", new ProgressBarIndeterminateState());
        uIDefaults.put("ProgressBar.Finished", new ProgressBarFinishedState());
        uIDefaults.put("ProgressBar.tileWhenIndeterminate", Boolean.TRUE);
        uIDefaults.put("ProgressBar.tileWidth", new Integer(27));
        uIDefaults.put("ProgressBar.paintOutsideClip", Boolean.TRUE);
        uIDefaults.put("ProgressBar.rotateText", Boolean.TRUE);
        uIDefaults.put("ProgressBar.vertictalSize", new DimensionUIResource(19, 150));
        uIDefaults.put("ProgressBar.horizontalSize", new DimensionUIResource(150, 19));
        uIDefaults.put("ProgressBar.cycleTime", new Integer(250));
        uIDefaults.put("ProgressBar.minBarSize", new DimensionUIResource(6, 6));
        uIDefaults.put("ProgressBar.glowWidth", new Integer(2));
        uIDefaults.put("ProgressBar[Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ProgressBarPainter", 1, new Insets(5, 5, 5, 5), new Dimension(29, 19), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        addColor(uIDefaults, "ProgressBar[Disabled].textForeground", "nimbusDisabledText", 0.0f, 0.0f, 0.0f, 0);
        uIDefaults.put("ProgressBar[Disabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ProgressBarPainter", 2, new Insets(5, 5, 5, 5), new Dimension(29, 19), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("ProgressBar[Enabled].foregroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ProgressBarPainter", 3, new Insets(3, 3, 3, 3), new Dimension(27, 19), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 2.0d));
        uIDefaults.put("ProgressBar[Enabled+Finished].foregroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ProgressBarPainter", 4, new Insets(3, 3, 3, 3), new Dimension(27, 19), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 2.0d));
        uIDefaults.put("ProgressBar[Enabled+Indeterminate].progressPadding", new Integer(3));
        uIDefaults.put("ProgressBar[Enabled+Indeterminate].foregroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ProgressBarPainter", 5, new Insets(3, 3, 3, 3), new Dimension(30, 13), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 2.0d));
        uIDefaults.put("ProgressBar[Disabled].foregroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ProgressBarPainter", 6, new Insets(3, 3, 3, 3), new Dimension(27, 19), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 2.0d));
        uIDefaults.put("ProgressBar[Disabled+Finished].foregroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ProgressBarPainter", 7, new Insets(3, 3, 3, 3), new Dimension(27, 19), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 2.0d));
        uIDefaults.put("ProgressBar[Disabled+Indeterminate].progressPadding", new Integer(3));
        uIDefaults.put("ProgressBar[Disabled+Indeterminate].foregroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ProgressBarPainter", 8, new Insets(3, 3, 3, 3), new Dimension(30, 13), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 2.0d));
        uIDefaults.put("Separator.contentMargins", new InsetsUIResource(0, 0, 0, 0));
        uIDefaults.put("Separator[Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SeparatorPainter", 1, new Insets(0, 40, 0, 40), new Dimension(100, 3), true, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("ScrollBar.contentMargins", new InsetsUIResource(0, 0, 0, 0));
        uIDefaults.put("ScrollBar.opaque", Boolean.TRUE);
        uIDefaults.put("ScrollBar.incrementButtonGap", new Integer(-8));
        uIDefaults.put("ScrollBar.decrementButtonGap", new Integer(-8));
        uIDefaults.put("ScrollBar.thumbHeight", new Integer(15));
        uIDefaults.put("ScrollBar.minimumThumbSize", new DimensionUIResource(29, 29));
        uIDefaults.put("ScrollBar.maximumThumbSize", new DimensionUIResource(1000, 1000));
        uIDefaults.put("ScrollBar:\"ScrollBar.button\".contentMargins", new InsetsUIResource(0, 0, 0, 0));
        uIDefaults.put("ScrollBar:\"ScrollBar.button\".size", new Integer(25));
        uIDefaults.put("ScrollBar:\"ScrollBar.button\"[Enabled].foregroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ScrollBarButtonPainter", 1, new Insets(1, 1, 1, 1), new Dimension(25, 15), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("ScrollBar:\"ScrollBar.button\"[Disabled].foregroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ScrollBarButtonPainter", 2, new Insets(1, 1, 1, 1), new Dimension(25, 15), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("ScrollBar:\"ScrollBar.button\"[MouseOver].foregroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ScrollBarButtonPainter", 3, new Insets(1, 1, 1, 1), new Dimension(25, 15), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("ScrollBar:\"ScrollBar.button\"[Pressed].foregroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ScrollBarButtonPainter", 4, new Insets(1, 1, 1, 1), new Dimension(25, 15), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("ScrollBar:ScrollBarThumb.contentMargins", new InsetsUIResource(0, 0, 0, 0));
        uIDefaults.put("ScrollBar:ScrollBarThumb[Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ScrollBarThumbPainter", 2, new Insets(0, 15, 0, 15), new Dimension(38, 15), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 2.0d));
        uIDefaults.put("ScrollBar:ScrollBarThumb[MouseOver].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ScrollBarThumbPainter", 4, new Insets(0, 15, 0, 15), new Dimension(38, 15), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 2.0d));
        uIDefaults.put("ScrollBar:ScrollBarThumb[Pressed].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ScrollBarThumbPainter", 5, new Insets(0, 15, 0, 15), new Dimension(38, 15), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 2.0d));
        uIDefaults.put("ScrollBar:ScrollBarTrack.contentMargins", new InsetsUIResource(0, 0, 0, 0));
        uIDefaults.put("ScrollBar:ScrollBarTrack[Disabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ScrollBarTrackPainter", 1, new Insets(5, 5, 5, 5), new Dimension(18, 15), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 2.0d));
        uIDefaults.put("ScrollBar:ScrollBarTrack[Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ScrollBarTrackPainter", 2, new Insets(5, 10, 5, 9), new Dimension(34, 15), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("ScrollPane.contentMargins", new InsetsUIResource(3, 3, 3, 3));
        uIDefaults.put("ScrollPane.useChildTextComponentFocus", Boolean.TRUE);
        uIDefaults.put("ScrollPane[Enabled+Focused].borderPainter", new LazyPainter("javax.swing.plaf.nimbus.ScrollPanePainter", 2, new Insets(5, 5, 5, 5), new Dimension(122, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("ScrollPane[Enabled].borderPainter", new LazyPainter("javax.swing.plaf.nimbus.ScrollPanePainter", 3, new Insets(5, 5, 5, 5), new Dimension(122, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("Viewport.contentMargins", new InsetsUIResource(0, 0, 0, 0));
        uIDefaults.put("Viewport.opaque", Boolean.TRUE);
        uIDefaults.put("Slider.contentMargins", new InsetsUIResource(0, 0, 0, 0));
        uIDefaults.put("Slider.States", "Enabled,MouseOver,Pressed,Disabled,Focused,Selected,ArrowShape");
        uIDefaults.put("Slider.ArrowShape", new SliderArrowShapeState());
        uIDefaults.put("Slider.thumbWidth", new Integer(17));
        uIDefaults.put("Slider.thumbHeight", new Integer(17));
        uIDefaults.put("Slider.trackBorder", new Integer(0));
        uIDefaults.put("Slider.paintValue", Boolean.FALSE);
        addColor(uIDefaults, "Slider.tickColor", 35, 40, 48, 255);
        uIDefaults.put("Slider:SliderThumb.contentMargins", new InsetsUIResource(0, 0, 0, 0));
        uIDefaults.put("Slider:SliderThumb.States", "Enabled,MouseOver,Pressed,Disabled,Focused,Selected,ArrowShape");
        uIDefaults.put("Slider:SliderThumb.ArrowShape", new SliderThumbArrowShapeState());
        uIDefaults.put("Slider:SliderThumb[Disabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SliderThumbPainter", 1, new Insets(5, 5, 5, 5), new Dimension(17, 17), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("Slider:SliderThumb[Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SliderThumbPainter", 2, new Insets(5, 5, 5, 5), new Dimension(17, 17), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("Slider:SliderThumb[Focused].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SliderThumbPainter", 3, new Insets(5, 5, 5, 5), new Dimension(17, 17), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("Slider:SliderThumb[Focused+MouseOver].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SliderThumbPainter", 4, new Insets(5, 5, 5, 5), new Dimension(17, 17), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("Slider:SliderThumb[Focused+Pressed].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SliderThumbPainter", 5, new Insets(5, 5, 5, 5), new Dimension(17, 17), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("Slider:SliderThumb[MouseOver].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SliderThumbPainter", 6, new Insets(5, 5, 5, 5), new Dimension(17, 17), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("Slider:SliderThumb[Pressed].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SliderThumbPainter", 7, new Insets(5, 5, 5, 5), new Dimension(17, 17), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("Slider:SliderThumb[ArrowShape+Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SliderThumbPainter", 8, new Insets(5, 5, 5, 5), new Dimension(17, 17), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("Slider:SliderThumb[ArrowShape+Disabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SliderThumbPainter", 9, new Insets(5, 5, 5, 5), new Dimension(17, 17), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("Slider:SliderThumb[ArrowShape+MouseOver].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SliderThumbPainter", 10, new Insets(5, 5, 5, 5), new Dimension(17, 17), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("Slider:SliderThumb[ArrowShape+Pressed].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SliderThumbPainter", 11, new Insets(5, 5, 5, 5), new Dimension(17, 17), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("Slider:SliderThumb[ArrowShape+Focused].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SliderThumbPainter", 12, new Insets(5, 5, 5, 5), new Dimension(17, 17), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("Slider:SliderThumb[ArrowShape+Focused+MouseOver].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SliderThumbPainter", 13, new Insets(5, 5, 5, 5), new Dimension(17, 17), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("Slider:SliderThumb[ArrowShape+Focused+Pressed].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SliderThumbPainter", 14, new Insets(5, 5, 5, 5), new Dimension(17, 17), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("Slider:SliderTrack.contentMargins", new InsetsUIResource(0, 0, 0, 0));
        uIDefaults.put("Slider:SliderTrack.States", "Enabled,MouseOver,Pressed,Disabled,Focused,Selected,ArrowShape");
        uIDefaults.put("Slider:SliderTrack.ArrowShape", new SliderTrackArrowShapeState());
        uIDefaults.put("Slider:SliderTrack[Disabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SliderTrackPainter", 1, new Insets(6, 5, 6, 5), new Dimension(23, 17), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, 2.0d));
        uIDefaults.put("Slider:SliderTrack[Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SliderTrackPainter", 2, new Insets(6, 5, 6, 5), new Dimension(23, 17), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("Spinner.contentMargins", new InsetsUIResource(0, 0, 0, 0));
        uIDefaults.put("Spinner:\"Spinner.editor\".contentMargins", new InsetsUIResource(0, 0, 0, 0));
        uIDefaults.put("Spinner:Panel:\"Spinner.formattedTextField\".contentMargins", new InsetsUIResource(6, 6, 5, 6));
        addColor(uIDefaults, "Spinner:Panel:\"Spinner.formattedTextField\"[Disabled].textForeground", "nimbusDisabledText", 0.0f, 0.0f, 0.0f, 0);
        uIDefaults.put("Spinner:Panel:\"Spinner.formattedTextField\"[Disabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SpinnerPanelSpinnerFormattedTextFieldPainter", 1, new Insets(5, 3, 3, 1), new Dimension(64, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("Spinner:Panel:\"Spinner.formattedTextField\"[Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SpinnerPanelSpinnerFormattedTextFieldPainter", 2, new Insets(5, 3, 3, 1), new Dimension(64, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("Spinner:Panel:\"Spinner.formattedTextField\"[Focused].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SpinnerPanelSpinnerFormattedTextFieldPainter", 3, new Insets(5, 3, 3, 1), new Dimension(64, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        addColor(uIDefaults, "Spinner:Panel:\"Spinner.formattedTextField\"[Selected].textForeground", "nimbusSelectedText", 0.0f, 0.0f, 0.0f, 0);
        uIDefaults.put("Spinner:Panel:\"Spinner.formattedTextField\"[Selected].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SpinnerPanelSpinnerFormattedTextFieldPainter", 4, new Insets(5, 3, 3, 1), new Dimension(64, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        addColor(uIDefaults, "Spinner:Panel:\"Spinner.formattedTextField\"[Focused+Selected].textForeground", "nimbusSelectedText", 0.0f, 0.0f, 0.0f, 0);
        uIDefaults.put("Spinner:Panel:\"Spinner.formattedTextField\"[Focused+Selected].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SpinnerPanelSpinnerFormattedTextFieldPainter", 5, new Insets(5, 3, 3, 1), new Dimension(64, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("Spinner:\"Spinner.previousButton\".contentMargins", new InsetsUIResource(0, 0, 0, 0));
        uIDefaults.put("Spinner:\"Spinner.previousButton\".size", new Integer(20));
        uIDefaults.put("Spinner:\"Spinner.previousButton\"[Disabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SpinnerPreviousButtonPainter", 1, new Insets(0, 1, 6, 7), new Dimension(20, 12), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("Spinner:\"Spinner.previousButton\"[Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SpinnerPreviousButtonPainter", 2, new Insets(0, 1, 6, 7), new Dimension(20, 12), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("Spinner:\"Spinner.previousButton\"[Focused].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SpinnerPreviousButtonPainter", 3, new Insets(0, 1, 6, 7), new Dimension(20, 12), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("Spinner:\"Spinner.previousButton\"[Focused+MouseOver].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SpinnerPreviousButtonPainter", 4, new Insets(3, 1, 6, 7), new Dimension(20, 12), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("Spinner:\"Spinner.previousButton\"[Focused+Pressed].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SpinnerPreviousButtonPainter", 5, new Insets(0, 1, 6, 7), new Dimension(20, 12), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("Spinner:\"Spinner.previousButton\"[MouseOver].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SpinnerPreviousButtonPainter", 6, new Insets(0, 1, 6, 7), new Dimension(20, 12), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("Spinner:\"Spinner.previousButton\"[Pressed].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SpinnerPreviousButtonPainter", 7, new Insets(0, 1, 6, 7), new Dimension(20, 12), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("Spinner:\"Spinner.previousButton\"[Disabled].foregroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SpinnerPreviousButtonPainter", 8, new Insets(3, 6, 5, 9), new Dimension(20, 12), true, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("Spinner:\"Spinner.previousButton\"[Enabled].foregroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SpinnerPreviousButtonPainter", 9, new Insets(3, 6, 5, 9), new Dimension(20, 12), true, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("Spinner:\"Spinner.previousButton\"[Focused].foregroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SpinnerPreviousButtonPainter", 10, new Insets(3, 6, 5, 9), new Dimension(20, 12), true, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("Spinner:\"Spinner.previousButton\"[Focused+MouseOver].foregroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SpinnerPreviousButtonPainter", 11, new Insets(3, 6, 5, 9), new Dimension(20, 12), true, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("Spinner:\"Spinner.previousButton\"[Focused+Pressed].foregroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SpinnerPreviousButtonPainter", 12, new Insets(3, 6, 5, 9), new Dimension(20, 12), true, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("Spinner:\"Spinner.previousButton\"[MouseOver].foregroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SpinnerPreviousButtonPainter", 13, new Insets(3, 6, 5, 9), new Dimension(20, 12), true, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("Spinner:\"Spinner.previousButton\"[Pressed].foregroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SpinnerPreviousButtonPainter", 14, new Insets(3, 6, 5, 9), new Dimension(20, 12), true, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("Spinner:\"Spinner.nextButton\".contentMargins", new InsetsUIResource(0, 0, 0, 0));
        uIDefaults.put("Spinner:\"Spinner.nextButton\".size", new Integer(20));
        uIDefaults.put("Spinner:\"Spinner.nextButton\"[Disabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SpinnerNextButtonPainter", 1, new Insets(7, 1, 1, 7), new Dimension(20, 12), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("Spinner:\"Spinner.nextButton\"[Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SpinnerNextButtonPainter", 2, new Insets(7, 1, 1, 7), new Dimension(20, 12), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("Spinner:\"Spinner.nextButton\"[Focused].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SpinnerNextButtonPainter", 3, new Insets(7, 1, 1, 7), new Dimension(20, 12), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("Spinner:\"Spinner.nextButton\"[Focused+MouseOver].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SpinnerNextButtonPainter", 4, new Insets(7, 1, 1, 7), new Dimension(20, 12), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("Spinner:\"Spinner.nextButton\"[Focused+Pressed].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SpinnerNextButtonPainter", 5, new Insets(7, 1, 1, 7), new Dimension(20, 12), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("Spinner:\"Spinner.nextButton\"[MouseOver].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SpinnerNextButtonPainter", 6, new Insets(7, 1, 1, 7), new Dimension(20, 12), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("Spinner:\"Spinner.nextButton\"[Pressed].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SpinnerNextButtonPainter", 7, new Insets(7, 1, 1, 7), new Dimension(20, 12), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("Spinner:\"Spinner.nextButton\"[Disabled].foregroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SpinnerNextButtonPainter", 8, new Insets(5, 6, 3, 9), new Dimension(20, 12), true, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("Spinner:\"Spinner.nextButton\"[Enabled].foregroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SpinnerNextButtonPainter", 9, new Insets(5, 6, 3, 9), new Dimension(20, 12), true, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("Spinner:\"Spinner.nextButton\"[Focused].foregroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SpinnerNextButtonPainter", 10, new Insets(3, 6, 3, 9), new Dimension(20, 12), true, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("Spinner:\"Spinner.nextButton\"[Focused+MouseOver].foregroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SpinnerNextButtonPainter", 11, new Insets(3, 6, 3, 9), new Dimension(20, 12), true, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("Spinner:\"Spinner.nextButton\"[Focused+Pressed].foregroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SpinnerNextButtonPainter", 12, new Insets(5, 6, 3, 9), new Dimension(20, 12), true, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("Spinner:\"Spinner.nextButton\"[MouseOver].foregroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SpinnerNextButtonPainter", 13, new Insets(5, 6, 3, 9), new Dimension(20, 12), true, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("Spinner:\"Spinner.nextButton\"[Pressed].foregroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SpinnerNextButtonPainter", 14, new Insets(5, 6, 3, 9), new Dimension(20, 12), true, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("SplitPane.contentMargins", new InsetsUIResource(1, 1, 1, 1));
        uIDefaults.put("SplitPane.States", "Enabled,MouseOver,Pressed,Disabled,Focused,Selected,Vertical");
        uIDefaults.put("SplitPane.Vertical", new SplitPaneVerticalState());
        uIDefaults.put("SplitPane.size", new Integer(10));
        uIDefaults.put("SplitPane.dividerSize", new Integer(10));
        uIDefaults.put("SplitPane.centerOneTouchButtons", Boolean.TRUE);
        uIDefaults.put("SplitPane.oneTouchButtonOffset", new Integer(30));
        uIDefaults.put("SplitPane.oneTouchExpandable", Boolean.FALSE);
        uIDefaults.put("SplitPane.continuousLayout", Boolean.TRUE);
        uIDefaults.put("SplitPane:SplitPaneDivider.contentMargins", new InsetsUIResource(0, 0, 0, 0));
        uIDefaults.put("SplitPane:SplitPaneDivider.States", "Enabled,MouseOver,Pressed,Disabled,Focused,Selected,Vertical");
        uIDefaults.put("SplitPane:SplitPaneDivider.Vertical", new SplitPaneDividerVerticalState());
        uIDefaults.put("SplitPane:SplitPaneDivider[Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SplitPaneDividerPainter", 1, new Insets(3, 0, 3, 0), new Dimension(68, 10), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("SplitPane:SplitPaneDivider[Focused].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SplitPaneDividerPainter", 2, new Insets(3, 0, 3, 0), new Dimension(68, 10), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("SplitPane:SplitPaneDivider[Enabled].foregroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SplitPaneDividerPainter", 3, new Insets(0, 24, 0, 24), new Dimension(68, 10), true, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("SplitPane:SplitPaneDivider[Enabled+Vertical].foregroundPainter", new LazyPainter("javax.swing.plaf.nimbus.SplitPaneDividerPainter", 4, new Insets(5, 0, 5, 0), new Dimension(10, 38), true, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("TabbedPane.contentMargins", new InsetsUIResource(0, 0, 0, 0));
        uIDefaults.put("TabbedPane.tabAreaStatesMatchSelectedTab", Boolean.TRUE);
        uIDefaults.put("TabbedPane.nudgeSelectedLabel", Boolean.FALSE);
        uIDefaults.put("TabbedPane.tabRunOverlay", new Integer(2));
        uIDefaults.put("TabbedPane.tabOverlap", new Integer(-1));
        uIDefaults.put("TabbedPane.extendTabsToBase", Boolean.TRUE);
        uIDefaults.put("TabbedPane.useBasicArrows", Boolean.TRUE);
        addColor(uIDefaults, "TabbedPane.shadow", "nimbusDisabledText", 0.0f, 0.0f, 0.0f, 0);
        addColor(uIDefaults, "TabbedPane.darkShadow", "text", 0.0f, 0.0f, 0.0f, 0);
        addColor(uIDefaults, "TabbedPane.highlight", "nimbusLightBackground", 0.0f, 0.0f, 0.0f, 0);
        uIDefaults.put("TabbedPane:TabbedPaneTab.contentMargins", new InsetsUIResource(2, 8, 3, 8));
        uIDefaults.put("TabbedPane:TabbedPaneTab[Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TabbedPaneTabPainter", 1, new Insets(7, 7, 1, 7), new Dimension(44, 20), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("TabbedPane:TabbedPaneTab[Enabled+MouseOver].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TabbedPaneTabPainter", 2, new Insets(7, 7, 1, 7), new Dimension(44, 20), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("TabbedPane:TabbedPaneTab[Enabled+Pressed].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TabbedPaneTabPainter", 3, new Insets(7, 6, 1, 7), new Dimension(44, 20), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        addColor(uIDefaults, "TabbedPane:TabbedPaneTab[Disabled].textForeground", "nimbusDisabledText", 0.0f, 0.0f, 0.0f, 0);
        uIDefaults.put("TabbedPane:TabbedPaneTab[Disabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TabbedPaneTabPainter", 4, new Insets(6, 7, 1, 7), new Dimension(44, 20), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("TabbedPane:TabbedPaneTab[Disabled+Selected].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TabbedPaneTabPainter", 5, new Insets(7, 7, 0, 7), new Dimension(44, 20), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("TabbedPane:TabbedPaneTab[Selected].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TabbedPaneTabPainter", 6, new Insets(7, 7, 0, 7), new Dimension(44, 20), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("TabbedPane:TabbedPaneTab[MouseOver+Selected].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TabbedPaneTabPainter", 7, new Insets(7, 9, 0, 9), new Dimension(44, 20), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        addColor(uIDefaults, "TabbedPane:TabbedPaneTab[Pressed+Selected].textForeground", 255, 255, 255, 255);
        uIDefaults.put("TabbedPane:TabbedPaneTab[Pressed+Selected].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TabbedPaneTabPainter", 8, new Insets(7, 9, 0, 9), new Dimension(44, 20), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("TabbedPane:TabbedPaneTab[Focused+Selected].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TabbedPaneTabPainter", 9, new Insets(7, 7, 3, 7), new Dimension(44, 20), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("TabbedPane:TabbedPaneTab[Focused+MouseOver+Selected].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TabbedPaneTabPainter", 10, new Insets(7, 9, 3, 9), new Dimension(44, 20), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        addColor(uIDefaults, "TabbedPane:TabbedPaneTab[Focused+Pressed+Selected].textForeground", 255, 255, 255, 255);
        uIDefaults.put("TabbedPane:TabbedPaneTab[Focused+Pressed+Selected].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TabbedPaneTabPainter", 11, new Insets(7, 9, 3, 9), new Dimension(44, 20), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("TabbedPane:TabbedPaneTabArea.contentMargins", new InsetsUIResource(3, 10, 4, 10));
        uIDefaults.put("TabbedPane:TabbedPaneTabArea[Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TabbedPaneTabAreaPainter", 1, new Insets(0, 5, 6, 5), new Dimension(5, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("TabbedPane:TabbedPaneTabArea[Disabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TabbedPaneTabAreaPainter", 2, new Insets(0, 5, 6, 5), new Dimension(5, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("TabbedPane:TabbedPaneTabArea[Enabled+MouseOver].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TabbedPaneTabAreaPainter", 3, new Insets(0, 5, 6, 5), new Dimension(5, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("TabbedPane:TabbedPaneTabArea[Enabled+Pressed].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TabbedPaneTabAreaPainter", 4, new Insets(0, 5, 6, 5), new Dimension(5, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("TabbedPane:TabbedPaneContent.contentMargins", new InsetsUIResource(0, 0, 0, 0));
        uIDefaults.put("Table.contentMargins", new InsetsUIResource(0, 0, 0, 0));
        uIDefaults.put("Table.opaque", Boolean.TRUE);
        addColor(uIDefaults, "Table.textForeground", 35, 35, 36, 255);
        addColor(uIDefaults, "Table.background", "nimbusLightBackground", 0.0f, 0.0f, 0.0f, 0);
        uIDefaults.put("Table.showGrid", Boolean.FALSE);
        uIDefaults.put("Table.intercellSpacing", new DimensionUIResource(0, 0));
        addColor(uIDefaults, "Table.alternateRowColor", "nimbusLightBackground", 0.0f, 0.0f, -0.05098039f, 0, false);
        uIDefaults.put("Table.rendererUseTableColors", Boolean.TRUE);
        uIDefaults.put("Table.rendererUseUIBorder", Boolean.TRUE);
        uIDefaults.put("Table.cellNoFocusBorder", new BorderUIResource(BorderFactory.createEmptyBorder(2, 5, 2, 5)));
        uIDefaults.put("Table.focusCellHighlightBorder", new BorderUIResource(new PainterBorder("Tree:TreeCell[Enabled+Focused].backgroundPainter", new Insets(2, 5, 2, 5))));
        addColor(uIDefaults, "Table.dropLineColor", "nimbusFocus", 0.0f, 0.0f, 0.0f, 0);
        addColor(uIDefaults, "Table.dropLineShortColor", "nimbusOrange", 0.0f, 0.0f, 0.0f, 0);
        addColor(uIDefaults, "Table[Enabled+Selected].textForeground", "nimbusLightBackground", 0.0f, 0.0f, 0.0f, 0, false);
        addColor(uIDefaults, "Table[Enabled+Selected].textBackground", "nimbusSelectionBackground", 0.0f, 0.0f, 0.0f, 0, false);
        addColor(uIDefaults, "Table[Disabled+Selected].textBackground", "nimbusSelectionBackground", 0.0f, 0.0f, 0.0f, 0, false);
        uIDefaults.put("Table:\"Table.cellRenderer\".contentMargins", new InsetsUIResource(0, 0, 0, 0));
        uIDefaults.put("Table:\"Table.cellRenderer\".opaque", Boolean.TRUE);
        addColor(uIDefaults, "Table:\"Table.cellRenderer\".background", "nimbusLightBackground", 0.0f, 0.0f, 0.0f, 0, false);
        uIDefaults.put("TableHeader.contentMargins", new InsetsUIResource(0, 0, 0, 0));
        uIDefaults.put("TableHeader.opaque", Boolean.TRUE);
        uIDefaults.put("TableHeader.rightAlignSortArrow", Boolean.TRUE);
        uIDefaults.put("TableHeader[Enabled].ascendingSortIconPainter", new LazyPainter("javax.swing.plaf.nimbus.TableHeaderPainter", 1, new Insets(0, 0, 0, 2), new Dimension(7, 7), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("Table.ascendingSortIcon", new NimbusIcon("TableHeader", "ascendingSortIconPainter", 7, 7));
        uIDefaults.put("TableHeader[Enabled].descendingSortIconPainter", new LazyPainter("javax.swing.plaf.nimbus.TableHeaderPainter", 2, new Insets(0, 0, 0, 0), new Dimension(7, 7), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("Table.descendingSortIcon", new NimbusIcon("TableHeader", "descendingSortIconPainter", 7, 7));
        uIDefaults.put("TableHeader:\"TableHeader.renderer\".contentMargins", new InsetsUIResource(2, 5, 4, 5));
        uIDefaults.put("TableHeader:\"TableHeader.renderer\".opaque", Boolean.TRUE);
        uIDefaults.put("TableHeader:\"TableHeader.renderer\".States", "Enabled,MouseOver,Pressed,Disabled,Focused,Selected,Sorted");
        uIDefaults.put("TableHeader:\"TableHeader.renderer\".Sorted", new TableHeaderRendererSortedState());
        uIDefaults.put("TableHeader:\"TableHeader.renderer\"[Disabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TableHeaderRendererPainter", 1, new Insets(5, 5, 5, 5), new Dimension(22, 20), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("TableHeader:\"TableHeader.renderer\"[Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TableHeaderRendererPainter", 2, new Insets(5, 5, 5, 5), new Dimension(22, 20), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("TableHeader:\"TableHeader.renderer\"[Enabled+Focused].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TableHeaderRendererPainter", 3, new Insets(5, 5, 5, 5), new Dimension(22, 20), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("TableHeader:\"TableHeader.renderer\"[MouseOver].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TableHeaderRendererPainter", 4, new Insets(5, 5, 5, 5), new Dimension(22, 20), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("TableHeader:\"TableHeader.renderer\"[Pressed].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TableHeaderRendererPainter", 5, new Insets(5, 5, 5, 5), new Dimension(22, 20), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("TableHeader:\"TableHeader.renderer\"[Enabled+Sorted].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TableHeaderRendererPainter", 6, new Insets(5, 5, 5, 5), new Dimension(22, 20), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("TableHeader:\"TableHeader.renderer\"[Enabled+Focused+Sorted].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TableHeaderRendererPainter", 7, new Insets(5, 5, 5, 5), new Dimension(22, 20), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("TableHeader:\"TableHeader.renderer\"[Disabled+Sorted].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TableHeaderRendererPainter", 8, new Insets(5, 5, 5, 5), new Dimension(22, 20), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("\"Table.editor\".contentMargins", new InsetsUIResource(3, 5, 3, 5));
        uIDefaults.put("\"Table.editor\".opaque", Boolean.TRUE);
        addColor(uIDefaults, "\"Table.editor\".background", "nimbusLightBackground", 0.0f, 0.0f, 0.0f, 0);
        addColor(uIDefaults, "\"Table.editor\"[Disabled].textForeground", "nimbusDisabledText", 0.0f, 0.0f, 0.0f, 0);
        uIDefaults.put("\"Table.editor\"[Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TableEditorPainter", 2, new Insets(5, 5, 5, 5), new Dimension(100, 30), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("\"Table.editor\"[Enabled+Focused].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TableEditorPainter", 3, new Insets(5, 5, 5, 5), new Dimension(100, 30), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        addColor(uIDefaults, "\"Table.editor\"[Selected].textForeground", "nimbusSelectedText", 0.0f, 0.0f, 0.0f, 0);
        uIDefaults.put("\"Tree.cellEditor\".contentMargins", new InsetsUIResource(2, 5, 2, 5));
        uIDefaults.put("\"Tree.cellEditor\".opaque", Boolean.TRUE);
        addColor(uIDefaults, "\"Tree.cellEditor\".background", "nimbusLightBackground", 0.0f, 0.0f, 0.0f, 0);
        addColor(uIDefaults, "\"Tree.cellEditor\"[Disabled].textForeground", "nimbusDisabledText", 0.0f, 0.0f, 0.0f, 0);
        uIDefaults.put("\"Tree.cellEditor\"[Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TreeCellEditorPainter", 2, new Insets(5, 5, 5, 5), new Dimension(100, 30), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("\"Tree.cellEditor\"[Enabled+Focused].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TreeCellEditorPainter", 3, new Insets(5, 5, 5, 5), new Dimension(100, 30), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        addColor(uIDefaults, "\"Tree.cellEditor\"[Selected].textForeground", "nimbusSelectedText", 0.0f, 0.0f, 0.0f, 0);
        uIDefaults.put("TextField.contentMargins", new InsetsUIResource(6, 6, 6, 6));
        addColor(uIDefaults, "TextField.background", "nimbusLightBackground", 0.0f, 0.0f, 0.0f, 0);
        addColor(uIDefaults, "TextField[Disabled].textForeground", "nimbusDisabledText", 0.0f, 0.0f, 0.0f, 0);
        uIDefaults.put("TextField[Disabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TextFieldPainter", 1, new Insets(5, 5, 5, 5), new Dimension(122, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("TextField[Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TextFieldPainter", 2, new Insets(5, 5, 5, 5), new Dimension(122, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        addColor(uIDefaults, "TextField[Selected].textForeground", "nimbusSelectedText", 0.0f, 0.0f, 0.0f, 0);
        uIDefaults.put("TextField[Selected].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TextFieldPainter", 3, new Insets(5, 5, 5, 5), new Dimension(122, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        addColor(uIDefaults, "TextField[Disabled].textForeground", "nimbusDisabledText", 0.0f, 0.0f, 0.0f, 0);
        uIDefaults.put("TextField[Disabled].borderPainter", new LazyPainter("javax.swing.plaf.nimbus.TextFieldPainter", 4, new Insets(5, 3, 3, 3), new Dimension(122, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("TextField[Focused].borderPainter", new LazyPainter("javax.swing.plaf.nimbus.TextFieldPainter", 5, new Insets(5, 5, 5, 5), new Dimension(122, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("TextField[Enabled].borderPainter", new LazyPainter("javax.swing.plaf.nimbus.TextFieldPainter", 6, new Insets(5, 5, 5, 5), new Dimension(122, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("FormattedTextField.contentMargins", new InsetsUIResource(6, 6, 6, 6));
        addColor(uIDefaults, "FormattedTextField[Disabled].textForeground", "nimbusDisabledText", 0.0f, 0.0f, 0.0f, 0);
        uIDefaults.put("FormattedTextField[Disabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.FormattedTextFieldPainter", 1, new Insets(5, 5, 5, 5), new Dimension(122, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("FormattedTextField[Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.FormattedTextFieldPainter", 2, new Insets(5, 5, 5, 5), new Dimension(122, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        addColor(uIDefaults, "FormattedTextField[Selected].textForeground", "nimbusSelectedText", 0.0f, 0.0f, 0.0f, 0);
        uIDefaults.put("FormattedTextField[Selected].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.FormattedTextFieldPainter", 3, new Insets(5, 5, 5, 5), new Dimension(122, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        addColor(uIDefaults, "FormattedTextField[Disabled].textForeground", "nimbusDisabledText", 0.0f, 0.0f, 0.0f, 0);
        uIDefaults.put("FormattedTextField[Disabled].borderPainter", new LazyPainter("javax.swing.plaf.nimbus.FormattedTextFieldPainter", 4, new Insets(5, 3, 3, 3), new Dimension(122, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("FormattedTextField[Focused].borderPainter", new LazyPainter("javax.swing.plaf.nimbus.FormattedTextFieldPainter", 5, new Insets(5, 5, 5, 5), new Dimension(122, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("FormattedTextField[Enabled].borderPainter", new LazyPainter("javax.swing.plaf.nimbus.FormattedTextFieldPainter", 6, new Insets(5, 5, 5, 5), new Dimension(122, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("PasswordField.contentMargins", new InsetsUIResource(6, 6, 6, 6));
        addColor(uIDefaults, "PasswordField[Disabled].textForeground", "nimbusDisabledText", 0.0f, 0.0f, 0.0f, 0);
        uIDefaults.put("PasswordField[Disabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.PasswordFieldPainter", 1, new Insets(5, 5, 5, 5), new Dimension(122, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("PasswordField[Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.PasswordFieldPainter", 2, new Insets(5, 5, 5, 5), new Dimension(122, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        addColor(uIDefaults, "PasswordField[Selected].textForeground", "nimbusSelectedText", 0.0f, 0.0f, 0.0f, 0);
        uIDefaults.put("PasswordField[Selected].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.PasswordFieldPainter", 3, new Insets(5, 5, 5, 5), new Dimension(122, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        addColor(uIDefaults, "PasswordField[Disabled].textForeground", "nimbusDisabledText", 0.0f, 0.0f, 0.0f, 0);
        uIDefaults.put("PasswordField[Disabled].borderPainter", new LazyPainter("javax.swing.plaf.nimbus.PasswordFieldPainter", 4, new Insets(5, 3, 3, 3), new Dimension(122, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("PasswordField[Focused].borderPainter", new LazyPainter("javax.swing.plaf.nimbus.PasswordFieldPainter", 5, new Insets(5, 5, 5, 5), new Dimension(122, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("PasswordField[Enabled].borderPainter", new LazyPainter("javax.swing.plaf.nimbus.PasswordFieldPainter", 6, new Insets(5, 5, 5, 5), new Dimension(122, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("TextArea.contentMargins", new InsetsUIResource(6, 6, 6, 6));
        uIDefaults.put("TextArea.States", "Enabled,MouseOver,Pressed,Selected,Disabled,Focused,NotInScrollPane");
        uIDefaults.put("TextArea.NotInScrollPane", new TextAreaNotInScrollPaneState());
        addColor(uIDefaults, "TextArea[Disabled].textForeground", "nimbusDisabledText", 0.0f, 0.0f, 0.0f, 0);
        uIDefaults.put("TextArea[Disabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TextAreaPainter", 1, new Insets(5, 5, 5, 5), new Dimension(122, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0d, 1.0d));
        uIDefaults.put("TextArea[Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TextAreaPainter", 2, new Insets(5, 5, 5, 5), new Dimension(122, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0d, 1.0d));
        addColor(uIDefaults, "TextArea[Disabled+NotInScrollPane].textForeground", "nimbusDisabledText", 0.0f, 0.0f, 0.0f, 0);
        uIDefaults.put("TextArea[Disabled+NotInScrollPane].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TextAreaPainter", 3, new Insets(5, 5, 5, 5), new Dimension(122, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0d, 1.0d));
        uIDefaults.put("TextArea[Enabled+NotInScrollPane].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TextAreaPainter", 4, new Insets(5, 5, 5, 5), new Dimension(122, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0d, 1.0d));
        addColor(uIDefaults, "TextArea[Selected].textForeground", "nimbusSelectedText", 0.0f, 0.0f, 0.0f, 0);
        uIDefaults.put("TextArea[Selected].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TextAreaPainter", 5, new Insets(5, 5, 5, 5), new Dimension(122, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0d, 1.0d));
        addColor(uIDefaults, "TextArea[Disabled+NotInScrollPane].textForeground", "nimbusDisabledText", 0.0f, 0.0f, 0.0f, 0);
        uIDefaults.put("TextArea[Disabled+NotInScrollPane].borderPainter", new LazyPainter("javax.swing.plaf.nimbus.TextAreaPainter", 6, new Insets(5, 3, 3, 3), new Dimension(122, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("TextArea[Focused+NotInScrollPane].borderPainter", new LazyPainter("javax.swing.plaf.nimbus.TextAreaPainter", 7, new Insets(5, 5, 5, 5), new Dimension(122, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("TextArea[Enabled+NotInScrollPane].borderPainter", new LazyPainter("javax.swing.plaf.nimbus.TextAreaPainter", 8, new Insets(5, 5, 5, 5), new Dimension(122, 24), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        uIDefaults.put("TextPane.contentMargins", new InsetsUIResource(4, 6, 4, 6));
        uIDefaults.put("TextPane.opaque", Boolean.TRUE);
        addColor(uIDefaults, "TextPane[Disabled].textForeground", "nimbusDisabledText", 0.0f, 0.0f, 0.0f, 0);
        uIDefaults.put("TextPane[Disabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TextPanePainter", 1, new Insets(5, 5, 5, 5), new Dimension(100, 30), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0d, 1.0d));
        uIDefaults.put("TextPane[Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TextPanePainter", 2, new Insets(5, 5, 5, 5), new Dimension(100, 30), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0d, 1.0d));
        addColor(uIDefaults, "TextPane[Selected].textForeground", "nimbusSelectedText", 0.0f, 0.0f, 0.0f, 0);
        uIDefaults.put("TextPane[Selected].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TextPanePainter", 3, new Insets(5, 5, 5, 5), new Dimension(100, 30), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0d, 1.0d));
        uIDefaults.put("EditorPane.contentMargins", new InsetsUIResource(4, 6, 4, 6));
        uIDefaults.put("EditorPane.opaque", Boolean.TRUE);
        addColor(uIDefaults, "EditorPane[Disabled].textForeground", "nimbusDisabledText", 0.0f, 0.0f, 0.0f, 0);
        uIDefaults.put("EditorPane[Disabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.EditorPanePainter", 1, new Insets(5, 5, 5, 5), new Dimension(100, 30), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0d, 1.0d));
        uIDefaults.put("EditorPane[Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.EditorPanePainter", 2, new Insets(5, 5, 5, 5), new Dimension(100, 30), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0d, 1.0d));
        addColor(uIDefaults, "EditorPane[Selected].textForeground", "nimbusSelectedText", 0.0f, 0.0f, 0.0f, 0);
        uIDefaults.put("EditorPane[Selected].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.EditorPanePainter", 3, new Insets(5, 5, 5, 5), new Dimension(100, 30), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0d, 1.0d));
        uIDefaults.put("ToolBar.contentMargins", new InsetsUIResource(2, 2, 2, 2));
        uIDefaults.put("ToolBar.opaque", Boolean.TRUE);
        uIDefaults.put("ToolBar.States", "North,East,West,South");
        uIDefaults.put("ToolBar.North", new ToolBarNorthState());
        uIDefaults.put("ToolBar.East", new ToolBarEastState());
        uIDefaults.put("ToolBar.West", new ToolBarWestState());
        uIDefaults.put("ToolBar.South", new ToolBarSouthState());
        uIDefaults.put("ToolBar[North].borderPainter", new LazyPainter("javax.swing.plaf.nimbus.ToolBarPainter", 1, new Insets(0, 0, 1, 0), new Dimension(30, 30), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0d, 1.0d));
        uIDefaults.put("ToolBar[South].borderPainter", new LazyPainter("javax.swing.plaf.nimbus.ToolBarPainter", 2, new Insets(1, 0, 0, 0), new Dimension(30, 30), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0d, 1.0d));
        uIDefaults.put("ToolBar[East].borderPainter", new LazyPainter("javax.swing.plaf.nimbus.ToolBarPainter", 3, new Insets(1, 0, 0, 0), new Dimension(30, 30), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0d, 1.0d));
        uIDefaults.put("ToolBar[West].borderPainter", new LazyPainter("javax.swing.plaf.nimbus.ToolBarPainter", 4, new Insets(0, 0, 1, 0), new Dimension(30, 30), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0d, 1.0d));
        uIDefaults.put("ToolBar[Enabled].handleIconPainter", new LazyPainter("javax.swing.plaf.nimbus.ToolBarPainter", 5, new Insets(5, 5, 5, 5), new Dimension(11, 38), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, 2.0d, Double.POSITIVE_INFINITY));
        uIDefaults.put("ToolBar.handleIcon", new NimbusIcon("ToolBar", "handleIconPainter", 11, 38));
        uIDefaults.put("ToolBar:Button.contentMargins", new InsetsUIResource(4, 4, 4, 4));
        uIDefaults.put("ToolBar:Button[Focused].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ToolBarButtonPainter", 2, new Insets(5, 5, 5, 5), new Dimension(104, 33), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, 2.0d, Double.POSITIVE_INFINITY));
        uIDefaults.put("ToolBar:Button[MouseOver].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ToolBarButtonPainter", 3, new Insets(5, 5, 5, 5), new Dimension(104, 33), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, 2.0d, Double.POSITIVE_INFINITY));
        uIDefaults.put("ToolBar:Button[Focused+MouseOver].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ToolBarButtonPainter", 4, new Insets(5, 5, 5, 5), new Dimension(104, 33), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, 2.0d, Double.POSITIVE_INFINITY));
        uIDefaults.put("ToolBar:Button[Pressed].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ToolBarButtonPainter", 5, new Insets(5, 5, 5, 5), new Dimension(104, 33), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, 2.0d, Double.POSITIVE_INFINITY));
        uIDefaults.put("ToolBar:Button[Focused+Pressed].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ToolBarButtonPainter", 6, new Insets(5, 5, 5, 5), new Dimension(104, 33), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, 2.0d, Double.POSITIVE_INFINITY));
        uIDefaults.put("ToolBar:ToggleButton.contentMargins", new InsetsUIResource(4, 4, 4, 4));
        uIDefaults.put("ToolBar:ToggleButton[Focused].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ToolBarToggleButtonPainter", 2, new Insets(5, 5, 5, 5), new Dimension(104, 34), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, 2.0d, Double.POSITIVE_INFINITY));
        uIDefaults.put("ToolBar:ToggleButton[MouseOver].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ToolBarToggleButtonPainter", 3, new Insets(5, 5, 5, 5), new Dimension(104, 34), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, 2.0d, Double.POSITIVE_INFINITY));
        uIDefaults.put("ToolBar:ToggleButton[Focused+MouseOver].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ToolBarToggleButtonPainter", 4, new Insets(5, 5, 5, 5), new Dimension(104, 34), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, 2.0d, Double.POSITIVE_INFINITY));
        uIDefaults.put("ToolBar:ToggleButton[Pressed].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ToolBarToggleButtonPainter", 5, new Insets(5, 5, 5, 5), new Dimension(72, 25), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, 2.0d, Double.POSITIVE_INFINITY));
        uIDefaults.put("ToolBar:ToggleButton[Focused+Pressed].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ToolBarToggleButtonPainter", 6, new Insets(5, 5, 5, 5), new Dimension(72, 25), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, 2.0d, Double.POSITIVE_INFINITY));
        uIDefaults.put("ToolBar:ToggleButton[Selected].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ToolBarToggleButtonPainter", 7, new Insets(5, 5, 5, 5), new Dimension(72, 25), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, 2.0d, Double.POSITIVE_INFINITY));
        uIDefaults.put("ToolBar:ToggleButton[Focused+Selected].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ToolBarToggleButtonPainter", 8, new Insets(5, 5, 5, 5), new Dimension(72, 25), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, 2.0d, Double.POSITIVE_INFINITY));
        uIDefaults.put("ToolBar:ToggleButton[Pressed+Selected].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ToolBarToggleButtonPainter", 9, new Insets(5, 5, 5, 5), new Dimension(72, 25), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, 2.0d, Double.POSITIVE_INFINITY));
        uIDefaults.put("ToolBar:ToggleButton[Focused+Pressed+Selected].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ToolBarToggleButtonPainter", 10, new Insets(5, 5, 5, 5), new Dimension(72, 25), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, 2.0d, Double.POSITIVE_INFINITY));
        uIDefaults.put("ToolBar:ToggleButton[MouseOver+Selected].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ToolBarToggleButtonPainter", 11, new Insets(5, 5, 5, 5), new Dimension(72, 25), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, 2.0d, Double.POSITIVE_INFINITY));
        uIDefaults.put("ToolBar:ToggleButton[Focused+MouseOver+Selected].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ToolBarToggleButtonPainter", 12, new Insets(5, 5, 5, 5), new Dimension(72, 25), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, 2.0d, Double.POSITIVE_INFINITY));
        addColor(uIDefaults, "ToolBar:ToggleButton[Disabled+Selected].textForeground", "nimbusDisabledText", 0.0f, 0.0f, 0.0f, 0);
        uIDefaults.put("ToolBar:ToggleButton[Disabled+Selected].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ToolBarToggleButtonPainter", 13, new Insets(5, 5, 5, 5), new Dimension(72, 25), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, 2.0d, Double.POSITIVE_INFINITY));
        uIDefaults.put("ToolBarSeparator.contentMargins", new InsetsUIResource(2, 0, 3, 0));
        addColor(uIDefaults, "ToolBarSeparator.textForeground", "nimbusBorder", 0.0f, 0.0f, 0.0f, 0);
        uIDefaults.put("ToolTip.contentMargins", new InsetsUIResource(4, 4, 4, 4));
        uIDefaults.put("ToolTip[Enabled].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.ToolTipPainter", 1, new Insets(1, 1, 1, 1), new Dimension(10, 10), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0d, 1.0d));
        uIDefaults.put("Tree.contentMargins", new InsetsUIResource(0, 0, 0, 0));
        uIDefaults.put("Tree.opaque", Boolean.TRUE);
        addColor(uIDefaults, "Tree.textForeground", "text", 0.0f, 0.0f, 0.0f, 0, false);
        addColor(uIDefaults, "Tree.textBackground", "nimbusLightBackground", 0.0f, 0.0f, 0.0f, 0, false);
        addColor(uIDefaults, "Tree.background", "nimbusLightBackground", 0.0f, 0.0f, 0.0f, 0);
        uIDefaults.put("Tree.rendererFillBackground", Boolean.FALSE);
        uIDefaults.put("Tree.leftChildIndent", new Integer(12));
        uIDefaults.put("Tree.rightChildIndent", new Integer(4));
        uIDefaults.put("Tree.drawHorizontalLines", Boolean.FALSE);
        uIDefaults.put("Tree.drawVerticalLines", Boolean.FALSE);
        uIDefaults.put("Tree.showRootHandles", Boolean.FALSE);
        uIDefaults.put("Tree.rendererUseTreeColors", Boolean.TRUE);
        uIDefaults.put("Tree.repaintWholeRow", Boolean.TRUE);
        uIDefaults.put("Tree.rowHeight", new Integer(0));
        uIDefaults.put("Tree.rendererMargins", new InsetsUIResource(2, 0, 1, 5));
        addColor(uIDefaults, "Tree.selectionForeground", "nimbusSelectedText", 0.0f, 0.0f, 0.0f, 0, false);
        addColor(uIDefaults, "Tree.selectionBackground", "nimbusSelectionBackground", 0.0f, 0.0f, 0.0f, 0, false);
        addColor(uIDefaults, "Tree.dropLineColor", "nimbusFocus", 0.0f, 0.0f, 0.0f, 0);
        uIDefaults.put("Tree:TreeCell.contentMargins", new InsetsUIResource(0, 0, 0, 0));
        addColor(uIDefaults, "Tree:TreeCell[Enabled].background", "nimbusLightBackground", 0.0f, 0.0f, 0.0f, 0);
        addColor(uIDefaults, "Tree:TreeCell[Enabled+Focused].background", "nimbusLightBackground", 0.0f, 0.0f, 0.0f, 0);
        uIDefaults.put("Tree:TreeCell[Enabled+Focused].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TreeCellPainter", 2, new Insets(5, 5, 5, 5), new Dimension(100, 30), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0d, 1.0d));
        addColor(uIDefaults, "Tree:TreeCell[Enabled+Selected].textForeground", 255, 255, 255, 255);
        uIDefaults.put("Tree:TreeCell[Enabled+Selected].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TreeCellPainter", 3, new Insets(5, 5, 5, 5), new Dimension(100, 30), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0d, 1.0d));
        addColor(uIDefaults, "Tree:TreeCell[Focused+Selected].textForeground", 255, 255, 255, 255);
        uIDefaults.put("Tree:TreeCell[Focused+Selected].backgroundPainter", new LazyPainter("javax.swing.plaf.nimbus.TreeCellPainter", 4, new Insets(5, 5, 5, 5), new Dimension(100, 30), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0d, 1.0d));
        uIDefaults.put("Tree:\"Tree.cellRenderer\".contentMargins", new InsetsUIResource(0, 0, 0, 0));
        addColor(uIDefaults, "Tree:\"Tree.cellRenderer\"[Disabled].textForeground", "nimbusDisabledText", 0.0f, 0.0f, 0.0f, 0);
        uIDefaults.put("Tree[Enabled].leafIconPainter", new LazyPainter("javax.swing.plaf.nimbus.TreePainter", 4, new Insets(5, 5, 5, 5), new Dimension(16, 16), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("Tree.leafIcon", new NimbusIcon("Tree", "leafIconPainter", 16, 16));
        uIDefaults.put("Tree[Enabled].closedIconPainter", new LazyPainter("javax.swing.plaf.nimbus.TreePainter", 5, new Insets(5, 5, 5, 5), new Dimension(16, 16), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("Tree.closedIcon", new NimbusIcon("Tree", "closedIconPainter", 16, 16));
        uIDefaults.put("Tree[Enabled].openIconPainter", new LazyPainter("javax.swing.plaf.nimbus.TreePainter", 6, new Insets(5, 5, 5, 5), new Dimension(16, 16), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("Tree.openIcon", new NimbusIcon("Tree", "openIconPainter", 16, 16));
        uIDefaults.put("Tree[Enabled].collapsedIconPainter", new LazyPainter("javax.swing.plaf.nimbus.TreePainter", 7, new Insets(5, 5, 5, 5), new Dimension(18, 7), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("Tree[Enabled+Selected].collapsedIconPainter", new LazyPainter("javax.swing.plaf.nimbus.TreePainter", 8, new Insets(5, 5, 5, 5), new Dimension(18, 7), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("Tree.collapsedIcon", new NimbusIcon("Tree", "collapsedIconPainter", 18, 7));
        uIDefaults.put("Tree[Enabled].expandedIconPainter", new LazyPainter("javax.swing.plaf.nimbus.TreePainter", 9, new Insets(5, 5, 5, 5), new Dimension(18, 7), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("Tree[Enabled+Selected].expandedIconPainter", new LazyPainter("javax.swing.plaf.nimbus.TreePainter", 10, new Insets(5, 5, 5, 5), new Dimension(18, 7), false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0d, 1.0d));
        uIDefaults.put("Tree.expandedIcon", new NimbusIcon("Tree", "expandedIconPainter", 18, 7));
        uIDefaults.put("RootPane.contentMargins", new InsetsUIResource(0, 0, 0, 0));
        uIDefaults.put("RootPane.opaque", Boolean.TRUE);
        addColor(uIDefaults, "RootPane.background", "control", 0.0f, 0.0f, 0.0f, 0);
    }

    void register(Region region, String str) {
        if (region == null || str == null) {
            throw new IllegalArgumentException("Neither Region nor Prefix may be null");
        }
        List<LazyStyle> list = this.f12828m.get(region);
        if (list == null) {
            LinkedList linkedList = new LinkedList();
            linkedList.add(new LazyStyle(str));
            this.f12828m.put(region, linkedList);
        } else {
            Iterator<LazyStyle> it = list.iterator();
            while (it.hasNext()) {
                if (str.equals(it.next().prefix)) {
                    return;
                }
            }
            list.add(new LazyStyle(str));
        }
        this.registeredRegions.put(region.getName(), region);
    }

    SynthStyle getStyle(JComponent jComponent, Region region) {
        if (jComponent == null || region == null) {
            throw new IllegalArgumentException("Neither comp nor r may be null");
        }
        List<LazyStyle> list = this.f12828m.get(region);
        if (list == null || list.size() == 0) {
            return this.defaultStyle;
        }
        LazyStyle lazyStyle = null;
        for (LazyStyle lazyStyle2 : list) {
            if (lazyStyle2.matches(jComponent) && (lazyStyle == null || lazyStyle.parts.length < lazyStyle2.parts.length || (lazyStyle.parts.length == lazyStyle2.parts.length && lazyStyle.simple && !lazyStyle2.simple))) {
                lazyStyle = lazyStyle2;
            }
        }
        return lazyStyle == null ? this.defaultStyle : lazyStyle.getStyle(jComponent, region);
    }

    public void clearOverridesCache(JComponent jComponent) {
        this.overridesCache.remove(jComponent);
    }

    /* loaded from: rt.jar:javax/swing/plaf/nimbus/NimbusDefaults$DerivedFont.class */
    static final class DerivedFont implements UIDefaults.ActiveValue {
        private float sizeOffset;
        private Boolean bold;
        private Boolean italic;
        private String parentKey;

        public DerivedFont(String str, float f2, Boolean bool, Boolean bool2) {
            if (str == null) {
                throw new IllegalArgumentException("You must specify a key");
            }
            this.parentKey = str;
            this.sizeOffset = f2;
            this.bold = bool;
            this.italic = bool2;
        }

        @Override // javax.swing.UIDefaults.ActiveValue
        public Object createValue(UIDefaults uIDefaults) {
            Font font = uIDefaults.getFont(this.parentKey);
            if (font != null) {
                float fRound = Math.round(font.getSize2D() * this.sizeOffset);
                int style = font.getStyle();
                if (this.bold != null) {
                    if (this.bold.booleanValue()) {
                        style |= 1;
                    } else {
                        style &= -2;
                    }
                }
                if (this.italic != null) {
                    if (this.italic.booleanValue()) {
                        style |= 2;
                    } else {
                        style &= -3;
                    }
                }
                return font.deriveFont(style, fRound);
            }
            return null;
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/nimbus/NimbusDefaults$LazyPainter.class */
    private static final class LazyPainter implements UIDefaults.LazyValue {
        private int which;
        private AbstractRegionPainter.PaintContext ctx;
        private String className;

        LazyPainter(String str, int i2, Insets insets, Dimension dimension, boolean z2) {
            if (str == null) {
                throw new IllegalArgumentException("The className must be specified");
            }
            this.className = str;
            this.which = i2;
            this.ctx = new AbstractRegionPainter.PaintContext(insets, dimension, z2);
        }

        LazyPainter(String str, int i2, Insets insets, Dimension dimension, boolean z2, AbstractRegionPainter.PaintContext.CacheMode cacheMode, double d2, double d3) {
            if (str == null) {
                throw new IllegalArgumentException("The className must be specified");
            }
            this.className = str;
            this.which = i2;
            this.ctx = new AbstractRegionPainter.PaintContext(insets, dimension, z2, cacheMode, d2, d3);
        }

        /* JADX WARN: Removed duplicated region for block: B:6:0x0012 A[Catch: Exception -> 0x0081, TryCatch #0 {Exception -> 0x0081, blocks: (B:4:0x0004, B:9:0x0021, B:11:0x0048, B:12:0x0065, B:13:0x0066, B:6:0x0012, B:8:0x001d), top: B:18:0x0004 }] */
        @Override // javax.swing.UIDefaults.LazyValue
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public java.lang.Object createValue(javax.swing.UIDefaults r7) {
            /*
                r6 = this;
                r0 = r7
                if (r0 == 0) goto L12
                r0 = r7
                java.lang.String r1 = "ClassLoader"
                java.lang.Object r0 = r0.get(r1)     // Catch: java.lang.Exception -> L81
                r1 = r0
                r9 = r1
                boolean r0 = r0 instanceof java.lang.ClassLoader     // Catch: java.lang.Exception -> L81
                if (r0 != 0) goto L21
            L12:
                java.lang.Thread r0 = java.lang.Thread.currentThread()     // Catch: java.lang.Exception -> L81
                java.lang.ClassLoader r0 = r0.getContextClassLoader()     // Catch: java.lang.Exception -> L81
                r9 = r0
                r0 = r9
                if (r0 != 0) goto L21
                java.lang.ClassLoader r0 = java.lang.ClassLoader.getSystemClassLoader()     // Catch: java.lang.Exception -> L81
                r9 = r0
            L21:
                r0 = r6
                java.lang.String r0 = r0.className     // Catch: java.lang.Exception -> L81
                r1 = 1
                r2 = r9
                java.lang.ClassLoader r2 = (java.lang.ClassLoader) r2     // Catch: java.lang.Exception -> L81
                java.lang.Class r0 = java.lang.Class.forName(r0, r1, r2)     // Catch: java.lang.Exception -> L81
                r8 = r0
                r0 = r8
                r1 = 2
                java.lang.Class[] r1 = new java.lang.Class[r1]     // Catch: java.lang.Exception -> L81
                r2 = r1
                r3 = 0
                java.lang.Class<javax.swing.plaf.nimbus.AbstractRegionPainter$PaintContext> r4 = javax.swing.plaf.nimbus.AbstractRegionPainter.PaintContext.class
                r2[r3] = r4     // Catch: java.lang.Exception -> L81
                r2 = r1
                r3 = 1
                java.lang.Class<java.lang.Integer> r4 = java.lang.Integer.TYPE     // Catch: java.lang.Exception -> L81
                r2[r3] = r4     // Catch: java.lang.Exception -> L81
                java.lang.reflect.Constructor r0 = r0.getConstructor(r1)     // Catch: java.lang.Exception -> L81
                r10 = r0
                r0 = r10
                if (r0 != 0) goto L66
                java.lang.NullPointerException r0 = new java.lang.NullPointerException     // Catch: java.lang.Exception -> L81
                r1 = r0
                java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> L81
                r3 = r2
                r3.<init>()     // Catch: java.lang.Exception -> L81
                java.lang.String r3 = "Failed to find the constructor for the class: "
                java.lang.StringBuilder r2 = r2.append(r3)     // Catch: java.lang.Exception -> L81
                r3 = r6
                java.lang.String r3 = r3.className     // Catch: java.lang.Exception -> L81
                java.lang.StringBuilder r2 = r2.append(r3)     // Catch: java.lang.Exception -> L81
                java.lang.String r2 = r2.toString()     // Catch: java.lang.Exception -> L81
                r1.<init>(r2)     // Catch: java.lang.Exception -> L81
                throw r0     // Catch: java.lang.Exception -> L81
            L66:
                r0 = r10
                r1 = 2
                java.lang.Object[] r1 = new java.lang.Object[r1]     // Catch: java.lang.Exception -> L81
                r2 = r1
                r3 = 0
                r4 = r6
                javax.swing.plaf.nimbus.AbstractRegionPainter$PaintContext r4 = r4.ctx     // Catch: java.lang.Exception -> L81
                r2[r3] = r4     // Catch: java.lang.Exception -> L81
                r2 = r1
                r3 = 1
                r4 = r6
                int r4 = r4.which     // Catch: java.lang.Exception -> L81
                java.lang.Integer r4 = java.lang.Integer.valueOf(r4)     // Catch: java.lang.Exception -> L81
                r2[r3] = r4     // Catch: java.lang.Exception -> L81
                java.lang.Object r0 = r0.newInstance(r1)     // Catch: java.lang.Exception -> L81
                return r0
            L81:
                r8 = move-exception
                r0 = r8
                r0.printStackTrace()
                r0 = 0
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: javax.swing.plaf.nimbus.NimbusDefaults.LazyPainter.createValue(javax.swing.UIDefaults):java.lang.Object");
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/nimbus/NimbusDefaults$LazyStyle.class */
    private final class LazyStyle {
        private String prefix;
        private boolean simple;
        private Part[] parts;
        private NimbusStyle style;

        private LazyStyle(String str) {
            this.simple = true;
            if (str == null) {
                throw new IllegalArgumentException("The prefix must not be null");
            }
            this.prefix = str;
            String str2 = str;
            List<String> listSplit = split((str2.endsWith("cellRenderer\"") || str2.endsWith("renderer\"") || str2.endsWith("listRenderer\"")) ? str2.substring(str2.lastIndexOf(":\"") + 1) : str2);
            this.parts = new Part[listSplit.size()];
            for (int i2 = 0; i2 < this.parts.length; i2++) {
                this.parts[i2] = new Part(listSplit.get(i2));
                if (this.parts[i2].named) {
                    this.simple = false;
                }
            }
        }

        SynthStyle getStyle(JComponent jComponent, Region region) {
            if (jComponent.getClientProperty("Nimbus.Overrides") != null) {
                Map map = (Map) NimbusDefaults.this.overridesCache.get(jComponent);
                SynthStyle nimbusStyle = null;
                if (map == null) {
                    map = new HashMap();
                    NimbusDefaults.this.overridesCache.put(jComponent, map);
                } else {
                    nimbusStyle = (SynthStyle) map.get(region);
                }
                if (nimbusStyle == null) {
                    nimbusStyle = new NimbusStyle(this.prefix, jComponent);
                    map.put(region, nimbusStyle);
                }
                return nimbusStyle;
            }
            if (this.style == null) {
                this.style = new NimbusStyle(this.prefix, null);
            }
            return this.style;
        }

        boolean matches(JComponent jComponent) {
            return matches(jComponent, this.parts.length - 1);
        }

        private boolean matches(Component component, int i2) {
            if (i2 < 0) {
                return true;
            }
            if (component == null) {
                return false;
            }
            String name = component.getName();
            if (this.parts[i2].named && this.parts[i2].f12829s.equals(name)) {
                return matches(component.getParent(), i2 - 1);
            }
            if (this.parts[i2].named) {
                return false;
            }
            Class cls = this.parts[i2].f12830c;
            if (cls != null && cls.isAssignableFrom(component.getClass())) {
                return matches(component.getParent(), i2 - 1);
            }
            if (cls != null || !NimbusDefaults.this.registeredRegions.containsKey(this.parts[i2].f12829s)) {
                return false;
            }
            Region region = (Region) NimbusDefaults.this.registeredRegions.get(this.parts[i2].f12829s);
            Component parent = region.isSubregion() ? component : component.getParent();
            if (region == Region.INTERNAL_FRAME_TITLE_PANE && parent != null && (parent instanceof JInternalFrame.JDesktopIcon)) {
                parent = ((JInternalFrame.JDesktopIcon) parent).getInternalFrame();
            }
            return matches(parent, i2 - 1);
        }

        private List<String> split(String str) {
            ArrayList arrayList = new ArrayList();
            int i2 = 0;
            boolean z2 = false;
            int i3 = 0;
            for (int i4 = 0; i4 < str.length(); i4++) {
                char cCharAt = str.charAt(i4);
                if (cCharAt == '[') {
                    i2++;
                } else if (cCharAt == '\"') {
                    z2 = !z2;
                } else if (cCharAt == ']') {
                    i2--;
                    if (i2 < 0) {
                        throw new RuntimeException("Malformed prefix: " + str);
                    }
                } else if (cCharAt == ':' && !z2 && i2 == 0) {
                    arrayList.add(str.substring(i3, i4));
                    i3 = i4 + 1;
                }
            }
            if (i3 < str.length() - 1 && !z2 && i2 == 0) {
                arrayList.add(str.substring(i3));
            }
            return arrayList;
        }

        /* loaded from: rt.jar:javax/swing/plaf/nimbus/NimbusDefaults$LazyStyle$Part.class */
        private final class Part {

            /* renamed from: s, reason: collision with root package name */
            private String f12829s;
            private boolean named;

            /* renamed from: c, reason: collision with root package name */
            private Class f12830c;

            Part(String str) {
                this.named = str.charAt(0) == '\"' && str.charAt(str.length() - 1) == '\"';
                if (this.named) {
                    this.f12829s = str.substring(1, str.length() - 1);
                    return;
                }
                this.f12829s = str;
                try {
                    this.f12830c = Class.forName("javax.swing.J" + str);
                } catch (Exception e2) {
                }
                try {
                    this.f12830c = Class.forName(str.replace("_", "."));
                } catch (Exception e3) {
                }
            }
        }
    }

    private void addColor(UIDefaults uIDefaults, String str, int i2, int i3, int i4, int i5) {
        ColorUIResource colorUIResource = new ColorUIResource(new Color(i2, i3, i4, i5));
        this.colorTree.addColor(str, colorUIResource);
        uIDefaults.put(str, colorUIResource);
    }

    private void addColor(UIDefaults uIDefaults, String str, String str2, float f2, float f3, float f4, int i2) {
        addColor(uIDefaults, str, str2, f2, f3, f4, i2, true);
    }

    private void addColor(UIDefaults uIDefaults, String str, String str2, float f2, float f3, float f4, int i2, boolean z2) {
        uIDefaults.put(str, getDerivedColor(str, str2, f2, f3, f4, i2, z2));
    }

    public DerivedColor getDerivedColor(String str, float f2, float f3, float f4, int i2, boolean z2) {
        return getDerivedColor(null, str, f2, f3, f4, i2, z2);
    }

    private DerivedColor getDerivedColor(String str, String str2, float f2, float f3, float f4, int i2, boolean z2) {
        DerivedColor derivedColor;
        if (z2) {
            derivedColor = new DerivedColor.UIResource(str2, f2, f3, f4, i2);
        } else {
            derivedColor = new DerivedColor(str2, f2, f3, f4, i2);
        }
        if (this.derivedColors.containsKey(derivedColor)) {
            return this.derivedColors.get(derivedColor);
        }
        this.derivedColors.put(derivedColor, derivedColor);
        derivedColor.rederiveColor();
        this.colorTree.addColor(str, derivedColor);
        return derivedColor;
    }

    /* loaded from: rt.jar:javax/swing/plaf/nimbus/NimbusDefaults$ColorTree.class */
    private class ColorTree implements PropertyChangeListener {
        private Node root;
        private Map<String, Node> nodes;

        private ColorTree() {
            this.root = new Node(null, null);
            this.nodes = new HashMap();
        }

        public Color getColor(String str) {
            return this.nodes.get(str).color;
        }

        public void addColor(String str, Color color) {
            Node parentNode = getParentNode(color);
            Node node = new Node(color, parentNode);
            parentNode.children.add(node);
            if (str != null) {
                this.nodes.put(str, node);
            }
        }

        private Node getParentNode(Color color) {
            Node node = this.root;
            if (color instanceof DerivedColor) {
                Node node2 = this.nodes.get(((DerivedColor) color).getUiDefaultParentName());
                if (node2 != null) {
                    node = node2;
                }
            }
            return node;
        }

        public void update() {
            this.root.update();
        }

        @Override // java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            Node node = this.nodes.get(propertyChangeEvent.getPropertyName());
            if (node != null) {
                node.parent.children.remove(node);
                Color color = (Color) propertyChangeEvent.getNewValue();
                Node parentNode = getParentNode(color);
                node.set(color, parentNode);
                parentNode.children.add(node);
                node.update();
            }
        }

        /* loaded from: rt.jar:javax/swing/plaf/nimbus/NimbusDefaults$ColorTree$Node.class */
        class Node {
            Color color;
            Node parent;
            List<Node> children = new LinkedList();

            Node(Color color, Node node) {
                set(color, node);
            }

            public void set(Color color, Node node) {
                this.color = color;
                this.parent = node;
            }

            public void update() {
                if (this.color instanceof DerivedColor) {
                    ((DerivedColor) this.color).rederiveColor();
                }
                Iterator<Node> it = this.children.iterator();
                while (it.hasNext()) {
                    it.next().update();
                }
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/nimbus/NimbusDefaults$DefaultsListener.class */
    private class DefaultsListener implements PropertyChangeListener {
        private DefaultsListener() {
        }

        @Override // java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            if ("lookAndFeel".equals(propertyChangeEvent.getPropertyName())) {
                NimbusDefaults.this.colorTree.update();
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/nimbus/NimbusDefaults$PainterBorder.class */
    private static final class PainterBorder implements Border, UIResource {
        private Insets insets;
        private Painter painter;
        private String painterKey;

        PainterBorder(String str, Insets insets) {
            this.insets = insets;
            this.painterKey = str;
        }

        @Override // javax.swing.border.Border
        public void paintBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
            if (this.painter == null) {
                this.painter = (Painter) UIManager.get(this.painterKey);
                if (this.painter == null) {
                    return;
                }
            }
            graphics.translate(i2, i3);
            if (graphics instanceof Graphics2D) {
                this.painter.paint((Graphics2D) graphics, component, i4, i5);
            } else {
                BufferedImage bufferedImage = new BufferedImage(i4, i5, 2);
                Graphics2D graphics2DCreateGraphics = bufferedImage.createGraphics();
                this.painter.paint(graphics2DCreateGraphics, component, i4, i5);
                graphics2DCreateGraphics.dispose();
                graphics.drawImage(bufferedImage, i2, i3, null);
            }
            graphics.translate(-i2, -i3);
        }

        @Override // javax.swing.border.Border
        public Insets getBorderInsets(Component component) {
            return (Insets) this.insets.clone();
        }

        @Override // javax.swing.border.Border
        public boolean isBorderOpaque() {
            return false;
        }
    }
}
