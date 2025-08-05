package javafx.scene.control;

import java.util.Arrays;
import java.util.Collection;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.TabBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/control/TabBuilder.class */
public class TabBuilder<B extends TabBuilder<B>> implements Builder<Tab> {
    private int __set;
    private boolean closable;
    private Node content;
    private ContextMenu contextMenu;
    private boolean disable;
    private Node graphic;
    private String id;
    private EventHandler<Event> onClosed;
    private EventHandler<Event> onSelectionChanged;
    private String style;
    private Collection<? extends String> styleClass;
    private String text;
    private Tooltip tooltip;
    private Object userData;

    protected TabBuilder() {
    }

    public static TabBuilder<?> create() {
        return new TabBuilder<>();
    }

    private void __set(int i2) {
        this.__set |= 1 << i2;
    }

    public void applyTo(Tab x2) {
        int set = this.__set;
        while (set != 0) {
            int i2 = Integer.numberOfTrailingZeros(set);
            set &= (1 << i2) ^ (-1);
            switch (i2) {
                case 0:
                    x2.setClosable(this.closable);
                    break;
                case 1:
                    x2.setContent(this.content);
                    break;
                case 2:
                    x2.setContextMenu(this.contextMenu);
                    break;
                case 3:
                    x2.setDisable(this.disable);
                    break;
                case 4:
                    x2.setGraphic(this.graphic);
                    break;
                case 5:
                    x2.setId(this.id);
                    break;
                case 6:
                    x2.setOnClosed(this.onClosed);
                    break;
                case 7:
                    x2.setOnSelectionChanged(this.onSelectionChanged);
                    break;
                case 8:
                    x2.setStyle(this.style);
                    break;
                case 9:
                    x2.getStyleClass().addAll(this.styleClass);
                    break;
                case 10:
                    x2.setText(this.text);
                    break;
                case 11:
                    x2.setTooltip(this.tooltip);
                    break;
                case 12:
                    x2.setUserData(this.userData);
                    break;
            }
        }
    }

    public B closable(boolean x2) {
        this.closable = x2;
        __set(0);
        return this;
    }

    public B content(Node x2) {
        this.content = x2;
        __set(1);
        return this;
    }

    public B contextMenu(ContextMenu x2) {
        this.contextMenu = x2;
        __set(2);
        return this;
    }

    public B disable(boolean x2) {
        this.disable = x2;
        __set(3);
        return this;
    }

    public B graphic(Node x2) {
        this.graphic = x2;
        __set(4);
        return this;
    }

    public B id(String x2) {
        this.id = x2;
        __set(5);
        return this;
    }

    public B onClosed(EventHandler<Event> x2) {
        this.onClosed = x2;
        __set(6);
        return this;
    }

    public B onSelectionChanged(EventHandler<Event> x2) {
        this.onSelectionChanged = x2;
        __set(7);
        return this;
    }

    public B style(String x2) {
        this.style = x2;
        __set(8);
        return this;
    }

    public B styleClass(Collection<? extends String> x2) {
        this.styleClass = x2;
        __set(9);
        return this;
    }

    public B styleClass(String... strArr) {
        return (B) styleClass(Arrays.asList(strArr));
    }

    public B text(String x2) {
        this.text = x2;
        __set(10);
        return this;
    }

    public B tooltip(Tooltip x2) {
        this.tooltip = x2;
        __set(11);
        return this;
    }

    public B userData(Object x2) {
        this.userData = x2;
        __set(12);
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    /* renamed from: build */
    public Tab build2() {
        Tab x2 = new Tab();
        applyTo(x2);
        return x2;
    }
}
