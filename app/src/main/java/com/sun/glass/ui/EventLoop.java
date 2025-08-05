package com.sun.glass.ui;

import java.util.ArrayDeque;
import java.util.Deque;

/* loaded from: jfxrt.jar:com/sun/glass/ui/EventLoop.class */
public final class EventLoop {
    private static final Deque<EventLoop> stack;
    private State state = State.IDLE;
    private Object returnValue;
    static final /* synthetic */ boolean $assertionsDisabled;

    /* loaded from: jfxrt.jar:com/sun/glass/ui/EventLoop$State.class */
    public enum State {
        IDLE,
        ACTIVE,
        LEAVING
    }

    static {
        $assertionsDisabled = !EventLoop.class.desiredAssertionStatus();
        stack = new ArrayDeque();
    }

    EventLoop() {
        Application.checkEventThread();
    }

    public State getState() {
        Application.checkEventThread();
        return this.state;
    }

    public Object enter() {
        Application.checkEventThread();
        if (!this.state.equals(State.IDLE)) {
            throw new IllegalStateException("The event loop object isn't idle");
        }
        this.state = State.ACTIVE;
        stack.push(this);
        try {
            Object ret = Application.enterNestedEventLoop();
            if (!$assertionsDisabled && ret != this) {
                throw new AssertionError((Object) "Internal inconsistency - wrong EventLoop");
            }
            if (!$assertionsDisabled && stack.peek() != this) {
                throw new AssertionError((Object) "Internal inconsistency - corrupted event loops stack");
            }
            if ($assertionsDisabled || this.state.equals(State.LEAVING)) {
                return this.returnValue;
            }
            throw new AssertionError((Object) "The event loop isn't leaving");
        } finally {
            this.returnValue = null;
            this.state = State.IDLE;
            stack.pop();
            if (!stack.isEmpty() && stack.peek().state.equals(State.LEAVING)) {
                Application.invokeLater(() -> {
                    EventLoop loop = stack.peek();
                    if (loop != null && loop.state.equals(State.LEAVING)) {
                        Application.leaveNestedEventLoop(loop);
                    }
                });
            }
        }
    }

    public void leave(Object ret) {
        Application.checkEventThread();
        if (!this.state.equals(State.ACTIVE)) {
            throw new IllegalStateException("The event loop object isn't active");
        }
        this.state = State.LEAVING;
        this.returnValue = ret;
        if (stack.peek() == this) {
            Application.leaveNestedEventLoop(this);
        }
    }
}
