package exa.free.interfaces;

public interface AppSelector {
    void selectApp(String packageName);
    void removeApp(String packageName);
    boolean isSelected(String packageName);
}