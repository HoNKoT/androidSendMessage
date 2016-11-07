package honkot.androidsendmessage;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableInt;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import honkot.androidsendmessage.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private UserInput mInput;
    private ActivityMainBinding mBind;
    private CheckRuntimePermission mCheckPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBind = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mCheckPermission = new CheckRuntimePermission();
        mCheckPermission.checkPermission(this);
        mInput = new UserInput();
        mBind.setInput(mInput);
        updateView();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        mCheckPermission.onRequestPermissionsResult(requestCode, permissions, grantResults);
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

        private final String DEFAULT_MESSAGE = "Hello";

        public UserInput() {
            // set Default RadioButton
            how.set(R.id.sms);
        }

        /* getter */
        public String getPhoneNumber() { return phoneNumber;}
        public String getEmail() { return email;}
        public String getMessage() { return message;}
        public ObservableInt getHow() { return how;}

        /* setter */
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

        /* click event handle */

        /**
         * Event handler when clicked Share button.
         * @param view
         */
        public void onClickShare(View view) {
            dump();

            if (!checkRequired()) {
                Toast.makeText(getApplicationContext(), "Check number or email first!", Toast.LENGTH_SHORT).show();
                return;
            }

            switch (how.get()) {
                case R.id.sms:
                    Uri uri=Uri.parse("smsto:" + phoneNumber);
                    Intent SMSintent=new Intent(Intent.ACTION_SENDTO, uri);
                    SMSintent.putExtra("sms_body", message.isEmpty() ? DEFAULT_MESSAGE : message);
                    SMSintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startActivity(SMSintent);
                    break;
                case R.id.whatsapp:
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, message.isEmpty() ? DEFAULT_MESSAGE : message);
                    sendIntent.setType("text/plain");
                    sendIntent.setPackage("com.whatsapp");
                    startActivity(sendIntent);
                    break;
            }
        }

        /**
         * Event handler when clicked Send button.
         * @param view
         */
        public void onClickSend(View view) {
            dump();

            if (!checkRequired()) {
                Toast.makeText(getApplicationContext(), "Check number or email first!", Toast.LENGTH_SHORT).show();
                return;
            }

            switch (how.get()) {
                case R.id.sms:
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(
                            phoneNumber, null,
                            message.isEmpty() ? DEFAULT_MESSAGE : message, null, null);
                    break;
                case R.id.whatsapp:
                    Uri uri = Uri.parse("smsto:" + phoneNumber);
                    Intent sendIntent = new Intent(Intent.ACTION_SENDTO, uri);
                    sendIntent.setPackage("com.whatsapp");
                    startActivity(sendIntent);
                    break;
            }
        }

        /**
         * Event handler when clicked Radio Button.
         * @param view
         */
        public void onClickHow(View view) {
            // just update view
            updateView();
        }

        private boolean checkRequired() {
            if (mBind.phoneNumber.getVisibility() != View.GONE) {
                if (phoneNumber.isEmpty()) {// || !phoneNumber.startsWith("+")) {
                    return false;
                }
            }

            if (mBind.email.getVisibility() != View.GONE) {
                if (email.isEmpty() || !email.contains("@")) {
                    return false;
                }
            }

            return true;
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
