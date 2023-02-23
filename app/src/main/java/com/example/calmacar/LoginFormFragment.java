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
 * Use the {@link LoginFormFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFormFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // UI elements
    Button btn_login;
    TextView tv_notMember;
    EditText et_email, et_password;

    public LoginFormFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFormFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFormFragment newInstance(String param1, String param2) {
        LoginFormFragment fragment = new LoginFormFragment();
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
        View view = inflater.inflate(R.layout.fragment_login_form, container, false);

        // Hooks
        btn_login = view.findViewById(R.id.btn_login);
        tv_notMember = view.findViewById(R.id.tv_notMember);
        et_email = view.findViewById(R.id.et_email);
        et_password = view.findViewById(R.id.et_password);


        // Click Listeners
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLoginButtonClicked(view);
            }
        });

        tv_notMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNotMember(view);
            }
        });

        return view;
    }

    public void onLoginButtonClicked(View view) {
        // validate the form
        Validator validator = new Validator();
        if (!validator.isEmailValid(et_email) |
        !validator.isPasswordValid(et_password)) {
            return;
        }
        // check if user already exists

        // compare input with the credentials in the database

        // login the new user
        Toast.makeText(view.getContext(), "Connexion en cours...", Toast.LENGTH_SHORT).show();

        // redirect to Driver Dashboard
        Intent intent = new Intent(this.getContext(), HomeActivity.class);
        this.startActivity(intent);
    }


    public void onNotMember(View view) {
        ((AuthActivity)this.getActivity()).LoadRegisterForm();
    }
}