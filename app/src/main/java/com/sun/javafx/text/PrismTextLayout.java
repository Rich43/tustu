package com.sun.javafx.text;

import com.sun.javafx.font.CharToGlyphMapper;
import com.sun.javafx.font.FontResource;
import com.sun.javafx.font.FontStrike;
import com.sun.javafx.font.Metrics;
import com.sun.javafx.font.PGFont;
import com.sun.javafx.font.PrismFontFactory;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.RoundRectangle2D;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.Translate2D;
import com.sun.javafx.scene.text.GlyphList;
import com.sun.javafx.scene.text.HitInfo;
import com.sun.javafx.scene.text.TextLayout;
import com.sun.javafx.scene.text.TextSpan;
import java.text.Bidi;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.PathElement;

/* loaded from: jfxrt.jar:com/sun/javafx/text/PrismTextLayout.class */
public class PrismTextLayout implements TextLayout {
    private static final int X_MIN_INDEX = 0;
    private static final int Y_MIN_INDEX = 1;
    private static final int X_MAX_INDEX = 2;
    private static final int Y_MAX_INDEX = 3;
    private static final int MAX_STRING_SIZE = 256;
    private char[] text;
    private TextSpan[] spans;
    private PGFont font;
    private FontStrike strike;
    private Integer cacheKey;
    private TextLine[] lines;
    private TextRun[] runs;
    private int runCount;
    private RectBounds visualBounds;
    private float layoutWidth;
    private float layoutHeight;
    private float wrapWidth;
    private float spacing;
    private LayoutCache layoutCache;
    private Shape shape;
    private static final BaseTransform IDENTITY = BaseTransform.IDENTITY_TRANSFORM;
    private static final Hashtable<Integer, LayoutCache> stringCache = new Hashtable<>();
    private static final Object CACHE_SIZE_LOCK = new Object();
    private static int cacheSize = 0;
    private static final int MAX_CACHE_SIZE = PrismFontFactory.cacheLayoutSize;
    private BaseBounds logicalBounds = new RectBounds();
    private int flags = 262144;

    private void reset() {
        this.layoutCache = null;
        this.runs = null;
        this.flags &= -2048;
        relayout();
    }

    private void relayout() {
        this.logicalBounds.makeEmpty();
        this.visualBounds = null;
        this.layoutHeight = 0.0f;
        this.layoutWidth = 0.0f;
        this.flags &= -1665;
        this.lines = null;
        this.shape = null;
    }

    @Override // com.sun.javafx.scene.text.TextLayout
    public boolean setContent(TextSpan[] spans) {
        if (spans == null && this.spans == null) {
            return false;
        }
        if (spans != null && this.spans != null && spans.length == this.spans.length) {
            int i2 = 0;
            while (i2 < spans.length && spans[i2] == this.spans[i2]) {
                i2++;
            }
            if (i2 == spans.length) {
                return false;
            }
        }
        reset();
        this.spans = spans;
        this.font = null;
        this.strike = null;
        this.text = null;
        this.cacheKey = null;
        return true;
    }

    @Override // com.sun.javafx.scene.text.TextLayout
    public boolean setContent(String text, Object font) {
        int length;
        reset();
        this.spans = null;
        this.font = (PGFont) font;
        this.strike = ((PGFont) font).getStrike(IDENTITY);
        this.text = text.toCharArray();
        if (MAX_CACHE_SIZE > 0 && 0 < (length = text.length()) && length <= 256) {
            this.cacheKey = Integer.valueOf(text.hashCode() * this.strike.hashCode());
            return true;
        }
        return true;
    }

    @Override // com.sun.javafx.scene.text.TextLayout
    public boolean setDirection(int direction) {
        if ((this.flags & TextLayout.DIRECTION_MASK) == direction) {
            return false;
        }
        this.flags &= -15361;
        this.flags |= direction & TextLayout.DIRECTION_MASK;
        reset();
        return true;
    }

    @Override // com.sun.javafx.scene.text.TextLayout
    public boolean setBoundsType(int type) {
        if ((this.flags & 16384) == type) {
            return false;
        }
        this.flags &= -16385;
        this.flags |= type & 16384;
        reset();
        return true;
    }

    @Override // com.sun.javafx.scene.text.TextLayout
    public boolean setAlignment(int alignment) {
        int align = 262144;
        switch (alignment) {
            case 0:
                align = 262144;
                break;
            case 1:
                align = 524288;
                break;
            case 2:
                align = 1048576;
                break;
            case 3:
                align = 2097152;
                break;
        }
        if ((this.flags & TextLayout.ALIGN_MASK) == align) {
            return false;
        }
        if (align == 2097152 || (this.flags & 2097152) != 0) {
            reset();
        }
        this.flags &= -3932161;
        this.flags |= align;
        relayout();
        return true;
    }

    @Override // com.sun.javafx.scene.text.TextLayout
    public boolean setWrapWidth(float newWidth) {
        if (Float.isInfinite(newWidth)) {
            newWidth = 0.0f;
        }
        if (Float.isNaN(newWidth)) {
            newWidth = 0.0f;
        }
        float oldWidth = this.wrapWidth;
        this.wrapWidth = Math.max(0.0f, newWidth);
        boolean needsLayout = true;
        if (this.lines != null && oldWidth != 0.0f && newWidth != 0.0f && (this.flags & 262144) != 0) {
            if (newWidth > oldWidth) {
                if ((this.flags & 128) == 0) {
                    needsLayout = false;
                }
            } else if (newWidth >= this.layoutWidth) {
                needsLayout = false;
            }
        }
        if (needsLayout) {
            relayout();
        }
        return needsLayout;
    }

    @Override // com.sun.javafx.scene.text.TextLayout
    public boolean setLineSpacing(float spacing) {
        if (this.spacing == spacing) {
            return false;
        }
        this.spacing = spacing;
        relayout();
        return true;
    }

    private void ensureLayout() {
        if (this.lines == null) {
            layout();
        }
    }

