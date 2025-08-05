package org.icepdf.ri.common.search;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import org.icepdf.core.pobjects.graphics.text.PageText;
import org.icepdf.core.search.SearchTerm;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/search/DocumentSearchModelImpl.class */
public class DocumentSearchModelImpl {
    private HashMap<Integer, WeakReference<PageText>> searchResultCache = new HashMap<>(256);
    private ArrayList<SearchTerm> searchTerms = new ArrayList<>();

    public ArrayList<SearchTerm> getSearchTerms() {
        return this.searchTerms;
    }

    public void addSearchTerm(SearchTerm searchTerm) {
        this.searchTerms.add(searchTerm);
    }

    public void removeSearchTerm(SearchTerm searchTerm) {
        this.searchTerms.remove(searchTerm);
    }

    public void addPageSearchHit(int pageIndex, PageText pageText) {
        this.searchResultCache.put(Integer.valueOf(pageIndex), new WeakReference<>(pageText));
    }

    public Set<Integer> getPageSearchHits() {
        return this.searchResultCache.keySet();
    }

    public boolean isPageSearchHit(int pageIndex) {
        return this.searchResultCache.get(Integer.valueOf(pageIndex)) != null;
    }

    public PageText getPageTextHit(int pageIndex) {
        WeakReference<PageText> ref = this.searchResultCache.get(Integer.valueOf(pageIndex));
        if (ref.get() != null) {
            return ref.get();
        }
        return null;
    }

    public boolean isPageTextMatch(int pageIndex, PageText pageText) {
        WeakReference<PageText> ref = this.searchResultCache.get(Integer.valueOf(pageIndex));
        if (ref == null) {
            return false;
        }
        PageText matchText = ref.get();
        return matchText == null || !matchText.equals(pageText);
    }

    public void clearSearchResults(int page) {
        PageText currentPageText;
        WeakReference<PageText> pageReference = this.searchResultCache.get(Integer.valueOf(page));
        if (pageReference != null && (currentPageText = pageReference.get()) != null) {
            currentPageText.clearHighlighted();
        }
        this.searchResultCache.remove(Integer.valueOf(page));
    }

    public void clearSearchResults() {
        Collection<WeakReference<PageText>> pagTextHits = this.searchResultCache.values();
        for (WeakReference<PageText> pageIndex : pagTextHits) {
            PageText currentPageText = pageIndex.get();
            if (currentPageText != null) {
                currentPageText.clearHighlighted();
            }
        }
        this.searchResultCache.clear();
        this.searchTerms.clear();
    }
}
