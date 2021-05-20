package me.japanesestudy.app.wordremember.frame.presenter;

import java.util.ArrayList;
import java.util.Date;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import me.japanesestudy.app.wordremember.component.application.AppConfig;
import me.japanesestudy.app.wordremember.datasource.data.CompileWordUnit;
import me.japanesestudy.app.wordremember.datasource.data.WordUnit;
import me.japanesestudy.app.wordremember.datasource.data.WordList;
import me.japanesestudy.app.wordremember.datasource.entity.BookEntity;
import me.japanesestudy.app.wordremember.datasource.entity.HistoryEntity;
import me.japanesestudy.app.wordremember.datasource.entity.UnitEntity;
import me.japanesestudy.app.wordremember.datasource.entity.WordEntity;
import me.japanesestudy.app.wordremember.frame.model.BookModel;
import me.japanesestudy.app.wordremember.frame.model.HistoryModel;
import me.japanesestudy.app.wordremember.frame.model.UnitModel;
import me.japanesestudy.app.wordremember.frame.model.WordModel;
import me.japanesestudy.app.wordremember.frame.view.IStudyView;
import me.japanesestudy.app.wordremember.tools.ArrayTool;
import me.japanesestudy.app.wordremember.tools.DateTool;
import me.japanesestudy.app.wordremember.tools.ToastTool;

/**
 * Created by guyu on 2018/1/23.
 */

public class StudyPresenter extends BasePresenter<IStudyView> {
    WordModel wordModel = new WordModel();
    UnitModel unitModel = UnitModel.getInstance();
    HistoryModel historyModel = HistoryModel.getInstance();
    BookModel bookModel = BookModel.getInstance();

    public StudyPresenter(IStudyView view) {
        super(view);
    }

    public void initStudy() {
        if(today().after(getStudentConfig().getLastStudyDate())) {
            WordUnit testUnit = getNewUnit();
            if(testUnit == null || testUnit.getTestUnitSize() == 0) {
                getView().onStudyNull(getStudentConfig().getUnitName());
                return;
            }
            if(getStudentConfig().getLastStudyHistoryParentId() != 0) {
                List<HistoryEntity> historyLink = historyModel.getHistoryLink(getStudentConfig().getLastStudyHistoryParentId());
                if(historyLink.get(0) == null) {
                    getStudentConfig().setLastStudyHistoryParentId(0);
                    AppConfig.save();
                    getView().initStudy(testUnit);
                    return;
                }
                HistoryEntity historyEntity = historyLink.get(historyLink.size() -1);
                if(historyEntity.getWrongWordSize() <= 0) {
                    finishStudy(testUnit);
                    return;
                }
                wordModel.injectTestUnitWords(historyEntity);
                testUnit.setTestUnitWord(historyEntity.getWrongWordList());
            }
            getView().initStudy(testUnit);
        } else {
            finishStudy();
        }
    }

    public void moreStudy() {
        getStudentConfig().setLastStudyDate(DateTool.getYesterday());
        getStudentConfig().setLastStudyHistoryParentId(0);
        AppConfig.save();
        initStudy();
    }

    public void initReview() {
        if(today().after(getReviewConfig().getLastReviewDate())) {
            getReviewConfig().setLastReviewHistoryParentId(0);
            appConfig.save();
            WordUnit testUnit = getNewReviewUnit();
            getView().initReview(testUnit);
        } else if(today().equals(getReviewConfig().getLastReviewDate()) && getReviewConfig().getLastReviewHistoryParentId() != 0) {
            WordUnit testUnit = getLastReview();
            if(testUnit == null) {
                getReviewConfig().reset();
                AppConfig.save();
                initReview();
                return;
            }
            if(testUnit.getTestUnitSize() == 0) {
                finishReview(testUnit);
            } else {
                getView().initReview(testUnit);
            }
        } else {
            finishReview();
        }
    }

    public void finishStudy(WordUnit testUnit) {
        getStudentConfig().setLastStudyHistoryParentId(0);
        getStudentConfig().setLastStudyDate(DateTool.getDate(System.currentTimeMillis()));
        getStudentConfig().setNextPart(getStudentConfig().getNextPart() + 1);
        getStudentConfig().setUnitName(testUnit.getNewName());
        getStudentConfig().setInfo(testUnit.getInfo());
        getStudentConfig().setSrcSize(testUnit.getSrcSize());
        appConfig.save();
        getView().onFinishStudy(getStudentConfig().getUnitName(), getStudentConfig().getInfo(), getStudentConfig().getSrcSize());
    }

