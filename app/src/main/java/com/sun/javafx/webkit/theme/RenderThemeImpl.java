package com.sun.javafx.webkit.theme;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import com.sun.javafx.webkit.Accessor;
import com.sun.webkit.LoadListenerClient;
import com.sun.webkit.graphics.Ref;
import com.sun.webkit.graphics.RenderTheme;
import com.sun.webkit.graphics.WCGraphicsContext;
import com.sun.webkit.graphics.WCSize;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Control;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;

/* loaded from: jfxrt.jar:com/sun/javafx/webkit/theme/RenderThemeImpl.class */
public final class RenderThemeImpl extends RenderTheme {
    private static final Logger log = Logger.getLogger(RenderThemeImpl.class.getName());
    private Accessor accessor;
    private boolean isDefault;
    private Pool<FormControl> pool;

    /* loaded from: jfxrt.jar:com/sun/javafx/webkit/theme/RenderThemeImpl$FormControl.class */
    private interface FormControl extends Widget {
        Control asControl();

        void setState(int i2);
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/webkit/theme/RenderThemeImpl$Widget.class */
    interface Widget {
        WidgetType getType();
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/webkit/theme/RenderThemeImpl$WidgetType.class */
    enum WidgetType {
        TEXTFIELD(0),
        BUTTON(1),
        CHECKBOX(2),
        RADIOBUTTON(3),
        MENULIST(4),
        MENULISTBUTTON(5),
        SLIDER(6),
        PROGRESSBAR(7),
        METER(8),
        SCROLLBAR(9);

        private static final HashMap<Integer, WidgetType> map = new HashMap<>();
        private final int value;

        static {
            for (WidgetType v2 : values()) {
                map.put(Integer.valueOf(v2.value), v2);
            }
        }

