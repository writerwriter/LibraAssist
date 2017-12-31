package com.writerwriter.libraassist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Larry on 2017/12/28.
 */

public class BorrowBookAdapter extends RecyclerView.Adapter<BorrowBookAdapter.ViewHolder> {
    private List<BorrowBookUnit> borrowBookUnitList;
    private Context mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView borrow_book_title;
        public TextView borrow_book_author;
        public TextView borrow_book_location;
        public TextView borrow_book_search_number;
        public TextView borrow_book_borrow_time;
        public TextView borrow_book_return_time;
        public TextView borrow_book_waiting_people_number;
        public TextView borrow_book_renew_count;

        public ViewHolder(View itemView){
            super(itemView);

            borrow_book_title = (TextView) itemView.findViewById(R.id.borrow_book_title);
            borrow_book_author = (TextView) itemView.findViewById(R.id.borrow_book_author);
            borrow_book_location = (TextView) itemView.findViewById(R.id.borrow_book_location);
            borrow_book_search_number = (TextView) itemView.findViewById(R.id.borrow_book_search_number);
            borrow_book_borrow_time = (TextView) itemView.findViewById(R.id.borrow_book_borrow_time);
            borrow_book_return_time = (TextView) itemView.findViewById(R.id.borrow_book_return_time);
            borrow_book_waiting_people_number = (TextView) itemView.findViewById(R.id.borrow_book_waiting_people_number);
            borrow_book_renew_count = (TextView) itemView.findViewById(R.id.borrow_book_renew_count);
        }
    }

    public BorrowBookAdapter(List<BorrowBookUnit> borrowBookUnitList, Context mContext) {
        this.borrowBookUnitList = borrowBookUnitList;
        this.mContext = mContext;
    }

    @Override
    public int getItemCount() {
        return borrowBookUnitList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View borrow_book_list_view = LayoutInflater.from(mContext).inflate(R.layout.borrow_book_item,parent,false);
        BorrowBookAdapter.ViewHolder viewHolder = new BorrowBookAdapter.ViewHolder(borrow_book_list_view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final BorrowBookUnit borrowBookUnit = borrowBookUnitList.get(position);
        holder.borrow_book_title.setText(borrowBookUnit.getBook_name());
        holder.borrow_book_author.setText("作者 : "+borrowBookUnit.getAuthor());
        holder.borrow_book_location.setText("館藏地 : "+borrowBookUnit.getLocation());
        holder.borrow_book_search_number.setText("索書號 : " + borrowBookUnit.getSearch_book_number());
        holder.borrow_book_borrow_time.setText("借閱日期 : "+borrowBookUnit.getBorrow_time());
        holder.borrow_book_return_time.setText("應還日期 : "+borrowBookUnit.getReturn_time());
        holder.borrow_book_waiting_people_number.setText("預約人數 : "+borrowBookUnit.getWaiting_people_number());
        holder.borrow_book_renew_count.setText("續借人數 : "+borrowBookUnit.getRenew_count());
        holder.itemView.setTag(position);
    }
}
