package com.example.myspeed.base;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myspeed.main.MarketFragment;
import com.example.myspeed.R;
import com.example.myspeed.main.DownloadFragment;
import com.example.myspeed.download.fragments.PanFragment;

import java.util.LinkedList;


/**
 * 管理 fragment  hide，show  显示
 *LinkedList 用作栈
 */



public class MyFragmentManager{

    private static MyFragmentManager myFragmentManager;
    private LinkedList<Fragment> fragmentStack=new LinkedList<>();
    private Fragment[] fragments={new DownloadFragment(),new PanFragment(),new ProgressFragment(),new MarketFragment()};
    private FragmentManager fm;

    private MyFragmentManager(){
        for (Fragment fragment:fragments) {
            fragmentStack.add(fragment);
        }
    }

    public static MyFragmentManager getInstance(){
        if (myFragmentManager==null){
            myFragmentManager=new MyFragmentManager();
        }
        return myFragmentManager;
    }

    public void setFm(FragmentManager fm) {
        this.fm = fm;
    }

    public Fragment getFragment(MyFragmentTag tag){
        return fragments[tag.ordinal()];
    }

    public void splide(MyFragmentTag tag){
        Fragment fragment=fragments[tag.ordinal()];
        if (!fragment.isAdded()){
            push(fragment,fm);
        }else {
            top(fragment,fm);
        }
    }


    //入栈,  hide-add-show
    private void push(Fragment fragment,FragmentManager fm){

        FragmentTransaction transition=fm.beginTransaction();
        for (Fragment frag:fragmentStack) {
            if (frag!=fragment){
                transition.hide(frag);
            }
        }
        transition.add(R.id.fragment_content,fragment)
                .show(fragment)
                .commit();
    }

    //获取栈顶元素
    public Fragment peek(){
        return fragmentStack.getFirst();
    }

    //推到栈顶(冒泡)  hide-show
    private void top(Fragment fragment,FragmentManager fm){
        FragmentTransaction transaction=fm.beginTransaction();

        int index=fragmentStack.indexOf(fragment);
        Fragment temp;
        for (int i = index; i >0; i--) {
            temp=fragmentStack.get(i-1);
            fragmentStack.set(i-1,fragment);
            fragmentStack.set(i,temp);
        }

        for (Fragment frag:fragmentStack) {
            if (frag!=fragment){
                transaction.hide(frag);
            }
        }
        transaction.show(fragment)
                .commit();

    }

}

