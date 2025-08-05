package javax.swing.plaf.synth;

import com.sun.xml.internal.ws.model.RuntimeModeler;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.NotSerializableException;
import java.io.ObjectOutputStream;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.security.AccessController;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.InsetsUIResource;
import javax.swing.plaf.basic.BasicLookAndFeel;
import sun.awt.AppContext;
import sun.awt.SunToolkit;
import sun.security.action.GetPropertyAction;
import sun.swing.DefaultLookup;
import sun.swing.SwingUtilities2;
import sun.swing.plaf.synth.SynthFileChooserUI;

/* loaded from: rt.jar:javax/swing/plaf/synth/SynthLookAndFeel.class */
public class SynthLookAndFeel extends BasicLookAndFeel {
    private static SynthStyleFactory lastFactory;
    private static AppContext lastContext;
    private Map<String, Object> defaultsMap;
    static final Insets EMPTY_UIRESOURCE_INSETS = new InsetsUIResource(0, 0, 0, 0);
    private static final Object STYLE_FACTORY_KEY = new StringBuffer("com.sun.java.swing.plaf.gtk.StyleCache");
    private static final Object SELECTED_UI_KEY = new StringBuilder("selectedUI");
    private static final Object SELECTED_UI_STATE_KEY = new StringBuilder("selectedUIState");
    private static ReferenceQueue<LookAndFeel> queue = new ReferenceQueue<>();
    private SynthStyleFactory factory = new DefaultSynthStyleFactory();
    private Handler _handler = new Handler();

    static ComponentUI getSelectedUI() {
        return (ComponentUI) AppContext.getAppContext().get(SELECTED_UI_KEY);
    }

    static void setSelectedUI(ComponentUI componentUI, boolean z2, boolean z3, boolean z4, boolean z5) {
        int i2;
        if (z2) {
            i2 = 512;
            if (z3) {
                i2 = 512 | 256;
            }
        } else if (z5 && z4) {
            i2 = 0 | 3;
            if (z3) {
                i2 |= 256;
            }
        } else if (z4) {
            i2 = 0 | 1;
            if (z3) {
                i2 |= 256;
            }
        } else {
            i2 = 0 | 8;
        }
        AppContext appContext = AppContext.getAppContext();
        appContext.put(SELECTED_UI_KEY, componentUI);
        appContext.put(SELECTED_UI_STATE_KEY, Integer.valueOf(i2));
    }

    static int getSelectedUIState() {
        Integer num = (Integer) AppContext.getAppContext().get(SELECTED_UI_STATE_KEY);
        if (num == null) {
            return 0;
        }
        return num.intValue();
    }

    static void resetSelectedUI() {
        AppContext.getAppContext().remove(SELECTED_UI_KEY);
    }

    public static void setStyleFactory(SynthStyleFactory synthStyleFactory) {
        synchronized (SynthLookAndFeel.class) {
            AppContext appContext = AppContext.getAppContext();
            lastFactory = synthStyleFactory;
            lastContext = appContext;
            appContext.put(STYLE_FACTORY_KEY, synthStyleFactory);
        }
    }

    public static SynthStyleFactory getStyleFactory() {
        synchronized (SynthLookAndFeel.class) {
            AppContext appContext = AppContext.getAppContext();
            if (lastContext == appContext) {
                return lastFactory;
            }
            lastContext = appContext;
            lastFactory = (SynthStyleFactory) appContext.get(STYLE_FACTORY_KEY);
            return lastFactory;
        }
    }

    static int getComponentState(Component component) {
        if (component.isEnabled()) {
            if (component.isFocusOwner()) {
                return 257;
            }
            return 1;
        }
        return 8;
    }

    public static SynthStyle getStyle(JComponent jComponent, Region region) {
        return getStyleFactory().getStyle(jComponent, region);
    }

    static boolean shouldUpdateStyle(PropertyChangeEvent propertyChangeEvent) {
        LookAndFeel lookAndFeel = UIManager.getLookAndFeel();
        return (lookAndFeel instanceof SynthLookAndFeel) && ((SynthLookAndFeel) lookAndFeel).shouldUpdateStyleOnEvent(propertyChangeEvent);
    }

