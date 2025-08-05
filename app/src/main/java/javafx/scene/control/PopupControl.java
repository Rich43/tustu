package javafx.scene.control;

import com.sun.javafx.application.PlatformImpl;
import com.sun.javafx.css.CssError;
import com.sun.javafx.css.StyleManager;
import com.sun.javafx.css.converters.StringConverter;
import com.sun.javafx.scene.control.Logging;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.StringProperty;
import javafx.beans.value.WritableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.css.CssMetaData;
import javafx.css.PseudoClass;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;
import javafx.css.StyleableStringProperty;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.PopupWindow;
import javafx.stage.Window;
import sun.util.logging.PlatformLogger;

/*  JADX ERROR: NullPointerException in pass: ClassModifier
    java.lang.NullPointerException
    */
/* loaded from: jfxrt.jar:javafx/scene/control/PopupControl.class */
public class PopupControl extends PopupWindow implements Skinnable, Styleable {
    public static final double USE_PREF_SIZE = Double.NEGATIVE_INFINITY;
    public static final double USE_COMPUTED_SIZE = -1.0d;
    private DoubleProperty minWidth;
    private DoubleProperty minHeight;
    private DoubleProperty prefWidth;
    private DoubleProperty prefHeight;
    private DoubleProperty maxWidth;
    private DoubleProperty maxHeight;
    private static final CssMetaData<CSSBridge, String> SKIN;
    private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;
    private final ObjectProperty<Skin<?>> skin = new ObjectPropertyBase<Skin<?>>() { // from class: javafx.scene.control.PopupControl.1
        private Skin<?> oldValue;

        @Override // javafx.beans.property.ObjectPropertyBase, javafx.beans.value.WritableObjectValue
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

        /* JADX WARN: Failed to check method for inline after forced processjavafx.scene.control.PopupControl.access$202(javafx.scene.control.PopupControl, double):double */
        /* JADX WARN: Failed to check method for inline after forced processjavafx.scene.control.PopupControl.access$302(javafx.scene.control.PopupControl, double):double */
        /* JADX WARN: Failed to check method for inline after forced processjavafx.scene.control.PopupControl.access$402(javafx.scene.control.PopupControl, double):double */
        /* JADX WARN: Failed to check method for inline after forced processjavafx.scene.control.PopupControl.access$502(javafx.scene.control.PopupControl, double):double */
        /* JADX WARN: Failed to check method for inline after forced processjavafx.scene.control.PopupControl.access$602(javafx.scene.control.PopupControl, double):double */
        /* JADX WARN: Failed to check method for inline after forced processjavafx.scene.control.PopupControl.access$702(javafx.scene.control.PopupControl, double):double */
        @Override // javafx.beans.property.ObjectPropertyBase
        protected void invalidated() {
            Skin<?> skin = get();
            PopupControl.this.currentSkinClassName = skin == null ? null : skin.getClass().getName();
            PopupControl.this.skinClassNameProperty().set(PopupControl.this.currentSkinClassName);
            if (this.oldValue != null) {
                this.oldValue.dispose();
            }
            this.oldValue = getValue2();
            PopupControl.access$202(PopupControl.this, -1.0d);
            PopupControl.access$302(PopupControl.this, -1.0d);
            PopupControl.access$402(PopupControl.this, -1.0d);
            PopupControl.access$502(PopupControl.this, -1.0d);
            PopupControl.access$602(PopupControl.this, -1.0d);
            PopupControl.access$702(PopupControl.this, -1.0d);
            PopupControl.this.skinSizeComputed = false;
            Node n2 = PopupControl.this.getSkinNode();
            if (n2 != null) {
                PopupControl.this.bridge.getChildren().setAll(n2);
            } else {
                PopupControl.this.bridge.getChildren().clear();
            }
            PopupControl.this.bridge.impl_reapplyCSS();
            PlatformLogger logger = Logging.getControlsLogger();
            if (logger.isLoggable(PlatformLogger.Level.FINEST)) {
                logger.finest("Stored skin[" + ((Object) getValue2()) + "] on " + ((Object) this));
            }
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return PopupControl.this;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return "skin";
        }
    };
    private String currentSkinClassName = null;
    private StringProperty skinClassName = null;
    private double prefWidthCache = -1.0d;
    private double prefHeightCache = -1.0d;
    private double minWidthCache = -1.0d;
    private double minHeightCache = -1.0d;
    private double maxWidthCache = -1.0d;
    private double maxHeightCache = -1.0d;
    private boolean skinSizeComputed = false;
    protected CSSBridge bridge = new CSSBridge();

