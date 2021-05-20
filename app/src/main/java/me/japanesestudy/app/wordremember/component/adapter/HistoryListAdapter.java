package me.japanesestudy.app.wordremember.component.adapter;

import android.util.Log;
import android.widget.CheckBox;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

import me.japanesestudy.app.wordremember.R;
import me.japanesestudy.app.wordremember.component.widget.ExpandableItemIndicator;
import me.japanesestudy.app.wordremember.datasource.data.HistoryGroup;
import me.japanesestudy.app.wordremember.datasource.data.HistoryHeader;
import me.japanesestudy.app.wordremember.datasource.data.HistoryShown;
import me.japanesestudy.app.wordremember.datasource.entity.HistoryEntity;

/**
 * Created by guyu on 2018/1/20.
 */

public class HistoryListAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    final public static int TYPE_HEADER = 0;
    final public static int TYPE_GROUP = 1;
    final public static int TYPE_CHILD = 2;


    private HistoryShownBtnClickListener historyShownBtnClickListener;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public HistoryListAdapter(List<MultiItemEntity> data) {
        super(data);
        addItemType(TYPE_HEADER, R.layout.item_history_header);
        addItemType(TYPE_GROUP, R.layout.item_history_group);
        addItemType(TYPE_CHILD, R.layout.item_history_child);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity item) {
        switch(item.getItemType()) {
            case TYPE_HEADER:
                onHeaderBind(helper, (HistoryHeader) item);
                break;
            case TYPE_GROUP:
                onGroupBind(helper, (HistoryGroup) item);
                break;
            case TYPE_CHILD:
                onChildBind(helper, (HistoryShown) item);
        }
    }

    private void onHeaderBind(BaseViewHolder holder, HistoryHeader historyHeader) {
        holder.setText(R.id.tv_header_title, historyHeader.getDateString());
    }

    private void onGroupBind(BaseViewHolder holder, HistoryGroup historyGroup) {
        holder.setText(R.id.tv_name, historyGroup.getName())
                .setText(R.id.tv_info, historyGroup.getInfo());
        // mark as clickable
        holder.itemView.setClickable(true);

        boolean isExpanded = historyGroup.isExpanded();
        int bgResId;
        if(isExpanded) {
            bgResId = R.drawable.bg_group_item_expanded_state;
        } else {
            bgResId = R.drawable.bg_group_item_normal_state;
        }
        holder.setBackgroundRes(R.id.container, bgResId);
        ((ExpandableItemIndicator)holder.getView(R.id.indicator)).setExpandedState(historyGroup.isExpanded(), false);
        holder.itemView.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            Log.d(TAG, "Level 1 item pos: " + pos + " isEnpanded:" + isExpanded);
            if(!historyGroup.isExpanded())
                expand(pos, true, true);
            else
                collapse(pos, true, true);
            ((ExpandableItemIndicator)holder.getView(R.id.indicator)).setExpandedState(historyGroup.isExpanded(), true);
        });
    }

    private void onChildBind(BaseViewHolder holder, HistoryShown historyShown) {
        holder.setText(R.id.tv_name, historyShown.getName())
                .setText(R.id.tv_info, historyShown.getInfo())
                .setOnClickListener(R.id.ckbox_collection, (buttonView) -> {
                    boolean isChecked = ((CheckBox)holder.getView(R.id.ckbox_collection)).isChecked();
                    if(historyShownBtnClickListener != null) {
                        historyShownBtnClickListener.onCollected(isChecked, historyShown, holder.getAdapterPosition());
                    }
                }).setOnClickListener(R.id.btn_delete_history, v -> {
                    if(historyShownBtnClickListener != null) {
                        historyShownBtnClickListener.onDeleted(historyShown, holder.getAdapterPosition());
                    }
                });
        holder.setChecked(R.id.ckbox_collection, historyShown.getHistoryEntity().getHasCollected());
        holder.setOnClickListener(R.id.content, v -> {
            if(historyShownBtnClickListener != null){
                historyShownBtnClickListener.onHistoryClicked(historyShown);
            }
        });
    }

    public interface HistoryShownBtnClickListener{
        void onCollected(boolean collected, HistoryShown historyShown, int itemPosition);
        void onDeleted(HistoryShown historyShown, int itemPosition);
        void onHistoryClicked(HistoryShown historyShown);
    }

    public HistoryShownBtnClickListener getHistoryShownBtnClickListener() {
        return historyShownBtnClickListener;
    }

    public void setHistoryShownBtnClickListener(HistoryShownBtnClickListener historyShownBtnClickListener) {
        this.historyShownBtnClickListener = historyShownBtnClickListener;
    }

    public void deleteHistory(HistoryShown target, int itemPosition) {
        int parentPosition = getParentPosition(target);
        if(target.getHistoryEntity().getParentHistoryId() != 0) {
            ((HistoryGroup) getData().get(parentPosition)).getSubItems().get(0).getHistoryEntity().getNextHistoryEntities().remove(target.getHistoryEntity());
        }
        ((HistoryGroup) getData().get(parentPosition)).removeSubItem(target);
        getData().remove(itemPosition);
        notifyItemRemoved(itemPosition);
        if(target.getHistoryEntity().getParentHistoryId() == 0) {
            getData().remove(parentPosition);
            notifyItemRemoved(itemPosition - 1);
            if(getData().get(parentPosition - 1) instanceof  HistoryHeader) {
                if(parentPosition > getData().size() || getData().get(parentPosition) instanceof HistoryHeader) {
                    getData().remove(parentPosition - 1);
                    notifyItemRemoved(itemPosition - 2);
                }
            }
        }
    }

    public void collectedHistory(HistoryShown target, HistoryEntity data, int itemPosition) {
        target.setHistoryEntity(data);
        notifyItemChanged(itemPosition);
    }

    public void refresh(List<MultiItemEntity> data) {
        setNewData(data);
    }
}
