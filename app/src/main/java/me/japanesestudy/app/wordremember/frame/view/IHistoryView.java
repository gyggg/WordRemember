package me.japanesestudy.app.wordremember.frame.view;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

import me.japanesestudy.app.wordremember.datasource.data.HistoryShown;
import me.japanesestudy.app.wordremember.datasource.entity.HistoryEntity;

/**
 * Created by guyu on 2018/1/20.
 */

public interface IHistoryView {
    void initListAdapter(List<MultiItemEntity> multiItemEntities);
    void onHistoryCollected(HistoryShown historyShown, HistoryEntity data, int itemPosition);
    void onHistoryDeleted(HistoryShown historyShown, int itemPosition);
    void onHistoryClicked(HistoryEntity historyEntity);
}
