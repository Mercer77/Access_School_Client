package com.example.administrator.access_school_client.Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.access_school_client.R;
import com.example.administrator.access_school_client.UI.FaceInputFm;
import com.example.administrator.access_school_client.UI.FragmentInfoPage;
import com.example.administrator.access_school_client.UI.Fragmentfour;
import com.example.administrator.access_school_client.UI.Fragmentthree;
import com.example.administrator.access_school_client.UI.HistoryActivity;
import com.example.administrator.access_school_client.Util.BitmapBlurUtil;
import com.example.administrator.access_school_client.Util.CircleImageView;
import com.example.administrator.access_school_client.Util.GauseBulrHelper;
import com.example.administrator.access_school_client.Util.PermissionUtil;
import com.example.administrator.access_school_client.Util.SharedPreferencesUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

/**
 * ..
 * author:liangliangattack 1364744931@.qq.com
 * Administrator on 2018/7/18 16:29.
 */
public class FragmentWD extends Fragment {

    private LinearLayout infopage;
    private LinearLayout faceinput;
    private LinearLayout three;
    private LinearLayout four;
    private CircleImageView userimage;
    private static LinearLayout userlinearlayout;
    private static int SELECT_IMAGE_CODE=1;
    private static int SELECT_IMAGE_CAMERA_CODE=0;
    private static String mImagePath;
    private TextView hintclick;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("HandlerLeak")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.wd_fragment, container, false);
        //final ImageView imageview = rootview.findViewById(R.id.test);
        faceinput = rootview.findViewById(R.id.face_input);
        infopage = rootview.findViewById(R.id.info);
        three = rootview.findViewById(R.id.three);
        four = rootview.findViewById(R.id.four);
        userlinearlayout = rootview.findViewById(R.id.user_ll);
        userimage = rootview.findViewById(R.id.user_picture);
        hintclick = rootview.findViewById(R.id.hintclick);
        Bitmap bitmap = ((BitmapDrawable) userimage.getDrawable()).getBitmap();

        //SharedPreferencesUtils.clear();

        String userImagePath = SharedPreferencesUtils.getUserImagePath("userimage");
        Log.d("FragmentWD",""+userImagePath);
        if(!(userImagePath.equals("error"))) {
            Log.d("xxxxxxxxxxxxxxxxxxx","默认。。。");
            BitmapFactory.Options opts = new BitmapFactory.Options();
            bitmap =  BitmapFactory.decodeFile(userImagePath, opts);
            userimage.setImageBitmap(bitmap);
            blur(getContext(),bitmap);
        }
        else {
            //GauseBulrHelper:
//        bitmap = GauseBulrHelper.blur(getContext(),bitmap);
//        userlinearlayout.setBackground(new BitmapDrawable(bitmap));


//        BitmapBlurUtil：
            //userimage.setImageResource(R.mipmap.user);
            Log.d("ZZZZZZZZZZZZZZZZZZ","默认模糊。。。");
            blur(getContext(), bitmap);
        }


        //imageview.setImageBitmap(bitmap);
        //imageview.setImageDrawable(circleImageView.getDrawable());
        //imageview.setImageDrawable(drawable);
        hintclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "suprise", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), HistoryActivity.class));
            }
        });

        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.drawlayout_content,new Fragmentfour());
                transaction.addToBackStack(null).commit();
            }
        });

        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.drawlayout_content,new Fragmentthree());
                transaction.addToBackStack(null).commit();
            }
        });

        faceinput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.drawlayout_content,new FaceInputFm());
                transaction.addToBackStack(null).commit();
            }
        });

        infopage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.drawlayout_content,new FragmentInfoPage());
                transaction.addToBackStack(null).commit();
            }
        });

        userimage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //Toast.makeText(getContext(),"长按",Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("头像");
                builder.setItems(new String[]{"拍照上传", "相册", "取消"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            default:
                                break;
                            case 0:
                                //打开相机
                                if(PermissionUtil.hasCameraPermission(getActivity())) {
                                    takePhoto();
                                    Toast.makeText(getContext(),"打开相机",Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(getContext(),"无拍照权限",Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case 1:
                                //打开相册
                                if(PermissionUtil.hasReadExternalStoragePermission(getActivity())) {
                                    pickPhoto();
                                }
                                else{
                                    Toast.makeText(getContext(),"无查看权限",Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case 2:
                                return;
                            case 3:
                                break;
                        }
                    }
                });
                builder.create().show();
                return false;
            }
        });
        return rootview;
    }


    //自定义方法实现模糊
    static void blur(Context context, final Bitmap bitmap) {
//        if (bitmap != null) {
//            //模糊处理
//            BitmapBlurUtil.addTask(bitmap, new Handler() {
//                @Override
//                public void handleMessage(Message msg) {
//                    super.handleMessage(msg);
//                    Drawable drawable = (Drawable) msg.obj;
//                    //设置模糊背景图片
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                        userlinearlayout.setBackground(drawable);
//                    }
//                    //imageview.setImageDrawable(drawable);
//                    bitmap.recycle();
//                }
//            });
//        }

        //GauseBulrHelper：
//        if(bitmap!=null){
//            Bitmap blur = GauseBulrHelper.blur(context, bitmap);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                userlinearlayout.setBackground(new BitmapDrawable(blur));
//            }
//        }
    }

    public void pickPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_IMAGE_CODE);
    }

    public void takePhoto() {
        // 执行拍照前，应该先判断SD卡是否存在
        String SDState = Environment.getExternalStorageState();
        if (SDState.equals(Environment.MEDIA_MOUNTED)) {

            //通过指定图片存储路径，解决部分机型onActivityResult回调 data返回为null的情况

            //获取与应用相关联的路径
            String imageFilePath = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
            //根据当前时间生成图片的名称
            String timestamp = "/" + formatter.format(new Date()) + ".png";
            File imageFile = new File(imageFilePath, timestamp);// 通过路径创建保存文件
            mImagePath = imageFile.getAbsolutePath();
            Uri imageFileUri = Uri.fromFile(imageFile);// 获取文件的Uri

            //开始相机操作
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);// 告诉相机拍摄完毕输出图片到指定的Uri
            startActivityForResult(intent, SELECT_IMAGE_CAMERA_CODE);
        } else {
            Toast.makeText(getContext(), "SD卡不存在!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String imagePath = "";
        if (SELECT_IMAGE_CAMERA_CODE == requestCode && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                //有数据返回直接使用返回的图片地址
                //imagePath = getFilePathByFileUri(getContext(), data.getData());
                imagePath = getPath(data.getData());
            } else {//无数据使用指定的图片路径
                imagePath = mImagePath;
            }
            //得到图片imagepath的相关操作：。。。。

            //dataList.addFirst(imagePath);
            //adapter.update(dataList); // 刷新图片

            SharedPreferencesUtils.setUserImagePath("userimage",imagePath);
        }
        else if(SELECT_IMAGE_CODE == requestCode && resultCode == RESULT_OK){
            Uri mPath = data.getData();
            imagePath = getPath(mPath);
            Log.d("图片地址：",imagePath+"dasdas");

            SharedPreferencesUtils.setUserImagePath("userimage",imagePath);
        }


        BitmapFactory.Options opts = new BitmapFactory.Options();
        Bitmap bitmap =  BitmapFactory.decodeFile(imagePath, opts);
        userimage.setImageBitmap(bitmap);
        blur(getContext(),bitmap);
    }

    /**
     * 根据图片路径和格式把图片转化为Base64编码格式
     * @param //imgPath
     * @param //bitmap
     * @param //imgFormat 图片格式
     * @param //quality 压缩的程度
     * @return
     */
