package fr.sio.ecp.federatedbirds.app.users;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

import fr.sio.ecp.federatedbirds.ApiClient;
import fr.sio.ecp.federatedbirds.R;
import fr.sio.ecp.federatedbirds.app.main.MainActivity;
import fr.sio.ecp.federatedbirds.auth.TokenManager;

/**
 * Created by bensoussan on 08/02/2016.
 */
public class AvatarUploadActivity extends AppCompatActivity {


    private static final int RESULT_LOAD_IMG = 1;
    private String imgDecodableString;

    //Intent galleryIntent;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.upload_avatar_activity);

        findViewById(R.id.id_gallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //if (Build.VERSION.SDK_INT < 19){
                   // galleryIntent = new Intent(Intent.ACTION_PICK,  MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    //galleryIntent.setType("*/*");

              //  } else {
                //    galleryIntent = new Intent(Intent.ACTION_PICK,  MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                  //  galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
                   // galleryIntent.setType("*/*");
                //}
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                        //new Intent(Intent.ACTION_PICK,
                        //android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);

                startActivityForResult(Intent.createChooser(galleryIntent, "Selectionner une image"), RESULT_LOAD_IMG);
            }
        });

        findViewById(R.id.id_upload_picture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTaskCompat.executeParallel(
                        new UploadPictureTask(imgDecodableString)
                );
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //try {
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && null != data) {

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                ImageView imgView = (ImageView) findViewById(R.id.new_avatar);

                //BitmapFactory.decodeFile(imgDecodableString)
                imgView.setImageBitmap(myDecodeImage(imgDecodableString));

                // A l'execution, il a un problème à la selection de l'image dans la gallerie..
                // l'image n'est pas charger dans la vue.
                // Je n'ai pas trouvé pourquoi, apparement c'est un problème de fuite mémoire.

            } else {
                Toast.makeText(this, R.string.choose_pict, Toast.LENGTH_LONG).show();
            }
        //} catch (Exception e) {
          //  Toast.makeText(this, R.string.upload_failed, Toast.LENGTH_LONG).show();
        //}

    }


    public Bitmap myDecodeImage(String filePath) {
        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 1024;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;

        return BitmapFactory.decodeFile(filePath, o2);


    }



    private class UploadPictureTask extends AsyncTask<Void, Void, String>{

        private String imageString;

        public UploadPictureTask(String imgString) {
            super();
            imageString = imgString;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                if (imageString != null) {
                    String urlToPost = ApiClient.getInstance(getApplicationContext()).getUrlToPost();
                    return ApiClient.getInstance(getApplicationContext()).uploadAvatar(imageString, urlToPost);

                } else {
                    Toast.makeText(getApplicationContext(), R.string.choose_pict, Toast.LENGTH_SHORT).show();
                    return null;
                }

            } catch (IOException e) {
                Log.e(AvatarUploadActivity.class.getSimpleName(), "Upload échoué", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String token) {
            if (token != null) {
                Toast.makeText(getApplicationContext(), R.string.upload_succeed, Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(getApplicationContext(), R.string.upload_failed, Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }


}
