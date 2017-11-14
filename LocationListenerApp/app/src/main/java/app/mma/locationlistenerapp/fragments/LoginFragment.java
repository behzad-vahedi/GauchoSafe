package app.mma.locationlistenerapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;

import app.mma.locationlistenerapp.R;
import app.mma.locationlistenerapp.config.App;
import app.mma.locationlistenerapp.net.ApiRes;
import app.mma.locationlistenerapp.net.UserInfo;
import app.mma.locationlistenerapp.utils.ColoredSnackbar;
import app.mma.locationlistenerapp.utils.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;


public class LoginFragment extends Fragment {

    EditText inputEmail, inputPassword;
    Button btnLogin;
    ProgressBar pb;
    SessionManager session;
    LoginCallback callback;
    View scrollView;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new SessionManager(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_login, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inputEmail = (EditText) view.findViewById(R.id.input_email);
        inputPassword = (EditText) view.findViewById(R.id.input_password);
        btnLogin = (Button) view.findViewById(R.id.btn_login);
        pb = (ProgressBar) view.findViewById(R.id.pb);
        scrollView = view.findViewById(R.id.scrollview);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            String email, password;
            @Override
            public void onClick(View v) {
                email = inputEmail.getText().toString().trim();
                password = inputPassword.getText().toString();
                if(validate()){
                    pb.setVisibility(View.VISIBLE);
                    App.apiService()
                            .login(email, password)
                            .enqueue(new Callback<ApiRes<UserInfo>>() {
                                @Override
                                public void onResponse(Call<ApiRes<UserInfo>> call, retrofit2.Response<ApiRes<UserInfo>> response) {
                                    pb.setVisibility(View.INVISIBLE);
                                    if(response.body() == null) return;
                                    if(response.body().isError()){
                                        showToast(response.body().getMsg(), TastyToast.ERROR);
                                    } else {
                                        UserInfo data = response.body().getData();
                                        session.setUserInfo(data.getFirstname(), data.getLastname(), data.getEmail(), data.getPhoneNumber());
                                        showToast(response.body().getMsg(), TastyToast.SUCCESS);
                                        callback.onLogin();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ApiRes<UserInfo>> call, Throwable t) {
                                    pb.setVisibility(View.INVISIBLE);
                                    final Snackbar snackbar = ColoredSnackbar.alert(
                                            Snackbar.make(scrollView, "Error : " + t.getLocalizedMessage(), Snackbar.LENGTH_LONG));
                                    snackbar.setAction("RETRY", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            snackbar.dismiss();
                                            btnLogin.performClick();
                                        }
                                    });

                                    snackbar.show();
                                }
                            });
                }

            }

            public boolean validate(){
                if(email.isEmpty()){
                    inputEmail.setError("Enter your email");
                    inputEmail.requestFocus();
                    return false;
                }
                if(password.isEmpty()){
                    inputPassword.setError("Enter password");
                    inputPassword.requestFocus();
                    return false;
                }
                return true;
            }
        });
    }


    private void showToast(String msg, int type){
        Toast toast = TastyToast.makeText(getContext(), msg, TastyToast.LENGTH_SHORT, type);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public interface LoginCallback {
        public void onLogin();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof LoginCallback){
            callback = (LoginCallback) context;
        } else {
            throw new RuntimeException("interface not implemented");
        }
    }
}
