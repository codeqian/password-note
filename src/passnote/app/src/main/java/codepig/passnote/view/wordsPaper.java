package codepig.passnote.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import codepig.passnote.R;

/**
 * 内容单页view
 * Created by QZD on 2015/9/17.
 */
public class wordsPaper  extends LinearLayout {

    public wordsPaper(Context context) {
        super(context);
        init(context);
    }

    public wordsPaper(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        LayoutInflater.from(context).inflate(R.layout.wordspaper_l, this);
    }
}
