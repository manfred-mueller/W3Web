package com.manfredmuellermuelheim.w3lager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;

import static android.text.TextUtils.isEmpty;

    public class MainActivity extends AppCompatActivity {

    MenuItem settingsItem;
    MenuItem infoItem;
    MenuItem aboutItem;
    MenuItem closeItem;
    MenuItem exitItem;
    ImageButton supportButton;
    public int chromeVer = 68;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        boolean chromeCheck = checkApps("Chrome","com.android.chrome");
        boolean quicksupportCheck = checkApps("Quicksupport", "com.teamviewer.quicksupport.market");
        TextView chromeVersionTextView = findViewById(R.id.chromeVersion);

        if(chromeCheck) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            PackageManager pm = context.getPackageManager();
            String UrlPrefix = getString(R.string.url_prefix);
            String UrlText = sharedPreferences.getString(getString(R.string.url_edit_key), getString(R.string.url_edit_default));
            String ChromePrefix = getString(R.string.chrome_prefix);
            String ChromeVersion = null;
            try {
                ChromeVersion = pm.getPackageInfo("com.android.chrome", 0).versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            String ChromeVersionText = UrlPrefix + UrlText + ChromePrefix + ChromeVersion;
            chromeVersionTextView.setText(ChromeVersionText);
            String startCheck = getString(R.string.url_edit_default);
            if (!startCheck.equals(sharedPreferences.getString(getString(R.string.url_edit_key),
                    getString(R.string.url_edit_default))) && !startCheck.isEmpty())
            {
                browserClick(findViewById(R.id.infoView));
            }
        }
        else {
            chromeVersionTextView.setText(R.string.chromeWarning);
        }
        if (quicksupportCheck) {
            {
                supportButton = findViewById(R.id.support_button);
                supportButton.setVisibility(View.VISIBLE);
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
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

        public void supportClick(View view) {
            Intent t;
            PackageManager manager = getPackageManager();
            try {
                t = manager.getLaunchIntentForPackage("com.teamviewer.quicksupport.market");
                if (t == null)
                    throw new PackageManager.NameNotFoundException();
                t.addCategory(Intent.CATEGORY_LAUNCHER);
                startActivity(t);
            } catch (PackageManager.NameNotFoundException e) {
                return;
            }
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

        private boolean checkApps(String pkgName, String uri) {
            PackageInfo pkgInfo;
            try {
                pkgInfo = getPackageManager().getPackageInfo(uri, 0);
            } catch (PackageManager.NameNotFoundException e) {
                return false;
            }
            if (pkgInfo != null) {
                int firstDotIndex = pkgInfo.versionName.indexOf(".");
                String majorVersion = pkgInfo.versionName.substring(0, firstDotIndex);
                if (pkgName.equals("Quicksupport")) {
                    return true;
                } else {
                    return Integer.parseInt(majorVersion) > chromeVer;
                }
            }
            return false;
        }
}
