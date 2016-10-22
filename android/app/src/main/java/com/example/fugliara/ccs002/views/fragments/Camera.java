package com.example.fugliara.ccs002.views.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.fugliara.ccs002.server.AzureResponse;
import com.example.fugliara.ccs002.server.AzureService;
import com.example.fugliara.ccs002.server.blob.AzureBlob;
import com.example.fugliara.ccs002.server.blob.AzureBlobResult;
import com.example.fugliara.ccs002.server.dataobjects.ImageItem;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;

import java.io.File;
import java.io.IOException;

public class Camera {
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String TAG = "TAG_Camera";
    public Uri mPhotoFileUri = null;
    public File mPhotoFile = null;

    private AppCompatActivity activity;
    private String tempPhoto;

    public Camera(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            try {
                mPhotoFile = createImageFile();

                if (mPhotoFile != null) {
                    mPhotoFileUri = Uri.fromFile(mPhotoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoFileUri);
                    activity.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            } catch (IOException ex) {
                Log.d(TAG, "dispatchTakePictureIntent()");
            }
        }
    }

    private File createImageFile() throws IOException {
        String imageFileName = "tempPhoto";
        File storageDir = Environment.getExternalStorageDirectory();
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        tempPhoto = image.getName();
        return image;
    }

    public void onCameraResult(int requestCode, int resultCode, final String imgType, final CameraPosResult posResult) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == activity.RESULT_OK) {

            AzureService.getInstance().getImageController().getImageUrl(new AzureResponse<ImageItem>() {
                @Override
                public void receive(MobileServiceList<ImageItem> result) {
                    File storageDir = Environment.getExternalStorageDirectory();
                    File from = new File(storageDir, tempPhoto);
                    final File to = new File(storageDir, result.get(0).id.toString() + ".jpg");
                    from.renameTo(to);

                    final ImageItem img = result.get(0);

                    AzureBlob blob = new AzureBlob("upload", "image" + imgType, to.getAbsolutePath(), new AzureBlobResult() {
                        @Override
                        public void success() {
                            posResult.action(to, img);
                        }

                        @Override
                        public void fail() {
                            Log.d(TAG, "uploadBlob()");
                            posResult.fail();
                        }
                    });
                    blob.execute();
                }

                @Override
                public void fail() {
                    Log.d(TAG, "getImageUrl()");
                    posResult.fail();
                }
            }, imgType);
        }
    }
}
