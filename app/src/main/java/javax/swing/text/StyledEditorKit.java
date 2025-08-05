package javax.swing.text;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import javax.swing.Action;
import javax.swing.JEditorPane;
import javax.swing.UIManager;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

/* loaded from: rt.jar:javax/swing/text/StyledEditorKit.class */
public class StyledEditorKit extends DefaultEditorKit {
    Element currentRun;
    Element currentParagraph;
    MutableAttributeSet inputAttributes;
    private AttributeTracker inputAttributeUpdater;
    private static final ViewFactory defaultFactory = new StyledViewFactory();
    private static final Action[] defaultActions = {new FontFamilyAction("font-family-SansSerif", "SansSerif"), new FontFamilyAction("font-family-Monospaced", "Monospaced"), new FontFamilyAction("font-family-Serif", "Serif"), new FontSizeAction("font-size-8", 8), new FontSizeAction("font-size-10", 10), new FontSizeAction("font-size-12", 12), new FontSizeAction("font-size-14", 14), new FontSizeAction("font-size-16", 16), new FontSizeAction("font-size-18", 18), new FontSizeAction("font-size-24", 24), new FontSizeAction("font-size-36", 36), new FontSizeAction("font-size-48", 48), new AlignmentAction("left-justify", 0), new AlignmentAction("center-justify", 1), new AlignmentAction("right-justify", 2), new BoldAction(), new ItalicAction(), new StyledInsertBreakAction(), new UnderlineAction()};

    public StyledEditorKit() {
        createInputAttributeUpdated();
        createInputAttributes();
    }

    @Override // javax.swing.text.DefaultEditorKit
    public MutableAttributeSet getInputAttributes() {
        return this.inputAttributes;
    }

    public Element getCharacterAttributeRun() {
        return this.currentRun;
    }

    @Override // javax.swing.text.DefaultEditorKit, javax.swing.text.EditorKit
    public Action[] getActions() {
        return TextAction.augmentList(super.getActions(), defaultActions);
    }

    @Override // javax.swing.text.DefaultEditorKit, javax.swing.text.EditorKit
    public Document createDefaultDocument() {
        return new DefaultStyledDocument();
    }

    @Override // javax.swing.text.EditorKit
    public void install(JEditorPane jEditorPane) {
        jEditorPane.addCaretListener(this.inputAttributeUpdater);
        jEditorPane.addPropertyChangeListener(this.inputAttributeUpdater);
        Caret caret = jEditorPane.getCaret();
        if (caret != null) {
            this.inputAttributeUpdater.updateInputAttributes(caret.getDot(), caret.getMark(), jEditorPane);
        }
    }

    @Override // javax.swing.text.EditorKit
    public void deinstall(JEditorPane jEditorPane) {
        jEditorPane.removeCaretListener(this.inputAttributeUpdater);
        jEditorPane.removePropertyChangeListener(this.inputAttributeUpdater);
        this.currentRun = null;
        this.currentParagraph = null;
    }

    @Override // javax.swing.text.DefaultEditorKit, javax.swing.text.EditorKit
    public ViewFactory getViewFactory() {
        return defaultFactory;
    }

    @Override // javax.swing.text.EditorKit
    public Object clone() {
        StyledEditorKit styledEditorKit = (StyledEditorKit) super.clone();
        styledEditorKit.currentParagraph = null;
        styledEditorKit.currentRun = null;
        styledEditorKit.createInputAttributeUpdated();
        styledEditorKit.createInputAttributes();
        return styledEditorKit;
    }

    private void createInputAttributes() {
        this.inputAttributes = new SimpleAttributeSet() { // from class: javax.swing.text.StyledEditorKit.1
            @Override // javax.swing.text.SimpleAttributeSet, javax.swing.text.AttributeSet
            public AttributeSet getResolveParent() {
                if (StyledEditorKit.this.currentParagraph != null) {
                    return StyledEditorKit.this.currentParagraph.getAttributes();
                }
                return null;
            }

            @Override // javax.swing.text.SimpleAttributeSet
            public Object clone() {
                return new SimpleAttributeSet(this);
            }
        };
    }

