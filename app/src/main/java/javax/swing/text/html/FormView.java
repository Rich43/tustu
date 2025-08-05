package javax.swing.text.html;

import com.sun.media.jfxmedia.MetadataParser;
import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.BitSet;
import javafx.fxml.FXMLLoader;
import javax.swing.AbstractListModel;
import javax.swing.Box;
import javax.swing.ButtonModel;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultButtonModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.ListDataListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.ComponentView;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.ElementIterator;
import javax.swing.text.PlainDocument;
import javax.swing.text.StyleConstants;
import javax.swing.text.html.FormSubmitEvent;
import javax.swing.text.html.FrameView;
import javax.swing.text.html.HTML;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: rt.jar:javax/swing/text/html/FormView.class */
public class FormView extends ComponentView implements ActionListener {

    @Deprecated
    public static final String SUBMIT = new String("Submit Query");

    @Deprecated
    public static final String RESET = new String("Reset");
    static final String PostDataProperty = "javax.swing.JEditorPane.postdata";
    private short maxIsPreferred;

    public FormView(Element element) {
        super(element);
    }

    @Override // javax.swing.text.ComponentView
    protected Component createComponent() {
        AttributeSet attributes = getElement().getAttributes();
        HTML.Tag tag = (HTML.Tag) attributes.getAttribute(StyleConstants.NameAttribute);
        JComponent jScrollPane = null;
        Object attribute = attributes.getAttribute(StyleConstants.ModelAttribute);
        removeStaleListenerForModel(attribute);
        if (tag == HTML.Tag.INPUT) {
            jScrollPane = createInputComponent(attributes, attribute);
        } else if (tag == HTML.Tag.SELECT) {
            if (attribute instanceof OptionListModel) {
                JList jList = new JList((ListModel) attribute);
                jList.setVisibleRowCount(HTML.getIntegerAttributeValue(attributes, HTML.Attribute.SIZE, 1));
                jList.setSelectionModel((ListSelectionModel) attribute);
                jScrollPane = new JScrollPane(jList);
            } else {
                jScrollPane = new JComboBox((ComboBoxModel) attribute);
                this.maxIsPreferred = (short) 3;
            }
        } else if (tag == HTML.Tag.TEXTAREA) {
            JTextArea jTextArea = new JTextArea((Document) attribute);
            jTextArea.setRows(HTML.getIntegerAttributeValue(attributes, HTML.Attribute.ROWS, 1));
            int integerAttributeValue = HTML.getIntegerAttributeValue(attributes, HTML.Attribute.COLS, 20);
            this.maxIsPreferred = (short) 3;
            jTextArea.setColumns(integerAttributeValue);
            jScrollPane = new JScrollPane(jTextArea, 22, 32);
        }
        if (jScrollPane != null) {
            jScrollPane.setAlignmentY(1.0f);
        }
        return jScrollPane;
    }

