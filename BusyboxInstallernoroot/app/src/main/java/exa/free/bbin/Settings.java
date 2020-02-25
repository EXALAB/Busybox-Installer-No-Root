package exa.free.bbin;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class Settings extends Fragment {

    Context context;
    SharedPreferences sharedPreferences;
    CheckBox checkBox;
    boolean shouldShowSystemApps;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.settings, container, false);

        context = getActivity().getApplicationContext();
        checkBox = view.findViewById(R.id.checkBox);

        sharedPreferences = context.getSharedPreferences("GlobalPreferences", 0);
        shouldShowSystemApps = sharedPreferences.getBoolean("ShouldShowSystemApps", false);

        if(shouldShowSystemApps){
            checkBox.setChecked(true);
        }else{
            checkBox.setChecked(false);
        }

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("ShouldShowSystemApps", true);
                    editor.apply();
                }else{
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("ShouldShowSystemApps", false);
                    editor.apply();
                }
            }
        });
        return view;
    }
}
