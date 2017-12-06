package com.writerwriter.libraassist;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsFragment extends Fragment{
    public static SettingsFragment Instance;
    private TextView userNameText;
    private SignInButton googleSigninBtn;
    private Button googleSignoutBtn;
    private Button[] libraryBtn = new Button[3];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Instance = this;
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
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
        // 台北市圖書館 Botton
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
        });


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
            for (Button btn : libraryBtn) {
                btn.setEnabled(true);
            }
        }
        else {
            googleSigninBtn.setVisibility(View.VISIBLE);
            googleSignoutBtn.setVisibility(View.INVISIBLE);
            userNameText.setText(AccountManager.Instance.GetGoogleAccountName());
            for (Button btn : libraryBtn) {
                btn.setEnabled(false);
            }
        }
        UpdateAccount(AccountManager.TAIPEI_LIB_KEY);
        UpdateAccount(AccountManager.NEWTAIPEI_LIB_KEY);
        UpdateAccount(AccountManager.NTPU_LIB_KEY);
    }

    // 更新帳號按鈕文字
    public void UpdateAccount(String lib){
        String account = AccountManager.Instance.GetLibraryAccount(lib);
        switch (lib) {
            case AccountManager.TAIPEI_LIB_KEY:
                ((EditText)getView().findViewById(R.id.tcl_account_edit)).setText("");
                ((EditText)getView().findViewById(R.id.tcl_password_edit)).setText("");
                libraryBtn[0].setText("臺北市立圖書館\n"+(account==null?"未登入":"帳號 : "+account));
                break;
            case AccountManager.NEWTAIPEI_LIB_KEY:
                ((EditText)getView().findViewById(R.id.ntcl_account_edit)).setText("");
                ((EditText)getView().findViewById(R.id.ntcl_password_edit)).setText("");
                libraryBtn[1].setText("新北市立圖書館\n"+(account==null?"未登入":"帳號 : "+account));
                break;
            case AccountManager.NTPU_LIB_KEY:
                ((EditText)getView().findViewById(R.id.ntpul_account_edit)).setText("");
                ((EditText)getView().findViewById(R.id.ntpul_password_edit)).setText("");
                libraryBtn[2].setText("臺北大學圖書館\n"+(account==null?"未登入":"帳號 : "+account));
                break;
        }
    }
}
