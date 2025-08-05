package java.util.concurrent;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.locks.LockSupport;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import sun.misc.Unsafe;

/* loaded from: rt.jar:java/util/concurrent/CompletableFuture.class */
public class CompletableFuture<T> implements Future<T>, CompletionStage<T> {
    volatile Object result;
    volatile Completion stack;
    static final AltResult NIL = new AltResult(null);
    private static final boolean useCommonPool;
    private static final Executor asyncPool;
    static final int SYNC = 0;
    static final int ASYNC = 1;
    static final int NESTED = -1;
    private static final int SPINS;
    private static final Unsafe UNSAFE;
    private static final long RESULT;
    private static final long STACK;
    private static final long NEXT;

    /* loaded from: rt.jar:java/util/concurrent/CompletableFuture$AsynchronousCompletionTask.class */
    public interface AsynchronousCompletionTask {
    }

    @Override // java.util.concurrent.CompletionStage
    public /* bridge */ /* synthetic */ CompletionStage runAfterEitherAsync(CompletionStage completionStage, Runnable runnable, Executor executor) {
        return runAfterEitherAsync((CompletionStage<?>) completionStage, runnable, executor);
    }

    @Override // java.util.concurrent.CompletionStage
    public /* bridge */ /* synthetic */ CompletionStage runAfterEitherAsync(CompletionStage completionStage, Runnable runnable) {
        return runAfterEitherAsync((CompletionStage<?>) completionStage, runnable);
    }

    @Override // java.util.concurrent.CompletionStage
    public /* bridge */ /* synthetic */ CompletionStage runAfterEither(CompletionStage completionStage, Runnable runnable) {
        return runAfterEither((CompletionStage<?>) completionStage, runnable);
    }

    @Override // java.util.concurrent.CompletionStage
    public /* bridge */ /* synthetic */ CompletionStage runAfterBothAsync(CompletionStage completionStage, Runnable runnable, Executor executor) {
        return runAfterBothAsync((CompletionStage<?>) completionStage, runnable, executor);
    }

    @Override // java.util.concurrent.CompletionStage
    public /* bridge */ /* synthetic */ CompletionStage runAfterBothAsync(CompletionStage completionStage, Runnable runnable) {
        return runAfterBothAsync((CompletionStage<?>) completionStage, runnable);
    }

    @Override // java.util.concurrent.CompletionStage
    public /* bridge */ /* synthetic */ CompletionStage runAfterBoth(CompletionStage completionStage, Runnable runnable) {
        return runAfterBoth((CompletionStage<?>) completionStage, runnable);
    }

    final boolean internalComplete(Object obj) {
        return UNSAFE.compareAndSwapObject(this, RESULT, null, obj);
    }

    final boolean casStack(Completion completion, Completion completion2) {
        return UNSAFE.compareAndSwapObject(this, STACK, completion, completion2);
    }

    final boolean tryPushStack(Completion completion) {
        Completion completion2 = this.stack;
        lazySetNext(completion, completion2);
        return UNSAFE.compareAndSwapObject(this, STACK, completion2, completion);
    }

