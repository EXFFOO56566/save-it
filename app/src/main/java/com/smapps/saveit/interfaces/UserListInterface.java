package com.smapps.saveit.interfaces;


import com.smapps.saveit.model.FBStoryModel.NodeModel;
import com.smapps.saveit.model.story.TrayModel;

public interface UserListInterface {
    void userListClick(int position, TrayModel trayModel);
    void fbUserListClick(int position, NodeModel trayModel);
}
