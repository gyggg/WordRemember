package me.japanesestudy.app.wordremember.component.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

import me.japanesestudy.app.wordremember.R;
import me.japanesestudy.app.wordremember.datasource.entity.BookEntity;

/**
 * Created by guyu on 2018/1/15.
 */

public class BookSelectListAdapter extends ArrayAdapter<BookEntity> {
    private int resource;
    public BookSelectListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<BookEntity> objects) {
        super(context, resource, objects);
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        BookEntity bookEntity = getItem(position);
        if(convertView == null) {
            convertView = View.inflate(getContext(), resource, null);
            viewHolder = new ViewHolder();
            x.view().inject(viewHolder, convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvBookName.setText(bookEntity.getName());
        return convertView;
    }

    class ViewHolder {
        @ViewInject(R.id.tv_book_name) private TextView tvBookName;
    }
}
