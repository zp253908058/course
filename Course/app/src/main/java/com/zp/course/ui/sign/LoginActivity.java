package com.zp.course.ui.sign;

import android.os.Bundle;

import androidx.annotation.Nullable;

import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.zp.course.R;
import com.zp.course.app.ToolbarActivity;
import com.zp.course.ui.home.HomeActivity;
import com.zp.course.util.StringUtils;
import com.zp.course.util.Toaster;

/**
 * Class description:
 *
 * @author zp
 * @version 1.0
 * @see LoginActivity
 * @since 2019/3/6
 */
public class LoginActivity extends ToolbarActivity {

    private EditText mUsernameText;
    private EditText mPasswordText;

    private CheckBox mCheckBox;

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
        if (StringUtils.isEmpty(username)) {
            mUsernameText.setError(getString(R.string.error_username_empty));
            mUsernameText.requestFocus();
            return;
        }

        String password = mPasswordText.getText().toString();
        if (StringUtils.isEmpty(password)) {
            mPasswordText.setError(getString(R.string.error_password_empty));
            mPasswordText.requestFocus();
            return;
        }

        if (StringUtils.lenth(password) < 6) {
            mPasswordText.setError(getString(R.string.error_password_too_short));
            mPasswordText.requestFocus();
            return;
        }

        boolean success = verify(username, password);

        if (success) {
            loginSuccess();
        } else {
            Toaster.showToast("用户名或者密码错误");
        }
    }

    private boolean verify(String username, String password) {
        return true;
    }

    private void loginSuccess() {
        boolean isRetain = mCheckBox.isChecked();
        if (!isRetain) {
            return;
        }


        HomeActivity.go(this);

    }

}