//    public static String imgToBase64(Context context, String imgPath, String imgFormat, int quality) {
//        Bitmap bitmap = null;
//        if (imgPath !=null && imgPath.length() > 0) {
//            bitmap = readBitmap(context,imgPath);
//        }
//        if(bitmap == null){
//            return null;
//        }
//        ByteArrayOutputStream out = null;
//        try {
//            out = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.PNG, quality, out);
//            out.flush();
//            out.close();
//
//            byte[] imgBytes = out.toByteArray();
//            long length = imgBytes.length;
//            //LogUtil.LogShitou("字节长度", length+"");
//            return Base64.encodeToString(imgBytes, Base64.DEFAULT);
//        } catch (Exception e) {
//            return null;
//        } finally {
//            try {
//                out.flush();
//                out.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }


    private String getPath(Uri uri) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (DocumentsContract.isDocumentUri(getContext(), uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }

                    // TODO handle non-primary volumes
                } else if (isDownloadsDocument(uri)) {

                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                    return getDataColumn(getContext(), contentUri, null, null);
                } else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[] {
                            split[1]
                    };

                    return getDataColumn(getContext(), contentUri, selection, selectionArgs);
                }
            }
        }
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor actualimagecursor = getContext().getContentResolver().query(uri, proj, null, null, null);
        int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        actualimagecursor.moveToFirst();
        String img_path = actualimagecursor.getString(actual_image_column_index);
        String end = img_path.substring(img_path.length() - 4);
        if (0 != end.compareToIgnoreCase(".jpg") && 0 != end.compareToIgnoreCase(".png")) {
            return null;
        }
        return img_path;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * 从指定路径读取图片文件，并进行缩放
     * @param context
     * @param imgPath
     * @return
     */
//    private static Bitmap readBitmap(Context context, String imgPath) {
//
//        try {
//            BitmapFactory.Options opts = new BitmapFactory.Options();
//            // 如果设置为true,仅仅返回图片的宽和高,宽和高是赋值给opts.outWidth,opts.outHeight;
//            opts.inJustDecodeBounds = true;
//            BitmapFactory.decodeFile(imgPath, opts);
//            // 得到原图的宽和高
//            int srcWidth = opts.outWidth;
//            int srHeigth = opts.outHeight;
//
//            //获取缩放的比例(我这里设置的是80dp)
//            int scale = 1;
//            int sx = srcWidth/dip2px(context, 80); // 3000/320= 9;
//            int sy = srHeigth/dip2px(context, 80); // 2262/480= 5;
//            if(sx > sy && sx > 1){
//                scale = sx;
//            }
//            if(sy > sx && sy > 1){
//                scale = sy;
//            }
//
//            // 设置为false表示,让decodeFile返回一个bitmap类型的对象
//            opts.inJustDecodeBounds = false;
//            // 设置缩放的比例,设置后,decodeFile方法就会按照这个比例缩放图片,加载到内存
//            opts.inSampleSize= scale;
//            // 缩放图片加载到内存(一般加载图片到内存，都要根据手机屏幕宽高对图片进行适当缩放，否则可能出现OOM异常)
//            Bitmap bitmap =  BitmapFactory.decodeFile(imgPath, opts);
//
//            return bitmap;
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            return null;
//        }
//    }

    /**
     * dp转换成px
     */
//    public static int dip2px(Context context, float dpValue) {
//        final float scale = context.getResources().getDisplayMetrics().density;
//        return (int) (dpValue * scale + 0.5f);
//    }

}