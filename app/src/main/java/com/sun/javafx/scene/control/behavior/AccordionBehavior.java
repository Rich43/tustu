package com.sun.javafx.scene.control.behavior;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Accordion;
import javafx.scene.control.FocusModel;
import javafx.scene.control.TitledPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/behavior/AccordionBehavior.class */
public class AccordionBehavior extends BehaviorBase<Accordion> {
    private AccordionFocusModel focusModel;
    private static final String HOME = "Home";
    private static final String END = "End";
    private static final String PAGE_UP = "Page_Up";
    private static final String PAGE_DOWN = "Page_Down";
    private static final String CTRL_PAGE_UP = "Ctrl_Page_Up";
    private static final String CTRL_PAGE_DOWN = "Ctrl_Page_Down";
    private static final String CTRL_TAB = "Ctrl_Tab";
    private static final String CTRL_SHIFT_TAB = "Ctrl_Shift_Tab";
    protected static final List<KeyBinding> ACCORDION_BINDINGS = new ArrayList();

    public AccordionBehavior(Accordion accordion) {
        super(accordion, ACCORDION_BINDINGS);
        this.focusModel = new AccordionFocusModel(accordion);
    }

    @Override // com.sun.javafx.scene.control.behavior.BehaviorBase
    public void dispose() {
        this.focusModel.dispose();
        super.dispose();
    }

    static {
        ACCORDION_BINDINGS.add(new KeyBinding(KeyCode.UP, "TraverseUp"));
        ACCORDION_BINDINGS.add(new KeyBinding(KeyCode.DOWN, "TraverseDown"));
        ACCORDION_BINDINGS.add(new KeyBinding(KeyCode.LEFT, "TraverseLeft"));
        ACCORDION_BINDINGS.add(new KeyBinding(KeyCode.RIGHT, "TraverseRight"));
        ACCORDION_BINDINGS.add(new KeyBinding(KeyCode.HOME, HOME));
        ACCORDION_BINDINGS.add(new KeyBinding(KeyCode.END, END));
        ACCORDION_BINDINGS.add(new KeyBinding(KeyCode.PAGE_UP, PAGE_UP));
        ACCORDION_BINDINGS.add(new KeyBinding(KeyCode.PAGE_DOWN, PAGE_DOWN));
        ACCORDION_BINDINGS.add(new KeyBinding(KeyCode.PAGE_UP, CTRL_PAGE_UP).ctrl());
        ACCORDION_BINDINGS.add(new KeyBinding(KeyCode.PAGE_DOWN, CTRL_PAGE_DOWN).ctrl());
        ACCORDION_BINDINGS.add(new KeyBinding(KeyCode.TAB, CTRL_TAB).ctrl());
        ACCORDION_BINDINGS.add(new KeyBinding(KeyCode.TAB, CTRL_SHIFT_TAB).shift().ctrl());
    }

    @Override // com.sun.javafx.scene.control.behavior.BehaviorBase
    protected void callAction(String name) {
        Accordion accordion = getControl();
        boolean rtl = accordion.getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT;
        if (("TraverseLeft".equals(name) && !rtl) || (("TraverseRight".equals(name) && rtl) || "TraverseUp".equals(name) || PAGE_UP.equals(name))) {
            if (this.focusModel.getFocusedIndex() != -1 && accordion.getPanes().get(this.focusModel.getFocusedIndex()).isFocused()) {
                this.focusModel.focusPrevious();
                int next = this.focusModel.getFocusedIndex();
                accordion.getPanes().get(next).requestFocus();
                if (PAGE_UP.equals(name)) {
                    accordion.getPanes().get(next).setExpanded(true);
                    return;
                }
                return;
            }
            return;
        }
        if (("TraverseRight".equals(name) && !rtl) || (("TraverseLeft".equals(name) && rtl) || "TraverseDown".equals(name) || PAGE_DOWN.equals(name))) {
            if (this.focusModel.getFocusedIndex() != -1 && accordion.getPanes().get(this.focusModel.getFocusedIndex()).isFocused()) {
                this.focusModel.focusNext();
                int next2 = this.focusModel.getFocusedIndex();
                accordion.getPanes().get(next2).requestFocus();
                if (PAGE_DOWN.equals(name)) {
                    accordion.getPanes().get(next2).setExpanded(true);
                    return;
                }
                return;
            }
            return;
        }
        if (CTRL_TAB.equals(name) || CTRL_PAGE_DOWN.equals(name)) {
            this.focusModel.focusNext();
            if (this.focusModel.getFocusedIndex() != -1) {
                int next3 = this.focusModel.getFocusedIndex();
                accordion.getPanes().get(next3).requestFocus();
                accordion.getPanes().get(next3).setExpanded(true);
                return;
            }
            return;
        }
        if (CTRL_SHIFT_TAB.equals(name) || CTRL_PAGE_UP.equals(name)) {
            this.focusModel.focusPrevious();
            if (this.focusModel.getFocusedIndex() != -1) {
                int next4 = this.focusModel.getFocusedIndex();
                accordion.getPanes().get(next4).requestFocus();
                accordion.getPanes().get(next4).setExpanded(true);
                return;
            }
            return;
        }
        if (HOME.equals(name)) {
            if (this.focusModel.getFocusedIndex() != -1 && accordion.getPanes().get(this.focusModel.getFocusedIndex()).isFocused()) {
                TitledPane tp = accordion.getPanes().get(0);
                tp.requestFocus();
                tp.setExpanded(!tp.isExpanded());
                return;
            }
            return;
        }
        if (END.equals(name)) {
            if (this.focusModel.getFocusedIndex() != -1 && accordion.getPanes().get(this.focusModel.getFocusedIndex()).isFocused()) {
                TitledPane tp2 = accordion.getPanes().get(accordion.getPanes().size() - 1);
                tp2.requestFocus();
                tp2.setExpanded(!tp2.isExpanded());
                return;
            }
            return;
        }
        super.callAction(name);
    }

