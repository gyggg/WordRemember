package me.japanesestudy.app.wordremember.datasource.entity;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.util.ArrayList;
import java.util.List;

import me.japanesestudy.app.wordremember.datasource.data.AbstractWordUnit;
import me.japanesestudy.app.wordremember.datasource.data.WordList;

/**
 * Created by guyu on 2018/1/9.
 */
@Table(name = "unit")
public class UnitEntity extends AbstractWordUnit implements IBaseEntity {

    @Column(name = "id",isId = true,autoGen = true,property = "NOT NULL")
    private int id;
    @Column(name = "book_id")
    private int bookId;
    @Column(name = "name")
    private String name;
    @Column(name="word_string")
    private String wrodString;
    private WordList wordEntities;
    private String bookName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWrodString() {
        return wrodString;
    }

    public void setWrodString(String wrodString) {
        this.wrodString = wrodString;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public int size() {
        if(getWordEntities() != null)
            return getWordEntities().size();
        else if(toString() != null && !toString().isEmpty()) {
            return getWordIds().size();
        } else
            return 0;
    }

    public WordList getWordEntities() {
        return wordEntities;
    }

    public void setWordEntities(WordList wordEntities) {
        this.wordEntities = wordEntities;
    }

    public void setWordEntities(List<WordEntity> wordEntities) {
        this.wordEntities = WordList.getInstance(wordEntities);
    }

    public void addAll(List<WordEntity> wordEntities) {
        wordEntities.addAll(wordEntities);
    }

    @Override
    public String toString() {
        if(wordEntities != null)
            return wordEntities.toString();
        else if(wrodString != null)
            return wrodString;
        else
            return "";
    }

    public List<Integer> getWordIds() {
        List<Integer> ids = new ArrayList<>();
        String []temp = toString().split(WordList.SPLIT_CHAR);
        for(String str : temp) {
            ids.add(Integer.getInteger(str));
        }
        return ids;
    }

    @Override
    public Object getKey() {
        return id;
    }

    @Override
    public String getTestUnitName() {
        return name;
    }

    @Override
    public int getTestUnitSize() {
        return size();
    }

    @Override
    public WordList getTestUnitWord() {
        return getWordEntities();
    }

    @Override
    public String getTestUnitWordString() {
        return getWrodString();
    }

    @Override
    public int getParentUnitId() {
        return 0;
    }

}
