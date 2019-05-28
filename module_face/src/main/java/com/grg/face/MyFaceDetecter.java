package com.grg.face;

import com.aibee.face.facesdk.AuthorityException;
import com.aibee.face.facesdk.FaceIdentifier;
import com.aibee.face.facesdk.FaceTracker;
import com.grg.face.core.FaceDetecter;

public class MyFaceDetecter extends FaceDetecter {

    public void init(boolean islive) {
        if (mTracker == null) {
            try {
                mTracker = new FaceTracker(mContext.getAssets());
            } catch (AuthorityException e) {
                e.printStackTrace();
            }
            if (mTracker != null) {
                mTracker.setIsVerifyLive(islive);  // set: Whether to do liveness checking
                mTracker.setIsCheckQuality(islive);     // set check the face image quality
                mTracker.setFaceScoreThr(0.2f);      // set: the face confidence threshold.
                mTracker.setMinFaceSize(100);    // set the smallest face size to be detected (in pixels)
                mTracker.setIsCheckQuality(true);     // set check the face image quality
                mTracker.setMaxCollectNum(1);        // set: How many face images to crop for each face in maximum
                mTracker.setDetectIntervalHasface(1000); // set: the minimum interval to detect new faces, when there is at least one face in the scene. (in ms)
                mTracker.setDetectIntervalNoface(500);   // set: the minimum interval to detect faces, when no face is in the scene. (in ms)
                mTracker.setCollectInterval(300); // set: the minimum interval between the cropped faces (in ms)
                mTracker.setEulurAngleThr(15, 15, 15);    // set: the cropped face pose must be in these angles (in degrees)
                mTracker.setIllumThr(50.0f);
                mTracker.setBlurThr(0.5f);
                mTracker.setOccluThr(0.5f);
                mTracker.setCropFaceSize(256);
                mTracker.setCropFaceEnlargeRatio(1.8f);
                mTracker.setIsEnsureOneFace(false);
                mTracker.setLiveThr(0.1f);
                mTracker.setThreadNum(2);      // Set the number of threads to use
                mTracker.setFaceOrientation(cameraRotate);
                //mTracker.enableDebugLog(true);
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
