package javafx.scene.web;

import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.ParentBuilder;
import javafx.util.Builder;
import javafx.util.Callback;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/web/WebViewBuilder.class */
public final class WebViewBuilder extends ParentBuilder<WebViewBuilder> implements Builder<WebView> {
    private double fontScale;
    private boolean fontScaleSet;
    private double maxHeight;
    private boolean maxHeightSet;
    private double maxWidth;
    private boolean maxWidthSet;
    private double minHeight;
    private boolean minHeightSet;
    private double minWidth;
    private boolean minWidthSet;
    private double prefHeight;
    private boolean prefHeightSet;
    private double prefWidth;
    private boolean prefWidthSet;
    private WebEngineBuilder engineBuilder;

    public static WebViewBuilder create() {
        return new WebViewBuilder();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    /* renamed from: build */
    public WebView build2() {
        WebView x2 = new WebView();
        applyTo(x2);
        return x2;
    }

    public void applyTo(WebView view) {
        super.applyTo((Parent) view);
        if (this.fontScaleSet) {
            view.setFontScale(this.fontScale);
        }
        if (this.maxHeightSet) {
            view.setMaxHeight(this.maxHeight);
        }
        if (this.maxWidthSet) {
            view.setMaxWidth(this.maxWidth);
        }
        if (this.minHeightSet) {
            view.setMinHeight(this.minHeight);
        }
        if (this.minWidthSet) {
            view.setMinWidth(this.minWidth);
        }
        if (this.prefHeightSet) {
            view.setPrefHeight(this.prefHeight);
        }
        if (this.prefWidthSet) {
            view.setPrefWidth(this.prefWidth);
        }
        if (this.engineBuilder != null) {
            this.engineBuilder.applyTo(view.getEngine());
        }
    }

    public WebViewBuilder fontScale(double value) {
        this.fontScale = value;
        this.fontScaleSet = true;
        return this;
    }

    public WebViewBuilder maxHeight(double value) {
        this.maxHeight = value;
        this.maxHeightSet = true;
        return this;
    }

    public WebViewBuilder maxWidth(double value) {
        this.maxWidth = value;
        this.maxWidthSet = true;
        return this;
    }

    public WebViewBuilder minHeight(double value) {
        this.minHeight = value;
        this.minHeightSet = true;
        return this;
    }

    public WebViewBuilder minWidth(double value) {
        this.minWidth = value;
        this.minWidthSet = true;
        return this;
    }

    public WebViewBuilder prefHeight(double value) {
        this.prefHeight = value;
        this.prefHeightSet = true;
        return this;
    }

    public WebViewBuilder prefWidth(double value) {
        this.prefWidth = value;
        this.prefWidthSet = true;
        return this;
    }

    public WebViewBuilder confirmHandler(Callback<String, Boolean> value) {
        engineBuilder().confirmHandler(value);
        return this;
    }

    public WebViewBuilder createPopupHandler(Callback<PopupFeatures, WebEngine> value) {
        engineBuilder().createPopupHandler(value);
        return this;
    }

    public WebViewBuilder onAlert(EventHandler<WebEvent<String>> value) {
        engineBuilder().onAlert(value);
        return this;
    }

    public WebViewBuilder onResized(EventHandler<WebEvent<Rectangle2D>> value) {
        engineBuilder().onResized(value);
        return this;
    }

    public WebViewBuilder onStatusChanged(EventHandler<WebEvent<String>> value) {
        engineBuilder().onStatusChanged(value);
        return this;
    }

    public WebViewBuilder onVisibilityChanged(EventHandler<WebEvent<Boolean>> value) {
        engineBuilder().onVisibilityChanged(value);
        return this;
    }

    public WebViewBuilder promptHandler(Callback<PromptData, String> value) {
        engineBuilder().promptHandler(value);
        return this;
    }

    public WebViewBuilder location(String value) {
        engineBuilder().location(value);
        return this;
    }

    private WebEngineBuilder engineBuilder() {
        if (this.engineBuilder == null) {
            this.engineBuilder = WebEngineBuilder.create();
        }
        return this.engineBuilder;
    }
}
