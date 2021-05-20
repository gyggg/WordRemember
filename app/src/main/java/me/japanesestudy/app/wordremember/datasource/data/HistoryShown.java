package me.japanesestudy.app.wordremember.datasource.data;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import me.japanesestudy.app.wordremember.component.adapter.HistoryListAdapter;
import me.japanesestudy.app.wordremember.datasource.entity.HistoryEntity;

/**
 * Created by guyu on 2018/1/21.
 */

public class HistoryShown implements MultiItemEntity {
    public String name;
    public String info;
    public HistoryEntity historyEntity;

    public HistoryShown(HistoryEntity historyEntity) {
        setHistoryEntity(historyEntity);
     }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public HistoryEntity getHistoryEntity() {
        return historyEntity;
    }

    public void setHistoryEntity(HistoryEntity historyEntity) {
        this.historyEntity = historyEntity;
        if(historyEntity.getHasCollected() && historyEntity.getCollectedName() != null)
            this.name = historyEntity.getCollectedName();
        else if(historyEntity.getParentHistoryId() == 0)
            this.name = " ● 首次记录" ;
        else
            this.name = " ● 后续";
        this.info = "正确数：" + (historyEntity.getSrcWordSize() - historyEntity.getWrongWordSize()) + " 错误数：" + historyEntity.getWrongWordSize() + " 正确率：" + historyEntity.getRightPercent() * 100 + "%";

    }

    @Override
    public int getItemType() {
        return HistoryListAdapter.TYPE_CHILD;
    }
}
