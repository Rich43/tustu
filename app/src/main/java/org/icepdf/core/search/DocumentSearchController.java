package org.icepdf.core.search;

import java.util.ArrayList;
import java.util.List;
import org.icepdf.core.pobjects.graphics.text.LineText;
import org.icepdf.core.pobjects.graphics.text.PageText;
import org.icepdf.core.pobjects.graphics.text.WordText;

/* loaded from: icepdf-core.jar:org/icepdf/core/search/DocumentSearchController.class */
public interface DocumentSearchController {
    int searchHighlightPage(int i2, String str, boolean z2, boolean z3);

    int searchHighlightPage(int i2);

    List<LineText> searchHighlightPage(int i2, int i3);

    ArrayList<WordText> searchPage(int i2);

    SearchTerm addSearchTerm(String str, boolean z2, boolean z3);

    void removeSearchTerm(SearchTerm searchTerm);

    void clearSearchHighlight(int i2);

    void clearAllSearchHighlight();

    boolean isSearchHighlightRefreshNeeded(int i2, PageText pageText);

    void dispose();
}
