package com.sun.javafx.tk.quantum;

import com.sun.javafx.scene.DirtyBits;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

/* loaded from: jfxrt.jar:com/sun/javafx/tk/quantum/OverlayWarning.class */
public class OverlayWarning extends Group {
    private static final float PAD = 40.0f;
    private static final float RECTW = 600.0f;
    private static final float RECTH = 100.0f;
    private static final float ARC = 20.0f;
    private static final int FONTSIZE = 24;
    private ViewScene view;
    private SequentialTransition overlayTransition;
    private boolean warningTransition;
    private Text text = new Text();
    private Rectangle background;

    public OverlayWarning(ViewScene vs) {
        this.view = vs;
        createOverlayGroup();
        PauseTransition pause = new PauseTransition(Duration.millis(4000.0d));
        FadeTransition fade = new FadeTransition(Duration.millis(1000.0d), this);
        fade.setFromValue(1.0d);
        fade.setToValue(0.0d);
        this.overlayTransition = new SequentialTransition();
        this.overlayTransition.getChildren().add(pause);
        this.overlayTransition.getChildren().add(fade);
        this.overlayTransition.setOnFinished(event -> {
            this.warningTransition = false;
            this.view.getWindowStage().setWarning(null);
        });
    }

    protected ViewScene getView() {
        return this.view;
    }

    protected final void setView(ViewScene vs) {
        if (this.view != null) {
            this.view.getWindowStage().setWarning(null);
        }
        this.view = vs;
        this.view.entireSceneNeedsRepaint();
    }

    protected void warn(String msg) {
        this.text.setText(msg);
        this.warningTransition = true;
        this.overlayTransition.play();
    }

    protected void cancel() {
        if (this.overlayTransition != null && this.overlayTransition.getStatus() == Animation.Status.RUNNING) {
            this.overlayTransition.stop();
            this.warningTransition = false;
        }
        this.view.getWindowStage().setWarning(null);
    }

    protected boolean inWarningTransition() {
        return this.warningTransition;
    }

    private void createOverlayGroup() {
        Font font = new Font(Font.getDefault().getFamily(), 24.0d);
        Rectangle2D screenBounds = new Rectangle2D(0.0d, 0.0d, this.view.getSceneState().getScreenWidth(), this.view.getSceneState().getScreenHeight());
        this.text.setStroke(Color.WHITE);
        this.text.setFill(Color.WHITE);
        this.text.setFont(font);
        this.text.setWrappingWidth(520.0d);
        this.text.setStyle("-fx-effect: dropshadow(two-pass-box, rgba(0,0,0,0.75), 3, 0.0, 0, 2);");
        this.text.setTextAlignment(TextAlignment.CENTER);
        this.background = createBackground(this.text, screenBounds);
        getChildren().add(this.background);
        getChildren().add(this.text);
    }

    private Rectangle createBackground(Text text, Rectangle2D screen) {
        Rectangle rectangle = new Rectangle();
        double textW = text.getLayoutBounds().getWidth();
        double textH = text.getLayoutBounds().getHeight();
        double rectX = (screen.getWidth() - 600.0d) / 2.0d;
        double rectY = screen.getHeight() / 2.0d;
        rectangle.setWidth(600.0d);
        rectangle.setHeight(100.0d);
        rectangle.setX(rectX);
        rectangle.setY(rectY - 100.0d);
        rectangle.setArcWidth(20.0d);
        rectangle.setArcHeight(20.0d);
        rectangle.setFill(Color.gray(0.0d, 0.6d));
        text.setX(rectX + ((600.0d - textW) / 2.0d));
        text.setY((rectY - 50.0d) + ((textH - text.getBaselineOffset()) / 2.0d));
        return rectangle;
    }

    @Override // javafx.scene.Parent, javafx.scene.Node
    public void impl_updatePeer() {
        this.text.impl_updatePeer();
        this.background.impl_updatePeer();
        super.impl_updatePeer();
    }

    @Override // javafx.scene.Parent
    protected void updateBounds() {
        super.updateBounds();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // javafx.scene.Node
    public void impl_markDirty(DirtyBits dirtyBit) {
        super.impl_markDirty(dirtyBit);
        this.view.synchroniseOverlayWarning();
    }
}
