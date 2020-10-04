package readability;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ReadingFile {
    public static String readFileAsString(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(fileName)));
    }
}

public class Main {

    public static String ariAge(double ari){
        String[] ages = new String[15];
        ages[1] = "6";
        ages[2]	= "7";
        ages[3] = "9";
        ages[4] = "10";
        ages[5] = "11";
        ages[6] = "12";
        ages[7] = "13";
        ages[8] = "14";
        ages[9] = "15";
        ages[10] = "16";
        ages[11] = "17";
        ages[12] = "18";
        ages[13] = "24";
        ages[14] = "25";
        return ages[(int) Math.round(ari)];
    }
    public static int syllablesInWord(String text) {
        int count = 0;
        Pattern vowel = Pattern.compile("[aeiouy]", Pattern.CASE_INSENSITIVE);
        Pattern doubleVowel = Pattern.compile("[aeiouy][aeiouy]", Pattern.CASE_INSENSITIVE);
        Pattern endsE = Pattern.compile("e$", Pattern.CASE_INSENSITIVE);
        Matcher matcher;
        matcher = vowel.matcher(text);
        while(matcher.find()) {
      //      System.out.println("Add vowel " + text);
            count++;
        }
        matcher = doubleVowel.matcher(text);
        while(matcher.find()) {
        //    System.out.println("Remove double in " + text);
            count -= 1;
        }
        matcher = endsE.matcher(text);
        if (matcher.find()) {
            count--;
        //    System.out.println("Remove e in " + text);
        }
        if (count == 0) {
       //     System.out.println("Add to no vowels " + text);
            count++;
        }
       // System.out.println("Result "+ text +":" + count);
        if (text.equalsIgnoreCase("you")) {
            return 1;
        }
        return count;
    }
    public static int syllables(String text) {
        int count = 0;
        for (String word : text.replaceAll("[!.?]", "").split("\\s+")) {
            count += syllablesInWord(word);
        }
        return count;
    }
    public static int polySyllables(String text) {
        int count = 0;
        for (String word : text.replaceAll("[!.?]", "").split("\\s+")) {
            if (syllablesInWord(word) > 2) {
                count++;
            }
        }
        return count;
    }

    public static int doAri(int chars, int words, int sents) {
        double ari;
        System.out.printf("Automated Readability Index: %.2f (about %s year olds).\n", ari = 4.71 * chars / words + 0.5 * words / sents - 21.43, ariAge(ari));
        return Integer.parseInt(ariAge(ari));
    }

    public static int doFK(int words, int sents, int sylla) {
        double fk;
        fk = 0.39 * words / sents + 11.8 * sylla / words - 15.59;
        System.out.printf("Flesch–Kincaid readability tests: %.2f (about %s year olds).\n", fk, ariAge(fk));
        return Integer.parseInt(ariAge(fk));
    }

    public static int doSMOG(int pSill, int sents) {
        double smog;
        smog = 1.043 * Math.sqrt(pSill * 30.0 / sents) + 3.1291;
        System.out.printf("Simple Measure of Gobbledygook: %.2f (about %s year olds).\n", smog, ariAge(smog));
        return Integer.parseInt(ariAge(smog));
    }

    public static int doCL(int chars, int words, int sents) {
        double l;
        double s;
        double cl;
        l = chars * 100.0 / words;
        s = sents * 100.0 / words;
        cl = 0.0588 * l - 0.296 * s - 15.8;
        System.out.printf("Coleman–Liau index: %.2f (about %s year olds).",cl , ariAge(cl));
        return Integer.parseInt(ariAge(cl));
    }

    public static void doAll(int chars, int words, int sents, int silla, int pSill) {
        int age = 0;
        age += doAri(chars, words, sents);
        age += doFK(words, sents, silla);
        age += doSMOG(pSill, sents);
        age += doCL(chars, words, sents);
        System.out.println();
        System.out.printf("This text should be understood in average by %.2f year olds.\n", age / 4.0);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String text = null;
        try {
            if (args.length == 0) {
                throw new IOException("Error");
            }
            text = ReadingFile.readFileAsString(args[0]);
        } catch (IOException e) {
            System.out.println("Error. Could not open file");
            System.exit(-1);
        }
        int words;
        int sents;
        int chars;
        int silla;
        int pSill;
        System.out.printf("Words: %d\n", words = text.split("\\s+").length);
        System.out.printf("Sentences: %d\n", sents = text.replaceAll("\\s+","").split("[.!?]").length);
        System.out.printf("Characters: %d\n", chars = text.replaceAll("\\s+","").length());
        System.out.printf("Syllables: %d\n", silla = syllables(text));
        System.out.printf("Polysyllables: %d\n", pSill = polySyllables(text));
        System.out.print("Enter the score you want to calculate (ARI, FK, SMOG, CL, all):");
        String chose = scanner.nextLine();
        switch (chose) {
            case "ARI":
                doAri(chars, words, sents);
                break;
            case "FK":
                doFK(words, sents, silla);
                break;
            case "SMOG":
                doSMOG(pSill, sents);
                break;
            case "CL":
                doCL(chars, words, sents);
                break;
            case "all":
                System.out.println();
                doAll(chars, words, sents, silla, pSill);
                break;
        }
    }
}

