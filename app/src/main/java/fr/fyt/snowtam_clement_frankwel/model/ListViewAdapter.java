package fr.fyt.snowtam_clement_frankwel.model;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import java.util.HashMap;
import java.util.List;

/**
 * Created by frank on 14/11/2017.
 * this class will be used to communique with ListView
 */

public class ListViewAdapter extends ArrayAdapter<String> {

    HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

    public ListViewAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
        for (int i = 0; i < objects.size(); i++) {
            mIdMap.put(objects.get(i), i);
        }
    }
    @Override
    public long getItemId(int position){
        String item = getItem(position);
        return mIdMap.get(item);
    }

    @Override
    public boolean hasStableIds(){
        return true;
    }
}