    /*  JADX ERROR: Failed to decode insn: 0x0002: MOVE_MULTI
        java.lang.ArrayIndexOutOfBoundsException: arraycopy: source index -1 out of bounds for object array[6]
        	at java.base/java.lang.System.arraycopy(Native Method)
        	at jadx.plugins.input.java.data.code.StackState.insert(StackState.java:52)
        	at jadx.plugins.input.java.data.code.CodeDecodeState.insert(CodeDecodeState.java:137)
        	at jadx.plugins.input.java.data.code.JavaInsnsRegister.dup2x1(JavaInsnsRegister.java:313)
        	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
        	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
        	at jadx.core.ProcessClass.process(ProcessClass.java:69)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:109)
        	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
        */
    static /* synthetic */ double access$202(javafx.scene.control.PopupControl r6, double r7) {
        /*
            r0 = r6
            r1 = r7
            // decode failed: arraycopy: source index -1 out of bounds for object array[6]
            r0.prefWidthCache = r1
            return r-1
        */
        throw new UnsupportedOperationException("Method not decompiled: javafx.scene.control.PopupControl.access$202(javafx.scene.control.PopupControl, double):double");
    }

    /*  JADX ERROR: Failed to decode insn: 0x0002: MOVE_MULTI
        java.lang.ArrayIndexOutOfBoundsException: arraycopy: source index -1 out of bounds for object array[6]
        	at java.base/java.lang.System.arraycopy(Native Method)
        	at jadx.plugins.input.java.data.code.StackState.insert(StackState.java:52)
        	at jadx.plugins.input.java.data.code.CodeDecodeState.insert(CodeDecodeState.java:137)
        	at jadx.plugins.input.java.data.code.JavaInsnsRegister.dup2x1(JavaInsnsRegister.java:313)
        	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
        	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
        	at jadx.core.ProcessClass.process(ProcessClass.java:69)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:109)
        	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
        */
    static /* synthetic */ double access$302(javafx.scene.control.PopupControl r6, double r7) {
        /*
            r0 = r6
            r1 = r7
            // decode failed: arraycopy: source index -1 out of bounds for object array[6]
            r0.prefHeightCache = r1
            return r-1
        */
        throw new UnsupportedOperationException("Method not decompiled: javafx.scene.control.PopupControl.access$302(javafx.scene.control.PopupControl, double):double");
    }

    /*  JADX ERROR: Failed to decode insn: 0x0002: MOVE_MULTI
        java.lang.ArrayIndexOutOfBoundsException: arraycopy: source index -1 out of bounds for object array[6]
        	at java.base/java.lang.System.arraycopy(Native Method)
        	at jadx.plugins.input.java.data.code.StackState.insert(StackState.java:52)
        	at jadx.plugins.input.java.data.code.CodeDecodeState.insert(CodeDecodeState.java:137)
        	at jadx.plugins.input.java.data.code.JavaInsnsRegister.dup2x1(JavaInsnsRegister.java:313)
        	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
        	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
        	at jadx.core.ProcessClass.process(ProcessClass.java:69)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:109)
        	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
        */
    static /* synthetic */ double access$402(javafx.scene.control.PopupControl r6, double r7) {
        /*
            r0 = r6
            r1 = r7
            // decode failed: arraycopy: source index -1 out of bounds for object array[6]
            r0.minWidthCache = r1
            return r-1
        */
        throw new UnsupportedOperationException("Method not decompiled: javafx.scene.control.PopupControl.access$402(javafx.scene.control.PopupControl, double):double");
    }

    /*  JADX ERROR: Failed to decode insn: 0x0002: MOVE_MULTI
        java.lang.ArrayIndexOutOfBoundsException: arraycopy: source index -1 out of bounds for object array[6]
        	at java.base/java.lang.System.arraycopy(Native Method)
        	at jadx.plugins.input.java.data.code.StackState.insert(StackState.java:52)
        	at jadx.plugins.input.java.data.code.CodeDecodeState.insert(CodeDecodeState.java:137)
        	at jadx.plugins.input.java.data.code.JavaInsnsRegister.dup2x1(JavaInsnsRegister.java:313)
        	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
        	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
        	at jadx.core.ProcessClass.process(ProcessClass.java:69)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:109)
        	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
        */
    static /* synthetic */ double access$502(javafx.scene.control.PopupControl r6, double r7) {
        /*
            r0 = r6
            r1 = r7
            // decode failed: arraycopy: source index -1 out of bounds for object array[6]
            r0.minHeightCache = r1
            return r-1
        */
        throw new UnsupportedOperationException("Method not decompiled: javafx.scene.control.PopupControl.access$502(javafx.scene.control.PopupControl, double):double");
    }

