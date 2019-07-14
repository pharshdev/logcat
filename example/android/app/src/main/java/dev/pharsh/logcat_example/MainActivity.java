package dev.pharsh.logcat_example;

import android.os.Bundle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import io.flutter.app.FlutterActivity;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugins.GeneratedPluginRegistrant;

public class MainActivity extends FlutterActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GeneratedPluginRegistrant.registerWith(this);

        new MethodChannel(getFlutterView(), "app.channel.logcat").setMethodCallHandler((call, result) -> {
            if (call.method.equals("execLogcat")) {
                String logs = getLogs();

                if (logs != null) {
                    result.success(logs);
                } else {
                    result.error("UNAVAILABLE", "logs not available.", null);
                }
            } else {
                result.notImplemented();
            }
        });
    }

    String getLogs() {
        try {
            Process process = Runtime.getRuntime().exec("logcat -d");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            StringBuilder log = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                log.append(line);
            }
            return log.toString();
        } catch (IOException e) {
            return "EXCEPTION" + e.toString();
        }
    }
}
