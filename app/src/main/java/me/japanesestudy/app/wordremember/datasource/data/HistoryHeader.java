package me.japanesestudy.app.wordremember.datasource.data;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by guyu on 2018/1/20.
 */

public class HistoryHeader implements MultiItemEntity {
    private String dateString;

    public HistoryHeader(String dateString) {

        this.dateString = dateString;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    @Override
    public int getItemType() {
        return 0;
    }
}
