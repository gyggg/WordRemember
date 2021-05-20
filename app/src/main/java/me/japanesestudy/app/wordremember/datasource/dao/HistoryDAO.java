package me.japanesestudy.app.wordremember.datasource.dao;

import org.xutils.ex.DbException;

import java.util.List;

import me.japanesestudy.app.wordremember.datasource.entity.HistoryEntity;

/**
 * Created by guyu on 2018/1/9.
 */

public class HistoryDAO extends BaseDAO<HistoryEntity> {
    public List<HistoryEntity> findByHasCollected(boolean isCollected) throws DbException {
        int intIsCollected = isCollected ? 1 : 0;
        return selector().where("has_collected", "=", intIsCollected).findAll();
    }
    public List<HistoryEntity> findParentHistory() throws DbException {
        return selector().where("parent_history_id", "=", 0).findAll();
    }
    public List<HistoryEntity> findByParentHistoryId(int parentHistoryId) throws DbException {
        return selector().where("parent_history_id", "=", parentHistoryId).findAll();
    }
}
