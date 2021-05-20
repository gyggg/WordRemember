package me.japanesestudy.app.wordremember.component.acitvity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.rey.material.widget.Button;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import me.japanesestudy.app.wordremember.R;
import me.japanesestudy.app.wordremember.component.application.AppConfig;
import me.japanesestudy.app.wordremember.component.application.StaticPrams;
import me.japanesestudy.app.wordremember.component.fragment.TestWordFragment;
import me.japanesestudy.app.wordremember.datasource.data.AbstractWordUnit;
import me.japanesestudy.app.wordremember.datasource.entity.HistoryEntity;
import me.japanesestudy.app.wordremember.frame.presenter.TestingPresenter;
import me.japanesestudy.app.wordremember.frame.view.ITestingView;
import me.japanesestudy.app.wordremember.tools.ToastTool;

/**
 * Created by guyu on 2018/1/17.
 */

@ContentView(R.layout.acitivity_test_word)
public class TestingActivity extends AppCompatActivity implements ITestingView{
    public final static int MODE_BEFORE_COMMIT = 0;
    private final static int MODE_COMMIT_RIGHT = 1;
    private final static int MODE_COMMIT_WRONG = 2;

    @ViewInject(R.id.btn_back)
    ImageButton btnBack;
    @ViewInject(R.id.tv_translate)
    TextView tvTranslate;
    @ViewInject(R.id.tv_word_type)
    TextView tvWordType;
    @ViewInject(R.id.tv_key)
    TextView tvKey;
    @ViewInject(R.id.et_input_key)
    EditText etInputKey;
    @ViewInject(R.id.btn_commit)
    Button btnCommit;
    @ViewInject(R.id.btn_skip)
    Button btnSkip;
    @ViewInject(R.id.tv_title)
    TextView tvTitle;
    @ViewInject(R.id.tv_last_word)
    TextView tvLastWord;
    @ViewInject(R.id.tv_last_key)
    TextView tvLastKey;
    AbstractWordUnit abstractWordUnit;
    TestingPresenter mPresenter;
    String resultString = "";

