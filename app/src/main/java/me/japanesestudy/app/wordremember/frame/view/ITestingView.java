package me.japanesestudy.app.wordremember.frame.view;

import me.japanesestudy.app.wordremember.datasource.entity.HistoryEntity;
import me.japanesestudy.app.wordremember.datasource.entity.WordEntity;

/**
 * Created by guyu on 2018/1/18.
 */

public interface ITestingView {
    void onCommitResult(boolean isRight);
    void onSetNextWord(String translate, String wordType, String keyword);
    void onNoNextWordWithSetName(HistoryEntity newHistory);
    void onNoNextWord(HistoryEntity newHistory);
    void onSkipWord();
    void onSetTestingInfo(int testedWordPosition, int testWordSum, double rightPercent, int wrongSum, int SkipSum, int rightSum, String translate, String wordType, String keyword);
    void onForwardBack(HistoryEntity historyEntity);
}
