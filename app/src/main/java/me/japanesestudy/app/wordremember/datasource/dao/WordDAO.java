package me.japanesestudy.app.wordremember.datasource.dao;

import android.database.Cursor;

import org.xutils.ex.DbException;

import java.util.List;

import me.japanesestudy.app.wordremember.datasource.entity.WordEntity;

/**
 * Created by guyu on 2018/1/9.
 */

public class WordDAO extends BaseDAO<WordEntity> {
    public List<WordEntity> findByUnitId(int unitId) throws DbException {
        List result =  selector().where("unit_id", "=", unitId).findAll();
        return result;
    }
    public List<WordEntity> findByWordType(int wordType) throws DbException {
        return selector().where("type", "=", wordType).findAll();
    }
    public List<WordEntity> findByIdScope(int start, int end) throws DbException{
        return selector().where("id",">", start).and("id", "<=", end).findAll();
    }

    public long linesByWordType(int wordType) throws DbException {
        return selector().where("type","=",wordType).count();
    }

    public long linesByUnitId(int unitId) throws DbException {
        return selector().where("unit_id","=",unitId).count();
    }
}
