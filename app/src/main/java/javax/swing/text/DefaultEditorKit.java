package javax.swing.text;

import com.sun.glass.ui.Clipboard;
import com.sun.org.apache.xml.internal.serialize.LineSeparator;
import java.awt.ComponentOrientation;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import javax.swing.Action;
import javax.swing.UIManager;
import javax.swing.text.Position;
import sun.awt.SunToolkit;

/* loaded from: rt.jar:javax/swing/text/DefaultEditorKit.class */
public class DefaultEditorKit extends EditorKit {
    public static final String EndOfLineStringProperty = "__EndOfLine__";
    public static final String insertContentAction = "insert-content";
    public static final String insertBreakAction = "insert-break";
    public static final String insertTabAction = "insert-tab";
    public static final String deletePrevCharAction = "delete-previous";
    public static final String deleteNextCharAction = "delete-next";
    public static final String readOnlyAction = "set-read-only";
    public static final String writableAction = "set-writable";
    public static final String cutAction = "cut-to-clipboard";
    public static final String copyAction = "copy-to-clipboard";
    public static final String pasteAction = "paste-from-clipboard";
    public static final String beepAction = "beep";
    public static final String selectWordAction = "select-word";
    public static final String selectLineAction = "select-line";
    public static final String selectParagraphAction = "select-paragraph";
    public static final String selectAllAction = "select-all";
    static final String unselectAction = "unselect";
    static final String toggleComponentOrientationAction = "toggle-componentOrientation";
    public static final String defaultKeyTypedAction = "default-typed";
    public static final String deletePrevWordAction = "delete-previous-word";
    public static final String deleteNextWordAction = "delete-next-word";
    public static final String pageUpAction = "page-up";
    public static final String pageDownAction = "page-down";
    static final String selectionPageUpAction = "selection-page-up";
    static final String selectionPageDownAction = "selection-page-down";
    static final String selectionPageLeftAction = "selection-page-left";
    static final String selectionPageRightAction = "selection-page-right";
    public static final String forwardAction = "caret-forward";
    public static final String backwardAction = "caret-backward";
    public static final String selectionForwardAction = "selection-forward";
    public static final String selectionBackwardAction = "selection-backward";
    public static final String upAction = "caret-up";
    public static final String downAction = "caret-down";
    public static final String selectionUpAction = "selection-up";
    public static final String selectionDownAction = "selection-down";
    public static final String beginWordAction = "caret-begin-word";
    public static final String endWordAction = "caret-end-word";
    public static final String selectionBeginWordAction = "selection-begin-word";
    public static final String selectionEndWordAction = "selection-end-word";
    public static final String previousWordAction = "caret-previous-word";
    public static final String nextWordAction = "caret-next-word";
    public static final String selectionPreviousWordAction = "selection-previous-word";
    public static final String selectionNextWordAction = "selection-next-word";
    public static final String beginLineAction = "caret-begin-line";
    public static final String endLineAction = "caret-end-line";
    public static final String selectionBeginLineAction = "selection-begin-line";
    public static final String selectionEndLineAction = "selection-end-line";
    public static final String beginParagraphAction = "caret-begin-paragraph";
    public static final String endParagraphAction = "caret-end-paragraph";
    public static final String selectionBeginParagraphAction = "selection-begin-paragraph";
    public static final String selectionEndParagraphAction = "selection-end-paragraph";
    public static final String beginAction = "caret-begin";
    public static final String endAction = "caret-end";
    public static final String selectionBeginAction = "selection-begin";
    public static final String selectionEndAction = "selection-end";
    private static final Action[] defaultActions = {new InsertContentAction(), new DeletePrevCharAction(), new DeleteNextCharAction(), new ReadOnlyAction(), new DeleteWordAction(deletePrevWordAction), new DeleteWordAction(deleteNextWordAction), new WritableAction(), new CutAction(), new CopyAction(), new PasteAction(), new VerticalPageAction(pageUpAction, -1, false), new VerticalPageAction(pageDownAction, 1, false), new VerticalPageAction(selectionPageUpAction, -1, true), new VerticalPageAction(selectionPageDownAction, 1, true), new PageAction(selectionPageLeftAction, true, true), new PageAction(selectionPageRightAction, false, true), new InsertBreakAction(), new BeepAction(), new NextVisualPositionAction(forwardAction, false, 3), new NextVisualPositionAction(backwardAction, false, 7), new NextVisualPositionAction(selectionForwardAction, true, 3), new NextVisualPositionAction(selectionBackwardAction, true, 7), new NextVisualPositionAction(upAction, false, 1), new NextVisualPositionAction(downAction, false, 5), new NextVisualPositionAction(selectionUpAction, true, 1), new NextVisualPositionAction(selectionDownAction, true, 5), new BeginWordAction(beginWordAction, false), new EndWordAction(endWordAction, false), new BeginWordAction(selectionBeginWordAction, true), new EndWordAction(selectionEndWordAction, true), new PreviousWordAction(previousWordAction, false), new NextWordAction(nextWordAction, false), new PreviousWordAction(selectionPreviousWordAction, true), new NextWordAction(selectionNextWordAction, true), new BeginLineAction(beginLineAction, false), new EndLineAction(endLineAction, false), new BeginLineAction(selectionBeginLineAction, true), new EndLineAction(selectionEndLineAction, true), new BeginParagraphAction(beginParagraphAction, false), new EndParagraphAction(endParagraphAction, false), new BeginParagraphAction(selectionBeginParagraphAction, true), new EndParagraphAction(selectionEndParagraphAction, true), new BeginAction(beginAction, false), new EndAction(endAction, false), new BeginAction(selectionBeginAction, true), new EndAction(selectionEndAction, true), new DefaultKeyTypedAction(), new InsertTabAction(), new SelectWordAction(), new SelectLineAction(), new SelectParagraphAction(), new SelectAllAction(), new UnselectAction(), new ToggleComponentOrientationAction(), new DumpModelAction()};