    @Override // com.sun.javafx.scene.control.behavior.BehaviorBase
    public void mousePressed(MouseEvent e2) {
        Accordion accordion = getControl();
        if (accordion.getPanes().size() > 0) {
            TitledPane lastTitledPane = accordion.getPanes().get(accordion.getPanes().size() - 1);
            lastTitledPane.requestFocus();
        } else {
            accordion.requestFocus();
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/behavior/AccordionBehavior$AccordionFocusModel.class */
    static class AccordionFocusModel extends FocusModel<TitledPane> {
        private final Accordion accordion;
        private final ChangeListener<Boolean> focusListener = new ChangeListener<Boolean>() { // from class: com.sun.javafx.scene.control.behavior.AccordionBehavior.AccordionFocusModel.1
            @Override // javafx.beans.value.ChangeListener
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue.booleanValue()) {
                    if (AccordionFocusModel.this.accordion.getExpandedPane() != null) {
                        AccordionFocusModel.this.accordion.getExpandedPane().requestFocus();
                    } else if (!AccordionFocusModel.this.accordion.getPanes().isEmpty()) {
                        AccordionFocusModel.this.accordion.getPanes().get(0).requestFocus();
                    }
                }
            }
        };
        private final ChangeListener<Boolean> paneFocusListener = new ChangeListener<Boolean>() { // from class: com.sun.javafx.scene.control.behavior.AccordionBehavior.AccordionFocusModel.2
            @Override // javafx.beans.value.ChangeListener
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue.booleanValue()) {
                    ReadOnlyBooleanProperty focusedProperty = (ReadOnlyBooleanProperty) observable;
                    TitledPane tp = (TitledPane) focusedProperty.getBean();
                    AccordionFocusModel.this.focus(AccordionFocusModel.this.accordion.getPanes().indexOf(tp));
                }
            }
        };
        private final ListChangeListener<TitledPane> panesListener = c2 -> {
            while (c2.next()) {
                if (c2.wasAdded()) {
                    for (TitledPane tp : c2.getAddedSubList()) {
                        tp.focusedProperty().addListener(this.paneFocusListener);
                    }
                } else if (c2.wasRemoved()) {
                    for (TitledPane tp2 : c2.getAddedSubList()) {
                        tp2.focusedProperty().removeListener(this.paneFocusListener);
                    }
                }
            }
        };

        public AccordionFocusModel(Accordion accordion) {
            if (accordion == null) {
                throw new IllegalArgumentException("Accordion can not be null");
            }
            this.accordion = accordion;
            this.accordion.focusedProperty().addListener(this.focusListener);
            this.accordion.getPanes().addListener(this.panesListener);
            for (TitledPane tp : this.accordion.getPanes()) {
                tp.focusedProperty().addListener(this.paneFocusListener);
            }
        }

        void dispose() {
            this.accordion.focusedProperty().removeListener(this.focusListener);
            this.accordion.getPanes().removeListener(this.panesListener);
            for (TitledPane tp : this.accordion.getPanes()) {
                tp.focusedProperty().removeListener(this.paneFocusListener);
            }
        }

        @Override // javafx.scene.control.FocusModel
        protected int getItemCount() {
            ObservableList<TitledPane> panes = this.accordion.getPanes();
            if (panes == null) {
                return 0;
            }
            return panes.size();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // javafx.scene.control.FocusModel
        public TitledPane getModelItem(int row) {
            ObservableList<TitledPane> panes = this.accordion.getPanes();
            if (panes != null && row >= 0) {
                return panes.get(row % panes.size());
            }
            return null;
        }

        @Override // javafx.scene.control.FocusModel
        public void focusPrevious() {
            if (getFocusedIndex() <= 0) {
                focus(this.accordion.getPanes().size() - 1);
            } else {
                focus((getFocusedIndex() - 1) % this.accordion.getPanes().size());
            }
        }

        @Override // javafx.scene.control.FocusModel
        public void focusNext() {
            if (getFocusedIndex() == -1) {
                focus(0);
            } else {
                focus((getFocusedIndex() + 1) % this.accordion.getPanes().size());
            }
        }
    }
}
