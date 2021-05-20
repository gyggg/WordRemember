package me.japanesestudy.app.wordremember.component.adapter;

import android.support.annotation.IntRange;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.h6ah4i.android.widget.advrecyclerview.expandable.ExpandableItemConstants;
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

import me.japanesestudy.app.wordremember.R;
import me.japanesestudy.app.wordremember.component.widget.ExpandableItemIndicator;
import me.japanesestudy.app.wordremember.datasource.data.AbstractWordUnit;
import me.japanesestudy.app.wordremember.datasource.entity.BookEntity;
import me.japanesestudy.app.wordremember.datasource.data.CompileWordUnit;

/**
 * Created by guyu on 2018/1/16.
 */

public class TestWordListAdapter extends AbstractExpandableItemAdapter<TestWordListAdapter.MyGroupViewHolder, TestWordListAdapter.MyChildViewHolder> {

    private static final int GROUP_ITEM_VIEW_TYPE_SECTION_HEADER = 0;
    private static final int GROUP_ITEM_VIEW_TYPE_SECTION_ITEM = 1;
    private static final String KEY_GROUP_POSITION = "@group_positon_key";
    private static final String KEY_CHILD_POSITION = "@child_position_key";

    private interface Expandable extends ExpandableItemConstants {
    }

    private TreeMap<BookEntity, List<AbstractWordUnit>> bookToUnitsMap;
    private TreeSet<BookEntity> bookEntities;
    private List<AbstractWordUnit> historyEntities;
    private List<AbstractWordUnit> testUnits = new ArrayList<>();
    private RecyclerViewExpandableItemManager mExpandableItemManager;
    private TestWordListAdpaterLintener testWordListAdpaterLintener;

    public TestWordListAdapter(RecyclerViewExpandableItemManager mExpandableItemManager, TreeMap<BookEntity, List<AbstractWordUnit>> bookToUnitsMap, List<AbstractWordUnit> historyEntities) {
        this.bookToUnitsMap = bookToUnitsMap;
        this.bookEntities = new TreeSet<>(bookToUnitsMap.keySet());
        this.historyEntities = historyEntities;
        this.mExpandableItemManager = mExpandableItemManager;
        // ExpandableItemAdapter requires stable ID, and also
        // have to implement the getGroupItemId()/getChildItemId() methods appropriately.
        setHasStableIds(true);
    }

    public static abstract class MyBaseViewHolder extends AbstractExpandableItemViewHolder {
        public FrameLayout mContainer;
        public TextView mTextView;

        public MyBaseViewHolder(View v) {
            super(v);
            mContainer = v.findViewById(R.id.container);
            mTextView = v.findViewById(android.R.id.text1);
        }
    }

    public static class MyGroupViewHolder extends MyBaseViewHolder {
        public ExpandableItemIndicator mIndicator;
        public MyGroupViewHolder(View v) {
            super(v);
            mIndicator = v.findViewById(R.id.indicator);
        }
    }

    public static class MyChildViewHolder extends MyBaseViewHolder {
        public Button btnAdd;
        public Button btnRemove;
        public MyChildViewHolder(View v) {
            super(v);
            btnAdd = v.findViewById(R.id.btn_add);
            btnRemove = v.findViewById(R.id.btn_remove);
        }
    }

    @Override
    public int getGroupItemViewType(int groupPosition) {
        List<? extends AbstractWordUnit> res = getGroupList(groupPosition);
        if (res == null) {
            return GROUP_ITEM_VIEW_TYPE_SECTION_HEADER;
        } else {
            return GROUP_ITEM_VIEW_TYPE_SECTION_ITEM;
        }
    }

    @Override
    public int getGroupCount() {
        return bookEntities.size() + 4;
    }

    @Override
    public int getChildCount(int groupPosition) {
        if(getGroupList(groupPosition) == null)
            return 0;
        else
            return getGroupList(groupPosition).size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        long childId = 0;
        for(int i = 0; i < groupPosition; i++) {
            childId += getChildCount(groupPosition);
        }
        childId += childPosition;
        return childId;
    }

