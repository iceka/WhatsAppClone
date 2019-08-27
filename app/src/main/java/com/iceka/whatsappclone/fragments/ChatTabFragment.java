package com.iceka.whatsappclone.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iceka.whatsappclone.R;

import java.util.ArrayList;

public class ChatTabFragment extends Fragment {

    private RecyclerView mRecyclerView;
//    private ChatAdapterRV mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recycler_view, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_chat);

//        final ArrayList<Chat> chatArrayList = new ArrayList<Chat>();
//        chatArrayList.add(new Chat("My Mom", "Imam ker dimana keneh?", R.mipmap.ic_launcher));
//        chatArrayList.add(new Chat("Teteh", "Heeh ngke we mun aya", R.mipmap.ic_launcher));
//        chatArrayList.add(new Chat("Imam", "I love you beybeh", R.mipmap.ic_launcher));
//        chatArrayList.add(new Chat("My Lovely", "I miss you", R.mipmap.ic_launcher));
//        chatArrayList.add(new Chat("My Mom", "Imam ker dimana keneh?", R.mipmap.ic_launcher));
//        chatArrayList.add(new Chat("Teteh", "Heeh ngke we mun aya", R.mipmap.ic_launcher));
//        chatArrayList.add(new Chat("Imam", "I love you beybeh", R.mipmap.ic_launcher));
//        chatArrayList.add(new Chat("My Lovely", "I miss you", R.mipmap.ic_launcher));
//        chatArrayList.add(new Chat("My Lovely", "I miss you", R.mipmap.ic_launcher));
//        chatArrayList.add(new Chat("My Mom", "Imam ker dimana keneh?", R.mipmap.ic_launcher));
//        chatArrayList.add(new Chat("Teteh", "Heeh ngke we mun aya", R.mipmap.ic_launcher));
//        chatArrayList.add(new Chat("Imam", "I love you beybeh", R.mipmap.ic_launcher));
//        chatArrayList.add(new Chat("My Lovely", "I miss you", R.mipmap.ic_launcher));

//        mAdapter = new ChatAdapterRV(getActivity(), chatArrayList);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
//        mRecyclerView.setLayoutManager(layoutManager);
//        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
//        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }


}
