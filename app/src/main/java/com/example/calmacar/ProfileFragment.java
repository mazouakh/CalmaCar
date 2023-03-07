package com.example.calmacar;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // References
    EditText et_firstname, et_lastname, et_phone;
    Button btn_updateProfile;
    FirebaseAuth mAuth;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);

        // Hooks
        et_firstname = view.findViewById(R.id.et_firstname);
        et_lastname = view.findViewById(R.id.et_lastname);
        et_phone = view.findViewById(R.id.et_phone);
        btn_updateProfile = view.findViewById(R.id.btn_updateProfile);

        // Firebase
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        // Load User Data
        loadProfile(firebaseUser);

        // Update Profile btn
        btn_updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUserData(firebaseUser, et_firstname, et_lastname, et_phone);
            }
        });

        return view;
    }

    private void updateUserData(FirebaseUser firebaseUser, EditText et_firstname, EditText et_lastname, EditText et_phone) {
        String userID = firebaseUser.getUid();

        // "Registered Users" reference
        DatabaseReference registeredUsersReference = FirebaseDatabase.getInstance().getReference("Registered Users");

        // validating new input data
        Validator validator = Validator.getInstance();
        if (!validator.isFirstnameValid(et_firstname) |
        !validator.isLastnameValid(et_lastname) |
        !validator.isPhoneNumberValid(et_phone)){
            return;
        }

        // Creating a new user object with the data
        User newUser = new User(
                userID,
                et_firstname.getText().toString(),
                et_lastname.getText().toString(),
                et_phone.getText().toString(),
                ((HomeActivity) getActivity()).getUserType());

        // finding the user in database and updating it
        registeredUsersReference.child(userID).setValue(newUser).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()){
                    try {
                        throw task.getException();
                    }catch (Exception e){
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Updating user info failed with error : " + e, Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
                Toast.makeText(getActivity().getApplicationContext(), "Votre profile a été mis à jour avec success", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadProfile(FirebaseUser firebaseUser) {
        String userID = firebaseUser.getUid();

        // "Registered Users" reference
        DatabaseReference registeredUsersReference = FirebaseDatabase.getInstance().getReference("Registered Users");

        // finding user info in database
        registeredUsersReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user == null){
                    Toast.makeText(getActivity().getApplicationContext(), "Could not load user data. Something went wrong!", Toast.LENGTH_SHORT).show();
                }
                Log.e("PROFILE_FRAG", "loadProfile: Loading user firstname " + user.getFirstname());

                et_firstname.setText(user.getFirstname());
                et_lastname.setText(user.getLastname());
                et_phone.setText(user.getPhone());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity().getApplicationContext(), "Could not load user data because it was cancelled", Toast.LENGTH_SHORT).show();
            }
        });
    }
}