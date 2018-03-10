package io.andromeda.downloader;

import org.asynchttpclient.AsyncHandler;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.AsyncHttpClientConfig;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.HttpResponseBodyPart;
import org.asynchttpclient.HttpResponseHeaders;
import org.asynchttpclient.HttpResponseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.Unsafe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;

public class Downloader {
    private static final Logger LOGGER = LoggerFactory.getLogger(Downloader.class);
    private AsyncHttpClientConfig configuration;
    private AsyncHttpClient client;

    public Downloader(AsyncHttpClientConfig configuration) {
        this.configuration = configuration;
        this.client = new DefaultAsyncHttpClient(configuration);
    }

    private static void disableWarning() {
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            Unsafe u = (Unsafe) theUnsafe.get(null);

            Class cls = Class.forName("jdk.internal.module.IllegalAccessLogger");
            Field logger = cls.getDeclaredField("logger");
            u.putObjectVolatile(cls, u.staticFieldOffset(logger), null);
        } catch (Exception e) {
            // ignore
        }
    }

    protected void finalize() throws Throwable {
        try {
            client.close();
        } finally {
            super.finalize();
        }
    }

    public AsyncHttpClientConfig getConfiguration() {
        return configuration;
    }

    public void downloadFile(String name, String url, File file, Runnable onCompleted) {
        disableWarning();
        try {
                client.prepareGet(url)
                        .execute(new AsyncHandler() {
                            FileOutputStream stream = new FileOutputStream(file);
                            public void onThrowable(Throwable t) {
                                LOGGER.error("Exception [" + name + "]: " + t);
                            }

                            @Override
                            public State onBodyPartReceived(HttpResponseBodyPart bodyPart)
                                    throws Exception {
                                stream.write(bodyPart.getBodyPartBytes());
                                LOGGER.debug("[" + name + "]: Part received");
                                return State.CONTINUE;
                            }

                            @Override
                            public Object onCompleted() throws IOException {
                                LOGGER.info("[" + name + "]: Download completed!");
                                stream.close();
                                if (onCompleted != null) {
                                    onCompleted.run();
                                }
                                return null;
                            }

                            @Override
                            public State onHeadersReceived(HttpResponseHeaders httpResponseHeaders) {
                                return State.CONTINUE;
                            }

                            @Override
                            public State onStatusReceived(HttpResponseStatus httpResponseStatus) {
                                return State.CONTINUE;
                            }
                        });
        } catch(IOException e) {
            LOGGER.error("Exception [" + name + "]: " + e);
        }
    }
}