    @Override // com.sun.javafx.scene.text.TextLayout
    public com.sun.javafx.scene.text.TextLine[] getLines() {
        ensureLayout();
        return this.lines;
    }

    @Override // com.sun.javafx.scene.text.TextLayout
    public GlyphList[] getRuns() {
        ensureLayout();
        GlyphList[] result = new GlyphList[this.runCount];
        int count = 0;
        for (int i2 = 0; i2 < this.lines.length; i2++) {
            GlyphList[] lineRuns = this.lines[i2].getRuns();
            int length = lineRuns.length;
            System.arraycopy(lineRuns, 0, result, count, length);
            count += length;
        }
        return result;
    }

    @Override // com.sun.javafx.scene.text.TextLayout
    public BaseBounds getBounds() {
        ensureLayout();
        return this.logicalBounds;
    }

    @Override // com.sun.javafx.scene.text.TextLayout
    public BaseBounds getBounds(TextSpan filter, BaseBounds bounds) {
        ensureLayout();
        float left = Float.POSITIVE_INFINITY;
        float top = Float.POSITIVE_INFINITY;
        float right = Float.NEGATIVE_INFINITY;
        float bottom = Float.NEGATIVE_INFINITY;
        if (filter != null) {
            for (int i2 = 0; i2 < this.lines.length; i2++) {
                TextLine line = this.lines[i2];
                TextRun[] lineRuns = line.getRuns();
                for (TextRun run : lineRuns) {
                    TextSpan span = run.getTextSpan();
                    if (span == filter) {
                        Point2D location = run.getLocation();
                        float runLeft = location.f11907x;
                        if (run.isLeftBearing()) {
                            runLeft += line.getLeftSideBearing();
                        }
                        float runRight = location.f11907x + run.getWidth();
                        if (run.isRightBearing()) {
                            runRight += line.getRightSideBearing();
                        }
                        float runTop = location.f11908y;
                        float runBottom = location.f11908y + line.getBounds().getHeight() + this.spacing;
                        if (runLeft < left) {
                            left = runLeft;
                        }
                        if (runTop < top) {
                            top = runTop;
                        }
                        if (runRight > right) {
                            right = runRight;
                        }
                        if (runBottom > bottom) {
                            bottom = runBottom;
                        }
                    }
                }
            }
        } else {
            bottom = 0.0f;
            top = 0.0f;
            for (int i3 = 0; i3 < this.lines.length; i3++) {
                TextLine line2 = this.lines[i3];
                RectBounds lineBounds = line2.getBounds();
                float lineLeft = lineBounds.getMinX() + line2.getLeftSideBearing();
                if (lineLeft < left) {
                    left = lineLeft;
                }
                float lineRight = lineBounds.getMaxX() + line2.getRightSideBearing();
                if (lineRight > right) {
                    right = lineRight;
                }
                bottom += lineBounds.getHeight();
            }
            if (isMirrored()) {
                float width = getMirroringWidth();
                float bearing = left;
                left = width - right;
                right = width - bearing;
            }
        }
        return bounds.deriveWithNewBounds(left, top, 0.0f, right, bottom, 0.0f);
    }

    @Override // com.sun.javafx.scene.text.TextLayout
    public PathElement[] getCaretShape(int offset, boolean isLeading, float x2, float y2) {
        float lineX;
        float lineY;
        float lineHeight;
        ensureLayout();
        int lineIndex = 0;
        int lineCount = getLineCount();
        while (lineIndex < lineCount - 1) {
            TextLine line = this.lines[lineIndex];
            int lineEnd = line.getStart() + line.getLength();
            if (lineEnd > offset) {
                break;
            }
            lineIndex++;
        }
        int sliptCaretOffset = -1;
        int level = 0;
        TextLine line2 = this.lines[lineIndex];
        TextRun[] runs = line2.getRuns();
        int runCount = runs.length;
        int runIndex = -1;
        int i2 = 0;
        while (true) {
            if (i2 >= runCount) {
                break;
            }
            TextRun run = runs[i2];
            int runStart = run.getStart();
            int runEnd = run.getEnd();
            if (runStart > offset || offset >= runEnd) {
                i2++;
            } else if (!run.isLinebreak()) {
                runIndex = i2;
            }
        }
        if (runIndex != -1) {
            TextRun run2 = runs[runIndex];
            int runStart2 = run2.getStart();
            Point2D location = run2.getLocation();
            lineX = location.f11907x + run2.getXAtOffset(offset - runStart2, isLeading);
            lineY = location.f11908y;
            lineHeight = line2.getBounds().getHeight();
            if (isLeading) {
                if (runIndex > 0 && offset == runStart2) {
                    level = run2.getLevel();
                    sliptCaretOffset = offset - 1;
                }
            } else {
                int runEnd2 = run2.getEnd();
                if (runIndex + 1 < runs.length && offset + 1 == runEnd2) {
                    level = run2.getLevel();
                    sliptCaretOffset = offset + 1;
                }
            }
        } else {
            int maxOffset = 0;
            int runIndex2 = 0;
            for (int i3 = 0; i3 < runCount; i3++) {
                TextRun run3 = runs[i3];
                if (run3.getStart() >= maxOffset && !run3.isLinebreak()) {
                    maxOffset = run3.getStart();
                    runIndex2 = i3;
                }
            }
            TextRun run4 = runs[runIndex2];
            Point2D location2 = run4.getLocation();
            lineX = location2.f11907x + (run4.isLeftToRight() ? run4.getWidth() : 0.0f);
            lineY = location2.f11908y;
            lineHeight = line2.getBounds().getHeight();
        }
        if (isMirrored()) {
            lineX = getMirroringWidth() - lineX;
        }
        float lineX2 = lineX + x2;
        float lineY2 = lineY + y2;
        if (sliptCaretOffset != -1) {
            for (TextRun run5 : runs) {
                int runStart3 = run5.getStart();
                int runEnd3 = run5.getEnd();
                if (runStart3 <= sliptCaretOffset && sliptCaretOffset < runEnd3 && (run5.getLevel() & 1) != (level & 1)) {
                    float lineX22 = run5.getLocation().f11907x;
                    if (isLeading) {
                        if ((level & 1) != 0) {
                            lineX22 += run5.getWidth();
                        }
                    } else if ((level & 1) == 0) {
                        lineX22 += run5.getWidth();
                    }
                    if (isMirrored()) {
                        lineX22 = getMirroringWidth() - lineX22;
                    }
                    float lineX23 = lineX22 + x2;
                    PathElement[] result = {new MoveTo(lineX2, lineY2), new LineTo(lineX2, lineY2 + (lineHeight / 2.0f)), new MoveTo(lineX23, lineY2 + (lineHeight / 2.0f)), new LineTo(lineX23, lineY2 + lineHeight)};
                    return result;
                }
            }
        }
        PathElement[] result2 = {new MoveTo(lineX2, lineY2), new LineTo(lineX2, lineY2 + lineHeight)};
        return result2;
    }

