package java.nio.channels;

import java.nio.ByteBuffer;
import java.util.concurrent.Future;

/* loaded from: rt.jar:java/nio/channels/AsynchronousByteChannel.class */
public interface AsynchronousByteChannel extends AsynchronousChannel {
    <A> void read(ByteBuffer byteBuffer, A a2, CompletionHandler<Integer, ? super A> completionHandler);

    Future<Integer> read(ByteBuffer byteBuffer);

    <A> void write(ByteBuffer byteBuffer, A a2, CompletionHandler<Integer, ? super A> completionHandler);

    Future<Integer> write(ByteBuffer byteBuffer);
}