    int mode;
    boolean isStudyTest;
    boolean isReviewTest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        init();
    }

    public void init() {
        abstractWordUnit = (AbstractWordUnit) getIntent().getSerializableExtra(StaticPrams.KEY_TEST_UNIT);
        TestWordFragment.callRefresh();
        isStudyTest = getIntent().getBooleanExtra(StaticPrams.KEY_STUDY_UNIT, false);
        isReviewTest = getIntent().getBooleanExtra(StaticPrams.KEY_REVIEY_UNIT, false);
        mPresenter = new TestingPresenter(this, abstractWordUnit, isStudyTest, isReviewTest);
        mPresenter.setNextWord();
    }

    @Override
    public void onCommitResult(boolean isRight) {
        if(isRight)
            mode = MODE_COMMIT_RIGHT;
        else
            mode = MODE_COMMIT_WRONG;
        setMode(mode);
    }

    @Override
    public void onSetNextWord(String translate, String wordType, String keyword) {
        mode = MODE_BEFORE_COMMIT;
        tvTranslate.setText(translate);
        tvWordType.setText("[" + wordType + "]");
        tvKey.setText(keyword);
        etInputKey.setText("");
        setMode(mode);
        mPresenter.setTestingInfo();
    }

    @Override
    public void onNoNextWordWithSetName(HistoryEntity newHistory) {
        String prefill = newHistory.getCollectedName() != null ? newHistory.getCollectedName() : newHistory.getNewName();
        if(prefill.length() > StaticPrams.TITLE_LIMIT)
            prefill = prefill.substring(0, StaticPrams.TITLE_LIMIT);
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .title("自定义标题")
                .content("为了能在历史记录中容易找到这条历史,可以自定义历史纪录标题")
                .positiveText("确定")
                .input("30字以内的标题", prefill, false, (dialog, input) -> {

                })
                .inputRange(1, StaticPrams.TITLE_LIMIT)
                .onPositive((dialog, which) -> {
                    String name = dialog.getInputEditText().getText().toString();
                    mPresenter.resetHistoryName(newHistory, name);
                    onNoNextWord(newHistory);
                });
        builder.show().setCancelable(false);
    }

    @Override
    public void onNoNextWord(HistoryEntity newHistory) {
        //TODO:set test result
        ToastTool.shortShow("测试结束");
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .title("测试结束")
                .content(resultString)
                .negativeText("结束测试");
        if(newHistory != null && newHistory.getWrongWordSize() > 0) {
            builder.positiveText("继续测试错词");
            builder.onPositive((dialog, which) -> {
                dialog.dismiss();
                this.setNextTesting(newHistory);
            });
        }
        builder.onNegative((dialog, which) -> {
            dialog.dismiss();
            this.finish();
        });
        builder.show().setCancelable(false);
    }

    @Override
    public void onSkipWord() {
        mode = MODE_BEFORE_COMMIT;
        mPresenter.setNextWord();
    }


    @Override
    public void onSetTestingInfo(int testedWordPosition, int testWordSum, double rightPercent, int wrongSum, int SkipSum, int rightSum, String translate, String wordType, String keyword) {
        //TODO:set testing info
        tvTitle.setText("(" + testedWordPosition + "/" + testWordSum + ")");
        resultString = "正确数：" + (testWordSum - wrongSum) + "\n" +
                "错误数：" + wrongSum + "\n" +
                "正确率：" + (rightPercent * 100) + "%";
        tvLastWord.setText("上一词：" + translate + wordType);
        tvLastKey.setText("答案：" + keyword);
    }

    @Override
    public void onForwardBack(HistoryEntity historyEntity) {
        if(!AppConfig.getInstance().isAutoHistoryName() && historyEntity.getParentHistoryId() == 0) {
            String prefill = historyEntity.getCollectedName() != null ? historyEntity.getCollectedName() : historyEntity.getNewName();
            if(prefill.length() > 20)
                prefill = prefill.substring(0, 19);
            MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                    .title("自定义标题")
                    .content("为了能在历史记录中容易找到这条历史,可以自定义历史纪录标题")
                    .positiveText("确定")
                    .input("20字以内的标题", prefill, false, (dialog, input) -> {

                    })
                    .inputRange(1, 20)
                    .onPositive((dialog, which) -> {
                        String name = dialog.getInputEditText().getText().toString();
                        mPresenter.resetHistoryName(historyEntity, name);
                        finish();
                    });
            builder.show().setCancelable(false);
        } else {
            finish();
        }
    }

    @Event(R.id.btn_back)
    private void back(View v) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .title("提前结束！")
                .content("请确认是否要提前结束，如要保存本次测试历史，所有未测试的单词均会被当作错误处理")
                .positiveText("继续测试")
                .neutralText("结束并保存记录")
                .negativeText("结束但不保存记录")
                .onPositive((dialog, which) -> {
                    dialog.dismiss();
                })
                .onNeutral((dialog, which) -> {
                    dialog.dismiss();
                    mPresenter.forwardFinishTesting();
                })
                .onNegative((dialog, which) -> {
                    dialog.dismiss();
                    this.finish();
                });
        builder.show().setCancelable(false);
    }

    @Event(R.id.btn_commit)
    private void commit(View v) {
        if(mode == MODE_BEFORE_COMMIT)
            mPresenter.judgeWord(etInputKey.getText().toString());
        else
            mPresenter.setNextWord();
    }

    @Event(R.id.btn_skip)
    private void skip(View v) {
        if(mode == MODE_BEFORE_COMMIT)
            mPresenter.skipWord();
        else
            mPresenter.setNextWord();
    }

    private void setMode(int mode) {
        this.mode = mode;
        switch (mode){
            case MODE_BEFORE_COMMIT:
                tvKey.setVisibility(View.GONE);
                btnCommit.setBackgroundResource(R.drawable.bg_test_commit_handle);
                setButtonText(btnCommit, "提交");
                btnSkip.setClickable(true);
                break;
            case MODE_COMMIT_RIGHT:
                tvKey.setVisibility(View.VISIBLE);
                tvKey.setTextColor(getResources().getColor(R.color.colorRight));
                btnCommit.setBackgroundResource(R.drawable.bg_test_right_handle);
                setButtonText(btnCommit, "下一个");
                btnSkip.setClickable(false);
                break;
            case MODE_COMMIT_WRONG:
                tvKey.setVisibility(View.VISIBLE);
                tvKey.setTextColor(getResources().getColor(R.color.colorError));
                btnCommit.setBackgroundResource(R.drawable.bg_test_error_handle);
                setButtonText(btnCommit, "下一个");
                btnSkip.setClickable(false);
                break;
        }
    }


    public void setButtonText(Button buttonRectangle, String text) {
//        TextView textView = (TextView) ReflectionUtils.getFieldValue(buttonRectangle, "textButton");
//        textView.setText(text);
        buttonRectangle.setText(text);
    }

    public void setNextTesting(HistoryEntity newHistory) {
        mPresenter = new TestingPresenter(this, newHistory, isStudyTest, isReviewTest);
        mPresenter.setNextWord();
    }

    public static void startTestingActivity(Context context, AbstractWordUnit abstractWordUnit) {
        startTestingActivity(context, abstractWordUnit, StaticPrams.TYPE_NORMAL_TEST_UNIT);
    }

    public static void startTestingActivity(Context context, AbstractWordUnit abstractWordUnit, int type) {
        Intent intent = new Intent(context, TestingActivity.class);
        intent.putExtra(StaticPrams.KEY_TEST_UNIT, abstractWordUnit);
        switch (type) {
            case StaticPrams.TYPE_STUDY_UNIT:
                intent.putExtra(StaticPrams.KEY_STUDY_UNIT, true);
                break;
            case StaticPrams.TYPE_REVIEW_UNIT:
                intent.putExtra(StaticPrams.KEY_REVIEY_UNIT, true);
                break;
        }
        context.startActivity(intent);
    }
}
