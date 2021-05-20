package me.japanesestudy.app.wordremember.frame.presenter;

import me.japanesestudy.app.wordremember.datasource.data.AbstractWordUnit;
import me.japanesestudy.app.wordremember.datasource.entity.WordEntity;
import me.japanesestudy.app.wordremember.frame.model.WordModel;
import me.japanesestudy.app.wordremember.frame.view.IWordView;

/**
 * Created by guyu on 2018/1/22.
 */

public class WordPresenter extends BasePresenter<IWordView> {
    WordModel wordModel = new WordModel();
    public WordPresenter(IWordView view) {
        super(view);
    }

    public void initWordList(AbstractWordUnit abstractWordUnit) {
        wordModel.injectTestUnitWords(abstractWordUnit);
        view.initWordListAdapter(abstractWordUnit.getTestUnitWord());
    }

    public void setCollected(boolean isCollected, WordEntity item, int itemPosition) {
        item.setHasCollected(isCollected);
        item = wordModel.saveWordEntity(item);
        view.onCollected(item, itemPosition);
    }
}
