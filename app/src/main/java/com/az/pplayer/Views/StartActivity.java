package com.az.pplayer.Views;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.az.pplayer.MainActivity;
import com.az.pplayer.R;
import com.az.pplayer.Storage.UserStorage;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.passwordTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        if (!UserStorage.Get().getPasswordProtected()){
            Intent intent = new Intent(StartActivity.this, MainActivity.class);
            startActivity(intent);
        }

        findViewById(R.id.ok_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = ((EditText)findViewById(R.id.pwd_edit)).getText().toString();
                if (UserStorage.Get().CheckPassword(password)) {
                    Intent intent = new Intent(StartActivity.this, CategoryViewActivity.class);
                    startActivity(intent);
                } else {
                    ((TextView)findViewById(R.id.txtview_error)).setText("Wrong password");
                }
            }
        });
        findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.exit(0);

            }
        });
    }
}
