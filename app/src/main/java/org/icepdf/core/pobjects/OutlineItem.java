package org.icepdf.core.pobjects;

import com.sun.org.glassfish.external.amx.AMX;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.icepdf.core.pobjects.actions.Action;
import org.icepdf.core.util.Library;
import org.icepdf.core.util.Utils;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/OutlineItem.class */
public class OutlineItem extends Dictionary {
    public static final Name A_KEY = new Name("A");
    public static final Name COUNT_KEY = new Name("Count");
    public static final Name TITLE_KEY = new Name("Title");
    public static final Name DEST_KEY = new Name("Dest");
    public static final Name FIRST_KEY = new Name("First");
    public static final Name LAST_KEY = new Name("Last");
    public static final Name NEXT_KEY = new Name("Next");
    public static final Name PREV_KEY = new Name("Prev");
    public static final Name PARENT_KEY = new Name(AMX.ATTR_PARENT);
    private String title;
    private Destination dest;
    private Action action;
    private Reference parent;
    private Reference prev;
    private Reference next;
    private Reference first;
    private Reference last;
    private int count;
    private boolean loadedSubItems;
    private List<OutlineItem> subItems;

    public OutlineItem(Library l2, HashMap h2) {
        super(l2, h2);
        this.count = -1;
        this.loadedSubItems = false;
        this.subItems = new ArrayList(Math.max(Math.abs(getCount()), 16));
    }

    public boolean isEmpty() {
        return getTitle() == null && getDest() == null && getAction() == null;
    }

    public int getSubItemCount() {
        ensureSubItemsLoaded();
        if (this.subItems != null) {
            return this.subItems.size();
        }
        return 0;
    }

    public OutlineItem getSubItem(int index) {
        ensureSubItemsLoaded();
        return this.subItems.get(index);
    }

    public Action getAction() {
        if (this.action == null) {
            Object obj = this.library.getObject(this.entries, A_KEY);
            if (obj instanceof HashMap) {
                this.action = new Action(this.library, (HashMap) obj);
            }
        }
        return this.action;
    }

    public Reference getFirst() {
        if (this.first == null) {
            Object attribute = this.entries.get(FIRST_KEY);
            if (attribute instanceof Reference) {
                this.first = (Reference) attribute;
            }
        }
        return this.first;
    }

    public Reference getLast() {
        if (this.last == null) {
            Object attribute = this.entries.get(LAST_KEY);
            if (attribute instanceof Reference) {
                this.last = (Reference) attribute;
            }
        }
        return this.last;
    }

    public Reference getNext() {
        if (this.next == null) {
            Object attribute = this.entries.get(NEXT_KEY);
            if (attribute instanceof Reference) {
                this.next = (Reference) attribute;
            }
        }
        return this.next;
    }

    public Reference getPrev() {
        if (this.prev == null) {
            Object attribute = this.entries.get(PREV_KEY);
            if (attribute instanceof Reference) {
                this.prev = (Reference) attribute;
            }
        }
        return this.prev;
    }

    public Reference getParent() {
        if (this.parent == null) {
            Object attribute = this.entries.get(PARENT_KEY);
            if (attribute instanceof Reference) {
                this.parent = (Reference) attribute;
            }
        }
        return this.parent;
    }

    private int getCount() {
        if (this.count < 0) {
            this.count = this.library.getInt(this.entries, COUNT_KEY);
        }
        return this.count;
    }

    public String getTitle() {
        if (this.title == null) {
            Object obj = this.library.getObject(this.entries, TITLE_KEY);
            if (obj instanceof StringObject) {
                this.title = Utils.convertStringObject(this.library, (StringObject) obj);
            }
        }
        return this.title;
    }

    public Destination getDest() {
        Object obj;
        if (this.dest == null && (obj = this.library.getObject(this.entries, DEST_KEY)) != null) {
            this.dest = new Destination(this.library, obj);
        }
        return this.dest;
    }

    private void ensureSubItemsLoaded() {
        Object tmp;
        if (this.loadedSubItems) {
            return;
        }
        this.loadedSubItems = true;
        if (getFirst() != null) {
            Reference nextReference = getFirst();
            while (nextReference != null && (tmp = this.library.getObject(nextReference)) != null && (tmp instanceof HashMap)) {
                HashMap dictionary = (HashMap) tmp;
                OutlineItem outLineItem = new OutlineItem(this.library, dictionary);
                this.subItems.add(outLineItem);
                Reference oldNextReference = nextReference;
                nextReference = outLineItem.getNext();
                if (oldNextReference.equals(nextReference)) {
                    return;
                }
            }
        }
    }
}
