package me.japanesestudy.app.wordremember.frame.model;

import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.japanesestudy.app.wordremember.datasource.dao.HistoryDAO;
import me.japanesestudy.app.wordremember.datasource.entity.HistoryEntity;
import me.japanesestudy.app.wordremember.tools.DateTool;

/**
 * Created by guyu on 2018/1/19.
 */

public class HistoryModel extends BaseModel<HistoryEntity> {
    private static HistoryModel instance = new HistoryModel();

    public static HistoryModel getInstance() {
        return instance;
    }

    private HistoryDAO historyDAO = new HistoryDAO();

    public HistoryEntity saveHistory(HistoryEntity historyEntity) {
        try {
            return historyDAO.save(historyEntity);
        } catch (Exception e) {
            return null;
        }
    }

    public void deleteHistory(HistoryEntity historyEntity) {
        try {
            historyDAO.deleteById(historyEntity.getId());
        } catch (Exception e) {
            cache = new ArrayList();
        }
    }

    public List<HistoryEntity> getAllHistory() {
        clean();
        try {
            cache = historyDAO.findAll();
        } catch (Exception e) {
            cache = new ArrayList();
        }
        return cache;
    }

    public List<HistoryEntity> getAllCollectedHistory() {
        clean();
        try {
            cache = historyDAO.findByHasCollected(true);
        } catch (Exception e) {
            cache = new ArrayList();
        }
        return cache;
    }

    public List<HistoryEntity> getAllHistoryWithFold() {
        clean();
        try {
            cache = historyDAO.findParentHistory();
            for(HistoryEntity historyEntity : cache) {
                historyEntity.setNextHistoryEntities(historyDAO.findByParentHistoryId(historyEntity.getId()));
            }
        } catch (Exception e) {
            cache = new ArrayList();
        }
        return cache;
    }

    public HistoryEntity getHistoryByIdWithFold(int historyId) {
        HistoryEntity historyEntity = null;
        try {
            historyEntity = historyDAO.findById(historyId);
            historyEntity.setNextHistoryEntities(historyDAO.findByParentHistoryId(historyId));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return historyEntity;
    }

    public List<HistoryEntity> getHistoryLink(int historyId) {
        List<HistoryEntity> result = new ArrayList<>();
        try {
            result.add(historyDAO.findById(historyId));
            result.addAll(historyDAO.findByParentHistoryId(historyId));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<HistoryEntity> getAllHistoryWithNearDay(int day) {
        List<HistoryEntity> historyEntities = getAllHistoryWithFold();
        Collections.reverse(historyEntities);
        List<HistoryEntity> result = new ArrayList<>();
        Set<String> timeSet = new HashSet<>();
        int hasDay = 0;
        for(int i = 0; i < historyEntities.size(); i++) {
            HistoryEntity historyEntity = historyEntities.get(i);
            if(DateTool.getDateString(historyEntity.getTime()).equals(DateTool.getDateString(System.currentTimeMillis())))
                continue;
            String timeString = DateTool.getDateString(historyEntity.getTime());
            boolean isNew = timeSet.add(timeString);
            if(isNew) {
                hasDay++;
            }
            if(hasDay > day)
                break;
            result.add(historyEntity);
        }
        return result;
    }

    public List<HistoryEntity> getHistoryLink(HistoryEntity historyEntity) {
        List<HistoryEntity> result;
        if(historyEntity.getNextHistoryEntities() == null || historyEntity.getNextHistoryEntities().isEmpty()) {
            result = getHistoryLink(historyEntity.getId());
        } else {
            result = historyEntity.getNextHistoryEntities();
            result.add(0, historyEntity);
        }
        return result;
    }

}
