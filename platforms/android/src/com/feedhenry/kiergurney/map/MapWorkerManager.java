package com.feedhenry.kiergurney.map;

import android.util.Log;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by kxiang on 25/02/2014.
 * Manage map threads
 */
public class MapWorkerManager {
    private static MapWorkerManager instance=null;
    public static MapWorkerManager getInstance(){
        if (instance==null){
            instance=new MapWorkerManager();
        }
        return instance;
    }
    private ThreadPoolExecutor threadPool;
    private int NUMBER_OF_CORES =1;
    private int KEEP_LIVE=10;
    private LinkedBlockingQueue<Runnable> queue;
    private MapWorkerManager(){
        this.NUMBER_OF_CORES=Runtime.getRuntime().availableProcessors();
        this.queue=new LinkedBlockingQueue<Runnable>();
        this.threadPool=new ThreadPoolExecutor(
                1,
                NUMBER_OF_CORES,
                KEEP_LIVE,
                TimeUnit.SECONDS,
                queue
        );


    }
    public ThreadPoolExecutor getThreadPool(){
        return this.threadPool;
    }
}
