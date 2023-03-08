package com.example.calmacar;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateTripFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateTripFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // References
    private Button btn_addTrip, btn_date;
    private EditText et_startCity, et_endCity;
    private TripsManager tripsManager;

    public CreateTripFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateTripFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateTripFragment newInstance(String param1, String param2) {
        CreateTripFragment fragment = new CreateTripFragment();
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

        tripsManager = TripsManager.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_trip, container, false);

        // Hooks
        btn_addTrip = view.findViewById(R.id.btn_addTrip);
        btn_date = view.findViewById(R.id.btn_date);
        et_startCity = view.findViewById(R.id.et_startCity);
        et_endCity = view.findViewById(R.id.et_endCity);

        // Click Listeners
        btn_addTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tripsManager.createNewTrip(
                        getActivity().getApplicationContext(),
                        FirebaseAuth.getInstance().getUid(),
                        btn_date.getText().toString(),
                        et_startCity.getText().toString(),
                        et_endCity.getText().toString());
            }
        });


        return view;
    }
}