package com.grg.idcard;

import com.decard.entitys.IDCard;

/**
 * 身份证识别运行线程
 *
 * @author yellowbaby on 2019-05-23
 */
public class IDCardRecognition extends Thread {

    private boolean mRunning;

    private boolean mIsAllowReadCard;

    private long mSleepTime = 500; //休眠时间

    private IDCardCallback mIDCardCallback;

    public IDCardRecognition(IDCardCallback idCardCallback){
        mIDCardCallback = idCardCallback;
    }

    public void setIDCardCallback(IDCardCallback IDCardCallback) {
        mIDCardCallback = IDCardCallback;
    }

    @Override
    public void run() {
        mRunning = true;
        mIsAllowReadCard = true;
        while (this.mRunning) {
            if (mIsAllowReadCard){
                IDCard idCard = IDCardManager.getInstance().getIDCard();
                if (idCard != null){
                    IDCardInfo idCardInfo = createIDCardInfo(idCard);
                    mIDCardCallback.getIDCardInfo(idCardInfo);
                    sleepTime(mSleepTime);
                }
            }
            sleepTime(1);
        }
        super.run();
    }

    private IDCardInfo createIDCardInfo(IDCard idCard) {
        IDCardInfo idCardInfo = new IDCardInfo();
        idCardInfo.setName(idCard.getName());
        idCardInfo.setSex(idCard.getSex());
        idCardInfo.setNation(idCard.getNation());
        idCardInfo.setAddress(idCard.getAddress());
        idCardInfo.setBirthday(idCard.getBirthday());
        idCardInfo.setId(idCard.getId());
        idCardInfo.setOffice(idCard.getOffice());
        idCardInfo.setStartTime(idCard.getStartTime());
        idCardInfo.setEndTime(idCard.getEndTime());
        idCardInfo.setPhotoDataHexStr(idCard.getPhotoDataHexStr());
        idCardInfo.setPhotoData(idCard.getPhotoData());
        idCardInfo.setFingerprintData(idCard.getFingerprintData());
        idCardInfo.setExtraData(idCard.getExtraData());
        return idCardInfo;
    }


    private void sleepTime(long time) {
        try {
            sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取身份证后，休眠指定时间后再读取下一次
     */
    public void setSleepTime(long sleepTime) {
        this.mSleepTime = sleepTime;
    }

    /**
     * @return 是否允许读身份证
     */
    public boolean isAllowReadCard() {
        return mIsAllowReadCard;
    }

    /**
     * 设置是否允许读身份证
     */
    public void setAllowReadCard(boolean isAllowReadCard) {
        this.mIsAllowReadCard = isAllowReadCard;
    }

    /**
     * 退出身份证读取线程
     */
    public void close() {
        this.mRunning = false;
    }


}
