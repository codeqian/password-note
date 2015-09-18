package codepig.passnote.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import codepig.passnote.R;
import codepig.passnote.Utils.accountData;

/**
 * 内容单页view
 * Created by QZD on 2015/9/17.
 */
public class expandPaper extends LinearLayout {
    private ImageView openBtn;
    private TextView index_t,title_t,account_t,password_t,info_t;
    private boolean opened=true;
    private int durationMillis = 350;
    private LinearLayout mainBody,contentPage;
    public expandPaper(Context context) {
        super(context);
        init(context);
    }

    public expandPaper(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        LayoutInflater.from(context).inflate(R.layout.expandpaper_l, this);
        mainBody=(LinearLayout) findViewById(R.id.mainBody);
        contentPage=(LinearLayout) findViewById(R.id.contentPage);
        openBtn=(ImageView) findViewById(R.id.openBtn);
        index_t=(TextView) findViewById(R.id.index_t);
        title_t=(TextView) findViewById(R.id.title_t);
        account_t=(TextView) findViewById(R.id.account_t);
        password_t=(TextView) findViewById(R.id.password_t);
        info_t=(TextView) findViewById(R.id.info_t);

        openBtn.setOnClickListener(btnClick);
    }

    /**
     * 设置具体信息
     * @param _data
     */
    public void showData(accountData _data){
        index_t.setText(Integer.toString(_data.paperId+1));
        title_t.setText(_data.paperName);
        account_t.setText(_data.account);
        password_t.setText(_data.password);
        info_t.setText(_data.info);
        Log.d("LOGCAT","height:"+contentPage.getHeight());
    }

    /**
     * 按钮事件
     */
    private View.OnClickListener btnClick = new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.openBtn:
                    //展开或关闭内容
                    mainBody.clearAnimation();
                    if(opened){
                        opened=false;
                        RotateAnimation animation = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                        animation.setDuration(durationMillis);
                        animation.setFillAfter(true);
                        openBtn.startAnimation(animation);
                    }else {
                        opened=true;
                        RotateAnimation animation = new RotateAnimation(180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                        animation.setDuration(durationMillis);
                        animation.setFillAfter(true);
                        openBtn.startAnimation(animation);
                    }
                    break;
                default:
                    break;
            }
        }
    };
}
