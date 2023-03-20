package com.example.calmacar.driver.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.calmacar.R;
import com.example.calmacar.common.model.TripsManager;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DeveloperFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeveloperFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // References
    Button btn_completeTrip, btn_handleOutdatedTrips;
    EditText et_tripID;
    TripsManager mTripsManager;

    public DeveloperFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DeveloperFragment newInstance(String param1, String param2) {
        DeveloperFragment fragment = new DeveloperFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mTripsManager = TripsManager.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_developer, container, false);

        // Hooks
        btn_completeTrip = view.findViewById(R.id.btn_completeTrip);
        btn_handleOutdatedTrips = view.findViewById(R.id.btn_handleOutdatedTrips);
        et_tripID = view.findViewById(R.id.et_tripID);

        btn_completeTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTripsManager.completeTripAndUpdateUI(
                        getActivity(),
                        null,
                        et_tripID.getText().toString());
            }
        });

        btn_handleOutdatedTrips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTripsManager.handleOutdatedTrips();
            }
        });

        return view;
    }
}