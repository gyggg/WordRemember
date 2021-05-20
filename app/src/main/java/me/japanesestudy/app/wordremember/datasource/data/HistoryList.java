package me.japanesestudy.app.wordremember.datasource.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import me.japanesestudy.app.wordremember.datasource.entity.HistoryEntity;

/**
 * Created by guyu on 2018/1/16.
 */

public class HistoryList extends ArrayList<HistoryEntity>  implements Serializable {
    public static final String SPLIT_CHAR = "@";
    public static HistoryList getInstance(List<HistoryEntity> historyEntities) {
        HistoryList res = new HistoryList();
        res.addAll(historyEntities);
        return res;
    }
    @Override
    public String toString() {
        String res = null;
        for(HistoryEntity historyEntity : this) {
            if(res == null) {
                res = "";
            }
            else {
                res = res + SPLIT_CHAR;
            }
            res = res + historyEntity.getId();
        }
        return res;
    }
}
