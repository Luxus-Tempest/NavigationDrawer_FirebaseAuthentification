package ensa.ma.app_firebase;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class LoginRegister extends AppCompatActivity {
    private static final String TAG ="LoginRegister";
    int AUTHUI_REQUEST_CODE = 10001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_launcher);


        List<AuthUI.IdpConfig> provider = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()



        );
        Intent intent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(provider)
                .setTosAndPrivacyPolicyUrls("https://example.com","https://example.com" )
                .setAlwaysShowSignInMethodScreen(true)
                .build();

        // Launch the authentication activity
        startActivityForResult(intent,AUTHUI_REQUEST_CODE );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == AUTHUI_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                //we have signed user or we have a new user
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Log.d(TAG, "onActivityResult:" + user.getEmail());

                if(user.getMetadata().getCreationTimestamp() == user.getMetadata().getLastSignInTimestamp()){
                    Toast.makeText(this, "Welcome new user", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "Welcome back again", Toast.LENGTH_SHORT).show();
                }

                Intent intent = new Intent(this, NavigationDrawer.class);
                startActivity(intent);
                this.finish();

            } else{
                //sign in failed
                IdpResponse response = IdpResponse.fromResultIntent(data);
                if(response == null){
                    Log.d(TAG, "onActivityResult: the user has conceled the sign in request");
                }else {
                    Log.e(TAG, "onActivityResult: ", response.getError());
                }

            }
        }
    }
}
