package br.com.delxmobile.fidelidade.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import br.com.delxmobile.fidelidade.model.User;


/**
 * Created by Guilherme on 05/12/2017.
 */
public class UserPreference {

    private static UserPreference instance;
    private static final String LOGGGED = "userLogged";
    private static final String NAME = "userNAme";
    private static final String EMAIL = "userEmail";
    private static final String IMAGE = "userImage";
    private static final String UID = "userUid";
    private static final String UPDATED_AT = "updatedAt";

    public static synchronized UserPreference getInstance(){
        if(instance==null)
            instance = new UserPreference();

        return instance;
    }

    private UserPreference(){

    }

    public UserPreference setUserLogged(Context context, boolean logged){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean(LOGGGED, logged);
        editor.commit();
        return this;
    }

    public boolean isUserLogged(Context context){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getBoolean(LOGGGED, false);
    }

    public User getLoggedUser(Context context){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        User user = new User();
        user.name = sharedPref.getString(NAME, "");
        user.email = sharedPref.getString(EMAIL, "");
        user.photo = sharedPref.getString(IMAGE, "");
        user.oId= sharedPref.getString(UID, "");
        user.updatedAt = sharedPref.getLong(UPDATED_AT, 0);

        return user;
    }

    public void setLoggedUser(Context context, User user){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(NAME, user.name);
        editor.putString(EMAIL, user.email);
        editor.putString(IMAGE, user.photo);
        editor.putString(UID, user.oId);
        editor.putLong(UPDATED_AT, user.updatedAt);
        editor.commit();
    }


}
