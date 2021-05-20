package me.japanesestudy.app.wordremember.component.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gc.materialdesign.views.Button;

import java.util.List;

import me.japanesestudy.app.wordremember.R;
import me.japanesestudy.app.wordremember.datasource.dao.BookDAO;
import me.japanesestudy.app.wordremember.datasource.entity.BookEntity;
import me.japanesestudy.app.wordremember.datasource.entity.UnitEntity;
import me.japanesestudy.app.wordremember.tools.ToastTool;

/**
 * Created by guyu on 2018/1/10.
 */

public class UnitListAdapter extends BaseAdapter implements View.OnClickListener {

    private List<UnitEntity> unitEntities;
    private Context context;
    private LayoutInflater layoutInflater;

    public UnitListAdapter(Context context, List<UnitEntity> unitEntities) {
        this.unitEntities = unitEntities;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return unitEntities.size();
    }

    @Override
    public Object getItem(int position) {
        return unitEntities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UnitEntity unitEntity = unitEntities.get(position);
        ViewHolder viewHolder;
        if(convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_remember_unit, null);
            viewHolder = new ViewHolder(convertView);
        }
        else viewHolder = (ViewHolder) convertView.getTag();
        BookDAO bookDAO = new BookDAO();
        try {
            BookEntity bookEntity = bookDAO.findById(unitEntity.getBookId());
            viewHolder.tvBookName.setText(bookEntity.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        viewHolder.tvUnitName.setText(unitEntity.getName());
        viewHolder.tvWordNum.setText(""+unitEntity.size());
        viewHolder.btnStudy.setTag(unitEntity);
        viewHolder.btnStudy.setOnClickListener(this);
//        if(position % 2 == 0) {
//            viewHolder.cardHeader.setCardBackgroundColor(context.getResources().getColor(R.color.colorPrimaryLight));
//        } else {
//            viewHolder.cardHeader.setCardBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
//        }
        return convertView;
    }

    @Override
    public void onClick(View v) {
        UnitEntity unitEntity = (UnitEntity) v.getTag();
        ToastTool.shortShow(unitEntity.getName());
    }

    class ViewHolder {
        TextView tvBookName;
        TextView tvUnitName;
        TextView tvWordNum;
        Button btnStudy;
        CardView cardHeader;
        ViewHolder(View view) {
            tvBookName = (TextView) view.findViewById(R.id.tv_book_name);
            tvUnitName = (TextView) view.findViewById(R.id.tv_review_name);
            tvWordNum = (TextView) view.findViewById(R.id.tv_word_num);
            btnStudy = (Button) view.findViewById(R.id.btn_study);
            cardHeader = (CardView) view.findViewById(R.id.card_header);
            view.setTag(this);
        }
    }
}
