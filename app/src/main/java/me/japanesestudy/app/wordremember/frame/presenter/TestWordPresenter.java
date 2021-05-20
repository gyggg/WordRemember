package me.japanesestudy.app.wordremember.frame.presenter;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import me.japanesestudy.app.wordremember.datasource.data.AbstractWordUnit;
import me.japanesestudy.app.wordremember.datasource.entity.BookEntity;
import me.japanesestudy.app.wordremember.datasource.entity.UnitEntity;
import me.japanesestudy.app.wordremember.frame.model.BookModel;
import me.japanesestudy.app.wordremember.frame.model.HistoryModel;
import me.japanesestudy.app.wordremember.frame.model.UnitModel;
import me.japanesestudy.app.wordremember.frame.model.WordModel;
import me.japanesestudy.app.wordremember.frame.view.ITestWordView;

/**
 * Created by guyu on 2018/1/16.
 */

public class TestWordPresenter extends BasePresenter<ITestWordView> {
    private UnitModel unitModel = UnitModel.getInstance();
    private BookModel bookModel = BookModel.getInstance();
    private HistoryModel historyModel = HistoryModel.getInstance();
    private WordModel wordModel = new WordModel();

    public TestWordPresenter(ITestWordView view) {
        super(view);
    }

    public void initTestUnitListAdapter() {
        x.task().run(() -> {
            List<BookEntity> bookEntityList = bookModel.getAllBookList();
            TreeMap<BookEntity, List<AbstractWordUnit>> bookEntityToUnitMap = new TreeMap<>();
            for(BookEntity bookEntity : bookEntityList) {
                List<UnitEntity> unitEntities = unitModel.getAllUnitEntityWithWord(bookEntity.getId(), appConfig.isCompositeUnit());
                bookEntityToUnitMap.put(bookEntity, new ArrayList<>(unitEntities));
            }
            List<AbstractWordUnit> historyEntities = new ArrayList<>();
            try {
                historyEntities.addAll(historyModel.getAllCollectedHistory());
            } catch (Exception e) {

            }
            for(AbstractWordUnit abstractWordUnit : historyEntities) {
                wordModel.injectTestUnitWords(abstractWordUnit);
            }
            x.task().post(() -> view.initAdapter(bookEntityToUnitMap, historyEntities));
        });

    }

    public AbstractWordUnit dealTestUnitNewName(AbstractWordUnit abstractWordUnit) {
        bookModel.injectBookName(abstractWordUnit);
        return abstractWordUnit;
    }
}
