package com.example.dominic.volleydemo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private String url2 = "http://mapi.yinyuetai.com/video/get_mv_areas.json?deviceinfo={%22aid%22:%2210201036%22,%22os%22:%22Android%22,%22ov%22:%226.0%22,%22rn%22:%22480*800%22,%22dn%22:%22Android%20SDK%20built%20for%20x86_64%22,%22cr%22:%2246000%22,%22as%22:%22WIFI%22,%22uid%22:%22dbcaa6c4482bc05ecb0bf39dabf207d2%22,%22clid%22:110025000}";
    private String imgUrl = "https://a-ssl.duitang.com/uploads/item/201608/02/20160802212747_Lx5Yd.jpeg";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }
    //返回字符串
    public void stringReqeust(View view) {
        StringRequest request = new StringRequest(url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "" + response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);


    }

    //返回json对象
    public void jsonObjectReqeust(View view) {
        //1.创建请求对象
        JSONObject jsonObject = null;//提交参数
        //jsonObject.put("username", "用户名") 正式开放不使用这种方式提交参数，正式开放使用Map集合
        JsonObjectRequest request = new JsonObjectRequest(url2, jsonObject, new Response.Listener<JSONObject>() {
            //成功回调方法
            @Override
            public void onResponse(JSONObject jsonObject) {
                //String province = jsonObject.getString("province");//当key不存在，空指针问题，正式开放不建议使用
                String name = jsonObject.optString("name");//正式开放建议使用，当key不存在，返回默认值:"",0,0.0
                Toast.makeText(getApplicationContext(), "" + name, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            //失败回调方法
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            //正式开放使用该方法提交参数
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //比如登录
                Map<String, String> map = new HashMap<String, String>();
                map.put("username", "用户名");
                map.put("password", "密码");
                return map;
            }
        };

        //2.创建请求队列
        RequestQueue queue = Volley.newRequestQueue(this);
        //3.发起网络请求
        queue.add(request);
    }



    //返回json数组
    public void jsonArrayReqeust(View view) {

        JsonArrayRequest request = new JsonArrayRequest(url2, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                for (int i = 0; i < jsonArray.length(); i++) {
                  //  jsonArray.getJSONObject(i);
                  //  object.get("name");
                    JSONObject object = jsonArray.optJSONObject(i);
                    String name = object.optString("name");
                    Toast.makeText(getApplicationContext(), "" + name, Toast.LENGTH_SHORT).show();
                 //   Toast.makeText(getApplicationContext(), "请求成功", Toast.LENGTH_SHORT).show();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "网络请求失败", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }


    //返回Bitmap对象
    public void imageReqeust(View view) {

        //大图压缩核心参数：inJustDecodeBounds，inSampleSize
        int maxWidth = 0;//0-默认大小，大于0图片压缩,压缩率
        int maxHeight = 0;
        Bitmap.Config decodeConfig = Bitmap.Config.RGB_565; //16位,2个字节，一个像素点占用2个字节
        //Bitmap.Config decodeConfig = Bitmap.Config.ARGB_8888;//32位,4个字节，一个像素点占用4个字节
        //Bitmap.Config decodeConfig = Bitmap.Config.ARGB_4444;//16位,2个字节，一个像素点占用2个字节
        //Bitmap.Config decodeConfig = Bitmap.Config.ALPHA_8;//8位,1个字节，一个像素点占用1个字节

        /**
         * 面试题：请就算480 * 800大小图片占用空间大小
         * Bitmap.Config.RGB_565
         *
         * 1.480 * 800个像素点
         * 2.图片每个像素的占用几个字节：2个字节
         * 3. 所有像素点占用字节：480 * 800 * 2 /1024 = ?kb
         */

        /**
         *  开发中到底使用哪个格式？
         */


        ImageRequest request = new ImageRequest(imgUrl, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                mImage.setImageBitmap(response);

            }
        }, maxWidth, maxHeight, decodeConfig, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "请求数据失败", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

    }

    private ImageView mImage;
    private void initView() {
       mImage = (ImageView) findViewById(R.id.image);
    }


}