    private void createInputAttributeUpdated() {
        this.inputAttributeUpdater = new AttributeTracker();
    }

    /* loaded from: rt.jar:javax/swing/text/StyledEditorKit$AttributeTracker.class */
    class AttributeTracker implements CaretListener, PropertyChangeListener, Serializable {
        AttributeTracker() {
        }

        void updateInputAttributes(int i2, int i3, JTextComponent jTextComponent) {
            Element characterElement;
            Document document = jTextComponent.getDocument();
            if (!(document instanceof StyledDocument)) {
                return;
            }
            int iMin = Math.min(i2, i3);
            StyledDocument styledDocument = (StyledDocument) document;
            StyledEditorKit.this.currentParagraph = styledDocument.getParagraphElement(iMin);
            if (StyledEditorKit.this.currentParagraph.getStartOffset() == iMin || i2 != i3) {
                characterElement = styledDocument.getCharacterElement(iMin);
            } else {
                characterElement = styledDocument.getCharacterElement(Math.max(iMin - 1, 0));
            }
            if (characterElement != StyledEditorKit.this.currentRun) {
                StyledEditorKit.this.currentRun = characterElement;
                StyledEditorKit.this.createInputAttributes(StyledEditorKit.this.currentRun, StyledEditorKit.this.getInputAttributes());
            }
        }

        @Override // java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            Object newValue = propertyChangeEvent.getNewValue();
            Object source = propertyChangeEvent.getSource();
            if ((source instanceof JTextComponent) && (newValue instanceof Document)) {
                updateInputAttributes(0, 0, (JTextComponent) source);
            }
        }

