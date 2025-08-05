package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.ButtonBehavior;
import javafx.scene.control.Hyperlink;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/HyperlinkSkin.class */
public class HyperlinkSkin extends LabeledSkinBase<Hyperlink, ButtonBehavior<Hyperlink>> {
    public HyperlinkSkin(Hyperlink link) {
        super(link, new ButtonBehavior(link));
    }
}
