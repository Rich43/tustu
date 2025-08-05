package com.sun.glass.ui.win;

import java.text.BreakIterator;
import javafx.geometry.Bounds;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleAttribute;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/* loaded from: jfxrt.jar:com/sun/glass/ui/win/WinTextRangeProvider.class */
class WinTextRangeProvider {
    private static final int TextPatternRangeEndpoint_Start = 0;
    private static final int TextPatternRangeEndpoint_End = 1;
    private static final int TextUnit_Character = 0;
    private static final int TextUnit_Format = 1;
    private static final int TextUnit_Word = 2;
    private static final int TextUnit_Line = 3;
    private static final int TextUnit_Paragraph = 4;
    private static final int TextUnit_Page = 5;
    private static final int TextUnit_Document = 6;
    private static final int UIA_FontNameAttributeId = 40005;
    private static final int UIA_FontSizeAttributeId = 40006;
    private static final int UIA_FontWeightAttributeId = 40007;
    private static final int UIA_IsHiddenAttributeId = 40013;
    private static final int UIA_IsItalicAttributeId = 40014;
    private static final int UIA_IsReadOnlyAttributeId = 40015;
    private static int idCount;
    private int id;
    private int start;
    private int end;
    private WinAccessible accessible;
    private long peer;

    private static native void _initIDs();

    private native long _createTextRangeProvider(long j2);

    private native void _destroyTextRangeProvider(long j2);

    static {
        _initIDs();
        idCount = 1;
    }

    WinTextRangeProvider(WinAccessible accessible) {
        this.accessible = accessible;
        this.peer = _createTextRangeProvider(accessible.getNativeAccessible());
        int i2 = idCount;
        idCount = i2 + 1;
        this.id = i2;
    }

    long getNativeProvider() {
        return this.peer;
    }

    void dispose() {
        _destroyTextRangeProvider(this.peer);
        this.peer = 0L;
    }

    private void validateRange(String text) {
        if (text == null) {
            this.end = 0;
            this.start = 0;
        } else {
            int length = text.length();
            this.start = Math.max(0, Math.min(this.start, length));
            this.end = Math.max(this.start, Math.min(this.end, length));
        }
    }

    void setRange(int start, int end) {
        this.start = start;
        this.end = end;
    }

    int getStart() {
        return this.start;
    }

    int getEnd() {
        return this.end;
    }

    public String toString() {
        return "Range(start: " + this.start + ", end: " + this.end + ", id: " + this.id + ")";
    }

    private Object getAttribute(AccessibleAttribute attribute, Object... parameters) {
        return this.accessible.getAttribute(attribute, parameters);
    }

    private boolean isWordStart(BreakIterator bi2, String text, int offset) {
        if (offset == 0 || offset >= text.length() || offset == -1) {
            return true;
        }
        return bi2.isBoundary(offset) && Character.isLetterOrDigit(text.charAt(offset));
    }

    private long Clone() {
        WinTextRangeProvider clone = new WinTextRangeProvider(this.accessible);
        clone.setRange(this.start, this.end);
        return clone.getNativeProvider();
    }

    private boolean Compare(WinTextRangeProvider range) {
        return range != null && this.accessible == range.accessible && this.start == range.start && this.end == range.end;
    }

    private int CompareEndpoints(int endpoint, WinTextRangeProvider targetRange, int targetEndpoint) {
        int offset = endpoint == 0 ? this.start : this.end;
        int targetOffset = targetEndpoint == 0 ? targetRange.start : targetRange.end;
        return offset - targetOffset;
    }

