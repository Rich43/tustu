package javax.swing;

import java.awt.Component;
import java.io.IOException;
import java.io.ObjectOutputStream;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;
import javax.swing.text.Element;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.StyledEditorKit;

/* loaded from: rt.jar:javax/swing/JTextPane.class */
public class JTextPane extends JEditorPane {
    private static final String uiClassID = "TextPaneUI";

    public JTextPane() {
        EditorKit editorKitCreateDefaultEditorKit = createDefaultEditorKit();
        String contentType = editorKitCreateDefaultEditorKit.getContentType();
        if (contentType != null && getEditorKitClassNameForContentType(contentType) == defaultEditorKitMap.get(contentType)) {
            setEditorKitForContentType(contentType, editorKitCreateDefaultEditorKit);
        }
        setEditorKit(editorKitCreateDefaultEditorKit);
    }

    public JTextPane(StyledDocument styledDocument) {
        this();
        setStyledDocument(styledDocument);
    }

    @Override // javax.swing.JEditorPane, javax.swing.JComponent
    public String getUIClassID() {
        return uiClassID;
    }

    @Override // javax.swing.text.JTextComponent
    public void setDocument(Document document) {
        if (document instanceof StyledDocument) {
            super.setDocument(document);
            return;
        }
        throw new IllegalArgumentException("Model must be StyledDocument");
    }

    public void setStyledDocument(StyledDocument styledDocument) {
        super.setDocument(styledDocument);
    }

    public StyledDocument getStyledDocument() {
        return (StyledDocument) getDocument();
    }

    @Override // javax.swing.JEditorPane, javax.swing.text.JTextComponent
    public void replaceSelection(String str) {
        replaceSelection(str, true);
    }

    private void replaceSelection(String str, boolean z2) {
        if (z2 && !isEditable()) {
            UIManager.getLookAndFeel().provideErrorFeedback(this);
            return;
        }
        Document styledDocument = getStyledDocument();
        if (styledDocument != null) {
            try {
                Caret caret = getCaret();
                boolean zSaveComposedText = saveComposedText(caret.getDot());
                int iMin = Math.min(caret.getDot(), caret.getMark());
                int iMax = Math.max(caret.getDot(), caret.getMark());
                AttributeSet attributeSetCopyAttributes = getInputAttributes().copyAttributes();
                if (styledDocument instanceof AbstractDocument) {
                    ((AbstractDocument) styledDocument).replace(iMin, iMax - iMin, str, attributeSetCopyAttributes);
                } else {
                    if (iMin != iMax) {
                        styledDocument.remove(iMin, iMax - iMin);
                    }
                    if (str != null && str.length() > 0) {
                        styledDocument.insertString(iMin, str, attributeSetCopyAttributes);
                    }
                }
                if (zSaveComposedText) {
                    restoreComposedText();
                }
            } catch (BadLocationException e2) {
                UIManager.getLookAndFeel().provideErrorFeedback(this);
            }
        }
    }

    public void insertComponent(Component component) {
        MutableAttributeSet inputAttributes = getInputAttributes();
        inputAttributes.removeAttributes(inputAttributes);
        StyleConstants.setComponent(inputAttributes, component);
        replaceSelection(" ", false);
        inputAttributes.removeAttributes(inputAttributes);
    }

    public void insertIcon(Icon icon) {
        MutableAttributeSet inputAttributes = getInputAttributes();
        inputAttributes.removeAttributes(inputAttributes);
        StyleConstants.setIcon(inputAttributes, icon);
        replaceSelection(" ", false);
        inputAttributes.removeAttributes(inputAttributes);
    }

    public Style addStyle(String str, Style style) {
        return getStyledDocument().addStyle(str, style);
    }

    public void removeStyle(String str) {
        getStyledDocument().removeStyle(str);
    }

    public Style getStyle(String str) {
        return getStyledDocument().getStyle(str);
    }

    public void setLogicalStyle(Style style) {
        getStyledDocument().setLogicalStyle(getCaretPosition(), style);
    }

    public Style getLogicalStyle() {
        return getStyledDocument().getLogicalStyle(getCaretPosition());
    }

    public AttributeSet getCharacterAttributes() {
        Element characterElement = getStyledDocument().getCharacterElement(getCaretPosition());
        if (characterElement != null) {
            return characterElement.getAttributes();
        }
        return null;
    }

    public void setCharacterAttributes(AttributeSet attributeSet, boolean z2) {
        int selectionStart = getSelectionStart();
        int selectionEnd = getSelectionEnd();
        if (selectionStart != selectionEnd) {
            getStyledDocument().setCharacterAttributes(selectionStart, selectionEnd - selectionStart, attributeSet, z2);
            return;
        }
        MutableAttributeSet inputAttributes = getInputAttributes();
        if (z2) {
            inputAttributes.removeAttributes(inputAttributes);
        }
        inputAttributes.addAttributes(attributeSet);
    }

    public AttributeSet getParagraphAttributes() {
        Element paragraphElement = getStyledDocument().getParagraphElement(getCaretPosition());
        if (paragraphElement != null) {
            return paragraphElement.getAttributes();
        }
        return null;
    }

    public void setParagraphAttributes(AttributeSet attributeSet, boolean z2) {
        int selectionStart = getSelectionStart();
        getStyledDocument().setParagraphAttributes(selectionStart, getSelectionEnd() - selectionStart, attributeSet, z2);
    }

    public MutableAttributeSet getInputAttributes() {
        return getStyledEditorKit().getInputAttributes();
    }

    protected final StyledEditorKit getStyledEditorKit() {
        return (StyledEditorKit) getEditorKit();
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        if (getUIClassID().equals(uiClassID)) {
            byte writeObjCounter = (byte) (JComponent.getWriteObjCounter(this) - 1);
            JComponent.setWriteObjCounter(this, writeObjCounter);
            if (writeObjCounter == 0 && this.ui != null) {
                this.ui.installUI(this);
            }
        }
    }

    @Override // javax.swing.JEditorPane
    protected EditorKit createDefaultEditorKit() {
        return new StyledEditorKit();
    }

    @Override // javax.swing.JEditorPane
    public final void setEditorKit(EditorKit editorKit) {
        if (editorKit instanceof StyledEditorKit) {
            super.setEditorKit(editorKit);
            return;
        }
        throw new IllegalArgumentException("Must be StyledEditorKit");
    }

    @Override // javax.swing.JEditorPane, javax.swing.text.JTextComponent, javax.swing.JComponent, java.awt.Container, java.awt.Component
    protected String paramString() {
        return super.paramString();
    }
}
