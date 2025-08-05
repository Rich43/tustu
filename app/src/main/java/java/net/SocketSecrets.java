package java.net;

import java.io.IOException;

/* loaded from: rt.jar:java/net/SocketSecrets.class */
class SocketSecrets {
    SocketSecrets() {
    }

    private static <T> void setOption(Object obj, SocketOption<T> socketOption, T t2) throws IOException {
        SocketImpl impl;
        if (obj instanceof Socket) {
            impl = ((Socket) obj).getImpl();
        } else if (obj instanceof ServerSocket) {
            impl = ((ServerSocket) obj).getImpl();
        } else {
            throw new IllegalArgumentException();
        }
        impl.setOption((SocketOption<SocketOption<T>>) socketOption, (SocketOption<T>) t2);
    }

    private static <T> T getOption(Object obj, SocketOption<T> socketOption) throws IOException {
        SocketImpl impl;
        if (obj instanceof Socket) {
            impl = ((Socket) obj).getImpl();
        } else if (obj instanceof ServerSocket) {
            impl = ((ServerSocket) obj).getImpl();
        } else {
            throw new IllegalArgumentException();
        }
        return (T) impl.getOption(socketOption);
    }

    private static <T> void setOption(DatagramSocket datagramSocket, SocketOption<T> socketOption, T t2) throws IOException {
        datagramSocket.getImpl().setOption((SocketOption<SocketOption<T>>) socketOption, (SocketOption<T>) t2);
    }

    private static <T> T getOption(DatagramSocket datagramSocket, SocketOption<T> socketOption) throws IOException {
        return (T) datagramSocket.getImpl().getOption(socketOption);
    }
}