    private void ExpandToEnclosingUnit(int unit) {
        String text = (String) getAttribute(AccessibleAttribute.TEXT, new Object[0]);
        if (text == null) {
            return;
        }
        int length = text.length();
        if (length == 0) {
            return;
        }
        validateRange(text);
        switch (unit) {
            case 0:
                if (this.start == length) {
                    this.start--;
                }
                this.end = this.start + 1;
                break;
            case 1:
            case 2:
                BreakIterator bi2 = BreakIterator.getWordInstance();
                bi2.setText(text);
                if (!isWordStart(bi2, text, this.start)) {
                    int iPreceding = bi2.preceding(this.start);
                    while (true) {
                        int offset = iPreceding;
                        if (!isWordStart(bi2, text, offset)) {
                            iPreceding = bi2.previous();
                        } else {
                            this.start = offset != -1 ? offset : 0;
                        }
                    }
                }
                if (!isWordStart(bi2, text, this.end)) {
                    int iFollowing = bi2.following(this.end);
                    while (true) {
                        int offset2 = iFollowing;
                        if (!isWordStart(bi2, text, offset2)) {
                            iFollowing = bi2.next();
                        } else {
                            this.end = offset2 != -1 ? offset2 : length;
                            break;
                        }
                    }
                }
                break;
            case 3:
                Integer lineIndex = (Integer) getAttribute(AccessibleAttribute.LINE_FOR_OFFSET, Integer.valueOf(this.start));
                Integer lineStart = (Integer) getAttribute(AccessibleAttribute.LINE_START, lineIndex);
                Integer lineEnd = (Integer) getAttribute(AccessibleAttribute.LINE_END, lineIndex);
                if (lineIndex == null || lineEnd == null || lineStart == null) {
                    this.start = 0;
                    this.end = length;
                    break;
                } else {
                    this.start = lineStart.intValue();
                    this.end = lineEnd.intValue();
                    break;
                }
                break;
            case 4:
                if (((Integer) getAttribute(AccessibleAttribute.LINE_FOR_OFFSET, Integer.valueOf(this.start))) == null) {
                    this.start = 0;
                    this.end = length;
                    break;
                } else {
                    BreakIterator bi3 = BreakIterator.getSentenceInstance();
                    bi3.setText(text);
                    if (!bi3.isBoundary(this.start)) {
                        int offset3 = bi3.preceding(this.start);
                        this.start = offset3 != -1 ? offset3 : 0;
                    }
                    int offset4 = bi3.following(this.start);
                    this.end = offset4 != -1 ? offset4 : length;
                    break;
                }
            case 5:
            case 6:
                this.start = 0;
                this.end = length;
                break;
        }
        validateRange(text);
    }

    private long FindAttribute(int attributeId, WinVariant val, boolean backward) {
        System.err.println("FindAttribute NOT IMPLEMENTED");
        return 0L;
    }

    private long FindText(String text, boolean backward, boolean ignoreCase) {
        String documentText;
        int index;
        if (text == null || (documentText = (String) getAttribute(AccessibleAttribute.TEXT, new Object[0])) == null) {
            return 0L;
        }
        String rangeText = documentText.substring(this.start, this.end);
        if (ignoreCase) {
            rangeText = rangeText.toLowerCase();
            text = text.toLowerCase();
        }
        if (backward) {
            index = rangeText.lastIndexOf(text);
        } else {
            index = rangeText.indexOf(text);
        }
        if (index == -1) {
            return 0L;
        }
        WinTextRangeProvider result = new WinTextRangeProvider(this.accessible);
        result.setRange(this.start + index, this.start + index + text.length());
        return result.getNativeProvider();
    }

