package ao;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.text.JTextComponent;

/* loaded from: TunerStudioMS.jar:ao/hB.class */
final class hB extends AbstractAction {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ Object[] f6001a;

    hB(Object[] objArr) {
        this.f6001a = objArr;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        JTextComponent jTextComponent = (JTextComponent) actionEvent.getSource();
        String str = (String) JOptionPane.showInputDialog(jTextComponent, null, "Insert Field Name", -1, null, this.f6001a, this.f6001a[0]);
        if (str == null) {
            return;
        }
        String text = jTextComponent.getText();
        StringBuilder sb = new StringBuilder();
        if (jTextComponent.getSelectionEnd() <= jTextComponent.getSelectionStart()) {
            String str2 = "[" + str + "]";
            int caretPosition = jTextComponent.getCaretPosition();
            sb.append(text);
            sb.insert(caretPosition, str2);
            jTextComponent.setText(sb.toString());
            jTextComponent.setCaretPosition(caretPosition + str2.length());
            return;
        }
        String strSubstring = text.substring(0, jTextComponent.getSelectionStart());
        String strSubstring2 = text.substring(jTextComponent.getSelectionEnd(), text.length());
        if (!strSubstring.endsWith("[") && !strSubstring2.startsWith("]")) {
            str = "[" + str + "]";
        }
        int selectionStart = jTextComponent.getSelectionStart();
        sb.append(strSubstring);
        sb.append(str);
        sb.append(strSubstring2);
        jTextComponent.setText(sb.toString());
        jTextComponent.setCaretPosition(selectionStart);
        jTextComponent.select(selectionStart + str.length(), selectionStart + str.length());
    }
}
