package com.example.calmacar.passenger;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.calmacar.R;
import com.example.calmacar.common.TripsManager;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReservationsHistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReservationsHistoryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // References
    ListView lv_bookedTrips, lv_completedTrips;
    private TripsManager mTripsManager;

    public ReservationsHistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReservationsHistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReservationsHistoryFragment newInstance(String param1, String param2) {
        ReservationsHistoryFragment fragment = new ReservationsHistoryFragment();
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
        View view = inflater.inflate(R.layout.fragment_reservations_history, container, false);

        // Hooks
        lv_bookedTrips = view.findViewById(R.id.lv_bookedTrips);
        lv_completedTrips = view.findViewById(R.id.lv_completedTrips);

        // Initialize ListView values
        mTripsManager.updatePassengerBookedTripsListView(getActivity(), lv_bookedTrips);
        mTripsManager.updatePassengerCompletedTripsListView(getActivity(), lv_completedTrips);

        return view;
    }
}