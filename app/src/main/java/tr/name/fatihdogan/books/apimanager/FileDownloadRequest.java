package tr.name.fatihdogan.books.apimanager;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;

import tr.name.fatihdogan.books.BaseApplication;
import tr.name.fatihdogan.books.callback.SimpleListener;

import static com.android.volley.Request.Method.GET;
import static tr.name.fatihdogan.books.utils.FileUtils.saveBytesToPathSafely;

public class FileDownloadRequest extends Request<byte[]> {

    private static RequestQueue requestQueue;

    public static void addRequest(Request request) {
        if (requestQueue == null)
            requestQueue = Volley.newRequestQueue(BaseApplication.getInstance());
        requestQueue.add(request);
    }

    private String path;
    private SimpleListener callback;

    public FileDownloadRequest(String url, String path, SimpleListener callback) {
        super(GET, url, null);
        setShouldCache(false);
        this.path = path;
        this.callback = callback;
    }

    @Override
    protected void deliverResponse(byte[] response) {
        saveBytesToPathSafely(path, response);
        callback.onResponse();
    }

    @Override
    protected Response<byte[]> parseNetworkResponse(NetworkResponse response) {
        return Response.success(response.data, HttpHeaderParser.parseCacheHeaders(response));
    }
}