    @Override // com.sun.javafx.scene.text.TextLayout
    public HitInfo getHitInfo(float x2, float y2) {
        ensureLayout();
        HitInfo info = new HitInfo();
        int lineIndex = getLineIndex(y2);
        if (lineIndex >= getLineCount()) {
            info.setCharIndex(getCharCount());
        } else {
            if (isMirrored()) {
                x2 = getMirroringWidth() - x2;
            }
            TextLine line = this.lines[lineIndex];
            TextRun[] runs = line.getRuns();
            RectBounds bounds = line.getBounds();
            TextRun run = null;
            float x3 = x2 - bounds.getMinX();
            for (int i2 = 0; i2 < runs.length; i2++) {
                run = runs[i2];
                if (x3 < run.getWidth()) {
                    break;
                }
                if (i2 + 1 < runs.length) {
                    if (runs[i2 + 1].isLinebreak()) {
                        break;
                    }
                    x3 -= run.getWidth();
                }
            }
            if (run != null) {
                int[] trailing = new int[1];
                info.setCharIndex(run.getStart() + run.getOffsetAtX(x3, trailing));
                info.setLeading(trailing[0] == 0);
            } else {
                info.setCharIndex(line.getStart());
                info.setLeading(true);
            }
        }
        return info;
    }

    @Override // com.sun.javafx.scene.text.TextLayout
    public PathElement[] getRange(int start, int end, int type, float x2, float y2) {
        float f2;
        float height;
        float f3;
        float runLeft;
        float runRight;
        FontStrike fontStrike;
        ensureLayout();
        int lineCount = getLineCount();
        ArrayList<PathElement> result = new ArrayList<>();
        float lineY = 0.0f;
        for (int lineIndex = 0; lineIndex < lineCount; lineIndex++) {
            TextLine line = this.lines[lineIndex];
            RectBounds lineBounds = line.getBounds();
            int lineStart = line.getStart();
            if (lineStart < end) {
                int lineEnd = lineStart + line.getLength();
                if (start > lineEnd) {
                    f2 = lineY;
                    height = lineBounds.getHeight();
                    f3 = this.spacing;
                } else {
                    TextRun[] runs = line.getRuns();
                    int count = Math.min(lineEnd, end) - Math.max(lineStart, start);
                    float left = -1.0f;
                    float right = -1.0f;
                    float lineX = lineBounds.getMinX();
                    for (int runIndex = 0; count > 0 && runIndex < runs.length; runIndex++) {
                        TextRun run = runs[runIndex];
                        int runStart = run.getStart();
                        int runEnd = run.getEnd();
                        float runWidth = run.getWidth();
                        int clmapStart = Math.max(runStart, Math.min(start, runEnd));
                        int clampEnd = Math.max(runStart, Math.min(end, runEnd));
                        int runCount = clampEnd - clmapStart;
                        if (runCount != 0) {
                            boolean ltr = run.isLeftToRight();
                            if (runStart > start) {
                                runLeft = ltr ? lineX : lineX + runWidth;
                            } else {
                                runLeft = lineX + run.getXAtOffset(start - runStart, true);
                            }
                            if (runEnd < end) {
                                runRight = ltr ? lineX + runWidth : lineX;
                            } else {
                                runRight = lineX + run.getXAtOffset(end - runStart, true);
                            }
                            if (runLeft > runRight) {
                                float tmp = runLeft;
                                runLeft = runRight;
                                runRight = tmp;
                            }
                            count -= runCount;
                            float top = 0.0f;
                            float bottom = 0.0f;
                            switch (type) {
                                case 1:
                                    top = lineY;
                                    bottom = lineY + lineBounds.getHeight();
                                    break;
                                case 2:
                                case 4:
                                    if (this.spans != null) {
                                        TextSpan span = run.getTextSpan();
                                        PGFont font = (PGFont) span.getFont();
                                        if (font != null) {
                                            fontStrike = font.getStrike(IDENTITY);
                                        }
                                    } else {
                                        fontStrike = this.strike;
                                    }
                                    float top2 = lineY - run.getAscent();
                                    Metrics metrics = fontStrike.getMetrics();
                                    if (type == 2) {
                                        top = top2 + metrics.getUnderLineOffset();
                                        bottom = top + metrics.getUnderLineThickness();
                                        break;
                                    } else {
                                        top = top2 + metrics.getStrikethroughOffset();
                                        bottom = top + metrics.getStrikethroughThickness();
                                        break;
                                    }
                            }
                            if (runLeft != right) {
                                if (left != -1.0f && right != -1.0f) {
                                    float l2 = left;
                                    float r2 = right;
                                    if (isMirrored()) {
                                        float width = getMirroringWidth();
                                        l2 = width - l2;
                                        r2 = width - r2;
                                    }
                                    result.add(new MoveTo(x2 + l2, y2 + top));
                                    result.add(new LineTo(x2 + r2, y2 + top));
                                    result.add(new LineTo(x2 + r2, y2 + bottom));
                                    result.add(new LineTo(x2 + l2, y2 + bottom));
                                    result.add(new LineTo(x2 + l2, y2 + top));
                                }
                                left = runLeft;
                            }
                            right = runRight;
                            if (count == 0) {
                                float l3 = left;
                                float r3 = right;
                                if (isMirrored()) {
                                    float width2 = getMirroringWidth();
                                    l3 = width2 - l3;
                                    r3 = width2 - r3;
                                }
                                result.add(new MoveTo(x2 + l3, y2 + top));
                                result.add(new LineTo(x2 + r3, y2 + top));
                                result.add(new LineTo(x2 + r3, y2 + bottom));
                                result.add(new LineTo(x2 + l3, y2 + bottom));
                                result.add(new LineTo(x2 + l3, y2 + top));
                            }
                        }
                        lineX += runWidth;
                    }
                    f2 = lineY;
                    height = lineBounds.getHeight();
                    f3 = this.spacing;
                }
                lineY = f2 + height + f3;
            } else {
                return (PathElement[]) result.toArray(new PathElement[result.size()]);
            }
        }
        return (PathElement[]) result.toArray(new PathElement[result.size()]);
    }

