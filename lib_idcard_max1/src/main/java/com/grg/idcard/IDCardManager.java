package com.grg.idcard;

import com.decard.NDKMethod.BasicOper;
import com.decard.entitys.IDCard;


/**
 * 身份证管理类
 *
 * @author yellowbaby on 2019-05-23
 */
public class IDCardManager {

    private static final String TAG = "IDCardManager";

    public static final String COM_IDCARD = "/dev/ttyS1";

    public static int BAUTERATE_IDCARD = 115200;

    private static IDCardManager idCardManager;

    private IDCardManager() {
    }

    public static IDCardManager getInstance() {
        if (idCardManager == null) {
            idCardManager = new IDCardManager();
        }
        return idCardManager;
    }

    /**
     * 读取身份证信息
     */
    public IDCard getIDCard() {
        return BasicOper.dc_SamAReadCardInfo(1);
    }

    /**
     * 连接身份证模块
     */
    public boolean connect() {
        int rst = BasicOper.dc_open("COM", null, COM_IDCARD, BAUTERATE_IDCARD);
        if (rst > 0) {//80
            return true;
        } else {
            return false;
        }
    }

    /**
     * 断开身份证模块连接
     */
    public void disconnect() {
        BasicOper.dc_exit();
    }


}
