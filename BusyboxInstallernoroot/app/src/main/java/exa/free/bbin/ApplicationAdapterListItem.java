package exa.free.bbin;

import android.graphics.drawable.Drawable;

public class ApplicationAdapterListItem {

    private String name;
    private String packageName;
    private Drawable icon;

    public ApplicationAdapterListItem(String name, String packageName, Drawable icon) {
        this.packageName = packageName;
        this.name = name;
        this.icon = icon;
    }
    public String getPackageName(){
        return packageName;
    }
    public String getName() {
        return name;
    }
    public Drawable getIcon() {
        return icon;
    }
}