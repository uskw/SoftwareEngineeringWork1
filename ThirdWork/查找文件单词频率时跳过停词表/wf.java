import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.lang.model.util.ElementScanner6;

import javafx.util.Pair;

public class wf {
    public static void main(String[] args) {
        File f = null;
        File stop = null;
        int n = -1;
        int flag = -1;
        for(int i = 0; i<args.length; i++)
        {
            switch (args[i])
            {
                case "-c":
                    f = new File(args[i+1]);
                    flag = 1;
                    i++;
                    break;
                case "-f":
                    flag = 2;
                    f = new File(args[i+1]);
                    i++;
                    break;
                case "-d":
                    flag = 3;
                    f = new File(args[i+1]);
                    break;
                case "-s":
                    flag = 4;
                    f = new File(args[i+1]);
                    i++;
                    break;
                case "-n":
                    n = Integer.parseInt(args[i+1]);
                    i++;
                    break;
                case "-x":
                    stop = new File(args[i+1]);
                    i++;
                    break;
                case "-p":
                    flag = 5;
                    break;
                case "-v":
                    flag = 6;
                    break;
            }
        }
        switch(flag)
        {
            case 1:
                printWordFrequency(f);
                break;
            case 2:
                printWords(stop, f, n);
                break;
            case 3:
                printWordsInDir(stop, f, n);
                break;
            case 4:
                printWordsInDir_recursive(stop, f, n);
                break;
            case 5:
                printPhrase();
                break;
            case 6:
                uniformVerbForm();
                break;
        }
    }

    public static void printWordFrequency(File f){      //传入一个文件
        System.out.println("输出26个字母的频率:");
        int flu[] = new int[26];
        int num = 0;
        for(int i = 0; i<26; i++)
        {
            flu[i] = 0;
        }
        if(f.exists())
        {
            try {
                FileInputStream fls = new FileInputStream(f);           
                InputStreamReader isr = new InputStreamReader(fls, "UTF-8");
                BufferedReader br = new BufferedReader(isr);
                int Ch;
                while((Ch = br.read()) != -1)
                {
                    char c = (char) Ch;
                    c = Character.toLowerCase(c);           //全部切换为小写字母
                    if((c-'a') >= 26 || (c-'a') < 0)
                        continue;
                    else
                    {
                        num++;
                        flu[c-'a']++;
                    }
                }
                br.close();
                ArrayList<Pair<String, Double>> wordFlu = new ArrayList<>();        //使用数组中的键值对存储字母和频率
                for(int i = 0; i<26; i++)
                {
                    char c = (char)(i+'a');
                    String s = String.valueOf(c);
                    Pair<String, Double> pair = new Pair<>(s, Double.valueOf((double)flu[i]/num));
                    wordFlu.add(pair);
                }
                wordFlu.sort(new Comparator() {                         //负责重写sort的匿名内部类
                    @Override
                    public int compare(Object o1, Object o2)        //重写了compare函数
                    {
                        Pair<String, Double> s1 = (Pair)o1;
                        Pair<String, Double> s2 = (Pair)o2;
                        return new Integer(s2.getValue().compareTo(s1.getValue()));
                    }
                });
                System.out.println(f.getName() + "中的字母频率从大到小为：");
                int sum = 0;
                for(int i = 0; i<26; i++)
                {
                    if(sum == 3)
                    {
                        sum = 0;
                        System.out.println();
                    }
                    System.out.format("%s : %.2f   ", wordFlu.get(i).getKey(), wordFlu.get(i).getValue()*100);
                    sum++;
                }
            }
            catch (UnsupportedEncodingException e){
                System.out.println("编码错误！");
                System.exit(1);
            }
            catch (IOException e){
                System.out.println("读写错误！");
                System.exit(1);
            }
        }
        else
        {
            System.out.println("没有找到该文件！");
            System.exit(1);
        }
    }

