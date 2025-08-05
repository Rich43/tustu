package javax.swing.text.html;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Shape;
import java.awt.Toolkit;
import java.io.Serializable;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.ViewFactory;
import javax.swing.text.html.HTML;

/* loaded from: rt.jar:javax/swing/text/html/HiddenTagView.class */
class HiddenTagView extends EditableView implements DocumentListener {
    float yAlign;
    boolean isSettingAttributes;
    static final int circleR = 3;
    static final int circleD = 6;
    static final int tagSize = 6;
    static final int padding = 3;
    static final Color UnknownTagBorderColor = Color.black;
    static final Border StartBorder = new StartTagBorder();
    static final Border EndBorder = new EndTagBorder();

    HiddenTagView(Element element) {
        super(element);
        this.yAlign = 1.0f;
    }

    @Override // javax.swing.text.ComponentView
    protected Component createComponent() {
        Font font;
        JTextField jTextField = new JTextField(getElement().getName());
        Document document = getDocument();
        if (document instanceof StyledDocument) {
            font = ((StyledDocument) document).getFont(getAttributes());
            jTextField.setFont(font);
        } else {
            font = jTextField.getFont();
        }
        jTextField.getDocument().addDocumentListener(this);
        updateYAlign(font);
        JPanel jPanel = new JPanel(new BorderLayout());
        jPanel.setBackground(null);
        if (isEndTag()) {
            jPanel.setBorder(EndBorder);
        } else {
            jPanel.setBorder(StartBorder);
        }
        jPanel.add(jTextField);
        return jPanel;
    }

    @Override // javax.swing.text.ComponentView, javax.swing.text.View
    public float getAlignment(int i2) {
        if (i2 == 1) {
            return this.yAlign;
        }
        return 0.5f;
    }

    @Override // javax.swing.text.html.EditableView, javax.swing.text.ComponentView, javax.swing.text.View
    public float getMinimumSpan(int i2) {
        if (i2 == 0 && isVisible()) {
            return Math.max(30.0f, super.getPreferredSpan(i2));
        }
        return super.getMinimumSpan(i2);
    }

    @Override // javax.swing.text.html.EditableView, javax.swing.text.ComponentView, javax.swing.text.View
    public float getPreferredSpan(int i2) {
        if (i2 == 0 && isVisible()) {
            return Math.max(30.0f, super.getPreferredSpan(i2));
        }
        return super.getPreferredSpan(i2);
    }

    @Override // javax.swing.text.html.EditableView, javax.swing.text.ComponentView, javax.swing.text.View
    public float getMaximumSpan(int i2) {
        if (i2 == 0 && isVisible()) {
            return Math.max(30.0f, super.getMaximumSpan(i2));
        }
        return super.getMaximumSpan(i2);
    }

    @Override // javax.swing.event.DocumentListener
    public void insertUpdate(DocumentEvent documentEvent) {
        updateModelFromText();
    }

    @Override // javax.swing.event.DocumentListener
    public void removeUpdate(DocumentEvent documentEvent) {
        updateModelFromText();
    }

    @Override // javax.swing.event.DocumentListener
    public void changedUpdate(DocumentEvent documentEvent) {
        updateModelFromText();
    }

    @Override // javax.swing.text.View
    public void changedUpdate(DocumentEvent documentEvent, Shape shape, ViewFactory viewFactory) {
        if (!this.isSettingAttributes) {
            setTextFromModel();
        }
    }

    void updateYAlign(Font font) {
        Container container = getContainer();
        float height = (container != null ? container.getFontMetrics(font) : Toolkit.getDefaultToolkit().getFontMetrics(font)).getHeight();
        this.yAlign = height > 0.0f ? (height - r7.getDescent()) / height : 0.0f;
    }

    void resetBorder() {
        Component component = getComponent();
        if (component != null) {
            if (isEndTag()) {
                ((JPanel) component).setBorder(EndBorder);
            } else {
                ((JPanel) component).setBorder(StartBorder);
            }
        }
    }

    void setTextFromModel() {
        if (SwingUtilities.isEventDispatchThread()) {
            _setTextFromModel();
        } else {
            SwingUtilities.invokeLater(new Runnable() { // from class: javax.swing.text.html.HiddenTagView.1
                @Override // java.lang.Runnable
                public void run() {
                    HiddenTagView.this._setTextFromModel();
                }
            });
        }
    }

    void _setTextFromModel() {
        Document document = getDocument();
        try {
            this.isSettingAttributes = true;
            if (document instanceof AbstractDocument) {
                ((AbstractDocument) document).readLock();
            }
            JTextComponent textComponent = getTextComponent();
            if (textComponent != null) {
                textComponent.setText(getRepresentedText());
                resetBorder();
                Container container = getContainer();
                if (container != null) {
                    preferenceChanged(this, true, true);
                    container.repaint();
                }
            }
        } finally {
            this.isSettingAttributes = false;
            if (document instanceof AbstractDocument) {
                ((AbstractDocument) document).readUnlock();
            }
        }
    }

