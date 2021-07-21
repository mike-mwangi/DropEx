package com.example.dropex;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class AuthResultContract extends ActivityResultContract<Integer, IdpResponse> {
    private List<AuthUI.IdpConfig> providers= Arrays.asList(
            new AuthUI.IdpConfig.PhoneBuilder().build());


    @Override
    public Intent createIntent(@NonNull @NotNull Context context, Integer input) {
        AuthMethodPickerLayout authMethodPickerLayout = new AuthMethodPickerLayout
                .Builder(R.layout.layout_sign_in)
                .setPhoneButtonId(R.id.btn_phone_login).build();



        Intent data = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAuthMethodPickerLayout(authMethodPickerLayout)
                .setIsSmartLockEnabled(false)
                .setTheme(R.style.Theme_MyApp)

                .setAvailableProviders(providers)
                .build();

        return data;
    }



    @Override
    public IdpResponse parseResult(int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent intent) {
        if(resultCode== Activity.RESULT_OK){
            return IdpResponse.fromResultIntent(intent);
        }
        else {
            return null;
        }
    }

}
