package com.zp.course.ui.sign;

import android.accounts.Account;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.zp.course.R;
import com.zp.course.app.ToolbarActivity;
import com.zp.course.storage.database.AppDatabase;
import com.zp.course.storage.database.dao.AccountDao;
import com.zp.course.storage.database.table.AccountEntity;
import com.zp.course.storage.database.table.UserEntity;
import com.zp.course.util.Toaster;
import com.zp.course.util.Validator;

import androidx.annotation.Nullable;

/**
 * Class description:
 *
 * @author zp
 * @version 1.0
 * @see RegisterActivity
 * @since 2019/3/13
 */
public class RegisterActivity extends ToolbarActivity {

    public static void go(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, RegisterActivity.class);
        context.startActivity(intent);
    }

    private EditText mUsernameText;
    private EditText mPasswordText;
    private EditText mConfirmPasswordText;

    private AccountDao mAccountDao;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_layout);

        initView();
    }

    private void initView() {
        mUsernameText = findViewById(R.id.register_username);
        mPasswordText = findViewById(R.id.register_password);
        mConfirmPasswordText = findViewById(R.id.register_password_confirm);

        mAccountDao = AppDatabase.getInstance().getAccountDao();
    }

    public void onClick(View view) {
        attemptRegister();
    }

    private void attemptRegister() {
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

        String confirmPassword = mConfirmPasswordText.getText().toString();
        if (!password.equals(confirmPassword)) {
            mConfirmPasswordText.setError(getString(R.string.error_password_inconsistent));
            mConfirmPasswordText.requestFocus();
            return;
        }

        boolean success = verify(username);

        if (success) {
            register(username, password);
            finish();
        } else {
            Toaster.showToast(R.string.tip_register_failed);
        }
    }

    private boolean verify(String username) {
        long id = mAccountDao.findByUsername(username);
        return id == 0;
    }

    /**
     * 注册账号，并增加一个用户
     *
     * @param username 账号
     * @param password 密码
     */
    private void register(String username, String password) {
        mAccountDao.register(new AccountEntity(username, password));
        long id = mAccountDao.findByUsername(username);
        UserEntity entity = new UserEntity();
        entity.setAccountId(id);
        AppDatabase.getInstance().getUserDao().add(entity);
        Toaster.showToast(R.string.tip_register_success);
    }
}