    private WinVariant GetAttributeValue(int attributeId) {
        WinVariant variant = null;
        switch (attributeId) {
            case UIA_FontNameAttributeId /* 40005 */:
                Font font = (Font) getAttribute(AccessibleAttribute.FONT, new Object[0]);
                if (font != null) {
                    variant = new WinVariant();
                    variant.vt = (short) 8;
                    variant.bstrVal = font.getName();
                    break;
                }
                break;
            case UIA_FontSizeAttributeId /* 40006 */:
                Font font2 = (Font) getAttribute(AccessibleAttribute.FONT, new Object[0]);
                if (font2 != null) {
                    variant = new WinVariant();
                    variant.vt = (short) 5;
                    variant.dblVal = font2.getSize();
                    break;
                }
                break;
            case UIA_FontWeightAttributeId /* 40007 */:
                Font font3 = (Font) getAttribute(AccessibleAttribute.FONT, new Object[0]);
                if (font3 != null) {
                    boolean bold = font3.getStyle().toLowerCase().contains("bold");
                    variant = new WinVariant();
                    variant.vt = (short) 3;
                    variant.lVal = bold ? FontWeight.BOLD.getWeight() : FontWeight.NORMAL.getWeight();
                    break;
                }
                break;
            case UIA_IsHiddenAttributeId /* 40013 */:
            case UIA_IsReadOnlyAttributeId /* 40015 */:
                variant = new WinVariant();
                variant.vt = (short) 11;
                variant.boolVal = false;
                break;
            case UIA_IsItalicAttributeId /* 40014 */:
                Font font4 = (Font) getAttribute(AccessibleAttribute.FONT, new Object[0]);
                if (font4 != null) {
                    boolean italic = font4.getStyle().toLowerCase().contains("italic");
                    variant = new WinVariant();
                    variant.vt = (short) 11;
                    variant.boolVal = italic;
                    break;
                }
                break;
        }
        return variant;
    }

    private double[] GetBoundingRectangles() {
        String text = (String) getAttribute(AccessibleAttribute.TEXT, new Object[0]);
        if (text == null) {
            return null;
        }
        int length = text.length();
        validateRange(text);
        if (length == 0) {
            return new double[0];
        }
        int endOffset = this.end;
        if (endOffset > 0 && endOffset > this.start && text.charAt(endOffset - 1) == '\n') {
            endOffset--;
        }
        if (endOffset > 0 && endOffset > this.start && text.charAt(endOffset - 1) == '\r') {
            endOffset--;
        }
        if (endOffset > 0 && endOffset > this.start && endOffset == length) {
            endOffset--;
        }
        Bounds[] bounds = (Bounds[]) getAttribute(AccessibleAttribute.BOUNDS_FOR_RANGE, Integer.valueOf(this.start), Integer.valueOf(endOffset));
        if (bounds != null) {
            double[] result = new double[bounds.length * 4];
            int index = 0;
            for (Bounds b2 : bounds) {
                int i2 = index;
                int index2 = index + 1;
                result[i2] = b2.getMinX();
                int index3 = index2 + 1;
                result[index2] = b2.getMinY();
                int index4 = index3 + 1;
                result[index3] = b2.getWidth();
                index = index4 + 1;
                result[index4] = b2.getHeight();
            }
            return result;
        }
        return null;
    }

    private long GetEnclosingElement() {
        return this.accessible.getNativeAccessible();
    }

    private String GetText(int maxLength) {
        String text = (String) getAttribute(AccessibleAttribute.TEXT, new Object[0]);
        if (text == null) {
            return null;
        }
        validateRange(text);
        int endOffset = maxLength != -1 ? Math.min(this.end, this.start + maxLength) : this.end;
        return text.substring(this.start, endOffset);
    }

