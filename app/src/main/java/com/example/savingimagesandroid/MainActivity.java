package com.example.savingimagesandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {


//    final String relativeLocation = Environment.DIRECTORY_PICTURES + File.separator + “YourSubforderName”;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.hanboreum); //CHOOSE PICTURE

        try{
        saveBitmap(this, bm, Bitmap.CompressFormat.JPEG, "image/jpeg", "MyImage"); //sesuaikan nama image/jpeg || image/png
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    //START SAVE IMAGE CODE
    private void saveBitmap(@NonNull final Context context, @NonNull final Bitmap bitmap,
                            @NonNull final Bitmap.CompressFormat format, @NonNull final String mimeType,
                            @NonNull final String displayName) throws IOException
    {
        final String relativeLocation = Environment.DIRECTORY_PICTURES;

        final ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, displayName);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, mimeType);
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, relativeLocation);

        final ContentResolver resolver = context.getContentResolver();

        OutputStream stream = null;
        Uri uri = null;

        try
        {
            final Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            uri = resolver.insert(contentUri, contentValues);

            if (uri == null)
            {
                throw new IOException("Failed to create new MediaStore record.");
            }

            stream = resolver.openOutputStream(uri);

            if (stream == null)
            {
                throw new IOException("Failed to get output stream.");
            }

            if (bitmap.compress(format, 95, stream) == false)
            {
                throw new IOException("Failed to save bitmap.");
            }
        }
        catch (IOException e)
        {
            if (uri != null)
            {
                // Don't leave an orphan entry in the MediaStore
                resolver.delete(uri, null, null);
            }

            throw e;
        }
        finally
        {
            if (stream != null)
            {
                stream.close();
            }
        }
    }
    //END SAVE IMAGE CODE
}
