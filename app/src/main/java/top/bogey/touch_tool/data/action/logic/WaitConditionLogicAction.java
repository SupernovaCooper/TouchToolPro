package top.bogey.touch_tool.data.action.logic;

import android.content.Context;

import com.google.gson.JsonObject;

import top.bogey.touch_tool.R;
import top.bogey.touch_tool.data.TaskRunnable;
import top.bogey.touch_tool.data.action.ActionContext;
import top.bogey.touch_tool.data.action.NormalAction;
import top.bogey.touch_tool.data.pin.Pin;
import top.bogey.touch_tool.data.pin.PinDirection;
import top.bogey.touch_tool.data.pin.object.PinBoolean;
import top.bogey.touch_tool.data.pin.object.PinExecute;
import top.bogey.touch_tool.data.pin.object.PinInteger;

public class WaitConditionLogicAction extends NormalAction {
    private transient final Pin conditionPin;
    private transient final Pin timeOutPin;
    private transient final Pin periodicPin;
    private transient final Pin falsePin;

    public WaitConditionLogicAction(Context context) {
        super(context, R.string.action_wait_condition_logic_title);
        conditionPin = addPin(new Pin(new PinBoolean(false), context.getString(R.string.action_condition_logic_subtitle_condition)));
        timeOutPin = addPin(new Pin(new PinInteger(1000), context.getString(R.string.action_wait_condition_logic_subtitle_timeout)));
        periodicPin = addPin(new Pin(new PinInteger(100), context.getString(R.string.action_wait_condition_logic_subtitle_periodic)));
        falsePin = addPin(new Pin(new PinExecute(), context.getString(R.string.action_logic_subtitle_false), PinDirection.OUT));
    }

    public WaitConditionLogicAction(JsonObject jsonObject) {
        super(jsonObject);
        conditionPin = addPin(tmpPins.remove(0));
        timeOutPin = addPin(tmpPins.remove(0));
        periodicPin = addPin(tmpPins.remove(0));
        falsePin = addPin(tmpPins.remove(0));
    }

    @Override
    public void doAction(TaskRunnable runnable, ActionContext actionContext, Pin pin) {
        PinBoolean condition = (PinBoolean) getPinValue(runnable, actionContext, conditionPin);
        PinInteger timeout = (PinInteger) getPinValue(runnable, actionContext, timeOutPin);
        PinInteger periodic = (PinInteger) getPinValue(runnable, actionContext, periodicPin);
        long startTime = System.currentTimeMillis();
        while (!condition.getValue()) {
            sleep(periodic.getValue());
            if (runnable.isInterrupt() || actionContext.isReturned()) return;
            if (timeout.getValue() < System.currentTimeMillis() - startTime) break;
            condition = (PinBoolean) getPinValue(runnable, actionContext, conditionPin);
        }

        if (condition.getValue()) {
            doNextAction(runnable, actionContext, outPin);
        } else {
            doNextAction(runnable, actionContext, falsePin);
        }
    }
}
