package com.sun.javafx.scene.web.behavior;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.behavior.KeyBinding;
import com.sun.javafx.scene.web.skin.HTMLEditorSkin;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.input.KeyCode;
import javafx.scene.web.HTMLEditor;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/web/behavior/HTMLEditorBehavior.class */
public class HTMLEditorBehavior extends BehaviorBase<HTMLEditor> {
    protected static final List<KeyBinding> HTML_EDITOR_BINDINGS = new ArrayList();

    static {
        HTML_EDITOR_BINDINGS.add(new KeyBinding(KeyCode.B, "bold").shortcut());
        HTML_EDITOR_BINDINGS.add(new KeyBinding(KeyCode.I, "italic").shortcut());
        HTML_EDITOR_BINDINGS.add(new KeyBinding(KeyCode.U, "underline").shortcut());
        HTML_EDITOR_BINDINGS.add(new KeyBinding(KeyCode.F12, "F12"));
        HTML_EDITOR_BINDINGS.add(new KeyBinding(KeyCode.TAB, "TraverseNext").ctrl());
        HTML_EDITOR_BINDINGS.add(new KeyBinding(KeyCode.TAB, "TraversePrevious").ctrl().shift());
    }

    public HTMLEditorBehavior(HTMLEditor htmlEditor) {
        super(htmlEditor, HTML_EDITOR_BINDINGS);
    }

    @Override // com.sun.javafx.scene.control.behavior.BehaviorBase
    protected void callAction(String name) {
        if ("bold".equals(name) || "italic".equals(name) || "underline".equals(name)) {
            HTMLEditor editor = getControl();
            HTMLEditorSkin editorSkin = (HTMLEditorSkin) editor.getSkin();
            editorSkin.keyboardShortcuts(name);
        } else if ("F12".equals(name)) {
            getControl().getImpl_traversalEngine().selectFirst().requestFocus();
        } else {
            super.callAction(name);
        }
    }
}
