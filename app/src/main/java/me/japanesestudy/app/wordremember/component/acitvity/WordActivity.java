package me.japanesestudy.app.wordremember.component.acitvity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonFloat;
import com.h6ah4i.android.widget.advrecyclerview.decoration.SimpleListDividerDecorator;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

import me.japanesestudy.app.wordremember.R;
import me.japanesestudy.app.wordremember.component.adapter.WordListAdapter;
import me.japanesestudy.app.wordremember.component.application.AppConfig;
import me.japanesestudy.app.wordremember.component.application.StaticPrams;
import me.japanesestudy.app.wordremember.datasource.data.AbstractWordUnit;
import me.japanesestudy.app.wordremember.datasource.entity.WordEntity;
import me.japanesestudy.app.wordremember.frame.presenter.WordPresenter;
import me.japanesestudy.app.wordremember.frame.view.IWordView;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;

/**
 * Created by guyu on 2018/1/22.
 */
@ContentView(R.layout.activity_word)
public class WordActivity extends AppCompatActivity implements IWordView {
    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerView;
    @ViewInject(R.id.tv_title)
    private TextView tvTitle;
    @ViewInject(R.id.btn_back)
    private ImageButton btnBack;
    @ViewInject(R.id.btn_start_test)
    private ButtonFloat btnStartTest;
    private WordListAdapter mAdapter;
    private WordPresenter mPresenter;
    private AbstractWordUnit mTestUnit;


    boolean isStudyTest;
    boolean isReviewTest;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        mPresenter = new WordPresenter(this);
        initView();
        firstLaunch();
    }

    public void firstLaunch() {
        if(AppConfig.getInstance().isFirstLaunch()) {
            new MaterialTapTargetPrompt.Builder(this)
                    .setTarget(btnStartTest)
                    .setPrimaryText("开始测试按钮")
                    .setSecondaryText("按这个按钮开始测试表内的单词")
                    .setPromptStateChangeListener((prompt, state) -> {
                        if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED)
                        {
                            // User has pressed the prompt target
                            prompt.dismiss();
                            AppConfig appConfig = AppConfig.getInstance();
                            appConfig.setFirstLaunch(false);
                        }
                    })
                    .show();
        }
    }

    public void initView() {
        mTestUnit = (AbstractWordUnit) getIntent().getSerializableExtra(StaticPrams.KEY_TEST_UNIT);
        isStudyTest = getIntent().getBooleanExtra(StaticPrams.KEY_STUDY_UNIT, false);
        isReviewTest = getIntent().getBooleanExtra(StaticPrams.KEY_REVIEY_UNIT, false);
        tvTitle.setText(mTestUnit.getNewName().replace("【", "\n【"));
        btnBack.setOnClickListener(v -> {
            this.finish();
        });
        initRecylerView();
    }

    public void initRecylerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new SimpleListDividerDecorator(ContextCompat.getDrawable(this, R.drawable.list_divider_h), true));
        mRecyclerView.setHasFixedSize(false);
        mPresenter.initWordList(mTestUnit);
    }

    @Override
    public void initWordListAdapter(List<WordEntity> wordEntities) {
        if(mAdapter == null) {
            mAdapter = new WordListAdapter(wordEntities);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setNewData(wordEntities);
        }
    }

    @Override
    public void onCollected(WordEntity wordEntity, int itemPosition) {
        mAdapter.setData(itemPosition, wordEntity);
    }

    @Event(R.id.btn_start_test)
    private void startTest(View view) {
        if(isStudyTest)
            TestingActivity.startTestingActivity(this, mTestUnit, StaticPrams.TYPE_STUDY_UNIT);
        else if(isReviewTest)
            TestingActivity.startTestingActivity(this, mTestUnit, StaticPrams.TYPE_REVIEW_UNIT);
        else
            TestingActivity.startTestingActivity(this, mTestUnit);
    }

    public static void startWordActivity(Context context, AbstractWordUnit abstractWordUnit) {
        startWordActivity(context, abstractWordUnit, StaticPrams.TYPE_NORMAL_TEST_UNIT);
    }

    public static void startWordActivity(Context context, AbstractWordUnit abstractWordUnit, int type) {
        Intent intent = new Intent(context, WordActivity.class);
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
