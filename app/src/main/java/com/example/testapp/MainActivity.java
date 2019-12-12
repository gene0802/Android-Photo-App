package com.example.testapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.provider.MediaStore;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.nio.ByteBuffer;



public class MainActivity extends AppCompatActivity {
    TextView seekBarText1 ,seekBarText2 ,seekBarText3,seekBarText4;
    SeekBar seekBar1 ,seekBar2 ,seekBar3, seekBar4;
    ImageView imageView1 ;
    Bitmap bm1,bm2;
    //int[][][] origin_ARGB;
    byte[] origin_ARGB;
    byte[] second_ARGB;
    private static int RESULT_LOAD_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        seekBarText1 = findViewById(R.id.seekBarText1);
        seekBar1 = findViewById(R.id.seekBar1);
        seekBarText2 = findViewById(R.id.seekBarText2);
        seekBar2 = findViewById(R.id.seekBar2);
        seekBarText3 = findViewById(R.id.seekBarText3);
        seekBar3 = findViewById(R.id.seekBar3);
        seekBarText4 = findViewById(R.id.seekBarText4);
        seekBar4 = findViewById(R.id.seekBar4);
        imageView1 = findViewById(R.id.imageView1);

        setSupportActionBar(toolbar);

       FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        // get bitmap from source image
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inScaled = false;
        opt.inMutable = true;
        bm1 = BitmapFactory.decodeResource(getResources(),R.drawable.natural_scene,opt);
        bm2 = BitmapFactory.decodeResource(getResources(),R.drawable.lake_view,opt);
        //set  seekBar/button  Listener
        seekBar1.setOnSeekBarChangeListener(seekBarChangeListener1);
        seekBar2.setOnSeekBarChangeListener(seekBarChangeListener2);
        seekBar3.setOnSeekBarChangeListener(seekBarChangeListener3);
        seekBar4.setOnSeekBarChangeListener(seekBarChangeListener4);
        //button1.setOnClickListener(buttonClickListener);

