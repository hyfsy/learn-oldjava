package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author baB_hyf
 * @date 2022/03/29
 */
public class Reactor implements Runnable {

    final Selector            selector;
    final ServerSocketChannel serverSocket;

    // Multiple Reactor Threads
    Selector[] selectors; // also create threads
    int        next = 0;

    public Reactor(int port) throws IOException {
        selector = Selector.open();
        serverSocket = ServerSocketChannel.open();
        serverSocket.socket().bind(new InetSocketAddress(port));
        serverSocket.configureBlocking(false);
        SelectionKey sk = serverSocket.register(selector, SelectionKey.OP_ACCEPT);
        sk.attach(new Acceptor());

        // Alternatively, use explicit SPI provider:
        // SelectorProvider p = SelectorProvider.provider();
        // selector = p.openSelector();
        // serverSocket = p.openServerSocketChannel();
    }

    public Reactor(int port, int parallelLevel) throws IOException {
        selectors = new Selector[parallelLevel];
        selector = Selector.open();
        serverSocket = ServerSocketChannel.open();
        serverSocket.socket().bind(new InetSocketAddress(port));
        serverSocket.configureBlocking(false);
        SelectionKey sk = serverSocket.register(selector, SelectionKey.OP_ACCEPT);
        sk.attach(new ParallelAcceptor());

        // Alternatively, use explicit SPI provider:
        // SelectorProvider p = SelectorProvider.provider();
        // selector = p.openSelector();
        // serverSocket = p.openServerSocketChannel();
    }

    @Override
    public void run() { // normally in a new Thread
        try {
            while (!Thread.interrupted()) {
                selector.select();
                Set<SelectionKey> selected = selector.selectedKeys();
                Iterator<SelectionKey> it = selected.iterator();
                while (it.hasNext()) {
                    dispatch(it.next());
                }
                selected.clear();
            }
        } catch (IOException ex) { /* ... */ }
    }

    void dispatch(SelectionKey k) {
        Runnable r = (Runnable) (k.attachment());
        if (r != null) {
            r.run();
        }
    }

    static class Handler implements Runnable {

        public static final int MAX_IN  = 2048;
        public static final int MAX_OUT = 2048;

        static final Executor pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        static final int READING = 0, SENDING = 1, PROCESSING = 2;
        final SocketChannel socket;
        final SelectionKey  sk;
        ByteBuffer input  = ByteBuffer.allocate(MAX_IN);
        ByteBuffer output = ByteBuffer.allocate(MAX_OUT);
        int        state  = READING;

        Handler(Selector sel, SocketChannel c) throws IOException {
            socket = c;
            c.configureBlocking(false); // Optionally try first read now
            sk = socket.register(sel, 0);
            sk.attach(this);
            sk.interestOps(SelectionKey.OP_READ);
            sel.wakeup();
        }

        boolean inputIsComplete() { /* ... */
            return true;
        }

        boolean outputIsComplete() { /* ... */
            return true;
        }

        void process() { /* ... */ }

        // Handler
        // @Override
        // public void run() {
        //     try {
        //         if (state == READING) {
        //             read();
        //         }
        //         else if (state == SENDING) {
        //             send();
        //         }
        //     } catch (IOException ex) { /* ... */ }
        // }
        //
        // void read() throws IOException {
        //     socket.read(input);
        //     if (inputIsComplete()) {
        //         process();
        //         state = SENDING; // Normally also do first write now
        //         sk.interestOps(SelectionKey.OP_WRITE);
        //     }
        // }
        //
        // void send() throws IOException {
        //     socket.write(output);
        //     if (outputIsComplete()) {
        //         sk.cancel();
        //     }
        // }

        // Per-State Handlers
        // @Override
        // public void run() { // initial state is reader
        //     try {
        //         socket.read(input);
        //         if (inputIsComplete()) {
        //             process();
        //             sk.attach(new Sender());
        //             sk.interestOps(SelectionKey.OP_WRITE);
        //             sk.selector().wakeup();
        //         }
        //     } catch (IOException e) { /* ... */ }
        //
        // }
        //
        // class Sender implements Runnable {
        //
        //     @Override
        //     public void run() { // ...
        //         try {
        //             socket.write(output);
        //             if (outputIsComplete()) {
        //                 sk.cancel();
        //             }
        //         } catch (IOException e) { /* ... */ }
        //     }
        // }

        // Handler with Thread Pool
        @Override
        public void run() {
            try {
                if (state == READING) {
                    read();
                }
                else if (state == SENDING) {
                    send();
                }
            } catch (IOException ex) { /* ... */ }
        }

        synchronized void read() { // ...
            try {
                socket.read(input);
                if (inputIsComplete()) {
                    state = PROCESSING;
                    pool.execute(new Processor());
                }
            } catch (IOException e) { /* ... */ }
        }

        synchronized void send() throws IOException {
            socket.write(output);
            if (outputIsComplete()) {
                sk.cancel();
            }
        }

        synchronized void processAndHandOff() {
            process();
            state = SENDING; // or rebind attachment
            sk.interestOps(SelectionKey.OP_WRITE);
        }

        class Processor implements Runnable {

            @Override
            public void run() {
                processAndHandOff();
            }
        }
    }

    class Acceptor implements Runnable { // inner

        @Override
        public void run() {
            try {
                SocketChannel c = serverSocket.accept();
                if (c != null) {
                    new Handler(selector, c);
                }
            } catch (IOException ex) { /* ... */ }
        }
    }

    // Multiple Reactor Threads
    class ParallelAcceptor implements Runnable { // inner

        @Override
        public synchronized void run() { // ...
            try {
                SocketChannel connection = serverSocket.accept();
                if (connection != null) {
                    new Handler(selectors[next], connection);
                }
                if (++next == selectors.length) {
                    next = 0;
                }
            } catch (IOException ex) { /* ... */ }
        }
    }
}