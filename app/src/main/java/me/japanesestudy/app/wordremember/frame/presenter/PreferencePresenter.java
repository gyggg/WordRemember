package me.japanesestudy.app.wordremember.frame.presenter;

import me.japanesestudy.app.wordremember.datasource.entity.WordEntity;
import me.japanesestudy.app.wordremember.frame.view.IPreferenceView;
import me.japanesestudy.app.wordremember.tools.ArrayTool;

/**
 * Created by guyu on 2018/1/25.
 */

public class PreferencePresenter extends BasePresenter<IPreferenceView> {
    public PreferencePresenter(IPreferenceView view) {
        super(view);
    }

    public void setAutoHistoryName(Boolean isAutoHistoryName) {
        appConfig.setAutoHistoryName(isAutoHistoryName);
        saveAppConfig();
    }

    public void setIsC2J(Boolean isC2J) {
        appConfig.setC2J(isC2J);
        saveAppConfig();
    }

    public void setHideKanji(Boolean isHideKanji) {
        appConfig.setShowKanji(!isHideKanji);
        saveAppConfig();
    }

    public void startSetSkipType() {
        String []typeString = WordEntity.WordType.getTypeStrings().toArray(new String[0]);
        int []typeIds = ArrayTool.getIntArray(WordEntity.WordType.getTypes());
        Integer []defaultTypes = ArrayTool.getIntegerArray(appConfig.getSkipTypeScope());
        getView().showSkipTypesDialog(typeString, typeIds, defaultTypes);
    }

    public void stepSetSkipType(Integer []selectedTypes) {
        appConfig.getSkipTypeScope().clear();
        for(int type : selectedTypes)
            appConfig.getSkipTypeScope().add(type);
        saveAppConfig();
    }
}
