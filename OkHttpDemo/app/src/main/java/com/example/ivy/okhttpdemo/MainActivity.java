package com.example.ivy.okhttpdemo;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    final String loginUrl = "";
    final String userInfoUrl = "";
    String uploadUrl = "";
    private String weatherInfoUrl = "http://v.juhe.cn/weather/index?cityname=%s&dtype=json&format=1&key=2dddd304db4c8294886f09c112b89119";
    private EditText etUserName;
    private EditText etPwd;
    private Button btLogin;
    private TextView tvResult;
    private Button btOperator;
    private OkHttpClient client = new OkHttpClient();
    private final String USER_AGENT = "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; WOW64; Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C)";
    private Button btUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etUserName = (EditText) findViewById(R.id.et_username);
        etPwd = (EditText) findViewById(R.id.et_pwd);
        tvResult = (TextView) findViewById(R.id.tv_result);
        btLogin = (Button) findViewById(R.id.bt_login);
        btOperator = (Button) findViewById(R.id.bt_operator);
        btUpload = (Button) findViewById(R.id.bt_upload);

        btLogin.setOnClickListener(this);
        btOperator.setOnClickListener(this);
        btUpload.setOnClickListener(this);


        client.setCookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ORIGINAL_SERVER));
    }

    @Override
    public void onClick(View v) {
        tvResult.setText("");
        switch (v.getId()) {
            case R.id.bt_login:
                //login();
//              login2();
//                login3();
                login4();
                break;
            case R.id.bt_operator:
                //getInfo();
                //getInfo2();
                //getInfo3();
                getInfo4();
                break;
            case R.id.bt_upload:
                upload();
                break;
        }

    }


    private void upload() {

        HashMap<String, String> map = new HashMap<>();
        map.put("type","big");

        File file = new File(getCacheDir(),"1.jpg");
        File[] files = {file};

        String[] formNames = {"Filedata"};


        OkHttpUtils.postUploadFilesAsync(this,  uploadUrl, map,files,formNames,"upload", new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.d("main","error...");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                final String result = response.body().string();
                Log.d("main","response..."+response.body().string());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvResult.setText(result);
                    }
                });
            }
        });
    }

    /**
     * 使用封装类-异步
     */
    private void getInfo4() {
        OkHttpUtils.getAsync(this, userInfoUrl, "userInfo",new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                        tvResult.setText(  "error");

                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                final String result = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "请求成功", Toast.LENGTH_SHORT).show();
                        tvResult.setText(result + "_4");

                    }
                });
            }
        });
    }

    /**
     * 使用封装类-同步
     */
    private void getInfo3() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                final String userInfo = OkHttpUtils.get(MainActivity.this, userInfoUrl, "userInfo");
                if(userInfo!=null){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvResult.setText(userInfo);
                        }
                    });
                }
            }
        }).start();

    }

    /**
     * 使用封装类-异步
     */
    private void login4() {
        final String username = etUserName.getText().toString().trim();
        final String pwd = etPwd.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, String> map = new HashMap<String, String>();
        map.put("email", username);
        map.put("password", pwd);
        map.put("ememberme", "1");

        OkHttpUtils.postAsync(this, map, loginUrl, "login", new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                        tvResult.setText("error");

                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                final String result = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "请求成功", Toast.LENGTH_SHORT).show();
                        tvResult.setText(result + "_4");

                    }
                });
            }
        });
    }

    /**
     * 使用封装类-同步
     */
    private void login3() {

        final String username = etUserName.getText().toString().trim();
        final String pwd = etPwd.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }


        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, String> map = new HashMap<String, String>();
                map.put("email", username);
                map.put("password", pwd);
                map.put("ememberme", "1");
                final String result = OkHttpUtils.post(MainActivity.this, map, loginUrl, "login");
                Log.d("main log 3", result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (result != null) {
                            tvResult.setText(result + "_3");
                        } else {
                            tvResult.setText("error");
                        }

                    }
                });

            }
        }).start();


    }

    private void getInfo2() {

        String city = null;
        try {
            city = URLEncoder.encode("上海", "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            city = "";
        }

        Log.d("city ", city);

        weatherInfoUrl = String.format(weatherInfoUrl, city);
        Request request = new Request.Builder()
                .url(weatherInfoUrl)
                .header("User-Agent", USER_AGENT)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                        tvResult.setText("error");
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                final String result = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "请求成功", Toast.LENGTH_SHORT).show();
                        tvResult.setText(result);

                    }
                });
            }
        });
    }

    private void getInfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
//                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url(userInfoUrl)
                        .header("User-Agent", USER_AGENT)
                        .build();

                Response response = null;
                try {
                    response = client.newCall(request).execute();

                    if (response.isSuccessful()) {
                        final String result = response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "请求成功", Toast.LENGTH_SHORT).show();
                                tvResult.setText(result);

                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }).start();


    }

    /**
     * 异步
     */
    private void login2() {
        final String username = etUserName.getText().toString().trim();
        final String pwd = etPwd.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }


//        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormEncodingBuilder()
                .add("email", username)
                .add("password", pwd)
                .add("ememberme", "1").build();
        final Request request = new Request.Builder().url(loginUrl).post(body).build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onResponse(final Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "请求成功", Toast.LENGTH_SHORT).show();
                        try {
                            tvResult.setText(response.body().string() + "_2");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
    }

    /**
     * 登陆方法-同步
     */
    private void login() {

        final String username = etUserName.getText().toString().trim();
        final String pwd = etPwd.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }


        new Thread(new Runnable() {
            @Override
            public void run() {
//                OkHttpClient client = new OkHttpClient();

                FormEncodingBuilder formBuilder = new FormEncodingBuilder();
                formBuilder.add("email", username);
                formBuilder.add("password", pwd);
                formBuilder.add("ememberme", "1");
                //获取requestBody 对象
                RequestBody requestBody = formBuilder.build();

                Request request = new Request.Builder().url(loginUrl).post(requestBody).build();

                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        final String result = response.body().string();
                        byte[] bytes = response.body().bytes();
                        Log.d("Main", result);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvResult.setText(result);
                            }
                        });

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("Main", "error...");
                }
            }
        }).start();

    }
}
