package org.icepdf.core.pobjects.graphics.text;

import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.icepdf.core.pobjects.OptionalContents;
import org.icepdf.core.util.Defs;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/text/PageText.class */
public class PageText implements TextSelect {
    private static boolean checkForDuplicates = Defs.booleanProperty("org.icepdf.core.views.page.text.trim.duplicates", false);
    private static boolean preserveColumns = Defs.booleanProperty("org.icepdf.core.views.page.text.preserveColumns", true);
    private LineText currentLine;
    private ArrayList<LineText> pageLines = new ArrayList<>(64);
    private ArrayList<LineText> sortedPageLines;
    private LinkedHashMap<OptionalContents, PageText> optionalPageLines;

    public void newLine(LinkedList<OptionalContents> oCGs) {
        if (oCGs != null && oCGs.size() > 0) {
            if (this.optionalPageLines == null) {
                this.optionalPageLines = new LinkedHashMap<>(10);
            }
            OptionalContents optionalContent = oCGs.peek();
            PageText pageText = this.optionalPageLines.get(optionalContent);
            if (pageText == null) {
                PageText pageText2 = new PageText();
                pageText2.newLine();
                this.optionalPageLines.put(optionalContent, pageText2);
                return;
            }
            pageText.newLine();
        }
    }

    public void newLine() {
        if (this.currentLine != null && this.currentLine.getWords().size() == 0) {
            return;
        }
        this.currentLine = new LineText();
        this.pageLines.add(this.currentLine);
    }

    protected void addGlyph(GlyphText sprite) {
        if (this.currentLine == null) {
            newLine();
        }
        this.currentLine.addText(sprite);
    }

    public ArrayList<LineText> getPageLines() {
        if (this.sortedPageLines == null) {
            sortAndFormatText();
        }
        return this.sortedPageLines;
    }

    private ArrayList<LineText> getVisiblePageLines() {
        ArrayList<LineText> visiblePageLines = new ArrayList<>(this.pageLines);
        if (this.optionalPageLines != null) {
            Set<OptionalContents> keys = this.optionalPageLines.keySet();
            LineText currentLine = new LineText();
            visiblePageLines.add(currentLine);
            for (OptionalContents key : keys) {
                if (key != null && key.isVisible()) {
                    ArrayList<LineText> pageLines = this.optionalPageLines.get(key).getVisiblePageLines();
                    Iterator i$ = pageLines.iterator();
                    while (i$.hasNext()) {
                        LineText lineText = i$.next();
                        currentLine.addAll(lineText.getWords());
                    }
                }
            }
            currentLine.getBounds();
        }
        return visiblePageLines;
    }

    private ArrayList<LineText> getAllPageLines() {
        ArrayList<LineText> visiblePageLines = new ArrayList<>(this.pageLines);
        if (this.optionalPageLines != null) {
            Set<OptionalContents> keys = this.optionalPageLines.keySet();
            LineText currentLine = new LineText();
            visiblePageLines.add(currentLine);
            for (OptionalContents key : keys) {
                if (key != null) {
                    ArrayList<LineText> pageLines = this.optionalPageLines.get(key).getVisiblePageLines();
                    Iterator i$ = pageLines.iterator();
                    while (i$.hasNext()) {
                        LineText lineText = i$.next();
                        currentLine.addAll(lineText.getWords());
                    }
                }
            }
            currentLine.getBounds();
        }
        return visiblePageLines;
    }

    public void addPageLines(ArrayList<LineText> pageLines) {
        if (pageLines != null) {
            this.pageLines.addAll(pageLines);
        }
    }

    public void addGlyph(GlyphText glyphText, LinkedList<OptionalContents> oCGs) {
        if (oCGs != null && oCGs.size() > 0) {
            if (oCGs.peek() != null) {
                addOptionalPageLines(oCGs.peek(), glyphText);
                return;
            }
            return;
        }
        addGlyph(glyphText);
    }

    protected void addOptionalPageLines(OptionalContents optionalContent, GlyphText sprite) {
        if (this.optionalPageLines == null) {
            this.optionalPageLines = new LinkedHashMap<>(10);
        }
        PageText pageText = this.optionalPageLines.get(optionalContent);
        if (pageText == null) {
            PageText pageText2 = new PageText();
            pageText2.addGlyph(sprite);
            this.optionalPageLines.put(optionalContent, pageText2);
            return;
        }
        pageText.addGlyph(sprite);
    }

    public void applyXObjectTransform(AffineTransform transform) {
        Iterator i$ = this.pageLines.iterator();
        while (i$.hasNext()) {
            LineText lineText = i$.next();
            lineText.clearBounds();
            for (WordText wordText : lineText.getWords()) {
                wordText.clearBounds();
                Iterator i$2 = wordText.getGlyphs().iterator();
                while (i$2.hasNext()) {
                    GlyphText glyph = i$2.next();
                    glyph.normalizeToUserSpace(transform);
                }
            }
        }
    }

