package javafx.scene.control;

import java.util.Arrays;
import java.util.Collection;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.MenuItemBuilder;
import javafx.scene.input.KeyCombination;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/control/MenuItemBuilder.class */
public class MenuItemBuilder<B extends MenuItemBuilder<B>> implements Builder<MenuItem> {
    private int __set;
    private KeyCombination accelerator;
    private boolean disable;
    private Node graphic;
    private String id;
    private boolean mnemonicParsing;
    private EventHandler<ActionEvent> onAction;
    private EventHandler<Event> onMenuValidation;
    private String style;
    private Collection<? extends String> styleClass;
    private String text;
    private Object userData;
    private boolean visible;

    protected MenuItemBuilder() {
    }

    public static MenuItemBuilder<?> create() {
        return new MenuItemBuilder<>();
    }

    private void __set(int i2) {
        this.__set |= 1 << i2;
    }

    public void applyTo(MenuItem x2) {
        int set = this.__set;
        while (set != 0) {
            int i2 = Integer.numberOfTrailingZeros(set);
            set &= (1 << i2) ^ (-1);
            switch (i2) {
                case 0:
                    x2.setAccelerator(this.accelerator);
                    break;
                case 1:
                    x2.setDisable(this.disable);
                    break;
                case 2:
                    x2.setGraphic(this.graphic);
                    break;
                case 3:
                    x2.setId(this.id);
                    break;
                case 4:
                    x2.setMnemonicParsing(this.mnemonicParsing);
                    break;
                case 5:
                    x2.setOnAction(this.onAction);
                    break;
                case 6:
                    x2.setOnMenuValidation(this.onMenuValidation);
                    break;
                case 7:
                    x2.setStyle(this.style);
                    break;
                case 8:
                    x2.getStyleClass().addAll(this.styleClass);
                    break;
                case 9:
                    x2.setText(this.text);
                    break;
                case 10:
                    x2.setUserData(this.userData);
                    break;
                case 11:
                    x2.setVisible(this.visible);
                    break;
            }
        }
    }

    public B accelerator(KeyCombination x2) {
        this.accelerator = x2;
        __set(0);
        return this;
    }

    public B disable(boolean x2) {
        this.disable = x2;
        __set(1);
        return this;
    }

    public B graphic(Node x2) {
        this.graphic = x2;
        __set(2);
        return this;
    }

    public B id(String x2) {
        this.id = x2;
        __set(3);
        return this;
    }

    public B mnemonicParsing(boolean x2) {
        this.mnemonicParsing = x2;
        __set(4);
        return this;
    }

    public B onAction(EventHandler<ActionEvent> x2) {
        this.onAction = x2;
        __set(5);
        return this;
    }

    public B onMenuValidation(EventHandler<Event> x2) {
        this.onMenuValidation = x2;
        __set(6);
        return this;
    }

    public B style(String x2) {
        this.style = x2;
        __set(7);
        return this;
    }

    public B styleClass(Collection<? extends String> x2) {
        this.styleClass = x2;
        __set(8);
        return this;
    }

    public B styleClass(String... strArr) {
        return (B) styleClass(Arrays.asList(strArr));
    }

    public B text(String x2) {
        this.text = x2;
        __set(9);
        return this;
    }

    public B userData(Object x2) {
        this.userData = x2;
        __set(10);
        return this;
    }

    public B visible(boolean x2) {
        this.visible = x2;
        __set(11);
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    /* renamed from: build */
    public MenuItem build2() {
        MenuItem x2 = new MenuItem();
        applyTo(x2);
        return x2;
    }
}
