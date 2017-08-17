package com.example.cao_hao.androidstudy.Activity;


import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.cao_hao.androidstudy.R;
import com.example.cao_hao.androidstudy.utils.LogUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * public abstract class AsyncTask<Params, Progress, Result> {
 * 一个异步任务的执行一般包括以下几个步骤：
 1.execute(Params... params)，执行一个异步任务，需要我们在代码中调用此方法，触发异步任务的执行。
 2.onPreExecute()，在execute(Params... params)被调用后立即执行，一般用来在执行后台任务前对UI做一些标记。
 3.doInBackground(Params... params)，在onPreExecute()完成后立即执行，用于执行较为费时的操作，此方法将接收输入参数和返回计算结果。在执行过程中可以调用publishProgress(Progress... values)来更新进度信息。
 4.onProgressUpdate(Progress... values)，在调用publishProgress(Progress... values)时，此方法被执行，直接将进度信息更新到UI组件上。
 5.onPostExecute(Result result)，当后台操作结束时，此方法将会被调用，计算结果将做为参数传递到此方法中，直接将结果显示到UI组件上。
 在使用的时候，有几点需要格外注意：
 1.异步任务的实例必须在UI线程中创建。
 2.execute(Params... params)方法必须在UI线程中调用。
 3.不要手动调用onPreExecute()，doInBackground(Params... params)，onProgressUpdate(Progress... values)，onPostExecute(Result result)这几个方法。
 4.不能在doInBackground(Params... params)中更改UI组件的信息。
 5.一个任务实例只能执行一次，如果执行第二次将会抛出异常。
 接下来，我们来看看如何使用AsyncTask执行异步任务操作，我们先建立一个项目，结构如下：
 */

public class AsyncTaskActivity extends Activity implements Button.OnClickListener {

    TextView textView = null;
    Button btnDownload = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async_task);
        textView = (TextView)findViewById(R.id.textView);
        btnDownload = (Button)findViewById(R.id.btnDownload);
        LogUtils.d("MainActivity -> onCreate, Thread name: " + Thread.currentThread().getName());
    }

    @Override
    public void onClick(View v) {
        //要下载的文件地址
        String[] urls = {
                "http://blog.csdn.net/iispring/article/details/47115879",
                "http://blog.csdn.net/iispring/article/details/47180325",
                "http://blog.csdn.net/iispring/article/details/47300819",
                "http://blog.csdn.net/iispring/article/details/47320407",
                "http://blog.csdn.net/iispring/article/details/47622705"
        };

        DownloadTask downloadTask = new DownloadTask();
        downloadTask.execute(urls);
    }

    //public abstract class AsyncTask<Params, Progress, Result>
    //在此例中，Params泛型是String类型，Progress泛型是Object类型，Result泛型是Long类型
    private class DownloadTask extends AsyncTask<String, Object, Long> {
        @Override
        protected void onPreExecute() {
            LogUtils.d ("DownloadTask -> onPreExecute, Thread name: " + Thread.currentThread().getName());
            super.onPreExecute();
            btnDownload.setEnabled(false);
            textView.setText("开始下载...");
        }
        @Override
        protected Long doInBackground(String... params) {
            LogUtils.d ("DownloadTask -> doInBackground, Thread name: " + Thread.currentThread().getName());
            //totalByte表示所有下载的文件的总字节数
            long totalByte = 0;
            //params是一个String数组
            for(String url: params){
                //遍历Url数组，依次下载对应的文件
                Object[] result = downloadSingleFile(url);
                int byteCount = (int)result[0];
                totalByte += byteCount;
                //在下载完一个文件之后，我们就把阶段性的处理结果发布出去
                publishProgress(result);
                //如果AsyncTask被调用了cancel()方法，那么任务取消，跳出for循环
                if(isCancelled()){
                    break;
                }
            }
            //将总共下载的字节数作为结果返回
            return totalByte;
        }

        //下载文件后返回一个Object数组：下载文件的字节数以及下载的博客的名字
        private Object[] downloadSingleFile(String str){
            Object[] result = new Object[2];
            int byteCount = 0;
            String blogName = "";
            HttpURLConnection conn = null;
            try{
                URL url = new URL(str);
                conn = (HttpURLConnection)url.openConnection();
                InputStream is = conn.getInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buf = new byte[1024];
                int length = -1;
                while ((length = is.read(buf)) != -1) {
                    baos.write(buf, 0, length);
                    byteCount += length;
                }
                String respone = new String(baos.toByteArray(), "utf-8");
                int startIndex = respone.indexOf("<title>");
                if(startIndex > 0){
                    startIndex += 7;
                    int endIndex = respone.indexOf("</title>");
                    if(endIndex > startIndex){
                        //解析出博客中的标题
                        blogName = respone.substring(startIndex, endIndex);
                    }
                }
            }catch(MalformedURLException e){
                e.printStackTrace();
            }catch(IOException e){
                e.printStackTrace();
            }
             finally {
                if(conn != null){
                    conn.disconnect();
                }
            }
            result[0] = byteCount;
            result[1] = blogName;
            return result;
        }

        @Override
        protected void onProgressUpdate(Object... values) {
            LogUtils.d("DownloadTask -> onProgressUpdate, Thread name: " + Thread.currentThread().getName());
            super.onProgressUpdate(values);
            int byteCount = (int)values[0];
            String blogName = (String)values[1];
            String text = textView.getText().toString();
            text += "\n博客《" + blogName + "》下载完成，共" + byteCount + "字节";
            textView.setText(text);
        }

        @Override
        protected void onPostExecute(Long aLong) {
            LogUtils.d("DownloadTask -> onPostExecute, Thread name: " + Thread.currentThread().getName());
            super.onPostExecute(aLong);
            String text = textView.getText().toString();
            text += "\n全部下载完成，总共下载了" + aLong + "个字节";
            textView.setText(text);
            btnDownload.setEnabled(true);
        }
        @Override
        protected void onCancelled() {
            LogUtils.d ("DownloadTask -> onCancelled, Thread name: " + Thread.currentThread().getName());
            super.onCancelled();
            textView.setText("取消下载");
            btnDownload.setEnabled(true);
        }
    }
}