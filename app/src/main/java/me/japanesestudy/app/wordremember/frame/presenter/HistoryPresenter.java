package me.japanesestudy.app.wordremember.frame.presenter;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.japanesestudy.app.wordremember.datasource.data.HistoryGroup;
import me.japanesestudy.app.wordremember.datasource.data.HistoryHeader;
import me.japanesestudy.app.wordremember.datasource.data.HistoryShown;
import me.japanesestudy.app.wordremember.datasource.entity.HistoryEntity;
import me.japanesestudy.app.wordremember.frame.model.HistoryModel;
import me.japanesestudy.app.wordremember.frame.model.WordModel;
import me.japanesestudy.app.wordremember.frame.view.IHistoryView;
import me.japanesestudy.app.wordremember.tools.DateTool;

/**
 * Created by guyu on 2018/1/21.
 */

public class HistoryPresenter extends BasePresenter<IHistoryView> {
    HistoryModel historyModel = HistoryModel.getInstance();

    public HistoryPresenter(IHistoryView view) {
        super(view);
    }

    public void initListAdapter() {
        List<HistoryEntity> parentEntities = historyModel.getAllHistoryWithFold();
        Collections.reverse(parentEntities);
        List<MultiItemEntity> res = new ArrayList<>();
        List<MultiItemEntity> historyGroups = new ArrayList<>();
        Set<String> timeSet = new HashSet<>();
        for(HistoryEntity parent : parentEntities) {
            String info = "源词数：" + parent.getSrcWordSize() + " 记录条数：" + (parent.getNextHistoryEntities().size() + 1);
            //初始化history gruop
            HistoryGroup historyGroup = new HistoryGroup(parent.getName(), info);
            historyGroups.add(historyGroup);
            historyGroup.addSubItem(new HistoryShown(parent));
            for(HistoryEntity nextEntity : parent.getNextHistoryEntities()) {
                historyGroup.addSubItem(new HistoryShown(nextEntity));
            }
            String timeString = DateTool.getDateString(parent.getTime());
            boolean isNew = timeSet.add(timeString);
            if(isNew) {
                res.add(new HistoryHeader(timeString));
            }
            res.add(historyGroup);
        }
        view.initListAdapter(res);
    }

    public void collectHistory(HistoryShown historyShown, String collectedName, int itemPosition){
        HistoryEntity historyEntity = historyShown.getHistoryEntity();
        historyEntity.setCollectedName(collectedName);
        historyEntity.setHasCollected(true);
        historyEntity = historyModel.saveHistory(historyEntity);
        view.onHistoryCollected(historyShown, historyEntity, itemPosition);
    }

    public void uncollectHistory(HistoryShown historyShown, int itemPosition) {
        HistoryEntity historyEntity = historyShown.getHistoryEntity();
        historyEntity.setHasCollected(false);
        historyEntity = historyModel.saveHistory(historyEntity);
        view.onHistoryCollected(historyShown, historyEntity, itemPosition);
    }

    public void deleteHistory(HistoryShown historyShown, int itemPosition) {
        HistoryEntity historyEntity = historyShown.getHistoryEntity();
        historyModel.deleteHistory(historyEntity);
        view.onHistoryDeleted(historyShown, itemPosition);
    }

    public void showHistory(HistoryShown historyShown) {
        HistoryEntity historyEntity = historyShown.getHistoryEntity();
        view.onHistoryClicked(historyEntity);
    }
}
