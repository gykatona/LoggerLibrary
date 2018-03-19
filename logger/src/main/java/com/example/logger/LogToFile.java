package com.example.logger;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LogToFile {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
    private static SimpleDateFormat timestampFormat = new SimpleDateFormat("MM-dd HH:mm:ss", Locale.ENGLISH);
    private static Context baseContext;
    private static String folderMain;

    public static boolean loggingEnabled = true;


    public static void init (Context context, File storage){
        baseContext = context;

        folderMain = getApplicationName(context)+"Logs";
        File f = new File(storage, folderMain);
        if (!f.exists()) {
            f.mkdirs();
        }
    }

    public static void writeToLogFile(String log) {
        if(loggingEnabled ) {

            try {
                String date = dateFormat.format(new Date());
                String timestamp = timestampFormat.format(new Date());

                File logFile = new File(folderMain, getApplicationName(baseContext)+ date + ".txt");
                StringBuilder fileContent = new StringBuilder();

                if (logFile.exists()) {
                    FileReader fr = new FileReader(logFile);
                    BufferedReader br = new BufferedReader(fr);

                    String sCurrentLine;
                    while ((sCurrentLine = br.readLine()) != null) {
                        fileContent.append(sCurrentLine);
                        fileContent.append("\n");
                    }

                    br.close();
                    fr.close();
                }

                FileOutputStream fileOutputStream = new FileOutputStream(logFile);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);

                outputStreamWriter.append(fileContent);
                outputStreamWriter.append("[" + timestamp + "] " + log + "\n");

                outputStreamWriter.close();
            } catch (IOException e) {
                Log.e("LoggerLib", e.getMessage());
            }
        }
    }

    public static String readLogToday() {
        String today = dateFormat.format(new Date());
        File logFileToday = new File(folderMain,getApplicationName(baseContext) + today + ".txt");

        return readLogFile(logFileToday);
    }

    public static String readLogFile(File fileToRead) {
        String res = null;

        if (fileToRead.exists()) {
            try {
                StringBuilder fileContent = new StringBuilder();
                FileReader fileReader = new FileReader(fileToRead);
                BufferedReader bufferedReader = new BufferedReader(fileReader);

                String sCurrentLine;
                while ((sCurrentLine = bufferedReader.readLine()) != null) {
                    fileContent.append(sCurrentLine);
                    fileContent.append("\n");
                }

                res = fileContent.toString();

                bufferedReader.close();
                fileReader.close();
            } catch (IOException e) {
                Log.e("LoggerLib", e.getMessage());
            }
        }

        return res;
    }

    public static String getApplicationName(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : context.getString(stringId);
    }

    public static void openLogfile() throws FileNotFoundException {

    }

    public static void closeLogFile(){

    }

}
