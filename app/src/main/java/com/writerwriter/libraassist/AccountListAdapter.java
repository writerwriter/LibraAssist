package com.writerwriter.libraassist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Larry on 2017/12/10.
 */

public class AccountListAdapter extends RecyclerView.Adapter<AccountListAdapter.ViewHolder> {
    private List<AccountUnit> accountList;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView account;
        public TextView libraryName;

        public ViewHolder(View itemView){
            super(itemView);

            account = (TextView)itemView.findViewById(R.id.account);
            libraryName = (TextView)itemView.findViewById(R.id.library_name);
        }
    }
    public AccountListAdapter(List<AccountUnit> accountList){
        this.accountList = accountList;
    }

    @Override
    public int getItemCount() {
        return accountList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View account_list_view = LayoutInflater.from(context).inflate(R.layout.account_list_card,parent,false);
        ViewHolder viewHolder = new ViewHolder(account_list_view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AccountUnit accountUnit = accountList.get(position);
        holder.libraryName.setText(accountUnit.getLibraryName());
        holder.account.setText(accountUnit.getAccount());
        holder.itemView.setTag(position);
    }

}
