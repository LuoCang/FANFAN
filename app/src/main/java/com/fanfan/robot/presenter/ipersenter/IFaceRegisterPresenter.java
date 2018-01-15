package com.fanfan.robot.presenter.ipersenter;

import android.graphics.Bitmap;

import com.fanfan.novel.common.presenter.BasePresenter;
import com.fanfan.novel.common.presenter.BaseView;
import com.fanfan.novel.model.FaceAuth;
import com.fanfan.youtu.api.base.event.BaseEvent;

/**
 * Created by android on 2018/1/9.
 */

public abstract class IFaceRegisterPresenter implements BasePresenter {


    protected IFaceRegView mBaseView;

    public IFaceRegisterPresenter(IFaceRegView baseView) {
        mBaseView = baseView;
    }

    public abstract void newPerson(Bitmap bitmap);

    public abstract void uploadFace(FaceAuth faceAuth, Bitmap bitmap);

    public abstract void setAddface();

    public interface IFaceRegView extends BaseView {

        void onError(int code, String msg);

        void onError(BaseEvent event);

        void newpersonSuccess(String person_id);

        void uploadBitmapFinish(int number);
    }
}
