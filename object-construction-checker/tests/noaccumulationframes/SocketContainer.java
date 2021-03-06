// A simple class that has a Socket as an owning field.
// This is a modified version of tests/socket/SocketContainer.java
// for checking that without RMC support we can't assign to non-final owning fields at all.

import java.net.*;
import java.io.*;

import org.checkerframework.checker.objectconstruction.qual.*;
import org.checkerframework.checker.calledmethods.qual.*;
import org.checkerframework.checker.mustcall.qual.*;

@MustCall("close")
class SocketContainer {
    @Owning Socket sock;

    public SocketContainer(String host, int port) throws Exception {
        // Assignments to owning fields should not be permitted.
        // :: error: required.method.not.called
        sock = new Socket(host, port);
    }

    // No missing reset must call error is issued, since RMC is disabled...
    public void reassign(String host, int port) throws Exception {
        sock.close();
        // For the RHS, because the field can't take ownership
        // :: error: required.method.not.called
        Socket sr = new Socket(host, port);
        // No warning for overwriting the field, since it can't take ownership!
        sock = sr;
    }

    @EnsuresCalledMethods(value="this.sock", methods="close")
    public void close() throws IOException {
        sock.close();
    }
}