    @Override // org.icepdf.core.pobjects.graphics.text.TextSelect
    public void clearSelected() {
        ArrayList<LineText> optionalLines;
        if (this.pageLines != null) {
            Iterator i$ = this.pageLines.iterator();
            while (i$.hasNext()) {
                LineText lineText = i$.next();
                lineText.clearSelected();
            }
        }
        if (this.sortedPageLines != null) {
            Iterator i$2 = this.sortedPageLines.iterator();
            while (i$2.hasNext()) {
                LineText lineText2 = i$2.next();
                lineText2.clearSelected();
            }
        }
        if (this.optionalPageLines != null) {
            Set<OptionalContents> keys = this.optionalPageLines.keySet();
            for (OptionalContents key : keys) {
                if (key != null && (optionalLines = this.optionalPageLines.get(key).getAllPageLines()) != null) {
                    Iterator i$3 = optionalLines.iterator();
                    while (i$3.hasNext()) {
                        LineText lineText3 = i$3.next();
                        lineText3.clearSelected();
                    }
                }
            }
        }
    }

    @Override // org.icepdf.core.pobjects.graphics.text.TextSelect
    public void clearHighlighted() {
        Iterator i$ = this.pageLines.iterator();
        while (i$.hasNext()) {
            LineText lineText = i$.next();
            lineText.clearHighlighted();
        }
        Iterator i$2 = this.sortedPageLines.iterator();
        while (i$2.hasNext()) {
            LineText lineText2 = i$2.next();
            lineText2.clearHighlighted();
        }
        if (this.optionalPageLines != null) {
            Set<OptionalContents> keys = this.optionalPageLines.keySet();
            for (OptionalContents key : keys) {
                if (key != null && key.isVisible()) {
                    ArrayList<LineText> optionalLines = this.optionalPageLines.get(key).getAllPageLines();
                    Iterator i$3 = optionalLines.iterator();
                    while (i$3.hasNext()) {
                        LineText lineText3 = i$3.next();
                        lineText3.clearHighlighted();
                    }
                }
            }
        }
    }

    @Override // org.icepdf.core.pobjects.graphics.text.TextSelect
    public StringBuilder getSelected() {
        StringBuilder selectedText = new StringBuilder();
        ArrayList<LineText> pageLines = getPageLines();
        if (pageLines != null) {
            Iterator i$ = pageLines.iterator();
            while (i$.hasNext()) {
                LineText lineText = i$.next();
                selectedText.append((CharSequence) lineText.getSelected());
            }
        }
        return selectedText;
    }

    @Override // org.icepdf.core.pobjects.graphics.text.TextSelect
    public void selectAll() {
        ArrayList<LineText> pageLines = getPageLines();
        if (pageLines != null) {
            Iterator i$ = pageLines.iterator();
            while (i$.hasNext()) {
                LineText lineText = i$.next();
                lineText.selectAll();
            }
        }
    }

    public void deselectAll() {
        Iterator i$ = this.pageLines.iterator();
        while (i$.hasNext()) {
            LineText lineText = i$.next();
            lineText.clearSelected();
        }
    }

    public String toString() {
        StringBuilder extractedText = new StringBuilder();
        Iterator i$ = this.pageLines.iterator();
        while (i$.hasNext()) {
            LineText lineText = i$.next();
            for (WordText wordText : lineText.getWords()) {
                extractedText.append(wordText.getText());
            }
            extractedText.append('\n');
        }
        return extractedText.toString();
    }

    public void sortAndFormatText() {
        ArrayList<LineText> visiblePageLines = getVisiblePageLines();
        if (visiblePageLines != null && visiblePageLines.size() > 0 && visiblePageLines.get(0) != null) {
            ArrayList<LineText> sortedPageLines = new ArrayList<>(64);
            Iterator i$ = visiblePageLines.iterator();
            while (i$.hasNext()) {
                LineText pageLine = i$.next();
                List<WordText> words = pageLine.getWords();
                if (words != null && words.size() > 0) {
                    if (!preserveColumns) {
                        Collections.sort(words, new LinePositionComparator());
                    }
                    double lastY = words.get(0).getBounds().f12405y;
                    int start = 0;
                    int end = 0;
                    for (WordText word : words) {
                        double currentY = Math.round(word.getBounds().getY());
                        if (currentY != lastY) {
                            LineText lineText = new LineText();
                            lineText.addAll(words.subList(start, end));
                            sortedPageLines.add(lineText);
                            start = end;
                        }
                        end++;
                        lastY = currentY;
                    }
                    if (start < end) {
                        LineText lineText2 = new LineText();
                        lineText2.addAll(words.subList(start, end));
                        sortedPageLines.add(lineText2);
                    }
                }
            }
            if (checkForDuplicates) {
                int maxLines = sortedPageLines.size();
                for (int k2 = 0; k2 < maxLines; k2++) {
                    LineText lineText3 = sortedPageLines.get(k2);
                    List<WordText> words2 = lineText3.getWords();
                    if (words2.size() > 0) {
                        List<WordText> trimmedWords = new ArrayList<>();
                        Set<String> refs = new HashSet<>();
                        for (WordText wordText : words2) {
                            String key = wordText.getText() + ((Object) wordText.getBounds());
                            if (refs.add(key)) {
                                trimmedWords.add(wordText);
                            }
                        }
                        lineText3.setWords(trimmedWords);
                    }
                }
            }
            if (sortedPageLines.size() > 0) {
                Iterator i$2 = sortedPageLines.iterator();
                while (i$2.hasNext()) {
                    LineText lineText4 = i$2.next();
                    Collections.sort(lineText4.getWords(), new WordPositionComparator());
                }
            }
            if (sortedPageLines.size() > 0) {
                Iterator i$3 = sortedPageLines.iterator();
                while (i$3.hasNext()) {
                    LineText lineText5 = i$3.next();
                    lineText5.getBounds();
                }
            }
            this.sortedPageLines = sortedPageLines;
        }
    }
}
