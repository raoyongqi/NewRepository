package com.janbar.shutdown;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 判断无障碍服务是否开启
        if (isAccessibilitySettingsOn()) {
            // 如果无障碍服务开启，启动ShutdownService
            startService(new Intent(this, ShutdownService.class));
        } else {
            // 如果无障碍服务未开启，引导用户到无障碍设置页面
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            startActivity(intent);
        }

        // 结束当前Activity
        finish();
    }

    // 判断无障碍服务是否开启
    private boolean isAccessibilitySettingsOn() {
        int accessibilityEnabled = 0;
        final String service = getPackageName() + "/" + ShutdownService.class.getCanonicalName();
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                    getContentResolver(),
                    Settings.Secure.ACCESSIBILITY_ENABLED
            );
        } catch (Settings.SettingNotFoundException ignored) {
        }

        TextUtils.SimpleStringSplitter colonSplitter = new TextUtils.SimpleStringSplitter(':');

        if (accessibilityEnabled == 1) {
            String settingValue = Settings.Secure.getString(
                    getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            );
            if (settingValue != null) {
                colonSplitter.setString(settingValue);
                while (colonSplitter.hasNext()) {
                    String componentName = colonSplitter.next();
                    if (componentName.equalsIgnoreCase(service)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
