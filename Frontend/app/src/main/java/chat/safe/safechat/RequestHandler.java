package chat.safe.safechat;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by Peter on 3/21/19.
 */

public class RequestHandler {
    private static RequestHandler instance;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private static Context ctx;

    private RequestHandler(Context context){
        ctx = context;
        requestQueue = getRequestQueue();

        imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> cache = new LruCache<>(20);

            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
        });
    }

    public static synchronized RequestHandler getInstance(Context context){
        if(instance == null){
            instance = new RequestHandler(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue(){
        if(requestQueue == null){
            // getApplicationContext() is needed, because it keeps people
            // from leaking the Activity or BroadcastReceiver if someone passes one in
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req){
        getRequestQueue().add(req);
    }

    public ImageLoader getImageLoader(){
        return imageLoader;
    }
}
