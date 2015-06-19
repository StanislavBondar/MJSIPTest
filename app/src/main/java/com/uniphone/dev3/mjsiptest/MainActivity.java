package com.uniphone.dev3.mjsiptest;

import android.speech.RecognitionListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.zoolu.net.IpAddress;
import org.zoolu.sip.address.NameAddress;
import org.zoolu.sip.address.SipURL;
import org.zoolu.sip.call.Call;
import org.zoolu.sip.call.CallListener;
import org.zoolu.sip.call.ExtendedCall;
import org.zoolu.sip.call.ExtendedCallListener;
import org.zoolu.sip.call.RegistrationClient;
import org.zoolu.sip.call.RegistrationClientListener;
import org.zoolu.sip.message.Message;
import org.zoolu.sip.provider.SipProvider;
import org.zoolu.sip.provider.SipStack;

import java.util.Vector;

import local.ua.UserAgent;
import local.ua.UserAgentListener;
import local.ua.UserAgentProfile;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MyAppLOG";
    private IpAddress mDeviceIp;
    private int port = 5060;
    int audioport = 3000;
    private String password = "3001";
    private String username = "3001";
    private String realm = "asterisk";
    private String mServerIp = "192.168.88.100:5060";
    String mFromUrl = username + "@" + "192.168.88.100:5060"; // FROM
    String mToUrl = "sip:3001@" + mServerIp; // TO
    Call call;
    SipProvider sipProvider;
    RegistrationClient registrationClient;
    TextView textView;
    NameAddress nameAddress1;
    private ExtendedCall mCall;
    NameAddress address;
    UserAgent agent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.hello);
        Log.v(TAG, "Start");
        if (!SipStack.isInit()) {
            SipStack.init(null);
            Log.v(TAG, "Stack.init");
        }



        SipStack.debug_level = 0;
        SipStack.auto_trying = true;
        SipStack.max_retransmission_timeout = 270;
        SipStack.default_port = 5065;
        SipStack.default_transport_protocols = new String[1];
        SipStack.default_transport_protocols[0] = "udp";

        mDeviceIp = IpAddress.getLocalHostAddress();
        sipProvider = new SipProvider("192.168.88.99", 5060);
        Log.v(TAG, "IP: " + mDeviceIp.toString() + " port: " + port);
        Log.v(TAG, "new sipProvider created");
        UserAgentProfile profile = new UserAgentProfile();
        //profile.audio = true;// using audio
        //profile.media_port = audioport;
        profile.hangup_time = 10;
        profile.auth_passwd = password;
        profile.auth_user = username;
        profile.auth_realm = realm;

        Log.v(TAG, "new profile");

        SipURL mAdressSipUrl = new SipURL("3001", "192.168.88.100");
       // NameAddress fromAdress = new NameAddress("\"3001\" sip:3001@192.168.88.100");

        nameAddress1 = new NameAddress("3001", mAdressSipUrl);
        SipURL sipURL = new SipURL(mServerIp);
        MyRegistrationListenr recognitionListener = new MyRegistrationListenr();
        ExCallListener exCallListener = new ExCallListener();
        mCall = new ExtendedCall(sipProvider, nameAddress1,username,realm,password,exCallListener);
        call = new Call(sipProvider, nameAddress1, new CallListener() {
            @Override
            public void onCallInvite(Call call, NameAddress nameAddress, NameAddress nameAddress1, String s, Message message) {
                Log.v(TAG, "Call "+"invite");
            }

            @Override
            public void onCallModify(Call call, String s, Message message) {
                Log.v(TAG, "Call "+"modify");
            }

            @Override
            public void onCallProgress(Call call, Message message) {
                Log.v(TAG, "Call "+"progress");
            }

            @Override
            public void onCallRinging(Call call, Message message) {
                Log.v(TAG, "Call "+"ringing");
            }

            @Override
            public void onCallAccepted(Call call, String s, Message message) {
                Log.v(TAG, "Call "+"accepted");
            }

            @Override
            public void onCallRefused(Call call, String s, Message message) {
                Log.v(TAG, "Call "+"refused");
            }

            @Override
            public void onCallRedirected(Call call, String s, Vector vector, Message message) {
                Log.v(TAG, "Call "+"redirected");
            }

            @Override
            public void onCallConfirmed(Call call, String s, Message message) {
                Log.v(TAG, "Call "+"confirmed");
            }

            @Override
            public void onCallTimeout(Call call) {
                Log.v(TAG, "Call "+"timeout");
            }

            @Override
            public void onCallReInviteAccepted(Call call, String s, Message message) {
                Log.v(TAG, "Call "+"Reinvite Accepted");
            }

            @Override
            public void onCallReInviteRefused(Call call, String s, Message message) {
                Log.v(TAG, "Call "+"Reinvite refused");
            }

            @Override
            public void onCallReInviteTimeout(Call call) {
                Log.v(TAG, "Call "+"Reinvite timeout");
            }

            @Override
            public void onCallCancel(Call call, Message message) {
                Log.v(TAG, "Call "+"cancel");
            }

            @Override
            public void onCallBye(Call call, Message message) {
                Log.v(TAG, "Call "+"bye");
            }

            @Override
            public void onCallClosed(Call call, Message message) {
                Log.v(TAG, "Call "+"closed");
            }
        });


        SipURL mAdressSipUrld = new SipURL("*100", "192.168.88.100");

        address = new NameAddress("*100", mAdressSipUrld);


        registrationClient = new RegistrationClient(
                sipProvider,
                sipURL,
                nameAddress1,
                nameAddress1,
                username,
                realm,
                password,
                recognitionListener);

        registrationClient.register();

        Log.v(TAG, "reg.register()");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public class MyRegistrationListenr implements RegistrationClientListener {
        @Override
        public void onRegistrationSuccess(RegistrationClient registrationClient, NameAddress nameAddress, final NameAddress nameAddress1, String s) {
            Log.v(TAG, "Listener: onUaRegistrationSuccess");
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SipURL mAdressSipUrl = new SipURL("*100", "192.168.88.100");
                    NameAddress address = new NameAddress("*100", mAdressSipUrl);
                    //mCall.acceptTransfer();

                    mCall.call(nameAddress1, address, null);
                }
            });

        }

        @Override
        public void onRegistrationFailure(RegistrationClient registrationClient, NameAddress nameAddress, NameAddress nameAddress1, String s) {
            Log.v(TAG, "Listener: onUaRegistrationFailure");
            Log.v(TAG, "Failure ===== " + registrationClient.toString() + "\n" +
                    nameAddress.toString() + "\n" + nameAddress1.toString() + "\n" +
                    s);
        }
    }

    public class ExCallListener implements ExtendedCallListener {

        @Override
        public void onCallTransfer(ExtendedCall extendedCall, NameAddress nameAddress, NameAddress nameAddress1, Message message) {

        }

        @Override
        public void onCallAttendedTransfer(ExtendedCall extendedCall, NameAddress nameAddress, NameAddress nameAddress1, String s, Message message) {

        }

        @Override
        public void onCallTransferAccepted(ExtendedCall extendedCall, Message message) {

        }

        @Override
        public void onCallTransferRefused(ExtendedCall extendedCall, String s, Message message) {

        }

        @Override
        public void onCallTransferSuccess(ExtendedCall extendedCall, Message message) {

        }

        @Override
        public void onCallTransferFailure(ExtendedCall extendedCall, String s, Message message) {

        }

        @Override
        public void onCallInvite(Call call, NameAddress nameAddress, NameAddress nameAddress1, String s, Message message) {

        }

        @Override
        public void onCallModify(Call call, String s, Message message) {

        }

        @Override
        public void onCallProgress(Call call, Message message) {

        }

        @Override
        public void onCallRinging(Call call, Message message) {

        }

        @Override
        public void onCallAccepted(Call call, String s, Message message) {

        }

        @Override
        public void onCallRefused(Call call, String s, Message message) {

        }

        @Override
        public void onCallRedirected(Call call, String s, Vector vector, Message message) {

        }

        @Override
        public void onCallConfirmed(Call call, String s, Message message) {

        }

        @Override
        public void onCallTimeout(Call call) {

        }

        @Override
        public void onCallReInviteAccepted(Call call, String s, Message message) {

        }

        @Override
        public void onCallReInviteRefused(Call call, String s, Message message) {

        }

        @Override
        public void onCallReInviteTimeout(Call call) {

        }

        @Override
        public void onCallCancel(Call call, Message message) {

        }

        @Override
        public void onCallBye(Call call, Message message) {

        }

        @Override
        public void onCallClosed(Call call, Message message) {

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