    @Override // javax.swing.text.EditorKit
    public String getContentType() {
        return Clipboard.TEXT_TYPE;
    }

    @Override // javax.swing.text.EditorKit
    public ViewFactory getViewFactory() {
        return null;
    }

    @Override // javax.swing.text.EditorKit
    public Action[] getActions() {
        return (Action[]) defaultActions.clone();
    }

    @Override // javax.swing.text.EditorKit
    public Caret createCaret() {
        return null;
    }

    @Override // javax.swing.text.EditorKit
    public Document createDefaultDocument() {
        return new PlainDocument();
    }

    @Override // javax.swing.text.EditorKit
    public void read(InputStream inputStream, Document document, int i2) throws IOException, BadLocationException {
        read(new InputStreamReader(inputStream), document, i2);
    }

    @Override // javax.swing.text.EditorKit
    public void write(OutputStream outputStream, Document document, int i2, int i3) throws IOException, BadLocationException {
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
        write(outputStreamWriter, document, i2, i3);
        outputStreamWriter.flush();
    }

    MutableAttributeSet getInputAttributes() {
        return null;
    }

    @Override // javax.swing.text.EditorKit
    public void read(Reader reader, Document document, int i2) throws IOException, BadLocationException {
        char[] cArr = new char[4096];
        boolean z2 = false;
        boolean z3 = false;
        boolean z4 = false;
        boolean z5 = document.getLength() == 0;
        MutableAttributeSet inputAttributes = getInputAttributes();
        while (true) {
            int i3 = reader.read(cArr, 0, cArr.length);
            if (i3 != -1) {
                int i4 = 0;
                for (int i5 = 0; i5 < i3; i5++) {
                    switch (cArr[i5]) {
                        case '\n':
                            if (z2) {
                                if (i5 > i4 + 1) {
                                    document.insertString(i2, new String(cArr, i4, (i5 - i4) - 1), inputAttributes);
                                    i2 += (i5 - i4) - 1;
                                }
                                z2 = false;
                                i4 = i5;
                                z3 = true;
                                break;
                            } else {
                                break;
                            }
                        case '\r':
                            if (z2) {
                                z4 = true;
                                if (i5 == 0) {
                                    document.insertString(i2, "\n", inputAttributes);
                                    i2++;
                                    break;
                                } else {
                                    cArr[i5 - 1] = '\n';
                                    break;
                                }
                            } else {
                                z2 = true;
                                break;
                            }
                        default:
                            if (z2) {
                                z4 = true;
                                if (i5 == 0) {
                                    document.insertString(i2, "\n", inputAttributes);
                                    i2++;
                                } else {
                                    cArr[i5 - 1] = '\n';
                                }
                                z2 = false;
                                break;
                            } else {
                                break;
                            }
                    }
                }
                if (i4 < i3) {
                    if (z2) {
                        if (i4 < i3 - 1) {
                            document.insertString(i2, new String(cArr, i4, (i3 - i4) - 1), inputAttributes);
                            i2 += (i3 - i4) - 1;
                        }
                    } else {
                        document.insertString(i2, new String(cArr, i4, i3 - i4), inputAttributes);
                        i2 += i3 - i4;
                    }
                }
            } else {
                if (z2) {
                    document.insertString(i2, "\n", inputAttributes);
                    z4 = true;
                }
                if (z5) {
                    if (z3) {
                        document.putProperty(EndOfLineStringProperty, "\r\n");
                        return;
                    } else if (z4) {
                        document.putProperty(EndOfLineStringProperty, LineSeparator.Macintosh);
                        return;
                    } else {
                        document.putProperty(EndOfLineStringProperty, "\n");
                        return;
                    }
                }
                return;
            }
        }
    }

