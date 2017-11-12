package com.customviews.library.utils;

import android.text.TextUtils;
import android.util.Patterns;
import android.webkit.URLUtil;

import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by rashed on 4/15/16.
 */
public class ValidationManager {

    public static boolean validatePositiveNumber(String positiveNumber) {
        try {
            int textLength = Integer.parseInt(positiveNumber);
            if (textLength < 0) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        final Pattern p = Pattern.compile("^[1-9]\\d*$");
        // Match the given string with the pattern
        final Matcher m = p.matcher(positiveNumber);
        // check whether match is found
        boolean matchFound = m.matches();
        return matchFound;
    }

    //PAIR_KEY = "AD6F2204D5";
    public static boolean validatePairKey(String pairKey){
        int textLength = pairKey.length();
        if (textLength < 10 || textLength > 10) {
            return false;
        }
        final Pattern p = Pattern.compile("^([A-Z0-9]{10})$");
        // Match the given string with the pattern
        final Matcher m = p.matcher(pairKey);
        // check whether match is found
        boolean matchFound = m.matches();
        return matchFound;
    }
    //LICENSE_KEY = "AD6F2204-D591-4F9B-8215-5A38F3DDAAE0";
    public static boolean validateLicenseKey(final String licenseKey) {
        int textLength = licenseKey.length();
        if (textLength < 36 || textLength > 36) {
            return false;
        }
        final Pattern p = Pattern.compile("^([A-Z0-9]{8})-([A-Z0-9]{4})-([A-Z0-9]{4})-([A-Z0-9]{4})-([A-Z0-9]{12})$");
        // Match the given string with the pattern
        final Matcher m = p.matcher(licenseKey);
        // check whether match is found
        boolean matchFound = m.matches();
        return matchFound;
    }

    public static boolean validateURL(String input) {
        if (TextUtils.isEmpty(input)) {
            return false;
        }
        Pattern URL_PATTERN = Patterns.WEB_URL;
        boolean isURL = URL_PATTERN.matcher(input).matches();
        if (!isURL) {
            String urlString = input + "";
            if (URLUtil.isNetworkUrl(urlString)) {
                try {
                    new URL(urlString);
                    isURL = true;
                } catch (Exception e) {
                }
            }
        }
        return isURL;
    }

    public static boolean isIPv4Address(final String input) {
        if (TextUtils.isEmpty(input)) {
            return false;
        }
        final Pattern IPV4_PATTERN =
                Pattern.compile(
                        "^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$");
        return IPV4_PATTERN.matcher(input).matches();
    }

    public static boolean isIPv6StdAddress(final String input) {
        if (TextUtils.isEmpty(input)) {
            return false;
        }
        final Pattern IPV6_STD_PATTERN =
                Pattern.compile(
                        "^(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$");
        return IPV6_STD_PATTERN.matcher(input).matches();
    }

    public static boolean isIPv6HexCompressedAddress(final String input) {
        if (TextUtils.isEmpty(input)) {
            return false;
        }
        final Pattern IPV6_HEX_COMPRESSED_PATTERN =
                Pattern.compile(
                        "^((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)::((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)$");
        return IPV6_HEX_COMPRESSED_PATTERN.matcher(input).matches();
    }

    public static boolean isIPv6Address(final String input) {
        if (TextUtils.isEmpty(input)) {
            return false;
        }
        return isIPv6StdAddress(input) || isIPv6HexCompressedAddress(input);
    }
}
