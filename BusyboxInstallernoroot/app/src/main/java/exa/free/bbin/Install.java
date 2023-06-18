package exa.free.bbin;

import static android.content.Context.DOWNLOAD_SERVICE;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import exa.free.interfaces.AppSelector;

public class Install extends Fragment implements AppSelector {

    Context context;
    List<ApplicationAdapterListItem> applicationAdapterListItems;
    TerminalChooserAdapter terminalChooserAdapter;
    SharedPreferences sharedPreferences;
    ListView listView;
    AlertDialog.Builder alertDialog;
    AlertDialog alert;
    Button button;
    Button button2;
    Button button3;
    Button button4;
    Button button5;
    TextView textView3;
    TextView textView5;
    ProgressDialog mProgressDialog;
    String s;
    String s2;
    InterstitialAd mInterstitialAd;
    protected static final String bbarm64 = "https://github.com/EXALAB/Busybox-static/raw/main/busybox_arm64";
    protected static final String bbarm = "https://github.com/EXALAB/Busybox-static/raw/main/busybox_arm";
    protected static final String bbamd64 = "https://github.com/EXALAB/Busybox-static/raw/main/busybox_amd64";
    protected static final String bbx86 = "https://github.com/EXALAB/Busybox-static/raw/main/busybox_x86";
    protected static final String bbmips64 = "https://github.com/EXALAB/Busybox-static/raw/main/busybox_mips64";
    protected static final String bbmips = "https://github.com/EXALAB/Busybox-static/raw/main/busybox_mips";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.install, container, false);

        context = getActivity().getApplicationContext();

        loadAd();

        sharedPreferences = context.getSharedPreferences("GlobalPreferences", 0);
        s = sharedPreferences.getString("ChoosenTerminal", "None");

        button = view.findViewById(R.id.button);
        button2 = view.findViewById(R.id.button2);
        button3 = view.findViewById(R.id.button3);
        button4 = view.findViewById(R.id.button4);
        button5 = view.findViewById(R.id.button5);
        textView3 = view.findViewById(R.id.textView3);
        textView5 = view.findViewById(R.id.textView5);

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("Connecting...");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(false);

        if(Build.VERSION.SDK_INT >= 21){
            s2 = Build.SUPPORTED_ABIS[0];
        }else{
            s2 = Build.CPU_ABI;
        }

        if(s.equals("None")){
            textView3.setText("Step 3 : Please Choose a Terminal Emulator App first");
            textView5.setText("Step 5 : Please Choose a Terminal Emulator App first");
            button3.setEnabled(false);
            button5.setEnabled(false);
        }else{
            if(s2.equals("arm64-v8a")){
                textView3.setText("Step 3 : Copy the command to clipboard :\n\n" + "cd /data/data/" + s + " && cp " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/busyboxinstallernoroot/busybox " + "/data/data/" + s + " && chmod 755 busybox");
                textView5.setText("Step 5 : Copy the command to clipboard :\n\n" + "cd /data/data" + s);
            }else if (s2.contains("arm")){
                textView3.setText("Step 3 : Copy the command to clipboard :\n\n" + "cd /data/data/" + s + " && cp " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/busyboxinstallernoroot/busybox " + "/data/data/" + s + " && chmod 755 busybox");
                textView5.setText("Step 5 : Copy the command to clipboard :\n\n" + "cd /data/data/" + s);
            }else if(s2.equals("x86")){
                textView3.setText("Step 3 : Copy the command to clipboard :\n\n" + "cd /data/data/" + s + " && cp " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/busyboxinstallernoroot/busybox " + "/data/data/" + s + " && chmod 755 busybox");
                textView5.setText("Step 5 : Copy the command to clipboard :\n\n" + "cd /data/data/" + s);
            }else if(s2.equals("x86_64")){
                textView3.setText("Step 3 : Copy the command to clipboard :\n\n" + "cd /data/data/" + s + " && cp " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/busyboxinstallernoroot/busybox " + "/data/data/" + s + " && chmod 755 busybox");
                textView5.setText("Step 5 : Copy the command to clipboard :\n\n" + "cd /data/data/" + s);
            }else if(s2.equals("mips")){
                textView3.setText("Step 3 : Copy the command to clipboard :\n\n" + "cd /data/data/" + s + " && cp " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/busyboxinstallernoroot/busybox " + "/data/data/" + s + " && chmod 755 busybox");
                textView5.setText("Step 5 : Copy the command to clipboard :\n\n" + "cd /data/data/" + s);
            }else if(s2.equals("mips64")){
                textView3.setText("Step 3 : Copy the command to clipboard :\n\n" + "cd /data/data/" + s + " && cp " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/busyboxinstallernoroot/busybox " + "/data/data/" + s + " && chmod 755 busybox");
                textView5.setText("Step 5 : Copy the command to clipboard :\n\n" + "cd /data/data/" + s);
            }
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new InstallBusybox().execute();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAppsDialog();
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
                if(s2.equals("arm64-v8a")){
                    ClipData clip = ClipData.newPlainText("Command", "cd /data/data/" + s + " && cp " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/busyboxinstallernoroot/busybox " + "/data/data/" + s + " && chmod 755 busybox");
                    clipboard.setPrimaryClip(clip);
                }else if (s2.contains("arm")){
                    ClipData clip = ClipData.newPlainText("Command", "cd /data/data/" + s + " && cp " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/busyboxinstallernoroot/busybox " + "/data/data/" + s + " && chmod 755 busybox");
                    clipboard.setPrimaryClip(clip);
                }else if(s2.equals("x86")){
                    ClipData clip = ClipData.newPlainText("Command", "cd /data/data/" + s + " && cp " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/busyboxinstallernoroot/busybox " + "/data/data/" + s + " && chmod 755 busybox");
                    clipboard.setPrimaryClip(clip);
                }else if(s2.equals("x86_64")){
                    ClipData clip = ClipData.newPlainText("Command", "cd /data/data/" + s + " && cp " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/busyboxinstallernoroot/busybox " + "/data/data/" + s + " && chmod 755 busybox");
                    clipboard.setPrimaryClip(clip);
                }else if(s2.equals("mips")){
                    ClipData clip = ClipData.newPlainText("Command", "cd /data/data/" + s + " && cp " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/busyboxinstallernoroot/busybox " + "/data/data/" + s + " && chmod 755 busybox");
                    clipboard.setPrimaryClip(clip);
                }else if(s2.equals("mips64")){
                    ClipData clip = ClipData.newPlainText("Command", "cd /data/data/" + s + " && cp " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/busyboxinstallernoroot/busybox " + "/data/data/" + s + " &&  chmod 755 busybox");
                    clipboard.setPrimaryClip(clip);
                }
                if(mInterstitialAd != null){
                    mInterstitialAd.show(getActivity());
                }
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s = sharedPreferences.getString("ChoosenTerminal", "None");
                Intent intent = context.getPackageManager().getLaunchIntentForPackage(s);
                if(isPackageInstalled(s, context.getPackageManager())){
                    startActivity(intent);
                }else{
                    Toast.makeText(context, "Oops, looks like the application has been uninstalled or hidden, please reinstall/enable it, or choose another Terminal Emulator App", Toast.LENGTH_LONG).show();
                }
            }
        });
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
                if(s2.equals("arm64-v8a")){
                    ClipData clip = ClipData.newPlainText("Command", "cd /data/data/" + s);
                    clipboard.setPrimaryClip(clip);
                }else if (s2.contains("arm")){
                    ClipData clip = ClipData.newPlainText("Command", "cd /data/data/" + s);
                    clipboard.setPrimaryClip(clip);
                }else if(s2.equals("x86")){
                    ClipData clip = ClipData.newPlainText("Command", "cd /data/data/" + s);
                    clipboard.setPrimaryClip(clip);
                }else if(s2.equals("x86_64")){
                    ClipData clip = ClipData.newPlainText("Command", "cd /data/data/" + s);
                    clipboard.setPrimaryClip(clip);
                }else if(s2.equals("mips")){
                    ClipData clip = ClipData.newPlainText("Command", "cd /data/data/" + s);
                    clipboard.setPrimaryClip(clip);
                }else if(s2.equals("mips64")){
                    ClipData clip = ClipData.newPlainText("Command", "cd /data/data/" + s);
                    clipboard.setPrimaryClip(clip);
                }
            }
        });

        return view;
    }
    public void loadAd(){
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(context,"ca-app-pub-5748356089815497/5836001022", adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                exa.free.bbin.Install.this.mInterstitialAd = interstitialAd;
                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Called when fullscreen content is dismissed.
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        // Called when fullscreen content failed to show.
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        // Called when fullscreen content is shown.
                        // Make sure to set your reference to null so you don't
                        // show it a second time.
                        mInterstitialAd = null;
                    }
                });
            }
        });
    }
    @Override
    public void selectApp(final String packageName){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("ChoosenTerminal", packageName);
        editor.apply();
        s = sharedPreferences.getString("ChoosenTerminal", "None");
        if(s2.equals("arm64-v8a")){
            textView3.setText("Step 3 : Copy the command to clipboard :\n\n" + "cd /data/data/" + s + " && cp " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/busyboxinstallernoroot/busybox " + "/data/data/" + s + " && chmod 755 busybox");
            textView5.setText("Step 5 : Copy the command to clipboard :\n\n" + "cd /data/data" + s);
        }else if (s2.contains("arm")){
            textView3.setText("Step 3 : Copy the command to clipboard :\n\n" + "cd /data/data/" + s + " && cp " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/busyboxinstallernoroot/busybox " + "/data/data/" + s + " && chmod 755 busybox");
            textView5.setText("Step 5 : Copy the command to clipboard :\n\n" + "cd /data/data/" + s);
        }else if(s2.equals("x86")){
            textView3.setText("Step 3 : Copy the command to clipboard :\n\n" + "cd /data/data/" + s + " && cp " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/busyboxinstallernoroot/busybox " + "/data/data/" + s + " && chmod 755 busybox");
            textView5.setText("Step 5 : Copy the command to clipboard :\n\n" + "cd /data/data/" + s);
        }else if(s2.equals("x86_64")){
            textView3.setText("Step 3 : Copy the command to clipboard :\n\n" + "cd /data/data/" + s + " && cp " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/busyboxinstallernoroot/busybox " + "/data/data/" + s + " && chmod 755 busybox");
            textView5.setText("Step 5 : Copy the command to clipboard :\n\n" + "cd /data/data/" + s);
        }else if(s2.equals("mips")){
            textView3.setText("Step 3 : Copy the command to clipboard :\n\n" + "cd /data/data/" + s + " && cp " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/busyboxinstallernoroot/busybox " + "/data/data/" + s + " && chmod 755 busybox");
            textView5.setText("Step 5 : Copy the command to clipboard :\n\n" + "cd /data/data/" + s);
        }else if(s2.equals("mips64")){
            textView3.setText("Step 3 : Copy the command to clipboard :\n\n" + "cd /data/data/" + s + " && cp " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/busyboxinstallernoroot/busybox " + "/data/data/" + s + " && chmod 755 busybox");
            textView5.setText("Step 5 : Copy the command to clipboard :\n\n" + "cd /data/data/" + s);
        }
        button3.setEnabled(true);
        button5.setEnabled(true);
        alert.dismiss();
    }
    @Override
    public void removeApp(String packageName){
    }
    @Override
    public boolean isSelected(String packageName){
        return false;
    }
    public void showAppsDialog(){
        final ViewGroup nullParent = null;
        alertDialog = new AlertDialog.Builder(getActivity());
        alert = alertDialog.create();
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View view = layoutInflater.inflate(R.layout.notify2, nullParent);
        listView = view.findViewById(R.id.listView);

        alert.setView(view);
        alert.show();
        new InitializeApps().execute();
    }
    private class InstallBusybox extends AsyncTask<Void, Void, Void> {
        String s;
        final ViewGroup nullParent = null;
        private AlertDialog.Builder builder;
        private AlertDialog alertDialog;
        private ProgressDialog dialog;

        private InstallBusybox() {
            this.builder = null;
            this.alertDialog = null;
            this.dialog = null;
        }

        protected void onPreExecute() {
            if(Build.VERSION.SDK_INT >= 26){
                this.builder = new AlertDialog.Builder(getActivity());
                this.alertDialog = builder.create();
                LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                View view = layoutInflater.inflate(R.layout.progress_bar, nullParent);
                this.alertDialog.setView(view);
                this.alertDialog.setCancelable(false);
                this.alertDialog.show();
                TextView textView = view.findViewById(R.id.textView);
                textView.setText(R.string.installing);
            }else{
                this.dialog = new ProgressDialog(getActivity());
                this.dialog.setMessage(getString(R.string.installing));
                this.dialog.setIndeterminate(true);
                this.dialog.setCancelable(false);
                this.dialog.show();
            }
        }

        protected Void doInBackground(Void... params) {
            if(Build.VERSION.SDK_INT >= 21){
                s = Build.SUPPORTED_ABIS[0];
            }else{
                s = Build.CPU_ABI;
            }
            if(s.equals("arm64-v8a")){
                DownloadBusyBox(bbarm64);
            }else if (s.contains("arm")){
                DownloadBusyBox(bbarm);
            }else if(s.equals("x86")){
                DownloadBusyBox(bbx86);
            }else if(s.equals("x86_64")){
                DownloadBusyBox(bbamd64);
            }else if(s.equals("mips")){
                DownloadBusyBox(bbmips);
            }else if(s.equals("mips64")){
                DownloadBusyBox(bbmips64);
            }else{
                Toast.makeText(context, "Sorry, your device is not supported !", Toast.LENGTH_LONG).show();
            }
            return null;
        }
        protected void onPostExecute(Void result) {
            if(exa.free.bbin.Install.this.isVisible()){
                if(Build.VERSION.SDK_INT >= 26){
                    this.alertDialog.dismiss();
                }else{
                    this.dialog.dismiss();
                }
            }
        }
    }
    private class InitializeApps extends AsyncTask<Void, Void, Void> {
        final ViewGroup nullParent = null;
        private AlertDialog.Builder builder;
        private AlertDialog alertDialog;
        private ProgressDialog dialog;
        private boolean shouldShowSystemApps;

        private InitializeApps() {
            this.builder = null;
            this.alertDialog = null;
            this.dialog = null;
        }

        protected void onPreExecute() {
            if(Build.VERSION.SDK_INT >= 26){
                this.builder = new AlertDialog.Builder(getActivity());
                this.alertDialog = builder.create();
                LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                View view = layoutInflater.inflate(R.layout.progress_bar, nullParent);
                this.alertDialog.setView(view);
                this.alertDialog.setCancelable(false);
                this.alertDialog.show();
                TextView textView = view.findViewById(R.id.textView);
                textView.setText("Working");
                shouldShowSystemApps = sharedPreferences.getBoolean("ShouldShowSystemApps", false);
            }else{
                this.dialog = new ProgressDialog(getActivity());
                this.dialog.setMessage("Working");
                this.dialog.setIndeterminate(true);
                this.dialog.setCancelable(false);
                this.dialog.show();
            }
        }
        protected Void doInBackground(Void... params) {
            if(shouldShowSystemApps){
                List<ApplicationAdapterListItem> applicationAdapterListItems = getAllApps();
                terminalChooserAdapter = new TerminalChooserAdapter(context, exa.free.bbin.Install.this, applicationAdapterListItems);
            }else{
                List<ApplicationAdapterListItem> applicationAdapterListItems = getUserApps();
                terminalChooserAdapter = new TerminalChooserAdapter(context, exa.free.bbin.Install.this, applicationAdapterListItems);
            }
            return null;
        }
        protected void onPostExecute(Void result) {
            if(exa.free.bbin.Install.this.isVisible()){
                if(Build.VERSION.SDK_INT >= 26){
                    this.alertDialog.dismiss();
                }else{
                    this.dialog.dismiss();
                }
                listView.setAdapter(terminalChooserAdapter);
            }
        }
    }
    private List<ApplicationAdapterListItem> getUserApps(){
        applicationAdapterListItems = new ArrayList<>();
        List<PackageInfo> packageInfos = context.getPackageManager().getInstalledPackages(0);
        final PackageItemInfo.DisplayNameComparator comparator = new PackageItemInfo.DisplayNameComparator(context.getPackageManager());
        Collections.sort(packageInfos, new Comparator<PackageInfo>() {
            @Override
            public int compare(PackageInfo one, PackageInfo two) {
                return comparator.compare(one.applicationInfo, two.applicationInfo);
            }
        });
        for(int i = 0; i < packageInfos.size(); i++){
            PackageInfo packageInfo = packageInfos.get(i);
            if(!isSystemPackage(packageInfo)){
                if(isApplicationExistOnLauncher(packageInfo.applicationInfo.packageName)){
                    String appName = packageInfo.applicationInfo.loadLabel(context.getPackageManager()).toString();
                    String packageName = packageInfo.applicationInfo.packageName;
                    Drawable icon = packageInfo.applicationInfo.loadIcon(context.getPackageManager());
                    applicationAdapterListItems.add(new ApplicationAdapterListItem(appName, packageName, icon));
                }
            }
        }
        return applicationAdapterListItems;
    }
    private List<ApplicationAdapterListItem> getAllApps(){
        applicationAdapterListItems = new ArrayList<>();
        List<PackageInfo> packageInfos = context.getPackageManager().getInstalledPackages(0);
        final PackageItemInfo.DisplayNameComparator comparator = new PackageItemInfo.DisplayNameComparator(context.getPackageManager());
        Collections.sort(packageInfos, new Comparator<PackageInfo>() {
            @Override
            public int compare(PackageInfo one, PackageInfo two) {
                return comparator.compare(one.applicationInfo, two.applicationInfo);
            }
        });
        for(int i = 0; i < packageInfos.size(); i++){
            PackageInfo packageInfo = packageInfos.get(i);
            if(isApplicationExistOnLauncher(packageInfo.applicationInfo.packageName)){
                String appName = packageInfo.applicationInfo.loadLabel(context.getPackageManager()).toString();
                String packageName = packageInfo.applicationInfo.packageName;
                Drawable icon = packageInfo.applicationInfo.loadIcon(context.getPackageManager());
                applicationAdapterListItems.add(new ApplicationAdapterListItem(appName, packageName, icon));
            }
        }
        return applicationAdapterListItems;
    }
    private boolean isSystemPackage(PackageInfo packageInfo) {
        return (packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
    }
    private boolean isApplicationExistOnLauncher(String packageName) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resolveInfos = context.getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resolveInfos) {
            if(packageName.equals(resolveInfo.activityInfo.packageName)){
                return true;
            }
        }
        return false;
    }
    private boolean isPackageInstalled(String packagename, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packagename, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
    private void saveData(){

        String csv_data = context.getExternalFilesDir(null) + "/busybox";/// your csv data as string;
        File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        //if you want to create a sub-dir

        // select the name for your file
        root = new File(root , "busybox");

        try {
            FileOutputStream fout = new FileOutputStream(root);
            fout.write(csv_data.getBytes());

            fout.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();

            boolean bool = false;
            try {
                // try to create the file
                bool = root.createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            if (bool){
                // call the method again
                saveData();
            }else {
                throw new IllegalStateException("Failed to create image file");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void DownloadBusyBox(String url){
        DownloadManager downloadManager = (DownloadManager)context.getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setTitle("Busybox Installer (no root)");
        request.setDescription("Downloading Busybox");
        request.setAllowedOverRoaming(false);
        request.setDestinationUri(Uri.fromFile(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/busyboxinstallernoroot/", "busybox")));
        downloadManager.enqueue(request);
    }
}