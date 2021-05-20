package me.japanesestudy.app.wordremember.datasource.data;

/**
 * Created by guyu on 2018/1/19.
 */

public class WordUnit extends AbstractWordUnit {
    protected String info;
    protected int srcSize;

    protected String testUnitName;
    protected WordList testUnitWord;
    protected int parentId;
    protected String testUntiWordString;

    public void setTestUntiWordString(String testUntiWordString) {
        this.testUntiWordString = testUntiWordString;
    }

    public void setTestUnitName(String testUnitName) {
        this.testUnitName = testUnitName;
    }

    public void setTestUnitWord(WordList testUnitWord) {
        this.testUnitWord = testUnitWord;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getSrcSize() {
        if(srcSize <= getTestUnitSize())
            return getTestUnitSize();
        return srcSize;
    }

    public void setSrcSize(int srcSize) {
        this.srcSize = srcSize;
    }

    public int getFinishedSize() {
        return getSrcSize() - getTestUnitSize();
    }

    public float getFinishedPercent() {
        return getFinishedSize() * 1.0f / getSrcSize() * 1.0f;
    }

    @Override
    public String getTestUnitName() {
        return testUnitName;
    }

    @Override
    public int getTestUnitSize() {
        if(testUnitWord == null)
            return 0;
        return testUnitWord.size();
    }

    @Override
    public WordList getTestUnitWord() {
        return testUnitWord;
    }

    @Override
    public String getTestUnitWordString() {
        return testUntiWordString;
    }

    @Override
    public int getParentUnitId() {
        return 0;
    }

}
