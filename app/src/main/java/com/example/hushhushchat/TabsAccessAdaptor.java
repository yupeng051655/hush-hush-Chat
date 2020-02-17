package com.example.hushhushchat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabsAccessAdaptor  extends FragmentPagerAdapter {

    public TabsAccessAdaptor(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int i){
        switch (i){
            case 0:
                Contact contact = new Contact();
                return contact;

            case 1:
                Groups groups = new Groups();
                return groups;


            default:
                return null;

        }
    }


    @Override
    public int getCount(){
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position){
            case 0:

                return "Contact";

            case 1:

                return "Group";

            default:
                return null;

        }
    }
}
