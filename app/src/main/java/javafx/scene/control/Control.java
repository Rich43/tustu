package javafx.scene.control;

import com.sun.javafx.application.PlatformImpl;
import com.sun.javafx.css.CssError;
import com.sun.javafx.css.StyleManager;
import com.sun.javafx.css.converters.StringConverter;
import com.sun.javafx.scene.control.ControlAcceleratorSupport;
import com.sun.javafx.scene.control.Logging;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.WritableValue;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.css.StyleableStringProperty;
import javafx.event.EventHandler;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleAttribute;
import javafx.scene.Node;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.Region;
import org.icepdf.core.util.PdfOps;
import sun.util.logging.PlatformLogger;

/* loaded from: jfxrt.jar:javafx/scene/control/Control.class */
public abstract class Control extends Region implements Skinnable {
    private List<CssMetaData<? extends Styleable, ?>> styleableProperties;
    private SkinBase<?> skinBase;
    private static final EventHandler<ContextMenuEvent> contextMenuHandler;
    private ObjectProperty<Tooltip> tooltip;
    private StringProperty skinClassName;
    static final /* synthetic */ boolean $assertionsDisabled;
    private ObjectProperty<Skin<?>> skin = new StyleableObjectProperty<Skin<?>>() { // from class: javafx.scene.control.Control.1
        private Skin<?> oldValue;

        @Override // javafx.css.StyleableObjectProperty, javafx.beans.property.ObjectPropertyBase, javafx.beans.value.WritableObjectValue
        public void set(Skin<?> v2) {
            if (v2 == null) {
                if (this.oldValue == null) {
                    return;
                }
            } else if (this.oldValue != null && v2.getClass().equals(this.oldValue.getClass())) {
                return;
            }
            super.set((AnonymousClass1) v2);
        }

        @Override // javafx.beans.property.ObjectPropertyBase
        protected void invalidated() {
            Skin<?> skin = get();
            Control.this.currentSkinClassName = skin == null ? null : skin.getClass().getName();
            Control.this.skinClassNameProperty().set(Control.this.currentSkinClassName);
            if (this.oldValue != null) {
                this.oldValue.dispose();
            }
            this.oldValue = skin;
            Control.this.skinBase = null;
            if (skin instanceof SkinBase) {
                Control.this.skinBase = (SkinBase) skin;
            } else {
                Node n2 = Control.this.getSkinNode();
                if (n2 != null) {
                    Control.this.getChildren().setAll(n2);
                } else {
                    Control.this.getChildren().clear();
                }
            }
            Control.this.styleableProperties = null;
            Control.this.impl_reapplyCSS();
            PlatformLogger logger = Logging.getControlsLogger();
            if (logger.isLoggable(PlatformLogger.Level.FINEST)) {
                logger.finest("Stored skin[" + ((Object) getValue2()) + "] on " + ((Object) this));
            }
        }

        @Override // javafx.css.StyleableProperty
        public CssMetaData getCssMetaData() {
            return StyleableProperties.SKIN;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return Control.this;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return "skin";
        }
    };
    private ObjectProperty<ContextMenu> contextMenu = new SimpleObjectProperty<ContextMenu>(this, "contextMenu") { // from class: javafx.scene.control.Control.3
        private WeakReference<ContextMenu> contextMenuRef;

        @Override // javafx.beans.property.ObjectPropertyBase
        protected void invalidated() {
            ContextMenu oldMenu = this.contextMenuRef == null ? null : this.contextMenuRef.get();
            if (oldMenu != null) {
                ControlAcceleratorSupport.removeAcceleratorsFromScene(oldMenu.getItems(), Control.this);
            }
            ContextMenu ctx = get();
            this.contextMenuRef = new WeakReference<>(ctx);
            if (ctx != null) {
                ctx.setImpl_showRelativeToWindow(true);
                ControlAcceleratorSupport.addAcceleratorsIntoScene(ctx.getItems(), (Node) Control.this);
            }
        }
    };
    private String currentSkinClassName = null;

