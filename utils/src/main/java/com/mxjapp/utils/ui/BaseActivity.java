package com.mxjapp.utils.ui;

import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.mxjapp.utils.R;
import com.mxjapp.utils.listener.OnRefreshListener;
import com.mxjapp.utils.util.ToastUtil;


/**
 * creator: jason
 * date: 2017/2/10
 * note:
 */

public class BaseActivity extends AppCompatActivity {
    protected View errorContainer,contentContainer,loadingContainer;
    protected Toolbar toolbar;
    private TextView titleTV,rightTV;
    protected SwipeRefreshLayout refreshLayout;

    /**
     * 初始化toolbar
     * @param title 标题
     * @param rightText 右侧文本
     * @param back 是否包含返回
     */
    protected void initToolBar(String title,String rightText,boolean back){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            titleTV=findViewById(R.id.toolbar_title);
            if(!TextUtils.isEmpty(title)){
                titleTV.setText(title);
            }
            if(!TextUtils.isEmpty(rightText)) {
                rightTV=findViewById(R.id.toolbar_right);
                rightTV.setText(rightText);
            }
            if(back){
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
            }
        }
    }
    protected void initToolBar(String title,String rightText){
        initToolBar(title,rightText,true);
    }
    protected void initToolBar(int resId,boolean back){
        initToolBar(getString(resId),null,back);
    }
    protected void initToolBar(String title){
        initToolBar(title,null,true);
    }
    protected void initToolBar(int resId){initToolBar(getString(resId),null,true);}
//    protected void initContainer(View.OnClickListener reload){
//        errorContainer=findViewById(R.id.error_container);
//        contentContainer=findViewById(R.id.container);
//        findViewById(R.id.error_reload).setOnClickListener(reload);
//        errorContainer.setVisibility(View.GONE);
//        contentContainer.setVisibility(View.GONE);
//    }
    //初始化refresh layout;
    protected void initRefreshLayout(SwipeRefreshLayout.OnRefreshListener refreshListener){
        refreshLayout=findViewById(R.id.refresh);
        refreshLayout.setColorSchemeColors(ContextCompat.getColor(this,));
        refreshLayout.setOnRefreshListener(refreshListener);
    }

    /**
     * 初始化容器（正常页面(xml中id为container)、加载中页面、错误页面）
     * @param onRefreshListener 刷新调用
     */
    protected void initContainer(final OnRefreshListener onRefreshListener){
        contentContainer=findViewById(R.id.container);
        errorContainer=getLayoutInflater().inflate(R.layout.view_net_error,(ViewGroup) contentContainer.getParent(),false);
        loadingContainer=getLayoutInflater().inflate(R.layout.view_loading,(ViewGroup) contentContainer.getParent(),false);
        ((ViewGroup)contentContainer.getParent()).addView(errorContainer);
        ((ViewGroup)contentContainer.getParent()).addView(loadingContainer);
        errorContainer.findViewById(R.id.error_reload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRefreshListener.onRefresh();
            }
        });
        errorContainer.setVisibility(View.GONE);
        contentContainer.setVisibility(View.GONE);

    }
    protected void showErrorContainer(){
        errorContainer.setVisibility(View.VISIBLE);
        contentContainer.setVisibility(View.GONE);
        loadingContainer.setVisibility(View.GONE);
        if(refreshLayout!=null) refreshLayout.setRefreshing(false);
    }
    protected void showContentContainer(){
        errorContainer.setVisibility(View.GONE);
        loadingContainer.setVisibility(View.GONE);
        contentContainer.setVisibility(View.VISIBLE);
        if(refreshLayout!=null) refreshLayout.setRefreshing(false);
    }
    protected void showLoadingContainer(){
        if(refreshLayout!=null){
//            loadingContainer.setVisibility(View.GONE);
            refreshLayout.setRefreshing(true);
            errorContainer.setVisibility(View.GONE);
        }else {
            loadingContainer.setVisibility(View.VISIBLE);
            errorContainer.setVisibility(View.GONE);
            contentContainer.setVisibility(View.GONE);
        }
    }
    //更改标题
    protected void changeTitle(String s){
        if(titleTV!=null) titleTV.setText(s);
    }
    //更改状态栏颜色
    protected void changeStatusBarColor(int color){
        if(Build.VERSION.SDK_INT>=23) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }
    //默认更改状态栏为白底黑字
    protected void changeStatusBarColor(){
        if(Build.VERSION.SDK_INT>=23) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            changeStatusBarColor(Color.WHITE);
        }
    }

    protected void alert(String s,int time){
        ToastUtil.showToast(getApplicationContext(),s,time);
    }
    protected void alert(String s){alert(s,Toast.LENGTH_SHORT);}
    protected void alert(int resId,int time){alert(getString(resId),time);}
    protected void alert(int resId){alert(resId,Toast.LENGTH_SHORT);}
    protected synchronized void alertNetBroken(){alert(R.string.error_net_broken);}

    protected void log(String s){
        Log.i("SSSSSSSSSSSSSSSSS",s);
    }
    protected void setToolbarRightClickListener(View.OnClickListener listener){
        if(rightTV!=null) rightTV.setOnClickListener(listener);
    }
}