    /*  JADX ERROR: Failed to decode insn: 0x0002: MOVE_MULTI
        java.lang.ArrayIndexOutOfBoundsException: arraycopy: source index -1 out of bounds for object array[6]
        	at java.base/java.lang.System.arraycopy(Native Method)
        	at jadx.plugins.input.java.data.code.StackState.insert(StackState.java:52)
        	at jadx.plugins.input.java.data.code.CodeDecodeState.insert(CodeDecodeState.java:137)
        	at jadx.plugins.input.java.data.code.JavaInsnsRegister.dup2x1(JavaInsnsRegister.java:313)
        	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
        	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
        	at jadx.core.ProcessClass.process(ProcessClass.java:69)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:109)
        	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
        */
    static /* synthetic */ double access$602(javafx.scene.control.PopupControl r6, double r7) {
        /*
            r0 = r6
            r1 = r7
            // decode failed: arraycopy: source index -1 out of bounds for object array[6]
            r0.maxWidthCache = r1
            return r-1
        */
        throw new UnsupportedOperationException("Method not decompiled: javafx.scene.control.PopupControl.access$602(javafx.scene.control.PopupControl, double):double");
    }

    /*  JADX ERROR: Failed to decode insn: 0x0002: MOVE_MULTI
        java.lang.ArrayIndexOutOfBoundsException: arraycopy: source index -1 out of bounds for object array[6]
        	at java.base/java.lang.System.arraycopy(Native Method)
        	at jadx.plugins.input.java.data.code.StackState.insert(StackState.java:52)
        	at jadx.plugins.input.java.data.code.CodeDecodeState.insert(CodeDecodeState.java:137)
        	at jadx.plugins.input.java.data.code.JavaInsnsRegister.dup2x1(JavaInsnsRegister.java:313)
        	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
        	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
        	at jadx.core.ProcessClass.process(ProcessClass.java:69)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:109)
        	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
        */
    static /* synthetic */ double access$702(javafx.scene.control.PopupControl r6, double r7) {
        /*
            r0 = r6
            r1 = r7
            // decode failed: arraycopy: source index -1 out of bounds for object array[6]
            r0.maxHeightCache = r1
            return r-1
        */
        throw new UnsupportedOperationException("Method not decompiled: javafx.scene.control.PopupControl.access$702(javafx.scene.control.PopupControl, double):double");
    }

