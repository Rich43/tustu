package javafx.scene.web;

import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.util.Builder;
import javafx.util.Callback;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/web/WebEngineBuilder.class */
public final class WebEngineBuilder implements Builder<WebEngine> {
    private Callback<String, Boolean> confirmHandler;
    private boolean confirmHandlerSet;
    private Callback<PopupFeatures, WebEngine> createPopupHandler;
    private boolean createPopupHandlerSet;
    private EventHandler<WebEvent<String>> onAlert;
    private boolean onAlertSet;
    private EventHandler<WebEvent<Rectangle2D>> onResized;
    private boolean onResizedSet;
    private EventHandler<WebEvent<String>> onStatusChanged;
    private boolean onStatusChangedSet;
    private EventHandler<WebEvent<Boolean>> onVisibilityChanged;
    private boolean onVisibilityChangedSet;
    private Callback<PromptData, String> promptHandler;
    private boolean promptHandlerSet;
    private String location;
    private boolean locationSet;

    public static WebEngineBuilder create() {
        return new WebEngineBuilder();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    /* renamed from: build */
    public WebEngine build2() {
        WebEngine engine = new WebEngine();
        applyTo(engine);
        return engine;
    }

    public void applyTo(WebEngine engine) {
        if (this.confirmHandlerSet) {
            engine.setConfirmHandler(this.confirmHandler);
        }
        if (this.createPopupHandlerSet) {
            engine.setCreatePopupHandler(this.createPopupHandler);
        }
        if (this.onAlertSet) {
            engine.setOnAlert(this.onAlert);
        }
        if (this.onResizedSet) {
            engine.setOnResized(this.onResized);
        }
        if (this.onStatusChangedSet) {
            engine.setOnStatusChanged(this.onStatusChanged);
        }
        if (this.onVisibilityChangedSet) {
            engine.setOnVisibilityChanged(this.onVisibilityChanged);
        }
        if (this.promptHandlerSet) {
            engine.setPromptHandler(this.promptHandler);
        }
        if (this.locationSet) {
            engine.load(this.location);
        }
    }

    public WebEngineBuilder confirmHandler(Callback<String, Boolean> value) {
        this.confirmHandler = value;
        this.confirmHandlerSet = true;
        return this;
    }

    public WebEngineBuilder createPopupHandler(Callback<PopupFeatures, WebEngine> value) {
        this.createPopupHandler = value;
        this.createPopupHandlerSet = true;
        return this;
    }

    public WebEngineBuilder onAlert(EventHandler<WebEvent<String>> value) {
        this.onAlert = value;
        this.onAlertSet = true;
        return this;
    }

    public WebEngineBuilder onResized(EventHandler<WebEvent<Rectangle2D>> value) {
        this.onResized = value;
        this.onResizedSet = true;
        return this;
    }

    public WebEngineBuilder onStatusChanged(EventHandler<WebEvent<String>> value) {
        this.onStatusChanged = value;
        this.onStatusChangedSet = true;
        return this;
    }

    public WebEngineBuilder onVisibilityChanged(EventHandler<WebEvent<Boolean>> value) {
        this.onVisibilityChanged = value;
        this.onVisibilityChangedSet = true;
        return this;
    }

    public WebEngineBuilder promptHandler(Callback<PromptData, String> value) {
        this.promptHandler = value;
        this.promptHandlerSet = true;
        return this;
    }

    public WebEngineBuilder location(String value) {
        this.location = value;
        this.locationSet = true;
        return this;
    }
}
