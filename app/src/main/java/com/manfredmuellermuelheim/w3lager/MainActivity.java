package com.manfredmuellermuelheim.w3lager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import static android.text.TextUtils.isEmpty;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    MenuItem settingsItem;
    MenuItem infoItem;
    MenuItem aboutItem;
    MenuItem closeItem;
    MenuItem exitItem;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        boolean chromeSuites = checkApp("com.android.chrome", 68);
        TextView chromeVersionTextView = findViewById(R.id.chromeVersion);

        if(chromeSuites) {
            setupSharedPreferences();
            PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
            PackageManager pm = context.getPackageManager();
            PackageInfo pInfo = null;
            try {
                pInfo = pm.getPackageInfo("com.android.chrome", 0);
            } catch (PackageManager.NameNotFoundException e) {
                Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.oldChrome), Toast.LENGTH_LONG);
                LinearLayout layout = (LinearLayout) toast.getView();
                if (layout.getChildCount() > 0) {
                    TextView tv = (TextView) layout.getChildAt(0);
                    tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                }
                toast.show();
                finish();
            }
            String ChromeText = getString(R.string.onChrome);
            assert pInfo != null;
            String ChromeVersion = pInfo.versionName;
            String ChromeVersionText = ChromeText + ChromeVersion;
            chromeVersionTextView.setText(ChromeVersionText);
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            String startCheck = getString(R.string.url_edit_default);
            if (!startCheck.equals(sharedPreferences.getString(getString(R.string.url_edit_key),
                    getString(R.string.url_edit_default))) && !startCheck.isEmpty())
            {
                browserClick(findViewById(R.id.infoView));
            }
        }
    }

    private void setupSharedPreferences() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        setEditUrlValue(sharedPreferences);
    }

    private void setEditUrlValue(SharedPreferences sharedPreferences){
        String defaultEditUrlValue = sharedPreferences.getString(getString(R.string.url_edit_key),
                getString(R.string.url_edit_default));
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(getString(R.string.url_edit_key))){
            setEditUrlValue(sharedPreferences);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.pref_menu, menu);
        settingsItem = menu.findItem(R.id.menu_settings);
        settingsItem.setVisible(true);
        infoItem = menu.findItem(R.id.menu_info);
        infoItem.setVisible(true);
        aboutItem = menu.findItem(R.id.menu_about);
        aboutItem.setVisible(true);
        closeItem = menu.findItem(R.id.menu_close);
        closeItem.setVisible(false);
        exitItem = menu.findItem(R.id.menu_exit);
        exitItem.setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.menu_settings){
            Intent startSettingsActivityIntent = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivityIntent);
            return true;
        }
        else if(id == R.id.menu_info){
            infoClick(findViewById(R.id.infoView));
            return true;
        }
        else if(id == R.id.menu_about){
            aboutClick(findViewById(R.id.infoView));
            return true;
        }
        else if(id == R.id.menu_close){
            closeClick();
            return true;
        }
        if(id == R.id.menu_exit){
            exitClick(findViewById(R.id.infoView));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void browserClick(View view) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String UriString = sharedPreferences.getString(getString(R.string.url_edit_key),
                getString(R.string.url_edit_default));
        if (!isEmpty(UriString) && URLUtil.isValidUrl(UriString))
        {
            assert UriString != null;
            if(!UriString.startsWith("http://")&& !UriString.startsWith("https://")){
                UriString = "https://"+UriString;
            }
            Uri W3LagerUri = Uri.parse(UriString);
            Intent i = new Intent(Intent.ACTION_VIEW, W3LagerUri);
            i.setPackage("com.android.chrome");
            startActivity(i);
        }
    }

    public void closeClick() {
        WebView wv;
        wv = findViewById(R.id.infoView);
        wv.loadUrl(getString(R.string.url_edit_default));
        wv.setVisibility(View.GONE);
        settingsItem.setVisible(true);
        infoItem.setVisible(true);
        aboutItem.setVisible(true);
        closeItem.setVisible(false);
        exitItem.setVisible(true);
    }

    public void exitClick(View view) {
        finish();
        System.exit(0);
    }

    public void infoClick(View view) {
        WebView wv;
        wv = findViewById(R.id.infoView);
        wv.loadUrl("file:///android_asset/w3lager.htm");
        wv.setVisibility(View.VISIBLE);
        settingsItem.setVisible(false);
        infoItem.setVisible(false);
        aboutItem.setVisible(false);
        exitItem.setVisible(false);
        closeItem.setVisible(true);
}

    public void aboutClick(View view) {
        WebView wv;
        wv = findViewById(R.id.infoView);
        wv.loadUrl("file:///android_asset/help.htm");
        wv.setVisibility(View.VISIBLE);
        settingsItem.setVisible(false);
        infoItem.setVisible(false);
        aboutItem.setVisible(false);
        exitItem.setVisible(false);
        closeItem.setVisible(true);
    }

        final Context context = this;

    private boolean checkApp(String uri, int appVer) {
        PackageInfo pInfo;
        try {
            pInfo = getPackageManager().getPackageInfo(uri, 0);
        } catch (PackageManager.NameNotFoundException e) {
            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.noChrome), Toast.LENGTH_LONG);
            LinearLayout layout = (LinearLayout) toast.getView();
            if (layout.getChildCount() > 0) {
                TextView tv = (TextView) layout.getChildAt(0);
                tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            }
            toast.show();
            finish();
            return false;
        }
        if (pInfo != null) {
            int firstDotIndex = pInfo.versionName.indexOf(".");
            String majorVersion = pInfo.versionName.substring(0, firstDotIndex);
            return Integer.parseInt(majorVersion) > appVer;
        }
        Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.oldChrome), Toast.LENGTH_LONG);
        LinearLayout layout = (LinearLayout) toast.getView();
        if (layout.getChildCount() > 0) {
            TextView tv = (TextView) layout.getChildAt(0);
            tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        }
        toast.show();
        finish();
        return false;
    }
}