    private int Move(int unit, int requestedCount) {
        String text;
        if (requestedCount == 0 || (text = (String) getAttribute(AccessibleAttribute.TEXT, new Object[0])) == null) {
            return 0;
        }
        int length = text.length();
        if (length == 0) {
            return 0;
        }
        int actualCount = 0;
        switch (unit) {
            case 0:
                int oldStart = this.start;
                this.start = Math.max(0, Math.min(this.start + requestedCount, length - 1));
                this.end = this.start + 1;
                actualCount = this.start - oldStart;
                break;
            case 1:
            case 2:
                BreakIterator bi2 = BreakIterator.getWordInstance();
                bi2.setText(text);
                int iPreceding = this.start;
                while (true) {
                    int offset = iPreceding;
                    if (!isWordStart(bi2, text, offset)) {
                        iPreceding = bi2.preceding(this.start);
                    } else {
                        while (offset != -1 && actualCount != requestedCount) {
                            if (requestedCount > 0) {
                                int iFollowing = bi2.following(offset);
                                while (true) {
                                    offset = iFollowing;
                                    if (!isWordStart(bi2, text, offset)) {
                                        iFollowing = bi2.next();
                                    } else {
                                        actualCount++;
                                    }
                                }
                            } else {
                                int iPreceding2 = bi2.preceding(offset);
                                while (true) {
                                    offset = iPreceding2;
                                    if (!isWordStart(bi2, text, offset)) {
                                        iPreceding2 = bi2.previous();
                                    } else {
                                        actualCount--;
                                    }
                                }
                            }
                        }
                        if (actualCount != 0) {
                            if (offset != -1) {
                                this.start = offset;
                            } else {
                                this.start = requestedCount > 0 ? length : 0;
                            }
                            int iFollowing2 = bi2.following(this.start);
                            while (true) {
                                int offset2 = iFollowing2;
                                if (!isWordStart(bi2, text, offset2)) {
                                    iFollowing2 = bi2.next();
                                } else {
                                    this.end = offset2 != -1 ? offset2 : length;
                                    break;
                                }
                            }
                        }
                    }
                }
                break;
            case 3:
                Integer lineIndex = (Integer) getAttribute(AccessibleAttribute.LINE_FOR_OFFSET, Integer.valueOf(this.start));
                if (lineIndex != null) {
                    int step = requestedCount > 0 ? 1 : -1;
                    while (requestedCount != actualCount && getAttribute(AccessibleAttribute.LINE_START, Integer.valueOf(lineIndex.intValue() + step)) != null) {
                        lineIndex = Integer.valueOf(lineIndex.intValue() + step);
                        actualCount += step;
                    }
                    if (actualCount != 0) {
                        Integer lineStart = (Integer) getAttribute(AccessibleAttribute.LINE_START, lineIndex);
                        Integer lineEnd = (Integer) getAttribute(AccessibleAttribute.LINE_END, lineIndex);
                        if (lineStart != null && lineEnd != null) {
                            this.start = lineStart.intValue();
                            this.end = lineEnd.intValue();
                            break;
                        } else {
                            return 0;
                        }
                    }
                } else {
                    return 0;
                }
                break;
            case 4:
                BreakIterator bi3 = BreakIterator.getSentenceInstance();
                bi3.setText(text);
                int offset3 = bi3.isBoundary(this.start) ? this.start : bi3.preceding(this.start);
                while (offset3 != -1 && actualCount != requestedCount) {
                    if (requestedCount > 0) {
                        offset3 = bi3.following(offset3);
                        actualCount++;
                    } else {
                        offset3 = bi3.preceding(offset3);
                        actualCount--;
                    }
                }
                if (actualCount != 0) {
                    this.start = offset3 != -1 ? offset3 : 0;
                    int offset4 = bi3.following(this.start);
                    this.end = offset4 != -1 ? offset4 : length;
                    break;
                }
                break;
            case 5:
            case 6:
                return 0;
        }
        validateRange(text);
        return actualCount;
    }

