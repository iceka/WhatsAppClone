package com.iceka.whatsappclone.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.iceka.whatsappclone.R;
import com.iceka.whatsappclone.adapters.ChatAdapter;
import com.iceka.whatsappclone.models.Chat;

import java.util.ArrayList;

public class ChatTabFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list_chat, container, false);

        final ArrayList<Chat> chatArrayList = new ArrayList<Chat>();
        chatArrayList.add(new Chat("My Mom", "Imam ker dimana keneh?", R.mipmap.ic_launcher));
        chatArrayList.add(new Chat("Teteh", "Heeh ngke we mun aya", R.mipmap.ic_launcher));
        chatArrayList.add(new Chat("Imam", "I love you beybeh", R.mipmap.ic_launcher));
        chatArrayList.add(new Chat("My Lovely", "I miss you", R.mipmap.ic_launcher));
        chatArrayList.add(new Chat("My Mom", "Imam ker dimana keneh?", R.mipmap.ic_launcher));
        chatArrayList.add(new Chat("Teteh", "Heeh ngke we mun aya", R.mipmap.ic_launcher));
        chatArrayList.add(new Chat("Imam", "I love you beybeh", R.mipmap.ic_launcher));
        chatArrayList.add(new Chat("My Lovely", "I miss you", R.mipmap.ic_launcher));

        ChatAdapter adapter = new ChatAdapter(getActivity(), chatArrayList);
        ListView listView = (ListView) rootView.findViewById(R.id.chat_list);
        listView.setAdapter(adapter);

        return rootView;
    }

}
