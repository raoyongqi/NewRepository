package com.janbar.shutdown;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.content.Intent;
import android.graphics.Path;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

public class ShutdownService extends AccessibilityService {

    private final Handler handler = new Handler();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            handler.postDelayed(this::performClickGestures, 1000); // 1 second delay
        }
        return START_STICKY;
    }

    private void performClickGestures() {
        float[][] coordinates = {
                {100, 200}   };

        for (float[] coord : coordinates) {
            GestureDescription gesture = createGesture(coord[0], coord[1], coord[0], coord[1], false, 100);
            if (gesture != null) {
                dispatchGesture(gesture, new GestureResultCallback() {
                }, handler);
            }
        }
    }

    private static GestureDescription createGesture(float x1, float y1, float x2, float y2, boolean swipe, int duration) {
        Path path = new Path();
        path.moveTo(x1, y1);
        if (swipe) {
            path.lineTo(x2, y2);
        }

        GestureDescription.StrokeDescription stroke = new GestureDescription.StrokeDescription(path, 0, duration);
        GestureDescription.Builder builder = new GestureDescription.Builder();
        builder.addStroke(stroke);
        return builder.build();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // Handle accessibility events if needed
    }

    @Override
    public void onInterrupt() {
        // Handle interruptions if needed
    }
}
