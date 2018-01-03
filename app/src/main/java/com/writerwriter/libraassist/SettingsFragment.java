package com.writerwriter.libraassist;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;
import com.ramotion.foldingcell.FoldingCell;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SettingsFragment extends Fragment{
    public static SettingsFragment Instance;

    private TextView userNameText;
    private SignInButton googleSigninBtn;
    private Button googleSignoutBtn;
    private List<AccountUnit> account_list = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView account_recyclerView;
    private AccountListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        account_list.add(new AccountUnit(null,null,"國立台北大學圖書館"));
        account_list.add(new AccountUnit(null,null,"新北市立圖書館"));
        account_list.add(new AccountUnit(null,null,"台北市立圖書館"));

        account_recyclerView = v.findViewById(R.id.account_recyclerlist);
        account_recyclerView.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        account_recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new AccountListAdapter(account_list,getContext());
        account_recyclerView.setAdapter(adapter);

        Instance = this;
        return v;
    }

    @Override
    public void onStart(){
        super.onStart();

        // 用戶名稱 TextView
        userNameText = getView().findViewById(R.id.username_text);

        // google登入 Botton
        googleSigninBtn = getView().findViewById(R.id.google_signin_button);
        googleSigninBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AccountManager.Instance.GoogleSignIn();
            }
        });
        // google登出 Botton
        googleSignoutBtn = getView().findViewById(R.id.google_signout_button);
        googleSignoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AccountManager.Instance.GoogleSignOut();
            }
        });

        UpdateUI(FirebaseAuth.getInstance().getCurrentUser() != null);
    }

    @Override
    public void onStop(){
        super.onStop();
    }

    // 更新介面
    public void UpdateUI(boolean isLogin){
        String name = AccountManager.Instance.GetGoogleAccountName();
        if (isLogin) {
            googleSigninBtn.setVisibility(View.INVISIBLE);
        }
        else {
            googleSigninBtn.setVisibility(View.VISIBLE);
        }
        if (name != null){
            googleSignoutBtn.setVisibility(View.VISIBLE);
            userNameText.setText(name);
        }
        else {
            googleSignoutBtn.setVisibility(View.INVISIBLE);
            userNameText.setText("Please Login");
        }
        UpdateAccount(AccountManager.TAIPEI_LIB_KEY);
        UpdateAccount(AccountManager.NEWTAIPEI_LIB_KEY);
        UpdateAccount(AccountManager.NTPU_LIB_KEY);

        adapter = new AccountListAdapter(account_list,getContext());
        account_recyclerView.setAdapter(adapter);
    }

    // 更新帳號按鈕文字
    private void UpdateAccount(String lib){
        String account = AccountManager.Instance.GetLibraryAccount(lib);
        account = account==null?"未登入":"帳號 : "+account;
        int state = AccountManager.Instance.GetLibraryState(lib);

        switch (lib) {
            case AccountManager.TAIPEI_LIB_KEY:
                account_list.get(2).setAccount(account);
                account_list.get(2).setState(state);
                break;
            case AccountManager.NEWTAIPEI_LIB_KEY:
                account_list.get(1).setAccount(account);
                account_list.get(1).setState(state);
                break;
            case AccountManager.NTPU_LIB_KEY:
                account_list.get(0).setAccount(account);
                account_list.get(0).setState(state);
                break;
        }
    }

    private void setAlarm(){
        List<BorrowBookUnit> allbooklist = AccountManager.Instance.borrowBookList;
        for(int i=0;i<allbooklist.size();i++) {
            BorrowBookUnit borrowBookUnit = allbooklist.get(i);
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy/MM/dd");
            SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
            Date cur = new Date(System.currentTimeMillis());
            Date return_date = cur; //initial
            if(borrowBookUnit.getReturn_time().matches("[0-9]+/[0-9]+/[0-9]+")){
                try {
                    return_date = dateFormat1.parse(borrowBookUnit.getReturn_time());
                }catch (ParseException e){
                    e.printStackTrace();
                }
            }
            else if(borrowBookUnit.getReturn_time().matches("[0-9]+-[0-9]+-[0-9]+")){
                try{
                    return_date = dateFormat2.parse(borrowBookUnit.getReturn_time());
                }catch (ParseException e){
                    e.printStackTrace();
                }
            }
            long left_time_millis = return_date.getTime() - cur.getTime();
            int left_time_days =(int)(left_time_millis / 1000 / 60 / 60 / 24);
            if(left_time_days > 0){
                Intent intent = new Intent();
                intent.setClass(getActivity(),AlarmReceiver.class);
                intent.putExtra("title",borrowBookUnit.getBook_name());
                intent.putExtra("time",left_time_days);
                intent.putExtra("id",i);
                PendingIntent pending1 = PendingIntent.getBroadcast(getContext(),i * 3,intent,PendingIntent.FLAG_CANCEL_CURRENT);
                PendingIntent pending2 = PendingIntent.getBroadcast(getContext(),i * 3 + 1,intent,PendingIntent.FLAG_CANCEL_CURRENT);
                PendingIntent pending3 = PendingIntent.getBroadcast(getContext(),i * 3 + 2,intent,PendingIntent.FLAG_CANCEL_CURRENT);
                AlarmManager alarm = (AlarmManager) getContext().getSystemService(getContext().ALARM_SERVICE);
                //書本要到期的前三天會通知
                alarm.set(AlarmManager.RTC_WAKEUP, return_date.getTime()-3*24*60*60*60,pending1);
                alarm.set(AlarmManager.RTC_WAKEUP, return_date.getTime()-2*24*60*60*60,pending2);
                alarm.set(AlarmManager.RTC_WAKEUP, return_date.getTime()-1*24*60*60*60,pending3);
            }
        }
    }
}
