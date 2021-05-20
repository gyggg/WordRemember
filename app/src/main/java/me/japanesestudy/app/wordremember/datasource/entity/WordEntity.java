package me.japanesestudy.app.wordremember.datasource.entity;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guyu on 2018/1/9.
 */
@Table(name = "word")
public class WordEntity implements IBaseEntity {

    public static class WordType {
        //名词
        final public static int N = 0;
        //一类动词
        final public static int V1 = 1;
        //二类动词
        final public static int V2 = 2;
        //三类动词
        final public static int V3 = 3;
        //形容词
        final public static int ADJ = 4;
        //形容动词
        final public static int ADJ_V = 5;
        //副词
        final public static int ADV = 6;
        //连体词
        final public static int CONJ = 7;
        //惯用语
        final public static int PHRASE = 8;
        //接续词
        final public static int CONN = 9;
        //寒暄语
        final public static int AISATSU = 10;
        //专用语
        final public static int EXPRESSION = 11;
        //代词
        final public static int PRON = 12;
        //感叹词
        final public static int INTERJECTION = 13;
        //词组
        final public static int WORD_GROUP = 14;

        public static String getTypeString(int type) {
            switch(type) {
                case N:return "名词";
                case V1:return "一类动词";
                case V2:return "二类动词";
                case V3:return "三类动词";
                case ADJ:return "形容词";
                case ADJ_V:return "形容动词";
                case ADV:return "副词";
                case CONJ:return "连体词";
                case PHRASE:return "惯用语";
                case CONN:return "接续词";
                case AISATSU:return "寒暄语";
                case EXPRESSION:return "专用语";
                case PRON:return "代词";
                case INTERJECTION:return "感叹词";
                case WORD_GROUP:return "词组";
                default:return "-";
            }
        }

        static public List<String> getTypeStrings() {
            List<String> result = new ArrayList<>();
            for(int i = 0; i < 15; i++)
                result.add(getTypeString(i));
            return result;
        }

        static public List<Integer> getTypes() {
            List<Integer> result = new ArrayList<>();
            for(int i = 0; i < 15; i++)
                result.add(i);
            return result;
        }
    }

    @Column(name = "id",isId = true,autoGen = true,property = "NOT NULL")
    private Integer id;
    @Column(name = "japanese_kanji")
    private String japaneseKanji;
    @Column(name = "japanese_alias")
    private String japaneseAlias;
    @Column(name = "chinese")
    private String chinese;
    @Column(name = "type")
    private Integer type;
    @Column(name = "tone")
    private String tone;
    @Column(name = "hint")
    private String hint;
    @Column(name = "unit_id")
    private int unitId;
    @Column(name = "has_collected")
    private boolean hasCollected = false;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getJapaneseKanji() {
        return japaneseKanji;
    }

    public void setJapaneseKanji(String japaneseKanji) {
        this.japaneseKanji = japaneseKanji;
    }

    public String getJapaneseAlias() {
        return japaneseAlias;
    }

    public void setJapaneseAlias(String japaneseAlias) {
        this.japaneseAlias = japaneseAlias;
    }

    public String getChinese() {
        return chinese;
    }

    public void setChinese(String chinese) {
        this.chinese = chinese;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTone() {
        return tone;
    }

    public void setTone(String tone) {
        this.tone = tone;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }

    public boolean isHasCollected() {
        return hasCollected;
    }

    public void setHasCollected(boolean hasCollected) {
        this.hasCollected = hasCollected;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof WordEntity) {
            WordEntity temp = (WordEntity) obj;
            return this.getId() == temp.getId();
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        if(id != null)
            return id;
        else
            return super.hashCode();
    }

    @Override
    public Object getKey() {
        return getId();
    }
}
