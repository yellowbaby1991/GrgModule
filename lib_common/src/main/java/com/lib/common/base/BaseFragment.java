package com.lib.common.base;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;

public class BaseFragment extends Fragment {

    private static BaseFragment INSTANCE = null;

    private static Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            INSTANCE.onMsgResult(msg);
        };
    };

    protected String TAG = getClass().getSimpleName();

    /**
     * Message 消息回调
     *
     * @param msg
     */
    protected void onMsgResult(Message msg) {

    }

    protected void onRemoveMsgs(int what) {
        mHandler.removeMessages(what);
    }

    protected void onSendMsgs(int what) {
        mHandler.sendMessage(mHandler.obtainMessage(what));
    }

    protected void onSendMsg(int what, Object obj) {
        mHandler.sendMessage(mHandler.obtainMessage(what, obj));
    }

    protected void onSendMsgDelayed(int what, Object obj, long delayMillis) {
        mHandler.removeMessages(what, obj);
        mHandler.sendMessageDelayed(mHandler.obtainMessage(what, obj),
                delayMillis);
    }


    @Override
    public void onResume() {
        INSTANCE = this;
        super.onResume();
    }
}
