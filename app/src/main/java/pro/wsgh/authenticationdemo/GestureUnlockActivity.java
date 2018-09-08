package pro.wsgh.authenticationdemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

import pro.wsgh.gestureunlock.fragment.GestureCreateFragment;
import pro.wsgh.gestureunlock.fragment.GestureVerifyFragment;
import pro.wsgh.gestureunlock.vo.ConfigGestureVO;
import pro.wsgh.gestureunlock.vo.ResultVerifyVO;

/**
 * File Description: 手势密码解锁认证Activity
 * <p>
 * Created by zhangyipeng with Email: sandy1108@163.com at Date: 2018/8/29.
 */
public class GestureUnlockActivity extends FragmentActivity{

    private static final String PRES_DATA_GESTURE_SP = "gesture_unlock_sp";
    private static final String PRES_DATA_GESTURE_KEY = "gesture_unlock_data";

    private Fragment currentFragment;
    private GestureCreateFragment mGestureCreateFragment;
    private GestureVerifyFragment mGestureVerifyFragment;

    /**
     * Perform initialization of all fragments.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(pro.wsgh.gestureunlock.R.layout.activity_gesture_unlock);
        try {
            Intent intent = getIntent();
            String type = intent.getStringExtra("type");
            if (!TextUtils.isEmpty(type) && type.equals("create")){
                //初始化手势密码
                showCreateGestureLayout();
            } else if(!TextUtils.isEmpty(type) && type.equals("verify")){
                //手势密码认证
                showVerifyGestureLayout();
            } else {
                //无效操作，退出
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
            //异常退出
            finish();
        }
    }

    /**
     * 显示初始化手势密码的布局
     */
    private void showCreateGestureLayout() {
        if (mGestureCreateFragment == null) {
            mGestureCreateFragment = new GestureCreateFragment();
            mGestureCreateFragment.setGestureCreateListener(new GestureCreateFragment.GestureCreateListener() {

                @Override
                public void onCreateFinished(String gestureCode) {
                    // 创建手势密码完成
                    GestureUnlock.getInstance().setGestureCode(GestureUnlockActivity.this, gestureCode);
                }

                @Override
                public void onCreateFailed(ResultVerifyVO result) {
                    // 创建手势密码失败
                }

                @Override
                public void closeLayout() {
                    // 关闭
                }

                @Override
                public void onCancel() {
                    // 取消创建手势密码
                }

                @Override
                public void onEventOccur(int eventCode) {

                }
            });
        }
        mGestureCreateFragment.setData(ConfigGestureVO.defaultConfig());
        safeAddFragment(mGestureCreateFragment, pro.wsgh.gestureunlock.R.id.fragment_container, "GestureCreateFragment");
    }

    /**
     * 显示验证手势密码的布局
     */
    private void showVerifyGestureLayout(){
        if (mGestureVerifyFragment == null){
            mGestureVerifyFragment = new GestureVerifyFragment();
            mGestureVerifyFragment.setGestureVerifyListener(new GestureVerifyFragment.GestureVerifyListener() {
                @Override
                public void onVerifyResult(ResultVerifyVO result) {

                }

                @Override
                public void closeLayout() {

                }

                @Override
                public void onStartCreate() {

                }

                @Override
                public void onCancel() {

                }

                @Override
                public void onEventOccur(int eventCode) {

                }
            });
        }
        mGestureVerifyFragment.setData(ConfigGestureVO.defaultConfig());
        safeAddFragment(mGestureVerifyFragment, pro.wsgh.gestureunlock.R.id.fragment_container, "GestureVerifyFragment");
    }

    /**
     * 检查fragment是否已经加入，防止重复
     *
     * @param fragment
     * @param id
     * @param tag
     */
    private void safeAddFragment(Fragment fragment, int id, String tag){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //优先检查，fragment是否存在，避免重叠
        Fragment tempFragment = fragmentManager.findFragmentByTag(tag);
        if(tempFragment != null){
            fragment = tempFragment;
        }
        if(fragment.isAdded()){
            addOrShowFragment(fragmentTransaction, fragment, id, tag);
        }else{
            if(currentFragment!=null&&currentFragment.isAdded()){
                fragmentTransaction.hide(currentFragment).add(id, fragment,tag).commit();
            }else{
                fragmentTransaction.add(id, fragment,tag).commit();
            }
            currentFragment = fragment;
        }
    }

    /**
     * 添加或者直接显示
     *
     * @param transaction
     * @param fragment
     * @param containerLayoutId
     * @param tag
     */
    private void addOrShowFragment(FragmentTransaction transaction, Fragment fragment, int containerLayoutId, String tag) {
        if(currentFragment == fragment)
            return;
        if (!fragment.isAdded()) { // 如果当前fragment未被添加，则添加到Fragment管理器中
            transaction.hide(currentFragment).add(containerLayoutId, fragment,tag).commit();
        } else {
            transaction.hide(currentFragment).show(fragment).commit();
        }
        currentFragment.setUserVisibleHint(false);
        currentFragment =  fragment;
        currentFragment.setUserVisibleHint(true);
    }

    /**
     * 获取手势密码信息
     *
     * @return
     */
    private String getGestureData(){
        SharedPreferences sp = getSharedPreferences(PRES_DATA_GESTURE_SP, MODE_PRIVATE);
        return sp.getString(PRES_DATA_GESTURE_KEY, "");
    }

    /**
     * 存储手势密码信息
     *
     * @param code
     */
    private void setGestureData(String code){
        SharedPreferences sp = getSharedPreferences(PRES_DATA_GESTURE_SP, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(PRES_DATA_GESTURE_KEY, code);
        editor.apply();
    }
}
