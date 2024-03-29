package com.example.down4din;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null) {
            startActivity(new Intent(getContext(), LoginActivity.class));
        }

        final ListView list = v.findViewById(R.id.list);
        final ArrayList<Map<String, Object>> entriesList = new ArrayList<>();

        CollectionReference collection = FirebaseFirestore.getInstance()
                .collection(getResources().getString(R.string.db_collection));
        collection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<DocumentSnapshot> allDocuments = task.getResult().getDocuments();
                    for (int i = 0; i < allDocuments.size(); i++) {
                        // TODO: don't add the current user's entry
                        entriesList.add(allDocuments.get(i).getData());
                    }
                    EntryAdapter adapter = new EntryAdapter(getContext(), R.layout.list_item, entriesList);
                    list.setAdapter(adapter);
                }
            }
        });

        return v;
    }
}
