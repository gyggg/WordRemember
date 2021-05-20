package me.japanesestudy.app.wordremember.component.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import me.japanesestudy.app.wordremember.R;
import me.japanesestudy.app.wordremember.datasource.data.WordShown;
import me.japanesestudy.app.wordremember.datasource.entity.WordEntity;

/**
 * Created by guyu on 2018/1/22.
 */

public class WordListAdapter extends BaseQuickAdapter<WordEntity, BaseViewHolder> {

    private WordListAdpaterLintener wordListAdpaterLintener;
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public WordListAdapter(List<WordEntity> data) {
        super(R.layout.item_word, data);
    }

    public WordListAdpaterLintener getWordListAdpaterLintener() {
        return wordListAdpaterLintener;
    }

    public void setWordListAdpaterLintener(WordListAdpaterLintener wordListAdpaterLintener) {
        this.wordListAdpaterLintener = wordListAdpaterLintener;
    }

    @Override
    protected void convert(BaseViewHolder helper, WordEntity item) {
        WordShown wordShown = new WordShown(item);
        helper.setText(R.id.tv_name, wordShown.getJapanese())
                .setText(R.id.tv_info, wordShown.getInfo())
                .setChecked(R.id.ckbox_collection, item.isHasCollected())
                .setOnClickListener(R.id.ckbox_collection, v -> {
                    if(wordListAdpaterLintener != null) {
                        wordListAdpaterLintener.onCollected(!item.isHasCollected(), item, helper.getAdapterPosition());
                    }
                });
    }

    public interface WordListAdpaterLintener {
        void onCollected(boolean collected, WordEntity item, int itemPosition);
    }

    public void resetItem(WordEntity item, int itemPosition) {
        this.setData(itemPosition, item);
    }
}
