package org.icepdf.core.search;

import java.util.ArrayList;

/* loaded from: icepdf-core.jar:org/icepdf/core/search/SearchTerm.class */
public class SearchTerm {
    private String term;
    private ArrayList<String> terms;
    private boolean caseSensitive;
    private boolean wholeWord;

    public SearchTerm(String term, ArrayList<String> terms, boolean caseSensitive, boolean wholeWord) {
        this.term = term;
        this.terms = terms;
        this.caseSensitive = caseSensitive;
        this.wholeWord = wholeWord;
    }

    public ArrayList<String> getTerms() {
        return this.terms;
    }

    public String getTerm() {
        return this.term;
    }

    public boolean isCaseSensitive() {
        return this.caseSensitive;
    }

    public boolean isWholeWord() {
        return this.wholeWord;
    }

    public boolean equals(Object object) {
        if (object instanceof SearchTerm) {
            SearchTerm test = (SearchTerm) object;
            return test.isCaseSensitive() == this.caseSensitive && test.isWholeWord() == this.wholeWord && test.getTerm().equals(this.term);
        }
        return false;
    }
}