    public void finishStudy() {
        getView().onFinishStudy(getStudentConfig().getUnitName(), getStudentConfig().getInfo(), getStudentConfig().getSrcSize());
    }

    public void finishReview(WordUnit testUnit) {
        getReviewConfig().setLastReviewDate(DateTool.getDate(System.currentTimeMillis()));
        getReviewConfig().setLastReviewHistoryParentId(0);
        getReviewConfig().setUnitName(testUnit.getNewName());
        getReviewConfig().setInfo(testUnit.getInfo());
        getReviewConfig().setSrcSize(testUnit.getSrcSize());
        appConfig.save();
        getView().onFinishReview(getReviewConfig().getUnitName(), getReviewConfig().getInfo(), getReviewConfig().getSrcSize());
    }

    public void finishReview() {
        getView().onFinishReview(getReviewConfig().getUnitName(), getReviewConfig().getInfo(), getReviewConfig().getSrcSize());
    }

    public Date today(){
        return DateTool.getDate(System.currentTimeMillis());
    }

    public WordUnit getNewUnit() {
        WordUnit result = new WordUnit();
        switch(getStudentConfig().getStudyType()) {
            case AppConfig.StudyConfig.TYPE_NORMAL_BOOK:
                BookEntity bookEntity = bookModel.getBookById(getStudentConfig().getTestBookId());
                List<UnitEntity> unitEntities = unitModel.getAllUnitEntityWithWord(bookEntity.getId(), true);
                if(getStudentConfig().getNextPart() >= unitEntities.size()) {
                    return null;
                }
                UnitEntity unitEntity = unitEntities.get(getStudentConfig().getNextPart());
                result.setTestUnitWord(unitEntity.getWordEntities());
                result.setNewName("《" + bookEntity.getName() + "》");
                result.setInfo("【" + unitEntity.getName() + "】");
                result.setSrcSize(unitEntity.size());
                break;
            case AppConfig.StudyConfig.TYPE_WORD_TYPE_SELECTED:
                //set name...
                String name = "专项训练：";
                for(int i = 0; i < getStudentConfig().getRandomTestTypeScope().size(); i++) {
                    name = name + WordEntity.WordType.getTypeString(getStudentConfig().getRandomTestTypeScope().get(i));
                    if(i != getStudentConfig().getRandomTestTypeScope().size() - 1)
                        name = name + " · ";
                    else
                        name = name + "";
                }
                result.setNewName(name);
                //set word
                List<WordEntity> wordEntities = wordModel.getWordEntitiesByWordType(getStudentConfig().getRandomTestTypeScope());
                if(getStudentConfig().getPerPart() * getStudentConfig().getNextPart() > wordEntities.size())
                    return result;
                long seed = getStudentConfig().getSeed();
                Collections.shuffle(wordEntities, new Random(seed));
                int partSum = wordEntities.size() / getStudentConfig().getPerPart();
                if(wordEntities.size() % getStudentConfig().getPerPart() != 0)
                    partSum += 1;
                int aidPos = (getStudentConfig().getNextPart() + 1) * getStudentConfig().getPerPart() < wordEntities.size() ? (getStudentConfig().getNextPart() + 1) * getStudentConfig().getPerPart() : wordEntities.size();
                wordEntities = wordEntities.subList(getStudentConfig().getNextPart() * getStudentConfig().getPerPart(), aidPos);
                result.setTestUnitWord(WordList.getInstance(wordEntities));
                //set info
                result.setInfo("【第 " + (getStudentConfig().getNextPart() + 1) + " / " + partSum + " 部分】");
                result.setSrcSize(wordEntities.size());
                break;
            case AppConfig.StudyConfig.TYPE_ALL_WORD:
                //set word
                List<WordEntity> allwords = wordModel.getWordsWithIdScope(getStudentConfig().getNextPart() * getStudentConfig().getPerPart(),
                        (getStudentConfig().getNextPart() + 1) * getStudentConfig().getPerPart());
                //set name...
                name = "《全词训练》";
                result.setNewName(name);
                if(allwords.isEmpty())
                    return result;
                seed = getStudentConfig().getSeed();
                Collections.shuffle(allwords, new Random(seed));
                partSum = wordModel.getWordSum() / getStudentConfig().getPerPart();
                if(allwords.size() % getStudentConfig().getPerPart() != 0)
                    partSum += 1;
                result.setTestUnitWord(WordList.getInstance(allwords));
                //set info
                result.setInfo("【第 " + (getStudentConfig().getNextPart() + 1) + " / " + partSum + " 部分】");
                result.setSrcSize(allwords.size());
                break;
        }
        return result;
    }

