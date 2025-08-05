package javax.swing.text.html;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyledDocument;
import javax.swing.text.html.HTML;

/* loaded from: rt.jar:javax/swing/text/html/CommentView.class */
class CommentView extends HiddenTagView {
    static final Border CBorder = new CommentBorder();
    static final int commentPadding = 3;
    static final int commentPaddingD = 9;

    CommentView(Element element) {
        super(element);
    }

    @Override // javax.swing.text.html.HiddenTagView, javax.swing.text.ComponentView
    protected Component createComponent() {
        Font font;
        Container container = getContainer();
        if (container != null && !((JTextComponent) container).isEditable()) {
            return null;
        }
        JTextArea jTextArea = new JTextArea(getRepresentedText());
        Document document = getDocument();
        if (document instanceof StyledDocument) {
            font = ((StyledDocument) document).getFont(getAttributes());
            jTextArea.setFont(font);
        } else {
            font = jTextArea.getFont();
        }
        updateYAlign(font);
        jTextArea.setBorder(CBorder);
        jTextArea.getDocument().addDocumentListener(this);
        jTextArea.setFocusable(isVisible());
        return jTextArea;
    }

    @Override // javax.swing.text.html.HiddenTagView
    void resetBorder() {
    }

    @Override // javax.swing.text.html.HiddenTagView
    void _updateModelFromText() {
        JTextComponent textComponent = getTextComponent();
        Document document = getDocument();
        if (textComponent != null && document != null) {
            String text = textComponent.getText();
            SimpleAttributeSet simpleAttributeSet = new SimpleAttributeSet();
            this.isSettingAttributes = true;
            try {
                simpleAttributeSet.addAttribute(HTML.Attribute.COMMENT, text);
                ((StyledDocument) document).setCharacterAttributes(getStartOffset(), getEndOffset() - getStartOffset(), simpleAttributeSet, false);
                this.isSettingAttributes = false;
            } catch (Throwable th) {
                this.isSettingAttributes = false;
                throw th;
            }
        }
    }

    @Override // javax.swing.text.html.HiddenTagView
    JTextComponent getTextComponent() {
        return (JTextComponent) getComponent();
    }

    @Override // javax.swing.text.html.HiddenTagView
    String getRepresentedText() {
        AttributeSet attributes = getElement().getAttributes();
        if (attributes != null) {
            Object attribute = attributes.getAttribute(HTML.Attribute.COMMENT);
            if (attribute instanceof String) {
                return (String) attribute;
            }
            return "";
        }
        return "";
    }

    /* loaded from: rt.jar:javax/swing/text/html/CommentView$CommentBorder.class */
    static class CommentBorder extends LineBorder {
        CommentBorder() {
            super(Color.black, 1);
        }

        @Override // javax.swing.border.LineBorder, javax.swing.border.AbstractBorder, javax.swing.border.Border
        public void paintBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
            super.paintBorder(component, graphics, i2 + 3, i3, i4 - 9, i5);
        }

        @Override // javax.swing.border.LineBorder, javax.swing.border.AbstractBorder
        public Insets getBorderInsets(Component component, Insets insets) {
            Insets borderInsets = super.getBorderInsets(component, insets);
            borderInsets.left += 3;
            borderInsets.right += 3;
            return borderInsets;
        }

        @Override // javax.swing.border.LineBorder, javax.swing.border.AbstractBorder, javax.swing.border.Border
        public boolean isBorderOpaque() {
            return false;
        }
    }
}
