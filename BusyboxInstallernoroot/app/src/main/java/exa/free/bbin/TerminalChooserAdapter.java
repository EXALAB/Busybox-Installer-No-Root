package exa.free.bbin;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import exa.free.interfaces.AppSelector;

class TerminalChooserAdapter extends BaseAdapter{

    private List<ApplicationAdapterListItem> applicationAdapterListItems;
    private AppSelector appSelector;

    TerminalChooserAdapter(Context context, AppSelector appSelector, List<ApplicationAdapterListItem> applicationAdapterListItems) {
        this.appSelector = appSelector;
        this.applicationAdapterListItems = applicationAdapterListItems;
    }

    @Override
    public int getCount() {
        return applicationAdapterListItems.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder listViewHolder;
        if (convertView == null) {
            listViewHolder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_list, parent, false);
        } else {
            listViewHolder = (ViewHolder) convertView.getTag();
        }
        listViewHolder.textView = convertView.findViewById(R.id.textView);
        listViewHolder.textView2 = convertView.findViewById(R.id.textView2);
        listViewHolder.imageView = convertView.findViewById(R.id.imageView);
        listViewHolder.button = convertView.findViewById(R.id.button);
        convertView.setTag(listViewHolder);

        listViewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appSelector.selectApp(applicationAdapterListItems.get(position).getPackageName());
                notifyDataSetChanged();
            }
        });

        listViewHolder.textView.setText(applicationAdapterListItems.get(position).getName());
        listViewHolder.textView2.setText(applicationAdapterListItems.get(position).getPackageName());
        listViewHolder.imageView.setImageDrawable(applicationAdapterListItems.get(position).getIcon());

        return convertView;
    }
    private static class ViewHolder {
        private TextView textView;
        private TextView textView2;
        private ImageView imageView;
        private Button button;
    }
}
