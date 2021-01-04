package com.aidul23.b_bariaceramics.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.aidul23.b_bariaceramics.Adapter.CommonAdapter;
import com.aidul23.b_bariaceramics.Interface.SubCatLoadCallback;
import com.aidul23.b_bariaceramics.Task.FirebaseQueryTask;
import com.aidul23.b_bariaceramics.ViewModel.MainActivityViewModel;
import com.aidul23.b_bariaceramics.databinding.FragmentSubactBinding;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SubCatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubCatFragment extends Fragment {

    private static final String TAG = "SubCatFragment";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private CommonAdapter adapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SubCatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CommonFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SubCatFragment newInstance(String param1, String param2) {
        SubCatFragment fragment = new SubCatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private MainActivityViewModel activityViewModel;

    private FragmentSubactBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityViewModel = ViewModelProviders.of(getActivity()).get(MainActivityViewModel.class);

        if (getArguments() != null) {

            String categoryTitle = getArguments().getString("title");

            new FirebaseQueryTask(new SubCatLoadCallback() {
                @Override
                public void onSubCatLoadCallback(List<String> subCats) {
                    binding.progressBar.setVisibility(View.GONE);
                    adapter = new CommonAdapter(subCats, "subCat", getContext(), categoryTitle);
                    binding.subCatRecyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    binding.subCatRecyclerView.scheduleLayoutAnimation();

                    adapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            activityViewModel.getSubCatSelectedItem().setValue(subCats.get(position));
                        }
                    });

                }
            }, categoryTitle, "subCat").execute();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentSubactBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }
}