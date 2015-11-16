package codepig.passnote;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

/**
 * 设置面板的fragment
 * Created by QZD on 2015/3/9.
 */
public class settingFragment extends PreferenceFragment {
    private Activity settingActivity;
    private SharedPreferences.Editor editor;
    private PreferenceScreen aboutMe;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Log.d("LOGCAT","setting creat");
        // 从xml文件加载选项
        addPreferencesFromResource(R.xml.preferences);
        settingActivity=getActivity();
        init();
    }

    //编辑设置数据
    public void editSetting(String _key,int _v){
        editor.putInt(_key,_v);
        editor.commit();
    }

    private void init(){
        aboutMe=(PreferenceScreen) findPreference("aboutMe");
        aboutMe.setOnPreferenceClickListener(settingClick);
//        SharedPreferences settings = settingActivity.getSharedPreferences("BoosjPlayerSetting", Context.MODE_PRIVATE);
//        editor = settings.edit();
        //根据之前的设置显示开关状态。（主要是重新安装以后，其他时候其实没意义）
    }

    //监听设置点击
    Preference.OnPreferenceClickListener settingClick=new Preference.OnPreferenceClickListener(){
        @Override
        public boolean onPreferenceClick(Preference pref) {
            if (pref.getKey().equals("aboutMe")){
                Uri uri = Uri.parse("qzdszz@163.com");
                startActivity(new Intent(Intent.ACTION_VIEW,uri));
            }
            return true;
        }
    };
}
