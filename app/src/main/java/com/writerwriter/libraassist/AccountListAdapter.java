package com.writerwriter.libraassist;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.ramotion.foldingcell.FoldingCell;

import java.util.List;

/**
 * Created by Larry on 2017/12/10.
 */

public class AccountListAdapter extends RecyclerView.Adapter<AccountListAdapter.ViewHolder> {
    private List<AccountUnit> accountList;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView account;
        public TextView libraryName;
        public EditText enter_account;
        public EditText enter_password;
        public Button log_in;

        public ViewHolder(View itemView){
            super(itemView);

            account = (TextView)itemView.findViewById(R.id.account);
            libraryName = (TextView)itemView.findViewById(R.id.library_name);
            enter_account = (EditText)itemView.findViewById(R.id.enter_account);
            enter_password = (EditText)itemView.findViewById(R.id.enter_password);
            log_in = (Button)itemView.findViewById(R.id.log_in_btn);
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
        account_list_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final FoldingCell fc = (FoldingCell) view.findViewById(R.id.account_cardview);
                fc.toggle(false);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final AccountUnit accountUnit = accountList.get(position);
        holder.libraryName.setText(accountUnit.getLibraryName());
        holder.account.setText(accountUnit.getAccount());
        holder.log_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(accountUnit.getLibraryName()){
                       case "國立台北大學圖書館":
                        AccountManager.Instance.UpdateLibaccount(AccountManager.NTPU_LIB_KEY,
                                holder.enter_account.getText().toString(),
                                holder.enter_password.getText().toString());
                        break;
                    case "新北市立圖書館":
                        AccountManager.Instance.UpdateLibaccount(AccountManager.NEWTAIPEI_LIB_KEY,
                                holder.enter_account.getText().toString(),
                                holder.enter_password.getText().toString());
                        break;
                    case "台北市立圖書館":
                        AccountManager.Instance.UpdateLibaccount(AccountManager.TAIPEI_LIB_KEY,
                                holder.enter_account.getText().toString(),
                                holder.enter_password.getText().toString());
                        break;
                }

            }
        });

        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            holder.log_in.setEnabled(true);
        }
        else{
            holder.log_in.setEnabled(false);
        }
        holder.itemView.setTag(position);

    }

}
