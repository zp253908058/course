package com.zp.course.ui.sign;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.zp.course.R;
import com.zp.course.app.ToolbarActivity;
import com.zp.course.app.UserManager;
import com.zp.course.storage.database.AppDatabase;
import com.zp.course.storage.database.dao.AccountDao;
import com.zp.course.storage.database.table.UserEntity;
import com.zp.course.storage.sharedpreference.AccountPreference;
import com.zp.course.storage.sharedpreference.SharedPreferenceManager;
import com.zp.course.ui.home.HomeActivity;
import com.zp.course.util.Toaster;
import com.zp.course.util.Validator;
import com.zp.course.util.log.Logger;

import androidx.annotation.Nullable;

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
        showNavigationIcon(false);
        setContentView(R.layout.activity_login);

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
        RegisterActivity.go(this);
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

        long id = verify(username, password);

        if (id > 0) {
            mEntity.setUsername(username);
            mEntity.setPassword(password);

            UserEntity entity = AppDatabase.getInstance().getUserDao().getUser(id);
            UserManager.getInstance().setUser(entity);
            loginSuccess();
        } else {
            Toaster.showToast(R.string.tip_login_failed);
        }
    }

    private long verify(String username, String password) {
        AccountDao dao = AppDatabase.getInstance().getAccountDao();
        return dao.verify(username, password);
    }

    private void loginSuccess() {
        boolean isRetain = mCheckBox.isChecked();
        if (!isRetain) {
            mEntity.setUsername("");
            mEntity.setPassword("");
        }

        boolean success = mManager.save(this, mEntity);
        if (!success) {
            Logger.e("保存失败");
        }
        HomeActivity.go(this);
        finish();
    }

}