    @Override // com.sun.javafx.scene.text.TextLayout
    public Shape getShape(int type, TextSpan filter) {
        FontStrike fontStrike;
        PGFont font;
        ensureLayout();
        boolean text = (type & 1) != 0;
        boolean underline = (type & 2) != 0;
        boolean strikethrough = (type & 4) != 0;
        boolean baselineType = (type & 8) != 0;
        if (this.shape != null && text && !underline && !strikethrough && baselineType) {
            return this.shape;
        }
        Path2D outline = new Path2D();
        BaseTransform tx = new Translate2D(0.0d, 0.0d);
        float firstBaseline = 0.0f;
        if (baselineType) {
            firstBaseline = -this.lines[0].getBounds().getMinY();
        }
        for (int i2 = 0; i2 < this.lines.length; i2++) {
            TextLine line = this.lines[i2];
            TextRun[] runs = line.getRuns();
            RectBounds bounds = line.getBounds();
            float baseline = -bounds.getMinY();
            for (TextRun run : runs) {
                if (this.spans != null) {
                    TextSpan span = run.getTextSpan();
                    if ((filter == null || span == filter) && (font = (PGFont) span.getFont()) != null) {
                        fontStrike = font.getStrike(IDENTITY);
                    }
                } else {
                    fontStrike = this.strike;
                }
                Point2D location = run.getLocation();
                float runX = location.f11907x;
                float runY = (location.f11908y + baseline) - firstBaseline;
                Metrics metrics = null;
                if (underline || strikethrough) {
                    metrics = fontStrike.getMetrics();
                }
                if (underline) {
                    RoundRectangle2D rect = new RoundRectangle2D();
                    rect.f11924x = runX;
                    rect.f11925y = runY + metrics.getUnderLineOffset();
                    rect.width = run.getWidth();
                    rect.height = metrics.getUnderLineThickness();
                    outline.append((Shape) rect, false);
                }
                if (strikethrough) {
                    RoundRectangle2D rect2 = new RoundRectangle2D();
                    rect2.f11924x = runX;
                    rect2.f11925y = runY + metrics.getStrikethroughOffset();
                    rect2.width = run.getWidth();
                    rect2.height = metrics.getStrikethroughThickness();
                    outline.append((Shape) rect2, false);
                }
                if (text && run.getGlyphCount() > 0) {
                    tx.restoreTransform(1.0d, 0.0d, 0.0d, 1.0d, runX, runY);
                    Path2D path = (Path2D) fontStrike.getOutline(run, tx);
                    outline.append((Shape) path, false);
                }
            }
        }
        if (text && !underline && !strikethrough) {
            this.shape = outline;
        }
        return outline;
    }

    private int getLineIndex(float y2) {
        int index = 0;
        float bottom = 0.0f;
        int lineCount = getLineCount();
        while (index < lineCount) {
            bottom += this.lines[index].getBounds().getHeight() + this.spacing;
            if (index + 1 == lineCount) {
                bottom -= this.lines[index].getLeading();
            }
            if (bottom > y2) {
                break;
            }
            index++;
        }
        return index;
    }

    private boolean copyCache() {
        int align = this.flags & TextLayout.ALIGN_MASK;
        int boundsType = this.flags & 16384;
        return this.wrapWidth != 0.0f || align != 262144 || boundsType == 0 || isMirrored();
    }

    private void initCache() {
        LayoutCache cache;
        if (this.cacheKey != null) {
            if (this.layoutCache == null && (cache = stringCache.get(this.cacheKey)) != null && cache.font.equals(this.font) && Arrays.equals(cache.text, this.text)) {
                this.layoutCache = cache;
                this.runs = cache.runs;
                this.runCount = cache.runCount;
                this.flags |= cache.analysis;
            }
            if (this.layoutCache != null) {
                if (copyCache()) {
                    if (this.layoutCache.runs == this.runs) {
                        this.runs = new TextRun[this.runCount];
                        System.arraycopy(this.layoutCache.runs, 0, this.runs, 0, this.runCount);
                        return;
                    }
                    return;
                }
                if (this.layoutCache.lines != null) {
                    this.runs = this.layoutCache.runs;
                    this.runCount = this.layoutCache.runCount;
                    this.flags |= this.layoutCache.analysis;
                    this.lines = this.layoutCache.lines;
                    this.layoutWidth = this.layoutCache.layoutWidth;
                    this.layoutHeight = this.layoutCache.layoutHeight;
                    float ascent = this.lines[0].getBounds().getMinY();
                    this.logicalBounds = this.logicalBounds.deriveWithNewBounds(0.0f, ascent, 0.0f, this.layoutWidth, this.layoutHeight + ascent, 0.0f);
                }
            }
        }
    }

    private int getLineCount() {
        return this.lines.length;
    }

    private int getCharCount() {
        if (this.text != null) {
            return this.text.length;
        }
        int count = 0;
        for (int i2 = 0; i2 < this.lines.length; i2++) {
            count += this.lines[i2].getLength();
        }
        return count;
    }