    @Override // javax.swing.text.EditorKit
    public void write(Writer writer, Document document, int i2, int i3) throws IOException, BadLocationException {
        String str;
        if (i2 < 0 || i2 + i3 > document.getLength()) {
            throw new BadLocationException("DefaultEditorKit.write", i2);
        }
        Segment segment = new Segment();
        int i4 = i3;
        int i5 = i2;
        Object property = document.getProperty(EndOfLineStringProperty);
        if (property == null) {
            try {
                property = System.getProperty("line.separator");
            } catch (SecurityException e2) {
            }
        }
        if (property instanceof String) {
            str = (String) property;
        } else {
            str = null;
        }
        if (property != null && !str.equals("\n")) {
            while (i4 > 0) {
                int iMin = Math.min(i4, 4096);
                document.getText(i5, iMin, segment);
                int i6 = segment.offset;
                char[] cArr = segment.array;
                int i7 = i6 + segment.count;
                for (int i8 = i6; i8 < i7; i8++) {
                    if (cArr[i8] == '\n') {
                        if (i8 > i6) {
                            writer.write(cArr, i6, i8 - i6);
                        }
                        writer.write(str);
                        i6 = i8 + 1;
                    }
                }
                if (i7 > i6) {
                    writer.write(cArr, i6, i7 - i6);
                }
                i5 += iMin;
                i4 -= iMin;
            }
        } else {
            while (i4 > 0) {
                int iMin2 = Math.min(i4, 4096);
                document.getText(i5, iMin2, segment);
                writer.write(segment.array, segment.offset, segment.count);
                i5 += iMin2;
                i4 -= iMin2;
            }
        }
        writer.flush();
    }

