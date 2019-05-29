package com.grg.idcard;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.lib.common.utils.ToastUtils;


@Route(path = "/idcard/activity")
public class IdCardActivity extends AppCompatActivity {

    private IDCardRecognition mIDCardRecognition; // 身份证识别类

    private IDCardCallback mIDCardCallback = new IDCardCallback() {
        @Override
        public void getIDCardInfo(final IDCardInfo idCard) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtils.show(idCard.getName().trim());
                }
            });
        }
    };

    @Autowired
    public String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_idcard);

        ARouter.getInstance().inject(this);

        boolean idcard = IDCardManager.getInstance().connect();

        if (idcard){
            //初始化成功
            mIDCardRecognition = new IDCardRecognition(mIDCardCallback);
            mIDCardRecognition.start();
        }else {
            Toast.makeText(getApplicationContext(),"初始化失败",Toast.LENGTH_SHORT).show();
        }

    }

}
