package pro.wsgh.authenticationdemo;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * File Description:
 * <p>
 * Created by zhangyipeng with Email: sandy1108@163.com at Date: 2018/9/8.
 */
public class SystemUtils {

    /**
     * 是否在后台
     * @return
     */
    public boolean isAppOnForground(Context context){
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String curPackageName = context.getApplicationContext().getPackageName();
        List<ActivityManager.RunningAppProcessInfo> app = am.getRunningAppProcesses();
        if(app == null){
            return false;
        }
        for(ActivityManager.RunningAppProcessInfo a:app){
            if(a.processName.equals(curPackageName)&&
                    a.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND){
                return true;
            }
        }
        return false;
		/*ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
		if(!TextUtils.isEmpty(curPackageName)&&curPackageName.equals(getPackageName())){
			return true;
		}
		return false;*/
    }

}
