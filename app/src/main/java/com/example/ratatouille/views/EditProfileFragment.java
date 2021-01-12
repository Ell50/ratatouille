package com.example.ratatouille.views;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.ratatouille.R;
import com.example.ratatouille.controllers.UserController;
import com.example.ratatouille.utils.Utils;
import com.example.ratatouille.vars.VariablesUsed;

public class EditProfileFragment extends Fragment {
    ImageView backButton;
    ImageView profileImage;
    TextView profileUsername;
    TextView profileEmail;
    TextView saveButton;
    EditText inputUsername;
    EditText inputPassword;
    EditText inputEmail;
    EditText inputPhoneNumber;
    EditText inputAddress;
    EditText inputName;

    public EditProfileFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        profileUsername = view.findViewById(R.id.ep_username);
        profileEmail = view.findViewById(R.id.ep_email);
        inputUsername = view.findViewById(R.id.ep_usernameInput);
        inputEmail = view.findViewById(R.id.ep_emailInput);
        inputPassword = view.findViewById(R.id.ep_passwordInput);
        inputPhoneNumber = view.findViewById(R.id.ep_phoneNumberInput);
        inputAddress = view.findViewById(R.id.ep_addressInput);
        inputName = view.findViewById(R.id.ep_nameInput);
        backButton = view.findViewById(R.id.ep_backbutton);
        saveButton = view.findViewById(R.id.ep_saveButton);
        profileImage = view.findViewById(R.id.ep_imageProfile);

        if(VariablesUsed.loggedUser.getPhotoUrl() != null) {
            Glide.with(getView().getContext())
                    .load(VariablesUsed.loggedUser.getPhotoUrl())
                    .into(profileImage);
        }

        inputPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

        profileUsername.setText(VariablesUsed.currentUser.getUsername());
        profileEmail.setText(VariablesUsed.loggedUser.getEmail());

        inputUsername.setText(VariablesUsed.currentUser.getUsername());
        inputEmail.setText(VariablesUsed.loggedUser.getEmail());
        inputPassword.setText("********");
        inputPhoneNumber.setText(VariablesUsed.currentUser.getPhone());
        inputAddress.setText(VariablesUsed.currentUser.getAddress());
        inputName.setText(VariablesUsed.currentUser.getName());

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewProfileFragment backPage = new ViewProfileFragment();
                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, backPage).commit();
            }
        });
        inputUsername.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                inputUsername.setText("");
            }
        });
        inputEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputEmail.setText("");
            }
        });
        inputPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputPassword.setText("");
            }
        });
        inputName.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                inputName.setText("");
            }
        });
        inputPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputPhoneNumber.setText("");
            }
        });
        inputAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputAddress.setText("");
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Boolean falseChecker = false;

                if(Utils.validateUsername(inputUsername.getText().toString())){
                    inputUsername.setError("Invalid Input!");
                    falseChecker = true;
                }

                if(Utils.validateEmail(inputEmail.getText().toString())){
                    inputEmail.setError("Invalid Input!");
                    falseChecker = true;
                }
                if(Utils.validatePassword(inputPassword.getText().toString())){
                    inputPassword.setError("Invalid Input!");
                    falseChecker = true;
                }
                if(Utils.validatePhone(inputPhoneNumber.getText().toString())){
                    inputPhoneNumber.setError("Invalid Input!");
                    falseChecker = true;
                }
                if(Utils.validateInput(inputAddress.getText().toString())){
                    inputAddress.setError("Invalid Input!");
                    falseChecker = true;
                }
                if(Utils.validateInput(inputName.getText().toString())){
                    inputName.setError("Invalid Input!");
                    falseChecker = true;
                }

                if(falseChecker == false){
                    UserController.updateProfile(inputUsername.getText().toString(), inputName.getText().toString(), inputPhoneNumber.getText().toString(), inputAddress.getText().toString(), VariablesUsed.currentUser.getPoints());
                    // Reload current fragment
                    final FragmentTransaction ft = getParentFragmentManager().beginTransaction();
                    Utils.showSuccessMessage(getView().getContext(), "Profile Updated", "Reloading the new data!");
                    ft.detach(EditProfileFragment.this);
                    ft.attach(EditProfileFragment.this);
                    ft.commit();
                } else {
                    Utils.showAlertMessage(getView().getContext(), "Credentials are not valid!", "Please fill in the indicated textfield correctly!");
                }
            }
        });
    }
}