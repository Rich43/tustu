package org.apache.commons.net.nntp;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: commons-net-3.6.jar:org/apache/commons/net/nntp/Threader.class */
public class Threader {
    public Threadable thread(List<? extends Threadable> messages) {
        return thread((Iterable<? extends Threadable>) messages);
    }

    public Threadable thread(Iterable<? extends Threadable> messages) {
        if (messages == null) {
            return null;
        }
        HashMap<String, ThreadContainer> idTable = new HashMap<>();
        for (Threadable t2 : messages) {
            if (!t2.isDummy()) {
                buildContainer(t2, idTable);
            }
        }
        if (idTable.isEmpty()) {
            return null;
        }
        ThreadContainer root = findRootSet(idTable);
        idTable.clear();
        pruneEmptyContainers(root);
        root.reverseChildren();
        gatherSubjects(root);
        if (root.next != null) {
            throw new RuntimeException("root node has a next:" + ((Object) root));
        }
        ThreadContainer threadContainer = root.child;
        while (true) {
            ThreadContainer r2 = threadContainer;
            if (r2 == null) {
                break;
            }
            if (r2.threadable == null) {
                r2.threadable = r2.child.threadable.makeDummy();
            }
            threadContainer = r2.next;
        }
        Threadable result = root.child == null ? null : root.child.threadable;
        root.flush();
        return result;
    }

    private void buildContainer(Threadable threadable, HashMap<String, ThreadContainer> idTable) {
        ThreadContainer rest;
        String id = threadable.messageThreadId();
        ThreadContainer container = idTable.get(id);
        if (container != null) {
            if (container.threadable != null) {
                int bogusIdCount = 0 + 1;
                id = "<Bogus-id:" + bogusIdCount + ">";
                container = null;
            } else {
                container.threadable = threadable;
            }
        }
        if (container == null) {
            container = new ThreadContainer();
            container.threadable = threadable;
            idTable.put(id, container);
        }
        ThreadContainer parentRef = null;
        String[] references = threadable.messageThreadReferences();
        for (String refString : references) {
            ThreadContainer ref = idTable.get(refString);
            if (ref == null) {
                ref = new ThreadContainer();
                idTable.put(refString, ref);
            }
            if (parentRef != null && ref.parent == null && parentRef != ref && !ref.findChild(parentRef)) {
                ref.parent = parentRef;
                ref.next = parentRef.child;
                parentRef.child = ref;
            }
            parentRef = ref;
        }
        if (parentRef != null && (parentRef == container || container.findChild(parentRef))) {
            parentRef = null;
        }
        if (container.parent != null) {
            ThreadContainer prev = null;
            ThreadContainer threadContainer = container.parent.child;
            while (true) {
                rest = threadContainer;
                if (rest == null || rest == container) {
                    break;
                }
                prev = rest;
                threadContainer = rest.next;
            }
            if (rest == null) {
                throw new RuntimeException("Didnt find " + ((Object) container) + " in parent" + ((Object) container.parent));
            }
            if (prev == null) {
                container.parent.child = container.next;
            } else {
                prev.next = container.next;
            }
            container.next = null;
            container.parent = null;
        }
        if (parentRef != null) {
            container.parent = parentRef;
            container.next = parentRef.child;
            parentRef.child = container;
        }
    }

    private ThreadContainer findRootSet(HashMap<String, ThreadContainer> idTable) {
        ThreadContainer root = new ThreadContainer();
        for (Map.Entry<String, ThreadContainer> entry : idTable.entrySet()) {
            ThreadContainer c2 = entry.getValue();
            if (c2.parent == null) {
                if (c2.next != null) {
                    throw new RuntimeException("c.next is " + c2.next.toString());
                }
                c2.next = root.child;
                root.child = c2;
            }
        }
        return root;
    }

    private void pruneEmptyContainers(ThreadContainer parent) {
        ThreadContainer tail;
        ThreadContainer prev = null;
        ThreadContainer container = parent.child;
        ThreadContainer threadContainer = container.next;
        while (true) {
            ThreadContainer next = threadContainer;
            if (container != null) {
                if (container.threadable == null && container.child == null) {
                    if (prev == null) {
                        parent.child = container.next;
                    } else {
                        prev.next = container.next;
                    }
                    container = prev;
                } else if (container.threadable == null && container.child != null && (container.parent != null || container.child.next == null)) {
                    ThreadContainer kids = container.child;
                    if (prev == null) {
                        parent.child = kids;
                    } else {
                        prev.next = kids;
                    }
                    ThreadContainer threadContainer2 = kids;
                    while (true) {
                        tail = threadContainer2;
                        if (tail.next == null) {
                            break;
                        }
                        tail.parent = container.parent;
                        threadContainer2 = tail.next;
                    }
                    tail.parent = container.parent;
                    tail.next = container.next;
                    next = kids;
                    container = prev;
                } else if (container.child != null) {
                    pruneEmptyContainers(container);
                }
                prev = container;
                container = next;
                threadContainer = container == null ? null : container.next;
            } else {
                return;
            }
        }
    }

