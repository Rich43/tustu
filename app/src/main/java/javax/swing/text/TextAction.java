package javax.swing.text;

import java.awt.event.ActionEvent;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.swing.AbstractAction;
import javax.swing.Action;

/* loaded from: rt.jar:javax/swing/text/TextAction.class */
public abstract class TextAction extends AbstractAction {
    public TextAction(String str) {
        super(str);
    }

    protected final JTextComponent getTextComponent(ActionEvent actionEvent) {
        if (actionEvent != null) {
            Object source = actionEvent.getSource();
            if (source instanceof JTextComponent) {
                return (JTextComponent) source;
            }
        }
        return getFocusedComponent();
    }

    public static final Action[] augmentList(Action[] actionArr, Action[] actionArr2) {
        Hashtable hashtable = new Hashtable();
        for (Action action : actionArr) {
            String str = (String) action.getValue("Name");
            hashtable.put(str != null ? str : "", action);
        }
        for (Action action2 : actionArr2) {
            String str2 = (String) action2.getValue("Name");
            hashtable.put(str2 != null ? str2 : "", action2);
        }
        Action[] actionArr3 = new Action[hashtable.size()];
        int i2 = 0;
        Enumeration enumerationElements = hashtable.elements();
        while (enumerationElements.hasMoreElements()) {
            int i3 = i2;
            i2++;
            actionArr3[i3] = (Action) enumerationElements.nextElement2();
        }
        return actionArr3;
    }

    protected final JTextComponent getFocusedComponent() {
        return JTextComponent.getFocusedComponent();
    }
}
