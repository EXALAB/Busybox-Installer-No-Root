package exa.free.bbin;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import android.system.Os;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import exa.free.security.PermissionVerifier;

public class Method1 extends Fragment {

    Context context;
    PermissionVerifier permissionVerifier;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Button button;
    Button button2;
    Button button3;
    TextView textView2;
    InterstitialAd mInterstitialAd;
    int version;
    boolean isUserNotified;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.method1, container, false);

        context = getActivity().getApplicationContext();

        permissionVerifier = new PermissionVerifier(context);

        sharedPreferences = context.getSharedPreferences("GlobalPreferences", 0);

        isUserNotified = sharedPreferences.getBoolean("IsUserNotified", false);

        editor = sharedPreferences.edit();

        version = sharedPreferences.getInt("Version", 0);

        button = view.findViewById(R.id.button);
        button2 = view.findViewById(R.id.button2);
        button3 = view.findViewById(R.id.button3);
        textView2 = view.findViewById(R.id.textView2);

        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId("ca-app-pub-5748356089815497/5774034698");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        textView2.setText("Step 2 : Copy the command to clipboard :\n\n" + "export PATH=$PATH:" + context.getFilesDir());

        int i = Integer.valueOf(getString(R.string.version));

        if(version == 0){
            button.setText("Install");
        }else if(version == i){
            button.setText("Reinstall");
            Toast.makeText(context, "Busybox Installed. Version : " + getString(R.string.version_string), Toast.LENGTH_LONG).show();
        }else if(version < i){
            button.setText("Update");
            Toast.makeText(context, "Busybox Installed. Version : " + getString(R.string.version_string) + " , Update available", Toast.LENGTH_LONG).show();
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mInterstitialAd != null && mInterstitialAd.isLoaded()){
                    mInterstitialAd.show();
                }else{
                    if(button.getText().toString().equalsIgnoreCase("Install")){
                        new Install().execute();
                    }else if(button.getText().toString().equalsIgnoreCase("Reinstall")){
                        new Reinstall().execute();
                    }else if(button.getText().toString().equalsIgnoreCase("Update")){
                        new Update().execute();
                    }
                }
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Command", "export PATH=$PATH:" + context.getFilesDir());
                clipboard.setPrimaryClip(clip);
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = context.getPackageManager().getLaunchIntentForPackage("jackpal.androidterm");
                if(isPackageInstalled("jackpal.androidterm", context.getPackageManager())){
                    startActivity(intent);
                }else{
                    notifyUserForInstallTerminal();
                }
            }
        });

        if(!permissionVerifier.verifyApplication().equals("PASSED")){
            Toast.makeText(context, "Please download genuine version from play store", Toast.LENGTH_LONG).show();
            ActivityCompat.finishAffinity(getActivity());
        }

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
                if(button.getText().toString().equalsIgnoreCase("Install")){
                    new Install().execute();
                }else if(button.getText().toString().equalsIgnoreCase("Reinstall")){
                    new Reinstall().execute();
                }else if(button.getText().toString().equalsIgnoreCase("Update")){
                    new Update().execute();
                }
            }
        });

        if(!isUserNotified){
            showFirstDialog();
        }

        return view;
    }
    private class Install extends AsyncTask<Void, Void, Void> {
        String s;
        final ViewGroup nullParent = null;
        private AlertDialog.Builder builder;
        private AlertDialog alertDialog;
        private ProgressDialog dialog;

        private Install() {
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
                CreateFile("busybox", R.raw.busybox_arm64);
            }else if (s.contains("arm")){
                CreateFile("busybox", R.raw.busybox_arm);
            }else if(s.equals("x86")){
                CreateFile("busybox", R.raw.busybox_x86);
            }else if(s.equals("x86_64")){
                CreateFile("busybox", R.raw.busybox_amd64);
            }else if(s.equals("mips")){
                CreateFile("busybox", R.raw.busybox_mips);
            }else if(s.equals("mips64")){
                CreateFile("busybox", R.raw.busybox_mips64);
            }else{
                Toast.makeText(context, "Sorry, your device is not supported !", Toast.LENGTH_LONG).show();
            }
            if(Build.VERSION.SDK_INT >= 21){
                try{
                    Os.symlink(context.getFilesDir() + "/busybox", "" + context.getFilesDir());
                }catch(Exception e){

                }
            }
            return null;
        }
        protected void onPostExecute(Void result) {
            if(Method1.this.isVisible()){
                if(Build.VERSION.SDK_INT >= 26){
                    this.alertDialog.dismiss();
                }else{
                    this.dialog.dismiss();
                }
            }
            editor.putInt("Version", Integer.valueOf(getString(R.string.version)));
            editor.apply();
            button.setText("REINSTALL");
            notifyUserForInstall();
        }
    }
    private class Update extends AsyncTask<Void, Void, Void> {
        String s;
        final ViewGroup nullParent = null;
        private AlertDialog.Builder builder;
        private AlertDialog alertDialog;
        private ProgressDialog dialog;

        private Update() {
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
                textView.setText(R.string.updating);
            }else{
                this.dialog = new ProgressDialog(getActivity());
                this.dialog.setMessage(getString(R.string.updating));
                this.dialog.setIndeterminate(true);
                this.dialog.setCancelable(false);
                this.dialog.show();
            }
        }

        protected Void doInBackground(Void... params) {
            DeleteBusybox();
            if(Build.VERSION.SDK_INT >= 21){
                s = Build.SUPPORTED_ABIS[0];
            }else{
                s = Build.CPU_ABI;
            }
            if(s.equals("arm64-v8a")){
                CreateFile("busybox", R.raw.busybox_arm64);
            }else if (s.contains("arm")){
                CreateFile("busybox", R.raw.busybox_arm);
            }else if(s.equals("x86")){
                CreateFile("busybox", R.raw.busybox_x86);
            }else if(s.equals("x86_64")){
                CreateFile("busybox", R.raw.busybox_amd64);
            }else if(s.equals("mips")){
                CreateFile("busybox", R.raw.busybox_mips);
            }else if(s.equals("mips64")){
                CreateFile("busybox", R.raw.busybox_mips64);
            }else{
                Toast.makeText(context, "Sorry, your device is not supported !", Toast.LENGTH_LONG).show();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            if(Method1.this.isVisible()){
                if(Build.VERSION.SDK_INT >= 26){
                    this.alertDialog.dismiss();
                }else{
                    this.dialog.dismiss();
                }
            }
            editor.putInt("Version", Integer.valueOf(getString(R.string.version)));
            editor.apply();
            button.setText("REINSTALL");
            notifyUserForUpdate();
        }
    }
    private class Reinstall extends AsyncTask<Void, Void, Void> {
        String s;
        final ViewGroup nullParent = null;
        private AlertDialog.Builder builder;
        private AlertDialog alertDialog;
        private ProgressDialog dialog;

        private Reinstall() {
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
                textView.setText(R.string.reinstalling);
            }else{
                this.dialog = new ProgressDialog(getActivity());
                this.dialog.setMessage(getString(R.string.reinstalling));
                this.dialog.setIndeterminate(true);
                this.dialog.setCancelable(false);
                this.dialog.show();
            }
        }

        protected Void doInBackground(Void... params) {
            DeleteBusybox();
            if(Build.VERSION.SDK_INT >= 21){
                s = Build.SUPPORTED_ABIS[0];
            }else{
                s = Build.CPU_ABI;
            }
            if(s.equals("arm64-v8a")){
                CreateFile("busybox", R.raw.busybox_arm64);
            }else if (s.contains("arm")){
                CreateFile("busybox", R.raw.busybox_arm);
            }else if(s.equals("x86")){
                CreateFile("busybox", R.raw.busybox_x86);
            }else if(s.equals("x86_64")){
                CreateFile("busybox", R.raw.busybox_amd64);
            }else if(s.equals("mips")){
                CreateFile("busybox", R.raw.busybox_mips);
            }else if(s.equals("mips64")){
                CreateFile("busybox", R.raw.busybox_mips64);
            }else{
                Toast.makeText(context, "Sorry, your device is not supported !", Toast.LENGTH_LONG).show();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            if(Method1.this.isVisible()){
                if(Build.VERSION.SDK_INT >= 26){
                    this.alertDialog.dismiss();
                }else{
                    this.dialog.dismiss();
                }
            }
            editor.putInt("Version", Integer.valueOf(getString(R.string.version)));
            editor.apply();
            notifyUserForReinstall();
        }
    }
    private void DeleteBusybox() {
        File file = new File(context.getFilesDir() + "/busybox");
        if(file.exists() && file.isFile()){
            file.delete();
        }
    }
    private void CreateFile(String filename, int resource) {
        if (!context.getFileStreamPath(filename).exists()) {
            try {
                copyFile(getResources().openRawResource(resource), context.openFileOutput(filename, 0));
            } catch (IOException e) {
                Log.e("error", "Failed to copy file: " + filename, e);
            }
            try {
                DataOutputStream os = new DataOutputStream(Runtime.getRuntime().exec("sh").getOutputStream());
                os.writeBytes("chmod 755 " + context.getFilesDir() + "/" + filename + "\n");
                os.writeBytes("exit\n");
                os.flush();
            } catch (IOException e2) {
                Log.e("error", "CreateBoostFile failed to execute");
            }
        }
    }
    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        while (true) {
            int read = in.read(buffer);
            if (read != -1) {
                out.write(buffer, 0, read);
            } else {
                return;
            }
        }
    }
    public void notifyUserForInstall(){
        final ViewGroup nullParent = null;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View view = layoutInflater.inflate(R.layout.notify1, nullParent);
        TextView textView = view.findViewById(R.id.textView);

        alertDialog.setView(view);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
        textView.setText("Busybox Installed\n\n" + "Version : " + getString(R.string.version_string));
    }
    public void notifyUserForUpdate(){
        final ViewGroup nullParent = null;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View view = layoutInflater.inflate(R.layout.notify1, nullParent);
        TextView textView = view.findViewById(R.id.textView);

        alertDialog.setView(view);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
        textView.setText("Busybox Updated\n\n" + "Version : " + getString(R.string.version_string));
    }
    public void notifyUserForReinstall(){
        final ViewGroup nullParent = null;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View view = layoutInflater.inflate(R.layout.notify1, nullParent);
        TextView textView = view.findViewById(R.id.textView);

        alertDialog.setView(view);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
        textView.setText("Busybox Reinstalled\n\n" + "Version : " + getString(R.string.version_string));
    }
    public void notifyUserForInstallTerminal(){
        final ViewGroup nullParent = null;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View view = layoutInflater.inflate(R.layout.notify1, nullParent);
        TextView textView = view.findViewById(R.id.textView);

        alertDialog.setView(view);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Uri uri = Uri.parse("market://details?id=jackpal.androidterm");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                if(Build.VERSION.SDK_INT >= 21){
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                }else{
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                }
                try{
                    startActivity(intent);
                }catch(ActivityNotFoundException e){
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=jackpal.androidterm")));
                }
                dialog.dismiss();
            }
        });
        alertDialog.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
        textView.setText("Terminal Emulator for Android is not installed, do you want to install it now ?");
    }
    private boolean isPackageInstalled(String packagename, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packagename, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
    protected void showFirstDialog(){

        final ViewGroup nullParent = null;

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View view = layoutInflater.inflate(R.layout.first_warning, nullParent);
        CheckBox checkBox = view.findViewById(R.id.checkBox);
        builder.setView(view);
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which){
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("IsUserNotified", true);
                editor.apply();
                isUserNotified = sharedPreferences.getBoolean("IsUserNotified", false);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which){
                getActivity().finish();
                dialog.dismiss();
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                }else{
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                }
            }
        });
    }
}
