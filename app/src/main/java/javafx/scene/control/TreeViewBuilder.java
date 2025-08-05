package javafx.scene.control;

import javafx.event.EventHandler;
import javafx.scene.control.TreeView;
import javafx.scene.control.TreeViewBuilder;
import javafx.util.Builder;
import javafx.util.Callback;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/control/TreeViewBuilder.class */
public class TreeViewBuilder<T, B extends TreeViewBuilder<T, B>> extends ControlBuilder<B> implements Builder<TreeView<T>> {
    private int __set;
    private Callback<TreeView<T>, TreeCell<T>> cellFactory;
    private boolean editable;
    private FocusModel<TreeItem<T>> focusModel;
    private EventHandler<TreeView.EditEvent<T>> onEditCancel;
    private EventHandler<TreeView.EditEvent<T>> onEditCommit;
    private EventHandler<TreeView.EditEvent<T>> onEditStart;
    private TreeItem<T> root;
    private MultipleSelectionModel<TreeItem<T>> selectionModel;
    private boolean showRoot;

    protected TreeViewBuilder() {
    }

    public static <T> TreeViewBuilder<T, ?> create() {
        return new TreeViewBuilder<>();
    }

    private void __set(int i2) {
        this.__set |= 1 << i2;
    }

    public void applyTo(TreeView<T> x2) {
        super.applyTo((Control) x2);
        int set = this.__set;
        while (set != 0) {
            int i2 = Integer.numberOfTrailingZeros(set);
            set &= (1 << i2) ^ (-1);
            switch (i2) {
                case 0:
                    x2.setCellFactory(this.cellFactory);
                    break;
                case 1:
                    x2.setEditable(this.editable);
                    break;
                case 2:
                    x2.setFocusModel(this.focusModel);
                    break;
                case 3:
                    x2.setOnEditCancel(this.onEditCancel);
                    break;
                case 4:
                    x2.setOnEditCommit(this.onEditCommit);
                    break;
                case 5:
                    x2.setOnEditStart(this.onEditStart);
                    break;
                case 6:
                    x2.setRoot(this.root);
                    break;
                case 7:
                    x2.setSelectionModel(this.selectionModel);
                    break;
                case 8:
                    x2.setShowRoot(this.showRoot);
                    break;
            }
        }
    }

    public B cellFactory(Callback<TreeView<T>, TreeCell<T>> x2) {
        this.cellFactory = x2;
        __set(0);
        return this;
    }

    public B editable(boolean x2) {
        this.editable = x2;
        __set(1);
        return this;
    }

    public B focusModel(FocusModel<TreeItem<T>> x2) {
        this.focusModel = x2;
        __set(2);
        return this;
    }

    public B onEditCancel(EventHandler<TreeView.EditEvent<T>> x2) {
        this.onEditCancel = x2;
        __set(3);
        return this;
    }

    public B onEditCommit(EventHandler<TreeView.EditEvent<T>> x2) {
        this.onEditCommit = x2;
        __set(4);
        return this;
    }

    public B onEditStart(EventHandler<TreeView.EditEvent<T>> x2) {
        this.onEditStart = x2;
        __set(5);
        return this;
    }

    public B root(TreeItem<T> x2) {
        this.root = x2;
        __set(6);
        return this;
    }

    public B selectionModel(MultipleSelectionModel<TreeItem<T>> x2) {
        this.selectionModel = x2;
        __set(7);
        return this;
    }

    public B showRoot(boolean x2) {
        this.showRoot = x2;
        __set(8);
        return this;
    }

    @Override // javafx.util.Builder
    /* renamed from: build */
    public TreeView<T> build2() {
        TreeView<T> x2 = new TreeView<>();
        applyTo((TreeView) x2);
        return x2;
    }
}
