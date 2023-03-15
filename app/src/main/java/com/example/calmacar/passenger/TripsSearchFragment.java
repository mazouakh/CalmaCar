package com.example.calmacar.passenger;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.calmacar.R;
import com.example.calmacar.common.PickerManager;
import com.example.calmacar.common.TripsManager;
import com.example.calmacar.common.Validator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TripsSearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TripsSearchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // References
    private EditText et_startCity, et_endCity;
    private Button btn_date, btn_startTime, btn_search;
    private PickerManager mPickerManager;
    private TripsManager mTripsManager;
    private Validator mValidator;

    public TripsSearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TripsSearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TripsSearchFragment newInstance(String param1, String param2) {
        TripsSearchFragment fragment = new TripsSearchFragment();
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

        // Services
        mPickerManager = PickerManager.getInstance();
        mTripsManager = TripsManager.getInstance();
        mValidator = Validator.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trips_search, container, false);

        // Hooks
        et_startCity = view.findViewById(R.id.et_startCity);
        et_endCity = view.findViewById(R.id.et_endCity);
        btn_date = view.findViewById(R.id.btn_date);
        btn_startTime = view.findViewById(R.id.btn_startTime);
        btn_search = view.findViewById(R.id.btn_search);

        // Click Listeners
        btn_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPickerManager.displayDatePicker(
                        getActivity(),
                        btn_date);
            }
        });

        btn_startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPickerManager.displayTimePicker(
                        getActivity(),
                        btn_startTime
                );
            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchForTrip();
            }
        });

        // initialization of picker buttons text
        mPickerManager.initializeDatePickerButton(btn_date);
        mPickerManager.initializeTimePickerButton(btn_startTime);

        return view;
    }

    private void searchForTrip() {
        // validate input
        if (!mValidator.isCityNameValid(et_startCity) |
        !mValidator.isCityNameValid(et_endCity))
            return;

        // Ask TripsManager to search for a trip
        mTripsManager.searchForTripsAndDisplayResult(
                getActivity(),
                et_startCity.getText().toString(),
                et_endCity.getText().toString(),
                btn_date.getText().toString(),
                btn_startTime.getText().toString());
    }
}