    void updateModelFromText() {
        if (!this.isSettingAttributes) {
            if (SwingUtilities.isEventDispatchThread()) {
                _updateModelFromText();
            } else {
                SwingUtilities.invokeLater(new Runnable() { // from class: javax.swing.text.html.HiddenTagView.2
                    @Override // java.lang.Runnable
                    public void run() {
                        HiddenTagView.this._updateModelFromText();
                    }
                });
            }
        }
    }

    void _updateModelFromText() {
        Document document = getDocument();
        if ((getElement().getAttributes().getAttribute(StyleConstants.NameAttribute) instanceof HTML.UnknownTag) && (document instanceof StyledDocument)) {
            SimpleAttributeSet simpleAttributeSet = new SimpleAttributeSet();
            JTextComponent textComponent = getTextComponent();
            if (textComponent != null) {
                String text = textComponent.getText();
                this.isSettingAttributes = true;
                try {
                    simpleAttributeSet.addAttribute(StyleConstants.NameAttribute, new HTML.UnknownTag(text));
                    ((StyledDocument) document).setCharacterAttributes(getStartOffset(), getEndOffset() - getStartOffset(), simpleAttributeSet, false);
                    this.isSettingAttributes = false;
                } catch (Throwable th) {
                    this.isSettingAttributes = false;
                    throw th;
                }
            }
        }
    }

    JTextComponent getTextComponent() {
        Component component = getComponent();
        if (component == null) {
            return null;
        }
        return (JTextComponent) ((Container) component).getComponent(0);
    }

    String getRepresentedText() {
        String name = getElement().getName();
        return name == null ? "" : name;
    }

    boolean isEndTag() {
        Object attribute;
        AttributeSet attributes = getElement().getAttributes();
        if (attributes != null && (attribute = attributes.getAttribute(HTML.Attribute.ENDTAG)) != null && (attribute instanceof String) && ((String) attribute).equals("true")) {
            return true;
        }
        return false;
    }

    /* loaded from: rt.jar:javax/swing/text/html/HiddenTagView$StartTagBorder.class */
    static class StartTagBorder implements Border, Serializable {
        StartTagBorder() {
        }

        @Override // javax.swing.border.Border
        public void paintBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
            graphics.setColor(HiddenTagView.UnknownTagBorderColor);
            int i6 = i2 + 3;
            int i7 = i4 - 6;
            graphics.drawLine(i6, i3 + 3, i6, (i3 + i5) - 3);
            graphics.drawArc(i6, ((i3 + i5) - 6) - 1, 6, 6, 180, 90);
            graphics.drawArc(i6, i3, 6, 6, 90, 90);
            graphics.drawLine(i6 + 3, i3, (i6 + i7) - 6, i3);
            graphics.drawLine(i6 + 3, (i3 + i5) - 1, (i6 + i7) - 6, (i3 + i5) - 1);
            graphics.drawLine((i6 + i7) - 6, i3, (i6 + i7) - 1, i3 + (i5 / 2));
            graphics.drawLine((i6 + i7) - 6, i3 + i5, (i6 + i7) - 1, i3 + (i5 / 2));
        }

        @Override // javax.swing.border.Border
        public Insets getBorderInsets(Component component) {
            return new Insets(2, 5, 2, 11);
        }

        @Override // javax.swing.border.Border
        public boolean isBorderOpaque() {
            return false;
        }
    }

    /* loaded from: rt.jar:javax/swing/text/html/HiddenTagView$EndTagBorder.class */
    static class EndTagBorder implements Border, Serializable {
        EndTagBorder() {
        }

        @Override // javax.swing.border.Border
        public void paintBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
            graphics.setColor(HiddenTagView.UnknownTagBorderColor);
            int i6 = i2 + 3;
            int i7 = i4 - 6;
            graphics.drawLine((i6 + i7) - 1, i3 + 3, (i6 + i7) - 1, (i3 + i5) - 3);
            graphics.drawArc(((i6 + i7) - 6) - 1, ((i3 + i5) - 6) - 1, 6, 6, 270, 90);
            graphics.drawArc(((i6 + i7) - 6) - 1, i3, 6, 6, 0, 90);
            graphics.drawLine(i6 + 6, i3, (i6 + i7) - 3, i3);
            graphics.drawLine(i6 + 6, (i3 + i5) - 1, (i6 + i7) - 3, (i3 + i5) - 1);
            graphics.drawLine(i6 + 6, i3, i6, i3 + (i5 / 2));
            graphics.drawLine(i6 + 6, i3 + i5, i6, i3 + (i5 / 2));
        }

        @Override // javax.swing.border.Border
        public Insets getBorderInsets(Component component) {
            return new Insets(2, 11, 2, 5);
        }

        @Override // javax.swing.border.Border
        public boolean isBorderOpaque() {
            return false;
        }
    }
}