    public static void printWords(File stop, File f, int n){
        String[] stopWord = new String[50];
        int flag = 0;
        if(stop != null)
        {
            System.out.println("输出" + f.getName() + "中跳过停词表后的常见单词：");
            flag = 1;
            if(stop.exists())
            {
                try
                {
                    FileInputStream fls1 = new FileInputStream(stop);
                    InputStreamReader isr1 = new InputStreamReader(fls1, "UTF-8");
                    BufferedReader br1 = new BufferedReader(isr1);
                    int ch, i = 0;
                    String s = "";
                    while((ch = br1.read()) != -1)
                    {
                        char c = (char)ch;
                        c = Character.toLowerCase(c);
                        if(c == ' ')
                        {
                            stopWord[i++] = s;
                            s = "";
                            continue;
                        }
                        else{
                            s += String.valueOf(c);
                        }
                    }
                }
                catch (UnsupportedEncodingException e){
                    System.out.println("编码错误！");
                    System.exit(1);
                }
                catch (IOException e){
                    System.out.println("读写错误！");
                    System.exit(1);
                }
            }
            else
            {
                System.out.println("没有找到停词表文件！");
                System.exit(1);
            }
        }
        else
        {
            System.out.println("输出" + f.getName() + "中最常见的单词：");
        }
        
        if(n == -1)
        {
            System.out.println("输出所有单词：");
        }
        else
        {
            System.out.println("输出前" + n + "个最常见的单词：");
        }
        if(f.exists())
        {
            try
            {
                FileInputStream fls = new FileInputStream(f);
                InputStreamReader isr = new InputStreamReader(fls, "UTF-8");
                BufferedReader br = new BufferedReader(isr);
                Map<String, Integer> wordNum = new HashMap<>();
                int ch;
                String s = "";
                while((ch = br.read()) != -1)
                {
                    char c = (char)ch;
                    c = Character.toLowerCase(c);
                    if(c < 'a' || c > 'z')
                    {
                        if(s == "")
                            continue;
                        if(!wordNum.containsKey(s))
                        {
                            wordNum.put(s, 1);
                        }
                        else
                        {
                            wordNum.put(s, wordNum.get(s)+1);
                        }
                        s = "";
                    }
                    else{
                        s += String.valueOf(c);
                    }
                }
                List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(wordNum.entrySet());
                Collections.sort(list, new Comparator<Map.Entry<String, Integer>>()
                {
                    public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2)
                    {
                        if(o1.getValue() == o2.getValue())
                        {
                            return -(o1.getKey().compareTo(o2.getKey()));
                        }
                        else
                        {
                            return o2.getValue().compareTo(o1.getValue());
                        }
                    }
                });
                int sum = 0,num = 0;
                for (Map.Entry<String, Integer> map : list) 
                {
                    if(num == n && n != -1)
                        break;
                    if(sum == 3)
                    {
                        sum = 0;
                        System.out.println();
                    }
                    if(flag == 1)
                    {
                        if(Arrays.asList(stopWord).contains(map.getKey()))
                        {
                            continue;
                        }
                        else
                        {
                            System.out.print(map.getKey() + "：" + map.getValue() + "次    ");
                            sum++;
                            num++;
                        }
                    }
                    else
                    {
                        System.out.print(map.getKey() + "：" + map.getValue() + "次    ");
                        sum++;
                        num++;
                    }
                }
                System.out.println();
            }
            catch (UnsupportedEncodingException e){
                System.out.println("编码错误！");
                System.exit(1);
            }
            catch (IOException e){
                System.out.println("读写错误！");
                System.exit(1);
            }
        }
        else
        {
            System.out.println("没有找到该文件！");
            System.exit(1);
        }
    }

    public static void printWordsInDir(File stop, File f, int n){
        System.out.println("输出" + f.getName() + "目录下，每一个文件中最常见的单词：");
        if(f.exists())
        {
            if(f.isDirectory())
            {
                File next[] = f.listFiles();
                for(int i = 0; i<next.length; i++)
                {
                    if(next[i].isFile())
                    {
                        printWords(stop, next[i], n);
                    }
                }
            }
        }
    }

    public static void printWordsInDir_recursive(File stop, File f, int n){
        System.out.println("输出" + f.getName() + "目录下，所有子目录的文件中最常见的单词：");
        if(f.exists())
        {
            if(f.isDirectory())
            {
                File next[] = f.listFiles();
                for(int i = 0; i<next.length; i++)
                {
                    if(next[i].isFile())
                    {
                        printWords(stop, next[i], n);
                    }
                    else
                    {
                        printWordsInDir_recursive(stop, next[i], n);
                    }
                }
            }
        }
    }

    public static void printPhrase(){
        System.out.println("?????????n??????????????????????");
    }

    public static void uniformVerbForm(){
        System.out.println("??????????????????????????????");
    }
}
