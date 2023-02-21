package top.bogey.touch_tool.data.action.convert;

import android.content.Context;

import com.google.gson.JsonObject;

import top.bogey.touch_tool.R;
import top.bogey.touch_tool.data.TaskRunnable;
import top.bogey.touch_tool.data.action.ActionContext;
import top.bogey.touch_tool.data.action.CalculateAction;
import top.bogey.touch_tool.data.pin.Pin;
import top.bogey.touch_tool.data.pin.PinDirection;
import top.bogey.touch_tool.data.pin.PinSlotType;
import top.bogey.touch_tool.data.pin.object.PinBoolean;

public class BoolConvertToNot extends CalculateAction {
    private transient final Pin outConditionPin;
    private transient final Pin conditionPin;

    public BoolConvertToNot(Context context) {
        super(context, R.string.action_bool_convert_not_title);
        outConditionPin = addPin(new Pin(new PinBoolean(), context.getString(R.string.action_state_subtitle_state), PinDirection.OUT, PinSlotType.MULTI));
        conditionPin = addPin(new Pin(new PinBoolean(), context.getString(R.string.action_bool_convert_and_subtitle_condition)));
    }

    public BoolConvertToNot(JsonObject jsonObject) {
        super(jsonObject);
        outConditionPin = addPin(tmpPins.remove(0));
        conditionPin = addPin(tmpPins.remove(0));
    }

    @Override
    protected void calculatePinValue(TaskRunnable runnable, ActionContext actionContext, Pin pin) {
        PinBoolean value = (PinBoolean) outConditionPin.getValue();
        PinBoolean resultPin = (PinBoolean) getPinValue(runnable, actionContext, conditionPin);
        value.setValue(!resultPin.getValue());
    }
}
