package me.japanesestudy.app.wordremember.datasource.data;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import me.japanesestudy.app.wordremember.component.adapter.HistoryListAdapter;

/**
 * Created by guyu on 2018/1/20.
 */

public class HistoryGroup extends AbstractExpandableItem<HistoryShown> implements MultiItemEntity {
    private String name;
    private String info;

    public HistoryGroup(String name, String info) {
        this.name = name;
        this.info = info;
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

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public int getItemType() {
        return HistoryListAdapter.TYPE_GROUP;
    }
}