    static {
        if (Application.getUserAgentStylesheet() == null) {
            PlatformImpl.setDefaultPlatformUserAgentStylesheet();
        }
        SKIN = new CssMetaData<CSSBridge, String>("-fx-skin", StringConverter.getInstance()) { // from class: javafx.scene.control.PopupControl.9
            @Override // javafx.css.CssMetaData
            public boolean isSettable(CSSBridge cssBridge) {
                return !cssBridge.popupControl.skinProperty().isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<String> getStyleableProperty(CSSBridge cssBridge) {
                return (StyleableProperty) cssBridge.popupControl.skinClassNameProperty();
            }
        };
        List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>();
        Collections.addAll(styleables, SKIN);
        STYLEABLES = Collections.unmodifiableList(styleables);
    }

    public PopupControl() {
        setAnchorLocation(PopupWindow.AnchorLocation.CONTENT_TOP_LEFT);
        getContent().add(this.bridge);
    }

    public final StringProperty idProperty() {
        return this.bridge.idProperty();
    }

    public final void setId(String value) {
        idProperty().set(value);
    }

    @Override // javafx.css.Styleable
    public final String getId() {
        return idProperty().get();
    }

    @Override // javafx.css.Styleable
    public final ObservableList<String> getStyleClass() {
        return this.bridge.getStyleClass();
    }

    public final void setStyle(String value) {
        styleProperty().set(value);
    }

    @Override // javafx.css.Styleable
    public final String getStyle() {
        return styleProperty().get();
    }

    public final StringProperty styleProperty() {
        return this.bridge.styleProperty();
    }

    @Override // javafx.scene.control.Skinnable
    public final ObjectProperty<Skin<?>> skinProperty() {
        return this.skin;
    }

    @Override // javafx.scene.control.Skinnable
    public final void setSkin(Skin<?> value) {
        skinProperty().setValue(value);
    }

    @Override // javafx.scene.control.Skinnable
    public final Skin<?> getSkin() {
        return skinProperty().getValue2();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public StringProperty skinClassNameProperty() {
        if (this.skinClassName == null) {
            this.skinClassName = new StyleableStringProperty() { // from class: javafx.scene.control.PopupControl.2
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
                    if (get() != null && !get().equals(PopupControl.this.currentSkinClassName)) {
                        Control.loadSkinClass(PopupControl.this, get());
                    }
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return PopupControl.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "skinClassName";
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, String> getCssMetaData() {
                    return PopupControl.SKIN;
                }
            };
        }
        return this.skinClassName;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Node getSkinNode() {
        if (getSkin() == null) {
            return null;
        }
        return getSkin().getNode();
    }

    public final void setMinWidth(double value) {
        minWidthProperty().set(value);
    }

    public final double getMinWidth() {
        if (this.minWidth == null) {
            return -1.0d;
        }
        return this.minWidth.get();
    }

    public final DoubleProperty minWidthProperty() {
        if (this.minWidth == null) {
            this.minWidth = new DoublePropertyBase(-1.0d) { // from class: javafx.scene.control.PopupControl.3
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    if (PopupControl.this.isShowing()) {
                        PopupControl.this.bridge.requestLayout();
                    }
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return PopupControl.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "minWidth";
                }
            };
        }
        return this.minWidth;
    }

    public final void setMinHeight(double value) {
        minHeightProperty().set(value);
    }

    public final double getMinHeight() {
        if (this.minHeight == null) {
            return -1.0d;
        }
        return this.minHeight.get();
    }

    public final DoubleProperty minHeightProperty() {
        if (this.minHeight == null) {
            this.minHeight = new DoublePropertyBase(-1.0d) { // from class: javafx.scene.control.PopupControl.4
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    if (PopupControl.this.isShowing()) {
                        PopupControl.this.bridge.requestLayout();
                    }
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return PopupControl.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "minHeight";
                }
            };
        }
        return this.minHeight;
    }

    public void setMinSize(double minWidth, double minHeight) {
        setMinWidth(minWidth);
        setMinHeight(minHeight);
    }

    public final void setPrefWidth(double value) {
        prefWidthProperty().set(value);
    }

    public final double getPrefWidth() {
        if (this.prefWidth == null) {
            return -1.0d;
        }
        return this.prefWidth.get();
    }

    public final DoubleProperty prefWidthProperty() {
        if (this.prefWidth == null) {
            this.prefWidth = new DoublePropertyBase(-1.0d) { // from class: javafx.scene.control.PopupControl.5
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    if (PopupControl.this.isShowing()) {
                        PopupControl.this.bridge.requestLayout();
                    }
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return PopupControl.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "prefWidth";
                }
            };
        }
        return this.prefWidth;
    }

    public final void setPrefHeight(double value) {
        prefHeightProperty().set(value);
    }

    public final double getPrefHeight() {
        if (this.prefHeight == null) {
            return -1.0d;
        }
        return this.prefHeight.get();
    }

