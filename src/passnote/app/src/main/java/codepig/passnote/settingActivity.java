package codepig.passnote;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * 设置面板的activity
 * Created by QZD on 2015/3/9.
 */
public class settingActivity extends Activity {
    //设置面板fragment对象
    private settingFragment settingFm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settingmain);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        settingFm = new settingFragment();
        fragmentTransaction.replace(R.id.content, settingFm);
        fragmentTransaction.commit();
        init();
    }
    private void init(){
        findViewById(R.id.titleBar).setOnClickListener(clickBtn);
    }

    //监听按钮
    private View.OnClickListener clickBtn = new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.titleBar:
                    finish();
                    break;
                default:
                    break;
            }
        }
    };
}