    static SynthStyle updateStyle(SynthContext synthContext, SynthUI synthUI) {
        SynthStyle style = getStyle(synthContext.getComponent(), synthContext.getRegion());
        SynthStyle style2 = synthContext.getStyle();
        if (style != style2) {
            if (style2 != null) {
                style2.uninstallDefaults(synthContext);
            }
            synthContext.setStyle(style);
            style.installDefaults(synthContext, synthUI);
        }
        return style;
    }

    public static void updateStyles(Component component) {
        if (component instanceof JComponent) {
            String name = component.getName();
            component.setName(null);
            if (name != null) {
                component.setName(name);
            }
            ((JComponent) component).revalidate();
        }
        Component[] components = null;
        if (component instanceof JMenu) {
            components = ((JMenu) component).getMenuComponents();
        } else if (component instanceof Container) {
            components = ((Container) component).getComponents();
        }
        if (components != null) {
            for (Component component2 : components) {
                updateStyles(component2);
            }
        }
        component.repaint();
    }

    public static Region getRegion(JComponent jComponent) {
        return Region.getRegion(jComponent);
    }

    static Insets getPaintingInsets(SynthContext synthContext, Insets insets) {
        Insets insets2;
        if (synthContext.isSubregion()) {
            insets2 = synthContext.getStyle().getInsets(synthContext, insets);
        } else {
            insets2 = synthContext.getComponent().getInsets(insets);
        }
        return insets2;
    }

    static void update(SynthContext synthContext, Graphics graphics) {
        paintRegion(synthContext, graphics, null);
    }

    static void updateSubregion(SynthContext synthContext, Graphics graphics, Rectangle rectangle) {
        paintRegion(synthContext, graphics, rectangle);
    }

    private static void paintRegion(SynthContext synthContext, Graphics graphics, Rectangle rectangle) {
        int i2;
        int i3;
        int width;
        int height;
        JComponent component = synthContext.getComponent();
        SynthStyle style = synthContext.getStyle();
        if (rectangle == null) {
            i2 = 0;
            i3 = 0;
            width = component.getWidth();
            height = component.getHeight();
        } else {
            i2 = rectangle.f12372x;
            i3 = rectangle.f12373y;
            width = rectangle.width;
            height = rectangle.height;
        }
        boolean zIsSubregion = synthContext.isSubregion();
        if ((zIsSubregion && style.isOpaque(synthContext)) || (!zIsSubregion && component.isOpaque())) {
            graphics.setColor(style.getColor(synthContext, ColorType.BACKGROUND));
            graphics.fillRect(i2, i3, width, height);
        }
    }

    static boolean isLeftToRight(Component component) {
        return component.getComponentOrientation().isLeftToRight();
    }

    static Object getUIOfType(ComponentUI componentUI, Class cls) {
        if (cls.isInstance(componentUI)) {
            return componentUI;
        }
        return null;
    }

