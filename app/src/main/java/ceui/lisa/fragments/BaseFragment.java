package ceui.lisa.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import ceui.lisa.utils.Channel;
import ceui.lisa.utils.Common;
import ceui.lisa.utils.Local;
import ceui.lisa.response.UserModel;

public abstract class BaseFragment extends Fragment {

    protected Context mContext;
    protected Activity mActivity;
    protected int mLayoutID;
    protected View parentView;
    protected UserModel mUserModel;

    abstract void initLayout();

    abstract View initView(View v);

    abstract void initData();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        mActivity = getActivity();
        mUserModel = Local.getUser();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (parentView == null) {
            initLayout();
            parentView = inflater.inflate(mLayoutID, container, false);
            initView(parentView);
            initData();
        } else {
            ViewGroup viewGroup = (ViewGroup) parentView.getParent();
            if (viewGroup != null) {
                viewGroup.removeView(parentView);
            }
        }
        return parentView;
    }



    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
