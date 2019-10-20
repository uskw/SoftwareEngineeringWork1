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
        int n = -1;//输出ｎ个常用单词
        int num = -1;//输出num个常用短语
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
                    num = Integer.parseInt(args[i+1]);
                    i++;
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
                printWords(stop, f, n, num);
                break;
            case 3:
                printWordsInDir(stop, f, n, num);
                break;
            case 4:
                printWordsInDir_recursive(stop, f, n, num);
                break;
            case 5:
                //printPhrase(num);
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

    public static void printWords(File stop, File f, int n1, int n2){
        if(n2 != -1)
        {
            printPhrase(f, n1, n2);
        }
        else
        {
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
            
            if(n1 == -1)
            {
                System.out.println("输出所有单词：");
            }
            else
            {
                System.out.println("输出前" + n1 + "个最常见的单词：");
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
                            int t = o2.getValue().compareTo(o1.getValue());
                            if(t == 0)
                            {
                                return o1.getKey().compareTo(o2.getKey());
                            }
                            return t;
                        }
                    });
                    int sum = 0,num = 0;
                    for (Map.Entry<String, Integer> map : list) 
                    {
                        if(num == n1 && n1 != -1)
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
    }

    public static void printWordsInDir(File stop, File f, int n1, int n2){
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
                        printWords(stop, next[i], n1, n2);
                    }
                }
            }
        }
    }

    public static void printWordsInDir_recursive(File stop, File f, int n1, int n2){
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
                        printWords(stop, next[i], n1, n2);
                    }
                    else
                    {
                        printWordsInDir_recursive(stop, next[i], n1, n2);
                    }
                }
            }
        }
    }

    public static void printPhrase(File f, int n1, int n){
        if(n == -1)
        {
            System.out.println("输入错误！");
            System.exit(1);
        }
        if(n1 != -1)
        {
            System.out.println("输出" + f.getName() + "中前" + n1 + "个频率最高的" + n + "位短语的出现次数：");
        }
        else
        {
             System.out.println("输出" + f.getName() + "中所有" + n + "位短语的出现次数：");
        }
        if(f.exists())
        {
            try
            {
                int n2 = n;
                Map<String, Integer> phrase = new HashMap<>();
                FileInputStream fls = new FileInputStream(f);
                InputStreamReader isr = new InputStreamReader(fls, "UTF-8");
                BufferedReader br = new BufferedReader(isr);
                while(n2 != 0)
                {
                    int ch, i = 0, sum = 0, flag = 0;
                    String s = "";
                    while((ch = br.read()) != -1)
                    {
                        char c = (char)ch;
                        c = Character.toLowerCase(c);
                        if(c == ' ')
                        {
                            sum++;
                            if(flag == 0)
                            {
                                sum = 0;
                                s = "";
                                continue;
                            }
                            if(sum == n)
                            {
                                if(phrase.containsValue(s))//此短语在数组中已经存在
                                {
                                    phrase.put(s, phrase.get(s)+1);
                                }
                                else
                                {
                                    phrase.put(s, 1);
                                }
                                sum = 0;
                                s = "";
                                flag = 0;
                            }
                            else
                            {
                                s += String.valueOf(c);
                            }
                        }
                        else if(c != ' ' && c < 'a' && c > 'z')
                        {
                            s = "";
                            sum = 0;
                        }
                        else if(c >= 'a' && c <= 'z')
                        {
                            flag = 1;
                            s += String.valueOf(c);
                        }
                    }
                    n2--;
                    br.mark((int)f.length()+1);
                    br.reset();
                    br.skip(1);
                }
                List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(phrase.entrySet());
                Collections.sort(list, new Comparator<Map.Entry<String, Integer>>()
                {
                    public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2)
                    {
                        int t = o2.getValue().compareTo(o1.getValue());
                        if(t == 0)
                        {
                            return o1.getKey().compareTo(o2.getKey());
                        }
                        return t;
                    }
                });
                int num = 0;
                for (Map.Entry<String,Integer> map : list) 
                {
                    if(n1 != -1)
                    {
                        if(num == n1)
                        {
                            break;
                        }
                    }
                    num++;
                    System.out.println(map.getKey() + "：" + map.getValue());
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
            System.out.println("文件不存在！");
            System.exit(1);
        }
    }

    public static void uniformVerbForm(){
        System.out.println("??????????????????????????????");
    }
}