    public static ComponentUI createUI(JComponent jComponent) {
        String strIntern = jComponent.getUIClassID().intern();
        if (strIntern == "ButtonUI") {
            return SynthButtonUI.createUI(jComponent);
        }
        if (strIntern == "CheckBoxUI") {
            return SynthCheckBoxUI.createUI(jComponent);
        }
        if (strIntern == "CheckBoxMenuItemUI") {
            return SynthCheckBoxMenuItemUI.createUI(jComponent);
        }
        if (strIntern == "ColorChooserUI") {
            return SynthColorChooserUI.createUI(jComponent);
        }
        if (strIntern == "ComboBoxUI") {
            return SynthComboBoxUI.createUI(jComponent);
        }
        if (strIntern == "DesktopPaneUI") {
            return SynthDesktopPaneUI.createUI(jComponent);
        }
        if (strIntern == "DesktopIconUI") {
            return SynthDesktopIconUI.createUI(jComponent);
        }
        if (strIntern == "EditorPaneUI") {
            return SynthEditorPaneUI.createUI(jComponent);
        }
        if (strIntern == "FileChooserUI") {
            return SynthFileChooserUI.createUI(jComponent);
        }
        if (strIntern == "FormattedTextFieldUI") {
            return SynthFormattedTextFieldUI.createUI(jComponent);
        }
        if (strIntern == "InternalFrameUI") {
            return SynthInternalFrameUI.createUI(jComponent);
        }
        if (strIntern == "LabelUI") {
            return SynthLabelUI.createUI(jComponent);
        }
        if (strIntern == "ListUI") {
            return SynthListUI.createUI(jComponent);
        }
        if (strIntern == "MenuBarUI") {
            return SynthMenuBarUI.createUI(jComponent);
        }
        if (strIntern == "MenuUI") {
            return SynthMenuUI.createUI(jComponent);
        }
        if (strIntern == "MenuItemUI") {
            return SynthMenuItemUI.createUI(jComponent);
        }
        if (strIntern == "OptionPaneUI") {
            return SynthOptionPaneUI.createUI(jComponent);
        }
        if (strIntern == "PanelUI") {
            return SynthPanelUI.createUI(jComponent);
        }
        if (strIntern == "PasswordFieldUI") {
            return SynthPasswordFieldUI.createUI(jComponent);
        }
        if (strIntern == "PopupMenuSeparatorUI") {
            return SynthSeparatorUI.createUI(jComponent);
        }
        if (strIntern == "PopupMenuUI") {
            return SynthPopupMenuUI.createUI(jComponent);
        }
        if (strIntern == "ProgressBarUI") {
            return SynthProgressBarUI.createUI(jComponent);
        }
        if (strIntern == "RadioButtonUI") {
            return SynthRadioButtonUI.createUI(jComponent);
        }
        if (strIntern == "RadioButtonMenuItemUI") {
            return SynthRadioButtonMenuItemUI.createUI(jComponent);
        }
        if (strIntern == "RootPaneUI") {
            return SynthRootPaneUI.createUI(jComponent);
        }
        if (strIntern == "ScrollBarUI") {
            return SynthScrollBarUI.createUI(jComponent);
        }
        if (strIntern == "ScrollPaneUI") {
            return SynthScrollPaneUI.createUI(jComponent);
        }
        if (strIntern == "SeparatorUI") {
            return SynthSeparatorUI.createUI(jComponent);
        }
        if (strIntern == "SliderUI") {
            return SynthSliderUI.createUI(jComponent);
        }
        if (strIntern == "SpinnerUI") {
            return SynthSpinnerUI.createUI(jComponent);
        }
        if (strIntern == "SplitPaneUI") {
            return SynthSplitPaneUI.createUI(jComponent);
        }
        if (strIntern == "TabbedPaneUI") {
            return SynthTabbedPaneUI.createUI(jComponent);
        }
        if (strIntern == "TableUI") {
            return SynthTableUI.createUI(jComponent);
        }
        if (strIntern == "TableHeaderUI") {
            return SynthTableHeaderUI.createUI(jComponent);
        }
        if (strIntern == "TextAreaUI") {
            return SynthTextAreaUI.createUI(jComponent);
        }
        if (strIntern == "TextFieldUI") {
            return SynthTextFieldUI.createUI(jComponent);
        }
        if (strIntern == "TextPaneUI") {
            return SynthTextPaneUI.createUI(jComponent);
        }
        if (strIntern == "ToggleButtonUI") {
            return SynthToggleButtonUI.createUI(jComponent);
        }
        if (strIntern == "ToolBarSeparatorUI") {
            return SynthSeparatorUI.createUI(jComponent);
        }
        if (strIntern == "ToolBarUI") {
            return SynthToolBarUI.createUI(jComponent);
        }
        if (strIntern == "ToolTipUI") {
            return SynthToolTipUI.createUI(jComponent);
        }
        if (strIntern == "TreeUI") {
            return SynthTreeUI.createUI(jComponent);
        }
        if (strIntern == "ViewportUI") {
            return SynthViewportUI.createUI(jComponent);
        }
        return null;
    }

    public void load(InputStream inputStream, Class<?> cls) throws ParseException, IllegalArgumentException {
        if (cls == null) {
            throw new IllegalArgumentException("You must supply a valid resource base Class");
        }
        if (this.defaultsMap == null) {
            this.defaultsMap = new HashMap();
        }
        new SynthParser().parse(inputStream, (DefaultSynthStyleFactory) this.factory, null, cls, this.defaultsMap);
    }

    public void load(URL url) throws IOException, ParseException, IllegalArgumentException {
        if (url == null) {
            throw new IllegalArgumentException("You must supply a valid Synth set URL");
        }
        if (this.defaultsMap == null) {
            this.defaultsMap = new HashMap();
        }
        new SynthParser().parse(url.openStream(), (DefaultSynthStyleFactory) this.factory, url, null, this.defaultsMap);
    }

