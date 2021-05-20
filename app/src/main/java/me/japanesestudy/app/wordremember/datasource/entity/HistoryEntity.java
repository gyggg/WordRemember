package me.japanesestudy.app.wordremember.datasource.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.japanesestudy.app.wordremember.component.adapter.HistoryListAdapter;
import me.japanesestudy.app.wordremember.datasource.data.AbstractWordUnit;
import me.japanesestudy.app.wordremember.datasource.data.HistoryList;
import me.japanesestudy.app.wordremember.datasource.data.WordList;
import me.japanesestudy.app.wordremember.tools.NumberTool;

/**
 * Created by guyu on 2018/1/9.
 */

@Table(name = "history")
public class HistoryEntity extends AbstractWordUnit implements IBaseEntity, MultiItemEntity{

    @Column(name = "id",isId = true,autoGen = true,property = "NOT NULL")
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "src_words")
    private String srcWords;
    @Column(name = "wrong_words")
    private String wrongWords;
    @Column(name = "time")
    private Long time;
    @Column(name = "parent_history_id")
    private Integer parentHistoryId = 0;
    @Column(name = "has_collected")
    private Boolean hasCollected = false;
    @Column(name = "collected_name")
    private String collectedName;

    private HistoryList nextHistoryEntities;
    private WordList srcWordList;
    private WordList wrongWordList;


    @Override
    public String getTestUnitName() {
        return name;
    }

    @Override
    public int getTestUnitSize() {
        return getWrongWordSize();
    }

    @Override
    public WordList getTestUnitWord() {
        return getWrongWordList();
    }

    @Override
    public String getTestUnitWordString() {
        return wrongWords;
    }

    @Override
    public int getParentUnitId() {
        if(parentHistoryId == 0)
            return id;
        else
            return parentHistoryId;
    }

    public String getHistoryName() {
        if(hasCollected && getCollectedName() != null)
            return getCollectedName();
        else if(getParentUnitId() == 0)
            return "首次记录：" + getNewName();
        else
            return "后续记录：" + getNewName();
    }

    public HistoryEntity() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSrcWords() {
        return srcWords;
    }

    public void setSrcWords(String srcWords) {
        this.srcWords = srcWords;
    }

    public String getWrongWords() {
        return wrongWords;
    }

    public void setWrongWords(String wrongWords) {
        this.wrongWords = wrongWords;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Date getDate() {
        return new Date(time);
    }

    public WordList getSrcWordList() {
        return srcWordList;
    }

    public void setSrcWordList(WordList srcWordList) {
        this.srcWordList = srcWordList;
    }

    public WordList getWrongWordList() {
        return wrongWordList;
    }

    public void setWrongWordList(WordList wrongWordList) {
        this.wrongWordList = wrongWordList;
    }

    public HistoryList getNextHistoryEntities() {
        return nextHistoryEntities;
    }

    public void setNextHistoryEntities(HistoryList nextHistoryEntities) {
        this.nextHistoryEntities = nextHistoryEntities;
    }

    public void setNextHistoryEntities(List<HistoryEntity> historyEntities) {
        this.nextHistoryEntities = HistoryList.getInstance(historyEntities);
    }

    public Integer getParentHistoryId() {
        return parentHistoryId;
    }

    public void setParentHistoryId(Integer parentHistoryId) {
        this.parentHistoryId = parentHistoryId;
    }

    public Boolean getHasCollected() {
        return hasCollected;
    }

    public void setHasCollected(Boolean hasCollected) {
        this.hasCollected = hasCollected;
    }

    public String getCollectedName() {
        return collectedName;
    }

    public void setCollectedName(String collectedName) {
        this.collectedName = collectedName;
    }

    public int getWrongWordSize() {
        if(wrongWordList != null)
            return wrongWordList.size();
        else if(wrongWords != null)
            return getWrongWordIds().size();
        else
            return 0;
    }

    public int getSrcWordSize() {
        if(srcWordList != null)
            return srcWordList.size();
        else if(srcWords != null)
            return getSrcWordIds().size();
        else
            return 0;
    }

    public List<Integer> getWrongWordIds() {
        List<Integer> ids = new ArrayList<>();
        if(wrongWords != null) {
            String[] temp = wrongWords.split(WordList.SPLIT_CHAR);
            for (String str : temp) {
                ids.add(Integer.getInteger(str));
            }
        }
        return ids;
    }

    public List<Integer> getSrcWordIds() {
        List<Integer> ids = new ArrayList<>();
        if(srcWords != null) {
            String[] temp = srcWords.split(WordList.SPLIT_CHAR);
            for (String str : temp) {
                ids.add(Integer.getInteger(str));
            }
        }
        return ids;
    }

    @Override
    public String getNewName() {
        if(hasCollected && collectedName != null)
            return collectedName;
        else
            return super.getNewName();
    }

    @Override
    public Object getKey() {
        return getId();
    }

    @Override
    public int getItemType() {
        return HistoryListAdapter.TYPE_CHILD;
    }

    public double getRightPercent() {
        double per = 1.0;
        try {
            per = 1 - (getWrongWordSize() * 1.0) / (getSrcWordSize() * 1.0);
        }catch (Exception e) {
            per = 1.0;
        }
        try {
            return NumberTool.saveNumberPoint(per, 2);
        } catch (Exception e) {
            return NumberTool.saveNumberPoint(1.0, 2);
        }
    }
}
