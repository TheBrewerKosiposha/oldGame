package com.faa.knowyourgame_new.ui.logout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.faa.knowyourgame_new.R;

public class LogoutFragment extends Fragment {

    private LogoutViewModel logoutViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        logoutViewModel = new ViewModelProvider(this).get(LogoutViewModel.class);

        View root = inflater.inflate(R.layout.fragment_logout, container, false);

        final TextView textView = root.findViewById(R.id.text_logout);
        final Button button = root.findViewById(R.id.btn_logout);

        button.setOnClickListener(listener -> this.getActivity().finish());

        logoutViewModel.getText().observe(getViewLifecycleOwner(), s -> textView.setText(s));
        logoutViewModel.getLogoutText().observe(getViewLifecycleOwner(), text -> button.setText(text));

        return root;
    }
}