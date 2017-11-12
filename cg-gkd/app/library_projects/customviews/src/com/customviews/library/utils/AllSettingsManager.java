package com.customviews.library.utils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import com.google.android.gms.maps.model.LatLng;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.customviews.library.model.PInfo;
import com.customviews.library.typefacehelper.TypefaceCollection;
import com.customviews.library.typefacehelper.TypefaceHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class AllSettingsManager {

    // set language
    public static void setLocale(Context context, String localeName) {
        Locale locale = new Locale(localeName);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config,
                context.getResources().getDisplayMetrics());
    }

    // Check internet connectivity
    public enum NetworkSet {
        NETWORK_3G, NETWORK_WIFI, NETWORK_ALL
    }

    public static boolean networkEnable(Context c, NetworkSet netset) {

        ConnectivityManager cm = (ConnectivityManager) c
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if (networkInfo == null) {
            return false;
        }

        if (!networkInfo.isAvailable()) {
            return false;
        } else if (!cm.getActiveNetworkInfo().isConnected()) {
            return false;
        } else {
            return true;
        }

    }

    public static boolean isWifiAvailable(Context ctx) {

        ConnectivityManager myConnManager = (ConnectivityManager) ctx

                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo myNetworkInfo = myConnManager

                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (myNetworkInfo.isConnected())

            return true;

        else

            return false;

    }

    public static boolean isGpsEnabled(Context ctx) {

        LocationManager locationManager = (LocationManager) ctx

                .getSystemService(Context.LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))

            return true;

        else

            return false;

    }

    public static boolean isWifiLocationEnabled(Context context) {
        ContentResolver cr = context.getContentResolver();
        String enabledProviders = Settings.Secure.getString(cr,
                Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (!TextUtils.isEmpty(enabledProviders)) {
            // not the fastest way to do that :)
            String[] providersList = TextUtils.split(enabledProviders, ",");
            for (String provider : providersList) {
                if (LocationManager.NETWORK_PROVIDER.equals(provider)) {
                    return true;
                }
            }
        }
        return false;
    }

    /*
     * font with path: fonts/ubuntu/Ubuntu-B.ttf
     */
    public static void iniCustomFonts(Context context,
                                      String normalFontWithPath, String boldFontWithPath,
                                      String italicFontWithPath, String boldItalicFontWithPath) {

        // Load helper with default custom typeface (single custom typeface)
        TypefaceHelper.init(new TypefaceCollection.Builder()
                .set(Typeface.NORMAL,
                        Typeface.createFromAsset(context.getAssets(),
                                normalFontWithPath))
                .set(Typeface.BOLD,
                        Typeface.createFromAsset(context.getAssets(),
                                boldFontWithPath))
                .set(Typeface.ITALIC,
                        Typeface.createFromAsset(context.getAssets(),
                                italicFontWithPath))
                .set(Typeface.BOLD_ITALIC,
                        Typeface.createFromAsset(context.getAssets(),
                                boldItalicFontWithPath)).create());

        // Multiple custom typefaces support
        TypefaceCollection mRobotoTypeface = new TypefaceCollection.Builder()
                .set(Typeface.NORMAL,
                        Typeface.createFromAsset(context.getAssets(),
                                normalFontWithPath))
                .set(Typeface.BOLD,
                        Typeface.createFromAsset(context.getAssets(),
                                boldFontWithPath))
                .set(Typeface.ITALIC,
                        Typeface.createFromAsset(context.getAssets(),
                                italicFontWithPath))
                .set(Typeface.BOLD_ITALIC,
                        Typeface.createFromAsset(context.getAssets(),
                                boldItalicFontWithPath)).create();

        // Multiple custom typefaces support
        TypefaceCollection mSystemDefaultTypeface = TypefaceCollection
                .createSystemDefault();
    }

    // Remove first and last character from a string
    public static String removeFirstAndLast(String data) {

        return data.substring(1, data.length() - 1);
    }

    public static boolean isNullOrEmpty(String myString) {
        if (myString == null) {
            return true;
        }
        if (myString.length() == 0 || myString.equalsIgnoreCase("null")
                || myString.equalsIgnoreCase("")) {
            return true;
        }
        return false;
    }

    public static boolean isInteger(String s) {
        if (s == null || s.length() == 0)
            return false;
        for (int i = 0; i < s.length(); i++) {
            if (Character.digit(s.charAt(i), 10) < 0)
                return false;
        }
        return true;
    }

    // encrypt data and save to sd card
    public static void encryptFileFromStringAndSaveToSDCard(
            String directoryFolderName, String fileName, String data)
            throws IOException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException {

        // Sd card checking
        Boolean isSDPresent = Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED);

        if (isSDPresent) {
            File root = Environment.getExternalStorageDirectory();

            File outDir = new File(root.getAbsolutePath() + File.separator
                    + directoryFolderName);
            if (!outDir.isDirectory()) {
                outDir.mkdir();
            }

            // Here you read the cleartext.

            InputStream fis = new ByteArrayInputStream(data.getBytes());

            // This stream write the encrypted text. This stream will be wrapped
            // by
            // another stream.
            FileOutputStream fos = new FileOutputStream(outDir + File.separator
                    + fileName);

            // Length is 16 byte
            SecretKeySpec sks = new SecretKeySpec(
                    "MyDifficultPassw".getBytes(), "AES");
            // Create cipher
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, sks);
            // Wrap the output stream
            CipherOutputStream cos = new CipherOutputStream(fos, cipher);
            // Write bytes
            int b;
            byte[] d = new byte[8];
            while ((b = fis.read(d)) != -1) {
                cos.write(d, 0, b);
            }
            // Flush and close streams.
            cos.flush();
            cos.close();
            fis.close();
        }
    }

    public static void encryptFileFromSDCardAndSaveToSDCard(
            String fileNameWithPath, String encryptedFileNameWithPath)
            throws IOException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException {

        // Here you read the cleartext.

        FileInputStream fis = new FileInputStream(fileNameWithPath);
        // This stream write the encrypted text. This stream will be wrapped by
        // another stream.
        FileOutputStream fos = new FileOutputStream(encryptedFileNameWithPath);

        // Length is 16 byte
        SecretKeySpec sks = new SecretKeySpec("MyDifficultPassw".getBytes(),
                "AES");
        // Create cipher
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, sks);
        // Wrap the output stream
        CipherOutputStream cos = new CipherOutputStream(fos, cipher);
        // Write bytes
        int b;
        byte[] d = new byte[8];
        while ((b = fis.read(d)) != -1) {
            cos.write(d, 0, b);
        }
        // Flush and close streams.
        cos.flush();
        cos.close();
        fis.close();
    }

    public static void encryptFileFromAssetAndSaveToSDCard(Context context,
                                                           String directoryFolderName, String fileName) throws IOException,
            NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException {

        String[] files = null;

        // Sd card checking
        Boolean isSDPresent = Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED);

        if (isSDPresent) {
            File root = Environment.getExternalStorageDirectory();

            AssetManager assetManager = context.getAssets();
            try {
                files = assetManager.list(directoryFolderName);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            if (files.length != 0) {

                File outDir = new File(root.getAbsolutePath() + File.separator
                        + directoryFolderName);
                if (!outDir.isDirectory()) {
                    outDir.mkdir();
                }
                for (int i = 0; i < files.length; i++) {
                    // String[] files = null;

                    if (files[i].equalsIgnoreCase(fileName)) {

                        InputStream in = null;
                        OutputStream out = null;
                        try {
                            in = assetManager.open(directoryFolderName
                                    + File.separator + files[i]);
                            out = new FileOutputStream(root.getAbsolutePath()
                                    + File.separator + directoryFolderName
                                    + File.separator + files[i]);

                            // Length is 16 byte
                            SecretKeySpec sks = new SecretKeySpec(
                                    "MyDifficultPassw".getBytes(), "AES");
                            // Create cipher
                            Cipher cipher = Cipher.getInstance("AES");
                            cipher.init(Cipher.ENCRYPT_MODE, sks);
                            // Wrap the output stream
                            CipherOutputStream cos = new CipherOutputStream(
                                    out, cipher);
                            // Write bytes
                            int b;
                            byte[] d = new byte[8];
                            while ((b = in.read(d)) != -1) {
                                cos.write(d, 0, b);
                            }
                            // Flush and close streams.
                            cos.flush();
                            cos.close();
                            cos = null;
                            out = null;
                            in.close();
                            in = null;

                        } catch (IOException e) {
                            Log.e("tag", "Failed to copy asset file: "
                                    + files[i], e);
                        }

                    }
                }
            }
        }

    }

    public static void decryptFileFromSDCardAndSaveToSDCard(
            String encryptedFileNameWithPath, String decryptedFileNameWithPath)
            throws IOException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException {

        FileInputStream fis = new FileInputStream(encryptedFileNameWithPath);

        FileOutputStream fos = new FileOutputStream(decryptedFileNameWithPath);
        SecretKeySpec sks = new SecretKeySpec("MyDifficultPassw".getBytes(),
                "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, sks);
        CipherInputStream cis = new CipherInputStream(fis, cipher);
        int b;
        byte[] d = new byte[8];
        while ((b = cis.read(d)) != -1) {
            fos.write(d, 0, b);
        }
        fos.flush();
        fos.close();
        cis.close();
    }

    // decrypt data
    public static String getAllDecryptedJSONStringFromSDCard(Context con,
                                                             String directoryFolderName, String fileName) throws JSONException,
            IOException, FileNotFoundException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException {

        JSONObject jObj;
        String myData = "";
        // Load file content
        File root = Environment.getExternalStorageDirectory();
        // File outDir = new File(root.getAbsolutePath() + File.separator
        // + directoryFolderName);
        File outDir = new File(root.getAbsolutePath() + File.separator
                + directoryFolderName + File.separator + fileName);
        if (outDir.exists()) {

            FileInputStream fis = new FileInputStream(outDir);
            SecretKeySpec sks = new SecretKeySpec(
                    "MyDifficultPassw".getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, sks);
            CipherInputStream is = new CipherInputStream(fis, cipher);

            StringBuffer fileContent = new StringBuffer("");

            byte[] buffer = new byte[1024];
            int length;

            while ((length = is.read(buffer)) != -1) {
                fileContent.append(new String(buffer));
            }
            myData = fileContent.toString();
            // jObj = new JSONObject(fileContent.toString());
            is.close();
            return myData;
        } else {

            return myData;
        }
    }

    // get device logged in email
    public static String getDeviceLoggedInEmail(Context context) {
        ArrayList<String> emailID = new ArrayList<String>();
        emailID.clear();
        Pattern emailPattern = Patterns.EMAIL_ADDRESS;
        Account[] accounts = AccountManager.get(context).getAccounts();
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                emailID.add(account.name);
            }
        }

        if (emailID.size() > 0) {
            Log.d("emailID", emailID.get(0));
            return emailID.get(0);
        } else {
            return null;
        }

    }

    // checking installed app
    public static boolean isAppInstalled(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    // checking enabled app
    public static boolean isAppEnabled(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        boolean app_enabled = false;
        try {
            ApplicationInfo af = pm.getApplicationInfo(packageName, 0);
            if (af.enabled) {
                app_enabled = true;
            } else {
                app_enabled = false;
            }
        } catch (NameNotFoundException e) {
            app_enabled = false;
        }
        return app_enabled;
    }

    // app info
    public static ArrayList<PInfo> getAllPackagesInfo(Context context,
                                                      boolean isSysPackages) {
        //false=no system package
        ArrayList<PInfo> apps = getAllInstalledApps(context, isSysPackages);
        final int max = apps.size();
        for (int i = 0; i < max; i++) {
            apps.get(i).prettyPrint();
        }
        return apps;
    }

    public static PInfo getSpecificPackageInfo(Context context,
                                               String packageName, boolean isSysPackages) {
        //false=no system package
        ArrayList<PInfo> apps = getAllInstalledApps(context, isSysPackages);
        final int max = apps.size();
        for (int i = 0; i < max; i++) {
            apps.get(i).prettyPrint();
            if (apps.get(i).getPname().equalsIgnoreCase(packageName)) {
                return apps.get(i);
            }
        }
        return null;
    }

    public static ArrayList<PInfo> getAllInstalledApps(Context context,
                                                       boolean isSysPackages) {
        ArrayList<PInfo> res = new ArrayList<PInfo>();
        List<PackageInfo> packs = context.getPackageManager()
                .getInstalledPackages(0);
        for (int i = 0; i < packs.size(); i++) {
            PackageInfo p = packs.get(i);
            if ((!isSysPackages) && (p.versionName == null)) {
                continue;
            }
            PInfo newInfo = new PInfo();
            newInfo.setAppname(p.applicationInfo.loadLabel(
                    context.getPackageManager()).toString());
            newInfo.setPname(p.packageName);
            newInfo.setVersionName(p.versionName);
            newInfo.setVersionCode(p.versionCode);
            newInfo.setIcon(p.applicationInfo.loadIcon(context
                    .getPackageManager()));
            res.add(newInfo);

        }
        return res;
    }

    // get updated facebook package
    public static boolean getUpdatedFacebookPackageInfo(Context context,
                                                        String facebookPackage, int versionName) {
        String packageName = facebookPackage;
        boolean isUpdated = false;

        PInfo appInfo = getSpecificPackageInfo(context, packageName, false);
        Log.d("fbpinfo", appInfo.getVersionName() + "");
        if (appInfo != null) {
            String version[] = appInfo.getVersionName().split("\\.");
            Log.d("fbpinfo", version.length + "");
            if (version.length > 0) {
                int versionNum = Integer.parseInt(version[0]);
                Log.d("fbpinfo", versionNum + "");
                if (versionNum <= versionName) {
                    isUpdated = true;
                }

            }
        }

        return isUpdated;
    }

    // validation check
    public static boolean isValidCreditCard(String cardNumber) {

        if (cardNumber.length() == 0) {
            return false;
        }
        final String digitsOnly = AllSettingsManager.getDigitsOnly(cardNumber);
        int sum = 0;
        int digit = 0;
        int addend = 0;
        boolean timesTwo = false;

        for (int i = digitsOnly.length() - 1; i >= 0; i--) {
            digit = Integer.parseInt(digitsOnly.substring(i, i + 1));
            if (timesTwo) {
                addend = digit * 2;
                if (addend > 9) {
                    addend -= 9;
                }
            } else {
                addend = digit;
            }
            sum += addend;
            timesTwo = !timesTwo;
        }

        final int modulus = sum % 10;
        return modulus == 0;

    }

    private static String getDigitsOnly(String s) {
        final StringBuffer digitsOnly = new StringBuffer();
        char c;
        for (int i = 0; i < s.length(); i++) {
            c = s.charAt(i);
            if (Character.isDigit(c)) {
                digitsOnly.append(c);
            }
        }
        return digitsOnly.toString();
    }

    public static boolean validateEmail(final String text) {

        if (text.length() == 0) {
            return false;
        }

        final Pattern p = Pattern
                .compile("[a-zA-Z0-9._-]+@[a-zA-Z0-9]+\\.+[a-z.]+");

        // Match the given string with the pattern
        final Matcher m = p.matcher(text);

        // check whether match is found
        final boolean matchFound = m.matches();

        final StringTokenizer st = new StringTokenizer(text, ".");
        String lastToken = null;
        while (st.hasMoreTokens()) {
            lastToken = st.nextToken();
        }

        if (matchFound && lastToken.length() >= 2
                && text.length() - 1 != lastToken.length()) {

            // validate the country code
            return true;
        } else {
            return false;
        }
    }

    // Rotate image file
    public static Bitmap rotateBitmap(String src, Bitmap bitmap) {
        try {
            int orientation = getExifOrientation(src);

            if (orientation == 1) {
                return bitmap;
            }

            Matrix matrix = new Matrix();
            switch (orientation) {
                case 2:
                    matrix.setScale(-1, 1);
                    break;
                case 3:
                    matrix.setRotate(180);
                    break;
                case 4:
                    matrix.setRotate(180);
                    matrix.postScale(-1, 1);
                    break;
                case 5:
                    matrix.setRotate(90);
                    matrix.postScale(-1, 1);
                    break;
                case 6:
                    matrix.setRotate(90);
                    break;
                case 7:
                    matrix.setRotate(-90);
                    matrix.postScale(-1, 1);
                    break;
                case 8:
                    matrix.setRotate(-90);
                    break;
                default:
                    return bitmap;
            }

            try {
                Bitmap oriented = Bitmap.createBitmap(bitmap, 0, 0,
                        bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                bitmap.recycle();
                return oriented;
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                return bitmap;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    private static int getExifOrientation(String src) throws IOException {
        int orientation = 1;

        try {
            /**
             * if your are targeting only api level >= 5 ExifInterface exif =
             * new ExifInterface(src); orientation =
             * exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
             */
            if (Build.VERSION.SDK_INT >= 5) {
                Class<?> exifClass = Class
                        .forName("android.media.ExifInterface");
                Constructor<?> exifConstructor = exifClass
                        .getConstructor(new Class[]{String.class});
                Object exifInstance = exifConstructor
                        .newInstance(new Object[]{src});
                Method getAttributeInt = exifClass.getMethod("getAttributeInt",
                        new Class[]{String.class, int.class});
                Field tagOrientationField = exifClass
                        .getField("TAG_ORIENTATION");
                String tagOrientation = (String) tagOrientationField.get(null);
                orientation = (Integer) getAttributeInt.invoke(exifInstance,
                        new Object[]{tagOrientation, 1});
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        return orientation;
    }

    public static int dpToPx(Resources res, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                res.getDisplayMetrics());
    }

    // get activity or package name
    public static String getActivityOrPackageNmae(Activity activity,
                                                  boolean isActivity, boolean isPackage) {
        ActivityManager am = (ActivityManager) activity
                .getSystemService(activity.ACTIVITY_SERVICE);
        List<RunningTaskInfo> taskInfo = am.getRunningTasks(1);
        ComponentName componentInfo = taskInfo.get(0).topActivity;
        Log.d("activity/packagename", "CURRENT Activity ::"
                + taskInfo.get(0).topActivity.getClassName()
                + "   Package Name :  " + componentInfo.getPackageName());

        String strNmae = "";

        if (!isActivity && !isPackage) {
            strNmae = "Wrong input. Choose any one ture.";
        } else if (isActivity && isPackage) {
            strNmae = "Wrong input. Choose any one ture.";
        } else if (isActivity) {
            strNmae = activity.getClass().getSimpleName();
        } else if (isPackage) {
            strNmae = componentInfo.getPackageName();
        }

        Log.d("activity/packagename", strNmae);
        return strNmae;

    }

    // get json form sdcard
    public static String getAllJSONStringFromSDCard(Context con,
                                                    String directoryFolderName, String fileName) throws JSONException,
            IOException, FileNotFoundException {
        JSONObject jObj;
        String myData = "";
        // Load file content
        File root = Environment.getExternalStorageDirectory();
        File outDir = new File(root.getAbsolutePath() + File.separator
                + directoryFolderName + File.separator + fileName);
        if (outDir.exists()) {

            InputStream is;

            is = new BufferedInputStream(new FileInputStream(outDir));
            // InputStream is = getAssets().open(filename);
            StringBuffer fileContent = new StringBuffer("");

            byte[] buffer = new byte[1024];
            int length;

            while ((length = is.read(buffer)) != -1) {
                fileContent.append(new String(buffer));
            }
            myData = fileContent.toString();

            return myData;
        } else {

            return myData;
        }

    }

    // decode any image
    public static void decodeAnyImage(String filePath, ImageView iv) {
        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 1024;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        Bitmap mBitmap = BitmapFactory.decodeFile(filePath, o2);

        iv.setImageBitmap(mBitmap);

    }

    /**
     * This snippet allows UI on main thread. Normally it's 2 lines but since
     * we're supporting 2.x, we need to reflect.
     */
    public static void disableStrictMode() {

        try {
            Class<?> strictModeClass = Class.forName("android.os.StrictMode",
                    true, Thread.currentThread().getContextClassLoader());
            Class<?> threadPolicyClass = Class.forName(
                    "android.os.StrictMode$ThreadPolicy", true, Thread
                            .currentThread().getContextClassLoader());
            Class<?> threadPolicyBuilderClass = Class.forName(
                    "android.os.StrictMode$ThreadPolicy$Builder", true, Thread
                            .currentThread().getContextClassLoader());

            Method setThreadPolicyMethod = strictModeClass.getMethod(
                    "setThreadPolicy", threadPolicyClass);

            Method detectAllMethod = threadPolicyBuilderClass
                    .getMethod("detectAll");
            Method penaltyMethod = threadPolicyBuilderClass
                    .getMethod("penaltyLog");
            Method buildMethod = threadPolicyBuilderClass.getMethod("build");

            Constructor<?> threadPolicyBuilderConstructor = threadPolicyBuilderClass
                    .getConstructor();
            Object threadPolicyBuilderObject = threadPolicyBuilderConstructor
                    .newInstance();

            Object obj = detectAllMethod.invoke(threadPolicyBuilderObject);

            obj = penaltyMethod.invoke(obj);
            Object threadPolicyObject = buildMethod.invoke(obj);
            setThreadPolicyMethod.invoke(strictModeClass, threadPolicyObject);
        } catch (Exception ex) {
            Log.w("disableStrictMode", ex);
        }
    }

    // apply font for any view
    public static void applyFont(final Context context, final View root,
                                 final String fontName) {
        try {
            if (root instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) root;
                for (int i = 0; i < viewGroup.getChildCount(); i++)
                    applyFont(context, viewGroup.getChildAt(i), fontName);
            } else if (root instanceof TextView)
                ((TextView) root).setTypeface(Typeface.createFromAsset(
                        context.getAssets(), fontName));
        } catch (Exception e) {
            Log.d("FontError", String.format(
                    "Error occured when trying to apply %s font for %s view",
                    fontName, root));
            e.printStackTrace();
        }
    }

    public static String getMD5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest
                    .getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Convert provided days to Milliseconds.
     *
     * @param numberOfDays integer
     * @return Milliseconds long
     */
    public static long toMilliseconds(final int numberOfDays) {

        int CONST_HOUR_COUNT = 24;
        int CONST_MINUTE_COUNT = 60;
        int CONST_SECOND_COUNT = 60;
        long CONST_MILLISECOND_COUNT = 1000L;

        return numberOfDays * CONST_HOUR_COUNT * CONST_MINUTE_COUNT
                * CONST_SECOND_COUNT * CONST_MILLISECOND_COUNT;
    }

    public static File compressFile(String filePath) {

        File myfile = new File(filePath);
        File compressedFile;
        try {
            Bitmap bitmap = BitmapFactory.decodeFile(filePath);

            compressedFile = new File(filePath);
            FileOutputStream out = new FileOutputStream(compressedFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, out);
            out.flush();
            out.close();

        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
            return myfile;
        } catch (IOException e) {
            e.printStackTrace();
            return myfile;
        }
        return compressedFile;

    }

    public static int getActionBarHeight(Activity activity) {
        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (activity.getTheme().resolveAttribute(
                    android.R.attr.actionBarSize, tv, true))
                actionBarHeight = TypedValue.complexToDimensionPixelSize(
                        tv.data, activity.getResources().getDisplayMetrics());
        } else {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,
                    activity.getResources().getDisplayMetrics());
        }

        Log.d("actionBarHeight", actionBarHeight + "");
        return actionBarHeight;
    }

    public static String getHashKey(Context context, String packageName) {
        String hashKey = "";
        try {

            PackageInfo info = context.getPackageManager().getPackageInfo(
                    packageName, PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                hashKey = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.d("KeyHash:",
                        Base64.encodeToString(md.digest(), Base64.DEFAULT));

            }
        } catch (NameNotFoundException e) {
            return hashKey;

        } catch (NoSuchAlgorithmException e) {
            return hashKey;
        }
        return hashKey;
    }

    public static String getMetaData(Context context, String name) {
        try {
            ApplicationInfo appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            if (appInfo.metaData != null) {
                return appInfo.metaData.getString(name);
            }
        } catch (NameNotFoundException e) {
            // if we can’t find it in the manifest, just return null
        }

        return null;
    }

    //get class or activity name
    public static String getActivityName(Context context) {
        return context.getClass().getSimpleName();
    }

    public static String getDateAndTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    @SuppressWarnings("deprecation")
    public static int getDeviceHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        return display.getHeight();
    }

    @SuppressWarnings("deprecation")
    public static int getDeviceWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        return display.getWidth();
    }

    @SuppressWarnings("deprecation")
    public static int getDisplayWidth(Context context) {
        return ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
    }

    public static String getDisplaySize(Context context) {
        return getDisplayWidth(context) + "x" + getDeviceHeight(context);
    }

    public static String getApplicationVersion(Context context) {
        PackageInfo pInfo = null;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return (pInfo != null ? pInfo.versionName : "(unknown)");
    }

    public static String getApplicationName(Context context) {
        final PackageManager pm = context.getPackageManager();
        ApplicationInfo ai;
        try {
            ai = pm.getApplicationInfo(context.getPackageName(), 0);
        } catch (final PackageManager.NameNotFoundException e) {
            ai = null;
        }
        return (String) (ai != null ? pm.getApplicationLabel(ai) : "(unknown)");
    }

    public static Drawable getApplicationIcon(Context context) {
        final PackageManager pm = context.getPackageManager();
        ApplicationInfo ai;
        Drawable appIcon = null;
        try {
            ai = pm.getApplicationInfo(context.getPackageName(), 0);
            appIcon = ai.loadIcon(pm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appIcon;
    }

    public static String getDeviceModel() {
        String brand = android.os.Build.MANUFACTURER;
        String model = android.os.Build.MODEL;
        brand = brand.substring(0, 1).toUpperCase() + brand.substring(1);
        return brand + " " + model;
    }

    public static Uri getRawURIPath(Context context, int rawResorceID) {
        String uriPath = "android.resource://" + context.getPackageName() + "/" + rawResorceID;
        Log.d("uri path: ", uriPath);
        return Uri.parse(uriPath);
    }

    public static int getPixelsFromDp(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static double calculateDistance(Location firstLocation, Location secondLocation) {
        double distance = 0.0;
        try {
            //For first location
            Location firstLoc = new Location("firstLocation");
            firstLoc.setLatitude(firstLocation.getLatitude());
            firstLoc.setLongitude(firstLocation.getLongitude());
            //For second location
            Location secondLoc = new Location("secondLocation");
            secondLoc.setLatitude(secondLocation.getLatitude());
            secondLoc.setLongitude(secondLocation.getLongitude());
            //Calculate distance
            distance = firstLoc.distanceTo(secondLoc);
        } catch (Exception e) {
            return 0.0;
        }
        return distance;
    }

    public static double calculateDistance(LatLng firstLocation, LatLng secondLocation) {
        double distance = 0.0;
        try {
            //For first location
            Location firstLoc = new Location("firstLocation");
            firstLoc.setLatitude(firstLocation.latitude);
            firstLoc.setLongitude(firstLocation.longitude);
            //For second location
            Location secondLoc = new Location("secondLocation");
            secondLoc.setLatitude(secondLocation.latitude);
            secondLoc.setLongitude(secondLocation.longitude);
            //Calculate distance
            distance = firstLoc.distanceTo(secondLoc);
        } catch (Exception e) {
            return 0.0;
        }
        return distance;
    }

    public static double formatDoubleValue(double number,int numberAfterDecimal) {
        String format="##.";
        if(number>0){
            for(int i=0;i<numberAfterDecimal;i++){
                format=format+"#";
            }
        }
        double value = Double.parseDouble(new DecimalFormat(format).format(number));
        return value;
    }

    /**
     * Force show softKeyboard.
     */
    public static void forceShowSoftKeyboard(@NonNull Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    /**
     * Force hide softKeyboard.
     */
    public static void forceHideSoftKeyboard(@NonNull Activity activity, @NonNull EditText editText) {
        if (activity.getCurrentFocus() == null || !(activity.getCurrentFocus() instanceof EditText)) {
            editText.requestFocus();
        }
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

}
