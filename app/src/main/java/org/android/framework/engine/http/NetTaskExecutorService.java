package org.android.framework.engine.http;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by shenyan on 2014/5/21.
 */
class NetTaskExecutorService {
    private ExecutorService executorService = Executors.newFixedThreadPool(3);

    private static NetTaskExecutorService instance = null;

    private NetTaskExecutorService() {

    }

    public synchronized static NetTaskExecutorService getInstance() {
        if (instance == null) {
            instance = new NetTaskExecutorService();
        }
        return instance;
    }

    public void submit(Runnable runnable) {
        if (runnable != null) {
            executorService.submit(runnable);
        }
    }
}
