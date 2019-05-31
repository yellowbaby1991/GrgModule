package com.grg.face.core;

import com.aibee.face.facesdk.AuthorityException;
import com.aibee.face.facesdk.FaceIdentifier;
import com.aibee.face.facesdk.FaceTracker;

/**
 * 用于单目活体检测的FaceDetecter
 */
public class SingleFaceLivingDetecter extends FaceDetecter {

    public void init(boolean islive) {
        if (mTracker == null) {
            try {
                mTracker = new FaceTracker(mContext.getAssets());
            } catch (AuthorityException e) {
                e.printStackTrace();
            }
            if (mTracker != null) {
                mTracker.setFaceScoreThr(0.2f);  // set: the face confidence threshold.
                mTracker.setMinFaceSize(100);    // set the smallest face size to be detected (in pixels)
                mTracker.setLostHoldFrames(5);   // Number of frames to keep even if the face is lost by the tracker
                mTracker.setIsVerifyLive(1);     // set: Whether to do liveness checking
                mTracker.setLiveThr(0.9f);       // set: the threshold of liveness checking
                mTracker.setIsCheckQuality(true);    // set check the face image quality
                mTracker.setMaxCollectNum(1);        // set: How many face images to crop for each face in maximum
                mTracker.setCropFaceSize(256);
                mTracker.setCropFaceEnlargeRatio(1.8f);
                mTracker.setDetectIntervalHasface(1000); // set: the minimum interval to detect new faces, when there is at least one face in the scene. (in ms)
                mTracker.setDetectIntervalNoface(500);   // set: the minimum interval to detect faces, when no face is in the scene. (in ms)
                mTracker.setStrictTryTimes(5);   // set: the number of try times for strict quality checking
                mTracker.setEulurAngleThr(15, 15, 15);    // set: the cropped face pose must be in these angles (in degrees)
                mTracker.setIllumThr(20.0f);
                mTracker.setBlurThr(0.4f);
                mTracker.setOccluThr(0.5f);
                mTracker.setIsCheckMouthOpen(true, 0.2f);
                mTracker.setIsCheckEyeClose(true, 0.3f);
                mTracker.setThreadNum(1);      // Set the number of threads to use
            }
        }
        if (faceIdentifier == null) {
            try {
                faceIdentifier = new FaceIdentifier(mContext.getAssets());
                faceIdentifier.cacheFaceidModel();
                faceIdentifier.setThreadNum(1);
            } catch (AuthorityException e) {
            }
        }
    }
    
}
