/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DF;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import javax.swing.JButton;

/**
 *
 * @author nur_uddin
 */
public class DFFinder implements Runnable{
    
    final int MAX = 100000;
    long []fileSizes = new long[MAX];
    String filePaths[][] = new String[MAX][3];
    boolean[] dupliflag = new boolean[MAX];
    java.util.List<int[]> dcList = new ArrayList<>();
    int[][] duplicounter = new int[MAX][100];
    int iter = 0;
    
    public View view ;
    String dirName;
    int alChoice;
    Thread t; 
    
    public DFFinder(View v, String dirName, int algoChoice){
        view = v;
        this.dirName = dirName;
        alChoice = algoChoice;
        t = new Thread(this, "fileFindThread");
        t.start();
    }
    
    public void run(){
        int countFile ;
        countFile = listAllFiles(this.dirName);
        System.out.println(countFile);
        
        try{
            showInfo(countFile);
        }catch(Exception e){
            
        }
    }
    
    /*public void findDuplicates(){
        int countFile ;
        countFile = listAllFiles(this.dirName);
        System.out.println(countFile);
        
        try{
            showInfo(countFile);
        }catch(Exception e){
            
        }
    } */
    
    public void cosineSimilaritySearch(int count)throws Exception{
        int col;
        String str1=null, str2=null;
        double similarity_score = 0.0;
        cosineSimilarity cS = new cosineSimilarity();
        
        for(int i=0 ; i<count-1 ; i++){
            System.out.println("inside cosine "+i);
            col = 1;
            for(int j=i+1 ; j<count; j++){
                if(dupliflag[j] == false){
                    //System.out.println("inside cosine "+i);
                    try{
                        File f = new File(filePaths[i][0]);
                        byte[] bytes = Files.readAllBytes(f.toPath());
                        str1 = new String(bytes, "UTF-8");
                        
                        f = new File(filePaths[j][0]);
                        bytes = Files.readAllBytes(f.toPath());
                        str2 = new String(bytes, "UTF-8");
                        System.out.println(view.cosineLimit);
                        
                        if(cS.cosineSimilarityScore(str1, str2)*100 > view.cosineLimit){
                            dupliflag[i] = true;
                            dupliflag[j] = true;
                            
                            if (duplicounter[i][0] == 0)
                            {
                                duplicounter[i][0]++;
                                duplicounter[i][col++] = i;
                            }
                            duplicounter[i][0]++;
                            duplicounter[i][col++] = j;
                            System.out.println(filePaths[i][0]+" and "+filePaths[j][0]+ " is similar to : "+cS.cosineSimilarityScore(str1, str2) +" percent");
                        }
                        
                    }catch(Exception e){
                        
                    }
                }
            }
            if (duplicounter[i][0] >= 1)
            {
                //making a ArrayList of the duplicate files' paths.
                dcList.add(duplicounter[i]);
            }
        }
    }
    
    public void duplicateSearchCRC32(int count)throws Exception{
        int col;

        for (int i = 0; i < count; i++)
        {
            //shows the current progress.
            view.progressStatus.setText("Files Left to Compare: " + (count - i)
                    + "\n" + "Searching Duplicate for : " + filePaths[i][1]);
            col = 1;
            for (int j = i + 1; j < count; j++)
            {
                if (fileSizes[i] == fileSizes[j] && dupliflag[j] == false)
                {
                    //shows the current progress.
                    view.progressStatus.setText("Files Left to Compare : " + (count - i) + "\n"
                            + "\n" + "Comparing CRC32CheckSUM for the Followings: " + "\n" + filePaths[i][0]
                            + "     with" + " " + filePaths[j][0]);
                    //checks the CRC32 Hash values.
                    if (CRC32.doChecksum(filePaths[i][0]) == CRC32.doChecksum(filePaths[j][0]))
                    {
                        dupliflag[i] = true;
                        dupliflag[j] = true;

                        if (duplicounter[i][0] == 0)
                        {
                            duplicounter[i][0]++;
                            duplicounter[i][col++] = i;
                        }
                        duplicounter[i][0]++;
                        duplicounter[i][col++] = j;
                    }
                }
            }
            if (duplicounter[i][0] >= 1)
            {
                //making a ArrayList of the duplicate files' paths.
                dcList.add(duplicounter[i]);
            }
        }
    }
    
