package javafx.scene.control;

import java.util.Arrays;
import java.util.Collection;
import javafx.scene.control.AccordionBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/control/AccordionBuilder.class */
public class AccordionBuilder<B extends AccordionBuilder<B>> extends ControlBuilder<B> implements Builder<Accordion> {
    private int __set;
    private TitledPane expandedPane;
    private Collection<? extends TitledPane> panes;

    protected AccordionBuilder() {
    }

    public static AccordionBuilder<?> create() {
        return new AccordionBuilder<>();
    }

    public void applyTo(Accordion x2) {
        super.applyTo((Control) x2);
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setExpandedPane(this.expandedPane);
        }
        if ((set & 2) != 0) {
            x2.getPanes().addAll(this.panes);
        }
    }

    public B expandedPane(TitledPane x2) {
        this.expandedPane = x2;
        this.__set |= 1;
        return this;
    }

    public B panes(Collection<? extends TitledPane> x2) {
        this.panes = x2;
        this.__set |= 2;
        return this;
    }

    public B panes(TitledPane... titledPaneArr) {
        return (B) panes(Arrays.asList(titledPaneArr));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    /* renamed from: build */
    public Accordion build2() {
        Accordion x2 = new Accordion();
        applyTo(x2);
        return x2;
    }
}
