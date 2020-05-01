package com.example.classifier;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MainActivity extends AppCompatActivity {

    Button JumpToActivity2;
    Bitmap bm;
    Button btn_pic;
    ImageView imageView;
    Interpreter tflite;
    Button inferButton;
    TextView outputNumber;

    private  static final int IMAGE_MEAN = 120;
    private  static  final float IMAGE_STD = 120.0f;
    private ByteBuffer imgData = null;
    private int DIM_IMG_SIZE_X = 64;
    private  int DIM_IMG_SIZE_Y = 64;
    private int DIM_PIXEL_SIZE =3;
    private int[] intValues;
    private String final_detected_emotion = "no emotion detected";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        JumpToActivity2 = (Button)findViewById(R.id.JumpToActivity2);

        intValues = new int[DIM_IMG_SIZE_X*DIM_IMG_SIZE_Y];
        try{
            tflite = new Interpreter(loadModelFile());
        } catch (Exception ex){
            ex.printStackTrace();
        }

        btn_pic = (Button) findViewById(R.id.btn_capture);
        imageView = (ImageView)findViewById(R.id.image);
        inferButton = (Button)findViewById(R.id.inferButton);

        imgData = ByteBuffer.allocateDirect(3*DIM_IMG_SIZE_Y*DIM_IMG_SIZE_X*4);
        imgData.order(ByteOrder.nativeOrder());
        outputNumber = (TextView) findViewById(R.id.outputNumber);

        inferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bitmap bit = getResizeBitmap(bm,64,64);
                convertBitmapToByteBuffer(bit);

                float[][] outputval;
                outputval = new float[1][7];

                tflite.run(imgData,outputval);
                float final_emotion;
                final_emotion = outputval[0][0];

                String[] emotion_list = {"anger","contempt","disgust","fear","happy","sadness","surprise"};
                final_detected_emotion = "anger";
                for(int i=0;i<7;i++)
                {
                    if(final_emotion < outputval[0][i])
                    {
                        final_emotion = outputval[0][i];
                        final_detected_emotion = emotion_list[i];
                    }
                }
                outputNumber.setText(final_detected_emotion + "ffgg");
            }
        });


        btn_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent indent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(indent, 0);
            }
        });

        JumpToActivity2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Activity2.class);
                intent.putExtra("final_detected_emotion",final_detected_emotion);
                startActivity(intent);
            }
        });

    }

    protected  void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode,data);

        Bitmap bitmap = (Bitmap)data.getExtras().get("data");
        bm = bitmap;
        imageView.setImageBitmap(bitmap);

    }

    public Bitmap getResizeBitmap(Bitmap bm , int newWidth, int newHeight){
        int width = bm.getWidth();
        int height =bm.getHeight();
        float scaleWidth = ((float) newWidth)/width;
        float scaleheight = ((float) newHeight)/ height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth,scaleheight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bm,0,0,width,height,matrix,false);
        return resizedBitmap;
    }


    private MappedByteBuffer loadModelFile() throws IOException{
        AssetFileDescriptor fileDescriptor = this.getAssets().openFd("model.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY,startOffset,declaredLength);
    }

    private void convertBitmapToByteBuffer(Bitmap bitmap){
        if(imgData == null){
            return;
        }
        imgData.rewind();
        bitmap.getPixels(intValues,0,bitmap.getWidth(),0,0,bitmap.getWidth(),bitmap.getHeight());
        int pixel =0;
        for(int i =0 ; i < DIM_IMG_SIZE_X;++i){
            for ( int j=0;j<DIM_IMG_SIZE_Y;++j){
                final int val = intValues[pixel++];
                imgData.putFloat((((val>>16) & 0xFF)-IMAGE_MEAN)/IMAGE_STD);
                imgData.putFloat((((val>>8) & 0xFF)-IMAGE_MEAN)/IMAGE_STD);
                imgData.putFloat((((val) & 0xFF)-IMAGE_MEAN)/IMAGE_STD);
            }
        }
    }
}
