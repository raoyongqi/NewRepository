package com.janbar.shutdown;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.content.Intent;
import android.graphics.Path;
import android.os.Handler;
import android.view.accessibility.AccessibilityEvent;

public class ShutdownService extends AccessibilityService {

    private static final int CLICK_INTERVAL_MS = 1000; // 1秒间隔
    private static final int MAX_CLICKS = 10; // 点击次数

    private Handler handler = new Handler();
    private int clickCount = 0;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            // 执行打开关机对话框
            openPowerDialog();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void openPowerDialog() {
        // 调用系统的关机对话框
        performGlobalAction(AccessibilityService.GLOBAL_ACTION_POWER_DIALOG);

        // 增加延迟后执行第一个点击手势
        new Handler().postDelayed(this::performFirstClick, 1000); // 1秒延迟
    }

    private void performFirstClick() {
        // 定义第一个点击手势的路径
        Path clickPath1 = new Path();
        clickPath1.moveTo(455, 1100);  // 第一个点击坐标 (可以根据实际需要调整)

        // 创建点击手势描述
        GestureDescription.StrokeDescription clickStroke1 = new GestureDescription.StrokeDescription(clickPath1, 0, 100);
        GestureDescription.Builder gestureBuilder = new GestureDescription.Builder();
        gestureBuilder.addStroke(clickStroke1);

        // 派发点击手势并在完成后执行第二个点击手势
        dispatchGesture(gestureBuilder.build(), new GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                super.onCompleted(gestureDescription);
                // 增加延迟后执行第二个点击手势
                new Handler().postDelayed(ShutdownService.this::startRepeatedClicks, 1000); // 1秒延迟
            }
        }, null);
    }

    private void startRepeatedClicks() {
        // 初始化点击计数器
        clickCount = 0;
        performRepeatedClick();
    }

    private void performRepeatedClick() {
        // 定义第二个点击手势的路径
        Path clickPath2 = new Path();
        clickPath2.moveTo(455, 1000);  // 第二个点击坐标 (可以根据实际需要调整)

        // 创建点击手势描述
        GestureDescription.StrokeDescription clickStroke2 = new GestureDescription.StrokeDescription(clickPath2, 0, 100);
        GestureDescription.Builder gestureBuilder = new GestureDescription.Builder();
        gestureBuilder.addStroke(clickStroke2);

        // 派发点击手势
        dispatchGesture(gestureBuilder.build(), new GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                super.onCompleted(gestureDescription);
                // 增加计数器并检查是否需要继续点击
                clickCount++;
                if (clickCount < MAX_CLICKS) {
                    // 再次延迟后执行下一次点击
                    handler.postDelayed(ShutdownService.this::performRepeatedClick, CLICK_INTERVAL_MS);
                }
            }
        }, null);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
    }

    @Override
    public void onInterrupt() {
    }
}
