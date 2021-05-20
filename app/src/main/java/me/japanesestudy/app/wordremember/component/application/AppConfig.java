package me.japanesestudy.app.wordremember.component.application;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import me.japanesestudy.app.wordremember.datasource.entity.WordEntity;
import me.japanesestudy.app.wordremember.tools.DateTool;
import me.japanesestudy.app.wordremember.tools.JsonTool;
import me.japanesestudy.app.wordremember.tools.PreferenceTool;

/**
 * Created by guyu on 2018/1/9.
 */

public class AppConfig {
    final static public String KEY_AUTO_CREATE_HISTORY_NAME = "key_auto_create_history_name";
    final static public String KEY_IS_C2J = "key_is_c2j";
    final static public String KEY_TEST_HIDE_KANJI = "key_test_hide_kanji";
    final static public String KEY_SKIP_TYPES = "key_skip_types";
    public static final String TAG = "AppConfig";
    public static final int BASE_CONFIG_VERSION = 31;
    static private Context context;

    private int configVersion = BASE_CONFIG_VERSION;
    private boolean isRandom = false;
    private boolean isCompositeUnit = true;
    private boolean isC2J = true;
    private boolean isShowKanji = true;
    private boolean isAutoHistoryName = false;
    private boolean isFirstLaunch = true;
    private ArrayList<Integer> skipTypeScope = new ArrayList<>();

    private ReviewConfig reviewConfig = new ReviewConfig();
    private StudyConfig studyConfig = new StudyConfig();
    public static class TestUnitConfig {
        private String unitName;
        private String info;
        private int srcSize;

        public String getUnitName() {
            return unitName;
        }

        public void setUnitName(String unitName) {
            this.unitName = unitName;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public int getSrcSize() {
            return srcSize;
        }

        public void setSrcSize(int srcSize) {
            this.srcSize = srcSize;
        }
    }
    public static class ReviewConfig extends TestUnitConfig {
        final static public int LEVEL_EASY = 0;
        final static public int LEVEL_NORMAL = 1;
        final static public int LEVEL_HARD = 2;
        final static public int LEVEL_NIGHTMARE = 3;

        private Date lastReviewDate;
        private int reviewDayScope = 2;
        private int reviewLevel = LEVEL_NORMAL;
        private int lastReviewHistoryParentId = 0;

        public static String getLevelName(int level) {
            switch (level) {
                case LEVEL_EASY:
                    return "简单模式";
                case LEVEL_NORMAL:
                    return "普通模式";
                case LEVEL_HARD:
                    return "困难模式";
                case LEVEL_NIGHTMARE:
                    return "噩梦模式";
                default:
                    return "虚空模式";
            }
        }

        public static List<String> getAllLevelName() {
            List<String> result = new ArrayList<>();
            for(int i = 0; i < 4; i++)
                result.add(getLevelName(i));
            return result;
        }

        public static List<Integer> getAllLevelId() {
            List<Integer> result = new ArrayList<>();
            for(int i = 0; i < 4; i++)
                result.add(i);
            return result;
        }

        public int getLastReviewHistoryParentId() {
            return lastReviewHistoryParentId;
        }

        public void setLastReviewHistoryParentId(int lastReviewHistoryParentId) {
            this.lastReviewHistoryParentId = lastReviewHistoryParentId;
        }

        public ReviewConfig() {
            lastReviewDate = DateTool.getYesterday();
        }

        public Date getLastReviewDate() {
            return lastReviewDate;
        }

        public void setLastReviewDate(Date lastReviewDate) {
            this.lastReviewDate = lastReviewDate;
        }

        public int getReviewDayScope() {
            return reviewDayScope;
        }

        public void setReviewDayScope(int reviewDayScope) {
            this.reviewDayScope = reviewDayScope;
        }

        public int getReviewLevel() {
            return reviewLevel;
        }

        public void setReviewLevel(int reviewLevel) {
            this.reviewLevel = reviewLevel;
        }

        public void reset() {
            lastReviewDate = DateTool.getYesterday();
            lastReviewHistoryParentId = 0;
        }
    }

    public static class StudyConfig extends TestUnitConfig {
        final static public int TYPE_NORMAL_BOOK = 0;
        final static public int TYPE_WORD_TYPE_SELECTED = 1;
        final static public int TYPE_ALL_WORD = 2;

        private long seed = 1234567L;
        private int studyType = TYPE_NORMAL_BOOK;
        private Date lastStudyDate;
        private int lastStudyHistoryParentId = 0;
        //random_test:
        private ArrayList<Integer> randomTestTypeScope = new ArrayList<>();
        private int perPart = 50;
        private int nextPart = 0;
        //normal_book:
        private int testBookId = 1;

        public StudyConfig() {
            lastStudyDate = DateTool.getYesterday();
            randomTestTypeScope.add(WordEntity.WordType.V1);
            randomTestTypeScope.add(WordEntity.WordType.V2);
        }

        public long getSeed() {
            return seed;
        }

        public void setSeed(long seed) {
            this.seed = seed;
        }

