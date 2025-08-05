package org.icepdf.ri.common.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.pobjects.graphics.text.LineText;
import org.icepdf.core.pobjects.graphics.text.PageText;
import org.icepdf.core.pobjects.graphics.text.WordText;
import org.icepdf.core.search.DocumentSearchController;
import org.icepdf.core.search.SearchTerm;
import org.icepdf.ri.common.SwingController;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/search/DocumentSearchControllerImpl.class */
public class DocumentSearchControllerImpl implements DocumentSearchController {
    private static final Logger logger = Logger.getLogger(DocumentSearchControllerImpl.class.toString());
    protected DocumentSearchModelImpl searchModel = new DocumentSearchModelImpl();
    protected SwingController viewerController;
    protected Document document;

    public DocumentSearchControllerImpl(SwingController viewerController) {
        this.viewerController = viewerController;
    }

    public DocumentSearchControllerImpl(Document document) {
        this.document = document;
    }

    @Override // org.icepdf.core.search.DocumentSearchController
    public int searchHighlightPage(int pageIndex, String term, boolean caseSensitive, boolean wholeWord) {
        clearSearchHighlight(pageIndex);
        addSearchTerm(term, caseSensitive, wholeWord);
        return searchHighlightPage(pageIndex);
    }

    @Override // org.icepdf.core.search.DocumentSearchController
    public int searchHighlightPage(int pageIndex) {
        Collection<SearchTerm> terms = this.searchModel.getSearchTerms();
        int hitCount = 0;
        PageText pageText = null;
        if (this.viewerController != null) {
            try {
                pageText = this.viewerController.getDocument().getPageText(pageIndex);
            } catch (InterruptedException e2) {
                logger.log(Level.SEVERE, "Page text extraction thread interrupted.", (Throwable) e2);
            }
        } else if (this.document != null) {
            pageText = this.document.getPageViewText(pageIndex);
        }
        if (pageText == null) {
            return 0;
        }
        for (SearchTerm term : terms) {
            int searchPhraseHitCount = 0;
            int searchPhraseFoundCount = term.getTerms().size();
            ArrayList<WordText> searchPhraseHits = new ArrayList<>(searchPhraseFoundCount);
            ArrayList<LineText> pageLines = pageText.getPageLines();
            if (pageLines != null) {
                Iterator i$ = pageLines.iterator();
                while (i$.hasNext()) {
                    LineText pageLine = i$.next();
                    List<WordText> lineWords = pageLine.getWords();
                    for (WordText word : lineWords) {
                        String wordString = term.isCaseSensitive() ? word.toString() : word.toString().toLowerCase();
                        if (term.isWholeWord()) {
                            if (wordString.equals(term.getTerms().get(searchPhraseHitCount))) {
                                searchPhraseHits.add(word);
                                searchPhraseHitCount++;
                            } else {
                                searchPhraseHits.clear();
                                searchPhraseHitCount = 0;
                            }
                        } else if (wordString.indexOf(term.getTerms().get(searchPhraseHitCount)) >= 0) {
                            searchPhraseHits.add(word);
                            searchPhraseHitCount++;
                        } else {
                            searchPhraseHits.clear();
                            searchPhraseHitCount = 0;
                        }
                        if (searchPhraseHitCount == searchPhraseFoundCount) {
                            Iterator i$2 = searchPhraseHits.iterator();
                            while (i$2.hasNext()) {
                                WordText wordHit = i$2.next();
                                wordHit.setHighlighted(true);
                                wordHit.setHasHighlight(true);
                            }
                            hitCount++;
                            searchPhraseHits.clear();
                            searchPhraseHitCount = 0;
                        }
                    }
                }
            }
        }
        if (hitCount > 0) {
            this.searchModel.addPageSearchHit(pageIndex, pageText);
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("Found search hits on page " + pageIndex + " hit count " + hitCount);
            }
        }
        return hitCount;
    }

    @Override // org.icepdf.core.search.DocumentSearchController
    public ArrayList<LineText> searchHighlightPage(int pageIndex, int wordPadding) {
        Collection<SearchTerm> terms = this.searchModel.getSearchTerms();
        ArrayList<LineText> searchHits = new ArrayList<>();
        PageText pageText = null;
        if (this.viewerController != null) {
            try {
                pageText = this.viewerController.getDocument().getPageText(pageIndex);
            } catch (InterruptedException e2) {
                logger.log(Level.SEVERE, "Page text extraction thread interrupted.", (Throwable) e2);
            }
        } else if (this.document != null) {
            pageText = this.document.getPageViewText(pageIndex);
        }
        if (pageText == null) {
            return searchHits;
        }
        for (SearchTerm term : terms) {
            int searchPhraseHitCount = 0;
            int searchPhraseFoundCount = term.getTerms().size();
            ArrayList<WordText> searchPhraseHits = new ArrayList<>(searchPhraseFoundCount);
            ArrayList<LineText> pageLines = pageText.getPageLines();
            if (pageLines != null) {
                Iterator i$ = pageLines.iterator();
                while (i$.hasNext()) {
                    LineText pageLine = i$.next();
                    List<WordText> lineWords = pageLine.getWords();
                    int max = lineWords.size();
                    for (int i2 = 0; i2 < max; i2++) {
                        WordText word = lineWords.get(i2);
                        String wordString = term.isCaseSensitive() ? word.toString() : word.toString().toLowerCase();
                        if (term.isWholeWord()) {
                            if (wordString.equals(term.getTerms().get(searchPhraseHitCount))) {
                                searchPhraseHits.add(word);
                                searchPhraseHitCount++;
                            } else {
                                searchPhraseHits.clear();
                                searchPhraseHitCount = 0;
                            }
                        } else if (wordString.indexOf(term.getTerms().get(searchPhraseHitCount)) >= 0) {
                            searchPhraseHits.add(word);
                            searchPhraseHitCount++;
                        } else {
                            searchPhraseHits.clear();
                            searchPhraseHitCount = 0;
                        }
                        if (searchPhraseHitCount == searchPhraseFoundCount) {
                            LineText lineText = new LineText();
                            int lineWordsSize = lineWords.size();
                            List<WordText> hitWords = lineText.getWords();
                            int start = ((i2 - searchPhraseHitCount) - wordPadding) + 1;
                            int start2 = start < 0 ? 0 : start;
                            int end = (i2 - searchPhraseHitCount) + 1;
                            int end2 = end < 0 ? 0 : end;
                            for (int p2 = start2; p2 < end2; p2++) {
                                hitWords.add(lineWords.get(p2));
                            }
                            Iterator i$2 = searchPhraseHits.iterator();
                            while (i$2.hasNext()) {
                                WordText wordHit = i$2.next();
                                wordHit.setHighlighted(true);
                                wordHit.setHasHighlight(true);
                            }
                            hitWords.addAll(searchPhraseHits);
                            int start3 = i2 + 1;
                            int start4 = start3 > lineWordsSize ? lineWordsSize : start3;
                            int end3 = start4 + wordPadding;
                            int end4 = end3 > lineWordsSize ? lineWordsSize : end3;
                            for (int p3 = start4; p3 < end4; p3++) {
                                hitWords.add(lineWords.get(p3));
                            }
                            searchHits.add(lineText);
                            searchPhraseHits.clear();
                            searchPhraseHitCount = 0;
                        }
                    }
                }
            }
        }
        if (searchHits.size() > 0) {
            this.searchModel.addPageSearchHit(pageIndex, pageText);
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("Found search hits on page " + pageIndex + " hit count " + searchHits.size());
            }
        }
        return searchHits;
    }

    @Override // org.icepdf.core.search.DocumentSearchController
    public ArrayList<WordText> searchPage(int pageIndex) {
        PageText searchText;
        int hits = searchHighlightPage(pageIndex);
        if (hits > 0 && (searchText = this.searchModel.getPageTextHit(pageIndex)) != null) {
            ArrayList<WordText> words = new ArrayList<>(hits);
            ArrayList<LineText> pageLines = searchText.getPageLines();
            if (pageLines != null) {
                Iterator i$ = pageLines.iterator();
                while (i$.hasNext()) {
                    LineText pageLine = i$.next();
                    List<WordText> lineWords = pageLine.getWords();
                    if (lineWords != null) {
                        for (WordText word : lineWords) {
                            if (word.isHighlighted()) {
                                words.add(word);
                            }
                        }
                    }
                }
            }
            return words;
        }
        return null;
    }

    @Override // org.icepdf.core.search.DocumentSearchController
    public SearchTerm addSearchTerm(String term, boolean caseSensitive, boolean wholeWord) {
        String origionalTerm = String.valueOf(term);
        if (!caseSensitive) {
            term = term.toLowerCase();
        }
        ArrayList<String> searchPhrase = searchPhraseParser(term);
        SearchTerm searchTerm = new SearchTerm(origionalTerm, searchPhrase, caseSensitive, wholeWord);
        this.searchModel.addSearchTerm(searchTerm);
        return searchTerm;
    }

    @Override // org.icepdf.core.search.DocumentSearchController
    public void removeSearchTerm(SearchTerm searchTerm) {
        this.searchModel.removeSearchTerm(searchTerm);
    }

    @Override // org.icepdf.core.search.DocumentSearchController
    public void clearSearchHighlight(int pageIndex) {
        this.searchModel.clearSearchResults(pageIndex);
    }

    @Override // org.icepdf.core.search.DocumentSearchController
    public void clearAllSearchHighlight() {
        this.searchModel.clearSearchResults();
    }

    @Override // org.icepdf.core.search.DocumentSearchController
    public boolean isSearchHighlightRefreshNeeded(int pageIndex, PageText pageText) {
        return this.searchModel.isPageTextMatch(pageIndex, pageText);
    }

    @Override // org.icepdf.core.search.DocumentSearchController
    public void dispose() {
        this.searchModel.clearSearchResults();
    }

    protected ArrayList<String> searchPhraseParser(String phrase) {
        String phrase2 = phrase.trim();
        ArrayList<String> words = new ArrayList<>();
        char prevC = ' ';
        int start = 0;
        int max = phrase2.length();
        for (int curs = 0; curs < max; curs++) {
            char c2 = phrase2.charAt(curs);
            if (!WordText.isDigit(prevC) && (WordText.isWhiteSpace(c2) || WordText.isPunctuation(c2))) {
                if (start != curs) {
                    words.add(phrase2.substring(start, curs));
                }
                words.add(phrase2.substring(curs, curs + 1));
                start = curs + 1 < max ? curs + 1 : start;
            } else if (curs + 1 == max) {
                words.add(phrase2.substring(start, curs + 1));
            }
            prevC = c2;
        }
        return words;
    }
}