    public WordUnit getNewReviewUnit() {
        CompileWordUnit result = new CompileWordUnit();
        List<HistoryEntity> nearHistory = historyModel.getAllHistoryWithNearDay(getReviewConfig().getReviewDayScope());
        for(HistoryEntity historyEntity : nearHistory) {
            List<HistoryEntity> historyLink = historyModel.getHistoryLink(historyEntity);
            HistoryEntity temp = new HistoryEntity();
            switch(getReviewConfig().getReviewLevel()) {
                case AppConfig.ReviewConfig.LEVEL_NIGHTMARE:
                    temp = historyLink.get(0);
                    temp.setWrongWords(temp.getSrcWords());
                    break;
                case AppConfig.ReviewConfig.LEVEL_HARD:
                    temp = historyLink.get(0);
                    break;
                case AppConfig.ReviewConfig.LEVEL_NORMAL:
                    temp = historyLink.size() >= 2 ? historyLink.get(1) : historyLink.get(0);
                    break;
                case AppConfig.ReviewConfig.LEVEL_EASY:
                    temp = historyLink.get(historyLink.size() - 1);
                    break;
            }
            wordModel.injectTestUnitWords(temp);
            result.addTestUnit(temp);
        }
        result.setNewName("每日复习：" + AppConfig.ReviewConfig.getLevelName(getReviewConfig().getReviewLevel()));
        result.setInfo("【最近 " + getReviewConfig().getReviewDayScope() + " 日】");
        result.setSrcSize(result.getTestUnitSize());
        return result;
    }

    public WordUnit getLastReview() {
        if(getReviewConfig().getLastReviewHistoryParentId() == 0)
            return null;
        WordUnit result = new WordUnit();
        result.setNewName("每日复习：" + AppConfig.ReviewConfig.getLevelName(getReviewConfig().getReviewLevel()));
        result.setInfo("【最近 " + getReviewConfig().getReviewDayScope() + " 日】");
        List<HistoryEntity> historyLink = historyModel.getHistoryLink(getReviewConfig().getLastReviewHistoryParentId());
        if(historyLink.get(0) == null)
            return null;
        result.setSrcSize(historyLink.get(0).getSrcWordSize());
        HistoryEntity temp = historyLink.get(historyLink.size() - 1);
        result.setTestUntiWordString(temp.getWrongWords());
        wordModel.injectTestUnitWords(result);
        return result;
    }

    public void startEditStudy() {
        String []studyTypes = {"按照课程背诵", "分词性专项训练", "全词背诵"};
        int []studyTypeIds = {AppConfig.StudyConfig.TYPE_NORMAL_BOOK, AppConfig.StudyConfig.TYPE_WORD_TYPE_SELECTED, AppConfig.StudyConfig.TYPE_ALL_WORD};
        getView().showStartEditStudyDialog(studyTypes, studyTypeIds);
    }

    public void stepEditStudyType(int typeId) {
        getStudentConfig().setStudyType(typeId);
        switch (typeId) {
            case AppConfig.StudyConfig.TYPE_NORMAL_BOOK:
                List<BookEntity> bookEntities = bookModel.getAllBookList();
                int []bookIds = new int[bookEntities.size()];
                ArrayList<String> bookNames = new ArrayList<>();
                int i = 0;
                for(BookEntity bookEntity : bookEntities) {
                    bookIds[i] = (bookEntity.getId());
                    bookNames.add(bookEntity.getName());
                    i++;
                }
                getView().showBookSelectDialog(bookNames.toArray(new String[bookNames.size()]), bookIds);
                break;
            case AppConfig.StudyConfig.TYPE_WORD_TYPE_SELECTED:
                String []wordTypes = WordEntity.WordType.getTypeStrings().toArray(new String[0]);
                int []wordTypeIds = new int[WordEntity.WordType.getTypes().size()];
                HashMap<Integer, Integer> wordSum = new HashMap<>();
                for(i = 0; i < WordEntity.WordType.getTypes().size(); i++) {
                    wordTypeIds[i] = WordEntity.WordType.getTypes().get(i);
                    int sum = wordModel.getWordSumByType(wordTypeIds[i]);
                    wordSum.put(wordTypeIds[i], sum);
                    wordTypes[i] = wordTypes[i] + "（单词数:" + sum + "个）";
                }
                getView().showTypeSelectDialog(wordTypes, wordTypeIds, wordSum);
                break;
            case AppConfig.StudyConfig.TYPE_ALL_WORD:
                getStudentConfig().setUnitName("全词背诵");
                int fullsize = wordModel.getWordSum();
                getView().showStudyFinalEditDialog(fullsize, getStudentConfig().getPerPart());
                break;
        }
    }

