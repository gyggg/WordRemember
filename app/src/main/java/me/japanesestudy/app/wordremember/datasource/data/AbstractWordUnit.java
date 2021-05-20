package me.japanesestudy.app.wordremember.datasource.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;

import me.japanesestudy.app.wordremember.datasource.entity.HistoryEntity;
import me.japanesestudy.app.wordremember.datasource.entity.WordEntity;

/**
 * Created by guyu on 2018/1/16.
 */

public abstract class AbstractWordUnit implements Serializable{
    final public static String TAG_KEY_DEFAULT = "@default_tag_key";
    final public static String TAG_KEY_TYPE = "@type_tag_key";
    final public static String TAG_KEY_NEW_NAME = "@new_name_tag_key";
    final public static int TEST_MODE_J2C = 0;
    final public static int TEST_MODE_C2J = 1;
    abstract public String getTestUnitName();
    abstract public int getTestUnitSize();
    abstract public WordList getTestUnitWord();
    abstract public String getTestUnitWordString();
    abstract public int getParentUnitId();

    private WordList mRightWords = new WordList();
    private WordList mWrongWords = new WordList();
    private WordList mSkipWords = new WordList();
    private String newName;
    private HashMap<String, Object> tags = new HashMap<>();



    public AbstractWordUnit() {
        setTag(TAG_KEY_TYPE, this.getClass());
    }

    public WordList getmRightWords() {
        return mRightWords;
    }

    public void setmRightWords(WordList mRightWords) {
        this.mRightWords = mRightWords;
    }

    public WordList getmWrongWords() {
        return mWrongWords;
    }

    public void setmWrongWords(WordList mWrongWords) {
        this.mWrongWords = mWrongWords;
    }

    public WordList getmSkipWords() {
        return mSkipWords;
    }

    public void setmSkipWords(WordList mSkipWords) {
        this.mSkipWords = mSkipWords;
    }

    public String getNewName() {
        if(newName == null)
            if(getTag(TAG_KEY_NEW_NAME) != null) {
                return (String)getTag(TAG_KEY_NEW_NAME);
            }
            else
                return getTestUnitName();
        else
            return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
        setTag(TAG_KEY_NEW_NAME, newName);
    }

    public Object getTag() {
        return getTag(TAG_KEY_DEFAULT);
    }

    public void setTag(Object tag) {
        setTag(TAG_KEY_DEFAULT, tag);
    }

    public Object getTag(String key) {
        return tags.get(key);
    }

    public void setTag(String key, Object tag) {
        tags.put(key, tag);
    }

    public Set<String> getKeyset() {
        return tags.keySet();
    }

    public HistoryEntity newHistory() {
        HistoryEntity historyEntity = new HistoryEntity();
        historyEntity.setName(getNewName());
        historyEntity.setWrongWords(mWrongWords.toString());
        historyEntity.setSrcWords(getTestUnitWord().toString());
        historyEntity.setTime(System.currentTimeMillis());
        if(this instanceof HistoryEntity) {
            if(this.getParentUnitId() == 0) {
                HistoryEntity temp = (HistoryEntity)this;
                historyEntity.setParentHistoryId(temp.getId());
            }
            else {
                historyEntity.setParentHistoryId(this.getParentUnitId());
            }
        }
        else
            historyEntity.setParentHistoryId(0);
        return historyEntity;
    }

    public boolean judge(String key, int position, int testMode) {
        boolean res = false;
        WordEntity wordEntity = getTestUnitWord().get(position);
        if(key == null || key.equals(""))
            res = false;
        else if(testMode == TEST_MODE_J2C) {
            if(wordEntity.getChinese().indexOf(key) >= 0)
                res = true;
        } else {
            if(wordEntity.getJapaneseKanji() != null &&wordEntity.getJapaneseKanji().indexOf(key) >= 0)
                res = true;
            else if(wordEntity.getJapaneseAlias().indexOf(key) >= 0)
                res = true;
        }
        if(res)
            mRightWords.add(wordEntity);
        else
            mWrongWords.add(wordEntity);
        return res;
    }

    public void skipWord(int position) {
        mSkipWords.add(getTestUnitWord().get(position));
    }

    public int getHasTestedWordSize() {
        return mRightWords.size() + mSkipWords.size() + mWrongWords.size();
    }
}
