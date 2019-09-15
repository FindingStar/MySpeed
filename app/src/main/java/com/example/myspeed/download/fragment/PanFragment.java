package com.example.myspeed.download.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myspeed.R;
import com.example.myspeed.download.adapter.PanWebClient;

public class PanFragment extends Fragment {
    private static final String TAG = "PanFragment";
    private WebView webView;

    private static PanFragment PanFragment;

    private Thread viewThread;

    private PanFragment() {

    }

    public static PanFragment newInstance() {
        if (PanFragment == null) {
            PanFragment=new PanFragment();
            return PanFragment;
        }
        return PanFragment;
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.pan_fragment, container, false);

        webView = view.findViewById(R.id.pan_wv);

        final WebSettings webSettings = webView.getSettings();
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setJavaScriptEnabled(true);

        webView.setWebViewClient(new PanWebClient(getActivity()));
        webView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if ((keyCode == KeyEvent.KEYCODE_BACK) && (webView.canGoBack())) {
                        webView.goBack();
                        return true;
                    }
                }
                return false;
            }
        });
        webView.loadUrl("https://pan.baidu.com/");

        return view;
    }

    public void backUi() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                webView.loadUrl("https://pan.baidu.com/");
            }
        });


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }
}
