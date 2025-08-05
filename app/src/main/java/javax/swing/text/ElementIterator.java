package javax.swing.text;

import java.util.Enumeration;
import java.util.Stack;

/* loaded from: rt.jar:javax/swing/text/ElementIterator.class */
public class ElementIterator implements Cloneable {
    private Element root;
    private Stack<StackItem> elementStack = null;

    /* loaded from: rt.jar:javax/swing/text/ElementIterator$StackItem.class */
    private class StackItem implements Cloneable {
        Element item;
        int childIndex;

        private StackItem(Element element) {
            this.item = element;
            this.childIndex = -1;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void incrementIndex() {
            this.childIndex++;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Element getElement() {
            return this.item;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getIndex() {
            return this.childIndex;
        }

        protected Object clone() throws CloneNotSupportedException {
            return super.clone();
        }
    }

    public ElementIterator(Document document) {
        this.root = document.getDefaultRootElement();
    }

    public ElementIterator(Element element) {
        this.root = element;
    }

    public synchronized Object clone() {
        try {
            ElementIterator elementIterator = new ElementIterator(this.root);
            if (this.elementStack != null) {
                elementIterator.elementStack = new Stack<>();
                for (int i2 = 0; i2 < this.elementStack.size(); i2++) {
                    elementIterator.elementStack.push((StackItem) this.elementStack.elementAt(i2).clone());
                }
            }
            return elementIterator;
        } catch (CloneNotSupportedException e2) {
            throw new InternalError(e2);
        }
    }

    public Element first() {
        if (this.root == null) {
            return null;
        }
        this.elementStack = new Stack<>();
        if (this.root.getElementCount() != 0) {
            this.elementStack.push(new StackItem(this.root));
        }
        return this.root;
    }

    public int depth() {
        if (this.elementStack == null) {
            return 0;
        }
        return this.elementStack.size();
    }

    public Element current() {
        if (this.elementStack == null) {
            return first();
        }
        if (!this.elementStack.empty()) {
            StackItem stackItemPeek = this.elementStack.peek();
            Element element = stackItemPeek.getElement();
            int index = stackItemPeek.getIndex();
            if (index == -1) {
                return element;
            }
            return element.getElement(index);
        }
        return null;
    }

    public Element next() {
        if (this.elementStack == null) {
            return first();
        }
        if (this.elementStack.isEmpty()) {
            return null;
        }
        StackItem stackItemPeek = this.elementStack.peek();
        Element element = stackItemPeek.getElement();
        int index = stackItemPeek.getIndex();
        if (index + 1 < element.getElementCount()) {
            Element element2 = element.getElement(index + 1);
            if (element2.isLeaf()) {
                stackItemPeek.incrementIndex();
            } else {
                this.elementStack.push(new StackItem(element2));
            }
            return element2;
        }
        this.elementStack.pop();
        if (!this.elementStack.isEmpty()) {
            this.elementStack.peek().incrementIndex();
            return next();
        }
        return null;
    }

    public Element previous() {
        int size;
        if (this.elementStack == null || (size = this.elementStack.size()) == 0) {
            return null;
        }
        StackItem stackItemPeek = this.elementStack.peek();
        Element element = stackItemPeek.getElement();
        int index = stackItemPeek.getIndex();
        if (index > 0) {
            return getDeepestLeaf(element.getElement(index - 1));
        }
        if (index == 0) {
            return element;
        }
        if (index != -1 || size == 1) {
            return null;
        }
        StackItem stackItemPop = this.elementStack.pop();
        StackItem stackItemPeek2 = this.elementStack.peek();
        this.elementStack.push(stackItemPop);
        Element element2 = stackItemPeek2.getElement();
        int index2 = stackItemPeek2.getIndex();
        return index2 == -1 ? element2 : getDeepestLeaf(element2.getElement(index2));
    }

    private Element getDeepestLeaf(Element element) {
        if (element.isLeaf()) {
            return element;
        }
        int elementCount = element.getElementCount();
        if (elementCount == 0) {
            return element;
        }
        return getDeepestLeaf(element.getElement(elementCount - 1));
    }

    private void dumpTree() {
        while (true) {
            Element next = next();
            if (next != null) {
                System.out.println("elem: " + next.getName());
                AttributeSet attributes = next.getAttributes();
                String str = "";
                Enumeration<?> attributeNames = attributes.getAttributeNames();
                while (attributeNames.hasMoreElements()) {
                    Object objNextElement2 = attributeNames.nextElement2();
                    Object attribute = attributes.getAttribute(objNextElement2);
                    if (attribute instanceof AttributeSet) {
                        str = str + objNextElement2 + "=**AttributeSet** ";
                    } else {
                        str = str + objNextElement2 + "=" + attribute + " ";
                    }
                }
                System.out.println("attributes: " + str);
            } else {
                return;
            }
        }
    }
}
