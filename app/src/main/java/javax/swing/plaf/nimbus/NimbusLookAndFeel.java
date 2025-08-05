package javax.swing.plaf.nimbus;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.security.AccessController;
import java.util.HashMap;
import java.util.Map;
import javax.swing.GrayFilter;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JToolBar;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.nimbus.NimbusDefaults;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthLookAndFeel;
import javax.swing.plaf.synth.SynthStyle;
import javax.swing.plaf.synth.SynthStyleFactory;
import sun.security.action.GetPropertyAction;
import sun.swing.ImageIconUIResource;
import sun.swing.plaf.GTKKeybindings;
import sun.swing.plaf.WindowsKeybindings;
import sun.swing.plaf.synth.SynthIcon;

/* loaded from: rt.jar:javax/swing/plaf/nimbus/NimbusLookAndFeel.class */
public class NimbusLookAndFeel extends SynthLookAndFeel {
    private static final String[] COMPONENT_KEYS = {"ArrowButton", "Button", "CheckBox", "CheckBoxMenuItem", "ColorChooser", "ComboBox", "DesktopPane", "DesktopIcon", "EditorPane", "FileChooser", "FormattedTextField", "InternalFrame", "InternalFrameTitlePane", "Label", "List", "Menu", "MenuBar", "MenuItem", "OptionPane", "Panel", "PasswordField", "PopupMenu", "PopupMenuSeparator", "ProgressBar", "RadioButton", "RadioButtonMenuItem", "RootPane", "ScrollBar", "ScrollBarTrack", "ScrollBarThumb", "ScrollPane", "Separator", "Slider", "SliderTrack", "SliderThumb", "Spinner", "SplitPane", "TabbedPane", "Table", "TableHeader", "TextArea", "TextField", "TextPane", "ToggleButton", "ToolBar", "ToolTip", "Tree", "Viewport"};
    private UIDefaults uiDefaults;
    private DefaultsListener defaultsListener = new DefaultsListener();
    private Map<String, Map<String, Object>> compiledDefaults = null;
    private boolean defaultListenerAdded = false;
    private NimbusDefaults defaults = new NimbusDefaults();

    @Override // javax.swing.plaf.synth.SynthLookAndFeel, javax.swing.plaf.basic.BasicLookAndFeel, javax.swing.LookAndFeel
    public void initialize() {
        super.initialize();
        this.defaults.initialize();
        setStyleFactory(new SynthStyleFactory() { // from class: javax.swing.plaf.nimbus.NimbusLookAndFeel.1
            @Override // javax.swing.plaf.synth.SynthStyleFactory
            public SynthStyle getStyle(JComponent jComponent, Region region) {
                return NimbusLookAndFeel.this.defaults.getStyle(jComponent, region);
            }
        });
    }

    @Override // javax.swing.plaf.synth.SynthLookAndFeel, javax.swing.plaf.basic.BasicLookAndFeel, javax.swing.LookAndFeel
    public void uninitialize() {
        super.uninitialize();
        this.defaults.uninitialize();
        ImageCache.getInstance().flush();
        UIManager.getDefaults().removePropertyChangeListener(this.defaultsListener);
    }

