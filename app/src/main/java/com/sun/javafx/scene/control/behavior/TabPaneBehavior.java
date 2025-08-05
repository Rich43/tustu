package com.sun.javafx.scene.control.behavior;

import java.util.ArrayList;
import java.util.List;
import javafx.event.Event;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/behavior/TabPaneBehavior.class */
public class TabPaneBehavior extends BehaviorBase<TabPane> {
    private static final String HOME = "Home";
    private static final String END = "End";
    private static final String CTRL_PAGE_UP = "Ctrl_Page_Up";
    private static final String CTRL_PAGE_DOWN = "Ctrl_Page_Down";
    private static final String CTRL_TAB = "Ctrl_Tab";
    private static final String CTRL_SHIFT_TAB = "Ctrl_Shift_Tab";
    protected static final List<KeyBinding> TAB_PANE_BINDINGS = new ArrayList();

    static {
        TAB_PANE_BINDINGS.add(new KeyBinding(KeyCode.UP, "TraverseUp"));
        TAB_PANE_BINDINGS.add(new KeyBinding(KeyCode.DOWN, "TraverseDown"));
        TAB_PANE_BINDINGS.add(new KeyBinding(KeyCode.LEFT, "TraverseLeft"));
        TAB_PANE_BINDINGS.add(new KeyBinding(KeyCode.RIGHT, "TraverseRight"));
        TAB_PANE_BINDINGS.add(new KeyBinding(KeyCode.HOME, HOME));
        TAB_PANE_BINDINGS.add(new KeyBinding(KeyCode.END, END));
        TAB_PANE_BINDINGS.add(new KeyBinding(KeyCode.PAGE_UP, CTRL_PAGE_UP).ctrl());
        TAB_PANE_BINDINGS.add(new KeyBinding(KeyCode.PAGE_DOWN, CTRL_PAGE_DOWN).ctrl());
        TAB_PANE_BINDINGS.add(new KeyBinding(KeyCode.TAB, CTRL_TAB).ctrl());
        TAB_PANE_BINDINGS.add(new KeyBinding(KeyCode.TAB, CTRL_SHIFT_TAB).shift().ctrl());
    }

    @Override // com.sun.javafx.scene.control.behavior.BehaviorBase
    protected void callAction(String name) {
        boolean rtl = getControl().getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT;
        if (("TraverseLeft".equals(name) && !rtl) || (("TraverseRight".equals(name) && rtl) || "TraverseUp".equals(name))) {
            if (getControl().isFocused()) {
                selectPreviousTab();
                return;
            }
            return;
        }
        if (("TraverseRight".equals(name) && !rtl) || (("TraverseLeft".equals(name) && rtl) || "TraverseDown".equals(name))) {
            if (getControl().isFocused()) {
                selectNextTab();
                return;
            }
            return;
        }
        if (CTRL_TAB.equals(name) || CTRL_PAGE_DOWN.equals(name)) {
            selectNextTab();
            return;
        }
        if (CTRL_SHIFT_TAB.equals(name) || CTRL_PAGE_UP.equals(name)) {
            selectPreviousTab();
            return;
        }
        if (HOME.equals(name)) {
            if (getControl().isFocused()) {
                moveSelection(0, 1);
            }
        } else {
            if (END.equals(name)) {
                if (getControl().isFocused()) {
                    moveSelection(getControl().getTabs().size() - 1, -1);
                    return;
                }
                return;
            }
            super.callAction(name);
        }
    }

    @Override // com.sun.javafx.scene.control.behavior.BehaviorBase
    public void mousePressed(MouseEvent e2) {
        super.mousePressed(e2);
        TabPane tp = getControl();
        tp.requestFocus();
    }

    public TabPaneBehavior(TabPane tabPane) {
        super(tabPane, TAB_PANE_BINDINGS);
    }

    public void selectTab(Tab tab) {
        getControl().getSelectionModel().select((SingleSelectionModel<Tab>) tab);
    }

    public boolean canCloseTab(Tab tab) {
        Event event = new Event(tab, tab, Tab.TAB_CLOSE_REQUEST_EVENT);
        Event.fireEvent(tab, event);
        return !event.isConsumed();
    }

    public void closeTab(Tab tab) {
        TabPane tabPane = getControl();
        int index = tabPane.getTabs().indexOf(tab);
        if (index != -1) {
            tabPane.getTabs().remove(index);
        }
        if (tab.getOnClosed() != null) {
            Event.fireEvent(tab, new Event(Tab.CLOSED_EVENT));
        }
    }

    public void selectNextTab() {
        moveSelection(1);
    }

    public void selectPreviousTab() {
        moveSelection(-1);
    }

    private void moveSelection(int delta) {
        moveSelection(getControl().getSelectionModel().getSelectedIndex(), delta);
    }

    private void moveSelection(int startIndex, int delta) {
        TabPane tabPane = getControl();
        int tabIndex = findValidTab(startIndex, delta);
        if (tabIndex > -1) {
            SelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
            selectionModel.select(tabIndex);
        }
        tabPane.requestFocus();
    }

    private int findValidTab(int startIndex, int delta) {
        TabPane tabPane = getControl();
        List<Tab> tabs = tabPane.getTabs();
        int max = tabs.size();
        int index = startIndex;
        do {
            index = nextIndex(index + delta, max);
            Tab tab = tabs.get(index);
            if (tab != null && !tab.isDisable()) {
                return index;
            }
        } while (index != startIndex);
        return -1;
    }

    private int nextIndex(int value, int max) {
        int r2 = value % max;
        if (r2 > 0 && max < 0) {
            r2 = (r2 + max) - 0;
        } else if (r2 < 0 && max > 0) {
            r2 = (r2 + max) - 0;
        }
        return r2;
    }
}