        WidgetType(int value) {
            this.value = value;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static WidgetType convert(int index) {
            return map.get(Integer.valueOf(index));
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/webkit/theme/RenderThemeImpl$Pool.class */
    static final class Pool<T extends Widget> {
        private static final int INITIAL_CAPACITY = 100;
        private int capacity = 100;
        private final LinkedHashMap<Long, Integer> ids = new LinkedHashMap<>();
        private final Map<Long, WeakReference<T>> pool = new HashMap();
        private final Notifier<T> notifier;
        private final String type;
        static final /* synthetic */ boolean $assertionsDisabled;

        /* loaded from: jfxrt.jar:com/sun/javafx/webkit/theme/RenderThemeImpl$Pool$Notifier.class */
        interface Notifier<T> {
            void notifyRemoved(T t2);
        }

        static {
            $assertionsDisabled = !RenderThemeImpl.class.desiredAssertionStatus();
        }

        Pool(Notifier<T> notifier, Class<T> type) {
            this.notifier = notifier;
            this.type = type.getSimpleName();
        }

        T get(long id) {
            T control;
            if (RenderThemeImpl.log.isLoggable(Level.FINE)) {
                RenderThemeImpl.log.log(Level.FINE, "type: {0}, size: {1}, id: 0x{2}", new Object[]{this.type, Integer.valueOf(this.pool.size()), Long.toHexString(id)});
            }
            if (!$assertionsDisabled && this.ids.size() != this.pool.size()) {
                throw new AssertionError();
            }
            WeakReference<T> controlRef = this.pool.get(Long.valueOf(id));
            if (controlRef == null || (control = controlRef.get()) == null) {
                return null;
            }
            Integer value = this.ids.remove(Long.valueOf(id));
            this.ids.put(Long.valueOf(id), value);
            return control;
        }

        void put(long id, T control, int updateContentCycleID) {
            if (RenderThemeImpl.log.isLoggable(Level.FINEST)) {
                RenderThemeImpl.log.log(Level.FINEST, "size: {0}, id: 0x{1}, control: {2}", new Object[]{Integer.valueOf(this.pool.size()), Long.toHexString(id), control.getType()});
            }
            if (this.ids.size() >= this.capacity) {
                Long _id = this.ids.keySet().iterator().next();
                Integer cycleID = this.ids.get(_id);
                if (cycleID.intValue() != updateContentCycleID) {
                    this.ids.remove(_id);
                    T _control = this.pool.remove(_id).get();
                    if (_control != null) {
                        this.notifier.notifyRemoved(_control);
                    }
                } else {
                    this.capacity = Math.min(this.capacity, (int) Math.ceil(1.073741823E9d)) * 2;
                }
            }
            this.ids.put(Long.valueOf(id), Integer.valueOf(updateContentCycleID));
            this.pool.put(Long.valueOf(id), new WeakReference<>(control));
        }

        void clear() {
            if (RenderThemeImpl.log.isLoggable(Level.FINE)) {
                RenderThemeImpl.log.fine("size: " + this.pool.size() + ", controls: " + ((Object) this.pool.values()));
            }
            if (this.pool.size() == 0) {
                return;
            }
            this.ids.clear();
            for (WeakReference<T> controlRef : this.pool.values()) {
                T control = controlRef.get();
                if (control != null) {
                    this.notifier.notifyRemoved(control);
                }
            }
            this.pool.clear();
            this.capacity = 100;
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/webkit/theme/RenderThemeImpl$ViewListener.class */
    static class ViewListener implements InvalidationListener {
        private final Pool pool;
        private final Accessor accessor;
        private LoadListenerClient loadListener;

        ViewListener(Pool pool, Accessor accessor) {
            this.pool = pool;
            this.accessor = accessor;
        }

        @Override // javafx.beans.InvalidationListener
        public void invalidated(Observable ov) {
            this.pool.clear();
            if (this.accessor.getPage() != null && this.loadListener == null) {
                this.loadListener = new LoadListenerClient() { // from class: com.sun.javafx.webkit.theme.RenderThemeImpl.ViewListener.1
                    @Override // com.sun.webkit.LoadListenerClient
                    public void dispatchLoadEvent(long frame, int state, String url, String contentType, double progress, int errorCode) {
                        if (state == 0) {
                            ViewListener.this.pool.clear();
                        }
                    }

                    @Override // com.sun.webkit.LoadListenerClient
                    public void dispatchResourceLoadEvent(long frame, int state, String url, String contentType, double progress, int errorCode) {
                    }
                };
                this.accessor.getPage().addLoadListenerClient(this.loadListener);
            }
        }
    }

    public RenderThemeImpl(Accessor accessor) {
        this.accessor = accessor;
        this.pool = new Pool<>(fc -> {
            accessor.removeChild(fc.asControl());
        }, FormControl.class);
        accessor.addViewListener(new ViewListener(this.pool, accessor));
    }

    public RenderThemeImpl() {
        this.isDefault = true;
    }

    private void ensureNotDefault() {
        if (this.isDefault) {
            throw new IllegalStateException("the method should not be called in this context");
        }
    }

    @Override // com.sun.webkit.graphics.RenderTheme
    protected Ref createWidget(long id, int widgetIndex, int state, int w2, int h2, int bgColor, ByteBuffer extParams) {
        ensureNotDefault();
        FormControl fc = (FormControl) this.pool.get(id);
        WidgetType type = WidgetType.convert(widgetIndex);
        if (fc == null || fc.getType() != type) {
            if (fc != null) {
                this.accessor.removeChild(fc.asControl());
            }
            switch (type) {
                case TEXTFIELD:
                    fc = new FormTextField();
                    break;
                case BUTTON:
                    fc = new FormButton();
                    break;
                case CHECKBOX:
                    fc = new FormCheckBox();
                    break;
                case RADIOBUTTON:
                    fc = new FormRadioButton();
                    break;
                case MENULIST:
                    fc = new FormMenuList();
                    break;
                case MENULISTBUTTON:
                    fc = new FormMenuListButton();
                    break;
                case SLIDER:
                    fc = new FormSlider();
                    break;
                case PROGRESSBAR:
                    fc = new FormProgressBar(WidgetType.PROGRESSBAR);
                    break;
                case METER:
                    fc = new FormProgressBar(WidgetType.METER);
                    break;
                default:
                    log.log(Level.ALL, "unknown widget index: {0}", Integer.valueOf(widgetIndex));
                    return null;
            }
            fc.asControl().setFocusTraversable(false);
            this.pool.put(id, fc, this.accessor.getPage().getUpdateContentCycleID());
            this.accessor.addChild(fc.asControl());
        }
        fc.setState(state);
        Control ctrl = fc.asControl();
        if (ctrl.getWidth() != w2 || ctrl.getHeight() != h2) {
            ctrl.resize(w2, h2);
        }
        if (ctrl.isManaged()) {
            ctrl.setManaged(false);
        }
        if (extParams != null) {
            if (type == WidgetType.SLIDER) {
                Slider slider = (Slider) ctrl;
                extParams.order(ByteOrder.nativeOrder());
                slider.setOrientation(extParams.getInt() == 0 ? Orientation.HORIZONTAL : Orientation.VERTICAL);
                slider.setMax(extParams.getFloat());
                slider.setMin(extParams.getFloat());
                slider.setValue(extParams.getFloat());
            } else if (type == WidgetType.PROGRESSBAR) {
                ProgressBar progress = (ProgressBar) ctrl;
                extParams.order(ByteOrder.nativeOrder());
                progress.setProgress(extParams.getInt() == 1 ? extParams.getFloat() : -1.0d);
            } else if (type == WidgetType.METER) {
                ProgressBar progress2 = (ProgressBar) ctrl;
                extParams.order(ByteOrder.nativeOrder());
                progress2.setProgress(extParams.getFloat());
                progress2.setStyle(getMeterStyle(extParams.getInt()));
            }
        }
        return new FormControlRef(fc);
    }

    private String getMeterStyle(int region) {
        switch (region) {
            case 1:
                return "-fx-accent: yellow";
            case 2:
                return "-fx-accent: red";
            default:
                return "-fx-accent: green";
        }
    }

    @Override // com.sun.webkit.graphics.RenderTheme
    public void drawWidget(WCGraphicsContext g2, Ref widget, int x2, int y2) {
        Control control;
        ensureNotDefault();
        FormControl fcontrol = ((FormControlRef) widget).asFormControl();
        if (fcontrol != null && (control = fcontrol.asControl()) != null) {
            g2.saveState();
            g2.translate(x2, y2);
            Renderer.getRenderer().render(control, g2);
            g2.restoreState();
        }
    }

    @Override // com.sun.webkit.graphics.RenderTheme
    public WCSize getWidgetSize(Ref widget) {
        ensureNotDefault();
        FormControl fcontrol = ((FormControlRef) widget).asFormControl();
        if (fcontrol != null) {
            Control control = fcontrol.asControl();
            return new WCSize((float) control.getWidth(), (float) control.getHeight());
        }
        return new WCSize(0.0f, 0.0f);
    }

    @Override // com.sun.webkit.graphics.RenderTheme
    protected int getRadioButtonSize() {
        String style = Application.getUserAgentStylesheet();
        if (!Application.STYLESHEET_MODENA.equalsIgnoreCase(style) && Application.STYLESHEET_CASPIAN.equalsIgnoreCase(style)) {
            return 19;
        }
        return 20;
    }

    @Override // com.sun.webkit.graphics.RenderTheme
    protected int getSelectionColor(int index) {
        switch (index) {
            case 0:
                return -16739329;
            case 1:
                return -1;
            default:
                return 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean hasState(int state, int mask) {
        return (state & mask) != 0;
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/webkit/theme/RenderThemeImpl$FormControlRef.class */
    private static final class FormControlRef extends Ref {
        private final WeakReference<FormControl> fcRef;

        private FormControlRef(FormControl fc) {
            this.fcRef = new WeakReference<>(fc);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public FormControl asFormControl() {
            return this.fcRef.get();
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/webkit/theme/RenderThemeImpl$FormButton.class */
    private static final class FormButton extends Button implements FormControl {
        private FormButton() {
        }

        @Override // com.sun.javafx.webkit.theme.RenderThemeImpl.FormControl
        public Control asControl() {
            return this;
        }

        @Override // com.sun.javafx.webkit.theme.RenderThemeImpl.FormControl
        public void setState(int state) {
            setDisabled(!RenderThemeImpl.hasState(state, 4));
            setFocused(RenderThemeImpl.hasState(state, 8));
            setHover(RenderThemeImpl.hasState(state, 32) && !isDisabled());
            setPressed(RenderThemeImpl.hasState(state, 16));
            if (isPressed()) {
                arm();
            } else {
                disarm();
            }
        }

        @Override // com.sun.javafx.webkit.theme.RenderThemeImpl.Widget
        public WidgetType getType() {
            return WidgetType.BUTTON;
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/webkit/theme/RenderThemeImpl$FormTextField.class */
    private static final class FormTextField extends TextField implements FormControl {
        private FormTextField() {
            setStyle("-fx-display-caret: false");
        }

        @Override // com.sun.javafx.webkit.theme.RenderThemeImpl.FormControl
        public Control asControl() {
            return this;
        }

        @Override // com.sun.javafx.webkit.theme.RenderThemeImpl.FormControl
        public void setState(int state) {
            setDisabled(!RenderThemeImpl.hasState(state, 4));
            setEditable(RenderThemeImpl.hasState(state, 64));
            setFocused(RenderThemeImpl.hasState(state, 8));
            setHover(RenderThemeImpl.hasState(state, 32) && !isDisabled());
        }

        @Override // com.sun.javafx.webkit.theme.RenderThemeImpl.Widget
        public WidgetType getType() {
            return WidgetType.TEXTFIELD;
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/webkit/theme/RenderThemeImpl$FormCheckBox.class */
    private static final class FormCheckBox extends CheckBox implements FormControl {
        private FormCheckBox() {
        }

        @Override // com.sun.javafx.webkit.theme.RenderThemeImpl.FormControl
        public Control asControl() {
            return this;
        }

        @Override // com.sun.javafx.webkit.theme.RenderThemeImpl.FormControl
        public void setState(int state) {
            setDisabled(!RenderThemeImpl.hasState(state, 4));
            setFocused(RenderThemeImpl.hasState(state, 8));
            setHover(RenderThemeImpl.hasState(state, 32) && !isDisabled());
            setSelected(RenderThemeImpl.hasState(state, 1));
        }

        @Override // com.sun.javafx.webkit.theme.RenderThemeImpl.Widget
        public WidgetType getType() {
            return WidgetType.CHECKBOX;
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/webkit/theme/RenderThemeImpl$FormRadioButton.class */
    private static final class FormRadioButton extends RadioButton implements FormControl {
        private FormRadioButton() {
        }

        @Override // com.sun.javafx.webkit.theme.RenderThemeImpl.FormControl
        public Control asControl() {
            return this;
        }

        @Override // com.sun.javafx.webkit.theme.RenderThemeImpl.FormControl
        public void setState(int state) {
            setDisabled(!RenderThemeImpl.hasState(state, 4));
            setFocused(RenderThemeImpl.hasState(state, 8));
            setHover(RenderThemeImpl.hasState(state, 32) && !isDisabled());
            setSelected(RenderThemeImpl.hasState(state, 1));
        }

        @Override // com.sun.javafx.webkit.theme.RenderThemeImpl.Widget
        public WidgetType getType() {
            return WidgetType.RADIOBUTTON;
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/webkit/theme/RenderThemeImpl$FormSlider.class */
    private static final class FormSlider extends Slider implements FormControl {
        private FormSlider() {
        }

        @Override // com.sun.javafx.webkit.theme.RenderThemeImpl.FormControl
        public Control asControl() {
            return this;
        }

        @Override // com.sun.javafx.webkit.theme.RenderThemeImpl.FormControl
        public void setState(int state) {
            setDisabled(!RenderThemeImpl.hasState(state, 4));
            setFocused(RenderThemeImpl.hasState(state, 8));
            setHover(RenderThemeImpl.hasState(state, 32) && !isDisabled());
        }

        @Override // com.sun.javafx.webkit.theme.RenderThemeImpl.Widget
        public WidgetType getType() {
            return WidgetType.SLIDER;
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/webkit/theme/RenderThemeImpl$FormProgressBar.class */
    private static final class FormProgressBar extends ProgressBar implements FormControl {
        private final WidgetType type;

        private FormProgressBar(WidgetType type) {
            this.type = type;
        }

        @Override // com.sun.javafx.webkit.theme.RenderThemeImpl.FormControl
        public Control asControl() {
            return this;
        }

        @Override // com.sun.javafx.webkit.theme.RenderThemeImpl.FormControl
        public void setState(int state) {
            setDisabled(!RenderThemeImpl.hasState(state, 4));
            setFocused(RenderThemeImpl.hasState(state, 8));
            setHover(RenderThemeImpl.hasState(state, 32) && !isDisabled());
        }

        @Override // com.sun.javafx.webkit.theme.RenderThemeImpl.Widget
        public WidgetType getType() {
            return this.type;
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/webkit/theme/RenderThemeImpl$FormMenuList.class */
    private static final class FormMenuList extends ChoiceBox implements FormControl {
        private FormMenuList() {
            List<String> l2 = new ArrayList<>();
            l2.add("");
            setItems(FXCollections.observableList(l2));
        }

        @Override // com.sun.javafx.webkit.theme.RenderThemeImpl.FormControl
        public Control asControl() {
            return this;
        }

        @Override // com.sun.javafx.webkit.theme.RenderThemeImpl.FormControl
        public void setState(int state) {
            setDisabled(!RenderThemeImpl.hasState(state, 4));
            setFocused(RenderThemeImpl.hasState(state, 8));
            setHover(RenderThemeImpl.hasState(state, 32) && !isDisabled());
        }

        @Override // com.sun.javafx.webkit.theme.RenderThemeImpl.Widget
        public WidgetType getType() {
            return WidgetType.MENULIST;
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/webkit/theme/RenderThemeImpl$FormMenuListButton.class */
    private static final class FormMenuListButton extends Button implements FormControl {
        private static final int MAX_WIDTH = 20;
        private static final int MIN_WIDTH = 16;

        @Override // com.sun.javafx.webkit.theme.RenderThemeImpl.FormControl
        public Control asControl() {
            return this;
        }

        @Override // com.sun.javafx.webkit.theme.RenderThemeImpl.FormControl
        public void setState(int state) {
            setDisabled(!RenderThemeImpl.hasState(state, 4));
            setHover(RenderThemeImpl.hasState(state, 32));
            setPressed(RenderThemeImpl.hasState(state, 16));
            if (isPressed()) {
                arm();
            } else {
                disarm();
            }
        }

        private FormMenuListButton() {
            setSkin(new Skin());
            setFocusTraversable(false);
            getStyleClass().add("form-select-button");
        }

        @Override // javafx.scene.layout.Region, javafx.scene.Node
        public void resize(double width, double height) {
            double width2 = height > 20.0d ? 20.0d : height < 16.0d ? 16.0d : height;
            super.resize(width2, height);
            setTranslateX(-width2);
        }

        /* loaded from: jfxrt.jar:com/sun/javafx/webkit/theme/RenderThemeImpl$FormMenuListButton$Skin.class */
        private final class Skin extends BehaviorSkinBase {
            Skin() {
                super(FormMenuListButton.this, new BehaviorBase(FormMenuListButton.this, Collections.EMPTY_LIST));
                Region arrow = new Region();
                arrow.getStyleClass().add("arrow");
                arrow.setMaxSize(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
                BorderPane pane = new BorderPane();
                pane.setCenter(arrow);
                getChildren().add(pane);
            }
        }

        @Override // com.sun.javafx.webkit.theme.RenderThemeImpl.Widget
        public WidgetType getType() {
            return WidgetType.MENULISTBUTTON;
        }
    }
}
