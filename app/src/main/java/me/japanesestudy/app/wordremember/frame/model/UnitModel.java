package me.japanesestudy.app.wordremember.frame.model;

import org.xutils.ex.DbException;

import java.util.List;

import me.japanesestudy.app.wordremember.datasource.dao.BookDAO;
import me.japanesestudy.app.wordremember.datasource.dao.UnitDAO;
import me.japanesestudy.app.wordremember.datasource.dao.WordDAO;
import me.japanesestudy.app.wordremember.datasource.entity.UnitEntity;
import me.japanesestudy.app.wordremember.datasource.entity.WordEntity;

/**
 * Created by guyu on 2018/1/10.
 */

public class UnitModel extends BaseModel {
    private UnitDAO unitDAO = new UnitDAO();
    private WordDAO wordDAO = new WordDAO();
    private BookDAO bookDAO = new BookDAO();
    private static UnitModel instance = new UnitModel();

    private UnitModel(){}

    static public UnitModel getInstance() {
        return instance;
    }

    public List<UnitEntity> getAllUnitEntityWithWord(int bookId, boolean isCom) {
        clean();
        List<UnitEntity> unitEntities;
        try {
            if (bookId < 1) {
                unitEntities = unitDAO.findAll();
            } else {
                unitEntities = unitDAO.findByBookId(bookId);
            }
            if (isCom) {
                UnitEntity nullObj = new UnitEntity();
                for (int i = 0; i < unitEntities.size(); i++) {
                    UnitEntity unitEntity = unitEntities.get(i);
                    List<WordEntity> wordEntities = wordDAO.findByUnitId(unitEntity.getId());
                    if (i % 2 != 0) {
                        unitEntities.set(i, nullObj);
                        unitEntities.get(i - 1).addAll(wordEntities);
                    } else {
                        unitEntity.setWordEntities(wordEntities);
                        unitEntity.setName(unitEntity.getName().replaceAll("-词汇.*", ""));
                    }
                }
                while (unitEntities.remove(nullObj)) ;
            } else {
                for (UnitEntity unitEntity : unitEntities) {
                    List<WordEntity> wordEntities = wordDAO.findByUnitId(unitEntity.getId());
                    unitEntity.setWordEntities(wordEntities);
                    if (unitEntity.getWrodString() == null) {
                        unitEntity.setWrodString(unitEntity.toString());
                        unitDAO.save(unitEntity);
                    }
                }
            }
            cache = unitEntities;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cache;
    }

    public List<UnitEntity> getAllUnitEntityWithoutWord(int bookId, boolean isCom) {
        clean();
        List<UnitEntity> unitEntities;
        try {
            if (bookId < 1) {
                unitEntities = unitDAO.findAll();
            } else {
                unitEntities = unitDAO.findByBookId(bookId);
            }
            if (isCom) {
                final UnitEntity NULL_OBJ = new UnitEntity();
                for (int i = 0; i < unitEntities.size(); i++) {
                    UnitEntity unitEntity = unitEntities.get(i);
                    if (i % 2 != 0) {
                        unitEntities.set(i, NULL_OBJ);
                    } else {
                        unitEntity.setName(unitEntity.getName().replaceAll("-词汇.*", ""));
                    }
                }
                while (unitEntities.remove(NULL_OBJ)) ;
            } else {
                for (UnitEntity unitEntity : unitEntities) {
                    List<WordEntity> wordEntities = null;
                    unitEntity.setWordEntities(wordEntities);
                    if (unitEntity.getWrodString() == null) {
                        unitEntity.setWrodString(unitEntity.toString());
                        unitDAO.save(unitEntity);
                    }
                }
            }
            cache = unitEntities;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cache;
    }
}