        Button buttonLoadImage = (Button) findViewById(R.id.buttonLoadPicture);
        buttonLoadImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });


        //copy ARGB to buffer
        ByteBuffer buf = ByteBuffer.allocate(bm1.getByteCount());
        bm1.copyPixelsToBuffer(buf);
        origin_ARGB = buf.array();

        ByteBuffer buf2 = ByteBuffer.allocate(bm2.getByteCount());
        bm2.copyPixelsToBuffer(buf2);
        second_ARGB = buf2.array();

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inScaled = false;
            opt.inMutable = true;
            System.out.println(picturePath);

            if (Build.VERSION.SDK_INT >= 23) {
                int REQUEST_CODE_PERMISSION_STORAGE = 100;
                String[] permissions = {
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                };

                for (String str : permissions) {
                    if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                        this.requestPermissions(permissions, REQUEST_CODE_PERMISSION_STORAGE);
                        return;
                    }
                }
            }

            Bitmap bm3 = BitmapFactory.decodeFile(picturePath,opt);
            imageView1.setImageBitmap(bm3);
            //System.out.println(bm3.getPixel(0,0));
            //System.out.println(bm3.getPixel(0,1));
            //System.out.println(bm3.getPixel(0,2));

        }

    }

    SeekBar.OnSeekBarChangeListener seekBarChangeListener1 = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) { }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) { }
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            int sat = seekBar.getProgress();
            seekBarText1.setText("Saturation: "+(float)sat/100.0f);
            updateImageView(Saturation((float)sat/100.0f));
        }
    };

    SeekBar.OnSeekBarChangeListener seekBarChangeListener2 = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) { }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) { }
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            int dis = seekBar.getProgress();
            seekBarText2.setText("Distortion: "+(float)dis/100.0f);
            updateImageView(Distortion((float)(dis-50.0)/50000000.0f));

        }
    };
    SeekBar.OnSeekBarChangeListener seekBarChangeListener3 = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) { }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) { }
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            int ratio = seekBar.getProgress();
            seekBarText3.setText("Transition1: "+(float)ratio/100.0f);
            updateImageView(pushIn_Transtion((float)ratio/100.0f));
        }
    };
    SeekBar.OnSeekBarChangeListener seekBarChangeListener4 = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) { }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) { }
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            int ratio = seekBar.getProgress();
            seekBarText4.setText("Transition2: "+(float)ratio/100.0f);
            updateImageView(fade_Transition((float)ratio/100.0f,origin_ARGB,second_ARGB));
        }
    };


    public void updateImageView(byte[]ARGB){
        ByteBuffer buf = ByteBuffer.wrap(ARGB);
        bm1.copyPixelsFromBuffer(buf);
        imageView1.setImageBitmap(bm1);
    }

    public byte[]  Saturation(float sat)
    {
        int width = bm1.getWidth();
        int height = bm1.getHeight();
        byte[]sat_ARGB = new byte[height*width*4];

        for(int i=0;i<height;i++){
            for(int j=0;j<width;j++){
                final float invSat = 1 - sat;
                final float R = 0.213f * invSat;
                final float G = 0.715f * invSat;
                final float B = 0.072f * invSat;
                int pixel_position = i*width*4+j*4;
                sat_ARGB[pixel_position+0] =(byte)((origin_ARGB[pixel_position+0]&0xFF) *(R+sat) + (origin_ARGB[pixel_position+1]&0xFF)* G +(origin_ARGB[pixel_position+2]&0xFF)* B);
                sat_ARGB[pixel_position+1] =(byte)((origin_ARGB[pixel_position+0]&0xFF)* R + (origin_ARGB[pixel_position+1]&0xFF)* (G+sat) +(origin_ARGB[pixel_position+2]&0xFF)* B);
                sat_ARGB[pixel_position+2] =(byte)((origin_ARGB[pixel_position+0]&0xFF)* R + (origin_ARGB[pixel_position+1]&0xFF)* G +(origin_ARGB[pixel_position+2]&0xFF)* (B+sat));
                sat_ARGB[pixel_position+3] =origin_ARGB[pixel_position+3];
            }
        }

        return sat_ARGB;
    }

    public byte[] Distortion(float dis)
    {
        int width = bm1.getWidth();
        int height = bm1.getHeight();
        int center_x = width/2;
        int center_y = height/2;
        //byte[]dis_ARGB = origin_ARGB.clone();
        byte[]dis_ARGB = new byte[height*width*4];
        // System.out.println(center_x);
        // System.out.println(center_y);
        for(int y=0;y<height;y++){
            for(int x=0;x<width;x++){
                float r = (center_x-x)*(center_x-x) + (center_y-y)*(center_y-y);
                float x3 = (x-center_x)/(1.0f+dis*r);
                float y3 = (y-center_y)/(1.0f+dis*r);
                int origin_x = center_x+ (int)((x-center_x)/(1.0f+dis*(x3*x3+y3*y3)));
                int origin_y = center_y+ (int)((y-center_y)/(1.0f+dis*(x3*x3+y3*y3)));

                if (0<=origin_x && origin_x<width && 0<= origin_y && origin_y<height) {
                    //System.out.println("x:"+x+",y:"+y+",origin_x:"+origin_x +",origin_y:"+origin_y);
                    int origin_position = (int)(origin_y * width * 4 + origin_x * 4)  ;
                    int position = y * width * 4 + x * 4;
                    dis_ARGB[position + 0] = origin_ARGB[origin_position + 0];
                    dis_ARGB[position + 1] = origin_ARGB[origin_position + 1];
                    dis_ARGB[position + 2] = origin_ARGB[origin_position + 2];
                    dis_ARGB[position + 3] = origin_ARGB[origin_position + 3];


                }
            }
        }
        return dis_ARGB;
    }
   public byte[] pushIn_Transtion(float ratio)
   {
       int width = bm1.getWidth();
       int height = bm1.getHeight();
       byte [] result_ARGB = new byte[height*width*4];
       int shiftByte1 = (int)(ratio*height)*width*4;
       int shiftByte2 = (int)(height*(1-ratio))*width*4;
       for(int y= 0;y<(int)(height*(1-ratio));y++){
           for(int x=0;x<width;x++){
                   int position =  y * width * 4 + x * 4;
                   int origin_position = position +shiftByte1;
                   result_ARGB[position + 0] = origin_ARGB[origin_position + 0];
                   result_ARGB[position + 1] = origin_ARGB[origin_position + 1];
                   result_ARGB[position + 2] = origin_ARGB[origin_position + 2];
                   result_ARGB[position + 3] = origin_ARGB[origin_position + 3];
           }
       }
       for(int y=(int)(height*(1-ratio));y<height;y++){
           for(int x=0;x<width;x++){
               int position =  y * width * 4 + x * 4;
               int origin_position = position - shiftByte2;
               result_ARGB[position + 0] = second_ARGB[origin_position + 0];
               result_ARGB[position + 1] = second_ARGB[origin_position + 1];
               result_ARGB[position + 2] = second_ARGB[origin_position + 2];
               result_ARGB[position + 3] = second_ARGB[origin_position + 3];
           }
       }
       return  result_ARGB;
   }
    public byte[] fade_Transition(float ratio,byte[] ARGB1,byte[]ARGB2)
    {
        int width = bm1.getWidth();
        int height = bm1.getHeight();
        byte[]result_ARGB = new byte[height*width*4];

        for(int i=0;i<height;i++){
            for(int j=0;j<width;j++){

                int pixel_position = i*width*4+j*4;
                //result_ARGB[pixel_position+0] =(byte)((ARGB[pixel_position+0]&0xFF)*ratio);
                //result_ARGB[pixel_position+1] =(byte)((ARGB[pixel_position+1]&0xFF)*ratio);
                //result_ARGB[pixel_position+2] =(byte)((ARGB[pixel_position+2]&0xFF)*ratio);
                //result_ARGB[pixel_position+3] =(byte)((ARGB1[pixel_position+3]&0xFF)*ratio)+ARGB2[];
                result_ARGB[pixel_position+0] =(byte)((ARGB1[pixel_position+0]&0xFF)*ratio + (ARGB2[pixel_position+0]&0xFF)*(1-ratio));
                result_ARGB[pixel_position+1] =(byte)((ARGB1[pixel_position+1]&0xFF)*ratio + (ARGB2[pixel_position+1]&0xFF)*(1-ratio));
                result_ARGB[pixel_position+2] =(byte)((ARGB1[pixel_position+2]&0xFF)*ratio + (ARGB2[pixel_position+2]&0xFF)*(1-ratio));
                //result_ARGB[pixel_position+3] =ARGB[pixel_position+3];
                //System.out.println(result_ARGB[pixel_position+3]&0xFF);
            }

        }
        System.out.println(result_ARGB[(height-1)*width*4+(width-1)*4+3]&0xFF);
        return result_ARGB;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
