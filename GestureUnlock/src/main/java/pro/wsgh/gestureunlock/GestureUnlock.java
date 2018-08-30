package pro.wsgh.gestureunlock;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import pro.wsgh.gestureunlock.util.ResourceUtil;
import pro.wsgh.gestureunlock.vo.ConfigGestureVO;

/**
 * 手势密码入口
 * <p>
 * Created by zhangyipeng with Email: sandy1108@163.com at Date: 2018/8/29.
 */
public class GestureUnlock {
    private static GestureUnlock sGestureUnlock;

    public static GestureUnlock getInstance(){
        if (sGestureUnlock == null){
            sGestureUnlock = new GestureUnlock();
        }
        return sGestureUnlock;
    }

    /**
     * 初始化配置
     *
     * @param applicationContext
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
}
