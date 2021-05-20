package me.japanesestudy.app.wordremember.datasource.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import me.japanesestudy.app.wordremember.datasource.entity.WordEntity;

/**
 * Created by guyu on 2018/1/9.
 */

public class WordList extends ArrayList<WordEntity> implements Serializable {
    public static final String SPLIT_CHAR = "@";
    public static WordList getInstance(List<WordEntity> wordEntities) {
        WordList res = new WordList();
        res.addAll(wordEntities);
        return res;
    }
    @Override
    public String toString() {
        String res = null;
        for(WordEntity wordEntity : this) {
            if(res == null) {
                res = "";
            }
            else {
                res = res + SPLIT_CHAR;
            }
            res = res + wordEntity.getId();
        }
        return res;
    }

    public WordList random() {
        Collections.shuffle(this);
        return this;
    }

    public void clearSameWord() {
        HashSet<WordEntity> temp = new HashSet<>(this);
        this.clear();
        this.addAll(temp);
    }
}
