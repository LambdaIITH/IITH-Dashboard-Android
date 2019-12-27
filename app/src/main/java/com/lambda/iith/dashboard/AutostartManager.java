package com.lambda.iith.dashboard;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

public class AutostartManager {
    private String BRAND_ASUS = "asus";
    private String PACKAGE_ASUS_MAIN = "com.asus.mobilemanager";
    private String PACKAGE_ASUS_COMPONENT = "com.asus.mobilemanager.powersaver.PowerSaverSettings";

    /***
     * Honor
     */
    private String BRAND_HONOR = "honor";
    private String PACKAGE_HONOR_MAIN = "com.huawei.systemmanager";
    private String PACKAGE_HONOR_COMPONENT = "com.huawei.systemmanager.optimize.process.ProtectActivity";

    /***
     * Huawei
     */
    private String BRAND_HUAWEI = "huawei";
    private String PACKAGE_HUAWEI_MAIN = "com.huawei.systemmanager";
    private String PACKAGE_HUAWEI_COMPONENT = "com.huawei.systemmanager.optimize.process.ProtectActivity";

    private Context context = null;
    public AutostartManager(Context context1) {
        context = context1;

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if(sharedPreferences.getInt("COUNTAUTOstart" , 0) >6){
            return;
        }else{
            sharedPreferences.edit().putInt("COUNTAUTOstart" , sharedPreferences.getInt("COUNTAUTOstart" , 0)+1).apply();
        }


        if (Build.MANUFACTURER.equalsIgnoreCase("oppo") || Build.MANUFACTURER.equalsIgnoreCase("vivo") || Build.BRAND.equalsIgnoreCase("xiaomi") || Build.BRAND.equalsIgnoreCase("asus") || Build.BRAND.equalsIgnoreCase("honor")) {

            AlertDialog.Builder alert = new AlertDialog.Builder(context);
            alert.setTitle("Please Enable autostart");
            alert.setMessage("Please allow IITH Dashboard to provide uninterrupted notifications, Ignore if already enabled");
            alert.setNegativeButton("Ignore", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {


                    dialog.dismiss();


                    if (Build.MANUFACTURER.equalsIgnoreCase("oppo")) {
                        try {
                            Intent intent = new Intent();
                            intent.setClassName("com.coloros.safecenter",
                                    "com.coloros.safecenter.permission.startup.StartupAppListActivity");
                            context.startActivity(intent);
                        } catch (Exception e) {
                            try {
                                Intent intent = new Intent();
                                intent.setClassName("com.oppo.safe",
                                        "com.oppo.safe.permission.startup.StartupAppListActivity");
                                context.startActivity(intent);

                            } catch (Exception ex) {
                                try {
                                    Intent intent = new Intent();
                                    intent.setClassName("com.coloros.safecenter",
                                            "com.coloros.safecenter.startupapp.StartupAppListActivity");
                                    context.startActivity(intent);
                                } catch (Exception exx) {

                                }
                            }
                        }
                    } else if (Build.MANUFACTURER.equalsIgnoreCase("vivo")) {
                        try {
                            Intent intent = new Intent();
                            intent.setComponent(new ComponentName("com.iqoo.secure",
                                    "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity"));
                            context.startActivity(intent);
                        } catch (Exception e) {
                            try {
                                Intent intent = new Intent();
                                intent.setComponent(new ComponentName("com.vivo.permissionmanager",
                                        "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"));
                                context.startActivity(intent);
                            } catch (Exception ex) {
                                try {
                                    Intent intent = new Intent();
                                    intent.setClassName("com.iqoo.secure",
                                            "com.iqoo.secure.ui.phoneoptimize.BgStartUpManager");
                                    context.startActivity(intent);
                                } catch (Exception exx) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                    } else if (Build.BRAND.equalsIgnoreCase("xiaomi")) {
                        try {
                            Intent intent = new Intent();
                            intent.setComponent(new

                                    ComponentName("com.miui.securitycenter",
                                    "com.miui.permcenter.autostart.AutoStartManagementActivity"));
                            context.startActivity(intent);
                        } catch (Exception e) {

                        }

                    } else if (Build.BRAND.equalsIgnoreCase("asus")) {
                        try {
                            Intent intent = new Intent();
                            intent.setComponent(new ComponentName(PACKAGE_ASUS_MAIN,
                                    PACKAGE_ASUS_COMPONENT));
                            context.startActivity(intent);
                        } catch (Exception e) {

                        }
                    } else if (Build.BRAND.equalsIgnoreCase("honor")) {
                        try {
                            Intent intent = new Intent();
                            intent.setComponent(new ComponentName(PACKAGE_HONOR_MAIN,
                                    PACKAGE_HONOR_COMPONENT));
                            context.startActivity(intent);
                        } catch (Exception e) {

                        }
                    }

                }
            });
            alert.show();


        }
    }


}

