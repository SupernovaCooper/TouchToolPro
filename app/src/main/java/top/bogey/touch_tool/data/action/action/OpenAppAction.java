package top.bogey.touch_tool.data.action.action;

import android.os.Parcel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import top.bogey.touch_tool.MainAccessibilityService;
import top.bogey.touch_tool.MainApplication;
import top.bogey.touch_tool.R;
import top.bogey.touch_tool.data.TaskRunnable;
import top.bogey.touch_tool.data.WorldState;
import top.bogey.touch_tool.data.pin.Pin;
import top.bogey.touch_tool.data.pin.object.PinObject;
import top.bogey.touch_tool.data.pin.object.PinSelectApp;
import top.bogey.touch_tool.ui.app.AppView;
import top.bogey.touch_tool.utils.AppUtils;

public class OpenAppAction extends NormalAction {
    private final Pin<? extends PinObject> appPin;

    public OpenAppAction() {
        super();
        appPin = addPin(new Pin<>(new PinSelectApp(AppView.SINGLE_WITH_ACTIVITY_MODE)));
        titleId = R.string.action_open_app_action_title;
    }

    public OpenAppAction(Parcel in) {
        super(in);
        appPin = addPin(pinsTmp.remove(0));
        titleId = R.string.action_open_app_action_title;
    }

    @Override
    public void doAction(WorldState worldState, TaskRunnable runnable) {
        PinSelectApp app = (PinSelectApp) getPinValue(worldState, runnable.getTask(), appPin);
        MainAccessibilityService service = MainApplication.getService();
        LinkedHashMap<CharSequence, ArrayList<CharSequence>> packages = app.getPackages();
        for (Map.Entry<CharSequence, ArrayList<CharSequence>> entry : packages.entrySet()) {
            ArrayList<CharSequence> value = entry.getValue();
            if (value == null) continue;
            if (value.size() > 0)
                AppUtils.gotoActivity(service, entry.getKey().toString(), value.get(0).toString());
            else AppUtils.gotoApp(service, entry.getKey().toString());
            break;
        }
        super.doAction(worldState, runnable);
    }
}