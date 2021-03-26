package com.nass.ek.w3web;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class KeytestActivity extends AppCompatActivity {

    ScrollView scrollView;
    TextView headerText;
    TextView scrollViewText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout linLayout = new LinearLayout(this);
        linLayout.setOrientation(LinearLayout.VERTICAL);
        ViewGroup.LayoutParams linLayoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        headerText = new TextView(this);
        headerText.setSingleLine(false);
        headerText.setHint(getString(R.string.press_a_key));

        LinearLayout linLayoutInner = new LinearLayout(this);
        linLayoutInner.setOrientation(LinearLayout.VERTICAL);
        linLayoutInner.addView(headerText);

        scrollViewText = new TextView(this);
        linLayoutInner.addView(scrollViewText);


        scrollView = new ScrollView(this);
        scrollView.addView(linLayoutInner);

        linLayout.addView(scrollView);


        setContentView(linLayout, linLayoutParams);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        String newstr = scrollViewText.getText().toString() == null ? "" : scrollViewText.getText().toString()
                + "\n"
                + "Keycode = " + keyCode;
        scrollViewText.setText(newstr);
        return false;
    }
}
