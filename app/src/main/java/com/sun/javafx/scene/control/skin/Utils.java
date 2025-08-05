package com.sun.javafx.scene.control.skin;

import com.sun.javafx.PlatformUtil;
import com.sun.javafx.scene.control.behavior.TextBinding;
import com.sun.javafx.scene.text.HitInfo;
import com.sun.javafx.scene.text.TextLayout;
import com.sun.javafx.tk.Toolkit;
import java.net.URL;
import java.text.Bidi;
import java.text.BreakIterator;
import java.util.function.Consumer;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.OverrunStyle;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.Mnemonic;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/Utils.class */
public class Utils {
    static final Text helper;
    static final double DEFAULT_WRAPPING_WIDTH;
    static final double DEFAULT_LINE_SPACING;
    static final String DEFAULT_TEXT;
    static final TextBoundsType DEFAULT_BOUNDS_TYPE;
    static final TextLayout layout;
    private static BreakIterator charIterator;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !Utils.class.desiredAssertionStatus();
        helper = new Text();
        DEFAULT_WRAPPING_WIDTH = helper.getWrappingWidth();
        DEFAULT_LINE_SPACING = helper.getLineSpacing();
        DEFAULT_TEXT = helper.getText();
        DEFAULT_BOUNDS_TYPE = helper.getBoundsType();
        layout = Toolkit.getToolkit().getTextLayoutFactory().createLayout();
        charIterator = null;
    }

    static double getAscent(Font font, TextBoundsType boundsType) {
        layout.setContent("", font.impl_getNativeFont());
        layout.setWrapWidth(0.0f);
        layout.setLineSpacing(0.0f);
        if (boundsType == TextBoundsType.LOGICAL_VERTICAL_CENTER) {
            layout.setBoundsType(16384);
        } else {
            layout.setBoundsType(0);
        }
        return -layout.getBounds().getMinY();
    }

    static double getLineHeight(Font font, TextBoundsType boundsType) {
        layout.setContent("", font.impl_getNativeFont());
        layout.setWrapWidth(0.0f);
        layout.setLineSpacing(0.0f);
        if (boundsType == TextBoundsType.LOGICAL_VERTICAL_CENTER) {
            layout.setBoundsType(16384);
        } else {
            layout.setBoundsType(0);
        }
        return layout.getLines()[0].getBounds().getHeight();
    }

    static double computeTextWidth(Font font, String text, double wrappingWidth) {
        layout.setContent(text != null ? text : "", font.impl_getNativeFont());
        layout.setWrapWidth((float) wrappingWidth);
        return layout.getBounds().getWidth();
    }

    static double computeTextHeight(Font font, String text, double wrappingWidth, TextBoundsType boundsType) {
        return computeTextHeight(font, text, wrappingWidth, 0.0d, boundsType);
    }

    static double computeTextHeight(Font font, String text, double wrappingWidth, double lineSpacing, TextBoundsType boundsType) {
        layout.setContent(text != null ? text : "", font.impl_getNativeFont());
        layout.setWrapWidth((float) wrappingWidth);
        layout.setLineSpacing((float) lineSpacing);
        if (boundsType == TextBoundsType.LOGICAL_VERTICAL_CENTER) {
            layout.setBoundsType(16384);
        } else {
            layout.setBoundsType(0);
        }
        return layout.getBounds().getHeight();
    }

    static int computeTruncationIndex(Font font, String text, double width) {
        helper.setText(text);
        helper.setFont(font);
        helper.setWrappingWidth(0.0d);
        helper.setLineSpacing(0.0d);
        Bounds bounds = helper.getLayoutBounds();
        Point2D endPoint = new Point2D(width - 2.0d, bounds.getMinY() + (bounds.getHeight() / 2.0d));
        int index = helper.impl_hitTestChar(endPoint).getCharIndex();
        helper.setWrappingWidth(DEFAULT_WRAPPING_WIDTH);
        helper.setLineSpacing(DEFAULT_LINE_SPACING);
        helper.setText(DEFAULT_TEXT);
        return index;
    }

    static String computeClippedText(Font font, String text, double width, OverrunStyle type, String ellipsisString) {
        String strSubstring;
        String strSubstring2;
        if (font == null) {
            throw new IllegalArgumentException("Must specify a font");
        }
        OverrunStyle style = (type == null || type == OverrunStyle.CLIP) ? OverrunStyle.ELLIPSIS : type;
        String ellipsis = type == OverrunStyle.CLIP ? "" : ellipsisString;
        if (text == null || "".equals(text)) {
            return text;
        }
        double stringWidth = computeTextWidth(font, text, 0.0d);
        if (stringWidth - width < 0.0010000000474974513d) {
            return text;
        }
        double ellipsisWidth = computeTextWidth(font, ellipsis, 0.0d);
        double availableWidth = width - ellipsisWidth;
        if (width < ellipsisWidth) {
            return "";
        }
        if (style == OverrunStyle.ELLIPSIS || style == OverrunStyle.WORD_ELLIPSIS || style == OverrunStyle.LEADING_ELLIPSIS || style == OverrunStyle.LEADING_WORD_ELLIPSIS) {
            boolean wordTrim = style == OverrunStyle.WORD_ELLIPSIS || style == OverrunStyle.LEADING_WORD_ELLIPSIS;
            if (style == OverrunStyle.ELLIPSIS && !new Bidi(text, 0).isMixed()) {
                int hit = computeTruncationIndex(font, text, width - ellipsisWidth);
                if (hit < 0 || hit >= text.length()) {
                    return text;
                }
                return text.substring(0, hit) + ellipsis;
            }
            int whitespaceIndex = -1;
            int index = 0;
            int start = (style == OverrunStyle.LEADING_ELLIPSIS || style == OverrunStyle.LEADING_WORD_ELLIPSIS) ? text.length() - 1 : 0;
            int end = start == 0 ? text.length() - 1 : 0;
            int stepValue = start == 0 ? 1 : -1;
            boolean done = start == 0 ? start > end : start < end;
            int i2 = start;
            while (true) {
                int i3 = i2;
                if (done) {
                    break;
                }
                index = i3;
                char c2 = text.charAt(index);
                if (start == 0) {
                    strSubstring2 = text.substring(0, i3 + 1);
                } else {
                    strSubstring2 = text.substring(i3, start + 1);
                }
                double total = computeTextWidth(font, strSubstring2, 0.0d);
                if (Character.isWhitespace(c2)) {
                    whitespaceIndex = index;
                }
                if (total > availableWidth) {
                    break;
                }
                done = start == 0 ? i3 >= end : i3 <= end;
                i2 = i3 + stepValue;
            }
            boolean fullTrim = !wordTrim || whitespaceIndex == -1;
            if (start == 0) {
                strSubstring = text.substring(0, fullTrim ? index : whitespaceIndex);
            } else {
                strSubstring = text.substring((fullTrim ? index : whitespaceIndex) + 1);
            }
            String substring = strSubstring;
            if (!$assertionsDisabled && text.equals(substring)) {
                throw new AssertionError();
            }
            if (style == OverrunStyle.ELLIPSIS || style == OverrunStyle.WORD_ELLIPSIS) {
                return substring + ellipsis;
            }
            return ellipsis + substring;
        }
        int leadingWhitespace = -1;
        int trailingWhitespace = -1;
        int leadingIndex = -1;
        int trailingIndex = -1;
        double total2 = 0.0d;
        for (int i4 = 0; i4 <= text.length() - 1; i4++) {
            char c3 = text.charAt(i4);
            double total3 = total2 + computeTextWidth(font, "" + c3, 0.0d);
            if (total3 > availableWidth) {
                break;
            }
            leadingIndex = i4;
            if (Character.isWhitespace(c3)) {
                leadingWhitespace = leadingIndex;
            }
            int index2 = (text.length() - 1) - i4;
            char c4 = text.charAt(index2);
            total2 = total3 + computeTextWidth(font, "" + c4, 0.0d);
            if (total2 > availableWidth) {
                break;
            }
            trailingIndex = index2;
            if (Character.isWhitespace(c4)) {
                trailingWhitespace = trailingIndex;
            }
        }
        if (leadingIndex < 0) {
            return ellipsis;
        }
        if (style == OverrunStyle.CENTER_ELLIPSIS) {
            if (trailingIndex < 0) {
                return text.substring(0, leadingIndex + 1) + ellipsis;
            }
            return text.substring(0, leadingIndex + 1) + ellipsis + text.substring(trailingIndex);
        }
        boolean leadingIndexIsLastLetterInWord = Character.isWhitespace(text.charAt(leadingIndex + 1));
        int index3 = (leadingWhitespace == -1 || leadingIndexIsLastLetterInWord) ? leadingIndex + 1 : leadingWhitespace;
        String leading = text.substring(0, index3);
        if (trailingIndex < 0) {
            return leading + ellipsis;
        }
        boolean trailingIndexIsFirstLetterInWord = Character.isWhitespace(text.charAt(trailingIndex - 1));
        int index4 = (trailingWhitespace == -1 || trailingIndexIsFirstLetterInWord) ? trailingIndex : trailingWhitespace + 1;
        String trailing = text.substring(index4);
        return leading + ellipsis + trailing;
    }

    static String computeClippedWrappedText(Font font, String text, double width, double height, OverrunStyle truncationStyle, String ellipsisString, TextBoundsType boundsType) {
        int brInd;
        int brInd2;
        int brInd3;
        if (font == null) {
            throw new IllegalArgumentException("Must specify a font");
        }
        String ellipsis = truncationStyle == OverrunStyle.CLIP ? "" : ellipsisString;
        int eLen = ellipsis.length();
        double eWidth = computeTextWidth(font, ellipsis, 0.0d);
        double eHeight = computeTextHeight(font, ellipsis, 0.0d, boundsType);
        if (width < eWidth || height < eHeight) {
            return text;
        }
        helper.setText(text);
        helper.setFont(font);
        helper.setWrappingWidth((int) Math.ceil(width));
        helper.setBoundsType(boundsType);
        helper.setLineSpacing(0.0d);
        boolean leading = truncationStyle == OverrunStyle.LEADING_ELLIPSIS || truncationStyle == OverrunStyle.LEADING_WORD_ELLIPSIS;
        boolean center = truncationStyle == OverrunStyle.CENTER_ELLIPSIS || truncationStyle == OverrunStyle.CENTER_WORD_ELLIPSIS;
        boolean trailing = (leading || center) ? false : true;
        boolean wordTrim = truncationStyle == OverrunStyle.WORD_ELLIPSIS || truncationStyle == OverrunStyle.LEADING_WORD_ELLIPSIS || truncationStyle == OverrunStyle.CENTER_WORD_ELLIPSIS;
        String result = text;
        int len = result != null ? result.length() : 0;
        int centerLen = -1;
        Point2D centerPoint = null;
        if (center) {
            centerPoint = new Point2D((width - eWidth) / 2.0d, (height / 2.0d) - helper.getBaselineOffset());
        }
        Point2D endPoint = new Point2D(0.0d, height - helper.getBaselineOffset());
        int hit = helper.impl_hitTestChar(endPoint).getCharIndex();
        if (hit >= len) {
            helper.setBoundsType(TextBoundsType.LOGICAL);
            return text;
        }
        if (center) {
            hit = helper.impl_hitTestChar(centerPoint).getCharIndex();
        }
        if (hit > 0 && hit < len) {
            if (center || trailing) {
                int ind = hit;
                if (center) {
                    if (wordTrim) {
                        int brInd4 = lastBreakCharIndex(text, ind + 1);
                        if (brInd4 >= 0) {
                            ind = brInd4 + 1;
                        } else {
                            int brInd5 = firstBreakCharIndex(text, ind);
                            if (brInd5 >= 0) {
                                ind = brInd5 + 1;
                            }
                        }
                    }
                    centerLen = ind + eLen;
                }
                result = result.substring(0, ind) + ellipsis;
            }
            if (leading || center) {
                int ind2 = Math.max(0, (len - hit) - 10);
                if (ind2 > 0 && wordTrim) {
                    int brInd6 = lastBreakCharIndex(text, ind2 + 1);
                    if (brInd6 >= 0) {
                        ind2 = brInd6 + 1;
                    } else {
                        int brInd7 = firstBreakCharIndex(text, ind2);
                        if (brInd7 >= 0) {
                            ind2 = brInd7 + 1;
                        }
                    }
                }
                if (center) {
                    result = result + text.substring(ind2);
                } else {
                    result = ellipsis + text.substring(ind2);
                }
            }
            while (true) {
                helper.setText(result);
                int hit2 = helper.impl_hitTestChar(endPoint).getCharIndex();
                if (center && hit2 < centerLen) {
                    if (hit2 > 0 && result.charAt(hit2 - 1) == '\n') {
                        hit2--;
                    }
                    result = text.substring(0, hit2) + ellipsis;
                } else {
                    if (hit2 <= 0 || hit2 >= result.length()) {
                        break;
                    }
                    if (leading) {
                        int ind3 = eLen + 1;
                        if (wordTrim && (brInd = firstBreakCharIndex(result, ind3)) >= 0) {
                            ind3 = brInd + 1;
                        }
                        result = ellipsis + result.substring(ind3);
                    } else if (center) {
                        int ind4 = centerLen + 1;
                        if (wordTrim && (brInd2 = firstBreakCharIndex(result, ind4)) >= 0) {
                            ind4 = brInd2 + 1;
                        }
                        result = result.substring(0, centerLen) + result.substring(ind4);
                    } else {
                        int ind5 = (result.length() - eLen) - 1;
                        if (wordTrim && (brInd3 = lastBreakCharIndex(result, ind5)) >= 0) {
                            ind5 = brInd3;
                        }
                        result = result.substring(0, ind5) + ellipsis;
                    }
                }
            }
        }
        helper.setWrappingWidth(DEFAULT_WRAPPING_WIDTH);
        helper.setLineSpacing(DEFAULT_LINE_SPACING);
        helper.setText(DEFAULT_TEXT);
        helper.setBoundsType(DEFAULT_BOUNDS_TYPE);
        return result;
    }

    private static int firstBreakCharIndex(String str, int start) {
        char[] chars = str.toCharArray();
        for (int i2 = start; i2 < chars.length; i2++) {
            if (isPreferredBreakCharacter(chars[i2])) {
                return i2;
            }
        }
        return -1;
    }

    private static int lastBreakCharIndex(String str, int start) {
        char[] chars = str.toCharArray();
        for (int i2 = start; i2 >= 0; i2--) {
            if (isPreferredBreakCharacter(chars[i2])) {
                return i2;
            }
        }
        return -1;
    }

    private static boolean isPreferredBreakCharacter(char ch) {
        if (Character.isWhitespace(ch)) {
            return true;
        }
        switch (ch) {
            case '.':
            case ':':
            case ';':
                break;
        }
        return true;
    }

    private static boolean requiresComplexLayout(Font font, String string) {
        return false;
    }

    static int computeStartOfWord(Font font, String text, int index) {
        if ("".equals(text) || index < 0) {
            return 0;
        }
        if (text.length() <= index) {
            return text.length();
        }
        if (Character.isWhitespace(text.charAt(index))) {
            return index;
        }
        boolean complexLayout = requiresComplexLayout(font, text);
        if (complexLayout) {
            return 0;
        }
        int i2 = index;
        do {
            i2--;
            if (i2 < 0) {
                return 0;
            }
        } while (!Character.isWhitespace(text.charAt(i2)));
        return i2 + 1;
    }

    static int computeEndOfWord(Font font, String text, int index) {
        if (text.equals("") || index < 0) {
            return 0;
        }
        if (text.length() <= index) {
            return text.length();
        }
        if (Character.isWhitespace(text.charAt(index))) {
            return index;
        }
        boolean complexLayout = requiresComplexLayout(font, text);
        if (complexLayout) {
            return text.length();
        }
        int i2 = index;
        do {
            i2++;
            if (i2 >= text.length()) {
                return text.length();
            }
        } while (!Character.isWhitespace(text.charAt(i2)));
        return i2;
    }

    public static double boundedSize(double value, double min, double max) {
        return Math.min(Math.max(value, min), Math.max(min, max));
    }

    static void addMnemonics(ContextMenu popup, Scene scene) {
        addMnemonics(popup, scene, false);
    }

    static void addMnemonics(ContextMenu popup, Scene scene, boolean initialState) {
        if (!PlatformUtil.isMac()) {
            ContextMenuContent cmContent = (ContextMenuContent) popup.getSkin().getNode();
            for (int i2 = 0; i2 < popup.getItems().size(); i2++) {
                MenuItem menuitem = popup.getItems().get(i2);
                if (menuitem.isMnemonicParsing()) {
                    TextBinding bindings = new TextBinding(menuitem.getText());
                    int mnemonicIndex = bindings.getMnemonicIndex();
                    if (mnemonicIndex >= 0) {
                        KeyCombination mnemonicKeyCombo = bindings.getMnemonicKeyCombination();
                        Mnemonic myMnemonic = new Mnemonic(cmContent.getLabelAt(i2), mnemonicKeyCombo);
                        scene.addMnemonic(myMnemonic);
                        cmContent.getLabelAt(i2).impl_setShowMnemonics(initialState);
                    }
                }
            }
        }
    }

    static void removeMnemonics(ContextMenu popup, Scene scene) {
        if (!PlatformUtil.isMac()) {
            ContextMenuContent cmContent = (ContextMenuContent) popup.getSkin().getNode();
            for (int i2 = 0; i2 < popup.getItems().size(); i2++) {
                MenuItem menuitem = popup.getItems().get(i2);
                if (menuitem.isMnemonicParsing()) {
                    TextBinding bindings = new TextBinding(menuitem.getText());
                    int mnemonicIndex = bindings.getMnemonicIndex();
                    if (mnemonicIndex >= 0) {
                        KeyCombination mnemonicKeyCombo = bindings.getMnemonicKeyCombination();
                        ObservableList<Mnemonic> mnemonicsList = scene.getMnemonics().get(mnemonicKeyCombo);
                        if (mnemonicsList != null) {
                            for (int j2 = 0; j2 < mnemonicsList.size(); j2++) {
                                if (mnemonicsList.get(j2).getNode() == cmContent.getLabelAt(i2)) {
                                    mnemonicsList.remove(j2);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    static double computeXOffset(double width, double contentWidth, HPos hpos) {
        if (hpos == null) {
            return 0.0d;
        }
        switch (hpos) {
            case LEFT:
                return 0.0d;
            case CENTER:
                return (width - contentWidth) / 2.0d;
            case RIGHT:
                return width - contentWidth;
            default:
                return 0.0d;
        }
    }

    static double computeYOffset(double height, double contentHeight, VPos vpos) {
        if (vpos == null) {
            return 0.0d;
        }
        switch (vpos) {
            case TOP:
                return 0.0d;
            case CENTER:
                return (height - contentHeight) / 2.0d;
            case BOTTOM:
                return height - contentHeight;
            default:
                return 0.0d;
        }
    }

    public static boolean isTwoLevelFocus() {
        return Platform.isSupported(ConditionalFeature.TWO_LEVEL_FOCUS);
    }

    public static int getHitInsertionIndex(HitInfo hit, String text) {
        int charIndex = hit.getCharIndex();
        if (text != null && !hit.isLeading()) {
            if (charIterator == null) {
                charIterator = BreakIterator.getCharacterInstance();
            }
            charIterator.setText(text);
            int next = charIterator.following(charIndex);
            charIndex = next == -1 ? hit.getInsertionIndex() : next;
        }
        return charIndex;
    }

    public static <T> void executeOnceWhenPropertyIsNonNull(final ObservableValue<T> p2, final Consumer<T> consumer) {
        if (p2 == null) {
            return;
        }
        T value = p2.getValue2();
        if (value != null) {
            consumer.accept(value);
        } else {
            InvalidationListener listener = new InvalidationListener() { // from class: com.sun.javafx.scene.control.skin.Utils.1
                @Override // javafx.beans.InvalidationListener
                public void invalidated(Observable observable) {
                    Object value2 = p2.getValue2();
                    if (value2 != null) {
                        p2.removeListener(this);
                        consumer.accept(value2);
                    }
                }
            };
            p2.addListener(listener);
        }
    }

    public static URL getResource(String str) {
        return Utils.class.getResource(str);
    }
}