    public final DoubleProperty prefHeightProperty() {
        if (this.prefHeight == null) {
            this.prefHeight = new DoublePropertyBase(-1.0d) { // from class: javafx.scene.control.PopupControl.6
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    if (PopupControl.this.isShowing()) {
                        PopupControl.this.bridge.requestLayout();
                    }
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return PopupControl.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "prefHeight";
                }
            };
        }
        return this.prefHeight;
    }

    public void setPrefSize(double prefWidth, double prefHeight) {
        setPrefWidth(prefWidth);
        setPrefHeight(prefHeight);
    }

    public final void setMaxWidth(double value) {
        maxWidthProperty().set(value);
    }

    public final double getMaxWidth() {
        if (this.maxWidth == null) {
            return -1.0d;
        }
        return this.maxWidth.get();
    }

    public final DoubleProperty maxWidthProperty() {
        if (this.maxWidth == null) {
            this.maxWidth = new DoublePropertyBase(-1.0d) { // from class: javafx.scene.control.PopupControl.7
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    if (PopupControl.this.isShowing()) {
                        PopupControl.this.bridge.requestLayout();
                    }
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return PopupControl.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "maxWidth";
                }
            };
        }
        return this.maxWidth;
    }

    public final void setMaxHeight(double value) {
        maxHeightProperty().set(value);
    }

    public final double getMaxHeight() {
        if (this.maxHeight == null) {
            return -1.0d;
        }
        return this.maxHeight.get();
    }

    public final DoubleProperty maxHeightProperty() {
        if (this.maxHeight == null) {
            this.maxHeight = new DoublePropertyBase(-1.0d) { // from class: javafx.scene.control.PopupControl.8
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    if (PopupControl.this.isShowing()) {
                        PopupControl.this.bridge.requestLayout();
                    }
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return PopupControl.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "maxHeight";
                }
            };
        }
        return this.maxHeight;
    }

    public void setMaxSize(double maxWidth, double maxHeight) {
        setMaxWidth(maxWidth);
        setMaxHeight(maxHeight);
    }

    public final double minWidth(double height) {
        double override = getMinWidth();
        if (override == -1.0d) {
            if (this.minWidthCache == -1.0d) {
                this.minWidthCache = recalculateMinWidth(height);
            }
            return this.minWidthCache;
        }
        if (override == Double.NEGATIVE_INFINITY) {
            return prefWidth(height);
        }
        return override;
    }

    public final double minHeight(double width) {
        double override = getMinHeight();
        if (override == -1.0d) {
            if (this.minHeightCache == -1.0d) {
                this.minHeightCache = recalculateMinHeight(width);
            }
            return this.minHeightCache;
        }
        if (override == Double.NEGATIVE_INFINITY) {
            return prefHeight(width);
        }
        return override;
    }

    public final double prefWidth(double height) {
        double override = getPrefWidth();
        if (override == -1.0d) {
            if (this.prefWidthCache == -1.0d) {
                this.prefWidthCache = recalculatePrefWidth(height);
            }
            return this.prefWidthCache;
        }
        if (override == Double.NEGATIVE_INFINITY) {
            return prefWidth(height);
        }
        return override;
    }

    public final double prefHeight(double width) {
        double override = getPrefHeight();
        if (override == -1.0d) {
            if (this.prefHeightCache == -1.0d) {
                this.prefHeightCache = recalculatePrefHeight(width);
            }
            return this.prefHeightCache;
        }
        if (override == Double.NEGATIVE_INFINITY) {
            return prefHeight(width);
        }
        return override;
    }

    public final double maxWidth(double height) {
        double override = getMaxWidth();
        if (override == -1.0d) {
            if (this.maxWidthCache == -1.0d) {
                this.maxWidthCache = recalculateMaxWidth(height);
            }
            return this.maxWidthCache;
        }
        if (override == Double.NEGATIVE_INFINITY) {
            return prefWidth(height);
        }
        return override;
    }

    public final double maxHeight(double width) {
        double override = getMaxHeight();
        if (override == -1.0d) {
            if (this.maxHeightCache == -1.0d) {
                this.maxHeightCache = recalculateMaxHeight(width);
            }
            return this.maxHeightCache;
        }
        if (override == Double.NEGATIVE_INFINITY) {
            return prefHeight(width);
        }
        return override;
    }

    private double recalculateMinWidth(double height) {
        recomputeSkinSize();
        if (getSkinNode() == null) {
            return 0.0d;
        }
        return getSkinNode().minWidth(height);
    }

    private double recalculateMinHeight(double width) {
        recomputeSkinSize();
        if (getSkinNode() == null) {
            return 0.0d;
        }
        return getSkinNode().minHeight(width);
    }

    private double recalculateMaxWidth(double height) {
        recomputeSkinSize();
        if (getSkinNode() == null) {
            return 0.0d;
        }
        return getSkinNode().maxWidth(height);
    }

    private double recalculateMaxHeight(double width) {
        recomputeSkinSize();
        if (getSkinNode() == null) {
            return 0.0d;
        }
        return getSkinNode().maxHeight(width);
    }

    private double recalculatePrefWidth(double height) {
        recomputeSkinSize();
        if (getSkinNode() == null) {
            return 0.0d;
        }
        return getSkinNode().prefWidth(height);
    }

    private double recalculatePrefHeight(double width) {
        recomputeSkinSize();
        if (getSkinNode() == null) {
            return 0.0d;
        }
        return getSkinNode().prefHeight(width);
    }

    private void recomputeSkinSize() {
        if (!this.skinSizeComputed) {
            this.bridge.applyCss();
            this.skinSizeComputed = true;
        }
    }

    protected Skin<?> createDefaultSkin() {
        return null;
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return STYLEABLES;
    }

    @Override // javafx.css.Styleable
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return getClassCssMetaData();
    }

    public final void pseudoClassStateChanged(PseudoClass pseudoClass, boolean active) {
        this.bridge.pseudoClassStateChanged(pseudoClass, active);
    }

    @Override // javafx.css.Styleable
    public String getTypeSelector() {
        return "PopupControl";
    }

    public Styleable getStyleableParent() {
        Scene ownerScene;
        Node ownerNode = getOwnerNode();
        if (ownerNode != null) {
            return ownerNode;
        }
        Window ownerWindow = getOwnerWindow();
        if (ownerWindow != null && (ownerScene = ownerWindow.getScene()) != null) {
            return ownerScene.getRoot();
        }
        return this.bridge.getParent();
    }

    @Override // javafx.css.Styleable
    public final ObservableSet<PseudoClass> getPseudoClassStates() {
        return FXCollections.emptyObservableSet();
    }

    @Deprecated
    public Node impl_styleableGetNode() {
        return this.bridge;
    }

    /* loaded from: jfxrt.jar:javafx/scene/control/PopupControl$CSSBridge.class */
    protected class CSSBridge extends Pane {
        private final PopupControl popupControl;

        protected CSSBridge() {
            this.popupControl = PopupControl.this;
        }

        /* JADX WARN: Failed to check method for inline after forced processjavafx.scene.control.PopupControl.access$202(javafx.scene.control.PopupControl, double):double */
        /* JADX WARN: Failed to check method for inline after forced processjavafx.scene.control.PopupControl.access$302(javafx.scene.control.PopupControl, double):double */
        /* JADX WARN: Failed to check method for inline after forced processjavafx.scene.control.PopupControl.access$402(javafx.scene.control.PopupControl, double):double */
        /* JADX WARN: Failed to check method for inline after forced processjavafx.scene.control.PopupControl.access$502(javafx.scene.control.PopupControl, double):double */
        /* JADX WARN: Failed to check method for inline after forced processjavafx.scene.control.PopupControl.access$602(javafx.scene.control.PopupControl, double):double */
        /* JADX WARN: Failed to check method for inline after forced processjavafx.scene.control.PopupControl.access$702(javafx.scene.control.PopupControl, double):double */
        @Override // javafx.scene.Parent
        public void requestLayout() {
            PopupControl.access$202(PopupControl.this, -1.0d);
            PopupControl.access$302(PopupControl.this, -1.0d);
            PopupControl.access$402(PopupControl.this, -1.0d);
            PopupControl.access$502(PopupControl.this, -1.0d);
            PopupControl.access$602(PopupControl.this, -1.0d);
            PopupControl.access$702(PopupControl.this, -1.0d);
            super.requestLayout();
        }

        @Override // javafx.scene.Node, javafx.css.Styleable
        public Styleable getStyleableParent() {
            return PopupControl.this.getStyleableParent();
        }

        @Deprecated
        protected void setSkinClassName(String skinClassName) {
        }

        @Override // javafx.scene.layout.Region, javafx.scene.Node, javafx.css.Styleable
        public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
            return PopupControl.this.getCssMetaData();
        }

        @Override // javafx.scene.Parent
        @Deprecated
        public List<String> impl_getAllParentStylesheets() {
            Styleable styleable = getStyleableParent();
            if (styleable instanceof Parent) {
                return ((Parent) styleable).impl_getAllParentStylesheets();
            }
            return null;
        }

        @Override // javafx.scene.Parent, javafx.scene.Node
        @Deprecated
        protected void impl_processCSS(WritableValue<Boolean> unused) {
            super.impl_processCSS(unused);
            if (PopupControl.this.getSkin() == null) {
                Skin<?> defaultSkin = PopupControl.this.createDefaultSkin();
                if (defaultSkin != null) {
                    PopupControl.this.skinProperty().set(defaultSkin);
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
    }
}
