package com.writerwriter.libraassist;

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

import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;
import com.ramotion.foldingcell.FoldingCell;

import java.util.ArrayList;
import java.util.List;

public class SettingsFragment extends Fragment{
    public static SettingsFragment Instance;

    private TextView userNameText;
    private SignInButton googleSigninBtn;
    private Button googleSignoutBtn;
    private Button[] libraryBtn = new Button[3];
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

        account_recyclerView = (RecyclerView)v.findViewById(R.id.account_recyclerlist);
        account_recyclerView.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        account_recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new AccountListAdapter(account_list);
        account_recyclerView.setAdapter(adapter);

        Instance = this;
        // Inflate the layout for this fragment
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
        /*// 台北市圖書館 Botton
        libraryBtn[0] = getView().findViewById(R.id.tcl_account_btn);
        libraryBtn[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AccountManager.Instance.UpdateLibaccount(AccountManager.TAIPEI_LIB_KEY,
                        ((EditText)getView().findViewById(R.id.tcl_account_edit)).getText().toString(),
                        ((EditText)getView().findViewById(R.id.tcl_password_edit)).getText().toString());
            }
        });
        // 新北市圖書館 Botton
        libraryBtn[1] = getView().findViewById(R.id.ntcl_account_btn);
        libraryBtn[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AccountManager.Instance.UpdateLibaccount(AccountManager.NEWTAIPEI_LIB_KEY,
                        ((EditText)getView().findViewById(R.id.ntcl_account_edit)).getText().toString(),
                        ((EditText)getView().findViewById(R.id.ntcl_password_edit)).getText().toString());
            }
        });
        // 台北大學圖書館 Botton
        libraryBtn[2] = getView().findViewById(R.id.ntpul_account_btn);
        libraryBtn[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AccountManager.Instance.UpdateLibaccount(AccountManager.NTPU_LIB_KEY,
                        ((EditText)getView().findViewById(R.id.ntpul_account_edit)).getText().toString(),
                        ((EditText)getView().findViewById(R.id.ntpul_password_edit)).getText().toString());
            }
        });*/


        UpdateUI(FirebaseAuth.getInstance().getCurrentUser() != null);
    }

    @Override
    public void onStop(){
        super.onStop();
    }

    // 更新介面
    public void UpdateUI(boolean isLogin){
        if (isLogin) {
            googleSigninBtn.setVisibility(View.INVISIBLE);
            googleSignoutBtn.setVisibility(View.VISIBLE);
            userNameText.setText(AccountManager.Instance.GetGoogleAccountName());
            /*for (Button btn : libraryBtn) {
                btn.setEnabled(true);
            }*/
        }
        else {
            googleSigninBtn.setVisibility(View.VISIBLE);
            googleSignoutBtn.setVisibility(View.INVISIBLE);
            userNameText.setText(AccountManager.Instance.GetGoogleAccountName());
            /*for (Button btn : libraryBtn) {
                btn.setEnabled(false);
            }*/
        }
        UpdateAccount(AccountManager.TAIPEI_LIB_KEY);
        UpdateAccount(AccountManager.NEWTAIPEI_LIB_KEY);
        UpdateAccount(AccountManager.NTPU_LIB_KEY);
    }

    // 更新帳號按鈕文字
    public void UpdateAccount(String lib){
        String account = AccountManager.Instance.GetLibraryAccount(lib);
        int state = AccountManager.Instance.GetLibraryState(lib);
        switch (lib) {
            case AccountManager.TAIPEI_LIB_KEY:
                account_list.get(2).setAccount(account==null?"未登入":"帳號 : "+account);
                account_list.get(2).setState(state);
                break;
            case AccountManager.NEWTAIPEI_LIB_KEY:
                account_list.get(1).setAccount(account==null?"未登入":"帳號 : "+account);
                account_list.get(1).setState(state);
                break;
            case AccountManager.NTPU_LIB_KEY:
                account_list.get(0).setAccount(account==null?"未登入":"帳號 : "+account);
                account_list.get(0).setState(state);
                break;
        }
        adapter = new AccountListAdapter(account_list);
        account_recyclerView.setAdapter(adapter);
    }
}