    public TextSpan[] getTextSpans() {
        return this.spans;
    }

    public PGFont getFont() {
        return this.font;
    }

    public int getDirection() {
        if ((this.flags & 1024) != 0) {
            return 0;
        }
        if ((this.flags & 2048) != 0) {
            return 1;
        }
        if ((this.flags & 4096) == 0 && (this.flags & 8192) != 0) {
            return -1;
        }
        return -2;
    }

    public void addTextRun(TextRun run) {
        if (this.runCount + 1 > this.runs.length) {
            TextRun[] newRuns = new TextRun[this.runs.length + 64];
            System.arraycopy(this.runs, 0, newRuns, 0, this.runs.length);
            this.runs = newRuns;
        }
        TextRun[] textRunArr = this.runs;
        int i2 = this.runCount;
        this.runCount = i2 + 1;
        textRunArr[i2] = run;
    }

    private void buildRuns(char[] chars) {
        this.runCount = 0;
        if (this.runs == null) {
            int count = Math.max(4, Math.min(chars.length / 16, 16));
            this.runs = new TextRun[count];
        }
        GlyphLayout layout = GlyphLayout.getInstance();
        this.flags = layout.breakRuns(this, chars, this.flags);
        layout.dispose();
        for (int j2 = this.runCount; j2 < this.runs.length; j2++) {
            this.runs[j2] = null;
        }
    }

    private void shape(TextRun run, char[] chars, GlyphLayout layout) {
        PGFont font;
        FontStrike strike;
        if (this.spans != null) {
            if (this.spans.length == 0) {
                return;
            }
            TextSpan span = run.getTextSpan();
            font = (PGFont) span.getFont();
            if (font == null) {
                RectBounds bounds = span.getBounds();
                run.setEmbedded(bounds, span.getText().length());
                return;
            }
            strike = font.getStrike(IDENTITY);
        } else {
            font = this.font;
            strike = this.strike;
        }
        if (run.getAscent() == 0.0f) {
            Metrics m2 = strike.getMetrics();
            if ((this.flags & 16384) == 16384) {
                float ascent = m2.getAscent();
                if (font.getFamilyName().equals("Segoe UI")) {
                    ascent = (float) (ascent * 0.8d);
                }
                float ascent2 = (int) (ascent - 0.75d);
                float descent = (int) (m2.getDescent() + 0.75d);
                float leading = (int) (m2.getLineGap() + 0.75d);
                float capHeight = (int) (m2.getCapHeight() + 0.75d);
                float topPadding = (-ascent2) - capHeight;
                if (topPadding > descent) {
                    descent = topPadding;
                } else {
                    ascent2 += topPadding - descent;
                }
                run.setMetrics(ascent2, descent, leading);
            } else {
                run.setMetrics(m2.getAscent(), m2.getDescent(), m2.getLineGap());
            }
        }
        if (run.isTab() || run.isLinebreak() || run.getGlyphCount() > 0) {
            return;
        }
        if (run.isComplex()) {
            layout.layout(run, font, strike, chars);
            return;
        }
        FontResource fr = strike.getFontResource();
        int start = run.getStart();
        int length = run.getLength();
        if (this.layoutCache == null) {
            float fontSize = strike.getSize();
            CharToGlyphMapper mapper = fr.getGlyphMapper();
            int[] glyphs = new int[length];
            mapper.charsToGlyphs(start, length, chars, glyphs);
            float[] positions = new float[(length + 1) << 1];
            float xadvance = 0.0f;
            for (int i2 = 0; i2 < length; i2++) {
                float width = fr.getAdvance(glyphs[i2], fontSize);
                positions[i2 << 1] = xadvance;
                xadvance += width;
            }
            positions[length << 1] = xadvance;
            run.shape(length, glyphs, positions, null);
            return;
        }
        if (!this.layoutCache.valid) {
            float fontSize2 = strike.getSize();
            CharToGlyphMapper mapper2 = fr.getGlyphMapper();
            mapper2.charsToGlyphs(start, length, chars, this.layoutCache.glyphs, start);
            int end = start + length;
            float width2 = 0.0f;
            for (int i3 = start; i3 < end; i3++) {
                float adv = fr.getAdvance(this.layoutCache.glyphs[i3], fontSize2);
                this.layoutCache.advances[i3] = adv;
                width2 += adv;
            }
            run.setWidth(width2);
        }
        run.shape(length, this.layoutCache.glyphs, this.layoutCache.advances);
    }

    private TextLine createLine(int start, int end, int startOffset) {
        int count = (end - start) + 1;
        TextRun[] lineRuns = new TextRun[count];
        if (start < this.runCount) {
            System.arraycopy(this.runs, start, lineRuns, 0, count);
        }
        float width = 0.0f;
        float ascent = 0.0f;
        float descent = 0.0f;
        float leading = 0.0f;
        int length = 0;
        for (TextRun run : lineRuns) {
            width += run.getWidth();
            ascent = Math.min(ascent, run.getAscent());
            descent = Math.max(descent, run.getDescent());
            leading = Math.max(leading, run.getLeading());
            length += run.getLength();
        }
        if (width > this.layoutWidth) {
            this.layoutWidth = width;
        }
        return new TextLine(startOffset, length, lineRuns, width, ascent, descent, leading);
    }

    private void reorderLine(TextLine line) {
        TextRun[] runs = line.getRuns();
        int length = runs.length;
        if (length > 0 && runs[length - 1].isLinebreak()) {
            length--;
        }
        if (length < 2) {
            return;
        }
        byte[] levels = new byte[length];
        for (int i2 = 0; i2 < length; i2++) {
            levels[i2] = runs[i2].getLevel();
        }
        Bidi.reorderVisually(levels, 0, runs, 0, length);
    }

    private char[] getText() {
        if (this.text == null) {
            int count = 0;
            for (int i2 = 0; i2 < this.spans.length; i2++) {
                count += this.spans[i2].getText().length();
            }
            this.text = new char[count];
            int offset = 0;
            for (int i3 = 0; i3 < this.spans.length; i3++) {
                String string = this.spans[i3].getText();
                int length = string.length();
                string.getChars(0, length, this.text, offset);
                offset += length;
            }
        }
        return this.text;
    }