    static {
        $assertionsDisabled = !Control.class.desiredAssertionStatus();
        if (Application.getUserAgentStylesheet() == null) {
            PlatformImpl.setDefaultPlatformUserAgentStylesheet();
        }
        contextMenuHandler = event -> {
            if (event.isConsumed()) {
                return;
            }
            Object source = event.getSource();
            if (source instanceof Control) {
                Control c2 = (Control) source;
                if (c2.getContextMenu() != null) {
                    c2.getContextMenu().show(c2, event.getScreenX(), event.getScreenY());
                    event.consume();
                }
            }
        };
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x0028  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static java.lang.Class<?> loadClass(java.lang.String r4, java.lang.Object r5) throws java.lang.ClassNotFoundException {
        /*
            r0 = r4
            r1 = 0
            java.lang.Class<javafx.scene.control.Control> r2 = javafx.scene.control.Control.class
            java.lang.ClassLoader r2 = r2.getClassLoader()     // Catch: java.lang.ClassNotFoundException -> Lb
            java.lang.Class r0 = java.lang.Class.forName(r0, r1, r2)     // Catch: java.lang.ClassNotFoundException -> Lb
            return r0
        Lb:
            r6 = move-exception
            java.lang.Thread r0 = java.lang.Thread.currentThread()
            java.lang.ClassLoader r0 = r0.getContextClassLoader()
            if (r0 == 0) goto L24
            java.lang.Thread r0 = java.lang.Thread.currentThread()     // Catch: java.lang.ClassNotFoundException -> L23
            java.lang.ClassLoader r0 = r0.getContextClassLoader()     // Catch: java.lang.ClassNotFoundException -> L23
            r7 = r0
            r0 = r4
            r1 = 0
            r2 = r7
            java.lang.Class r0 = java.lang.Class.forName(r0, r1, r2)     // Catch: java.lang.ClassNotFoundException -> L23
            return r0
        L23:
            r7 = move-exception
        L24:
            r0 = r5
            if (r0 == 0) goto L49
            r0 = r5
            java.lang.Class r0 = r0.getClass()
            r7 = r0
        L2d:
            r0 = r7
            if (r0 == 0) goto L49
            r0 = r7
            java.lang.ClassLoader r0 = r0.getClassLoader()     // Catch: java.lang.ClassNotFoundException -> L3f
            r8 = r0
            r0 = r4
            r1 = 0
            r2 = r8
            java.lang.Class r0 = java.lang.Class.forName(r0, r1, r2)     // Catch: java.lang.ClassNotFoundException -> L3f
            return r0
        L3f:
            r8 = move-exception
            r0 = r7
            java.lang.Class r0 = r0.getSuperclass()
            r7 = r0
            goto L2d
        L49:
            r0 = r6
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: javafx.scene.control.Control.loadClass(java.lang.String, java.lang.Object):java.lang.Class");
    }

    @Override // javafx.scene.control.Skinnable
    public final ObjectProperty<Skin<?>> skinProperty() {
        return this.skin;
    }

    @Override // javafx.scene.control.Skinnable
    public final void setSkin(Skin<?> value) {
        skinProperty().set(value);
    }

    @Override // javafx.scene.control.Skinnable
    public final Skin<?> getSkin() {
        return skinProperty().getValue2();
    }

    public final ObjectProperty<Tooltip> tooltipProperty() {
        if (this.tooltip == null) {
            this.tooltip = new ObjectPropertyBase<Tooltip>() { // from class: javafx.scene.control.Control.2
                private Tooltip old = null;

                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    Tooltip t2 = get();
                    if (t2 != this.old) {
                        if (this.old != null) {
                            Tooltip.uninstall(Control.this, this.old);
                        }
                        if (t2 != null) {
                            Tooltip.install(Control.this, t2);
                        }
                        this.old = t2;
                    }
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Control.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "tooltip";
                }
            };
        }
        return this.tooltip;
    }

    public final void setTooltip(Tooltip value) {
        tooltipProperty().setValue(value);
    }

    public final Tooltip getTooltip() {
        if (this.tooltip == null) {
            return null;
        }
        return this.tooltip.getValue2();
    }

    public final ObjectProperty<ContextMenu> contextMenuProperty() {
        return this.contextMenu;
    }

    public final void setContextMenu(ContextMenu value) {
        this.contextMenu.setValue(value);
    }

    public final ContextMenu getContextMenu() {
        if (this.contextMenu == null) {
            return null;
        }
        return this.contextMenu.getValue2();
    }

    protected Control() {
        StyleableProperty<Boolean> prop = (StyleableProperty) focusTraversableProperty();
        prop.applyStyle(null, Boolean.TRUE);
        addEventHandler(ContextMenuEvent.CONTEXT_MENU_REQUESTED, contextMenuHandler);
    }

    @Override // javafx.scene.layout.Region, javafx.scene.Node
    public boolean isResizable() {
        return true;
    }

    @Override // javafx.scene.layout.Region, javafx.scene.Parent
    protected double computeMinWidth(double height) {
        if (this.skinBase != null) {
            return this.skinBase.computeMinWidth(height, snappedTopInset(), snappedRightInset(), snappedBottomInset(), snappedLeftInset());
        }
        Node skinNode = getSkinNode();
        if (skinNode == null) {
            return 0.0d;
        }
        return skinNode.minWidth(height);
    }

    @Override // javafx.scene.layout.Region, javafx.scene.Parent
    protected double computeMinHeight(double width) {
        if (this.skinBase != null) {
            return this.skinBase.computeMinHeight(width, snappedTopInset(), snappedRightInset(), snappedBottomInset(), snappedLeftInset());
        }
        Node skinNode = getSkinNode();
        if (skinNode == null) {
            return 0.0d;
        }
        return skinNode.minHeight(width);
    }

    @Override // javafx.scene.layout.Region
    protected double computeMaxWidth(double height) {
        if (this.skinBase != null) {
            return this.skinBase.computeMaxWidth(height, snappedTopInset(), snappedRightInset(), snappedBottomInset(), snappedLeftInset());
        }
        Node skinNode = getSkinNode();
        if (skinNode == null) {
            return 0.0d;
        }
        return skinNode.maxWidth(height);
    }

    @Override // javafx.scene.layout.Region
    protected double computeMaxHeight(double width) {
        if (this.skinBase != null) {
            return this.skinBase.computeMaxHeight(width, snappedTopInset(), snappedRightInset(), snappedBottomInset(), snappedLeftInset());
        }
        Node skinNode = getSkinNode();
        if (skinNode == null) {
            return 0.0d;
        }
        return skinNode.maxHeight(width);
    }

    @Override // javafx.scene.layout.Region, javafx.scene.Parent
    protected double computePrefWidth(double height) {
        if (this.skinBase != null) {
            return this.skinBase.computePrefWidth(height, snappedTopInset(), snappedRightInset(), snappedBottomInset(), snappedLeftInset());
        }
        Node skinNode = getSkinNode();
        if (skinNode == null) {
            return 0.0d;
        }
        return skinNode.prefWidth(height);
    }

    @Override // javafx.scene.layout.Region, javafx.scene.Parent
    protected double computePrefHeight(double width) {
        if (this.skinBase != null) {
            return this.skinBase.computePrefHeight(width, snappedTopInset(), snappedRightInset(), snappedBottomInset(), snappedLeftInset());
        }
        Node skinNode = getSkinNode();
        if (skinNode == null) {
            return 0.0d;
        }
        return skinNode.prefHeight(width);
    }

    @Override // javafx.scene.Parent, javafx.scene.Node
    public double getBaselineOffset() {
        if (this.skinBase != null) {
            return this.skinBase.computeBaselineOffset(snappedTopInset(), snappedRightInset(), snappedBottomInset(), snappedLeftInset());
        }
        Node skinNode = getSkinNode();
        if (skinNode == null) {
            return 0.0d;
        }
        return skinNode.getBaselineOffset();
    }

    @Override // javafx.scene.Parent
    protected void layoutChildren() {
        if (this.skinBase != null) {
            double x2 = snappedLeftInset();
            double y2 = snappedTopInset();
            double w2 = (snapSize(getWidth()) - x2) - snappedRightInset();
            double h2 = (snapSize(getHeight()) - y2) - snappedBottomInset();
            this.skinBase.layoutChildren(x2, y2, w2, h2);
            return;
        }
        Node n2 = getSkinNode();
        if (n2 != null) {
            n2.resizeRelocate(0.0d, 0.0d, getWidth(), getHeight());
        }
    }

    protected Skin<?> createDefaultSkin() {
        return null;
    }

    ObservableList<Node> getControlChildren() {
        return getChildren();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Node getSkinNode() {
        if (!$assertionsDisabled && this.skinBase != null) {
            throw new AssertionError();
        }
        Skin<?> skin = getSkin();
        if (skin == null) {
            return null;
        }
        return skin.getNode();
    }

    @Deprecated
    protected StringProperty skinClassNameProperty() {
        if (this.skinClassName == null) {
            this.skinClassName = new StyleableStringProperty() { // from class: javafx.scene.control.Control.4
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // javafx.css.StyleableStringProperty, javafx.beans.property.StringPropertyBase, javafx.beans.value.WritableObjectValue
                public void set(String v2) {
                    if (v2 == null || v2.isEmpty() || v2.equals(get())) {
                        return;
                    }
                    super.set(v2);
                }

                @Override // javafx.beans.property.StringPropertyBase
                public void invalidated() {
                    if (get() != null && !get().equals(Control.this.currentSkinClassName)) {
                        Control.loadSkinClass(Control.this, Control.this.skinClassName.get());
                    }
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Control.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "skinClassName";
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, String> getCssMetaData() {
                    return StyleableProperties.SKIN;
                }
            };
        }
        return this.skinClassName;
    }

    static void loadSkinClass(Skinnable control, String skinClassName) {
        if (skinClassName == null || skinClassName.isEmpty()) {
            String msg = "Empty -fx-skin property specified for control " + ((Object) control);
            List<CssError> errors = StyleManager.getErrors();
            if (errors != null) {
                CssError error = new CssError(msg);
                errors.add(error);
            }
            Logging.getControlsLogger().severe(msg);
            return;
        }
        try {
            Class<?> skinClass = loadClass(skinClassName, control);
            if (!Skin.class.isAssignableFrom(skinClass)) {
                String msg2 = PdfOps.SINGLE_QUOTE_TOKEN + skinClassName + "' is not a valid Skin class for control " + ((Object) control);
                List<CssError> errors2 = StyleManager.getErrors();
                if (errors2 != null) {
                    CssError error2 = new CssError(msg2);
                    errors2.add(error2);
                }
                Logging.getControlsLogger().severe(msg2);
                return;
            }
            Constructor<?>[] constructors = skinClass.getConstructors();
            Constructor<?> skinConstructor = null;
            int length = constructors.length;
            int i2 = 0;
            while (true) {
                if (i2 >= length) {
                    break;
                }
                Constructor<?> c2 = constructors[i2];
                Class<?>[] parameterTypes = c2.getParameterTypes();
                if (parameterTypes.length != 1 || !Skinnable.class.isAssignableFrom(parameterTypes[0])) {
                    i2++;
                } else {
                    skinConstructor = c2;
                    break;
                }
            }
            if (skinConstructor == null) {
                String msg3 = "No valid constructor defined in '" + skinClassName + "' for control " + ((Object) control) + ".\r\nYou must provide a constructor that accepts a single Skinnable (e.g. Control or PopupControl) parameter in " + skinClassName + ".";
                List<CssError> errors3 = StyleManager.getErrors();
                if (errors3 != null) {
                    CssError error3 = new CssError(msg3);
                    errors3.add(error3);
                }
                Logging.getControlsLogger().severe(msg3);
            } else {
                Skin<?> skinInstance = (Skin) skinConstructor.newInstance(control);
                control.skinProperty().set(skinInstance);
            }
        } catch (InvocationTargetException e2) {
            String msg4 = "Failed to load skin '" + skinClassName + "' for control " + ((Object) control);
            List<CssError> errors4 = StyleManager.getErrors();
            if (errors4 != null) {
                CssError error4 = new CssError(msg4 + " :" + e2.getLocalizedMessage());
                errors4.add(error4);
            }
            Logging.getControlsLogger().severe(msg4, e2.getCause());
        } catch (Exception e3) {
            String msg5 = "Failed to load skin '" + skinClassName + "' for control " + ((Object) control);
            List<CssError> errors5 = StyleManager.getErrors();
            if (errors5 != null) {
                CssError error5 = new CssError(msg5 + " :" + e3.getLocalizedMessage());
                errors5.add(error5);
            }
            Logging.getControlsLogger().severe(msg5, e3);
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/control/Control$StyleableProperties.class */
    private static class StyleableProperties {
        private static final CssMetaData<Control, String> SKIN = new CssMetaData<Control, String>("-fx-skin", StringConverter.getInstance()) { // from class: javafx.scene.control.Control.StyleableProperties.1
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Control n2) {
                return n2.skin == null || !n2.skin.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<String> getStyleableProperty(Control n2) {
                return (StyleableProperty) n2.skinClassNameProperty();
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(Region.getClassCssMetaData());
            styleables.add(SKIN);
            STYLEABLES = Collections.unmodifiableList(styleables);
        }
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    @Override // javafx.scene.layout.Region, javafx.scene.Node, javafx.css.Styleable
    public final List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        if (this.styleableProperties == null) {
            Map<String, CssMetaData<? extends Styleable, ?>> map = new HashMap<>();
            List<CssMetaData<? extends Styleable, ?>> list = getControlCssMetaData();
            int nMax = list != null ? list.size() : 0;
            for (int n2 = 0; n2 < nMax; n2++) {
                CssMetaData<? extends Styleable, ?> metaData = list.get(n2);
                if (metaData != null) {
                    map.put(metaData.getProperty(), metaData);
                }
            }
            List<CssMetaData<? extends Styleable, ?>> list2 = this.skinBase != null ? this.skinBase.getCssMetaData() : null;
            int nMax2 = list2 != null ? list2.size() : 0;
            for (int n3 = 0; n3 < nMax2; n3++) {
                CssMetaData<? extends Styleable, ?> metaData2 = list2.get(n3);
                if (metaData2 != null) {
                    map.put(metaData2.getProperty(), metaData2);
                }
            }
            this.styleableProperties = new ArrayList();
            this.styleableProperties.addAll(map.values());
        }
        return this.styleableProperties;
    }

    protected List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        return getClassCssMetaData();
    }

    @Override // javafx.scene.Parent, javafx.scene.Node
    @Deprecated
    protected void impl_processCSS(WritableValue<Boolean> unused) {
        super.impl_processCSS(unused);
        if (getSkin() == null) {
            Skin<?> defaultSkin = createDefaultSkin();
            if (defaultSkin != null) {
                skinProperty().set(defaultSkin);
                super.impl_processCSS(unused);
                return;
            }
            String msg = "The -fx-skin property has not been defined in CSS for " + ((Object) this) + " and createDefaultSkin() returned null.";
            List<CssError> errors = StyleManager.getErrors();
            if (errors != null) {
                CssError error = new CssError(msg);
                errors.add(error);
            }
            Logging.getControlsLogger().severe(msg);
        }
    }

    @Override // javafx.scene.Node
    @Deprecated
    protected Boolean impl_cssGetFocusTraversableInitialValue() {
        return Boolean.TRUE;
    }

    @Override // javafx.scene.Parent, javafx.scene.Node
    public Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
        Object result;
        switch (attribute) {
            case HELP:
                String help = getAccessibleHelp();
                if (help != null && !help.isEmpty()) {
                    return help;
                }
                Tooltip tooltip = getTooltip();
                return tooltip == null ? "" : tooltip.getText();
            default:
                return (this.skinBase == null || (result = this.skinBase.queryAccessibleAttribute(attribute, parameters)) == null) ? super.queryAccessibleAttribute(attribute, parameters) : result;
        }
    }

    @Override // javafx.scene.Node
    public void executeAccessibleAction(AccessibleAction action, Object... parameters) {
        if (this.skinBase != null) {
            this.skinBase.executeAccessibleAction(action, parameters);
        }
        super.executeAccessibleAction(action, parameters);
    }
}
