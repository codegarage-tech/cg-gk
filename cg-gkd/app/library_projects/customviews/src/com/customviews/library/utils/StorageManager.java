package com.customviews.library.utils;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URI;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class StorageManager {

    private ArrayList<File> fileList = new ArrayList<File>();

    public static StorageManager getInstance() {
        return new StorageManager();
    }

    private StorageManager() {
        fileList.clear();
    }

    // pass fileType as a music , video, etc.
    public ArrayList<File> getAllFilesFromExternalSdCard(File dir, FileType fileType) {

        File listFile[] = dir.listFiles();
        if (listFile != null && listFile.length > 0) {
            for (int i = 0; i < listFile.length; i++) {
                if (listFile[i].isDirectory()) {
                    getAllFilesFromExternalSdCard(listFile[i], fileType);
                } else {
                    switch (fileType) {
                        case DOCUMENT:
                            if (listFile[i].getName().endsWith(".pdf") || listFile[i].getName().endsWith(".txt")
                                    || listFile[i].getName().endsWith(".xml") || listFile[i].getName().endsWith(".doc")
                                    || listFile[i].getName().endsWith(".xls") || listFile[i].getName().endsWith(".xlsx")
                                    || listFile[i].getName().endsWith(".java") || listFile[i].getName().endsWith(".html")
                                    || listFile[i].getName().endsWith(".htm") || listFile[i].getName().endsWith(".css")
                                    || listFile[i].getName().endsWith(".cpp") || listFile[i].getName().endsWith(".cs")
                                    || listFile[i].getName().endsWith(".php") || listFile[i].getName().endsWith(".docx")
                                    || listFile[i].getName().endsWith(".ods") || listFile[i].getName().endsWith(".csv")
                                    || listFile[i].getName().endsWith(".db") || listFile[i].getName().endsWith(".srt")
                                    || listFile[i].getName().endsWith(".js") || listFile[i].getName().endsWith(".php")) {
                                fileList.add(listFile[i]);
                            }
                            break;
                        case MUSIC:
                            if (listFile[i].getName().endsWith(".mp3") || listFile[i].getName().endsWith(".aac")
                                    || listFile[i].getName().endsWith(".amr") || listFile[i].getName().endsWith(".m4r")) {
                                fileList.add(listFile[i]);
                            }
                            break;
                        case VIDEO:
                            if (listFile[i].getName().endsWith(".mp4") || listFile[i].getName().endsWith(".flv")
                                    || listFile[i].getName().endsWith(".avi") || listFile[i].getName().endsWith(".mkv")
                                    || listFile[i].getName().endsWith(".wmv") || listFile[i].getName().endsWith(".webm")
                                    || listFile[i].getName().endsWith(".3gp") || listFile[i].getName().endsWith(".dat")
                                    || listFile[i].getName().endsWith(".swf") || listFile[i].getName().endsWith(".mov")
                                    || listFile[i].getName().endsWith(".vob") || listFile[i].getName().endsWith(".mpg")
                                    || listFile[i].getName().endsWith(".mpeg") || listFile[i].getName().endsWith(".mpeg1")
                                    || listFile[i].getName().endsWith(".mpeg2") || listFile[i].getName().endsWith(".mpeg3")
                                    || listFile[i].getName().endsWith(".mpeg3") || listFile[i].getName().endsWith(".m4v")) {
                                fileList.add(listFile[i]);
                            }
                            break;
                        case IMAGE:
                            if (listFile[i].getName().endsWith(".png") || listFile[i].getName().endsWith(".jpg")
                                    || listFile[i].getName().endsWith(".jpeg") || listFile[i].getName().endsWith(".gif")) {
                                fileList.add(listFile[i]);
                            }
                            break;
                    }
                }
            }
        }
        return fileList;
    }

    public boolean isDesiredFileType(File file, FileType fileType) {
        switch (fileType) {
            case DOCUMENT:
                if (file.getName().endsWith(".pdf") || file.getName().endsWith(".txt")
                        || file.getName().endsWith(".xml") || file.getName().endsWith(".doc")
                        || file.getName().endsWith(".xls") || file.getName().endsWith(".xlsx")
                        || file.getName().endsWith(".java") || file.getName().endsWith(".html")
                        || file.getName().endsWith(".htm") || file.getName().endsWith(".css")
                        || file.getName().endsWith(".cpp") || file.getName().endsWith(".cs")
                        || file.getName().endsWith(".php") || file.getName().endsWith(".docx")
                        || file.getName().endsWith(".ods") || file.getName().endsWith(".csv")
                        || file.getName().endsWith(".db") || file.getName().endsWith(".srt")
                        || file.getName().endsWith(".js") || file.getName().endsWith(".php")) {
                    return true;
                }
                break;
            case MUSIC:
                if (file.getName().endsWith(".mp3") || file.getName().endsWith(".aac")
                        || file.getName().endsWith(".amr") || file.getName().endsWith(".m4r")) {
                    return true;
                }
                break;
            case VIDEO:
                if (file.getName().endsWith(".mp4") || file.getName().endsWith(".flv")
                        || file.getName().endsWith(".avi") || file.getName().endsWith(".mkv")
                        || file.getName().endsWith(".wmv") || file.getName().endsWith(".webm")
                        || file.getName().endsWith(".3gp") || file.getName().endsWith(".dat")
                        || file.getName().endsWith(".swf") || file.getName().endsWith(".mov")
                        || file.getName().endsWith(".vob") || file.getName().endsWith(".mpg")
                        || file.getName().endsWith(".mpeg") || file.getName().endsWith(".mpeg1")
                        || file.getName().endsWith(".mpeg2") || file.getName().endsWith(".mpeg3")
                        || file.getName().endsWith(".mpeg3") || file.getName().endsWith(".m4v")) {
                    return true;
                }
                break;
            case IMAGE:
                if (file.getName().endsWith(".png") || file.getName().endsWith(".jpg")
                        || file.getName().endsWith(".jpeg") || file.getName().endsWith(".gif")) {
                    return true;
                }
                break;
        }
        return false;
    }

    //get all sd card folder name
    public ArrayList<File> getAllFolderName(File dir) {
        ArrayList<File> allFolder = new ArrayList<File>();
        File listFile[] = dir.listFiles();
        if (listFile != null && listFile.length > 0) {
            int index = 1;
            for (int i = 0; i < listFile.length; i++) {
                if (listFile[i].isDirectory()) {
//                    Log.d("StorageManagerBJIT", index + " no file is: " + listFile[i].getName());
                    allFolder.add(listFile[i]);
                    index++;
                } else {
                    Log.d("StorageManagerBJIT", "Not directory: " + listFile[i].getName());
                }
            }
            Log.d("StorageManagerBJIT", "Total folders: " + allFolder.size());
        }
        return allFolder;
    }

    public ArrayList<File> getAllFolderLessFile(File dir) {
        ArrayList<File> allFolder = new ArrayList<File>();
        File listFile[] = dir.listFiles();
        if (listFile != null && listFile.length > 0) {
            int index = 1;
            for (int i = 0; i < listFile.length; i++) {
                if (listFile[i].isDirectory()) {
//                    Log.d("StorageManagerBJIT", index + " no file is: " + listFile[i].getName());
                } else {
                    allFolder.add(listFile[i]);
                    index++;
                    Log.d("StorageManagerBJIT", "Not directory: " + listFile[i].getName());
                }
            }
            Log.d("StorageManagerBJIT", "Total folders: " + allFolder.size());
        }
        return allFolder;
    }

    // pass fileType as a music , video, etc.
    public HashMap<File, ArrayList<File>> getHashedGroupedFilesFromSdCard(File dir, FileType fileType) {
        HashMap<File, ArrayList<File>> folderWiseFiles = new HashMap<File, ArrayList<File>>();
        ArrayList<File> allFolders = getAllFolderName(Environment.getExternalStorageDirectory());
        if (allFolders != null && allFolders.size() > 0) {
            for (int i = 0; i < allFolders.size(); i++) {
                folderWiseFiles.put(allFolders.get(i), getAllFilesFromExternalSdCard(allFolders.get(i), fileType));
//                Log.d("StorageManagerBJIT", "Folder: " + allFolders.get(i).getName());
            }
        }
        return folderWiseFiles;
    }

    public ArrayList<Parent> getListedGroupedFilesFromSdCard(File dir, FileType fileType) {
        ArrayList<Parent> folderWiseFiles = new ArrayList<Parent>();
        ArrayList<File> allFolders = getAllFolderName(Environment.getExternalStorageDirectory());
//        Log.d("StorageManagerBJIT", "Root path: " + Environment.getExternalStorageDirectory());
        if (allFolders != null && allFolders.size() > 0) {
            for (int i = 0; i < allFolders.size(); i++) {
                File mFile = allFolders.get(i);
//                ArrayList<File> specificFolderFile=getInstance().getAllFilesFromExternalSdCard(mFile,FileType.IMAGE);
//                Parent parent=new Parent(mFile,specificFolderFile);
//                Log.d("StorageManagerBJIT", "Folder: " + mFile.getAbsolutePath());
                folderWiseFiles.add(new Parent(mFile, getInstance().getAllFilesFromExternalSdCard(mFile, fileType)));
            }

            folderWiseFiles.add(new Parent(dir, getListedGroupedFilesWithoutFolderFromSdCard(fileType)));
        }
        return folderWiseFiles;
    }

    public ArrayList<File> getListedGroupedFilesWithoutFolderFromSdCard(FileType fileType) {
        ArrayList<File> allFolderlessFile = getAllFolderLessFile(Environment.getExternalStorageDirectory());
        ArrayList<File> folderlessChilds = new ArrayList<File>();
//        Log.d("StorageManagerBJIT", "Root path: " + Environment.getExternalStorageDirectory());
        if (allFolderlessFile != null && allFolderlessFile.size() > 0) {
            for (int i = 0; i < allFolderlessFile.size(); i++) {
                File mFile = allFolderlessFile.get(i);
                if (isDesiredFileType(mFile, fileType)) {
                    folderlessChilds.add(mFile);
                }
//                ArrayList<File> specificFolderFile=getInstance().getAllFilesFromExternalSdCard(mFile,FileType.IMAGE);
//                Parent parent=new Parent(mFile,specificFolderFile);
//                Log.d("StorageManagerBJIT", "Folder: " + mFile.getAbsolutePath());
            }
//            folderWiseFiles.add(new Parent(new File(Environment.getExternalStorageState()), getInstance().getAllFolderlessFile(new File(Environment.getExternalStorageState()), fileType)));
        }
        return folderlessChilds;
    }

    public enum FileType {
        DOCUMENT, MUSIC, VIDEO, IMAGE
    }

    public static class Parent {

        private Children parent;
        private ArrayList<Children> children;
        private boolean isSelected;

        public Parent(File title, ArrayList<File> childs) {
            parent = new Children(title, false);
            children = new ArrayList<Children>();
            for (int i = 0; i < childs.size(); i++) {
                children.add(new Children(childs.get(i), false));
            }
            isSelected = false;
        }

        public Parent(Children title, ArrayList<Children> childs) {
            parent = title;
            children = childs;
            isSelected = false;
        }

        public Parent(Children title, boolean isSelected, ArrayList<Children> childs) {
            parent = title;
            children = childs;
            isSelected = isSelected;
        }

        public Parent(File title, boolean isSelected, ArrayList<File> child) {
            parent = new Children(title, false);
            children = new ArrayList<Children>();
            for (int i = 0; i < child.size(); i++) {
                children.add(new Children(child.get(i), false));
            }
            this.isSelected = isSelected;
        }

        private Parent() {
        }

        public Children getParent() {
            return parent;
        }

        public void setParent(File parent) {
            this.parent = new Children(parent, false);
        }

        public ArrayList<Children> getChildren() {
            return children;
        }

        public void setChildren(ArrayList<File> childList) {
            children.clear();
            for (int i = 0; i < childList.size(); i++) {
                children.add(new Children(childList.get(i), false));
            }
        }


        public boolean isSelected() {
            for (int i = 0; i < getChildren().size(); i++) {
                if (getChildren().get(i).isSelected()) {
                    return true;
                }
            }

            return false;
        }

        public void setIsSelected(boolean isSelected) {
            this.isSelected = isSelected;
        }
    }

    public static class Children {

        private boolean isSelected;
        private File data;

        public Children(File data, boolean isSelected) {
            this.isSelected = isSelected;
            this.data = data;
        }

        private Children() {
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setIsSelected(boolean isSelected) {
            this.isSelected = isSelected;
        }

        public File getData() {
            return data;
        }

        public void setData(File data) {
            this.data = data;
        }
    }

    public static String readableFileSize(long size) {
        if (size <= 0) return "0";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB", "PB", "EB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.00").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    public static String readFileFromSDcard(String folderPathInSDcard, String fileName) {

        String filePath = "";
        String txt = "";
        try {
            if (!folderPathInSDcard.substring(folderPathInSDcard.length() - 1).equalsIgnoreCase("/")) {
                filePath = folderPathInSDcard + File.separator + fileName;
            } else {
                filePath = folderPathInSDcard + fileName;
            }
            //Get the text file
            File file = new File(filePath);

            // check if file exist
            if (file.exists()) {
                FileInputStream fIn = new FileInputStream(file);
                BufferedReader myReader = new BufferedReader(
                        new InputStreamReader(fIn));
                String aDataRow = "";
                while ((aDataRow = myReader.readLine()) != null) {
                    txt += aDataRow + "\n";
                }
                myReader.close();
                return txt;
            }
        } catch (Exception ex) {
            txt = "";
            return txt;
        }
        return txt;
    }

    public static boolean writeFileIntoSDcard(String folderPathInSDcard, String fileName, String fileContent) {

        String filePath = "";
        String txt = "";
        boolean isSuccess = false;
        try {
            if (!folderPathInSDcard.substring(folderPathInSDcard.length() - 1).equalsIgnoreCase("/")) {
                filePath = folderPathInSDcard + File.separator + fileName + ".txt";
                Log.d("filepath: ", filePath);
            } else {
                filePath = folderPathInSDcard + fileName + ".txt";
                Log.d("filepath: ", filePath);
            }
            //Get the text file
            File file = new File(filePath);

            // check if file exist
//            if (!file.exists()) {
            file.createNewFile();

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(fileContent);
            bw.close();

            Log.d("Suceess", "Sucess");
            isSuccess = true;
            return isSuccess;
//            }
        } catch (Exception ex) {
            isSuccess = false;
            return isSuccess;
        }
//        return isSuccess;
    }

    public static boolean createFolderIntoExternalSdCard(String folderName) {
        boolean isFolderCreated = false;
        File f = new File(Environment.getExternalStorageDirectory(), folderName);
        if (!f.exists()) {
            isFolderCreated = f.mkdirs();
        }
        return isFolderCreated;
    }

}
