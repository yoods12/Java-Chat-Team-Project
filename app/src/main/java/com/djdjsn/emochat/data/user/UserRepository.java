package com.djdjsn.emochat.data.user;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class UserRepository {

    private final CollectionReference userCollection;

    @Inject
    public UserRepository(FirebaseFirestore firestore) {

        userCollection = firestore.collection("users");
    }

    // DB에 회원정보를 추가하는 메소드

    public void addUser(User user, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {

        userCollection.document(user.getUid())
                .set(user)
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }

    // DB에서 회원정보를 검색하는 메소드

    public LiveData<User> getUser(String uid) {

        MutableLiveData<User> data = new MutableLiveData<>();
        if (uid == null) {
            data.setValue(null);
            return data;
        }

        userCollection.document(uid)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        data.setValue(null);
                        return;
                    }
                    if (value == null) {
                        data.setValue(null);
                        return;
                    }
                    data.setValue(value.toObject(User.class));
                });

        return data;
    }

    public void getUser(String uid, OnSuccessListener<User> onSuccessListener, OnFailureListener onFailureListener) {

        userCollection.document(uid).get()
                .addOnSuccessListener(documentSnapshot -> onSuccessListener.onSuccess(documentSnapshot.toObject(User.class)))
                .addOnFailureListener(onFailureListener);
    }

    public LiveData<List<User>> getUserByPhone(String phone) {

        MutableLiveData<List<User>> data = new MutableLiveData<>();
        if (phone == null) {
            data.setValue(null);
            return data;
        }

        userCollection.whereEqualTo("phone", phone)
                .addSnapshotListener((values, error) -> {
                    if (error != null) {
                        data.setValue(null);
                        return;
                    }
                    if (values == null) {
                        data.setValue(null);
                        return;
                    }
                    data.setValue(values.toObjects(User.class));
                });

        return data;
    }


    // DB 에서 다수의 회원정보를 검색하는 메소드

    public LiveData<List<User>> getUserList(List<String> uids) {

        MutableLiveData<List<User>> data = new MutableLiveData<>();

        userCollection.addSnapshotListener((value, error) -> {
            if (error != null) {
                error.printStackTrace();
            }
            if (error != null || value == null) {
                data.setValue(null);
                return;
            }
            List<User> userList = new ArrayList<>();
            for (DocumentSnapshot snapshot : value) {
                if (snapshot == null) {
                    userList.add(null);
                    continue;
                }
                User user = snapshot.toObject(User.class);
                if (user == null) {
                    userList.add(null);
                    continue;
                }
                if (uids != null && !uids.contains(user.getUid())) {
                    continue;
                }
                userList.add(user);
            }
            data.setValue(userList);
        });

        return data;
    }

}








