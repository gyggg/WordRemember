package me.japanesestudy.app.wordremember.component.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.h6ah4i.android.widget.advrecyclerview.decoration.ItemShadowDecorator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.SimpleListDividerDecorator;
import com.oushangfeng.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

import me.japanesestudy.app.wordremember.R;
import me.japanesestudy.app.wordremember.component.acitvity.WordActivity;
import me.japanesestudy.app.wordremember.component.adapter.HistoryListAdapter;
import me.japanesestudy.app.wordremember.component.application.StaticPrams;
import me.japanesestudy.app.wordremember.datasource.data.HistoryShown;
import me.japanesestudy.app.wordremember.datasource.entity.HistoryEntity;
import me.japanesestudy.app.wordremember.frame.presenter.HistoryPresenter;
import me.japanesestudy.app.wordremember.frame.view.IHistoryView;
import me.japanesestudy.app.wordremember.tools.ToastTool;

/**
 * Created by guyu on 2018/1/9.
 */

@ContentView(R.layout.fragment_history)
public class HistoryFragment extends Fragment implements IHistoryView{

    @ViewInject(R.id.recycler_view_history)
    RecyclerView mRecyclerView;

    HistoryListAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.ItemDecoration mItemDecoration;

    HistoryPresenter mPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view =  x.view().inject(this, inflater, container);
        mPresenter = new HistoryPresenter(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecylerView();
    }

    public void initRecylerView() {
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        mItemDecoration = new PinnedHeaderItemDecoration.Builder(HistoryListAdapter.TYPE_HEADER)
                .enableDivider(false)
                .disableHeaderClick(true)
                .create();
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(mItemDecoration);
        mRecyclerView.setHasFixedSize(false);
        //noinspection StatementWithEmptyBody
        if (supportsViewElevation()) {
            // Lollipop or later has native drop shadow feature. ItemShadowDecorator is not required.
        } else {
            mRecyclerView.addItemDecoration(new ItemShadowDecorator((NinePatchDrawable) ContextCompat.getDrawable(getActivity(), R.drawable.material_shadow_z1)));
        }
        mRecyclerView.addItemDecoration(new SimpleListDividerDecorator(ContextCompat.getDrawable(getActivity(), R.drawable.list_divider_h), true));
    }

    @Override
    public void initListAdapter(List<MultiItemEntity> multiItemEntities) {
        if(mAdapter == null) {
            mAdapter = new HistoryListAdapter(multiItemEntities);
            mAdapter.setHistoryShownBtnClickListener(new HistoryListAdapter.HistoryShownBtnClickListener() {
                @Override
                public void onCollected(boolean collected, HistoryShown historyShown, int itemPosition) {
                    TestWordFragment.callRefresh();
                    if (collected) {
                        HistoryEntity historyEntity = historyShown.getHistoryEntity();
                        String prefill = historyEntity.getCollectedName() != null ? historyEntity.getCollectedName() : historyEntity.getNewName();
                        if(prefill.length() > StaticPrams.TITLE_LIMIT)
                            prefill = prefill.substring(0, StaticPrams.TITLE_LIMIT);
                        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity())
                                .title("设置名字")
                                .content("收藏的历史纪录可以被自由测试选中，为它加一个名字方便你找到它。")
                                .input("30字以内的标题", prefill, false, (dialog, input) -> {

                                }).inputRange(1, StaticPrams.TITLE_LIMIT)
                                .positiveText("确定")
                                .onPositive((dialog, which) -> {
                                    String name = dialog.getInputEditText().getText().toString();
                                    mPresenter.collectHistory(historyShown, name, itemPosition);
                                });
                        builder.show().setCancelable(false);
                    } else {
                        mPresenter.uncollectHistory(historyShown, itemPosition);
                    }
                }

                @Override
                public void onDeleted(HistoryShown historyShown, int itemPosition) {
                    TestWordFragment.callRefresh();
                    MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity());
                    HistoryEntity historyEntity = historyShown.getHistoryEntity();
                    if (historyEntity.getParentHistoryId() == 0) {
                        builder.title("警告：你正在删除原始记录!")
                                .content("你现在将要删除的是原始记录，只有当该记录下的后续记录都被删除时该记录才可以被删除！")
                                .negativeText("取消");
                        if (historyEntity.getNextHistoryEntities().isEmpty()) {
                            builder.positiveText("确定")
                                    .onPositive((dialog, which) -> {
                                        mPresenter.deleteHistory(historyShown, itemPosition);
                                    });
                        }
                    } else {
                        builder.title("删除这条记录?")
                                .content("注意，该操作不可逆！")
                                .negativeText("取消")
                                .positiveText("确定")
                                .onPositive((dialog, which) -> {
                                    mPresenter.deleteHistory(historyShown, itemPosition);
                                });
                    }
                    builder.show().setCancelable(false);
                }

                @Override
                public void onHistoryClicked(HistoryShown historyShown) {
                    mPresenter.showHistory(historyShown);
                }

            });
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.refresh(multiItemEntities);
        }
    }

    @Override
    public void onHistoryCollected(HistoryShown historyShown, HistoryEntity data, int itemPosition) {
        mAdapter.collectedHistory(historyShown, data, itemPosition);
    }

    @Override
    public void onHistoryDeleted(HistoryShown historyShown, int itemPosition) {
        mAdapter.deleteHistory(historyShown, itemPosition);
    }

    @Override
    public void onHistoryClicked(HistoryEntity historyEntity) {
        //TODO:跳转到单词页面
        if(historyEntity.getTestUnitSize() <= 0) {
            ToastTool.shortShow("没有错词的记录不能打开");
            return;
        }
        Intent intent = new Intent(getActivity(), WordActivity.class);
        intent.putExtra(StaticPrams.KEY_TEST_UNIT, historyEntity);
        startActivity(intent);
    }

    private boolean supportsViewElevation() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.initListAdapter();
    }
}
