package com.djdjsn.emochat.ui.auth.register;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.djdjsn.emochat.data.user.User;
import com.djdjsn.emochat.utils.AuthUtils;
import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class RegisterViewModel extends ViewModel {

    private final MutableLiveData<Event> event = new MutableLiveData<>();

    private String id = "";
    private String password = "";
    private String passwordConfirm = "";

    private String nickname = "";
    private String phone = "";

    private final MutableLiveData<Boolean> doAgreePrivacyPolicy = new MutableLiveData<>(false);

    private final FirebaseAuth firebaseAuth;


    @Inject
    public RegisterViewModel(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
    }

    public LiveData<Event> getEvent() {
        event.setValue(null);
        return event;
    }

    public LiveData<Boolean> doAgreePrivacyPolicy() {
        return doAgreePrivacyPolicy;
    }


    public void onIdChanged(String value) {
        // 아이디 입력값이 변경되었을 때
        id = value.trim();
    }

    public void onPasswordChanged(String value) {
        // 비밀번호 입력값이 변경되었을 때
        password = value;
    }

    public void onPasswordConfirmChanged(String value) {
        // 비밀번호 확인값이 변경되었을 때
        passwordConfirm = value;
    }

    public void onNicknameChanged(String value) {
        nickname = value.trim();
    }

    public void onPhoneChanged(String value) {
        phone = value.trim();
    }

    public void onPrivacyPolicyClick() {

        Boolean doAgreePrivacyPolicyValue = doAgreePrivacyPolicy.getValue();
        assert doAgreePrivacyPolicyValue != null;

        if (doAgreePrivacyPolicyValue) {
            doAgreePrivacyPolicy.setValue(false);
        } else {
            event.setValue(new Event.ShowPrivacyPolicy());
        }
    }

    public void onPrivacyPolicyAgreed() {
        doAgreePrivacyPolicy.setValue(true);
        event.setValue(new Event.HideKeyboard());
    }

    public void onRegisterClick() {

        // 회원가입 요청했을 때 : 회원가입 시도하기

        if (id.length() < 4) {
            // 경고 : 짧은 아이디
            event.setValue(new Event.ShowGeneralMessage("아이디를 4글자 이상 입력해주세요"));
            return;
        }
        if (password.length() < 6) {
            // 경고 : 짧은 패스워드
            event.setValue(new Event.ShowGeneralMessage("비밀번호를 6글자 이상 입력해주세요"));
            return;
        }
        if (!passwordConfirm.equals(password)) {
            // 경고 : 비밀번호 확인 불일치
            event.setValue(new Event.ShowGeneralMessage("비밀번호를 정확하게 입력해주세요"));
            return;
        }
        if (nickname.length() < 2) {
            // 경고 : 짧은 닉네임
            event.setValue(new Event.ShowGeneralMessage("닉네임을 2글자 이상 입력해주세요"));
            return;
        }
        if (phone.length() != 11) {
            // 경고 : 전화번호 길이
            event.setValue(new Event.ShowGeneralMessage("연락처를 정확하게 입력해주세요"));
            return;
        }

        Boolean doAgreePrivacyPolicyValue = doAgreePrivacyPolicy.getValue();
        assert doAgreePrivacyPolicyValue != null;
        if (!doAgreePrivacyPolicyValue) {
            // 경고 : 개인정보처리방침 미동의
            event.setValue(new Event.ShowGeneralMessage("개인정보처리방침에 동의하지 않았습니다"));
            return;
        }

        String email = AuthUtils.emailize(id);

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    if (authResult.getUser() == null) {
                        return;
                    }
                    String uid = authResult.getUser().getUid();
                    User userData = new User(uid, id, nickname, phone);
                    event.setValue(new Event.NavigateBackWithResult(id, password, userData));
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    event.setValue(new Event.ShowGeneralMessage("회원가입에 실패했습니다"));
                });
    }


    public static class Event {

        public static class ShowGeneralMessage extends Event {
            public final String message;
            public ShowGeneralMessage(String message) {
                this.message = message;
            }
        }

        public static class NavigateBackWithResult extends Event {
            public final String id;
            public final String password;
            public final User userData;
            public NavigateBackWithResult(String id, String password, User userData) {
                this.id = id;
                this.password = password;
                this.userData = userData;
            }
        }

        public static class ShowPrivacyPolicy extends Event {
        }

        public static class HideKeyboard extends Event {
        }
    }

}





