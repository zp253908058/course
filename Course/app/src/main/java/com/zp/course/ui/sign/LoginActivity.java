package com.zp.course.ui.sign;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.zp.course.R;
import com.zp.course.app.ToolbarActivity;
import com.zp.course.storage.database.AppDatabase;
import com.zp.course.storage.database.dao.AccountDao;
import com.zp.course.storage.sharedprefence.SharedPreferenceManager;
import com.zp.course.storage.sharedprefence.AccountPreference;
import com.zp.course.ui.home.HomeActivity;
import com.zp.course.util.Validator;
import com.zp.course.util.Toaster;
import com.zp.course.util.log.Logger;

/**
 * Class description:
 *
 * @author zp
 * @version 1.0
 * @see LoginActivity
 * @since 2019/3/6
 */
public class LoginActivity extends ToolbarActivity {

    public static void go(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, LoginActivity.class);
        context.startActivity(intent);
    }

    private EditText mUsernameText;
    private EditText mPasswordText;

    private CheckBox mCheckBox;
    private SharedPreferenceManager mManager = SharedPreferenceManager.getInstance();
    private AccountPreference mEntity = new AccountPreference();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_layout);

        initView();
    }

    private void initView() {
        mUsernameText = findViewById(R.id.login_username);
        mPasswordText = findViewById(R.id.login_password);

        mCheckBox = findViewById(R.id.login_retain_password);

        boolean success = mManager.load(this, mEntity);
        if (success) {
            mUsernameText.setText(mEntity.getUsername());
            mPasswordText.setText(mEntity.getPassword());
        }
    }

    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.login_register:
                onRegister();
                break;
            case R.id.login_button:
                attemptLogin();
                break;
        }
    }

    private void onRegister() {

    }

    /**
     * 登录操作
     */
    private void attemptLogin() {
        String username = mUsernameText.getText().toString();
        if (Validator.isEmpty(username)) {
            mUsernameText.setError(getString(R.string.error_username_empty));
            mUsernameText.requestFocus();
            return;
        }

        String password = mPasswordText.getText().toString();
        if (Validator.isEmpty(password)) {
            mPasswordText.setError(getString(R.string.error_password_empty));
            mPasswordText.requestFocus();
            return;
        }

        if (password.length() < 6) {
            mPasswordText.setError(getString(R.string.error_password_too_short));
            mPasswordText.requestFocus();
            return;
        }

        boolean success = verify(username, password);

        if (success) {
            mEntity.setUsername(username);
            mEntity.setPassword(password);
            loginSuccess();
        } else {
            Toaster.showToast("用户名或者密码错误");
        }
    }

    private boolean verify(String username, String password) {
        AccountDao dao = AppDatabase.getInstance(this).getAccountDao();
        return dao.verify(username, password) > 0;
    }

    private void loginSuccess() {
        boolean isRetain = mCheckBox.isChecked();
        if (!isRetain) {
            return;
        }

        boolean success = mManager.save(this, mEntity);
        if (!success) {
            Logger.e("保存失败");
        }
        HomeActivity.go(this);
        finish();
    }

}