        @Override // javax.swing.event.CaretListener
        public void caretUpdate(CaretEvent caretEvent) {
            updateInputAttributes(caretEvent.getDot(), caretEvent.getMark(), (JTextComponent) caretEvent.getSource());
        }
    }

    protected void createInputAttributes(Element element, MutableAttributeSet mutableAttributeSet) {
        if (element.getAttributes().getAttributeCount() > 0 || element.getEndOffset() - element.getStartOffset() > 1 || element.getEndOffset() < element.getDocument().getLength()) {
            mutableAttributeSet.removeAttributes(mutableAttributeSet);
            mutableAttributeSet.addAttributes(element.getAttributes());
            mutableAttributeSet.removeAttribute(StyleConstants.ComponentAttribute);
            mutableAttributeSet.removeAttribute(StyleConstants.IconAttribute);
            mutableAttributeSet.removeAttribute(AbstractDocument.ElementNameAttribute);
            mutableAttributeSet.removeAttribute(StyleConstants.ComposedTextAttribute);
        }
    }

    /* loaded from: rt.jar:javax/swing/text/StyledEditorKit$StyledViewFactory.class */
    static class StyledViewFactory implements ViewFactory {
        StyledViewFactory() {
        }

        @Override // javax.swing.text.ViewFactory
        public View create(Element element) {
            String name = element.getName();
            if (name != null) {
                if (name.equals(AbstractDocument.ContentElementName)) {
                    return new LabelView(element);
                }
                if (name.equals(AbstractDocument.ParagraphElementName)) {
                    return new ParagraphView(element);
                }
                if (name.equals(AbstractDocument.SectionElementName)) {
                    return new BoxView(element, 1);
                }
                if (name.equals("component")) {
                    return new ComponentView(element);
                }
                if (name.equals("icon")) {
                    return new IconView(element);
                }
            }
            return new LabelView(element);
        }
    }

    /* loaded from: rt.jar:javax/swing/text/StyledEditorKit$StyledTextAction.class */
    public static abstract class StyledTextAction extends TextAction {
        public StyledTextAction(String str) {
            super(str);
        }

        protected final JEditorPane getEditor(ActionEvent actionEvent) {
            JTextComponent textComponent = getTextComponent(actionEvent);
            if (textComponent instanceof JEditorPane) {
                return (JEditorPane) textComponent;
            }
            return null;
        }

        protected final StyledDocument getStyledDocument(JEditorPane jEditorPane) {
            Document document = jEditorPane.getDocument();
            if (document instanceof StyledDocument) {
                return (StyledDocument) document;
            }
            throw new IllegalArgumentException("document must be StyledDocument");
        }

        protected final StyledEditorKit getStyledEditorKit(JEditorPane jEditorPane) {
            EditorKit editorKit = jEditorPane.getEditorKit();
            if (editorKit instanceof StyledEditorKit) {
                return (StyledEditorKit) editorKit;
            }
            throw new IllegalArgumentException("EditorKit must be StyledEditorKit");
        }

        protected final void setCharacterAttributes(JEditorPane jEditorPane, AttributeSet attributeSet, boolean z2) {
            int selectionStart = jEditorPane.getSelectionStart();
            int selectionEnd = jEditorPane.getSelectionEnd();
            if (selectionStart != selectionEnd) {
                getStyledDocument(jEditorPane).setCharacterAttributes(selectionStart, selectionEnd - selectionStart, attributeSet, z2);
            }
            MutableAttributeSet inputAttributes = getStyledEditorKit(jEditorPane).getInputAttributes();
            if (z2) {
                inputAttributes.removeAttributes(inputAttributes);
            }
            inputAttributes.addAttributes(attributeSet);
        }

        protected final void setParagraphAttributes(JEditorPane jEditorPane, AttributeSet attributeSet, boolean z2) {
            int selectionStart = jEditorPane.getSelectionStart();
            getStyledDocument(jEditorPane).setParagraphAttributes(selectionStart, jEditorPane.getSelectionEnd() - selectionStart, attributeSet, z2);
        }
    }

    /* loaded from: rt.jar:javax/swing/text/StyledEditorKit$FontFamilyAction.class */
    public static class FontFamilyAction extends StyledTextAction {
        private String family;

        public FontFamilyAction(String str, String str2) {
            super(str);
            this.family = str2;
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            String actionCommand;
            JEditorPane editor = getEditor(actionEvent);
            if (editor != null) {
                String str = this.family;
                if (actionEvent != null && actionEvent.getSource() == editor && (actionCommand = actionEvent.getActionCommand()) != null) {
                    str = actionCommand;
                }
                if (str != null) {
                    SimpleAttributeSet simpleAttributeSet = new SimpleAttributeSet();
                    StyleConstants.setFontFamily(simpleAttributeSet, str);
                    setCharacterAttributes(editor, simpleAttributeSet, false);
                    return;
                }
                UIManager.getLookAndFeel().provideErrorFeedback(editor);
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/text/StyledEditorKit$FontSizeAction.class */
    public static class FontSizeAction extends StyledTextAction {
        private int size;

        public FontSizeAction(String str, int i2) {
            super(str);
            this.size = i2;
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            JEditorPane editor = getEditor(actionEvent);
            if (editor != null) {
                int i2 = this.size;
                if (actionEvent != null && actionEvent.getSource() == editor) {
                    try {
                        i2 = Integer.parseInt(actionEvent.getActionCommand(), 10);
                    } catch (NumberFormatException e2) {
                    }
                }
                if (i2 != 0) {
                    SimpleAttributeSet simpleAttributeSet = new SimpleAttributeSet();
                    StyleConstants.setFontSize(simpleAttributeSet, i2);
                    setCharacterAttributes(editor, simpleAttributeSet, false);
                    return;
                }
                UIManager.getLookAndFeel().provideErrorFeedback(editor);
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/text/StyledEditorKit$ForegroundAction.class */
    public static class ForegroundAction extends StyledTextAction {
        private Color fg;

        public ForegroundAction(String str, Color color) {
            super(str);
            this.fg = color;
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            JEditorPane editor = getEditor(actionEvent);
            if (editor != null) {
                Color colorDecode = this.fg;
                if (actionEvent != null && actionEvent.getSource() == editor) {
                    try {
                        colorDecode = Color.decode(actionEvent.getActionCommand());
                    } catch (NumberFormatException e2) {
                    }
                }
                if (colorDecode != null) {
                    SimpleAttributeSet simpleAttributeSet = new SimpleAttributeSet();
                    StyleConstants.setForeground(simpleAttributeSet, colorDecode);
                    setCharacterAttributes(editor, simpleAttributeSet, false);
                    return;
                }
                UIManager.getLookAndFeel().provideErrorFeedback(editor);
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/text/StyledEditorKit$AlignmentAction.class */
    public static class AlignmentAction extends StyledTextAction {

        /* renamed from: a, reason: collision with root package name */
        private int f12842a;

        public AlignmentAction(String str, int i2) {
            super(str);
            this.f12842a = i2;
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            JEditorPane editor = getEditor(actionEvent);
            if (editor != null) {
                int i2 = this.f12842a;
                if (actionEvent != null && actionEvent.getSource() == editor) {
                    try {
                        i2 = Integer.parseInt(actionEvent.getActionCommand(), 10);
                    } catch (NumberFormatException e2) {
                    }
                }
                SimpleAttributeSet simpleAttributeSet = new SimpleAttributeSet();
                StyleConstants.setAlignment(simpleAttributeSet, i2);
                setParagraphAttributes(editor, simpleAttributeSet, false);
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/text/StyledEditorKit$BoldAction.class */
    public static class BoldAction extends StyledTextAction {
        public BoldAction() {
            super("font-bold");
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            JEditorPane editor = getEditor(actionEvent);
            if (editor != null) {
                boolean z2 = !StyleConstants.isBold(getStyledEditorKit(editor).getInputAttributes());
                SimpleAttributeSet simpleAttributeSet = new SimpleAttributeSet();
                StyleConstants.setBold(simpleAttributeSet, z2);
                setCharacterAttributes(editor, simpleAttributeSet, false);
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/text/StyledEditorKit$ItalicAction.class */
    public static class ItalicAction extends StyledTextAction {
        public ItalicAction() {
            super("font-italic");
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            JEditorPane editor = getEditor(actionEvent);
            if (editor != null) {
                boolean z2 = !StyleConstants.isItalic(getStyledEditorKit(editor).getInputAttributes());
                SimpleAttributeSet simpleAttributeSet = new SimpleAttributeSet();
                StyleConstants.setItalic(simpleAttributeSet, z2);
                setCharacterAttributes(editor, simpleAttributeSet, false);
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/text/StyledEditorKit$UnderlineAction.class */
    public static class UnderlineAction extends StyledTextAction {
        public UnderlineAction() {
            super("font-underline");
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            JEditorPane editor = getEditor(actionEvent);
            if (editor != null) {
                boolean z2 = !StyleConstants.isUnderline(getStyledEditorKit(editor).getInputAttributes());
                SimpleAttributeSet simpleAttributeSet = new SimpleAttributeSet();
                StyleConstants.setUnderline(simpleAttributeSet, z2);
                setCharacterAttributes(editor, simpleAttributeSet, false);
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/text/StyledEditorKit$StyledInsertBreakAction.class */
    static class StyledInsertBreakAction extends StyledTextAction {
        private SimpleAttributeSet tempSet;

        StyledInsertBreakAction() {
            super(DefaultEditorKit.insertBreakAction);
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            JEditorPane editor = getEditor(actionEvent);
            if (editor != null) {
                if (!editor.isEditable() || !editor.isEnabled()) {
                    UIManager.getLookAndFeel().provideErrorFeedback(editor);
                    return;
                }
                StyledEditorKit styledEditorKit = getStyledEditorKit(editor);
                if (this.tempSet != null) {
                    this.tempSet.removeAttributes(this.tempSet);
                } else {
                    this.tempSet = new SimpleAttributeSet();
                }
                this.tempSet.addAttributes(styledEditorKit.getInputAttributes());
                editor.replaceSelection("\n");
                MutableAttributeSet inputAttributes = styledEditorKit.getInputAttributes();
                inputAttributes.removeAttributes(inputAttributes);
                inputAttributes.addAttributes(this.tempSet);
                this.tempSet.removeAttributes(this.tempSet);
                return;
            }
            JTextComponent textComponent = getTextComponent(actionEvent);
            if (textComponent != null) {
                if (!textComponent.isEditable() || !textComponent.isEnabled()) {
                    UIManager.getLookAndFeel().provideErrorFeedback(editor);
                } else {
                    textComponent.replaceSelection("\n");
                }
            }
        }
    }
}
