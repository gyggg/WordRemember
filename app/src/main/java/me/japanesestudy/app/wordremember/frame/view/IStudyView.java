package me.japanesestudy.app.wordremember.frame.view;

import java.util.Map;

import me.japanesestudy.app.wordremember.datasource.data.WordUnit;

/**
 * Created by guyu on 2018/1/23.
 */

public interface IStudyView {
    //study model
    void initStudy(WordUnit testUnit);
    void onFinishStudy(String unitName, String info, int maxnumber);
    void onStudyNull(String unitName);
    void showStartEditStudyDialog(String[] studyTypes, int[] studyTypeIds);
    void showBookSelectDialog(String[] bookNames, int[] bookIds);
    void showTypeSelectDialog(String[] typeStrings, int[] typeIds, Map<Integer, Integer> typeWordSum);
    void showStudyFinalEditDialog(int fullSize, int defPerPart);
    void showEditStudyConfirmDialog();

    //review model
    void initReview(WordUnit testUnit);
    void onFinishReview(String unitName, String info, int maxnumber);
    void showEditReviewLevelDialog(String[] level, int[] levelIds);
    void showEditReviewDayScopeDialog(String[] dayscope);
    void showEditReviewConfirmDialog();
}
