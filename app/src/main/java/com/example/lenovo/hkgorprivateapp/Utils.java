package com.example.lenovo.hkgorprivateapp;

import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.util.regex.Pattern;

/**
 * Created by GorA on 9/23/16.
 */

public class Utils {
    private static final Pattern DIR_SEPORATOR = Pattern.compile("/");

    /**
     * Raturns all available SD-Cards in the system (include emulated)
     * <p>
     * Warning: Hack! Based on Android source code of version 4.3 (API 18)
     * Because there is no standart way to get it.
     *
     * @return paths to all available SD-Cards in the system (include emulated)
     */
    public static String getStorageDirectories() {
        // Final set of paths
        String rv = "";

        // Primary physical SD-CARD (not emulated)
        final String rawExternalStorage = System.getenv("EXTERNAL_STORAGE");


        // All Secondary SD-CARDs (all exclude primary) separated by ":"
        final String rawSecondaryStoragesStr = System.getenv("SECONDARY_STORAGE");


        // Primary emulated SD-CARD

        final String rawEmulatedStorageTarget = System.getenv("EMULATED_STORAGE_TARGET");


        if (TextUtils.isEmpty(rawEmulatedStorageTarget)) {
            // Device has physical external storage; use plain paths.
            if (TextUtils.isEmpty(rawExternalStorage)) {
                // EXTERNAL_STORAGE undefined; falling back to default.
                return ("/storage/sdcard0");

            } else {
                return (rawExternalStorage);
            }
        } else {
            // Device has emulated storage; external storage paths should have
            // userId burned into them.
            final String rawUserId;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
                rawUserId = "";
            } else {
                final String path = Environment.getExternalStorageDirectory().getAbsolutePath();
                final String[] folders = DIR_SEPORATOR.split(path);
                final String lastFolder = folders[folders.length - 1];
                boolean isDigit = false;
                try {
                    Integer.valueOf(lastFolder);
                    isDigit = true;
                } catch (NumberFormatException ignored) {
                }
                rawUserId = isDigit ? lastFolder : "";
            }
            // /storage/emulated/0[1,2,...]
            if (TextUtils.isEmpty(rawUserId)) {
                return (rawEmulatedStorageTarget);
            } else {
                return (rawEmulatedStorageTarget + File.separator + rawUserId);
            }
        }
//        // Add all secondary storages
//        if (!TextUtils.isEmpty(rawSecondaryStoragesStr)) {
//            // All Secondary SD-CARDs splited into array
//            final String[] rawSecondaryStorages = rawSecondaryStoragesStr.split(File.pathSeparator);
//            Collections.addAll(rv, rawSecondaryStorages);
//        }
    }

    public static String getStorageDirectoriesCustom() {

        if(new File("/storage/extSdCard"+ Constants.FILES_FOLDER_NAME).exists()) {
            return "/storage/extSdCard";
        }
        if(new File("/storage/sdcard1"+ Constants.FILES_FOLDER_NAME).exists()) {
            return "/storage/sdcard1";
        }
        if(new File("/storage/usbcard1"+ Constants.FILES_FOLDER_NAME).exists()) {
            return "/storage/usbcard1";
        }
        if(new File("/storage/sdcard0"+ Constants.FILES_FOLDER_NAME).exists()) {
            return "/storage/sdcard0";
        }
        return getStorageDirectories();
    }

}
