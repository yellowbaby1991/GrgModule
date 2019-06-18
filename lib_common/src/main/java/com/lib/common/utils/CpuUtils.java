package com.lib.common.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.util.Enumeration;

public class CpuUtils {

    public static int getCpuUsageRate() {
        try {
            String Result;
            Process p = Runtime.getRuntime().exec("top -n 1");
            BufferedReader br = new BufferedReader(new InputStreamReader
                    (p.getInputStream()));
            while ((Result = br.readLine()) != null) {
                if (Result.trim().length() < 1) {
                    continue;
                } else {
                    StringBuffer tv = new StringBuffer();
                    String[] CPUusr = Result.split("%");
                    tv.append("USER:" + CPUusr[0] + "\n");
                    String[] CPUusage = CPUusr[0].split("User");
                    String[] SYSusage = CPUusr[1].split("System");
                    tv.append("CPU:" + CPUusage[1].trim() + " length:" + CPUusage[1].trim().length() + "\n");
                    tv.append("SYS:" + SYSusage[1].trim() + " length:" + SYSusage[1].trim().length() + "\n");
                    tv.append(Result + "\n");
                    return Integer.valueOf(CPUusage[1].trim()) + Integer.valueOf(SYSusage[1].trim());
                }
            }
        } catch (Exception e) {
            return -1;
        }
        return -1;
    }

    public static int getSDTotalSize() {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return FormetFileSize(blockSize * totalBlocks);
    }

    public static int getSDAvailableSize() {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return FormetFileSize(blockSize * availableBlocks);
    }


    public static int getAvailMemory(Context context) {// 获取android当前可用内存大小
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        return FormetFileSize(mi.availMem);// 将获取的内存大小规格化
    }


    public static int getTotalMemory() {
        String str1 = "/proc/meminfo";// 系统内存信息文件
        String str2;
        String[] arrayOfString;
        long initial_memory = 0;

        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(
                    localFileReader, 8192);
            str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小

            arrayOfString = str2.split("\\s+");
            for (String num : arrayOfString) {
                Log.i(str2, num + "\t");
            }

            initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte
            localBufferedReader.close();

        } catch (IOException e) {
        }
        return FormetFileSize(initial_memory);// Byte转换为KB或者MB，内存大小规格化
    }

    private static int FormetFileSize(long fileS) {//单位是M
        DecimalFormat df = new DecimalFormat("#");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return 0;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "";
        } else {
            fileSizeString = df.format((double) fileS / 1048576) + "";
            //fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return Integer.valueOf(fileSizeString);
    }


    public static int getMemoryUsageRate(Context context) {
        double total = getTotalMemory();
        double avail = getAvailMemory(context);
        double used = total - avail;
        double rate = used / total;
        return (int) (rate * 100);
    }

    public static int getUsageMemory(Context context) {
        double total = getTotalMemory();
        double avail = getAvailMemory(context);
        return (int) (total - avail);
    }

    public static int getSDUsageSize() {
        double total = getSDTotalSize();
        double avail = getSDAvailableSize();
        return (int) (total - avail);
    }

    public static int getSDUsageRate() {
        double total = getSDTotalSize();
        double used = getSDUsageSize();
        double rate = used / total;
        return (int) (rate * 100);
    }

    public static String getLocalMacAddressFromIp() {

        String strMacAddr = null;

        try {

            //获得IpD地址

            InetAddress ip = getLocalInetAddress();

            byte[] b = NetworkInterface.getByInetAddress(ip).getHardwareAddress();

            StringBuffer buffer = new StringBuffer();

            for (int i = 0; i < b.length; i++) {

                if (i != 0) {

                    buffer.append(':');

                }

                String str = Integer.toHexString(b[i] & 0xFF);

                buffer.append(str.length() == 1 ? 0 + str : str);

            }

            strMacAddr = buffer.toString().toUpperCase();

        } catch (Exception e) {


        }


        return strMacAddr;

    }

    private static InetAddress getLocalInetAddress() {

        InetAddress ip = null;

        try {

            //列举

            Enumeration<NetworkInterface> en_netInterface = NetworkInterface.getNetworkInterfaces();

            while (en_netInterface.hasMoreElements()) {//是否还有元素

                NetworkInterface ni = (NetworkInterface) en_netInterface.nextElement();//得到下一个元素

                Enumeration<InetAddress> en_ip = ni.getInetAddresses();//得到一个ip地址的列举

                while (en_ip.hasMoreElements()) {

                    ip = en_ip.nextElement();

                    if (!ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1)

                        break;

                    else

                        ip = null;

                }


                if (ip != null) {

                    break;

                }

            }

        } catch (SocketException e) {


            e.printStackTrace();

        }

        return ip;

    }

}
