package javax.accessibility;

/* loaded from: rt.jar:javax/accessibility/AccessibleAction.class */
public interface AccessibleAction {
    public static final String TOGGLE_EXPAND = new String("toggleexpand");
    public static final String INCREMENT = new String("increment");
    public static final String DECREMENT = new String("decrement");
    public static final String CLICK = new String("click");
    public static final String TOGGLE_POPUP = new String("toggle popup");

    int getAccessibleActionCount();

    String getAccessibleActionDescription(int i2);

    boolean doAccessibleAction(int i2);
}