    private boolean isSimpleLayout() {
        int textAlignment = this.flags & TextLayout.ALIGN_MASK;
        boolean justify = this.wrapWidth > 0.0f && textAlignment == 2097152;
        return (this.flags & 24) == 0 && !justify;
    }

    private boolean isMirrored() {
        boolean mirrored = false;
        switch (this.flags & TextLayout.DIRECTION_MASK) {
            case 1024:
                mirrored = false;
                break;
            case 2048:
                mirrored = true;
                break;
            case 4096:
            case 8192:
                mirrored = (this.flags & 256) != 0;
                break;
        }
        return mirrored;
    }

    private float getMirroringWidth() {
        return this.wrapWidth != 0.0f ? this.wrapWidth : this.layoutWidth;
    }

    private void reuseRuns() {
        TextRun run;
        TextRun nextRun;
        this.runCount = 0;
        int index = 0;
        while (index < this.runs.length && (run = this.runs[index]) != null) {
            this.runs[index] = null;
            index++;
            TextRun[] textRunArr = this.runs;
            int i2 = this.runCount;
            this.runCount = i2 + 1;
            TextRun run2 = run.unwrap();
            textRunArr[i2] = run2;
            if (run2.isSplit()) {
                run2.merge(null);
                while (index < this.runs.length && (nextRun = this.runs[index]) != null) {
                    run2.merge(nextRun);
                    this.runs[index] = null;
                    index++;
                    if (nextRun.isSplitLast()) {
                        break;
                    }
                }
            }
        }
    }

    private float getTabAdvance() {
        float spaceAdvance = 0.0f;
        if (this.spans != null) {
            int i2 = 0;
            while (true) {
                if (i2 >= this.spans.length) {
                    break;
                }
                TextSpan span = this.spans[i2];
                PGFont font = (PGFont) span.getFont();
                if (font == null) {
                    i2++;
                } else {
                    FontStrike strike = font.getStrike(IDENTITY);
                    spaceAdvance = strike.getCharAdvance(' ');
                    break;
                }
            }
        } else {
            spaceAdvance = this.strike.getCharAdvance(' ');
        }
        return 8.0f * spaceAdvance;
    }

