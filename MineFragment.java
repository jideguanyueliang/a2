package com.example.xxxmes;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MineFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.mine_fragment, container, false);

        Log.d("MineFragment", "onCreateView called");

        // 获取按钮
        Button editProfileButton = view.findViewById(R.id.edit_profile_button);
        Button myTasksButton = view.findViewById(R.id.my_tasks_button);
        Button settingsButton = view.findViewById(R.id.settings_button);
        Button helpButton = view.findViewById(R.id.help_button);
        Button banbenButton = view.findViewById(R.id.banben_button);
        Button guanyuButton = view.findViewById(R.id.guanyu_button);

        // 你可以选择在这里为按钮设置其他的属性，但不需要设置点击事件
        // 例如，设置按钮的可见性、文本或其他属性

        return view;
    }
}