    private JComponent createInputComponent(AttributeSet attributeSet, Object obj) {
        JTextField jTextField;
        JButton jButton;
        JComponent jRadioButton = null;
        String str = (String) attributeSet.getAttribute(HTML.Attribute.TYPE);
        if (str.equals("submit") || str.equals(Constants.RESET)) {
            String string = (String) attributeSet.getAttribute(HTML.Attribute.VALUE);
            if (string == null) {
                if (str.equals("submit")) {
                    string = UIManager.getString("FormView.submitButtonText");
                } else {
                    string = UIManager.getString("FormView.resetButtonText");
                }
            }
            JButton jButton2 = new JButton(string);
            if (obj != null) {
                jButton2.setModel((ButtonModel) obj);
                jButton2.addActionListener(this);
            }
            jRadioButton = jButton2;
            this.maxIsPreferred = (short) 3;
        } else if (str.equals(MetadataParser.IMAGE_TAG_NAME)) {
            String str2 = (String) attributeSet.getAttribute(HTML.Attribute.SRC);
            try {
                jButton = new JButton(new ImageIcon(new URL(((HTMLDocument) getElement().getDocument()).getBase(), str2)));
            } catch (MalformedURLException e2) {
                jButton = new JButton(str2);
            }
            if (obj != null) {
                jButton.setModel((ButtonModel) obj);
                jButton.addMouseListener(new MouseEventListener());
            }
            jRadioButton = jButton;
            this.maxIsPreferred = (short) 3;
        } else if (str.equals("checkbox")) {
            jRadioButton = new JCheckBox();
            if (obj != null) {
                ((JCheckBox) jRadioButton).setModel((JToggleButton.ToggleButtonModel) obj);
            }
            this.maxIsPreferred = (short) 3;
        } else if (str.equals("radio")) {
            jRadioButton = new JRadioButton();
            if (obj != null) {
                ((JRadioButton) jRadioButton).setModel((JToggleButton.ToggleButtonModel) obj);
            }
            this.maxIsPreferred = (short) 3;
        } else if (str.equals("text")) {
            int integerAttributeValue = HTML.getIntegerAttributeValue(attributeSet, HTML.Attribute.SIZE, -1);
            if (integerAttributeValue > 0) {
                jTextField = new JTextField();
                jTextField.setColumns(integerAttributeValue);
            } else {
                jTextField = new JTextField();
                jTextField.setColumns(20);
            }
            jRadioButton = jTextField;
            if (obj != null) {
                jTextField.setDocument((Document) obj);
            }
            jTextField.addActionListener(this);
            this.maxIsPreferred = (short) 3;
        } else if (str.equals("password")) {
            JPasswordField jPasswordField = new JPasswordField();
            jRadioButton = jPasswordField;
            if (obj != null) {
                jPasswordField.setDocument((Document) obj);
            }
            int integerAttributeValue2 = HTML.getIntegerAttributeValue(attributeSet, HTML.Attribute.SIZE, -1);
            jPasswordField.setColumns(integerAttributeValue2 > 0 ? integerAttributeValue2 : 20);
            jPasswordField.addActionListener(this);
            this.maxIsPreferred = (short) 3;
        } else if (str.equals(DeploymentDescriptorParser.ATTR_FILE)) {
            JTextField jTextField2 = new JTextField();
            if (obj != null) {
                jTextField2.setDocument((Document) obj);
            }
            int integerAttributeValue3 = HTML.getIntegerAttributeValue(attributeSet, HTML.Attribute.SIZE, -1);
            jTextField2.setColumns(integerAttributeValue3 > 0 ? integerAttributeValue3 : 20);
            JButton jButton3 = new JButton(UIManager.getString("FormView.browseFileButtonText"));
            Box boxCreateHorizontalBox = Box.createHorizontalBox();
            boxCreateHorizontalBox.add(jTextField2);
            boxCreateHorizontalBox.add(Box.createHorizontalStrut(5));
            boxCreateHorizontalBox.add(jButton3);
            jButton3.addActionListener(new BrowseFileAction(attributeSet, (Document) obj));
            jRadioButton = boxCreateHorizontalBox;
            this.maxIsPreferred = (short) 3;
        }
        return jRadioButton;
    }

    private void removeStaleListenerForModel(Object obj) {
        if (!(obj instanceof DefaultButtonModel)) {
            if (obj instanceof AbstractListModel) {
                AbstractListModel abstractListModel = (AbstractListModel) obj;
                for (ListDataListener listDataListener : abstractListModel.getListDataListeners()) {
                    if ("javax.swing.plaf.basic.BasicListUI$Handler".equals(listDataListener.getClass().getName()) || "javax.swing.plaf.basic.BasicComboBoxUI$Handler".equals(listDataListener.getClass().getName())) {
                        abstractListModel.removeListDataListener(listDataListener);
                    }
                }
                return;
            }
            if (obj instanceof AbstractDocument) {
                AbstractDocument abstractDocument = (AbstractDocument) obj;
                for (DocumentListener documentListener : abstractDocument.getDocumentListeners()) {
                    if ("javax.swing.plaf.basic.BasicTextUI$UpdateHandler".equals(documentListener.getClass().getName()) || "javax.swing.text.DefaultCaret$Handler".equals(documentListener.getClass().getName())) {
                        abstractDocument.removeDocumentListener(documentListener);
                    }
                }
                return;
            }
            return;
        }
        DefaultButtonModel defaultButtonModel = (DefaultButtonModel) obj;
        for (ActionListener actionListener : defaultButtonModel.getActionListeners()) {
            if ("javax.swing.AbstractButton$Handler".equals(actionListener.getClass().getName())) {
                defaultButtonModel.removeActionListener(actionListener);
            }
        }
        for (ChangeListener changeListener : defaultButtonModel.getChangeListeners()) {
            if ("javax.swing.AbstractButton$Handler".equals(changeListener.getClass().getName())) {
                defaultButtonModel.removeChangeListener(changeListener);
            }
        }
        for (ItemListener itemListener : defaultButtonModel.getItemListeners()) {
            if ("javax.swing.AbstractButton$Handler".equals(itemListener.getClass().getName())) {
                defaultButtonModel.removeItemListener(itemListener);
            }
        }
    }

