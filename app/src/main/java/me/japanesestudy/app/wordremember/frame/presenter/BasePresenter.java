package me.japanesestudy.app.wordremember.frame.presenter;

import me.japanesestudy.app.wordremember.component.application.AppConfig;

/**
 * Created by guyu on 2018/1/16.
 */

public class BasePresenter<T> {
    protected T view;
    protected AppConfig appConfig;
    public BasePresenter(T view) {
        this.view = view;
        appConfig = AppConfig.getInstance();
    }

    protected T getView() {
        return view;
    }

    public AppConfig getAppConfig() {
        return AppConfig.getInstance();
    }

    public void saveAppConfig() {
        AppConfig.save();
    }

    public AppConfig.StudyConfig getStudentConfig() {
        return AppConfig.getStudyConfig();
    }

    public AppConfig.ReviewConfig getReviewConfig() {
        return AppConfig.getReviewConfig();
    }

}
