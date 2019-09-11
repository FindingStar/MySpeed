package com.example.myspeed.pan;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.myspeed.MainActivity;
import com.example.myspeed.R;
import com.example.myspeed.base.MyFragmentManager;
import com.example.myspeed.base.MyFragmentTag;
import com.example.myspeed.download.DownloadFragment;
import com.example.myspeed.progress.ProgressFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import static android.content.Context.MODE_PRIVATE;

public class PanWebClient extends WebViewClient {

    private Context context;

    private Map<String, String> querys = new HashMap<>();
    private boolean downed = false;

    // path , random ,app_id, cookie
    private String baseUrl = "https://pcs.baidu.com/rest/2.0/pcs/file?method=download";
    private Pattern pattern;
    private Matcher matcher;

    public PanWebClient(Context context) {
        this.context = context;
    }


    public static final String TAG = "PanWebClient";

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        view.loadUrl(request.getUrl().toString());
        return true;
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {

        sameDownload(request);
        return super.shouldInterceptRequest(view, request);

    }


    /**
     * 通过 webview 下载
     * 统一格式
     *
     * @param request
     */
    private void sameDownload(WebResourceRequest request) {

        //app_id
        if (querys.get("app_id") == null) {
            if (request.getUrl().getQueryParameter("app_id") != null) {
                querys.put("app_id", request.getUrl().getQueryParameter("app_id"));
                return;
            }
        }

        // path = u+ "%2F" +tt
        if (querys.get("u") == null) {

            pattern = Pattern.compile("https://hm.baidu.com/hm.gif?.+");
            matcher = pattern.matcher(request.getUrl().toString());
            String u = null;
            if (matcher.matches()) {
                u = request.getUrl().getQueryParameter("u");
                if (u != null) {
                    if (u.contains("=")) {
                        int start = u.indexOf("=") + 1;
                        int end = u.indexOf("&");
                        u = u.substring(start, end);
                        querys.put("u", u);
                        return;
                    }
                }
            }

        }

        if (querys.get("tt") == null) {
            String tt = null;
            pattern = Pattern.compile("https://hm.baidu.com/hm.gif?.+");
            matcher = pattern.matcher(request.getUrl().toString());
            if (matcher.matches()) {
                tt = request.getUrl().getQueryParameter("tt");
                if ((tt != null) && (!tt.equals("百度网盘-我的文件")) && (!tt.equals("百度网盘，让美好永远陪伴"))) {
                    try {
                        tt = URLEncoder.encode(tt, "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    String[] ts = tt.split("%7C");
                    tt = ts[0];
                    querys.put("tt", tt);
                    return;
                }
            }
        }


        String ep = request.getUrl().getQueryParameter("ep");
        if (ep != null) {
            pattern = Pattern.compile("wap_click_highDownload_single?.+");
            matcher = pattern.matcher(ep);
            if (matcher.matches()) {

                if ((querys.get("u")==null)|(querys.get("tt")==null)){
                    return;
                }

                //random
                Random random = new Random();

                String path = querys.get("u") + "%2F" + querys.get("tt");
                final String url = baseUrl + "&path=" + path + "&random=" + random.nextInt(1) + "&app_id=" + querys.get("app_id");

                //Cookie
                CookieManager cookieManager = CookieManager.getInstance();
                final String cookie = cookieManager.getCookie("https://pcs.baidu.com/rest/2.0/pcs/file?method=plantcookie&type=ett");

                // 网盘下载调用，先得实例化 fragment
                Bundle args = new Bundle();
                args.putString("flag", "pan_download");

                MyFragmentManager myFm = MyFragmentManager.getInstance();
                final ProgressFragment progressFragment = (ProgressFragment) myFm.getFragment(MyFragmentTag.PROGRESS);
                progressFragment.setArguments(args);
                myFm.splide(MyFragmentTag.PROGRESS);

                new Thread() {
                    @Override
                    public void run() {
                        try {
                            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                            connection.setRequestProperty("Cookie", cookie);
                            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36");

                            Map<String, String> params = new HashMap<>();
                            params.put("Cookie", cookie);
                            params.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36");
                            progressFragment.download(url, connection, params);


                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                querys.clear();


            }

        }

    }








    /*
        小文件下载-- 构造部分参数
     */
    public void smallDownload(WebResourceRequest request) {
        //get Refer
        Pattern pattern = Pattern.compile("https://pan.baidu.com/wap/home\\?realName=1&wapBehaviorSucc?.+");
        Matcher matcher = pattern.matcher(request.getUrl().toString());

        String referUrl = null;
        if (matcher.matches()) {
            referUrl = request.getUrl().toString();
            SharedPreferences sharedPref = context.getSharedPreferences("config", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("referUrl", referUrl);
            editor.commit();
        }

        // get  logid app_id
        Pattern pattern2 = Pattern.compile("https://pan.baidu.com/api/list?.+");
        Matcher matcher2 = pattern2.matcher(request.getUrl().toString());
        if (matcher2.matches()) {

            String query = request.getUrl().getQuery();

            int end = query.indexOf("&order=time");
            query = query.substring(0, end);
            SharedPreferences sharedPref = context.getSharedPreferences("config", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("queryUrl", query);
            editor.commit();
        }

        // get ....
        Pattern pattern1 = Pattern.compile("https://pan.baidu.com/wap/view?.+");
        Matcher matcher1 = pattern1.matcher(request.getUrl().toString());

        if (matcher1.matches()) {

            //get  sign1,sign3,   function s   ,fs_id

            final String url = request.getUrl().toString();

            SharedPreferences sharedPreferences = context.getSharedPreferences("config", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("downReferUrl", url);
            editor.commit();

            new Thread() {
                @Override
                public void run() {
                    SharedPreferences sPreferences = context.getSharedPreferences("config", MODE_PRIVATE);
                    String referUrl = sPreferences.getString("referUrl", null);
                    if (referUrl != null) {

                        Log.d(TAG, "run: " + url);
                        getSignRes(url);
                    } else {
                        Log.e(TAG, "run:   referUrl is  null");
                    }

                }
            }.start();

        }
    }

    /*
        小文件下载--- 获取具体的地址
     */
    public void getSignRes(String url) {
        try {
            CookieManager cookieManager = CookieManager.getInstance();
            String cookie = cookieManager.getCookie(url);

            SharedPreferences sPreferences = context.getSharedPreferences("config", MODE_PRIVATE);
            String referUrl = sPreferences.getString("referUrl", null);

            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestProperty("Cookie", cookie);
            connection.setRequestProperty("Upgrade-Insecure-Requests", "1");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 9; BKL-AL00 Build/HUAWEIBKL-AL00; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/75.0.3770.143 Mobile Safari/537.36");
            connection.setRequestProperty("X-Requested-With", "mark.via");
            connection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7");
            connection.setRequestProperty("Accept-Encoding", " gzip, deflate, br");
            connection.setRequestProperty("dnt", "1");
            connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");

            connection.setRequestProperty("Referer", referUrl);

            GZIPInputStream gzipIn = new GZIPInputStream(connection.getInputStream());
            StringBuilder sb = new StringBuilder();
            int i;
            while ((i = gzipIn.read()) != -1) {
                sb.append((char) i);
            }
            String res = sb.toString();
            int start = res.indexOf("window.yunData =") + "window.yunData =".length();
            int end = res.indexOf("\"};");
            JSONObject jsonObject = new JSONObject(res.substring(start, end + 2));
            String sign1 = jsonObject.getString("sign1");
            String sign3 = jsonObject.getString("sign3");
            String timestamp = jsonObject.getString("timestamp");
            String bdstoken = jsonObject.getString("bdstoken");


            JSONArray jsonArray = jsonObject.getJSONArray("file_list");
            JSONObject jsonObject1 = jsonArray.getJSONObject(0);
            String fs_id = jsonObject1.getString("fs_id");


            String sign = base64Encode(s(sign3, sign1));
            sign = sign.replace("=", "%3D");
            sign = sign.replace("+", "%2B");
            sign = sign.replace("/", "%2F");
            //Log.d(TAG, "getSignRes:   sign : " + sign + "    fs_id :  " + fs_id + "  timestamp : " + timestamp);


            // 1. get download url
            CookieManager cookieManager1 = CookieManager.getInstance();
            String downcookie = cookieManager1.getCookie("https://pcs.baidu.com/rest/2.0/pcs/file?method=plantcookie&type=ett");
            downcookie = downcookie + ";PANWEB=1";

            SharedPreferences sharedPreferences = context.getSharedPreferences("config", MODE_PRIVATE);
            String downUrl = sharedPreferences.getString("queryUrl", "");
            String downReferUrl = sharedPreferences.getString("downReferUrl", "");
            downUrl = "https://pan.baidu.com/api/download?" + downUrl;


            HttpURLConnection downConnection = (HttpURLConnection) new URL(downUrl).openConnection();
            downConnection.setRequestMethod("POST");
            downConnection.setRequestProperty("Cookie", downcookie);
            downConnection.setRequestProperty("Origin", "https://pan.baidu.com");
            downConnection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
            downConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 9; BKL-AL00 Build/HUAWEIBKL-AL00; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/75.0.3770.143 Mobile Safari/537.36");
            downConnection.setRequestProperty("Referer", downReferUrl);
            downConnection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7");
            downConnection.setRequestProperty("Accept-Encoding", " gzip, deflate, br");
            downConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            downConnection.setRequestProperty("Accept", "*/*");

            String body = "sign=" + sign + "&timestamp=" + timestamp + "&fidlist=%5B%22" + fs_id + "%22%5D&type=dlink";

            Log.d(TAG, "getSignRes:    down Url : " + downUrl + "\n downrefer : " + downReferUrl + "\n  downcookie : " + downcookie + "\nbody:  " + body);

            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(downConnection.getOutputStream()));
            out.write(body);
            out.close();

            GZIPInputStream in = new GZIPInputStream(downConnection.getInputStream());
            StringBuilder stringBuilder = new StringBuilder();
            int j;
            while ((j = in.read()) != -1) {
                stringBuilder.append((char) j);
            }
            //Log.d(TAG, "getSignRes:    downResponse : "+stringBuilder.toString());


            //2.
            JSONObject jsonObject2 = new JSONObject(stringBuilder.toString());
            JSONArray dlinkArray = jsonObject2.getJSONArray("dlink");
            JSONObject dlinkObj = dlinkArray.getJSONObject(0);
            String dlink = dlinkObj.getString("dlink");

            HttpURLConnection dlinkConnection = (HttpURLConnection) new URL(dlink).openConnection();
            dlinkConnection.setRequestProperty("Cookie", downcookie);
            dlinkConnection.setRequestMethod("GET");
            dlinkConnection.setRequestProperty("Upgrade-Insecure-Requests", "1");
            dlinkConnection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
            dlinkConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 9; BKL-AL00 Build/HUAWEIBKL-AL00; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/75.0.3770.143 Mobile Safari/537.36");
            dlinkConnection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7");
            dlinkConnection.setRequestProperty("Accept-Encoding", "gzip, deflate, br");

            Log.d(TAG, "getSignRes: " + dlinkConnection.getContentLength());
            InputStream dlinkIn = dlinkConnection.getInputStream();

            StringBuilder dlinkSb = new StringBuilder();
            int k;
            while ((k = dlinkIn.read()) != -1) {
                dlinkSb.append((char) k);
            }
            Log.d(TAG, "getSignRes:  dlink: " + dlinkSb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //  SSL证书
    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        handler.proceed();
    }

    /*
       小文件下载--- 生成初步的sign
    */
    public String s(String sign3, String sign1) {

        String j = sign3;
        String r = sign1;
        int[] a = new int[256];
        int[] p = new int[256];
        String o = "";
        int v = j.length();
        for (int q = 0; q < 256; q++) {
            a[q] = j.substring((q % v), v).charAt(0);
            p[q] = q;
        }
        for (int u = 0, q = 0; q < 256; q++) {
            u = (u + p[q] + a[q]) % 256;
            int t = p[q];
            p[q] = p[u];
            p[u] = t;
        }
        for (int i = 0, u = 0, q = 0; q < r.length(); q++) {
            i = (i + 1) % 256;
            u = (u + p[i]) % 256;
            int t = p[i];
            p[i] = p[u];
            p[u] = t;
            int k = p[((p[i] + p[u]) % 256)];
            o += (char) (((int) r.charAt(q)) ^ k);
        }
        return o;
    }

    /*
       小文件下载--- base64编码初步的sign，产出sign
    */
    public static String base64Encode(String e) {
        int t = 0, n = 0, a = 0, i = 0, o = 0;
        String r = "";
        String u = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

        for (n = e.length(), t = 0, r = ""; n > t; ) {
            if (a != 255) {
                a = e.charAt(t++);
            }
            if (t == n) {
                r += u.charAt(a >> 2);
                r += u.charAt((3 & a) << 4);
                r += "==";
                break;
            }
            i = e.charAt(t++);
            if (t == n) {
                r += u.charAt(a >> 2);
                r += u.charAt((3 & a) << 4 | (240 & i) >> 4);
                r += u.charAt((15 & i) << 2);
                r += "=";
                break;
            }
            o = e.charAt(t++);
            r += u.charAt(a >> 2);
            r += u.charAt((3 & a) << 4 | (240 & i) >> 4);
            r += u.charAt((15 & i) << 2 | (192 & o) >> 6);
            r += u.charAt(63 & o);
        }
        return r;
    }


}
