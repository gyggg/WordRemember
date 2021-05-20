package me.japanesestudy.app.wordremember.frame.view;

import java.util.List;
import java.util.TreeMap;

import me.japanesestudy.app.wordremember.datasource.data.AbstractWordUnit;
import me.japanesestudy.app.wordremember.datasource.entity.BookEntity;

/**
 * Created by guyu on 2018/1/16.
 */

public interface ITestWordView {
    void initAdapter(TreeMap<BookEntity, List<AbstractWordUnit>> bookToUnitsMap, List<AbstractWordUnit> historyEntities);
}
