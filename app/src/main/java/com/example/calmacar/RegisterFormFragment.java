package com.example.calmacar;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFormFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFormFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // UI elements
    Button btn_signup;
    TextView tv_alreadyMember;
    EditText et_lastname, et_firstname, et_email, et_password;

    public RegisterFormFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFormFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFormFragment newInstance(String param1, String param2) {
        RegisterFormFragment fragment = new RegisterFormFragment();
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
        View view = inflater.inflate(R.layout.fragment_register_form, container, false);

        // Hooks
        btn_signup = view.findViewById(R.id.btn_signup);
        tv_alreadyMember = view.findViewById(R.id.tv_alreadyMember);
        et_lastname = view.findViewById(R.id.et_lastname);
        et_firstname = view.findViewById(R.id.et_firstname);
        et_email = view.findViewById(R.id.et_email);
        et_password = view.findViewById(R.id.et_password);

        // Click listeners
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onInscriptionButtonClicked(view);
            }
        });
        tv_alreadyMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAlreadyMember(view);
            }
        });

        return view;
    }

    public void onInscriptionButtonClicked(View view) {
        // validate the form
        Validator validator = Validator.getInstance();
        if (!validator.isLastnameValid(et_lastname) |
            !validator.isFirstnameValid(et_firstname) |
            !validator.isEmailValid(et_email) |
            !validator.isPasswordValid(et_password))
            return;

        // Send data to auth activity
        Intent signupIntent = new Intent("DATA_SIGNUP");
        signupIntent.putExtra("EXTRA_LASTNAME", et_lastname.getText().toString().trim());
        signupIntent.putExtra("EXTRA_FIRSTNAME", et_firstname.getText().toString().trim());
        signupIntent.putExtra("EXTRA_EMAIL", et_email.getText().toString().trim());
        signupIntent.putExtra("EXTRA_PASSWORD", et_password.getText().toString().trim());
        getActivity().sendBroadcast(signupIntent);

    }
    public void onAlreadyMember(View view) {
        ((AuthActivity)this.getActivity()).LoadLoginForm();
    }
}