    private void layout() {
        float align;
        float height;
        TextRun[] lineRuns;
        int lineRunCount;
        initCache();
        if (this.lines != null) {
            return;
        }
        char[] chars = getText();
        if ((this.flags & 2) != 0 && isSimpleLayout()) {
            reuseRuns();
        } else {
            buildRuns(chars);
        }
        GlyphLayout layout = null;
        if ((this.flags & 16) != 0) {
            layout = GlyphLayout.getInstance();
        }
        float tabAdvance = 0.0f;
        if ((this.flags & 4) != 0) {
            tabAdvance = getTabAdvance();
        }
        BreakIterator boundary = null;
        if (this.wrapWidth > 0.0f && (this.flags & 80) != 0) {
            boundary = BreakIterator.getLineInstance();
            boundary.setText(new CharArrayIterator(chars));
        }
        int textAlignment = this.flags & TextLayout.ALIGN_MASK;
        if (isSimpleLayout()) {
            if (this.layoutCache == null) {
                this.layoutCache = new LayoutCache();
                this.layoutCache.glyphs = new int[chars.length];
                this.layoutCache.advances = new float[chars.length];
            }
        } else {
            this.layoutCache = null;
        }
        float lineWidth = 0.0f;
        int startIndex = 0;
        int startOffset = 0;
        ArrayList<TextLine> linesList = new ArrayList<>();
        int i2 = 0;
        while (i2 < this.runCount) {
            TextRun run = this.runs[i2];
            shape(run, chars, layout);
            if (run.isTab()) {
                float tabStop = (((int) (lineWidth / tabAdvance)) + 1) * tabAdvance;
                run.setWidth(tabStop - lineWidth);
            }
            float runWidth = run.getWidth();
            if (this.wrapWidth > 0.0f && lineWidth + runWidth > this.wrapWidth && !run.isLinebreak()) {
                int hitOffset = run.getStart() + run.getWrapIndex(this.wrapWidth - lineWidth);
                int offset = hitOffset;
                int runEnd = run.getEnd();
                if (offset + 1 < runEnd && chars[offset] == ' ') {
                    offset++;
                }
                int breakOffset = offset;
                if (boundary != null) {
                    breakOffset = (boundary.isBoundary(offset) || chars[offset] == '\t') ? offset : boundary.preceding(offset);
                } else {
                    boolean currentChar = Character.isWhitespace(chars[breakOffset]);
                    while (breakOffset > startOffset) {
                        boolean previousChar = Character.isWhitespace(chars[breakOffset - 1]);
                        if (!currentChar && previousChar) {
                            break;
                        }
                        currentChar = previousChar;
                        breakOffset--;
                    }
                }
                if (breakOffset < startOffset) {
                    breakOffset = startOffset;
                }
                int breakRunIndex = startIndex;
                TextRun breakRun = null;
                while (breakRunIndex < this.runCount) {
                    breakRun = this.runs[breakRunIndex];
                    if (breakRun.getEnd() > breakOffset) {
                        break;
                    } else {
                        breakRunIndex++;
                    }
                }
                if (breakOffset == startOffset) {
                    breakRun = run;
                    breakRunIndex = i2;
                    breakOffset = hitOffset;
                }
                int breakOffsetInRun = breakOffset - breakRun.getStart();
                if (breakOffsetInRun == 0 && breakRunIndex != startIndex) {
                    i2 = breakRunIndex - 1;
                } else {
                    i2 = breakRunIndex;
                    if (breakOffsetInRun == 0) {
                        breakOffsetInRun++;
                    }
                    if (breakOffsetInRun < breakRun.getLength()) {
                        if (this.runCount >= this.runs.length) {
                            TextRun[] newRuns = new TextRun[this.runs.length + 64];
                            System.arraycopy(this.runs, 0, newRuns, 0, i2 + 1);
                            System.arraycopy(this.runs, i2 + 1, newRuns, i2 + 2, (this.runs.length - i2) - 1);
                            this.runs = newRuns;
                        } else {
                            System.arraycopy(this.runs, i2 + 1, this.runs, i2 + 2, (this.runCount - i2) - 1);
                        }
                        this.runs[i2 + 1] = breakRun.split(breakOffsetInRun);
                        if (breakRun.isComplex()) {
                            shape(breakRun, chars, layout);
                        }
                        this.runCount++;
                    }
                }
                if (i2 + 1 < this.runCount && !this.runs[i2 + 1].isLinebreak()) {
                    run = this.runs[i2];
                    run.setSoftbreak();
                    this.flags |= 128;
                }
            }
            lineWidth += runWidth;
            if (run.isBreak()) {
                TextLine line = createLine(startIndex, i2, startOffset);
                linesList.add(line);
                startIndex = i2 + 1;
                startOffset += line.getLength();
                lineWidth = 0.0f;
            }
            i2++;
        }
        if (layout != null) {
            layout.dispose();
        }
        linesList.add(createLine(startIndex, this.runCount - 1, startOffset));
        this.lines = new TextLine[linesList.size()];
        linesList.toArray(this.lines);
        float fullWidth = Math.max(this.wrapWidth, this.layoutWidth);
        float lineY = 0.0f;
        if (isMirrored()) {
            align = 1.0f;
            if (textAlignment == 1048576) {
                align = 0.0f;
            }
        } else {
            align = 0.0f;
            if (textAlignment == 1048576) {
                align = 1.0f;
            }
        }
        if (textAlignment == 524288) {
            align = 0.5f;
        }
        for (int i3 = 0; i3 < this.lines.length; i3++) {
            TextLine line2 = this.lines[i3];
            int lineStart = line2.getStart();
            RectBounds bounds = line2.getBounds();
            float lineX = (fullWidth - bounds.getWidth()) * align;
            line2.setAlignment(lineX);
            boolean justify = this.wrapWidth > 0.0f && textAlignment == 2097152;
            if (justify && (lineRunCount = (lineRuns = line2.getRuns()).length) > 0 && lineRuns[lineRunCount - 1].isSoftbreak()) {
                int lineEnd = lineStart + line2.getLength();
                int wsCount = 0;
                boolean hitChar = false;
                for (int j2 = lineEnd - 1; j2 >= lineStart; j2--) {
                    if (!hitChar && chars[j2] != ' ') {
                        hitChar = true;
                    }
                    if (hitChar && chars[j2] == ' ') {
                        wsCount++;
                    }
                }
                if (wsCount != 0) {
                    float inc = (fullWidth - bounds.getWidth()) / wsCount;
                    for (int j3 = 0; j3 < lineRunCount; j3++) {
                        TextRun textRun = lineRuns[j3];
                        int runStart = textRun.getStart();
                        int runEnd2 = textRun.getEnd();
                        for (int k2 = runStart; k2 < runEnd2; k2++) {
                            if (chars[k2] == ' ') {
                                textRun.justify(k2 - runStart, inc);
                                wsCount--;
                                if (wsCount == 0) {
                                    break;
                                }
                            }
                        }
                    }
                    lineX = 0.0f;
                    line2.setAlignment(0.0f);
                    line2.setWidth(fullWidth);
                }
            }
            if ((this.flags & 8) != 0) {
                reorderLine(line2);
            }
            computeSideBearings(line2);
            float runX = lineX;
            TextRun[] lineRuns2 = line2.getRuns();
            for (TextRun run2 : lineRuns2) {
                run2.setLocation(runX, lineY);
                run2.setLine(line2);
                runX += run2.getWidth();
            }
            if (i3 + 1 < this.lines.length) {
                height = Math.max(lineY, lineY + bounds.getHeight() + this.spacing);
            } else {
                height = lineY + (bounds.getHeight() - line2.getLeading());
            }
            lineY = height;
        }
        float ascent = this.lines[0].getBounds().getMinY();
        this.layoutHeight = lineY;
        this.logicalBounds = this.logicalBounds.deriveWithNewBounds(0.0f, ascent, 0.0f, this.layoutWidth, this.layoutHeight + ascent, 0.0f);
        if (this.layoutCache != null) {
            if (this.cacheKey != null && !this.layoutCache.valid && !copyCache()) {
                this.layoutCache.font = this.font;
                this.layoutCache.text = this.text;
                this.layoutCache.runs = this.runs;
                this.layoutCache.runCount = this.runCount;
                this.layoutCache.lines = this.lines;
                this.layoutCache.layoutWidth = this.layoutWidth;
                this.layoutCache.layoutHeight = this.layoutHeight;
                this.layoutCache.analysis = this.flags & 2047;
                synchronized (CACHE_SIZE_LOCK) {
                    int charCount = chars.length;
                    if (cacheSize + charCount > MAX_CACHE_SIZE) {
                        stringCache.clear();
                        cacheSize = 0;
                    }
                    stringCache.put(this.cacheKey, this.layoutCache);
                    cacheSize += charCount;
                }
            }
            this.layoutCache.valid = true;
        }
    }

