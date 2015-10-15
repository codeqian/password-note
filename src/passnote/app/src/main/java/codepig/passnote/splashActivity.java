package codepig.passnote;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import codepig.passnote.Utils.config;
import codepig.passnote.Utils.dataCenter;
import codepig.passnote.math.codeFactory;

/**
 * 引导页
 * Created by QZD on 2015/9/15.
 */
public class splashActivity extends Activity {
    private Context context;
    private Button enterBtn;
    private EditText password_t;
    private EditText passwordCheck_t;
    private boolean firstTime=true;
    private SharedPreferences.Editor editor;
    private SharedPreferences settings;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_l);
        context = this;

        enterBtn=(Button) findViewById(R.id.enterBtn);
        password_t=(EditText) findViewById(R.id.password_t);
        passwordCheck_t=(EditText) findViewById(R.id.passwordCheck_t);
        enterBtn.setOnClickListener(enterApp);
        settings = getSharedPreferences("pwNoteSetting", Context.MODE_PRIVATE);
        editor = settings.edit();
        String cameBefore=settings.getString("cameBefore", "");
        if(!cameBefore.equals("")){
            passwordCheck_t.setVisibility(View.GONE);
            firstTime=false;
        }
    }

    /**
     * enter按钮点击事件
     */
    private View.OnClickListener enterApp = new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
            if(firstTime){
                if(password_t.getText().toString().equals("")){
                    Toast.makeText(context, "您还啥都没输入呢！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!password_t.getText().toString().equals(passwordCheck_t.getText().toString())){
                    Toast.makeText(context, "您两次输入不一样啊！", Toast.LENGTH_SHORT).show();
                    return;
                }
                editor.putString("cameBefore", codeFactory.key2Md5(password_t.getText().toString()));
                editor.commit();
            }else{
                //判断口令是否正确
                if(!codeFactory.key2Md5(password_t.getText().toString()).equals(settings.getString("cameBefore", ""))){
                    Toast.makeText(context, "暗号对不上啊！", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            dataCenter.theWords= password_t.getText().toString();
            startActivity(new Intent(getApplication(), MainActivity.class));
            finish();
        }
    };
}
