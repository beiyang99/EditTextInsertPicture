package com.example.test;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    public final String TAG = this.getClass().getName();
    private final int PICK_PIC = 1;
    private float mResizedPicWidth;

    private void logi(String msg){
        Log.i(TAG,msg);
    }

    @BindView(R.id.et_addimage)
    EditText et_addimage;


    private void initView() {
        ViewTreeObserver vto = et_addimage.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                et_addimage.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int mEditTextWidth = et_addimage.getWidth();
//                mResizedPicWidth = mImgViewWidth * 0.8f;
                mResizedPicWidth = mEditTextWidth;
            }
        });
    }

    private SpannableString getSSOfResizedBitmap(Bitmap pic, Uri uri) {
        int imgWidth = pic.getWidth();
        int imgHeight = pic.getHeight();
        // 只对大尺寸图片进行下面的压缩，小尺寸图片使用原图
        if (imgWidth >= mResizedPicWidth) {
            float scale = (float) mResizedPicWidth / imgWidth;
            Matrix mx = new Matrix();
            mx.setScale(scale, scale);
            pic = Bitmap.createBitmap(pic, 0, 0, imgWidth, imgHeight, mx, true);
        }
        String picPath = uri.getPath();
        SpannableString ssPicPath = new SpannableString(picPath);
        ImageSpan newImageSpan = new ImageSpan(this, pic);
        ssPicPath.setSpan(newImageSpan, 0, picPath.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ssPicPath;
    }
    private void insertIntoEditText(SpannableString ss) {

        // 先获取Edittext中原有的内容
        Editable et = et_addimage.getText();
        //start means the start postion to insert the new
        int start = et_addimage.getSelectionStart();
        // insert picture after the existing
        et.insert(start, ss);
        //add a nextline to the picture,for we always want ot start a new line after picture.
        et.append("\n");
        // 把et添加到Edittext中
        et_addimage.setText(et);
        // 设置Edittext光标在最后显示,+1 corresponds to "\n"
        et_addimage.setSelection(start + ss.length()+1);
    }
    private Bitmap getOriginalBitmap(Uri photoUri) {
        if (photoUri == null) {
            return null;
        }
        Bitmap bitmap = null;
        try {
            ContentResolver conReslv = getContentResolver();
            // 得到选择图片的Bitmap对象
            bitmap = MediaStore.Images.Media.getBitmap(conReslv, photoUri);
        } catch (Exception e) {
            Log.e(TAG, "Media.getBitmap failed", e);
        }
        return bitmap;
    }

    @OnClick(R.id.btn_insert_pic)
    public void test(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_PIC);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_PIC) {
                if (data == null) {
                    Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show();
                } else {
                    Uri uri = data.getData();
                    Bitmap bitmap = getOriginalBitmap(uri);
                    SpannableString ss = getSSOfResizedBitmap(bitmap, uri);
                    insertIntoEditText(ss);
                }
            }
        }
    }


    private void foo(){
       EditText username = (EditText)findViewById(R.id.et_addimage);
//---------------------------设置引入图片的尺寸---------------------------------------------
       Drawable username_drawable = getResources().getDrawable(R.drawable.tab_home);
//四个参数分别是设置图片的左、上、右、下的尺寸
       username_drawable.setBounds(0,0,80,80);
//这个是选择将图片绘制在EditText的位置，参数对应的是：左、上、右、下
       username.setCompoundDrawables(username_drawable,username_drawable,username_drawable,username_drawable);
//---------------------------------------------------------------------
   }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main2);
        ButterKnife.bind(this);
//        foo();
        initView();
    }


}