    public void stepEditStudyWordType(int []wordTypes) {
        if(wordTypes.length <= 0 || wordTypes.length > 2) {
            stepEditStudyType(AppConfig.StudyConfig.TYPE_WORD_TYPE_SELECTED);
            ToastTool.shortShow("请选择1到2项！");
            return;
        }
        getStudentConfig().getRandomTestTypeScope().clear();
        int fullsize = 0;
        for(int typeId : wordTypes) {
            getStudentConfig().getRandomTestTypeScope().add(typeId);
            fullsize = fullsize + wordModel.getWordSumByType(typeId);
        }
        getStudentConfig().setUnitName(getTypeScopeUnitName(getStudentConfig().getRandomTestTypeScope()));
        getView().showStudyFinalEditDialog(fullsize, getStudentConfig().getPerPart());
    }

    public void stepEditStudyBook(int bookId) {
        BookEntity bookEntity = bookModel.getBookById(bookId);
        getStudentConfig().setTestBookId(bookId);
        getStudentConfig().setUnitName(bookEntity.getName());
        List<UnitEntity> unitEntities = unitModel.getAllUnitEntityWithoutWord(bookId, true);
        int fullsize = 0;
        for(UnitEntity unitEntity : unitEntities) {
            fullsize += wordModel.getWordSumByUnitId(unitEntity.getId());
        }
        getView().showEditStudyConfirmDialog();
    }

    public void backStepToAfterEditedStudyType() {
        if(getStudentConfig().getStudyType() !=AppConfig.StudyConfig.TYPE_ALL_WORD)
            stepEditStudyType(getStudentConfig().getStudyType());
        else
            startEditStudy();
    }

    public void stepComfirmEditStep(String perPartString, int fullsize) {
        int perPart;
        try {
            perPart = Integer.parseInt(perPartString);
        } catch (Exception e) {
            backStepToFinalStep();
            return;
        }
        if(perPart > fullsize || perPart <= 0)
            backStepToFinalStep();
        getStudentConfig().setPerPart(perPart);
        getView().showEditStudyConfirmDialog();
    }

    public void backStepToFinalStep() {
        switch(getStudentConfig().getStudyType()) {
            case AppConfig.StudyConfig.TYPE_ALL_WORD:
                stepEditStudyType(getStudentConfig().getStudyType());
                break;
            case AppConfig.StudyConfig.TYPE_NORMAL_BOOK:
                stepEditStudyType(getStudentConfig().getStudyType());
                break;
            case AppConfig.StudyConfig.TYPE_WORD_TYPE_SELECTED:
                int[] selectTypes = ArrayTool.getIntArray(getStudentConfig().getRandomTestTypeScope());
                stepEditStudyWordType(selectTypes);
                break;
        }
    }

    public void cancleEditConfig() {
        AppConfig.reload();
    }

    public void saveEditStudy(){
        getStudentConfig().reset();
        AppConfig.save();
        initStudy();
    }

    public void saveEditReview() {
        getReviewConfig().reset();
        AppConfig.save();
        initReview();
    }

    public String getTypeScopeUnitName(List<Integer> typeScope) {
        String name = "专项训练：";
        for(int i = 0; i < typeScope.size(); i++) {
            name = name + WordEntity.WordType.getTypeString(getStudentConfig().getRandomTestTypeScope().get(i));
            if(i != typeScope.size() - 1)
                name = name + " · ";
            else
                name = name + "";
        }
        return name;
    }

    public void startEditReview() {
        String []levelNames = AppConfig.ReviewConfig.getAllLevelName().toArray(new String[0]);
        int []levleIds = ArrayTool.getIntArray(AppConfig.ReviewConfig.getAllLevelId());
        getView().showEditReviewLevelDialog(levelNames, levleIds);
    }

    public void stepEditReviewLevel(int level) {
        getReviewConfig().setReviewLevel(level);
        String []dayStrings = {"最近 1 天", "最近 2 天（推荐）", "最近 3 天", "最近 4 天"};
        getView().showEditReviewDayScopeDialog(dayStrings);
    }

    public void stepEditReviewDayscope(int dayscope) {
        getReviewConfig().setReviewDayScope(dayscope);
        getView().showEditReviewConfirmDialog();
    }

    public void backToEditReviewLevel() {
        startEditReview();
    }

    public void backToEditReviewDayscope() {
        stepEditReviewLevel(getReviewConfig().getReviewLevel());
    }
}