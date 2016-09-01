package com.srkrit.swachh;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class ViewPagerFragment extends Fragment {


    public static ViewPagerFragment newInstance() {
        return new ViewPagerFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.viewpager_fragment, container, false);

        return view;
    }
}










//
//package com.srkrit.swachh;
//
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ListView;
//
///**
// * Created by bmcansh on 6/4/16.
// */
//public class ViewPagerFragment extends Fragment {
//
//
//    ListView lv;
//    public static ViewPagerFragment newInstance() {
//        return new ViewPagerFragment();
//    }
//
//    public static int [] prgmImages={R.drawable.srkr_logo,R.drawable.srkrit_logo,R.drawable.srkr_logo,R.drawable.srkrit_logo,R.drawable.srkrit_logo,R.drawable.srkrit_logo,R.drawable.srkrit_logo,R.drawable.srkrit_logo,R.drawable.srkrit_logo};
//    public static String [] prgmNameList={"Let Us C","c++","JAVA","Jsp","Microsoft .Net","Android","PHP","Jquery","JavaScript"};
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
//                             @Nullable Bundle savedInstanceState) {
//        super.onCreateView(inflater, container, savedInstanceState);
//
//        View view = inflater.inflate(R.layout.viewpager_fragment, container, false);
//
//        lv=(ListView) view.findViewById(R.id.listView);
//        lv.setAdapter(new CustomAdapter(this, prgmNameList,prgmImages));
//
//        return view;
//    }
//}