    private int MoveEndpointByUnit(int endpoint, int unit, int requestedCount) {
        String text;
        if (requestedCount == 0 || (text = (String) getAttribute(AccessibleAttribute.TEXT, new Object[0])) == null) {
            return 0;
        }
        int length = text.length();
        validateRange(text);
        int actualCount = 0;
        int offset = endpoint == 0 ? this.start : this.end;
        switch (unit) {
            case 0:
                offset = Math.max(0, Math.min(offset + requestedCount, length));
                actualCount = offset - offset;
                break;
            case 1:
            case 2:
                BreakIterator bi2 = BreakIterator.getWordInstance();
                bi2.setText(text);
                while (offset != -1 && actualCount != requestedCount) {
                    if (requestedCount > 0) {
                        int iFollowing = bi2.following(offset);
                        while (true) {
                            offset = iFollowing;
                            if (!isWordStart(bi2, text, offset)) {
                                iFollowing = bi2.next();
                            } else {
                                actualCount++;
                            }
                        }
                    } else {
                        int iPreceding = bi2.preceding(offset);
                        while (true) {
                            offset = iPreceding;
                            if (!isWordStart(bi2, text, offset)) {
                                iPreceding = bi2.previous();
                            } else {
                                actualCount--;
                            }
                        }
                    }
                }
                if (offset == -1) {
                    offset = requestedCount > 0 ? length : 0;
                    break;
                }
                break;
            case 3:
                Integer lineIndex = (Integer) getAttribute(AccessibleAttribute.LINE_FOR_OFFSET, Integer.valueOf(offset));
                Integer lineStart = (Integer) getAttribute(AccessibleAttribute.LINE_START, lineIndex);
                Integer lineEnd = (Integer) getAttribute(AccessibleAttribute.LINE_END, lineIndex);
                if (lineIndex == null || lineStart == null || lineEnd == null) {
                    offset = requestedCount > 0 ? length : 0;
                    break;
                } else {
                    int step = requestedCount > 0 ? 1 : -1;
                    int endOffset = (requestedCount > 0 ? lineEnd : lineStart).intValue();
                    if (offset != endOffset) {
                        actualCount = 0 + step;
                    }
                    while (requestedCount != actualCount && getAttribute(AccessibleAttribute.LINE_START, Integer.valueOf(lineIndex.intValue() + step)) != null) {
                        lineIndex = Integer.valueOf(lineIndex.intValue() + step);
                        actualCount += step;
                    }
                    if (actualCount != 0) {
                        Integer lineStart2 = (Integer) getAttribute(AccessibleAttribute.LINE_START, lineIndex);
                        Integer lineEnd2 = (Integer) getAttribute(AccessibleAttribute.LINE_END, lineIndex);
                        if (lineStart2 != null && lineEnd2 != null) {
                            offset = (requestedCount > 0 ? lineEnd2 : lineStart2).intValue();
                            break;
                        } else {
                            return 0;
                        }
                    }
                }
                break;
            case 4:
                BreakIterator bi3 = BreakIterator.getSentenceInstance();
                bi3.setText(text);
                while (offset != -1 && actualCount != requestedCount) {
                    if (requestedCount > 0) {
                        offset = bi3.following(offset);
                        actualCount++;
                    } else {
                        offset = bi3.preceding(offset);
                        actualCount--;
                    }
                }
                if (offset == -1) {
                    offset = requestedCount > 0 ? length : 0;
                    break;
                }
                break;
            case 5:
            case 6:
                return 0;
        }
        if (endpoint == 0) {
            this.start = offset;
        } else {
            this.end = offset;
        }
        if (this.start > this.end) {
            int i2 = offset;
            this.end = i2;
            this.start = i2;
        }
        validateRange(text);
        return actualCount;
    }

    private void MoveEndpointByRange(int endpoint, WinTextRangeProvider targetRange, int targetEndpoint) {
        String text = (String) getAttribute(AccessibleAttribute.TEXT, new Object[0]);
        if (text == null) {
            return;
        }
        text.length();
        int offset = targetEndpoint == 0 ? targetRange.start : targetRange.end;
        if (endpoint == 0) {
            this.start = offset;
        } else {
            this.end = offset;
        }
        if (this.start > this.end) {
            this.end = offset;
            this.start = offset;
        }
        validateRange(text);
    }

    private void Select() {
        this.accessible.executeAction(AccessibleAction.SET_TEXT_SELECTION, Integer.valueOf(this.start), Integer.valueOf(this.end));
    }

    private void AddToSelection() {
    }

    private void RemoveFromSelection() {
    }

    private void ScrollIntoView(boolean alignToTop) {
        this.accessible.executeAction(AccessibleAction.SHOW_TEXT_RANGE, Integer.valueOf(this.start), Integer.valueOf(this.end));
    }

    private long[] GetChildren() {
        return new long[0];
    }
}
