package com.example.karosuo.gyrocontrol;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by karosuo on 31/07/16.
 */
public class AddTripFragment extends Fragment {

    private Uri imageCaptureUri;
    private static final int PICK_FROM_CAMERA=1;
    private static final int PICK_FROM_FILE=2;
    private Button btn_choose_image;
    private ImageView mImageView;

    public static final String CURRENT_TRIP_ID = "Current_trip_ID";


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View myView = inflater.inflate(R.layout.add_trip_fragment, container, false);

        /** Put tool tip on the New Trip's name box */
        EditText editText = (EditText) myView.findViewById(R.id.edit_trip_name);
        editText.setHint(R.string.New_trip_text);

        return myView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        /** Configure the dialog and acces to the image sources, for the bk image */
        final String[] items = new String[]{"From Cam","From SD Card"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.select_dialog_item,items);
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle("Select Image");
        builder.setAdapter(adapter, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0){
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File file = new File(Environment.getExternalStorageDirectory(), String.valueOf(System.currentTimeMillis()) + ".jpg");

                    imageCaptureUri = Uri.fromFile(file);

                    try{
                        //intent.putExtra(MediaStore.EXTRA_OUTPUT, imageCaptureUri);
                        //intent.putExtra("return data", true);
                        startActivityForResult(intent, PICK_FROM_CAMERA);
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }
                    dialog.cancel();
                }else if(which == 1){
                    //Intent intent = new Intent();
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    //intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_FILE);
                }
            }
        });

        final AlertDialog dialog = builder.create();
        mImageView = (ImageView) this.getActivity().findViewById(R.id.load_image_view);
        btn_choose_image = (Button) this.getActivity().findViewById(R.id.load_img_button);
        btn_choose_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != this.getActivity().RESULT_OK)
            return;
        Bitmap bitmap = null;
        //String path="";

        imageCaptureUri = data.getData();
        ImageView ivImage = (ImageView) getActivity().findViewById(R.id.load_image_view);
        bitmap = BitmapHelper.decodeSampledBitmapFromUri(imageCaptureUri, ivImage.getHeight(), ivImage.getWidth());
        mImageView.setImageBitmap(bitmap);
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
                EditText editText = (EditText) this.getView().findViewById(R.id.edit_trip_name);
                String tripName = editText.getText().toString();
                if (tripName.isEmpty()){
                    Toast theTripName_str = Toast.makeText(this.getActivity(), getResources().getString(R.string.not_null_name), Toast.LENGTH_LONG);
                    theTripName_str.show();
                }else{
                    MyDBAccess myDB = new MyDBAccess(this.getActivity());
                    Trip myNewTrip = new Trip();
                    ArrayList<Trip> myTempList = new ArrayList<>();

                    myNewTrip.setName(tripName);
                        if (imageCaptureUri == null){
                            myNewTrip.setImgUri("Default");
                        }else{
                            myNewTrip.setImgUri(imageCaptureUri.toString());
                        }
                    myDB.addTrip(myNewTrip);
                    myTempList = myDB.getTripByName(myNewTrip.getName());

                    Intent intent = new Intent(this.getActivity(), ControllerMapActivity.class);
                    intent.putExtra(CURRENT_TRIP_ID, myTempList.get(myTempList.size() - 1).getId());
                    startActivity(intent);
                    this.getActivity().finish();
                }
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