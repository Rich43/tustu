package org.icepdf.core.pobjects.graphics.text;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/text/LineText.class */
public class LineText extends AbstractText implements TextSelect {
    private WordText currentWord;
    private List<WordText> words = new ArrayList(16);

    @Override // org.icepdf.core.pobjects.graphics.text.AbstractText, org.icepdf.core.pobjects.graphics.text.Text
    public Rectangle2D.Float getBounds() {
        if (this.bounds == null) {
            for (WordText word : this.words) {
                if (this.bounds == null) {
                    this.bounds = new Rectangle2D.Float();
                    this.bounds.setRect(word.getBounds());
                } else {
                    this.bounds.add(word.getBounds());
                }
            }
            if (this.bounds == null) {
                this.bounds = new Rectangle2D.Float();
            }
        }
        return this.bounds;
    }

    protected void addText(GlyphText sprite) {
        if (WordText.detectWhiteSpace(sprite)) {
            WordText newWord = new WordText();
            newWord.setWhiteSpace(true);
            newWord.addText(sprite);
            addWord(newWord);
            this.currentWord = null;
            return;
        }
        if (WordText.detectPunctuation(sprite, this.currentWord)) {
            WordText newWord2 = new WordText();
            newWord2.setWhiteSpace(true);
            newWord2.addText(sprite);
            addWord(newWord2);
            this.currentWord = null;
            return;
        }
        if (getCurrentWord().detectSpace(sprite)) {
            WordText spaceWord = this.currentWord.buildSpaceWord(sprite);
            spaceWord.setWhiteSpace(true);
            addWord(spaceWord);
            this.currentWord = null;
            addText(sprite);
            return;
        }
        getCurrentWord().addText(sprite);
    }

    private void addWord(WordText wordText) {
        this.words.add(wordText);
        this.currentWord = wordText;
    }

    public void addAll(List<WordText> words) {
        this.words.addAll(words);
    }

    protected void setWords(List<WordText> words) {
        this.words = words;
    }

    private WordText getCurrentWord() {
        if (this.currentWord == null) {
            this.currentWord = new WordText();
            this.words.add(this.currentWord);
        }
        return this.currentWord;
    }

    public List<WordText> getWords() {
        return this.words;
    }

    @Override // org.icepdf.core.pobjects.graphics.text.TextSelect
    public void selectAll() {
        setSelected(true);
        setHasSelected(true);
        for (WordText word : this.words) {
            word.selectAll();
        }
    }

    @Override // org.icepdf.core.pobjects.graphics.text.TextSelect
    public void clearSelected() {
        setSelected(false);
        setHasSelected(false);
        for (WordText word : this.words) {
            word.clearSelected();
        }
    }

    @Override // org.icepdf.core.pobjects.graphics.text.TextSelect
    public void clearHighlighted() {
        setHighlighted(false);
        setHasHighlight(false);
        for (WordText word : this.words) {
            word.setHighlighted(false);
        }
    }

    @Override // org.icepdf.core.pobjects.graphics.text.TextSelect
    public StringBuilder getSelected() {
        StringBuilder selectedText = new StringBuilder();
        for (WordText word : this.words) {
            selectedText.append((CharSequence) word.getSelected());
        }
        if (this.hasSelected) {
            selectedText.append('\n');
        }
        return selectedText;
    }

    public String toString() {
        return this.words.toString();
    }
}
