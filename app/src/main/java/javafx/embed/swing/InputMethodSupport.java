package javafx.embed.swing;

import com.sun.javafx.collections.ObservableListWrapper;
import com.sun.javafx.scene.input.ExtendedInputMethodRequests;
import java.awt.Rectangle;
import java.awt.event.InputMethodEvent;
import java.awt.font.TextHitInfo;
import java.awt.im.InputMethodRequests;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.input.InputMethodHighlight;
import javafx.scene.input.InputMethodTextRun;

/* loaded from: jfxrt.jar:javafx/embed/swing/InputMethodSupport.class */
class InputMethodSupport {
    InputMethodSupport() {
    }

    /* loaded from: jfxrt.jar:javafx/embed/swing/InputMethodSupport$InputMethodRequestsAdapter.class */
    public static class InputMethodRequestsAdapter implements InputMethodRequests {
        private final javafx.scene.input.InputMethodRequests fxRequests;

        public InputMethodRequestsAdapter(javafx.scene.input.InputMethodRequests fxRequests) {
            this.fxRequests = fxRequests;
        }

        @Override // java.awt.im.InputMethodRequests
        public Rectangle getTextLocation(TextHitInfo offset) {
            Point2D result = this.fxRequests.getTextLocation(offset.getInsertionIndex());
            return new Rectangle((int) result.getX(), (int) result.getY(), 0, 0);
        }

        @Override // java.awt.im.InputMethodRequests
        public TextHitInfo getLocationOffset(int x2, int y2) {
            int result = this.fxRequests.getLocationOffset(x2, y2);
            return TextHitInfo.afterOffset(result);
        }

        @Override // java.awt.im.InputMethodRequests
        public int getInsertPositionOffset() {
            if (this.fxRequests instanceof ExtendedInputMethodRequests) {
                return ((ExtendedInputMethodRequests) this.fxRequests).getInsertPositionOffset();
            }
            return 0;
        }

        @Override // java.awt.im.InputMethodRequests
        public AttributedCharacterIterator getCommittedText(int beginIndex, int endIndex, AttributedCharacterIterator.Attribute[] attributes) {
            String result = null;
            if (this.fxRequests instanceof ExtendedInputMethodRequests) {
                result = ((ExtendedInputMethodRequests) this.fxRequests).getCommittedText(beginIndex, endIndex);
            }
            if (result == null) {
                result = "";
            }
            return new AttributedString(result).getIterator();
        }

        @Override // java.awt.im.InputMethodRequests
        public int getCommittedTextLength() {
            if (this.fxRequests instanceof ExtendedInputMethodRequests) {
                return ((ExtendedInputMethodRequests) this.fxRequests).getCommittedTextLength();
            }
            return 0;
        }

        @Override // java.awt.im.InputMethodRequests
        public AttributedCharacterIterator cancelLatestCommittedText(AttributedCharacterIterator.Attribute[] attributes) {
            return null;
        }

        @Override // java.awt.im.InputMethodRequests
        public AttributedCharacterIterator getSelectedText(AttributedCharacterIterator.Attribute[] attributes) {
            String text = this.fxRequests.getSelectedText();
            if (text == null) {
                text = "";
            }
            return new AttributedString(text).getIterator();
        }
    }

    public static ObservableList<InputMethodTextRun> inputMethodEventComposed(String text, int commitCount) {
        List<InputMethodTextRun> composed = new ArrayList<>();
        if (commitCount < text.length()) {
            composed.add(new InputMethodTextRun(text.substring(commitCount), InputMethodHighlight.UNSELECTED_RAW));
        }
        return new ObservableListWrapper(composed);
    }

    public static String getTextForEvent(InputMethodEvent e2) {
        AttributedCharacterIterator text = e2.getText();
        if (e2.getText() != null) {
            StringBuilder result = new StringBuilder();
            for (char c2 = text.first(); c2 != 65535; c2 = text.next()) {
                result.append(c2);
            }
            return result.toString();
        }
        return "";
    }
}
