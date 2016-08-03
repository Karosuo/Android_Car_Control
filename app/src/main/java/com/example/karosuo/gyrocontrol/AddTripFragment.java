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

/**
 * Created by karosuo on 31/07/16.
 */
public class AddTripFragment extends Fragment {

    private Uri imageCaptureUri;
    private static final int PICK_FROM_CAMERA=1;
    private static final int PICK_FROM_FILE=2;
    private Button btn_choose_image;
    private ImageView mImageView;


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
                    File file = new File(Environment.getExternalStorageDirectory(), "tmp_avatar" + String.valueOf(System.currentTimeMillis()) + ".jpg");

                    imageCaptureUri = Uri.fromFile(file);

                    try{
                        //intent.putExtra(MediaStore.EXTRA_OUTPUT, imageCaptureUri);
                        //intent.putExtra("return data", true);
                        startActivityForResult(intent, PICK_FROM_CAMERA);
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }
                    dialog.cancel();
                }else{
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
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
        String path="";
        if (requestCode == PICK_FROM_FILE){
            imageCaptureUri = data.getData();
            path = getRealPathFromURI(imageCaptureUri);
            if (path == null)
                path = imageCaptureUri.getPath();
            if (path != null)
                bitmap = BitmapFactory.decodeFile(path);
        }else if(requestCode == PICK_FROM_CAMERA){
            /** Android tut suggestion
             * https://www.youtube.com/watch?v=UiqmekHYCSU*/
            /*path = imageCaptureUri.getPath();
            bitmap = BitmapFactory.decodeFile(path);*/

            /** Tejas Jasani suggestion
             * http://www.theappguruz.com/blog/android-take-photo-camera-gallery-code-sample*/
            //onCaptureImageResult(data, bitmap);

            /** Android Dev page suggestion */
            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("data");
            //ImageView ivImage = (ImageView) getActivity().findViewById(R.id.load_image_view);
            //ivImage.setImageBitmap(imageBitmap);
        }

        mImageView.setImageBitmap(bitmap);
    }
/*
    private void onCaptureImageResult(Intent data, Bitmap thumbnail) {
        ImageView ivImage = (ImageView) getActivity().findViewById(R.id.load_image_view);

        //Bitmap
        thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //ivImage.setImageBitmap(thumbnail);
    }
*/

    public String getRealPathFromURI(Uri contentUri){
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = this.getActivity().getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor==null)
            return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
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
                    myNewTrip.setName(tripName);
                    myDB.addTrip(myNewTrip);
                    Intent intent = new Intent(this.getActivity(), ControllerMapActivity.class);
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