    @Override // javax.swing.plaf.synth.SynthLookAndFeel, javax.swing.plaf.basic.BasicLookAndFeel, javax.swing.LookAndFeel
    public UIDefaults getDefaults() {
        if (this.uiDefaults == null) {
            String systemProperty = getSystemProperty("os.name");
            boolean z2 = systemProperty != null && systemProperty.contains("Windows");
            this.uiDefaults = super.getDefaults();
            this.defaults.initializeDefaults(this.uiDefaults);
            if (z2) {
                WindowsKeybindings.installKeybindings(this.uiDefaults);
            } else {
                GTKKeybindings.installKeybindings(this.uiDefaults);
            }
            this.uiDefaults.put("TitledBorder.titlePosition", 1);
            this.uiDefaults.put("TitledBorder.border", new BorderUIResource(new LoweredBorder()));
            this.uiDefaults.put("TitledBorder.titleColor", getDerivedColor("text", 0.0f, 0.0f, 0.23f, 0, true));
            this.uiDefaults.put("TitledBorder.font", new NimbusDefaults.DerivedFont("defaultFont", 1.0f, true, null));
            this.uiDefaults.put("OptionPane.isYesLast", Boolean.valueOf(!z2));
            this.uiDefaults.put("Table.scrollPaneCornerComponent", new UIDefaults.ActiveValue() { // from class: javax.swing.plaf.nimbus.NimbusLookAndFeel.2
                @Override // javax.swing.UIDefaults.ActiveValue
                public Object createValue(UIDefaults uIDefaults) {
                    return new TableScrollPaneCorner();
                }
            });
            this.uiDefaults.put("ToolBarSeparator[Enabled].backgroundPainter", new ToolBarSeparatorPainter());
            for (String str : COMPONENT_KEYS) {
                String str2 = str + ".foreground";
                if (!this.uiDefaults.containsKey(str2)) {
                    this.uiDefaults.put(str2, new NimbusProperty(str, "textForeground"));
                }
                String str3 = str + ".background";
                if (!this.uiDefaults.containsKey(str3)) {
                    this.uiDefaults.put(str3, new NimbusProperty(str, "background"));
                }
                String str4 = str + ".font";
                if (!this.uiDefaults.containsKey(str4)) {
                    this.uiDefaults.put(str4, new NimbusProperty(str, "font"));
                }
                String str5 = str + ".disabledText";
                if (!this.uiDefaults.containsKey(str5)) {
                    this.uiDefaults.put(str5, new NimbusProperty(str, "Disabled", "textForeground"));
                }
                String str6 = str + ".disabled";
                if (!this.uiDefaults.containsKey(str6)) {
                    this.uiDefaults.put(str6, new NimbusProperty(str, "Disabled", "background"));
                }
            }
            this.uiDefaults.put("FileView.computerIcon", new LinkProperty("FileChooser.homeFolderIcon"));
            this.uiDefaults.put("FileView.directoryIcon", new LinkProperty("FileChooser.directoryIcon"));
            this.uiDefaults.put("FileView.fileIcon", new LinkProperty("FileChooser.fileIcon"));
            this.uiDefaults.put("FileView.floppyDriveIcon", new LinkProperty("FileChooser.floppyDriveIcon"));
            this.uiDefaults.put("FileView.hardDriveIcon", new LinkProperty("FileChooser.hardDriveIcon"));
        }
        return this.uiDefaults;
    }

    public static NimbusStyle getStyle(JComponent jComponent, Region region) {
        return (NimbusStyle) SynthLookAndFeel.getStyle(jComponent, region);
    }

    @Override // javax.swing.plaf.synth.SynthLookAndFeel, javax.swing.LookAndFeel
    public String getName() {
        return "Nimbus";
    }

    @Override // javax.swing.plaf.synth.SynthLookAndFeel, javax.swing.LookAndFeel
    public String getID() {
        return "Nimbus";
    }

    @Override // javax.swing.plaf.synth.SynthLookAndFeel, javax.swing.LookAndFeel
    public String getDescription() {
        return "Nimbus Look and Feel";
    }

    @Override // javax.swing.plaf.synth.SynthLookAndFeel
    public boolean shouldUpdateStyleOnAncestorChanged() {
        return true;
    }

    @Override // javax.swing.plaf.synth.SynthLookAndFeel
    protected boolean shouldUpdateStyleOnEvent(PropertyChangeEvent propertyChangeEvent) {
        String propertyName = propertyChangeEvent.getPropertyName();
        if ("name" == propertyName || "ancestor" == propertyName || "Nimbus.Overrides" == propertyName || "Nimbus.Overrides.InheritDefaults" == propertyName || "JComponent.sizeVariant" == propertyName) {
            this.defaults.clearOverridesCache((JComponent) propertyChangeEvent.getSource());
            return true;
        }
        return super.shouldUpdateStyleOnEvent(propertyChangeEvent);
    }

    public void register(Region region, String str) {
        this.defaults.register(region, str);
    }

    private String getSystemProperty(String str) {
        return (String) AccessController.doPrivileged(new GetPropertyAction(str));
    }

    @Override // javax.swing.LookAndFeel
    public Icon getDisabledIcon(JComponent jComponent, Icon icon) {
        if (icon instanceof SynthIcon) {
            SynthIcon synthIcon = (SynthIcon) icon;
            BufferedImage bufferedImageCreateCompatibleTranslucentImage = EffectUtils.createCompatibleTranslucentImage(synthIcon.getIconWidth(), synthIcon.getIconHeight());
            Graphics2D graphics2DCreateGraphics = bufferedImageCreateCompatibleTranslucentImage.createGraphics();
            synthIcon.paintIcon(jComponent, graphics2DCreateGraphics, 0, 0);
            graphics2DCreateGraphics.dispose();
            return new ImageIconUIResource(GrayFilter.createDisabledImage(bufferedImageCreateCompatibleTranslucentImage));
        }
        return super.getDisabledIcon(jComponent, icon);
    }

    public Color getDerivedColor(String str, float f2, float f3, float f4, int i2, boolean z2) {
        return this.defaults.getDerivedColor(str, f2, f3, f4, i2, z2);
    }

    protected final Color getDerivedColor(Color color, Color color2, float f2, boolean z2) {
        int iDeriveARGB = deriveARGB(color, color2, f2);
        if (z2) {
            return new ColorUIResource(iDeriveARGB);
        }
        return new Color(iDeriveARGB);
    }

