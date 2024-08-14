package com.janbar.shutdown;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.content.Intent;
import android.graphics.Path;
import android.os.Handler;
import android.view.accessibility.AccessibilityEvent;

public class ShutdownService extends AccessibilityService {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            // 直接执行下滑手势，无需延迟
            performSwipeDownGesture();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void performSwipeDownGesture() {
        // 定义下滑手势的路径
        Path swipePath = new Path();
        swipePath.moveTo(200, 50);  // 手势起点 (可以根据屏幕分辨率调整)
        swipePath.lineTo(200, 1000); // 手势终点 (可以根据屏幕分辨率调整)

        // 创建手势描述
        GestureDescription.StrokeDescription swipeStroke = new GestureDescription.StrokeDescription(swipePath, 0, 2000);
        GestureDescription.Builder gestureBuilder = new GestureDescription.Builder();
        gestureBuilder.addStroke(swipeStroke);

        // 派发手势并在完成后执行点击手势
        dispatchGesture(gestureBuilder.build(), new GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                super.onCompleted(gestureDescription);
                // 增加延迟后再执行点击手势
                new Handler().postDelayed(ShutdownService.this::performTapGesture, 1000); // 0.5秒延迟
            }
        }, null);
    }

    private void performTapGesture() {
        // 定义点击手势的路径 (点击左上角)
        Path tapPath = new Path();
        tapPath.moveTo(64, 331);  // 点击坐标 (可以根据实际需要调整)

        // 创建点击手势描述
        GestureDescription.StrokeDescription tapStroke = new GestureDescription.StrokeDescription(tapPath, 0, 100);
        GestureDescription.Builder gestureBuilder = new GestureDescription.Builder();
        gestureBuilder.addStroke(tapStroke);

        // 派发点击手势
        dispatchGesture(gestureBuilder.build(), null, null);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
    }

    @Override
    public void onInterrupt() {
    }
}
