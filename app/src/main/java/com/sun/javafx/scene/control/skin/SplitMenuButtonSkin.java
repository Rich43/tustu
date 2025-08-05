package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.SplitMenuButtonBehavior;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.input.MouseEvent;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/SplitMenuButtonSkin.class */
public class SplitMenuButtonSkin extends MenuButtonSkinBase<SplitMenuButton, SplitMenuButtonBehavior> {
    public SplitMenuButtonSkin(SplitMenuButton splitMenuButton) {
        super(splitMenuButton, new SplitMenuButtonBehavior(splitMenuButton));
        this.behaveLikeButton = true;
        this.arrowButton.addEventHandler(MouseEvent.ANY, event -> {
            event.consume();
        });
        this.arrowButton.setOnMousePressed(e2 -> {
            ((SplitMenuButtonBehavior) getBehavior()).mousePressed(e2, false);
            e2.consume();
        });
        this.arrowButton.setOnMouseReleased(e3 -> {
            ((SplitMenuButtonBehavior) getBehavior()).mouseReleased(e3, false);
            e3.consume();
        });
        this.label.setLabelFor(splitMenuButton);
    }
}
