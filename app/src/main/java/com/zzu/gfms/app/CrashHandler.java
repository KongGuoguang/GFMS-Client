package com.zzu.gfms.app;


import com.blankj.utilcode.util.LogUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Author:kongguoguang
 * Date:2017-07-28
 * Time:16:18
 * Summary:全局崩溃日志打印
 */

class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static CrashHandler handler;

    private Thread.UncaughtExceptionHandler defaultHandler;

    private CrashHandler(){}

    public static CrashHandler getInstance(){
        if (handler == null){
            handler = new CrashHandler();
        }
        return handler;
    }

    void init(){
        defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(handler);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        Writer writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        e.printStackTrace(pw);
        pw.close();
        String crash = writer.toString();
        LogUtils.file("crash", "--------- beginning of crash");
        LogUtils.file("crash", "AndroidRuntime: FATAL EXCEPTION: " + Thread.currentThread().getName());
        LogUtils.file("crash", crash);

        defaultHandler.uncaughtException(t, e);
    }
}
