package javax.swing.text.html.parser;

/* loaded from: rt.jar:javax/swing/text/html/parser/ContentModelState.class */
class ContentModelState {
    ContentModel model;
    long value;
    ContentModelState next;

    public ContentModelState(ContentModel contentModel) {
        this(contentModel, null, 0L);
    }

    ContentModelState(Object obj, ContentModelState contentModelState) {
        this(obj, contentModelState, 0L);
    }

    ContentModelState(Object obj, ContentModelState contentModelState, long j2) {
        this.model = (ContentModel) obj;
        this.next = contentModelState;
        this.value = j2;
    }

    public ContentModel getModel() {
        ContentModel contentModel = this.model;
        for (int i2 = 0; i2 < this.value; i2++) {
            if (contentModel.next != null) {
                contentModel = contentModel.next;
            } else {
                return null;
            }
        }
        return contentModel;
    }

    public boolean terminate() {
        switch (this.model.type) {
            case 38:
                int i2 = 0;
                for (ContentModel contentModel = (ContentModel) this.model.content; contentModel != null; contentModel = contentModel.next) {
                    if ((this.value & (1 << i2)) != 0 || contentModel.empty()) {
                        i2++;
                    } else {
                        return false;
                    }
                }
                return this.next == null || this.next.terminate();
            case 42:
            case 63:
                break;
            case 43:
                if (this.value == 0 && !this.model.empty()) {
                    return false;
                }
                break;
            case 44:
                ContentModel contentModel2 = (ContentModel) this.model.content;
                int i3 = 0;
                while (i3 < this.value) {
                    i3++;
                    contentModel2 = contentModel2.next;
                }
                while (contentModel2 != null && contentModel2.empty()) {
                    contentModel2 = contentModel2.next;
                }
                if (contentModel2 != null) {
                    return false;
                }
                return this.next == null || this.next.terminate();
            case 124:
                ContentModel contentModel3 = (ContentModel) this.model.content;
                while (true) {
                    ContentModel contentModel4 = contentModel3;
                    if (contentModel4 != null) {
                        if (contentModel4.empty()) {
                            return this.next == null || this.next.terminate();
                        }
                        contentModel3 = contentModel4.next;
                    } else {
                        return false;
                    }
                }
                break;
            default:
                return false;
        }
        return this.next == null || this.next.terminate();
    }

    public Element first() {
        switch (this.model.type) {
            case 38:
            case 42:
            case 63:
            case 124:
                return null;
            case 43:
                return this.model.first();
            case 44:
                ContentModel contentModel = (ContentModel) this.model.content;
                int i2 = 0;
                while (i2 < this.value) {
                    i2++;
                    contentModel = contentModel.next;
                }
                return contentModel.first();
            default:
                return this.model.first();
        }
    }

    public ContentModelState advance(Object obj) {
        switch (this.model.type) {
            case 38:
                boolean z2 = true;
                int i2 = 0;
                for (ContentModel contentModel = (ContentModel) this.model.content; contentModel != null; contentModel = contentModel.next) {
                    if ((this.value & (1 << i2)) == 0) {
                        if (contentModel.first(obj)) {
                            return new ContentModelState(contentModel, new ContentModelState(this.model, this.next, this.value | (1 << i2))).advance(obj);
                        }
                        if (!contentModel.empty()) {
                            z2 = false;
                        }
                    }
                    i2++;
                }
                if (z2 && this.next != null) {
                    return this.next.advance(obj);
                }
                return null;
            case 42:
                if (this.model.first(obj)) {
                    return new ContentModelState(this.model.content, this).advance(obj);
                }
                if (this.next != null) {
                    return this.next.advance(obj);
                }
                return null;
            case 43:
                if (this.model.first(obj)) {
                    return new ContentModelState(this.model.content, new ContentModelState(this.model, this.next, this.value + 1)).advance(obj);
                }
                if (this.value != 0 && this.next != null) {
                    return this.next.advance(obj);
                }
                return null;
            case 44:
                ContentModel contentModel2 = (ContentModel) this.model.content;
                int i3 = 0;
                while (i3 < this.value) {
                    i3++;
                    contentModel2 = contentModel2.next;
                }
                if (contentModel2.first(obj) || contentModel2.empty()) {
                    if (contentModel2.next == null) {
                        return new ContentModelState(contentModel2, this.next).advance(obj);
                    }
                    return new ContentModelState(contentModel2, new ContentModelState(this.model, this.next, this.value + 1)).advance(obj);
                }
                return null;
            case 63:
                if (this.model.first(obj)) {
                    return new ContentModelState(this.model.content, this.next).advance(obj);
                }
                if (this.next != null) {
                    return this.next.advance(obj);
                }
                return null;
            case 124:
                ContentModel contentModel3 = (ContentModel) this.model.content;
                while (true) {
                    ContentModel contentModel4 = contentModel3;
                    if (contentModel4 != null) {
                        if (!contentModel4.first(obj)) {
                            contentModel3 = contentModel4.next;
                        } else {
                            return new ContentModelState(contentModel4, this.next).advance(obj);
                        }
                    } else {
                        return null;
                    }
                }
            default:
                if (this.model.content == obj) {
                    if (this.next == null && (obj instanceof Element) && ((Element) obj).content != null) {
                        return new ContentModelState(((Element) obj).content);
                    }
                    return this.next;
                }
                return null;
        }
    }
}
