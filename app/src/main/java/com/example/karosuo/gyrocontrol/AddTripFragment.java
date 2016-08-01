package com.example.karosuo.gyrocontrol;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * Created by karosuo on 31/07/16.
 */
public class AddTripFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View myView = inflater.inflate(R.layout.add_trip_fragment, container, false);
        EditText editText = (EditText) myView.findViewById(R.id.edit_trip_name);
        editText.setHint(R.string.New_trip_text);
        return myView;
    }

    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.add_cancel_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.begin_new_trip:
                //MyDBAccess myDB = new MyDBAccess(this.getActivity());
                Intent intent = new Intent(this.getActivity(), ControllerMapActivity.class);
                startActivity(intent);
                this.getActivity().finish();
                break;
            case R.id.cancel_new_trip:
                this.getActivity().finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}


/** In order to hide some items from the menu*/
/*
if (mMenu != null) {
   mMenu.findItem(R.id.edit_item).setVisible(false);
}
*/

/** In order to remove the item(s) from menu */
/*
if (mMenu != null) {
   mMenu.removeItem(R.id.edit_item);
}
* */