    protected final Color getDerivedColor(Color color, Color color2, float f2) {
        return getDerivedColor(color, color2, f2, true);
    }

    static Object resolveToolbarConstraint(JToolBar jToolBar) {
        Container parent;
        if (jToolBar != null && (parent = jToolBar.getParent()) != null) {
            LayoutManager layout = parent.getLayout();
            if (layout instanceof BorderLayout) {
                Object constraints = ((BorderLayout) layout).getConstraints(jToolBar);
                if (constraints == "South" || constraints == "East" || constraints == "West") {
                    return constraints;
                }
                return "North";
            }
            return "North";
        }
        return "North";
    }

    static int deriveARGB(Color color, Color color2, float f2) {
        return (((color.getAlpha() + Math.round((color2.getAlpha() - color.getAlpha()) * f2)) & 255) << 24) | (((color.getRed() + Math.round((color2.getRed() - color.getRed()) * f2)) & 255) << 16) | (((color.getGreen() + Math.round((color2.getGreen() - color.getGreen()) * f2)) & 255) << 8) | ((color.getBlue() + Math.round((color2.getBlue() - color.getBlue()) * f2)) & 255);
    }

    /* loaded from: rt.jar:javax/swing/plaf/nimbus/NimbusLookAndFeel$LinkProperty.class */
    private class LinkProperty implements UIDefaults.ActiveValue, UIResource {
        private String dstPropName;

        private LinkProperty(String str) {
            this.dstPropName = str;
        }

        @Override // javax.swing.UIDefaults.ActiveValue
        public Object createValue(UIDefaults uIDefaults) {
            return UIManager.get(this.dstPropName);
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/nimbus/NimbusLookAndFeel$NimbusProperty.class */
    private class NimbusProperty implements UIDefaults.ActiveValue, UIResource {
        private String prefix;
        private String state;
        private String suffix;
        private boolean isFont;

        private NimbusProperty(String str, String str2) {
            this.state = null;
            this.prefix = str;
            this.suffix = str2;
            this.isFont = "font".equals(str2);
        }

        private NimbusProperty(NimbusLookAndFeel nimbusLookAndFeel, String str, String str2, String str3) {
            this(str, str3);
            this.state = str2;
        }

        @Override // javax.swing.UIDefaults.ActiveValue
        public Object createValue(UIDefaults uIDefaults) {
            Object obj = null;
            if (this.state != null) {
                obj = NimbusLookAndFeel.this.uiDefaults.get(this.prefix + "[" + this.state + "]." + this.suffix);
            }
            if (obj == null) {
                obj = NimbusLookAndFeel.this.uiDefaults.get(this.prefix + "[Enabled]." + this.suffix);
            }
            if (obj == null) {
                obj = this.isFont ? NimbusLookAndFeel.this.uiDefaults.get("defaultFont") : NimbusLookAndFeel.this.uiDefaults.get(this.suffix);
            }
            return obj;
        }
    }

    static String parsePrefix(String str) {
        if (str == null) {
            return null;
        }
        boolean z2 = false;
        for (int i2 = 0; i2 < str.length(); i2++) {
            char cCharAt = str.charAt(i2);
            if (cCharAt == '\"') {
                z2 = !z2;
            } else if ((cCharAt == '[' || cCharAt == '.') && !z2) {
                return str.substring(0, i2);
            }
        }
        return null;
    }

    Map<String, Object> getDefaultsForPrefix(String str) {
        if (this.compiledDefaults == null) {
            this.compiledDefaults = new HashMap();
            for (Map.Entry<Object, Object> entry : UIManager.getDefaults().entrySet()) {
                if (entry.getKey() instanceof String) {
                    addDefault((String) entry.getKey(), entry.getValue());
                }
            }
            if (!this.defaultListenerAdded) {
                UIManager.getDefaults().addPropertyChangeListener(this.defaultsListener);
                this.defaultListenerAdded = true;
            }
        }
        return this.compiledDefaults.get(str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addDefault(String str, Object obj) {
        String prefix;
        if (this.compiledDefaults != null && (prefix = parsePrefix(str)) != null) {
            Map<String, Object> map = this.compiledDefaults.get(prefix);
            if (map == null) {
                map = new HashMap();
                this.compiledDefaults.put(prefix, map);
            }
            map.put(str, obj);
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/nimbus/NimbusLookAndFeel$DefaultsListener.class */
    private class DefaultsListener implements PropertyChangeListener {
        private DefaultsListener() {
        }

        @Override // java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            String propertyName = propertyChangeEvent.getPropertyName();
            if ("UIDefaults".equals(propertyName)) {
                NimbusLookAndFeel.this.compiledDefaults = null;
            } else {
                NimbusLookAndFeel.this.addDefault(propertyName, propertyChangeEvent.getNewValue());
            }
        }
    }
}
