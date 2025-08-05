package ao;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.text.JTextComponent;

/* loaded from: TunerStudioMS.jar:ao/hC.class */
final class hC extends AbstractAction {
    hC() {
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) throws HeadlessException {
        JOptionPane.showMessageDialog((JTextComponent) actionEvent.getSource(), "Please open log file in order to insert field names");
    }
}