    final void pushStack(Completion completion) {
        while (!tryPushStack(completion)) {
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/CompletableFuture$AltResult.class */
    static final class AltResult {
        final Throwable ex;

        AltResult(Throwable th) {
            this.ex = th;
        }
    }

    static {
        useCommonPool = ForkJoinPool.getCommonPoolParallelism() > 1;
        asyncPool = useCommonPool ? ForkJoinPool.commonPool() : new ThreadPerTaskExecutor();
        SPINS = Runtime.getRuntime().availableProcessors() > 1 ? 256 : 0;
        try {
            Unsafe unsafe = Unsafe.getUnsafe();
            UNSAFE = unsafe;
            RESULT = unsafe.objectFieldOffset(CompletableFuture.class.getDeclaredField("result"));
            STACK = unsafe.objectFieldOffset(CompletableFuture.class.getDeclaredField("stack"));
            NEXT = unsafe.objectFieldOffset(Completion.class.getDeclaredField(Constants.NEXT));
        } catch (Exception e2) {
            throw new Error(e2);
        }
    }

    final boolean completeNull() {
        return UNSAFE.compareAndSwapObject(this, RESULT, null, NIL);
    }

    final Object encodeValue(T t2) {
        return t2 == null ? NIL : t2;
    }

    final boolean completeValue(T t2) {
        return UNSAFE.compareAndSwapObject(this, RESULT, null, t2 == null ? NIL : t2);
    }

    static AltResult encodeThrowable(Throwable th) {
        return new AltResult(th instanceof CompletionException ? th : new CompletionException(th));
    }

    final boolean completeThrowable(Throwable th) {
        return UNSAFE.compareAndSwapObject(this, RESULT, null, encodeThrowable(th));
    }

    static Object encodeThrowable(Throwable th, Object obj) {
        if (!(th instanceof CompletionException)) {
            th = new CompletionException(th);
        } else if ((obj instanceof AltResult) && th == ((AltResult) obj).ex) {
            return obj;
        }
        return new AltResult(th);
    }

    final boolean completeThrowable(Throwable th, Object obj) {
        return UNSAFE.compareAndSwapObject(this, RESULT, null, encodeThrowable(th, obj));
    }

    Object encodeOutcome(T t2, Throwable th) {
        return th == null ? t2 == null ? NIL : t2 : encodeThrowable(th);
    }

    static Object encodeRelay(Object obj) {
        Throwable th;
        return (!(obj instanceof AltResult) || (th = ((AltResult) obj).ex) == null || (th instanceof CompletionException)) ? obj : new AltResult(new CompletionException(th));
    }

    final boolean completeRelay(Object obj) {
        return UNSAFE.compareAndSwapObject(this, RESULT, null, encodeRelay(obj));
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static <T> T reportGet(Object obj) throws ExecutionException, InterruptedException {
        Throwable cause;
        if (obj == 0) {
            throw new InterruptedException();
        }
        if (obj instanceof AltResult) {
            Throwable th = ((AltResult) obj).ex;
            Throwable th2 = th;
            if (th == null) {
                return null;
            }
            if (th2 instanceof CancellationException) {
                throw ((CancellationException) th2);
            }
            if ((th2 instanceof CompletionException) && (cause = th2.getCause()) != null) {
                th2 = cause;
            }
            throw new ExecutionException(th2);
        }
        return obj;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static <T> T reportJoin(Object obj) {
        if (obj instanceof AltResult) {
            Throwable th = ((AltResult) obj).ex;
            if (th == null) {
                return null;
            }
            if (th instanceof CancellationException) {
                throw ((CancellationException) th);
            }
            if (th instanceof CompletionException) {
                throw ((CompletionException) th);
            }
            throw new CompletionException(th);
        }
        return obj;
    }

    /* loaded from: rt.jar:java/util/concurrent/CompletableFuture$ThreadPerTaskExecutor.class */
    static final class ThreadPerTaskExecutor implements Executor {
        ThreadPerTaskExecutor() {
        }

        @Override // java.util.concurrent.Executor
        public void execute(Runnable runnable) {
            new Thread(runnable).start();
        }
    }

    static Executor screenExecutor(Executor executor) {
        if (!useCommonPool && executor == ForkJoinPool.commonPool()) {
            return asyncPool;
        }
        if (executor == null) {
            throw new NullPointerException();
        }
        return executor;
    }

    /* loaded from: rt.jar:java/util/concurrent/CompletableFuture$Completion.class */
    static abstract class Completion extends ForkJoinTask<Void> implements Runnable, AsynchronousCompletionTask {
        volatile Completion next;

        abstract CompletableFuture<?> tryFire(int i2);

        abstract boolean isLive();

        Completion() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            tryFire(1);
        }

        @Override // java.util.concurrent.ForkJoinTask
        public final boolean exec() {
            tryFire(1);
            return true;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.concurrent.ForkJoinTask
        public final Void getRawResult() {
            return null;
        }

        @Override // java.util.concurrent.ForkJoinTask
        public final void setRawResult(Void r2) {
        }
    }

    static void lazySetNext(Completion completion, Completion completion2) {
        UNSAFE.putOrderedObject(completion, NEXT, completion2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    final void postComplete() {
        CompletableFuture completableFuture = this;
        while (true) {
            Completion completion = completableFuture.stack;
            Completion completion2 = completion;
            if (completion == null) {
                if (completableFuture == this) {
                    return;
                }
                completableFuture = this;
                Completion completion3 = this.stack;
                completion2 = completion3;
                if (completion3 == null) {
                    return;
                }
            }
            Completion completion4 = completion2.next;
            if (completableFuture.casStack(completion2, completion4)) {
                if (completion4 != null) {
                    if (completableFuture != this) {
                        pushStack(completion2);
                    } else {
                        completion2.next = null;
                    }
                }
                CompletableFuture completableFutureTryFire = completion2.tryFire(-1);
                completableFuture = completableFutureTryFire == null ? this : completableFutureTryFire;
            }
        }
    }

    final void cleanStack() {
        Completion completion = null;
        Completion completion2 = this.stack;
        while (true) {
            Completion completion3 = completion2;
            if (completion3 != null) {
                Completion completion4 = completion3.next;
                if (completion3.isLive()) {
                    completion = completion3;
                    completion2 = completion4;
                } else if (completion == null) {
                    casStack(completion3, completion4);
                    completion2 = this.stack;
                } else {
                    completion.next = completion4;
                    if (completion.isLive()) {
                        completion2 = completion4;
                    } else {
                        completion = null;
                        completion2 = this.stack;
                    }
                }
            } else {
                return;
            }
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/CompletableFuture$UniCompletion.class */
    static abstract class UniCompletion<T, V> extends Completion {
        Executor executor;
        CompletableFuture<V> dep;
        CompletableFuture<T> src;

        UniCompletion(Executor executor, CompletableFuture<V> completableFuture, CompletableFuture<T> completableFuture2) {
            this.executor = executor;
            this.dep = completableFuture;
            this.src = completableFuture2;
        }

        final boolean claim() {
            Executor executor = this.executor;
            if (compareAndSetForkJoinTaskTag((short) 0, (short) 1)) {
                if (executor == null) {
                    return true;
                }
                this.executor = null;
                executor.execute(this);
                return false;
            }
            return false;
        }

        @Override // java.util.concurrent.CompletableFuture.Completion
        final boolean isLive() {
            return this.dep != null;
        }
    }

    final void push(UniCompletion<?, ?> uniCompletion) {
        if (uniCompletion != null) {
            while (this.result == null && !tryPushStack(uniCompletion)) {
                lazySetNext(uniCompletion, null);
            }
        }
    }

    final CompletableFuture<T> postFire(CompletableFuture<?> completableFuture, int i2) {
        if (completableFuture != null && completableFuture.stack != null) {
            if (i2 < 0 || completableFuture.result == null) {
                completableFuture.cleanStack();
            } else {
                completableFuture.postComplete();
            }
        }
        if (this.result != null && this.stack != null) {
            if (i2 < 0) {
                return this;
            }
            postComplete();
            return null;
        }
        return null;
    }

    /* loaded from: rt.jar:java/util/concurrent/CompletableFuture$UniApply.class */
    static final class UniApply<T, V> extends UniCompletion<T, V> {
        Function<? super T, ? extends V> fn;

        UniApply(Executor executor, CompletableFuture<V> completableFuture, CompletableFuture<T> completableFuture2, Function<? super T, ? extends V> function) {
            super(executor, completableFuture, completableFuture2);
            this.fn = function;
        }

        @Override // java.util.concurrent.CompletableFuture.Completion
        final CompletableFuture<V> tryFire(int i2) {
            CompletableFuture<V> completableFuture = this.dep;
            if (completableFuture != null) {
                CompletableFuture<T> completableFuture2 = this.src;
                if (!completableFuture.uniApply(completableFuture2, this.fn, i2 > 0 ? null : this)) {
                    return null;
                }
                this.dep = null;
                this.src = null;
                this.fn = null;
                return completableFuture.postFire(completableFuture2, i2);
            }
            return null;
        }
    }

    final <S> boolean uniApply(CompletableFuture<S> completableFuture, Function<? super S, ? extends T> function, UniApply<S, T> uniApply) {
        if (completableFuture == null) {
            return false;
        }
        Object obj = completableFuture.result;
        Object obj2 = obj;
        if (obj == null || function == null) {
            return false;
        }
        if (this.result == null) {
            if (obj2 instanceof AltResult) {
                Throwable th = ((AltResult) obj2).ex;
                if (th != null) {
                    completeThrowable(th, obj2);
                    return true;
                }
                obj2 = null;
            }
            if (uniApply != null) {
                try {
                    if (!uniApply.claim()) {
                        return false;
                    }
                } catch (Throwable th2) {
                    completeThrowable(th2);
                    return true;
                }
            }
            completeValue(function.apply(obj2));
            return true;
        }
        return true;
    }

    private <V> CompletableFuture<V> uniApplyStage(Executor executor, Function<? super T, ? extends V> function) {
        if (function == null) {
            throw new NullPointerException();
        }
        CompletableFuture<V> completableFuture = new CompletableFuture<>();
        if (executor != null || !completableFuture.uniApply(this, function, null)) {
            UniApply uniApply = new UniApply(executor, completableFuture, this, function);
            push(uniApply);
            uniApply.tryFire(0);
        }
        return completableFuture;
    }

    /* loaded from: rt.jar:java/util/concurrent/CompletableFuture$UniAccept.class */
    static final class UniAccept<T> extends UniCompletion<T, Void> {
        Consumer<? super T> fn;

        UniAccept(Executor executor, CompletableFuture<Void> completableFuture, CompletableFuture<T> completableFuture2, Consumer<? super T> consumer) {
            super(executor, completableFuture, completableFuture2);
            this.fn = consumer;
        }

        @Override // java.util.concurrent.CompletableFuture.Completion
        final CompletableFuture<Void> tryFire(int i2) {
            CompletableFuture<V> completableFuture = this.dep;
            if (completableFuture != 0) {
                CompletableFuture<T> completableFuture2 = this.src;
                if (!completableFuture.uniAccept(completableFuture2, this.fn, i2 > 0 ? null : this)) {
                    return null;
                }
                this.dep = null;
                this.src = null;
                this.fn = null;
                return completableFuture.postFire(completableFuture2, i2);
            }
            return null;
        }
    }

    final <S> boolean uniAccept(CompletableFuture<S> completableFuture, Consumer<? super S> consumer, UniAccept<S> uniAccept) {
        if (completableFuture == null) {
            return false;
        }
        Object obj = completableFuture.result;
        Object obj2 = obj;
        if (obj == null || consumer == null) {
            return false;
        }
        if (this.result == null) {
            if (obj2 instanceof AltResult) {
                Throwable th = ((AltResult) obj2).ex;
                if (th != null) {
                    completeThrowable(th, obj2);
                    return true;
                }
                obj2 = null;
            }
            if (uniAccept != null) {
                try {
                    if (!uniAccept.claim()) {
                        return false;
                    }
                } catch (Throwable th2) {
                    completeThrowable(th2);
                    return true;
                }
            }
            consumer.accept(obj2);
            completeNull();
            return true;
        }
        return true;
    }

    private CompletableFuture<Void> uniAcceptStage(Executor executor, Consumer<? super T> consumer) {
        if (consumer == null) {
            throw new NullPointerException();
        }
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();
        if (executor != null || !completableFuture.uniAccept(this, consumer, null)) {
            UniAccept uniAccept = new UniAccept(executor, completableFuture, this, consumer);
            push(uniAccept);
            uniAccept.tryFire(0);
        }
        return completableFuture;
    }

    /* loaded from: rt.jar:java/util/concurrent/CompletableFuture$UniRun.class */
    static final class UniRun<T> extends UniCompletion<T, Void> {
        Runnable fn;

        UniRun(Executor executor, CompletableFuture<Void> completableFuture, CompletableFuture<T> completableFuture2, Runnable runnable) {
            super(executor, completableFuture, completableFuture2);
            this.fn = runnable;
        }

        @Override // java.util.concurrent.CompletableFuture.Completion
        final CompletableFuture<Void> tryFire(int i2) {
            CompletableFuture<V> completableFuture = this.dep;
            if (completableFuture != 0) {
                CompletableFuture<T> completableFuture2 = this.src;
                if (!completableFuture.uniRun(completableFuture2, this.fn, i2 > 0 ? null : this)) {
                    return null;
                }
                this.dep = null;
                this.src = null;
                this.fn = null;
                return completableFuture.postFire(completableFuture2, i2);
            }
            return null;
        }
    }

    final boolean uniRun(CompletableFuture<?> completableFuture, Runnable runnable, UniRun<?> uniRun) {
        Object obj;
        Throwable th;
        if (completableFuture == null || (obj = completableFuture.result) == null || runnable == null) {
            return false;
        }
        if (this.result == null) {
            if ((obj instanceof AltResult) && (th = ((AltResult) obj).ex) != null) {
                completeThrowable(th, obj);
                return true;
            }
            if (uniRun != null) {
                try {
                    if (!uniRun.claim()) {
                        return false;
                    }
                } catch (Throwable th2) {
                    completeThrowable(th2);
                    return true;
                }
            }
            runnable.run();
            completeNull();
            return true;
        }
        return true;
    }

    private CompletableFuture<Void> uniRunStage(Executor executor, Runnable runnable) {
        if (runnable == null) {
            throw new NullPointerException();
        }
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();
        if (executor != null || !completableFuture.uniRun(this, runnable, null)) {
            UniRun uniRun = new UniRun(executor, completableFuture, this, runnable);
            push(uniRun);
            uniRun.tryFire(0);
        }
        return completableFuture;
    }

    /* loaded from: rt.jar:java/util/concurrent/CompletableFuture$UniWhenComplete.class */
    static final class UniWhenComplete<T> extends UniCompletion<T, T> {
        BiConsumer<? super T, ? super Throwable> fn;

        UniWhenComplete(Executor executor, CompletableFuture<T> completableFuture, CompletableFuture<T> completableFuture2, BiConsumer<? super T, ? super Throwable> biConsumer) {
            super(executor, completableFuture, completableFuture2);
            this.fn = biConsumer;
        }

        @Override // java.util.concurrent.CompletableFuture.Completion
        final CompletableFuture<T> tryFire(int i2) {
            CompletableFuture<V> completableFuture = this.dep;
            if (completableFuture != 0) {
                CompletableFuture<T> completableFuture2 = this.src;
                if (!completableFuture.uniWhenComplete(completableFuture2, this.fn, i2 > 0 ? null : this)) {
                    return null;
                }
                this.dep = null;
                this.src = null;
                this.fn = null;
                return completableFuture.postFire(completableFuture2, i2);
            }
            return null;
        }
    }

    final boolean uniWhenComplete(CompletableFuture<T> completableFuture, BiConsumer<? super T, ? super Throwable> biConsumer, UniWhenComplete<T> uniWhenComplete) {
        Object obj;
        Object obj2;
        Throwable th = null;
        if (completableFuture == null || (obj = completableFuture.result) == null || biConsumer == null) {
            return false;
        }
        if (this.result == null) {
            if (uniWhenComplete != null) {
                try {
                    if (!uniWhenComplete.claim()) {
                        return false;
                    }
                } catch (Throwable th2) {
                    if (th == null) {
                        th = th2;
                    }
                }
            }
            if (obj instanceof AltResult) {
                th = ((AltResult) obj).ex;
                obj2 = null;
            } else {
                obj2 = obj;
            }
            biConsumer.accept(obj2, th);
            if (th == null) {
                internalComplete(obj);
                return true;
            }
            completeThrowable(th, obj);
            return true;
        }
        return true;
    }

    private CompletableFuture<T> uniWhenCompleteStage(Executor executor, BiConsumer<? super T, ? super Throwable> biConsumer) {
        if (biConsumer == null) {
            throw new NullPointerException();
        }
        CompletableFuture<T> completableFuture = new CompletableFuture<>();
        if (executor != null || !completableFuture.uniWhenComplete(this, biConsumer, null)) {
            UniWhenComplete uniWhenComplete = new UniWhenComplete(executor, completableFuture, this, biConsumer);
            push(uniWhenComplete);
            uniWhenComplete.tryFire(0);
        }
        return completableFuture;
    }

    /* loaded from: rt.jar:java/util/concurrent/CompletableFuture$UniHandle.class */
    static final class UniHandle<T, V> extends UniCompletion<T, V> {
        BiFunction<? super T, Throwable, ? extends V> fn;

        UniHandle(Executor executor, CompletableFuture<V> completableFuture, CompletableFuture<T> completableFuture2, BiFunction<? super T, Throwable, ? extends V> biFunction) {
            super(executor, completableFuture, completableFuture2);
            this.fn = biFunction;
        }

        @Override // java.util.concurrent.CompletableFuture.Completion
        final CompletableFuture<V> tryFire(int i2) {
            CompletableFuture<V> completableFuture = this.dep;
            if (completableFuture != null) {
                CompletableFuture<T> completableFuture2 = this.src;
                if (!completableFuture.uniHandle(completableFuture2, this.fn, i2 > 0 ? null : this)) {
                    return null;
                }
                this.dep = null;
                this.src = null;
                this.fn = null;
                return completableFuture.postFire(completableFuture2, i2);
            }
            return null;
        }
    }

    final <S> boolean uniHandle(CompletableFuture<S> completableFuture, BiFunction<? super S, Throwable, ? extends T> biFunction, UniHandle<S, T> uniHandle) {
        Object obj;
        Throwable th;
        Object obj2;
        if (completableFuture == null || (obj = completableFuture.result) == null || biFunction == null) {
            return false;
        }
        if (this.result == null) {
            if (uniHandle != null) {
                try {
                    if (!uniHandle.claim()) {
                        return false;
                    }
                } catch (Throwable th2) {
                    completeThrowable(th2);
                    return true;
                }
            }
            if (obj instanceof AltResult) {
                th = ((AltResult) obj).ex;
                obj2 = null;
            } else {
                th = null;
                obj2 = obj;
            }
            completeValue(biFunction.apply(obj2, th));
            return true;
        }
        return true;
    }

    private <V> CompletableFuture<V> uniHandleStage(Executor executor, BiFunction<? super T, Throwable, ? extends V> biFunction) {
        if (biFunction == null) {
            throw new NullPointerException();
        }
        CompletableFuture<V> completableFuture = new CompletableFuture<>();
        if (executor != null || !completableFuture.uniHandle(this, biFunction, null)) {
            UniHandle uniHandle = new UniHandle(executor, completableFuture, this, biFunction);
            push(uniHandle);
            uniHandle.tryFire(0);
        }
        return completableFuture;
    }

    /* loaded from: rt.jar:java/util/concurrent/CompletableFuture$UniExceptionally.class */
    static final class UniExceptionally<T> extends UniCompletion<T, T> {
        Function<? super Throwable, ? extends T> fn;

        UniExceptionally(CompletableFuture<T> completableFuture, CompletableFuture<T> completableFuture2, Function<? super Throwable, ? extends T> function) {
            super(null, completableFuture, completableFuture2);
            this.fn = function;
        }

        @Override // java.util.concurrent.CompletableFuture.Completion
        final CompletableFuture<T> tryFire(int i2) {
            CompletableFuture<V> completableFuture = this.dep;
            if (completableFuture == 0) {
                return null;
            }
            CompletableFuture<T> completableFuture2 = this.src;
            if (!completableFuture.uniExceptionally(completableFuture2, this.fn, this)) {
                return null;
            }
            this.dep = null;
            this.src = null;
            this.fn = null;
            return completableFuture.postFire(completableFuture2, i2);
        }
    }

    final boolean uniExceptionally(CompletableFuture<T> completableFuture, Function<? super Throwable, ? extends T> function, UniExceptionally<T> uniExceptionally) {
        Object obj;
        Throwable th;
        if (completableFuture == null || (obj = completableFuture.result) == null || function == null) {
            return false;
        }
        if (this.result == null) {
            try {
                if ((obj instanceof AltResult) && (th = ((AltResult) obj).ex) != null) {
                    if (uniExceptionally != null && !uniExceptionally.claim()) {
                        return false;
                    }
                    completeValue(function.apply(th));
                } else {
                    internalComplete(obj);
                }
                return true;
            } catch (Throwable th2) {
                completeThrowable(th2);
                return true;
            }
        }
        return true;
    }

    private CompletableFuture<T> uniExceptionallyStage(Function<Throwable, ? extends T> function) {
        if (function == null) {
            throw new NullPointerException();
        }
        CompletableFuture<T> completableFuture = new CompletableFuture<>();
        if (!completableFuture.uniExceptionally(this, function, null)) {
            UniExceptionally uniExceptionally = new UniExceptionally(completableFuture, this, function);
            push(uniExceptionally);
            uniExceptionally.tryFire(0);
        }
        return completableFuture;
    }

    /* loaded from: rt.jar:java/util/concurrent/CompletableFuture$UniRelay.class */
    static final class UniRelay<T> extends UniCompletion<T, T> {
        UniRelay(CompletableFuture<T> completableFuture, CompletableFuture<T> completableFuture2) {
            super(null, completableFuture, completableFuture2);
        }

        @Override // java.util.concurrent.CompletableFuture.Completion
        final CompletableFuture<T> tryFire(int i2) {
            CompletableFuture<V> completableFuture = this.dep;
            if (completableFuture == 0) {
                return null;
            }
            CompletableFuture<T> completableFuture2 = this.src;
            if (!completableFuture.uniRelay(completableFuture2)) {
                return null;
            }
            this.src = null;
            this.dep = null;
            return completableFuture.postFire(completableFuture2, i2);
        }
    }

    final boolean uniRelay(CompletableFuture<T> completableFuture) {
        Object obj;
        if (completableFuture == null || (obj = completableFuture.result) == null) {
            return false;
        }
        if (this.result == null) {
            completeRelay(obj);
            return true;
        }
        return true;
    }

    /* loaded from: rt.jar:java/util/concurrent/CompletableFuture$UniCompose.class */
    static final class UniCompose<T, V> extends UniCompletion<T, V> {
        Function<? super T, ? extends CompletionStage<V>> fn;

        UniCompose(Executor executor, CompletableFuture<V> completableFuture, CompletableFuture<T> completableFuture2, Function<? super T, ? extends CompletionStage<V>> function) {
            super(executor, completableFuture, completableFuture2);
            this.fn = function;
        }

        @Override // java.util.concurrent.CompletableFuture.Completion
        final CompletableFuture<V> tryFire(int i2) {
            CompletableFuture<V> completableFuture = this.dep;
            if (completableFuture != null) {
                CompletableFuture<T> completableFuture2 = this.src;
                if (!completableFuture.uniCompose(completableFuture2, this.fn, i2 > 0 ? null : this)) {
                    return null;
                }
                this.dep = null;
                this.src = null;
                this.fn = null;
                return completableFuture.postFire(completableFuture2, i2);
            }
            return null;
        }
    }

    final <S> boolean uniCompose(CompletableFuture<S> completableFuture, Function<? super S, ? extends CompletionStage<T>> function, UniCompose<S, T> uniCompose) {
        if (completableFuture == null) {
            return false;
        }
        Object obj = completableFuture.result;
        Object obj2 = obj;
        if (obj == null || function == null) {
            return false;
        }
        if (this.result == null) {
            if (obj2 instanceof AltResult) {
                Throwable th = ((AltResult) obj2).ex;
                if (th != null) {
                    completeThrowable(th, obj2);
                    return true;
                }
                obj2 = null;
            }
            if (uniCompose != null) {
                try {
                    if (!uniCompose.claim()) {
                        return false;
                    }
                } catch (Throwable th2) {
                    completeThrowable(th2);
                    return true;
                }
            }
            CompletableFuture<T> completableFuture2 = function.apply(obj2).toCompletableFuture();
            if (completableFuture2.result == null || !uniRelay(completableFuture2)) {
                UniRelay uniRelay = new UniRelay(this, completableFuture2);
                completableFuture2.push(uniRelay);
                uniRelay.tryFire(0);
                if (this.result == null) {
                    return false;
                }
                return true;
            }
            return true;
        }
        return true;
    }

    private <V> CompletableFuture<V> uniComposeStage(Executor executor, Function<? super T, ? extends CompletionStage<V>> function) {
        if (function == null) {
            throw new NullPointerException();
        }
        if (executor == null) {
            Object obj = this.result;
            Object obj2 = obj;
            if (obj != null) {
                if (obj2 instanceof AltResult) {
                    Throwable th = ((AltResult) obj2).ex;
                    if (th != null) {
                        return new CompletableFuture<>(encodeThrowable(th, obj2));
                    }
                    obj2 = null;
                }
                try {
                    CompletableFuture<V> completableFuture = function.apply(obj2).toCompletableFuture();
                    Object obj3 = completableFuture.result;
                    if (obj3 != null) {
                        return new CompletableFuture<>(encodeRelay(obj3));
                    }
                    CompletableFuture<V> completableFuture2 = new CompletableFuture<>();
                    UniRelay uniRelay = new UniRelay(completableFuture2, completableFuture);
                    completableFuture.push(uniRelay);
                    uniRelay.tryFire(0);
                    return completableFuture2;
                } catch (Throwable th2) {
                    return new CompletableFuture<>(encodeThrowable(th2));
                }
            }
        }
        CompletableFuture<V> completableFuture3 = new CompletableFuture<>();
        UniCompose uniCompose = new UniCompose(executor, completableFuture3, this, function);
        push(uniCompose);
        uniCompose.tryFire(0);
        return completableFuture3;
    }

    /* loaded from: rt.jar:java/util/concurrent/CompletableFuture$BiCompletion.class */
    static abstract class BiCompletion<T, U, V> extends UniCompletion<T, V> {
        CompletableFuture<U> snd;

        BiCompletion(Executor executor, CompletableFuture<V> completableFuture, CompletableFuture<T> completableFuture2, CompletableFuture<U> completableFuture3) {
            super(executor, completableFuture, completableFuture2);
            this.snd = completableFuture3;
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/CompletableFuture$CoCompletion.class */
    static final class CoCompletion extends Completion {
        BiCompletion<?, ?, ?> base;

        CoCompletion(BiCompletion<?, ?, ?> biCompletion) {
            this.base = biCompletion;
        }

        @Override // java.util.concurrent.CompletableFuture.Completion
        final CompletableFuture<?> tryFire(int i2) {
            CompletableFuture<?> completableFutureTryFire;
            BiCompletion<?, ?, ?> biCompletion = this.base;
            if (biCompletion == null || (completableFutureTryFire = biCompletion.tryFire(i2)) == null) {
                return null;
            }
            this.base = null;
            return completableFutureTryFire;
        }

        @Override // java.util.concurrent.CompletableFuture.Completion
        final boolean isLive() {
            BiCompletion<?, ?, ?> biCompletion = this.base;
            return (biCompletion == null || biCompletion.dep == null) ? false : true;
        }
    }

    final void bipush(CompletableFuture<?> completableFuture, BiCompletion<?, ?, ?> biCompletion) {
        Object obj;
        if (biCompletion != null) {
            while (true) {
                obj = this.result;
                if (obj != null || tryPushStack(biCompletion)) {
                    break;
                } else {
                    lazySetNext(biCompletion, null);
                }
            }
            if (completableFuture != null && completableFuture != this && completableFuture.result == null) {
                Completion coCompletion = obj != null ? biCompletion : new CoCompletion(biCompletion);
                while (completableFuture.result == null && !completableFuture.tryPushStack(coCompletion)) {
                    lazySetNext(coCompletion, null);
                }
            }
        }
    }

    final CompletableFuture<T> postFire(CompletableFuture<?> completableFuture, CompletableFuture<?> completableFuture2, int i2) {
        if (completableFuture2 != null && completableFuture2.stack != null) {
            if (i2 < 0 || completableFuture2.result == null) {
                completableFuture2.cleanStack();
            } else {
                completableFuture2.postComplete();
            }
        }
        return postFire(completableFuture, i2);
    }

    /* loaded from: rt.jar:java/util/concurrent/CompletableFuture$BiApply.class */
    static final class BiApply<T, U, V> extends BiCompletion<T, U, V> {
        BiFunction<? super T, ? super U, ? extends V> fn;

        BiApply(Executor executor, CompletableFuture<V> completableFuture, CompletableFuture<T> completableFuture2, CompletableFuture<U> completableFuture3, BiFunction<? super T, ? super U, ? extends V> biFunction) {
            super(executor, completableFuture, completableFuture2, completableFuture3);
            this.fn = biFunction;
        }

        @Override // java.util.concurrent.CompletableFuture.Completion
        final CompletableFuture<V> tryFire(int i2) {
            CompletableFuture<V> completableFuture = this.dep;
            if (completableFuture != null) {
                CompletableFuture<T> completableFuture2 = this.src;
                CompletableFuture<U> completableFuture3 = this.snd;
                if (!completableFuture.biApply(completableFuture2, completableFuture3, this.fn, i2 > 0 ? null : this)) {
                    return null;
                }
                this.dep = null;
                this.src = null;
                this.snd = null;
                this.fn = null;
                return completableFuture.postFire(completableFuture2, completableFuture3, i2);
            }
            return null;
        }
    }

    final <R, S> boolean biApply(CompletableFuture<R> completableFuture, CompletableFuture<S> completableFuture2, BiFunction<? super R, ? super S, ? extends T> biFunction, BiApply<R, S, T> biApply) {
        if (completableFuture == null) {
            return false;
        }
        Object obj = completableFuture.result;
        Object obj2 = obj;
        if (obj == null || completableFuture2 == null) {
            return false;
        }
        Object obj3 = completableFuture2.result;
        Object obj4 = obj3;
        if (obj3 == null || biFunction == null) {
            return false;
        }
        if (this.result == null) {
            if (obj2 instanceof AltResult) {
                Throwable th = ((AltResult) obj2).ex;
                if (th != null) {
                    completeThrowable(th, obj2);
                    return true;
                }
                obj2 = null;
            }
            if (obj4 instanceof AltResult) {
                Throwable th2 = ((AltResult) obj4).ex;
                if (th2 != null) {
                    completeThrowable(th2, obj4);
                    return true;
                }
                obj4 = null;
            }
            if (biApply != null) {
                try {
                    if (!biApply.claim()) {
                        return false;
                    }
                } catch (Throwable th3) {
                    completeThrowable(th3);
                    return true;
                }
            }
            completeValue(biFunction.apply(obj2, obj4));
            return true;
        }
        return true;
    }

    private <U, V> CompletableFuture<V> biApplyStage(Executor executor, CompletionStage<U> completionStage, BiFunction<? super T, ? super U, ? extends V> biFunction) {
        CompletableFuture<?> completableFuture;
        if (biFunction == null || (completableFuture = completionStage.toCompletableFuture()) == null) {
            throw new NullPointerException();
        }
        CompletableFuture<V> completableFuture2 = new CompletableFuture<>();
        if (executor != null || !completableFuture2.biApply(this, completableFuture, biFunction, null)) {
            BiApply biApply = new BiApply(executor, completableFuture2, this, completableFuture, biFunction);
            bipush(completableFuture, biApply);
            biApply.tryFire(0);
        }
        return completableFuture2;
    }

    /* loaded from: rt.jar:java/util/concurrent/CompletableFuture$BiAccept.class */
    static final class BiAccept<T, U> extends BiCompletion<T, U, Void> {
        BiConsumer<? super T, ? super U> fn;

        BiAccept(Executor executor, CompletableFuture<Void> completableFuture, CompletableFuture<T> completableFuture2, CompletableFuture<U> completableFuture3, BiConsumer<? super T, ? super U> biConsumer) {
            super(executor, completableFuture, completableFuture2, completableFuture3);
            this.fn = biConsumer;
        }

        @Override // java.util.concurrent.CompletableFuture.Completion
        final CompletableFuture<Void> tryFire(int i2) {
            CompletableFuture<V> completableFuture = this.dep;
            if (completableFuture != 0) {
                CompletableFuture<T> completableFuture2 = this.src;
                CompletableFuture<U> completableFuture3 = this.snd;
                if (!completableFuture.biAccept(completableFuture2, completableFuture3, this.fn, i2 > 0 ? null : this)) {
                    return null;
                }
                this.dep = null;
                this.src = null;
                this.snd = null;
                this.fn = null;
                return completableFuture.postFire(completableFuture2, completableFuture3, i2);
            }
            return null;
        }
    }

    final <R, S> boolean biAccept(CompletableFuture<R> completableFuture, CompletableFuture<S> completableFuture2, BiConsumer<? super R, ? super S> biConsumer, BiAccept<R, S> biAccept) {
        if (completableFuture == null) {
            return false;
        }
        Object obj = completableFuture.result;
        Object obj2 = obj;
        if (obj == null || completableFuture2 == null) {
            return false;
        }
        Object obj3 = completableFuture2.result;
        Object obj4 = obj3;
        if (obj3 == null || biConsumer == null) {
            return false;
        }
        if (this.result == null) {
            if (obj2 instanceof AltResult) {
                Throwable th = ((AltResult) obj2).ex;
                if (th != null) {
                    completeThrowable(th, obj2);
                    return true;
                }
                obj2 = null;
            }
            if (obj4 instanceof AltResult) {
                Throwable th2 = ((AltResult) obj4).ex;
                if (th2 != null) {
                    completeThrowable(th2, obj4);
                    return true;
                }
                obj4 = null;
            }
            if (biAccept != null) {
                try {
                    if (!biAccept.claim()) {
                        return false;
                    }
                } catch (Throwable th3) {
                    completeThrowable(th3);
                    return true;
                }
            }
            biConsumer.accept(obj2, obj4);
            completeNull();
            return true;
        }
        return true;
    }

    private <U> CompletableFuture<Void> biAcceptStage(Executor executor, CompletionStage<U> completionStage, BiConsumer<? super T, ? super U> biConsumer) {
        CompletableFuture<?> completableFuture;
        if (biConsumer == null || (completableFuture = completionStage.toCompletableFuture()) == null) {
            throw new NullPointerException();
        }
        CompletableFuture<Void> completableFuture2 = new CompletableFuture<>();
        if (executor != null || !completableFuture2.biAccept(this, completableFuture, biConsumer, null)) {
            BiAccept biAccept = new BiAccept(executor, completableFuture2, this, completableFuture, biConsumer);
            bipush(completableFuture, biAccept);
            biAccept.tryFire(0);
        }
        return completableFuture2;
    }

    /* loaded from: rt.jar:java/util/concurrent/CompletableFuture$BiRun.class */
    static final class BiRun<T, U> extends BiCompletion<T, U, Void> {
        Runnable fn;

        BiRun(Executor executor, CompletableFuture<Void> completableFuture, CompletableFuture<T> completableFuture2, CompletableFuture<U> completableFuture3, Runnable runnable) {
            super(executor, completableFuture, completableFuture2, completableFuture3);
            this.fn = runnable;
        }

        @Override // java.util.concurrent.CompletableFuture.Completion
        final CompletableFuture<Void> tryFire(int i2) {
            CompletableFuture<V> completableFuture = this.dep;
            if (completableFuture != 0) {
                CompletableFuture<T> completableFuture2 = this.src;
                CompletableFuture<U> completableFuture3 = this.snd;
                if (!completableFuture.biRun(completableFuture2, completableFuture3, this.fn, i2 > 0 ? null : this)) {
                    return null;
                }
                this.dep = null;
                this.src = null;
                this.snd = null;
                this.fn = null;
                return completableFuture.postFire(completableFuture2, completableFuture3, i2);
            }
            return null;
        }
    }

    final boolean biRun(CompletableFuture<?> completableFuture, CompletableFuture<?> completableFuture2, Runnable runnable, BiRun<?, ?> biRun) {
        Object obj;
        Object obj2;
        Throwable th;
        Throwable th2;
        if (completableFuture == null || (obj = completableFuture.result) == null || completableFuture2 == null || (obj2 = completableFuture2.result) == null || runnable == null) {
            return false;
        }
        if (this.result == null) {
            if ((obj instanceof AltResult) && (th2 = ((AltResult) obj).ex) != null) {
                completeThrowable(th2, obj);
                return true;
            }
            if ((obj2 instanceof AltResult) && (th = ((AltResult) obj2).ex) != null) {
                completeThrowable(th, obj2);
                return true;
            }
            if (biRun != null) {
                try {
                    if (!biRun.claim()) {
                        return false;
                    }
                } catch (Throwable th3) {
                    completeThrowable(th3);
                    return true;
                }
            }
            runnable.run();
            completeNull();
            return true;
        }
        return true;
    }

    private CompletableFuture<Void> biRunStage(Executor executor, CompletionStage<?> completionStage, Runnable runnable) {
        CompletableFuture<?> completableFuture;
        if (runnable == null || (completableFuture = completionStage.toCompletableFuture()) == null) {
            throw new NullPointerException();
        }
        CompletableFuture<Void> completableFuture2 = new CompletableFuture<>();
        if (executor != null || !completableFuture2.biRun(this, completableFuture, runnable, null)) {
            BiRun biRun = new BiRun(executor, completableFuture2, this, completableFuture, runnable);
            bipush(completableFuture, biRun);
            biRun.tryFire(0);
        }
        return completableFuture2;
    }

    /* loaded from: rt.jar:java/util/concurrent/CompletableFuture$BiRelay.class */
    static final class BiRelay<T, U> extends BiCompletion<T, U, Void> {
        BiRelay(CompletableFuture<Void> completableFuture, CompletableFuture<T> completableFuture2, CompletableFuture<U> completableFuture3) {
            super(null, completableFuture, completableFuture2, completableFuture3);
        }

        @Override // java.util.concurrent.CompletableFuture.Completion
        final CompletableFuture<Void> tryFire(int i2) {
            CompletableFuture<V> completableFuture = this.dep;
            if (completableFuture == 0) {
                return null;
            }
            CompletableFuture<T> completableFuture2 = this.src;
            CompletableFuture<U> completableFuture3 = this.snd;
            if (!completableFuture.biRelay(completableFuture2, completableFuture3)) {
                return null;
            }
            this.src = null;
            this.snd = null;
            this.dep = null;
            return completableFuture.postFire(completableFuture2, completableFuture3, i2);
        }
    }

    boolean biRelay(CompletableFuture<?> completableFuture, CompletableFuture<?> completableFuture2) {
        Object obj;
        Object obj2;
        Throwable th;
        Throwable th2;
        if (completableFuture == null || (obj = completableFuture.result) == null || completableFuture2 == null || (obj2 = completableFuture2.result) == null) {
            return false;
        }
        if (this.result == null) {
            if ((obj instanceof AltResult) && (th2 = ((AltResult) obj).ex) != null) {
                completeThrowable(th2, obj);
                return true;
            }
            if ((obj2 instanceof AltResult) && (th = ((AltResult) obj2).ex) != null) {
                completeThrowable(th, obj2);
                return true;
            }
            completeNull();
            return true;
        }
        return true;
    }

    static CompletableFuture<Void> andTree(CompletableFuture<?>[] completableFutureArr, int i2, int i3) {
        CompletableFuture<?> completableFutureAndTree;
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();
        if (i2 > i3) {
            completableFuture.result = NIL;
        } else {
            int i4 = (i2 + i3) >>> 1;
            CompletableFuture<?> completableFutureAndTree2 = i2 == i4 ? completableFutureArr[i2] : andTree(completableFutureArr, i2, i4);
            CompletableFuture<?> completableFuture2 = completableFutureAndTree2;
            if (completableFutureAndTree2 != null) {
                if (i2 == i3) {
                    completableFutureAndTree = completableFuture2;
                } else {
                    completableFutureAndTree = i3 == i4 + 1 ? completableFutureArr[i3] : andTree(completableFutureArr, i4 + 1, i3);
                }
                CompletableFuture<?> completableFuture3 = completableFutureAndTree;
                if (completableFutureAndTree != null) {
                    if (!completableFuture.biRelay(completableFuture2, completableFuture3)) {
                        BiRelay biRelay = new BiRelay(completableFuture, completableFuture2, completableFuture3);
                        completableFuture2.bipush(completableFuture3, biRelay);
                        biRelay.tryFire(0);
                    }
                }
            }
            throw new NullPointerException();
        }
        return completableFuture;
    }

    final void orpush(CompletableFuture<?> completableFuture, BiCompletion<?, ?, ?> biCompletion) {
        if (biCompletion == null) {
            return;
        }
        while (true) {
            if ((completableFuture == null || completableFuture.result == null) && this.result == null) {
                if (tryPushStack(biCompletion)) {
                    if (completableFuture != null && completableFuture != this && completableFuture.result == null) {
                        CoCompletion coCompletion = new CoCompletion(biCompletion);
                        while (this.result == null && completableFuture.result == null && !completableFuture.tryPushStack(coCompletion)) {
                            lazySetNext(coCompletion, null);
                        }
                        return;
                    }
                    return;
                }
                lazySetNext(biCompletion, null);
            } else {
                return;
            }
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/CompletableFuture$OrApply.class */
    static final class OrApply<T, U extends T, V> extends BiCompletion<T, U, V> {
        Function<? super T, ? extends V> fn;

        OrApply(Executor executor, CompletableFuture<V> completableFuture, CompletableFuture<T> completableFuture2, CompletableFuture<U> completableFuture3, Function<? super T, ? extends V> function) {
            super(executor, completableFuture, completableFuture2, completableFuture3);
            this.fn = function;
        }

        @Override // java.util.concurrent.CompletableFuture.Completion
        final CompletableFuture<V> tryFire(int i2) {
            CompletableFuture<V> completableFuture = this.dep;
            if (completableFuture != null) {
                CompletableFuture<T> completableFuture2 = this.src;
                CompletableFuture<U> completableFuture3 = this.snd;
                if (!completableFuture.orApply(completableFuture2, completableFuture3, this.fn, i2 > 0 ? null : this)) {
                    return null;
                }
                this.dep = null;
                this.src = null;
                this.snd = null;
                this.fn = null;
                return completableFuture.postFire(completableFuture2, completableFuture3, i2);
            }
            return null;
        }
    }

    final <R, S extends R> boolean orApply(CompletableFuture<R> completableFuture, CompletableFuture<S> completableFuture2, Function<? super R, ? extends T> function, OrApply<R, S, T> orApply) {
        if (completableFuture == null || completableFuture2 == null) {
            return false;
        }
        Object obj = completableFuture.result;
        Object obj2 = obj;
        if (obj == null) {
            Object obj3 = completableFuture2.result;
            obj2 = obj3;
            if (obj3 == null) {
                return false;
            }
        }
        if (function == null) {
            return false;
        }
        if (this.result == null) {
            if (orApply != null) {
                try {
                    if (!orApply.claim()) {
                        return false;
                    }
                } catch (Throwable th) {
                    completeThrowable(th);
                    return true;
                }
            }
            if (obj2 instanceof AltResult) {
                Throwable th2 = ((AltResult) obj2).ex;
                if (th2 != null) {
                    completeThrowable(th2, obj2);
                    return true;
                }
                obj2 = null;
            }
            completeValue(function.apply(obj2));
            return true;
        }
        return true;
    }

    private <U extends T, V> CompletableFuture<V> orApplyStage(Executor executor, CompletionStage<U> completionStage, Function<? super T, ? extends V> function) {
        CompletableFuture<?> completableFuture;
        if (function == null || (completableFuture = completionStage.toCompletableFuture()) == null) {
            throw new NullPointerException();
        }
        CompletableFuture<V> completableFuture2 = new CompletableFuture<>();
        if (executor != null || !completableFuture2.orApply(this, completableFuture, function, null)) {
            OrApply orApply = new OrApply(executor, completableFuture2, this, completableFuture, function);
            orpush(completableFuture, orApply);
            orApply.tryFire(0);
        }
        return completableFuture2;
    }

    /* loaded from: rt.jar:java/util/concurrent/CompletableFuture$OrAccept.class */
    static final class OrAccept<T, U extends T> extends BiCompletion<T, U, Void> {
        Consumer<? super T> fn;

        OrAccept(Executor executor, CompletableFuture<Void> completableFuture, CompletableFuture<T> completableFuture2, CompletableFuture<U> completableFuture3, Consumer<? super T> consumer) {
            super(executor, completableFuture, completableFuture2, completableFuture3);
            this.fn = consumer;
        }

        @Override // java.util.concurrent.CompletableFuture.Completion
        final CompletableFuture<Void> tryFire(int i2) {
            CompletableFuture<V> completableFuture = this.dep;
            if (completableFuture != 0) {
                CompletableFuture<T> completableFuture2 = this.src;
                CompletableFuture<U> completableFuture3 = this.snd;
                if (!completableFuture.orAccept(completableFuture2, completableFuture3, this.fn, i2 > 0 ? null : this)) {
                    return null;
                }
                this.dep = null;
                this.src = null;
                this.snd = null;
                this.fn = null;
                return completableFuture.postFire(completableFuture2, completableFuture3, i2);
            }
            return null;
        }
    }

    final <R, S extends R> boolean orAccept(CompletableFuture<R> completableFuture, CompletableFuture<S> completableFuture2, Consumer<? super R> consumer, OrAccept<R, S> orAccept) {
        if (completableFuture == null || completableFuture2 == null) {
            return false;
        }
        Object obj = completableFuture.result;
        Object obj2 = obj;
        if (obj == null) {
            Object obj3 = completableFuture2.result;
            obj2 = obj3;
            if (obj3 == null) {
                return false;
            }
        }
        if (consumer == null) {
            return false;
        }
        if (this.result == null) {
            if (orAccept != null) {
                try {
                    if (!orAccept.claim()) {
                        return false;
                    }
                } catch (Throwable th) {
                    completeThrowable(th);
                    return true;
                }
            }
            if (obj2 instanceof AltResult) {
                Throwable th2 = ((AltResult) obj2).ex;
                if (th2 != null) {
                    completeThrowable(th2, obj2);
                    return true;
                }
                obj2 = null;
            }
            consumer.accept(obj2);
            completeNull();
            return true;
        }
        return true;
    }

    private <U extends T> CompletableFuture<Void> orAcceptStage(Executor executor, CompletionStage<U> completionStage, Consumer<? super T> consumer) {
        CompletableFuture<?> completableFuture;
        if (consumer == null || (completableFuture = completionStage.toCompletableFuture()) == null) {
            throw new NullPointerException();
        }
        CompletableFuture<Void> completableFuture2 = new CompletableFuture<>();
        if (executor != null || !completableFuture2.orAccept(this, completableFuture, consumer, null)) {
            OrAccept orAccept = new OrAccept(executor, completableFuture2, this, completableFuture, consumer);
            orpush(completableFuture, orAccept);
            orAccept.tryFire(0);
        }
        return completableFuture2;
    }

    /* loaded from: rt.jar:java/util/concurrent/CompletableFuture$OrRun.class */
    static final class OrRun<T, U> extends BiCompletion<T, U, Void> {
        Runnable fn;

        OrRun(Executor executor, CompletableFuture<Void> completableFuture, CompletableFuture<T> completableFuture2, CompletableFuture<U> completableFuture3, Runnable runnable) {
            super(executor, completableFuture, completableFuture2, completableFuture3);
            this.fn = runnable;
        }

        @Override // java.util.concurrent.CompletableFuture.Completion
        final CompletableFuture<Void> tryFire(int i2) {
            CompletableFuture<V> completableFuture = this.dep;
            if (completableFuture != 0) {
                CompletableFuture<T> completableFuture2 = this.src;
                CompletableFuture<U> completableFuture3 = this.snd;
                if (!completableFuture.orRun(completableFuture2, completableFuture3, this.fn, i2 > 0 ? null : this)) {
                    return null;
                }
                this.dep = null;
                this.src = null;
                this.snd = null;
                this.fn = null;
                return completableFuture.postFire(completableFuture2, completableFuture3, i2);
            }
            return null;
        }
    }

    final boolean orRun(CompletableFuture<?> completableFuture, CompletableFuture<?> completableFuture2, Runnable runnable, OrRun<?, ?> orRun) {
        Throwable th;
        if (completableFuture == null || completableFuture2 == null) {
            return false;
        }
        Object obj = completableFuture.result;
        Object obj2 = obj;
        if (obj == null) {
            Object obj3 = completableFuture2.result;
            obj2 = obj3;
            if (obj3 == null) {
                return false;
            }
        }
        if (runnable == null) {
            return false;
        }
        if (this.result == null) {
            if (orRun != null) {
                try {
                    if (!orRun.claim()) {
                        return false;
                    }
                } catch (Throwable th2) {
                    completeThrowable(th2);
                    return true;
                }
            }
            if ((obj2 instanceof AltResult) && (th = ((AltResult) obj2).ex) != null) {
                completeThrowable(th, obj2);
            } else {
                runnable.run();
                completeNull();
            }
            return true;
        }
        return true;
    }

    private CompletableFuture<Void> orRunStage(Executor executor, CompletionStage<?> completionStage, Runnable runnable) {
        CompletableFuture<?> completableFuture;
        if (runnable == null || (completableFuture = completionStage.toCompletableFuture()) == null) {
            throw new NullPointerException();
        }
        CompletableFuture<Void> completableFuture2 = new CompletableFuture<>();
        if (executor != null || !completableFuture2.orRun(this, completableFuture, runnable, null)) {
            OrRun orRun = new OrRun(executor, completableFuture2, this, completableFuture, runnable);
            orpush(completableFuture, orRun);
            orRun.tryFire(0);
        }
        return completableFuture2;
    }

    /* loaded from: rt.jar:java/util/concurrent/CompletableFuture$OrRelay.class */
    static final class OrRelay<T, U> extends BiCompletion<T, U, Object> {
        OrRelay(CompletableFuture<Object> completableFuture, CompletableFuture<T> completableFuture2, CompletableFuture<U> completableFuture3) {
            super(null, completableFuture, completableFuture2, completableFuture3);
        }

        @Override // java.util.concurrent.CompletableFuture.Completion
        final CompletableFuture<Object> tryFire(int i2) {
            CompletableFuture<V> completableFuture = this.dep;
            if (completableFuture == 0) {
                return null;
            }
            CompletableFuture<T> completableFuture2 = this.src;
            CompletableFuture<U> completableFuture3 = this.snd;
            if (!completableFuture.orRelay(completableFuture2, completableFuture3)) {
                return null;
            }
            this.src = null;
            this.snd = null;
            this.dep = null;
            return completableFuture.postFire(completableFuture2, completableFuture3, i2);
        }
    }

    final boolean orRelay(CompletableFuture<?> completableFuture, CompletableFuture<?> completableFuture2) {
        if (completableFuture == null || completableFuture2 == null) {
            return false;
        }
        Object obj = completableFuture.result;
        Object obj2 = obj;
        if (obj == null) {
            Object obj3 = completableFuture2.result;
            obj2 = obj3;
            if (obj3 == null) {
                return false;
            }
        }
        if (this.result == null) {
            completeRelay(obj2);
            return true;
        }
        return true;
    }

    static CompletableFuture<Object> orTree(CompletableFuture<?>[] completableFutureArr, int i2, int i3) {
        CompletableFuture<?> completableFutureOrTree;
        CompletableFuture<Object> completableFuture = new CompletableFuture<>();
        if (i2 <= i3) {
            int i4 = (i2 + i3) >>> 1;
            CompletableFuture<?> completableFutureOrTree2 = i2 == i4 ? completableFutureArr[i2] : orTree(completableFutureArr, i2, i4);
            CompletableFuture<?> completableFuture2 = completableFutureOrTree2;
            if (completableFutureOrTree2 != null) {
                if (i2 == i3) {
                    completableFutureOrTree = completableFuture2;
                } else {
                    completableFutureOrTree = i3 == i4 + 1 ? completableFutureArr[i3] : orTree(completableFutureArr, i4 + 1, i3);
                }
                CompletableFuture<?> completableFuture3 = completableFutureOrTree;
                if (completableFutureOrTree != null) {
                    if (!completableFuture.orRelay(completableFuture2, completableFuture3)) {
                        OrRelay orRelay = new OrRelay(completableFuture, completableFuture2, completableFuture3);
                        completableFuture2.orpush(completableFuture3, orRelay);
                        orRelay.tryFire(0);
                    }
                }
            }
            throw new NullPointerException();
        }
        return completableFuture;
    }

    /* loaded from: rt.jar:java/util/concurrent/CompletableFuture$AsyncSupply.class */
    static final class AsyncSupply<T> extends ForkJoinTask<Void> implements Runnable, AsynchronousCompletionTask {
        CompletableFuture<T> dep;
        Supplier<T> fn;

        AsyncSupply(CompletableFuture<T> completableFuture, Supplier<T> supplier) {
            this.dep = completableFuture;
            this.fn = supplier;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.concurrent.ForkJoinTask
        public final Void getRawResult() {
            return null;
        }

        @Override // java.util.concurrent.ForkJoinTask
        public final void setRawResult(Void r2) {
        }

        @Override // java.util.concurrent.ForkJoinTask
        public final boolean exec() {
            run();
            return true;
        }

        @Override // java.lang.Runnable
        public void run() {
            Supplier<T> supplier;
            CompletableFuture<T> completableFuture = this.dep;
            if (completableFuture != null && (supplier = this.fn) != null) {
                this.dep = null;
                this.fn = null;
                if (completableFuture.result == null) {
                    try {
                        completableFuture.completeValue(supplier.get());
                    } catch (Throwable th) {
                        completableFuture.completeThrowable(th);
                    }
                }
                completableFuture.postComplete();
            }
        }
    }

    static <U> CompletableFuture<U> asyncSupplyStage(Executor executor, Supplier<U> supplier) {
        if (supplier == null) {
            throw new NullPointerException();
        }
        CompletableFuture<U> completableFuture = new CompletableFuture<>();
        executor.execute(new AsyncSupply(completableFuture, supplier));
        return completableFuture;
    }

    /* loaded from: rt.jar:java/util/concurrent/CompletableFuture$AsyncRun.class */
    static final class AsyncRun extends ForkJoinTask<Void> implements Runnable, AsynchronousCompletionTask {
        CompletableFuture<Void> dep;
        Runnable fn;

        AsyncRun(CompletableFuture<Void> completableFuture, Runnable runnable) {
            this.dep = completableFuture;
            this.fn = runnable;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.concurrent.ForkJoinTask
        public final Void getRawResult() {
            return null;
        }

        @Override // java.util.concurrent.ForkJoinTask
        public final void setRawResult(Void r2) {
        }

        @Override // java.util.concurrent.ForkJoinTask
        public final boolean exec() {
            run();
            return true;
        }

        @Override // java.lang.Runnable
        public void run() {
            Runnable runnable;
            CompletableFuture<Void> completableFuture = this.dep;
            if (completableFuture != null && (runnable = this.fn) != null) {
                this.dep = null;
                this.fn = null;
                if (completableFuture.result == null) {
                    try {
                        runnable.run();
                        completableFuture.completeNull();
                    } catch (Throwable th) {
                        completableFuture.completeThrowable(th);
                    }
                }
                completableFuture.postComplete();
            }
        }
    }

    static CompletableFuture<Void> asyncRunStage(Executor executor, Runnable runnable) {
        if (runnable == null) {
            throw new NullPointerException();
        }
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();
        executor.execute(new AsyncRun(completableFuture, runnable));
        return completableFuture;
    }

    /* loaded from: rt.jar:java/util/concurrent/CompletableFuture$Signaller.class */
    static final class Signaller extends Completion implements ForkJoinPool.ManagedBlocker {
        long nanos;
        final long deadline;
        volatile int interruptControl;
        volatile Thread thread = Thread.currentThread();

        /*  JADX ERROR: Failed to decode insn: 0x003A: MOVE_MULTI
            java.lang.ArrayIndexOutOfBoundsException: arraycopy: source index -1 out of bounds for object array[6]
            	at java.base/java.lang.System.arraycopy(Native Method)
            	at jadx.plugins.input.java.data.code.StackState.insert(StackState.java:52)
            	at jadx.plugins.input.java.data.code.CodeDecodeState.insert(CodeDecodeState.java:137)
            	at jadx.plugins.input.java.data.code.JavaInsnsRegister.dup2x1(JavaInsnsRegister.java:313)
            	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
            	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
            	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
            	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:464)
            	at jadx.core.ProcessClass.process(ProcessClass.java:69)
            	at jadx.core.ProcessClass.generateCode(ProcessClass.java:117)
            	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
            	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
            	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
            */
        @Override // java.util.concurrent.ForkJoinPool.ManagedBlocker
        public boolean isReleasable() {
            /*
                r6 = this;
                r0 = r6
                java.lang.Thread r0 = r0.thread
                if (r0 != 0) goto L9
                r0 = 1
                return r0
                boolean r0 = java.lang.Thread.interrupted()
                if (r0 == 0) goto L1f
                r0 = r6
                int r0 = r0.interruptControl
                r7 = r0
                r0 = r6
                r1 = -1
                r0.interruptControl = r1
                r0 = r7
                if (r0 <= 0) goto L1f
                r0 = 1
                return r0
                r0 = r6
                long r0 = r0.deadline
                r1 = 0
                int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
                if (r0 == 0) goto L4a
                r0 = r6
                long r0 = r0.nanos
                r1 = 0
                int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
                if (r0 <= 0) goto L43
                r0 = r6
                r1 = r6
                long r1 = r1.deadline
                long r2 = java.lang.System.nanoTime()
                long r1 = r1 - r2
                // decode failed: arraycopy: source index -1 out of bounds for object array[6]
                r0.nanos = r1
                r0 = 0
                int r-1 = (r-1 > r0 ? 1 : (r-1 == r0 ? 0 : -1))
                if (r-1 > 0) goto L4a
                r0 = r6
                r1 = 0
                r0.thread = r1
                r0 = 1
                return r0
                r0 = 0
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.CompletableFuture.Signaller.isReleasable():boolean");
        }

        Signaller(boolean z2, long j2, long j3) {
            this.interruptControl = z2 ? 1 : 0;
            this.nanos = j2;
            this.deadline = j3;
        }

        @Override // java.util.concurrent.CompletableFuture.Completion
        final CompletableFuture<?> tryFire(int i2) {
            Thread thread = this.thread;
            if (thread != null) {
                this.thread = null;
                LockSupport.unpark(thread);
                return null;
            }
            return null;
        }

        @Override // java.util.concurrent.ForkJoinPool.ManagedBlocker
        public boolean block() {
            if (isReleasable()) {
                return true;
            }
            if (this.deadline == 0) {
                LockSupport.park(this);
            } else if (this.nanos > 0) {
                LockSupport.parkNanos(this, this.nanos);
            }
            return isReleasable();
        }

        @Override // java.util.concurrent.CompletableFuture.Completion
        final boolean isLive() {
            return this.thread != null;
        }
    }

    private Object waitingGet(boolean z2) {
        Signaller signaller = null;
        boolean zTryPushStack = false;
        int i2 = -1;
        while (true) {
            Object obj = this.result;
            Object obj2 = obj;
            if (obj == null) {
                if (i2 < 0) {
                    i2 = SPINS;
                } else if (i2 > 0) {
                    if (ThreadLocalRandom.nextSecondarySeed() >= 0) {
                        i2--;
                    }
                } else if (signaller == null) {
                    signaller = new Signaller(z2, 0L, 0L);
                } else if (!zTryPushStack) {
                    zTryPushStack = tryPushStack(signaller);
                } else {
                    if (z2 && signaller.interruptControl < 0) {
                        signaller.thread = null;
                        cleanStack();
                        return null;
                    }
                    if (signaller.thread != null && this.result == null) {
                        try {
                            ForkJoinPool.managedBlock(signaller);
                        } catch (InterruptedException e2) {
                            signaller.interruptControl = -1;
                        }
                    }
                }
            } else {
                if (signaller != null) {
                    signaller.thread = null;
                    if (signaller.interruptControl < 0) {
                        if (z2) {
                            obj2 = null;
                        } else {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
                postComplete();
                return obj2;
            }
        }
    }

    private Object timedGet(long j2) throws TimeoutException {
        if (Thread.interrupted()) {
            return null;
        }
        if (j2 <= 0) {
            throw new TimeoutException();
        }
        long jNanoTime = System.nanoTime() + j2;
        Signaller signaller = new Signaller(true, j2, jNanoTime == 0 ? 1L : jNanoTime);
        boolean zTryPushStack = false;
        while (true) {
            Object obj = this.result;
            Object obj2 = obj;
            if (obj == null) {
                if (!zTryPushStack) {
                    zTryPushStack = tryPushStack(signaller);
                } else {
                    if (signaller.interruptControl < 0 || signaller.nanos <= 0) {
                        break;
                    }
                    if (signaller.thread != null && this.result == null) {
                        try {
                            ForkJoinPool.managedBlock(signaller);
                        } catch (InterruptedException e2) {
                            signaller.interruptControl = -1;
                        }
                    }
                }
            } else {
                if (signaller.interruptControl < 0) {
                    obj2 = null;
                }
                signaller.thread = null;
                postComplete();
                return obj2;
            }
        }
        signaller.thread = null;
        cleanStack();
        if (signaller.interruptControl < 0) {
            return null;
        }
        throw new TimeoutException();
    }

    public CompletableFuture() {
    }

    private CompletableFuture(Object obj) {
        this.result = obj;
    }

    public static <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier) {
        return asyncSupplyStage(asyncPool, supplier);
    }

    public static <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier, Executor executor) {
        return asyncSupplyStage(screenExecutor(executor), supplier);
    }

    public static CompletableFuture<Void> runAsync(Runnable runnable) {
        return asyncRunStage(asyncPool, runnable);
    }

    public static CompletableFuture<Void> runAsync(Runnable runnable, Executor executor) {
        return asyncRunStage(screenExecutor(executor), runnable);
    }

    public static <U> CompletableFuture<U> completedFuture(U u2) {
        return new CompletableFuture<>(u2 == null ? NIL : u2);
    }

    @Override // java.util.concurrent.Future
    public boolean isDone() {
        return this.result != null;
    }

    @Override // java.util.concurrent.Future
    public T get() throws ExecutionException, InterruptedException {
        Object obj = this.result;
        return (T) reportGet(obj == null ? waitingGet(true) : obj);
    }

    @Override // java.util.concurrent.Future
    public T get(long j2, TimeUnit timeUnit) throws ExecutionException, InterruptedException, TimeoutException {
        long nanos = timeUnit.toNanos(j2);
        Object obj = this.result;
        return (T) reportGet(obj == null ? timedGet(nanos) : obj);
    }

    public T join() {
        Object obj = this.result;
        return (T) reportJoin(obj == null ? waitingGet(false) : obj);
    }

    public T getNow(T t2) {
        Object obj = this.result;
        return obj == null ? t2 : (T) reportJoin(obj);
    }

    public boolean complete(T t2) {
        boolean zCompleteValue = completeValue(t2);
        postComplete();
        return zCompleteValue;
    }

    public boolean completeExceptionally(Throwable th) {
        if (th == null) {
            throw new NullPointerException();
        }
        boolean zInternalComplete = internalComplete(new AltResult(th));
        postComplete();
        return zInternalComplete;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.concurrent.CompletionStage
    public <U> CompletableFuture<U> thenApply(Function<? super T, ? extends U> function) {
        return (CompletableFuture<U>) uniApplyStage(null, function);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.concurrent.CompletionStage
    public <U> CompletableFuture<U> thenApplyAsync(Function<? super T, ? extends U> function) {
        return (CompletableFuture<U>) uniApplyStage(asyncPool, function);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.concurrent.CompletionStage
    public <U> CompletableFuture<U> thenApplyAsync(Function<? super T, ? extends U> function, Executor executor) {
        return (CompletableFuture<U>) uniApplyStage(screenExecutor(executor), function);
    }

    @Override // java.util.concurrent.CompletionStage
    public CompletableFuture<Void> thenAccept(Consumer<? super T> consumer) {
        return uniAcceptStage(null, consumer);
    }

    @Override // java.util.concurrent.CompletionStage
    public CompletableFuture<Void> thenAcceptAsync(Consumer<? super T> consumer) {
        return uniAcceptStage(asyncPool, consumer);
    }

    @Override // java.util.concurrent.CompletionStage
    public CompletableFuture<Void> thenAcceptAsync(Consumer<? super T> consumer, Executor executor) {
        return uniAcceptStage(screenExecutor(executor), consumer);
    }

    @Override // java.util.concurrent.CompletionStage
    public CompletableFuture<Void> thenRun(Runnable runnable) {
        return uniRunStage(null, runnable);
    }

    @Override // java.util.concurrent.CompletionStage
    public CompletableFuture<Void> thenRunAsync(Runnable runnable) {
        return uniRunStage(asyncPool, runnable);
    }

    @Override // java.util.concurrent.CompletionStage
    public CompletableFuture<Void> thenRunAsync(Runnable runnable, Executor executor) {
        return uniRunStage(screenExecutor(executor), runnable);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.concurrent.CompletionStage
    public <U, V> CompletableFuture<V> thenCombine(CompletionStage<? extends U> completionStage, BiFunction<? super T, ? super U, ? extends V> biFunction) {
        return biApplyStage(null, completionStage, biFunction);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.concurrent.CompletionStage
    public <U, V> CompletableFuture<V> thenCombineAsync(CompletionStage<? extends U> completionStage, BiFunction<? super T, ? super U, ? extends V> biFunction) {
        return biApplyStage(asyncPool, completionStage, biFunction);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.concurrent.CompletionStage
    public <U, V> CompletableFuture<V> thenCombineAsync(CompletionStage<? extends U> completionStage, BiFunction<? super T, ? super U, ? extends V> biFunction, Executor executor) {
        return biApplyStage(screenExecutor(executor), completionStage, biFunction);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.concurrent.CompletionStage
    public <U> CompletableFuture<Void> thenAcceptBoth(CompletionStage<? extends U> completionStage, BiConsumer<? super T, ? super U> biConsumer) {
        return biAcceptStage(null, completionStage, biConsumer);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.concurrent.CompletionStage
    public <U> CompletableFuture<Void> thenAcceptBothAsync(CompletionStage<? extends U> completionStage, BiConsumer<? super T, ? super U> biConsumer) {
        return biAcceptStage(asyncPool, completionStage, biConsumer);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.concurrent.CompletionStage
    public <U> CompletableFuture<Void> thenAcceptBothAsync(CompletionStage<? extends U> completionStage, BiConsumer<? super T, ? super U> biConsumer, Executor executor) {
        return biAcceptStage(screenExecutor(executor), completionStage, biConsumer);
    }

    @Override // java.util.concurrent.CompletionStage
    public CompletableFuture<Void> runAfterBoth(CompletionStage<?> completionStage, Runnable runnable) {
        return biRunStage(null, completionStage, runnable);
    }

    @Override // java.util.concurrent.CompletionStage
    public CompletableFuture<Void> runAfterBothAsync(CompletionStage<?> completionStage, Runnable runnable) {
        return biRunStage(asyncPool, completionStage, runnable);
    }

    @Override // java.util.concurrent.CompletionStage
    public CompletableFuture<Void> runAfterBothAsync(CompletionStage<?> completionStage, Runnable runnable, Executor executor) {
        return biRunStage(screenExecutor(executor), completionStage, runnable);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.concurrent.CompletionStage
    public <U> CompletableFuture<U> applyToEither(CompletionStage<? extends T> completionStage, Function<? super T, U> function) {
        return (CompletableFuture<U>) orApplyStage(null, completionStage, function);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.concurrent.CompletionStage
    public <U> CompletableFuture<U> applyToEitherAsync(CompletionStage<? extends T> completionStage, Function<? super T, U> function) {
        return (CompletableFuture<U>) orApplyStage(asyncPool, completionStage, function);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.concurrent.CompletionStage
    public <U> CompletableFuture<U> applyToEitherAsync(CompletionStage<? extends T> completionStage, Function<? super T, U> function, Executor executor) {
        return (CompletableFuture<U>) orApplyStage(screenExecutor(executor), completionStage, function);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.concurrent.CompletionStage
    public CompletableFuture<Void> acceptEither(CompletionStage<? extends T> completionStage, Consumer<? super T> consumer) {
        return orAcceptStage(null, completionStage, consumer);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.concurrent.CompletionStage
    public CompletableFuture<Void> acceptEitherAsync(CompletionStage<? extends T> completionStage, Consumer<? super T> consumer) {
        return orAcceptStage(asyncPool, completionStage, consumer);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.concurrent.CompletionStage
    public CompletableFuture<Void> acceptEitherAsync(CompletionStage<? extends T> completionStage, Consumer<? super T> consumer, Executor executor) {
        return orAcceptStage(screenExecutor(executor), completionStage, consumer);
    }

    @Override // java.util.concurrent.CompletionStage
    public CompletableFuture<Void> runAfterEither(CompletionStage<?> completionStage, Runnable runnable) {
        return orRunStage(null, completionStage, runnable);
    }

    @Override // java.util.concurrent.CompletionStage
    public CompletableFuture<Void> runAfterEitherAsync(CompletionStage<?> completionStage, Runnable runnable) {
        return orRunStage(asyncPool, completionStage, runnable);
    }

    @Override // java.util.concurrent.CompletionStage
    public CompletableFuture<Void> runAfterEitherAsync(CompletionStage<?> completionStage, Runnable runnable, Executor executor) {
        return orRunStage(screenExecutor(executor), completionStage, runnable);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.concurrent.CompletionStage
    public <U> CompletableFuture<U> thenCompose(Function<? super T, ? extends CompletionStage<U>> function) {
        return (CompletableFuture<U>) uniComposeStage(null, function);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.concurrent.CompletionStage
    public <U> CompletableFuture<U> thenComposeAsync(Function<? super T, ? extends CompletionStage<U>> function) {
        return (CompletableFuture<U>) uniComposeStage(asyncPool, function);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.concurrent.CompletionStage
    public <U> CompletableFuture<U> thenComposeAsync(Function<? super T, ? extends CompletionStage<U>> function, Executor executor) {
        return (CompletableFuture<U>) uniComposeStage(screenExecutor(executor), function);
    }

    @Override // java.util.concurrent.CompletionStage
    public CompletableFuture<T> whenComplete(BiConsumer<? super T, ? super Throwable> biConsumer) {
        return uniWhenCompleteStage(null, biConsumer);
    }

    @Override // java.util.concurrent.CompletionStage
    public CompletableFuture<T> whenCompleteAsync(BiConsumer<? super T, ? super Throwable> biConsumer) {
        return uniWhenCompleteStage(asyncPool, biConsumer);
    }

    @Override // java.util.concurrent.CompletionStage
    public CompletableFuture<T> whenCompleteAsync(BiConsumer<? super T, ? super Throwable> biConsumer, Executor executor) {
        return uniWhenCompleteStage(screenExecutor(executor), biConsumer);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.concurrent.CompletionStage
    public <U> CompletableFuture<U> handle(BiFunction<? super T, Throwable, ? extends U> biFunction) {
        return (CompletableFuture<U>) uniHandleStage(null, biFunction);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.concurrent.CompletionStage
    public <U> CompletableFuture<U> handleAsync(BiFunction<? super T, Throwable, ? extends U> biFunction) {
        return (CompletableFuture<U>) uniHandleStage(asyncPool, biFunction);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.concurrent.CompletionStage
    public <U> CompletableFuture<U> handleAsync(BiFunction<? super T, Throwable, ? extends U> biFunction, Executor executor) {
        return (CompletableFuture<U>) uniHandleStage(screenExecutor(executor), biFunction);
    }

    @Override // java.util.concurrent.CompletionStage
    public CompletableFuture<T> toCompletableFuture() {
        return this;
    }

    @Override // java.util.concurrent.CompletionStage
    public CompletableFuture<T> exceptionally(Function<Throwable, ? extends T> function) {
        return uniExceptionallyStage(function);
    }

    public static CompletableFuture<Void> allOf(CompletableFuture<?>... completableFutureArr) {
        return andTree(completableFutureArr, 0, completableFutureArr.length - 1);
    }

    public static CompletableFuture<Object> anyOf(CompletableFuture<?>... completableFutureArr) {
        return orTree(completableFutureArr, 0, completableFutureArr.length - 1);
    }

    @Override // java.util.concurrent.Future
    public boolean cancel(boolean z2) {
        boolean z3 = this.result == null && internalComplete(new AltResult(new CancellationException()));
        postComplete();
        return z3 || isCancelled();
    }

    @Override // java.util.concurrent.Future
    public boolean isCancelled() {
        Object obj = this.result;
        return (obj instanceof AltResult) && (((AltResult) obj).ex instanceof CancellationException);
    }

    public boolean isCompletedExceptionally() {
        Object obj = this.result;
        return (obj instanceof AltResult) && obj != NIL;
    }

    public void obtrudeValue(T t2) {
        this.result = t2 == null ? NIL : t2;
        postComplete();
    }

    public void obtrudeException(Throwable th) {
        if (th == null) {
            throw new NullPointerException();
        }
        this.result = new AltResult(th);
        postComplete();
    }

    public int getNumberOfDependents() {
        int i2 = 0;
        Completion completion = this.stack;
        while (true) {
            Completion completion2 = completion;
            if (completion2 != null) {
                i2++;
                completion = completion2.next;
            } else {
                return i2;
            }
        }
    }

    public String toString() {
        String str;
        Object obj = this.result;
        StringBuilder sbAppend = new StringBuilder().append(super.toString());
        if (obj == null) {
            int numberOfDependents = getNumberOfDependents();
            str = numberOfDependents == 0 ? "[Not completed]" : "[Not completed, " + numberOfDependents + " dependents]";
        } else {
            str = (!(obj instanceof AltResult) || ((AltResult) obj).ex == null) ? "[Completed normally]" : "[Completed exceptionally]";
        }
        return sbAppend.append(str).toString();
    }
}
