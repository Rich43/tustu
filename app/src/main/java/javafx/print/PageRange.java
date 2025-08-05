package javafx.print;

import javafx.beans.NamedArg;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;

/* loaded from: jfxrt.jar:javafx/print/PageRange.class */
public final class PageRange {
    private ReadOnlyIntegerWrapper startPage;
    private ReadOnlyIntegerWrapper endPage;

    public PageRange(@NamedArg("startPage") int startPage, @NamedArg("endPage") int endPage) {
        if (startPage <= 0 || startPage > endPage) {
            throw new IllegalArgumentException("Invalid range : " + startPage + " -> " + endPage);
        }
        startPageImplProperty().set(startPage);
        endPageImplProperty().set(endPage);
    }

    private ReadOnlyIntegerWrapper startPageImplProperty() {
        if (this.startPage == null) {
            this.startPage = new ReadOnlyIntegerWrapper(this, "startPage", 1) { // from class: javafx.print.PageRange.1
                @Override // javafx.beans.property.IntegerPropertyBase, javafx.beans.value.WritableIntegerValue
                public void set(int value) {
                    if (value > 0) {
                        if (PageRange.this.endPage != null && value < PageRange.this.endPage.get()) {
                            return;
                        }
                        super.set(value);
                    }
                }
            };
        }
        return this.startPage;
    }

    public ReadOnlyIntegerProperty startPageProperty() {
        return startPageImplProperty().getReadOnlyProperty();
    }

    public int getStartPage() {
        return startPageProperty().get();
    }

    private ReadOnlyIntegerWrapper endPageImplProperty() {
        if (this.endPage == null) {
            this.endPage = new ReadOnlyIntegerWrapper(this, "endPage", 9999) { // from class: javafx.print.PageRange.2
                @Override // javafx.beans.property.IntegerPropertyBase, javafx.beans.value.WritableIntegerValue
                public void set(int value) {
                    if (value > 0) {
                        if (PageRange.this.startPage != null && value < PageRange.this.startPage.get()) {
                            return;
                        }
                        super.set(value);
                    }
                }
            };
        }
        return this.endPage;
    }

    public ReadOnlyIntegerProperty endPageProperty() {
        return endPageImplProperty().getReadOnlyProperty();
    }

    public int getEndPage() {
        return endPageProperty().get();
    }
}
