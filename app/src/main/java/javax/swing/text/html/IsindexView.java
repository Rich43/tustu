package javax.swing.text.html;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.text.AttributeSet;
import javax.swing.text.ComponentView;
import javax.swing.text.Element;
import javax.swing.text.html.HTML;

/* loaded from: rt.jar:javax/swing/text/html/IsindexView.class */
class IsindexView extends ComponentView implements ActionListener {
    JTextField textField;

    public IsindexView(Element element) {
        super(element);
    }

    @Override // javax.swing.text.ComponentView
    public Component createComponent() {
        AttributeSet attributes = getElement().getAttributes();
        JPanel jPanel = new JPanel(new BorderLayout());
        jPanel.setBackground(null);
        String string = (String) attributes.getAttribute(HTML.Attribute.PROMPT);
        if (string == null) {
            string = UIManager.getString("IsindexView.prompt");
        }
        JLabel jLabel = new JLabel(string);
        this.textField = new JTextField();
        this.textField.addActionListener(this);
        jPanel.add(jLabel, "West");
        jPanel.add(this.textField, BorderLayout.CENTER);
        jPanel.setAlignmentY(1.0f);
        jPanel.setOpaque(false);
        return jPanel;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        String text = this.textField.getText();
        if (text != null) {
            text = URLEncoder.encode(text);
        }
        AttributeSet attributes = getElement().getAttributes();
        HTMLDocument hTMLDocument = (HTMLDocument) getElement().getDocument();
        String string = (String) attributes.getAttribute(HTML.Attribute.ACTION);
        if (string == null) {
            string = hTMLDocument.getBase().toString();
        }
        try {
            ((JEditorPane) getContainer()).setPage(new URL(string + "?" + text));
        } catch (MalformedURLException e2) {
        } catch (IOException e3) {
        }
    }
}
