package com.example.pr06_version2.data.local;

import com.example.pr06_version2.R;
import com.example.pr06_version2.data.model.Avatar;
import com.example.pr06_version2.data.model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class Database {
    private static Database instance;

    private final ArrayList<Avatar> avatars = new ArrayList<>();
    private final Random random = new Random(1);
    private long count;

    private ArrayList<User> users;
    private MutableLiveData<List<User>> usersLiveData = new MutableLiveData<>();

    private Database() {
        insertAvatar(new Avatar(R.drawable.cat1, "Tom"));
        insertAvatar(new Avatar(R.drawable.cat2, "Luna"));
        insertAvatar(new Avatar(R.drawable.cat3, "Simba"));
        insertAvatar(new Avatar(R.drawable.cat4, "Kitty"));
        insertAvatar(new Avatar(R.drawable.cat5, "Felix"));
        insertAvatar(new Avatar(R.drawable.cat6, "Nina"));

        users = new ArrayList<>(Arrays.asList(
                new User(1, "Carremos", "lol@wtf.com", "SedePodemos",
                        "http://carremos.carre", 687969655, queryAvatars().get(0)),
                new User(2, "Pablemos", "aylmao@wtf.com", "SedeCiutadans",
                        "http://pablemos.vox", 687995033, queryAvatars().get(2))
        ));
        updateUsersLiveData();
    }

    public static Database getInstance() {
        if (instance == null) {
            synchronized (Database.class) {
                if (instance == null) {
                    instance = new Database();
                }
            }
        }
        return instance;
    }

    public LiveData<List<User>> getUsers() {
        return usersLiveData;
    }

    public int getNextIdAvailable() {
        return (users.size() != 0 ? users.get(users.size() - 1).getId() + 1 : 1);
    }

    private void updateUsersLiveData() {
        usersLiveData.setValue(new ArrayList<>(users));
    }

    public void deleteUser(User user) {
        users.remove(user);
        updateUsersLiveData();
    }

    public void addUser(User user) {
        users.add(user);
        updateUsersLiveData();
    }

    public void editUser(User user, String name, String email, String address, String web, int tlf, Avatar avatar) {
        user.setName(name);
        user.setEmail(email);
        user.setAddress(address);
        user.setWeb(web);
        user.setTlf(tlf);
        user.setImg_Avatar(avatar);
        updateUsersLiveData();
    }

    @VisibleForTesting()
    private void insertAvatar(Avatar avatar) {
        long id = ++count;
        avatar.setId(id);
        avatars.add(avatar);
    }

    public Avatar getRandomAvatar() {
        if (avatars.size() == 0) return null;
        return avatars.get(random.nextInt(avatars.size()));
    }

    public Avatar getDefaultAvatar() {
        if (avatars.size() == 0) return null;
        return avatars.get(0);
    }

    public List<Avatar> queryAvatars() {
        return new ArrayList<>(avatars);
    }

    public Avatar queryAvatar(long id) {
        for (Avatar avatar : avatars) {
            if (avatar.getId() == id) {
                return avatar;
            }
        }
        return null;
    }

    @VisibleForTesting
    public void setAvatars(List<Avatar> list) {
        count = 0;
        avatars.clear();
        avatars.addAll(list);
    }
}