    @Override // com.sun.javafx.scene.text.TextLayout
    public BaseBounds getVisualBounds(int type) {
        ensureLayout();
        if (this.strike == null) {
            return null;
        }
        boolean underline = (type & 2) != 0;
        boolean hasUnderline = (this.flags & 512) != 0;
        boolean strikethrough = (type & 4) != 0;
        boolean hasStrikethrough = (this.flags & 1024) != 0;
        if (this.visualBounds != null && underline == hasUnderline && strikethrough == hasStrikethrough) {
            return this.visualBounds;
        }
        this.flags &= -1537;
        if (underline) {
            this.flags |= 512;
        }
        if (strikethrough) {
            this.flags |= 1024;
        }
        this.visualBounds = new RectBounds();
        float xMin = Float.POSITIVE_INFINITY;
        float yMin = Float.POSITIVE_INFINITY;
        float xMax = Float.NEGATIVE_INFINITY;
        float yMax = Float.NEGATIVE_INFINITY;
        float[] bounds = new float[4];
        FontResource fr = this.strike.getFontResource();
        Metrics metrics = this.strike.getMetrics();
        float size = this.strike.getSize();
        for (int i2 = 0; i2 < this.lines.length; i2++) {
            TextLine line = this.lines[i2];
            TextRun[] runs = line.getRuns();
            for (TextRun run : runs) {
                Point2D pt = run.getLocation();
                if (!run.isLinebreak()) {
                    int glyphCount = run.getGlyphCount();
                    for (int gi = 0; gi < glyphCount; gi++) {
                        int gc = run.getGlyphCode(gi);
                        if (gc != 65535) {
                            fr.getGlyphBoundingBox(run.getGlyphCode(gi), size, bounds);
                            if (bounds[0] != bounds[2]) {
                                float glyphX = pt.f11907x + run.getPosX(gi);
                                float glyphY = pt.f11908y + run.getPosY(gi);
                                float glyphMinX = glyphX + bounds[0];
                                float glyphMinY = glyphY - bounds[3];
                                float glyphMaxX = glyphX + bounds[2];
                                float glyphMaxY = glyphY - bounds[1];
                                if (glyphMinX < xMin) {
                                    xMin = glyphMinX;
                                }
                                if (glyphMinY < yMin) {
                                    yMin = glyphMinY;
                                }
                                if (glyphMaxX > xMax) {
                                    xMax = glyphMaxX;
                                }
                                if (glyphMaxY > yMax) {
                                    yMax = glyphMaxY;
                                }
                            }
                        }
                    }
                    if (underline) {
                        float underlineMinX = pt.f11907x;
                        float underlineMinY = pt.f11908y + metrics.getUnderLineOffset();
                        float underlineMaxX = underlineMinX + run.getWidth();
                        float underlineMaxY = underlineMinY + metrics.getUnderLineThickness();
                        if (underlineMinX < xMin) {
                            xMin = underlineMinX;
                        }
                        if (underlineMinY < yMin) {
                            yMin = underlineMinY;
                        }
                        if (underlineMaxX > xMax) {
                            xMax = underlineMaxX;
                        }
                        if (underlineMaxY > yMax) {
                            yMax = underlineMaxY;
                        }
                    }
                    if (strikethrough) {
                        float strikethroughMinX = pt.f11907x;
                        float strikethroughMinY = pt.f11908y + metrics.getStrikethroughOffset();
                        float strikethroughMaxX = strikethroughMinX + run.getWidth();
                        float strikethroughMaxY = strikethroughMinY + metrics.getStrikethroughThickness();
                        if (strikethroughMinX < xMin) {
                            xMin = strikethroughMinX;
                        }
                        if (strikethroughMinY < yMin) {
                            yMin = strikethroughMinY;
                        }
                        if (strikethroughMaxX > xMax) {
                            xMax = strikethroughMaxX;
                        }
                        if (strikethroughMaxY > yMax) {
                            yMax = strikethroughMaxY;
                        }
                    }
                }
            }
        }
        if (xMin < xMax && yMin < yMax) {
            this.visualBounds.setBounds(xMin, yMin, xMax, yMax);
        }
        return this.visualBounds;
    }

    private void computeSideBearings(TextLine line) {
        int gc;
        int gc2;
        TextRun[] runs = line.getRuns();
        if (runs.length == 0) {
            return;
        }
        float[] bounds = new float[4];
        FontResource defaultFontResource = null;
        float size = 0.0f;
        if (this.strike != null) {
            defaultFontResource = this.strike.getFontResource();
            size = this.strike.getSize();
        }
        float lsb = 0.0f;
        float width = 0.0f;
        int i2 = 0;
        while (true) {
            if (i2 >= runs.length) {
                break;
            }
            TextRun run = runs[i2];
            int glyphCount = run.getGlyphCount();
            for (int gi = 0; gi < glyphCount; gi++) {
                float advance = run.getAdvance(gi);
                if (advance != 0.0f && (gc2 = run.getGlyphCode(gi)) != 65535) {
                    FontResource fr = defaultFontResource;
                    if (fr == null) {
                        TextSpan span = run.getTextSpan();
                        PGFont font = (PGFont) span.getFont();
                        size = font.getSize();
                        fr = font.getFontResource();
                    }
                    fr.getGlyphBoundingBox(gc2, size, bounds);
                    float glyphLsb = bounds[0];
                    lsb = Math.min(0.0f, glyphLsb + width);
                    run.setLeftBearing();
                } else {
                    width += advance;
                }
            }
            if (glyphCount == 0) {
                width += run.getWidth();
            }
            i2++;
        }
        float rsb = 0.0f;
        float width2 = 0.0f;
        int i3 = runs.length - 1;
        while (true) {
            if (i3 < 0) {
                break;
            }
            TextRun run2 = runs[i3];
            int glyphCount2 = run2.getGlyphCount();
            for (int gi2 = glyphCount2 - 1; gi2 >= 0; gi2--) {
                float advance2 = run2.getAdvance(gi2);
                if (advance2 != 0.0f && (gc = run2.getGlyphCode(gi2)) != 65535) {
                    FontResource fr2 = defaultFontResource;
                    if (fr2 == null) {
                        TextSpan span2 = run2.getTextSpan();
                        PGFont font2 = (PGFont) span2.getFont();
                        size = font2.getSize();
                        fr2 = font2.getFontResource();
                    }
                    fr2.getGlyphBoundingBox(gc, size, bounds);
                    float glyphRsb = bounds[2] - advance2;
                    rsb = Math.max(0.0f, glyphRsb - width2);
                    run2.setRightBearing();
                } else {
                    width2 += advance2;
                }
            }
            if (glyphCount2 == 0) {
                width2 += run2.getWidth();
            }
            i3--;
        }
        line.setSideBearings(lsb, rsb);
    }
}
