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
            // 显示电源对话框
//            performGlobalAction(AccessibilityService.GLOBAL_ACTION_POWER_DIALOG);

            // 延迟执行下滑手势，确保电源对话框已经显示
            new Handler().postDelayed(this::performSwipeDownGesture, 1000); // 1秒延迟
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

        // 派发手势
        dispatchGesture(gestureBuilder.build(), null, null);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
    }

    @Override
    public void onInterrupt() {
    }
}
