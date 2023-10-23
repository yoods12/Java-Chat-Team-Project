package com.djdjsn.emochat.ui.auth.friends;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.djdjsn.emochat.data.user.User;
import com.djdjsn.emochat.data.user.UserRepository;

import javax.inject.Inject;

public class AddFriendViewModel extends ViewModel {

    private final UserRepository userRepository;
    private final MutableLiveData<Event> event = new MutableLiveData<>();

    @Inject
    public AddFriendViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public LiveData<Event> getEvent() {
        return event;
    }

    public void searchUserByEmail(String userEmail) {
        userRepository.getUserByPhone(userEmail)
                .observeForever(users -> {
                    if (users != null && !users.isEmpty()) {
                        User foundUser = users.get(0); // 첫 번째 사용자를 가져옴
                        event.setValue(new Event.UserFound(foundUser));
                    } else {
                        event.setValue(new Event.UserNotFound());
                    }
                });
    }

    public void addFriend(User user) {
        // 친구 목록에 user를 추가 하는 작업 이벤트 발생, 알림 보내기 등등
    }

    public static class Event {
        public static class UserFound extends Event {
            private final User foundUser;
            public UserFound(User foundUser) {
                this.foundUser = foundUser;
            }
            public User getFoundUser() {
                return foundUser;
            }
        }
        public static class UserNotFound extends Event {
        }
        // 친구 추가 성공 및 실패 등 다양한 이벤트를 추가할 수 있습니다.
    }
}
