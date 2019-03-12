package com.example.pr06_version2.base;

import com.example.pr06_version2.data.model.User;

public interface OnUserClickListener {
    void onEditUser(User user);

    void onDeleteUser(User user);
}
