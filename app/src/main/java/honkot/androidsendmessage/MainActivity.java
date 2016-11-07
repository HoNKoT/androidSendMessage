package honkot.androidsendmessage;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableInt;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import honkot.androidsendmessage.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private UserInput mInput;
    private ActivityMainBinding mBind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBind = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mInput = new UserInput();
        mBind.setInput(mInput);
        updateView();
    }

    private void updateView() {
        switch (mInput.getHow().get()) {
            case R.id.sms:
            case R.id.whatsapp:
                mBind.email.setVisibility(View.GONE);
                break;
        }
    }

    public class UserInput extends BaseObservable {
        private String TAG = "androidSendMessage";

        @Bindable
        private String phoneNumber = "";
        @Bindable
        private String email = "";
        @Bindable
        private String message = "";
        @Bindable
        private ObservableInt how = new ObservableInt();

        private final String EmptyMessage = "Hello";

        public UserInput() {
            // set Default RadioButton
            how.set(R.id.sms);
        }

        public String getPhoneNumber() { return phoneNumber;}
        public String getEmail() { return email;}
        public String getMessage() { return message;}
        public ObservableInt getHow() { return how;}

        public void setPhoneNumber(String value) {
            phoneNumber = value;
            notifyPropertyChanged(honkot.androidsendmessage.BR.phoneNumber);
        }
        public void setEmail(String value) {
            email = value;
            notifyPropertyChanged(honkot.androidsendmessage.BR.email);
        }
        public void setMessage(String value) {
            message = value;
            notifyPropertyChanged(honkot.androidsendmessage.BR.message);
        }
        public void setHow(ObservableInt id) {
            how = id;
            notifyPropertyChanged(honkot.androidsendmessage.BR.how);
        }
        public void onClickShare(View view) {
            dump();
        }
        public void onClickSend(View view) {
            dump();
        }
        public void onClickHow(View view) {
            updateView();
        }
        private void dump() {
            StringBuffer buf = new StringBuffer();
            buf.append("phoneNumber:'");
            buf.append(phoneNumber);
            buf.append("'");
            buf.append(System.getProperty("line.separator"));
            buf.append("email:'");
            buf.append(email);
            buf.append("'");
            buf.append(System.getProperty("line.separator"));
            buf.append("message:'");
            buf.append(message);
            buf.append("'");
            buf.append(System.getProperty("line.separator"));
            buf.append("radio:'");
            switch (how.get()) {
                case R.id.sms: buf.append("SMS"); break;
                case R.id.whatsapp: buf.append("WhatsApp"); break;
                default: buf.append("UNKNOWN:" + how.get()); break;
            }
            buf.append("'");
            Log.i(TAG, buf.toString());
        }
    }
}
