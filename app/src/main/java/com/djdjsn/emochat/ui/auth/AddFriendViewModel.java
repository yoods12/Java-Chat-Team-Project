package com.djdjsn.emochat.ui.auth.friends;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.djdjsn.emochat.data.user.User;
import com.djdjsn.emochat.data.user.UserRepository;

import javax.inject.Inject;

public class AddFriendViewModel extends ViewModel {

    // UserRepository 클래스를 사용하여 사용자 데이터를 가져오기 위한 UserRepository 인스턴스
    private final UserRepository userRepository;

    // 뷰모델 내에서 발생하는 이벤트를 관리하는 MutableLiveData
    private final MutableLiveData<Event> event = new MutableLiveData<>();

    // 뷰모델을 생성할 때 UserRepository 의존성을 주입받음
    @Inject
    public AddFriendViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // UI 컴포넌트(액티비티 또는 프래그먼트)에서 이벤트를 관찰할 수 있도록 LiveData를 반환
    public LiveData<Event> getEvent() {
        return event;
    }

    // 사용자 이메일로 다른 사용자를 검색하는 메서드
    public void searchUserByEmail(String userEmail) {
        // UserRepository를 사용하여 userEmail을 기반으로 사용자를 검색
        userRepository.getUserByPhone(userEmail)
            .observeForever(users -> {
                if (users != null && !users.isEmpty()) {
                    // 사용자가 발견되었을 때 UserFound 이벤트를 발생시키고 발견된 사용자 정보를 포함시킴
                    User foundUser = users.get(0); // 검색된 첫 번째 사용자를 가져옴
                    event.setValue(new Event.UserFound(foundUser));
                } else {
                    // 사용자가 발견되지 않았을 때 UserNotFound 이벤트를 발생시킴
                    event.setValue(new Event.UserNotFound());
                }
            });
    }

    // 사용자를 친구 목록에 추가하는 메서드
    public void addFriend(User user) {
        // 사용자를 친구 목록에 추가하는 작업 수행
        // 이 작업은 데이터베이스나 서버 API에 따라 구현되어야 함
        // 작업이 완료되면 이벤트를 발생시키거나 사용자에게 알림을 보내어 친구가 추가되었음을 알릴 수 있음
    }

    // 뷰모델에서 발생하는 이벤트를 정의하는 내부 Event 클래스
    public static class Event {
        // 사용자를 찾은 경우에 사용하는 이벤트
        public static class UserFound extends Event {
            private final User foundUser;

            public UserFound(User foundUser) {
                this.foundUser = foundUser;
            }

            // 검색된 사용자 정보를 반환
            public User getFoundUser() {
                return foundUser;
            }
        }

        // 사용자를 찾지 못한 경우에 사용하는 이벤트
        public static class UserNotFound extends Event {
        }

        // 친구 추가 성공 및 실패 등 다양한 이벤트를 추가할 수 있음
    }
}
