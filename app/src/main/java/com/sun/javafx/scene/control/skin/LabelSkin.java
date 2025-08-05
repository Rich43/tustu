package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import java.util.Collections;
import javafx.scene.control.Label;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/LabelSkin.class */
public class LabelSkin extends LabeledSkinBase<Label, BehaviorBase<Label>> {
    public LabelSkin(Label label) {
        super(label, new BehaviorBase(label, Collections.emptyList()));
        consumeMouseEvents(false);
        registerChangeListener(label.labelForProperty(), "LABEL_FOR");
    }

    @Override // com.sun.javafx.scene.control.skin.LabeledSkinBase, com.sun.javafx.scene.control.skin.BehaviorSkinBase
    protected void handleControlPropertyChanged(String p2) {
        super.handleControlPropertyChanged(p2);
        if ("LABEL_FOR".equals(p2)) {
            mnemonicTargetChanged();
        }
    }
}
