package javax.swing.plaf.nimbus;

import javax.swing.JComponent;

/* loaded from: rt.jar:javax/swing/plaf/nimbus/TableHeaderRendererSortedState.class */
class TableHeaderRendererSortedState extends State {
    TableHeaderRendererSortedState() {
        super("Sorted");
    }

    @Override // javax.swing.plaf.nimbus.State
    protected boolean isInState(JComponent jComponent) {
        String str = (String) jComponent.getClientProperty("Table.sortOrder");
        return str != null && ("ASCENDING".equals(str) || "DESCENDING".equals(str));
    }
}
