package top.bogey.touch_tool.ui;

import android.app.Activity;
import android.content.Intent;

import top.bogey.touch_tool.MainApplication;
import top.bogey.touch_tool.service.MainAccessibilityService;

public class PermissionActivity extends BaseActivity {
    public static final String INTENT_KEY_START_CAPTURE = "INTENT_KEY_START_CAPTURE";

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (intent == null) return;

        boolean startCaptureService = intent.getBooleanExtra(INTENT_KEY_START_CAPTURE, false);
        if (startCaptureService) {
            MainAccessibilityService service = MainApplication.getInstance().getService();
            if (service == null || !service.isServiceConnected()) {
                finish();
                return;
            }

            launchNotification((notifyCode, notifyIntent) -> {
                if (notifyCode == Activity.RESULT_OK) {
                    launchCapture((code, data) -> {
                        service.bindCaptureService(code == Activity.RESULT_OK, data);
                        finish();
                    });
                } else {
                    service.callStartCaptureFailed();
                    finish();
                }
            });
            return;
        }
        finish();
    }
}
