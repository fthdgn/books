package com.fthdgn.books.utils;

public class ThreadUtils {

    public static void runOnBackground(Runnable r) {
        new Thread(r).start();
    }

}
