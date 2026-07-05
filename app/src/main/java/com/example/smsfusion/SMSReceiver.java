package com.example.smsfusion;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SMSReceiver extends BroadcastReceiver {
    private static final String TAG = "SMSReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                if (pdus != null) {
                    for (Object pdu : pdus) {
                        SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
                        String messageBody = smsMessage.getMessageBody();
                        String sender = smsMessage.getOriginatingAddress();

                        Log.d(TAG, "SMS From: " + sender + ", Content: " + messageBody);

                        String otp = extractOTP(messageBody);
                        if (otp != null) {
                            Log.d(TAG, "Extracted OTP: " + otp);
                            // ওটিপি কোডটি এখান থেকে আপনার ক্লিপবোর্ড বা ফিউশন সার্ভারে পাঠানো যাবে
                        }
                    }
                }
            }
        }
    }

    private String extractOTP(String message) {
        Pattern pattern = Pattern.compile("(\\d{4,6})");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            return matcher.group(0);
        }
        return null;
    }
}
