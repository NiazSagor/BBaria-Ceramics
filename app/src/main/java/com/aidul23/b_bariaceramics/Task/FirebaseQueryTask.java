package com.aidul23.b_bariaceramics.Task;

import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aidul23.b_bariaceramics.Interface.SubCatLoadCallback;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class FirebaseQueryTask extends AsyncTask<Void, Void, Void> {

    private static final String TAG = "FirebaseQueryTask";

    private final DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    private final String subCatEntry;
    private final String entryType;
    private List<String> subCategoryNames;

    private CountDownLatch latch;

    private SubCatLoadCallback catLoadCallback;

    public FirebaseQueryTask(SubCatLoadCallback catLoadCallback, String subCatEntry, String entryType) {
        this.catLoadCallback = catLoadCallback;
        this.subCatEntry = subCatEntry;
        this.entryType = entryType;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        if (entryType.equals("subCat")) {
            // only show sub cat
            subCategoryNames = new ArrayList<>();
            database.child(subCatEntry).child("Sub Category").addChildEventListener(subCatChildEventListener);
        } else if (entryType.equals("product")) {
            subCategoryNames = new ArrayList<>();
            database.child("Sub Category").child(subCatEntry).addValueEventListener(valueEventListener);
        }

        return null;
    }

    private final ChildEventListener subCatChildEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            if (dataSnapshot.exists()) {
                latch = new CountDownLatch((int) dataSnapshot.getChildrenCount());
                String catName = dataSnapshot.getValue(String.class);
                subCategoryNames.add(catName);
                if (entryType.equals("product")) {
                    Picasso.get().load(catName).fetch(new Callback() {
                        @Override
                        public void onSuccess() {
                            latch.countDown();
                            try {
                                latch.await();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            catLoadCallback.onSubCatLoadCallback(subCategoryNames);
                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
                } else {
                    catLoadCallback.onSubCatLoadCallback(subCategoryNames);
                }
            }
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private final ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {

                latch = new CountDownLatch((int) dataSnapshot.getChildrenCount());

                Log.d(TAG, "onDataChange: " + latch.getCount());

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String catName = ds.getValue(String.class);
                    subCategoryNames.add(catName);

                    Log.d(TAG, "onDataChange: " + catName);

                    Picasso.get().load(catName).fetch();

                    latch.countDown();
                }

                try {
                    latch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                catLoadCallback.onSubCatLoadCallback(subCategoryNames);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
}