    @Override // javax.swing.text.ComponentView, javax.swing.text.View
    public float getMaximumSpan(int i2) {
        switch (i2) {
            case 0:
                if ((this.maxIsPreferred & 1) == 1) {
                    super.getMaximumSpan(i2);
                    return getPreferredSpan(i2);
                }
                return super.getMaximumSpan(i2);
            case 1:
                if ((this.maxIsPreferred & 2) == 2) {
                    super.getMaximumSpan(i2);
                    return getPreferredSpan(i2);
                }
                return super.getMaximumSpan(i2);
            default:
                return super.getMaximumSpan(i2);
        }
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        Element element = getElement();
        StringBuilder sb = new StringBuilder();
        String str = (String) element.getAttributes().getAttribute(HTML.Attribute.TYPE);
        if (str.equals("submit")) {
            getFormData(sb);
            submitData(sb.toString());
            return;
        }
        if (str.equals(Constants.RESET)) {
            resetForm();
            return;
        }
        if (str.equals("text") || str.equals("password")) {
            if (isLastTextOrPasswordField()) {
                getFormData(sb);
                submitData(sb.toString());
            } else {
                getComponent().transferFocus();
            }
        }
    }

    protected void submitData(String str) {
        URL url;
        Element formElement = getFormElement();
        AttributeSet attributes = formElement.getAttributes();
        HTMLDocument hTMLDocument = (HTMLDocument) formElement.getDocument();
        URL base = hTMLDocument.getBase();
        String str2 = (String) attributes.getAttribute(HTML.Attribute.TARGET);
        if (str2 == null) {
            str2 = "_self";
        }
        String str3 = (String) attributes.getAttribute(HTML.Attribute.METHOD);
        if (str3 == null) {
            str3 = "GET";
        }
        boolean zEquals = str3.toLowerCase().equals("post");
        if (zEquals) {
            storePostData(hTMLDocument, str2, str);
        }
        String str4 = (String) attributes.getAttribute(HTML.Attribute.ACTION);
        try {
            url = str4 == null ? new URL(base.getProtocol(), base.getHost(), base.getPort(), base.getFile()) : new URL(base, str4);
            if (!zEquals) {
                url = new URL(((Object) url) + "?" + str.toString());
            }
        } catch (MalformedURLException e2) {
            url = null;
        }
        final JEditorPane jEditorPane = (JEditorPane) getContainer();
        FormSubmitEvent formSubmitEvent = null;
        if (!((HTMLEditorKit) jEditorPane.getEditorKit()).isAutoFormSubmission() || hTMLDocument.isFrameDocument()) {
            formSubmitEvent = new FormSubmitEvent(this, HyperlinkEvent.EventType.ACTIVATED, url, formElement, str2, zEquals ? FormSubmitEvent.MethodType.POST : FormSubmitEvent.MethodType.GET, str);
        }
        final FormSubmitEvent formSubmitEvent2 = formSubmitEvent;
        final URL url2 = url;
        SwingUtilities.invokeLater(new Runnable() { // from class: javax.swing.text.html.FormView.1
            @Override // java.lang.Runnable
            public void run() {
                if (formSubmitEvent2 != null) {
                    jEditorPane.fireHyperlinkUpdate(formSubmitEvent2);
                    return;
                }
                try {
                    jEditorPane.setPage(url2);
                } catch (IOException e3) {
                    UIManager.getLookAndFeel().provideErrorFeedback(jEditorPane);
                }
            }
        });
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v14, types: [javax.swing.text.Document] */
    private void storePostData(HTMLDocument hTMLDocument, String str, String str2) {
        JEditorPane outermostJEditorPane;
        HTMLDocument document = hTMLDocument;
        String str3 = PostDataProperty;
        if (hTMLDocument.isFrameDocument() && (outermostJEditorPane = ((FrameView.FrameEditorPane) getContainer()).getFrameView().getOutermostJEditorPane()) != null) {
            document = outermostJEditorPane.getDocument();
            str3 = str3 + "." + str;
        }
        document.putProperty(str3, str2);
    }

    /* loaded from: rt.jar:javax/swing/text/html/FormView$MouseEventListener.class */
    protected class MouseEventListener extends MouseAdapter {
        protected MouseEventListener() {
        }

        @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
        public void mouseReleased(MouseEvent mouseEvent) {
            FormView.this.imageSubmit(FormView.this.getImageData(mouseEvent.getPoint()));
        }
    }

    protected void imageSubmit(String str) {
        StringBuilder sb = new StringBuilder();
        getFormData(sb);
        if (sb.length() > 0) {
            sb.append('&');
        }
        sb.append(str);
        submitData(sb.toString());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getImageData(Point point) {
        String str;
        String str2 = point.f12370x + CallSiteDescriptor.TOKEN_DELIMITER + point.f12371y;
        int iIndexOf = str2.indexOf(58);
        String strSubstring = str2.substring(0, iIndexOf);
        String strSubstring2 = str2.substring(iIndexOf + 1);
        String str3 = (String) getElement().getAttributes().getAttribute(HTML.Attribute.NAME);
        if (str3 == null || str3.equals("")) {
            str = "x=" + strSubstring + "&y=" + strSubstring2;
        } else {
            String strEncode = URLEncoder.encode(str3);
            str = strEncode + ".x=" + strSubstring + "&" + strEncode + ".y=" + strSubstring2;
        }
        return str;
    }

    private Element getFormElement() {
        Element element = getElement();
        while (true) {
            Element element2 = element;
            if (element2 != null) {
                if (element2.getAttributes().getAttribute(StyleConstants.NameAttribute) == HTML.Tag.FORM) {
                    return element2;
                }
                element = element2.getParentElement();
            } else {
                return null;
            }
        }
    }

    private void getFormData(StringBuilder sb) {
        String str;
        Element formElement = getFormElement();
        if (formElement != null) {
            ElementIterator elementIterator = new ElementIterator(formElement);
            while (true) {
                Element next = elementIterator.next();
                if (next != null) {
                    if (isControl(next) && ((str = (String) next.getAttributes().getAttribute(HTML.Attribute.TYPE)) == null || !str.equals("submit") || next == getElement())) {
                        if (str == null || !str.equals(MetadataParser.IMAGE_TAG_NAME)) {
                            loadElementDataIntoBuffer(next, sb);
                        }
                    }
                } else {
                    return;
                }
            }
        }
    }

    private void loadElementDataIntoBuffer(Element element, StringBuilder sb) {
        AttributeSet attributes = element.getAttributes();
        String str = (String) attributes.getAttribute(HTML.Attribute.NAME);
        if (str == null) {
            return;
        }
        String textAreaData = null;
        HTML.Tag tag = (HTML.Tag) element.getAttributes().getAttribute(StyleConstants.NameAttribute);
        if (tag == HTML.Tag.INPUT) {
            textAreaData = getInputElementData(attributes);
        } else if (tag == HTML.Tag.TEXTAREA) {
            textAreaData = getTextAreaData(attributes);
        } else if (tag == HTML.Tag.SELECT) {
            loadSelectData(attributes, sb);
        }
        if (str != null && textAreaData != null) {
            appendBuffer(sb, str, textAreaData);
        }
    }

    private String getInputElementData(AttributeSet attributeSet) {
        String text;
        Object attribute = attributeSet.getAttribute(StyleConstants.ModelAttribute);
        String str = (String) attributeSet.getAttribute(HTML.Attribute.TYPE);
        String text2 = null;
        if (str.equals("text") || str.equals("password")) {
            Document document = (Document) attribute;
            try {
                text2 = document.getText(0, document.getLength());
            } catch (BadLocationException e2) {
                text2 = null;
            }
        } else if (str.equals("submit") || str.equals("hidden")) {
            text2 = (String) attributeSet.getAttribute(HTML.Attribute.VALUE);
            if (text2 == null) {
                text2 = "";
            }
        } else if (str.equals("radio") || str.equals("checkbox")) {
            if (((ButtonModel) attribute).isSelected()) {
                text2 = (String) attributeSet.getAttribute(HTML.Attribute.VALUE);
                if (text2 == null) {
                    text2 = FXMLLoader.EVENT_HANDLER_PREFIX;
                }
            }
        } else if (str.equals(DeploymentDescriptorParser.ATTR_FILE)) {
            Document document2 = (Document) attribute;
            try {
                text = document2.getText(0, document2.getLength());
            } catch (BadLocationException e3) {
                text = null;
            }
            if (text != null && text.length() > 0) {
                text2 = text;
            }
        }
        return text2;
    }

    private String getTextAreaData(AttributeSet attributeSet) {
        Document document = (Document) attributeSet.getAttribute(StyleConstants.ModelAttribute);
        try {
            return document.getText(0, document.getLength());
        } catch (BadLocationException e2) {
            return null;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void loadSelectData(AttributeSet attributeSet, StringBuilder sb) {
        Option option;
        String str = (String) attributeSet.getAttribute(HTML.Attribute.NAME);
        if (str == null) {
            return;
        }
        Object attribute = attributeSet.getAttribute(StyleConstants.ModelAttribute);
        if (attribute instanceof OptionListModel) {
            OptionListModel optionListModel = (OptionListModel) attribute;
            for (int i2 = 0; i2 < optionListModel.getSize(); i2++) {
                if (optionListModel.isSelectedIndex(i2)) {
                    appendBuffer(sb, str, ((Option) optionListModel.getElementAt(i2)).getValue());
                }
            }
            return;
        }
        if ((attribute instanceof ComboBoxModel) && (option = (Option) ((ComboBoxModel) attribute).getSelectedItem()) != null) {
            appendBuffer(sb, str, option.getValue());
        }
    }

    private void appendBuffer(StringBuilder sb, String str, String str2) {
        if (sb.length() > 0) {
            sb.append('&');
        }
        sb.append(URLEncoder.encode(str));
        sb.append('=');
        sb.append(URLEncoder.encode(str2));
    }

    private boolean isControl(Element element) {
        return element.isLeaf();
    }

    boolean isLastTextOrPasswordField() {
        Element formElement = getFormElement();
        Element element = getElement();
        if (formElement != null) {
            ElementIterator elementIterator = new ElementIterator(formElement);
            boolean z2 = false;
            while (true) {
                Element next = elementIterator.next();
                if (next != null) {
                    if (next == element) {
                        z2 = true;
                    } else if (z2 && isControl(next)) {
                        AttributeSet attributes = next.getAttributes();
                        if (HTMLDocument.matchNameAttribute(attributes, HTML.Tag.INPUT)) {
                            String str = (String) attributes.getAttribute(HTML.Attribute.TYPE);
                            if ("text".equals(str) || "password".equals(str)) {
                                return false;
                            }
                        } else {
                            continue;
                        }
                    }
                } else {
                    return true;
                }
            }
        } else {
            return true;
        }
    }

    void resetForm() {
        String str;
        Element formElement = getFormElement();
        if (formElement != null) {
            ElementIterator elementIterator = new ElementIterator(formElement);
            while (true) {
                Element next = elementIterator.next();
                if (next != null) {
                    if (isControl(next)) {
                        AttributeSet attributes = next.getAttributes();
                        Object attribute = attributes.getAttribute(StyleConstants.ModelAttribute);
                        if (attribute instanceof TextAreaDocument) {
                            ((TextAreaDocument) attribute).reset();
                        } else if (attribute instanceof PlainDocument) {
                            try {
                                PlainDocument plainDocument = (PlainDocument) attribute;
                                plainDocument.remove(0, plainDocument.getLength());
                                if (HTMLDocument.matchNameAttribute(attributes, HTML.Tag.INPUT) && (str = (String) attributes.getAttribute(HTML.Attribute.VALUE)) != null) {
                                    plainDocument.insertString(0, str, null);
                                }
                            } catch (BadLocationException e2) {
                            }
                        } else if (attribute instanceof OptionListModel) {
                            OptionListModel optionListModel = (OptionListModel) attribute;
                            int size = optionListModel.getSize();
                            for (int i2 = 0; i2 < size; i2++) {
                                optionListModel.removeIndexInterval(i2, i2);
                            }
                            BitSet initialSelection = optionListModel.getInitialSelection();
                            for (int i3 = 0; i3 < initialSelection.size(); i3++) {
                                if (initialSelection.get(i3)) {
                                    optionListModel.addSelectionInterval(i3, i3);
                                }
                            }
                        } else if (attribute instanceof OptionComboBoxModel) {
                            OptionComboBoxModel optionComboBoxModel = (OptionComboBoxModel) attribute;
                            Option initialSelection2 = optionComboBoxModel.getInitialSelection();
                            if (initialSelection2 != null) {
                                optionComboBoxModel.setSelectedItem(initialSelection2);
                            }
                        } else if (attribute instanceof JToggleButton.ToggleButtonModel) {
                            ((JToggleButton.ToggleButtonModel) attribute).setSelected(((String) attributes.getAttribute(HTML.Attribute.CHECKED)) != null);
                        }
                    }
                } else {
                    return;
                }
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/text/html/FormView$BrowseFileAction.class */
    private class BrowseFileAction implements ActionListener {
        private AttributeSet attrs;
        private Document model;

        BrowseFileAction(AttributeSet attributeSet, Document document) {
            this.attrs = attributeSet;
            this.model = document;
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            File selectedFile;
            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.setMultiSelectionEnabled(false);
            if (jFileChooser.showOpenDialog(FormView.this.getContainer()) == 0 && (selectedFile = jFileChooser.getSelectedFile()) != null) {
                try {
                    if (this.model.getLength() > 0) {
                        this.model.remove(0, this.model.getLength());
                    }
                    this.model.insertString(0, selectedFile.getPath(), null);
                } catch (BadLocationException e2) {
                }
            }
        }
    }
}