    public void duplicateSearchMD5(int count) throws Exception{
        int col;

        for (int i = 0; i < count; i++)
        {
            //shows the current progress.
            view.progressStatus.setText("Files left to Compare: " + (count - i)
                    + "\n" + "Searching Duplicate for : " + filePaths[i][1]);

            col = 1;
            for (int j = i + 1; j < count; j++)
            {
                if (fileSizes[i] == fileSizes[j] && dupliflag[j] == false)
                {
                    //shows the current progress.
                    view.progressStatus.setText("Files Left to Compare : " + (count - i)
                            + "\n" + "Comparing MD5CheckSUM for the Followings: " + "\n" + filePaths[i][0]
                            + "     with" + " " + filePaths[j][0]);
                    //checks the MD5 Hash values.
                    if (MD5.hashing(filePaths[i][0]).equals(MD5.hashing(filePaths[j][0])))
                    {
                        dupliflag[i] = true;
                        dupliflag[j] = true;

                        if (duplicounter[i][0] == 0)
                        {
                            duplicounter[i][0]++;
                            duplicounter[i][col++] = i;
                        }
                        duplicounter[i][0]++;
                        duplicounter[i][col++] = j;
                    }
                }
            }
            if (duplicounter[i][0] >= 1)
            {
                //making a ArrayList of the duplicate files' paths.
                dcList.add(duplicounter[i]);
            }
        }
    }
    
    public void duplicateSearchFileSize (int count)throws Exception{
        int col;

        for (int i = 0; i < count; i++)
        {

            col = 1;
            for (int j = i + 1; j < count; j++)
            {
                if (fileSizes[i] == fileSizes[j] && dupliflag[j] == false)
                {
                    {
                        dupliflag[i] = true;
                        dupliflag[j] = true;

                        if (duplicounter[i][0] == 0)
                        {
                            duplicounter[i][0]++;
                            duplicounter[i][col++] = i;
                        }
                        view.progressStatus.setText("Searching Duplicate For : " + filePaths[i][1]);
                        duplicounter[i][0]++;
                        duplicounter[i][col++] = j;
                    }
                }
            }
            if (duplicounter[i][0] >= 1)
            {
                dcList.add(duplicounter[i]);
            }
        }
    }
    
    public void showInfo(int count)throws Exception{
        int num, n, incident, totalDuplicateFiles, duplicatePathNum;
        
        String duplicateFilePath, duplicateFileExtension;
        String output = "";
        long dFileSize;
        if(count ==0){
            view.progressStatus.setText("Empty Directory "+"\n");
            view.duplicateShow.setText("NO FILES HERE TO COMPARE ...");
        }
        else{
            if(alChoice==1){
                cosineSimilaritySearch(count);
            }
            else if(alChoice==2){
                duplicateSearchMD5(count);
            }
            else if(alChoice==3){
                duplicateSearchCRC32(count);
            }
            else if(alChoice==4){
                duplicateSearchFileSize (count);
            }
            incident = dcList.size();
            totalDuplicateFiles = 0;
            view.progressStatus.setText("Generating Duplicate File Lists ...");
            
            output="";
            view.duplicateShow.setText(output);
            
            for(int k=0 ; k<incident ; k++){
                num = dcList.get(k)[0];
                totalDuplicateFiles += (num-1);
                if(num >= 1){
                    if(alChoice==1){
                        output = "\nFollwing " + num + " Files are similar to more than "+ view.cosineLimit +" Percent"+ "\n";
                    }
                    else
                        output = "\nFollwing " + num + " Files are equal :" + "\n";
                    view.duplicateShow.append(output);
                    
                    for(n=1 ; n<=num ; n++){
                        duplicatePathNum = dcList.get(k)[n];
                        
                        duplicateFilePath = filePaths[duplicatePathNum][0];
                        duplicateFileExtension = filePaths[duplicatePathNum][2];
                        dFileSize = fileSizes[duplicatePathNum];
                        
                        output = duplicateFilePath +  " file type : ." + duplicateFileExtension + "  file size: " 
                                + dFileSize + "bytes " + "  " + "\n";
                        
                        view.duplicateShow.append(output);
                        
                        //view.duplicateShow.add(output);
                        
                    }
                }
            }
            if(totalDuplicateFiles == 0){
                view.progressStatus.setText("NO Duplicate Files found" + "\n");
                
            }
            else{
                view.progressStatus.setText("Total " + totalDuplicateFiles + " Duplicates  of " + incident
                        + " files FOUND !" + "\n");
            }
            
        }
    }
    
    public int listAllFiles(String dirName){
        
        String filePath, fileName, ext;
        
        File directory = new File(dirName);
        File[] fileList = directory.listFiles();
        
        for(File file : fileList){
            
            if(file.isFile()){
                filePath = file.getAbsolutePath();
                fileName = file.getName();
                
                filePaths[iter][0] = filePath;
                filePaths[iter][1] = fileName;
                ext = "";
                
                int i = filePath.lastIndexOf('.');
                int p = Math.max(filePath.lastIndexOf('/'), filePath.lastIndexOf('\\'));
                if(i>p){
                    ext = filePath.substring(i+1);
                }
                filePaths[iter][2] = ext;
                fileSizes[iter] = file.length();
                System.out.println(filePath);
                iter++;
                view.progressStatus.setText("currently scanning : "+filePath+"\n"
                        +"Scanned : "+iter+" files");
            }
            else if(file.isDirectory()){
                listAllFiles(file.getAbsolutePath());
            }
        }
        
        return iter;
    }
}
