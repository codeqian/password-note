package codepig.passnote;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import codepig.passnote.Utils.dataCenter;
import codepig.passnote.math.codeFactory;
import codepig.passnote.math.filemanager;

/**
 * 设置面板的activity
 * Created by QZD on 2015/3/9.
 */
public class settingActivity extends Activity {
    private Context context;
    private LinearLayout backBtn,connectBtn,githubBtn;
    private EditText old_t,new_t,dir_t;
    private Button okBtn,saveBtn,backupBtn,recoverBtn;
    private SharedPreferences.Editor editor;
    private SharedPreferences settings;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context=this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_l);
        init();
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        Toast.makeText(context, msg.getData().getString("Msg"), Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(context, msg.getData().getString("Msg"), Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(context, msg.getData().getString("Msg"), Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                super.handleMessage(msg);
            }
        };
    }
    private void init(){
        backBtn=(LinearLayout) findViewById(R.id.backBtn);
        connectBtn=(LinearLayout) findViewById(R.id.connectBtn);
        githubBtn=(LinearLayout) findViewById(R.id.githubBtn);
        old_t=(EditText) findViewById(R.id.old_t);
        new_t=(EditText) findViewById(R.id.new_t);
        dir_t=(EditText) findViewById(R.id.dir_t);
        okBtn=(Button) findViewById(R.id.okBtn);
        saveBtn=(Button) findViewById(R.id.saveBtn);
        backupBtn=(Button) findViewById(R.id.backupBtn);
        recoverBtn=(Button) findViewById(R.id.recoverBtn);
        backBtn.setOnClickListener(clickBtn);
        connectBtn.setOnClickListener(clickBtn);
        githubBtn.setOnClickListener(clickBtn);
        okBtn.setOnClickListener(clickBtn);
        saveBtn.setOnClickListener(clickBtn);
        backupBtn.setOnClickListener(clickBtn);
        recoverBtn.setOnClickListener(clickBtn);
    }

    //监听按钮
    protected View.OnClickListener clickBtn = new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.backBtn://关闭
                    finish();
                    break;
                case R.id.connectBtn://发邮件
                    Uri mailUri = Uri.parse("mailto:qzdszz@163.com");
                    startActivity(new Intent(Intent.ACTION_SENDTO,mailUri));
                    break;
                case R.id.githubBtn:
                    Uri gitUri = Uri.parse("https://github.com/codeqian/password-note/");
                    startActivity(new Intent(Intent.ACTION_VIEW,gitUri));
                    break;
                case R.id.okBtn://修改口令
                    settings = getSharedPreferences("pwNoteSetting", Context.MODE_PRIVATE);
                    editor = settings.edit();
                    if(!codeFactory.key2Md5(old_t.getText().toString()).equals(settings.getString("cameBefore", ""))){
                        Toast.makeText(context, "旧暗号对不上啊！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(new_t.getText().toString().equals("")){
                        Toast.makeText(context, "您还没输入新口令呢！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String checkMsg= codeFactory.checkWordLength(new_t.getText().toString());
                    if(checkMsg.equals("tooLong")){
                        Toast.makeText(context, "口令长度不能超过16个字符！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(checkMsg.equals("notLetter")){
                        Toast.makeText(context, "口令只能包含数字和英文字母！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    savePassword(new_t.getText().toString());
                    break;
                case R.id.saveBtn://保存文本
                    Runnable homeInfoRunnable=new Runnable() {
                        @Override
                        public void run() {
                            String saveCheck=filemanager.saveList2txt(dir_t.getText().toString());
                            Message msg = new Message();
                            msg.what = 0;
                            Bundle bundle = new Bundle();
                            bundle.putString("Msg", saveCheck);
                            msg.setData(bundle);
                            mHandler.sendMessage(msg);
                        }
                    };
                    homeInfoRunnable.run();
                    break;
                case R.id.backupBtn:
                    Runnable backupRunnable=new Runnable() {
                        @Override
                        public void run() {
                            String saveCheck=filemanager.dataBackup();
                            Message msg = new Message();
                            msg.what = 1;
                            Bundle bundle = new Bundle();
                            bundle.putString("Msg", saveCheck);
                            msg.setData(bundle);
                            mHandler.sendMessage(msg);
                        }
                    };
                    backupRunnable.run();
                    break;
                case R.id.recoverBtn:
                    Runnable recoverRunnable=new Runnable() {
                        @Override
                        public void run() {
                            String saveCheck=filemanager.recoverData();
                            Message msg = new Message();
                            msg.what = 2;
                            Bundle bundle = new Bundle();
                            bundle.putString("Msg", saveCheck);
                            msg.setData(bundle);
                            mHandler.sendMessage(msg);
                        }
                    };
                    recoverRunnable.run();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 更改口令
     * @param password_t
     */
    public void savePassword(String password_t){
        editor.putString("cameBefore", codeFactory.key2Md5(password_t));
        editor.commit();
        dataCenter.theWords=password_t;
        codeFactory.reEncodeWords();
        Toast.makeText(context, "口令修改成功！", Toast.LENGTH_SHORT).show();
        finish();
    }
}
