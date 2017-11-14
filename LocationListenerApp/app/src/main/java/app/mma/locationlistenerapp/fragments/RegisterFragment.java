package app.mma.locationlistenerapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.telephony.PhoneNumberUtils;
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
import app.mma.locationlistenerapp.config.Config;
import app.mma.locationlistenerapp.net.ApiRes;
import app.mma.locationlistenerapp.net.UserInfo;
import app.mma.locationlistenerapp.utils.ColoredSnackbar;
import app.mma.locationlistenerapp.utils.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;


public class RegisterFragment extends Fragment {



    EditText inputFirstname, inputLastname;
    EditText inputPass, inputConfirmPass;
    EditText inputEmail, inputPhoneNumber;
    Button btnRegister;
    ProgressBar pb;
    SessionManager session;
    RegisterCallback callback;

    View scrollView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new SessionManager(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_register, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inputFirstname = (EditText) view.findViewById(R.id.input_firstname);
        inputLastname = (EditText) view.findViewById(R.id.input_lastname);
        inputPass = (EditText) view.findViewById(R.id.input_password);
        inputConfirmPass = (EditText) view.findViewById(R.id.input_confirmpassword);
        inputEmail = (EditText) view.findViewById(R.id.input_email);
        inputPhoneNumber = (EditText) view.findViewById(R.id.input_phoneNumber);
        btnRegister = (Button) view.findViewById(R.id.btn_register);
        pb = (ProgressBar) view.findViewById(R.id.pb);
        scrollView = view.findViewById(R.id.scrollview);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            String firstname, lastname, email, pass;
            String phoneNumber, passConfirm;

            @Override
            public void onClick(View v) {
                firstname = inputFirstname.getText().toString().trim();
                lastname = inputLastname.getText().toString().trim();
                email = inputEmail.getText().toString().trim();
//                phoneNumber = PhoneNumberUtils.formatNumber(inputPhoneNumber.getText().toString(),"US");
                phoneNumber = inputPhoneNumber.getText().toString();
                pass = inputPass.getText().toString();
                passConfirm = inputConfirmPass.getText().toString();

                if(validate()){
                    pb.setVisibility(View.VISIBLE);
                    App.apiService()
                            .register(firstname, lastname, phoneNumber, email, pass)
                            .enqueue(new Callback<ApiRes<UserInfo>>() {
                                @Override
                                public void onResponse(Call<ApiRes<UserInfo>> call, retrofit2.Response<ApiRes<UserInfo>> response) {
                                    pb.setVisibility(View.INVISIBLE);
                                    if(response == null || response.body() == null){
                                        return;
                                    }

                                    if(response.body().isError()){
                                        showToast(response.body().getMsg(), TastyToast.ERROR);
                                    } else {
                                        UserInfo info = response.body().getData();
                                        session.setUserInfo(info.getFirstname(), info.getLastname(), info.getEmail(), info.getPhoneNumber());
                                        showToast(response.body().getMsg(), TastyToast.SUCCESS);
                                        callback.onRegister();
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
                                            btnRegister.performClick();
                                        }
                                    });
                                    snackbar.show();
                                }
                            });
                }
            }

            private boolean validate(){
                if(firstname.isEmpty()){
                    inputFirstname.setError("This field is required");
                    inputFirstname.requestFocus();
                    return false;
                }
                if(lastname.isEmpty()){
                    inputLastname.setError("This field is required");
                    inputLastname.requestFocus();
                    return false;
                }
                if(email.isEmpty() || !email.contains("@") || !email.contains(".") ||
                        email.endsWith(".") || email.lastIndexOf(".") < email.lastIndexOf("@")){
                    inputEmail.setError("Wrong Email !");
                    inputEmail.requestFocus();
                    return false;
                }
                String regexStr = "^[+]?[0-9]{10,13}$";
                if(phoneNumber.length()<8 || phoneNumber.length()>15 || phoneNumber.matches(regexStr)==false  ) {
                    inputPhoneNumber.setError("Phone number is not valid");
                    inputPhoneNumber.requestFocus();
                    return false;
                }
                if(pass.length() < Config.MIN_PASSWORD_LENGTH) {
                    inputPass.setError("Password must be at least " + Config.MIN_PASSWORD_LENGTH + " characters");
                    inputPass.requestFocus();
                    return false;
                }
                if(! passConfirm.equals(pass)){
                    inputConfirmPass.setError("Passwords do not match!");
                    inputConfirmPass.requestFocus();
                    return false;
                }
                return true;
            }


        });
    }


    public interface RegisterCallback {
        public void onRegister();
    }


    private void showToast(String msg, int type){
        Toast toast = TastyToast.makeText(getContext(), msg, TastyToast.LENGTH_SHORT, type);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof RegisterCallback){
            callback = (RegisterCallback) context;
        } else {
            throw new RuntimeException("interface not implemented");
        }
    }
}
