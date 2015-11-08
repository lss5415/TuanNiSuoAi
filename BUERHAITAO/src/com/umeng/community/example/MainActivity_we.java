
package com.umeng.community.example;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.ZYKJ.tuannisuoai.R;
import com.umeng.comm.core.CommunitySDK;
import com.umeng.comm.core.beans.CommConfig;
import com.umeng.comm.core.beans.CommUser;
import com.umeng.comm.core.beans.FeedItem;
import com.umeng.comm.core.beans.Topic;
import com.umeng.comm.core.impl.CommunityFactory;
import com.umeng.comm.core.listeners.Listeners.FetchListener;
import com.umeng.comm.core.nets.responses.FeedsResponse;
import com.umeng.comm.core.nets.responses.TopicResponse;
import com.umeng.comm.core.nets.responses.UsersResponse;
import com.umeng.comm.core.sdkmanager.ImageLoaderManager;
import com.umeng.comm.core.sdkmanager.LoginSDKManager;
import com.umeng.comm.core.sdkmanager.PushSDKManager;
import com.umeng.comm.login.sso.UMQQSsoHandler;
import com.umeng.comm.push.UmengPushImpl;
import com.umeng.comm.ui.fragments.CommunityMainFragment;
import com.umeng.community.example.custom.MyLoginImpl;
import com.umeng.community.example.custom.UILImageLoader;
import com.umeng.community.example.fragments.CustomFragment;
import com.umeng.login.controller.UMAuthService;
import com.umeng.login.controller.UMLoginServiceFactory;

public class MainActivity_we extends FragmentActivity {

    static CommunitySDK mCommSDK = null;

    // TODO
    String topicId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_we);

        mCommSDK = CommunityFactory.getCommSDK(this);

        // 开发者使用ViewPager的方式集成。注意此时你的ViewPager必须继承CommunityViewPager，
        // 避免滑动时间冲突导致问题
        // CommunityViewPager viewPager = (CommunityViewPager)
        // findViewById(R.id.viewPager);
        // viewPager.setAdapter(new MyAdapter());

        // 单纯Fragment使用方式
        CommunityMainFragment fragment = new CommunityMainFragment();
        fragment.setBackButtonVisibility(View.GONE);
        getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).commit();
        // 在初始化CommunitySDK之前配置推送和登录等组件
        useSocialLogin();
        useMyPushComponent();

        // 使用自定义的ImageLoader
        // useMyImageLoader();
        // 使用自定义的登录系统
//        userCustomLogin();

        // getWindow().getDecorView().postDelayed(new Runnable() {
        //
        // @Override
        // public void run() {
        // Intent intent = new Intent(getApplicationContext(),
        // SearchResultActivity.class);
        // startActivity(intent);
        // }
        // }, 1500);
    }

    /**
     * 自定义自己的登录系统
     */
    protected void useSocialLogin() {

        // 用户自定义的登录
        UMAuthService mLogin = UMLoginServiceFactory.getLoginService("umeng_login_impl");
        String appId = "100424468";
        String appKey = "c7394704798a158208a74ab60104f0ba";
        // SSO 设置
        // mLogin.getConfig().setSsoHandler(new SinaSsoHandler());
        new UMQQSsoHandler(this, appId, appKey).addToSocialSDK();

        // String wxappId = "wx967daebe835fbeac";
        // String wxappSecret = "5bb696d9ccd75a38c8a0bfe0675559b3";
        // new UMWXHandler(getApplicationContext(), wxappId,
        // wxappSecret).addToSocialSDK();

        // 管理器
        LoginSDKManager manager = CommConfig.getConfig().getLoginSDKManager();
        // 将登录实现注入到sdk中,key为umeng_login
        manager.addImpl("umeng_login", mLogin);
        // 使用该登录实现
        manager.useThis("umeng_login");

    }

    protected void userCustomLogin() {
        // 管理器
        LoginSDKManager.getInstance().addAndUse(new MyLoginImpl());
    }

    /**
     * 自定义自己的ImageLoader
     */
    protected void useMyImageLoader() {
        //
        final String imageLoadKey = UILImageLoader.class.getSimpleName();
        // 使用第三方ImageLoader库,添加到sdk manager中, 并且使用useThis来使用该加载器.
        ImageLoaderManager manager = CommConfig.getConfig().getImageLoaderManager();
        manager.addImpl(imageLoadKey, new UILImageLoader(this));
        manager.useThis(imageLoadKey);
    }

    /**
     * 自定义自己的推送系统
     */
    protected void useMyPushComponent() {
        final String pushClz = UmengPushImpl.class.getSimpleName();
        // 推送实现
        PushSDKManager.getInstance().addImpl(pushClz, new UmengPushImpl());
        // 使用这个推送实现
        PushSDKManager.getInstance().useThis(pushClz);
    }

    class MyAdapter extends FragmentPagerAdapter {

        /**
         * @param fm
         */
        public MyAdapter() {
            super(getSupportFragmentManager());
        }

        @Override
        public Fragment getItem(int pos) {
            return getFragment(pos);
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    Fragment getFragment(int pos) {
        if (pos == 1) {
            CommunityMainFragment communityMainFragment = new CommunityMainFragment();
            communityMainFragment.setBackButtonVisibility(View.GONE);
            return communityMainFragment;
        }
        return CustomFragment.newInstnce(pos);
    }

    /**
     * 获取推荐的数据
     */
    private void fetchRecommendData() {

        // 未登录下获取话题
        mCommSDK.fetchTopics(new FetchListener<TopicResponse>() {

            @Override
            public void onStart() {

            }

            @Override
            public void onComplete(TopicResponse response) {
                for (Topic item : response.result) {
                    Log.e("", "### topic id : " + item.id + ", name = " + item.name);
                    topicId = item.id;
                }

            }
        });

        // 未登录情况下获取某个话题下的feed
        mCommSDK.fetchTopicFeed(topicId, new
                FetchListener<FeedsResponse>() {

                    @Override
                    public void onComplete(FeedsResponse response) {
                        Log.e("", "### 未登录下获取到某个topic下的feed : " + response.result.size());
                        for (FeedItem item : response.result) {
                            Log.e("", "### topic feed id : " + item.id + ", name = " +
                                    item.text);
                        }

                    }

                    @Override
                    public void onStart() {
                    }
                });

        // 推荐的feed
        mCommSDK.fetchRecommendedFeeds(new FetchListener<FeedsResponse>() {

            @Override
            public void onComplete(FeedsResponse response) {
                Log.e("", "### 推荐feed  code : " + response.errCode + ", msg = " + response.errMsg);
                for (FeedItem item : response.result) {
                    Log.e("", "### 推荐feed id : " + item.id + ", name = " + item.text);
                }
            }

            @Override
            public void onStart() {

            }
        });

        // 获取推荐的话题
        mCommSDK.fetchRecommendedTopics(new FetchListener<TopicResponse>() {

            @Override
            public void onComplete(TopicResponse response) {
                Log.e("", "### 推荐的话题 : ");
                for (Topic item : response.result) {
                    Log.e("", "### 话题 : " + item.name);
                }
            }

            @Override
            public void onStart() {

            }
        });

        // 获取某个话题活跃的用户
        mCommSDK.fetchActiveUsers("541fe6f40bbbaf4f41f7aa3f", new FetchListener<UsersResponse>() {

            @Override
            public void onStart() {

            }

            @Override
            public void onComplete(UsersResponse response) {
                Log.e("", "### 某个话题的活跃用户 : ");
                for (CommUser user : response.result) {
                    Log.e("", "### 活跃用户 : " + user.name);
                }
            }
        });

    }

}
