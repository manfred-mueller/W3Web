package com.nass.ek.w3web;

import static android.text.TextUtils.isEmpty;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    final Context context = this;
    MenuItem settingsItem;
    MenuItem infoItem;
    MenuItem helpItem;
    MenuItem wifiItem;
    MenuItem clockItem;
    MenuItem keytestItem;
    MenuItem closeItem;
    MenuItem exitItem;
    ImageButton supportButton;
    ImageButton scannerButton;
    String launchPackage;
    public int chromeVer = 68;
    WebView webView;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        setContentView(R.layout.activity_main);
        boolean chromeCheck = checkApps("Chrome", "com.android.chrome");
        boolean foxCheck = checkApps("Firefox", "org.mozilla.firefox");
        boolean scannerCheck = checkApps("Scanner", "com.rscja.scanner");
        boolean quicksupportCheck = checkApps("Quicksupport", "com.teamviewer.quicksupport.market");
        boolean autourlCheck = sharedPreferences.getBoolean("AutoUrlKey", false);
        boolean useFoxCheck = sharedPreferences.getBoolean("UseFoxKey", false);
        TextView editUrlTextView = findViewById(R.id.editUrl);
        TextView browserVersionTextView = findViewById(R.id.browserVersion);
        webView = findViewById(R.id.webView);

        if ((chromeCheck) && (!useFoxCheck)) {
            PackageManager pm = context.getPackageManager();
            String UrlPrefix = getString(R.string.url_prefix);
            String UrlText = sharedPreferences.getString(getString(R.string.url_edit_key), getString(R.string.url_edit_default));
            String editUrlText = UrlPrefix + UrlText;
            editUrlTextView.setText(editUrlText);
            String BrowserPrefix = getString(R.string.chrome_prefix);
            String BrowserVersion = null;
            try {
                BrowserVersion = pm.getPackageInfo("com.android.chrome", 0).versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            String BrowserVersionText = BrowserPrefix + BrowserVersion;
            browserVersionTextView.setText(BrowserVersionText);
            String startCheck = getString(R.string.url_edit_default);
            if (!startCheck.equals(sharedPreferences.getString(getString(R.string.url_edit_key),
                    getString(R.string.url_edit_default))) && !startCheck.isEmpty() && (autourlCheck)) {
                browserClick(findViewById(R.id.webView));
            }
        } else if ((foxCheck) && (useFoxCheck)) {
            PackageManager pm = context.getPackageManager();
            String UrlPrefix = getString(R.string.url_prefix);
            String UrlText = sharedPreferences.getString(getString(R.string.url_edit_key), getString(R.string.url_edit_default));
            String editUrlText = UrlPrefix + UrlText;
            editUrlTextView.setText(editUrlText);
            String BrowserPrefix = getString(R.string.fox_prefix);
            String BrowserVersion = null;
            try {
                BrowserVersion = pm.getPackageInfo("org.mozilla.firefox", 0).versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            String BrowserVersionText = BrowserPrefix + BrowserVersion;
            browserVersionTextView.setText(BrowserVersionText);
            String startCheck = getString(R.string.url_edit_default);
            if (!startCheck.equals(sharedPreferences.getString(getString(R.string.url_edit_key), getString(R.string.url_edit_default))) && !startCheck.isEmpty() && (autourlCheck))
                {
                    browserClick(findViewById(R.id.webView));
                }
        } else {
            browserVersionTextView.setText(R.string.browserWarning);
        }
        if (quicksupportCheck) {
            {
                supportButton = findViewById(R.id.support_button);
                supportButton.setVisibility(View.VISIBLE);
            }
        }
        if (scannerCheck) {
            {
                scannerButton = findViewById(R.id.scanner_button);
                scannerButton.setVisibility(View.VISIBLE);
            }
        }
        setupSharedPreferences();

        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
    }

    private void setupSharedPreferences() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        TextView editUrlTextView = findViewById(R.id.editUrl);
        String UrlPrefix = getString(R.string.url_prefix);
        String UrlText = sharedPreferences.getString(getString(R.string.url_edit_key), getString(R.string.url_edit_default));
        String editUrlText = UrlPrefix + UrlText;
        editUrlTextView.setText(editUrlText);
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
        helpItem = menu.findItem(R.id.menu_help);
        helpItem.setVisible(true);
        wifiItem = menu.findItem(R.id.menu_wireless);
        wifiItem.setVisible(true);
        clockItem = menu.findItem(R.id.menu_date);
        clockItem.setVisible(true);
        keytestItem = menu.findItem(R.id.menu_keytest);
        keytestItem.setVisible(true);
        closeItem = menu.findItem(R.id.menu_close);
        closeItem.setVisible(false);
        exitItem = menu.findItem(R.id.menu_exit);
        exitItem.setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_settings) {
            Intent startSettingsActivityIntent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(startSettingsActivityIntent);
        } else if (id == R.id.menu_wireless) {
            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            return true;
        } else if (id == R.id.menu_date) {
            startActivity(new Intent(Settings.ACTION_DATE_SETTINGS));
            return true;
        } else if (id == R.id.menu_keytest) {
            Intent startKeytestActivityIntent = new Intent(this, KeytestActivity.class);
            startActivity(startKeytestActivityIntent);
        } else if (id == R.id.menu_info) {
            infoClick(findViewById(R.id.webView));
            return true;
        } else if (id == R.id.menu_help) {
            aboutClick(findViewById(R.id.webView));
            return true;
        } else if (id == R.id.menu_close) {
            closeClick(findViewById(R.id.webView));
            return true;
        }
        if (id == R.id.menu_exit) {
            exitClick(findViewById(R.id.webView));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void browserClick(View view) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean useFoxCheck = sharedPreferences.getBoolean("UseFoxKey", false);
        if ((checkApps("Chrome", "com.android.chrome")) && (!useFoxCheck)) {
            String UriString = sharedPreferences.getString(getString(R.string.url_edit_key), getString(R.string.url_edit_default));
            if (!isEmpty(UriString) && URLUtil.isValidUrl(UriString)) {
                if (!UriString.startsWith("http://") && !UriString.startsWith("https://")) {
                    UriString = "https://" + UriString;
                }
                Uri W3LagerUri = Uri.parse(UriString);
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                builder.setShowTitle(false);
                builder.setShareState(CustomTabsIntent.SHARE_STATE_OFF);
                builder.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.intent.setPackage("com.android.chrome");
                customTabsIntent.launchUrl(this, W3LagerUri);
            }
        }
        else if ((checkApps("Firefox", "org.mozilla.firefox")) && (useFoxCheck)) {
            String UriString = sharedPreferences.getString(getString(R.string.url_edit_key), getString(R.string.url_edit_default));
            if (!isEmpty(UriString) && URLUtil.isValidUrl(UriString)) {
                if (!UriString.startsWith("http://") && !UriString.startsWith("https://")) {
                    UriString = "https://" + UriString;
                }
                Uri W3LagerUri = Uri.parse(UriString);
                Intent i = new Intent(Intent.ACTION_VIEW, W3LagerUri);
                i.setPackage("org.mozilla.firefox");
                startActivity(i);
            }
        }
    }

    public void closeClick(View view) {
        webView = findViewById(R.id.webView);
        webView.loadUrl(getString(R.string.url_edit_default));
        webView.setVisibility(View.GONE);
        settingsItem.setVisible(true);
        infoItem.setVisible(true);
        helpItem.setVisible(true);
        wifiItem.setVisible(true);
        clockItem.setVisible(true);
        keytestItem.setVisible(true);
        closeItem.setVisible(false);
        exitItem.setVisible(true);
    }

    public void exitClick(View view) {
        finish();
        System.exit(0);
    }

    public void appClick(String pkgName) {

        switch (pkgName) {
            case "Quicksupport" -> launchPackage = "com.teamviewer.quicksupport.market";
            case "Scanner" -> launchPackage = "com.rscja.scanner";
        }

        Intent t;
        PackageManager manager = getPackageManager();
        try {
            t = manager.getLaunchIntentForPackage(launchPackage);
            if (t == null)
                throw new PackageManager.NameNotFoundException();
            t.addCategory(Intent.CATEGORY_LAUNCHER);
            startActivity(t);
        } catch (PackageManager.NameNotFoundException ignored) {

        }
    }

    public void scannerClick(View view) {
        appClick("Scanner");
    }

    public void supportClick(View view) {
        appClick("Quicksupport");
    }

    public void infoClick(View view) {
        WebView infoView = findViewById(R.id.webView);
        infoView.loadUrl("file:///android_asset/presentation.htm");
        infoView.setVisibility(View.VISIBLE);
        settingsItem.setVisible(false);
        infoItem.setVisible(false);
        helpItem.setVisible(false);
        exitItem.setVisible(false);
        wifiItem.setVisible(false);
        clockItem.setVisible(false);
        keytestItem.setVisible(false);
        closeItem.setVisible(true);
    }

    public void aboutClick(View view) {
        WebView helpView = findViewById(R.id.webView);
        helpView.loadUrl("file:///android_asset/help.htm");
        helpView.setVisibility(View.VISIBLE);
        settingsItem.setVisible(false);
        infoItem.setVisible(false);
        helpItem.setVisible(false);
        exitItem.setVisible(false);
        wifiItem.setVisible(false);
        clockItem.setVisible(false);
        keytestItem.setVisible(false);
        closeItem.setVisible(true);
    }

    private boolean checkApps(String pkgName, String uri) {
        PackageInfo pkgInfo;
        try {
            pkgInfo = getPackageManager().getPackageInfo(uri, 0);
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
        if (pkgInfo != null) {
            if (!pkgName.equals("Chrome")) {
                return true;
            } else {
                String[] split = pkgInfo.versionName.split(Pattern.quote("."));
                String majorVersion = split[0];
                return Integer.parseInt(majorVersion) >= chromeVer;
            }
        }
        return false;
    }
}
