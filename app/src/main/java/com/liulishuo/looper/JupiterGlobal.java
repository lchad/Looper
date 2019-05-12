package com.liulishuo.looper;

import android.content.Context;

/**
 * Created by lchad on 2019-05-12.
 */
public class JupiterGlobal {
    private static Context context;

    public JupiterGlobal() {
    }

    public static void init(Context ctx) {
        context = ctx;
    }

    public static Context context() {
        return context;
    }
}
