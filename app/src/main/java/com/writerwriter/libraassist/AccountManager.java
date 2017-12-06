package com.writerwriter.libraassist;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by KKK on 2017/10/19.
 */

public class AccountManager {
    public static AccountManager Instance;

    public static final int RC_SIGN_IN = 1;
    public static final String TAIPEI_LIB_KEY = "tc_lib";
    public static final String NEWTAIPEI_LIB_KEY = "ntc_lib";
    public static final String NTPU_LIB_KEY = "ntpu_lib";
    private static final String LOG_FLAG = "---AccountManager---";
    private static final String USER_DATABASE_PATH = "user_data/";
    private static final String ACCOUNT_DATABASE_KEY = "library_account";

    public FirebaseAuth mAuth;

    private final AppCompatActivity mActivity;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference ref;
    private ChildEventListener mChildEventListener;
    private GoogleApiClient mGoogleApiClient;
    private HashMap<String, String> libraryAccount = new HashMap<>();

    public AccountManager(AppCompatActivity activity){
        mActivity = activity;
        Instance = this;

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(mActivity.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(mActivity.getApplicationContext())
                .enableAutoManage(mActivity, new GoogleApiClient.OnConnectionFailedListener(){
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(mActivity, "Error : AutoManage connect Fail", Toast.LENGTH_LONG).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // 定義 Firebase 身分驗證 Listener
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(LOG_FLAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    // 設定db路徑為 Account/uid 若id!=googleid將會無法存取
                    ref = database.getReference(USER_DATABASE_PATH + mAuth.getCurrentUser().getUid());
                    if (mChildEventListener != null) {
                        ref.removeEventListener(mChildEventListener);
                    }
                    ref.addChildEventListener(mChildEventListener);
                    if (SettingsFragment.Instance != null){
                        SettingsFragment.Instance.UpdateUI(true);
                    }
                }
                else {
                    Log.d(LOG_FLAG, "onAuthStateChanged:signed_out");
                    libraryAccount.clear();
                    if (SettingsFragment.Instance != null){
                        SettingsFragment.Instance.UpdateUI(false);
                    }
                }
            }
        };

        // 定義 Firebase 資料庫 Listener
        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                //Log.d(LOG_FLAG, "DataAdd    "+dataSnapshot.getKey()+" "+dataSnapshot.getValue());
                if (dataSnapshot.getKey().equals(ACCOUNT_DATABASE_KEY)) {
                    Iterator<DataSnapshot> iter = dataSnapshot.getChildren().iterator();
                    while (iter.hasNext()) {
                        DataSnapshot iterSnapshot = iter.next();
                        String account = ((HashMap<String, String>)iterSnapshot.getValue()).get("account").toString();
                        libraryAccount.put(iterSnapshot.getKey(), account);
                        if (SettingsFragment.Instance != null) {
                            SettingsFragment.Instance.UpdateAccount(iterSnapshot.getKey());
                        }
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
                //Log.d(LOG_FLAG, "DataChange "+dataSnapshot.getKey()+" "+dataSnapshot.getValue());
                if (dataSnapshot.getKey().equals(ACCOUNT_DATABASE_KEY)) {
                    libraryAccount.clear();
                    Iterator<DataSnapshot> iter = dataSnapshot.getChildren().iterator();
                    while (iter.hasNext()) {
                        DataSnapshot iterSnapshot = iter.next();
                        String account = ((HashMap<String, String>)iterSnapshot.getValue()).get("account").toString();
                        libraryAccount.put(iterSnapshot.getKey(), account);
                    }
                    if (SettingsFragment.Instance != null) {
                        SettingsFragment.Instance.UpdateUI(true);
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                //Log.d(LOG_FLAG, "DataRemove "+dataSnapshot.getKey()+" "+dataSnapshot.getValue());
                if (dataSnapshot.getKey().equals(ACCOUNT_DATABASE_KEY)) {
                    Iterator<DataSnapshot> iter = dataSnapshot.getChildren().iterator();
                    while (iter.hasNext()) {
                        DataSnapshot iterSnapshot = iter.next();
                        libraryAccount.remove(iterSnapshot.getKey());
                        if (SettingsFragment.Instance != null) {
                            SettingsFragment.Instance.UpdateAccount(iterSnapshot.getKey());
                        }
                    }
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
    }

    // 初始化 啟動Listerner
    public void Init() {
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
        mAuth.addAuthStateListener(mAuthListener);
    }

    // 關閉Listerner
    public void Close() {
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
        if (mChildEventListener != null) {
            ref.removeEventListener(mChildEventListener);
        }
    }

    // Google 登入
    public void GoogleSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        mActivity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    // Google 登出
    public void GoogleSignOut() {
        mAuth.signOut();
    }

    // 取得 Firebase 身分驗證
    public void AuthenticateWithFirebase(Intent data){
        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
        if (result.isSuccess()) {
            // Google Sign In was successful, authenticate with Firebase
            GoogleSignInAccount account = result.getSignInAccount();
            AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(mActivity, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success
                                Log.d(LOG_FLAG, "Authentication success.");
                                //Toast.makeText(mActivity.getApplicationContext(),  mAuth.getCurrentUser() + " Authentication Success.", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                // Authentication failed
                                Log.d(LOG_FLAG, "Authentication failed.");
                                Toast.makeText(mActivity.getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            //Toast.makeText(mActivity.getApplicationContext(), "Google Sign In success.", Toast.LENGTH_SHORT).show();
        } else {
            // Google Sign In failed, update UI appropriately.
            Log.d(LOG_FLAG, "ERROR : Google Sign In failed.");
            Toast.makeText(mActivity.getApplicationContext(), "ERROR : Google Sign In failed.", Toast.LENGTH_SHORT).show();
        }
    }

    // 取得 google 名稱
    public String GetGoogleAccountName() {
        return mAuth.getCurrentUser()!=null?mAuth.getCurrentUser().getDisplayName():"Please Login";
    }

    // 取得 library 帳號
    public String GetLibraryAccount(String lib) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null && libraryAccount.containsKey(lib))
            return libraryAccount.get(lib);
        else
            return null;
    }

    public void UpdateLibaccount(String lib, String account, String password) {
        // 修改帳號密碼
        if (CheckAccountAvailable(account, password)) {
            Map<String, Object> users = new HashMap<String, Object>();
            users.put(ACCOUNT_DATABASE_KEY+"/"+lib+"/account", account);
            users.put(ACCOUNT_DATABASE_KEY+"/"+lib+"/password", password);
            //libraryAccount.put(lib, account);
            ref.updateChildren(users);
            Toast.makeText(mActivity.getApplicationContext(), "Account update success: Update account & passwd.", Toast.LENGTH_SHORT).show();
        }
        // 刪除帳號
        else if (account.equals("") && password.equals("")) { //TODO: 暫時以兩個欄位都為空白就刪除帳號
            ref.child(ACCOUNT_DATABASE_KEY+"/"+lib).removeValue();
            Toast.makeText(mActivity.getApplicationContext(), "Account update success: Deleted account.", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(mActivity.getApplicationContext(), "Account update failed: Some label is empty.", Toast.LENGTH_SHORT).show();
        }
    }

    // 判斷帳號密碼是否合法 可能以後不會需要
    public boolean CheckAccountAvailable(String account, String password){
        if (account.equals("") || password.equals("")) {
            return false;
        }
        return true;
    }

    //TODO: FOR DEBUG
    public void SendMessage(String msg){
        Map<String, Object> users = new HashMap<String, Object>();
        users.put(new java.util.Date().toString(), msg);
        ref.updateChildren(users);
    }
}
