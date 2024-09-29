package com.example.daily_selfie;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static final long INTERVAL_TWO_MINUTES = 1 * 60 * 1000L;
    private static final String CHANNEL_ID = "1";
    private static final int REQUEST_ID_READ_WRITE_PERMISSION = 99;
    private static final int REQUEST_ID_IMAGE_CAPTURE = 100;
    private static final int REQUEST_ID_VIDEO_CAPTURE = 101;
    private ImageView imageView;
    private ListView listView;
    private ArrayList<Image> arr = new ArrayList<Image>();
    private ArrayList<String> str = new ArrayList<String>();
    private String file;
    private Bitmap bp;
    private String currentDateandTime;
    public String state;
    private AlertDialog.Builder builder;
    private final String[] PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private final int REQUEST_PERMISSION = 1000;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.camera){
            captureImage();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView);
        String root = Environment.getExternalStorageDirectory().toString();//get external storage
        builder = new AlertDialog.Builder(this);

        File myDir = new File(root +"/files/PICTURES");

        myDir.mkdirs();

        if(isMyServiceRunning(AlarmReceiver.class)){
            Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
            stopService(intent);
        }

        if(readFromFile("nameImg.txt")!=""){
            String nameimg[] = readFromFile("nameImg.txt").trim().split(", ");

            for(String item : nameimg){
                Bitmap bitmap = null;
                str.add(item);

                    state = Environment.getExternalStorageState();
                    if (Environment.MEDIA_MOUNTED.equals(state)){
                        file = "/sdcard/Android/data/com.example.daily_selfie/files/PICTURES/" + item + ".jpg";
                        bitmap = readFile(file);
                    }
//                if(bitmap!=null){
                    arr.add(new Image(item,bitmap));
//                }
//                else arr.add(new Image(item, null));
            }
        };

        CustomAdapter adapter = new CustomAdapter(this,arr);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Image img = arr.get(position);
                Bitmap bmp = img.getImg();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                Intent intImg = new Intent(MainActivity.this,ImageActivity.class);
                intImg.putExtra("image",byteArray);
                startActivity(intImg);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                builder.setMessage("Do you want to delete this image:\n"+str.get(i))
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String nameToRemove = str.get(i);
                                delFile(nameToRemove);
                                removeFromFile(i);
                                adapter.setImgs(arr);
                                listView.setAdapter(adapter);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        }) ;
                AlertDialog alertDialog = builder.create();
                alertDialog.setTitle("Confirm");
                alertDialog.show();
            return true;
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(!isMyServiceRunning(AlarmReceiver.class)){
            createAlarm();
        }
    }

    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        this.startActivityForResult(intent, REQUEST_ID_IMAGE_CAPTURE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ID_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                bp = (Bitmap) data.getExtras().get("data");
                currentDateandTime =(String) new SimpleDateFormat("dd/MM/yyyy_HH:mm:ss").format(new Date());
                str.add(currentDateandTime);
                String tmp = str.toString();
                tmp = tmp.replace("["," ");
                tmp = tmp.replace("]"," ");
                tmp.trim();

                saveFile(bp,currentDateandTime);
                writeToFile(tmp,"nameImg.txt");
                arr.add(new Image(currentDateandTime,bp));
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Action canceled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Action Failed", Toast.LENGTH_LONG).show();
            }}
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        CustomAdapter adapter = new CustomAdapter(this,arr);
        listView.setAdapter(adapter);
    }
//    protected void onDestroy() {
//        super.onDestroy();
//        Intent intent = new Intent(this, MyService.class);
//        startService(intent);
//    }

    private void delFile(String imgName){
        String path = getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString();
        String fname = imgName +".jpg";
        File fileImg = new File (path, fname);
        if (fileImg.exists()){
            fileImg.delete();
        }
    }

    private void removeFromFile(int i){
        arr.remove(i);
        str.remove(i);
        if(str.isEmpty()){
            writeToFile("", "nameImg.txt");
        }
        else {
            String tmp = str.toString();
            tmp = tmp.replace("[", " ");
            tmp = tmp.replace("]", " ");
            tmp.trim();
            writeToFile(tmp, "nameImg.txt");
        }
    }

    private void saveFile(Bitmap finalBitmap,String imgName){
        String path = getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString();
        String fname = imgName +".jpg";
        File fileImg = new File (path, fname);
        if (fileImg.exists()){
            fileImg.delete();
        }
        else {
            try {
                FileOutputStream out = new FileOutputStream(fileImg);
                finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.flush();
                out.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private Bitmap readFile(String file){
        Bitmap bit = null;
        try {
            InputStream inputStream = new FileInputStream(file);
            bit = BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bit;
    }
    private void writeToFile(String data,String name) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(this.openFileOutput(name, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
    private String readFromFile(String name) {

        String ret = "";

        try {
            InputStream inputStream = this.openFileInput(name);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append("\n").append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }
    private void createAlarm() {
        try {
            Intent intent = new Intent(this, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + INTERVAL_TWO_MINUTES,
                    INTERVAL_TWO_MINUTES,
                    pendingIntent);
        }
        catch (Exception exception) {
            Log.d("ALARM", exception.getMessage().toString());
        }
    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}