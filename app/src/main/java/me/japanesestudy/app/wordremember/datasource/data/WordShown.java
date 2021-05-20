package me.japanesestudy.app.wordremember.datasource.data;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import me.japanesestudy.app.wordremember.datasource.entity.WordEntity;

/**
 * Created by guyu on 2018/1/22.
 */

public class WordShown implements MultiItemEntity {

    private String japanese = "";
    private String info = "";
    private WordEntity wordEntity;

    public WordShown(WordEntity wordEntity) {
        this.setWordEntity(wordEntity);
    }

    public String getJapanese() {
        return japanese;
    }

    public void setJapanese(String japanese) {
        this.japanese = japanese;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public WordEntity getWordEntity() {
        return wordEntity;
    }

    public void setWordEntity(WordEntity wordEntity) {
        this.wordEntity = wordEntity;
        if(wordEntity == null)
            return;
        if(wordEntity.getJapaneseKanji() != null && !wordEntity.getJapaneseKanji().equals("")) {
            this.japanese = wordEntity.getJapaneseKanji() + "（" + wordEntity.getJapaneseAlias() + "）";
        } else {
            this.japanese = wordEntity.getJapaneseAlias();
        }
        this.info = "【" + WordEntity.WordType.getTypeString(wordEntity.getType()) + "】" + wordEntity.getChinese();
    }

    @Override
    public int getItemType() {
        return 0;
    }
}
