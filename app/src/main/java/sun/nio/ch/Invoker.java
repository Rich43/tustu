package sun.nio.ch;

import java.nio.channels.AsynchronousChannel;
import java.nio.channels.CompletionHandler;
import java.nio.channels.ShutdownChannelGroupException;
import java.security.AccessController;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import sun.misc.InnocuousThread;
import sun.security.action.GetIntegerAction;

/* loaded from: rt.jar:sun/nio/ch/Invoker.class */
class Invoker {
    private static final int maxHandlerInvokeCount;
    private static final ThreadLocal<GroupAndInvokeCount> myGroupAndInvokeCount;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !Invoker.class.desiredAssertionStatus();
        maxHandlerInvokeCount = ((Integer) AccessController.doPrivileged(new GetIntegerAction("sun.nio.ch.maxCompletionHandlersOnStack", 16))).intValue();
        myGroupAndInvokeCount = new ThreadLocal<GroupAndInvokeCount>() { // from class: sun.nio.ch.Invoker.1
            /* JADX INFO: Access modifiers changed from: protected */
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.lang.ThreadLocal
            public GroupAndInvokeCount initialValue() {
                return null;
            }
        };
    }

    private Invoker() {
    }

    /* loaded from: rt.jar:sun/nio/ch/Invoker$GroupAndInvokeCount.class */
    static class GroupAndInvokeCount {
        private final AsynchronousChannelGroupImpl group;
        private int handlerInvokeCount;

        GroupAndInvokeCount(AsynchronousChannelGroupImpl asynchronousChannelGroupImpl) {
            this.group = asynchronousChannelGroupImpl;
        }

        AsynchronousChannelGroupImpl group() {
            return this.group;
        }

        int invokeCount() {
            return this.handlerInvokeCount;
        }

        void setInvokeCount(int i2) {
            this.handlerInvokeCount = i2;
        }

        void resetInvokeCount() {
            this.handlerInvokeCount = 0;
        }

        void incrementInvokeCount() {
            this.handlerInvokeCount++;
        }
    }

    static void bindToGroup(AsynchronousChannelGroupImpl asynchronousChannelGroupImpl) {
        myGroupAndInvokeCount.set(new GroupAndInvokeCount(asynchronousChannelGroupImpl));
    }

    static GroupAndInvokeCount getGroupAndInvokeCount() {
        return myGroupAndInvokeCount.get();
    }

    static boolean isBoundToAnyGroup() {
        return myGroupAndInvokeCount.get() != null;
    }

    static boolean mayInvokeDirect(GroupAndInvokeCount groupAndInvokeCount, AsynchronousChannelGroupImpl asynchronousChannelGroupImpl) {
        if (groupAndInvokeCount != null && groupAndInvokeCount.group() == asynchronousChannelGroupImpl && groupAndInvokeCount.invokeCount() < maxHandlerInvokeCount) {
            return true;
        }
        return false;
    }

    /* JADX WARN: Multi-variable type inference failed */
    static <V, A> void invokeUnchecked(CompletionHandler<V, ? super A> completionHandler, A a2, V v2, Throwable th) {
        if (th == null) {
            completionHandler.completed(v2, a2);
        } else {
            completionHandler.failed(th, a2);
        }
        Thread.interrupted();
        if (System.getSecurityManager() != null) {
            Thread threadCurrentThread = Thread.currentThread();
            if (threadCurrentThread instanceof InnocuousThread) {
                GroupAndInvokeCount groupAndInvokeCount = myGroupAndInvokeCount.get();
                ((InnocuousThread) threadCurrentThread).eraseThreadLocals();
                if (groupAndInvokeCount != null) {
                    myGroupAndInvokeCount.set(groupAndInvokeCount);
                }
            }
        }
    }

    static <V, A> void invokeDirect(GroupAndInvokeCount groupAndInvokeCount, CompletionHandler<V, ? super A> completionHandler, A a2, V v2, Throwable th) {
        groupAndInvokeCount.incrementInvokeCount();
        invokeUnchecked(completionHandler, a2, v2, th);
    }

    static <V, A> void invoke(AsynchronousChannel asynchronousChannel, CompletionHandler<V, ? super A> completionHandler, A a2, V v2, Throwable th) {
        boolean z2 = false;
        boolean z3 = false;
        GroupAndInvokeCount groupAndInvokeCount = myGroupAndInvokeCount.get();
        if (groupAndInvokeCount != null) {
            if (groupAndInvokeCount.group() == ((Groupable) asynchronousChannel).group()) {
                z3 = true;
            }
            if (z3 && groupAndInvokeCount.invokeCount() < maxHandlerInvokeCount) {
                z2 = true;
            }
        }
        if (z2) {
            invokeDirect(groupAndInvokeCount, completionHandler, a2, v2, th);
            return;
        }
        try {
            invokeIndirectly(asynchronousChannel, completionHandler, a2, v2, th);
        } catch (RejectedExecutionException e2) {
            if (z3) {
                invokeDirect(groupAndInvokeCount, completionHandler, a2, v2, th);
                return;
            }
            throw new ShutdownChannelGroupException();
        }
    }

    static <V, A> void invokeIndirectly(AsynchronousChannel asynchronousChannel, final CompletionHandler<V, ? super A> completionHandler, final A a2, final V v2, final Throwable th) {
        try {
            ((Groupable) asynchronousChannel).group().executeOnPooledThread(new Runnable() { // from class: sun.nio.ch.Invoker.2
                @Override // java.lang.Runnable
                public void run() {
                    GroupAndInvokeCount groupAndInvokeCount = (GroupAndInvokeCount) Invoker.myGroupAndInvokeCount.get();
                    if (groupAndInvokeCount != null) {
                        groupAndInvokeCount.setInvokeCount(1);
                    }
                    Invoker.invokeUnchecked(completionHandler, a2, v2, th);
                }
            });
        } catch (RejectedExecutionException e2) {
            throw new ShutdownChannelGroupException();
        }
    }

    static <V, A> void invokeIndirectly(final CompletionHandler<V, ? super A> completionHandler, final A a2, final V v2, final Throwable th, Executor executor) {
        try {
            executor.execute(new Runnable() { // from class: sun.nio.ch.Invoker.3
                @Override // java.lang.Runnable
                public void run() {
                    Invoker.invokeUnchecked(completionHandler, a2, v2, th);
                }
            });
        } catch (RejectedExecutionException e2) {
            throw new ShutdownChannelGroupException();
        }
    }

    static void invokeOnThreadInThreadPool(Groupable groupable, Runnable runnable) {
        boolean z2;
        GroupAndInvokeCount groupAndInvokeCount = myGroupAndInvokeCount.get();
        AsynchronousChannelGroupImpl asynchronousChannelGroupImplGroup = groupable.group();
        if (groupAndInvokeCount != null) {
            z2 = groupAndInvokeCount.group == asynchronousChannelGroupImplGroup;
        } else {
            z2 = false;
        }
        try {
            if (z2) {
                runnable.run();
            } else {
                asynchronousChannelGroupImplGroup.executeOnPooledThread(runnable);
            }
        } catch (RejectedExecutionException e2) {
            throw new ShutdownChannelGroupException();
        }
    }

    static <V, A> void invokeUnchecked(PendingFuture<V, A> pendingFuture) {
        if (!$assertionsDisabled && !pendingFuture.isDone()) {
            throw new AssertionError();
        }
        CompletionHandler<V, ? super A> completionHandlerHandler = pendingFuture.handler();
        if (completionHandlerHandler != null) {
            invokeUnchecked(completionHandlerHandler, pendingFuture.attachment(), pendingFuture.value(), pendingFuture.exception());
        }
    }

    static <V, A> void invoke(PendingFuture<V, A> pendingFuture) {
        if (!$assertionsDisabled && !pendingFuture.isDone()) {
            throw new AssertionError();
        }
        CompletionHandler<V, ? super A> completionHandlerHandler = pendingFuture.handler();
        if (completionHandlerHandler != null) {
            invoke(pendingFuture.channel(), completionHandlerHandler, pendingFuture.attachment(), pendingFuture.value(), pendingFuture.exception());
        }
    }

    static <V, A> void invokeIndirectly(PendingFuture<V, A> pendingFuture) {
        if (!$assertionsDisabled && !pendingFuture.isDone()) {
            throw new AssertionError();
        }
        CompletionHandler<V, ? super A> completionHandlerHandler = pendingFuture.handler();
        if (completionHandlerHandler != null) {
            invokeIndirectly(pendingFuture.channel(), completionHandlerHandler, pendingFuture.attachment(), pendingFuture.value(), pendingFuture.exception());
        }
    }
}
