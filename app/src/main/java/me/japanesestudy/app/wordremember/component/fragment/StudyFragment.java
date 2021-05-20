package me.japanesestudy.app.wordremember.component.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.rey.material.widget.ProgressView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Arrays;
import java.util.Map;

import me.japanesestudy.app.wordremember.R;
import me.japanesestudy.app.wordremember.component.acitvity.TestingActivity;
import me.japanesestudy.app.wordremember.component.acitvity.WordActivity;
import me.japanesestudy.app.wordremember.component.application.AppConfig;
import me.japanesestudy.app.wordremember.component.application.StaticPrams;
import me.japanesestudy.app.wordremember.datasource.data.WordUnit;
import me.japanesestudy.app.wordremember.frame.presenter.StudyPresenter;
import me.japanesestudy.app.wordremember.frame.view.IStudyView;
import me.japanesestudy.app.wordremember.tools.ArrayTool;

/**
 * Created by guyu on 2018/1/23.
 */
@ContentView(R.layout.fragment_study)
public class
StudyFragment extends Fragment implements IStudyView{

    @ViewInject(R.id.tv_unit_name)
    TextView tvUnitName;
    @ViewInject(R.id.tv_unit_info)
    TextView tvUnitInfo;
    @ViewInject(R.id.tv_unit_number)
    TextView tvUnitNumber;
    @ViewInject(R.id.tv_start_study)
    TextView tvStartStudy;
    @ViewInject(R.id.tv_start_test)
    TextView tvStartTest;
    @ViewInject(R.id.pv_study_progress)
    ProgressView pvStudyProgress;
    @ViewInject(R.id.tv_start_more_study)
    TextView tvStartMoreStudy;


    @ViewInject(R.id.tv_review_name)
    TextView tvReviewName;
    @ViewInject(R.id.tv_review_info)
    TextView tvReviewInfo;
    @ViewInject(R.id.tv_review_number)
    TextView tvReviewNumber;
    @ViewInject(R.id.tv_start_review)
    TextView tvStartReview;
    @ViewInject(R.id.pv_review_progress)
    ProgressView pvReviewProgress;

    StudyPresenter mPresenter;

    WordUnit studyUnit;
    WordUnit reviewUnit;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return x.view().inject(this, inflater, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter = new StudyPresenter(this);
    }


    @Override
    public void onResume() {
        super.onResume();
        mPresenter.initStudy();
        mPresenter.initReview();
    }

    @Override
    public void initStudy(WordUnit testUnit) {
        tvUnitName.setText(testUnit.getNewName());
        tvUnitInfo.setText(testUnit.getInfo());
        tvUnitNumber.setText(testUnit.getFinishedSize() + " / " + testUnit.getSrcSize());
        pvStudyProgress.setProgress(testUnit.getFinishedPercent());
        studyUnit = testUnit;
        tvStartStudy.setTextColor(getResources().getColor(R.color.colorAccent));
        tvStartStudy.setClickable(true);
        tvStartTest.setTextColor(getResources().getColor(R.color.colorAccent));
        tvStartTest.setClickable(true);
        tvStartMoreStudy.setVisibility(View.GONE);
    }

    @Override
    public void initReview(WordUnit testUnit) {
        tvReviewName.setText(testUnit.getNewName());
        tvReviewInfo.setText(testUnit.getInfo());
        tvReviewNumber.setText(testUnit.getFinishedSize() + " / " + testUnit.getSrcSize());
        pvReviewProgress.setProgress(testUnit.getFinishedPercent());
        reviewUnit = testUnit;
        tvStartReview.setClickable(true);
        tvStartReview.setTextColor(getResources().getColor(R.color.colorAccent));
    }

    @Override
    public void onFinishStudy(String unitName, String info, int maxnumber) {
        tvUnitName.setText(unitName);
        tvUnitInfo.setText(info);
        tvUnitNumber.setText(maxnumber + " / " + maxnumber);
        pvStudyProgress.setProgress(1);
        tvStartStudy.setTextColor(getResources().getColor(R.color.colorSkip));
        tvStartStudy.setClickable(false);
        tvStartTest.setTextColor(getResources().getColor(R.color.colorSkip));
        tvStartTest.setClickable(false);
        tvStartMoreStudy.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFinishReview(String unitName, String info, int maxnumber) {
        tvReviewName.setText(unitName);
        tvReviewInfo.setText(info);
        tvReviewNumber.setText(maxnumber + " / " + maxnumber);
        pvReviewProgress.setProgress(1);
        tvStartReview.setClickable(false);
        tvStartReview.setTextColor(getResources().getColor(R.color.colorSkip));
    }

    @Override
    public void onStudyNull(String unitName) {
        tvUnitName.setText(unitName);
        tvUnitInfo.setText("【点击修改计划以新建学习计划】");
        tvUnitNumber.setText("已完成全部训练");
        pvStudyProgress.setProgress(1);
        tvStartStudy.setTextColor(getResources().getColor(R.color.colorSkip));
        tvStartStudy.setClickable(false);
        tvStartTest.setTextColor(getResources().getColor(R.color.colorSkip));
        tvStartTest.setClickable(false);
        tvStartMoreStudy.setVisibility(View.GONE);
    }

    @Event(R.id.tv_study_edit)
    private void startEditStudy(View view) {
        mPresenter.startEditStudy();
    }

    @Event(R.id.tv_start_study)
    private void startStudy(View view) {
        studyUnit.setNewName(tvUnitName.getText() + "" + tvUnitInfo.getText());
        WordActivity.startWordActivity(this.getActivity(), studyUnit, StaticPrams.TYPE_STUDY_UNIT);
    }

    @Event(R.id.tv_review_edit)
    private void startEditReview(View view) {
        mPresenter.startEditReview();
    }

    @Event(R.id.tv_start_review)
    private void startReview(View view) {
        TestingActivity.startTestingActivity(this.getActivity(), reviewUnit, StaticPrams.TYPE_REVIEW_UNIT);
    }

    @Event(R.id.tv_start_more_study)
    private void startMoreStudy(View view) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this.getActivity());
        builder.title("继续学习")
                .content("想要再来一部分单词背诵吗，小心吃不消。")
                .negativeText("算了")
                .positiveText("再来！")
                .onPositive((dialog, which) -> {
                    mPresenter.moreStudy();
                });
        builder.show();
    }

    @Event(R.id.tv_start_test)
    private void startText(View view) {
        studyUnit.setNewName(tvUnitName.getText() + "" + tvUnitInfo.getText());
        TestingActivity.startTestingActivity(this.getActivity(), studyUnit, StaticPrams.TYPE_STUDY_UNIT);
    }

    @Override
    public void showStartEditStudyDialog(String[] studyTypes, int[] studyTypeIds) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this.getActivity());
        builder.title("新建计划")
                .content("请注意！新建学习计划会重置当前全部进度！从下面选择一个计划。")
                .items(studyTypes)
                .itemsIds(studyTypeIds)
                .itemsCallback((dialog, itemView, position, text) -> {
                    int typeId = itemView.getId();
                    mPresenter.stepEditStudyType(typeId);
                })
                .negativeText("取消")
                .onNegative((dialog, which) -> {
                    mPresenter.cancleEditConfig();
                });
        builder.show().setCancelable(false);
    }

    @Override
    public void showBookSelectDialog(String[] bookNames, int[] bookIds) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this.getActivity());
        int defBookId = AppConfig.getStudyConfig().getTestBookId();
        defBookId = Arrays.binarySearch(bookIds, defBookId);
        builder.title("选择课本")
                .content("请选择下列课本中的一个")
                .items(bookNames)
                .itemsIds(bookIds)
                .itemsCallbackSingleChoice(defBookId, (dialog, itemView, which, text) -> false)
                .negativeText("取消")
                .onNegative((dialog, which) -> {
                    mPresenter.cancleEditConfig();
                })
                .positiveText("下一步")
                .onPositive((dialog, which) -> {
                    int bookId = bookIds[dialog.getSelectedIndex()];
                    mPresenter.stepEditStudyBook(bookId);
                })
                .neutralText("上一步")
                .onNeutral((dialog, which) -> {
                    mPresenter.startEditStudy();
                });
        builder.show().setCancelable(false);
    }

    @Override
    public void showTypeSelectDialog(String[] typeStrings, int[] typeIds, Map<Integer, Integer> typeWordSum) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this.getActivity());
        Integer []defTypeIds = ArrayTool.getIntegerArray(AppConfig.getStudyConfig().getRandomTestTypeScope());
        builder.title("选择词性")
                .content("请选择下列词性中至少1项，最多2项")
                .items(typeStrings)
                .itemsIds(typeIds)
                .itemsCallbackMultiChoice(defTypeIds, (dialog, which, text) -> false)
                .negativeText("取消")
                .onNegative((dialog, which) -> {
                    mPresenter.cancleEditConfig();
                })
                .positiveText("下一步")
                .onPositive((dialog, which) -> {
                    Integer []selectedIndexs = dialog.getSelectedIndices();
                    int []selectedId = ArrayTool.getIntArray(selectedIndexs);
                    mPresenter.stepEditStudyWordType(selectedId);
                })
                .neutralText("上一步")
                .onNeutral((dialog, which) -> {
                    mPresenter.startEditStudy();
                });
        builder.show().setCancelable(false);
    }

    @Override
    public void showStudyFinalEditDialog(int fullSize, int defPerPart) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this.getActivity());
        if(defPerPart > fullSize)
            defPerPart = fullSize;
        builder.title("设置每日背词数")
                .content("输入每日背词数量（最少1，最大" + fullSize + "）")
                .inputType(InputType.TYPE_CLASS_NUMBER)
                .input("输入每日要背的单词数", defPerPart + "", false, (dialog, input) -> {

                })
                .negativeText("取消")
                .onNegative((dialog, which) -> {
                    mPresenter.cancleEditConfig();
                })
                .positiveText("下一步")
                .onPositive((dialog, which) -> {
                    String perPartString = dialog.getInputEditText().getText().toString();
                    mPresenter.stepComfirmEditStep(perPartString, fullSize);
                })
                .neutralText("上一步")
                .onNeutral((dialog, which) -> {
                    mPresenter.backStepToAfterEditedStudyType();
                });
        builder.show().setCancelable(false);
    }

    @Override
    public void showEditStudyConfirmDialog() {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this.getActivity());
        builder.title("确定要新建计划吗")
                .content("会清除当前已有的学习进度且不可恢复！")
                .negativeText("取消")
                .onNegative((dialog, which) -> {
                    mPresenter.cancleEditConfig();
                })
                .positiveText("确定")
                .onPositive((dialog, which) -> {
                    mPresenter.saveEditStudy();
                })
                .neutralText("上一步")
                .onNeutral((dialog, which) -> {
                    mPresenter.backStepToFinalStep();
                });
        builder.show().setCancelable(false);
    }

    @Override
    public void showEditReviewLevelDialog(String[] level, int[] levelIds) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity());
        builder.title("重置复习强度")
                .content("重置复习强度会重置今日的复习进度，请注意！选择下面一个复习难度。")
                .items(level)
                .itemsIds(levelIds)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        int level = itemView.getId();
                        mPresenter.stepEditReviewLevel(level);
                    }
                })
                .negativeText("取消")
                .onNegative((dialog, which) -> {
                    mPresenter.cancleEditConfig();
                });
        builder.show().setCancelable(false);
    }

    @Override
    public void showEditReviewDayScopeDialog(String[] dayscope) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity());
        builder.title("设置复习天数")
                .content("选择想复习最近多少日的学习内容。")
                .items(dayscope)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        int day = position + 1;
                        mPresenter.stepEditReviewDayscope(day);
                    }
                })
                .negativeText("取消")
                .onNegative((dialog, which) -> {
                    mPresenter.cancleEditConfig();
                })
                .neutralText("上一步")
                .onNeutral((dialog, which) -> {
                    mPresenter.backToEditReviewLevel();
                });
        builder.show();
    }

    @Override
    public void showEditReviewConfirmDialog() {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this.getActivity());
        builder.title("确定要重置复习强度吗")
                .content("会清除当前已有的复习进度且不可恢复！")
                .negativeText("取消")
                .onNegative((dialog, which) -> {
                    mPresenter.cancleEditConfig();
                })
                .positiveText("确定")
                .onPositive((dialog, which) -> {
                    mPresenter.saveEditReview();
                })
                .neutralText("上一步")
                .onNeutral((dialog, which) -> {
                    mPresenter.backToEditReviewDayscope();
                });
        builder.show().setCancelable(false);
    }
}
