package net.montoyo.mcef.client;

import net.montoyo.mcef.api.IScheme;
import net.montoyo.mcef.api.SchemePreResponse;
import org.cef.callback.CefCallback;
import org.cef.handler.CefResourceHandlerAdapter;
import org.cef.misc.IntRef;
import org.cef.misc.StringRef;
import org.cef.network.CefRequest;
import org.cef.network.CefResponse;

public class SchemeResourceHandler extends CefResourceHandlerAdapter {

    // forge causes some problems without this
    private static final ClassLoader clr;
    
    static {
        ClassLoader c = Thread.currentThread().getContextClassLoader();
        if (c == null) c = SchemeResourceHandler.class.getClassLoader();
        clr = c;
    }
    
    private final IScheme scheme;

    public SchemeResourceHandler(IScheme scm) {
        scheme = scm;
    }

    @Override
    public boolean processRequest(CefRequest request, CefCallback callback) {
        Thread.currentThread().setContextClassLoader(clr);
        
        SchemePreResponse resp = scheme.processRequest(request.getURL());

        switch(resp) {
            case HANDLED_CONTINUE:
                callback.Continue();
                return true;

            case HANDLED_CANCEL:
                callback.cancel();
                return true;

            default:
                return false;
        }
    }

    @Override
    public void getResponseHeaders(CefResponse response, IntRef response_length, StringRef redirectUrl) {
        scheme.getResponseHeaders(new SchemeResponseHeaders(response, response_length, redirectUrl));
    }

    @Override
    public boolean readResponse(byte[] data_out, int bytes_to_read, IntRef bytes_read, CefCallback callback) {
        return scheme.readResponse(new SchemeResponseData(data_out, bytes_to_read, bytes_read));
    }

}
