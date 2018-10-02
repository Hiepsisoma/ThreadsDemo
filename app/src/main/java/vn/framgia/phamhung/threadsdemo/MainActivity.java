package vn.framgia.phamhung.threadsdemo;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ImageAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        new ImageAsyncTask().execute();
    }

    private void initViews() {
        RecyclerView recycler = findViewById(R.id.recycler_image);
        mAdapter = new ImageAdapter(this);
        recycler.setAdapter(mAdapter);
    }

    public static Intent getMainIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }

    private class ImageAsyncTask extends AsyncTask<Void, Void, List<String>> {

        @Override
        protected List<String> doInBackground(Void... voids) {
            List<String> images = new ArrayList<>();
            String[] projection = {MediaStore.MediaColumns.DATA,
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
            Uri uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            Cursor cursor = getApplicationContext().getContentResolver().query(uri, projection,
                    null, null, null);
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            while (cursor.moveToNext()) {
                String absolutePathOfImage = cursor.getString(columnIndex);
                images.add(absolutePathOfImage);
            }
            return images;
        }

        @Override
        protected void onPostExecute(List<String> images) {
            super.onPostExecute(images);
            mAdapter.addDatas(images);
        }
    }
}
