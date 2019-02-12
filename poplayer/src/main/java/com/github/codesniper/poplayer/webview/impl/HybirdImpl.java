package com.github.codesniper.poplayer.webview.impl;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;


import com.github.codesniper.poplayer.custom.PopWebView;
import com.github.codesniper.poplayer.util.PopUtils;
import com.github.codesniper.poplayer.webview.inter.HybirdManager;

import com.github.codesniper.poplayer.webview.service.PopWebViewService;

import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

import static com.github.codesniper.poplayer.config.LayerConfig.POP_TAG;

/**
 *  Poplayer默认的交互机制
 */

public class HybirdImpl implements HybirdManager {


    @Override
    public void injectJsBridge(WebView webView,String JsName) {
        webView.loadUrl("javascript:" + PopUtils.getJsCode(webView.getContext(),JsName));
        Log.d(POP_TAG,"注入JSBrige成功");
    }

    /**
     * 进来的入口 3个 1.原生2.jsprompt 3.post请求拦截
     * @param instruction
     */
    @Override
    public void invokeAppServices(String instruction) {

        //{"invokeId":"name_2_1549953808581","methodName":"name","methodParams":"123"}
        //解析
        //hrzapi.invoke("printService",{'name':'123','param1':'123'});

        Log.d(POP_TAG,"接收到指令"+instruction);

        if(TextUtils.isEmpty(instruction)){
            return;
        }

        try{
            PopWebViewService popWebViewService=null;

            JSONObject jsonObject = new JSONObject(instruction.substring(instruction.indexOf("{"), instruction.lastIndexOf("}") + 1));

            String invokeId=jsonObject.getString("invokeId");

            String methodName = jsonObject.getString("methodName");
          //  String android_methodName = methodName.split(Pattern.quote("."))[1];

            JSONObject paramObject = jsonObject.getJSONObject("methodParams");

            Iterator iterator = paramObject.keys();
            Map map = new HashMap();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                map.put(key, paramObject.getString(key));
            }
            map.put("invokeId",invokeId);

//        map.put("context", BrowserActivity.this);
//        map.put("webview",webView);



            //前端调原生 方法集合类
            Class<PopWebViewService> invokeMethodObject = (Class<PopWebViewService>) Class.forName(PopWebViewService.class.getName());

            if (invokeMethodObject != null) {
                popWebViewService = invokeMethodObject.newInstance();
            }

            if (invokeMethodObject != null) {
                Method repay1 = invokeMethodObject.getDeclaredMethod(methodName, Map.class);
                if (repay1 != null&& popWebViewService!=null) {
                    repay1.setAccessible(true);
                    repay1.invoke(popWebViewService, map);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }


}