    private void gatherSubjects(ThreadContainer root) {
        ThreadContainer old;
        ThreadContainer tail;
        ThreadContainer old2;
        int count = 0;
        ThreadContainer threadContainer = root.child;
        while (true) {
            ThreadContainer c2 = threadContainer;
            if (c2 == null) {
                break;
            }
            count++;
            threadContainer = c2.next;
        }
        HashMap<String, ThreadContainer> subjectTable = new HashMap<>((int) (count * 1.2d), 0.9f);
        int count2 = 0;
        ThreadContainer threadContainer2 = root.child;
        while (true) {
            ThreadContainer c3 = threadContainer2;
            if (c3 == null) {
                break;
            }
            Threadable threadable = c3.threadable;
            if (threadable == null) {
                threadable = c3.child.threadable;
            }
            String subj = threadable.simplifiedSubject();
            if (subj != null && subj.length() != 0 && ((old2 = subjectTable.get(subj)) == null || ((c3.threadable == null && old2.threadable != null) || (old2.threadable != null && old2.threadable.subjectIsReply() && c3.threadable != null && !c3.threadable.subjectIsReply())))) {
                subjectTable.put(subj, c3);
                count2++;
            }
            threadContainer2 = c3.next;
        }
        if (count2 == 0) {
            return;
        }
        ThreadContainer prev = null;
        ThreadContainer c4 = root.child;
        ThreadContainer threadContainer3 = c4.next;
        while (true) {
            ThreadContainer rest = threadContainer3;
            if (c4 != null) {
                Threadable threadable2 = c4.threadable;
                if (threadable2 == null) {
                    threadable2 = c4.child.threadable;
                }
                String subj2 = threadable2.simplifiedSubject();
                if (subj2 != null && subj2.length() != 0 && (old = subjectTable.get(subj2)) != c4) {
                    if (prev == null) {
                        root.child = c4.next;
                    } else {
                        prev.next = c4.next;
                    }
                    c4.next = null;
                    if (old.threadable == null && c4.threadable == null) {
                        ThreadContainer threadContainer4 = old.child;
                        while (true) {
                            tail = threadContainer4;
                            if (tail == null || tail.next == null) {
                                break;
                            } else {
                                threadContainer4 = tail.next;
                            }
                        }
                        if (tail != null) {
                            tail.next = c4.child;
                        }
                        ThreadContainer threadContainer5 = c4.child;
                        while (true) {
                            ThreadContainer tail2 = threadContainer5;
                            if (tail2 == null) {
                                break;
                            }
                            tail2.parent = old;
                            threadContainer5 = tail2.next;
                        }
                        c4.child = null;
                    } else if (old.threadable == null || (c4.threadable != null && c4.threadable.subjectIsReply() && !old.threadable.subjectIsReply())) {
                        c4.parent = old;
                        c4.next = old.child;
                        old.child = c4;
                    } else {
                        ThreadContainer newc = new ThreadContainer();
                        newc.threadable = old.threadable;
                        newc.child = old.child;
                        ThreadContainer threadContainer6 = newc.child;
                        while (true) {
                            ThreadContainer tail3 = threadContainer6;
                            if (tail3 == null) {
                                break;
                            }
                            tail3.parent = newc;
                            threadContainer6 = tail3.next;
                        }
                        old.threadable = null;
                        old.child = null;
                        c4.parent = old;
                        newc.parent = old;
                        old.child = c4;
                        c4.next = newc;
                    }
                    c4 = prev;
                }
                prev = c4;
                c4 = rest;
                threadContainer3 = rest == null ? null : rest.next;
            } else {
                subjectTable.clear();
                return;
            }
        }
    }

    @Deprecated
    public Threadable thread(Threadable[] messages) {
        if (messages == null) {
            return null;
        }
        return thread(Arrays.asList(messages));
    }
}
