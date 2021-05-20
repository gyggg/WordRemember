package me.japanesestudy.app.wordremember.datasource.dao;

import org.xutils.ex.DbException;

import java.util.List;

import me.japanesestudy.app.wordremember.datasource.entity.UnitEntity;

/**
 * Created by guyu on 2018/1/9.
 */

public class UnitDAO extends BaseDAO<UnitEntity> {
    public List<UnitEntity> findByBookId(int bookId) throws DbException {
        return selector().where("book_id","=",bookId).findAll();
    }
}
