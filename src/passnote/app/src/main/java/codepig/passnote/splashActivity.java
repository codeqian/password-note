package codepig.passnote;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import codepig.passnote.Utils.config;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_l);
        context = this;

        enterBtn=(Button) findViewById(R.id.enterBtn);
        password_t=(EditText) findViewById(R.id.password_t);
        passwordCheck_t=(EditText) findViewById(R.id.passwordCheck_t);
        enterBtn.setOnClickListener(enterApp);
        //>>>>先判断是否第一次
        if(!firstTime){
            passwordCheck_t.setVisibility(View.GONE);
        }
    }

    /**
     * enter按钮点击事件
     */
    private View.OnClickListener enterApp = new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
//            if(firstTime){
//                if(password_t.getText().toString().equals("")){
//                    Toast.makeText(context, "同学，您还啥都没输入呢！", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                if(!password_t.getText().toString().equals(passwordCheck_t.getText().toString())){
//                    Toast.makeText(context, "同学，您两次输入不一样啊！", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//            }
            config.theWords=password_t.getText().toString();
            startActivity(new Intent(getApplication(), MainActivity.class));
            finish();
        }
    };
}
