package com.example.ratatouille.controllers;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.ratatouille.utils.Utils;
import com.example.ratatouille.db.DatabaseHelper;
import com.example.ratatouille.db.DatabaseVars;
import com.example.ratatouille.models.Users;
import com.example.ratatouille.utils.callbackHelper;
import com.example.ratatouille.vars.VariablesUsed;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;

import static android.content.ContentValues.TAG;

public class UserController {

    public static void UserLogin(callbackHelper cb, Context context, String email, String password) {
        // We use Firebase Auth, for authentication process..
        FirebaseAuth dbAuth = DatabaseHelper.getDbAuth();

        dbAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            VariablesUsed.loggedUser = dbAuth.getCurrentUser();
                            if(VariablesUsed.loggedUser.isEmailVerified()) {
                                Utils.showSuccessMessage(context, "Success Logged In", "Welcome, " + VariablesUsed.loggedUser.getEmail() + " !");

                                //success logged in, filling the userdatas via database..
                                DatabaseVars.UsersTable dbVars = new DatabaseVars.UsersTable();
                                DatabaseReference dbRef = DatabaseHelper.getDb().getReference().child(dbVars.USERS_TABLE).child(VariablesUsed.loggedUser.getUid());

                                dbRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        Users currentUser = new Users(
                                                VariablesUsed.loggedUser.getUid(),
                                                VariablesUsed.loggedUser.getEmail(),
                                                snapshot.child(dbVars.USERNAME).getValue().toString(),
                                                snapshot.child(dbVars.NAME).getValue().toString(),
                                                snapshot.child(dbVars.PHONE).getValue().toString(),
                                                snapshot.child(dbVars.ADDRESS).getValue().toString(),
                                                snapshot.child(dbVars.LASTLOGIN).getValue().toString()
                                        );
                                        VariablesUsed.currentUser = currentUser; // update the current logged in user..
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Log.w(TAG, "User Data retrieval failed!");
                                    }
                                });
                                // then, call the next intent / screen..
                                cb.onUserLoadCallback(VariablesUsed.currentUser);
                            } else {
                                Utils.showAlertMessage(context, "Please Verify Account", "To continue, please check verification link on your email account!");
                            }
                        } else {
                            Utils.showAlertMessage(context, "Incorrect Credentials","Please try again, or contact our Customer Service for help.");
                        }
                    }
                });
    }

    public static void UserSignup(Context context, String username, String email, String password, String name, String phone, String address){
        Users.save(username, email, password, name, phone, address);
    }

    public static void firebaseAuthWithGoogle(callbackHelper cb, Context context, String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        DatabaseHelper.getDbAuth().signInWithCredential(credential)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            VariablesUsed.loggedUser= DatabaseHelper.getDbAuth().getCurrentUser();

                            DatabaseVars.UsersTable dbVars = new DatabaseVars.UsersTable();
                            DatabaseReference dbRef = DatabaseHelper.getDb().getReference().child(dbVars.USERS_TABLE).child(VariablesUsed.loggedUser.getUid());

                            dbRef.child(dbVars.USERNAME).setValue(VariablesUsed.loggedUser.getDisplayName());
                            dbRef.child(dbVars.EMAIL).setValue(VariablesUsed.loggedUser.getEmail());
                            dbRef.child(dbVars.NAME).setValue(VariablesUsed.loggedUser.getDisplayName());
                            dbRef.child(dbVars.PHONE).setValue(VariablesUsed.loggedUser.getPhoneNumber());
                            dbRef.child(dbVars.ADDRESS).setValue(null);

                            VariablesUsed.currentUser = new Users(
                                    VariablesUsed.loggedUser.getUid(),
                                    VariablesUsed.loggedUser.getEmail(),
                                    VariablesUsed.loggedUser.getDisplayName(), //username, disamakan dengan name
                                    VariablesUsed.loggedUser.getDisplayName(),
                                    VariablesUsed.loggedUser.getPhoneNumber(),
                                    null, // address
                                    new Timestamp(System.currentTimeMillis()).toString()
                            );

                            cb.onUserLoadCallback(VariablesUsed.currentUser);
                        }
                        else {
                            Utils.showAlertMessage(context, "Failed Login","Please try again, or contact our Customer Service for help.");
                        }
                    }
                });
    }

    public static void uploadProfilePicture(String path){
        Uri file = Uri.fromFile(new File(path));
        StorageReference stRef = DatabaseHelper.getStorage().getReference().child("images/Users/" + VariablesUsed.currentUser.getUser_id());

        stRef.putFile(file);
    }

//    public static Bitmap downloadProfilePicture(){
//        Bitmap imageFile;
//        try {
//            File localFile = File.createTempFile(VariablesUsed.loggedUser.getUid(), "jpg");
//            StorageReference stRef = DatabaseHelper.getStorage().getReference().child("images/Users/");
//
//            stRef.getFile(localFile)
//            .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                    TODO: belum selesai
//                }
//            });
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return imageFile;
//
//    }
}