        public int getStudyType() {
            return studyType;
        }

        public void setStudyType(int studyType) {
            this.studyType = studyType;
        }

        public Date getLastStudyDate() {
            return lastStudyDate;
        }

        public void setLastStudyDate(Date lastStudyDate) {
            this.lastStudyDate = lastStudyDate;
        }

        public int getLastStudyHistoryParentId() {
            return lastStudyHistoryParentId;
        }

        public void setLastStudyHistoryParentId(int lastStudyHistoryParentId) {
            this.lastStudyHistoryParentId = lastStudyHistoryParentId;
        }

        public ArrayList<Integer> getRandomTestTypeScope() {
            return randomTestTypeScope;
        }

        public void setRandomTestTypeScope(ArrayList<Integer> randomTestTypeScope) {
            this.randomTestTypeScope = randomTestTypeScope;
        }

        public int getPerPart() {
            return perPart;
        }

        public void setPerPart(int perPart) {
            this.perPart = perPart;
        }

        public int getNextPart() {
            return nextPart;
        }

        public void setNextPart(int nextPart) {
            this.nextPart = nextPart;
        }

        public int getTestBookId() {
            return testBookId;
        }

        public void setTestBookId(int testBookId) {
            this.testBookId = testBookId;
        }

        public void reset() {
            setNextPart(0);
            setLastStudyDate(DateTool.getYesterday());
            setLastStudyHistoryParentId(0);
            seed = new Random().nextLong();
        }
    }


    public boolean isC2J() {
        return isC2J;
    }

    public static final String PREFERENCE_KEY_NAME = "config";

    public static AppConfig instance = null;

    private AppConfig(){}

    public static void init(Context context) {
        PreferenceTool.init(context);
        AppConfig.context = context;
        String configString = PreferenceTool.load(PREFERENCE_KEY_NAME, null);
        if(configString != null) {
            instance = JsonTool.getInstance(configString, AppConfig.class);
            if(instance == null || instance.getConfigVersion() < BASE_CONFIG_VERSION) {
                defaultInit();
                instance.setConfigVersion(BASE_CONFIG_VERSION);
                Log.d(TAG, "reset app config");
            }
        } else {
            defaultInit();
        }
        save();
    }

    public static void reload() {
        String configString = PreferenceTool.load(PREFERENCE_KEY_NAME, null);
        if(configString != null) {
            instance = JsonTool.getInstance(configString, AppConfig.class);
            if(instance == null || instance.getConfigVersion() < BASE_CONFIG_VERSION) {
                defaultInit();
                instance.setConfigVersion(BASE_CONFIG_VERSION);
                Log.d(TAG, "reset app config");
            }
        } else {
            defaultInit();
        }
        save();
    }

    public static AppConfig getInstance() {
        return instance;
    }

    public static void save() {
        String configString = JsonTool.toJsonString(instance);
        PreferenceTool.save(PREFERENCE_KEY_NAME, configString);
    }

    private static void defaultInit() {
        instance = new AppConfig();
        save();
    }

    public static void loadAppPreference(String fileName) {
        instance.setAutoHistoryName(PreferenceTool.loadBoolean(fileName, KEY_AUTO_CREATE_HISTORY_NAME, instance.isAutoHistoryName));
        instance.setC2J(PreferenceTool.loadBoolean(fileName, KEY_IS_C2J, instance.isC2J));
        instance.setShowKanji(PreferenceTool.loadBoolean(fileName, KEY_TEST_HIDE_KANJI, instance.isShowKanji));
        save();
    }


    public boolean isRandom() {
        return isRandom;
    }

    public void setRandom(boolean random) {
        isRandom = random;
    }

    public boolean isCompositeUnit() {
        return isCompositeUnit;
    }

    public void setCompositeUnit(boolean compositeUnit) {
        isCompositeUnit = compositeUnit;
    }


    public int getConfigVersion() {
        return configVersion;
    }

    public void setConfigVersion(int configVersion) {
        this.configVersion = configVersion;
    }

    public void setC2J(boolean c2J) {
        isC2J = c2J;
    }

    public boolean isShowKanji() {
        return isShowKanji;
    }

    public void setShowKanji(boolean showKanji) {
        isShowKanji = showKanji;
    }

    public boolean isAutoHistoryName() {
        return isAutoHistoryName;
    }

    public void setAutoHistoryName(boolean autoHistoryName) {
        isAutoHistoryName = autoHistoryName;
    }

    public boolean isFirstLaunch() {
        return isFirstLaunch;
    }

    public void setFirstLaunch(boolean firstLaunch) {
        this.isFirstLaunch = firstLaunch;
    }

    public ArrayList<Integer> getSkipTypeScope() {
        return skipTypeScope;
    }

    public void setSkipTypeScope(ArrayList<Integer> skipTypeScope) {
        this.skipTypeScope = skipTypeScope;
    }

    public static StudyConfig getStudyConfig() {
        return instance.studyConfig;
    }

    public static ReviewConfig getReviewConfig() {
        return instance.reviewConfig;
    }
}
