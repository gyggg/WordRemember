package me.japanesestudy.app.wordremember.frame.presenter;

import me.japanesestudy.app.wordremember.component.application.AppConfig;
import me.japanesestudy.app.wordremember.datasource.data.AbstractWordUnit;
import me.japanesestudy.app.wordremember.datasource.entity.HistoryEntity;
import me.japanesestudy.app.wordremember.datasource.entity.WordEntity;
import me.japanesestudy.app.wordremember.frame.model.HistoryModel;
import me.japanesestudy.app.wordremember.frame.model.WordModel;
import me.japanesestudy.app.wordremember.frame.view.ITestingView;
import me.japanesestudy.app.wordremember.tools.DateTool;
import me.japanesestudy.app.wordremember.tools.NumberTool;

/**
 * Created by guyu on 2018/1/18.
 */

public class TestingPresenter extends BasePresenter<ITestingView> {
    AbstractWordUnit abstractWordUnit;
    HistoryModel historyModel = new HistoryModel();
    WordModel wordModel = new WordModel();
    int position = 0;
    int testMode = 0;
    boolean isStudyTest;
    boolean isReviewTest;
    int skipDistance = 0;
    public TestingPresenter(ITestingView view, AbstractWordUnit abstractWordUnit, boolean isStudyTest, boolean isReviewTest) {
        super(view);
        this.abstractWordUnit = abstractWordUnit;
        this.abstractWordUnit.getTestUnitWord().random();
        if(appConfig.isC2J())
            testMode = AbstractWordUnit.TEST_MODE_C2J;
        else
            testMode = AbstractWordUnit.TEST_MODE_J2C;
        this.isStudyTest = isStudyTest;
        this.isReviewTest = isReviewTest;
    }



    public void setNextWord() {
        if(position >= abstractWordUnit.getTestUnitSize()){
            setTestingInfo();
            HistoryEntity historyEntity = saveHistory();
            if(!appConfig.isAutoHistoryName() && historyEntity.getParentHistoryId() == 0)
                view.onNoNextWordWithSetName(historyEntity);
            else
                view.onNoNextWord(historyEntity);
            return;
        }
        WordEntity nextWord = abstractWordUnit.getTestUnitWord().get(position);
        while (appConfig.getSkipTypeScope().indexOf(nextWord.getType()) >= 0) {
            abstractWordUnit.getmSkipWords().add(nextWord);
            position++;
            skipDistance++;
            if(position >= abstractWordUnit.getTestUnitSize()) {
                setNextWord();
                return;
            }
            nextWord = abstractWordUnit.getTestUnitWord().get(position);
        }
        String translate, wordType, keyWord;
        wordType = WordEntity.WordType.getTypeString(nextWord.getType());
        if (appConfig.isC2J()) {
            translate = nextWord.getChinese();
            if(nextWord.getJapaneseKanji() != null && !nextWord.getJapaneseKanji().equals(""))
                keyWord = nextWord.getJapaneseKanji() + "（" + nextWord.getJapaneseAlias() + "）";
            else
                keyWord = nextWord.getJapaneseAlias();
        }
        else {
            if(appConfig.isShowKanji() && nextWord.getJapaneseKanji() != null && !nextWord.getJapaneseKanji().equals("")) {
                translate = nextWord.getJapaneseKanji() + "（" + nextWord.getJapaneseAlias() + "）";
            }
            else
                translate =nextWord.getJapaneseAlias();
            keyWord = nextWord.getChinese();
        }
        view.onSetNextWord(translate, wordType, keyWord);
    }

    public void judgeWord(String key) {
        boolean res = abstractWordUnit.judge(key, position, testMode);
        position++;
        view.onCommitResult(res);
    }

    public void setTestingInfo() {
        double rightPercent = 1.0;
        try {
            rightPercent = 1.0 - (abstractWordUnit.getmWrongWords().size()) * 1.0 / abstractWordUnit.getHasTestedWordSize() * 1.0;
            rightPercent = NumberTool.saveNumberPoint(rightPercent, 2);
        } catch (Exception e) {

        }
        WordEntity lastWord = position > 0 && (position - 1 - skipDistance >= 0)? abstractWordUnit.getTestUnitWord().get(position - 1 - skipDistance) : null;
        skipDistance = 0;
        String translate, wordType, keyWord;
        if(lastWord != null) {
            wordType = "【" + WordEntity.WordType.getTypeString(lastWord.getType()) + "】";
            if (appConfig.isC2J()) {
                translate = lastWord.getChinese();
                if (lastWord.getJapaneseKanji() != null && !lastWord.getJapaneseKanji().equals(""))
                    keyWord = lastWord.getJapaneseKanji() + "（" + lastWord.getJapaneseAlias() + "）";
                else
                    keyWord = lastWord.getJapaneseAlias();
            } else {
                if (appConfig.isShowKanji() && lastWord.getJapaneseKanji() != null && !lastWord.getJapaneseKanji().equals("")) {
                    translate = lastWord.getJapaneseKanji() + "（" + lastWord.getJapaneseAlias() + "）";
                } else
                    translate = lastWord.getJapaneseAlias();
                keyWord = lastWord.getChinese();
            }
        } else {
            translate = "";
            wordType = "";
            keyWord = "";
        }
        view.onSetTestingInfo(position, abstractWordUnit.getTestUnitSize(), rightPercent,
                abstractWordUnit.getmWrongWords().size(),
                abstractWordUnit.getmSkipWords().size(),
                abstractWordUnit.getmRightWords().size(),
                translate, wordType, keyWord);
    }

    public void skipWord() {
        abstractWordUnit.skipWord(position);
        position++;
        view.onSkipWord();
    }

    public void setNextTesting(AbstractWordUnit abstractWordUnit) {
        this.abstractWordUnit = wordModel.injectTestUnitWords(abstractWordUnit);
        this.position = 0;
        this.setNextWord();
    }

    public void forwardFinishTesting() {
        for(int i = position; i < abstractWordUnit.getTestUnitSize(); i++) {
            abstractWordUnit.getmWrongWords().add(abstractWordUnit.getTestUnitWord().get(i));
        }
        HistoryEntity historyEntity = saveHistory();
        view.onForwardBack(historyEntity);
    }

    public void resetHistoryName(HistoryEntity historyEntity, String name) {
        historyEntity.setName(name);
        historyModel.saveHistory(historyEntity);
    }

    public HistoryEntity saveHistory() {
        HistoryEntity historyEntity = historyModel.saveHistory(abstractWordUnit.newHistory());
        int parentId = historyEntity.getParentHistoryId() != 0 ? historyEntity.getParentHistoryId() : historyEntity.getId();
        if(isStudyTest) {
            if(AppConfig.getStudyConfig().getLastStudyHistoryParentId() == 0) {
                AppConfig.getStudyConfig().setLastStudyHistoryParentId(parentId);
            } else {
                historyEntity.setParentHistoryId(AppConfig.getStudyConfig().getLastStudyHistoryParentId());
                historyEntity = historyModel.saveHistory(historyEntity);
            }
        }
        if(isReviewTest) {
            if(AppConfig.getReviewConfig().getLastReviewHistoryParentId() == 0) {
                AppConfig.getReviewConfig().setLastReviewDate(DateTool.getToday());
                AppConfig.getReviewConfig().setLastReviewHistoryParentId(parentId);
            } else {
                historyEntity.setParentHistoryId(AppConfig.getReviewConfig().getLastReviewHistoryParentId());
                historyEntity = historyModel.saveHistory(historyEntity);
            }
        }
        AppConfig.save();
        return historyEntity;
    }
}
