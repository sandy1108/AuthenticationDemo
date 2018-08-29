package pro.wsgh.gestureunlock.util;

import pro.wsgh.gestureunlock.entity.GesturePoint;

public interface OnDrawArrowListener {
    public void onDrawArrow(GesturePoint first, GesturePoint second, int blockWidth);
    public void onErrorState();
    public void clearAllArrow();
}
