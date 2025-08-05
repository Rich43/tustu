package javafx.scene.web;

import com.sun.webkit.BackForwardList;
import com.sun.webkit.WebPage;
import java.net.URL;
import java.util.Date;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/* loaded from: jfxrt.jar:javafx/scene/web/WebHistory.class */
public final class WebHistory {
    private final BackForwardList bfl;
    private IntegerProperty maxSize;
    static final /* synthetic */ boolean $assertionsDisabled;
    private final ReadOnlyIntegerWrapper currentIndex = new ReadOnlyIntegerWrapper(this, "currentIndex");
    private final ObservableList<Entry> list = FXCollections.observableArrayList();
    private final ObservableList<Entry> ulist = FXCollections.unmodifiableObservableList(this.list);

    static {
        $assertionsDisabled = !WebHistory.class.desiredAssertionStatus();
    }

    /* loaded from: jfxrt.jar:javafx/scene/web/WebHistory$Entry.class */
    public final class Entry {
        private final URL url;
        private final ReadOnlyObjectWrapper<String> title;
        private final ReadOnlyObjectWrapper<Date> lastVisitedDate;
        private final BackForwardList.Entry peer;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !WebHistory.class.desiredAssertionStatus();
        }

        private Entry(BackForwardList.Entry entry) {
            this.title = new ReadOnlyObjectWrapper<>(this, "title");
            this.lastVisitedDate = new ReadOnlyObjectWrapper<>(this, "lastVisitedDate");
            this.url = entry.getURL();
            this.title.set(entry.getTitle());
            this.lastVisitedDate.set(entry.getLastVisitedDate());
            this.peer = entry;
            entry.addChangeListener(e2 -> {
                String _title = entry.getTitle();
                if (_title == null || !_title.equals(getTitle())) {
                    this.title.set(_title);
                }
                Date _date = entry.getLastVisitedDate();
                if (_date != null && !_date.equals(getLastVisitedDate())) {
                    this.lastVisitedDate.set(_date);
                }
            });
        }

        public String getUrl() {
            if ($assertionsDisabled || this.url != null) {
                return this.url.toString();
            }
            throw new AssertionError();
        }

        public ReadOnlyObjectProperty<String> titleProperty() {
            return this.title.getReadOnlyProperty();
        }

        public String getTitle() {
            return this.title.get();
        }

        public ReadOnlyObjectProperty<Date> lastVisitedDateProperty() {
            return this.lastVisitedDate.getReadOnlyProperty();
        }

        public Date getLastVisitedDate() {
            return this.lastVisitedDate.get();
        }

        boolean isPeer(BackForwardList.Entry entry) {
            return this.peer == entry;
        }

        public String toString() {
            return "[url: " + getUrl() + ", title: " + getTitle() + ", date: " + ((Object) getLastVisitedDate()) + "]";
        }
    }

    WebHistory(WebPage page) {
        this.bfl = page.createBackForwardList();
        setMaxSize(getMaxSize());
        this.bfl.addChangeListener(e2 -> {
            if (this.bfl.size() > this.list.size()) {
                if (!$assertionsDisabled && this.bfl.size() != this.list.size() + 1) {
                    throw new AssertionError();
                }
                this.list.add(new Entry(this.bfl.getCurrentEntry()));
                setCurrentIndex(this.list.size() - 1);
                return;
            }
            if (this.bfl.size() == this.list.size()) {
                if (this.list.size() == 0) {
                    return;
                }
                if (!$assertionsDisabled && this.list.size() <= 0) {
                    throw new AssertionError();
                }
                BackForwardList.Entry last = this.bfl.get(this.list.size() - 1);
                BackForwardList.Entry first = this.bfl.get(0);
                if (this.list.get(this.list.size() - 1).isPeer(last)) {
                    setCurrentIndex(this.bfl.getCurrentIndex());
                    return;
                } else if (!this.list.get(0).isPeer(first)) {
                    this.list.remove(0);
                    this.list.add(new Entry(last));
                    setCurrentIndex(this.bfl.getCurrentIndex());
                    return;
                }
            }
            if (!$assertionsDisabled && this.bfl.size() > this.list.size()) {
                throw new AssertionError();
            }
            this.list.remove(this.bfl.size(), this.list.size());
            int lastIndex = this.list.size() - 1;
            if (lastIndex >= 0 && !this.list.get(lastIndex).isPeer(this.bfl.get(lastIndex))) {
                this.list.remove(lastIndex);
                this.list.add(new Entry(this.bfl.get(lastIndex)));
            }
            setCurrentIndex(this.bfl.getCurrentIndex());
        });
    }

    public ReadOnlyIntegerProperty currentIndexProperty() {
        return this.currentIndex.getReadOnlyProperty();
    }

    public int getCurrentIndex() {
        return currentIndexProperty().get();
    }

    private void setCurrentIndex(int value) {
        this.currentIndex.set(value);
    }

    public IntegerProperty maxSizeProperty() {
        if (this.maxSize == null) {
            this.maxSize = new SimpleIntegerProperty(this, "maxSize", 100) { // from class: javafx.scene.web.WebHistory.1
                @Override // javafx.beans.property.IntegerPropertyBase, javafx.beans.value.WritableIntegerValue
                public void set(int value) {
                    if (value < 0) {
                        throw new IllegalArgumentException("value cannot be negative.");
                    }
                    super.set(value);
                }
            };
        }
        return this.maxSize;
    }

    public void setMaxSize(int value) {
        maxSizeProperty().set(value);
        this.bfl.setMaximumSize(value);
    }

    public int getMaxSize() {
        return maxSizeProperty().get();
    }

    public ObservableList<Entry> getEntries() {
        return this.ulist;
    }

    public void go(int offset) throws IndexOutOfBoundsException {
        if (offset == 0) {
            return;
        }
        int index = getCurrentIndex() + offset;
        if (index < 0 || index >= this.list.size()) {
            throw new IndexOutOfBoundsException("the effective index " + index + " is out of the range [0.." + (this.list.size() - 1) + "]");
        }
        this.bfl.setCurrentIndex(index);
    }
}
