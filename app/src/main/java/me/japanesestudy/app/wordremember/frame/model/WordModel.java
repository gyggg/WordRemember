package me.japanesestudy.app.wordremember.frame.model;

import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

import me.japanesestudy.app.wordremember.datasource.dao.WordDAO;
import me.japanesestudy.app.wordremember.datasource.data.AbstractWordUnit;
import me.japanesestudy.app.wordremember.datasource.entity.HistoryEntity;
import me.japanesestudy.app.wordremember.datasource.data.WordUnit;
import me.japanesestudy.app.wordremember.datasource.entity.UnitEntity;
import me.japanesestudy.app.wordremember.datasource.entity.WordEntity;
import me.japanesestudy.app.wordremember.datasource.data.WordList;

/**
 * Created by guyu on 2018/1/19.
 */

public class WordModel extends BaseModel {
    private static WordModel instance = new WordModel();

    public static WordModel getInstance() {
        return instance;
    }

    private WordDAO wordDAO = new WordDAO();
    public AbstractWordUnit injectTestUnitWords(AbstractWordUnit testUnit) {
        if(testUnit == null)
            return null;
        if(testUnit.getTestUnitWord() != null && !testUnit.getTestUnitWord().isEmpty())
            return testUnit;
        if(testUnit instanceof HistoryEntity) {
            HistoryEntity temp = (HistoryEntity)testUnit;
            temp.setWrongWordList(getWordEntitiesByIdString(temp.getWrongWords()));
            temp.setSrcWordList(getWordEntitiesByIdString(temp.getSrcWords()));
        } else if(testUnit instanceof UnitEntity) {
            UnitEntity temp = (UnitEntity)testUnit;
            temp.setWordEntities(getWordEntitiesByIdString(temp.getWrodString()));
        } else if(testUnit instanceof WordUnit) {
            WordUnit temp = (WordUnit)testUnit;
            temp.setTestUnitWord(getWordEntitiesByIdString(temp.getTestUnitWordString()));
        }
        return testUnit;
    }
    public void injectTestUnitWords(List<AbstractWordUnit> testUnits) {
        for(AbstractWordUnit testUnit : testUnits) {
            injectTestUnitWords(testUnit);
        }
    }
    public WordList getWordEntitiesByIdString(String idString) {
        if(idString == null)
            return new WordList();
        String[] ids = idString.split(WordList.SPLIT_CHAR);
        WordList wordEntities = new WordList();
        for(String id: ids) {
            try {
                WordEntity wordEntity = wordDAO.findById(Integer.parseInt(id));
                wordEntities.add(wordEntity);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return wordEntities;
    }
    public WordEntity saveWordEntity(WordEntity wordEntity) {
        try {
            wordEntity = wordDAO.save(wordEntity);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return wordEntity;
    }
    public List<WordEntity> getWordEntitiesByWordType(List<Integer> wordTypes) {
        List<WordEntity> result = new ArrayList<>();
        for(int type:wordTypes) {
            try {
                result.addAll(wordDAO.findByWordType(type));
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public List<WordEntity> getAllWords() {
        List<WordEntity> result = new ArrayList<>();
        try {
            result = wordDAO.findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<WordEntity> getWordsWithIdScope(int start, int end) {
        List<WordEntity> result = new ArrayList<>();
        try {
            result = wordDAO.findByIdScope(start, end);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return result;
    }

    public int getWordSum() {
        try {
            return (int) wordDAO.lines();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getWordSumByType(int type) {
        try {
            return (int) wordDAO.linesByWordType(type);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getWordSumByUnitId(int unitId) {
        try {
            return (int) wordDAO.linesByUnitId(unitId);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
