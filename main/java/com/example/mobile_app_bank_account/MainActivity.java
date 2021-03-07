package com.example.mobile_app_bank_account;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    protected void onStart() {
        super.onStart();

        Button validateButton = (Button) findViewById(R.id.ValidateButton);
        CheckBox offlineCheckBox = (CheckBox) findViewById(R.id.offlineCheckBox);
        TextView valueUserId = (TextView) findViewById(R.id.ValueUserId);


        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String checkboxValue = "0"; //If not check
                if(offlineCheckBox.isChecked())
                {
                    checkboxValue = "1"; //If check
                }

                String stringValueUserID = String.valueOf(valueUserId.getText());
                try {
                    if(Integer.valueOf(stringValueUserID) > 29)
                    {
                        String stringUrl = "You don't have an account";
                        Context context = getApplicationContext();
                        CharSequence text = stringUrl;
                        int duration = Toast.LENGTH_LONG;
                        Toast.makeText(context, text, duration).show();
                    }
                    else
                    {
                        Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                        intent.putExtra("value", stringValueUserID);
                        intent.putExtra("checkboxValue", checkboxValue);
                        startActivity(intent);
                    }
                }catch(NumberFormatException e)
                {
                    String stringUrl = "You don't have an account";
                    Context context = getApplicationContext();
                    CharSequence text = stringUrl;
                    int duration = Toast.LENGTH_LONG;
                    Toast.makeText(context, text, duration).show();
                }

            }
        });

    }
}