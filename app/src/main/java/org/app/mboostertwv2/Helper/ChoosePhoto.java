package org.app.mboostertwv2.Helper;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;

import org.app.mboostertwv2.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ChoosePhoto {

    public static int CHOOSE_PHOTO_INTENT = 101;
    public static int SELECTED_IMG_CROP = 102;
    public static int SELECT_PICTURE_CAMERA = 103;
    public static int currentAndroidDeviceVersion = Build.VERSION.SDK_INT;

    private int ASPECT_X = 1;
    private int ASPECT_Y = 1;
    private int OUT_PUT_X = 300;
    private int OUT_PUT_Y = 300;
    private boolean SCALE = true;


    private Uri cropPictureUrl, selectedImageUri = null, cameraUrl = null;
    private Context mContext;

    public ChoosePhoto(Context context) {
        mContext = context;
        init();
    }

    private void init() {
        PermissionUtil permissionUtil = new PermissionUtil();

        if (permissionUtil.checkMarshMellowPermission()) {
            if (permissionUtil.verifyPermissions(mContext, permissionUtil.getCameraPermissions()) && permissionUtil.verifyPermissions(mContext, permissionUtil.getGalleryPermissions()))
                showAlertDialog();
            else {
                ActivityCompat.requestPermissions((Activity) mContext, permissionUtil.getCameraPermissions(), SELECT_PICTURE_CAMERA);
            }
        } else {
            showAlertDialog();
        }
    }

    public void showAlertDialog() {
        final Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");

        cameraUrl = FileUtil.getInstance(mContext).createImageUri();
        //Create any other intents you want
        final Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUrl);

        //Add them to an intent array
        Intent[] intents = new Intent[]{cameraIntent};

        //Create a choose from your first intent then pass in the intent array
        final Intent chooserIntent = Intent.createChooser(galleryIntent, mContext.getString(R.string.choose_photo));
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intents);

        ((Activity) mContext).startActivityForResult(chooserIntent, CHOOSE_PHOTO_INTENT);
    }

    // Change this method(edited)
    public void handleGalleryResult(Intent data) {
        try {
            cropPictureUrl = Uri.fromFile(FileUtil.getInstance(mContext)
                    .createImageTempFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)));
            String realPathFromURI = FileUtil.getRealPathFromURI(mContext, data.getData());
            File file = new File(realPathFromURI == null ? getImageUrlWithAuthority(mContext, data.getData()) : realPathFromURI);
            Uri getUri;
            if (file.exists()) {
                if (currentAndroidDeviceVersion > 23) {
//                    cropImage(FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".provider", file), cropPictureUrl);
                    getUri = FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".provider", file);
                } else {
//                    cropImage(Uri.fromFile(file), cropPictureUrl);
                    getUri = Uri.fromFile(file);
                }
            } else {
//                cropImage(data.getData(), cropPictureUrl);
                getUri = data.getData();
            }
            //rotate image
            Uri rotatePicUri = rotateImage(getUri);
            //then crop if needed
            cropImage(rotatePicUri, cropPictureUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String getImageUrlWithAuthority(Context context, Uri uri) {
        InputStream is = null;
        if (uri.getAuthority() != null) {
            try {
                is = context.getContentResolver().openInputStream(uri);
                Bitmap bmp = BitmapFactory.decodeStream(is);
                return writeToTempImageAndGetPathUri(context, bmp).toString();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static Uri writeToTempImageAndGetPathUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    public void handleCameraResult(Uri cameraPictureUrl) {
        try {
            cropPictureUrl = Uri.fromFile(FileUtil.getInstance(mContext)
                    .createImageTempFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)));
            //cropImage(cameraPictureUrl, cropPictureUrl);

            //rotate image
            Uri rotatePicUri = rotateImage(cameraPictureUrl);
            //then crop
            cropImage(rotatePicUri, cropPictureUrl);


        } catch (IOException e) {
            e.printStackTrace();

        }

    }

    public Uri getCameraUri() {
        return cameraUrl;
    }

    public Uri getCropImageUrl() {
        return selectedImageUri;
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public Uri rotateImage(final Uri sourceImage) {
        Bitmap bsource;

        //Rotate image if needed
        try {
            InputStream input = mContext.getContentResolver().openInputStream(sourceImage);
            ExifInterface ei;
            if (Build.VERSION.SDK_INT > 23)
                ei = new ExifInterface(input);
            else
                ei = new ExifInterface(sourceImage.getPath());

            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);
            Log.i("Orientation", String.valueOf(orientation));


//            Bitmap img = BitmapFactory.decodeStream(input);
            Bitmap img = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), sourceImage);


            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    bsource = rotateImage(img, 90);
                    Log.i("Rotate img saved", "90");
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    bsource = rotateImage(img, 180);
                    Log.i("Rotate img saved", "180");
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    bsource = rotateImage(img, 270);
                    Log.i("Rotate img saved", "270");
                    break;
                default:
                    bsource = img;
            }

            //save bsource(Bitmap) into sourceImage(Uri)
            return getImageUri(mContext, bsource);

        } catch (Exception e) {
            e.printStackTrace();
            return sourceImage;
        }
    }

    private void cropImage(final Uri sourceImage, Uri destinationImage) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        intent.setType("image/*");

        List<ResolveInfo> list = mContext.getPackageManager().queryIntentActivities(intent, 0);
        int size = list.size();

        if (size == 0) {
            //Utils.showToast(mContext, mContext.getString(R.string.error_cant_select_cropping_app));
            selectedImageUri = sourceImage;
            intent.putExtra(MediaStore.EXTRA_OUTPUT, sourceImage);
            ((Activity) mContext).startActivityForResult(intent, SELECTED_IMG_CROP);
            return;
        } else {

            intent.setDataAndType(sourceImage, "image/*");
            intent.putExtra("aspectX", ASPECT_X);
            intent.putExtra("aspectY", ASPECT_Y);
            intent.putExtra("outputY", OUT_PUT_Y);
            intent.putExtra("outputX", OUT_PUT_X);
            intent.putExtra("scale", SCALE);

            //intent.putExtra("return-data", true);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, destinationImage);
            selectedImageUri = destinationImage;
            if (size == 1) {
                Intent i = new Intent(intent);
                ResolveInfo res = list.get(0);
                i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
                ((Activity) mContext).startActivityForResult(intent, SELECTED_IMG_CROP);
            } else {
                Intent i = new Intent(intent);
                i.putExtra(Intent.EXTRA_INITIAL_INTENTS, list.toArray(new Parcelable[list.size()]));
                ((Activity) mContext).startActivityForResult(intent, SELECTED_IMG_CROP);
            }
        }
    }
}