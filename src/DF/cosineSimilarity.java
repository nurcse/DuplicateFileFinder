/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DF;

/**
 *
 * @author nur_uddin
 */
import java.util.Hashtable;
import java.util.LinkedList;

public class cosineSimilarity {
    
    /**this method calculate the cosine similarity score between two file string
     *
     * @param str1 this receives the byte string of a file
     * @param str2 this receives the byte string of another file
     * @return sim_score this is the similarity score for given two file string
     */
    public double cosineSimilarityScore(String str1, String str2) {

        double sim_score = 0.0;
        //1. Identify distinct words from both documents
        String[] wordSeqStr1 = str1.split(" ");
        String[] wordSeqStr2 = str2.split(" ");
        Hashtable<String, Values> wordFreqVector = new Hashtable<String,  Values>();
        LinkedList<String> distinctWordsInStr_1_2 = new LinkedList<String>();

        //prepare word frequency vector by using Text1
        for (int i = 0; i < wordSeqStr1.length; i++) {
            String tempWord = wordSeqStr1[i].trim();
            if (tempWord.length() > 0) {
                if (wordFreqVector.containsKey(tempWord)) {
                    Values val1 = wordFreqVector.get(tempWord);
                    int freq1 = val1.value1 + 1;
                    int freq2 = val1.value2;
                    val1.updateValue(freq1, freq2);
                    wordFreqVector.put(tempWord, val1);
                } else {
                    Values vals1 = new Values(1, 0);
                    wordFreqVector.put(tempWord, vals1);
                    distinctWordsInStr_1_2.add(tempWord);
                }
            }
        }

        //prepare word frequency vector by using Text2
        for (int i = 0; i < wordSeqStr2.length; i++) {
            String tempWord = wordSeqStr2[i].trim();
            if (tempWord.length() > 0) {
                if (wordFreqVector.containsKey(tempWord)) {
                    Values val1 = wordFreqVector.get(tempWord);
                    int freq1 = val1.value1;
                    int freq2 = val1.value2 + 1;
                    val1.updateValue(freq1, freq2);
                    wordFreqVector.put(tempWord, val1);
                } else {
                    Values vals1 = new Values(0, 1);
                    wordFreqVector.put(tempWord, vals1);
                    distinctWordsInStr_1_2.add(tempWord);
                }
            }
        }

        //calculate the cosine similarity score.
        double vectAB = 0.0;
        double vectA_Seq = 0.0;
        double vectB_Seq = 0.0;

        for (int i = 0; i < distinctWordsInStr_1_2.size(); i++) {
            Values vals12 = wordFreqVector.get(distinctWordsInStr_1_2.get(i));

            double freq1 = (double) vals12.value1;
            double freq2 = (double) vals12.value2;

            vectAB = vectAB + (freq1 * freq2);

            vectA_Seq = vectA_Seq + freq1 * freq1;
            vectB_Seq = vectB_Seq + freq2 * freq2;
        }

        sim_score = ((vectAB) / (Math.sqrt(vectA_Seq) * Math.sqrt(vectB_Seq)));

        return (sim_score);
    }
}