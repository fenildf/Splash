package com.example.keranbing.splash.helper.viewsHelper.dialogHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by keranbing on 2016/8/5.
 */
public class DialogHelper {
    /*
    * @context 上下文对象
    * @layout 自定义的dialog样式布局文件
    * @sureId 确定按钮的Id
    * @cancelId 取消按钮的Id
    * @titleId 对话框Textview 的id
    * @title 对话框显示的文本
    * @position 考虑到可能是点击列表的某一项，比如说删除某一项，提示用户是否确定删除，传入下标position，
    *           在回调接口中返回给用户，方便用户获取列表点击项的数据，如果不是这种情况，则可随便传个数值即可，
    * @dialogPromptListener 回调接口
    * */
    public static void createPromptDialog(Activity context, int layout, int sureId, int cancelId, int titleId, String title, final int position, final DialogPromptListener dialogPromptListener) {
        final Dialog builder = new AlertDialog.Builder(context).create();  //先得到构造器
        View view = LayoutInflater.from(context).inflate(layout, null);
        ((TextView) view.findViewById(titleId)).setText(title);
        //监听返回键
        ((Button) view.findViewById(cancelId)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.dismiss();
            }
        });

        ((Button) view.findViewById(sureId)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogPromptListener.setOnPrompItemClick(position);
                builder.dismiss();
            }
        });
        builder.show();
        builder.setCancelable(false);//取消底部返回键事件
        builder.getWindow().setContentView(view);
        builder.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }



    public interface DialogListviewListener {
        public void setOnlistViewItemClick(JSONObject jsonObject);
    }


    public interface DialogPromptListener {
        public void setOnPrompItemClick(int position);
    }

}