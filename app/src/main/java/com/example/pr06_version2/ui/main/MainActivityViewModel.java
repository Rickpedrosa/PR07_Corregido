package com.example.pr06_version2.ui.main;

import android.widget.ImageView;

import com.example.pr06_version2.data.local.Database;
import com.example.pr06_version2.data.model.Avatar;
import com.example.pr06_version2.data.model.User;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainActivityViewModel extends ViewModel {

    private Database database;
    private LiveData<List<User>> usersCache;
    private MutableLiveData<Avatar> avatarLiveData;

    MainActivityViewModel(Database database) {
        this.database = database;
    }

    /**********************************************/
    public LiveData<List<User>> getUsers() {
        if (usersCache == null) {
            usersCache = database.getUsers();
        }
        return usersCache;
    }

    public void deleteUser(User user) {
        database.deleteUser(user);
    }

    /**********************************************/

    public MutableLiveData<Avatar> getAvatarLiveData() {
        if (avatarLiveData == null) {
            avatarLiveData = database.getAvatarLiveData();
        }
        return avatarLiveData;
    }

    public void setAvatarToObserve(Avatar avatar) {
        database.updateAvatarLiveData(avatar);
    }

    public Avatar getDefaultAvatar() {
        return database.getDefaultAvatar();
    }

    public List<Avatar> queryAvatars() {
        return database.queryAvatars();
    }

    public void tintSavedImageV2(Avatar avatar, List<Avatar> listCat, List<ImageView> imgCat) {
        for (ImageView anImgCat : imgCat) {
            anImgCat.setAlpha(1f);
        }
        for (int i = 0; i < listCat.size(); i++) {
            if (avatar.getId() == listCat.get(i).getId()) {
                imgCat.get(i).setAlpha(0.5f);
            }
        }
    }

    /**********************************************/


    public void addUser(User user) {
        int id = database.getNextIdAvailable();
        user.setId(id);
        database.addUser(user);
    }

    public void editUser(User user, String name, String email, String address, String web, int tlf, Avatar avatar) {
        database.editUser(user, name, email, address, web, tlf, avatar);
    }

}
