package com.writerwriter.libraassist;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.ramotion.foldingcell.FoldingCell;

import java.util.List;

/**
 * Created by Larry on 2017/12/10.
 */

public class AccountListAdapter extends RecyclerView.Adapter<AccountListAdapter.ViewHolder> {
    private List<AccountUnit> accountList;
    private Context mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView account;
        public TextView libraryName;
        public EditText enter_account;
        public EditText enter_password;
        public Button log_in;
        public Button log_out;
        public ImageView image;

        public ViewHolder(View itemView){
            super(itemView);

            account = (TextView)itemView.findViewById(R.id.account);
            libraryName = (TextView)itemView.findViewById(R.id.library_name);
            enter_account = (EditText)itemView.findViewById(R.id.enter_account);
            enter_password = (EditText)itemView.findViewById(R.id.enter_password);
            log_in = (Button)itemView.findViewById(R.id.log_in_btn);
            log_out = (Button)itemView.findViewById(R.id.log_out_btn);
            image = (ImageView)itemView.findViewById(R.id.library_login_icon);
        }
    }
    public AccountListAdapter(List<AccountUnit> accountList, Context context){
        this.accountList = accountList;
        this.mContext = context;
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
        /*account_list_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final FoldingCell fc = (FoldingCell) view.findViewById(R.id.account_cardview);
                fc.toggle(false);
            }
        });*/
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final AccountUnit accountUnit = accountList.get(position);
        switch ((accountUnit.getLibraryName())){
            case "國立台北大學圖書館":
                holder.image.setImageResource(R.drawable.ntpu_lib);
                holder.image.setMaxHeight(holder.image.getWidth()*960/1920);
                break;
            case "新北市立圖書館":
                holder.image.setImageResource(R.drawable.ntc_lib);
                holder.image.setMaxHeight(holder.image.getWidth()*472/1280);
                break;
            case "台北市立圖書館":
                holder.image.setImageResource(R.drawable.tc_lib);
                holder.image.setMaxHeight(holder.image.getWidth()*960/1920);
                break;
        }
        holder.libraryName.setText(accountUnit.getLibraryName());
        holder.account.setText(accountUnit.getAccount());
        holder.log_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!holder.enter_account.getText().toString().isEmpty() && !holder.enter_account.getText().toString().isEmpty()) {
                    switch (accountUnit.getLibraryName()) {
                        case "國立台北大學圖書館":
                            AccountManager.Instance.SendUpdateAccount(AccountManager.NTPU_LIB_KEY,
                                    holder.enter_account.getText().toString(),
                                    holder.enter_password.getText().toString());
                            break;
                        case "新北市立圖書館":
                            AccountManager.Instance.SendUpdateAccount(AccountManager.NEWTAIPEI_LIB_KEY,
                                    holder.enter_account.getText().toString(),
                                    holder.enter_password.getText().toString());
                            break;
                        case "台北市立圖書館":
                            AccountManager.Instance.SendUpdateAccount(AccountManager.TAIPEI_LIB_KEY,
                                    holder.enter_account.getText().toString(),
                                    holder.enter_password.getText().toString());
                            break;
                    }
                }
                else if(holder.enter_account.getText().toString().isEmpty() || holder.enter_account.getText().toString().isEmpty()){
                    Toast.makeText(mContext,"請輸入完整的帳號和密碼。",Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(accountUnit.getLibraryName()){
                    case "國立台北大學圖書館":
                        AccountManager.Instance.SendDeleteAccount(AccountManager.NTPU_LIB_KEY);
                        break;
                    case "新北市立圖書館":
                        AccountManager.Instance.SendDeleteAccount(AccountManager.NEWTAIPEI_LIB_KEY);
                        break;
                    case "台北市立圖書館":
                        AccountManager.Instance.SendDeleteAccount(AccountManager.TAIPEI_LIB_KEY);
                        break;
                }
            }
        });
        if (accountUnit.getState() == AccountUnit.PENDING){
            holder.account.setText("驗證帳號密碼中...");
        }
        else if(accountUnit.getState() == AccountUnit.ERROR){
            holder.account.setText("帳號密碼驗證失敗");
        }

        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            holder.log_in.setEnabled(true);
        }
        else{
            holder.log_in.setEnabled(false);
        }
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(accountList.get(position).getState() != AccountUnit.PENDING) {
                    final FoldingCell fc = (FoldingCell) view.findViewById(R.id.account_cardview);
                    fc.toggle(false);
                }
            }
        });

    }

}