    @Override
    public MyGroupViewHolder onCreateGroupViewHolder(ViewGroup parent, @IntRange(from = -8388608L, to = 8388607L) int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        final View v;
        switch (viewType) {
            case GROUP_ITEM_VIEW_TYPE_SECTION_HEADER:
                v = inflater.inflate(R.layout.list_section_header, parent, false);
                break;
            case GROUP_ITEM_VIEW_TYPE_SECTION_ITEM:
                v = inflater.inflate(R.layout.item_test_unit_group, parent, false);
                break;
            default:
                throw new IllegalStateException("Unexpected viewType (= " + viewType + ")");
        }

        return new MyGroupViewHolder(v);
    }

    @Override
    public MyChildViewHolder onCreateChildViewHolder(ViewGroup parent, @IntRange(from = -8388608L, to = 8388607L) int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.item_test_unit_child, parent, false);
        return new MyChildViewHolder(v);
    }

    @Override
    public void onBindGroupViewHolder(MyGroupViewHolder holder, int groupPosition, @IntRange(from = -8388608L, to = 8388607L) int viewType) {
        switch (viewType) {
            case GROUP_ITEM_VIEW_TYPE_SECTION_HEADER:
                onBindSectionHeaderGroupViewHolder(holder, groupPosition);
                break;
            case GROUP_ITEM_VIEW_TYPE_SECTION_ITEM:
                onBindItemGroupViewHolder(holder, groupPosition);
                break;
        }
    }

    private void onBindItemGroupViewHolder(MyGroupViewHolder holder, int groupPosition) {
        // set text
        holder.mTextView.setText(getGroupName(groupPosition));

        // mark as clickable
        holder.itemView.setClickable(true);

        // set background resource (target view ID: container)
        final int expandState = holder.getExpandStateFlags();

        if ((expandState & ExpandableItemConstants.STATE_FLAG_IS_UPDATED) != 0) {
            int bgResId;
            boolean isExpanded;
            boolean animateIndicator = ((expandState & Expandable.STATE_FLAG_HAS_EXPANDED_STATE_CHANGED) != 0);

            if ((expandState & Expandable.STATE_FLAG_IS_EXPANDED) != 0) {
                bgResId = R.drawable.bg_group_item_expanded_state;
                isExpanded = true;
            } else {
                bgResId = R.drawable.bg_group_item_normal_state;
                isExpanded = false;
            }

            holder.mContainer.setBackgroundResource(bgResId);
            holder.mIndicator.setExpandedState(isExpanded, animateIndicator);
        }
    }

    private void onBindSectionHeaderGroupViewHolder(MyGroupViewHolder holder, int groupPosition) {
        holder.mTextView.setText(getGroupName(groupPosition));
    }


    @Override
    public void onBindChildViewHolder(MyChildViewHolder holder, int groupPosition, int childPosition, @IntRange(from = -8388608L, to = 8388607L) int viewType) {
        List<AbstractWordUnit> units = getGroupList(groupPosition);

        AbstractWordUnit unit = (AbstractWordUnit) units.get(childPosition);
        // set text
        holder.mTextView.setText(unit.getNewName());
        if(groupPosition == getGroupCount() - 1) {
            //remove from test units
            holder.btnAdd.setVisibility(View.GONE);
            holder.btnRemove.setVisibility(View.VISIBLE);
            holder.btnRemove.setOnClickListener((View v)->{
                testUnits.remove(unit);
                int guPosition = (int) unit.getTag(KEY_GROUP_POSITION);
                int removePosition = (int) unit.getTag(KEY_CHILD_POSITION);
                unit.setNewName(unit.getTestUnitName());
                getGroupList(guPosition).add(removePosition, unit);
                mExpandableItemManager.notifyChildItemRangeInserted(guPosition, removePosition, 1);
                mExpandableItemManager.notifyChildItemRangeRemoved(getGroupCount() - 1, childPosition, 1);
                mExpandableItemManager.notifyGroupItemChanged(getGroupCount() - 2);
            });
        } else {
            //insert to test units
            holder.btnAdd.setVisibility(View.VISIBLE);
            holder.btnRemove.setVisibility(View.GONE);
            holder.btnAdd.setOnClickListener((View v)->{
                testUnits.add(0, unit);
                unit.setTag(KEY_GROUP_POSITION, groupPosition);
                unit.setTag(KEY_CHILD_POSITION, childPosition);
                unit.setNewName(getGroupName(groupPosition) + "：" + unit.getTestUnitName() + "（词数：" + unit.getTestUnitSize() + "）");
                units.remove(unit);
                mExpandableItemManager.notifyChildItemRangeInserted(getGroupCount() - 1, 0, 1);
                mExpandableItemManager.notifyChildItemRangeRemoved(groupPosition, childPosition, 1);
                mExpandableItemManager.notifyGroupItemChanged(getGroupCount() - 2);
            });

        }
        // set background resource (target view ID: container)
        int bgResId;
        bgResId = R.drawable.bg_item_normal_state;
        holder.mContainer.setBackgroundResource(bgResId);
        holder.mContainer.setOnClickListener(v -> {
            if(testWordListAdpaterLintener != null) {
                testWordListAdpaterLintener.onChildClicked(unit);
            }
        });
    }

    @Override
    public boolean onCheckCanExpandOrCollapseGroup(MyGroupViewHolder holder, int groupPosition, int x, int y, boolean expand) {
        // check the item is *not* pinned
        if (getGroupList(groupPosition) == null) {
            // return false to raise View.OnClickListener#onClick() event
            return false;
        }

        // check is enabled
        if (!(holder.itemView.isEnabled() && holder.itemView.isClickable())) {
            return false;
        }

        return true;
    }


    public List<AbstractWordUnit> getGroupList(int groupPosition) {
        if(groupPosition == 0)
            return null;
        else if(groupPosition <= bookEntities.size()) {
            BookEntity bookEntity = (BookEntity) bookEntities.toArray()[groupPosition - 1];
            return bookToUnitsMap.get(bookEntity);
        }
        else if(groupPosition == bookEntities.size() + 1) {
            return historyEntities;
        }
        else if(groupPosition == bookEntities.size() + 2)
            return null;
        else
            return testUnits;
    }

    public String getGroupName(int groupPosition) {
        String res = "";
        if(groupPosition == 0)
            res = "可测试内容";
        else if(groupPosition <= bookEntities.size()){
            BookEntity bookEntity = (BookEntity) bookEntities.toArray()[groupPosition - 1];
            res = bookEntity.getName();
        }
        else if(groupPosition == bookEntities.size() + 1) {
            res = "历史纪录-收藏列表";
        }
        else if(groupPosition == bookEntities.size() + 2) {
            CompileWordUnit compileTestUnit = CompileWordUnit.getInstance(testUnits);
            res = "待测试内容（已选词数：" + compileTestUnit.getTestUnitSize() + "）";
        }
        else {
            res = "待测内容列表";
        }
        return res;
    }

    public AbstractWordUnit getTestUnit() {
        if(testUnits.isEmpty())
            return null;
        else if(testUnits.size() == 1) {
            AbstractWordUnit res =  testUnits.get(0);
            res.setNewName(res.getTestUnitName());
            return res;
        }
        else {
            return CompileWordUnit.getInstance(testUnits);
        }
    }

    public void reload() {
        for(AbstractWordUnit testUnit : testUnits) {
            int groupPosition = (int) testUnit.getTag(KEY_GROUP_POSITION);
            int childPosition = (int) testUnit.getTag(KEY_CHILD_POSITION);
            List<AbstractWordUnit> groupList = getGroupList(groupPosition);
            groupList.add(childPosition, testUnit);
            testUnit.setNewName(testUnit.getTestUnitName());
            mExpandableItemManager.notifyChildItemRangeInserted(groupPosition, childPosition, 1);
        }
        int testUnitSize = testUnits.size();
        testUnits.clear();
        mExpandableItemManager.notifyChildItemRangeRemoved(getGroupCount() - 1, 0, testUnitSize);
        mExpandableItemManager.notifyGroupItemChanged(getGroupCount() - 2);
        mExpandableItemManager.collapseAll();
    }

    public interface TestWordListAdpaterLintener {
        void onChildClicked(AbstractWordUnit testUnit);
    }

    public TestWordListAdpaterLintener getTestWordListAdpaterLintener() {
        return testWordListAdpaterLintener;
    }

    public void setTestWordListAdpaterLintener(TestWordListAdpaterLintener testWordListAdpaterLintener) {
        this.testWordListAdpaterLintener = testWordListAdpaterLintener;
    }
}