    /* loaded from: rt.jar:javax/swing/text/DefaultEditorKit$DefaultKeyTypedAction.class */
    public static class DefaultKeyTypedAction extends TextAction {
        public DefaultKeyTypedAction() {
            super(DefaultEditorKit.defaultKeyTypedAction);
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            JTextComponent textComponent = getTextComponent(actionEvent);
            if (textComponent == null || actionEvent == null || !textComponent.isEditable() || !textComponent.isEnabled()) {
                return;
            }
            String actionCommand = actionEvent.getActionCommand();
            int modifiers = actionEvent.getModifiers();
            if (actionCommand != null && actionCommand.length() > 0) {
                boolean zIsPrintableCharacterModifiersMask = true;
                Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
                if (defaultToolkit instanceof SunToolkit) {
                    zIsPrintableCharacterModifiersMask = ((SunToolkit) defaultToolkit).isPrintableCharacterModifiersMask(modifiers);
                }
                char cCharAt = actionCommand.charAt(0);
                if ((zIsPrintableCharacterModifiersMask && cCharAt >= ' ' && cCharAt != 127) || (!zIsPrintableCharacterModifiersMask && cCharAt >= 8204 && cCharAt <= 8205)) {
                    textComponent.replaceSelection(actionCommand);
                }
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/text/DefaultEditorKit$InsertContentAction.class */
    public static class InsertContentAction extends TextAction {
        public InsertContentAction() {
            super(DefaultEditorKit.insertContentAction);
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            JTextComponent textComponent = getTextComponent(actionEvent);
            if (textComponent != null && actionEvent != null) {
                if (!textComponent.isEditable() || !textComponent.isEnabled()) {
                    UIManager.getLookAndFeel().provideErrorFeedback(textComponent);
                    return;
                }
                String actionCommand = actionEvent.getActionCommand();
                if (actionCommand != null) {
                    textComponent.replaceSelection(actionCommand);
                } else {
                    UIManager.getLookAndFeel().provideErrorFeedback(textComponent);
                }
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/text/DefaultEditorKit$InsertBreakAction.class */
    public static class InsertBreakAction extends TextAction {
        public InsertBreakAction() {
            super(DefaultEditorKit.insertBreakAction);
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            JTextComponent textComponent = getTextComponent(actionEvent);
            if (textComponent != null) {
                if (!textComponent.isEditable() || !textComponent.isEnabled()) {
                    UIManager.getLookAndFeel().provideErrorFeedback(textComponent);
                } else {
                    textComponent.replaceSelection("\n");
                }
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/text/DefaultEditorKit$InsertTabAction.class */
    public static class InsertTabAction extends TextAction {
        public InsertTabAction() {
            super(DefaultEditorKit.insertTabAction);
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            JTextComponent textComponent = getTextComponent(actionEvent);
            if (textComponent != null) {
                if (!textComponent.isEditable() || !textComponent.isEnabled()) {
                    UIManager.getLookAndFeel().provideErrorFeedback(textComponent);
                } else {
                    textComponent.replaceSelection("\t");
                }
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/text/DefaultEditorKit$DeletePrevCharAction.class */
    static class DeletePrevCharAction extends TextAction {
        DeletePrevCharAction() {
            super(DefaultEditorKit.deletePrevCharAction);
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            JTextComponent textComponent = getTextComponent(actionEvent);
            boolean z2 = true;
            if (textComponent != null && textComponent.isEditable()) {
                try {
                    Document document = textComponent.getDocument();
                    Caret caret = textComponent.getCaret();
                    int dot = caret.getDot();
                    int mark = caret.getMark();
                    if (dot != mark) {
                        document.remove(Math.min(dot, mark), Math.abs(dot - mark));
                        z2 = false;
                    } else if (dot > 0) {
                        int i2 = 1;
                        if (dot > 1) {
                            String text = document.getText(dot - 2, 2);
                            char cCharAt = text.charAt(0);
                            char cCharAt2 = text.charAt(1);
                            if (cCharAt >= 55296 && cCharAt <= 56319 && cCharAt2 >= 56320 && cCharAt2 <= 57343) {
                                i2 = 2;
                            }
                        }
                        document.remove(dot - i2, i2);
                        z2 = false;
                    }
                } catch (BadLocationException e2) {
                }
            }
            if (z2) {
                UIManager.getLookAndFeel().provideErrorFeedback(textComponent);
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/text/DefaultEditorKit$DeleteNextCharAction.class */
    static class DeleteNextCharAction extends TextAction {
        DeleteNextCharAction() {
            super(DefaultEditorKit.deleteNextCharAction);
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            JTextComponent textComponent = getTextComponent(actionEvent);
            boolean z2 = true;
            if (textComponent != null && textComponent.isEditable()) {
                try {
                    Document document = textComponent.getDocument();
                    Caret caret = textComponent.getCaret();
                    int dot = caret.getDot();
                    int mark = caret.getMark();
                    if (dot != mark) {
                        document.remove(Math.min(dot, mark), Math.abs(dot - mark));
                        z2 = false;
                    } else if (dot < document.getLength()) {
                        int i2 = 1;
                        if (dot < document.getLength() - 1) {
                            String text = document.getText(dot, 2);
                            char cCharAt = text.charAt(0);
                            char cCharAt2 = text.charAt(1);
                            if (cCharAt >= 55296 && cCharAt <= 56319 && cCharAt2 >= 56320 && cCharAt2 <= 57343) {
                                i2 = 2;
                            }
                        }
                        document.remove(dot, i2);
                        z2 = false;
                    }
                } catch (BadLocationException e2) {
                }
            }
            if (z2) {
                UIManager.getLookAndFeel().provideErrorFeedback(textComponent);
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/text/DefaultEditorKit$DeleteWordAction.class */
    static class DeleteWordAction extends TextAction {
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !DefaultEditorKit.class.desiredAssertionStatus();
        }

        DeleteWordAction(String str) {
            super(str);
            if (!$assertionsDisabled && str != DefaultEditorKit.deletePrevWordAction && str != DefaultEditorKit.deleteNextWordAction) {
                throw new AssertionError();
            }
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            int prevWordInParagraph;
            JTextComponent textComponent = getTextComponent(actionEvent);
            if (textComponent != null && actionEvent != null) {
                if (!textComponent.isEditable() || !textComponent.isEnabled()) {
                    UIManager.getLookAndFeel().provideErrorFeedback(textComponent);
                    return;
                }
                boolean z2 = true;
                try {
                    int selectionStart = textComponent.getSelectionStart();
                    Element paragraphElement = Utilities.getParagraphElement(textComponent, selectionStart);
                    if (DefaultEditorKit.deleteNextWordAction == getValue("Name")) {
                        prevWordInParagraph = Utilities.getNextWordInParagraph(textComponent, paragraphElement, selectionStart, false);
                        if (prevWordInParagraph == -1) {
                            int endOffset = paragraphElement.getEndOffset();
                            if (selectionStart == endOffset - 1) {
                                prevWordInParagraph = endOffset;
                            } else {
                                prevWordInParagraph = endOffset - 1;
                            }
                        }
                    } else {
                        prevWordInParagraph = Utilities.getPrevWordInParagraph(textComponent, paragraphElement, selectionStart);
                        if (prevWordInParagraph == -1) {
                            int startOffset = paragraphElement.getStartOffset();
                            if (selectionStart == startOffset) {
                                prevWordInParagraph = startOffset - 1;
                            } else {
                                prevWordInParagraph = startOffset;
                            }
                        }
                    }
                    int iMin = Math.min(selectionStart, prevWordInParagraph);
                    int iAbs = Math.abs(prevWordInParagraph - selectionStart);
                    if (iMin >= 0) {
                        textComponent.getDocument().remove(iMin, iAbs);
                        z2 = false;
                    }
                } catch (BadLocationException e2) {
                }
                if (z2) {
                    UIManager.getLookAndFeel().provideErrorFeedback(textComponent);
                }
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/text/DefaultEditorKit$ReadOnlyAction.class */
    static class ReadOnlyAction extends TextAction {
        ReadOnlyAction() {
            super(DefaultEditorKit.readOnlyAction);
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            JTextComponent textComponent = getTextComponent(actionEvent);
            if (textComponent != null) {
                textComponent.setEditable(false);
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/text/DefaultEditorKit$WritableAction.class */
    static class WritableAction extends TextAction {
        WritableAction() {
            super(DefaultEditorKit.writableAction);
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            JTextComponent textComponent = getTextComponent(actionEvent);
            if (textComponent != null) {
                textComponent.setEditable(true);
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/text/DefaultEditorKit$CutAction.class */
    public static class CutAction extends TextAction {
        public CutAction() {
            super(DefaultEditorKit.cutAction);
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            JTextComponent textComponent = getTextComponent(actionEvent);
            if (textComponent != null) {
                textComponent.cut();
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/text/DefaultEditorKit$CopyAction.class */
    public static class CopyAction extends TextAction {
        public CopyAction() {
            super(DefaultEditorKit.copyAction);
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            JTextComponent textComponent = getTextComponent(actionEvent);
            if (textComponent != null) {
                textComponent.copy();
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/text/DefaultEditorKit$PasteAction.class */
    public static class PasteAction extends TextAction {
        public PasteAction() {
            super(DefaultEditorKit.pasteAction);
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            JTextComponent textComponent = getTextComponent(actionEvent);
            if (textComponent != null) {
                textComponent.paste();
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/text/DefaultEditorKit$BeepAction.class */
    public static class BeepAction extends TextAction {
        public BeepAction() {
            super(DefaultEditorKit.beepAction);
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            UIManager.getLookAndFeel().provideErrorFeedback(getTextComponent(actionEvent));
        }
    }

    /* loaded from: rt.jar:javax/swing/text/DefaultEditorKit$VerticalPageAction.class */
    static class VerticalPageAction extends TextAction {
        private boolean select;
        private int direction;

        public VerticalPageAction(String str, int i2, boolean z2) {
            super(str);
            this.select = z2;
            this.direction = i2;
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            int iViewToModel;
            JTextComponent textComponent = getTextComponent(actionEvent);
            if (textComponent != null) {
                Rectangle visibleRect = textComponent.getVisibleRect();
                Rectangle rectangle = new Rectangle(visibleRect);
                int caretPosition = textComponent.getCaretPosition();
                int scrollableBlockIncrement = this.direction * textComponent.getScrollableBlockIncrement(visibleRect, 1, this.direction);
                int i2 = visibleRect.f12373y;
                Caret caret = textComponent.getCaret();
                Point magicCaretPosition = caret.getMagicCaretPosition();
                if (caretPosition != -1) {
                    try {
                        Rectangle rectangleModelToView = textComponent.modelToView(caretPosition);
                        int i3 = magicCaretPosition != null ? magicCaretPosition.f12370x : rectangleModelToView.f12372x;
                        int i4 = rectangleModelToView.height;
                        if (i4 > 0) {
                            scrollableBlockIncrement = (scrollableBlockIncrement / i4) * i4;
                        }
                        rectangle.f12373y = constrainY(textComponent, i2 + scrollableBlockIncrement, visibleRect.height);
                        if (visibleRect.contains(rectangleModelToView.f12372x, rectangleModelToView.f12373y)) {
                            iViewToModel = textComponent.viewToModel(new Point(i3, constrainY(textComponent, rectangleModelToView.f12373y + scrollableBlockIncrement, 0)));
                        } else if (this.direction == -1) {
                            iViewToModel = textComponent.viewToModel(new Point(i3, rectangle.f12373y));
                        } else {
                            iViewToModel = textComponent.viewToModel(new Point(i3, rectangle.f12373y + visibleRect.height));
                        }
                        int iConstrainOffset = constrainOffset(textComponent, iViewToModel);
                        if (iConstrainOffset != caretPosition) {
                            int adjustedY = getAdjustedY(textComponent, rectangle, iConstrainOffset);
                            if ((this.direction == -1 && adjustedY <= i2) || (this.direction == 1 && adjustedY >= i2)) {
                                rectangle.f12373y = adjustedY;
                                if (this.select) {
                                    textComponent.moveCaretPosition(iConstrainOffset);
                                } else {
                                    textComponent.setCaretPosition(iConstrainOffset);
                                }
                            }
                        }
                    } catch (BadLocationException e2) {
                    }
                } else {
                    rectangle.f12373y = constrainY(textComponent, i2 + scrollableBlockIncrement, visibleRect.height);
                }
                if (magicCaretPosition != null) {
                    caret.setMagicCaretPosition(magicCaretPosition);
                }
                textComponent.scrollRectToVisible(rectangle);
            }
        }

        private int constrainY(JTextComponent jTextComponent, int i2, int i3) {
            if (i2 < 0) {
                i2 = 0;
            } else if (i2 + i3 > jTextComponent.getHeight()) {
                i2 = Math.max(0, jTextComponent.getHeight() - i3);
            }
            return i2;
        }

        private int constrainOffset(JTextComponent jTextComponent, int i2) {
            Document document = jTextComponent.getDocument();
            if (i2 != 0 && i2 > document.getLength()) {
                i2 = document.getLength();
            }
            if (i2 < 0) {
                i2 = 0;
            }
            return i2;
        }

        private int getAdjustedY(JTextComponent jTextComponent, Rectangle rectangle, int i2) {
            int i3 = rectangle.f12373y;
            try {
                Rectangle rectangleModelToView = jTextComponent.modelToView(i2);
                if (rectangleModelToView.f12373y < rectangle.f12373y) {
                    i3 = rectangleModelToView.f12373y;
                } else if (rectangleModelToView.f12373y > rectangle.f12373y + rectangle.height || rectangleModelToView.f12373y + rectangleModelToView.height > rectangle.f12373y + rectangle.height) {
                    i3 = (rectangleModelToView.f12373y + rectangleModelToView.height) - rectangle.height;
                }
            } catch (BadLocationException e2) {
            }
            return i3;
        }
    }

    /* loaded from: rt.jar:javax/swing/text/DefaultEditorKit$PageAction.class */
    static class PageAction extends TextAction {
        private boolean select;
        private boolean left;

        public PageAction(String str, boolean z2, boolean z3) {
            super(str);
            this.select = z3;
            this.left = z2;
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            int iViewToModel;
            JTextComponent textComponent = getTextComponent(actionEvent);
            if (textComponent != null) {
                Rectangle rectangle = new Rectangle();
                textComponent.computeVisibleRect(rectangle);
                if (this.left) {
                    rectangle.f12372x = Math.max(0, rectangle.f12372x - rectangle.width);
                } else {
                    rectangle.f12372x += rectangle.width;
                }
                if (textComponent.getCaretPosition() != -1) {
                    if (this.left) {
                        iViewToModel = textComponent.viewToModel(new Point(rectangle.f12372x, rectangle.f12373y));
                    } else {
                        iViewToModel = textComponent.viewToModel(new Point((rectangle.f12372x + rectangle.width) - 1, (rectangle.f12373y + rectangle.height) - 1));
                    }
                    Document document = textComponent.getDocument();
                    if (iViewToModel != 0 && iViewToModel > document.getLength() - 1) {
                        iViewToModel = document.getLength() - 1;
                    } else if (iViewToModel < 0) {
                        iViewToModel = 0;
                    }
                    if (this.select) {
                        textComponent.moveCaretPosition(iViewToModel);
                    } else {
                        textComponent.setCaretPosition(iViewToModel);
                    }
                }
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/text/DefaultEditorKit$DumpModelAction.class */
    static class DumpModelAction extends TextAction {
        DumpModelAction() {
            super("dump-model");
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            JTextComponent textComponent = getTextComponent(actionEvent);
            if (textComponent != null) {
                Document document = textComponent.getDocument();
                if (document instanceof AbstractDocument) {
                    ((AbstractDocument) document).dump(System.err);
                }
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/text/DefaultEditorKit$NextVisualPositionAction.class */
    static class NextVisualPositionAction extends TextAction {
        private boolean select;
        private int direction;

        NextVisualPositionAction(String str, boolean z2, int i2) {
            super(str);
            this.select = z2;
            this.direction = i2;
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            Rectangle rectangleModelToView;
            int nextVisualPositionFrom;
            JTextComponent textComponent = getTextComponent(actionEvent);
            if (textComponent != null) {
                Caret caret = textComponent.getCaret();
                DefaultCaret defaultCaret = caret instanceof DefaultCaret ? (DefaultCaret) caret : null;
                int dot = caret.getDot();
                Position.Bias[] biasArr = new Position.Bias[1];
                Point magicCaretPosition = caret.getMagicCaretPosition();
                if (magicCaretPosition == null) {
                    try {
                        if (this.direction == 1 || this.direction == 5) {
                            if (defaultCaret != null) {
                                rectangleModelToView = textComponent.getUI().modelToView(textComponent, dot, defaultCaret.getDotBias());
                            } else {
                                rectangleModelToView = textComponent.modelToView(dot);
                            }
                            Rectangle rectangle = rectangleModelToView;
                            magicCaretPosition = new Point(rectangle.f12372x, rectangle.f12373y);
                        }
                    } catch (BadLocationException e2) {
                        return;
                    }
                }
                NavigationFilter navigationFilter = textComponent.getNavigationFilter();
                if (navigationFilter != null) {
                    nextVisualPositionFrom = navigationFilter.getNextVisualPositionFrom(textComponent, dot, defaultCaret != null ? defaultCaret.getDotBias() : Position.Bias.Forward, this.direction, biasArr);
                } else {
                    nextVisualPositionFrom = textComponent.getUI().getNextVisualPositionFrom(textComponent, dot, defaultCaret != null ? defaultCaret.getDotBias() : Position.Bias.Forward, this.direction, biasArr);
                }
                if (biasArr[0] == null) {
                    biasArr[0] = Position.Bias.Forward;
                }
                if (defaultCaret != null) {
                    if (this.select) {
                        defaultCaret.moveDot(nextVisualPositionFrom, biasArr[0]);
                    } else {
                        defaultCaret.setDot(nextVisualPositionFrom, biasArr[0]);
                    }
                } else if (this.select) {
                    caret.moveDot(nextVisualPositionFrom);
                } else {
                    caret.setDot(nextVisualPositionFrom);
                }
                if (magicCaretPosition != null && (this.direction == 1 || this.direction == 5)) {
                    textComponent.getCaret().setMagicCaretPosition(magicCaretPosition);
                }
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/text/DefaultEditorKit$BeginWordAction.class */
    static class BeginWordAction extends TextAction {
        private boolean select;

        BeginWordAction(String str, boolean z2) {
            super(str);
            this.select = z2;
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            JTextComponent textComponent = getTextComponent(actionEvent);
            if (textComponent != null) {
                try {
                    int wordStart = Utilities.getWordStart(textComponent, textComponent.getCaretPosition());
                    if (this.select) {
                        textComponent.moveCaretPosition(wordStart);
                    } else {
                        textComponent.setCaretPosition(wordStart);
                    }
                } catch (BadLocationException e2) {
                    UIManager.getLookAndFeel().provideErrorFeedback(textComponent);
                }
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/text/DefaultEditorKit$EndWordAction.class */
    static class EndWordAction extends TextAction {
        private boolean select;

        EndWordAction(String str, boolean z2) {
            super(str);
            this.select = z2;
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            JTextComponent textComponent = getTextComponent(actionEvent);
            if (textComponent != null) {
                try {
                    int wordEnd = Utilities.getWordEnd(textComponent, textComponent.getCaretPosition());
                    if (this.select) {
                        textComponent.moveCaretPosition(wordEnd);
                    } else {
                        textComponent.setCaretPosition(wordEnd);
                    }
                } catch (BadLocationException e2) {
                    UIManager.getLookAndFeel().provideErrorFeedback(textComponent);
                }
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/text/DefaultEditorKit$PreviousWordAction.class */
    static class PreviousWordAction extends TextAction {
        private boolean select;

        PreviousWordAction(String str, boolean z2) {
            super(str);
            this.select = z2;
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            JTextComponent textComponent = getTextComponent(actionEvent);
            if (textComponent != null) {
                int caretPosition = textComponent.getCaretPosition();
                boolean z2 = false;
                try {
                    Element paragraphElement = Utilities.getParagraphElement(textComponent, caretPosition);
                    caretPosition = Utilities.getPreviousWord(textComponent, caretPosition);
                    if (caretPosition < paragraphElement.getStartOffset()) {
                        caretPosition = Utilities.getParagraphElement(textComponent, caretPosition).getEndOffset() - 1;
                    }
                } catch (BadLocationException e2) {
                    if (caretPosition != 0) {
                        caretPosition = 0;
                    } else {
                        z2 = true;
                    }
                }
                if (!z2) {
                    if (this.select) {
                        textComponent.moveCaretPosition(caretPosition);
                        return;
                    } else {
                        textComponent.setCaretPosition(caretPosition);
                        return;
                    }
                }
                UIManager.getLookAndFeel().provideErrorFeedback(textComponent);
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/text/DefaultEditorKit$NextWordAction.class */
    static class NextWordAction extends TextAction {
        private boolean select;

        NextWordAction(String str, boolean z2) {
            super(str);
            this.select = z2;
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            JTextComponent textComponent = getTextComponent(actionEvent);
            if (textComponent != null) {
                int caretPosition = textComponent.getCaretPosition();
                boolean z2 = false;
                Element paragraphElement = Utilities.getParagraphElement(textComponent, caretPosition);
                try {
                    caretPosition = Utilities.getNextWord(textComponent, caretPosition);
                    if (caretPosition >= paragraphElement.getEndOffset() && caretPosition != paragraphElement.getEndOffset() - 1) {
                        caretPosition = paragraphElement.getEndOffset() - 1;
                    }
                } catch (BadLocationException e2) {
                    int length = textComponent.getDocument().getLength();
                    if (caretPosition != length) {
                        if (caretPosition != paragraphElement.getEndOffset() - 1) {
                            caretPosition = paragraphElement.getEndOffset() - 1;
                        } else {
                            caretPosition = length;
                        }
                    } else {
                        z2 = true;
                    }
                }
                if (!z2) {
                    if (this.select) {
                        textComponent.moveCaretPosition(caretPosition);
                        return;
                    } else {
                        textComponent.setCaretPosition(caretPosition);
                        return;
                    }
                }
                UIManager.getLookAndFeel().provideErrorFeedback(textComponent);
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/text/DefaultEditorKit$BeginLineAction.class */
    static class BeginLineAction extends TextAction {
        private boolean select;

        BeginLineAction(String str, boolean z2) {
            super(str);
            this.select = z2;
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            JTextComponent textComponent = getTextComponent(actionEvent);
            if (textComponent != null) {
                try {
                    int rowStart = Utilities.getRowStart(textComponent, textComponent.getCaretPosition());
                    if (this.select) {
                        textComponent.moveCaretPosition(rowStart);
                    } else {
                        textComponent.setCaretPosition(rowStart);
                    }
                } catch (BadLocationException e2) {
                    UIManager.getLookAndFeel().provideErrorFeedback(textComponent);
                }
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/text/DefaultEditorKit$EndLineAction.class */
    static class EndLineAction extends TextAction {
        private boolean select;

        EndLineAction(String str, boolean z2) {
            super(str);
            this.select = z2;
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            JTextComponent textComponent = getTextComponent(actionEvent);
            if (textComponent != null) {
                try {
                    int rowEnd = Utilities.getRowEnd(textComponent, textComponent.getCaretPosition());
                    if (this.select) {
                        textComponent.moveCaretPosition(rowEnd);
                    } else {
                        textComponent.setCaretPosition(rowEnd);
                    }
                } catch (BadLocationException e2) {
                    UIManager.getLookAndFeel().provideErrorFeedback(textComponent);
                }
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/text/DefaultEditorKit$BeginParagraphAction.class */
    static class BeginParagraphAction extends TextAction {
        private boolean select;

        BeginParagraphAction(String str, boolean z2) {
            super(str);
            this.select = z2;
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            JTextComponent textComponent = getTextComponent(actionEvent);
            if (textComponent != null) {
                int startOffset = Utilities.getParagraphElement(textComponent, textComponent.getCaretPosition()).getStartOffset();
                if (this.select) {
                    textComponent.moveCaretPosition(startOffset);
                } else {
                    textComponent.setCaretPosition(startOffset);
                }
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/text/DefaultEditorKit$EndParagraphAction.class */
    static class EndParagraphAction extends TextAction {
        private boolean select;

        EndParagraphAction(String str, boolean z2) {
            super(str);
            this.select = z2;
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            JTextComponent textComponent = getTextComponent(actionEvent);
            if (textComponent != null) {
                int iMin = Math.min(textComponent.getDocument().getLength(), Utilities.getParagraphElement(textComponent, textComponent.getCaretPosition()).getEndOffset());
                if (this.select) {
                    textComponent.moveCaretPosition(iMin);
                } else {
                    textComponent.setCaretPosition(iMin);
                }
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/text/DefaultEditorKit$BeginAction.class */
    static class BeginAction extends TextAction {
        private boolean select;

        BeginAction(String str, boolean z2) {
            super(str);
            this.select = z2;
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            JTextComponent textComponent = getTextComponent(actionEvent);
            if (textComponent != null) {
                if (this.select) {
                    textComponent.moveCaretPosition(0);
                } else {
                    textComponent.setCaretPosition(0);
                }
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/text/DefaultEditorKit$EndAction.class */
    static class EndAction extends TextAction {
        private boolean select;

        EndAction(String str, boolean z2) {
            super(str);
            this.select = z2;
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            JTextComponent textComponent = getTextComponent(actionEvent);
            if (textComponent != null) {
                int length = textComponent.getDocument().getLength();
                if (this.select) {
                    textComponent.moveCaretPosition(length);
                } else {
                    textComponent.setCaretPosition(length);
                }
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/text/DefaultEditorKit$SelectWordAction.class */
    static class SelectWordAction extends TextAction {
        private Action start;
        private Action end;

        SelectWordAction() {
            super(DefaultEditorKit.selectWordAction);
            this.start = new BeginWordAction("pigdog", false);
            this.end = new EndWordAction("pigdog", true);
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            this.start.actionPerformed(actionEvent);
            this.end.actionPerformed(actionEvent);
        }
    }

    /* loaded from: rt.jar:javax/swing/text/DefaultEditorKit$SelectLineAction.class */
    static class SelectLineAction extends TextAction {
        private Action start;
        private Action end;

        SelectLineAction() {
            super(DefaultEditorKit.selectLineAction);
            this.start = new BeginLineAction("pigdog", false);
            this.end = new EndLineAction("pigdog", true);
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            this.start.actionPerformed(actionEvent);
            this.end.actionPerformed(actionEvent);
        }
    }

    /* loaded from: rt.jar:javax/swing/text/DefaultEditorKit$SelectParagraphAction.class */
    static class SelectParagraphAction extends TextAction {
        private Action start;
        private Action end;

        SelectParagraphAction() {
            super(DefaultEditorKit.selectParagraphAction);
            this.start = new BeginParagraphAction("pigdog", false);
            this.end = new EndParagraphAction("pigdog", true);
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            this.start.actionPerformed(actionEvent);
            this.end.actionPerformed(actionEvent);
        }
    }

    /* loaded from: rt.jar:javax/swing/text/DefaultEditorKit$SelectAllAction.class */
    static class SelectAllAction extends TextAction {
        SelectAllAction() {
            super(DefaultEditorKit.selectAllAction);
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            JTextComponent textComponent = getTextComponent(actionEvent);
            if (textComponent != null) {
                Document document = textComponent.getDocument();
                textComponent.setCaretPosition(0);
                textComponent.moveCaretPosition(document.getLength());
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/text/DefaultEditorKit$UnselectAction.class */
    static class UnselectAction extends TextAction {
        UnselectAction() {
            super(DefaultEditorKit.unselectAction);
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            JTextComponent textComponent = getTextComponent(actionEvent);
            if (textComponent != null) {
                textComponent.setCaretPosition(textComponent.getCaretPosition());
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/text/DefaultEditorKit$ToggleComponentOrientationAction.class */
    static class ToggleComponentOrientationAction extends TextAction {
        ToggleComponentOrientationAction() {
            super(DefaultEditorKit.toggleComponentOrientationAction);
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            ComponentOrientation componentOrientation;
            JTextComponent textComponent = getTextComponent(actionEvent);
            if (textComponent != null) {
                if (textComponent.getComponentOrientation() == ComponentOrientation.RIGHT_TO_LEFT) {
                    componentOrientation = ComponentOrientation.LEFT_TO_RIGHT;
                } else {
                    componentOrientation = ComponentOrientation.RIGHT_TO_LEFT;
                }
                textComponent.setComponentOrientation(componentOrientation);
                textComponent.repaint();
            }
        }
    }
}
