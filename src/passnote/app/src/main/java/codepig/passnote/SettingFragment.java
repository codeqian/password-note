package codepig.passnote;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.util.Log;

import codepig.passnote.Utils.dataCenter;
import codepig.passnote.Utils.CodeFactory;

/**
 * 设置面板的fragment(暂不使用)
 * Created by QZD on 2015/3/9.
 */
public class SettingFragment extends PreferenceFragment {
    private Activity settingActivity;
    private SharedPreferences.Editor editor;
    private PreferenceScreen mailMe;
    private EditTextPreference newKey;

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
    public void savePassword(String password_t){
        editor.putString("cameBefore", CodeFactory.key2Md5(password_t));
        editor.commit();
        dataCenter.theWords=password_t;
        CodeFactory.reEncodeWords();
    }

    private void init(){
        mailMe=(PreferenceScreen) findPreference("mailMe");
        newKey=(EditTextPreference) findPreference("newKey");
        mailMe.setOnPreferenceClickListener(settingClick);
        newKey.setOnPreferenceClickListener(settingClick);
        newKey.setOnPreferenceChangeListener(settingChange);
        SharedPreferences settings = settingActivity.getSharedPreferences("pwNoteSetting", Context.MODE_PRIVATE);
        editor = settings.edit();
    }

    /**
     * 监听设置点击
     */
    Preference.OnPreferenceClickListener settingClick=new Preference.OnPreferenceClickListener(){
        @Override
        public boolean onPreferenceClick(Preference pref) {
            Log.d("LOGCAT", "click:" + pref.getKey());
            if (pref.getKey().equals("mailMe")){
                Uri uri = Uri.parse("mailto:qzdszz@163.com");
                startActivity(new Intent(Intent.ACTION_SENDTO,uri));
            }
            return true;
        }
    };

    /**
     * 监听设置开关
     */
    Preference.OnPreferenceChangeListener settingChange=new Preference.OnPreferenceChangeListener(){
        @Override
        public boolean onPreferenceChange(Preference pre,Object newValue) {
            Log.d("LOGCAT", "change:" + pre.getKey());
            if (pre.getKey().equals("newKey")){
                savePassword(newKey.getText().toString());
            }
            return true;
        }
    };
}