    @Override // javax.swing.plaf.basic.BasicLookAndFeel, javax.swing.LookAndFeel
    public void initialize() {
        super.initialize();
        DefaultLookup.setDefaultLookup(new SynthDefaultLookup());
        setStyleFactory(this.factory);
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addPropertyChangeListener(this._handler);
    }

    @Override // javax.swing.plaf.basic.BasicLookAndFeel, javax.swing.LookAndFeel
    public void uninitialize() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().removePropertyChangeListener(this._handler);
        super.uninitialize();
    }

    @Override // javax.swing.plaf.basic.BasicLookAndFeel, javax.swing.LookAndFeel
    public UIDefaults getDefaults() {
        UIDefaults uIDefaults = new UIDefaults(60, 0.75f);
        Region.registerUIs(uIDefaults);
        uIDefaults.setDefaultLocale(Locale.getDefault());
        uIDefaults.addResourceBundle("com.sun.swing.internal.plaf.basic.resources.basic");
        uIDefaults.addResourceBundle("com.sun.swing.internal.plaf.synth.resources.synth");
        uIDefaults.put("TabbedPane.isTabRollover", Boolean.TRUE);
        uIDefaults.put("ColorChooser.swatchesRecentSwatchSize", new Dimension(10, 10));
        uIDefaults.put("ColorChooser.swatchesDefaultRecentColor", Color.RED);
        uIDefaults.put("ColorChooser.swatchesSwatchSize", new Dimension(10, 10));
        uIDefaults.put("html.pendingImage", SwingUtilities2.makeIcon(getClass(), BasicLookAndFeel.class, "icons/image-delayed.png"));
        uIDefaults.put("html.missingImage", SwingUtilities2.makeIcon(getClass(), BasicLookAndFeel.class, "icons/image-failed.png"));
        uIDefaults.put("PopupMenu.selectedWindowInputMapBindings", new Object[]{"ESCAPE", "cancel", "DOWN", "selectNext", "KP_DOWN", "selectNext", "UP", "selectPrevious", "KP_UP", "selectPrevious", "LEFT", "selectParent", "KP_LEFT", "selectParent", "RIGHT", "selectChild", "KP_RIGHT", "selectChild", "ENTER", RuntimeModeler.RETURN, "SPACE", RuntimeModeler.RETURN});
        uIDefaults.put("PopupMenu.selectedWindowInputMapBindings.RightToLeft", new Object[]{"LEFT", "selectChild", "KP_LEFT", "selectChild", "RIGHT", "selectParent", "KP_RIGHT", "selectParent"});
        flushUnreferenced();
        uIDefaults.put(SwingUtilities2.AA_TEXT_PROPERTY_KEY, getAATextInfo());
        new AATextListener(this);
        if (this.defaultsMap != null) {
            uIDefaults.putAll(this.defaultsMap);
        }
        return uIDefaults;
    }

    @Override // javax.swing.LookAndFeel
    public boolean isSupportedLookAndFeel() {
        return true;
    }

    @Override // javax.swing.LookAndFeel
    public boolean isNativeLookAndFeel() {
        return false;
    }

    @Override // javax.swing.LookAndFeel
    public String getDescription() {
        return "Synth look and feel";
    }

    @Override // javax.swing.LookAndFeel
    public String getName() {
        return "Synth look and feel";
    }

    @Override // javax.swing.LookAndFeel
    public String getID() {
        return "Synth";
    }

    public boolean shouldUpdateStyleOnAncestorChanged() {
        return false;
    }

    protected boolean shouldUpdateStyleOnEvent(PropertyChangeEvent propertyChangeEvent) {
        String propertyName = propertyChangeEvent.getPropertyName();
        if ("name" == propertyName || "componentOrientation" == propertyName) {
            return true;
        }
        if ("ancestor" == propertyName && propertyChangeEvent.getNewValue() != null) {
            return shouldUpdateStyleOnAncestorChanged();
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Object getAATextInfo() {
        String language = Locale.getDefault().getLanguage();
        return SwingUtilities2.AATextInfo.getAATextInfo(SwingUtilities2.isLocalDisplay() && !("gnome".equals((String) AccessController.doPrivileged(new GetPropertyAction("sun.desktop"))) && (Locale.CHINESE.getLanguage().equals(language) || Locale.JAPANESE.getLanguage().equals(language) || Locale.KOREAN.getLanguage().equals(language))));
    }

    private static void flushUnreferenced() {
        while (true) {
            AATextListener aATextListener = (AATextListener) queue.poll();
            if (aATextListener != null) {
                aATextListener.dispose();
            } else {
                return;
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/synth/SynthLookAndFeel$AATextListener.class */
    private static class AATextListener extends WeakReference<LookAndFeel> implements PropertyChangeListener {
        private String key;
        private static boolean updatePending;

        AATextListener(LookAndFeel lookAndFeel) {
            super(lookAndFeel, SynthLookAndFeel.queue);
            this.key = SunToolkit.DESKTOPFONTHINTS;
            Toolkit.getDefaultToolkit().addPropertyChangeListener(this.key, this);
        }

        @Override // java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            UIDefaults lookAndFeelDefaults = UIManager.getLookAndFeelDefaults();
            if (lookAndFeelDefaults.getBoolean("Synth.doNotSetTextAA")) {
                dispose();
                return;
            }
            LookAndFeel lookAndFeel = get();
            if (lookAndFeel != null && lookAndFeel == UIManager.getLookAndFeel()) {
                lookAndFeelDefaults.put(SwingUtilities2.AA_TEXT_PROPERTY_KEY, SynthLookAndFeel.getAATextInfo());
                updateUI();
                return;
            }
            dispose();
        }

        void dispose() {
            Toolkit.getDefaultToolkit().removePropertyChangeListener(this.key, this);
        }

        private static void updateWindowUI(Window window) {
            SynthLookAndFeel.updateStyles(window);
            for (Window window2 : window.getOwnedWindows()) {
                updateWindowUI(window2);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static void updateAllUIs() {
            for (Frame frame : Frame.getFrames()) {
                updateWindowUI(frame);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static synchronized void setUpdatePending(boolean z2) {
            updatePending = z2;
        }

        private static synchronized boolean isUpdatePending() {
            return updatePending;
        }

        protected void updateUI() {
            if (!isUpdatePending()) {
                setUpdatePending(true);
                SwingUtilities.invokeLater(new Runnable() { // from class: javax.swing.plaf.synth.SynthLookAndFeel.AATextListener.1
                    @Override // java.lang.Runnable
                    public void run() {
                        AATextListener.updateAllUIs();
                        AATextListener.setUpdatePending(false);
                    }
                });
            }
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        throw new NotSerializableException(getClass().getName());
    }

    /* loaded from: rt.jar:javax/swing/plaf/synth/SynthLookAndFeel$Handler.class */
    private class Handler implements PropertyChangeListener {
        private Handler() {
        }

        @Override // java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            String propertyName = propertyChangeEvent.getPropertyName();
            Object newValue = propertyChangeEvent.getNewValue();
            Object oldValue = propertyChangeEvent.getOldValue();
            if ("focusOwner" == propertyName) {
                if (oldValue instanceof JComponent) {
                    repaintIfBackgroundsDiffer((JComponent) oldValue);
                }
                if (newValue instanceof JComponent) {
                    repaintIfBackgroundsDiffer((JComponent) newValue);
                    return;
                }
                return;
            }
            if ("managingFocus" == propertyName) {
                KeyboardFocusManager keyboardFocusManager = (KeyboardFocusManager) propertyChangeEvent.getSource();
                if (newValue.equals(Boolean.FALSE)) {
                    keyboardFocusManager.removePropertyChangeListener(SynthLookAndFeel.this._handler);
                } else {
                    keyboardFocusManager.addPropertyChangeListener(SynthLookAndFeel.this._handler);
                }
            }
        }

        private void repaintIfBackgroundsDiffer(JComponent jComponent) {
            Object obj = (ComponentUI) jComponent.getClientProperty(SwingUtilities2.COMPONENT_UI_PROPERTY_KEY);
            if (obj instanceof SynthUI) {
                SynthContext context = ((SynthUI) obj).getContext(jComponent);
                SynthStyle style = context.getStyle();
                int componentState = context.getComponentState();
                Color color = style.getColor(context, ColorType.BACKGROUND);
                int i2 = componentState ^ 256;
                context.setComponentState(i2);
                Color color2 = style.getColor(context, ColorType.BACKGROUND);
                context.setComponentState(i2 ^ 256);
                if (color != null && !color.equals(color2)) {
                    jComponent.repaint();
                }
                context.dispose();
            }
        }
    }
}
