package com.fit_with_friends.fitWithFriends.tasks;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import com.fit_with_friends.App;
import com.fit_with_friends.common.concurrency.BaseAsyncTask;
import com.fit_with_friends.fitWithFriends.dialogs.ProgressDialogFragment;
import com.fit_with_friends.fitWithFriends.utils.image.ImageUtils;
import com.fit_with_friends.fitWithFriends.utils.image.SchemeType;
import com.fit_with_friends.fitWithFriends.listeners.OnImagePickedListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.WeakReference;

public class GetFilepathFromUriTask extends BaseAsyncTask<Intent, Void, File> {

    private WeakReference<FragmentManager> fmWeakReference;
    private OnImagePickedListener listener;
    private int requestCode;

    public GetFilepathFromUriTask(FragmentManager fragmentManager, OnImagePickedListener listener, int requestCode) {
        this.fmWeakReference = new WeakReference<>(fragmentManager);
        this.listener = listener;
        this.requestCode = requestCode;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        showProgress();
    }

    @Override
    public File performInBackground(Intent... params) throws Exception {
        Intent data = params[0];

        String imageFilePath = null;
        Uri uri = data.getData();
        assert uri != null;
        String uriScheme = uri.getScheme();

        boolean isFromGoogleApp = uri.toString().startsWith(SchemeType.SCHEME_CONTENT_GOOGLE);
        boolean isKitKatAndUpper = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        Log.d("test_rotation", "performInBackground()");

        if (SchemeType.SCHEME_CONTENT.equalsIgnoreCase(uriScheme) && !isFromGoogleApp && !isKitKatAndUpper) {
            Log.d("test_rotation", "performInBackground(). 1");
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = App.getInstance().getContentResolver().query(uri, filePathColumn, null, null, null);
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imageFilePath = cursor.getString(columnIndex);
                }
                cursor.close();
            }
        } else if (SchemeType.SCHEME_CONTENT.equalsIgnoreCase(uriScheme)) {
            Log.d("test_rotation", "performInBackground(). 1");

            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = App.getInstance().getContentResolver().query(uri, filePathColumn, null, null, null);
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imageFilePath = cursor.getString(columnIndex);
                }
                cursor.close();
            }
        } else if (SchemeType.SCHEME_FILE.equalsIgnoreCase(uriScheme)) {
            Log.d("test_rotation", "performInBackground(). 2");
            imageFilePath = uri.getPath();
            //                ImageUtils.checkForRotation(imageFilePath);
        } else {
            Log.d("test_rotation", "performInBackground(). 3");
            imageFilePath = ImageUtils.saveUriToFile(uri);
        }

        if (TextUtils.isEmpty(imageFilePath)) {
            throw new IOException("Can't find a filepath for URI " + uri.toString());
        }

        Log.d("test_rotation", "performInBackground(). END, imageFilePath = " + imageFilePath);

        return getFileFromBitmap(imageFilePath);
    }

    private File getFileFromBitmap(String filePath) {
        OutputStream outStream;
        Bitmap bitmap;
        File file = new File(filePath);
        try {
            // make a new bitmap from your file
            bitmap = ImageUtils.decodeFile(file);
            int orientation = ImageUtils.getExifInterfaceOrientation(filePath);
            bitmap = ImageUtils.rotateImage(orientation, bitmap);

            outStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("file", "" + file);
        return file;
    }

    @Override
    public void onResult(File file) {
        hideProgress();
        Log.w(GetFilepathFromUriTask.class.getSimpleName(), "onResult listener = " + listener);
        if (listener != null) {
            listener.onImagePicked(requestCode, file);
        }
    }

    @Override
    public void onException(Exception e) {
        hideProgress();
        Log.w(GetFilepathFromUriTask.class.getSimpleName(), "onException listener = " + listener);
        if (listener != null) {
            listener.onImagePickError(requestCode, e);
        }
    }

    private void showProgress() {
        try {
            FragmentManager fragmentManager = fmWeakReference.get();
            if (fragmentManager != null) {
                ProgressDialogFragment.show(fragmentManager);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void hideProgress() {
        try {
            FragmentManager fragmentManager = fmWeakReference.get();
            if (fragmentManager != null) {
                ProgressDialogFragment.hide(fragmentManager);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}