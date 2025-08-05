package javafx.scene.control;

import javafx.scene.control.CheckBoxTreeItemBuilder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/control/CheckBoxTreeItemBuilder.class */
public class CheckBoxTreeItemBuilder<T, B extends CheckBoxTreeItemBuilder<T, B>> extends TreeItemBuilder<T, B> {
    protected CheckBoxTreeItemBuilder() {
    }

    public static <T> CheckBoxTreeItemBuilder<T, ?> create() {
        return new CheckBoxTreeItemBuilder<>();
    }

    @Override // javafx.scene.control.TreeItemBuilder, javafx.util.Builder
    /* renamed from: build */
    public CheckBoxTreeItem<T> build2() {
        CheckBoxTreeItem<T> x2 = new CheckBoxTreeItem<>();
        applyTo(x2);
        return x2;
    }
}
