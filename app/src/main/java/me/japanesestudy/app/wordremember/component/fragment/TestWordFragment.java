package me.japanesestudy.app.wordremember.component.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.gc.materialdesign.views.ButtonFloat;
import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.RefactoredDefaultItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.ItemShadowDecorator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.SimpleListDividerDecorator;
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;
import java.util.TreeMap;

import me.japanesestudy.app.wordremember.R;
import me.japanesestudy.app.wordremember.component.acitvity.TestingActivity;
import me.japanesestudy.app.wordremember.component.acitvity.WordActivity;
import me.japanesestudy.app.wordremember.component.adapter.TestWordListAdapter;
import me.japanesestudy.app.wordremember.component.application.AppConfig;
import me.japanesestudy.app.wordremember.component.application.StaticPrams;
import me.japanesestudy.app.wordremember.datasource.data.AbstractWordUnit;
import me.japanesestudy.app.wordremember.datasource.entity.BookEntity;
import me.japanesestudy.app.wordremember.frame.presenter.TestWordPresenter;
import me.japanesestudy.app.wordremember.frame.view.ITestWordView;
import me.japanesestudy.app.wordremember.tools.ToastTool;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;

/**
 * Created by guyu on 2018/1/9.
 */

@ContentView(R.layout.fragment_test_word)
public class TestWordFragment extends Fragment implements ITestWordView, RecyclerViewExpandableItemManager.OnGroupExpandListener, RecyclerViewExpandableItemManager.OnGroupCollapseListener, TestWordListAdapter.TestWordListAdpaterLintener {

    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mWrappedAdapter;
    private TestWordListAdapter testWordListAdapter;
    private RecyclerViewExpandableItemManager mRecyclerViewExpandableItemManager;
    @ViewInject(R.id.toolbar)
    private Toolbar toolbar;
    @ViewInject(R.id.btn_clear)
    private Button clearButton;
    @ViewInject(R.id.btn_start_test)
    private ButtonFloat btnStartTest;

    private TestWordPresenter mPresenter = new TestWordPresenter(this);
    private static boolean needToRefresh = true;

    public TestWordFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view =  x.view().inject(this, inflater, container);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        firstLaunch();
    }

    public void firstLaunch() {
        if(AppConfig.getInstance().isFirstLaunch()) {
            new MaterialTapTargetPrompt.Builder(getActivity())
                    .setTarget(btnStartTest)
                    .setPrimaryText("开始测试按钮")
                    .setSecondaryText("选择好要测试的单元后，按这个按钮开始测试")
                    .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener()
                    {
                        @Override
                        public void onPromptStateChanged(MaterialTapTargetPrompt prompt, int state)
                        {
                            if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED)
                            {
                                // User has pressed the prompt target
                                prompt.dismiss();
                                AppConfig appConfig = AppConfig.getInstance();
                                appConfig.setFirstLaunch(false);
                            }
                        }
                    })
                    .show();
        }
    }

    public void initView() {
        mLayoutManager = new LinearLayoutManager(getContext());
        final GeneralItemAnimator animator = new RefactoredDefaultItemAnimator();
        // Change animations are enabled by default since support-v7-recyclerview v22.
        // Need to disable them when using animation indicator.
        animator.setSupportsChangeAnimations(false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(animator);
        mRecyclerView.setHasFixedSize(false);
        //noinspection StatementWithEmptyBody
        if (supportsViewElevation()) {
            // Lollipop or later has native drop shadow feature. ItemShadowDecorator is not required.
        } else {
            mRecyclerView.addItemDecoration(new ItemShadowDecorator((NinePatchDrawable) ContextCompat.getDrawable(getContext(), R.drawable.material_shadow_z1)));
        }
        mRecyclerView.addItemDecoration(new SimpleListDividerDecorator(ContextCompat.getDrawable(getContext(), R.drawable.list_divider_h), true));
    }

    public Context getContext() {
        return getActivity();
    }

    @Override
    public void onGroupCollapse(int groupPosition, boolean fromUser, Object payload) {
    }

    @Override
    public void onGroupExpand(int groupPosition, boolean fromUser, Object payload) {
        if (fromUser) {
            adjustScrollPositionOnGroupExpanded(groupPosition);
        }
    }

    private void adjustScrollPositionOnGroupExpanded(int groupPosition) {
        int childItemHeight = getActivity().getResources().getDimensionPixelSize(R.dimen.list_item_height);
        int topMargin = (int) (getActivity().getResources().getDisplayMetrics().density * 16); // top-spacing: 16dp
        int bottomMargin = topMargin; // bottom-spacing: 16dp

        mRecyclerViewExpandableItemManager.scrollToGroup(groupPosition, childItemHeight, topMargin, bottomMargin);
    }


    @Override
    public void onDestroyView() {
        if (mRecyclerViewExpandableItemManager != null) {
            mRecyclerViewExpandableItemManager.release();
            mRecyclerViewExpandableItemManager = null;
        }

        if (mRecyclerView != null) {
            mRecyclerView.setItemAnimator(null);
            mRecyclerView.setAdapter(null);
            mRecyclerView = null;
        }

        if (mWrappedAdapter != null) {
            WrapperAdapterUtils.releaseAll(mWrappedAdapter);
            mWrappedAdapter = null;
        }
        mLayoutManager = null;

        super.onDestroyView();
    }

    private boolean supportsViewElevation() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
    }

    @Override
    public void initAdapter(TreeMap<BookEntity, List<AbstractWordUnit>> bookToUnitsMap, List<AbstractWordUnit> historyEntities) {
        mRecyclerViewExpandableItemManager = new RecyclerViewExpandableItemManager(null);
        mRecyclerViewExpandableItemManager.setOnGroupExpandListener(this);
        mRecyclerViewExpandableItemManager.setOnGroupCollapseListener(this);        // additional decorations
        testWordListAdapter = new TestWordListAdapter(mRecyclerViewExpandableItemManager, bookToUnitsMap, historyEntities);
        mWrappedAdapter = mRecyclerViewExpandableItemManager.createWrappedAdapter(testWordListAdapter);
        mRecyclerView.setAdapter(mWrappedAdapter);  // requires *wrapped* adapter
        mRecyclerViewExpandableItemManager.attachRecyclerView(mRecyclerView);
        testWordListAdapter.setTestWordListAdpaterLintener(this);

    }

    @Event(R.id.btn_clear)
    private void onClearBtnClicked(View view) {
        mPresenter.initTestUnitListAdapter();
    }

    @Event(R.id.btn_start_test)
    private void onStartTestButtonClicked(View view) {
        AbstractWordUnit testUnit = testWordListAdapter.getTestUnit();
        if(testUnit == null) {
            ToastTool.shortShow("请至少选择一门课程!");
            return;
        }
        mPresenter.dealTestUnitNewName(testUnit);
        Intent intent = new Intent(getActivity(), TestingActivity.class);
        intent.putExtra(StaticPrams.KEY_TEST_UNIT, testUnit);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(needToRefresh) {
            mPresenter.initTestUnitListAdapter();
            needToRefresh = false;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if(!hidden && needToRefresh) {
            mPresenter.initTestUnitListAdapter();
            needToRefresh = false;
        }
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onChildClicked(AbstractWordUnit testUnit) {
        WordActivity.startWordActivity(this.getActivity(), testUnit);
    }

    public static void callRefresh() {
        needToRefresh = true;
    }
}
