package codepig.passnote;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * 引导页
 * Created by QZD on 2015/9/15.
 */
public class splashActivity extends Activity {
    private Context context;
    private Button enterBtn;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_l);
        context = this;

        enterBtn=(Button) findViewById(R.id.enterBtn);
        enterBtn.setOnClickListener(enterApp);
    }

    private View.OnClickListener enterApp = new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
            startActivity(new Intent(getApplication(),MainActivity.class));
            finish();
        }
    };
}
