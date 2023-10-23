package com.djdjsn.emochat.ui.auth.friends;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.djdjsn.emochat.R;

public class AddFriendFragment extends Fragment {

    private EditText editTextEmail;
    private Button buttonAddFriend;
    private AddFriendViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_friend, container, false);

        editTextEmail = root.findViewById(R.id.editTextEmail);
        buttonAddFriend = root.findViewById(R.id.buttonAddFriend);

        buttonAddFriend.setOnClickListener(v -> addFriend());

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(AddFriendViewModel.class);

        // 이메일 검색 결과를 관찰하여 처리하는 부분 추가
        viewModel.getSearchResult().observe(getViewLifecycleOwner(), result -> {
            if (result) {
                // 검색 결과가 성공적일 경우
                // 사용자를 친구 목록에 추가하는 등의 동작 수행
            } else {
                // 검색 결과가 실패일 경우
                // 오류 메시지 출력 등의 동작 수행
            }
        });
    }

    private void addFriend() {
        String email = editTextEmail.getText().toString();
        viewModel.searchUserByEmail(email);
    }
}
