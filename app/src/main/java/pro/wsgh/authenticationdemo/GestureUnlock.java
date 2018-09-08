package pro.wsgh.authenticationdemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;

import pro.wsgh.gestureunlock.util.ResourceUtil;

/**
 * 手势密码入口
 * <p>
 * Created by zhangyipeng with Email: sandy1108@163.com at Date: 2018/8/29.
 */
public class GestureUnlock {
    private static GestureUnlock sGestureUnlock;

    private final static String SP_GESTURE_UNLOCK = "sp_gesture_unlock";
    private final static String SP_KEY_GESTURE_UNLOCK_CODE = "gesture_unlock_code";

    public static GestureUnlock getInstance(){
        if (sGestureUnlock == null){
            sGestureUnlock = new GestureUnlock();
        }
        return sGestureUnlock;
    }

    /**
     * 初始化配置
     *
     */
    public void init(Context applicationContext){
        ResourceUtil.init(applicationContext);
    }

    public void createGestureUnlock(Context activityContext){
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(activityContext, GestureUnlockActivity.class));
        intent.putExtra("type", "create");
        activityContext.startActivity(intent);
    }

    public void verifyGestureUnlock(Context activityContext){
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(activityContext, GestureUnlockActivity.class));
        intent.putExtra("type", "verify");
        activityContext.startActivity(intent);
    }

    /**
     * 判断是否设置了手势密码
     *
     */
    public boolean isGestureCodeSet(Context context){
        return !TextUtils.isEmpty(getGestureCodeSet(context));
    }

    /**
     * 获取手势密码（供手势密码验证使用）
     *
     */
    public String getGestureCodeSet(Context context){
        SharedPreferences sp = context.getSharedPreferences(SP_GESTURE_UNLOCK, Context.MODE_PRIVATE);
        String gestureCode = sp.getString(SP_KEY_GESTURE_UNLOCK_CODE, null);
        return gestureCode;
    }

    /**
     * 重置手势密码为空
     *
     */
    public void resetGestureCode(Context context){
        setGestureCode(context, "");
    }

    /**
     * 暂时只用于内部。未来如果要保存到服务端，从服务端恢复，也可以。
     *
     */
    public void setGestureCode(Context context, String gestureCode){
        SharedPreferences sp = context.getSharedPreferences(SP_GESTURE_UNLOCK, Context.MODE_PRIVATE);
        sp.edit().putString(SP_KEY_GESTURE_UNLOCK_CODE, gestureCode).apply();
    }
}
